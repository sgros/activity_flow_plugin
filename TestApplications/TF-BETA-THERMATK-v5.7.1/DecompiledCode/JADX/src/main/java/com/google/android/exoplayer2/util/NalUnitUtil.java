package com.google.android.exoplayer2.util;

import java.nio.ByteBuffer;

public final class NalUnitUtil {
    public static final float[] ASPECT_RATIO_IDC_VALUES = new float[]{1.0f, 1.0f, 1.0909091f, 0.90909094f, 1.4545455f, 1.2121212f, 2.1818182f, 1.8181819f, 2.909091f, 2.4242425f, 1.6363636f, 1.3636364f, 1.939394f, 1.6161616f, 1.3333334f, 1.5f, 2.0f};
    public static final int EXTENDED_SAR = 255;
    private static final int H264_NAL_UNIT_TYPE_SEI = 6;
    private static final int H264_NAL_UNIT_TYPE_SPS = 7;
    private static final int H265_NAL_UNIT_TYPE_PREFIX_SEI = 39;
    public static final byte[] NAL_START_CODE = new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 1};
    private static final String TAG = "NalUnitUtil";
    private static int[] scratchEscapePositions = new int[10];
    private static final Object scratchEscapePositionsLock = new Object();

    public static final class PpsData {
        public final boolean bottomFieldPicOrderInFramePresentFlag;
        public final int picParameterSetId;
        public final int seqParameterSetId;

        public PpsData(int i, int i2, boolean z) {
            this.picParameterSetId = i;
            this.seqParameterSetId = i2;
            this.bottomFieldPicOrderInFramePresentFlag = z;
        }
    }

    public static final class SpsData {
        public final int constraintsFlagsAndReservedZero2Bits;
        public final boolean deltaPicOrderAlwaysZeroFlag;
        public final boolean frameMbsOnlyFlag;
        public final int frameNumLength;
        public final int height;
        public final int levelIdc;
        public final int picOrderCntLsbLength;
        public final int picOrderCountType;
        public final float pixelWidthAspectRatio;
        public final int profileIdc;
        public final boolean separateColorPlaneFlag;
        public final int seqParameterSetId;
        public final int width;

        public SpsData(int i, int i2, int i3, int i4, int i5, int i6, float f, boolean z, boolean z2, int i7, int i8, int i9, boolean z3) {
            this.profileIdc = i;
            this.constraintsFlagsAndReservedZero2Bits = i2;
            this.levelIdc = i3;
            this.seqParameterSetId = i4;
            this.width = i5;
            this.height = i6;
            this.pixelWidthAspectRatio = f;
            this.separateColorPlaneFlag = z;
            this.frameMbsOnlyFlag = z2;
            this.frameNumLength = i7;
            this.picOrderCountType = i8;
            this.picOrderCntLsbLength = i9;
            this.deltaPicOrderAlwaysZeroFlag = z3;
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:21:0x0052 in {9, 10, 15, 18, 20} preds:[]
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
    public static int unescapeStream(byte[] r8, int r9) {
        /*
        r0 = scratchEscapePositionsLock;
        monitor-enter(r0);
        r1 = 0;
        r2 = 0;
        r3 = 0;
        if (r2 >= r9) goto L_0x002c;
        r2 = findNextUnescapeIndex(r8, r2, r9);	 Catch:{ all -> 0x002a }
        if (r2 >= r9) goto L_0x0006;	 Catch:{ all -> 0x002a }
        r4 = scratchEscapePositions;	 Catch:{ all -> 0x002a }
        r4 = r4.length;	 Catch:{ all -> 0x002a }
        if (r4 > r3) goto L_0x0020;	 Catch:{ all -> 0x002a }
        r4 = scratchEscapePositions;	 Catch:{ all -> 0x002a }
        r5 = scratchEscapePositions;	 Catch:{ all -> 0x002a }
        r5 = r5.length;	 Catch:{ all -> 0x002a }
        r5 = r5 * 2;	 Catch:{ all -> 0x002a }
        r4 = java.util.Arrays.copyOf(r4, r5);	 Catch:{ all -> 0x002a }
        scratchEscapePositions = r4;	 Catch:{ all -> 0x002a }
        r4 = scratchEscapePositions;	 Catch:{ all -> 0x002a }
        r5 = r3 + 1;	 Catch:{ all -> 0x002a }
        r4[r3] = r2;	 Catch:{ all -> 0x002a }
        r2 = r2 + 3;	 Catch:{ all -> 0x002a }
        r3 = r5;	 Catch:{ all -> 0x002a }
        goto L_0x0006;	 Catch:{ all -> 0x002a }
        r8 = move-exception;	 Catch:{ all -> 0x002a }
        goto L_0x0050;	 Catch:{ all -> 0x002a }
        r9 = r9 - r3;	 Catch:{ all -> 0x002a }
        r2 = 0;	 Catch:{ all -> 0x002a }
        r4 = 0;	 Catch:{ all -> 0x002a }
        r5 = 0;	 Catch:{ all -> 0x002a }
        if (r2 >= r3) goto L_0x0049;	 Catch:{ all -> 0x002a }
        r6 = scratchEscapePositions;	 Catch:{ all -> 0x002a }
        r6 = r6[r2];	 Catch:{ all -> 0x002a }
        r6 = r6 - r5;	 Catch:{ all -> 0x002a }
        java.lang.System.arraycopy(r8, r5, r8, r4, r6);	 Catch:{ all -> 0x002a }
        r4 = r4 + r6;	 Catch:{ all -> 0x002a }
        r7 = r4 + 1;	 Catch:{ all -> 0x002a }
        r8[r4] = r1;	 Catch:{ all -> 0x002a }
        r4 = r7 + 1;	 Catch:{ all -> 0x002a }
        r8[r7] = r1;	 Catch:{ all -> 0x002a }
        r6 = r6 + 3;	 Catch:{ all -> 0x002a }
        r5 = r5 + r6;	 Catch:{ all -> 0x002a }
        r2 = r2 + 1;	 Catch:{ all -> 0x002a }
        goto L_0x0030;	 Catch:{ all -> 0x002a }
        r1 = r9 - r4;	 Catch:{ all -> 0x002a }
        java.lang.System.arraycopy(r8, r5, r8, r4, r1);	 Catch:{ all -> 0x002a }
        monitor-exit(r0);	 Catch:{ all -> 0x002a }
        return r9;	 Catch:{ all -> 0x002a }
        monitor-exit(r0);	 Catch:{ all -> 0x002a }
        throw r8;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.util.NalUnitUtil.unescapeStream(byte[], int):int");
    }

    public static void discardToSps(ByteBuffer byteBuffer) {
        int position = byteBuffer.position();
        int i = 0;
        int i2 = 0;
        while (true) {
            int i3 = i + 1;
            if (i3 < position) {
                int i4 = byteBuffer.get(i) & EXTENDED_SAR;
                if (i2 == 3) {
                    if (i4 == 1 && (byteBuffer.get(i3) & 31) == 7) {
                        ByteBuffer duplicate = byteBuffer.duplicate();
                        duplicate.position(i - 3);
                        duplicate.limit(position);
                        byteBuffer.position(0);
                        byteBuffer.put(duplicate);
                        return;
                    }
                } else if (i4 == 0) {
                    i2++;
                }
                if (i4 != 0) {
                    i2 = 0;
                }
                i = i3;
            } else {
                byteBuffer.clear();
                return;
            }
        }
    }

    public static boolean isNalUnitSei(String str, byte b) {
        if ("video/avc".equals(str) && (b & 31) == 6) {
            return true;
        }
        if (MimeTypes.VIDEO_H265.equals(str) && ((b & 126) >> 1) == 39) {
            return true;
        }
        return false;
    }

    public static int getNalUnitType(byte[] bArr, int i) {
        return bArr[i + 3] & 31;
    }

    public static int getH265NalUnitType(byte[] bArr, int i) {
        return (bArr[i + 3] & 126) >> 1;
    }

    /* JADX WARNING: Removed duplicated region for block: B:53:0x00d7  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x00e9  */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x0145  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x0132  */
    public static com.google.android.exoplayer2.util.NalUnitUtil.SpsData parseSpsNalUnit(byte[] r20, int r21, int r22) {
        /*
        r0 = new com.google.android.exoplayer2.util.ParsableNalUnitBitArray;
        r1 = r20;
        r2 = r21;
        r3 = r22;
        r0.<init>(r1, r2, r3);
        r1 = 8;
        r0.skipBits(r1);
        r3 = r0.readBits(r1);
        r4 = r0.readBits(r1);
        r5 = r0.readBits(r1);
        r6 = r0.readUnsignedExpGolombCodedInt();
        r2 = 3;
        r9 = 1;
        r10 = 100;
        if (r3 == r10) goto L_0x004e;
    L_0x0026:
        r10 = 110; // 0x6e float:1.54E-43 double:5.43E-322;
        if (r3 == r10) goto L_0x004e;
    L_0x002a:
        r10 = 122; // 0x7a float:1.71E-43 double:6.03E-322;
        if (r3 == r10) goto L_0x004e;
    L_0x002e:
        r10 = 244; // 0xf4 float:3.42E-43 double:1.206E-321;
        if (r3 == r10) goto L_0x004e;
    L_0x0032:
        r10 = 44;
        if (r3 == r10) goto L_0x004e;
    L_0x0036:
        r10 = 83;
        if (r3 == r10) goto L_0x004e;
    L_0x003a:
        r10 = 86;
        if (r3 == r10) goto L_0x004e;
    L_0x003e:
        r10 = 118; // 0x76 float:1.65E-43 double:5.83E-322;
        if (r3 == r10) goto L_0x004e;
    L_0x0042:
        r10 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        if (r3 == r10) goto L_0x004e;
    L_0x0046:
        r10 = 138; // 0x8a float:1.93E-43 double:6.8E-322;
        if (r3 != r10) goto L_0x004b;
    L_0x004a:
        goto L_0x004e;
    L_0x004b:
        r10 = 1;
        r11 = 0;
        goto L_0x0087;
    L_0x004e:
        r10 = r0.readUnsignedExpGolombCodedInt();
        if (r10 != r2) goto L_0x0059;
    L_0x0054:
        r11 = r0.readBit();
        goto L_0x005a;
    L_0x0059:
        r11 = 0;
    L_0x005a:
        r0.readUnsignedExpGolombCodedInt();
        r0.readUnsignedExpGolombCodedInt();
        r0.skipBit();
        r12 = r0.readBit();
        if (r12 == 0) goto L_0x0087;
    L_0x0069:
        if (r10 == r2) goto L_0x006e;
    L_0x006b:
        r12 = 8;
        goto L_0x0070;
    L_0x006e:
        r12 = 12;
    L_0x0070:
        r13 = 0;
    L_0x0071:
        if (r13 >= r12) goto L_0x0087;
    L_0x0073:
        r14 = r0.readBit();
        if (r14 == 0) goto L_0x0084;
    L_0x0079:
        r14 = 6;
        if (r13 >= r14) goto L_0x007f;
    L_0x007c:
        r14 = 16;
        goto L_0x0081;
    L_0x007f:
        r14 = 64;
    L_0x0081:
        skipScalingList(r0, r14);
    L_0x0084:
        r13 = r13 + 1;
        goto L_0x0071;
    L_0x0087:
        r12 = r0.readUnsignedExpGolombCodedInt();
        r12 = r12 + 4;
        r13 = r0.readUnsignedExpGolombCodedInt();
        if (r13 != 0) goto L_0x009a;
    L_0x0093:
        r14 = r0.readUnsignedExpGolombCodedInt();
        r14 = r14 + 4;
        goto L_0x00bb;
    L_0x009a:
        if (r13 != r9) goto L_0x00ba;
    L_0x009c:
        r14 = r0.readBit();
        r0.readSignedExpGolombCodedInt();
        r0.readSignedExpGolombCodedInt();
        r15 = r0.readUnsignedExpGolombCodedInt();
        r1 = (long) r15;
        r15 = 0;
    L_0x00ac:
        r7 = (long) r15;
        r17 = (r7 > r1 ? 1 : (r7 == r1 ? 0 : -1));
        if (r17 >= 0) goto L_0x00b7;
    L_0x00b1:
        r0.readUnsignedExpGolombCodedInt();
        r15 = r15 + 1;
        goto L_0x00ac;
    L_0x00b7:
        r15 = r14;
        r14 = 0;
        goto L_0x00bc;
    L_0x00ba:
        r14 = 0;
    L_0x00bb:
        r15 = 0;
    L_0x00bc:
        r0.readUnsignedExpGolombCodedInt();
        r0.skipBit();
        r1 = r0.readUnsignedExpGolombCodedInt();
        r1 = r1 + r9;
        r2 = r0.readUnsignedExpGolombCodedInt();
        r2 = r2 + r9;
        r16 = r0.readBit();
        r7 = 2;
        r8 = 2 - r16;
        r8 = r8 * r2;
        if (r16 != 0) goto L_0x00da;
    L_0x00d7:
        r0.skipBit();
    L_0x00da:
        r0.skipBit();
        r2 = 16;
        r1 = r1 * 16;
        r8 = r8 * 16;
        r2 = r0.readBit();
        if (r2 == 0) goto L_0x0119;
    L_0x00e9:
        r2 = r0.readUnsignedExpGolombCodedInt();
        r17 = r0.readUnsignedExpGolombCodedInt();
        r18 = r0.readUnsignedExpGolombCodedInt();
        r19 = r0.readUnsignedExpGolombCodedInt();
        if (r10 != 0) goto L_0x0100;
    L_0x00fb:
        r7 = 2 - r16;
        r9 = r7;
        r7 = 1;
        goto L_0x010e;
    L_0x0100:
        r7 = 3;
        if (r10 != r7) goto L_0x0105;
    L_0x0103:
        r7 = 1;
        goto L_0x0106;
    L_0x0105:
        r7 = 2;
    L_0x0106:
        if (r10 != r9) goto L_0x0109;
    L_0x0108:
        r9 = 2;
    L_0x0109:
        r10 = 2;
        r10 = 2 - r16;
        r9 = r9 * r10;
    L_0x010e:
        r2 = r2 + r17;
        r2 = r2 * r7;
        r1 = r1 - r2;
        r18 = r18 + r19;
        r18 = r18 * r9;
        r8 = r8 - r18;
    L_0x0119:
        r7 = r1;
        r1 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r2 = r0.readBit();
        if (r2 == 0) goto L_0x0164;
    L_0x0122:
        r2 = r0.readBit();
        if (r2 == 0) goto L_0x0164;
    L_0x0128:
        r2 = 8;
        r2 = r0.readBits(r2);
        r9 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        if (r2 != r9) goto L_0x0145;
    L_0x0132:
        r9 = 16;
        r2 = r0.readBits(r9);
        r0 = r0.readBits(r9);
        if (r2 == 0) goto L_0x0143;
    L_0x013e:
        if (r0 == 0) goto L_0x0143;
    L_0x0140:
        r1 = (float) r2;
        r0 = (float) r0;
        r1 = r1 / r0;
    L_0x0143:
        r9 = r1;
        goto L_0x0166;
    L_0x0145:
        r0 = ASPECT_RATIO_IDC_VALUES;
        r9 = r0.length;
        if (r2 >= r9) goto L_0x014e;
    L_0x014a:
        r0 = r0[r2];
        r9 = r0;
        goto L_0x0166;
    L_0x014e:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r9 = "Unexpected aspect_ratio_idc value: ";
        r0.append(r9);
        r0.append(r2);
        r0 = r0.toString();
        r2 = "NalUnitUtil";
        com.google.android.exoplayer2.util.Log.m18w(r2, r0);
    L_0x0164:
        r9 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
    L_0x0166:
        r0 = new com.google.android.exoplayer2.util.NalUnitUtil$SpsData;
        r2 = r0;
        r10 = r11;
        r11 = r16;
        r2.<init>(r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.util.NalUnitUtil.parseSpsNalUnit(byte[], int, int):com.google.android.exoplayer2.util.NalUnitUtil$SpsData");
    }

    public static PpsData parsePpsNalUnit(byte[] bArr, int i, int i2) {
        ParsableNalUnitBitArray parsableNalUnitBitArray = new ParsableNalUnitBitArray(bArr, i, i2);
        parsableNalUnitBitArray.skipBits(8);
        int readUnsignedExpGolombCodedInt = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        i = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        parsableNalUnitBitArray.skipBit();
        return new PpsData(readUnsignedExpGolombCodedInt, i, parsableNalUnitBitArray.readBit());
    }

    /* JADX WARNING: Missing block: B:64:0x0097, code skipped:
            r8 = true;
     */
    public static int findNalUnit(byte[] r7, int r8, int r9, boolean[] r10) {
        /*
        r0 = r9 - r8;
        r1 = 0;
        r2 = 1;
        if (r0 < 0) goto L_0x0008;
    L_0x0006:
        r3 = 1;
        goto L_0x0009;
    L_0x0008:
        r3 = 0;
    L_0x0009:
        com.google.android.exoplayer2.util.Assertions.checkState(r3);
        if (r0 != 0) goto L_0x000f;
    L_0x000e:
        return r9;
    L_0x000f:
        r3 = 2;
        if (r10 == 0) goto L_0x0040;
    L_0x0012:
        r4 = r10[r1];
        if (r4 == 0) goto L_0x001c;
    L_0x0016:
        clearPrefixFlags(r10);
        r8 = r8 + -3;
        return r8;
    L_0x001c:
        if (r0 <= r2) goto L_0x002b;
    L_0x001e:
        r4 = r10[r2];
        if (r4 == 0) goto L_0x002b;
    L_0x0022:
        r4 = r7[r8];
        if (r4 != r2) goto L_0x002b;
    L_0x0026:
        clearPrefixFlags(r10);
        r8 = r8 - r3;
        return r8;
    L_0x002b:
        if (r0 <= r3) goto L_0x0040;
    L_0x002d:
        r4 = r10[r3];
        if (r4 == 0) goto L_0x0040;
    L_0x0031:
        r4 = r7[r8];
        if (r4 != 0) goto L_0x0040;
    L_0x0035:
        r4 = r8 + 1;
        r4 = r7[r4];
        if (r4 != r2) goto L_0x0040;
    L_0x003b:
        clearPrefixFlags(r10);
        r8 = r8 - r2;
        return r8;
    L_0x0040:
        r4 = r9 + -1;
        r8 = r8 + r3;
    L_0x0043:
        if (r8 >= r4) goto L_0x0067;
    L_0x0045:
        r5 = r7[r8];
        r5 = r5 & 254;
        if (r5 == 0) goto L_0x004c;
    L_0x004b:
        goto L_0x0064;
    L_0x004c:
        r5 = r8 + -2;
        r6 = r7[r5];
        if (r6 != 0) goto L_0x0062;
    L_0x0052:
        r6 = r8 + -1;
        r6 = r7[r6];
        if (r6 != 0) goto L_0x0062;
    L_0x0058:
        r6 = r7[r8];
        if (r6 != r2) goto L_0x0062;
    L_0x005c:
        if (r10 == 0) goto L_0x0061;
    L_0x005e:
        clearPrefixFlags(r10);
    L_0x0061:
        return r5;
    L_0x0062:
        r8 = r8 + -2;
    L_0x0064:
        r8 = r8 + 3;
        goto L_0x0043;
    L_0x0067:
        if (r10 == 0) goto L_0x00bb;
    L_0x0069:
        if (r0 <= r3) goto L_0x007e;
    L_0x006b:
        r8 = r9 + -3;
        r8 = r7[r8];
        if (r8 != 0) goto L_0x007c;
    L_0x0071:
        r8 = r9 + -2;
        r8 = r7[r8];
        if (r8 != 0) goto L_0x007c;
    L_0x0077:
        r8 = r7[r4];
        if (r8 != r2) goto L_0x007c;
    L_0x007b:
        goto L_0x0097;
    L_0x007c:
        r8 = 0;
        goto L_0x0098;
    L_0x007e:
        if (r0 != r3) goto L_0x008f;
    L_0x0080:
        r8 = r10[r3];
        if (r8 == 0) goto L_0x007c;
    L_0x0084:
        r8 = r9 + -2;
        r8 = r7[r8];
        if (r8 != 0) goto L_0x007c;
    L_0x008a:
        r8 = r7[r4];
        if (r8 != r2) goto L_0x007c;
    L_0x008e:
        goto L_0x0097;
    L_0x008f:
        r8 = r10[r2];
        if (r8 == 0) goto L_0x007c;
    L_0x0093:
        r8 = r7[r4];
        if (r8 != r2) goto L_0x007c;
    L_0x0097:
        r8 = 1;
    L_0x0098:
        r10[r1] = r8;
        if (r0 <= r2) goto L_0x00a7;
    L_0x009c:
        r8 = r9 + -2;
        r8 = r7[r8];
        if (r8 != 0) goto L_0x00b1;
    L_0x00a2:
        r8 = r7[r4];
        if (r8 != 0) goto L_0x00b1;
    L_0x00a6:
        goto L_0x00af;
    L_0x00a7:
        r8 = r10[r3];
        if (r8 == 0) goto L_0x00b1;
    L_0x00ab:
        r8 = r7[r4];
        if (r8 != 0) goto L_0x00b1;
    L_0x00af:
        r8 = 1;
        goto L_0x00b2;
    L_0x00b1:
        r8 = 0;
    L_0x00b2:
        r10[r2] = r8;
        r7 = r7[r4];
        if (r7 != 0) goto L_0x00b9;
    L_0x00b8:
        r1 = 1;
    L_0x00b9:
        r10[r3] = r1;
    L_0x00bb:
        return r9;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.util.NalUnitUtil.findNalUnit(byte[], int, int, boolean[]):int");
    }

    public static void clearPrefixFlags(boolean[] zArr) {
        zArr[0] = false;
        zArr[1] = false;
        zArr[2] = false;
    }

    private static int findNextUnescapeIndex(byte[] bArr, int i, int i2) {
        while (i < i2 - 2) {
            if (bArr[i] == (byte) 0 && bArr[i + 1] == (byte) 0 && bArr[i + 2] == (byte) 3) {
                return i;
            }
            i++;
        }
        return i2;
    }

    private static void skipScalingList(ParsableNalUnitBitArray parsableNalUnitBitArray, int i) {
        int i2 = 8;
        int i3 = 8;
        for (int i4 = 0; i4 < i; i4++) {
            if (i2 != 0) {
                i2 = ((parsableNalUnitBitArray.readSignedExpGolombCodedInt() + i3) + 256) % 256;
            }
            if (i2 != 0) {
                i3 = i2;
            }
        }
    }

    private NalUnitUtil() {
    }
}
