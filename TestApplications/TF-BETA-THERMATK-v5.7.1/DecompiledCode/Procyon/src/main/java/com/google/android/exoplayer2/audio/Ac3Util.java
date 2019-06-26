// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.util.ParsableBitArray;
import java.util.List;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.nio.ByteBuffer;

public final class Ac3Util
{
    private static final int[] BITRATE_BY_HALF_FRMSIZECOD;
    private static final int[] BLOCKS_PER_SYNCFRAME_BY_NUMBLKSCOD;
    private static final int[] CHANNEL_COUNT_BY_ACMOD;
    private static final int[] SAMPLE_RATE_BY_FSCOD;
    private static final int[] SAMPLE_RATE_BY_FSCOD2;
    private static final int[] SYNCFRAME_SIZE_WORDS_BY_HALF_FRMSIZECOD_44_1;
    
    static {
        BLOCKS_PER_SYNCFRAME_BY_NUMBLKSCOD = new int[] { 1, 2, 3, 6 };
        SAMPLE_RATE_BY_FSCOD = new int[] { 48000, 44100, 32000 };
        SAMPLE_RATE_BY_FSCOD2 = new int[] { 24000, 22050, 16000 };
        CHANNEL_COUNT_BY_ACMOD = new int[] { 2, 1, 2, 3, 3, 4, 4, 5 };
        BITRATE_BY_HALF_FRMSIZECOD = new int[] { 32, 40, 48, 56, 64, 80, 96, 112, 128, 160, 192, 224, 256, 320, 384, 448, 512, 576, 640 };
        SYNCFRAME_SIZE_WORDS_BY_HALF_FRMSIZECOD_44_1 = new int[] { 69, 87, 104, 121, 139, 174, 208, 243, 278, 348, 417, 487, 557, 696, 835, 975, 1114, 1253, 1393 };
    }
    
    public static int findTrueHdSyncframeOffset(final ByteBuffer byteBuffer) {
        final int position = byteBuffer.position();
        for (int limit = byteBuffer.limit(), i = position; i <= limit - 10; ++i) {
            if ((byteBuffer.getInt(i + 4) & 0xFEFFFFFF) == 0xBA6F72F8) {
                return i - position;
            }
        }
        return -1;
    }
    
    public static int getAc3SyncframeAudioSampleCount() {
        return 1536;
    }
    
    private static int getAc3SyncframeSize(int n, int n2) {
        final int n3 = n2 / 2;
        if (n >= 0) {
            final int[] sample_RATE_BY_FSCOD = Ac3Util.SAMPLE_RATE_BY_FSCOD;
            if (n < sample_RATE_BY_FSCOD.length && n2 >= 0) {
                final int[] syncframe_SIZE_WORDS_BY_HALF_FRMSIZECOD_44_1 = Ac3Util.SYNCFRAME_SIZE_WORDS_BY_HALF_FRMSIZECOD_44_1;
                if (n3 < syncframe_SIZE_WORDS_BY_HALF_FRMSIZECOD_44_1.length) {
                    n = sample_RATE_BY_FSCOD[n];
                    if (n == 44100) {
                        return (syncframe_SIZE_WORDS_BY_HALF_FRMSIZECOD_44_1[n3] + n2 % 2) * 2;
                    }
                    n2 = Ac3Util.BITRATE_BY_HALF_FRMSIZECOD[n3];
                    if (n == 32000) {
                        return n2 * 6;
                    }
                    return n2 * 4;
                }
            }
        }
        return -1;
    }
    
    public static Format parseAc3AnnexFFormat(final ParsableByteArray parsableByteArray, final String s, final String s2, final DrmInitData drmInitData) {
        final int n = Ac3Util.SAMPLE_RATE_BY_FSCOD[(parsableByteArray.readUnsignedByte() & 0xC0) >> 6];
        final int unsignedByte = parsableByteArray.readUnsignedByte();
        int n2 = Ac3Util.CHANNEL_COUNT_BY_ACMOD[(unsignedByte & 0x38) >> 3];
        if ((unsignedByte & 0x4) != 0x0) {
            ++n2;
        }
        return Format.createAudioSampleFormat(s, "audio/ac3", null, -1, -1, n2, n, null, drmInitData, 0, s2);
    }
    
