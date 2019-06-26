// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.upstream;

import java.io.IOException;
import java.io.Closeable;
import com.google.android.exoplayer2.util.Util;
import java.io.InputStream;
import com.google.android.exoplayer2.util.Assertions;
import java.util.List;
import java.util.Map;
import android.net.Uri;

public final class ParsingLoadable<T> implements Loadable
{
    private final StatsDataSource dataSource;
    public final DataSpec dataSpec;
    private final Parser<? extends T> parser;
    private volatile T result;
    public final int type;
    
    public ParsingLoadable(final DataSource dataSource, final Uri uri, final int n, final Parser<? extends T> parser) {
        this(dataSource, new DataSpec(uri, 1), n, parser);
    }
    
    public ParsingLoadable(final DataSource dataSource, final DataSpec dataSpec, final int type, final Parser<? extends T> parser) {
        this.dataSource = new StatsDataSource(dataSource);
        this.dataSpec = dataSpec;
        this.type = type;
        this.parser = parser;
    }
    
    public long bytesLoaded() {
        return this.dataSource.getBytesRead();
    }
    
    @Override
    public final void cancelLoad() {
    }
    
    public Map<String, List<String>> getResponseHeaders() {
        return this.dataSource.getLastResponseHeaders();
    }
    
    public final T getResult() {
        return this.result;
    }
    
    public Uri getUri() {
        return this.dataSource.getLastOpenedUri();
    }
    
    @Override
    public final void load() throws IOException {
        this.dataSource.resetBytesRead();
        final DataSourceInputStream dataSourceInputStream = new DataSourceInputStream(this.dataSource, this.dataSpec);
        try {
            dataSourceInputStream.open();
            final Uri uri = this.dataSource.getUri();
            Assertions.checkNotNull(uri);
            this.result = (T)this.parser.parse(uri, dataSourceInputStream);
        }
        finally {
            Util.closeQuietly(dataSourceInputStream);
        }
    }
    
    public interface Parser<T>
    {
        T parse(final Uri p0, final InputStream p1) throws IOException;
    }
}
