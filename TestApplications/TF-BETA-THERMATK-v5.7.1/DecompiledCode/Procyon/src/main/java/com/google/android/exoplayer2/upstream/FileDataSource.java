// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.upstream;

import java.io.EOFException;
import java.io.IOException;
import android.net.Uri;
import java.io.RandomAccessFile;

public final class FileDataSource extends BaseDataSource
{
    private long bytesRemaining;
    private RandomAccessFile file;
    private boolean opened;
    private Uri uri;
    
    public FileDataSource() {
        super(false);
    }
    
    @Override
    public void close() throws FileDataSourceException {
        this.uri = null;
        try {
            try {
                if (this.file != null) {
                    this.file.close();
                }
                this.file = null;
                if (this.opened) {
                    this.opened = false;
                    this.transferEnded();
                }
            }
            finally {
                this.file = null;
                if (this.opened) {
                    this.opened = false;
                    this.transferEnded();
                }
            }
        }
        catch (IOException ex) {}
    }
    
    @Override
    public Uri getUri() {
        return this.uri;
    }
    
    @Override
    public long open(final DataSpec dataSpec) throws FileDataSourceException {
        try {
            this.uri = dataSpec.uri;
            this.transferInitializing(dataSpec);
            (this.file = new RandomAccessFile(dataSpec.uri.getPath(), "r")).seek(dataSpec.position);
            long length;
            if (dataSpec.length == -1L) {
                length = this.file.length() - dataSpec.position;
            }
            else {
                length = dataSpec.length;
            }
            this.bytesRemaining = length;
            if (this.bytesRemaining >= 0L) {
                this.opened = true;
                this.transferStarted(dataSpec);
                return this.bytesRemaining;
            }
            throw new EOFException();
        }
        catch (IOException ex) {
            throw new FileDataSourceException(ex);
        }
    }
    
    @Override
    public int read(final byte[] b, int read, final int n) throws FileDataSourceException {
        if (n == 0) {
            return 0;
        }
        final long bytesRemaining = this.bytesRemaining;
        if (bytesRemaining == 0L) {
            return -1;
        }
        try {
            read = this.file.read(b, read, (int)Math.min(bytesRemaining, n));
            if (read > 0) {
                this.bytesRemaining -= read;
                this.bytesTransferred(read);
            }
            return read;
        }
        catch (IOException ex) {
            throw new FileDataSourceException(ex);
        }
    }
    
    public static class FileDataSourceException extends IOException
    {
        public FileDataSourceException(final IOException cause) {
            super(cause);
        }
    }
}
