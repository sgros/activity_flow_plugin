// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor;

import java.util.Arrays;
import com.google.android.exoplayer2.util.Util;

public final class ChunkIndex implements SeekMap
{
    private final long durationUs;
    public final long[] durationsUs;
    public final int length;
    public final long[] offsets;
    public final int[] sizes;
    public final long[] timesUs;
    
    public ChunkIndex(final int[] sizes, final long[] offsets, final long[] durationsUs, final long[] timesUs) {
        this.sizes = sizes;
        this.offsets = offsets;
        this.durationsUs = durationsUs;
        this.timesUs = timesUs;
        this.length = sizes.length;
        final int length = this.length;
        if (length > 0) {
            this.durationUs = durationsUs[length - 1] + timesUs[length - 1];
        }
        else {
            this.durationUs = 0L;
        }
    }
    
    public int getChunkIndex(final long n) {
        return Util.binarySearchFloor(this.timesUs, n, true, true);
    }
    
    @Override
    public long getDurationUs() {
        return this.durationUs;
    }
    
    @Override
    public SeekPoints getSeekPoints(final long n) {
        int chunkIndex = this.getChunkIndex(n);
        final SeekPoint seekPoint = new SeekPoint(this.timesUs[chunkIndex], this.offsets[chunkIndex]);
        if (seekPoint.timeUs < n && chunkIndex != this.length - 1) {
            final long[] timesUs = this.timesUs;
            ++chunkIndex;
            return new SeekPoints(seekPoint, new SeekPoint(timesUs[chunkIndex], this.offsets[chunkIndex]));
        }
        return new SeekPoints(seekPoint);
    }
    
    @Override
    public boolean isSeekable() {
        return true;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ChunkIndex(length=");
        sb.append(this.length);
        sb.append(", sizes=");
        sb.append(Arrays.toString(this.sizes));
        sb.append(", offsets=");
        sb.append(Arrays.toString(this.offsets));
        sb.append(", timeUs=");
        sb.append(Arrays.toString(this.timesUs));
        sb.append(", durationsUs=");
        sb.append(Arrays.toString(this.durationsUs));
        sb.append(")");
        return sb.toString();
    }
}
