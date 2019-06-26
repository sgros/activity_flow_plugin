package com.github.mikephil.charting.charts;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.highlight.BarHighlighter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.renderer.BarChartRenderer;

public class BarChart extends BarLineChartBase implements BarDataProvider {
   private boolean mDrawBarShadow = false;
   private boolean mDrawValueAboveBar = true;
   private boolean mFitBars = false;
   protected boolean mHighlightFullBarEnabled = false;

   public BarChart(Context var1) {
      super(var1);
   }

   public BarChart(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   public BarChart(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
   }

   protected void calcMinMax() {
      if (this.mFitBars) {
         this.mXAxis.calculate(((BarData)this.mData).getXMin() - ((BarData)this.mData).getBarWidth() / 2.0F, ((BarData)this.mData).getXMax() + ((BarData)this.mData).getBarWidth() / 2.0F);
      } else {
         this.mXAxis.calculate(((BarData)this.mData).getXMin(), ((BarData)this.mData).getXMax());
      }

      this.mAxisLeft.calculate(((BarData)this.mData).getYMin(YAxis.AxisDependency.LEFT), ((BarData)this.mData).getYMax(YAxis.AxisDependency.LEFT));
      this.mAxisRight.calculate(((BarData)this.mData).getYMin(YAxis.AxisDependency.RIGHT), ((BarData)this.mData).getYMax(YAxis.AxisDependency.RIGHT));
   }

   public RectF getBarBounds(BarEntry var1) {
      RectF var2 = new RectF();
      this.getBarBounds(var1, var2);
      return var2;
   }

   public void getBarBounds(BarEntry var1, RectF var2) {
      IBarDataSet var3 = (IBarDataSet)((BarData)this.mData).getDataSetForEntry(var1);
      if (var3 == null) {
         var2.set(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);
      } else {
         float var4 = var1.getY();
         float var5 = var1.getX();
         float var6 = ((BarData)this.mData).getBarWidth() / 2.0F;
         float var7;
         if (var4 >= 0.0F) {
            var7 = var4;
         } else {
            var7 = 0.0F;
         }

         if (var4 > 0.0F) {
            var4 = 0.0F;
         }

         var2.set(var5 - var6, var7, var5 + var6, var4);
         this.getTransformer(var3.getAxisDependency()).rectValueToPixel(var2);
      }
   }

   public BarData getBarData() {
      return (BarData)this.mData;
   }

   public Highlight getHighlightByTouchPoint(float var1, float var2) {
      if (this.mData == null) {
         Log.e("MPAndroidChart", "Can't select by touch. No data set.");
         return null;
      } else {
         Highlight var3 = this.getHighlighter().getHighlight(var1, var2);
         return var3 != null && this.isHighlightFullBarEnabled() ? new Highlight(var3.getX(), var3.getY(), var3.getXPx(), var3.getYPx(), var3.getDataSetIndex(), -1, var3.getAxis()) : var3;
      }
   }

   public void groupBars(float var1, float var2, float var3) {
      if (this.getBarData() == null) {
         throw new RuntimeException("You need to set data for the chart before grouping bars.");
      } else {
         this.getBarData().groupBars(var1, var2, var3);
         this.notifyDataSetChanged();
      }
   }

   public void highlightValue(float var1, int var2, int var3) {
      this.highlightValue(new Highlight(var1, var2, var3), false);
   }

   protected void init() {
      super.init();
      this.mRenderer = new BarChartRenderer(this, this.mAnimator, this.mViewPortHandler);
      this.setHighlighter(new BarHighlighter(this));
      this.getXAxis().setSpaceMin(0.5F);
      this.getXAxis().setSpaceMax(0.5F);
   }

   public boolean isDrawBarShadowEnabled() {
      return this.mDrawBarShadow;
   }

   public boolean isDrawValueAboveBarEnabled() {
      return this.mDrawValueAboveBar;
   }

   public boolean isHighlightFullBarEnabled() {
      return this.mHighlightFullBarEnabled;
   }

   public void setDrawBarShadow(boolean var1) {
      this.mDrawBarShadow = var1;
   }

   public void setDrawValueAboveBar(boolean var1) {
      this.mDrawValueAboveBar = var1;
   }

   public void setFitBars(boolean var1) {
      this.mFitBars = var1;
   }

   public void setHighlightFullBarEnabled(boolean var1) {
      this.mHighlightFullBarEnabled = var1;
   }
}
