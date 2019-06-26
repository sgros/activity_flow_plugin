// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.listener;

import android.view.MotionEvent;
import android.view.GestureDetector$OnGestureListener;
import com.github.mikephil.charting.highlight.Highlight;
import android.view.GestureDetector;
import android.view.View$OnTouchListener;
import android.view.GestureDetector$SimpleOnGestureListener;
import com.github.mikephil.charting.charts.Chart;

public abstract class ChartTouchListener<T extends Chart<?>> extends GestureDetector$SimpleOnGestureListener implements View$OnTouchListener
{
    protected static final int DRAG = 1;
    protected static final int NONE = 0;
    protected static final int PINCH_ZOOM = 4;
    protected static final int POST_ZOOM = 5;
    protected static final int ROTATE = 6;
    protected static final int X_ZOOM = 2;
    protected static final int Y_ZOOM = 3;
    protected T mChart;
    protected GestureDetector mGestureDetector;
    protected ChartGesture mLastGesture;
    protected Highlight mLastHighlighted;
    protected int mTouchMode;
    
    public ChartTouchListener(final T mChart) {
        this.mLastGesture = ChartGesture.NONE;
        this.mTouchMode = 0;
        this.mChart = mChart;
        this.mGestureDetector = new GestureDetector(mChart.getContext(), (GestureDetector$OnGestureListener)this);
    }
    
    protected static float distance(float n, float n2, final float n3, final float n4) {
        n -= n2;
        n2 = n3 - n4;
        return (float)Math.sqrt(n * n + n2 * n2);
    }
    
    public void endAction(final MotionEvent motionEvent) {
        final OnChartGestureListener onChartGestureListener = this.mChart.getOnChartGestureListener();
        if (onChartGestureListener != null) {
            onChartGestureListener.onChartGestureEnd(motionEvent, this.mLastGesture);
        }
    }
    
    public ChartGesture getLastGesture() {
        return this.mLastGesture;
    }
    
    public int getTouchMode() {
        return this.mTouchMode;
    }
    
    protected void performHighlight(final Highlight mLastHighlighted, final MotionEvent motionEvent) {
        if (mLastHighlighted != null && !mLastHighlighted.equalTo(this.mLastHighlighted)) {
            this.mChart.highlightValue(mLastHighlighted, true);
            this.mLastHighlighted = mLastHighlighted;
        }
        else {
            this.mChart.highlightValue(null, true);
            this.mLastHighlighted = null;
        }
    }
    
    public void setLastHighlighted(final Highlight mLastHighlighted) {
        this.mLastHighlighted = mLastHighlighted;
    }
    
    public void startAction(final MotionEvent motionEvent) {
        final OnChartGestureListener onChartGestureListener = this.mChart.getOnChartGestureListener();
        if (onChartGestureListener != null) {
            onChartGestureListener.onChartGestureStart(motionEvent, this.mLastGesture);
        }
    }
    
    public enum ChartGesture
    {
        DOUBLE_TAP, 
        DRAG, 
        FLING, 
        LONG_PRESS, 
        NONE, 
        PINCH_ZOOM, 
        ROTATE, 
        SINGLE_TAP, 
        X_ZOOM, 
        Y_ZOOM;
    }
}
