package com.google.android.exoplayer2.extractor.ogg;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.Arrays;

final class VorbisUtil {

    public static final class CodeBook {
        public final int dimensions;
        public final int entries;
        public final boolean isOrdered;
        public final long[] lengthMap;
        public final int lookupType;

        public CodeBook(int i, int i2, long[] jArr, int i3, boolean z) {
            this.dimensions = i;
            this.entries = i2;
            this.lengthMap = jArr;
            this.lookupType = i3;
            this.isOrdered = z;
        }
    }

    public static final class CommentHeader {
        public final String[] comments;
        public final int length;
        public final String vendor;

        public CommentHeader(String str, String[] strArr, int i) {
            this.vendor = str;
            this.comments = strArr;
            this.length = i;
        }
    }

    public static final class Mode {
        public final boolean blockFlag;
        public final int mapping;
        public final int transformType;
        public final int windowType;

        public Mode(boolean z, int i, int i2, int i3) {
            this.blockFlag = z;
            this.windowType = i;
            this.transformType = i2;
            this.mapping = i3;
        }
    }

    public static final class VorbisIdHeader {
        public final int bitrateMax;
        public final int bitrateMin;
        public final int bitrateNominal;
        public final int blockSize0;
        public final int blockSize1;
        public final int channels;
        public final byte[] data;
        public final boolean framingFlag;
        public final long sampleRate;
        public final long version;

        public VorbisIdHeader(long j, int i, long j2, int i2, int i3, int i4, int i5, int i6, boolean z, byte[] bArr) {
            this.version = j;
            this.channels = i;
            this.sampleRate = j2;
            this.bitrateMax = i2;
            this.bitrateNominal = i3;
            this.bitrateMin = i4;
            this.blockSize0 = i5;
            this.blockSize1 = i6;
            this.framingFlag = z;
            this.data = bArr;
        }
    }

