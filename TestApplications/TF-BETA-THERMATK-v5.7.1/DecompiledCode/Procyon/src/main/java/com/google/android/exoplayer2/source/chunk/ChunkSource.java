// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.chunk;

import java.io.IOException;
import java.util.List;
import com.google.android.exoplayer2.SeekParameters;

public interface ChunkSource
{
    long getAdjustedSeekPositionUs(final long p0, final SeekParameters p1);
    
    void getNextChunk(final long p0, final long p1, final List<? extends MediaChunk> p2, final ChunkHolder p3);
    
    int getPreferredQueueSize(final long p0, final List<? extends MediaChunk> p1);
    
    void maybeThrowError() throws IOException;
    
    void onChunkLoadCompleted(final Chunk p0);
    
    boolean onChunkLoadError(final Chunk p0, final boolean p1, final Exception p2, final long p3);
}
