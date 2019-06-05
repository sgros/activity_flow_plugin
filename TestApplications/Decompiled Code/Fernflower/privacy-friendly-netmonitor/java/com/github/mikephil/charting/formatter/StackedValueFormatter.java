package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.text.DecimalFormat;

public class StackedValueFormatter implements IValueFormatter {
   private String mAppendix;
   private boolean mDrawWholeStack;
   private DecimalFormat mFormat;

   public StackedValueFormatter(boolean var1, String var2, int var3) {
      this.mDrawWholeStack = var1;
      this.mAppendix = var2;
      StringBuffer var6 = new StringBuffer();

      for(int var4 = 0; var4 < var3; ++var4) {
         if (var4 == 0) {
            var6.append(".");
         }

         var6.append("0");
      }

      StringBuilder var5 = new StringBuilder();
      var5.append("###,###,###,##0");
      var5.append(var6.toString());
      this.mFormat = new DecimalFormat(var5.toString());
   }

   public String getFormattedValue(float var1, Entry var2, int var3, ViewPortHandler var4) {
      if (!this.mDrawWholeStack && var2 instanceof BarEntry) {
         BarEntry var5 = (BarEntry)var2;
         float[] var7 = var5.getYVals();
         if (var7 != null) {
            if (var7[var7.length - 1] == var1) {
               StringBuilder var8 = new StringBuilder();
               var8.append(this.mFormat.format((double)var5.getY()));
               var8.append(this.mAppendix);
               return var8.toString();
            }

            return "";
         }
      }

      StringBuilder var6 = new StringBuilder();
      var6.append(this.mFormat.format((double)var1));
      var6.append(this.mAppendix);
      return var6.toString();
   }
}