    public static int iLog(int i) {
        int i2 = 0;
        while (i > 0) {
            i2++;
            i >>>= 1;
        }
        return i2;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:39:0x00da in {10, 11, 12, 13, 21, 22, 26, 30, 31, 32, 34, 36, 38} preds:[]
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
    private static com.google.android.exoplayer2.extractor.ogg.VorbisUtil.CodeBook readBook(com.google.android.exoplayer2.extractor.ogg.VorbisBitArray r14) throws com.google.android.exoplayer2.ParserException {
        /*
        r0 = 24;
        r1 = r14.readBits(r0);
        r2 = 5653314; // 0x564342 float:7.92198E-39 double:2.793108E-317;
        if (r1 != r2) goto L_0x00bf;
        r1 = 16;
        r3 = r14.readBits(r1);
        r4 = r14.readBits(r0);
        r5 = new long[r4];
        r7 = r14.readBit();
        r0 = 0;
        r2 = 0;
        r6 = 5;
        r8 = 1;
        if (r7 != 0) goto L_0x0048;
        r9 = r14.readBit();
        r10 = r5.length;
        if (r2 >= r10) goto L_0x006f;
        if (r9 == 0) goto L_0x003d;
        r10 = r14.readBit();
        if (r10 == 0) goto L_0x003a;
        r10 = r14.readBits(r6);
        r10 = r10 + r8;
        r10 = (long) r10;
        r5[r2] = r10;
        goto L_0x0045;
        r5[r2] = r0;
        goto L_0x0045;
        r10 = r14.readBits(r6);
        r10 = r10 + r8;
        r10 = (long) r10;
        r5[r2] = r10;
        r2 = r2 + 1;
        goto L_0x0026;
        r6 = r14.readBits(r6);
        r6 = r6 + r8;
        r9 = r6;
        r6 = 0;
        r10 = r5.length;
        if (r6 >= r10) goto L_0x006f;
        r10 = r4 - r6;
        r10 = iLog(r10);
        r10 = r14.readBits(r10);
        r11 = r6;
        r6 = 0;
        if (r6 >= r10) goto L_0x006b;
        r12 = r5.length;
        if (r11 >= r12) goto L_0x006b;
        r12 = (long) r9;
        r5[r11] = r12;
        r11 = r11 + 1;
        r6 = r6 + 1;
        goto L_0x005e;
        r9 = r9 + 1;
        r6 = r11;
        goto L_0x004f;
        r2 = 4;
        r6 = r14.readBits(r2);
        r9 = 2;
        if (r6 > r9) goto L_0x00a8;
        if (r6 == r8) goto L_0x007b;
        if (r6 != r9) goto L_0x00a1;
        r9 = 32;
        r14.skipBits(r9);
        r14.skipBits(r9);
        r2 = r14.readBits(r2);
        r2 = r2 + r8;
        r14.skipBits(r8);
        if (r6 != r8) goto L_0x0096;
        if (r3 == 0) goto L_0x009a;
        r0 = (long) r4;
        r8 = (long) r3;
        r0 = mapType1QuantValues(r0, r8);
        goto L_0x009a;
        r0 = (long) r4;
        r8 = (long) r3;
        r0 = r0 * r8;
        r8 = (long) r2;
        r0 = r0 * r8;
        r1 = (int) r0;
        r14.skipBits(r1);
        r14 = new com.google.android.exoplayer2.extractor.ogg.VorbisUtil$CodeBook;
        r2 = r14;
        r2.<init>(r3, r4, r5, r6, r7);
        return r14;
        r14 = new com.google.android.exoplayer2.ParserException;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "lookup type greater than 2 not decodable: ";
        r0.append(r1);
        r0.append(r6);
        r0 = r0.toString();
        r14.<init>(r0);
        throw r14;
        r0 = new com.google.android.exoplayer2.ParserException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "expected code book to start with [0x56, 0x43, 0x42] at ";
        r1.append(r2);
        r14 = r14.getPosition();
        r1.append(r14);
        r14 = r1.toString();
        r0.<init>(r14);
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.extractor.ogg.VorbisUtil.readBook(com.google.android.exoplayer2.extractor.ogg.VorbisBitArray):com.google.android.exoplayer2.extractor.ogg.VorbisUtil$CodeBook");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:10:0x0052 in {3, 7, 9} preds:[]
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
    public static com.google.android.exoplayer2.extractor.ogg.VorbisUtil.CommentHeader readVorbisCommentHeader(com.google.android.exoplayer2.util.ParsableByteArray r9) throws com.google.android.exoplayer2.ParserException {
        /*
        r0 = 0;
        r1 = 3;
        verifyVorbisHeaderCapturePattern(r1, r9, r0);
        r1 = r9.readLittleEndianUnsignedInt();
        r2 = (int) r1;
        r1 = r9.readString(r2);
        r2 = r1.length();
        r3 = 11;
        r3 = r3 + r2;
        r4 = r9.readLittleEndianUnsignedInt();
        r2 = (int) r4;
        r2 = new java.lang.String[r2];
        r3 = r3 + 4;
        r6 = (long) r0;
        r8 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1));
        if (r8 >= 0) goto L_0x003a;
        r6 = r9.readLittleEndianUnsignedInt();
        r7 = (int) r6;
        r3 = r3 + 4;
        r6 = r9.readString(r7);
        r2[r0] = r6;
        r6 = r2[r0];
        r6 = r6.length();
        r3 = r3 + r6;
        r0 = r0 + 1;
        goto L_0x001e;
        r9 = r9.readUnsignedByte();
        r9 = r9 & 1;
        if (r9 == 0) goto L_0x004a;
        r3 = r3 + 1;
        r9 = new com.google.android.exoplayer2.extractor.ogg.VorbisUtil$CommentHeader;
        r9.<init>(r1, r2, r3);
        return r9;
        r9 = new com.google.android.exoplayer2.ParserException;
        r0 = "framing bit expected to be set";
        r9.<init>(r0);
        throw r9;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.extractor.ogg.VorbisUtil.readVorbisCommentHeader(com.google.android.exoplayer2.util.ParsableByteArray):com.google.android.exoplayer2.extractor.ogg.VorbisUtil$CommentHeader");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:15:0x005c in {2, 7, 9, 12, 14} preds:[]
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
    public static com.google.android.exoplayer2.extractor.ogg.VorbisUtil.Mode[] readVorbisModes(com.google.android.exoplayer2.util.ParsableByteArray r4, int r5) throws com.google.android.exoplayer2.ParserException {
        /*
        r0 = 0;
        r1 = 5;
        verifyVorbisHeaderCapturePattern(r1, r4, r0);
        r1 = r4.readUnsignedByte();
        r1 = r1 + 1;
        r2 = new com.google.android.exoplayer2.extractor.ogg.VorbisBitArray;
        r3 = r4.data;
        r2.<init>(r3);
        r4 = r4.getPosition();
        r4 = r4 * 8;
        r2.skipBits(r4);
        r4 = 0;
        if (r4 >= r1) goto L_0x0024;
        readBook(r2);
        r4 = r4 + 1;
        goto L_0x001c;
        r4 = 6;
        r4 = r2.readBits(r4);
        r4 = r4 + 1;
        if (r0 >= r4) goto L_0x0040;
        r1 = 16;
        r1 = r2.readBits(r1);
        if (r1 != 0) goto L_0x0038;
        r0 = r0 + 1;
        goto L_0x002b;
        r4 = new com.google.android.exoplayer2.ParserException;
        r5 = "placeholder of time domain transforms not zeroed out";
        r4.<init>(r5);
        throw r4;
        readFloors(r2);
        readResidues(r2);
        readMappings(r5, r2);
        r4 = readModes(r2);
        r5 = r2.readBit();
        if (r5 == 0) goto L_0x0054;
        return r4;
        r4 = new com.google.android.exoplayer2.ParserException;
        r5 = "framing bit after modes not set as expected";
        r4.<init>(r5);
        throw r4;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.extractor.ogg.VorbisUtil.readVorbisModes(com.google.android.exoplayer2.util.ParsableByteArray, int):com.google.android.exoplayer2.extractor.ogg.VorbisUtil$Mode[]");
    }

    public static VorbisIdHeader readVorbisIdentificationHeader(ParsableByteArray parsableByteArray) throws ParserException {
        ParsableByteArray parsableByteArray2 = parsableByteArray;
        verifyVorbisHeaderCapturePattern(1, parsableByteArray2, false);
        long readLittleEndianUnsignedInt = parsableByteArray.readLittleEndianUnsignedInt();
        int readUnsignedByte = parsableByteArray.readUnsignedByte();
        long readLittleEndianUnsignedInt2 = parsableByteArray.readLittleEndianUnsignedInt();
        int readLittleEndianInt = parsableByteArray.readLittleEndianInt();
        int readLittleEndianInt2 = parsableByteArray.readLittleEndianInt();
        int readLittleEndianInt3 = parsableByteArray.readLittleEndianInt();
        int readUnsignedByte2 = parsableByteArray.readUnsignedByte();
        return new VorbisIdHeader(readLittleEndianUnsignedInt, readUnsignedByte, readLittleEndianUnsignedInt2, readLittleEndianInt, readLittleEndianInt2, readLittleEndianInt3, (int) Math.pow(2.0d, (double) (readUnsignedByte2 & 15)), (int) Math.pow(2.0d, (double) ((readUnsignedByte2 & 240) >> 4)), (parsableByteArray.readUnsignedByte() & 1) > 0, Arrays.copyOf(parsableByteArray2.data, parsableByteArray.limit()));
    }

    public static boolean verifyVorbisHeaderCapturePattern(int i, ParsableByteArray parsableByteArray, boolean z) throws ParserException {
        StringBuilder stringBuilder;
        if (parsableByteArray.bytesLeft() < 7) {
            if (z) {
                return false;
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("too short header: ");
            stringBuilder.append(parsableByteArray.bytesLeft());
            throw new ParserException(stringBuilder.toString());
        } else if (parsableByteArray.readUnsignedByte() != i) {
            if (z) {
                return false;
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("expected header type ");
            stringBuilder.append(Integer.toHexString(i));
            throw new ParserException(stringBuilder.toString());
        } else if (parsableByteArray.readUnsignedByte() == 118 && parsableByteArray.readUnsignedByte() == 111 && parsableByteArray.readUnsignedByte() == 114 && parsableByteArray.readUnsignedByte() == 98 && parsableByteArray.readUnsignedByte() == 105 && parsableByteArray.readUnsignedByte() == 115) {
            return true;
        } else {
            if (z) {
                return false;
            }
            throw new ParserException("expected characters 'vorbis'");
        }
    }

    private static Mode[] readModes(VorbisBitArray vorbisBitArray) {
        int readBits = vorbisBitArray.readBits(6) + 1;
        Mode[] modeArr = new Mode[readBits];
        for (int i = 0; i < readBits; i++) {
            modeArr[i] = new Mode(vorbisBitArray.readBit(), vorbisBitArray.readBits(16), vorbisBitArray.readBits(16), vorbisBitArray.readBits(8));
        }
        return modeArr;
    }

    private static void readMappings(int i, VorbisBitArray vorbisBitArray) throws ParserException {
        int readBits = vorbisBitArray.readBits(6) + 1;
        for (int i2 = 0; i2 < readBits; i2++) {
            int readBits2 = vorbisBitArray.readBits(16);
            if (readBits2 != 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("mapping type other than 0 not supported: ");
                stringBuilder.append(readBits2);
                Log.m14e("VorbisUtil", stringBuilder.toString());
            } else {
                int readBits3;
                readBits2 = vorbisBitArray.readBit() ? vorbisBitArray.readBits(4) + 1 : 1;
                if (vorbisBitArray.readBit()) {
                    readBits3 = vorbisBitArray.readBits(8) + 1;
                    for (int i3 = 0; i3 < readBits3; i3++) {
                        int i4 = i - 1;
                        vorbisBitArray.skipBits(iLog(i4));
                        vorbisBitArray.skipBits(iLog(i4));
                    }
                }
                if (vorbisBitArray.readBits(2) == 0) {
                    if (readBits2 > 1) {
                        for (readBits3 = 0; readBits3 < i; readBits3++) {
                            vorbisBitArray.skipBits(4);
                        }
                    }
                    for (int i5 = 0; i5 < readBits2; i5++) {
                        vorbisBitArray.skipBits(8);
                        vorbisBitArray.skipBits(8);
                        vorbisBitArray.skipBits(8);
                    }
                } else {
                    throw new ParserException("to reserved bits must be zero after mapping coupling steps");
                }
            }
        }
    }

    private static void readResidues(VorbisBitArray vorbisBitArray) throws ParserException {
        int readBits = vorbisBitArray.readBits(6) + 1;
        int i = 0;
        while (i < readBits) {
            if (vorbisBitArray.readBits(16) <= 2) {
                int i2;
                vorbisBitArray.skipBits(24);
                vorbisBitArray.skipBits(24);
                vorbisBitArray.skipBits(24);
                int readBits2 = vorbisBitArray.readBits(6) + 1;
                vorbisBitArray.skipBits(8);
                int[] iArr = new int[readBits2];
                for (i2 = 0; i2 < readBits2; i2++) {
                    iArr[i2] = ((vorbisBitArray.readBit() ? vorbisBitArray.readBits(5) : 0) * 8) + vorbisBitArray.readBits(3);
                }
                for (i2 = 0; i2 < readBits2; i2++) {
                    for (int i3 = 0; i3 < 8; i3++) {
                        if ((iArr[i2] & (1 << i3)) != 0) {
                            vorbisBitArray.skipBits(8);
                        }
                    }
                }
                i++;
            } else {
                throw new ParserException("residueType greater than 2 is not decodable");
            }
        }
    }

    private static void readFloors(VorbisBitArray vorbisBitArray) throws ParserException {
        int readBits = vorbisBitArray.readBits(6) + 1;
        for (int i = 0; i < readBits; i++) {
            int readBits2 = vorbisBitArray.readBits(16);
            int readBits3;
            if (readBits2 == 0) {
                vorbisBitArray.skipBits(8);
                vorbisBitArray.skipBits(16);
                vorbisBitArray.skipBits(16);
                vorbisBitArray.skipBits(6);
                vorbisBitArray.skipBits(8);
                readBits3 = vorbisBitArray.readBits(4) + 1;
                for (readBits2 = 0; readBits2 < readBits3; readBits2++) {
                    vorbisBitArray.skipBits(8);
                }
            } else if (readBits2 == 1) {
                int readBits4;
                readBits3 = vorbisBitArray.readBits(5);
                int[] iArr = new int[readBits3];
                int i2 = -1;
                for (readBits2 = 0; readBits2 < readBits3; readBits2++) {
                    iArr[readBits2] = vorbisBitArray.readBits(4);
                    if (iArr[readBits2] > i2) {
                        i2 = iArr[readBits2];
                    }
                }
                int[] iArr2 = new int[(i2 + 1)];
                for (i2 = 0; i2 < iArr2.length; i2++) {
                    iArr2[i2] = vorbisBitArray.readBits(3) + 1;
                    readBits4 = vorbisBitArray.readBits(2);
                    if (readBits4 > 0) {
                        vorbisBitArray.skipBits(8);
                    }
                    for (int i3 = 0; i3 < (1 << readBits4); i3++) {
                        vorbisBitArray.skipBits(8);
                    }
                }
                vorbisBitArray.skipBits(2);
                int readBits5 = vorbisBitArray.readBits(4);
                i2 = 0;
                readBits4 = 0;
                for (int i4 = 0; i4 < readBits3; i4++) {
                    i2 += iArr2[iArr[i4]];
                    while (readBits4 < i2) {
                        vorbisBitArray.skipBits(readBits5);
                        readBits4++;
                    }
                }
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("floor type greater than 1 not decodable: ");
                stringBuilder.append(readBits2);
                throw new ParserException(stringBuilder.toString());
            }
        }
    }

    private static long mapType1QuantValues(long j, long j2) {
        double d = (double) j;
        double d2 = (double) j2;
        Double.isNaN(d2);
        return (long) Math.floor(Math.pow(d, 1.0d / d2));
    }
}
