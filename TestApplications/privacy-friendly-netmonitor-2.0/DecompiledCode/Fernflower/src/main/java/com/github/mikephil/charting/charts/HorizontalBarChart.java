package com.github.mikephil.charting.charts;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.highlight.HorizontalBarHighlighter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.renderer.HorizontalBarChartRenderer;
import com.github.mikephil.charting.renderer.XAxisRendererHorizontalBarChart;
import com.github.mikephil.charting.renderer.YAxisRendererHorizontalBarChart;
import com.github.mikephil.charting.utils.HorizontalViewPortHandler;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.TransformerHorizontalBarChart;
import com.github.mikephil.charting.utils.Utils;

public class HorizontalBarChart extends BarChart {
   protected float[] mGetPositionBuffer = new float[2];
   private RectF mOffsetsBuffer = new RectF();

   public HorizontalBarChart(Context var1) {
      super(var1);
   }

   public HorizontalBarChart(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   public HorizontalBarChart(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
   }

   public void calculateOffsets() {
      this.calculateLegendOffsets(this.mOffsetsBuffer);
      float var1 = this.mOffsetsBuffer.left + 0.0F;
      float var2 = this.mOffsetsBuffer.top + 0.0F;
      float var3 = this.mOffsetsBuffer.right + 0.0F;
      float var4 = 0.0F + this.mOffsetsBuffer.bottom;
      float var5 = var2;
      if (this.mAxisLeft.needsOffset()) {
         var5 = var2 + this.mAxisLeft.getRequiredHeightSpace(this.mAxisRendererLeft.getPaintAxisLabels());
      }

      float var6 = var4;
      if (this.mAxisRight.needsOffset()) {
         var6 = var4 + this.mAxisRight.getRequiredHeightSpace(this.mAxisRendererRight.getPaintAxisLabels());
      }

      float var7 = (float)this.mXAxis.mLabelRotatedWidth;
      var4 = var1;
      var2 = var3;
      if (this.mXAxis.isEnabled()) {
         if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM) {
            var4 = var1 + var7;
            var2 = var3;
         } else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP) {
            var2 = var3 + var7;
            var4 = var1;
         } else {
            var4 = var1;
            var2 = var3;
            if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTH_SIDED) {
               var4 = var1 + var7;
               var2 = var3 + var7;
            }
         }
      }

      var5 += this.getExtraTopOffset();
      var2 += this.getExtraRightOffset();
      var6 += this.getExtraBottomOffset();
      var4 += this.getExtraLeftOffset();
      var3 = Utils.convertDpToPixel(this.mMinOffset);
      this.mViewPortHandler.restrainViewPort(Math.max(var3, var4), Math.max(var3, var5), Math.max(var3, var2), Math.max(var3, var6));
      if (this.mLogEnabled) {
         StringBuilder var8 = new StringBuilder();
         var8.append("offsetLeft: ");
         var8.append(var4);
         var8.append(", offsetTop: ");
         var8.append(var5);
         var8.append(", offsetRight: ");
         var8.append(var2);
         var8.append(", offsetBottom: ");
         var8.append(var6);
         Log.i("MPAndroidChart", var8.toString());
         var8 = new StringBuilder();
         var8.append("Content: ");
         var8.append(this.mViewPortHandler.getContentRect().toString());
         Log.i("MPAndroidChart", var8.toString());
      }

      this.prepareOffsetMatrix();
      this.prepareValuePxMatrix();
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

         var2.set(var7, var5 - var6, var4, var5 + var6);
         this.getTransformer(var3.getAxisDependency()).rectValueToPixel(var2);
      }
   }

   public float getHighestVisibleX() {
      this.getTransformer(YAxis.AxisDependency.LEFT).getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop(), this.posForGetHighestVisibleX);
      return (float)Math.min((double)this.mXAxis.mAxisMaximum, this.posForGetHighestVisibleX.y);
   }

   public Highlight getHighlightByTouchPoint(float var1, float var2) {
      if (this.mData == null) {
         if (this.mLogEnabled) {
            Log.e("MPAndroidChart", "Can't select by touch. No data set.");
         }

         return null;
      } else {
         return this.getHighlighter().getHighlight(var2, var1);
      }
   }

   public float getLowestVisibleX() {
      this.getTransformer(YAxis.AxisDependency.LEFT).getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentBottom(), this.posForGetLowestVisibleX);
      return (float)Math.max((double)this.mXAxis.mAxisMinimum, this.posForGetLowestVisibleX.y);
   }

   protected float[] getMarkerPosition(Highlight var1) {
      return new float[]{var1.getDrawY(), var1.getDrawX()};
   }

   public MPPointF getPosition(Entry var1, YAxis.AxisDependency var2) {
      if (var1 == null) {
         return null;
      } else {
         float[] var3 = this.mGetPositionBuffer;
         var3[0] = var1.getY();
         var3[1] = var1.getX();
         this.getTransformer(var2).pointValuesToPixel(var3);
         return MPPointF.getInstance(var3[0], var3[1]);
      }
   }

   protected void init() {
      this.mViewPortHandler = new HorizontalViewPortHandler();
      super.init();
      this.mLeftAxisTransformer = new TransformerHorizontalBarChart(this.mViewPortHandler);
      this.mRightAxisTransformer = new TransformerHorizontalBarChart(this.mViewPortHandler);
      this.mRenderer = new HorizontalBarChartRenderer(this, this.mAnimator, this.mViewPortHandler);
      this.setHighlighter(new HorizontalBarHighlighter(this));
      this.mAxisRendererLeft = new YAxisRendererHorizontalBarChart(this.mViewPortHandler, this.mAxisLeft, this.mLeftAxisTransformer);
      this.mAxisRendererRight = new YAxisRendererHorizontalBarChart(this.mViewPortHandler, this.mAxisRight, this.mRightAxisTransformer);
      this.mXAxisRenderer = new XAxisRendererHorizontalBarChart(this.mViewPortHandler, this.mXAxis, this.mLeftAxisTransformer, this);
   }

   protected void prepareValuePxMatrix() {
      this.mRightAxisTransformer.prepareMatrixValuePx(this.mAxisRight.mAxisMinimum, this.mAxisRight.mAxisRange, this.mXAxis.mAxisRange, this.mXAxis.mAxisMinimum);
      this.mLeftAxisTransformer.prepareMatrixValuePx(this.mAxisLeft.mAxisMinimum, this.mAxisLeft.mAxisRange, this.mXAxis.mAxisRange, this.mXAxis.mAxisMinimum);
   }

   public void setVisibleXRange(float var1, float var2) {
      var1 = this.mXAxis.mAxisRange / var1;
      var2 = this.mXAxis.mAxisRange / var2;
      this.mViewPortHandler.setMinMaxScaleY(var1, var2);
   }

   public void setVisibleXRangeMaximum(float var1) {
      var1 = this.mXAxis.mAxisRange / var1;
      this.mViewPortHandler.setMinimumScaleY(var1);
   }

   public void setVisibleXRangeMinimum(float var1) {
      var1 = this.mXAxis.mAxisRange / var1;
      this.mViewPortHandler.setMaximumScaleY(var1);
   }

   public void setVisibleYRange(float var1, float var2, YAxis.AxisDependency var3) {
      var1 = this.getAxisRange(var3) / var1;
      var2 = this.getAxisRange(var3) / var2;
      this.mViewPortHandler.setMinMaxScaleX(var1, var2);
   }

   public void setVisibleYRangeMaximum(float var1, YAxis.AxisDependency var2) {
      var1 = this.getAxisRange(var2) / var1;
      this.mViewPortHandler.setMinimumScaleX(var1);
   }

   public void setVisibleYRangeMinimum(float var1, YAxis.AxisDependency var2) {
      var1 = this.getAxisRange(var2) / var1;
      this.mViewPortHandler.setMaximumScaleX(var1);
   }
}
