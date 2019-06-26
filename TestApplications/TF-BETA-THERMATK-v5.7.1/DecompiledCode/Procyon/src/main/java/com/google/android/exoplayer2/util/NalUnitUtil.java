// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.util;

import java.util.Arrays;
import java.nio.ByteBuffer;

public final class NalUnitUtil
{
    public static final float[] ASPECT_RATIO_IDC_VALUES;
    public static final int EXTENDED_SAR = 255;
    private static final int H264_NAL_UNIT_TYPE_SEI = 6;
    private static final int H264_NAL_UNIT_TYPE_SPS = 7;
    private static final int H265_NAL_UNIT_TYPE_PREFIX_SEI = 39;
    public static final byte[] NAL_START_CODE;
    private static final String TAG = "NalUnitUtil";
    private static int[] scratchEscapePositions;
    private static final Object scratchEscapePositionsLock;
    
    static {
        NAL_START_CODE = new byte[] { 0, 0, 0, 1 };
        ASPECT_RATIO_IDC_VALUES = new float[] { 1.0f, 1.0f, 1.0909091f, 0.90909094f, 1.4545455f, 1.2121212f, 2.1818182f, 1.8181819f, 2.909091f, 2.4242425f, 1.6363636f, 1.3636364f, 1.939394f, 1.6161616f, 1.3333334f, 1.5f, 2.0f };
        scratchEscapePositionsLock = new Object();
        NalUnitUtil.scratchEscapePositions = new int[10];
    }
    
    private NalUnitUtil() {
    }
    
    public static void clearPrefixFlags(final boolean[] array) {
        array[0] = false;
        array[2] = (array[1] = false);
    }
    
    public static void discardToSps(final ByteBuffer byteBuffer) {
        final int position = byteBuffer.position();
        int n = 0;
        int n2 = 0;
        while (true) {
            final int n3 = n + 1;
            if (n3 >= position) {
                byteBuffer.clear();
                return;
            }
            final int n4 = byteBuffer.get(n) & 0xFF;
            int n5;
            if (n2 == 3) {
                n5 = n2;
                if (n4 == 1) {
                    n5 = n2;
                    if ((byteBuffer.get(n3) & 0x1F) == 0x7) {
                        final ByteBuffer duplicate = byteBuffer.duplicate();
                        duplicate.position(n - 3);
                        duplicate.limit(position);
                        byteBuffer.position(0);
                        byteBuffer.put(duplicate);
                        return;
                    }
                }
            }
            else {
                n5 = n2;
                if (n4 == 0) {
                    n5 = n2 + 1;
                }
            }
            n2 = n5;
            if (n4 != 0) {
                n2 = 0;
            }
            n = n3;
        }
    }
    
    public static int findNalUnit(final byte[] array, int i, final int n, final boolean[] array2) {
        final int n2 = n - i;
        final boolean b = false;
        Assertions.checkState(n2 >= 0);
        if (n2 == 0) {
            return n;
        }
        if (array2 != null) {
            if (array2[0]) {
                clearPrefixFlags(array2);
                return i - 3;
            }
            if (n2 > 1 && array2[1] && array[i] == 1) {
                clearPrefixFlags(array2);
                return i - 2;
            }
            if (n2 > 2 && array2[2] && array[i] == 0 && array[i + 1] == 1) {
                clearPrefixFlags(array2);
                return i - 1;
            }
        }
        int n3;
        for (n3 = n - 1, i += 2; i < n3; i += 3) {
            if ((array[i] & 0xFE) == 0x0) {
                final int n4 = i - 2;
                if (array[n4] == 0 && array[i - 1] == 0 && array[i] == 1) {
                    if (array2 != null) {
                        clearPrefixFlags(array2);
                    }
                    return n4;
                }
                i -= 2;
            }
        }
        if (array2 != null) {
            array2[0] = ((n2 <= 2) ? ((n2 != 2) ? (array2[1] && array[n3] == 1) : (array2[2] && array[n - 2] == 0 && array[n3] == 1)) : (array[n - 3] == 0 && array[n - 2] == 0 && array[n3] == 1));
            array2[1] = ((n2 > 1) ? (array[n - 2] == 0 && array[n3] == 0) : (array2[2] && array[n3] == 0));
            boolean b2 = b;
            if (array[n3] == 0) {
                b2 = true;
            }
            array2[2] = b2;
        }
        return n;
    }
    
