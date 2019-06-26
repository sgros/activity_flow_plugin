// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.data;

import android.annotation.SuppressLint;

@SuppressLint({ "ParcelCreator" })
public class RadarEntry extends Entry
{
    public RadarEntry(final float n) {
        super(0.0f, n);
    }
    
    public RadarEntry(final float n, final Object o) {
        super(0.0f, n, o);
    }
    
    @Override
    public RadarEntry copy() {
        return new RadarEntry(this.getY(), this.getData());
    }
    
    public float getValue() {
        return this.getY();
    }
    
    @Deprecated
    @Override
    public float getX() {
        return super.getX();
    }
    
    @Deprecated
    @Override
    public void setX(final float x) {
        super.setX(x);
    }
}
