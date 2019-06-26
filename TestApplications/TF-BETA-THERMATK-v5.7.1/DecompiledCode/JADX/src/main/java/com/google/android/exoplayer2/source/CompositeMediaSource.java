package com.google.android.exoplayer2.source;

import android.os.Handler;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource.MediaPeriodId;
import com.google.android.exoplayer2.source.MediaSource.SourceInfoRefreshListener;
import com.google.android.exoplayer2.source.MediaSourceEventListener.EventDispatcher;
import com.google.android.exoplayer2.source.MediaSourceEventListener.LoadEventInfo;
import com.google.android.exoplayer2.source.MediaSourceEventListener.MediaLoadData;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.HashMap;

public abstract class CompositeMediaSource<T> extends BaseMediaSource {
    private final HashMap<T, MediaSourceAndListener> childSources = new HashMap();
    private Handler eventHandler;
    private TransferListener mediaTransferListener;

    private static final class MediaSourceAndListener {
        public final MediaSourceEventListener eventListener;
        public final SourceInfoRefreshListener listener;
        public final MediaSource mediaSource;

        public MediaSourceAndListener(MediaSource mediaSource, SourceInfoRefreshListener sourceInfoRefreshListener, MediaSourceEventListener mediaSourceEventListener) {
            this.mediaSource = mediaSource;
            this.listener = sourceInfoRefreshListener;
            this.eventListener = mediaSourceEventListener;
        }
    }

    private final class ForwardingEventListener implements MediaSourceEventListener {
        private EventDispatcher eventDispatcher;
        /* renamed from: id */
        private final T f623id;

        public ForwardingEventListener(T t) {
            this.eventDispatcher = CompositeMediaSource.this.createEventDispatcher(null);
            this.f623id = t;
        }

        public void onMediaPeriodCreated(int i, MediaPeriodId mediaPeriodId) {
            if (maybeUpdateEventDispatcher(i, mediaPeriodId)) {
                this.eventDispatcher.mediaPeriodCreated();
            }
        }

        public void onMediaPeriodReleased(int i, MediaPeriodId mediaPeriodId) {
            if (maybeUpdateEventDispatcher(i, mediaPeriodId)) {
                this.eventDispatcher.mediaPeriodReleased();
            }
        }

        public void onLoadStarted(int i, MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
            if (maybeUpdateEventDispatcher(i, mediaPeriodId)) {
                this.eventDispatcher.loadStarted(loadEventInfo, maybeUpdateMediaLoadData(mediaLoadData));
            }
        }

        public void onLoadCompleted(int i, MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
            if (maybeUpdateEventDispatcher(i, mediaPeriodId)) {
                this.eventDispatcher.loadCompleted(loadEventInfo, maybeUpdateMediaLoadData(mediaLoadData));
            }
        }

        public void onLoadCanceled(int i, MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
            if (maybeUpdateEventDispatcher(i, mediaPeriodId)) {
                this.eventDispatcher.loadCanceled(loadEventInfo, maybeUpdateMediaLoadData(mediaLoadData));
            }
        }

        public void onLoadError(int i, MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData, IOException iOException, boolean z) {
            if (maybeUpdateEventDispatcher(i, mediaPeriodId)) {
                this.eventDispatcher.loadError(loadEventInfo, maybeUpdateMediaLoadData(mediaLoadData), iOException, z);
            }
        }

        public void onReadingStarted(int i, MediaPeriodId mediaPeriodId) {
            if (maybeUpdateEventDispatcher(i, mediaPeriodId)) {
                this.eventDispatcher.readingStarted();
            }
        }

        public void onUpstreamDiscarded(int i, MediaPeriodId mediaPeriodId, MediaLoadData mediaLoadData) {
            if (maybeUpdateEventDispatcher(i, mediaPeriodId)) {
                this.eventDispatcher.upstreamDiscarded(maybeUpdateMediaLoadData(mediaLoadData));
            }
        }

        public void onDownstreamFormatChanged(int i, MediaPeriodId mediaPeriodId, MediaLoadData mediaLoadData) {
            if (maybeUpdateEventDispatcher(i, mediaPeriodId)) {
                this.eventDispatcher.downstreamFormatChanged(maybeUpdateMediaLoadData(mediaLoadData));
            }
        }

