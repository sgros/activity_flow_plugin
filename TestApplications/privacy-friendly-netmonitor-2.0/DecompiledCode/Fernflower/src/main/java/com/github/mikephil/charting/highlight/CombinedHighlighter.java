package com.github.mikephil.charting.highlight;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.dataprovider.CombinedDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import java.util.Iterator;
import java.util.List;

public class CombinedHighlighter extends ChartHighlighter implements IHighlighter {
   protected BarHighlighter barHighlighter;

   public CombinedHighlighter(CombinedDataProvider var1, BarDataProvider var2) {
      super(var1);
      BarHighlighter var3;
      if (var2.getBarData() == null) {
         var3 = null;
      } else {
         var3 = new BarHighlighter(var2);
      }

      this.barHighlighter = var3;
   }

   protected List getHighlightsAtXValue(float var1, float var2, float var3) {
      this.mHighlightBuffer.clear();
      List var4 = ((CombinedDataProvider)this.mChart).getCombinedData().getAllData();

      for(int var5 = 0; var5 < var4.size(); ++var5) {
         ChartData var6 = (ChartData)var4.get(var5);
         if (this.barHighlighter != null && var6 instanceof BarData) {
            Highlight var12 = this.barHighlighter.getHighlight(var2, var3);
            if (var12 != null) {
               var12.setDataIndex(var5);
               this.mHighlightBuffer.add(var12);
            }
         } else {
            int var7 = var6.getDataSetCount();

            for(int var8 = 0; var8 < var7; ++var8) {
               IDataSet var10 = ((BarLineScatterCandleBubbleData)var4.get(var5)).getDataSetByIndex(var8);
               if (var10.isHighlightEnabled()) {
                  Iterator var11 = this.buildHighlights(var10, var8, var1, DataSet.Rounding.CLOSEST).iterator();

                  while(var11.hasNext()) {
                     Highlight var9 = (Highlight)var11.next();
                     var9.setDataIndex(var5);
                     this.mHighlightBuffer.add(var9);
                  }
               }
            }
         }
      }

      return this.mHighlightBuffer;
   }
}
