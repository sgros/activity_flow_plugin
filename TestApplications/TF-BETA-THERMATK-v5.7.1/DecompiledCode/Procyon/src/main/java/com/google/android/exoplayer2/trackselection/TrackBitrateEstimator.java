// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.trackselection;

import com.google.android.exoplayer2.source.chunk.MediaChunkIterator;
import com.google.android.exoplayer2.source.chunk.MediaChunk;
import java.util.List;
import com.google.android.exoplayer2.Format;

public interface TrackBitrateEstimator
{
    public static final TrackBitrateEstimator DEFAULT = _$$Lambda$TrackBitrateEstimator$2lQ5lBvmOkJuNPw2qehuzXBInmI.INSTANCE;
    
    int[] getBitrates(final Format[] p0, final List<? extends MediaChunk> p1, final MediaChunkIterator[] p2, final int[] p3);
}
