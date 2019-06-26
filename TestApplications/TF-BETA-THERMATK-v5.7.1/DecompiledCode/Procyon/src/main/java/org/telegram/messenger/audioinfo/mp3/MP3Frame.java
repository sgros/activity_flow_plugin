// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.audioinfo.mp3;

public class MP3Frame
{
    private final byte[] bytes;
    private final Header header;
    
    MP3Frame(final Header header, final byte[] bytes) {
        this.header = header;
        this.bytes = bytes;
    }
    
    public Header getHeader() {
        return this.header;
    }
    
    public int getNumberOfFrames() {
        int n;
        byte b;
        if (this.isXingFrame()) {
            final int xingOffset = this.header.getXingOffset();
            final byte[] bytes = this.bytes;
            if ((bytes[xingOffset + 7] & 0x1) == 0x0) {
                return -1;
            }
            n = ((bytes[xingOffset + 8] & 0xFF) << 24 | (bytes[xingOffset + 9] & 0xFF) << 16 | (bytes[xingOffset + 10] & 0xFF) << 8);
            b = bytes[xingOffset + 11];
        }
        else {
            if (!this.isVBRIFrame()) {
                return -1;
            }
            final int vbriOffset = this.header.getVBRIOffset();
            final byte[] bytes2 = this.bytes;
            n = ((bytes2[vbriOffset + 14] & 0xFF) << 24 | (bytes2[vbriOffset + 15] & 0xFF) << 16 | (bytes2[vbriOffset + 16] & 0xFF) << 8);
            b = bytes2[vbriOffset + 17];
        }
        return (b & 0xFF) | n;
    }
    
    public int getSize() {
        return this.bytes.length;
    }
    
    boolean isChecksumError() {
        final int protection = this.header.getProtection();
        boolean b2;
        final boolean b = b2 = false;
        if (protection == 0) {
            b2 = b;
            if (this.header.getLayer() == 1) {
                final CRC16 crc16 = new CRC16();
                crc16.update(this.bytes[2]);
                crc16.update(this.bytes[3]);
                for (int sideInfoSize = this.header.getSideInfoSize(), i = 0; i < sideInfoSize; ++i) {
                    crc16.update(this.bytes[i + 6]);
                }
                final byte[] bytes = this.bytes;
                final byte b3 = bytes[4];
                b2 = b;
                if (((bytes[5] & 0xFF) | (b3 & 0xFF) << 8) != crc16.getValue()) {
                    b2 = true;
                }
            }
        }
        return b2;
    }
    
    boolean isVBRIFrame() {
        final int vbriOffset = this.header.getVBRIOffset();
        final byte[] bytes = this.bytes;
        final int length = bytes.length;
        final boolean b = false;
        if (length < vbriOffset + 26) {
            return false;
        }
        boolean b2 = b;
        if (bytes[vbriOffset] == 86) {
            b2 = b;
            if (bytes[vbriOffset + 1] == 66) {
                b2 = b;
                if (bytes[vbriOffset + 2] == 82) {
                    b2 = b;
                    if (bytes[vbriOffset + 3] == 73) {
                        b2 = true;
                    }
                }
            }
        }
        return b2;
    }
    
    boolean isXingFrame() {
        final int xingOffset = this.header.getXingOffset();
        final byte[] bytes = this.bytes;
        if (bytes.length < xingOffset + 12) {
            return false;
        }
        if (xingOffset >= 0) {
            if (bytes.length >= xingOffset + 8) {
                if (bytes[xingOffset] == 88 && bytes[xingOffset + 1] == 105 && bytes[xingOffset + 2] == 110 && bytes[xingOffset + 3] == 103) {
                    return true;
                }
                final byte[] bytes2 = this.bytes;
                if (bytes2[xingOffset] == 73 && bytes2[xingOffset + 1] == 110 && bytes2[xingOffset + 2] == 102 && bytes2[xingOffset + 3] == 111) {
                    return true;
                }
            }
        }
        return false;
    }
    
    static final class CRC16
    {
        private short crc;
        
        CRC16() {
            this.crc = -1;
        }
        
        public short getValue() {
            return this.crc;
        }
        
        public void reset() {
            this.crc = -1;
        }
        
        public void update(final byte b) {
            this.update(b, 8);
        }
        
        public void update(final int n, int n2) {
            n2 = 1 << n2 - 1;
            do {
                final short crc = this.crc;
                int n3 = false ? 1 : 0;
                final boolean b = (crc & 0x8000) == 0x0;
                if ((n & n2) == 0x0) {
                    n3 = (true ? 1 : 0);
                }
                if (((b ? 1 : 0) ^ n3) != 0x0) {
                    this.crc <<= 1;
                    this.crc ^= (short)32773;
                }
                else {
                    this.crc <<= 1;
                }
            } while ((n2 >>>= 1) != 0);
        }
    }
    
    public static class Header
    {
        private static final int[][] BITRATES;
        private static final int[][] BITRATES_COLUMN;
        private static final int[][] FREQUENCIES;
        private static final int MPEG_BITRATE_FREE = 0;
        private static final int MPEG_BITRATE_RESERVED = 15;
        public static final int MPEG_CHANNEL_MODE_MONO = 3;
        private static final int MPEG_FRQUENCY_RESERVED = 3;
        public static final int MPEG_LAYER_1 = 3;
        public static final int MPEG_LAYER_2 = 2;
        public static final int MPEG_LAYER_3 = 1;
        private static final int MPEG_LAYER_RESERVED = 0;
        public static final int MPEG_PROTECTION_CRC = 0;
        public static final int MPEG_VERSION_1 = 3;
        public static final int MPEG_VERSION_2 = 2;
        public static final int MPEG_VERSION_2_5 = 0;
        private static final int MPEG_VERSION_RESERVED = 1;
        private static final int[][] SIDE_INFO_SIZES;
        private static final int[][] SIZE_COEFFICIENTS;
        private static final int[] SLOT_SIZES;
        private final int bitrate;
        private final int channelMode;
        private final int frequency;
        private final int layer;
        private final int padding;
        private final int protection;
        private final int version;
        
