// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.audioinfo.mp3;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.telegram.messenger.audioinfo.util.PositionInputStream;
import java.io.InputStream;

public class ID3v2TagHeader
{
    private boolean compression;
    private int footerSize;
    private int headerSize;
    private int paddingSize;
    private int revision;
    private int totalTagSize;
    private boolean unsynchronization;
    private int version;
    
    public ID3v2TagHeader(final InputStream inputStream) throws IOException, ID3v2Exception {
        this(new PositionInputStream(inputStream));
    }
    
    ID3v2TagHeader(final PositionInputStream positionInputStream) throws IOException, ID3v2Exception {
        boolean unsynchronization = false;
        final boolean b = false;
        this.version = 0;
        this.revision = 0;
        this.headerSize = 0;
        this.totalTagSize = 0;
        this.paddingSize = 0;
        this.footerSize = 0;
        final long position = positionInputStream.getPosition();
        final ID3v2DataInput id3v2DataInput = new ID3v2DataInput(positionInputStream);
        final String s = new String(id3v2DataInput.readFully(3), "ISO-8859-1");
        if (!"ID3".equals(s)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid ID3 identifier: ");
            sb.append(s);
            throw new ID3v2Exception(sb.toString());
        }
        this.version = id3v2DataInput.readByte();
        final int version = this.version;
        if (version != 2 && version != 3 && version != 4) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Unsupported ID3v2 version: ");
            sb2.append(this.version);
            throw new ID3v2Exception(sb2.toString());
        }
        this.revision = id3v2DataInput.readByte();
        final byte byte1 = id3v2DataInput.readByte();
        this.totalTagSize = id3v2DataInput.readSyncsafeInt() + 10;
        if (this.version == 2) {
            this.unsynchronization = ((byte1 & 0x80) != 0x0);
            boolean compression = b;
            if ((byte1 & 0x40) != 0x0) {
                compression = true;
            }
            this.compression = compression;
        }
        else {
            if ((byte1 & 0x80) != 0x0) {
                unsynchronization = true;
            }
            this.unsynchronization = unsynchronization;
            if ((byte1 & 0x40) != 0x0) {
                if (this.version == 3) {
                    final int int1 = id3v2DataInput.readInt();
                    id3v2DataInput.readByte();
                    id3v2DataInput.readByte();
                    this.paddingSize = id3v2DataInput.readInt();
                    id3v2DataInput.skipFully(int1 - 6);
                }
                else {
                    id3v2DataInput.skipFully(id3v2DataInput.readSyncsafeInt() - 4);
                }
            }
            if (this.version >= 4 && (byte1 & 0x10) != 0x0) {
                this.footerSize = 10;
                this.totalTagSize += 10;
            }
        }
        this.headerSize = (int)(positionInputStream.getPosition() - position);
    }
    
    public int getFooterSize() {
        return this.footerSize;
    }
    
    public int getHeaderSize() {
        return this.headerSize;
    }
    
    public int getPaddingSize() {
        return this.paddingSize;
    }
    
    public int getRevision() {
        return this.revision;
    }
    
    public int getTotalTagSize() {
        return this.totalTagSize;
    }
    
    public int getVersion() {
        return this.version;
    }
    
    public boolean isCompression() {
        return this.compression;
    }
    
    public boolean isUnsynchronization() {
        return this.unsynchronization;
    }
    
    public ID3v2TagBody tagBody(final InputStream inputStream) throws IOException, ID3v2Exception {
        if (this.compression) {
            throw new ID3v2Exception("Tag compression is not supported");
        }
        if (this.version < 4 && this.unsynchronization) {
            final byte[] fully = new ID3v2DataInput(inputStream).readFully(this.totalTagSize - this.headerSize);
            final int length = fully.length;
            int i = 0;
            int length2 = 0;
            int n = 0;
            while (i < length) {
                final byte b = fully[i];
                int n2 = 0;
                Label_0091: {
                    if (n != 0) {
                        n2 = length2;
                        if (b == 0) {
                            break Label_0091;
                        }
                    }
                    fully[length2] = b;
                    n2 = length2 + 1;
                }
                if (b == -1) {
                    n = 1;
                }
                else {
                    n = 0;
                }
                ++i;
                length2 = n2;
            }
            return new ID3v2TagBody(new ByteArrayInputStream(fully, 0, length2), this.headerSize, length2, this);
        }
        final int headerSize = this.headerSize;
        return new ID3v2TagBody(inputStream, headerSize, this.totalTagSize - headerSize - this.footerSize, this);
    }
    
    @Override
    public String toString() {
        return String.format("%s[version=%s, totalTagSize=%d]", ID3v2TagHeader.class.getSimpleName(), this.version, this.totalTagSize);
    }
}
