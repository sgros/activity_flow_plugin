// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.data;

import java.util.ArrayList;
import java.util.List;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;

public class RadarDataSet extends LineRadarDataSet<RadarEntry> implements IRadarDataSet
{
    protected boolean mDrawHighlightCircleEnabled;
    protected int mHighlightCircleFillColor;
    protected float mHighlightCircleInnerRadius;
    protected float mHighlightCircleOuterRadius;
    protected int mHighlightCircleStrokeAlpha;
    protected int mHighlightCircleStrokeColor;
    protected float mHighlightCircleStrokeWidth;
    
    public RadarDataSet(final List<RadarEntry> list, final String s) {
        super(list, s);
        this.mDrawHighlightCircleEnabled = false;
        this.mHighlightCircleFillColor = -1;
        this.mHighlightCircleStrokeColor = 1122867;
        this.mHighlightCircleStrokeAlpha = 76;
        this.mHighlightCircleInnerRadius = 3.0f;
        this.mHighlightCircleOuterRadius = 4.0f;
        this.mHighlightCircleStrokeWidth = 2.0f;
    }
    
    @Override
    public DataSet<RadarEntry> copy() {
        final ArrayList<RadarEntry> list = new ArrayList<RadarEntry>();
        for (int i = 0; i < this.mValues.size(); ++i) {
            list.add(((RadarEntry)this.mValues.get(i)).copy());
        }
        final RadarDataSet set = new RadarDataSet(list, this.getLabel());
        set.mColors = this.mColors;
        set.mHighLightColor = this.mHighLightColor;
        return set;
    }
    
    @Override
    public int getHighlightCircleFillColor() {
        return this.mHighlightCircleFillColor;
    }
    
    @Override
    public float getHighlightCircleInnerRadius() {
        return this.mHighlightCircleInnerRadius;
    }
    
    @Override
    public float getHighlightCircleOuterRadius() {
        return this.mHighlightCircleOuterRadius;
    }
    
    @Override
    public int getHighlightCircleStrokeAlpha() {
        return this.mHighlightCircleStrokeAlpha;
    }
    
    @Override
    public int getHighlightCircleStrokeColor() {
        return this.mHighlightCircleStrokeColor;
    }
    
    @Override
    public float getHighlightCircleStrokeWidth() {
        return this.mHighlightCircleStrokeWidth;
    }
    
    @Override
    public boolean isDrawHighlightCircleEnabled() {
        return this.mDrawHighlightCircleEnabled;
    }
    
    @Override
    public void setDrawHighlightCircleEnabled(final boolean mDrawHighlightCircleEnabled) {
        this.mDrawHighlightCircleEnabled = mDrawHighlightCircleEnabled;
    }
    
    public void setHighlightCircleFillColor(final int mHighlightCircleFillColor) {
        this.mHighlightCircleFillColor = mHighlightCircleFillColor;
    }
    
    public void setHighlightCircleInnerRadius(final float mHighlightCircleInnerRadius) {
        this.mHighlightCircleInnerRadius = mHighlightCircleInnerRadius;
    }
    
    public void setHighlightCircleOuterRadius(final float mHighlightCircleOuterRadius) {
        this.mHighlightCircleOuterRadius = mHighlightCircleOuterRadius;
    }
    
    public void setHighlightCircleStrokeAlpha(final int mHighlightCircleStrokeAlpha) {
        this.mHighlightCircleStrokeAlpha = mHighlightCircleStrokeAlpha;
    }
    
    public void setHighlightCircleStrokeColor(final int mHighlightCircleStrokeColor) {
        this.mHighlightCircleStrokeColor = mHighlightCircleStrokeColor;
    }
    
    public void setHighlightCircleStrokeWidth(final float mHighlightCircleStrokeWidth) {
        this.mHighlightCircleStrokeWidth = mHighlightCircleStrokeWidth;
    }
}
