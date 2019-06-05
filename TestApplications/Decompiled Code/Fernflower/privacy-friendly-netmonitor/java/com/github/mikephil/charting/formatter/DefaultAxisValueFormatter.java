package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.components.AxisBase;
import java.text.DecimalFormat;

public class DefaultAxisValueFormatter implements IAxisValueFormatter {
   protected int digits;
   protected DecimalFormat mFormat;

   public DefaultAxisValueFormatter(int var1) {
      int var2 = 0;
      this.digits = 0;
      this.digits = var1;

      StringBuffer var3;
      for(var3 = new StringBuffer(); var2 < var1; ++var2) {
         if (var2 == 0) {
            var3.append(".");
         }

         var3.append("0");
      }

      StringBuilder var4 = new StringBuilder();
      var4.append("###,###,###,##0");
      var4.append(var3.toString());
      this.mFormat = new DecimalFormat(var4.toString());
   }

   public int getDecimalDigits() {
      return this.digits;
   }

   public String getFormattedValue(float var1, AxisBase var2) {
      return this.mFormat.format((double)var1);
   }
}
