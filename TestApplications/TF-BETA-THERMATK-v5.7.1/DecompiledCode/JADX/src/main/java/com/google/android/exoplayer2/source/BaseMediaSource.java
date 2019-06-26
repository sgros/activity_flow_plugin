package com.google.android.exoplayer2.source;

import android.os.Handler;
import android.os.Looper;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource.MediaPeriodId;
import com.google.android.exoplayer2.source.MediaSource.SourceInfoRefreshListener;
import com.google.android.exoplayer2.source.MediaSourceEventListener.EventDispatcher;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Assertions;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class BaseMediaSource implements MediaSource {
    private final EventDispatcher eventDispatcher = new EventDispatcher();
    private Looper looper;
    private Object manifest;
    private final ArrayList<SourceInfoRefreshListener> sourceInfoListeners = new ArrayList(1);
    private Timeline timeline;

    public abstract void prepareSourceInternal(TransferListener transferListener);

    public abstract void releaseSourceInternal();

    /* Access modifiers changed, original: protected|final */
    public final void refreshSourceInfo(Timeline timeline, Object obj) {
        this.timeline = timeline;
        this.manifest = obj;
        Iterator it = this.sourceInfoListeners.iterator();
        while (it.hasNext()) {
            ((SourceInfoRefreshListener) it.next()).onSourceInfoRefreshed(this, timeline, obj);
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final EventDispatcher createEventDispatcher(MediaPeriodId mediaPeriodId) {
        return this.eventDispatcher.withParameters(0, mediaPeriodId, 0);
    }

    /* Access modifiers changed, original: protected|final */
    public final EventDispatcher createEventDispatcher(MediaPeriodId mediaPeriodId, long j) {
        Assertions.checkArgument(mediaPeriodId != null);
        return this.eventDispatcher.withParameters(0, mediaPeriodId, j);
    }

    /* Access modifiers changed, original: protected|final */
    public final EventDispatcher createEventDispatcher(int i, MediaPeriodId mediaPeriodId, long j) {
        return this.eventDispatcher.withParameters(i, mediaPeriodId, j);
    }

    public final void addEventListener(Handler handler, MediaSourceEventListener mediaSourceEventListener) {
        this.eventDispatcher.addEventListener(handler, mediaSourceEventListener);
    }

    public final void removeEventListener(MediaSourceEventListener mediaSourceEventListener) {
        this.eventDispatcher.removeEventListener(mediaSourceEventListener);
    }

    public final void prepareSource(SourceInfoRefreshListener sourceInfoRefreshListener, TransferListener transferListener) {
        Looper myLooper = Looper.myLooper();
        Looper looper = this.looper;
        boolean z = looper == null || looper == myLooper;
        Assertions.checkArgument(z);
        this.sourceInfoListeners.add(sourceInfoRefreshListener);
        if (this.looper == null) {
            this.looper = myLooper;
            prepareSourceInternal(transferListener);
            return;
        }
        Timeline timeline = this.timeline;
        if (timeline != null) {
            sourceInfoRefreshListener.onSourceInfoRefreshed(this, timeline, this.manifest);
        }
    }

    public final void releaseSource(SourceInfoRefreshListener sourceInfoRefreshListener) {
        this.sourceInfoListeners.remove(sourceInfoRefreshListener);
        if (this.sourceInfoListeners.isEmpty()) {
            this.looper = null;
            this.timeline = null;
            this.manifest = null;
            releaseSourceInternal();
        }
    }
}
