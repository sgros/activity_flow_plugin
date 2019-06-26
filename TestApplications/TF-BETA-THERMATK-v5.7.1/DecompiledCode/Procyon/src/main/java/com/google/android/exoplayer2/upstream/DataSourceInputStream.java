// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.upstream;

import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;
import java.io.InputStream;

public final class DataSourceInputStream extends InputStream
{
    private boolean closed;
    private final DataSource dataSource;
    private final DataSpec dataSpec;
    private boolean opened;
    private final byte[] singleByteArray;
    private long totalBytesRead;
    
    public DataSourceInputStream(final DataSource dataSource, final DataSpec dataSpec) {
        this.opened = false;
        this.closed = false;
        this.dataSource = dataSource;
        this.dataSpec = dataSpec;
        this.singleByteArray = new byte[1];
    }
    
    private void checkOpened() throws IOException {
        if (!this.opened) {
            this.dataSource.open(this.dataSpec);
            this.opened = true;
        }
    }
    
    @Override
    public void close() throws IOException {
        if (!this.closed) {
            this.dataSource.close();
            this.closed = true;
        }
    }
    
    public void open() throws IOException {
        this.checkOpened();
    }
    
    @Override
    public int read() throws IOException {
        final int read = this.read(this.singleByteArray);
        int n = -1;
        if (read != -1) {
            n = (this.singleByteArray[0] & 0xFF);
        }
        return n;
    }
    
    @Override
    public int read(final byte[] array) throws IOException {
        return this.read(array, 0, array.length);
    }
    
    @Override
    public int read(final byte[] array, int read, final int n) throws IOException {
        Assertions.checkState(this.closed ^ true);
        this.checkOpened();
        read = this.dataSource.read(array, read, n);
        if (read == -1) {
            return -1;
        }
        this.totalBytesRead += read;
        return read;
    }
}
