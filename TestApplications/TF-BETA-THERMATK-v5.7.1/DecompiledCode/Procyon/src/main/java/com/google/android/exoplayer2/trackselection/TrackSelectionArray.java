// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.trackselection;

import java.util.Arrays;

public final class TrackSelectionArray
{
    private int hashCode;
    public final int length;
    private final TrackSelection[] trackSelections;
    
    public TrackSelectionArray(final TrackSelection... trackSelections) {
        this.trackSelections = trackSelections;
        this.length = trackSelections.length;
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o != null && TrackSelectionArray.class == o.getClass() && Arrays.equals(this.trackSelections, ((TrackSelectionArray)o).trackSelections));
    }
    
    public TrackSelection get(final int n) {
        return this.trackSelections[n];
    }
    
    public TrackSelection[] getAll() {
        return this.trackSelections.clone();
    }
    
    @Override
    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = 527 + Arrays.hashCode(this.trackSelections);
        }
        return this.hashCode;
    }
}
