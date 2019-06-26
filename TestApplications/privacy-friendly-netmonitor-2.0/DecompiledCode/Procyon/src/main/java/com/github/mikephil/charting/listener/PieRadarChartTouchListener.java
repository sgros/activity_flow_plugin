// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.listener;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import com.github.mikephil.charting.utils.Utils;
import android.view.animation.AnimationUtils;
import com.github.mikephil.charting.utils.MPPointF;
import java.util.ArrayList;
import com.github.mikephil.charting.charts.PieRadarChartBase;

public class PieRadarChartTouchListener extends ChartTouchListener<PieRadarChartBase<?>>
{
    private ArrayList<AngularVelocitySample> _velocitySamples;
    private float mDecelerationAngularVelocity;
    private long mDecelerationLastTime;
    private float mStartAngle;
    private MPPointF mTouchStartPoint;
    
    public PieRadarChartTouchListener(final PieRadarChartBase<?> pieRadarChartBase) {
        super(pieRadarChartBase);
        this.mTouchStartPoint = MPPointF.getInstance(0.0f, 0.0f);
        this.mStartAngle = 0.0f;
        this._velocitySamples = new ArrayList<AngularVelocitySample>();
        this.mDecelerationLastTime = 0L;
        this.mDecelerationAngularVelocity = 0.0f;
    }
    
    private float calculateVelocity() {
        if (this._velocitySamples.isEmpty()) {
            return 0.0f;
        }
        final ArrayList<AngularVelocitySample> velocitySamples = this._velocitySamples;
        final int n = 0;
        final AngularVelocitySample angularVelocitySample = velocitySamples.get(0);
        final AngularVelocitySample angularVelocitySample2 = this._velocitySamples.get(this._velocitySamples.size() - 1);
        int i = this._velocitySamples.size() - 1;
        AngularVelocitySample angularVelocitySample3 = angularVelocitySample;
        while (i >= 0) {
            angularVelocitySample3 = this._velocitySamples.get(i);
            if (angularVelocitySample3.angle != angularVelocitySample2.angle) {
                break;
            }
            --i;
        }
        float n2;
        if ((n2 = (angularVelocitySample2.time - angularVelocitySample.time) / 1000.0f) == 0.0f) {
            n2 = 0.1f;
        }
        int n3 = n;
        if (angularVelocitySample2.angle >= angularVelocitySample3.angle) {
            n3 = 1;
        }
        int n4 = n3;
        if (Math.abs(angularVelocitySample2.angle - angularVelocitySample3.angle) > 270.0) {
            n4 = (n3 ^ 0x1);
        }
        if (angularVelocitySample2.angle - angularVelocitySample.angle > 180.0) {
            angularVelocitySample.angle += 360.0;
        }
        else if (angularVelocitySample.angle - angularVelocitySample2.angle > 180.0) {
            angularVelocitySample2.angle += 360.0;
        }
        float abs = Math.abs((angularVelocitySample2.angle - angularVelocitySample.angle) / n2);
        if (n4 == 0) {
            abs = -abs;
        }
        return abs;
    }
    
    private void resetVelocity() {
        this._velocitySamples.clear();
    }
    
    private void sampleVelocity(final float n, final float n2) {
        final long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
        this._velocitySamples.add(new AngularVelocitySample(currentAnimationTimeMillis, ((PieRadarChartBase)this.mChart).getAngleForPoint(n, n2)));
        for (int size = this._velocitySamples.size(); size - 2 > 0 && currentAnimationTimeMillis - this._velocitySamples.get(0).time > 1000L; --size) {
            this._velocitySamples.remove(0);
        }
    }
    
    public void computeScroll() {
        if (this.mDecelerationAngularVelocity == 0.0f) {
            return;
        }
        final long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
        this.mDecelerationAngularVelocity *= ((PieRadarChartBase)this.mChart).getDragDecelerationFrictionCoef();
        ((PieRadarChartBase)this.mChart).setRotationAngle(((PieRadarChartBase)this.mChart).getRotationAngle() + this.mDecelerationAngularVelocity * ((currentAnimationTimeMillis - this.mDecelerationLastTime) / 1000.0f));
        this.mDecelerationLastTime = currentAnimationTimeMillis;
        if (Math.abs(this.mDecelerationAngularVelocity) >= 0.001) {
            Utils.postInvalidateOnAnimation((View)this.mChart);
        }
        else {
            this.stopDeceleration();
        }
    }
    
