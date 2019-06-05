package com.github.mikephil.charting.highlight;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.MPPointD;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HorizontalBarHighlighter extends BarHighlighter {
   public HorizontalBarHighlighter(BarDataProvider var1) {
      super(var1);
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
            MPPointD var9 = ((BarDataProvider)this.mChart).getTransformer(var1.getAxisDependency()).getPixelForValues(var8.getY(), var8.getX());
            var5.add(new Highlight(var8.getX(), var8.getY(), (float)var9.x, (float)var9.y, var2, var1.getAxisDependency()));
         }

         return var5;
      }
   }

   protected float getDistance(float var1, float var2, float var3, float var4) {
      return Math.abs(var2 - var4);
   }

   public Highlight getHighlight(float var1, float var2) {
      BarData var3 = ((BarDataProvider)this.mChart).getBarData();
      MPPointD var4 = this.getValsForTouch(var2, var1);
      Highlight var5 = this.getHighlightForX((float)var4.y, var2, var1);
      if (var5 == null) {
         return null;
      } else {
         IBarDataSet var6 = (IBarDataSet)var3.getDataSetByIndex(var5.getDataSetIndex());
         if (var6.isStacked()) {
            return this.getStackedHighlight(var5, var6, (float)var4.y, (float)var4.x);
         } else {
            MPPointD.recycleInstance(var4);
            return var5;
         }
      }
   }
}
