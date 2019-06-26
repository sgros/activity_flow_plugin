// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source;

public abstract class DefaultMediaSourceEventListener implements MediaSourceEventListener
{
    @Override
    public void onDownstreamFormatChanged(final int n, final MediaSource.MediaPeriodId mediaPeriodId, final MediaLoadData mediaLoadData) {
    }
    
    @Override
    public void onLoadCanceled(final int n, final MediaSource.MediaPeriodId mediaPeriodId, final LoadEventInfo loadEventInfo, final MediaLoadData mediaLoadData) {
    }
    
    @Override
    public void onLoadCompleted(final int n, final MediaSource.MediaPeriodId mediaPeriodId, final LoadEventInfo loadEventInfo, final MediaLoadData mediaLoadData) {
    }
    
    @Override
    public void onLoadStarted(final int n, final MediaSource.MediaPeriodId mediaPeriodId, final LoadEventInfo loadEventInfo, final MediaLoadData mediaLoadData) {
    }
    
    @Override
    public void onMediaPeriodCreated(final int n, final MediaSource.MediaPeriodId mediaPeriodId) {
    }
    
    @Override
    public void onMediaPeriodReleased(final int n, final MediaSource.MediaPeriodId mediaPeriodId) {
    }
    
    @Override
    public void onReadingStarted(final int n, final MediaSource.MediaPeriodId mediaPeriodId) {
    }
    
    @Override
    public void onUpstreamDiscarded(final int n, final MediaSource.MediaPeriodId mediaPeriodId, final MediaLoadData mediaLoadData) {
    }
}
