package com.github.mikephil.charting.data;

import android.graphics.Color;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import java.util.ArrayList;
import java.util.List;

public class BarDataSet extends BarLineScatterCandleBubbleDataSet implements IBarDataSet {
   private int mBarBorderColor = -16777216;
   private float mBarBorderWidth = 0.0F;
   private int mBarShadowColor = Color.rgb(215, 215, 215);
   private int mEntryCountStacks = 0;
   private int mHighLightAlpha = 120;
   private String[] mStackLabels = new String[]{"Stack"};
   private int mStackSize = 1;

   public BarDataSet(List var1, String var2) {
      super(var1, var2);
      this.mHighLightColor = Color.rgb(0, 0, 0);
      this.calcStackSize(var1);
      this.calcEntryCountIncludingStacks(var1);
   }

   private void calcEntryCountIncludingStacks(List var1) {
      int var2 = 0;

      for(this.mEntryCountStacks = 0; var2 < var1.size(); ++var2) {
         float[] var3 = ((BarEntry)var1.get(var2)).getYVals();
         if (var3 == null) {
            ++this.mEntryCountStacks;
         } else {
            this.mEntryCountStacks += var3.length;
         }
      }

   }

   private void calcStackSize(List var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         float[] var3 = ((BarEntry)var1.get(var2)).getYVals();
         if (var3 != null && var3.length > this.mStackSize) {
            this.mStackSize = var3.length;
         }
      }

   }

   protected void calcMinMax(BarEntry var1) {
      if (var1 != null && !Float.isNaN(var1.getY())) {
         if (var1.getYVals() == null) {
            if (var1.getY() < this.mYMin) {
               this.mYMin = var1.getY();
            }

            if (var1.getY() > this.mYMax) {
               this.mYMax = var1.getY();
            }
         } else {
            if (-var1.getNegativeSum() < this.mYMin) {
               this.mYMin = -var1.getNegativeSum();
            }

            if (var1.getPositiveSum() > this.mYMax) {
               this.mYMax = var1.getPositiveSum();
            }
         }

         this.calcMinMaxX(var1);
      }

   }

   public DataSet copy() {
      ArrayList var1 = new ArrayList();
      var1.clear();

      for(int var2 = 0; var2 < this.mValues.size(); ++var2) {
         var1.add(((BarEntry)this.mValues.get(var2)).copy());
      }

      BarDataSet var3 = new BarDataSet(var1, this.getLabel());
      var3.mColors = this.mColors;
      var3.mStackSize = this.mStackSize;
      var3.mBarShadowColor = this.mBarShadowColor;
      var3.mStackLabels = this.mStackLabels;
      var3.mHighLightColor = this.mHighLightColor;
      var3.mHighLightAlpha = this.mHighLightAlpha;
      return var3;
   }

   public int getBarBorderColor() {
      return this.mBarBorderColor;
   }

   public float getBarBorderWidth() {
      return this.mBarBorderWidth;
   }

   public int getBarShadowColor() {
      return this.mBarShadowColor;
   }

   public int getEntryCountStacks() {
      return this.mEntryCountStacks;
   }

   public int getHighLightAlpha() {
      return this.mHighLightAlpha;
   }

   public String[] getStackLabels() {
      return this.mStackLabels;
   }

   public int getStackSize() {
      return this.mStackSize;
   }

   public boolean isStacked() {
      int var1 = this.mStackSize;
      boolean var2 = true;
      if (var1 <= 1) {
         var2 = false;
      }

      return var2;
   }

   public void setBarBorderColor(int var1) {
      this.mBarBorderColor = var1;
   }

   public void setBarBorderWidth(float var1) {
      this.mBarBorderWidth = var1;
   }

   public void setBarShadowColor(int var1) {
      this.mBarShadowColor = var1;
   }

   public void setHighLightAlpha(int var1) {
      this.mHighLightAlpha = var1;
   }

   public void setStackLabels(String[] var1) {
      this.mStackLabels = var1;
   }
}
