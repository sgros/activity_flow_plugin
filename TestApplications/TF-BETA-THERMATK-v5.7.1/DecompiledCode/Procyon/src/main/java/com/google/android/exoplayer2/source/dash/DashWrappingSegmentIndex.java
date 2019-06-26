// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.dash;

import com.google.android.exoplayer2.source.dash.manifest.RangedUri;
import com.google.android.exoplayer2.extractor.ChunkIndex;

public final class DashWrappingSegmentIndex implements DashSegmentIndex
{
    private final ChunkIndex chunkIndex;
    private final long timeOffsetUs;
    
    public DashWrappingSegmentIndex(final ChunkIndex chunkIndex, final long timeOffsetUs) {
        this.chunkIndex = chunkIndex;
        this.timeOffsetUs = timeOffsetUs;
    }
    
    @Override
    public long getDurationUs(final long n, final long n2) {
        return this.chunkIndex.durationsUs[(int)n];
    }
    
    @Override
    public long getFirstSegmentNum() {
        return 0L;
    }
    
    @Override
    public int getSegmentCount(final long n) {
        return this.chunkIndex.length;
    }
    
    @Override
    public long getSegmentNum(final long n, final long n2) {
        return this.chunkIndex.getChunkIndex(n + this.timeOffsetUs);
    }
    
    @Override
    public RangedUri getSegmentUrl(final long n) {
        final ChunkIndex chunkIndex = this.chunkIndex;
        final long[] offsets = chunkIndex.offsets;
        final int n2 = (int)n;
        return new RangedUri(null, offsets[n2], chunkIndex.sizes[n2]);
    }
    
    @Override
    public long getTimeUs(final long n) {
        return this.chunkIndex.timesUs[(int)n] - this.timeOffsetUs;
    }
    
    @Override
    public boolean isExplicit() {
        return true;
    }
}
