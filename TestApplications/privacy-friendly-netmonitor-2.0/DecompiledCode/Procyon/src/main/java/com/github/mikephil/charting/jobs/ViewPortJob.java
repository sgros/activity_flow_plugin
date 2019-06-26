// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.jobs;

import android.view.View;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ObjectPool;

public abstract class ViewPortJob extends Poolable implements Runnable
{
    protected Transformer mTrans;
    protected ViewPortHandler mViewPortHandler;
    protected float[] pts;
    protected View view;
    protected float xValue;
    protected float yValue;
    
    public ViewPortJob(final ViewPortHandler mViewPortHandler, final float xValue, final float yValue, final Transformer mTrans, final View view) {
        this.pts = new float[2];
        this.xValue = 0.0f;
        this.yValue = 0.0f;
        this.mViewPortHandler = mViewPortHandler;
        this.xValue = xValue;
        this.yValue = yValue;
        this.mTrans = mTrans;
        this.view = view;
    }
    
    public float getXValue() {
        return this.xValue;
    }
    
    public float getYValue() {
        return this.yValue;
    }
}
