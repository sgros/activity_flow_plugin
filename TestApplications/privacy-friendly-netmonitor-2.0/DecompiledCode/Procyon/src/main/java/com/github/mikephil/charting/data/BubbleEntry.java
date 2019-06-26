// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.data;

import android.graphics.drawable.Drawable;
import android.annotation.SuppressLint;

@SuppressLint({ "ParcelCreator" })
public class BubbleEntry extends Entry
{
    private float mSize;
    
    public BubbleEntry(final float n, final float n2, final float mSize) {
        super(n, n2);
        this.mSize = 0.0f;
        this.mSize = mSize;
    }
    
    public BubbleEntry(final float n, final float n2, final float mSize, final Drawable drawable) {
        super(n, n2, drawable);
        this.mSize = 0.0f;
        this.mSize = mSize;
    }
    
    public BubbleEntry(final float n, final float n2, final float mSize, final Drawable drawable, final Object o) {
        super(n, n2, drawable, o);
        this.mSize = 0.0f;
        this.mSize = mSize;
    }
    
    public BubbleEntry(final float n, final float n2, final float mSize, final Object o) {
        super(n, n2, o);
        this.mSize = 0.0f;
        this.mSize = mSize;
    }
    
    @Override
    public BubbleEntry copy() {
        return new BubbleEntry(this.getX(), this.getY(), this.mSize, this.getData());
    }
    
    public float getSize() {
        return this.mSize;
    }
    
    public void setSize(final float mSize) {
        this.mSize = mSize;
    }
}
