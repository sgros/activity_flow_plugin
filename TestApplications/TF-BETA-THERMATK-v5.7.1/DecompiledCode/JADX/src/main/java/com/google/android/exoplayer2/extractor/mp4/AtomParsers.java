package com.google.android.exoplayer2.extractor.mp4;

import android.util.Pair;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.audio.Ac3Util;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.Metadata.Entry;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.CodecSpecificDataUtil;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.AvcConfig;
import com.google.android.exoplayer2.video.HevcConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.telegram.messenger.MessagesController;

final class AtomParsers {
    private static final int TYPE_clcp = Util.getIntegerCodeForString("clcp");
    private static final int TYPE_mdta = Util.getIntegerCodeForString("mdta");
    private static final int TYPE_meta = Util.getIntegerCodeForString("meta");
    private static final int TYPE_sbtl = Util.getIntegerCodeForString("sbtl");
    private static final int TYPE_soun = Util.getIntegerCodeForString("soun");
    private static final int TYPE_subt = Util.getIntegerCodeForString("subt");
    private static final int TYPE_text = Util.getIntegerCodeForString(MimeTypes.BASE_TYPE_TEXT);
    private static final int TYPE_vide = Util.getIntegerCodeForString("vide");
    private static final byte[] opusMagic = Util.getUtf8Bytes("OpusHead");

    private static final class ChunkIterator {
        private final ParsableByteArray chunkOffsets;
        private final boolean chunkOffsetsAreLongs;
        public int index;
        public final int length;
        private int nextSamplesPerChunkChangeIndex;
        public int numSamples;
        public long offset;
        private int remainingSamplesPerChunkChanges;
        private final ParsableByteArray stsc;

        public ChunkIterator(ParsableByteArray parsableByteArray, ParsableByteArray parsableByteArray2, boolean z) {
            this.stsc = parsableByteArray;
            this.chunkOffsets = parsableByteArray2;
            this.chunkOffsetsAreLongs = z;
            parsableByteArray2.setPosition(12);
            this.length = parsableByteArray2.readUnsignedIntToInt();
            parsableByteArray.setPosition(12);
            this.remainingSamplesPerChunkChanges = parsableByteArray.readUnsignedIntToInt();
            boolean z2 = true;
            if (parsableByteArray.readInt() != 1) {
                z2 = false;
            }
            Assertions.checkState(z2, "first_chunk must be 1");
            this.index = -1;
        }

        public boolean moveNext() {
            int i = this.index + 1;
            this.index = i;
            if (i == this.length) {
                return false;
            }
            long readUnsignedLongToLong;
            if (this.chunkOffsetsAreLongs) {
                readUnsignedLongToLong = this.chunkOffsets.readUnsignedLongToLong();
            } else {
                readUnsignedLongToLong = this.chunkOffsets.readUnsignedInt();
            }
            this.offset = readUnsignedLongToLong;
            if (this.index == this.nextSamplesPerChunkChangeIndex) {
                this.numSamples = this.stsc.readUnsignedIntToInt();
                this.stsc.skipBytes(4);
                i = this.remainingSamplesPerChunkChanges - 1;
                this.remainingSamplesPerChunkChanges = i;
                this.nextSamplesPerChunkChangeIndex = i > 0 ? this.stsc.readUnsignedIntToInt() - 1 : -1;
            }
            return true;
        }
    }

    private interface SampleSizeBox {
        int getSampleCount();

        boolean isFixedSampleSize();

        int readNextSampleSize();
    }

    private static final class StsdData {
        public Format format;
        public int nalUnitLengthFieldLength;
        public int requiredSampleTransformation = 0;
        public final TrackEncryptionBox[] trackEncryptionBoxes;

        public StsdData(int i) {
            this.trackEncryptionBoxes = new TrackEncryptionBox[i];
        }
    }

    private static final class TkhdData {
        private final long duration;
        /* renamed from: id */
        private final int f18id;
        private final int rotationDegrees;

        public TkhdData(int i, long j, int i2) {
            this.f18id = i;
            this.duration = j;
            this.rotationDegrees = i2;
        }
    }

    static final class StszSampleSizeBox implements SampleSizeBox {
        private final ParsableByteArray data;
        private final int fixedSampleSize = this.data.readUnsignedIntToInt();
        private final int sampleCount = this.data.readUnsignedIntToInt();

        public StszSampleSizeBox(LeafAtom leafAtom) {
            this.data = leafAtom.data;
            this.data.setPosition(12);
        }

        public int getSampleCount() {
            return this.sampleCount;
        }

        public int readNextSampleSize() {
            int i = this.fixedSampleSize;
            return i == 0 ? this.data.readUnsignedIntToInt() : i;
        }

        public boolean isFixedSampleSize() {
            return this.fixedSampleSize != 0;
        }
    }

    static final class Stz2SampleSizeBox implements SampleSizeBox {
        private int currentByte;
        private final ParsableByteArray data;
        private final int fieldSize = (this.data.readUnsignedIntToInt() & NalUnitUtil.EXTENDED_SAR);
        private final int sampleCount = this.data.readUnsignedIntToInt();
        private int sampleIndex;

        public boolean isFixedSampleSize() {
            return false;
        }

        public Stz2SampleSizeBox(LeafAtom leafAtom) {
            this.data = leafAtom.data;
            this.data.setPosition(12);
        }

        public int getSampleCount() {
            return this.sampleCount;
        }

