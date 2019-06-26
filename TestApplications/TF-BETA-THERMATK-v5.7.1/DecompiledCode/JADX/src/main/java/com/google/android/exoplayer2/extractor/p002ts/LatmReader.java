package com.google.android.exoplayer2.extractor.p002ts;

import android.util.Pair;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.extractor.p002ts.TsPayloadReader.TrackIdGenerator;
import com.google.android.exoplayer2.util.CodecSpecificDataUtil;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.ParsableByteArray;

/* renamed from: com.google.android.exoplayer2.extractor.ts.LatmReader */
public final class LatmReader implements ElementaryStreamReader {
    private int audioMuxVersionA;
    private int bytesRead;
    private int channelCount;
    private Format format;
    private String formatId;
    private int frameLengthType;
    private final String language;
    private int numSubframes;
    private long otherDataLenBits;
    private boolean otherDataPresent;
    private TrackOutput output;
    private final ParsableBitArray sampleBitArray = new ParsableBitArray(this.sampleDataBuffer.data);
    private final ParsableByteArray sampleDataBuffer = new ParsableByteArray(1024);
    private long sampleDurationUs;
    private int sampleRateHz;
    private int sampleSize;
    private int secondHeaderByte;
    private int state;
    private boolean streamMuxRead;
    private long timeUs;

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:8:0x0017 in {5, 7} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
        	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    private int parsePayloadLengthInfo(com.google.android.exoplayer2.util.ParsableBitArray r4) throws com.google.android.exoplayer2.ParserException {
        /*
        r3 = this;
        r0 = r3.frameLengthType;
        if (r0 != 0) goto L_0x0011;
        r0 = 0;
        r1 = 8;
        r1 = r4.readBits(r1);
        r0 = r0 + r1;
        r2 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        if (r1 == r2) goto L_0x0005;
        return r0;
        r4 = new com.google.android.exoplayer2.ParserException;
        r4.<init>();
        throw r4;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.extractor.p002ts.LatmReader.parsePayloadLengthInfo(com.google.android.exoplayer2.util.ParsableBitArray):int");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:35:0x00d7 in {2, 3, 7, 17, 18, 22, 24, 27, 28, 30, 32, 34} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
        	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    private void parseStreamMuxConfig(com.google.android.exoplayer2.util.ParsableBitArray r20) throws com.google.android.exoplayer2.ParserException {
        /*
        r19 = this;
        r0 = r19;
        r1 = r20;
        r2 = 1;
        r3 = r1.readBits(r2);
        r4 = 0;
        if (r3 != r2) goto L_0x0011;
        r5 = r1.readBits(r2);
        goto L_0x0012;
        r5 = 0;
        r0.audioMuxVersionA = r5;
        r5 = r0.audioMuxVersionA;
        if (r5 != 0) goto L_0x00d1;
        if (r3 != r2) goto L_0x001d;
        com.google.android.exoplayer2.extractor.p002ts.LatmReader.latmGetValue(r20);
        r5 = r20.readBit();
        if (r5 == 0) goto L_0x00cb;
        r5 = 6;
        r5 = r1.readBits(r5);
        r0.numSubframes = r5;
        r5 = 4;
        r5 = r1.readBits(r5);
        r6 = 3;
        r6 = r1.readBits(r6);
        if (r5 != 0) goto L_0x00c5;
        if (r6 != 0) goto L_0x00c5;
        r5 = 8;
        if (r3 != 0) goto L_0x0083;
        r6 = r20.getPosition();
        r7 = r19.parseAudioSpecificConfig(r20);
        r1.setPosition(r6);
        r6 = r7 + 7;
        r6 = r6 / r5;
        r6 = new byte[r6];
        r1.readBits(r6, r4, r7);
        r8 = r0.formatId;
        r10 = 0;
        r11 = -1;
        r12 = -1;
        r13 = r0.channelCount;
        r14 = r0.sampleRateHz;
        r15 = java.util.Collections.singletonList(r6);
        r16 = 0;
        r17 = 0;
        r4 = r0.language;
        r9 = "audio/mp4a-latm";
        r18 = r4;
        r4 = com.google.android.exoplayer2.Format.createAudioSampleFormat(r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18);
        r6 = r0.format;
        r6 = r4.equals(r6);
        if (r6 != 0) goto L_0x0090;
        r0.format = r4;
        r6 = 1024000000; // 0x3d090000 float:0.033447266 double:5.059232213E-315;
        r8 = r4.sampleRate;
        r8 = (long) r8;
        r6 = r6 / r8;
        r0.sampleDurationUs = r6;
        r6 = r0.output;
        r6.format(r4);
        goto L_0x0090;
        r6 = com.google.android.exoplayer2.extractor.p002ts.LatmReader.latmGetValue(r20);
        r4 = (int) r6;
        r6 = r19.parseAudioSpecificConfig(r20);
        r4 = r4 - r6;
        r1.skipBits(r4);
        r19.parseFrameLength(r20);
        r4 = r20.readBit();
        r0.otherDataPresent = r4;
        r6 = 0;
        r0.otherDataLenBits = r6;
        r4 = r0.otherDataPresent;
        if (r4 == 0) goto L_0x00bb;
        if (r3 != r2) goto L_0x00aa;
        r2 = com.google.android.exoplayer2.extractor.p002ts.LatmReader.latmGetValue(r20);
        r0.otherDataLenBits = r2;
        goto L_0x00bb;
        r2 = r20.readBit();
        r3 = r0.otherDataLenBits;
        r3 = r3 << r5;
        r6 = r1.readBits(r5);
        r6 = (long) r6;
        r3 = r3 + r6;
        r0.otherDataLenBits = r3;
        if (r2 != 0) goto L_0x00aa;
        r2 = r20.readBit();
        if (r2 == 0) goto L_0x00c4;
        r1.skipBits(r5);
        return;
        r1 = new com.google.android.exoplayer2.ParserException;
        r1.<init>();
        throw r1;
        r1 = new com.google.android.exoplayer2.ParserException;
        r1.<init>();
        throw r1;
        r1 = new com.google.android.exoplayer2.ParserException;
        r1.<init>();
        throw r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.extractor.p002ts.LatmReader.parseStreamMuxConfig(com.google.android.exoplayer2.util.ParsableBitArray):void");
    }

    public void packetFinished() {
    }

    public LatmReader(String str) {
        this.language = str;
    }

    public void seek() {
        this.state = 0;
        this.streamMuxRead = false;
    }

    public void createTracks(ExtractorOutput extractorOutput, TrackIdGenerator trackIdGenerator) {
        trackIdGenerator.generateNewId();
        this.output = extractorOutput.track(trackIdGenerator.getTrackId(), 1);
        this.formatId = trackIdGenerator.getFormatId();
    }

    public void packetStarted(long j, int i) {
        this.timeUs = j;
    }

    public void consume(ParsableByteArray parsableByteArray) throws ParserException {
        while (parsableByteArray.bytesLeft() > 0) {
            int i = this.state;
            if (i != 0) {
                if (i == 1) {
                    i = parsableByteArray.readUnsignedByte();
                    if ((i & 224) == 224) {
                        this.secondHeaderByte = i;
                        this.state = 2;
                    } else if (i != 86) {
                        this.state = 0;
                    }
                } else if (i == 2) {
                    this.sampleSize = ((this.secondHeaderByte & -225) << 8) | parsableByteArray.readUnsignedByte();
                    i = this.sampleSize;
                    if (i > this.sampleDataBuffer.data.length) {
                        resetBufferForSize(i);
                    }
                    this.bytesRead = 0;
                    this.state = 3;
                } else if (i == 3) {
                    i = Math.min(parsableByteArray.bytesLeft(), this.sampleSize - this.bytesRead);
                    parsableByteArray.readBytes(this.sampleBitArray.data, this.bytesRead, i);
                    this.bytesRead += i;
                    if (this.bytesRead == this.sampleSize) {
                        this.sampleBitArray.setPosition(0);
                        parseAudioMuxElement(this.sampleBitArray);
                        this.state = 0;
                    }
                } else {
                    throw new IllegalStateException();
                }
            } else if (parsableByteArray.readUnsignedByte() == 86) {
                this.state = 1;
            }
        }
    }

    private void parseAudioMuxElement(ParsableBitArray parsableBitArray) throws ParserException {
        if (!parsableBitArray.readBit()) {
            this.streamMuxRead = true;
            parseStreamMuxConfig(parsableBitArray);
        } else if (!this.streamMuxRead) {
            return;
        }
        if (this.audioMuxVersionA != 0) {
            throw new ParserException();
        } else if (this.numSubframes == 0) {
            parsePayloadMux(parsableBitArray, parsePayloadLengthInfo(parsableBitArray));
            if (this.otherDataPresent) {
                parsableBitArray.skipBits((int) this.otherDataLenBits);
            }
        } else {
            throw new ParserException();
        }
    }

    private void parseFrameLength(ParsableBitArray parsableBitArray) {
        this.frameLengthType = parsableBitArray.readBits(3);
        int i = this.frameLengthType;
        if (i == 0) {
            parsableBitArray.skipBits(8);
        } else if (i == 1) {
            parsableBitArray.skipBits(9);
        } else if (i == 3 || i == 4 || i == 5) {
            parsableBitArray.skipBits(6);
        } else if (i == 6 || i == 7) {
            parsableBitArray.skipBits(1);
        } else {
            throw new IllegalStateException();
        }
    }

    private int parseAudioSpecificConfig(ParsableBitArray parsableBitArray) throws ParserException {
        int bitsLeft = parsableBitArray.bitsLeft();
        Pair parseAacAudioSpecificConfig = CodecSpecificDataUtil.parseAacAudioSpecificConfig(parsableBitArray, true);
        this.sampleRateHz = ((Integer) parseAacAudioSpecificConfig.first).intValue();
        this.channelCount = ((Integer) parseAacAudioSpecificConfig.second).intValue();
        return bitsLeft - parsableBitArray.bitsLeft();
    }

    private void parsePayloadMux(ParsableBitArray parsableBitArray, int i) {
        int position = parsableBitArray.getPosition();
        if ((position & 7) == 0) {
            this.sampleDataBuffer.setPosition(position >> 3);
        } else {
            parsableBitArray.readBits(this.sampleDataBuffer.data, 0, i * 8);
            this.sampleDataBuffer.setPosition(0);
        }
        this.output.sampleData(this.sampleDataBuffer, i);
        this.output.sampleMetadata(this.timeUs, 1, i, 0, null);
        this.timeUs += this.sampleDurationUs;
    }

    private void resetBufferForSize(int i) {
        this.sampleDataBuffer.reset(i);
        this.sampleBitArray.reset(this.sampleDataBuffer.data);
    }

    private static long latmGetValue(ParsableBitArray parsableBitArray) {
        return (long) parsableBitArray.readBits((parsableBitArray.readBits(2) + 1) * 8);
    }
}
