// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.highlight;

import com.github.mikephil.charting.components.YAxis;

public class Highlight
{
    private YAxis.AxisDependency axis;
    private int mDataIndex;
    private int mDataSetIndex;
    private float mDrawX;
    private float mDrawY;
    private int mStackIndex;
    private float mX;
    private float mXPx;
    private float mY;
    private float mYPx;
    
    public Highlight(final float n, final float n2, final float n3, final float n4, final int n5, final int mStackIndex, final YAxis.AxisDependency axisDependency) {
        this(n, n2, n3, n4, n5, axisDependency);
        this.mStackIndex = mStackIndex;
    }
    
    public Highlight(final float mx, final float my, final float mxPx, final float myPx, final int mDataSetIndex, final YAxis.AxisDependency axis) {
        this.mX = Float.NaN;
        this.mY = Float.NaN;
        this.mDataIndex = -1;
        this.mStackIndex = -1;
        this.mX = mx;
        this.mY = my;
        this.mXPx = mxPx;
        this.mYPx = myPx;
        this.mDataSetIndex = mDataSetIndex;
        this.axis = axis;
    }
    
    public Highlight(final float mx, final float my, final int mDataSetIndex) {
        this.mX = Float.NaN;
        this.mY = Float.NaN;
        this.mDataIndex = -1;
        this.mStackIndex = -1;
        this.mX = mx;
        this.mY = my;
        this.mDataSetIndex = mDataSetIndex;
    }
    
    public Highlight(final float n, final int n2, final int mStackIndex) {
        this(n, Float.NaN, n2);
        this.mStackIndex = mStackIndex;
    }
    
    public boolean equalTo(final Highlight highlight) {
        return highlight != null && (this.mDataSetIndex == highlight.mDataSetIndex && this.mX == highlight.mX && this.mStackIndex == highlight.mStackIndex && this.mDataIndex == highlight.mDataIndex);
    }
    
    public YAxis.AxisDependency getAxis() {
        return this.axis;
    }
    
    public int getDataIndex() {
        return this.mDataIndex;
    }
    
    public int getDataSetIndex() {
        return this.mDataSetIndex;
    }
    
    public float getDrawX() {
        return this.mDrawX;
    }
    
    public float getDrawY() {
        return this.mDrawY;
    }
    
    public int getStackIndex() {
        return this.mStackIndex;
    }
    
    public float getX() {
        return this.mX;
    }
    
    public float getXPx() {
        return this.mXPx;
    }
    
    public float getY() {
        return this.mY;
    }
    
    public float getYPx() {
        return this.mYPx;
    }
    
    public boolean isStacked() {
        return this.mStackIndex >= 0;
    }
    
    public void setDataIndex(final int mDataIndex) {
        this.mDataIndex = mDataIndex;
    }
    
    public void setDraw(final float mDrawX, final float mDrawY) {
        this.mDrawX = mDrawX;
        this.mDrawY = mDrawY;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Highlight, x: ");
        sb.append(this.mX);
        sb.append(", y: ");
        sb.append(this.mY);
        sb.append(", dataSetIndex: ");
        sb.append(this.mDataSetIndex);
        sb.append(", stackIndex (only stacked barentry): ");
        sb.append(this.mStackIndex);
        return sb.toString();
    }
}
