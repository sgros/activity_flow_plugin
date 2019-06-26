// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.jobs;

import android.animation.ValueAnimator;
import com.github.mikephil.charting.charts.BarLineChartBase;
import android.animation.Animator;
import com.github.mikephil.charting.utils.Transformer;
import android.view.View;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.components.YAxis;
import android.graphics.Matrix;
import com.github.mikephil.charting.utils.ObjectPool;
import android.annotation.SuppressLint;
import android.animation.Animator$AnimatorListener;

@SuppressLint({ "NewApi" })
public class AnimatedZoomJob extends AnimatedViewPortJob implements Animator$AnimatorListener
{
    private static ObjectPool<AnimatedZoomJob> pool;
    protected Matrix mOnAnimationUpdateMatrixBuffer;
    protected float xAxisRange;
    protected YAxis yAxis;
    protected float zoomCenterX;
    protected float zoomCenterY;
    protected float zoomOriginX;
    protected float zoomOriginY;
    
    static {
        AnimatedZoomJob.pool = (ObjectPool<AnimatedZoomJob>)ObjectPool.create(8, (ObjectPool.Poolable)new AnimatedZoomJob(null, null, null, null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0L));
    }
    
    @SuppressLint({ "NewApi" })
    public AnimatedZoomJob(final ViewPortHandler viewPortHandler, final View view, final Transformer transformer, final YAxis yAxis, final float xAxisRange, final float n, final float n2, final float n3, final float n4, final float zoomCenterX, final float zoomCenterY, final float zoomOriginX, final float zoomOriginY, final long n5) {
        super(viewPortHandler, n, n2, transformer, view, n3, n4, n5);
        this.mOnAnimationUpdateMatrixBuffer = new Matrix();
        this.zoomCenterX = zoomCenterX;
        this.zoomCenterY = zoomCenterY;
        this.zoomOriginX = zoomOriginX;
        this.zoomOriginY = zoomOriginY;
        this.animator.addListener((Animator$AnimatorListener)this);
        this.yAxis = yAxis;
        this.xAxisRange = xAxisRange;
    }
    
    public static AnimatedZoomJob getInstance(final ViewPortHandler mViewPortHandler, final View view, final Transformer mTrans, final YAxis yAxis, final float n, final float xValue, final float yValue, final float xOrigin, final float yOrigin, final float n2, final float n3, final float n4, final float n5, final long duration) {
        final AnimatedZoomJob animatedZoomJob = AnimatedZoomJob.pool.get();
        animatedZoomJob.mViewPortHandler = mViewPortHandler;
        animatedZoomJob.xValue = xValue;
        animatedZoomJob.yValue = yValue;
        animatedZoomJob.mTrans = mTrans;
        animatedZoomJob.view = view;
        animatedZoomJob.xOrigin = xOrigin;
        animatedZoomJob.yOrigin = yOrigin;
        animatedZoomJob.resetAnimator();
        animatedZoomJob.animator.setDuration(duration);
        return animatedZoomJob;
    }
    
    protected Poolable instantiate() {
        return new AnimatedZoomJob(null, null, null, null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0L);
    }
    
    @Override
    public void onAnimationCancel(final Animator animator) {
    }
    
    @Override
    public void onAnimationEnd(final Animator animator) {
        ((BarLineChartBase)this.view).calculateOffsets();
        this.view.postInvalidate();
    }
    
    @Override
    public void onAnimationRepeat(final Animator animator) {
    }
    
    @Override
    public void onAnimationStart(final Animator animator) {
    }
    
    @Override
    public void onAnimationUpdate(final ValueAnimator valueAnimator) {
        final float xOrigin = this.xOrigin;
        final float xValue = this.xValue;
        final float xOrigin2 = this.xOrigin;
        final float phase = this.phase;
        final float yOrigin = this.yOrigin;
        final float yValue = this.yValue;
        final float yOrigin2 = this.yOrigin;
        final float phase2 = this.phase;
        final Matrix mOnAnimationUpdateMatrixBuffer = this.mOnAnimationUpdateMatrixBuffer;
        this.mViewPortHandler.setZoom(xOrigin + (xValue - xOrigin2) * phase, yOrigin + (yValue - yOrigin2) * phase2, mOnAnimationUpdateMatrixBuffer);
        this.mViewPortHandler.refresh(mOnAnimationUpdateMatrixBuffer, this.view, false);
        final float n = this.yAxis.mAxisRange / this.mViewPortHandler.getScaleY();
        this.pts[0] = this.zoomOriginX + (this.zoomCenterX - this.xAxisRange / this.mViewPortHandler.getScaleX() / 2.0f - this.zoomOriginX) * this.phase;
        this.pts[1] = this.zoomOriginY + (this.zoomCenterY + n / 2.0f - this.zoomOriginY) * this.phase;
        this.mTrans.pointValuesToPixel(this.pts);
        this.mViewPortHandler.translate(this.pts, mOnAnimationUpdateMatrixBuffer);
        this.mViewPortHandler.refresh(mOnAnimationUpdateMatrixBuffer, this.view, true);
    }
    
    @Override
    public void recycleSelf() {
    }
}
