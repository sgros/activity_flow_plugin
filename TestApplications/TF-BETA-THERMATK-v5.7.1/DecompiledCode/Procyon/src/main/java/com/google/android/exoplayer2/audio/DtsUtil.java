// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.audio;

import java.util.List;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmInitData;
import java.nio.ByteBuffer;
import java.util.Arrays;
import com.google.android.exoplayer2.util.ParsableBitArray;

public final class DtsUtil
{
    private static final int[] CHANNELS_BY_AMODE;
    private static final int[] SAMPLE_RATE_BY_SFREQ;
    private static final int[] TWICE_BITRATE_KBPS_BY_RATE;
    
    static {
        CHANNELS_BY_AMODE = new int[] { 1, 2, 2, 2, 2, 3, 3, 4, 4, 5, 6, 6, 6, 7, 8, 8 };
        SAMPLE_RATE_BY_SFREQ = new int[] { -1, 8000, 16000, 32000, -1, -1, 11025, 22050, 44100, -1, -1, 12000, 24000, 48000, -1, -1 };
        TWICE_BITRATE_KBPS_BY_RATE = new int[] { 64, 112, 128, 192, 224, 256, 384, 448, 512, 640, 768, 896, 1024, 1152, 1280, 1536, 1920, 2048, 2304, 2560, 2688, 2816, 2823, 2944, 3072, 3840, 4096, 6144, 7680 };
    }
    
    public static int getDtsFrameSize(final byte[] array) {
        final int n = 0;
        final byte b = array[0];
        int n4 = 0;
        int n5 = 0;
        Label_0164: {
            int n2 = 0;
            byte b2 = 0;
            Label_0048: {
                if (b != -2) {
                    int n3;
                    byte b3;
                    if (b != -1) {
                        if (b != 31) {
                            n2 = ((array[5] & 0x3) << 12 | (array[6] & 0xFF) << 4);
                            b2 = array[7];
                            break Label_0048;
                        }
                        n3 = ((array[6] & 0x3) << 12 | (array[7] & 0xFF) << 4);
                        b3 = array[8];
                    }
                    else {
                        n3 = ((array[7] & 0x3) << 12 | (array[6] & 0xFF) << 4);
                        b3 = array[9];
                    }
                    n4 = ((b3 & 0x3C) >> 2 | n3) + 1;
                    n5 = 1;
                    break Label_0164;
                }
                n2 = ((array[4] & 0x3) << 12 | (array[7] & 0xFF) << 4);
                b2 = array[6];
            }
            n4 = ((b2 & 0xF0) >> 4 | n2) + 1;
            n5 = n;
        }
        int n6 = n4;
        if (n5 != 0) {
            n6 = n4 * 16 / 14;
        }
        return n6;
    }
    
    private static ParsableBitArray getNormalizedFrameHeader(final byte[] original) {
        if (original[0] == 127) {
            return new ParsableBitArray(original);
        }
        final byte[] copy = Arrays.copyOf(original, original.length);
        if (isLittleEndianFrameHeader(copy)) {
            for (int i = 0; i < copy.length - 1; i += 2) {
                final byte b = copy[i];
                final int n = i + 1;
                copy[i] = copy[n];
                copy[n] = b;
            }
        }
        final ParsableBitArray parsableBitArray = new ParsableBitArray(copy);
        if (copy[0] == 31) {
            final ParsableBitArray parsableBitArray2 = new ParsableBitArray(copy);
            while (parsableBitArray2.bitsLeft() >= 16) {
                parsableBitArray2.skipBits(2);
                parsableBitArray.putInt(parsableBitArray2.readBits(14), 14);
            }
        }
        parsableBitArray.reset(copy);
        return parsableBitArray;
    }
    
    private static boolean isLittleEndianFrameHeader(final byte[] array) {
        boolean b = false;
        if (array[0] == -2 || array[0] == -1) {
            b = true;
        }
        return b;
    }
    
    public static boolean isSyncWord(final int n) {
        return n == 2147385345 || n == -25230976 || n == 536864768 || n == -14745368;
    }
    
    public static int parseDtsAudioSampleCount(final ByteBuffer byteBuffer) {
        final int position = byteBuffer.position();
        final byte value = byteBuffer.get(position);
        int n = 0;
        byte b = 0;
        Label_0049: {
            if (value != -2) {
                byte b2;
                if (value != -1) {
                    if (value != 31) {
                        n = (byteBuffer.get(position + 4) & 0x1) << 6;
                        b = byteBuffer.get(position + 5);
                        break Label_0049;
                    }
                    n = (byteBuffer.get(position + 5) & 0x7) << 4;
                    b2 = byteBuffer.get(position + 6);
                }
                else {
                    n = (byteBuffer.get(position + 4) & 0x7) << 4;
                    b2 = byteBuffer.get(position + 7);
                }
                final int n2 = b2 & 0x3C;
                return ((n2 >> 2 | n) + 1) * 32;
            }
            n = (byteBuffer.get(position + 5) & 0x1) << 6;
            b = byteBuffer.get(position + 4);
        }
        final int n2 = b & 0xFC;
        return ((n2 >> 2 | n) + 1) * 32;
    }
    
    public static int parseDtsAudioSampleCount(final byte[] array) {
        final byte b = array[0];
        int n = 0;
        byte b2 = 0;
        Label_0034: {
            if (b != -2) {
                byte b3;
                if (b != -1) {
                    if (b != 31) {
                        n = (array[4] & 0x1) << 6;
                        b2 = array[5];
                        break Label_0034;
                    }
                    n = (array[5] & 0x7) << 4;
                    b3 = array[6];
                }
                else {
                    n = (array[4] & 0x7) << 4;
                    b3 = array[7];
                }
                final int n2 = b3 & 0x3C;
                return ((n2 >> 2 | n) + 1) * 32;
            }
            n = (array[5] & 0x1) << 6;
            b2 = array[4];
        }
        final int n2 = b2 & 0xFC;
        return ((n2 >> 2 | n) + 1) * 32;
    }
    
    public static Format parseDtsFormat(final byte[] array, final String s, final String s2, final DrmInitData drmInitData) {
        final ParsableBitArray normalizedFrameHeader = getNormalizedFrameHeader(array);
        normalizedFrameHeader.skipBits(60);
        final int n = DtsUtil.CHANNELS_BY_AMODE[normalizedFrameHeader.readBits(6)];
        final int n2 = DtsUtil.SAMPLE_RATE_BY_SFREQ[normalizedFrameHeader.readBits(4)];
        final int bits = normalizedFrameHeader.readBits(5);
        final int[] twice_BITRATE_KBPS_BY_RATE = DtsUtil.TWICE_BITRATE_KBPS_BY_RATE;
        int n3;
        if (bits >= twice_BITRATE_KBPS_BY_RATE.length) {
            n3 = -1;
        }
        else {
            n3 = twice_BITRATE_KBPS_BY_RATE[bits] * 1000 / 2;
        }
        normalizedFrameHeader.skipBits(10);
        int n4;
        if (normalizedFrameHeader.readBits(2) > 0) {
            n4 = 1;
        }
        else {
            n4 = 0;
        }
        return Format.createAudioSampleFormat(s, "audio/vnd.dts", null, n3, -1, n + n4, n2, null, drmInitData, 0, s2);
    }
}