    public static SyncFrameInfo parseAc3SyncframeInfo(final ParsableBitArray parsableBitArray) {
        final int position = parsableBitArray.getPosition();
        parsableBitArray.skipBits(40);
        final boolean b = parsableBitArray.readBits(5) == 16;
        parsableBitArray.setPosition(position);
        final int n = -1;
        int n3;
        String s;
        int n8;
        int n9;
        int ac3SyncframeSize;
        int n10;
        if (b) {
            parsableBitArray.skipBits(16);
            final int bits = parsableBitArray.readBits(2);
            int n2;
            if (bits != 0) {
                if (bits != 1) {
                    if (bits != 2) {
                        n2 = n;
                    }
                    else {
                        n2 = 2;
                    }
                }
                else {
                    n2 = 1;
                }
            }
            else {
                n2 = 0;
            }
            parsableBitArray.skipBits(3);
            final int bits2 = parsableBitArray.readBits(11);
            final int bits3 = parsableBitArray.readBits(2);
            int bits4;
            int n4;
            if (bits3 == 3) {
                n3 = Ac3Util.SAMPLE_RATE_BY_FSCOD2[parsableBitArray.readBits(2)];
                bits4 = 3;
                n4 = 6;
            }
            else {
                bits4 = parsableBitArray.readBits(2);
                n4 = Ac3Util.BLOCKS_PER_SYNCFRAME_BY_NUMBLKSCOD[bits4];
                n3 = Ac3Util.SAMPLE_RATE_BY_FSCOD[bits3];
            }
            final int bits5 = parsableBitArray.readBits(3);
            final int bit = parsableBitArray.readBit() ? 1 : 0;
            final int n5 = Ac3Util.CHANNEL_COUNT_BY_ACMOD[bits5];
            parsableBitArray.skipBits(10);
            if (parsableBitArray.readBit()) {
                parsableBitArray.skipBits(8);
            }
            if (bits5 == 0) {
                parsableBitArray.skipBits(5);
                if (parsableBitArray.readBit()) {
                    parsableBitArray.skipBits(8);
                }
            }
            if (n2 == 1 && parsableBitArray.readBit()) {
                parsableBitArray.skipBits(16);
            }
            if (parsableBitArray.readBit()) {
                if (bits5 > 2) {
                    parsableBitArray.skipBits(2);
                }
                if ((bits5 & 0x1) != 0x0 && bits5 > 2) {
                    parsableBitArray.skipBits(6);
                }
                if ((bits5 & 0x4) != 0x0) {
                    parsableBitArray.skipBits(6);
                }
                if (bit != 0 && parsableBitArray.readBit()) {
                    parsableBitArray.skipBits(5);
                }
                if (n2 == 0) {
                    if (parsableBitArray.readBit()) {
                        parsableBitArray.skipBits(6);
                    }
                    if (bits5 == 0 && parsableBitArray.readBit()) {
                        parsableBitArray.skipBits(6);
                    }
                    if (parsableBitArray.readBit()) {
                        parsableBitArray.skipBits(6);
                    }
                    final int bits6 = parsableBitArray.readBits(2);
                    if (bits6 == 1) {
                        parsableBitArray.skipBits(5);
                    }
                    else if (bits6 == 2) {
                        parsableBitArray.skipBits(12);
                    }
                    else if (bits6 == 3) {
                        final int bits7 = parsableBitArray.readBits(5);
                        if (parsableBitArray.readBit()) {
                            parsableBitArray.skipBits(5);
                            if (parsableBitArray.readBit()) {
                                parsableBitArray.skipBits(4);
                            }
                            if (parsableBitArray.readBit()) {
                                parsableBitArray.skipBits(4);
                            }
                            if (parsableBitArray.readBit()) {
                                parsableBitArray.skipBits(4);
                            }
                            if (parsableBitArray.readBit()) {
                                parsableBitArray.skipBits(4);
                            }
                            if (parsableBitArray.readBit()) {
                                parsableBitArray.skipBits(4);
                            }
                            if (parsableBitArray.readBit()) {
                                parsableBitArray.skipBits(4);
                            }
                            if (parsableBitArray.readBit()) {
                                parsableBitArray.skipBits(4);
                            }
                            if (parsableBitArray.readBit()) {
                                if (parsableBitArray.readBit()) {
                                    parsableBitArray.skipBits(4);
                                }
                                if (parsableBitArray.readBit()) {
                                    parsableBitArray.skipBits(4);
                                }
                            }
                        }
                        if (parsableBitArray.readBit()) {
                            parsableBitArray.skipBits(5);
                            if (parsableBitArray.readBit()) {
                                parsableBitArray.skipBits(7);
                                if (parsableBitArray.readBit()) {
                                    parsableBitArray.skipBits(8);
                                }
                            }
                        }
                        parsableBitArray.skipBits((bits7 + 2) * 8);
                        parsableBitArray.byteAlign();
                    }
                    if (bits5 < 2) {
                        if (parsableBitArray.readBit()) {
                            parsableBitArray.skipBits(14);
                        }
                        if (bits5 == 0 && parsableBitArray.readBit()) {
                            parsableBitArray.skipBits(14);
                        }
                    }
                    if (parsableBitArray.readBit()) {
                        if (bits4 == 0) {
                            parsableBitArray.skipBits(5);
                        }
                        else {
                            for (int i = 0; i < n4; ++i) {
                                if (parsableBitArray.readBit()) {
                                    parsableBitArray.skipBits(5);
                                }
                            }
                        }
                    }
                }
            }
            if (parsableBitArray.readBit()) {
                parsableBitArray.skipBits(5);
                if (bits5 == 2) {
                    parsableBitArray.skipBits(4);
                }
                if (bits5 >= 6) {
                    parsableBitArray.skipBits(2);
                }
                if (parsableBitArray.readBit()) {
                    parsableBitArray.skipBits(8);
                }
                if (bits5 == 0 && parsableBitArray.readBit()) {
                    parsableBitArray.skipBits(8);
                }
                if (bits3 < 3) {
                    parsableBitArray.skipBit();
                }
            }
            if (n2 == 0 && bits4 != 3) {
                parsableBitArray.skipBit();
            }
            if (n2 == 2 && (bits4 == 3 || parsableBitArray.readBit())) {
                parsableBitArray.skipBits(6);
            }
            if (parsableBitArray.readBit() && parsableBitArray.readBits(6) == 1 && parsableBitArray.readBits(8) == 1) {
                s = "audio/eac3-joc";
            }
            else {
                s = "audio/eac3";
            }
            final int n6 = (bits2 + 1) * 2;
            final int n7 = n4 * 256;
            n8 = n5 + bit;
            n9 = n2;
            ac3SyncframeSize = n6;
            n10 = n7;
        }
        else {
            parsableBitArray.skipBits(32);
            final int bits8 = parsableBitArray.readBits(2);
            ac3SyncframeSize = getAc3SyncframeSize(bits8, parsableBitArray.readBits(6));
            parsableBitArray.skipBits(8);
            final int bits9 = parsableBitArray.readBits(3);
            if ((bits9 & 0x1) != 0x0 && bits9 != 1) {
                parsableBitArray.skipBits(2);
            }
            if ((bits9 & 0x4) != 0x0) {
                parsableBitArray.skipBits(2);
            }
            if (bits9 == 2) {
                parsableBitArray.skipBits(2);
            }
            n3 = Ac3Util.SAMPLE_RATE_BY_FSCOD[bits8];
            final int bit2 = parsableBitArray.readBit() ? 1 : 0;
            final int n11 = Ac3Util.CHANNEL_COUNT_BY_ACMOD[bits9];
            s = "audio/ac3";
            n8 = n11 + bit2;
            n9 = -1;
            n10 = 1536;
        }
        return new SyncFrameInfo(s, n9, n8, n3, ac3SyncframeSize, n10);
    }
    
