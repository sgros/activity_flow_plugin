// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.jobs;

import com.github.mikephil.charting.charts.BarLineChartBase;
import android.view.View;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;
import android.graphics.Matrix;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.utils.ObjectPool;

public class ZoomJob extends ViewPortJob
{
    private static ObjectPool<ZoomJob> pool;
    protected YAxis.AxisDependency axisDependency;
    protected Matrix mRunMatrixBuffer;
    protected float scaleX;
    protected float scaleY;
    
    static {
        (ZoomJob.pool = (ObjectPool<ZoomJob>)ObjectPool.create(1, (ObjectPool.Poolable)new ZoomJob(null, 0.0f, 0.0f, 0.0f, 0.0f, null, null, null))).setReplenishPercentage(0.5f);
    }
    
    public ZoomJob(final ViewPortHandler viewPortHandler, final float scaleX, final float scaleY, final float n, final float n2, final Transformer transformer, final YAxis.AxisDependency axisDependency, final View view) {
        super(viewPortHandler, n, n2, transformer, view);
        this.mRunMatrixBuffer = new Matrix();
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.axisDependency = axisDependency;
    }
    
    public static ZoomJob getInstance(final ViewPortHandler mViewPortHandler, final float scaleX, final float scaleY, final float xValue, final float yValue, final Transformer mTrans, final YAxis.AxisDependency axisDependency, final View view) {
        final ZoomJob zoomJob = ZoomJob.pool.get();
        zoomJob.xValue = xValue;
        zoomJob.yValue = yValue;
        zoomJob.scaleX = scaleX;
        zoomJob.scaleY = scaleY;
        zoomJob.mViewPortHandler = mViewPortHandler;
        zoomJob.mTrans = mTrans;
        zoomJob.axisDependency = axisDependency;
        zoomJob.view = view;
        return zoomJob;
    }
    
    public static void recycleInstance(final ZoomJob zoomJob) {
        ZoomJob.pool.recycle(zoomJob);
    }
    
    @Override
    protected Poolable instantiate() {
        return new ZoomJob(null, 0.0f, 0.0f, 0.0f, 0.0f, null, null, null);
    }
    
    @Override
    public void run() {
        final Matrix mRunMatrixBuffer = this.mRunMatrixBuffer;
        this.mViewPortHandler.zoom(this.scaleX, this.scaleY, mRunMatrixBuffer);
        this.mViewPortHandler.refresh(mRunMatrixBuffer, this.view, false);
        final float n = ((BarLineChartBase)this.view).getAxis(this.axisDependency).mAxisRange / this.mViewPortHandler.getScaleY();
        this.pts[0] = this.xValue - ((BarLineChartBase)this.view).getXAxis().mAxisRange / this.mViewPortHandler.getScaleX() / 2.0f;
        this.pts[1] = this.yValue + n / 2.0f;
        this.mTrans.pointValuesToPixel(this.pts);
        this.mViewPortHandler.translate(this.pts, mRunMatrixBuffer);
        this.mViewPortHandler.refresh(mRunMatrixBuffer, this.view, false);
        ((BarLineChartBase)this.view).calculateOffsets();
        this.view.postInvalidate();
        recycleInstance(this);
    }
}
