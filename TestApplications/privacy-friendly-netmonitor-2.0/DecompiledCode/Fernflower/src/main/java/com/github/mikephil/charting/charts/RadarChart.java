package com.github.mikephil.charting.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.AttributeSet;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.highlight.RadarHighlighter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.renderer.RadarChartRenderer;
import com.github.mikephil.charting.renderer.XAxisRendererRadarChart;
import com.github.mikephil.charting.renderer.YAxisRendererRadarChart;
import com.github.mikephil.charting.utils.Utils;

public class RadarChart extends PieRadarChartBase {
   private boolean mDrawWeb = true;
   private float mInnerWebLineWidth = 1.5F;
   private int mSkipWebLineCount = 0;
   private int mWebAlpha = 150;
   private int mWebColor = Color.rgb(122, 122, 122);
   private int mWebColorInner = Color.rgb(122, 122, 122);
   private float mWebLineWidth = 2.5F;
   protected XAxisRendererRadarChart mXAxisRenderer;
   private YAxis mYAxis;
   protected YAxisRendererRadarChart mYAxisRenderer;

   public RadarChart(Context var1) {
      super(var1);
   }

   public RadarChart(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   public RadarChart(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
   }

   protected void calcMinMax() {
      super.calcMinMax();
      this.mYAxis.calculate(((RadarData)this.mData).getYMin(YAxis.AxisDependency.LEFT), ((RadarData)this.mData).getYMax(YAxis.AxisDependency.LEFT));
      this.mXAxis.calculate(0.0F, (float)((IRadarDataSet)((RadarData)this.mData).getMaxEntryCountSet()).getEntryCount());
   }

   public float getFactor() {
      RectF var1 = this.mViewPortHandler.getContentRect();
      return Math.min(var1.width() / 2.0F, var1.height() / 2.0F) / this.mYAxis.mAxisRange;
   }

   public int getIndexForAngle(float var1) {
      float var2 = Utils.getNormalizedAngle(var1 - this.getRotationAngle());
      var1 = this.getSliceAngle();
      int var3 = ((IRadarDataSet)((RadarData)this.mData).getMaxEntryCountSet()).getEntryCount();
      byte var4 = 0;
      int var5 = 0;

      int var6;
      while(true) {
         var6 = var4;
         if (var5 >= var3) {
            break;
         }

         var6 = var5 + 1;
         if ((float)var6 * var1 - var1 / 2.0F > var2) {
            var6 = var5;
            break;
         }

         var5 = var6;
      }

      return var6;
   }

   public float getRadius() {
      RectF var1 = this.mViewPortHandler.getContentRect();
      return Math.min(var1.width() / 2.0F, var1.height() / 2.0F);
   }

   protected float getRequiredBaseOffset() {
      float var1;
      if (this.mXAxis.isEnabled() && this.mXAxis.isDrawLabelsEnabled()) {
         var1 = (float)this.mXAxis.mLabelRotatedWidth;
      } else {
         var1 = Utils.convertDpToPixel(10.0F);
      }

      return var1;
   }

   protected float getRequiredLegendOffset() {
      return this.mLegendRenderer.getLabelPaint().getTextSize() * 4.0F;
   }

   public int getSkipWebLineCount() {
      return this.mSkipWebLineCount;
   }

   public float getSliceAngle() {
      return 360.0F / (float)((IRadarDataSet)((RadarData)this.mData).getMaxEntryCountSet()).getEntryCount();
   }

   public int getWebAlpha() {
      return this.mWebAlpha;
   }

   public int getWebColor() {
      return this.mWebColor;
   }

   public int getWebColorInner() {
      return this.mWebColorInner;
   }

   public float getWebLineWidth() {
      return this.mWebLineWidth;
   }

   public float getWebLineWidthInner() {
      return this.mInnerWebLineWidth;
   }

   public YAxis getYAxis() {
      return this.mYAxis;
   }

   public float getYChartMax() {
      return this.mYAxis.mAxisMaximum;
   }

   public float getYChartMin() {
      return this.mYAxis.mAxisMinimum;
   }

   public float getYRange() {
      return this.mYAxis.mAxisRange;
   }

   protected void init() {
      super.init();
      this.mYAxis = new YAxis(YAxis.AxisDependency.LEFT);
      this.mWebLineWidth = Utils.convertDpToPixel(1.5F);
      this.mInnerWebLineWidth = Utils.convertDpToPixel(0.75F);
      this.mRenderer = new RadarChartRenderer(this, this.mAnimator, this.mViewPortHandler);
      this.mYAxisRenderer = new YAxisRendererRadarChart(this.mViewPortHandler, this.mYAxis, this);
      this.mXAxisRenderer = new XAxisRendererRadarChart(this.mViewPortHandler, this.mXAxis, this);
      this.mHighlighter = new RadarHighlighter(this);
   }

   public void notifyDataSetChanged() {
      if (this.mData != null) {
         this.calcMinMax();
         this.mYAxisRenderer.computeAxis(this.mYAxis.mAxisMinimum, this.mYAxis.mAxisMaximum, this.mYAxis.isInverted());
         this.mXAxisRenderer.computeAxis(this.mXAxis.mAxisMinimum, this.mXAxis.mAxisMaximum, false);
         if (this.mLegend != null && !this.mLegend.isLegendCustom()) {
            this.mLegendRenderer.computeLegend(this.mData);
         }

         this.calculateOffsets();
      }
   }

   protected void onDraw(Canvas var1) {
      super.onDraw(var1);
      if (this.mData != null) {
         if (this.mXAxis.isEnabled()) {
            this.mXAxisRenderer.computeAxis(this.mXAxis.mAxisMinimum, this.mXAxis.mAxisMaximum, false);
         }

         this.mXAxisRenderer.renderAxisLabels(var1);
         if (this.mDrawWeb) {
            this.mRenderer.drawExtras(var1);
         }

         if (this.mYAxis.isEnabled() && this.mYAxis.isDrawLimitLinesBehindDataEnabled()) {
            this.mYAxisRenderer.renderLimitLines(var1);
         }

         this.mRenderer.drawData(var1);
         if (this.valuesToHighlight()) {
            this.mRenderer.drawHighlighted(var1, this.mIndicesToHighlight);
         }

         if (this.mYAxis.isEnabled() && !this.mYAxis.isDrawLimitLinesBehindDataEnabled()) {
            this.mYAxisRenderer.renderLimitLines(var1);
         }

         this.mYAxisRenderer.renderAxisLabels(var1);
         this.mRenderer.drawValues(var1);
         this.mLegendRenderer.renderLegend(var1);
         this.drawDescription(var1);
         this.drawMarkers(var1);
      }
   }

   public void setDrawWeb(boolean var1) {
      this.mDrawWeb = var1;
   }

   public void setSkipWebLineCount(int var1) {
      this.mSkipWebLineCount = Math.max(0, var1);
   }

   public void setWebAlpha(int var1) {
      this.mWebAlpha = var1;
   }

   public void setWebColor(int var1) {
      this.mWebColor = var1;
   }

   public void setWebColorInner(int var1) {
      this.mWebColorInner = var1;
   }

   public void setWebLineWidth(float var1) {
      this.mWebLineWidth = Utils.convertDpToPixel(var1);
   }

   public void setWebLineWidthInner(float var1) {
      this.mInnerWebLineWidth = Utils.convertDpToPixel(var1);
   }
}
