package com.github.mikephil.charting.jobs;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.view.View;
import com.github.mikephil.charting.utils.ObjectPool;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

@SuppressLint({"NewApi"})
public class AnimatedMoveViewJob extends AnimatedViewPortJob {
   private static ObjectPool pool = ObjectPool.create(4, new AnimatedMoveViewJob((ViewPortHandler)null, 0.0F, 0.0F, (Transformer)null, (View)null, 0.0F, 0.0F, 0L));

   static {
      pool.setReplenishPercentage(0.5F);
   }

   public AnimatedMoveViewJob(ViewPortHandler var1, float var2, float var3, Transformer var4, View var5, float var6, float var7, long var8) {
      super(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public static AnimatedMoveViewJob getInstance(ViewPortHandler var0, float var1, float var2, Transformer var3, View var4, float var5, float var6, long var7) {
      AnimatedMoveViewJob var9 = (AnimatedMoveViewJob)pool.get();
      var9.mViewPortHandler = var0;
      var9.xValue = var1;
      var9.yValue = var2;
      var9.mTrans = var3;
      var9.view = var4;
      var9.xOrigin = var5;
      var9.yOrigin = var6;
      var9.animator.setDuration(var7);
      return var9;
   }

   public static void recycleInstance(AnimatedMoveViewJob var0) {
      pool.recycle((ObjectPool.Poolable)var0);
   }

   protected ObjectPool.Poolable instantiate() {
      return new AnimatedMoveViewJob((ViewPortHandler)null, 0.0F, 0.0F, (Transformer)null, (View)null, 0.0F, 0.0F, 0L);
   }

   public void onAnimationUpdate(ValueAnimator var1) {
      this.pts[0] = this.xOrigin + (this.xValue - this.xOrigin) * this.phase;
      this.pts[1] = this.yOrigin + (this.yValue - this.yOrigin) * this.phase;
      this.mTrans.pointValuesToPixel(this.pts);
      this.mViewPortHandler.centerViewPort(this.pts, this.view);
   }

   public void recycleSelf() {
      recycleInstance(this);
   }
}
