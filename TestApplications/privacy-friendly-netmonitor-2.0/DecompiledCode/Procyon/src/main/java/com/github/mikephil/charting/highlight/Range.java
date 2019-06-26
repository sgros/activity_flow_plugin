// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.highlight;

public final class Range
{
    public float from;
    public float to;
    
    public Range(final float from, final float to) {
        this.from = from;
        this.to = to;
    }
    
    public boolean contains(final float n) {
        return n > this.from && n <= this.to;
    }
    
    public boolean isLarger(final float n) {
        return n > this.to;
    }
    
    public boolean isSmaller(final float n) {
        return n < this.from;
    }
}
