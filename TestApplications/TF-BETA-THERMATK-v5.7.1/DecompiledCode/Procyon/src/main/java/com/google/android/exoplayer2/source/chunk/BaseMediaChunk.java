// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.chunk;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DataSource;

public abstract class BaseMediaChunk extends MediaChunk
{
    public final long clippedEndTimeUs;
    public final long clippedStartTimeUs;
    private int[] firstSampleIndices;
    private BaseMediaChunkOutput output;
    
    public BaseMediaChunk(final DataSource dataSource, final DataSpec dataSpec, final Format format, final int n, final Object o, final long n2, final long n3, final long clippedStartTimeUs, final long clippedEndTimeUs, final long n4) {
        super(dataSource, dataSpec, format, n, o, n2, n3, n4);
        this.clippedStartTimeUs = clippedStartTimeUs;
        this.clippedEndTimeUs = clippedEndTimeUs;
    }
    
    public final int getFirstSampleIndex(final int n) {
        return this.firstSampleIndices[n];
    }
    
    protected final BaseMediaChunkOutput getOutput() {
        return this.output;
    }
    
    public void init(final BaseMediaChunkOutput output) {
        this.output = output;
        this.firstSampleIndices = output.getWriteIndices();
    }
}
