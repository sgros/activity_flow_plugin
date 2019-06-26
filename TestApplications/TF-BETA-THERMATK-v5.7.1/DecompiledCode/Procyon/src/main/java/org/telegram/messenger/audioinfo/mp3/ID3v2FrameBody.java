// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.audioinfo.mp3;

import java.io.IOException;
import java.io.InputStream;
import org.telegram.messenger.audioinfo.util.RangeInputStream;

public class ID3v2FrameBody
{
    static final ThreadLocal<Buffer> textBuffer;
    private final ID3v2DataInput data;
    private final ID3v2FrameHeader frameHeader;
    private final RangeInputStream input;
    private final ID3v2TagHeader tagHeader;
    
    static {
        textBuffer = new ThreadLocal<Buffer>() {
            @Override
            protected Buffer initialValue() {
                return new Buffer(4096);
            }
        };
    }
    
    ID3v2FrameBody(final InputStream inputStream, final long n, final int n2, final ID3v2TagHeader tagHeader, final ID3v2FrameHeader frameHeader) throws IOException {
        this.input = new RangeInputStream(inputStream, n, n2);
        this.data = new ID3v2DataInput(this.input);
        this.tagHeader = tagHeader;
        this.frameHeader = frameHeader;
    }
    
    private String extractString(final byte[] bytes, final int offset, final int n, final ID3v2Encoding id3v2Encoding, final boolean b) {
        int length = n;
        if (b) {
            int n2 = 0;
            int n3 = 0;
            while (true) {
                length = n;
                if (n2 >= n) {
                    break;
                }
                final int n4 = offset + n2;
                if (bytes[n4] == 0 && (id3v2Encoding != ID3v2Encoding.UTF_16 || n3 != 0 || n4 % 2 == 0)) {
                    if (++n3 == id3v2Encoding.getZeroBytes()) {
                        length = n2 + 1 - id3v2Encoding.getZeroBytes();
                        break;
                    }
                }
                else {
                    n3 = 0;
                }
                ++n2;
            }
        }
        try {
            String substring;
            final String s = substring = new String(bytes, offset, length, id3v2Encoding.getCharset().name());
            if (s.length() > 0) {
                substring = s;
                if (s.charAt(0) == '\ufeff') {
                    substring = s.substring(1);
                }
            }
            return substring;
        }
        catch (Exception ex) {
            return "";
        }
    }
    
    public ID3v2DataInput getData() {
        return this.data;
    }
    
    public ID3v2FrameHeader getFrameHeader() {
        return this.frameHeader;
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
    
    public ID3v2Encoding readEncoding() throws IOException, ID3v2Exception {
        final byte byte1 = this.data.readByte();
        if (byte1 == 0) {
            return ID3v2Encoding.ISO_8859_1;
        }
        if (byte1 == 1) {
            return ID3v2Encoding.UTF_16;
        }
        if (byte1 == 2) {
            return ID3v2Encoding.UTF_16BE;
        }
        if (byte1 == 3) {
            return ID3v2Encoding.UTF_8;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid encoding: ");
        sb.append(byte1);
        throw new ID3v2Exception(sb.toString());
    }
    
    public String readFixedLengthString(final int i, final ID3v2Encoding id3v2Encoding) throws IOException, ID3v2Exception {
        if (i <= this.getRemainingLength()) {
            final byte[] bytes = ID3v2FrameBody.textBuffer.get().bytes(i);
            this.data.readFully(bytes, 0, i);
            return this.extractString(bytes, 0, i, id3v2Encoding, true);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Could not read fixed-length string of length: ");
        sb.append(i);
        throw new ID3v2Exception(sb.toString());
    }
    
    public String readZeroTerminatedString(int a, final ID3v2Encoding id3v2Encoding) throws IOException, ID3v2Exception {
        final int min = Math.min(a, (int)this.getRemainingLength());
        final byte[] bytes = ID3v2FrameBody.textBuffer.get().bytes(min);
        int i = 0;
        a = 0;
        while (i < min) {
            final byte byte1 = this.data.readByte();
            bytes[i] = byte1;
            if (byte1 == 0 && (id3v2Encoding != ID3v2Encoding.UTF_16 || a != 0 || i % 2 == 0)) {
                if (++a == id3v2Encoding.getZeroBytes()) {
                    return this.extractString(bytes, 0, i + 1 - id3v2Encoding.getZeroBytes(), id3v2Encoding, false);
                }
            }
            else {
                a = 0;
            }
            ++i;
        }
        throw new ID3v2Exception("Could not read zero-termiated string");
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("id3v2frame[pos=");
        sb.append(this.getPosition());
        sb.append(", ");
        sb.append(this.getRemainingLength());
        sb.append(" left]");
        return sb.toString();
    }
    
    static final class Buffer
    {
        byte[] bytes;
        
        Buffer(final int n) {
            this.bytes = new byte[n];
        }
        
        byte[] bytes(final int n) {
            final byte[] bytes = this.bytes;
            if (n > bytes.length) {
                int length = bytes.length;
                do {
                    length *= 2;
                } while (n > length);
                this.bytes = new byte[length];
            }
            return this.bytes;
        }
    }
}
