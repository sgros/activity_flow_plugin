package com.github.mikephil.charting.charts;

import android.content.Context;
import android.util.AttributeSet;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.renderer.LineChartRenderer;

public class LineChart extends BarLineChartBase implements LineDataProvider {
   public LineChart(Context var1) {
      super(var1);
   }

   public LineChart(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   public LineChart(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
   }

   public LineData getLineData() {
      return (LineData)this.mData;
   }

   protected void init() {
      super.init();
      this.mRenderer = new LineChartRenderer(this, this.mAnimator, this.mViewPortHandler);
   }

   protected void onDetachedFromWindow() {
      if (this.mRenderer != null && this.mRenderer instanceof LineChartRenderer) {
         ((LineChartRenderer)this.mRenderer).releaseBitmap();
      }

      super.onDetachedFromWindow();
   }
}
