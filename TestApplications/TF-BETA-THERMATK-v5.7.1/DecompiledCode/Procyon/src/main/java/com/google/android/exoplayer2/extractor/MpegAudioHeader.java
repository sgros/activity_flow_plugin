// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor;

public final class MpegAudioHeader
{
    private static final int[] BITRATE_V1_L1;
    private static final int[] BITRATE_V1_L2;
    private static final int[] BITRATE_V1_L3;
    private static final int[] BITRATE_V2;
    private static final int[] BITRATE_V2_L1;
    private static final String[] MIME_TYPE_BY_LAYER;
    private static final int[] SAMPLING_RATE_V1;
    public int bitrate;
    public int channels;
    public int frameSize;
    public String mimeType;
    public int sampleRate;
    public int samplesPerFrame;
    public int version;
    
    static {
        MIME_TYPE_BY_LAYER = new String[] { "audio/mpeg-L1", "audio/mpeg-L2", "audio/mpeg" };
        SAMPLING_RATE_V1 = new int[] { 44100, 48000, 32000 };
        BITRATE_V1_L1 = new int[] { 32000, 64000, 96000, 128000, 160000, 192000, 224000, 256000, 288000, 320000, 352000, 384000, 416000, 448000 };
        BITRATE_V2_L1 = new int[] { 32000, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 144000, 160000, 176000, 192000, 224000, 256000 };
        BITRATE_V1_L2 = new int[] { 32000, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 160000, 192000, 224000, 256000, 320000, 384000 };
        BITRATE_V1_L3 = new int[] { 32000, 40000, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 160000, 192000, 224000, 256000, 320000 };
        BITRATE_V2 = new int[] { 8000, 16000, 24000, 32000, 40000, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 144000, 160000 };
    }
    
    public static int getFrameSize(int n) {
        if ((n & 0xFFE00000) != 0xFFE00000) {
            return -1;
        }
        final int n2 = n >>> 19 & 0x3;
        if (n2 == 1) {
            return -1;
        }
        final int n3 = n >>> 17 & 0x3;
        if (n3 == 0) {
            return -1;
        }
        final int n4 = n >>> 12 & 0xF;
        if (n4 == 0 || n4 == 15) {
            return -1;
        }
        final int n5 = n >>> 10 & 0x3;
        if (n5 == 3) {
            return -1;
        }
        final int n6 = MpegAudioHeader.SAMPLING_RATE_V1[n5];
        int n7;
        if (n2 == 2) {
            n7 = n6 / 2;
        }
        else {
            n7 = n6;
            if (n2 == 0) {
                n7 = n6 / 4;
            }
        }
        final int n8 = n >>> 9 & 0x1;
        if (n3 == 3) {
            if (n2 == 3) {
                n = MpegAudioHeader.BITRATE_V1_L1[n4 - 1];
            }
            else {
                n = MpegAudioHeader.BITRATE_V2_L1[n4 - 1];
            }
            return (n * 12 / n7 + n8) * 4;
        }
        if (n2 == 3) {
            if (n3 == 2) {
                n = MpegAudioHeader.BITRATE_V1_L2[n4 - 1];
            }
            else {
                n = MpegAudioHeader.BITRATE_V1_L3[n4 - 1];
            }
        }
        else {
            n = MpegAudioHeader.BITRATE_V2[n4 - 1];
        }
        int n9 = 144;
        if (n2 == 3) {
            return n * 144 / n7 + n8;
        }
        if (n3 == 1) {
            n9 = 72;
        }
        return n9 * n / n7 + n8;
    }
    
    public static boolean populateHeader(int n, final MpegAudioHeader mpegAudioHeader) {
        if ((n & 0xFFE00000) != 0xFFE00000) {
            return false;
        }
        final int n2 = n >>> 19 & 0x3;
        if (n2 == 1) {
            return false;
        }
        final int n3 = n >>> 17 & 0x3;
        if (n3 == 0) {
            return false;
        }
        final int n4 = n >>> 12 & 0xF;
        if (n4 == 0 || n4 == 15) {
            return false;
        }
        final int n5 = n >>> 10 & 0x3;
        if (n5 == 3) {
            return false;
        }
        final int n6 = MpegAudioHeader.SAMPLING_RATE_V1[n5];
        int n7;
        if (n2 == 2) {
            n7 = n6 / 2;
        }
        else {
            n7 = n6;
            if (n2 == 0) {
                n7 = n6 / 4;
            }
        }
        final int n8 = n >>> 9 & 0x1;
        int n9 = 1152;
        int n11;
        int n12;
        if (n3 == 3) {
            int n10;
            if (n2 == 3) {
                n10 = MpegAudioHeader.BITRATE_V1_L1[n4 - 1];
            }
            else {
                n10 = MpegAudioHeader.BITRATE_V2_L1[n4 - 1];
            }
            n11 = (n10 * 12 / n7 + n8) * 4;
            n12 = 384;
        }
        else {
            int n13 = 144;
            if (n2 == 3) {
                int n14;
                if (n3 == 2) {
                    n14 = MpegAudioHeader.BITRATE_V1_L2[n4 - 1];
                }
                else {
                    n14 = MpegAudioHeader.BITRATE_V1_L3[n4 - 1];
                }
                n11 = n14 * 144 / n7 + n8;
                n12 = 1152;
            }
            else {
                final int n15 = MpegAudioHeader.BITRATE_V2[n4 - 1];
                if (n3 == 1) {
                    n9 = 576;
                }
                if (n3 == 1) {
                    n13 = 72;
                }
                final int n16 = n13 * n15 / n7 + n8;
                n12 = n9;
                n11 = n16;
            }
        }
        final int n17 = n11 * 8 * n7 / n12;
        final String s = MpegAudioHeader.MIME_TYPE_BY_LAYER[3 - n3];
        if ((n >> 6 & 0x3) == 0x3) {
            n = 1;
        }
        else {
            n = 2;
        }
        mpegAudioHeader.setValues(n2, s, n11, n7, n, n17, n12);
        return true;
    }
    
    private void setValues(final int version, final String mimeType, final int frameSize, final int sampleRate, final int channels, final int bitrate, final int samplesPerFrame) {
        this.version = version;
        this.mimeType = mimeType;
        this.frameSize = frameSize;
        this.sampleRate = sampleRate;
        this.channels = channels;
        this.bitrate = bitrate;
        this.samplesPerFrame = samplesPerFrame;
    }
}
