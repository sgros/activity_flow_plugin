// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.util;

import java.util.ArrayList;
import com.google.android.exoplayer2.ParserException;
import android.util.Pair;

public final class CodecSpecificDataUtil
{
    private static final int AUDIO_OBJECT_TYPE_AAC_LC = 2;
    private static final int AUDIO_OBJECT_TYPE_ER_BSAC = 22;
    private static final int AUDIO_OBJECT_TYPE_ESCAPE = 31;
    private static final int AUDIO_OBJECT_TYPE_PS = 29;
    private static final int AUDIO_OBJECT_TYPE_SBR = 5;
    private static final int AUDIO_SPECIFIC_CONFIG_CHANNEL_CONFIGURATION_INVALID = -1;
    private static final int[] AUDIO_SPECIFIC_CONFIG_CHANNEL_COUNT_TABLE;
    private static final int AUDIO_SPECIFIC_CONFIG_FREQUENCY_INDEX_ARBITRARY = 15;
    private static final int[] AUDIO_SPECIFIC_CONFIG_SAMPLING_RATE_TABLE;
    private static final byte[] NAL_START_CODE;
    
    static {
        NAL_START_CODE = new byte[] { 0, 0, 0, 1 };
        AUDIO_SPECIFIC_CONFIG_SAMPLING_RATE_TABLE = new int[] { 96000, 88200, 64000, 48000, 44100, 32000, 24000, 22050, 16000, 12000, 11025, 8000, 7350 };
        AUDIO_SPECIFIC_CONFIG_CHANNEL_COUNT_TABLE = new int[] { 0, 1, 2, 3, 4, 5, 6, 8, -1, -1, -1, 7, 8, -1, 8, -1 };
    }
    
    private CodecSpecificDataUtil() {
    }
    
    public static byte[] buildAacAudioSpecificConfig(final int n, final int n2, final int n3) {
        return new byte[] { (byte)((n << 3 & 0xF8) | (n2 >> 1 & 0x7)), (byte)((n2 << 7 & 0x80) | (n3 << 3 & 0x78)) };
    }
    