        public int readNextSampleSize() {
            int i = this.fieldSize;
            if (i == 8) {
                return this.data.readUnsignedByte();
            }
            if (i == 16) {
                return this.data.readUnsignedShort();
            }
            i = this.sampleIndex;
            this.sampleIndex = i + 1;
            if (i % 2 != 0) {
                return this.currentByte & 15;
            }
            this.currentByte = this.data.readUnsignedByte();
            return (this.currentByte & 240) >> 4;
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:205:0x04d7 in {2, 5, 9, 12, 13, 16, 17, 20, 21, 24, 25, 30, 31, 32, 40, 41, 51, 54, 59, 60, 61, 64, 67, 68, 73, 74, 75, 79, 80, 81, 86, 87, 88, 96, 97, 98, 101, 102, 103, 104, 108, 109, 114, 126, 132, 133, 141, 143, 146, 147, 158, 161, 162, 163, 164, 165, 168, 169, 172, 173, 175, 176, 178, 179, 181, 182, 188, 189, 196, 197, 198, 200, 202, 204} preds:[]
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
    public static com.google.android.exoplayer2.extractor.mp4.TrackSampleTable parseStbl(com.google.android.exoplayer2.extractor.mp4.Track r35, com.google.android.exoplayer2.extractor.mp4.Atom.ContainerAtom r36, com.google.android.exoplayer2.extractor.GaplessInfoHolder r37) throws com.google.android.exoplayer2.ParserException {
        /*
        r1 = r35;
        r0 = r36;
        r2 = r37;
        r3 = com.google.android.exoplayer2.extractor.mp4.Atom.TYPE_stsz;
        r3 = r0.getLeafAtomOfType(r3);
        if (r3 == 0) goto L_0x0014;
        r4 = new com.google.android.exoplayer2.extractor.mp4.AtomParsers$StszSampleSizeBox;
        r4.<init>(r3);
        goto L_0x0021;
        r3 = com.google.android.exoplayer2.extractor.mp4.Atom.TYPE_stz2;
        r3 = r0.getLeafAtomOfType(r3);
        if (r3 == 0) goto L_0x04cf;
        r4 = new com.google.android.exoplayer2.extractor.mp4.AtomParsers$Stz2SampleSizeBox;
        r4.<init>(r3);
        r3 = r4.getSampleCount();
        r5 = 0;
        if (r3 != 0) goto L_0x0042;
        r9 = new com.google.android.exoplayer2.extractor.mp4.TrackSampleTable;
        r2 = new long[r5];
        r3 = new int[r5];
        r4 = 0;
        r6 = new long[r5];
        r7 = new int[r5];
        r10 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
        r0 = r9;
        r1 = r35;
        r5 = r6;
        r6 = r7;
        r7 = r10;
        r0.<init>(r1, r2, r3, r4, r5, r6, r7);
        return r9;
        r6 = com.google.android.exoplayer2.extractor.mp4.Atom.TYPE_stco;
        r6 = r0.getLeafAtomOfType(r6);
        r7 = 1;
        if (r6 != 0) goto L_0x0053;
        r6 = com.google.android.exoplayer2.extractor.mp4.Atom.TYPE_co64;
        r6 = r0.getLeafAtomOfType(r6);
        r8 = 1;
        goto L_0x0054;
        r8 = 0;
        r6 = r6.data;
        r9 = com.google.android.exoplayer2.extractor.mp4.Atom.TYPE_stsc;
        r9 = r0.getLeafAtomOfType(r9);
        r9 = r9.data;
        r10 = com.google.android.exoplayer2.extractor.mp4.Atom.TYPE_stts;
        r10 = r0.getLeafAtomOfType(r10);
        r10 = r10.data;
        r11 = com.google.android.exoplayer2.extractor.mp4.Atom.TYPE_stss;
        r11 = r0.getLeafAtomOfType(r11);
        r12 = 0;
        if (r11 == 0) goto L_0x0072;
        r11 = r11.data;
        goto L_0x0073;
        r11 = r12;
        r13 = com.google.android.exoplayer2.extractor.mp4.Atom.TYPE_ctts;
        r0 = r0.getLeafAtomOfType(r13);
        if (r0 == 0) goto L_0x007e;
        r0 = r0.data;
        goto L_0x007f;
        r0 = r12;
        r13 = new com.google.android.exoplayer2.extractor.mp4.AtomParsers$ChunkIterator;
        r13.<init>(r9, r6, r8);
        r6 = 12;
        r10.setPosition(r6);
        r8 = r10.readUnsignedIntToInt();
        r8 = r8 - r7;
        r9 = r10.readUnsignedIntToInt();
        r14 = r10.readUnsignedIntToInt();
        if (r0 == 0) goto L_0x00a0;
        r0.setPosition(r6);
        r15 = r0.readUnsignedIntToInt();
        goto L_0x00a1;
        r15 = 0;
        r16 = -1;
        if (r11 == 0) goto L_0x00b7;
        r11.setPosition(r6);
        r6 = r11.readUnsignedIntToInt();
        if (r6 <= 0) goto L_0x00b5;
        r12 = r11.readUnsignedIntToInt();
        r16 = r12 + -1;
        goto L_0x00b8;
        r11 = r12;
        goto L_0x00b8;
        r6 = 0;
        r12 = r4.isFixedSampleSize();
        if (r12 == 0) goto L_0x00d2;
        r12 = r1.format;
        r12 = r12.sampleMimeType;
        r5 = "audio/raw";
        r5 = r5.equals(r12);
        if (r5 == 0) goto L_0x00d2;
        if (r8 != 0) goto L_0x00d2;
        if (r15 != 0) goto L_0x00d2;
        if (r6 != 0) goto L_0x00d2;
        r5 = 1;
        goto L_0x00d3;
        r5 = 0;
        r18 = 0;
        if (r5 != 0) goto L_0x0244;
        r5 = new long[r3];
        r12 = new int[r3];
        r7 = new long[r3];
        r36 = r6;
        r6 = new int[r3];
        r28 = r8;
        r27 = r10;
        r10 = r14;
        r21 = r18;
        r23 = r21;
        r1 = 0;
        r8 = 0;
        r25 = 0;
        r26 = 0;
        r14 = r36;
        r36 = r15;
        r15 = r9;
        r9 = r16;
        r16 = 0;
        r2 = "AtomParsers";
        if (r8 >= r3) goto L_0x01b9;
        r29 = r23;
        r23 = 1;
        if (r16 != 0) goto L_0x011e;
        r23 = r13.moveNext();
        if (r23 == 0) goto L_0x011e;
        r24 = r14;
        r31 = r15;
        r14 = r13.offset;
        r32 = r3;
        r3 = r13.numSamples;
        r16 = r3;
        r29 = r14;
        r14 = r24;
        r15 = r31;
        r3 = r32;
        goto L_0x0101;
        r32 = r3;
        r24 = r14;
        r31 = r15;
        if (r23 != 0) goto L_0x013f;
        r3 = "Unexpected end of chunk data";
        com.google.android.exoplayer2.util.Log.m18w(r2, r3);
        r5 = java.util.Arrays.copyOf(r5, r8);
        r12 = java.util.Arrays.copyOf(r12, r8);
        r7 = java.util.Arrays.copyOf(r7, r8);
        r6 = java.util.Arrays.copyOf(r6, r8);
        r32 = r8;
        goto L_0x01bf;
        if (r0 == 0) goto L_0x0156;
        r2 = r36;
        if (r25 != 0) goto L_0x0152;
        if (r2 <= 0) goto L_0x0152;
        r25 = r0.readUnsignedIntToInt();
        r26 = r0.readInt();
        r2 = r2 + -1;
        goto L_0x0143;
        r25 = r25 + -1;
        r3 = r2;
        goto L_0x0158;
        r3 = r36;
        r2 = r26;
        r5[r8] = r29;
        r14 = r4.readNextSampleSize();
        r12[r8] = r14;
        r14 = r12[r8];
        if (r14 <= r1) goto L_0x0168;
        r1 = r12[r8];
        r14 = (long) r2;
        r14 = r21 + r14;
        r7[r8] = r14;
        if (r11 != 0) goto L_0x0171;
        r14 = 1;
        goto L_0x0172;
        r14 = 0;
        r6[r8] = r14;
        if (r8 != r9) goto L_0x0187;
        r14 = 1;
        r6[r8] = r14;
        r15 = r24 + -1;
        if (r15 <= 0) goto L_0x0182;
        r9 = r11.readUnsignedIntToInt();
        r9 = r9 - r14;
        r36 = r1;
        r14 = r15;
        r15 = r2;
        goto L_0x018c;
        r36 = r1;
        r15 = r2;
        r14 = r24;
        r1 = (long) r10;
        r21 = r21 + r1;
        r1 = r31 + -1;
        if (r1 != 0) goto L_0x01a0;
        if (r28 <= 0) goto L_0x01a0;
        r1 = r27.readUnsignedIntToInt();
        r2 = r27.readInt();
        r28 = r28 + -1;
        r10 = r2;
        r2 = r12[r8];
        r23 = r1;
        r1 = (long) r2;
        r1 = r29 + r1;
        r16 = r16 + -1;
        r8 = r8 + 1;
        r26 = r15;
        r15 = r23;
        r23 = r1;
        r1 = r36;
        r36 = r3;
        r3 = r32;
        goto L_0x00f9;
        r32 = r3;
        r24 = r14;
        r31 = r15;
        r3 = r16;
        r15 = r26;
        r8 = (long) r15;
        r21 = r21 + r8;
        r4 = r36;
        if (r4 <= 0) goto L_0x01d8;
        r8 = r0.readUnsignedIntToInt();
        if (r8 == 0) goto L_0x01d2;
        r0 = 0;
        goto L_0x01d9;
        r0.readInt();
        r4 = r4 + -1;
        goto L_0x01c8;
        r0 = 1;
        if (r24 != 0) goto L_0x01ec;
        if (r31 != 0) goto L_0x01ec;
        if (r3 != 0) goto L_0x01ec;
        if (r28 != 0) goto L_0x01ec;
        r4 = r25;
        if (r4 != 0) goto L_0x01ee;
        if (r0 != 0) goto L_0x01e8;
        goto L_0x01ee;
        r9 = r1;
        r1 = r35;
        goto L_0x023f;
        r4 = r25;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = "Inconsistent stbl box for track ";
        r8.append(r9);
        r9 = r1;
        r1 = r35;
        r10 = r1.f19id;
        r8.append(r10);
        r10 = ": remainingSynchronizationSamples ";
        r8.append(r10);
        r14 = r24;
        r8.append(r14);
        r10 = ", remainingSamplesAtTimestampDelta ";
        r8.append(r10);
        r10 = r31;
        r8.append(r10);
        r10 = ", remainingSamplesInChunk ";
        r8.append(r10);
        r8.append(r3);
        r3 = ", remainingTimestampDeltaChanges ";
        r8.append(r3);
        r3 = r28;
        r8.append(r3);
        r3 = ", remainingSamplesAtTimestampOffset ";
        r8.append(r3);
        r8.append(r4);
        if (r0 != 0) goto L_0x0233;
        r0 = ", ctts invalid";
        goto L_0x0235;
        r0 = "";
        r8.append(r0);
        r0 = r8.toString();
        com.google.android.exoplayer2.util.Log.m18w(r2, r0);
        r2 = r5;
        r5 = r7;
        r4 = r9;
        r3 = r12;
        goto L_0x027a;
        r32 = r3;
        r0 = r13.length;
        r2 = new long[r0];
        r0 = new int[r0];
        r3 = r13.moveNext();
        if (r3 == 0) goto L_0x025d;
        r3 = r13.index;
        r4 = r13.offset;
        r2[r3] = r4;
        r4 = r13.numSamples;
        r0[r3] = r4;
        goto L_0x024c;
        r3 = r1.format;
        r4 = r3.pcmEncoding;
        r3 = r3.channelCount;
        r3 = com.google.android.exoplayer2.util.Util.getPcmFrameSize(r4, r3);
        r4 = (long) r14;
        r0 = com.google.android.exoplayer2.extractor.mp4.FixedSampleSizeRechunker.rechunk(r3, r2, r0, r4);
        r2 = r0.offsets;
        r3 = r0.sizes;
        r4 = r0.maximumSize;
        r5 = r0.timestamps;
        r6 = r0.flags;
        r7 = r0.duration;
        r21 = r7;
        r0 = r32;
        r11 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
        r13 = r1.timescale;
        r9 = r21;
        r7 = com.google.android.exoplayer2.util.Util.scaleLargeTimestamp(r9, r11, r13);
        r9 = r1.editListDurations;
        r14 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
        if (r9 == 0) goto L_0x04b2;
        r9 = r37.hasGaplessInfo();
        if (r9 == 0) goto L_0x0296;
        goto L_0x04b2;
        r7 = r1.editListDurations;
        r8 = r7.length;
        r9 = 1;
        if (r8 != r9) goto L_0x0326;
        r8 = r1.type;
        if (r8 != r9) goto L_0x0326;
        r8 = r5.length;
        r9 = 2;
        if (r8 < r9) goto L_0x0326;
        r8 = r1.editListMediaTimes;
        r9 = 0;
        r23 = r8[r9];
        r25 = r7[r9];
        r7 = r1.timescale;
        r9 = r1.movieTimescale;
        r27 = r7;
        r29 = r9;
        r7 = com.google.android.exoplayer2.util.Util.scaleLargeTimestamp(r25, r27, r29);
        r7 = r23 + r7;
        r9 = r5;
        r10 = r21;
        r12 = r23;
        r16 = r3;
        r36 = r4;
        r3 = r14;
        r14 = r7;
        r9 = canApplyEditWithGaplessInfo(r9, r10, r12, r14);
        if (r9 == 0) goto L_0x032a;
        r10 = r21 - r7;
        r7 = 0;
        r8 = r5[r7];
        r25 = r23 - r8;
        r7 = r1.format;
        r7 = r7.sampleRate;
        r7 = (long) r7;
        r12 = r1.timescale;
        r27 = r7;
        r29 = r12;
        r7 = com.google.android.exoplayer2.util.Util.scaleLargeTimestamp(r25, r27, r29);
        r9 = r1.format;
        r9 = r9.sampleRate;
        r12 = (long) r9;
        r14 = r1.timescale;
        r9 = com.google.android.exoplayer2.util.Util.scaleLargeTimestamp(r10, r12, r14);
        r11 = (r7 > r18 ? 1 : (r7 == r18 ? 0 : -1));
        if (r11 != 0) goto L_0x02f3;
        r11 = (r9 > r18 ? 1 : (r9 == r18 ? 0 : -1));
        if (r11 == 0) goto L_0x032a;
        r11 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r13 = (r7 > r11 ? 1 : (r7 == r11 ? 0 : -1));
        if (r13 > 0) goto L_0x032a;
        r13 = (r9 > r11 ? 1 : (r9 == r11 ? 0 : -1));
        if (r13 > 0) goto L_0x032a;
        r0 = (int) r7;
        r7 = r37;
        r7.encoderDelay = r0;
        r0 = (int) r9;
        r7.encoderPadding = r0;
        r7 = r1.timescale;
        com.google.android.exoplayer2.util.Util.scaleLargeTimestampsInPlace(r5, r3, r7);
        r0 = r1.editListDurations;
        r3 = 0;
        r7 = r0[r3];
        r9 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
        r11 = r1.movieTimescale;
        r7 = com.google.android.exoplayer2.util.Util.scaleLargeTimestamp(r7, r9, r11);
        r9 = new com.google.android.exoplayer2.extractor.mp4.TrackSampleTable;
        r0 = r9;
        r1 = r35;
        r3 = r16;
        r4 = r36;
        r0.<init>(r1, r2, r3, r4, r5, r6, r7);
        return r9;
        r16 = r3;
        r36 = r4;
        r3 = r1.editListDurations;
        r4 = r3.length;
        r7 = 1;
        if (r4 != r7) goto L_0x0369;
        r4 = 0;
        r7 = r3[r4];
        r3 = (r7 > r18 ? 1 : (r7 == r18 ? 0 : -1));
        if (r3 != 0) goto L_0x0369;
        r0 = r1.editListMediaTimes;
        r7 = r0[r4];
        r0 = 0;
        r3 = r5.length;
        if (r0 >= r3) goto L_0x0351;
        r3 = r5[r0];
        r9 = r3 - r7;
        r11 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
        r13 = r1.timescale;
        r3 = com.google.android.exoplayer2.util.Util.scaleLargeTimestamp(r9, r11, r13);
        r5[r0] = r3;
        r0 = r0 + 1;
        goto L_0x033c;
        r9 = r21 - r7;
        r11 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
        r13 = r1.timescale;
        r7 = com.google.android.exoplayer2.util.Util.scaleLargeTimestamp(r9, r11, r13);
        r9 = new com.google.android.exoplayer2.extractor.mp4.TrackSampleTable;
        r0 = r9;
        r1 = r35;
        r3 = r16;
        r4 = r36;
        r0.<init>(r1, r2, r3, r4, r5, r6, r7);
        return r9;
        r3 = r1.type;
        r4 = 1;
        if (r3 != r4) goto L_0x0370;
        r3 = 1;
        goto L_0x0371;
        r3 = 0;
        r4 = r1.editListDurations;
        r7 = r4.length;
        r7 = new int[r7];
        r4 = r4.length;
        r4 = new int[r4];
        r8 = 0;
        r9 = 0;
        r10 = 0;
        r11 = 0;
        r12 = r1.editListDurations;
        r13 = r12.length;
        if (r8 >= r13) goto L_0x03e1;
        r13 = r1.editListMediaTimes;
        r14 = r13[r8];
        r21 = -1;
        r13 = (r14 > r21 ? 1 : (r14 == r21 ? 0 : -1));
        if (r13 == 0) goto L_0x03d3;
        r21 = r12[r8];
        r12 = r1.timescale;
        r37 = r9;
        r27 = r10;
        r9 = r1.movieTimescale;
        r23 = r12;
        r25 = r9;
        r9 = com.google.android.exoplayer2.util.Util.scaleLargeTimestamp(r21, r23, r25);
        r12 = 1;
        r13 = com.google.android.exoplayer2.util.Util.binarySearchCeil(r5, r14, r12, r12);
        r7[r8] = r13;
        r14 = r14 + r9;
        r9 = 0;
        r10 = com.google.android.exoplayer2.util.Util.binarySearchCeil(r5, r14, r3, r9);
        r4[r8] = r10;
        r10 = r7[r8];
        r13 = r4[r8];
        if (r10 >= r13) goto L_0x03c0;
        r10 = r7[r8];
        r10 = r6[r10];
        r10 = r10 & r12;
        if (r10 != 0) goto L_0x03c0;
        r10 = r7[r8];
        r10 = r10 + r12;
        r7[r8] = r10;
        goto L_0x03ad;
        r10 = r4[r8];
        r13 = r7[r8];
        r10 = r10 - r13;
        r10 = r27 + r10;
        r13 = r7[r8];
        if (r11 == r13) goto L_0x03cd;
        r11 = 1;
        goto L_0x03ce;
        r11 = 0;
        r11 = r37 | r11;
        r13 = r4[r8];
        goto L_0x03dc;
        r37 = r9;
        r27 = r10;
        r9 = 0;
        r12 = 1;
        r13 = r11;
        r11 = r37;
        r8 = r8 + 1;
        r9 = r11;
        r11 = r13;
        goto L_0x037d;
        r37 = r9;
        r9 = 0;
        r12 = 1;
        if (r10 == r0) goto L_0x03e8;
        goto L_0x03e9;
        r12 = 0;
        r0 = r37 | r12;
        if (r0 == 0) goto L_0x03f0;
        r3 = new long[r10];
        goto L_0x03f1;
        r3 = r2;
        if (r0 == 0) goto L_0x03f6;
        r8 = new int[r10];
        goto L_0x03f8;
        r8 = r16;
        if (r0 == 0) goto L_0x03fc;
        r11 = 0;
        goto L_0x03fe;
        r11 = r36;
        if (r0 == 0) goto L_0x0403;
        r12 = new int[r10];
        goto L_0x0404;
        r12 = r6;
        r10 = new long[r10];
        r36 = r11;
        r13 = r18;
        r15 = 0;
        r11 = r1.editListDurations;
        r11 = r11.length;
        if (r9 >= r11) goto L_0x0492;
        r11 = r1.editListMediaTimes;
        r23 = r11[r9];
        r11 = r7[r9];
        r25 = r7;
        r7 = r4[r9];
        if (r0 == 0) goto L_0x042e;
        r26 = r4;
        r4 = r7 - r11;
        java.lang.System.arraycopy(r2, r11, r3, r15, r4);
        r27 = r2;
        r2 = r16;
        java.lang.System.arraycopy(r2, r11, r8, r15, r4);
        java.lang.System.arraycopy(r6, r11, r12, r15, r4);
        goto L_0x0434;
        r27 = r2;
        r26 = r4;
        r2 = r16;
        r4 = r36;
        if (r11 >= r7) goto L_0x0474;
        r19 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
        r16 = r6;
        r37 = r7;
        r6 = r1.movieTimescale;
        r17 = r13;
        r21 = r6;
        r6 = com.google.android.exoplayer2.util.Util.scaleLargeTimestamp(r17, r19, r21);
        r17 = r5[r11];
        r28 = r17 - r23;
        r30 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
        r34 = r12;
        r17 = r13;
        r12 = r1.timescale;
        r32 = r12;
        r12 = com.google.android.exoplayer2.util.Util.scaleLargeTimestamp(r28, r30, r32);
        r6 = r6 + r12;
        r10[r15] = r6;
        if (r0 == 0) goto L_0x0467;
        r6 = r8[r15];
        if (r6 <= r4) goto L_0x0467;
        r4 = r2[r11];
        r15 = r15 + 1;
        r11 = r11 + 1;
        r7 = r37;
        r6 = r16;
        r13 = r17;
        r12 = r34;
        goto L_0x0436;
        r16 = r6;
        r34 = r12;
        r17 = r13;
        r6 = r1.editListDurations;
        r11 = r6[r9];
        r13 = r17 + r11;
        r9 = r9 + 1;
        r36 = r4;
        r6 = r16;
        r7 = r25;
        r4 = r26;
        r12 = r34;
        r16 = r2;
        r2 = r27;
        goto L_0x040b;
        r34 = r12;
        r17 = r13;
        r19 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
        r4 = r1.movieTimescale;
        r21 = r4;
        r11 = com.google.android.exoplayer2.util.Util.scaleLargeTimestamp(r17, r19, r21);
        r9 = new com.google.android.exoplayer2.extractor.mp4.TrackSampleTable;
        r0 = r9;
        r1 = r35;
        r2 = r3;
        r3 = r8;
        r4 = r36;
        r5 = r10;
        r6 = r34;
        r7 = r11;
        r0.<init>(r1, r2, r3, r4, r5, r6, r7);
        return r9;
        r27 = r2;
        r2 = r3;
        r36 = r4;
        r16 = r6;
        r3 = r14;
        r9 = r1.timescale;
        com.google.android.exoplayer2.util.Util.scaleLargeTimestampsInPlace(r5, r3, r9);
        r9 = new com.google.android.exoplayer2.extractor.mp4.TrackSampleTable;
        r0 = r9;
        r1 = r35;
        r3 = r2;
        r2 = r27;
        r4 = r36;
        r6 = r16;
        r0.<init>(r1, r2, r3, r4, r5, r6, r7);
        return r9;
        r0 = new com.google.android.exoplayer2.ParserException;
        r1 = "Track has no sample table size information";
        r0.<init>(r1);
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.extractor.mp4.AtomParsers.parseStbl(com.google.android.exoplayer2.extractor.mp4.Track, com.google.android.exoplayer2.extractor.mp4.Atom$ContainerAtom, com.google.android.exoplayer2.extractor.GaplessInfoHolder):com.google.android.exoplayer2.extractor.mp4.TrackSampleTable");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:19:0x0064 in {5, 8, 11, 14, 16, 18} preds:[]
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
    private static void parseTextSampleEntry(com.google.android.exoplayer2.util.ParsableByteArray r19, int r20, int r21, int r22, int r23, java.lang.String r24, com.google.android.exoplayer2.extractor.mp4.AtomParsers.StsdData r25) throws com.google.android.exoplayer2.ParserException {
        /*
        r0 = r19;
        r1 = r20;
        r2 = r25;
        r3 = r21 + 8;
        r3 = r3 + 8;
        r0.setPosition(r3);
        r3 = com.google.android.exoplayer2.extractor.mp4.Atom.TYPE_TTML;
        r4 = "application/ttml+xml";
        r5 = 0;
        r6 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        if (r1 != r3) goto L_0x001f;
        r9 = r4;
        r18 = r5;
        r16 = r6;
        goto L_0x004c;
        r3 = com.google.android.exoplayer2.extractor.mp4.Atom.TYPE_tx3g;
        if (r1 != r3) goto L_0x0034;
        r1 = r22 + -8;
        r1 = r1 + -8;
        r3 = new byte[r1];
        r4 = 0;
        r0.readBytes(r3, r4, r1);
        r5 = java.util.Collections.singletonList(r3);
        r4 = "application/x-quicktime-tx3g";
        goto L_0x0019;
        r0 = com.google.android.exoplayer2.extractor.mp4.Atom.TYPE_wvtt;
        if (r1 != r0) goto L_0x003b;
        r4 = "application/x-mp4-vtt";
        goto L_0x0019;
        r0 = com.google.android.exoplayer2.extractor.mp4.Atom.TYPE_stpp;
        if (r1 != r0) goto L_0x0042;
        r6 = 0;
        goto L_0x0019;
        r0 = com.google.android.exoplayer2.extractor.mp4.Atom.TYPE_c608;
        if (r1 != r0) goto L_0x005e;
        r0 = 1;
        r2.requiredSampleTransformation = r0;
        r4 = "application/x-mp4-cea-608";
        goto L_0x0019;
        r8 = java.lang.Integer.toString(r23);
        r10 = 0;
        r11 = -1;
        r12 = 0;
        r14 = -1;
        r15 = 0;
        r13 = r24;
        r0 = com.google.android.exoplayer2.Format.createTextSampleFormat(r8, r9, r10, r11, r12, r13, r14, r15, r16, r18);
        r2.format = r0;
        return;
        r0 = new java.lang.IllegalStateException;
        r0.<init>();
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.extractor.mp4.AtomParsers.parseTextSampleEntry(com.google.android.exoplayer2.util.ParsableByteArray, int, int, int, int, java.lang.String, com.google.android.exoplayer2.extractor.mp4.AtomParsers$StsdData):void");
    }

    public static Track parseTrak(ContainerAtom containerAtom, LeafAtom leafAtom, long j, DrmInitData drmInitData, boolean z, boolean z2) throws ParserException {
        ContainerAtom containerAtom2 = containerAtom;
        ContainerAtom containerAtomOfType = containerAtom2.getContainerAtomOfType(Atom.TYPE_mdia);
        int trackTypeForHdlr = getTrackTypeForHdlr(parseHdlr(containerAtomOfType.getLeafAtomOfType(Atom.TYPE_hdlr).data));
        if (trackTypeForHdlr == -1) {
            return null;
        }
        long access$000;
        LeafAtom leafAtom2;
        long[] jArr;
        long[] jArr2;
        Track track;
        TkhdData parseTkhd = parseTkhd(containerAtom2.getLeafAtomOfType(Atom.TYPE_tkhd).data);
        long j2 = -9223372036854775807L;
        if (j == -9223372036854775807L) {
            access$000 = parseTkhd.duration;
            leafAtom2 = leafAtom;
        } else {
            leafAtom2 = leafAtom;
            access$000 = j;
        }
        long parseMvhd = parseMvhd(leafAtom2.data);
        if (access$000 != -9223372036854775807L) {
            j2 = Util.scaleLargeTimestamp(access$000, 1000000, parseMvhd);
        }
        long j3 = j2;
        ContainerAtom containerAtomOfType2 = containerAtomOfType.getContainerAtomOfType(Atom.TYPE_minf).getContainerAtomOfType(Atom.TYPE_stbl);
        Pair parseMdhd = parseMdhd(containerAtomOfType.getLeafAtomOfType(Atom.TYPE_mdhd).data);
        StsdData parseStsd = parseStsd(containerAtomOfType2.getLeafAtomOfType(Atom.TYPE_stsd).data, parseTkhd.f18id, parseTkhd.rotationDegrees, (String) parseMdhd.second, drmInitData, z2);
        if (z) {
            jArr = null;
            jArr2 = jArr;
        } else {
            Pair parseEdts = parseEdts(containerAtom2.getContainerAtomOfType(Atom.TYPE_edts));
            jArr2 = (long[]) parseEdts.second;
            jArr = (long[]) parseEdts.first;
        }
        if (parseStsd.format == null) {
            track = null;
        } else {
            int access$100 = parseTkhd.f18id;
            j2 = ((Long) parseMdhd.first).longValue();
            Format format = parseStsd.format;
            int i = parseStsd.requiredSampleTransformation;
            TrackEncryptionBox[] trackEncryptionBoxArr = parseStsd.trackEncryptionBoxes;
            int i2 = parseStsd.nalUnitLengthFieldLength;
            Track track2 = new Track(access$100, trackTypeForHdlr, j2, parseMvhd, j3, format, i, trackEncryptionBoxArr, i2, jArr, jArr2);
        }
        return track;
    }

    public static Metadata parseUdta(LeafAtom leafAtom, boolean z) {
        if (z) {
            return null;
        }
        ParsableByteArray parsableByteArray = leafAtom.data;
        parsableByteArray.setPosition(8);
        while (parsableByteArray.bytesLeft() >= 8) {
            int position = parsableByteArray.getPosition();
            int readInt = parsableByteArray.readInt();
            if (parsableByteArray.readInt() == Atom.TYPE_meta) {
                parsableByteArray.setPosition(position);
                return parseUdtaMeta(parsableByteArray, position + readInt);
            }
            parsableByteArray.setPosition(position + readInt);
        }
        return null;
    }

    public static Metadata parseMdtaFromMeta(ContainerAtom containerAtom) {
        LeafAtom leafAtomOfType = containerAtom.getLeafAtomOfType(Atom.TYPE_hdlr);
        LeafAtom leafAtomOfType2 = containerAtom.getLeafAtomOfType(Atom.TYPE_keys);
        LeafAtom leafAtomOfType3 = containerAtom.getLeafAtomOfType(Atom.TYPE_ilst);
        if (leafAtomOfType == null || leafAtomOfType2 == null || leafAtomOfType3 == null || parseHdlr(leafAtomOfType.data) != TYPE_mdta) {
            return null;
        }
        int i;
        int readInt;
        ParsableByteArray parsableByteArray = leafAtomOfType2.data;
        parsableByteArray.setPosition(12);
        int readInt2 = parsableByteArray.readInt();
        String[] strArr = new String[readInt2];
        for (i = 0; i < readInt2; i++) {
            readInt = parsableByteArray.readInt();
            parsableByteArray.skipBytes(4);
            strArr[i] = parsableByteArray.readString(readInt - 8);
        }
        ParsableByteArray parsableByteArray2 = leafAtomOfType3.data;
        parsableByteArray2.setPosition(8);
        List arrayList = new ArrayList();
        while (parsableByteArray2.bytesLeft() > 8) {
            readInt2 = parsableByteArray2.getPosition();
            i = parsableByteArray2.readInt();
            readInt = parsableByteArray2.readInt() - 1;
            if (readInt < 0 || readInt >= strArr.length) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Skipped metadata with unknown key index: ");
                stringBuilder.append(readInt);
                Log.m18w("AtomParsers", stringBuilder.toString());
            } else {
                MdtaMetadataEntry parseMdtaMetadataEntryFromIlst = MetadataUtil.parseMdtaMetadataEntryFromIlst(parsableByteArray2, readInt2 + i, strArr[readInt]);
                if (parseMdtaMetadataEntryFromIlst != null) {
                    arrayList.add(parseMdtaMetadataEntryFromIlst);
                }
            }
            parsableByteArray2.setPosition(readInt2 + i);
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return new Metadata(arrayList);
    }

    private static Metadata parseUdtaMeta(ParsableByteArray parsableByteArray, int i) {
        parsableByteArray.skipBytes(12);
        while (parsableByteArray.getPosition() < i) {
            int position = parsableByteArray.getPosition();
            int readInt = parsableByteArray.readInt();
            if (parsableByteArray.readInt() == Atom.TYPE_ilst) {
                parsableByteArray.setPosition(position);
                return parseIlst(parsableByteArray, position + readInt);
            }
            parsableByteArray.setPosition(position + readInt);
        }
        return null;
    }

    private static Metadata parseIlst(ParsableByteArray parsableByteArray, int i) {
        parsableByteArray.skipBytes(8);
        List arrayList = new ArrayList();
        while (parsableByteArray.getPosition() < i) {
            Entry parseIlstElement = MetadataUtil.parseIlstElement(parsableByteArray);
            if (parseIlstElement != null) {
                arrayList.add(parseIlstElement);
            }
        }
        return arrayList.isEmpty() ? null : new Metadata(arrayList);
    }

    private static long parseMvhd(ParsableByteArray parsableByteArray) {
        int i = 8;
        parsableByteArray.setPosition(8);
        if (Atom.parseFullAtomVersion(parsableByteArray.readInt()) != 0) {
            i = 16;
        }
        parsableByteArray.skipBytes(i);
        return parsableByteArray.readUnsignedInt();
    }

    private static TkhdData parseTkhd(ParsableByteArray parsableByteArray) {
        Object obj;
        int i = 8;
        parsableByteArray.setPosition(8);
        int parseFullAtomVersion = Atom.parseFullAtomVersion(parsableByteArray.readInt());
        parsableByteArray.skipBytes(parseFullAtomVersion == 0 ? 8 : 16);
        int readInt = parsableByteArray.readInt();
        parsableByteArray.skipBytes(4);
        int position = parsableByteArray.getPosition();
        if (parseFullAtomVersion == 0) {
            i = 4;
        }
        int i2 = 0;
        for (int i3 = 0; i3 < i; i3++) {
            if (parsableByteArray.data[position + i3] != (byte) -1) {
                obj = null;
                break;
            }
        }
        obj = 1;
        long j = -9223372036854775807L;
        if (obj != null) {
            parsableByteArray.skipBytes(i);
        } else {
            long readUnsignedInt = parseFullAtomVersion == 0 ? parsableByteArray.readUnsignedInt() : parsableByteArray.readUnsignedLongToLong();
            if (readUnsignedInt != 0) {
                j = readUnsignedInt;
            }
        }
        parsableByteArray.skipBytes(16);
        i = parsableByteArray.readInt();
        parseFullAtomVersion = parsableByteArray.readInt();
        parsableByteArray.skipBytes(4);
        int readInt2 = parsableByteArray.readInt();
        int readInt3 = parsableByteArray.readInt();
        if (i == 0 && parseFullAtomVersion == MessagesController.UPDATE_MASK_CHECK && readInt2 == -65536 && readInt3 == 0) {
            i2 = 90;
        } else if (i == 0 && parseFullAtomVersion == -65536 && readInt2 == MessagesController.UPDATE_MASK_CHECK && readInt3 == 0) {
            i2 = 270;
        } else if (i == -65536 && parseFullAtomVersion == 0 && readInt2 == 0 && readInt3 == -65536) {
            i2 = 180;
        }
        return new TkhdData(readInt, j, i2);
    }

    private static int parseHdlr(ParsableByteArray parsableByteArray) {
        parsableByteArray.setPosition(16);
        return parsableByteArray.readInt();
    }

    private static int getTrackTypeForHdlr(int i) {
        if (i == TYPE_soun) {
            return 1;
        }
        if (i == TYPE_vide) {
            return 2;
        }
        if (i == TYPE_text || i == TYPE_sbtl || i == TYPE_subt || i == TYPE_clcp) {
            return 3;
        }
        return i == TYPE_meta ? 4 : -1;
    }

    private static Pair<Long, String> parseMdhd(ParsableByteArray parsableByteArray) {
        int i = 8;
        parsableByteArray.setPosition(8);
        int parseFullAtomVersion = Atom.parseFullAtomVersion(parsableByteArray.readInt());
        parsableByteArray.skipBytes(parseFullAtomVersion == 0 ? 8 : 16);
        long readUnsignedInt = parsableByteArray.readUnsignedInt();
        if (parseFullAtomVersion == 0) {
            i = 4;
        }
        parsableByteArray.skipBytes(i);
        int readUnsignedShort = parsableByteArray.readUnsignedShort();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("");
        stringBuilder.append((char) (((readUnsignedShort >> 10) & 31) + 96));
        stringBuilder.append((char) (((readUnsignedShort >> 5) & 31) + 96));
        stringBuilder.append((char) ((readUnsignedShort & 31) + 96));
        return Pair.create(Long.valueOf(readUnsignedInt), stringBuilder.toString());
    }

    private static StsdData parseStsd(ParsableByteArray parsableByteArray, int i, int i2, String str, DrmInitData drmInitData, boolean z) throws ParserException {
        ParsableByteArray parsableByteArray2 = parsableByteArray;
        parsableByteArray2.setPosition(12);
        int readInt = parsableByteArray.readInt();
        StsdData stsdData = new StsdData(readInt);
        for (int i3 = 0; i3 < readInt; i3++) {
            int position = parsableByteArray.getPosition();
            int readInt2 = parsableByteArray.readInt();
            Assertions.checkArgument(readInt2 > 0, "childAtomSize should be positive");
            int readInt3 = parsableByteArray.readInt();
            if (readInt3 == Atom.TYPE_avc1 || readInt3 == Atom.TYPE_avc3 || readInt3 == Atom.TYPE_encv || readInt3 == Atom.TYPE_mp4v || readInt3 == Atom.TYPE_hvc1 || readInt3 == Atom.TYPE_hev1 || readInt3 == Atom.TYPE_s263 || readInt3 == Atom.TYPE_vp08 || readInt3 == Atom.TYPE_vp09) {
                parseVideoSampleEntry(parsableByteArray, readInt3, position, readInt2, i, i2, drmInitData, stsdData, i3);
            } else if (readInt3 == Atom.TYPE_mp4a || readInt3 == Atom.TYPE_enca || readInt3 == Atom.TYPE_ac_3 || readInt3 == Atom.TYPE_ec_3 || readInt3 == Atom.TYPE_dtsc || readInt3 == Atom.TYPE_dtse || readInt3 == Atom.TYPE_dtsh || readInt3 == Atom.TYPE_dtsl || readInt3 == Atom.TYPE_samr || readInt3 == Atom.TYPE_sawb || readInt3 == Atom.TYPE_lpcm || readInt3 == Atom.TYPE_sowt || readInt3 == Atom.TYPE__mp3 || readInt3 == Atom.TYPE_alac || readInt3 == Atom.TYPE_alaw || readInt3 == Atom.TYPE_ulaw || readInt3 == Atom.TYPE_Opus || readInt3 == Atom.TYPE_fLaC) {
                parseAudioSampleEntry(parsableByteArray, readInt3, position, readInt2, i, str, z, drmInitData, stsdData, i3);
            } else if (readInt3 == Atom.TYPE_TTML || readInt3 == Atom.TYPE_tx3g || readInt3 == Atom.TYPE_wvtt || readInt3 == Atom.TYPE_stpp || readInt3 == Atom.TYPE_c608) {
                parseTextSampleEntry(parsableByteArray, readInt3, position, readInt2, i, str, stsdData);
            } else if (readInt3 == Atom.TYPE_camm) {
                stsdData.format = Format.createSampleFormat(Integer.toString(i), MimeTypes.APPLICATION_CAMERA_MOTION, null, -1, null);
            }
            parsableByteArray2.setPosition(position + readInt2);
        }
        return stsdData;
    }

    private static void parseVideoSampleEntry(ParsableByteArray parsableByteArray, int i, int i2, int i3, int i4, int i5, DrmInitData drmInitData, StsdData stsdData, int i6) throws ParserException {
        Pair parseSampleEntryEncryptionData;
        ParsableByteArray parsableByteArray2 = parsableByteArray;
        int i7 = i2;
        int i8 = i3;
        DrmInitData drmInitData2 = drmInitData;
        StsdData stsdData2 = stsdData;
        parsableByteArray2.setPosition((i7 + 8) + 8);
        parsableByteArray2.skipBytes(16);
        int readUnsignedShort = parsableByteArray.readUnsignedShort();
        int readUnsignedShort2 = parsableByteArray.readUnsignedShort();
        parsableByteArray2.skipBytes(50);
        int position = parsableByteArray.getPosition();
        String str = null;
        int i9 = i;
        if (i9 == Atom.TYPE_encv) {
            parseSampleEntryEncryptionData = parseSampleEntryEncryptionData(parsableByteArray2, i7, i8);
            if (parseSampleEntryEncryptionData != null) {
                i9 = ((Integer) parseSampleEntryEncryptionData.first).intValue();
                if (drmInitData2 == null) {
                    drmInitData2 = null;
                } else {
                    drmInitData2 = drmInitData2.copyWithSchemeType(((TrackEncryptionBox) parseSampleEntryEncryptionData.second).schemeType);
                }
                stsdData2.trackEncryptionBoxes[i6] = (TrackEncryptionBox) parseSampleEntryEncryptionData.second;
            }
            parsableByteArray2.setPosition(position);
        }
        DrmInitData drmInitData3 = drmInitData2;
        List list = null;
        byte[] bArr = list;
        Object obj = null;
        float f = 1.0f;
        int i10 = -1;
        while (position - i7 < i8) {
            parsableByteArray2.setPosition(position);
            int position2 = parsableByteArray.getPosition();
            int readInt = parsableByteArray.readInt();
            if (readInt == 0 && parsableByteArray.getPosition() - i7 == i8) {
                break;
            }
            Assertions.checkArgument(readInt > 0, "childAtomSize should be positive");
            int readInt2 = parsableByteArray.readInt();
            if (readInt2 == Atom.TYPE_avcC) {
                Assertions.checkState(str == null);
                parsableByteArray2.setPosition(position2 + 8);
                AvcConfig parse = AvcConfig.parse(parsableByteArray);
                list = parse.initializationData;
                stsdData2.nalUnitLengthFieldLength = parse.nalUnitLengthFieldLength;
                if (obj == null) {
                    f = parse.pixelWidthAspectRatio;
                }
                str = "video/avc";
            } else if (readInt2 == Atom.TYPE_hvcC) {
                Assertions.checkState(str == null);
                parsableByteArray2.setPosition(position2 + 8);
                HevcConfig parse2 = HevcConfig.parse(parsableByteArray);
                list = parse2.initializationData;
                stsdData2.nalUnitLengthFieldLength = parse2.nalUnitLengthFieldLength;
                str = MimeTypes.VIDEO_H265;
            } else if (readInt2 == Atom.TYPE_vpcC) {
                Assertions.checkState(str == null);
                str = i9 == Atom.TYPE_vp08 ? MimeTypes.VIDEO_VP8 : MimeTypes.VIDEO_VP9;
            } else if (readInt2 == Atom.TYPE_d263) {
                Assertions.checkState(str == null);
                str = MimeTypes.VIDEO_H263;
            } else if (readInt2 == Atom.TYPE_esds) {
                Assertions.checkState(str == null);
                parseSampleEntryEncryptionData = parseEsdsFromParent(parsableByteArray2, position2);
                str = (String) parseSampleEntryEncryptionData.first;
                list = Collections.singletonList(parseSampleEntryEncryptionData.second);
            } else if (readInt2 == Atom.TYPE_pasp) {
                f = parsePaspFromParent(parsableByteArray2, position2);
                obj = 1;
            } else if (readInt2 == Atom.TYPE_sv3d) {
                bArr = parseProjFromParent(parsableByteArray2, position2, readInt);
            } else if (readInt2 == Atom.TYPE_st3d) {
                readInt2 = parsableByteArray.readUnsignedByte();
                parsableByteArray2.skipBytes(3);
                if (readInt2 == 0) {
                    readInt2 = parsableByteArray.readUnsignedByte();
                    if (readInt2 == 0) {
                        i10 = 0;
                    } else if (readInt2 == 1) {
                        i10 = 1;
                    } else if (readInt2 == 2) {
                        i10 = 2;
                    } else if (readInt2 == 3) {
                        i10 = 3;
                    }
                }
            }
            position += readInt;
        }
        if (str != null) {
            stsdData2.format = Format.createVideoSampleFormat(Integer.toString(i4), str, null, -1, -1, readUnsignedShort, readUnsignedShort2, -1.0f, list, i5, f, bArr, i10, null, drmInitData3);
        }
    }

    private static Pair<long[], long[]> parseEdts(ContainerAtom containerAtom) {
        if (containerAtom != null) {
            LeafAtom leafAtomOfType = containerAtom.getLeafAtomOfType(Atom.TYPE_elst);
            if (leafAtomOfType != null) {
                ParsableByteArray parsableByteArray = leafAtomOfType.data;
                parsableByteArray.setPosition(8);
                int parseFullAtomVersion = Atom.parseFullAtomVersion(parsableByteArray.readInt());
                int readUnsignedIntToInt = parsableByteArray.readUnsignedIntToInt();
                long[] jArr = new long[readUnsignedIntToInt];
                long[] jArr2 = new long[readUnsignedIntToInt];
                int i = 0;
                while (i < readUnsignedIntToInt) {
                    jArr[i] = parseFullAtomVersion == 1 ? parsableByteArray.readUnsignedLongToLong() : parsableByteArray.readUnsignedInt();
                    jArr2[i] = parseFullAtomVersion == 1 ? parsableByteArray.readLong() : (long) parsableByteArray.readInt();
                    if (parsableByteArray.readShort() == (short) 1) {
                        parsableByteArray.skipBytes(2);
                        i++;
                    } else {
                        throw new IllegalArgumentException("Unsupported media rate.");
                    }
                }
                return Pair.create(jArr, jArr2);
            }
        }
        return Pair.create(null, null);
    }

    private static float parsePaspFromParent(ParsableByteArray parsableByteArray, int i) {
        parsableByteArray.setPosition(i + 8);
        return ((float) parsableByteArray.readUnsignedIntToInt()) / ((float) parsableByteArray.readUnsignedIntToInt());
    }

    private static void parseAudioSampleEntry(ParsableByteArray parsableByteArray, int i, int i2, int i3, int i4, String str, boolean z, DrmInitData drmInitData, StsdData stsdData, int i5) throws ParserException {
        int readUnsignedShort;
        int readUnsignedShort2;
        int readUnsignedFixedPoint1616;
        String str2;
        String str3;
        DrmInitData drmInitData2;
        ParsableByteArray parsableByteArray2 = parsableByteArray;
        int i6 = i2;
        int i7 = i3;
        String str4 = str;
        DrmInitData drmInitData3 = drmInitData;
        StsdData stsdData2 = stsdData;
        parsableByteArray2.setPosition((i6 + 8) + 8);
        if (z) {
            readUnsignedShort = parsableByteArray.readUnsignedShort();
            parsableByteArray2.skipBytes(6);
        } else {
            parsableByteArray2.skipBytes(8);
            readUnsignedShort = 0;
        }
        if (readUnsignedShort == 0 || readUnsignedShort == 1) {
            readUnsignedShort2 = parsableByteArray.readUnsignedShort();
            parsableByteArray2.skipBytes(6);
            readUnsignedFixedPoint1616 = parsableByteArray.readUnsignedFixedPoint1616();
            if (readUnsignedShort == 1) {
                parsableByteArray2.skipBytes(16);
            }
        } else if (readUnsignedShort == 2) {
            parsableByteArray2.skipBytes(16);
            readUnsignedShort = (int) Math.round(parsableByteArray.readDouble());
            readUnsignedFixedPoint1616 = parsableByteArray.readUnsignedIntToInt();
            parsableByteArray2.skipBytes(20);
            readUnsignedShort2 = readUnsignedFixedPoint1616;
            readUnsignedFixedPoint1616 = readUnsignedShort;
        } else {
            return;
        }
        readUnsignedShort = parsableByteArray.getPosition();
        int i8 = i;
        if (i8 == Atom.TYPE_enca) {
            Pair parseSampleEntryEncryptionData = parseSampleEntryEncryptionData(parsableByteArray2, i6, i7);
            if (parseSampleEntryEncryptionData != null) {
                i8 = ((Integer) parseSampleEntryEncryptionData.first).intValue();
                if (drmInitData3 == null) {
                    drmInitData3 = null;
                } else {
                    drmInitData3 = drmInitData3.copyWithSchemeType(((TrackEncryptionBox) parseSampleEntryEncryptionData.second).schemeType);
                }
                stsdData2.trackEncryptionBoxes[i5] = (TrackEncryptionBox) parseSampleEntryEncryptionData.second;
            }
            parsableByteArray2.setPosition(readUnsignedShort);
        }
        DrmInitData drmInitData4 = drmInitData3;
        int i9 = Atom.TYPE_ac_3;
        String str5 = MimeTypes.AUDIO_RAW;
        String str6 = i8 == i9 ? MimeTypes.AUDIO_AC3 : i8 == Atom.TYPE_ec_3 ? MimeTypes.AUDIO_E_AC3 : i8 == Atom.TYPE_dtsc ? MimeTypes.AUDIO_DTS : (i8 == Atom.TYPE_dtsh || i8 == Atom.TYPE_dtsl) ? MimeTypes.AUDIO_DTS_HD : i8 == Atom.TYPE_dtse ? MimeTypes.AUDIO_DTS_EXPRESS : i8 == Atom.TYPE_samr ? MimeTypes.AUDIO_AMR_NB : i8 == Atom.TYPE_sawb ? MimeTypes.AUDIO_AMR_WB : (i8 == Atom.TYPE_lpcm || i8 == Atom.TYPE_sowt) ? str5 : i8 == Atom.TYPE__mp3 ? MimeTypes.AUDIO_MPEG : i8 == Atom.TYPE_alac ? MimeTypes.AUDIO_ALAC : i8 == Atom.TYPE_alaw ? MimeTypes.AUDIO_ALAW : i8 == Atom.TYPE_ulaw ? MimeTypes.AUDIO_MLAW : i8 == Atom.TYPE_Opus ? MimeTypes.AUDIO_OPUS : i8 == Atom.TYPE_fLaC ? MimeTypes.AUDIO_FLAC : null;
        int i10 = readUnsignedFixedPoint1616;
        i8 = readUnsignedShort;
        int i11 = readUnsignedShort2;
        Object obj = null;
        String str7 = str6;
        while (i8 - i6 < i7) {
            parsableByteArray2.setPosition(i8);
            int readInt = parsableByteArray.readInt();
            Assertions.checkArgument(readInt > 0, "childAtomSize should be positive");
            i9 = parsableByteArray.readInt();
            if (i9 == Atom.TYPE_esds || (z && i9 == Atom.TYPE_wave)) {
                readUnsignedShort = readInt;
                str2 = str7;
                readInt = i8;
                str3 = str5;
                drmInitData2 = drmInitData4;
                if (i9 == Atom.TYPE_esds) {
                    i8 = readInt;
                } else {
                    i8 = findEsdsPosition(parsableByteArray2, readInt, readUnsignedShort);
                }
                if (i8 != -1) {
                    Pair parseEsdsFromParent = parseEsdsFromParent(parsableByteArray2, i8);
                    str7 = (String) parseEsdsFromParent.first;
                    obj = (byte[]) parseEsdsFromParent.second;
                    if (MimeTypes.AUDIO_AAC.equals(str7)) {
                        parseEsdsFromParent = CodecSpecificDataUtil.parseAacAudioSpecificConfig(obj);
                        i10 = ((Integer) parseEsdsFromParent.first).intValue();
                        i11 = ((Integer) parseEsdsFromParent.second).intValue();
                    }
                    i8 = readInt + readUnsignedShort;
                    i6 = i2;
                    drmInitData4 = drmInitData2;
                    str5 = str3;
                }
            } else {
                int i12;
                int i13;
                if (i9 == Atom.TYPE_dac3) {
                    parsableByteArray2.setPosition(i8 + 8);
                    stsdData2.format = Ac3Util.parseAc3AnnexFFormat(parsableByteArray2, Integer.toString(i4), str4, drmInitData4);
                } else if (i9 == Atom.TYPE_dec3) {
                    parsableByteArray2.setPosition(i8 + 8);
                    stsdData2.format = Ac3Util.parseEAc3AnnexFFormat(parsableByteArray2, Integer.toString(i4), str4, drmInitData4);
                } else if (i9 == Atom.TYPE_ddts) {
                    i12 = readInt;
                    str2 = str7;
                    i13 = i8;
                    str3 = str5;
                    drmInitData2 = drmInitData4;
                    stsdData2.format = Format.createAudioSampleFormat(Integer.toString(i4), str7, null, -1, -1, i11, i10, null, drmInitData2, 0, str);
                    readUnsignedShort = i12;
                    readInt = i13;
                } else {
                    i12 = readInt;
                    str2 = str7;
                    i13 = i8;
                    str3 = str5;
                    drmInitData2 = drmInitData4;
                    if (i9 == Atom.TYPE_alac) {
                        readUnsignedShort = i12;
                        Object obj2 = new byte[readUnsignedShort];
                        readInt = i13;
                        parsableByteArray2.setPosition(readInt);
                        parsableByteArray2.readBytes(obj2, 0, readUnsignedShort);
                        obj = obj2;
                    } else {
                        readUnsignedShort = i12;
                        readInt = i13;
                        if (i9 == Atom.TYPE_dOps) {
                            i9 = readUnsignedShort - 8;
                            byte[] bArr = opusMagic;
                            Object obj3 = new byte[(bArr.length + i9)];
                            System.arraycopy(bArr, 0, obj3, 0, bArr.length);
                            parsableByteArray2.setPosition(readInt + 8);
                            parsableByteArray2.readBytes(obj3, opusMagic.length, i9);
                            obj = obj3;
                        } else if (readUnsignedShort == Atom.TYPE_dfLa) {
                            i9 = readUnsignedShort - 12;
                            Object obj4 = new byte[i9];
                            parsableByteArray2.setPosition(readInt + 12);
                            parsableByteArray2.readBytes(obj4, 0, i9);
                            obj = obj4;
                        }
                    }
                }
                readUnsignedShort = readInt;
                str2 = str7;
                readInt = i8;
                str3 = str5;
                drmInitData2 = drmInitData4;
            }
            str7 = str2;
            i8 = readInt + readUnsignedShort;
            i6 = i2;
            drmInitData4 = drmInitData2;
            str5 = str3;
        }
        str2 = str7;
        str3 = str5;
        drmInitData2 = drmInitData4;
        int i14 = 2;
        if (stsdData2.format == null) {
            str7 = str2;
            if (str7 != null) {
                List list;
                if (!str3.equals(str7)) {
                    i14 = -1;
                }
                String num = Integer.toString(i4);
                if (obj == null) {
                    list = null;
                } else {
                    list = Collections.singletonList(obj);
                }
                stsdData2.format = Format.createAudioSampleFormat(num, str7, null, -1, -1, i11, i10, i14, list, drmInitData2, 0, str);
            }
        }
    }

    private static int findEsdsPosition(ParsableByteArray parsableByteArray, int i, int i2) {
        int position = parsableByteArray.getPosition();
        while (position - i < i2) {
            parsableByteArray.setPosition(position);
            int readInt = parsableByteArray.readInt();
            Assertions.checkArgument(readInt > 0, "childAtomSize should be positive");
            if (parsableByteArray.readInt() == Atom.TYPE_esds) {
                return position;
            }
            position += readInt;
        }
        return -1;
    }

    private static Pair<String, byte[]> parseEsdsFromParent(ParsableByteArray parsableByteArray, int i) {
        parsableByteArray.setPosition((i + 8) + 4);
        parsableByteArray.skipBytes(1);
        parseExpandableClassSize(parsableByteArray);
        parsableByteArray.skipBytes(2);
        int readUnsignedByte = parsableByteArray.readUnsignedByte();
        if ((readUnsignedByte & 128) != 0) {
            parsableByteArray.skipBytes(2);
        }
        if ((readUnsignedByte & 64) != 0) {
            parsableByteArray.skipBytes(parsableByteArray.readUnsignedShort());
        }
        if ((readUnsignedByte & 32) != 0) {
            parsableByteArray.skipBytes(2);
        }
        parsableByteArray.skipBytes(1);
        parseExpandableClassSize(parsableByteArray);
        String mimeTypeFromMp4ObjectType = MimeTypes.getMimeTypeFromMp4ObjectType(parsableByteArray.readUnsignedByte());
        if (MimeTypes.AUDIO_MPEG.equals(mimeTypeFromMp4ObjectType) || MimeTypes.AUDIO_DTS.equals(mimeTypeFromMp4ObjectType) || MimeTypes.AUDIO_DTS_HD.equals(mimeTypeFromMp4ObjectType)) {
            return Pair.create(mimeTypeFromMp4ObjectType, null);
        }
        parsableByteArray.skipBytes(12);
        parsableByteArray.skipBytes(1);
        i = parseExpandableClassSize(parsableByteArray);
        byte[] bArr = new byte[i];
        parsableByteArray.readBytes(bArr, 0, i);
        return Pair.create(mimeTypeFromMp4ObjectType, bArr);
    }

    private static Pair<Integer, TrackEncryptionBox> parseSampleEntryEncryptionData(ParsableByteArray parsableByteArray, int i, int i2) {
        int position = parsableByteArray.getPosition();
        while (position - i < i2) {
            parsableByteArray.setPosition(position);
            int readInt = parsableByteArray.readInt();
            Assertions.checkArgument(readInt > 0, "childAtomSize should be positive");
            if (parsableByteArray.readInt() == Atom.TYPE_sinf) {
                Pair parseCommonEncryptionSinfFromParent = parseCommonEncryptionSinfFromParent(parsableByteArray, position, readInt);
                if (parseCommonEncryptionSinfFromParent != null) {
                    return parseCommonEncryptionSinfFromParent;
                }
            }
            position += readInt;
        }
        return null;
    }

    static Pair<Integer, TrackEncryptionBox> parseCommonEncryptionSinfFromParent(ParsableByteArray parsableByteArray, int i, int i2) {
        int i3 = i + 8;
        Object obj = null;
        Object obj2 = obj;
        int i4 = -1;
        int i5 = 0;
        while (i3 - i < i2) {
            parsableByteArray.setPosition(i3);
            int readInt = parsableByteArray.readInt();
            int readInt2 = parsableByteArray.readInt();
            if (readInt2 == Atom.TYPE_frma) {
                obj2 = Integer.valueOf(parsableByteArray.readInt());
            } else if (readInt2 == Atom.TYPE_schm) {
                parsableByteArray.skipBytes(4);
                obj = parsableByteArray.readString(4);
            } else if (readInt2 == Atom.TYPE_schi) {
                i4 = i3;
                i5 = readInt;
            }
            i3 += readInt;
        }
        if (!"cenc".equals(obj) && !"cbc1".equals(obj) && !"cens".equals(obj) && !"cbcs".equals(obj)) {
            return null;
        }
        boolean z = true;
        Assertions.checkArgument(obj2 != null, "frma atom is mandatory");
        Assertions.checkArgument(i4 != -1, "schi atom is mandatory");
        TrackEncryptionBox parseSchiFromParent = parseSchiFromParent(parsableByteArray, i4, i5, obj);
        if (parseSchiFromParent == null) {
            z = false;
        }
        Assertions.checkArgument(z, "tenc atom is mandatory");
        return Pair.create(obj2, parseSchiFromParent);
    }

    private static TrackEncryptionBox parseSchiFromParent(ParsableByteArray parsableByteArray, int i, int i2, String str) {
        int i3 = i + 8;
        while (true) {
            TrackEncryptionBox trackEncryptionBox = null;
            if (i3 - i >= i2) {
                return null;
            }
            parsableByteArray.setPosition(i3);
            int readInt = parsableByteArray.readInt();
            if (parsableByteArray.readInt() == Atom.TYPE_tenc) {
                int i4;
                int i5;
                i = Atom.parseFullAtomVersion(parsableByteArray.readInt());
                parsableByteArray.skipBytes(1);
                if (i == 0) {
                    parsableByteArray.skipBytes(1);
                    i4 = 0;
                    i5 = 0;
                } else {
                    i = parsableByteArray.readUnsignedByte();
                    i5 = i & 15;
                    i4 = (i & 240) >> 4;
                }
                boolean z = parsableByteArray.readUnsignedByte() == 1;
                int readUnsignedByte = parsableByteArray.readUnsignedByte();
                byte[] bArr = new byte[16];
                parsableByteArray.readBytes(bArr, 0, bArr.length);
                if (z && readUnsignedByte == 0) {
                    i = parsableByteArray.readUnsignedByte();
                    trackEncryptionBox = new byte[i];
                    parsableByteArray.readBytes(trackEncryptionBox, 0, i);
                }
                return new TrackEncryptionBox(z, str, readUnsignedByte, bArr, i4, i5, trackEncryptionBox);
            }
            i3 += readInt;
        }
    }

    private static byte[] parseProjFromParent(ParsableByteArray parsableByteArray, int i, int i2) {
        int i3 = i + 8;
        while (i3 - i < i2) {
            parsableByteArray.setPosition(i3);
            int readInt = parsableByteArray.readInt();
            if (parsableByteArray.readInt() == Atom.TYPE_proj) {
                return Arrays.copyOfRange(parsableByteArray.data, i3, readInt + i3);
            }
            i3 += readInt;
        }
        return null;
    }

    private static int parseExpandableClassSize(ParsableByteArray parsableByteArray) {
        int readUnsignedByte = parsableByteArray.readUnsignedByte();
        int i = readUnsignedByte & 127;
        while ((readUnsignedByte & 128) == 128) {
            readUnsignedByte = parsableByteArray.readUnsignedByte();
            i = (i << 7) | (readUnsignedByte & 127);
        }
        return i;
    }

    private static boolean canApplyEditWithGaplessInfo(long[] jArr, long j, long j2, long j3) {
        int length = jArr.length - 1;
        int constrainValue = Util.constrainValue(3, 0, length);
        length = Util.constrainValue(jArr.length - 3, 0, length);
        if (jArr[0] > j2 || j2 >= jArr[constrainValue] || jArr[length] >= j3 || j3 > j) {
            return false;
        }
        return true;
    }
}
