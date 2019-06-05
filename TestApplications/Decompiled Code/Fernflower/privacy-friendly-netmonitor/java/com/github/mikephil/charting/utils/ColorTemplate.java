package com.github.mikephil.charting.utils;

import android.content.res.Resources;
import android.graphics.Color;
import java.util.ArrayList;
import java.util.List;

public class ColorTemplate {
   public static final int[] COLORFUL_COLORS = new int[]{Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0), Color.rgb(106, 150, 31), Color.rgb(179, 100, 53)};
   public static final int COLOR_NONE = 1122867;
   public static final int COLOR_SKIP = 1122868;
   public static final int[] JOYFUL_COLORS = new int[]{Color.rgb(217, 80, 138), Color.rgb(254, 149, 7), Color.rgb(254, 247, 120), Color.rgb(106, 167, 134), Color.rgb(53, 194, 209)};
   public static final int[] LIBERTY_COLORS = new int[]{Color.rgb(207, 248, 246), Color.rgb(148, 212, 212), Color.rgb(136, 180, 187), Color.rgb(118, 174, 175), Color.rgb(42, 109, 130)};
   public static final int[] MATERIAL_COLORS = new int[]{rgb("#2ecc71"), rgb("#f1c40f"), rgb("#e74c3c"), rgb("#3498db")};
   public static final int[] PASTEL_COLORS = new int[]{Color.rgb(64, 89, 128), Color.rgb(149, 165, 124), Color.rgb(217, 184, 162), Color.rgb(191, 134, 134), Color.rgb(179, 48, 80)};
   public static final int[] VORDIPLOM_COLORS = new int[]{Color.rgb(192, 255, 140), Color.rgb(255, 247, 140), Color.rgb(255, 208, 140), Color.rgb(140, 234, 255), Color.rgb(255, 140, 157)};

   public static int colorWithAlpha(int var0, int var1) {
      return var0 & 16777215 | (var1 & 255) << 24;
   }

   public static List createColors(Resources var0, int[] var1) {
      ArrayList var2 = new ArrayList();
      int var3 = 0;

      for(int var4 = var1.length; var3 < var4; ++var3) {
         var2.add(var0.getColor(var1[var3]));
      }

      return var2;
   }

   public static List createColors(int[] var0) {
      ArrayList var1 = new ArrayList();
      int var2 = 0;

      for(int var3 = var0.length; var2 < var3; ++var2) {
         var1.add(var0[var2]);
      }

      return var1;
   }

   public static int getHoloBlue() {
      return Color.rgb(51, 181, 229);
   }

   public static int rgb(String var0) {
      int var1 = (int)Long.parseLong(var0.replace("#", ""), 16);
      return Color.rgb(var1 >> 16 & 255, var1 >> 8 & 255, var1 >> 0 & 255);
   }
}
