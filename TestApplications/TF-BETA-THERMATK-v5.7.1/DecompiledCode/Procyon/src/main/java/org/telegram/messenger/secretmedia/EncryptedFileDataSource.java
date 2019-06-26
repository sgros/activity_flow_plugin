// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.secretmedia;

import org.telegram.messenger.Utilities;
import java.io.EOFException;
import org.telegram.messenger.FileLoader;
import java.io.File;
import com.google.android.exoplayer2.upstream.DataSpec;
import java.io.IOException;
import com.google.android.exoplayer2.upstream.TransferListener;
import android.net.Uri;
import java.io.RandomAccessFile;
import com.google.android.exoplayer2.upstream.BaseDataSource;

public final class EncryptedFileDataSource extends BaseDataSource
{
    private long bytesRemaining;
    private RandomAccessFile file;
    private int fileOffset;
    private byte[] iv;
    private byte[] key;
    private boolean opened;
    private Uri uri;
    
    public EncryptedFileDataSource() {
        super(false);
        this.key = new byte[32];
        this.iv = new byte[16];
    }
    
    @Deprecated
    public EncryptedFileDataSource(final TransferListener transferListener) {
        this();
        if (transferListener != null) {
            this.addTransferListener(transferListener);
        }
    }
    
    @Override
    public void close() throws EncryptedFileDataSourceException {
        this.uri = null;
        this.fileOffset = 0;
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
    public long open(final DataSpec dataSpec) throws EncryptedFileDataSourceException {
        try {
            this.uri = dataSpec.uri;
            final File file = new File(dataSpec.uri.getPath());
            final String name = file.getName();
            final File internalCacheDir = FileLoader.getInternalCacheDir();
            final StringBuilder sb = new StringBuilder();
            sb.append(name);
            sb.append(".key");
            final RandomAccessFile randomAccessFile = new RandomAccessFile(new File(internalCacheDir, sb.toString()), "r");
            randomAccessFile.read(this.key);
            randomAccessFile.read(this.iv);
            randomAccessFile.close();
            (this.file = new RandomAccessFile(file, "r")).seek(dataSpec.position);
            this.fileOffset = (int)dataSpec.position;
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
            throw new EncryptedFileDataSourceException(ex);
        }
    }
    
    @Override
    public int read(final byte[] b, final int off, int read) throws EncryptedFileDataSourceException {
        if (read == 0) {
            return 0;
        }
        final long bytesRemaining = this.bytesRemaining;
        if (bytesRemaining == 0L) {
            return -1;
        }
        try {
            read = this.file.read(b, off, (int)Math.min(bytesRemaining, read));
            Utilities.aesCtrDecryptionByteArray(b, this.key, this.iv, off, read, this.fileOffset);
            this.fileOffset += read;
            if (read > 0) {
                this.bytesRemaining -= read;
                this.bytesTransferred(read);
            }
            return read;
        }
        catch (IOException ex) {
            throw new EncryptedFileDataSourceException(ex);
        }
    }
    
    public static class EncryptedFileDataSourceException extends IOException
    {
        public EncryptedFileDataSourceException(final IOException cause) {
            super(cause);
        }
    }
}
