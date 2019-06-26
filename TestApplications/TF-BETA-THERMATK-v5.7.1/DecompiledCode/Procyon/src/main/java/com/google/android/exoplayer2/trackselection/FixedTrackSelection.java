// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.trackselection;

import com.google.android.exoplayer2.source.chunk.MediaChunkIterator;
import com.google.android.exoplayer2.source.chunk.MediaChunk;
import java.util.List;
import com.google.android.exoplayer2.source.TrackGroup;

public final class FixedTrackSelection extends BaseTrackSelection
{
    private final Object data;
    private final int reason;
    
    public FixedTrackSelection(final TrackGroup trackGroup, final int n) {
        this(trackGroup, n, 0, null);
    }
    
    public FixedTrackSelection(final TrackGroup trackGroup, final int n, final int reason, final Object data) {
        super(trackGroup, new int[] { n });
        this.reason = reason;
        this.data = data;
    }
    
    @Override
    public int getSelectedIndex() {
        return 0;
    }
    
    @Override
    public Object getSelectionData() {
        return this.data;
    }
    
    @Override
    public int getSelectionReason() {
        return this.reason;
    }
    
    @Override
    public void updateSelectedTrack(final long n, final long n2, final long n3, final List<? extends MediaChunk> list, final MediaChunkIterator[] array) {
    }
}
