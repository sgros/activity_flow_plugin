// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source;

import java.util.Iterator;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Assertions;
import android.os.Handler;
import com.google.android.exoplayer2.Timeline;
import java.util.ArrayList;
import android.os.Looper;

public abstract class BaseMediaSource implements MediaSource
{
    private final MediaSourceEventListener.EventDispatcher eventDispatcher;
    private Looper looper;
    private Object manifest;
    private final ArrayList<SourceInfoRefreshListener> sourceInfoListeners;
    private Timeline timeline;
    
    public BaseMediaSource() {
        this.sourceInfoListeners = new ArrayList<SourceInfoRefreshListener>(1);
        this.eventDispatcher = new MediaSourceEventListener.EventDispatcher();
    }
    
    @Override
    public final void addEventListener(final Handler handler, final MediaSourceEventListener mediaSourceEventListener) {
        this.eventDispatcher.addEventListener(handler, mediaSourceEventListener);
    }
    
    protected final MediaSourceEventListener.EventDispatcher createEventDispatcher(final int n, final MediaPeriodId mediaPeriodId, final long n2) {
        return this.eventDispatcher.withParameters(n, mediaPeriodId, n2);
    }
    
    protected final MediaSourceEventListener.EventDispatcher createEventDispatcher(final MediaPeriodId mediaPeriodId) {
        return this.eventDispatcher.withParameters(0, mediaPeriodId, 0L);
    }
    
    protected final MediaSourceEventListener.EventDispatcher createEventDispatcher(final MediaPeriodId mediaPeriodId, final long n) {
        Assertions.checkArgument(mediaPeriodId != null);
        return this.eventDispatcher.withParameters(0, mediaPeriodId, n);
    }
    
    @Override
    public final void prepareSource(final SourceInfoRefreshListener e, final TransferListener transferListener) {
        final Looper myLooper = Looper.myLooper();
        final Looper looper = this.looper;
        Assertions.checkArgument(looper == null || looper == myLooper);
        this.sourceInfoListeners.add(e);
        if (this.looper == null) {
            this.looper = myLooper;
            this.prepareSourceInternal(transferListener);
        }
        else {
            final Timeline timeline = this.timeline;
            if (timeline != null) {
                e.onSourceInfoRefreshed(this, timeline, this.manifest);
            }
        }
    }
    
    protected abstract void prepareSourceInternal(final TransferListener p0);
    
    protected final void refreshSourceInfo(final Timeline timeline, final Object manifest) {
        this.timeline = timeline;
        this.manifest = manifest;
        final Iterator<SourceInfoRefreshListener> iterator = this.sourceInfoListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onSourceInfoRefreshed(this, timeline, manifest);
        }
    }
    
    @Override
    public final void releaseSource(final SourceInfoRefreshListener o) {
        this.sourceInfoListeners.remove(o);
        if (this.sourceInfoListeners.isEmpty()) {
            this.looper = null;
            this.timeline = null;
            this.manifest = null;
            this.releaseSourceInternal();
        }
    }
    
    protected abstract void releaseSourceInternal();
    
    @Override
    public final void removeEventListener(final MediaSourceEventListener mediaSourceEventListener) {
        this.eventDispatcher.removeEventListener(mediaSourceEventListener);
    }
}
