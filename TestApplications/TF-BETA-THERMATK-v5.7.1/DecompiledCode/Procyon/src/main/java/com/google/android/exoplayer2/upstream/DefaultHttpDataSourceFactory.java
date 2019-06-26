// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.upstream;

import com.google.android.exoplayer2.util.Predicate;

public final class DefaultHttpDataSourceFactory extends BaseFactory
{
    private final boolean allowCrossProtocolRedirects;
    private final int connectTimeoutMillis;
    private final TransferListener listener;
    private final int readTimeoutMillis;
    private final String userAgent;
    
    public DefaultHttpDataSourceFactory(final String s, final TransferListener transferListener) {
        this(s, transferListener, 8000, 8000, false);
    }
    
    public DefaultHttpDataSourceFactory(final String userAgent, final TransferListener listener, final int connectTimeoutMillis, final int readTimeoutMillis, final boolean allowCrossProtocolRedirects) {
        this.userAgent = userAgent;
        this.listener = listener;
        this.connectTimeoutMillis = connectTimeoutMillis;
        this.readTimeoutMillis = readTimeoutMillis;
        this.allowCrossProtocolRedirects = allowCrossProtocolRedirects;
    }
    
    protected DefaultHttpDataSource createDataSourceInternal(final RequestProperties requestProperties) {
        final DefaultHttpDataSource defaultHttpDataSource = new DefaultHttpDataSource(this.userAgent, null, this.connectTimeoutMillis, this.readTimeoutMillis, this.allowCrossProtocolRedirects, requestProperties);
        final TransferListener listener = this.listener;
        if (listener != null) {
            defaultHttpDataSource.addTransferListener(listener);
        }
        return defaultHttpDataSource;
    }
}
