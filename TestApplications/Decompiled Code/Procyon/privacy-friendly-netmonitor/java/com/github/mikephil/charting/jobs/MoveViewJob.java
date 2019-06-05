// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.jobs;

import android.view.View;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.utils.ObjectPool;

public class MoveViewJob extends ViewPortJob
{
    private static ObjectPool<MoveViewJob> pool;
    
    static {
        (MoveViewJob.pool = (ObjectPool<MoveViewJob>)ObjectPool.create(2, (ObjectPool.Poolable)new MoveViewJob(null, 0.0f, 0.0f, null, null))).setReplenishPercentage(0.5f);
    }
    
    public MoveViewJob(final ViewPortHandler viewPortHandler, final float n, final float n2, final Transformer transformer, final View view) {
        super(viewPortHandler, n, n2, transformer, view);
    }
    
    public static MoveViewJob getInstance(final ViewPortHandler mViewPortHandler, final float xValue, final float yValue, final Transformer mTrans, final View view) {
        final MoveViewJob moveViewJob = MoveViewJob.pool.get();
        moveViewJob.mViewPortHandler = mViewPortHandler;
        moveViewJob.xValue = xValue;
        moveViewJob.yValue = yValue;
        moveViewJob.mTrans = mTrans;
        moveViewJob.view = view;
        return moveViewJob;
    }
    
    public static void recycleInstance(final MoveViewJob moveViewJob) {
        MoveViewJob.pool.recycle(moveViewJob);
    }
    
    @Override
    protected Poolable instantiate() {
        return new MoveViewJob(this.mViewPortHandler, this.xValue, this.yValue, this.mTrans, this.view);
    }
    
    @Override
    public void run() {
        this.pts[0] = this.xValue;
        this.pts[1] = this.yValue;
        this.mTrans.pointValuesToPixel(this.pts);
        this.mViewPortHandler.centerViewPort(this.pts, this.view);
        recycleInstance(this);
    }
}
