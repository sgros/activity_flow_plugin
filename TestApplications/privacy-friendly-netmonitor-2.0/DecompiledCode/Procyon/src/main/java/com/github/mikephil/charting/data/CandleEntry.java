// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.data;

import android.graphics.drawable.Drawable;
import android.annotation.SuppressLint;

@SuppressLint({ "ParcelCreator" })
public class CandleEntry extends Entry
{
    private float mClose;
    private float mOpen;
    private float mShadowHigh;
    private float mShadowLow;
    
    public CandleEntry(final float n, final float mShadowHigh, final float mShadowLow, final float mOpen, final float mClose) {
        super(n, (mShadowHigh + mShadowLow) / 2.0f);
        this.mShadowHigh = 0.0f;
        this.mShadowLow = 0.0f;
        this.mClose = 0.0f;
        this.mOpen = 0.0f;
        this.mShadowHigh = mShadowHigh;
        this.mShadowLow = mShadowLow;
        this.mOpen = mOpen;
        this.mClose = mClose;
    }
    
    public CandleEntry(final float n, final float mShadowHigh, final float mShadowLow, final float mOpen, final float mClose, final Drawable drawable) {
        super(n, (mShadowHigh + mShadowLow) / 2.0f, drawable);
        this.mShadowHigh = 0.0f;
        this.mShadowLow = 0.0f;
        this.mClose = 0.0f;
        this.mOpen = 0.0f;
        this.mShadowHigh = mShadowHigh;
        this.mShadowLow = mShadowLow;
        this.mOpen = mOpen;
        this.mClose = mClose;
    }
    
    public CandleEntry(final float n, final float mShadowHigh, final float mShadowLow, final float mOpen, final float mClose, final Drawable drawable, final Object o) {
        super(n, (mShadowHigh + mShadowLow) / 2.0f, drawable, o);
        this.mShadowHigh = 0.0f;
        this.mShadowLow = 0.0f;
        this.mClose = 0.0f;
        this.mOpen = 0.0f;
        this.mShadowHigh = mShadowHigh;
        this.mShadowLow = mShadowLow;
        this.mOpen = mOpen;
        this.mClose = mClose;
    }
    
    public CandleEntry(final float n, final float mShadowHigh, final float mShadowLow, final float mOpen, final float mClose, final Object o) {
        super(n, (mShadowHigh + mShadowLow) / 2.0f, o);
        this.mShadowHigh = 0.0f;
        this.mShadowLow = 0.0f;
        this.mClose = 0.0f;
        this.mOpen = 0.0f;
        this.mShadowHigh = mShadowHigh;
        this.mShadowLow = mShadowLow;
        this.mOpen = mOpen;
        this.mClose = mClose;
    }
    
    @Override
    public CandleEntry copy() {
        return new CandleEntry(this.getX(), this.mShadowHigh, this.mShadowLow, this.mOpen, this.mClose, this.getData());
    }
    
    public float getBodyRange() {
        return Math.abs(this.mOpen - this.mClose);
    }
    
    public float getClose() {
        return this.mClose;
    }
    
    public float getHigh() {
        return this.mShadowHigh;
    }
    
    public float getLow() {
        return this.mShadowLow;
    }
    
    public float getOpen() {
        return this.mOpen;
    }
    
    public float getShadowRange() {
        return Math.abs(this.mShadowHigh - this.mShadowLow);
    }
    
    @Override
    public float getY() {
        return super.getY();
    }
    
    public void setClose(final float mClose) {
        this.mClose = mClose;
    }
    
    public void setHigh(final float mShadowHigh) {
        this.mShadowHigh = mShadowHigh;
    }
    
    public void setLow(final float mShadowLow) {
        this.mShadowLow = mShadowLow;
    }
    
    public void setOpen(final float mOpen) {
        this.mOpen = mOpen;
    }
}
