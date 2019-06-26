// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.audioinfo.mp3;

import java.util.zip.InflaterInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.telegram.messenger.audioinfo.util.RangeInputStream;

public class ID3v2TagBody
{
    private final ID3v2DataInput data;
    private final RangeInputStream input;
    private final ID3v2TagHeader tagHeader;
    
    ID3v2TagBody(final InputStream inputStream, final long n, final int n2, final ID3v2TagHeader tagHeader) throws IOException {
        this.input = new RangeInputStream(inputStream, n, n2);
        this.data = new ID3v2DataInput(this.input);
        this.tagHeader = tagHeader;
    }
    
    public ID3v2FrameBody frameBody(final ID3v2FrameHeader id3v2FrameHeader) throws IOException, ID3v2Exception {
        int length = id3v2FrameHeader.getBodySize();
        InputStream input = this.input;
        if (id3v2FrameHeader.isUnsynchronization()) {
            final byte[] fully = this.data.readFully(id3v2FrameHeader.getBodySize());
            final int length2 = fully.length;
            int i = 0;
            length = 0;
            int n = 0;
            while (i < length2) {
                final byte b = fully[i];
                int n2 = 0;
                Label_0078: {
                    if (n != 0) {
                        n2 = length;
                        if (b == 0) {
                            break Label_0078;
                        }
                    }
                    fully[length] = b;
                    n2 = length + 1;
                }
                if (b == -1) {
                    n = 1;
                }
                else {
                    n = 0;
                }
                ++i;
                length = n2;
            }
            input = new ByteArrayInputStream(fully, 0, length);
        }
        if (!id3v2FrameHeader.isEncryption()) {
            if (id3v2FrameHeader.isCompression()) {
                length = id3v2FrameHeader.getDataLengthIndicator();
                input = new InflaterInputStream(input);
            }
            return new ID3v2FrameBody(input, id3v2FrameHeader.getHeaderSize(), length, this.tagHeader, id3v2FrameHeader);
        }
        throw new ID3v2Exception("Frame encryption is not supported");
    }
    
    public ID3v2DataInput getData() {
        return this.data;
    }
    
    public long getPosition() {
        return this.input.getPosition();
    }
    
    public long getRemainingLength() {
        return this.input.getRemainingLength();
    }
    
    public ID3v2TagHeader getTagHeader() {
        return this.tagHeader;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("id3v2tag[pos=");
        sb.append(this.getPosition());
        sb.append(", ");
        sb.append(this.getRemainingLength());
        sb.append(" left]");
        return sb.toString();
    }
}
