package com.github.mikephil.charting.components;

import android.graphics.DashPathEffect;
import android.util.Log;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.Utils;
import java.util.ArrayList;
import java.util.List;

public abstract class AxisBase extends ComponentBase {
   private int mAxisLineColor = -7829368;
   private DashPathEffect mAxisLineDashPathEffect = null;
   private float mAxisLineWidth = 1.0F;
   public float mAxisMaximum = 0.0F;
   public float mAxisMinimum = 0.0F;
   public float mAxisRange = 0.0F;
   protected IAxisValueFormatter mAxisValueFormatter;
   protected boolean mCenterAxisLabels = false;
   public float[] mCenteredEntries = new float[0];
   protected boolean mCustomAxisMax = false;
   protected boolean mCustomAxisMin = false;
   public int mDecimals;
   protected boolean mDrawAxisLine = true;
   protected boolean mDrawGridLines = true;
   protected boolean mDrawLabels = true;
   protected boolean mDrawLimitLineBehindData = false;
   public float[] mEntries = new float[0];
   public int mEntryCount;
   protected boolean mForceLabels = false;
   protected float mGranularity = 1.0F;
   protected boolean mGranularityEnabled = false;
   private int mGridColor = -7829368;
   private DashPathEffect mGridDashPathEffect = null;
   private float mGridLineWidth = 1.0F;
   private int mLabelCount = 6;
   protected List mLimitLines;
   protected float mSpaceMax = 0.0F;
   protected float mSpaceMin = 0.0F;

   public AxisBase() {
      this.mTextSize = Utils.convertDpToPixel(10.0F);
      this.mXOffset = Utils.convertDpToPixel(5.0F);
      this.mYOffset = Utils.convertDpToPixel(5.0F);
      this.mLimitLines = new ArrayList();
   }

   public void addLimitLine(LimitLine var1) {
      this.mLimitLines.add(var1);
      if (this.mLimitLines.size() > 6) {
         Log.e("MPAndroiChart", "Warning! You have more than 6 LimitLines on your axis, do you really want that?");
      }

   }

   public void calculate(float var1, float var2) {
      if (this.mCustomAxisMin) {
         var1 = this.mAxisMinimum;
      } else {
         var1 -= this.mSpaceMin;
      }

      if (this.mCustomAxisMax) {
         var2 = this.mAxisMaximum;
      } else {
         var2 += this.mSpaceMax;
      }

      float var3 = var1;
      float var4 = var2;
      if (Math.abs(var2 - var1) == 0.0F) {
         var4 = var2 + 1.0F;
         var3 = var1 - 1.0F;
      }

      this.mAxisMinimum = var3;
      this.mAxisMaximum = var4;
      this.mAxisRange = Math.abs(var4 - var3);
   }

   public void disableAxisLineDashedLine() {
      this.mAxisLineDashPathEffect = null;
   }

   public void disableGridDashedLine() {
      this.mGridDashPathEffect = null;
   }

   public void enableAxisLineDashedLine(float var1, float var2, float var3) {
      this.mAxisLineDashPathEffect = new DashPathEffect(new float[]{var1, var2}, var3);
   }

   public void enableGridDashedLine(float var1, float var2, float var3) {
      this.mGridDashPathEffect = new DashPathEffect(new float[]{var1, var2}, var3);
   }

   public int getAxisLineColor() {
      return this.mAxisLineColor;
   }

   public DashPathEffect getAxisLineDashPathEffect() {
      return this.mAxisLineDashPathEffect;
   }

   public float getAxisLineWidth() {
      return this.mAxisLineWidth;
   }

   public float getAxisMaximum() {
      return this.mAxisMaximum;
   }

   public float getAxisMinimum() {
      return this.mAxisMinimum;
   }

   public String getFormattedLabel(int var1) {
      return var1 >= 0 && var1 < this.mEntries.length ? this.getValueFormatter().getFormattedValue(this.mEntries[var1], this) : "";
   }

   public float getGranularity() {
      return this.mGranularity;
   }

   public int getGridColor() {
      return this.mGridColor;
   }

   public DashPathEffect getGridDashPathEffect() {
      return this.mGridDashPathEffect;
   }

   public float getGridLineWidth() {
      return this.mGridLineWidth;
   }

   public int getLabelCount() {
      return this.mLabelCount;
   }

   public List getLimitLines() {
      return this.mLimitLines;
   }

   public String getLongestLabel() {
      String var1 = "";

      String var4;
      for(int var2 = 0; var2 < this.mEntries.length; var1 = var4) {
         String var3 = this.getFormattedLabel(var2);
         var4 = var1;
         if (var3 != null) {
            var4 = var1;
            if (var1.length() < var3.length()) {
               var4 = var3;
            }
         }

         ++var2;
      }

      return var1;
   }

   public float getSpaceMax() {
      return this.mSpaceMax;
   }

   public float getSpaceMin() {
      return this.mSpaceMin;
   }

