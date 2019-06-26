// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import android.net.Uri;
import com.google.android.exoplayer2.upstream.DataSpec;
import java.util.Iterator;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.util.Assertions;
import android.os.Looper;
import android.os.Handler;
import com.google.android.exoplayer2.C;
import java.util.concurrent.CopyOnWriteArrayList;
import java.io.IOException;

public interface MediaSourceEventListener
{
    void onDownstreamFormatChanged(final int p0, final MediaSource.MediaPeriodId p1, final MediaLoadData p2);
    
    void onLoadCanceled(final int p0, final MediaSource.MediaPeriodId p1, final LoadEventInfo p2, final MediaLoadData p3);
    
    void onLoadCompleted(final int p0, final MediaSource.MediaPeriodId p1, final LoadEventInfo p2, final MediaLoadData p3);
    
    void onLoadError(final int p0, final MediaSource.MediaPeriodId p1, final LoadEventInfo p2, final MediaLoadData p3, final IOException p4, final boolean p5);
    
    void onLoadStarted(final int p0, final MediaSource.MediaPeriodId p1, final LoadEventInfo p2, final MediaLoadData p3);
    
    void onMediaPeriodCreated(final int p0, final MediaSource.MediaPeriodId p1);
    
    void onMediaPeriodReleased(final int p0, final MediaSource.MediaPeriodId p1);
    
    void onReadingStarted(final int p0, final MediaSource.MediaPeriodId p1);
    
    void onUpstreamDiscarded(final int p0, final MediaSource.MediaPeriodId p1, final MediaLoadData p2);
    
    public static final class EventDispatcher
    {
        private final CopyOnWriteArrayList<ListenerAndHandler> listenerAndHandlers;
        public final MediaSource.MediaPeriodId mediaPeriodId;
        private final long mediaTimeOffsetMs;
        public final int windowIndex;
        
        public EventDispatcher() {
            this(new CopyOnWriteArrayList<ListenerAndHandler>(), 0, null, 0L);
        }
        
        private EventDispatcher(final CopyOnWriteArrayList<ListenerAndHandler> listenerAndHandlers, final int windowIndex, final MediaSource.MediaPeriodId mediaPeriodId, final long mediaTimeOffsetMs) {
            this.listenerAndHandlers = listenerAndHandlers;
            this.windowIndex = windowIndex;
            this.mediaPeriodId = mediaPeriodId;
            this.mediaTimeOffsetMs = mediaTimeOffsetMs;
        }
        
        private long adjustMediaTime(long n) {
            final long usToMs = C.usToMs(n);
            n = -9223372036854775807L;
            if (usToMs != -9223372036854775807L) {
                n = this.mediaTimeOffsetMs + usToMs;
            }
            return n;
        }
        
        private void postOrRun(final Handler handler, final Runnable runnable) {
            if (handler.getLooper() == Looper.myLooper()) {
                runnable.run();
            }
            else {
                handler.post(runnable);
            }
        }
        
        public void addEventListener(final Handler handler, final MediaSourceEventListener mediaSourceEventListener) {
            Assertions.checkArgument(handler != null && mediaSourceEventListener != null);
            this.listenerAndHandlers.add(new ListenerAndHandler(handler, mediaSourceEventListener));
        }
        
        public void downstreamFormatChanged(final int n, final Format format, final int n2, final Object o, final long n3) {
            this.downstreamFormatChanged(new MediaLoadData(1, n, format, n2, o, this.adjustMediaTime(n3), -9223372036854775807L));
        }
        
        public void downstreamFormatChanged(final MediaLoadData mediaLoadData) {
            for (final ListenerAndHandler listenerAndHandler : this.listenerAndHandlers) {
                this.postOrRun(listenerAndHandler.handler, new _$$Lambda$MediaSourceEventListener$EventDispatcher$ES4FdQzWtupQEe6zuV_1M9_f9xU(this, listenerAndHandler.listener, mediaLoadData));
            }
        }
        
        public void loadCanceled(final LoadEventInfo loadEventInfo, final MediaLoadData mediaLoadData) {
            for (final ListenerAndHandler listenerAndHandler : this.listenerAndHandlers) {
                this.postOrRun(listenerAndHandler.handler, new _$$Lambda$MediaSourceEventListener$EventDispatcher$1_VoN1d1C8yHbFOrB_mXtUwAn3M(this, listenerAndHandler.listener, loadEventInfo, mediaLoadData));
            }
        }
        
        public void loadCanceled(final DataSpec dataSpec, final Uri uri, final Map<String, List<String>> map, final int n, final int n2, final Format format, final int n3, final Object o, final long n4, final long n5, final long n6, final long n7, final long n8) {
            this.loadCanceled(new LoadEventInfo(dataSpec, uri, map, n6, n7, n8), new MediaLoadData(n, n2, format, n3, o, this.adjustMediaTime(n4), this.adjustMediaTime(n5)));
        }
        
