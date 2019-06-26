package com.google.android.exoplayer2.extractor.wav;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;

final class WavHeaderReader {

    private static final class ChunkHeader {
        /* renamed from: id */
        public final int f20id;
        public final long size;

        private ChunkHeader(int i, long j) {
            this.f20id = i;
            this.size = j;
        }

        public static ChunkHeader peek(ExtractorInput extractorInput, ParsableByteArray parsableByteArray) throws IOException, InterruptedException {
            extractorInput.peekFully(parsableByteArray.data, 0, 8);
            parsableByteArray.setPosition(0);
            return new ChunkHeader(parsableByteArray.readInt(), parsableByteArray.readLittleEndianUnsignedInt());
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:25:0x00d8 in {2, 6, 10, 13, 14, 20, 22, 24} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    public static com.google.android.exoplayer2.extractor.wav.WavHeader peek(com.google.android.exoplayer2.extractor.ExtractorInput r14) throws java.io.IOException, java.lang.InterruptedException {
        /*
        com.google.android.exoplayer2.util.Assertions.checkNotNull(r14);
        r0 = new com.google.android.exoplayer2.util.ParsableByteArray;
        r1 = 16;
        r0.<init>(r1);
        r2 = com.google.android.exoplayer2.extractor.wav.WavHeaderReader.ChunkHeader.peek(r14, r0);
        r2 = r2.f20id;
        r3 = com.google.android.exoplayer2.audio.WavUtil.RIFF_FOURCC;
        r4 = 0;
        if (r2 == r3) goto L_0x0016;
        return r4;
        r2 = r0.data;
        r3 = 4;
        r5 = 0;
        r14.peekFully(r2, r5, r3);
        r0.setPosition(r5);
        r2 = r0.readInt();
        r3 = com.google.android.exoplayer2.audio.WavUtil.WAVE_FOURCC;
        r6 = "WavHeaderReader";
        if (r2 == r3) goto L_0x003f;
        r14 = new java.lang.StringBuilder;
        r14.<init>();
        r0 = "Unsupported RIFF format: ";
        r14.append(r0);
        r14.append(r2);
        r14 = r14.toString();
        com.google.android.exoplayer2.util.Log.m14e(r6, r14);
        return r4;
        r2 = com.google.android.exoplayer2.extractor.wav.WavHeaderReader.ChunkHeader.peek(r14, r0);
        r3 = r2.f20id;
        r7 = com.google.android.exoplayer2.audio.WavUtil.FMT_FOURCC;
        if (r3 == r7) goto L_0x0054;
        r2 = r2.size;
        r3 = (int) r2;
        r14.advancePeekPosition(r3);
        r2 = com.google.android.exoplayer2.extractor.wav.WavHeaderReader.ChunkHeader.peek(r14, r0);
        goto L_0x0043;
        r7 = r2.size;
        r9 = 16;
        r3 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1));
        if (r3 < 0) goto L_0x005e;
        r3 = 1;
        goto L_0x005f;
        r3 = 0;
        com.google.android.exoplayer2.util.Assertions.checkState(r3);
        r3 = r0.data;
        r14.peekFully(r3, r5, r1);
        r0.setPosition(r5);
        r3 = r0.readLittleEndianUnsignedShort();
        r8 = r0.readLittleEndianUnsignedShort();
        r9 = r0.readLittleEndianUnsignedIntToInt();
        r10 = r0.readLittleEndianUnsignedIntToInt();
        r11 = r0.readLittleEndianUnsignedShort();
        r12 = r0.readLittleEndianUnsignedShort();
        r0 = r8 * r12;
        r0 = r0 / 8;
        if (r11 != r0) goto L_0x00b9;
        r13 = com.google.android.exoplayer2.audio.WavUtil.getEncodingForType(r3, r12);
        if (r13 != 0) goto L_0x00ab;
        r14 = new java.lang.StringBuilder;
        r14.<init>();
        r0 = "Unsupported WAV format: ";
        r14.append(r0);
        r14.append(r12);
        r0 = " bit/sample, type ";
        r14.append(r0);
        r14.append(r3);
        r14 = r14.toString();
        com.google.android.exoplayer2.util.Log.m14e(r6, r14);
        return r4;
        r2 = r2.size;
        r0 = (int) r2;
        r0 = r0 - r1;
        r14.advancePeekPosition(r0);
        r14 = new com.google.android.exoplayer2.extractor.wav.WavHeader;
        r7 = r14;
        r7.<init>(r8, r9, r10, r11, r12, r13);
        return r14;
        r14 = new com.google.android.exoplayer2.ParserException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Expected block alignment: ";
        r1.append(r2);
        r1.append(r0);
        r0 = "; got: ";
        r1.append(r0);
        r1.append(r11);
        r0 = r1.toString();
        r14.<init>(r0);
        throw r14;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.extractor.wav.WavHeaderReader.peek(com.google.android.exoplayer2.extractor.ExtractorInput):com.google.android.exoplayer2.extractor.wav.WavHeader");
    }

    public static void skipToData(ExtractorInput extractorInput, WavHeader wavHeader) throws IOException, InterruptedException {
        Assertions.checkNotNull(extractorInput);
        Assertions.checkNotNull(wavHeader);
        extractorInput.resetPeekPosition();
        ParsableByteArray parsableByteArray = new ParsableByteArray(8);
        ChunkHeader peek = ChunkHeader.peek(extractorInput, parsableByteArray);
        while (peek.f20id != Util.getIntegerCodeForString("data")) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Ignoring unknown WAV chunk: ");
            stringBuilder.append(peek.f20id);
            Log.m18w("WavHeaderReader", stringBuilder.toString());
            long j = peek.size + 8;
            if (peek.f20id == Util.getIntegerCodeForString("RIFF")) {
                j = 12;
            }
            if (j <= 2147483647L) {
                extractorInput.skipFully((int) j);
                peek = ChunkHeader.peek(extractorInput, parsableByteArray);
            } else {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Chunk is too large (~2GB+) to skip; id: ");
                stringBuilder2.append(peek.f20id);
                throw new ParserException(stringBuilder2.toString());
            }
        }
        extractorInput.skipFully(8);
        wavHeader.setDataBounds(extractorInput.getPosition(), peek.size);
    }
}
