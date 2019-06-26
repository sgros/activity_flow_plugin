package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.components.AxisBase;
import java.util.Collection;

public class IndexAxisValueFormatter implements IAxisValueFormatter {
   private int mValueCount = 0;
   private String[] mValues = new String[0];

   public IndexAxisValueFormatter() {
   }

   public IndexAxisValueFormatter(Collection var1) {
      if (var1 != null) {
         this.setValues((String[])var1.toArray(new String[var1.size()]));
      }

   }

   public IndexAxisValueFormatter(String[] var1) {
      if (var1 != null) {
         this.setValues(var1);
      }

   }

   public String getFormattedValue(float var1, AxisBase var2) {
      int var3 = Math.round(var1);
      return var3 >= 0 && var3 < this.mValueCount && var3 == (int)var1 ? this.mValues[var3] : "";
   }

   public String[] getValues() {
      return this.mValues;
   }

   public void setValues(String[] var1) {
      String[] var2 = var1;
      if (var1 == null) {
         var2 = new String[0];
      }

      this.mValues = var2;
      this.mValueCount = var2.length;
   }
}
