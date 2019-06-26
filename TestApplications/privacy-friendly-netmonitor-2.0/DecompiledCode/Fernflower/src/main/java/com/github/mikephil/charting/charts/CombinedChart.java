package com.github.mikephil.charting.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.highlight.CombinedHighlighter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.CombinedDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import com.github.mikephil.charting.renderer.CombinedChartRenderer;

public class CombinedChart extends BarLineChartBase implements CombinedDataProvider {
   private boolean mDrawBarShadow = false;
   protected CombinedChart.DrawOrder[] mDrawOrder;
   private boolean mDrawValueAboveBar = true;
   protected boolean mHighlightFullBarEnabled = false;

   public CombinedChart(Context var1) {
      super(var1);
   }

   public CombinedChart(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   public CombinedChart(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
   }

   protected void drawMarkers(Canvas var1) {
      if (this.mMarker != null && this.isDrawMarkersEnabled() && this.valuesToHighlight()) {
         for(int var2 = 0; var2 < this.mIndicesToHighlight.length; ++var2) {
            Highlight var3 = this.mIndicesToHighlight[var2];
            IBarLineScatterCandleBubbleDataSet var4 = ((CombinedData)this.mData).getDataSetByHighlight(var3);
            Entry var5 = ((CombinedData)this.mData).getEntryForHighlight(var3);
            if (var5 != null && (float)var4.getEntryIndex(var5) <= (float)var4.getEntryCount() * this.mAnimator.getPhaseX()) {
               float[] var6 = this.getMarkerPosition(var3);
               if (this.mViewPortHandler.isInBounds(var6[0], var6[1])) {
                  this.mMarker.refreshContent(var5, var3);
                  this.mMarker.draw(var1, var6[0], var6[1]);
               }
            }
         }

      }
   }

   public BarData getBarData() {
      return this.mData == null ? null : ((CombinedData)this.mData).getBarData();
   }

   public BubbleData getBubbleData() {
      return this.mData == null ? null : ((CombinedData)this.mData).getBubbleData();
   }

   public CandleData getCandleData() {
      return this.mData == null ? null : ((CombinedData)this.mData).getCandleData();
   }

   public CombinedData getCombinedData() {
      return (CombinedData)this.mData;
   }

   public CombinedChart.DrawOrder[] getDrawOrder() {
      return this.mDrawOrder;
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

   public LineData getLineData() {
      return this.mData == null ? null : ((CombinedData)this.mData).getLineData();
   }

   public ScatterData getScatterData() {
      return this.mData == null ? null : ((CombinedData)this.mData).getScatterData();
   }

   protected void init() {
      super.init();
      this.mDrawOrder = new CombinedChart.DrawOrder[]{CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.BUBBLE, CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.CANDLE, CombinedChart.DrawOrder.SCATTER};
      this.setHighlighter(new CombinedHighlighter(this, this));
      this.setHighlightFullBarEnabled(true);
      this.mRenderer = new CombinedChartRenderer(this, this.mAnimator, this.mViewPortHandler);
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

   public void setData(CombinedData var1) {
      super.setData(var1);
      this.setHighlighter(new CombinedHighlighter(this, this));
      ((CombinedChartRenderer)this.mRenderer).createRenderers();
      this.mRenderer.initBuffers();
   }

   public void setDrawBarShadow(boolean var1) {
      this.mDrawBarShadow = var1;
   }

   public void setDrawOrder(CombinedChart.DrawOrder[] var1) {
      if (var1 != null && var1.length > 0) {
         this.mDrawOrder = var1;
      }
   }

   public void setDrawValueAboveBar(boolean var1) {
      this.mDrawValueAboveBar = var1;
   }

   public void setHighlightFullBarEnabled(boolean var1) {
      this.mHighlightFullBarEnabled = var1;
   }

   public static enum DrawOrder {
      BAR,
      BUBBLE,
      CANDLE,
      LINE,
      SCATTER;
   }
}
