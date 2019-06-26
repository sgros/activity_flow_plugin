package com.google.android.exoplayer2.extractor.mkv;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.NalUnitUtil;
import java.io.IOException;
import java.util.ArrayDeque;

final class DefaultEbmlReader implements EbmlReader {
    private long elementContentSize;
    private int elementId;
    private int elementState;
    private final ArrayDeque<MasterElement> masterElementsStack = new ArrayDeque();
    private EbmlReaderOutput output;
    private final byte[] scratch = new byte[8];
    private final VarintReader varintReader = new VarintReader();

    private static final class MasterElement {
        private final long elementEndPosition;
        private final int elementId;

        private MasterElement(int i, long j) {
            this.elementId = i;
            this.elementEndPosition = j;
        }
    }

    public void init(EbmlReaderOutput ebmlReaderOutput) {
        this.output = ebmlReaderOutput;
    }

    public void reset() {
        this.elementState = 0;
        this.masterElementsStack.clear();
        this.varintReader.reset();
    }

    public boolean read(ExtractorInput extractorInput) throws IOException, InterruptedException {
        Assertions.checkState(this.output != null);
        while (true) {
            if (this.masterElementsStack.isEmpty() || extractorInput.getPosition() < ((MasterElement) this.masterElementsStack.peek()).elementEndPosition) {
                if (this.elementState == 0) {
                    long readUnsignedVarint = this.varintReader.readUnsignedVarint(extractorInput, true, false, 4);
                    if (readUnsignedVarint == -2) {
                        readUnsignedVarint = maybeResyncToNextLevel1Element(extractorInput);
                    }
                    if (readUnsignedVarint == -1) {
                        return false;
                    }
                    this.elementId = (int) readUnsignedVarint;
                    this.elementState = 1;
                }
                if (this.elementState == 1) {
                    this.elementContentSize = this.varintReader.readUnsignedVarint(extractorInput, false, true, 8);
                    this.elementState = 2;
                }
                int elementType = this.output.getElementType(this.elementId);
                long j;
                StringBuilder stringBuilder;
                if (elementType == 0) {
                    extractorInput.skipFully((int) this.elementContentSize);
                    this.elementState = 0;
                } else if (elementType == 1) {
                    long position = extractorInput.getPosition();
                    this.masterElementsStack.push(new MasterElement(this.elementId, this.elementContentSize + position));
                    this.output.startMasterElement(this.elementId, position, this.elementContentSize);
                    this.elementState = 0;
                    return true;
                } else if (elementType == 2) {
                    j = this.elementContentSize;
                    if (j <= 8) {
                        this.output.integerElement(this.elementId, readInteger(extractorInput, (int) j));
                        this.elementState = 0;
                        return true;
                    }
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Invalid integer size: ");
                    stringBuilder.append(this.elementContentSize);
                    throw new ParserException(stringBuilder.toString());
                } else if (elementType == 3) {
                    j = this.elementContentSize;
                    if (j <= 2147483647L) {
                        this.output.stringElement(this.elementId, readString(extractorInput, (int) j));
                        this.elementState = 0;
                        return true;
                    }
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("String element size: ");
                    stringBuilder.append(this.elementContentSize);
                    throw new ParserException(stringBuilder.toString());
                } else if (elementType == 4) {
                    this.output.binaryElement(this.elementId, (int) this.elementContentSize, extractorInput);
                    this.elementState = 0;
                    return true;
                } else if (elementType == 5) {
                    j = this.elementContentSize;
                    if (j == 4 || j == 8) {
                        this.output.floatElement(this.elementId, readFloat(extractorInput, (int) this.elementContentSize));
                        this.elementState = 0;
                        return true;
                    }
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Invalid float size: ");
                    stringBuilder.append(this.elementContentSize);
                    throw new ParserException(stringBuilder.toString());
                } else {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Invalid element type ");
                    stringBuilder2.append(elementType);
                    throw new ParserException(stringBuilder2.toString());
                }
            }
            this.output.endMasterElement(((MasterElement) this.masterElementsStack.pop()).elementId);
            return true;
        }
    }

    private long maybeResyncToNextLevel1Element(ExtractorInput extractorInput) throws IOException, InterruptedException {
        extractorInput.resetPeekPosition();
        while (true) {
            extractorInput.peekFully(this.scratch, 0, 4);
            int parseUnsignedVarintLength = VarintReader.parseUnsignedVarintLength(this.scratch[0]);
            if (parseUnsignedVarintLength != -1 && parseUnsignedVarintLength <= 4) {
                int assembleVarint = (int) VarintReader.assembleVarint(this.scratch, parseUnsignedVarintLength, false);
                if (this.output.isLevel1Element(assembleVarint)) {
                    extractorInput.skipFully(parseUnsignedVarintLength);
                    return (long) assembleVarint;
                }
            }
            extractorInput.skipFully(1);
        }
    }

    private long readInteger(ExtractorInput extractorInput, int i) throws IOException, InterruptedException {
        int i2 = 0;
        extractorInput.readFully(this.scratch, 0, i);
        long j = 0;
        while (i2 < i) {
            j = (j << 8) | ((long) (this.scratch[i2] & NalUnitUtil.EXTENDED_SAR));
            i2++;
        }
        return j;
    }

    private double readFloat(ExtractorInput extractorInput, int i) throws IOException, InterruptedException {
        long readInteger = readInteger(extractorInput, i);
        if (i == 4) {
            return (double) Float.intBitsToFloat((int) readInteger);
        }
        return Double.longBitsToDouble(readInteger);
    }

    private String readString(ExtractorInput extractorInput, int i) throws IOException, InterruptedException {
        if (i == 0) {
            return "";
        }
        byte[] bArr = new byte[i];
        extractorInput.readFully(bArr, 0, i);
        while (i > 0 && bArr[i - 1] == (byte) 0) {
            i--;
        }
        return new String(bArr, 0, i);
    }
}
