// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2;

import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.upstream.Allocator;

public interface LoadControl
{
    Allocator getAllocator();
    
    long getBackBufferDurationUs();
    
    void onPrepared();
    
    void onReleased();
    
    void onStopped();
    
    void onTracksSelected(final Renderer[] p0, final TrackGroupArray p1, final TrackSelectionArray p2);
    
    boolean retainBackBufferFromKeyframe();
    
    boolean shouldContinueLoading(final long p0, final float p1);
    
    boolean shouldStartPlayback(final long p0, final float p1, final boolean p2);
}
