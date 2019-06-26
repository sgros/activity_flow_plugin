// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.upstream;

import java.io.EOFException;
import java.io.IOException;
import android.content.Context;
import android.net.Uri;
import java.io.InputStream;
import android.content.res.AssetManager;

public final class AssetDataSource extends BaseDataSource
{
    private final AssetManager assetManager;
    private long bytesRemaining;
    private InputStream inputStream;
    private boolean opened;
    private Uri uri;
    
    public AssetDataSource(final Context context) {
        super(false);
        this.assetManager = context.getAssets();
    }
    
    @Override
    public void close() throws AssetDataSourceException {
        this.uri = null;
        try {
            try {
                if (this.inputStream != null) {
                    this.inputStream.close();
                }
                this.inputStream = null;
                if (this.opened) {
                    this.opened = false;
                    this.transferEnded();
                }
            }
            finally {
                this.inputStream = null;
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
    public long open(final DataSpec dataSpec) throws AssetDataSourceException {
        try {
            this.uri = dataSpec.uri;
            final String path = this.uri.getPath();
            String s;
            if (path.startsWith("/android_asset/")) {
                s = path.substring(15);
            }
            else {
                s = path;
                if (path.startsWith("/")) {
                    s = path.substring(1);
                }
            }
            this.transferInitializing(dataSpec);
            this.inputStream = this.assetManager.open(s, 1);
            if (this.inputStream.skip(dataSpec.position) >= dataSpec.position) {
                if (dataSpec.length != -1L) {
                    this.bytesRemaining = dataSpec.length;
                }
                else {
                    this.bytesRemaining = this.inputStream.available();
                    if (this.bytesRemaining == 2147483647L) {
                        this.bytesRemaining = -1L;
                    }
                }
                this.opened = true;
                this.transferStarted(dataSpec);
                return this.bytesRemaining;
            }
            throw new EOFException();
        }
        catch (IOException ex) {
            throw new AssetDataSourceException(ex);
        }
    }
    
    @Override
    public int read(final byte[] b, int read, int len) throws AssetDataSourceException {
        if (len == 0) {
            return 0;
        }
        final long bytesRemaining = this.bytesRemaining;
        if (bytesRemaining == 0L) {
            return -1;
        }
        Label_0046: {
            if (bytesRemaining == -1L) {
                break Label_0046;
            }
            final long b2 = len;
            try {
                len = (int)Math.min(bytesRemaining, b2);
                read = this.inputStream.read(b, read, len);
                if (read != -1) {
                    final long bytesRemaining2 = this.bytesRemaining;
                    if (bytesRemaining2 != -1L) {
                        this.bytesRemaining = bytesRemaining2 - read;
                    }
                    this.bytesTransferred(read);
                    return read;
                }
                if (this.bytesRemaining == -1L) {
                    return -1;
                }
                throw new AssetDataSourceException(new EOFException());
            }
            catch (IOException ex) {
                throw new AssetDataSourceException(ex);
            }
        }
    }
    
    public static final class AssetDataSourceException extends IOException
    {
        public AssetDataSourceException(final IOException cause) {
            super(cause);
        }
    }
}
