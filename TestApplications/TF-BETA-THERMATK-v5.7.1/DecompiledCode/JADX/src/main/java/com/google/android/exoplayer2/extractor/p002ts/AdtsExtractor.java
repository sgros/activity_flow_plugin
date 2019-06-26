package com.google.android.exoplayer2.extractor.p002ts;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.ConstantBitrateSeekMap;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.SeekMap.Unseekable;
import com.google.android.exoplayer2.extractor.p002ts.TsPayloadReader.TrackIdGenerator;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;

/* renamed from: com.google.android.exoplayer2.extractor.ts.AdtsExtractor */
public final class AdtsExtractor implements Extractor {
    public static final ExtractorsFactory FACTORY = C3340-$$Lambda$AdtsExtractor$cqGYwjddB4W6E3ogPGiWfjTa23c.INSTANCE;
    private static final int ID3_TAG = Util.getIntegerCodeForString("ID3");
    private int averageFrameSize;
    private ExtractorOutput extractorOutput;
    private long firstFramePosition;
    private long firstSampleTimestampUs;
    private final long firstStreamSampleTimestampUs;
    private final int flags;
    private boolean hasCalculatedAverageFrameSize;
    private boolean hasOutputSeekMap;
    private final ParsableByteArray packetBuffer;
    private final AdtsReader reader;
    private final ParsableByteArray scratch;
    private final ParsableBitArray scratchBits;
    private boolean startedPacket;

    public void release() {
    }

    public AdtsExtractor() {
        this(0);
    }

    public AdtsExtractor(long j) {
        this(j, 0);
    }

    public AdtsExtractor(long j, int i) {
        this.firstStreamSampleTimestampUs = j;
        this.firstSampleTimestampUs = j;
        this.flags = i;
        this.reader = new AdtsReader(true);
        this.packetBuffer = new ParsableByteArray(2048);
        this.averageFrameSize = -1;
        this.firstFramePosition = -1;
        this.scratch = new ParsableByteArray(10);
        this.scratchBits = new ParsableBitArray(this.scratch.data);
    }

