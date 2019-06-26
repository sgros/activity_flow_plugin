// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.mkv;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import java.util.ArrayDeque;

final class DefaultEbmlReader implements EbmlReader
{
    private long elementContentSize;
    private int elementId;
    private int elementState;
    private final ArrayDeque<MasterElement> masterElementsStack;
    private EbmlReaderOutput output;
    private final byte[] scratch;
    private final VarintReader varintReader;
    
    public DefaultEbmlReader() {
        this.scratch = new byte[8];
        this.masterElementsStack = new ArrayDeque<MasterElement>();
        this.varintReader = new VarintReader();
    }
    
    private long maybeResyncToNextLevel1Element(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        extractorInput.resetPeekPosition();
        int unsignedVarintLength;
        int n;
        while (true) {
            extractorInput.peekFully(this.scratch, 0, 4);
            unsignedVarintLength = VarintReader.parseUnsignedVarintLength(this.scratch[0]);
            if (unsignedVarintLength != -1 && unsignedVarintLength <= 4) {
                n = (int)VarintReader.assembleVarint(this.scratch, unsignedVarintLength, false);
                if (this.output.isLevel1Element(n)) {
                    break;
                }
            }
            extractorInput.skipFully(1);
        }
        extractorInput.skipFully(unsignedVarintLength);
        return n;
    }
    
    private double readFloat(final ExtractorInput extractorInput, final int n) throws IOException, InterruptedException {
        final long integer = this.readInteger(extractorInput, n);
        double longBitsToDouble;
        if (n == 4) {
            longBitsToDouble = Float.intBitsToFloat((int)integer);
        }
        else {
            longBitsToDouble = Double.longBitsToDouble(integer);
        }
        return longBitsToDouble;
    }
    
    private long readInteger(final ExtractorInput extractorInput, final int n) throws IOException, InterruptedException {
        final byte[] scratch = this.scratch;
        int i = 0;
        extractorInput.readFully(scratch, 0, n);
        long n2 = 0L;
        while (i < n) {
            n2 = (n2 << 8 | (long)(this.scratch[i] & 0xFF));
            ++i;
        }
        return n2;
    }
    
    private String readString(final ExtractorInput extractorInput, int length) throws IOException, InterruptedException {
        if (length == 0) {
            return "";
        }
        final byte[] bytes = new byte[length];
        extractorInput.readFully(bytes, 0, length);
        while (length > 0 && bytes[length - 1] == 0) {
            --length;
        }
        return new String(bytes, 0, length);
    }
    
    @Override
    public void init(final EbmlReaderOutput output) {
        this.output = output;
    }
    
    @Override
    public boolean read(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        Assertions.checkState(this.output != null);
        while (this.masterElementsStack.isEmpty() || extractorInput.getPosition() < this.masterElementsStack.peek().elementEndPosition) {
            if (this.elementState == 0) {
                long n;
                if ((n = this.varintReader.readUnsignedVarint(extractorInput, true, false, 4)) == -2L) {
                    n = this.maybeResyncToNextLevel1Element(extractorInput);
                }
                if (n == -1L) {
                    return false;
                }
                this.elementId = (int)n;
                this.elementState = 1;
            }
            if (this.elementState == 1) {
                this.elementContentSize = this.varintReader.readUnsignedVarint(extractorInput, false, true, 8);
                this.elementState = 2;
            }
            final int elementType = this.output.getElementType(this.elementId);
            if (elementType != 0) {
                if (elementType == 1) {
                    final long position = extractorInput.getPosition();
                    this.masterElementsStack.push(new MasterElement(this.elementId, this.elementContentSize + position));
                    this.output.startMasterElement(this.elementId, position, this.elementContentSize);
                    this.elementState = 0;
                    return true;
                }
                if (elementType != 2) {
                    if (elementType != 3) {
                        if (elementType == 4) {
                            this.output.binaryElement(this.elementId, (int)this.elementContentSize, extractorInput);
                            this.elementState = 0;
                            return true;
                        }
                        if (elementType != 5) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Invalid element type ");
                            sb.append(elementType);
                            throw new ParserException(sb.toString());
                        }
                        final long elementContentSize = this.elementContentSize;
                        if (elementContentSize != 4L && elementContentSize != 8L) {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("Invalid float size: ");
                            sb2.append(this.elementContentSize);
                            throw new ParserException(sb2.toString());
                        }
                        this.output.floatElement(this.elementId, this.readFloat(extractorInput, (int)this.elementContentSize));
                        this.elementState = 0;
                        return true;
                    }
                    else {
                        final long elementContentSize2 = this.elementContentSize;
                        if (elementContentSize2 <= 2147483647L) {
                            this.output.stringElement(this.elementId, this.readString(extractorInput, (int)elementContentSize2));
                            this.elementState = 0;
                            return true;
                        }
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("String element size: ");
                        sb3.append(this.elementContentSize);
                        throw new ParserException(sb3.toString());
                    }
                }
                else {
                    final long elementContentSize3 = this.elementContentSize;
                    if (elementContentSize3 <= 8L) {
                        this.output.integerElement(this.elementId, this.readInteger(extractorInput, (int)elementContentSize3));
                        this.elementState = 0;
                        return true;
                    }
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("Invalid integer size: ");
                    sb4.append(this.elementContentSize);
                    throw new ParserException(sb4.toString());
                }
            }
            else {
                extractorInput.skipFully((int)this.elementContentSize);
                this.elementState = 0;
            }
        }
        this.output.endMasterElement(this.masterElementsStack.pop().elementId);
        return true;
    }
    
    @Override
    public void reset() {
        this.elementState = 0;
        this.masterElementsStack.clear();
        this.varintReader.reset();
    }
    
    private static final class MasterElement
    {
        private final long elementEndPosition;
        private final int elementId;
        
        private MasterElement(final int elementId, final long elementEndPosition) {
            this.elementId = elementId;
            this.elementEndPosition = elementEndPosition;
        }
    }
}
