// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.upstream;

import java.net.URLDecoder;
import android.util.Base64;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.ParserException;
import android.net.Uri;
import java.io.IOException;

public final class DataSchemeDataSource extends BaseDataSource
{
    private int bytesRead;
    private byte[] data;
    private DataSpec dataSpec;
    
    public DataSchemeDataSource() {
        super(false);
    }
    
    @Override
    public void close() throws IOException {
        if (this.data != null) {
            this.data = null;
            this.transferEnded();
        }
        this.dataSpec = null;
    }
    
    @Override
    public Uri getUri() {
        final DataSpec dataSpec = this.dataSpec;
        Uri uri;
        if (dataSpec != null) {
            uri = dataSpec.uri;
        }
        else {
            uri = null;
        }
        return uri;
    }
    
    @Override
    public long open(final DataSpec dataSpec) throws IOException {
        this.transferInitializing(dataSpec);
        this.dataSpec = dataSpec;
        final Uri uri = dataSpec.uri;
        final String scheme = uri.getScheme();
        if (!"data".equals(scheme)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unsupported scheme: ");
            sb.append(scheme);
            throw new ParserException(sb.toString());
        }
        final String[] split = Util.split(uri.getSchemeSpecificPart(), ",");
        if (split.length == 2) {
            final String s = split[1];
            Label_0120: {
                if (split[0].contains(";base64")) {
                    try {
                        this.data = Base64.decode(s, 0);
                        break Label_0120;
                    }
                    catch (IllegalArgumentException ex) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Error while parsing Base64 encoded string: ");
                        sb2.append(s);
                        throw new ParserException(sb2.toString(), ex);
                    }
                }
                this.data = Util.getUtf8Bytes(URLDecoder.decode(s, "US-ASCII"));
            }
            this.transferStarted(dataSpec);
            return this.data.length;
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("Unexpected URI format: ");
        sb3.append(uri);
        throw new ParserException(sb3.toString());
    }
    
    @Override
    public int read(final byte[] array, final int n, int min) {
        if (min == 0) {
            return 0;
        }
        final int b = this.data.length - this.bytesRead;
        if (b == 0) {
            return -1;
        }
        min = Math.min(min, b);
        System.arraycopy(this.data, this.bytesRead, array, n, min);
        this.bytesRead += min;
        this.bytesTransferred(min);
        return min;
    }
}
