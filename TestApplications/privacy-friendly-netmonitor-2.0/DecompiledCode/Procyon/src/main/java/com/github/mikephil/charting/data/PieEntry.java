// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.data;

import android.util.Log;
import android.graphics.drawable.Drawable;
import android.annotation.SuppressLint;

@SuppressLint({ "ParcelCreator" })
public class PieEntry extends Entry
{
    private String label;
    
    public PieEntry(final float n) {
        super(0.0f, n);
    }
    
    public PieEntry(final float n, final Drawable drawable) {
        super(0.0f, n, drawable);
    }
    
    public PieEntry(final float n, final Drawable drawable, final Object o) {
        super(0.0f, n, drawable, o);
    }
    
    public PieEntry(final float n, final Object o) {
        super(0.0f, n, o);
    }
    
    public PieEntry(final float n, final String label) {
        super(0.0f, n);
        this.label = label;
    }
    
    public PieEntry(final float n, final String label, final Drawable drawable) {
        super(0.0f, n, drawable);
        this.label = label;
    }
    
    public PieEntry(final float n, final String label, final Drawable drawable, final Object o) {
        super(0.0f, n, drawable, o);
        this.label = label;
    }
    
    public PieEntry(final float n, final String label, final Object o) {
        super(0.0f, n, o);
        this.label = label;
    }
    
    @Override
    public PieEntry copy() {
        return new PieEntry(this.getY(), this.label, this.getData());
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public float getValue() {
        return this.getY();
    }
    
    @Deprecated
    @Override
    public float getX() {
        Log.i("DEPRECATED", "Pie entries do not have x values");
        return super.getX();
    }
    
    public void setLabel(final String label) {
        this.label = label;
    }
    
    @Deprecated
    @Override
    public void setX(final float x) {
        super.setX(x);
        Log.i("DEPRECATED", "Pie entries do not have x values");
    }
}