    public static byte[] buildAacLcAudioSpecificConfig(final int i, final int j) {
        final int n = 0;
        int n2 = 0;
        int n3 = -1;
        while (true) {
            final int[] audio_SPECIFIC_CONFIG_SAMPLING_RATE_TABLE = CodecSpecificDataUtil.AUDIO_SPECIFIC_CONFIG_SAMPLING_RATE_TABLE;
            if (n2 >= audio_SPECIFIC_CONFIG_SAMPLING_RATE_TABLE.length) {
                break;
            }
            if (i == audio_SPECIFIC_CONFIG_SAMPLING_RATE_TABLE[n2]) {
                n3 = n2;
            }
            ++n2;
        }
        int n4 = -1;
        int n5 = n;
        while (true) {
            final int[] audio_SPECIFIC_CONFIG_CHANNEL_COUNT_TABLE = CodecSpecificDataUtil.AUDIO_SPECIFIC_CONFIG_CHANNEL_COUNT_TABLE;
            if (n5 >= audio_SPECIFIC_CONFIG_CHANNEL_COUNT_TABLE.length) {
                break;
            }
            if (j == audio_SPECIFIC_CONFIG_CHANNEL_COUNT_TABLE[n5]) {
                n4 = n5;
            }
            ++n5;
        }
        if (i != -1 && n4 != -1) {
            return buildAacAudioSpecificConfig(2, n3, n4);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid sample rate or number of channels: ");
        sb.append(i);
        sb.append(", ");
        sb.append(j);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static String buildAvcCodecString(final int i, final int j, final int k) {
        return String.format("avc1.%02X%02X%02X", i, j, k);
    }
    
    public static byte[] buildNalUnit(final byte[] array, final int n, final int n2) {
        final byte[] nal_START_CODE = CodecSpecificDataUtil.NAL_START_CODE;
        final byte[] array2 = new byte[nal_START_CODE.length + n2];
        System.arraycopy(nal_START_CODE, 0, array2, 0, nal_START_CODE.length);
        System.arraycopy(array, n, array2, CodecSpecificDataUtil.NAL_START_CODE.length, n2);
        return array2;
    }
    
    private static int findNalStartCode(final byte[] array, int i) {
        while (i <= array.length - CodecSpecificDataUtil.NAL_START_CODE.length) {
            if (isNalStartCode(array, i)) {
                return i;
            }
            ++i;
        }
        return -1;
    }
    
    private static int getAacAudioObjectType(final ParsableBitArray parsableBitArray) {
        int bits;
        if ((bits = parsableBitArray.readBits(5)) == 31) {
            bits = parsableBitArray.readBits(6) + 32;
        }
        return bits;
    }
    
    private static int getAacSamplingFrequency(final ParsableBitArray parsableBitArray) {
        final int bits = parsableBitArray.readBits(4);
        int bits2;
        if (bits == 15) {
            bits2 = parsableBitArray.readBits(24);
        }
        else {
            Assertions.checkArgument(bits < 13);
            bits2 = CodecSpecificDataUtil.AUDIO_SPECIFIC_CONFIG_SAMPLING_RATE_TABLE[bits];
        }
        return bits2;
    }
    
    private static boolean isNalStartCode(final byte[] array, final int n) {
        if (array.length - n <= CodecSpecificDataUtil.NAL_START_CODE.length) {
            return false;
        }
        int n2 = 0;
        while (true) {
            final byte[] nal_START_CODE = CodecSpecificDataUtil.NAL_START_CODE;
            if (n2 >= nal_START_CODE.length) {
                return true;
            }
            if (array[n + n2] != nal_START_CODE[n2]) {
                return false;
            }
            ++n2;
        }
    }
    
    public static Pair<Integer, Integer> parseAacAudioSpecificConfig(final ParsableBitArray parsableBitArray, final boolean b) throws ParserException {
        final int aacAudioObjectType = getAacAudioObjectType(parsableBitArray);
        int aacSamplingFrequency = getAacSamplingFrequency(parsableBitArray);
        final int bits = parsableBitArray.readBits(4);
        int aacAudioObjectType2 = 0;
        int bits2 = 0;
        Label_0076: {
            if (aacAudioObjectType != 5) {
                aacAudioObjectType2 = aacAudioObjectType;
                bits2 = bits;
                if (aacAudioObjectType != 29) {
                    break Label_0076;
                }
            }
            final int aacSamplingFrequency2 = getAacSamplingFrequency(parsableBitArray);
            final int n = aacAudioObjectType2 = getAacAudioObjectType(parsableBitArray);
            aacSamplingFrequency = aacSamplingFrequency2;
            bits2 = bits;
            if (n == 22) {
                bits2 = parsableBitArray.readBits(4);
                aacSamplingFrequency = aacSamplingFrequency2;
                aacAudioObjectType2 = n;
            }
        }
        final boolean b2 = true;
        if (b) {
            if (aacAudioObjectType2 != 1 && aacAudioObjectType2 != 2 && aacAudioObjectType2 != 3 && aacAudioObjectType2 != 4 && aacAudioObjectType2 != 6 && aacAudioObjectType2 != 7 && aacAudioObjectType2 != 17) {
                switch (aacAudioObjectType2) {
                    default: {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Unsupported audio object type: ");
                        sb.append(aacAudioObjectType2);
                        throw new ParserException(sb.toString());
                    }
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                    case 23: {
                        break;
                    }
                }
            }
            parseGaSpecificConfig(parsableBitArray, aacAudioObjectType2, bits2);
            switch (aacAudioObjectType2) {
                case 17:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23: {
                    final int bits3 = parsableBitArray.readBits(2);
                    if (bits3 != 2 && bits3 != 3) {
                        break;
                    }
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Unsupported epConfig: ");
                    sb2.append(bits3);
                    throw new ParserException(sb2.toString());
                }
            }
        }
        final int i = CodecSpecificDataUtil.AUDIO_SPECIFIC_CONFIG_CHANNEL_COUNT_TABLE[bits2];
        Assertions.checkArgument(i != -1 && b2);
        return (Pair<Integer, Integer>)Pair.create((Object)aacSamplingFrequency, (Object)i);
    }
    
    public static Pair<Integer, Integer> parseAacAudioSpecificConfig(final byte[] array) throws ParserException {
        return parseAacAudioSpecificConfig(new ParsableBitArray(array), false);
    }
    
    private static void parseGaSpecificConfig(final ParsableBitArray parsableBitArray, final int n, final int n2) {
        parsableBitArray.skipBits(1);
        if (parsableBitArray.readBit()) {
            parsableBitArray.skipBits(14);
        }
        final boolean bit = parsableBitArray.readBit();
        if (n2 != 0) {
            if (n == 6 || n == 20) {
                parsableBitArray.skipBits(3);
            }
            if (bit) {
                if (n == 22) {
                    parsableBitArray.skipBits(16);
                }
                if (n == 17 || n == 19 || n == 20 || n == 23) {
                    parsableBitArray.skipBits(3);
                }
                parsableBitArray.skipBits(1);
            }
            return;
        }
        throw new UnsupportedOperationException();
    }
    
    public static byte[][] splitNalUnits(final byte[] array) {
        if (!isNalStartCode(array, 0)) {
            return null;
        }
        final ArrayList<Integer> list = new ArrayList<Integer>();
        int nalStartCode = 0;
        do {
            list.add(nalStartCode);
        } while ((nalStartCode = findNalStartCode(array, nalStartCode + CodecSpecificDataUtil.NAL_START_CODE.length)) != -1);
        final byte[][] array2 = new byte[list.size()][];
        for (int i = 0; i < list.size(); ++i) {
            final int intValue = list.get(i);
            int n;
            if (i < list.size() - 1) {
                n = list.get(i + 1);
            }
            else {
                n = array.length;
            }
            final byte[] array3 = new byte[n - intValue];
            System.arraycopy(array, intValue, array3, 0, array3.length);
            array2[i] = array3;
        }
        return array2;
    }
}
