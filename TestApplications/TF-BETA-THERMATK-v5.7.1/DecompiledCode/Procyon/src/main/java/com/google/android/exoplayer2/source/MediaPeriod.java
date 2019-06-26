// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.trackselection.TrackSelection;
import java.io.IOException;
import com.google.android.exoplayer2.SeekParameters;

public interface MediaPeriod extends SequenceableLoader
{
    boolean continueLoading(final long p0);
    
    void discardBuffer(final long p0, final boolean p1);
    
    long getAdjustedSeekPositionUs(final long p0, final SeekParameters p1);
    
    long getBufferedPositionUs();
    
    long getNextLoadPositionUs();
    
    TrackGroupArray getTrackGroups();
    
    void maybeThrowPrepareError() throws IOException;
    
    void prepare(final Callback p0, final long p1);
    
    long readDiscontinuity();
    
    void reevaluateBuffer(final long p0);
    
    long seekToUs(final long p0);
    
    long selectTracks(final TrackSelection[] p0, final boolean[] p1, final SampleStream[] p2, final boolean[] p3, final long p4);
    
    public interface Callback extends SequenceableLoader.Callback<MediaPeriod>
    {
        void onPrepared(final MediaPeriod p0);
    }
}
