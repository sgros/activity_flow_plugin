// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.data;

import android.graphics.drawable.Drawable;

public abstract class BaseEntry
{
    private Object mData;
    private Drawable mIcon;
    private float y;
    
    public BaseEntry() {
        this.y = 0.0f;
        this.mData = null;
        this.mIcon = null;
    }
    
    public BaseEntry(final float y) {
        this.y = 0.0f;
        this.mData = null;
        this.mIcon = null;
        this.y = y;
    }
    
    public BaseEntry(final float n, final Drawable mIcon) {
        this(n);
        this.mIcon = mIcon;
    }
    
    public BaseEntry(final float n, final Drawable mIcon, final Object mData) {
        this(n);
        this.mIcon = mIcon;
        this.mData = mData;
    }
    
    public BaseEntry(final float n, final Object mData) {
        this(n);
        this.mData = mData;
    }
    
    public Object getData() {
        return this.mData;
    }
    
    public Drawable getIcon() {
        return this.mIcon;
    }
    
    public float getY() {
        return this.y;
    }
    
    public void setData(final Object mData) {
        this.mData = mData;
    }
    
    public void setIcon(final Drawable mIcon) {
        this.mIcon = mIcon;
    }
    
    public void setY(final float y) {
        this.y = y;
    }
}
