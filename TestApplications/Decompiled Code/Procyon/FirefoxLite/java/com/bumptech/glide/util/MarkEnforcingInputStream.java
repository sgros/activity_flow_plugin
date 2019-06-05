// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.FilterInputStream;

public class MarkEnforcingInputStream extends FilterInputStream
{
    private int availableBytes;
    
    public MarkEnforcingInputStream(final InputStream in) {
        super(in);
        this.availableBytes = Integer.MIN_VALUE;
    }
    
    private long getBytesToRead(final long n) {
        if (this.availableBytes == 0) {
            return -1L;
        }
        if (this.availableBytes != Integer.MIN_VALUE && n > this.availableBytes) {
            return this.availableBytes;
        }
        return n;
    }
    
    private void updateAvailableBytesAfterRead(final long n) {
        if (this.availableBytes != Integer.MIN_VALUE && n != -1L) {
            this.availableBytes -= (int)n;
        }
    }
    
    @Override
    public int available() throws IOException {
        int n;
        if (this.availableBytes == Integer.MIN_VALUE) {
            n = super.available();
        }
        else {
            n = Math.min(this.availableBytes, super.available());
        }
        return n;
    }
    
    @Override
    public void mark(final int n) {
        super.mark(n);
        this.availableBytes = n;
    }
    
    @Override
    public int read() throws IOException {
        if (this.getBytesToRead(1L) == -1L) {
            return -1;
        }
        final int read = super.read();
        this.updateAvailableBytesAfterRead(1L);
        return read;
    }
    
    @Override
    public int read(final byte[] b, int read, int len) throws IOException {
        len = (int)this.getBytesToRead(len);
        if (len == -1) {
            return -1;
        }
        read = super.read(b, read, len);
        this.updateAvailableBytesAfterRead(read);
        return read;
    }
    
    @Override
    public void reset() throws IOException {
        super.reset();
        this.availableBytes = Integer.MIN_VALUE;
    }
    
    @Override
    public long skip(long n) throws IOException {
        n = this.getBytesToRead(n);
        if (n == -1L) {
            return -1L;
        }
        n = super.skip(n);
        this.updateAvailableBytesAfterRead(n);
        return n;
    }
}
