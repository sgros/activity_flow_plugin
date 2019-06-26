// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.chunk;

import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DataSource;

public abstract class MediaChunk extends Chunk
{
    public final long chunkIndex;
    
    public MediaChunk(final DataSource dataSource, final DataSpec dataSpec, final Format format, final int n, final Object o, final long n2, final long n3, final long chunkIndex) {
        super(dataSource, dataSpec, 1, format, n, o, n2, n3);
        Assertions.checkNotNull(format);
        this.chunkIndex = chunkIndex;
    }
    
    public long getNextChunkIndex() {
        final long chunkIndex = this.chunkIndex;
        long n = -1L;
        if (chunkIndex != -1L) {
            n = 1L + chunkIndex;
        }
        return n;
    }
    
    public abstract boolean isLoadCompleted();
}
