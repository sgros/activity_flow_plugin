// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.data;

import com.github.mikephil.charting.utils.Utils;
import android.annotation.TargetApi;
import android.graphics.Color;
import java.util.List;
import android.graphics.drawable.Drawable;
import com.github.mikephil.charting.interfaces.datasets.ILineRadarDataSet;

public abstract class LineRadarDataSet<T extends Entry> extends LineScatterCandleRadarDataSet<T> implements ILineRadarDataSet<T>
{
    private boolean mDrawFilled;
    private int mFillAlpha;
    private int mFillColor;
    protected Drawable mFillDrawable;
    private float mLineWidth;
    
    public LineRadarDataSet(final List<T> list, final String s) {
        super(list, s);
        this.mFillColor = Color.rgb(140, 234, 255);
        this.mFillAlpha = 85;
        this.mLineWidth = 2.5f;
        this.mDrawFilled = false;
    }
    
    @Override
    public int getFillAlpha() {
        return this.mFillAlpha;
    }
    
    @Override
    public int getFillColor() {
        return this.mFillColor;
    }
    
    @Override
    public Drawable getFillDrawable() {
        return this.mFillDrawable;
    }
    
    @Override
    public float getLineWidth() {
        return this.mLineWidth;
    }
    
    @Override
    public boolean isDrawFilledEnabled() {
        return this.mDrawFilled;
    }
    
    @Override
    public void setDrawFilled(final boolean mDrawFilled) {
        this.mDrawFilled = mDrawFilled;
    }
    
    public void setFillAlpha(final int mFillAlpha) {
        this.mFillAlpha = mFillAlpha;
    }
    
    public void setFillColor(final int mFillColor) {
        this.mFillColor = mFillColor;
        this.mFillDrawable = null;
    }
    
    @TargetApi(18)
    public void setFillDrawable(final Drawable mFillDrawable) {
        this.mFillDrawable = mFillDrawable;
    }
    
    public void setLineWidth(float n) {
        float n2 = n;
        if (n < 0.0f) {
            n2 = 0.0f;
        }
        n = n2;
        if (n2 > 10.0f) {
            n = 10.0f;
        }
        this.mLineWidth = Utils.convertDpToPixel(n);
    }
}
