package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

public class DefaultFillFormatter implements IFillFormatter {
   public float getFillLinePosition(ILineDataSet var1, LineDataProvider var2) {
      float var3 = var2.getYChartMax();
      float var4 = var2.getYChartMin();
      LineData var7 = var2.getLineData();
      float var5 = var1.getYMax();
      float var6 = 0.0F;
      if (var5 <= 0.0F || var1.getYMin() >= 0.0F) {
         var6 = var3;
         if (var7.getYMax() > 0.0F) {
            var6 = 0.0F;
         }

         if (var7.getYMin() < 0.0F) {
            var4 = 0.0F;
         }

         if (var1.getYMin() >= 0.0F) {
            var6 = var4;
         }
      }

      return var6;
   }
}
