package android.support.v4.graphics;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

public final class ColorUtils {
   private static final int MIN_ALPHA_SEARCH_MAX_ITERATIONS = 10;
   private static final int MIN_ALPHA_SEARCH_PRECISION = 1;
   private static final ThreadLocal TEMP_ARRAY = new ThreadLocal();
   private static final double XYZ_EPSILON = 0.008856D;
   private static final double XYZ_KAPPA = 903.3D;
   private static final double XYZ_WHITE_REFERENCE_X = 95.047D;
   private static final double XYZ_WHITE_REFERENCE_Y = 100.0D;
   private static final double XYZ_WHITE_REFERENCE_Z = 108.883D;

   private ColorUtils() {
   }

   @ColorInt
   public static int HSLToColor(@NonNull float[] var0) {
      float var1 = var0[0];
      float var2 = var0[1];
      float var3 = var0[2];
      var2 = (1.0F - Math.abs(2.0F * var3 - 1.0F)) * var2;
      float var4 = var3 - 0.5F * var2;
      var3 = (1.0F - Math.abs(var1 / 60.0F % 2.0F - 1.0F)) * var2;
      int var5;
      int var6;
      int var7;
      switch((int)var1 / 60) {
      case 0:
         var6 = Math.round((var2 + var4) * 255.0F);
         var7 = Math.round((var3 + var4) * 255.0F);
         var5 = Math.round(255.0F * var4);
         break;
      case 1:
         var6 = Math.round((var3 + var4) * 255.0F);
         var7 = Math.round((var2 + var4) * 255.0F);
         var5 = Math.round(255.0F * var4);
         break;
      case 2:
         var6 = Math.round(255.0F * var4);
         var7 = Math.round((var2 + var4) * 255.0F);
         var5 = Math.round(255.0F * (var3 + var4));
         break;
      case 3:
         var6 = Math.round(255.0F * var4);
         var7 = Math.round((var3 + var4) * 255.0F);
         var5 = Math.round(255.0F * (var2 + var4));
         break;
      case 4:
         var6 = Math.round((var3 + var4) * 255.0F);
         var7 = Math.round(255.0F * var4);
         var5 = Math.round(255.0F * (var2 + var4));
         break;
      case 5:
      case 6:
         var6 = Math.round((var2 + var4) * 255.0F);
         var7 = Math.round(255.0F * var4);
         var5 = Math.round(255.0F * (var3 + var4));
         break;
      default:
         var5 = 0;
         var6 = var5;
         var7 = var5;
      }

      return Color.rgb(constrain(var6, 0, 255), constrain(var7, 0, 255), constrain(var5, 0, 255));
   }

   @ColorInt
   public static int LABToColor(@FloatRange(from = 0.0D,to = 100.0D) double var0, @FloatRange(from = -128.0D,to = 127.0D) double var2, @FloatRange(from = -128.0D,to = 127.0D) double var4) {
      double[] var6 = getTempDouble3Array();
      LABToXYZ(var0, var2, var4, var6);
      return XYZToColor(var6[0], var6[1], var6[2]);
   }

   public static void LABToXYZ(@FloatRange(from = 0.0D,to = 100.0D) double var0, @FloatRange(from = -128.0D,to = 127.0D) double var2, @FloatRange(from = -128.0D,to = 127.0D) double var4, @NonNull double[] var6) {
      double var7 = (var0 + 16.0D) / 116.0D;
      double var9 = var2 / 500.0D + var7;
      double var11 = var7 - var4 / 200.0D;
      var2 = Math.pow(var9, 3.0D);
      if (var2 <= 0.008856D) {
         var2 = (var9 * 116.0D - 16.0D) / 903.3D;
      }

      if (var0 > 7.9996247999999985D) {
         var0 = Math.pow(var7, 3.0D);
      } else {
         var0 /= 903.3D;
      }

      var4 = Math.pow(var11, 3.0D);
      if (var4 <= 0.008856D) {
         var4 = (116.0D * var11 - 16.0D) / 903.3D;
      }

      var6[0] = var2 * 95.047D;
      var6[1] = var0 * 100.0D;
      var6[2] = var4 * 108.883D;
   }

