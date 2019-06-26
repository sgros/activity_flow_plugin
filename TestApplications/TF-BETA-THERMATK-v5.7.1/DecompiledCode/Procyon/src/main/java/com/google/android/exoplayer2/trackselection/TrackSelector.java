// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.trackselection;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.RendererCapabilities;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.upstream.BandwidthMeter;

public abstract class TrackSelector
{
    private BandwidthMeter bandwidthMeter;
    private InvalidationListener listener;
    
    protected final BandwidthMeter getBandwidthMeter() {
        final BandwidthMeter bandwidthMeter = this.bandwidthMeter;
        Assertions.checkNotNull(bandwidthMeter);
        return bandwidthMeter;
    }
    
    public final void init(final InvalidationListener listener, final BandwidthMeter bandwidthMeter) {
        this.listener = listener;
        this.bandwidthMeter = bandwidthMeter;
    }
    
    public abstract void onSelectionActivated(final Object p0);
    
    public abstract TrackSelectorResult selectTracks(final RendererCapabilities[] p0, final TrackGroupArray p1, final MediaSource.MediaPeriodId p2, final Timeline p3) throws ExoPlaybackException;
    
    public interface InvalidationListener
    {
    }
}
