// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.util;

import com.google.android.exoplayer2.PlaybackParameters;

public interface MediaClock
{
    PlaybackParameters getPlaybackParameters();
    
    long getPositionUs();
    
    PlaybackParameters setPlaybackParameters(final PlaybackParameters p0);
}