    /* JADX WARNING: Missing block: B:4:0x0021, code skipped:
            r9.resetPeekPosition();
            r3 = r3 + 1;
     */
    /* JADX WARNING: Missing block: B:5:0x002a, code skipped:
            if ((r3 - r0) < org.telegram.messenger.MessagesController.UPDATE_MASK_CHAT) goto L_0x002d;
     */
    /* JADX WARNING: Missing block: B:6:0x002c, code skipped:
            return false;
     */
    public boolean sniff(com.google.android.exoplayer2.extractor.ExtractorInput r9) throws java.io.IOException, java.lang.InterruptedException {
        /*
        r8 = this;
        r0 = r8.peekId3Header(r9);
        r1 = 0;
        r3 = r0;
    L_0x0006:
        r2 = 0;
        r4 = 0;
    L_0x0008:
        r5 = r8.scratch;
        r5 = r5.data;
        r6 = 2;
        r9.peekFully(r5, r1, r6);
        r5 = r8.scratch;
        r5.setPosition(r1);
        r5 = r8.scratch;
        r5 = r5.readUnsignedShort();
        r5 = com.google.android.exoplayer2.extractor.p002ts.AdtsReader.isAdtsSyncWord(r5);
        if (r5 != 0) goto L_0x0031;
    L_0x0021:
        r9.resetPeekPosition();
        r3 = r3 + 1;
        r2 = r3 - r0;
        r4 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
        if (r2 < r4) goto L_0x002d;
    L_0x002c:
        return r1;
    L_0x002d:
        r9.advancePeekPosition(r3);
        goto L_0x0006;
    L_0x0031:
        r5 = 1;
        r2 = r2 + r5;
        r6 = 4;
        if (r2 < r6) goto L_0x003b;
    L_0x0036:
        r7 = 188; // 0xbc float:2.63E-43 double:9.3E-322;
        if (r4 <= r7) goto L_0x003b;
    L_0x003a:
        return r5;
    L_0x003b:
        r5 = r8.scratch;
        r5 = r5.data;
        r9.peekFully(r5, r1, r6);
        r5 = r8.scratchBits;
        r6 = 14;
        r5.setPosition(r6);
        r5 = r8.scratchBits;
        r6 = 13;
        r5 = r5.readBits(r6);
        r6 = 6;
        if (r5 > r6) goto L_0x0055;
    L_0x0054:
        return r1;
    L_0x0055:
        r6 = r5 + -6;
        r9.advancePeekPosition(r6);
        r4 = r4 + r5;
        goto L_0x0008;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.extractor.p002ts.AdtsExtractor.sniff(com.google.android.exoplayer2.extractor.ExtractorInput):boolean");
    }

    public void init(ExtractorOutput extractorOutput) {
        this.extractorOutput = extractorOutput;
        this.reader.createTracks(extractorOutput, new TrackIdGenerator(0, 1));
        extractorOutput.endTracks();
    }

    public void seek(long j, long j2) {
        this.startedPacket = false;
        this.reader.seek();
        this.firstSampleTimestampUs = this.firstStreamSampleTimestampUs + j2;
    }

    public int read(ExtractorInput extractorInput, PositionHolder positionHolder) throws IOException, InterruptedException {
        long length = extractorInput.getLength();
        boolean z = ((this.flags & 1) == 0 || length == -1) ? false : true;
        if (z) {
            calculateAverageFrameSize(extractorInput);
        }
        int read = extractorInput.read(this.packetBuffer.data, 0, 2048);
        boolean z2 = read == -1;
        maybeOutputSeekMap(length, z, z2);
        if (z2) {
            return -1;
        }
        this.packetBuffer.setPosition(0);
        this.packetBuffer.setLimit(read);
        if (!this.startedPacket) {
            this.reader.packetStarted(this.firstSampleTimestampUs, 4);
            this.startedPacket = true;
        }
        this.reader.consume(this.packetBuffer);
        return 0;
    }

    private int peekId3Header(ExtractorInput extractorInput) throws IOException, InterruptedException {
        int i = 0;
        while (true) {
            extractorInput.peekFully(this.scratch.data, 0, 10);
            this.scratch.setPosition(0);
            if (this.scratch.readUnsignedInt24() != ID3_TAG) {
                break;
            }
            this.scratch.skipBytes(3);
            int readSynchSafeInt = this.scratch.readSynchSafeInt();
            i += readSynchSafeInt + 10;
            extractorInput.advancePeekPosition(readSynchSafeInt);
        }
        extractorInput.resetPeekPosition();
        extractorInput.advancePeekPosition(i);
        if (this.firstFramePosition == -1) {
            this.firstFramePosition = (long) i;
        }
        return i;
    }

    private void maybeOutputSeekMap(long j, boolean z, boolean z2) {
        if (!this.hasOutputSeekMap) {
            Object obj = (!z || this.averageFrameSize <= 0) ? null : 1;
            if (obj == null || this.reader.getSampleDurationUs() != -9223372036854775807L || z2) {
                ExtractorOutput extractorOutput = this.extractorOutput;
                Assertions.checkNotNull(extractorOutput);
                extractorOutput = extractorOutput;
                if (obj == null || this.reader.getSampleDurationUs() == -9223372036854775807L) {
                    extractorOutput.seekMap(new Unseekable(-9223372036854775807L));
                } else {
                    extractorOutput.seekMap(getConstantBitrateSeekMap(j));
                }
                this.hasOutputSeekMap = true;
            }
        }
    }

    private void calculateAverageFrameSize(ExtractorInput extractorInput) throws IOException, InterruptedException {
        if (!this.hasCalculatedAverageFrameSize) {
            this.averageFrameSize = -1;
            extractorInput.resetPeekPosition();
            long j = 0;
            if (extractorInput.getPosition() == 0) {
                peekId3Header(extractorInput);
            }
            int i = 0;
            while (extractorInput.peekFully(this.scratch.data, 0, 2, true)) {
                this.scratch.setPosition(0);
                if (!AdtsReader.isAdtsSyncWord(this.scratch.readUnsignedShort())) {
                    i = 0;
                    break;
                } else if (!extractorInput.peekFully(this.scratch.data, 0, 4, true)) {
                    break;
                } else {
                    this.scratchBits.setPosition(14);
                    int readBits = this.scratchBits.readBits(13);
                    if (readBits > 6) {
                        j += (long) readBits;
                        i++;
                        if (i != 1000) {
                            if (!extractorInput.advancePeekPosition(readBits - 6, true)) {
                                break;
                            }
                        }
                        break;
                    }
                    this.hasCalculatedAverageFrameSize = true;
                    throw new ParserException("Malformed ADTS stream");
                }
            }
            extractorInput.resetPeekPosition();
            if (i > 0) {
                this.averageFrameSize = (int) (j / ((long) i));
            } else {
                this.averageFrameSize = -1;
            }
            this.hasCalculatedAverageFrameSize = true;
        }
    }

    private SeekMap getConstantBitrateSeekMap(long j) {
        return new ConstantBitrateSeekMap(j, this.firstFramePosition, AdtsExtractor.getBitrateFromFrameSize(this.averageFrameSize, this.reader.getSampleDurationUs()), this.averageFrameSize);
    }

    private static int getBitrateFromFrameSize(int i, long j) {
        return (int) ((((long) (i * 8)) * 1000000) / j);
    }
}