        private boolean maybeUpdateEventDispatcher(int i, MediaPeriodId mediaPeriodId) {
            Object mediaPeriodIdForChildMediaPeriodId;
            if (mediaPeriodId != null) {
                mediaPeriodIdForChildMediaPeriodId = CompositeMediaSource.this.getMediaPeriodIdForChildMediaPeriodId(this.f623id, mediaPeriodId);
                if (mediaPeriodIdForChildMediaPeriodId == null) {
                    return false;
                }
            }
            mediaPeriodIdForChildMediaPeriodId = null;
            CompositeMediaSource.this.getWindowIndexForChildWindowIndex(this.f623id, i);
            EventDispatcher eventDispatcher = this.eventDispatcher;
            if (!(eventDispatcher.windowIndex == i && Util.areEqual(eventDispatcher.mediaPeriodId, mediaPeriodIdForChildMediaPeriodId))) {
                this.eventDispatcher = CompositeMediaSource.this.createEventDispatcher(i, mediaPeriodIdForChildMediaPeriodId, 0);
            }
            return true;
        }

        private MediaLoadData maybeUpdateMediaLoadData(MediaLoadData mediaLoadData) {
            CompositeMediaSource compositeMediaSource = CompositeMediaSource.this;
            Object obj = this.f623id;
            long j = mediaLoadData.mediaStartTimeMs;
            compositeMediaSource.getMediaTimeForChildMediaTime(obj, j);
            compositeMediaSource = CompositeMediaSource.this;
            obj = this.f623id;
            long j2 = mediaLoadData.mediaEndTimeMs;
            compositeMediaSource.getMediaTimeForChildMediaTime(obj, j2);
            if (j == mediaLoadData.mediaStartTimeMs && j2 == mediaLoadData.mediaEndTimeMs) {
                return mediaLoadData;
            }
            return new MediaLoadData(mediaLoadData.dataType, mediaLoadData.trackType, mediaLoadData.trackFormat, mediaLoadData.trackSelectionReason, mediaLoadData.trackSelectionData, j, j2);
        }
    }

    public abstract MediaPeriodId getMediaPeriodIdForChildMediaPeriodId(T t, MediaPeriodId mediaPeriodId);

    /* Access modifiers changed, original: protected */
    public long getMediaTimeForChildMediaTime(T t, long j) {
        return j;
    }

    /* Access modifiers changed, original: protected */
    public int getWindowIndexForChildWindowIndex(T t, int i) {
        return i;
    }

    /* renamed from: onChildSourceInfoRefreshed */
    public abstract void lambda$prepareChildSource$0$CompositeMediaSource(T t, MediaSource mediaSource, Timeline timeline, Object obj);

    protected CompositeMediaSource() {
    }

    public void prepareSourceInternal(TransferListener transferListener) {
        this.mediaTransferListener = transferListener;
        this.eventHandler = new Handler();
    }

    public void maybeThrowSourceInfoRefreshError() throws IOException {
        for (MediaSourceAndListener mediaSourceAndListener : this.childSources.values()) {
            mediaSourceAndListener.mediaSource.maybeThrowSourceInfoRefreshError();
        }
    }

    public void releaseSourceInternal() {
        for (MediaSourceAndListener mediaSourceAndListener : this.childSources.values()) {
            mediaSourceAndListener.mediaSource.releaseSource(mediaSourceAndListener.listener);
            mediaSourceAndListener.mediaSource.removeEventListener(mediaSourceAndListener.eventListener);
        }
        this.childSources.clear();
    }

    /* Access modifiers changed, original: protected|final */
    public final void prepareChildSource(T t, MediaSource mediaSource) {
        Assertions.checkArgument(this.childSources.containsKey(t) ^ 1);
        C3345-$$Lambda$CompositeMediaSource$ahAPO18YbnzL6kKRAWdp4FR_Vco c3345-$$Lambda$CompositeMediaSource$ahAPO18YbnzL6kKRAWdp4FR_Vco = new C3345-$$Lambda$CompositeMediaSource$ahAPO18YbnzL6kKRAWdp4FR_Vco(this, t);
        ForwardingEventListener forwardingEventListener = new ForwardingEventListener(t);
        this.childSources.put(t, new MediaSourceAndListener(mediaSource, c3345-$$Lambda$CompositeMediaSource$ahAPO18YbnzL6kKRAWdp4FR_Vco, forwardingEventListener));
        Handler handler = this.eventHandler;
        Assertions.checkNotNull(handler);
        mediaSource.addEventListener(handler, forwardingEventListener);
        mediaSource.prepareSource(c3345-$$Lambda$CompositeMediaSource$ahAPO18YbnzL6kKRAWdp4FR_Vco, this.mediaTransferListener);
    }
}
