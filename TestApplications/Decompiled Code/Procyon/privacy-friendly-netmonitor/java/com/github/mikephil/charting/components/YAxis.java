// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.components;

import com.github.mikephil.charting.utils.Utils;
import android.graphics.Paint;

public class YAxis extends AxisBase
{
    private AxisDependency mAxisDependency;
    private boolean mDrawBottomYLabelEntry;
    private boolean mDrawTopYLabelEntry;
    protected boolean mDrawZeroLine;
    protected boolean mInverted;
    protected float mMaxWidth;
    protected float mMinWidth;
    private YAxisLabelPosition mPosition;
    protected float mSpacePercentBottom;
    protected float mSpacePercentTop;
    protected int mZeroLineColor;
    protected float mZeroLineWidth;
    
    public YAxis() {
        this.mDrawBottomYLabelEntry = true;
        this.mDrawTopYLabelEntry = true;
        this.mInverted = false;
        this.mDrawZeroLine = false;
        this.mZeroLineColor = -7829368;
        this.mZeroLineWidth = 1.0f;
        this.mSpacePercentTop = 10.0f;
        this.mSpacePercentBottom = 10.0f;
        this.mPosition = YAxisLabelPosition.OUTSIDE_CHART;
        this.mMinWidth = 0.0f;
        this.mMaxWidth = Float.POSITIVE_INFINITY;
        this.mAxisDependency = AxisDependency.LEFT;
        this.mYOffset = 0.0f;
    }
    
    public YAxis(final AxisDependency mAxisDependency) {
        this.mDrawBottomYLabelEntry = true;
        this.mDrawTopYLabelEntry = true;
        this.mInverted = false;
        this.mDrawZeroLine = false;
        this.mZeroLineColor = -7829368;
        this.mZeroLineWidth = 1.0f;
        this.mSpacePercentTop = 10.0f;
        this.mSpacePercentBottom = 10.0f;
        this.mPosition = YAxisLabelPosition.OUTSIDE_CHART;
        this.mMinWidth = 0.0f;
        this.mMaxWidth = Float.POSITIVE_INFINITY;
        this.mAxisDependency = mAxisDependency;
        this.mYOffset = 0.0f;
    }
    
    @Override
    public void calculate(float mAxisMinimum, float mAxisMaximum) {
        if (this.mCustomAxisMin) {
            mAxisMinimum = this.mAxisMinimum;
        }
        if (this.mCustomAxisMax) {
            mAxisMaximum = this.mAxisMaximum;
        }
        final float abs = Math.abs(mAxisMaximum - mAxisMinimum);
        float n = mAxisMinimum;
        float n2 = mAxisMaximum;
        if (abs == 0.0f) {
            n2 = mAxisMaximum + 1.0f;
            n = mAxisMinimum - 1.0f;
        }
        if (!this.mCustomAxisMin) {
            this.mAxisMinimum = n - abs / 100.0f * this.getSpaceBottom();
        }
        if (!this.mCustomAxisMax) {
            this.mAxisMaximum = n2 + abs / 100.0f * this.getSpaceTop();
        }
        this.mAxisRange = Math.abs(this.mAxisMaximum - this.mAxisMinimum);
    }
    
    public AxisDependency getAxisDependency() {
        return this.mAxisDependency;
    }
    
    public YAxisLabelPosition getLabelPosition() {
        return this.mPosition;
    }
    
    public float getMaxWidth() {
        return this.mMaxWidth;
    }
    
    public float getMinWidth() {
        return this.mMinWidth;
    }
    
    public float getRequiredHeightSpace(final Paint paint) {
        paint.setTextSize(this.mTextSize);
        return Utils.calcTextHeight(paint, this.getLongestLabel()) + this.getYOffset() * 2.0f;
    }
    
    public float getRequiredWidthSpace(final Paint paint) {
        paint.setTextSize(this.mTextSize);
        final float a = Utils.calcTextWidth(paint, this.getLongestLabel()) + this.getXOffset() * 2.0f;
        final float minWidth = this.getMinWidth();
        final float maxWidth = this.getMaxWidth();
        float convertDpToPixel = minWidth;
        if (minWidth > 0.0f) {
            convertDpToPixel = Utils.convertDpToPixel(minWidth);
        }
        float convertDpToPixel2 = maxWidth;
        if (maxWidth > 0.0f) {
            convertDpToPixel2 = maxWidth;
            if (maxWidth != Float.POSITIVE_INFINITY) {
                convertDpToPixel2 = Utils.convertDpToPixel(maxWidth);
            }
        }
        if (convertDpToPixel2 <= 0.0) {
            convertDpToPixel2 = a;
        }
        return Math.max(convertDpToPixel, Math.min(a, convertDpToPixel2));
    }
    
    public float getSpaceBottom() {
        return this.mSpacePercentBottom;
    }
    
    public float getSpaceTop() {
        return this.mSpacePercentTop;
    }
    
    public int getZeroLineColor() {
        return this.mZeroLineColor;
    }
    
    public float getZeroLineWidth() {
        return this.mZeroLineWidth;
    }
    
    public boolean isDrawBottomYLabelEntryEnabled() {
        return this.mDrawBottomYLabelEntry;
    }
    
    public boolean isDrawTopYLabelEntryEnabled() {
        return this.mDrawTopYLabelEntry;
    }
    
    public boolean isDrawZeroLineEnabled() {
        return this.mDrawZeroLine;
    }
    
    public boolean isInverted() {
        return this.mInverted;
    }
    
    public boolean needsOffset() {
        return this.isEnabled() && this.isDrawLabelsEnabled() && this.getLabelPosition() == YAxisLabelPosition.OUTSIDE_CHART;
    }
    
    public void setDrawTopYLabelEntry(final boolean mDrawTopYLabelEntry) {
        this.mDrawTopYLabelEntry = mDrawTopYLabelEntry;
    }
    
    public void setDrawZeroLine(final boolean mDrawZeroLine) {
        this.mDrawZeroLine = mDrawZeroLine;
    }
    
    public void setInverted(final boolean mInverted) {
        this.mInverted = mInverted;
    }
    
    public void setMaxWidth(final float mMaxWidth) {
        this.mMaxWidth = mMaxWidth;
    }
    
    public void setMinWidth(final float mMinWidth) {
        this.mMinWidth = mMinWidth;
    }
    
    public void setPosition(final YAxisLabelPosition mPosition) {
        this.mPosition = mPosition;
    }
    
    public void setSpaceBottom(final float mSpacePercentBottom) {
        this.mSpacePercentBottom = mSpacePercentBottom;
    }
    
    public void setSpaceTop(final float mSpacePercentTop) {
        this.mSpacePercentTop = mSpacePercentTop;
    }
    
    @Deprecated
    public void setStartAtZero(final boolean b) {
        if (b) {
            this.setAxisMinimum(0.0f);
        }
        else {
            this.resetAxisMinimum();
        }
    }
    
    public void setZeroLineColor(final int mZeroLineColor) {
        this.mZeroLineColor = mZeroLineColor;
    }
    
    public void setZeroLineWidth(final float n) {
        this.mZeroLineWidth = Utils.convertDpToPixel(n);
    }
    
    public enum AxisDependency
    {
        LEFT, 
        RIGHT;
    }
    
    public enum YAxisLabelPosition
    {
        INSIDE_CHART, 
        OUTSIDE_CHART;
    }
}
