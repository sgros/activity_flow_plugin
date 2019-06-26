// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.chunk;

import com.google.android.exoplayer2.extractor.DummyTrackOutput;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.source.SampleQueue;

public final class BaseMediaChunkOutput implements TrackOutputProvider
{
    private final SampleQueue[] sampleQueues;
    private final int[] trackTypes;
    
    public BaseMediaChunkOutput(final int[] trackTypes, final SampleQueue[] sampleQueues) {
        this.trackTypes = trackTypes;
        this.sampleQueues = sampleQueues;
    }
    
    public int[] getWriteIndices() {
        final int[] array = new int[this.sampleQueues.length];
        int n = 0;
        while (true) {
            final SampleQueue[] sampleQueues = this.sampleQueues;
            if (n >= sampleQueues.length) {
                break;
            }
            if (sampleQueues[n] != null) {
                array[n] = sampleQueues[n].getWriteIndex();
            }
            ++n;
        }
        return array;
    }
    
    public void setSampleOffsetUs(final long sampleOffsetUs) {
        for (final SampleQueue sampleQueue : this.sampleQueues) {
            if (sampleQueue != null) {
                sampleQueue.setSampleOffsetUs(sampleOffsetUs);
            }
        }
    }
    
    @Override
    public TrackOutput track(int n, final int i) {
        n = 0;
        while (true) {
            final int[] trackTypes = this.trackTypes;
            if (n >= trackTypes.length) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unmatched track of type: ");
                sb.append(i);
                Log.e("BaseMediaChunkOutput", sb.toString());
                return new DummyTrackOutput();
            }
            if (i == trackTypes[n]) {
                return this.sampleQueues[n];
            }
            ++n;
        }
    }
}