    public void onLongPress(final MotionEvent motionEvent) {
        this.mLastGesture = ChartGesture.LONG_PRESS;
        final OnChartGestureListener onChartGestureListener = ((PieRadarChartBase)this.mChart).getOnChartGestureListener();
        if (onChartGestureListener != null) {
            onChartGestureListener.onChartLongPressed(motionEvent);
        }
    }
    
    public boolean onSingleTapConfirmed(final MotionEvent motionEvent) {
        return true;
    }
    
    public boolean onSingleTapUp(final MotionEvent motionEvent) {
        this.mLastGesture = ChartGesture.SINGLE_TAP;
        final OnChartGestureListener onChartGestureListener = ((PieRadarChartBase)this.mChart).getOnChartGestureListener();
        if (onChartGestureListener != null) {
            onChartGestureListener.onChartSingleTapped(motionEvent);
        }
        if (!((PieRadarChartBase)this.mChart).isHighlightPerTapEnabled()) {
            return false;
        }
        this.performHighlight(((PieRadarChartBase)this.mChart).getHighlightByTouchPoint(motionEvent.getX(), motionEvent.getY()), motionEvent);
        return true;
    }
    
    @SuppressLint({ "ClickableViewAccessibility" })
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        if (this.mGestureDetector.onTouchEvent(motionEvent)) {
            return true;
        }
        if (((PieRadarChartBase)this.mChart).isRotationEnabled()) {
            final float x = motionEvent.getX();
            final float y = motionEvent.getY();
            switch (motionEvent.getAction()) {
                case 2: {
                    if (((PieRadarChartBase)this.mChart).isDragDecelerationEnabled()) {
                        this.sampleVelocity(x, y);
                    }
                    if (this.mTouchMode == 0 && ChartTouchListener.distance(x, this.mTouchStartPoint.x, y, this.mTouchStartPoint.y) > Utils.convertDpToPixel(8.0f)) {
                        this.mLastGesture = ChartGesture.ROTATE;
                        this.mTouchMode = 6;
                        ((PieRadarChartBase)this.mChart).disableScroll();
                    }
                    else if (this.mTouchMode == 6) {
                        this.updateGestureRotation(x, y);
                        ((PieRadarChartBase)this.mChart).invalidate();
                    }
                    this.endAction(motionEvent);
                    break;
                }
                case 1: {
                    if (((PieRadarChartBase)this.mChart).isDragDecelerationEnabled()) {
                        this.stopDeceleration();
                        this.sampleVelocity(x, y);
                        this.mDecelerationAngularVelocity = this.calculateVelocity();
                        if (this.mDecelerationAngularVelocity != 0.0f) {
                            this.mDecelerationLastTime = AnimationUtils.currentAnimationTimeMillis();
                            Utils.postInvalidateOnAnimation((View)this.mChart);
                        }
                    }
                    ((PieRadarChartBase)this.mChart).enableScroll();
                    this.mTouchMode = 0;
                    this.endAction(motionEvent);
                    break;
                }
                case 0: {
                    this.startAction(motionEvent);
                    this.stopDeceleration();
                    this.resetVelocity();
                    if (((PieRadarChartBase)this.mChart).isDragDecelerationEnabled()) {
                        this.sampleVelocity(x, y);
                    }
                    this.setGestureStartAngle(x, y);
                    this.mTouchStartPoint.x = x;
                    this.mTouchStartPoint.y = y;
                    break;
                }
            }
        }
        return true;
    }
    
    public void setGestureStartAngle(final float n, final float n2) {
        this.mStartAngle = ((PieRadarChartBase)this.mChart).getAngleForPoint(n, n2) - ((PieRadarChartBase)this.mChart).getRawRotationAngle();
    }
    
    public void stopDeceleration() {
        this.mDecelerationAngularVelocity = 0.0f;
    }
    
    public void updateGestureRotation(final float n, final float n2) {
        ((PieRadarChartBase)this.mChart).setRotationAngle(((PieRadarChartBase)this.mChart).getAngleForPoint(n, n2) - this.mStartAngle);
    }
    
    private class AngularVelocitySample
    {
        public float angle;
        public long time;
        
        public AngularVelocitySample(final long time, final float angle) {
            this.time = time;
            this.angle = angle;
        }
    }
}
