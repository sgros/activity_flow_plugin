// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.jobs;

import android.animation.ValueAnimator;
import android.view.View;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.utils.ObjectPool;
import android.annotation.SuppressLint;

@SuppressLint({ "NewApi" })
public class AnimatedMoveViewJob extends AnimatedViewPortJob
{
    private static ObjectPool<AnimatedMoveViewJob> pool;
    
    static {
        (AnimatedMoveViewJob.pool = (ObjectPool<AnimatedMoveViewJob>)ObjectPool.create(4, (ObjectPool.Poolable)new AnimatedMoveViewJob(null, 0.0f, 0.0f, null, null, 0.0f, 0.0f, 0L))).setReplenishPercentage(0.5f);
    }
    
    public AnimatedMoveViewJob(final ViewPortHandler viewPortHandler, final float n, final float n2, final Transformer transformer, final View view, final float n3, final float n4, final long n5) {
        super(viewPortHandler, n, n2, transformer, view, n3, n4, n5);
    }
    
    public static AnimatedMoveViewJob getInstance(final ViewPortHandler mViewPortHandler, final float xValue, final float yValue, final Transformer mTrans, final View view, final float xOrigin, final float yOrigin, final long duration) {
        final AnimatedMoveViewJob animatedMoveViewJob = AnimatedMoveViewJob.pool.get();
        animatedMoveViewJob.mViewPortHandler = mViewPortHandler;
        animatedMoveViewJob.xValue = xValue;
        animatedMoveViewJob.yValue = yValue;
        animatedMoveViewJob.mTrans = mTrans;
        animatedMoveViewJob.view = view;
        animatedMoveViewJob.xOrigin = xOrigin;
        animatedMoveViewJob.yOrigin = yOrigin;
        animatedMoveViewJob.animator.setDuration(duration);
        return animatedMoveViewJob;
    }
    
    public static void recycleInstance(final AnimatedMoveViewJob animatedMoveViewJob) {
        AnimatedMoveViewJob.pool.recycle(animatedMoveViewJob);
    }
    
    protected Poolable instantiate() {
        return new AnimatedMoveViewJob(null, 0.0f, 0.0f, null, null, 0.0f, 0.0f, 0L);
    }
    
    @Override
    public void onAnimationUpdate(final ValueAnimator valueAnimator) {
        this.pts[0] = this.xOrigin + (this.xValue - this.xOrigin) * this.phase;
        this.pts[1] = this.yOrigin + (this.yValue - this.yOrigin) * this.phase;
        this.mTrans.pointValuesToPixel(this.pts);
        this.mViewPortHandler.centerViewPort(this.pts, this.view);
    }
    
    @Override
    public void recycleSelf() {
        recycleInstance(this);
    }
}
