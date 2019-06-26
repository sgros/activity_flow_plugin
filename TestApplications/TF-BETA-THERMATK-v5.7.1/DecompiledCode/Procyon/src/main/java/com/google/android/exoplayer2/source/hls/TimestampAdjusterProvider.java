// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.hls;

import com.google.android.exoplayer2.util.TimestampAdjuster;
import android.util.SparseArray;

public final class TimestampAdjusterProvider
{
    private final SparseArray<TimestampAdjuster> timestampAdjusters;
    
    public TimestampAdjusterProvider() {
        this.timestampAdjusters = (SparseArray<TimestampAdjuster>)new SparseArray();
    }
    
    public TimestampAdjuster getAdjuster(final int n) {
        TimestampAdjuster timestampAdjuster;
        if ((timestampAdjuster = (TimestampAdjuster)this.timestampAdjusters.get(n)) == null) {
            timestampAdjuster = new TimestampAdjuster(Long.MAX_VALUE);
            this.timestampAdjusters.put(n, (Object)timestampAdjuster);
        }
        return timestampAdjuster;
    }
    
    public void reset() {
        this.timestampAdjusters.clear();
    }
}
