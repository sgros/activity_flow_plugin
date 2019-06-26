// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.audioinfo.mp3;

import java.io.IOException;
import java.io.EOFException;
import java.io.InputStream;

public class ID3v2DataInput
{
    private final InputStream input;
    
    public ID3v2DataInput(final InputStream input) {
        this.input = input;
    }
    
    public byte readByte() throws IOException {
        final int read = this.input.read();
        if (read >= 0) {
            return (byte)read;
        }
        throw new EOFException();
    }
    
    public final void readFully(final byte[] b, final int n, final int n2) throws IOException {
        int read;
        for (int i = 0; i < n2; i += read) {
            read = this.input.read(b, n + i, n2 - i);
            if (read <= 0) {
                throw new EOFException();
            }
        }
    }
    
    public byte[] readFully(final int n) throws IOException {
        final byte[] array = new byte[n];
        this.readFully(array, 0, n);
        return array;
    }
    
    public int readInt() throws IOException {
        return (this.readByte() & 0xFF) << 24 | (this.readByte() & 0xFF) << 16 | (this.readByte() & 0xFF) << 8 | (this.readByte() & 0xFF);
    }
    
    public int readSyncsafeInt() throws IOException {
        return (this.readByte() & 0x7F) << 21 | (this.readByte() & 0x7F) << 14 | (this.readByte() & 0x7F) << 7 | (this.readByte() & 0x7F);
    }
    
    public void skipFully(final long n) throws IOException {
        long skip;
        for (long n2 = 0L; n2 < n; n2 += skip) {
            skip = this.input.skip(n - n2);
            if (skip <= 0L) {
                throw new EOFException();
            }
        }
    }
}