        public void loadCanceled(final DataSpec dataSpec, final Uri uri, final Map<String, List<String>> map, final int n, final long n2, final long n3, final long n4) {
            this.loadCanceled(dataSpec, uri, map, n, -1, null, 0, null, -9223372036854775807L, -9223372036854775807L, n2, n3, n4);
        }
        
        public void loadCompleted(final LoadEventInfo loadEventInfo, final MediaLoadData mediaLoadData) {
            for (final ListenerAndHandler listenerAndHandler : this.listenerAndHandlers) {
                this.postOrRun(listenerAndHandler.handler, new _$$Lambda$MediaSourceEventListener$EventDispatcher$IejPnkXyHgj2V1iyO1dqtBKfihI(this, listenerAndHandler.listener, loadEventInfo, mediaLoadData));
            }
        }
        
        public void loadCompleted(final DataSpec dataSpec, final Uri uri, final Map<String, List<String>> map, final int n, final int n2, final Format format, final int n3, final Object o, final long n4, final long n5, final long n6, final long n7, final long n8) {
            this.loadCompleted(new LoadEventInfo(dataSpec, uri, map, n6, n7, n8), new MediaLoadData(n, n2, format, n3, o, this.adjustMediaTime(n4), this.adjustMediaTime(n5)));
        }
        
        public void loadCompleted(final DataSpec dataSpec, final Uri uri, final Map<String, List<String>> map, final int n, final long n2, final long n3, final long n4) {
            this.loadCompleted(dataSpec, uri, map, n, -1, null, 0, null, -9223372036854775807L, -9223372036854775807L, n2, n3, n4);
        }
        
        public void loadError(final LoadEventInfo loadEventInfo, final MediaLoadData mediaLoadData, final IOException ex, final boolean b) {
            for (final ListenerAndHandler listenerAndHandler : this.listenerAndHandlers) {
                this.postOrRun(listenerAndHandler.handler, new _$$Lambda$MediaSourceEventListener$EventDispatcher$0X_TAsNqR4TUW1yA_ZD1_p3oT84(this, listenerAndHandler.listener, loadEventInfo, mediaLoadData, ex, b));
            }
        }
        
        public void loadError(final DataSpec dataSpec, final Uri uri, final Map<String, List<String>> map, final int n, final int n2, final Format format, final int n3, final Object o, final long n4, final long n5, final long n6, final long n7, final long n8, final IOException ex, final boolean b) {
            this.loadError(new LoadEventInfo(dataSpec, uri, map, n6, n7, n8), new MediaLoadData(n, n2, format, n3, o, this.adjustMediaTime(n4), this.adjustMediaTime(n5)), ex, b);
        }
        
        public void loadError(final DataSpec dataSpec, final Uri uri, final Map<String, List<String>> map, final int n, final long n2, final long n3, final long n4, final IOException ex, final boolean b) {
            this.loadError(dataSpec, uri, map, n, -1, null, 0, null, -9223372036854775807L, -9223372036854775807L, n2, n3, n4, ex, b);
        }
        
        public void loadStarted(final LoadEventInfo loadEventInfo, final MediaLoadData mediaLoadData) {
            for (final ListenerAndHandler listenerAndHandler : this.listenerAndHandlers) {
                this.postOrRun(listenerAndHandler.handler, new _$$Lambda$MediaSourceEventListener$EventDispatcher$WQKVpIh5ilpOizOGmbnyUThugMU(this, listenerAndHandler.listener, loadEventInfo, mediaLoadData));
            }
        }
        
        public void loadStarted(final DataSpec dataSpec, final int n, final int n2, final Format format, final int n3, final Object o, final long n4, final long n5, final long n6) {
            this.loadStarted(new LoadEventInfo(dataSpec, dataSpec.uri, Collections.emptyMap(), n6, 0L, 0L), new MediaLoadData(n, n2, format, n3, o, this.adjustMediaTime(n4), this.adjustMediaTime(n5)));
        }
        
        public void loadStarted(final DataSpec dataSpec, final int n, final long n2) {
            this.loadStarted(dataSpec, n, -1, null, 0, null, -9223372036854775807L, -9223372036854775807L, n2);
        }
        
        public void mediaPeriodCreated() {
            final MediaSource.MediaPeriodId mediaPeriodId = this.mediaPeriodId;
            Assertions.checkNotNull(mediaPeriodId);
            final MediaSource.MediaPeriodId mediaPeriodId2 = mediaPeriodId;
            for (final ListenerAndHandler listenerAndHandler : this.listenerAndHandlers) {
                this.postOrRun(listenerAndHandler.handler, new _$$Lambda$MediaSourceEventListener$EventDispatcher$N_EOPAK5UK0__YMNjezq7UM3UNI(this, listenerAndHandler.listener, mediaPeriodId2));
            }
        }
        
