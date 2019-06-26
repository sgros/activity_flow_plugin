// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.chunk;

public abstract class BaseMediaChunkIterator implements MediaChunkIterator
{
    private long currentIndex;
    private final long fromIndex;
    private final long toIndex;
    
    public BaseMediaChunkIterator(final long fromIndex, final long toIndex) {
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
        this.reset();
    }
    
    public void reset() {
        this.currentIndex = this.fromIndex - 1L;
    }
}
