// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.FilterInputStream;

public final class ContentLengthInputStream extends FilterInputStream
{
    private final long contentLength;
    private int readSoFar;
    
    ContentLengthInputStream(final InputStream in, final long contentLength) {
        super(in);
        this.contentLength = contentLength;
    }
    
    private int checkReadSoFarOrThrow(final int n) throws IOException {
        if (n >= 0) {
            this.readSoFar += n;
        }
        else if (this.contentLength - this.readSoFar > 0L) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to read all expected data, expected: ");
            sb.append(this.contentLength);
            sb.append(", but read: ");
            sb.append(this.readSoFar);
            throw new IOException(sb.toString());
        }
        return n;
    }
    
    public static InputStream obtain(final InputStream inputStream, final long n) {
        return new ContentLengthInputStream(inputStream, n);
    }
    
    @Override
    public int available() throws IOException {
        synchronized (this) {
            return (int)Math.max(this.contentLength - this.readSoFar, this.in.available());
        }
    }
    
    @Override
    public int read() throws IOException {
        synchronized (this) {
            final int read = super.read();
            int n;
            if (read >= 0) {
                n = 1;
            }
            else {
                n = -1;
            }
            this.checkReadSoFarOrThrow(n);
            return read;
        }
    }
    
    @Override
    public int read(final byte[] array) throws IOException {
        return this.read(array, 0, array.length);
    }
    
    @Override
    public int read(final byte[] b, int checkReadSoFarOrThrow, final int len) throws IOException {
        synchronized (this) {
            checkReadSoFarOrThrow = this.checkReadSoFarOrThrow(super.read(b, checkReadSoFarOrThrow, len));
            return checkReadSoFarOrThrow;
        }
    }
}
