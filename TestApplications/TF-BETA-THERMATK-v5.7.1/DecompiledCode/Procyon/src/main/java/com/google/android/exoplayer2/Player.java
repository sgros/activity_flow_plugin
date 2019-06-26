// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2;

import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.source.TrackGroupArray;

public interface Player
{
    long getBufferedPosition();
    
    long getContentPosition();
    
    int getCurrentAdGroupIndex();
    
    int getCurrentAdIndexInAdGroup();
    
    long getCurrentPosition();
    
    Timeline getCurrentTimeline();
    
    int getCurrentWindowIndex();
    
    long getDuration();
    
    long getTotalBufferedDuration();
    
    void seekTo(final int p0, final long p1);
    
    public interface AudioComponent
    {
    }
    
    public interface EventListener
    {
        void onLoadingChanged(final boolean p0);
        
        void onPlaybackParametersChanged(final PlaybackParameters p0);
        
        void onPlayerError(final ExoPlaybackException p0);
        
        void onPlayerStateChanged(final boolean p0, final int p1);
        
        void onPositionDiscontinuity(final int p0);
        
        void onSeekProcessed();
        
        void onTimelineChanged(final Timeline p0, final Object p1, final int p2);
        
        void onTracksChanged(final TrackGroupArray p0, final TrackSelectionArray p1);
    }
    
    public interface MetadataComponent
    {
    }
    
    public interface TextComponent
    {
    }
    
    public interface VideoComponent
    {
    }
}
