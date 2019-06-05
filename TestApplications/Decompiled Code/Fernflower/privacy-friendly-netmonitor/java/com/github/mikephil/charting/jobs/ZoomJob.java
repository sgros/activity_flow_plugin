package com.github.mikephil.charting.jobs;

import android.graphics.Matrix;
import android.view.View;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.utils.ObjectPool;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class ZoomJob extends ViewPortJob {
   private static ObjectPool pool = ObjectPool.create(1, new ZoomJob((ViewPortHandler)null, 0.0F, 0.0F, 0.0F, 0.0F, (Transformer)null, (YAxis.AxisDependency)null, (View)null));
   protected YAxis.AxisDependency axisDependency;
   protected Matrix mRunMatrixBuffer = new Matrix();
   protected float scaleX;
   protected float scaleY;

   static {
      pool.setReplenishPercentage(0.5F);
   }

   public ZoomJob(ViewPortHandler var1, float var2, float var3, float var4, float var5, Transformer var6, YAxis.AxisDependency var7, View var8) {
      super(var1, var4, var5, var6, var8);
      this.scaleX = var2;
      this.scaleY = var3;
      this.axisDependency = var7;
   }

   public static ZoomJob getInstance(ViewPortHandler var0, float var1, float var2, float var3, float var4, Transformer var5, YAxis.AxisDependency var6, View var7) {
      ZoomJob var8 = (ZoomJob)pool.get();
      var8.xValue = var3;
      var8.yValue = var4;
      var8.scaleX = var1;
      var8.scaleY = var2;
      var8.mViewPortHandler = var0;
      var8.mTrans = var5;
      var8.axisDependency = var6;
      var8.view = var7;
      return var8;
   }

   public static void recycleInstance(ZoomJob var0) {
      pool.recycle((ObjectPool.Poolable)var0);
   }

   protected ObjectPool.Poolable instantiate() {
      return new ZoomJob((ViewPortHandler)null, 0.0F, 0.0F, 0.0F, 0.0F, (Transformer)null, (YAxis.AxisDependency)null, (View)null);
   }

   public void run() {
      Matrix var1 = this.mRunMatrixBuffer;
      this.mViewPortHandler.zoom(this.scaleX, this.scaleY, var1);
      this.mViewPortHandler.refresh(var1, this.view, false);
      float var2 = ((BarLineChartBase)this.view).getAxis(this.axisDependency).mAxisRange / this.mViewPortHandler.getScaleY();
      float var3 = ((BarLineChartBase)this.view).getXAxis().mAxisRange / this.mViewPortHandler.getScaleX();
      this.pts[0] = this.xValue - var3 / 2.0F;
      this.pts[1] = this.yValue + var2 / 2.0F;
      this.mTrans.pointValuesToPixel(this.pts);
      this.mViewPortHandler.translate(this.pts, var1);
      this.mViewPortHandler.refresh(var1, this.view, false);
      ((BarLineChartBase)this.view).calculateOffsets();
      this.view.postInvalidate();
      recycleInstance(this);
   }
}