    public static int parseAc3SyncframeSize(final byte[] array) {
        if (array.length < 6) {
            return -1;
        }
        if ((array[5] & 0xFF) >> 3 == 16) {
            return (((array[3] & 0xFF) | (array[2] & 0x7) << 8) + 1) * 2;
        }
        return getAc3SyncframeSize((array[4] & 0xC0) >> 6, array[4] & 0x3F);
    }
    
    public static Format parseEAc3AnnexFFormat(final ParsableByteArray parsableByteArray, final String s, final String s2, final DrmInitData drmInitData) {
        parsableByteArray.skipBytes(2);
        final int n = Ac3Util.SAMPLE_RATE_BY_FSCOD[(parsableByteArray.readUnsignedByte() & 0xC0) >> 6];
        final int unsignedByte = parsableByteArray.readUnsignedByte();
        int n3;
        final int n2 = n3 = Ac3Util.CHANNEL_COUNT_BY_ACMOD[(unsignedByte & 0xE) >> 1];
        if ((unsignedByte & 0x1) != 0x0) {
            n3 = n2 + 1;
        }
        int n4 = n3;
        if ((parsableByteArray.readUnsignedByte() & 0x1E) >> 1 > 0) {
            n4 = n3;
            if ((0x2 & parsableByteArray.readUnsignedByte()) != 0x0) {
                n4 = n3 + 2;
            }
        }
        String s3;
        if (parsableByteArray.bytesLeft() > 0 && (parsableByteArray.readUnsignedByte() & 0x1) != 0x0) {
            s3 = "audio/eac3-joc";
        }
        else {
            s3 = "audio/eac3";
        }
        return Format.createAudioSampleFormat(s, s3, null, -1, -1, n4, n, null, drmInitData, 0, s2);
    }
    
