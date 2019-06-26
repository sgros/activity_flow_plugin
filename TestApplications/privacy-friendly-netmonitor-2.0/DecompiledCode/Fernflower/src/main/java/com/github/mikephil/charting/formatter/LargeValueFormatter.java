package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.text.DecimalFormat;

public class LargeValueFormatter implements IValueFormatter, IAxisValueFormatter {
   private static final int MAX_LENGTH = 5;
   private static String[] SUFFIX = new String[]{"", "k", "m", "b", "t"};
   private DecimalFormat mFormat;
   private String mText;

   public LargeValueFormatter() {
      this.mText = "";
      this.mFormat = new DecimalFormat("###E00");
   }

   public LargeValueFormatter(String var1) {
      this();
      this.mText = var1;
   }

   private String makePretty(double var1) {
      String var3 = this.mFormat.format(var1);
      int var4 = Character.getNumericValue(var3.charAt(var3.length() - 1));
      int var5 = Character.getNumericValue(var3.charAt(var3.length() - 2));
      StringBuilder var6 = new StringBuilder();
      var6.append(var5);
      var6.append("");
      var6.append(var4);
      var4 = Integer.valueOf(var6.toString());

      StringBuilder var7;
      String var8;
      for(var8 = var3.replaceAll("E[0-9][0-9]", SUFFIX[var4 / 3]); var8.length() > 5 || var8.matches("[0-9]+\\.[a-z]"); var8 = var7.toString()) {
         var7 = new StringBuilder();
         var7.append(var8.substring(0, var8.length() - 2));
         var7.append(var8.substring(var8.length() - 1));
      }

      return var8;
   }

   public int getDecimalDigits() {
      return 0;
   }

   public String getFormattedValue(float var1, AxisBase var2) {
      StringBuilder var3 = new StringBuilder();
      var3.append(this.makePretty((double)var1));
      var3.append(this.mText);
      return var3.toString();
   }

   public String getFormattedValue(float var1, Entry var2, int var3, ViewPortHandler var4) {
      StringBuilder var5 = new StringBuilder();
      var5.append(this.makePretty((double)var1));
      var5.append(this.mText);
      return var5.toString();
   }

   public void setAppendix(String var1) {
      this.mText = var1;
   }

   public void setSuffix(String[] var1) {
      SUFFIX = var1;
   }
}
