package com.google.android.exoplayer2.upstream;

import android.net.Uri;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class StatsDataSource implements DataSource {
    private long bytesRead;
    private final DataSource dataSource;
    private Uri lastOpenedUri = Uri.EMPTY;
    private Map<String, List<String>> lastResponseHeaders = Collections.emptyMap();

    public StatsDataSource(DataSource dataSource) {
        Assertions.checkNotNull(dataSource);
        this.dataSource = dataSource;
    }

    public void resetBytesRead() {
        this.bytesRead = 0;
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

    public void addTransferListener(TransferListener transferListener) {
        this.dataSource.addTransferListener(transferListener);
    }

    public long open(DataSpec dataSpec) throws IOException {
        this.lastOpenedUri = dataSpec.uri;
        this.lastResponseHeaders = Collections.emptyMap();
        long open = this.dataSource.open(dataSpec);
        Uri uri = getUri();
        Assertions.checkNotNull(uri);
        this.lastOpenedUri = uri;
        this.lastResponseHeaders = getResponseHeaders();
        return open;
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        int read = this.dataSource.read(bArr, i, i2);
        if (read != -1) {
            this.bytesRead += (long) read;
        }
        return read;
    }

    public Uri getUri() {
        return this.dataSource.getUri();
    }

    public Map<String, List<String>> getResponseHeaders() {
        return this.dataSource.getResponseHeaders();
    }

    public void close() throws IOException {
        this.dataSource.close();
    }
}