    public static int parseEAc3SyncframeAudioSampleCount(final ByteBuffer byteBuffer) {
        final byte value = byteBuffer.get(byteBuffer.position() + 4);
        int n = 6;
        if ((value & 0xC0) >> 6 != 3) {
            n = Ac3Util.BLOCKS_PER_SYNCFRAME_BY_NUMBLKSCOD[(byteBuffer.get(byteBuffer.position() + 4) & 0x30) >> 4];
        }
        return n * 256;
    }
    
    public static int parseTrueHdSyncframeAudioSampleCount(final ByteBuffer byteBuffer, final int n) {
        final boolean b = (byteBuffer.get(byteBuffer.position() + n + 7) & 0xFF) == 0xBB;
        final int position = byteBuffer.position();
        int n2;
        if (b) {
            n2 = 9;
        }
        else {
            n2 = 8;
        }
        return 40 << (byteBuffer.get(position + n + n2) >> 4 & 0x7);
    }
    
    public static int parseTrueHdSyncframeAudioSampleCount(final byte[] array) {
        final byte b = array[4];
        boolean b2 = false;
        if (b == -8 && array[5] == 114 && array[6] == 111 && (array[7] & 0xFE) == 0xBA) {
            if ((array[7] & 0xFF) == 0xBB) {
                b2 = true;
            }
            int n;
            if (b2) {
                n = 9;
            }
            else {
                n = 8;
            }
            return 40 << (array[n] >> 4 & 0x7);
        }
        return 0;
    }
    
    public static final class SyncFrameInfo
    {
        public final int channelCount;
        public final int frameSize;
        public final String mimeType;
        public final int sampleCount;
        public final int sampleRate;
        public final int streamType;
        
        private SyncFrameInfo(final String mimeType, final int streamType, final int channelCount, final int sampleRate, final int frameSize, final int sampleCount) {
            this.mimeType = mimeType;
            this.streamType = streamType;
            this.channelCount = channelCount;
            this.sampleRate = sampleRate;
            this.frameSize = frameSize;
            this.sampleCount = sampleCount;
        }
    }
}