   public static void RGBToHSL(@IntRange(from = 0L,to = 255L) int var0, @IntRange(from = 0L,to = 255L) int var1, @IntRange(from = 0L,to = 255L) int var2, @NonNull float[] var3) {
      float var4 = (float)var0 / 255.0F;
      float var5 = (float)var1 / 255.0F;
      float var6 = (float)var2 / 255.0F;
      float var7 = Math.max(var4, Math.max(var5, var6));
      float var8 = Math.min(var4, Math.min(var5, var6));
      float var9 = var7 - var8;
      float var10 = (var7 + var8) / 2.0F;
      if (var7 == var8) {
         var8 = 0.0F;
         var9 = var8;
      } else {
         if (var7 == var4) {
            var8 = (var5 - var6) / var9 % 6.0F;
         } else if (var7 == var5) {
            var8 = (var6 - var4) / var9 + 2.0F;
         } else {
            var8 = (var4 - var5) / var9 + 4.0F;
         }

         var9 /= 1.0F - Math.abs(2.0F * var10 - 1.0F);
      }

      var6 = var8 * 60.0F % 360.0F;
      var8 = var6;
      if (var6 < 0.0F) {
         var8 = var6 + 360.0F;
      }

      var3[0] = constrain(var8, 0.0F, 360.0F);
      var3[1] = constrain(var9, 0.0F, 1.0F);
      var3[2] = constrain(var10, 0.0F, 1.0F);
   }

   public static void RGBToLAB(@IntRange(from = 0L,to = 255L) int var0, @IntRange(from = 0L,to = 255L) int var1, @IntRange(from = 0L,to = 255L) int var2, @NonNull double[] var3) {
      RGBToXYZ(var0, var1, var2, var3);
      XYZToLAB(var3[0], var3[1], var3[2], var3);
   }

   public static void RGBToXYZ(@IntRange(from = 0L,to = 255L) int var0, @IntRange(from = 0L,to = 255L) int var1, @IntRange(from = 0L,to = 255L) int var2, @NonNull double[] var3) {
      if (var3.length != 3) {
         throw new IllegalArgumentException("outXyz must have a length of 3.");
      } else {
         double var4 = (double)var0 / 255.0D;
         if (var4 < 0.04045D) {
            var4 /= 12.92D;
         } else {
            var4 = Math.pow((var4 + 0.055D) / 1.055D, 2.4D);
         }

         double var6 = (double)var1 / 255.0D;
         if (var6 < 0.04045D) {
            var6 /= 12.92D;
         } else {
            var6 = Math.pow((var6 + 0.055D) / 1.055D, 2.4D);
         }

         double var8 = (double)var2 / 255.0D;
         if (var8 < 0.04045D) {
            var8 /= 12.92D;
         } else {
            var8 = Math.pow((var8 + 0.055D) / 1.055D, 2.4D);
         }

         var3[0] = (0.4124D * var4 + 0.3576D * var6 + 0.1805D * var8) * 100.0D;
         var3[1] = (0.2126D * var4 + 0.7152D * var6 + 0.0722D * var8) * 100.0D;
         var3[2] = 100.0D * (var4 * 0.0193D + var6 * 0.1192D + var8 * 0.9505D);
      }
   }