    private static int findNextUnescapeIndex(final byte[] array, int i, final int n) {
        while (i < n - 2) {
            if (array[i] == 0 && array[i + 1] == 0 && array[i + 2] == 3) {
                return i;
            }
            ++i;
        }
        return n;
    }
    
    public static int getH265NalUnitType(final byte[] array, final int n) {
        return (array[n + 3] & 0x7E) >> 1;
    }
    
    public static int getNalUnitType(final byte[] array, final int n) {
        return array[n + 3] & 0x1F;
    }
    
    public static boolean isNalUnitSei(final String s, final byte b) {
        final boolean equals = "video/avc".equals(s);
        final boolean b2 = true;
        if (equals) {
            final boolean b3 = b2;
            if ((b & 0x1F) == 0x6) {
                return b3;
            }
        }
        return "video/hevc".equals(s) && (b & 0x7E) >> 1 == 39 && b2;
    }
    
    public static PpsData parsePpsNalUnit(final byte[] array, int unsignedExpGolombCodedInt, int unsignedExpGolombCodedInt2) {
        final ParsableNalUnitBitArray parsableNalUnitBitArray = new ParsableNalUnitBitArray(array, unsignedExpGolombCodedInt, unsignedExpGolombCodedInt2);
        parsableNalUnitBitArray.skipBits(8);
        unsignedExpGolombCodedInt2 = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        unsignedExpGolombCodedInt = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        parsableNalUnitBitArray.skipBit();
        return new PpsData(unsignedExpGolombCodedInt2, unsignedExpGolombCodedInt, parsableNalUnitBitArray.readBit());
    }
    
