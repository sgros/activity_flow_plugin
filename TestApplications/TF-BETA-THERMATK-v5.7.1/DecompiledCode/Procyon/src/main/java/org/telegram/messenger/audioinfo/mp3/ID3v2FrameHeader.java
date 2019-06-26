// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.audioinfo.mp3;

import java.io.IOException;

public class ID3v2FrameHeader
{
    private int bodySize;
    private boolean compression;
    private int dataLengthIndicator;
    private boolean encryption;
    private String frameId;
    private int headerSize;
    private boolean unsynchronization;
    
    public ID3v2FrameHeader(final ID3v2TagBody id3v2TagBody) throws IOException, ID3v2Exception {
        final long position = id3v2TagBody.getPosition();
        final ID3v2DataInput data = id3v2TagBody.getData();
        final int version = id3v2TagBody.getTagHeader().getVersion();
        byte b = 2;
        if (version == 2) {
            this.frameId = new String(data.readFully(3), "ISO-8859-1");
        }
        else {
            this.frameId = new String(data.readFully(4), "ISO-8859-1");
        }
        final int version2 = id3v2TagBody.getTagHeader().getVersion();
        int n = 8;
        if (version2 == 2) {
            this.bodySize = ((data.readByte() & 0xFF) << 16 | (data.readByte() & 0xFF) << 8 | (data.readByte() & 0xFF));
        }
        else if (id3v2TagBody.getTagHeader().getVersion() == 3) {
            this.bodySize = data.readInt();
        }
        else {
            this.bodySize = data.readSyncsafeInt();
        }
        if (id3v2TagBody.getTagHeader().getVersion() > 2) {
            data.readByte();
            final byte byte1 = data.readByte();
            final int version3 = id3v2TagBody.getTagHeader().getVersion();
            byte b2 = 64;
            final boolean b3 = false;
            byte b4;
            byte b5;
            if (version3 == 3) {
                n = 128;
                b = 0;
                b4 = 32;
                b5 = 0;
            }
            else {
                b4 = 64;
                b2 = 4;
                b5 = 1;
            }
            this.compression = ((n & byte1) != 0x0);
            this.unsynchronization = ((byte1 & b) != 0x0);
            boolean encryption = b3;
            if ((byte1 & b2) != 0x0) {
                encryption = true;
            }
            this.encryption = encryption;
            if (id3v2TagBody.getTagHeader().getVersion() == 3) {
                if (this.compression) {
                    this.dataLengthIndicator = data.readInt();
                    this.bodySize -= 4;
                }
                if (this.encryption) {
                    data.readByte();
                    --this.bodySize;
                }
                if ((byte1 & b4) != 0x0) {
                    data.readByte();
                    --this.bodySize;
                }
            }
            else {
                if ((byte1 & b4) != 0x0) {
                    data.readByte();
                    --this.bodySize;
                }
                if (this.encryption) {
                    data.readByte();
                    --this.bodySize;
                }
                if ((byte1 & b5) != 0x0) {
                    this.dataLengthIndicator = data.readSyncsafeInt();
                    this.bodySize -= 4;
                }
            }
        }
        this.headerSize = (int)(id3v2TagBody.getPosition() - position);
    }
    
    public int getBodySize() {
        return this.bodySize;
    }
    
    public int getDataLengthIndicator() {
        return this.dataLengthIndicator;
    }
    
    public String getFrameId() {
        return this.frameId;
    }
    
    public int getHeaderSize() {
        return this.headerSize;
    }
    
    public boolean isCompression() {
        return this.compression;
    }
    
    public boolean isEncryption() {
        return this.encryption;
    }
    
    public boolean isPadding() {
        boolean b = false;
        for (int i = 0; i < this.frameId.length(); ++i) {
            if (this.frameId.charAt(0) != '\0') {
                return false;
            }
        }
        if (this.bodySize == 0) {
            b = true;
        }
        return b;
    }
    
    public boolean isUnsynchronization() {
        return this.unsynchronization;
    }
    
    public boolean isValid() {
        boolean b = false;
        for (int i = 0; i < this.frameId.length(); ++i) {
            if ((this.frameId.charAt(i) < 'A' || this.frameId.charAt(i) > 'Z') && (this.frameId.charAt(i) < '0' || this.frameId.charAt(i) > '9')) {
                return false;
            }
        }
        if (this.bodySize > 0) {
            b = true;
        }
        return b;
    }
    
    @Override
    public String toString() {
        return String.format("%s[id=%s, bodysize=%d]", ID3v2FrameHeader.class.getSimpleName(), this.frameId, this.bodySize);
    }
}
