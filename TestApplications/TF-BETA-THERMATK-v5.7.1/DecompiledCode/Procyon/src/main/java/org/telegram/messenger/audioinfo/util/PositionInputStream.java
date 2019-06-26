// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.audioinfo.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.FilterInputStream;

public class PositionInputStream extends FilterInputStream
{
    private long position;
    private long positionMark;
    
    public PositionInputStream(final InputStream inputStream) {
        this(inputStream, 0L);
    }
    
    public PositionInputStream(final InputStream in, final long position) {
        super(in);
        this.position = position;
    }
    
    public long getPosition() {
        return this.position;
    }
    
    @Override
    public void mark(final int readlimit) {
        synchronized (this) {
            this.positionMark = this.position;
            super.mark(readlimit);
        }
    }
    
    @Override
    public int read() throws IOException {
        final int read = super.read();
        if (read >= 0) {
            ++this.position;
        }
        return read;
    }
    
    @Override
    public final int read(final byte[] array) throws IOException {
        return this.read(array, 0, array.length);
    }
    
    @Override
    public int read(final byte[] b, int read, final int len) throws IOException {
        final long position = this.position;
        read = super.read(b, read, len);
        if (read > 0) {
            this.position = position + read;
        }
        return read;
    }
    
    @Override
    public void reset() throws IOException {
        synchronized (this) {
            super.reset();
            this.position = this.positionMark;
        }
    }
    
    @Override
    public long skip(long skip) throws IOException {
        final long position = this.position;
        skip = super.skip(skip);
        this.position = position + skip;
        return skip;
    }
}
