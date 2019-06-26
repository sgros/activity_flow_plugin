// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.trackselection;

import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.source.chunk.MediaChunkIterator;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.source.chunk.MediaChunk;
import java.util.List;

public interface TrackSelection
{
    boolean blacklist(final int p0, final long p1);
    
    void disable();
    
    void enable();
    
    int evaluateQueueSize(final long p0, final List<? extends MediaChunk> p1);
    
    Format getFormat(final int p0);
    
    int getIndexInTrackGroup(final int p0);
    
    Format getSelectedFormat();
    
    int getSelectedIndex();
    
    int getSelectedIndexInTrackGroup();
    
    Object getSelectionData();
    
    int getSelectionReason();
    
    TrackGroup getTrackGroup();
    
    int indexOf(final int p0);
    
    int indexOf(final Format p0);
    
    int length();
    
    void onDiscontinuity();
    
    void onPlaybackSpeed(final float p0);
    
    @Deprecated
    void updateSelectedTrack(final long p0, final long p1, final long p2);
    
    void updateSelectedTrack(final long p0, final long p1, final long p2, final List<? extends MediaChunk> p3, final MediaChunkIterator[] p4);
    
    public static final class Definition
    {
        public final TrackGroup group;
        public final int[] tracks;
        
        public Definition(final TrackGroup group, final int... tracks) {
            this.group = group;
            this.tracks = tracks;
        }
    }
    
    public interface Factory
    {
        TrackSelection[] createTrackSelections(final Definition[] p0, final BandwidthMeter p1);
    }
}