   @ColorInt
   public static int XYZToColor(@FloatRange(from = 0.0D,to = 95.047D) double var0, @FloatRange(from = 0.0D,to = 100.0D) double var2, @FloatRange(from = 0.0D,to = 108.883D) double var4) {
      double var6 = (3.2406D * var0 + -1.5372D * var2 + -0.4986D * var4) / 100.0D;
      double var8 = (-0.9689D * var0 + 1.8758D * var2 + 0.0415D * var4) / 100.0D;
      var4 = (var0 * 0.0557D + var2 * -0.204D + 1.057D * var4) / 100.0D;
      if (var6 > 0.0031308D) {
         var0 = Math.pow(var6, 0.4166666666666667D) * 1.055D - 0.055D;
      } else {
         var0 = 12.92D * var6;
      }

      if (var8 > 0.0031308D) {
         var2 = Math.pow(var8, 0.4166666666666667D) * 1.055D - 0.055D;
      } else {
         var2 = 12.92D * var8;
      }

      if (var4 > 0.0031308D) {
         var4 = 1.055D * Math.pow(var4, 0.4166666666666667D) - 0.055D;
      } else {
         var4 = 12.92D * var4;
      }

      return Color.rgb(constrain((int)Math.round(var0 * 255.0D), 0, 255), constrain((int)Math.round(var2 * 255.0D), 0, 255), constrain((int)Math.round(255.0D * var4), 0, 255));
   }

   public static void XYZToLAB(@FloatRange(from = 0.0D,to = 95.047D) double var0, @FloatRange(from = 0.0D,to = 100.0D) double var2, @FloatRange(from = 0.0D,to = 108.883D) double var4, @NonNull double[] var6) {
      if (var6.length != 3) {
         throw new IllegalArgumentException("outLab must have a length of 3.");
      } else {
         var0 = pivotXyzComponent(var0 / 95.047D);
         var2 = pivotXyzComponent(var2 / 100.0D);
         var4 = pivotXyzComponent(var4 / 108.883D);
         var6[0] = Math.max(0.0D, 116.0D * var2 - 16.0D);
         var6[1] = 500.0D * (var0 - var2);
         var6[2] = 200.0D * (var2 - var4);
      }
   }

   @ColorInt
   public static int blendARGB(@ColorInt int var0, @ColorInt int var1, @FloatRange(from = 0.0D,to = 1.0D) float var2) {
      float var3 = 1.0F - var2;
      float var4 = (float)Color.alpha(var0);
      float var5 = (float)Color.alpha(var1);
      float var6 = (float)Color.red(var0);
      float var7 = (float)Color.red(var1);
      float var8 = (float)Color.green(var0);
      float var9 = (float)Color.green(var1);
      float var10 = (float)Color.blue(var0);
      float var11 = (float)Color.blue(var1);
      return Color.argb((int)(var4 * var3 + var5 * var2), (int)(var6 * var3 + var7 * var2), (int)(var8 * var3 + var9 * var2), (int)(var10 * var3 + var11 * var2));
   }

   public static void blendHSL(@NonNull float[] var0, @NonNull float[] var1, @FloatRange(from = 0.0D,to = 1.0D) float var2, @NonNull float[] var3) {
      if (var3.length != 3) {
         throw new IllegalArgumentException("result must have a length of 3.");
      } else {
         float var4 = 1.0F - var2;
         var3[0] = circularInterpolate(var0[0], var1[0], var2);
         var3[1] = var0[1] * var4 + var1[1] * var2;
         var3[2] = var0[2] * var4 + var1[2] * var2;
      }
   }

   public static void blendLAB(@NonNull double[] var0, @NonNull double[] var1, @FloatRange(from = 0.0D,to = 1.0D) double var2, @NonNull double[] var4) {
      if (var4.length != 3) {
         throw new IllegalArgumentException("outResult must have a length of 3.");
      } else {
         double var5 = 1.0D - var2;
         var4[0] = var0[0] * var5 + var1[0] * var2;
         var4[1] = var0[1] * var5 + var1[1] * var2;
         var4[2] = var0[2] * var5 + var1[2] * var2;
      }
   }

