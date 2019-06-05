// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.buffer;

public abstract class AbstractBuffer<T>
{
    public final float[] buffer;
    protected int index;
    protected int mFrom;
    protected int mTo;
    protected float phaseX;
    protected float phaseY;
    
    public AbstractBuffer(final int n) {
        this.index = 0;
        this.phaseX = 1.0f;
        this.phaseY = 1.0f;
        this.mFrom = 0;
        this.mTo = 0;
        this.index = 0;
        this.buffer = new float[n];
    }
    
    public abstract void feed(final T p0);
    
    public void limitFrom(final int n) {
        int mFrom = n;
        if (n < 0) {
            mFrom = 0;
        }
        this.mFrom = mFrom;
    }
    
    public void limitTo(final int n) {
        int mTo = n;
        if (n < 0) {
            mTo = 0;
        }
        this.mTo = mTo;
    }
    
    public void reset() {
        this.index = 0;
    }
    
    public void setPhases(final float phaseX, final float phaseY) {
        this.phaseX = phaseX;
        this.phaseY = phaseY;
    }
    
    public int size() {
        return this.buffer.length;
    }
}