        static {
            FREQUENCIES = new int[][] { { 11025, -1, 22050, 44100 }, { 12000, -1, 24000, 48000 }, { 8000, -1, 16000, 32000 }, { -1, -1, -1, -1 } };
            BITRATES = new int[][] { { 0, 0, 0, 0, 0 }, { 32000, 32000, 32000, 32000, 8000 }, { 64000, 48000, 40000, 48000, 16000 }, { 96000, 56000, 48000, 56000, 24000 }, { 128000, 64000, 56000, 64000, 32000 }, { 160000, 80000, 64000, 80000, 40000 }, { 192000, 96000, 80000, 96000, 48000 }, { 224000, 112000, 96000, 112000, 56000 }, { 256000, 128000, 112000, 128000, 64000 }, { 288000, 160000, 128000, 144000, 80000 }, { 320000, 192000, 160000, 160000, 96000 }, { 352000, 224000, 192000, 176000, 112000 }, { 384000, 256000, 224000, 192000, 128000 }, { 416000, 320000, 256000, 224000, 144000 }, { 448000, 384000, 320000, 256000, 160000 }, { -1, -1, -1, -1, -1 } };
            BITRATES_COLUMN = new int[][] { { -1, 4, 4, 3 }, { -1, -1, -1, -1 }, { -1, 4, 4, 3 }, { -1, 2, 1, 0 } };
            SIZE_COEFFICIENTS = new int[][] { { -1, 72, 144, 12 }, { -1, -1, -1, -1 }, { -1, 72, 144, 12 }, { -1, 144, 144, 12 } };
            SLOT_SIZES = new int[] { -1, 1, 1, 4 };
            SIDE_INFO_SIZES = new int[][] { { 17, -1, 17, 32 }, { 17, -1, 17, 32 }, { 17, -1, 17, 32 }, { 9, -1, 9, 17 } };
        }
        
        public Header(int n, int i, final int n2) throws MP3Exception {
            this.version = (n >> 3 & 0x3);
            if (this.version == 1) {
                throw new MP3Exception("Reserved version");
            }
            this.layer = (n >> 1 & 0x3);
            if (this.layer == 0) {
                throw new MP3Exception("Reserved layer");
            }
            this.bitrate = (i >> 4 & 0xF);
            final int bitrate = this.bitrate;
            if (bitrate == 15) {
                throw new MP3Exception("Reserved bitrate");
            }
            if (bitrate == 0) {
                throw new MP3Exception("Free bitrate");
            }
            this.frequency = (i >> 2 & 0x3);
            if (this.frequency == 3) {
                throw new MP3Exception("Reserved frequency");
            }
            final int n3 = 6;
            this.channelMode = (n2 >> 6 & 0x3);
            this.padding = (i >> 1 & 0x1);
            this.protection = (n & 0x1);
            if (this.protection == 0) {
                n = n3;
            }
            else {
                n = 4;
            }
            i = n;
            if (this.layer == 1) {
                i = n + this.getSideInfoSize();
            }
            if (this.getFrameSize() >= i) {
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Frame size must be at least ");
            sb.append(i);
            throw new MP3Exception(sb.toString());
        }
        
        public int getBitrate() {
            return Header.BITRATES[this.bitrate][Header.BITRATES_COLUMN[this.version][this.layer]];
        }
        
        public int getChannelMode() {
            return this.channelMode;
        }
        
        public int getDuration() {
            return (int)this.getTotalDuration(this.getFrameSize());
        }
        
        public int getFrameSize() {
            return (Header.SIZE_COEFFICIENTS[this.version][this.layer] * this.getBitrate() / this.getFrequency() + this.padding) * Header.SLOT_SIZES[this.layer];
        }
        
        public int getFrequency() {
            return Header.FREQUENCIES[this.frequency][this.version];
        }
        
        public int getLayer() {
            return this.layer;
        }
        
        public int getProtection() {
            return this.protection;
        }
        
        public int getSampleCount() {
            if (this.layer == 3) {
                return 384;
            }
            return 1152;
        }
        
        public int getSideInfoSize() {
            return Header.SIDE_INFO_SIZES[this.channelMode][this.version];
        }
        
        public long getTotalDuration(long n) {
            final long n2 = n = this.getSampleCount() * n * 1000L / (this.getFrameSize() * this.getFrequency());
            if (this.getVersion() != 3) {
                n = n2;
                if (this.getChannelMode() == 3) {
                    n = n2 / 2L;
                }
            }
            return n;
        }
        
        public int getVBRIOffset() {
            return 36;
        }
        
        public int getVersion() {
            return this.version;
        }
        
        public int getXingOffset() {
            return this.getSideInfoSize() + 4;
        }
        
        public boolean isCompatible(final Header header) {
            return this.layer == header.layer && this.version == header.version && this.frequency == header.frequency && this.channelMode == header.channelMode;
        }
    }
}
