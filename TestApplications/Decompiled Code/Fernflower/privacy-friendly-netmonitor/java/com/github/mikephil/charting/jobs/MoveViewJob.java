package com.github.mikephil.charting.jobs;

import android.view.View;
import com.github.mikephil.charting.utils.ObjectPool;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class MoveViewJob extends ViewPortJob {
   private static ObjectPool pool = ObjectPool.create(2, new MoveViewJob((ViewPortHandler)null, 0.0F, 0.0F, (Transformer)null, (View)null));

   static {
      pool.setReplenishPercentage(0.5F);
   }

   public MoveViewJob(ViewPortHandler var1, float var2, float var3, Transformer var4, View var5) {
      super(var1, var2, var3, var4, var5);
   }

   public static MoveViewJob getInstance(ViewPortHandler var0, float var1, float var2, Transformer var3, View var4) {
      MoveViewJob var5 = (MoveViewJob)pool.get();
      var5.mViewPortHandler = var0;
      var5.xValue = var1;
      var5.yValue = var2;
      var5.mTrans = var3;
      var5.view = var4;
      return var5;
   }

   public static void recycleInstance(MoveViewJob var0) {
      pool.recycle((ObjectPool.Poolable)var0);
   }

   protected ObjectPool.Poolable instantiate() {
      return new MoveViewJob(this.mViewPortHandler, this.xValue, this.yValue, this.mTrans, this.view);
   }

   public void run() {
      this.pts[0] = this.xValue;
      this.pts[1] = this.yValue;
      this.mTrans.pointValuesToPixel(this.pts);
      this.mViewPortHandler.centerViewPort(this.pts, this.view);
      recycleInstance(this);
   }
}