        public void mediaPeriodReleased() {
            final MediaSource.MediaPeriodId mediaPeriodId = this.mediaPeriodId;
            Assertions.checkNotNull(mediaPeriodId);
            final MediaSource.MediaPeriodId mediaPeriodId2 = mediaPeriodId;
            for (final ListenerAndHandler listenerAndHandler : this.listenerAndHandlers) {
                this.postOrRun(listenerAndHandler.handler, new _$$Lambda$MediaSourceEventListener$EventDispatcher$zyck4ebRbqvR6eQIjdzRcIBkRbI(this, listenerAndHandler.listener, mediaPeriodId2));
            }
        }
        
        public void readingStarted() {
            final MediaSource.MediaPeriodId mediaPeriodId = this.mediaPeriodId;
            Assertions.checkNotNull(mediaPeriodId);
            final MediaSource.MediaPeriodId mediaPeriodId2 = mediaPeriodId;
            for (final ListenerAndHandler listenerAndHandler : this.listenerAndHandlers) {
                this.postOrRun(listenerAndHandler.handler, new _$$Lambda$MediaSourceEventListener$EventDispatcher$PV8wmqGm7vRMJNlt__V3zhXfxiE(this, listenerAndHandler.listener, mediaPeriodId2));
            }
        }
        
        public void removeEventListener(final MediaSourceEventListener mediaSourceEventListener) {
            for (final ListenerAndHandler o : this.listenerAndHandlers) {
                if (o.listener == mediaSourceEventListener) {
                    this.listenerAndHandlers.remove(o);
                }
            }
        }
        
        public void upstreamDiscarded(final int n, final long n2, final long n3) {
            this.upstreamDiscarded(new MediaLoadData(1, n, null, 3, null, this.adjustMediaTime(n2), this.adjustMediaTime(n3)));
        }
        
        public void upstreamDiscarded(final MediaLoadData mediaLoadData) {
            final MediaSource.MediaPeriodId mediaPeriodId = this.mediaPeriodId;
            Assertions.checkNotNull(mediaPeriodId);
            final MediaSource.MediaPeriodId mediaPeriodId2 = mediaPeriodId;
            for (final ListenerAndHandler listenerAndHandler : this.listenerAndHandlers) {
                this.postOrRun(listenerAndHandler.handler, new _$$Lambda$MediaSourceEventListener$EventDispatcher$BtPa14lQQTv1oUeMy_9QaCysWHY(this, listenerAndHandler.listener, mediaPeriodId2, mediaLoadData));
            }
        }
        
        public EventDispatcher withParameters(final int n, final MediaSource.MediaPeriodId mediaPeriodId, final long n2) {
            return new EventDispatcher(this.listenerAndHandlers, n, mediaPeriodId, n2);
        }
        
        private static final class ListenerAndHandler
        {
            public final Handler handler;
            public final MediaSourceEventListener listener;
            
            public ListenerAndHandler(final Handler handler, final MediaSourceEventListener listener) {
                this.handler = handler;
                this.listener = listener;
            }
        }
    }
    
    public static final class LoadEventInfo
    {
        public final long bytesLoaded;
        public final DataSpec dataSpec;
        public final long elapsedRealtimeMs;
        public final long loadDurationMs;
        public final Map<String, List<String>> responseHeaders;
        public final Uri uri;
        
        public LoadEventInfo(final DataSpec dataSpec, final Uri uri, final Map<String, List<String>> responseHeaders, final long elapsedRealtimeMs, final long loadDurationMs, final long bytesLoaded) {
            this.dataSpec = dataSpec;
            this.uri = uri;
            this.responseHeaders = responseHeaders;
            this.elapsedRealtimeMs = elapsedRealtimeMs;
            this.loadDurationMs = loadDurationMs;
            this.bytesLoaded = bytesLoaded;
        }
    }
    
    public static final class MediaLoadData
    {
        public final int dataType;
        public final long mediaEndTimeMs;
        public final long mediaStartTimeMs;
        public final Format trackFormat;
        public final Object trackSelectionData;
        public final int trackSelectionReason;
        public final int trackType;
        
        public MediaLoadData(final int dataType, final int trackType, final Format trackFormat, final int trackSelectionReason, final Object trackSelectionData, final long mediaStartTimeMs, final long mediaEndTimeMs) {
            this.dataType = dataType;
            this.trackType = trackType;
            this.trackFormat = trackFormat;
            this.trackSelectionReason = trackSelectionReason;
            this.trackSelectionData = trackSelectionData;
            this.mediaStartTimeMs = mediaStartTimeMs;
            this.mediaEndTimeMs = mediaEndTimeMs;
        }
    }
}
