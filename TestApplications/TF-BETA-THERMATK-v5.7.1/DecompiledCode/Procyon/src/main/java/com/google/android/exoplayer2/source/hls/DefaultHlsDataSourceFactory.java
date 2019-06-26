// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.hls;

import com.google.android.exoplayer2.upstream.DataSource;

public final class DefaultHlsDataSourceFactory implements HlsDataSourceFactory
{
    private final DataSource.Factory dataSourceFactory;
    
    public DefaultHlsDataSourceFactory(final DataSource.Factory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }
    
    @Override
    public DataSource createDataSource(final int n) {
        return this.dataSourceFactory.createDataSource();
    }
}
