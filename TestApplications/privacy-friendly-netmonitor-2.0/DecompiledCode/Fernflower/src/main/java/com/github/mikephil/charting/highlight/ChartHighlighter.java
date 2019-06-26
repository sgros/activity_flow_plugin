package com.github.mikephil.charting.highlight;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.MPPointD;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChartHighlighter implements IHighlighter {
   protected BarLineScatterCandleBubbleDataProvider mChart;
   protected List mHighlightBuffer = new ArrayList();

   public ChartHighlighter(BarLineScatterCandleBubbleDataProvider var1) {
      this.mChart = var1;
   }

   protected List buildHighlights(IDataSet var1, int var2, float var3, DataSet.Rounding var4) {
      ArrayList var5 = new ArrayList();
      List var6 = var1.getEntriesForXValue(var3);
      List var7 = var6;
      Entry var8;
      if (var6.size() == 0) {
         var8 = var1.getEntryForXValue(var3, Float.NaN, var4);
         var7 = var6;
         if (var8 != null) {
            var7 = var1.getEntriesForXValue(var8.getX());
         }
      }

      if (var7.size() == 0) {
         return var5;
      } else {
         Iterator var10 = var7.iterator();

         while(var10.hasNext()) {
            var8 = (Entry)var10.next();
            MPPointD var9 = this.mChart.getTransformer(var1.getAxisDependency()).getPixelForValues(var8.getX(), var8.getY());
            var5.add(new Highlight(var8.getX(), var8.getY(), (float)var9.x, (float)var9.y, var2, var1.getAxisDependency()));
         }

         return var5;
      }
   }

   public Highlight getClosestHighlightByPixel(List var1, float var2, float var3, YAxis.AxisDependency var4, float var5) {
      Highlight var6 = null;
      int var7 = 0;

      for(float var8 = var5; var7 < var1.size(); var8 = var5) {
         Highlight var10;
         label17: {
            Highlight var9 = (Highlight)var1.get(var7);
            if (var4 != null) {
               var10 = var6;
               var5 = var8;
               if (var9.getAxis() != var4) {
                  break label17;
               }
            }

            float var11 = this.getDistance(var2, var3, var9.getXPx(), var9.getYPx());
            var10 = var6;
            var5 = var8;
            if (var11 < var8) {
               var10 = var9;
               var5 = var11;
            }
         }

         ++var7;
         var6 = var10;
      }

      return var6;
   }

   protected BarLineScatterCandleBubbleData getData() {
      return this.mChart.getData();
   }

   protected float getDistance(float var1, float var2, float var3, float var4) {
      return (float)Math.hypot((double)(var1 - var3), (double)(var2 - var4));
   }

   public Highlight getHighlight(float var1, float var2) {
      MPPointD var3 = this.getValsForTouch(var1, var2);
      float var4 = (float)var3.x;
      MPPointD.recycleInstance(var3);
      return this.getHighlightForX(var4, var1, var2);
   }

   protected Highlight getHighlightForX(float var1, float var2, float var3) {
      List var4 = this.getHighlightsAtXValue(var1, var2, var3);
      if (var4.isEmpty()) {
         return null;
      } else {
         YAxis.AxisDependency var5;
         if (this.getMinimumDistance(var4, var3, YAxis.AxisDependency.LEFT) < this.getMinimumDistance(var4, var3, YAxis.AxisDependency.RIGHT)) {
            var5 = YAxis.AxisDependency.LEFT;
         } else {
            var5 = YAxis.AxisDependency.RIGHT;
         }

         return this.getClosestHighlightByPixel(var4, var2, var3, var5, this.mChart.getMaxHighlightDistance());
      }
   }

   protected float getHighlightPos(Highlight var1) {
      return var1.getYPx();
   }

   protected List getHighlightsAtXValue(float var1, float var2, float var3) {
      this.mHighlightBuffer.clear();
      BarLineScatterCandleBubbleData var4 = this.getData();
      if (var4 == null) {
         return this.mHighlightBuffer;
      } else {
         int var5 = 0;

         for(int var6 = var4.getDataSetCount(); var5 < var6; ++var5) {
            IDataSet var7 = var4.getDataSetByIndex(var5);
            if (var7.isHighlightEnabled()) {
               this.mHighlightBuffer.addAll(this.buildHighlights(var7, var5, var1, DataSet.Rounding.CLOSEST));
            }
         }

         return this.mHighlightBuffer;
      }
   }

   protected float getMinimumDistance(List var1, float var2, YAxis.AxisDependency var3) {
      float var4 = Float.MAX_VALUE;

      float var7;
      for(int var5 = 0; var5 < var1.size(); var4 = var7) {
         Highlight var6 = (Highlight)var1.get(var5);
         var7 = var4;
         if (var6.getAxis() == var3) {
            float var8 = Math.abs(this.getHighlightPos(var6) - var2);
            var7 = var4;
            if (var8 < var4) {
               var7 = var8;
            }
         }

         ++var5;
      }

      return var4;
   }

   protected MPPointD getValsForTouch(float var1, float var2) {
      return this.mChart.getTransformer(YAxis.AxisDependency.LEFT).getValuesByTouchPoint(var1, var2);
   }
}
