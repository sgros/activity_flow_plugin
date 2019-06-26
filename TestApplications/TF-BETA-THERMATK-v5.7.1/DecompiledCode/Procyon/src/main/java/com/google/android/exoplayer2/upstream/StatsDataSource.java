// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.upstream;

import java.io.IOException;
import java.util.Collections;
import com.google.android.exoplayer2.util.Assertions;
import java.util.List;
import java.util.Map;
import android.net.Uri;

public final class StatsDataSource implements DataSource
{
    private long bytesRead;
    private final DataSource dataSource;
    private Uri lastOpenedUri;
    private Map<String, List<String>> lastResponseHeaders;
    
    public StatsDataSource(final DataSource dataSource) {
        Assertions.checkNotNull(dataSource);
        this.dataSource = dataSource;
        this.lastOpenedUri = Uri.EMPTY;
        this.lastResponseHeaders = Collections.emptyMap();
    }
    
    @Override
    public void addTransferListener(final TransferListener transferListener) {
        this.dataSource.addTransferListener(transferListener);
    }
    
    @Override
    public void close() throws IOException {
        this.dataSource.close();
    }
    
    public long getBytesRead() {
        return this.bytesRead;
    }
    
    public Uri getLastOpenedUri() {
        return this.lastOpenedUri;
    }
    
    public Map<String, List<String>> getLastResponseHeaders() {
        return this.lastResponseHeaders;
    }
    
    @Override
    public Map<String, List<String>> getResponseHeaders() {
        return this.dataSource.getResponseHeaders();
    }
    
    @Override
    public Uri getUri() {
        return this.dataSource.getUri();
    }
    
    @Override
    public long open(final DataSpec dataSpec) throws IOException {
        this.lastOpenedUri = dataSpec.uri;
        this.lastResponseHeaders = Collections.emptyMap();
        final long open = this.dataSource.open(dataSpec);
        final Uri uri = this.getUri();
        Assertions.checkNotNull(uri);
        this.lastOpenedUri = uri;
        this.lastResponseHeaders = this.getResponseHeaders();
        return open;
    }
    
    @Override
    public int read(final byte[] array, int read, final int n) throws IOException {
        read = this.dataSource.read(array, read, n);
        if (read != -1) {
            this.bytesRead += read;
        }
        return read;
    }
    
    public void resetBytesRead() {
        this.bytesRead = 0L;
    }
}