    public static SpsData parseSpsNalUnit(final byte[] array, int unsignedExpGolombCodedInt, int n) {
        final ParsableNalUnitBitArray parsableNalUnitBitArray = new ParsableNalUnitBitArray(array, unsignedExpGolombCodedInt, n);
        parsableNalUnitBitArray.skipBits(8);
        final int bits = parsableNalUnitBitArray.readBits(8);
        final int bits2 = parsableNalUnitBitArray.readBits(8);
        final int bits3 = parsableNalUnitBitArray.readBits(8);
        final int unsignedExpGolombCodedInt2 = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        final int n2 = 1;
        int n3;
        boolean b;
        if (bits != 100 && bits != 110 && bits != 122 && bits != 244 && bits != 44 && bits != 83 && bits != 86 && bits != 118 && bits != 128 && bits != 138) {
            n3 = 1;
            b = false;
        }
        else {
            final int unsignedExpGolombCodedInt3 = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
            final boolean b2 = unsignedExpGolombCodedInt3 == 3 && parsableNalUnitBitArray.readBit();
            parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
            parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
            parsableNalUnitBitArray.skipBit();
            n3 = unsignedExpGolombCodedInt3;
            b = b2;
            if (parsableNalUnitBitArray.readBit()) {
                if (unsignedExpGolombCodedInt3 != 3) {
                    unsignedExpGolombCodedInt = 8;
                }
                else {
                    unsignedExpGolombCodedInt = 12;
                }
                n = 0;
                while (true) {
                    n3 = unsignedExpGolombCodedInt3;
                    b = b2;
                    if (n >= unsignedExpGolombCodedInt) {
                        break;
                    }
                    if (parsableNalUnitBitArray.readBit()) {
                        int n4;
                        if (n < 6) {
                            n4 = 16;
                        }
                        else {
                            n4 = 64;
                        }
                        skipScalingList(parsableNalUnitBitArray, n4);
                    }
                    ++n;
                }
            }
        }
        final int unsignedExpGolombCodedInt4 = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        final int unsignedExpGolombCodedInt5 = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        boolean bit = false;
        Label_0332: {
            if (unsignedExpGolombCodedInt5 == 0) {
                unsignedExpGolombCodedInt = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt() + 4;
            }
            else {
                if (unsignedExpGolombCodedInt5 == 1) {
                    bit = parsableNalUnitBitArray.readBit();
                    parsableNalUnitBitArray.readSignedExpGolombCodedInt();
                    parsableNalUnitBitArray.readSignedExpGolombCodedInt();
                    long n5;
                    for (n5 = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt(), unsignedExpGolombCodedInt = 0; unsignedExpGolombCodedInt < n5; ++unsignedExpGolombCodedInt) {
                        parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
                    }
                    n = 0;
                    break Label_0332;
                }
                unsignedExpGolombCodedInt = 0;
            }
            bit = false;
            n = unsignedExpGolombCodedInt;
        }
        parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        parsableNalUnitBitArray.skipBit();
        final int unsignedExpGolombCodedInt6 = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        unsignedExpGolombCodedInt = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        final int bit2 = parsableNalUnitBitArray.readBit() ? 1 : 0;
        if (bit2 == 0) {
            parsableNalUnitBitArray.skipBit();
        }
        parsableNalUnitBitArray.skipBit();
        final int n6 = (unsignedExpGolombCodedInt6 + 1) * 16;
        final int n7 = (2 - bit2) * (unsignedExpGolombCodedInt + 1) * 16;
        int n8 = n6;
        unsignedExpGolombCodedInt = n7;
        if (parsableNalUnitBitArray.readBit()) {
            final int unsignedExpGolombCodedInt7 = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
            final int unsignedExpGolombCodedInt8 = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
            final int unsignedExpGolombCodedInt9 = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
            final int unsignedExpGolombCodedInt10 = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
            int n9;
            if (n3 == 0) {
                n9 = 2 - bit2;
                unsignedExpGolombCodedInt = 1;
            }
            else {
                if (n3 == 3) {
                    unsignedExpGolombCodedInt = 1;
                }
                else {
                    unsignedExpGolombCodedInt = 2;
                }
                int n10 = n2;
                if (n3 == 1) {
                    n10 = 2;
                }
                n9 = n10 * (2 - bit2);
            }
            n8 = n6 - (unsignedExpGolombCodedInt7 + unsignedExpGolombCodedInt8) * unsignedExpGolombCodedInt;
            unsignedExpGolombCodedInt = n7 - (unsignedExpGolombCodedInt9 + unsignedExpGolombCodedInt10) * n9;
        }
        final float n11 = 1.0f;
        if (parsableNalUnitBitArray.readBit() && parsableNalUnitBitArray.readBit()) {
            final int bits4 = parsableNalUnitBitArray.readBits(8);
            if (bits4 == 255) {
                final int bits5 = parsableNalUnitBitArray.readBits(16);
                final int bits6 = parsableNalUnitBitArray.readBits(16);
                float n12 = n11;
                if (bits5 != 0) {
                    n12 = n11;
                    if (bits6 != 0) {
                        n12 = bits5 / (float)bits6;
                    }
                }
                return new SpsData(bits, bits2, bits3, unsignedExpGolombCodedInt2, n8, unsignedExpGolombCodedInt, n12, b, (boolean)(bit2 != 0), unsignedExpGolombCodedInt4 + 4, unsignedExpGolombCodedInt5, n, bit);
            }
            final float[] aspect_RATIO_IDC_VALUES = NalUnitUtil.ASPECT_RATIO_IDC_VALUES;
            if (bits4 < aspect_RATIO_IDC_VALUES.length) {
                final float n12 = aspect_RATIO_IDC_VALUES[bits4];
                return new SpsData(bits, bits2, bits3, unsignedExpGolombCodedInt2, n8, unsignedExpGolombCodedInt, n12, b, (boolean)(bit2 != 0), unsignedExpGolombCodedInt4 + 4, unsignedExpGolombCodedInt5, n, bit);
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Unexpected aspect_ratio_idc value: ");
            sb.append(bits4);
            Log.w("NalUnitUtil", sb.toString());
        }
        float n12 = 1.0f;
        return new SpsData(bits, bits2, bits3, unsignedExpGolombCodedInt2, n8, unsignedExpGolombCodedInt, n12, b, (boolean)(bit2 != 0), unsignedExpGolombCodedInt4 + 4, unsignedExpGolombCodedInt5, n, bit);
    }
    
    private static void skipScalingList(final ParsableNalUnitBitArray parsableNalUnitBitArray, final int n) {
        int n2 = 8;
        int i = 0;
        int n3 = 8;
        while (i < n) {
            int n4;
            if ((n4 = n2) != 0) {
                n4 = (parsableNalUnitBitArray.readSignedExpGolombCodedInt() + n3 + 256) % 256;
            }
            if (n4 != 0) {
                n3 = n4;
            }
            ++i;
            n2 = n4;
        }
    }
    
    public static int unescapeStream(final byte[] array, int n) {
        final Object scratchEscapePositionsLock = NalUnitUtil.scratchEscapePositionsLock;
        // monitorenter(scratchEscapePositionsLock)
        int nextUnescapeIndex = 0;
        int n2 = 0;
        while (true) {
            Label_0080: {
                if (nextUnescapeIndex >= n) {
                    break Label_0080;
                }
                try {
                    final int n3 = nextUnescapeIndex = findNextUnescapeIndex(array, nextUnescapeIndex, n);
                    if (n3 >= n) {
                        continue;
                    }
                    if (NalUnitUtil.scratchEscapePositions.length <= n2) {
                        NalUnitUtil.scratchEscapePositions = Arrays.copyOf(NalUnitUtil.scratchEscapePositions, NalUnitUtil.scratchEscapePositions.length * 2);
                    }
                    NalUnitUtil.scratchEscapePositions[n2] = n3;
                    nextUnescapeIndex = n3 + 3;
                    ++n2;
                }
                finally {
                    // monitorexit(scratchEscapePositionsLock)
                    while (true) {
                        int n5 = 0;
                        final int n4 = NalUnitUtil.scratchEscapePositions[n5] - n;
                        int n6 = 0;
                        System.arraycopy(array, n, array, n6, n4);
                        final int n7 = n6 + n4;
                        final int n8 = n7 + 1;
                        array[n7] = 0;
                        n6 = n8 + 1;
                        array[n8] = 0;
                        n += n4 + 3;
                        ++n5;
                        Label_0093: {
                            break Label_0093;
                            final int n9 = n - n2;
                            n5 = 0;
                            n6 = 0;
                            n = 0;
                            break Label_0093;
                            Label_0162: {
                                System.arraycopy(array, n, array, n6, n9 - n6);
                            }
                            return n9;
                        }
                        continue;
                    }
                }
                // monitorexit(scratchEscapePositionsLock)
                // iftrue(Label_0162:, n5 >= n2)
            }
        }
    }
    
    public static final class PpsData
    {
        public final boolean bottomFieldPicOrderInFramePresentFlag;
        public final int picParameterSetId;
        public final int seqParameterSetId;
        
        public PpsData(final int picParameterSetId, final int seqParameterSetId, final boolean bottomFieldPicOrderInFramePresentFlag) {
            this.picParameterSetId = picParameterSetId;
            this.seqParameterSetId = seqParameterSetId;
            this.bottomFieldPicOrderInFramePresentFlag = bottomFieldPicOrderInFramePresentFlag;
        }
    }
    
    public static final class SpsData
    {
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
        
        public SpsData(final int profileIdc, final int constraintsFlagsAndReservedZero2Bits, final int levelIdc, final int seqParameterSetId, final int width, final int height, final float pixelWidthAspectRatio, final boolean separateColorPlaneFlag, final boolean frameMbsOnlyFlag, final int frameNumLength, final int picOrderCountType, final int picOrderCntLsbLength, final boolean deltaPicOrderAlwaysZeroFlag) {
            this.profileIdc = profileIdc;
            this.constraintsFlagsAndReservedZero2Bits = constraintsFlagsAndReservedZero2Bits;
            this.levelIdc = levelIdc;
            this.seqParameterSetId = seqParameterSetId;
            this.width = width;
            this.height = height;
            this.pixelWidthAspectRatio = pixelWidthAspectRatio;
            this.separateColorPlaneFlag = separateColorPlaneFlag;
            this.frameMbsOnlyFlag = frameMbsOnlyFlag;
            this.frameNumLength = frameNumLength;
            this.picOrderCountType = picOrderCountType;
            this.picOrderCntLsbLength = picOrderCntLsbLength;
            this.deltaPicOrderAlwaysZeroFlag = deltaPicOrderAlwaysZeroFlag;
        }
    }
}
