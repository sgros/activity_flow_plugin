// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.audioinfo.mp3;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import org.telegram.messenger.audioinfo.util.PositionInputStream;

public class MP3Input extends PositionInputStream
{
    public MP3Input(final InputStream inputStream) throws IOException {
        super(inputStream);
    }
    
    public MP3Input(final InputStream inputStream, final long n) {
        super(inputStream, n);
    }
    
    public final void readFully(final byte[] array, final int n, final int n2) throws IOException {
        int read;
        for (int i = 0; i < n2; i += read) {
            read = this.read(array, n + i, n2 - i);
            if (read <= 0) {
                throw new EOFException();
            }
        }
    }
    
    public void skipFully(final long n) throws IOException {
        long skip;
        for (long n2 = 0L; n2 < n; n2 += skip) {
            skip = this.skip(n - n2);
            if (skip <= 0L) {
                throw new EOFException();
            }
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("mp3[pos=");
        sb.append(this.getPosition());
        sb.append("]");
        return sb.toString();
    }
}
