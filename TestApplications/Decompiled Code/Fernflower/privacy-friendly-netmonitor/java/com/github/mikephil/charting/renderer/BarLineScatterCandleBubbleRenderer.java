package com.github.mikephil.charting.renderer;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

public abstract class BarLineScatterCandleBubbleRenderer extends DataRenderer {
   protected BarLineScatterCandleBubbleRenderer.XBounds mXBounds = new BarLineScatterCandleBubbleRenderer.XBounds();

   public BarLineScatterCandleBubbleRenderer(ChartAnimator var1, ViewPortHandler var2) {
      super(var1, var2);
   }

   protected boolean isInBoundsX(Entry var1, IBarLineScatterCandleBubbleDataSet var2) {
      if (var1 == null) {
         return false;
      } else {
         float var3 = (float)var2.getEntryIndex(var1);
         return var1 != null && var3 < (float)var2.getEntryCount() * this.mAnimator.getPhaseX();
      }
   }

   protected boolean shouldDrawValues(IDataSet var1) {
      boolean var2;
      if (!var1.isVisible() || !var1.isDrawValuesEnabled() && !var1.isDrawIconsEnabled()) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   protected class XBounds {
      public int max;
      public int min;
      public int range;

      public void set(BarLineScatterCandleBubbleDataProvider var1, IBarLineScatterCandleBubbleDataSet var2) {
         float var3 = Math.max(0.0F, Math.min(1.0F, BarLineScatterCandleBubbleRenderer.this.mAnimator.getPhaseX()));
         float var4 = var1.getLowestVisibleX();
         float var5 = var1.getHighestVisibleX();
         Entry var6 = var2.getEntryForXValue(var4, Float.NaN, DataSet.Rounding.DOWN);
         Entry var9 = var2.getEntryForXValue(var5, Float.NaN, DataSet.Rounding.UP);
         byte var7 = 0;
         int var8;
         if (var6 == null) {
            var8 = 0;
         } else {
            var8 = var2.getEntryIndex(var6);
         }

         this.min = var8;
         if (var9 == null) {
            var8 = var7;
         } else {
            var8 = var2.getEntryIndex(var9);
         }

         this.max = var8;
         this.range = (int)((float)(this.max - this.min) * var3);
      }
   }
}
