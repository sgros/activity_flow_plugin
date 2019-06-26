package androidx.core.graphics;

import android.graphics.Color;

public final class ColorUtils {
   private static final ThreadLocal TEMP_ARRAY = new ThreadLocal();

   public static void RGBToHSL(int var0, int var1, int var2, float[] var3) {
      float var4 = (float)var0 / 255.0F;
      float var5 = (float)var1 / 255.0F;
      float var6 = (float)var2 / 255.0F;
      float var7 = Math.max(var4, Math.max(var5, var6));
      float var8 = Math.min(var4, Math.min(var5, var6));
      float var9 = var7 - var8;
      float var10 = (var7 + var8) / 2.0F;
      if (var7 == var8) {
         var6 = 0.0F;
         var9 = 0.0F;
      } else {
         if (var7 == var4) {
            var6 = (var5 - var6) / var9 % 6.0F;
         } else if (var7 == var5) {
            var6 = (var6 - var4) / var9 + 2.0F;
         } else {
            var6 = (var4 - var5) / var9 + 4.0F;
         }

         var9 /= 1.0F - Math.abs(2.0F * var10 - 1.0F);
      }

      var4 = var6 * 60.0F % 360.0F;
      var6 = var4;
      if (var4 < 0.0F) {
         var6 = var4 + 360.0F;
      }

      var3[0] = constrain(var6, 0.0F, 360.0F);
      var3[1] = constrain(var9, 0.0F, 1.0F);
      var3[2] = constrain(var10, 0.0F, 1.0F);
   }

   public static void RGBToXYZ(int var0, int var1, int var2, double[] var3) {
      if (var3.length == 3) {
         double var4 = (double)var0;
         Double.isNaN(var4);
         var4 /= 255.0D;
         if (var4 < 0.04045D) {
            var4 /= 12.92D;
         } else {
            var4 = Math.pow((var4 + 0.055D) / 1.055D, 2.4D);
         }

         double var6 = (double)var1;
         Double.isNaN(var6);
         var6 /= 255.0D;
         if (var6 < 0.04045D) {
            var6 /= 12.92D;
         } else {
            var6 = Math.pow((var6 + 0.055D) / 1.055D, 2.4D);
         }

         double var8 = (double)var2;
         Double.isNaN(var8);
         var8 /= 255.0D;
         if (var8 < 0.04045D) {
            var8 /= 12.92D;
         } else {
            var8 = Math.pow((var8 + 0.055D) / 1.055D, 2.4D);
         }

         var3[0] = (0.4124D * var4 + 0.3576D * var6 + 0.1805D * var8) * 100.0D;
         var3[1] = (0.2126D * var4 + 0.7152D * var6 + 0.0722D * var8) * 100.0D;
         var3[2] = (var4 * 0.0193D + var6 * 0.1192D + var8 * 0.9505D) * 100.0D;
      } else {
         throw new IllegalArgumentException("outXyz must have a length of 3.");
      }
   }

   public static double calculateContrast(int var0, int var1) {
      if (Color.alpha(var1) == 255) {
         int var2 = var0;
         if (Color.alpha(var0) < 255) {
            var2 = compositeColors(var0, var1);
         }

         double var3 = calculateLuminance(var2) + 0.05D;
         double var5 = calculateLuminance(var1) + 0.05D;
         return Math.max(var3, var5) / Math.min(var3, var5);
      } else {
         StringBuilder var7 = new StringBuilder();
         var7.append("background can not be translucent: #");
         var7.append(Integer.toHexString(var1));
         throw new IllegalArgumentException(var7.toString());
      }
   }

   public static double calculateLuminance(int var0) {
      double[] var1 = getTempDouble3Array();
      colorToXYZ(var0, var1);
      return var1[1] / 100.0D;
   }

   public static int calculateMinimumAlpha(int var0, int var1, float var2) {
      int var3 = Color.alpha(var1);
      int var4 = 255;
      if (var3 != 255) {
         StringBuilder var11 = new StringBuilder();
         var11.append("background can not be translucent: #");
         var11.append(Integer.toHexString(var1));
         throw new IllegalArgumentException(var11.toString());
      } else {
         double var5 = calculateContrast(setAlphaComponent(var0, 255), var1);
         double var7 = (double)var2;
         if (var5 < var7) {
            return -1;
         } else {
            var3 = 0;

            for(int var9 = 0; var3 <= 10 && var4 - var9 > 1; ++var3) {
               int var10 = (var9 + var4) / 2;
               if (calculateContrast(setAlphaComponent(var0, var10), var1) < var7) {
                  var9 = var10;
               } else {
                  var4 = var10;
               }
            }

            return var4;
         }
      }
   }

   public static void colorToHSL(int var0, float[] var1) {
      RGBToHSL(Color.red(var0), Color.green(var0), Color.blue(var0), var1);
   }

   public static void colorToXYZ(int var0, double[] var1) {
      RGBToXYZ(Color.red(var0), Color.green(var0), Color.blue(var0), var1);
   }

   private static int compositeAlpha(int var0, int var1) {
      return 255 - (255 - var1) * (255 - var0) / 255;
   }

   public static int compositeColors(int var0, int var1) {
      int var2 = Color.alpha(var1);
      int var3 = Color.alpha(var0);
      int var4 = compositeAlpha(var3, var2);
      return Color.argb(var4, compositeComponent(Color.red(var0), var3, Color.red(var1), var2, var4), compositeComponent(Color.green(var0), var3, Color.green(var1), var2, var4), compositeComponent(Color.blue(var0), var3, Color.blue(var1), var2, var4));
   }

   private static int compositeComponent(int var0, int var1, int var2, int var3, int var4) {
      return var4 == 0 ? 0 : (var0 * 255 * var1 + var2 * var3 * (255 - var1)) / (var4 * 255);
   }

   private static float constrain(float var0, float var1, float var2) {
      if (var0 >= var1) {
         var1 = var0;
         if (var0 > var2) {
            var1 = var2;
         }
      }

      return var1;
   }

   private static double[] getTempDouble3Array() {
      double[] var0 = (double[])TEMP_ARRAY.get();
      double[] var1 = var0;
      if (var0 == null) {
         var1 = new double[3];
         TEMP_ARRAY.set(var1);
      }

      return var1;
   }

   public static int setAlphaComponent(int var0, int var1) {
      if (var1 >= 0 && var1 <= 255) {
         return var0 & 16777215 | var1 << 24;
      } else {
         throw new IllegalArgumentException("alpha must be between 0 and 255.");
      }
   }
}