   public IAxisValueFormatter getValueFormatter() {
      if (this.mAxisValueFormatter == null || this.mAxisValueFormatter instanceof DefaultAxisValueFormatter && ((DefaultAxisValueFormatter)this.mAxisValueFormatter).getDecimalDigits() != this.mDecimals) {
         this.mAxisValueFormatter = new DefaultAxisValueFormatter(this.mDecimals);
      }

      return this.mAxisValueFormatter;
   }

   public boolean isAxisLineDashedLineEnabled() {
      boolean var1;
      if (this.mAxisLineDashPathEffect == null) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean isAxisMaxCustom() {
      return this.mCustomAxisMax;
   }

   public boolean isAxisMinCustom() {
      return this.mCustomAxisMin;
   }

   public boolean isCenterAxisLabelsEnabled() {
      boolean var1;
      if (this.mCenterAxisLabels && this.mEntryCount > 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isDrawAxisLineEnabled() {
      return this.mDrawAxisLine;
   }

   public boolean isDrawGridLinesEnabled() {
      return this.mDrawGridLines;
   }

   public boolean isDrawLabelsEnabled() {
      return this.mDrawLabels;
   }

   public boolean isDrawLimitLinesBehindDataEnabled() {
      return this.mDrawLimitLineBehindData;
   }

   public boolean isForceLabelsEnabled() {
      return this.mForceLabels;
   }

   public boolean isGranularityEnabled() {
      return this.mGranularityEnabled;
   }

   public boolean isGridDashedLineEnabled() {
      boolean var1;
      if (this.mGridDashPathEffect == null) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public void removeAllLimitLines() {
      this.mLimitLines.clear();
   }

   public void removeLimitLine(LimitLine var1) {
      this.mLimitLines.remove(var1);
   }

   public void resetAxisMaximum() {
      this.mCustomAxisMax = false;
   }

   public void resetAxisMinimum() {
      this.mCustomAxisMin = false;
   }

   public void setAxisLineColor(int var1) {
      this.mAxisLineColor = var1;
   }

   public void setAxisLineDashedLine(DashPathEffect var1) {
      this.mAxisLineDashPathEffect = var1;
   }

   public void setAxisLineWidth(float var1) {
      this.mAxisLineWidth = Utils.convertDpToPixel(var1);
   }

   @Deprecated
   public void setAxisMaxValue(float var1) {
      this.setAxisMaximum(var1);
   }

   public void setAxisMaximum(float var1) {
      this.mCustomAxisMax = true;
      this.mAxisMaximum = var1;
      this.mAxisRange = Math.abs(var1 - this.mAxisMinimum);
   }

   @Deprecated
   public void setAxisMinValue(float var1) {
      this.setAxisMinimum(var1);
   }

   public void setAxisMinimum(float var1) {
      this.mCustomAxisMin = true;
      this.mAxisMinimum = var1;
      this.mAxisRange = Math.abs(this.mAxisMaximum - var1);
   }

   public void setCenterAxisLabels(boolean var1) {
      this.mCenterAxisLabels = var1;
   }

   public void setDrawAxisLine(boolean var1) {
      this.mDrawAxisLine = var1;
   }

   public void setDrawGridLines(boolean var1) {
      this.mDrawGridLines = var1;
   }

   public void setDrawLabels(boolean var1) {
      this.mDrawLabels = var1;
   }

   public void setDrawLimitLinesBehindData(boolean var1) {
      this.mDrawLimitLineBehindData = var1;
   }

   public void setGranularity(float var1) {
      this.mGranularity = var1;
      this.mGranularityEnabled = true;
   }

   public void setGranularityEnabled(boolean var1) {
      this.mGranularityEnabled = var1;
   }

   public void setGridColor(int var1) {
      this.mGridColor = var1;
   }

   public void setGridDashedLine(DashPathEffect var1) {
      this.mGridDashPathEffect = var1;
   }

   public void setGridLineWidth(float var1) {
      this.mGridLineWidth = Utils.convertDpToPixel(var1);
   }

   public void setLabelCount(int var1) {
      int var2 = var1;
      if (var1 > 25) {
         var2 = 25;
      }

      var1 = var2;
      if (var2 < 2) {
         var1 = 2;
      }

      this.mLabelCount = var1;
      this.mForceLabels = false;
   }

   public void setLabelCount(int var1, boolean var2) {
      this.setLabelCount(var1);
      this.mForceLabels = var2;
   }

   public void setSpaceMax(float var1) {
      this.mSpaceMax = var1;
   }

   public void setSpaceMin(float var1) {
      this.mSpaceMin = var1;
   }

   public void setValueFormatter(IAxisValueFormatter var1) {
      if (var1 == null) {
         this.mAxisValueFormatter = new DefaultAxisValueFormatter(this.mDecimals);
      } else {
         this.mAxisValueFormatter = var1;
      }

   }
}
