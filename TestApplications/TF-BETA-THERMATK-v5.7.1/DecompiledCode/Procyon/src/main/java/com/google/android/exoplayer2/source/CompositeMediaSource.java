// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;
import java.util.Iterator;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.upstream.TransferListener;
import android.os.Handler;
import java.util.HashMap;

public abstract class CompositeMediaSource<T> extends BaseMediaSource
{
    private final HashMap<T, MediaSourceAndListener> childSources;
    private Handler eventHandler;
    private TransferListener mediaTransferListener;
    
    protected CompositeMediaSource() {
        this.childSources = new HashMap<T, MediaSourceAndListener>();
    }
    
    protected abstract MediaPeriodId getMediaPeriodIdForChildMediaPeriodId(final T p0, final MediaPeriodId p1);
    
    protected long getMediaTimeForChildMediaTime(final T t, final long n) {
        return n;
    }
    
    protected int getWindowIndexForChildWindowIndex(final T t, final int n) {
        return n;
    }
    
    @Override
    public void maybeThrowSourceInfoRefreshError() throws IOException {
        final Iterator<MediaSourceAndListener> iterator = this.childSources.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().mediaSource.maybeThrowSourceInfoRefreshError();
        }
    }
    
    protected abstract void onChildSourceInfoRefreshed(final T p0, final MediaSource p1, final Timeline p2, final Object p3);
    
    protected final void prepareChildSource(final T t, final MediaSource mediaSource) {
        Assertions.checkArgument(this.childSources.containsKey(t) ^ true);
        final _$$Lambda$CompositeMediaSource$ahAPO18YbnzL6kKRAWdp4FR_Vco $$Lambda$CompositeMediaSource$ahAPO18YbnzL6kKRAWdp4FR_Vco = new _$$Lambda$CompositeMediaSource$ahAPO18YbnzL6kKRAWdp4FR_Vco(this, t);
        final ForwardingEventListener forwardingEventListener = new ForwardingEventListener(t);
        this.childSources.put(t, new MediaSourceAndListener(mediaSource, $$Lambda$CompositeMediaSource$ahAPO18YbnzL6kKRAWdp4FR_Vco, forwardingEventListener));
        final Handler eventHandler = this.eventHandler;
        Assertions.checkNotNull(eventHandler);
        mediaSource.addEventListener(eventHandler, forwardingEventListener);
        mediaSource.prepareSource((MediaSource.SourceInfoRefreshListener)$$Lambda$CompositeMediaSource$ahAPO18YbnzL6kKRAWdp4FR_Vco, this.mediaTransferListener);
    }
    
    public void prepareSourceInternal(final TransferListener mediaTransferListener) {
        this.mediaTransferListener = mediaTransferListener;
        this.eventHandler = new Handler();
    }
    
    public void releaseSourceInternal() {
        for (final MediaSourceAndListener mediaSourceAndListener : this.childSources.values()) {
            mediaSourceAndListener.mediaSource.releaseSource(mediaSourceAndListener.listener);
            mediaSourceAndListener.mediaSource.removeEventListener(mediaSourceAndListener.eventListener);
        }
        this.childSources.clear();
    }
    
    private final class ForwardingEventListener implements MediaSourceEventListener
    {
        private EventDispatcher eventDispatcher;
        private final T id;
        
        public ForwardingEventListener(final T id) {
            this.eventDispatcher = CompositeMediaSource.this.createEventDispatcher(null);
            this.id = id;
        }
        
        private boolean maybeUpdateEventDispatcher(final int n, MediaPeriodId mediaPeriodIdForChildMediaPeriodId) {
            if (mediaPeriodIdForChildMediaPeriodId != null) {
                if ((mediaPeriodIdForChildMediaPeriodId = CompositeMediaSource.this.getMediaPeriodIdForChildMediaPeriodId(this.id, mediaPeriodIdForChildMediaPeriodId)) == null) {
                    return false;
                }
            }
            else {
                mediaPeriodIdForChildMediaPeriodId = null;
            }
            CompositeMediaSource.this.getWindowIndexForChildWindowIndex(this.id, n);
            final EventDispatcher eventDispatcher = this.eventDispatcher;
            if (eventDispatcher.windowIndex != n || !Util.areEqual(eventDispatcher.mediaPeriodId, mediaPeriodIdForChildMediaPeriodId)) {
                this.eventDispatcher = CompositeMediaSource.this.createEventDispatcher(n, mediaPeriodIdForChildMediaPeriodId, 0L);
            }
            return true;
        }
        
        private MediaLoadData maybeUpdateMediaLoadData(final MediaLoadData mediaLoadData) {
            final CompositeMediaSource this$0 = CompositeMediaSource.this;
            final T id = this.id;
            final long mediaStartTimeMs = mediaLoadData.mediaStartTimeMs;
            this$0.getMediaTimeForChildMediaTime(id, mediaStartTimeMs);
            final CompositeMediaSource this$2 = CompositeMediaSource.this;
            final T id2 = this.id;
            final long mediaEndTimeMs = mediaLoadData.mediaEndTimeMs;
            this$2.getMediaTimeForChildMediaTime(id2, mediaEndTimeMs);
            if (mediaStartTimeMs == mediaLoadData.mediaStartTimeMs && mediaEndTimeMs == mediaLoadData.mediaEndTimeMs) {
                return mediaLoadData;
            }
            return new MediaLoadData(mediaLoadData.dataType, mediaLoadData.trackType, mediaLoadData.trackFormat, mediaLoadData.trackSelectionReason, mediaLoadData.trackSelectionData, mediaStartTimeMs, mediaEndTimeMs);
        }
        
        @Override
        public void onDownstreamFormatChanged(final int n, final MediaPeriodId mediaPeriodId, final MediaLoadData mediaLoadData) {
            if (this.maybeUpdateEventDispatcher(n, mediaPeriodId)) {
                this.eventDispatcher.downstreamFormatChanged(this.maybeUpdateMediaLoadData(mediaLoadData));
            }
        }
        
        @Override
        public void onLoadCanceled(final int n, final MediaPeriodId mediaPeriodId, final LoadEventInfo loadEventInfo, final MediaLoadData mediaLoadData) {
            if (this.maybeUpdateEventDispatcher(n, mediaPeriodId)) {
                this.eventDispatcher.loadCanceled(loadEventInfo, this.maybeUpdateMediaLoadData(mediaLoadData));
            }
        }
        
        @Override
        public void onLoadCompleted(final int n, final MediaPeriodId mediaPeriodId, final LoadEventInfo loadEventInfo, final MediaLoadData mediaLoadData) {
            if (this.maybeUpdateEventDispatcher(n, mediaPeriodId)) {
                this.eventDispatcher.loadCompleted(loadEventInfo, this.maybeUpdateMediaLoadData(mediaLoadData));
            }
        }
        
        @Override
        public void onLoadError(final int n, final MediaPeriodId mediaPeriodId, final LoadEventInfo loadEventInfo, final MediaLoadData mediaLoadData, final IOException ex, final boolean b) {
            if (this.maybeUpdateEventDispatcher(n, mediaPeriodId)) {
                this.eventDispatcher.loadError(loadEventInfo, this.maybeUpdateMediaLoadData(mediaLoadData), ex, b);
            }
        }
        
        @Override
        public void onLoadStarted(final int n, final MediaPeriodId mediaPeriodId, final LoadEventInfo loadEventInfo, final MediaLoadData mediaLoadData) {
            if (this.maybeUpdateEventDispatcher(n, mediaPeriodId)) {
                this.eventDispatcher.loadStarted(loadEventInfo, this.maybeUpdateMediaLoadData(mediaLoadData));
            }
        }
        
        @Override
        public void onMediaPeriodCreated(final int n, final MediaPeriodId mediaPeriodId) {
            if (this.maybeUpdateEventDispatcher(n, mediaPeriodId)) {
                this.eventDispatcher.mediaPeriodCreated();
            }
        }
        
        @Override
        public void onMediaPeriodReleased(final int n, final MediaPeriodId mediaPeriodId) {
            if (this.maybeUpdateEventDispatcher(n, mediaPeriodId)) {
                this.eventDispatcher.mediaPeriodReleased();
            }
        }
        
        @Override
        public void onReadingStarted(final int n, final MediaPeriodId mediaPeriodId) {
            if (this.maybeUpdateEventDispatcher(n, mediaPeriodId)) {
                this.eventDispatcher.readingStarted();
            }
        }
        
        @Override
        public void onUpstreamDiscarded(final int n, final MediaPeriodId mediaPeriodId, final MediaLoadData mediaLoadData) {
            if (this.maybeUpdateEventDispatcher(n, mediaPeriodId)) {
                this.eventDispatcher.upstreamDiscarded(this.maybeUpdateMediaLoadData(mediaLoadData));
            }
        }
    }
    
    private static final class MediaSourceAndListener
    {
        public final MediaSourceEventListener eventListener;
        public final SourceInfoRefreshListener listener;
        public final MediaSource mediaSource;
        
        public MediaSourceAndListener(final MediaSource mediaSource, final SourceInfoRefreshListener listener, final MediaSourceEventListener eventListener) {
            this.mediaSource = mediaSource;
            this.listener = listener;
            this.eventListener = eventListener;
        }
    }
}
