package com.github.mikephil.charting.highlight;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.MPPointD;

public class BarHighlighter extends ChartHighlighter {
   public BarHighlighter(BarDataProvider var1) {
      super(var1);
   }

   protected int getClosestStackIndex(Range[] var1, float var2) {
      byte var3 = 0;
      if (var1 != null && var1.length != 0) {
         int var4 = var1.length;
         int var5 = 0;

         int var6;
         for(var6 = var5; var5 < var4; ++var5) {
            if (var1[var5].contains(var2)) {
               return var6;
            }

            ++var6;
         }

         var6 = Math.max(var1.length - 1, 0);
         var5 = var3;
         if (var2 > var1[var6].to) {
            var5 = var6;
         }

         return var5;
      } else {
         return 0;
      }
   }

   protected BarLineScatterCandleBubbleData getData() {
      return ((BarDataProvider)this.mChart).getBarData();
   }

   protected float getDistance(float var1, float var2, float var3, float var4) {
      return Math.abs(var1 - var3);
   }

   public Highlight getHighlight(float var1, float var2) {
      Highlight var3 = super.getHighlight(var1, var2);
      if (var3 == null) {
         return null;
      } else {
         MPPointD var4 = this.getValsForTouch(var1, var2);
         IBarDataSet var5 = (IBarDataSet)((BarDataProvider)this.mChart).getBarData().getDataSetByIndex(var3.getDataSetIndex());
         if (var5.isStacked()) {
            return this.getStackedHighlight(var3, var5, (float)var4.x, (float)var4.y);
         } else {
            MPPointD.recycleInstance(var4);
            return var3;
         }
      }
   }

   public Highlight getStackedHighlight(Highlight var1, IBarDataSet var2, float var3, float var4) {
      BarEntry var5 = (BarEntry)var2.getEntryForXValue(var3, var4);
      if (var5 == null) {
         return null;
      } else if (var5.getYVals() == null) {
         return var1;
      } else {
         Range[] var6 = var5.getRanges();
         if (var6.length > 0) {
            int var7 = this.getClosestStackIndex(var6, var4);
            MPPointD var8 = ((BarDataProvider)this.mChart).getTransformer(var2.getAxisDependency()).getPixelForValues(var1.getX(), var6[var7].to);
            var1 = new Highlight(var5.getX(), var5.getY(), (float)var8.x, (float)var8.y, var1.getDataSetIndex(), var7, var1.getAxis());
            MPPointD.recycleInstance(var8);
            return var1;
         } else {
            return null;
         }
      }
   }
}
