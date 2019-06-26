// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.components;

import com.github.mikephil.charting.utils.Utils;
import android.graphics.Typeface;

public abstract class ComponentBase
{
    protected boolean mEnabled;
    protected int mTextColor;
    protected float mTextSize;
    protected Typeface mTypeface;
    protected float mXOffset;
    protected float mYOffset;
    
    public ComponentBase() {
        this.mEnabled = true;
        this.mXOffset = 5.0f;
        this.mYOffset = 5.0f;
        this.mTypeface = null;
        this.mTextSize = Utils.convertDpToPixel(10.0f);
        this.mTextColor = -16777216;
    }
    
    public int getTextColor() {
        return this.mTextColor;
    }
    
    public float getTextSize() {
        return this.mTextSize;
    }
    
    public Typeface getTypeface() {
        return this.mTypeface;
    }
    
    public float getXOffset() {
        return this.mXOffset;
    }
    
    public float getYOffset() {
        return this.mYOffset;
    }
    
    public boolean isEnabled() {
        return this.mEnabled;
    }
    
    public void setEnabled(final boolean mEnabled) {
        this.mEnabled = mEnabled;
    }
    
    public void setTextColor(final int mTextColor) {
        this.mTextColor = mTextColor;
    }
    
    public void setTextSize(float n) {
        float n2 = n;
        if (n > 24.0f) {
            n2 = 24.0f;
        }
        n = n2;
        if (n2 < 6.0f) {
            n = 6.0f;
        }
        this.mTextSize = Utils.convertDpToPixel(n);
    }
    
    public void setTypeface(final Typeface mTypeface) {
        this.mTypeface = mTypeface;
    }
    
    public void setXOffset(final float n) {
        this.mXOffset = Utils.convertDpToPixel(n);
    }
    
    public void setYOffset(final float n) {
        this.mYOffset = Utils.convertDpToPixel(n);
    }
}