   public static double calculateContrast(@ColorInt int var0, @ColorInt int var1) {
      if (Color.alpha(var1) != 255) {
         StringBuilder var2 = new StringBuilder();
         var2.append("background can not be translucent: #");
         var2.append(Integer.toHexString(var1));
         throw new IllegalArgumentException(var2.toString());
      } else {
         int var3 = var0;
         if (Color.alpha(var0) < 255) {
            var3 = compositeColors(var0, var1);
         }

         double var4 = calculateLuminance(var3) + 0.05D;
         double var6 = calculateLuminance(var1) + 0.05D;
         return Math.max(var4, var6) / Math.min(var4, var6);
      }
   }

   @FloatRange(
      from = 0.0D,
      to = 1.0D
   )
   public static double calculateLuminance(@ColorInt int var0) {
      double[] var1 = getTempDouble3Array();
      colorToXYZ(var0, var1);
      return var1[1] / 100.0D;
   }

   public static int calculateMinimumAlpha(@ColorInt int var0, @ColorInt int var1, float var2) {
      int var3 = Color.alpha(var1);
      int var4 = 255;
      if (var3 != 255) {
         StringBuilder var5 = new StringBuilder();
         var5.append("background can not be translucent: #");
         var5.append(Integer.toHexString(var1));
         throw new IllegalArgumentException(var5.toString());
      } else {
         double var6 = calculateContrast(setAlphaComponent(var0, 255), var1);
         double var8 = (double)var2;
         if (var6 < var8) {
            return -1;
         } else {
            var3 = 0;

            for(int var10 = 0; var3 <= 10 && var4 - var10 > 1; ++var3) {
               int var11 = (var10 + var4) / 2;
               if (calculateContrast(setAlphaComponent(var0, var11), var1) < var8) {
                  var10 = var11;
               } else {
                  var4 = var11;
               }
            }

            return var4;
         }
      }
   }

   @VisibleForTesting
   static float circularInterpolate(float var0, float var1, float var2) {
      float var3 = var0;
      float var4 = var1;
      if (Math.abs(var1 - var0) > 180.0F) {
         if (var1 > var0) {
            var3 = var0 + 360.0F;
            var4 = var1;
         } else {
            var4 = var1 + 360.0F;
            var3 = var0;
         }
      }

      return (var3 + (var4 - var3) * var2) % 360.0F;
   }

   public static void colorToHSL(@ColorInt int var0, @NonNull float[] var1) {
      RGBToHSL(Color.red(var0), Color.green(var0), Color.blue(var0), var1);
   }

   public static void colorToLAB(@ColorInt int var0, @NonNull double[] var1) {
      RGBToLAB(Color.red(var0), Color.green(var0), Color.blue(var0), var1);
   }

   public static void colorToXYZ(@ColorInt int var0, @NonNull double[] var1) {
      RGBToXYZ(Color.red(var0), Color.green(var0), Color.blue(var0), var1);
   }

   private static int compositeAlpha(int var0, int var1) {
      return 255 - (255 - var1) * (255 - var0) / 255;
   }

   public static int compositeColors(@ColorInt int var0, @ColorInt int var1) {
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

   private static int constrain(int var0, int var1, int var2) {
      if (var0 >= var1) {
         var1 = var0;
         if (var0 > var2) {
            var1 = var2;
         }
      }

      return var1;
   }

   public static double distanceEuclidean(@NonNull double[] var0, @NonNull double[] var1) {
      return Math.sqrt(Math.pow(var0[0] - var1[0], 2.0D) + Math.pow(var0[1] - var1[1], 2.0D) + Math.pow(var0[2] - var1[2], 2.0D));
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

   private static double pivotXyzComponent(double var0) {
      if (var0 > 0.008856D) {
         var0 = Math.pow(var0, 0.3333333333333333D);
      } else {
         var0 = (903.3D * var0 + 16.0D) / 116.0D;
      }

      return var0;
   }

   @ColorInt
   public static int setAlphaComponent(@ColorInt int var0, @IntRange(from = 0L,to = 255L) int var1) {
      if (var1 >= 0 && var1 <= 255) {
         return var0 & 16777215 | var1 << 24;
      } else {
         throw new IllegalArgumentException("alpha must be between 0 and 255.");
      }
   }
}
