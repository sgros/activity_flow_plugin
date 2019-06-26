package com.github.mikephil.charting.jobs;

import android.view.View;
import com.github.mikephil.charting.utils.ObjectPool;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

public abstract class ViewPortJob extends ObjectPool.Poolable implements Runnable {
   protected Transformer mTrans;
   protected ViewPortHandler mViewPortHandler;
   protected float[] pts = new float[2];
   protected View view;
   protected float xValue = 0.0F;
   protected float yValue = 0.0F;

   public ViewPortJob(ViewPortHandler var1, float var2, float var3, Transformer var4, View var5) {
      this.mViewPortHandler = var1;
      this.xValue = var2;
      this.yValue = var3;
      this.mTrans = var4;
      this.view = var5;
   }

   public float getXValue() {
      return this.xValue;
   }

   public float getYValue() {
      return this.yValue;
   }
}
