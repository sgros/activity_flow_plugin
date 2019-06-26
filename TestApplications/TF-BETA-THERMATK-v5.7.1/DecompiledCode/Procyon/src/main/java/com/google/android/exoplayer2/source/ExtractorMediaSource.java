// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.upstream.DefaultLoadErrorHandlingPolicy;
import android.os.Handler;
import android.net.Uri;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.upstream.DataSource;

public final class ExtractorMediaSource extends BaseMediaSource implements Listener
{
    private final int continueLoadingCheckIntervalBytes;
    private final String customCacheKey;
    private final DataSource.Factory dataSourceFactory;
    private final ExtractorsFactory extractorsFactory;
    private final LoadErrorHandlingPolicy loadableLoadErrorHandlingPolicy;
    private final Object tag;
    private long timelineDurationUs;
    private boolean timelineIsSeekable;
    private TransferListener transferListener;
    private final Uri uri;
    
    @Deprecated
    public ExtractorMediaSource(final Uri uri, final DataSource.Factory factory, final ExtractorsFactory extractorsFactory, final Handler handler, final EventListener eventListener) {
        this(uri, factory, extractorsFactory, handler, eventListener, null);
    }
    
    @Deprecated
    public ExtractorMediaSource(final Uri uri, final DataSource.Factory factory, final ExtractorsFactory extractorsFactory, final Handler handler, final EventListener eventListener, final String s) {
        this(uri, factory, extractorsFactory, handler, eventListener, s, 1048576);
    }
    
    @Deprecated
    public ExtractorMediaSource(final Uri uri, final DataSource.Factory factory, final ExtractorsFactory extractorsFactory, final Handler handler, final EventListener eventListener, final String s, final int n) {
        this(uri, factory, extractorsFactory, new DefaultLoadErrorHandlingPolicy(), s, n, null);
        if (eventListener != null && handler != null) {
            this.addEventListener(handler, new EventListenerWrapper(eventListener));
        }
    }
    
    private ExtractorMediaSource(final Uri uri, final DataSource.Factory dataSourceFactory, final ExtractorsFactory extractorsFactory, final LoadErrorHandlingPolicy loadableLoadErrorHandlingPolicy, final String customCacheKey, final int continueLoadingCheckIntervalBytes, final Object tag) {
        this.uri = uri;
        this.dataSourceFactory = dataSourceFactory;
        this.extractorsFactory = extractorsFactory;
        this.loadableLoadErrorHandlingPolicy = loadableLoadErrorHandlingPolicy;
        this.customCacheKey = customCacheKey;
        this.continueLoadingCheckIntervalBytes = continueLoadingCheckIntervalBytes;
        this.timelineDurationUs = -9223372036854775807L;
        this.tag = tag;
    }
    
    private void notifySourceInfoRefreshed(final long timelineDurationUs, final boolean timelineIsSeekable) {
        this.timelineDurationUs = timelineDurationUs;
        this.timelineIsSeekable = timelineIsSeekable;
        this.refreshSourceInfo(new SinglePeriodTimeline(this.timelineDurationUs, this.timelineIsSeekable, false, this.tag), null);
    }
    
    @Override
    public MediaPeriod createPeriod(final MediaPeriodId mediaPeriodId, final Allocator allocator, final long n) {
        final DataSource dataSource = this.dataSourceFactory.createDataSource();
        final TransferListener transferListener = this.transferListener;
        if (transferListener != null) {
            dataSource.addTransferListener(transferListener);
        }
        return new ExtractorMediaPeriod(this.uri, dataSource, this.extractorsFactory.createExtractors(), this.loadableLoadErrorHandlingPolicy, this.createEventDispatcher(mediaPeriodId), (ExtractorMediaPeriod.Listener)this, allocator, this.customCacheKey, this.continueLoadingCheckIntervalBytes);
    }
    
    @Override
    public void maybeThrowSourceInfoRefreshError() throws IOException {
    }
    
    @Override
    public void onSourceInfoRefreshed(final long n, final boolean b) {
        long timelineDurationUs = n;
        if (n == -9223372036854775807L) {
            timelineDurationUs = this.timelineDurationUs;
        }
        if (this.timelineDurationUs == timelineDurationUs && this.timelineIsSeekable == b) {
            return;
        }
        this.notifySourceInfoRefreshed(timelineDurationUs, b);
    }
    
    public void prepareSourceInternal(final TransferListener transferListener) {
        this.transferListener = transferListener;
        this.notifySourceInfoRefreshed(this.timelineDurationUs, this.timelineIsSeekable);
    }
    
    @Override
    public void releasePeriod(final MediaPeriod mediaPeriod) {
        ((ExtractorMediaPeriod)mediaPeriod).release();
    }
    
    public void releaseSourceInternal() {
    }
    
    @Deprecated
    public interface EventListener
    {
        void onLoadError(final IOException p0);
    }
    
    @Deprecated
    private static final class EventListenerWrapper extends DefaultMediaSourceEventListener
    {
        private final EventListener eventListener;
        
        public EventListenerWrapper(final EventListener eventListener) {
            Assertions.checkNotNull(eventListener);
            this.eventListener = eventListener;
        }
        
        @Override
        public void onLoadError(final int n, final MediaPeriodId mediaPeriodId, final LoadEventInfo loadEventInfo, final MediaLoadData mediaLoadData, final IOException ex, final boolean b) {
            this.eventListener.onLoadError(ex);
        }
    }
}
