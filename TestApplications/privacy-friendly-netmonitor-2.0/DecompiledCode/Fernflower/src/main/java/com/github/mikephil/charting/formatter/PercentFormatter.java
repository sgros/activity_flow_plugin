package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.text.DecimalFormat;

public class PercentFormatter implements IValueFormatter, IAxisValueFormatter {
   protected DecimalFormat mFormat;

   public PercentFormatter() {
      this.mFormat = new DecimalFormat("###,###,##0.0");
   }

   public PercentFormatter(DecimalFormat var1) {
      this.mFormat = var1;
   }

   public int getDecimalDigits() {
      return 1;
   }

   public String getFormattedValue(float var1, AxisBase var2) {
      StringBuilder var3 = new StringBuilder();
      var3.append(this.mFormat.format((double)var1));
      var3.append(" %");
      return var3.toString();
   }

   public String getFormattedValue(float var1, Entry var2, int var3, ViewPortHandler var4) {
      StringBuilder var5 = new StringBuilder();
      var5.append(this.mFormat.format((double)var1));
      var5.append(" %");
      return var5.toString();
   }
}
