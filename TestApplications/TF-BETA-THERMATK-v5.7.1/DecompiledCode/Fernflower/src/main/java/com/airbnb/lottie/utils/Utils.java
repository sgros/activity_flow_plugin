package com.airbnb.lottie.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.os.Build.VERSION;
import android.provider.Settings.Global;
import android.provider.Settings.System;
import com.airbnb.lottie.L;
import com.airbnb.lottie.animation.content.TrimPathContent;
import com.airbnb.lottie.animation.keyframe.FloatKeyframeAnimation;
import java.io.Closeable;

public final class Utils {
   private static final float SQRT_2 = (float)Math.sqrt(2.0D);
   private static float dpScale = -1.0F;
   private static final PathMeasure pathMeasure = new PathMeasure();
   private static final float[] points = new float[4];
   private static final Path tempPath = new Path();
   private static final Path tempPath2 = new Path();

   public static void applyTrimPathIfNeeded(Path var0, float var1, float var2, float var3) {
      L.beginSection("applyTrimPathIfNeeded");
      pathMeasure.setPath(var0, false);
      float var4 = pathMeasure.getLength();
      if (var1 == 1.0F && var2 == 0.0F) {
         L.endSection("applyTrimPathIfNeeded");
      } else if (var4 >= 1.0F && (double)Math.abs(var2 - var1 - 1.0F) >= 0.01D) {
         float var5 = var1 * var4;
         var2 *= var4;
         var1 = Math.min(var5, var2);
         var5 = Math.max(var5, var2);
         var3 *= var4;
         var2 = var1 + var3;
         var5 += var3;
         var3 = var2;
         var1 = var5;
         if (var2 >= var4) {
            var3 = var2;
            var1 = var5;
            if (var5 >= var4) {
               var3 = (float)MiscUtils.floorMod(var2, var4);
               var1 = (float)MiscUtils.floorMod(var5, var4);
            }
         }

         var2 = var3;
         if (var3 < 0.0F) {
            var2 = (float)MiscUtils.floorMod(var3, var4);
         }

         var3 = var1;
         if (var1 < 0.0F) {
            var3 = (float)MiscUtils.floorMod(var1, var4);
         }

         if (var2 == var3) {
            var0.reset();
            L.endSection("applyTrimPathIfNeeded");
         } else {
            var1 = var2;
            if (var2 >= var3) {
               var1 = var2 - var4;
            }

            tempPath.reset();
            pathMeasure.getSegment(var1, var3, tempPath, true);
            if (var3 > var4) {
               tempPath2.reset();
               pathMeasure.getSegment(0.0F, var3 % var4, tempPath2, true);
               tempPath.addPath(tempPath2);
            } else if (var1 < 0.0F) {
               tempPath2.reset();
               pathMeasure.getSegment(var1 + var4, var4, tempPath2, true);
               tempPath.addPath(tempPath2);
            }

            var0.set(tempPath);
            L.endSection("applyTrimPathIfNeeded");
         }
      } else {
         L.endSection("applyTrimPathIfNeeded");
      }
   }

   public static void applyTrimPathIfNeeded(Path var0, TrimPathContent var1) {
      if (var1 != null && !var1.isHidden()) {
         float var2 = ((FloatKeyframeAnimation)var1.getStart()).getFloatValue();
         float var3 = ((FloatKeyframeAnimation)var1.getEnd()).getFloatValue();
         float var4 = ((FloatKeyframeAnimation)var1.getOffset()).getFloatValue();
         applyTrimPathIfNeeded(var0, var2 / 100.0F, var3 / 100.0F, var4 / 360.0F);
      }

   }

   public static void closeQuietly(Closeable var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (RuntimeException var1) {
            throw var1;
         } catch (Exception var2) {
         }
      }

   }

   public static Path createPath(PointF var0, PointF var1, PointF var2, PointF var3) {
      Path var4 = new Path();
      var4.moveTo(var0.x, var0.y);
      if (var2 == null || var3 == null || var2.length() == 0.0F && var3.length() == 0.0F) {
         var4.lineTo(var1.x, var1.y);
      } else {
         float var5 = var0.x;
         float var6 = var2.x;
         float var7 = var0.y;
         float var8 = var2.y;
         float var9 = var1.x;
         float var10 = var3.x;
         float var11 = var1.y;
         var4.cubicTo(var6 + var5, var7 + var8, var9 + var10, var11 + var3.y, var9, var11);
      }

      return var4;
   }

   public static float dpScale() {
      if (dpScale == -1.0F) {
         dpScale = Resources.getSystem().getDisplayMetrics().density;
      }

      return dpScale;
   }

   public static float getAnimationScale(Context var0) {
      return VERSION.SDK_INT >= 17 ? Global.getFloat(var0.getContentResolver(), "animator_duration_scale", 1.0F) : System.getFloat(var0.getContentResolver(), "animator_duration_scale", 1.0F);
   }

   public static float getScale(Matrix var0) {
      float[] var1 = points;
      var1[0] = 0.0F;
      var1[1] = 0.0F;
      float var2 = SQRT_2;
      var1[2] = var2;
      var1[3] = var2;
      var0.mapPoints(var1);
      float[] var6 = points;
      var2 = var6[2];
      float var3 = var6[0];
      float var4 = var6[3];
      float var5 = var6[1];
      return (float)Math.hypot((double)(var2 - var3), (double)(var4 - var5)) / 2.0F;
   }

   public static boolean hasZeroScaleAxis(Matrix var0) {
      float[] var1 = points;
      var1[0] = 0.0F;
      var1[1] = 0.0F;
      var1[2] = 37394.73F;
      var1[3] = 39575.234F;
      var0.mapPoints(var1);
      float[] var2 = points;
      return var2[0] == var2[2] || var2[1] == var2[3];
   }

   public static int hashFor(float var0, float var1, float var2, float var3) {
      int var4;
      if (var0 != 0.0F) {
         var4 = (int)((float)527 * var0);
      } else {
         var4 = 17;
      }

      int var5 = var4;
      if (var1 != 0.0F) {
         var5 = (int)((float)(var4 * 31) * var1);
      }

      var4 = var5;
      if (var2 != 0.0F) {
         var4 = (int)((float)(var5 * 31) * var2);
      }

      var5 = var4;
      if (var3 != 0.0F) {
         var5 = (int)((float)(var4 * 31) * var3);
      }

      return var5;
   }

   public static boolean isAtLeastVersion(int var0, int var1, int var2, int var3, int var4, int var5) {
      boolean var6 = false;
      if (var0 < var3) {
         return false;
      } else if (var0 > var3) {
         return true;
      } else if (var1 < var4) {
         return false;
      } else if (var1 > var4) {
         return true;
      } else {
         if (var2 >= var5) {
            var6 = true;
         }

         return var6;
      }
   }

   public static Bitmap resizeBitmapIfNeeded(Bitmap var0, int var1, int var2) {
      if (var0.getWidth() == var1 && var0.getHeight() == var2) {
         return var0;
      } else {
         var0.getWidth();
         var0.getHeight();
         Bitmap var3 = Bitmap.createScaledBitmap(var0, var1, var2, true);
         var0.recycle();
         return var3;
      }
   }
}
