package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Preconditions;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class TransformationUtils {
   private static final Lock BITMAP_DRAWABLE_LOCK;
   private static final Paint CIRCLE_CROP_BITMAP_PAINT;
   private static final Paint CIRCLE_CROP_SHAPE_PAINT = new Paint(7);
   private static final Paint DEFAULT_PAINT = new Paint(6);
   private static final List MODELS_REQUIRING_BITMAP_LOCK = Arrays.asList("XT1097", "XT1085");

   static {
      Object var0;
      if (MODELS_REQUIRING_BITMAP_LOCK.contains(Build.MODEL) && VERSION.SDK_INT == 22) {
         var0 = new ReentrantLock();
      } else {
         var0 = new TransformationUtils.NoLock();
      }

      BITMAP_DRAWABLE_LOCK = (Lock)var0;
      CIRCLE_CROP_BITMAP_PAINT = new Paint(7);
      CIRCLE_CROP_BITMAP_PAINT.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
   }

   private static void applyMatrix(Bitmap var0, Bitmap var1, Matrix var2) {
      BITMAP_DRAWABLE_LOCK.lock();

      try {
         Canvas var3 = new Canvas(var1);
         var3.drawBitmap(var0, var2, DEFAULT_PAINT);
         clear(var3);
      } finally {
         BITMAP_DRAWABLE_LOCK.unlock();
      }

   }

   public static Bitmap centerCrop(BitmapPool var0, Bitmap var1, int var2, int var3) {
      if (var1.getWidth() == var2 && var1.getHeight() == var3) {
         return var1;
      } else {
         Matrix var4 = new Matrix();
         int var5 = var1.getWidth();
         int var6 = var1.getHeight();
         float var7 = 0.0F;
         float var8;
         float var9;
         if (var5 * var3 > var6 * var2) {
            var8 = (float)var3 / (float)var1.getHeight();
            var9 = ((float)var2 - (float)var1.getWidth() * var8) * 0.5F;
         } else {
            var8 = (float)var2 / (float)var1.getWidth();
            var7 = ((float)var3 - (float)var1.getHeight() * var8) * 0.5F;
            var9 = 0.0F;
         }

         var4.setScale(var8, var8);
         var4.postTranslate((float)((int)(var9 + 0.5F)), (float)((int)(var7 + 0.5F)));
         Bitmap var10 = var0.get(var2, var3, getSafeConfig(var1));
         setAlpha(var1, var10);
         applyMatrix(var1, var10, var4);
         return var10;
      }
   }

   public static Bitmap centerInside(BitmapPool var0, Bitmap var1, int var2, int var3) {
      if (var1.getWidth() <= var2 && var1.getHeight() <= var3) {
         if (Log.isLoggable("TransformationUtils", 2)) {
            Log.v("TransformationUtils", "requested target size larger or equal to input, returning input");
         }

         return var1;
      } else {
         if (Log.isLoggable("TransformationUtils", 2)) {
            Log.v("TransformationUtils", "requested target size too big for input, fit centering instead");
         }

         return fitCenter(var0, var1, var2, var3);
      }
   }

   public static Bitmap circleCrop(BitmapPool var0, Bitmap var1, int var2, int var3) {
      var3 = Math.min(var2, var3);
      float var4 = (float)var3;
      float var5 = var4 / 2.0F;
      var2 = var1.getWidth();
      int var6 = var1.getHeight();
      float var7 = (float)var2;
      float var8 = var4 / var7;
      float var9 = (float)var6;
      var8 = Math.max(var8, var4 / var9);
      var7 *= var8;
      var8 *= var9;
      var9 = (var4 - var7) / 2.0F;
      var4 = (var4 - var8) / 2.0F;
      RectF var10 = new RectF(var9, var4, var7 + var9, var8 + var4);
      Bitmap var11 = getAlphaSafeBitmap(var0, var1);
      Bitmap var12 = var0.get(var3, var3, Config.ARGB_8888);
      var12.setHasAlpha(true);
      BITMAP_DRAWABLE_LOCK.lock();

      try {
         Canvas var13 = new Canvas(var12);
         var13.drawCircle(var5, var5, var5, CIRCLE_CROP_SHAPE_PAINT);
         var13.drawBitmap(var11, (Rect)null, var10, CIRCLE_CROP_BITMAP_PAINT);
         clear(var13);
      } finally {
         BITMAP_DRAWABLE_LOCK.unlock();
      }

      if (!var11.equals(var1)) {
         var0.put(var11);
      }

      return var12;
   }

   private static void clear(Canvas var0) {
      var0.setBitmap((Bitmap)null);
   }

   public static Bitmap fitCenter(BitmapPool var0, Bitmap var1, int var2, int var3) {
      if (var1.getWidth() == var2 && var1.getHeight() == var3) {
         if (Log.isLoggable("TransformationUtils", 2)) {
            Log.v("TransformationUtils", "requested target size matches input, returning input");
         }

         return var1;
      } else {
         float var4 = Math.min((float)var2 / (float)var1.getWidth(), (float)var3 / (float)var1.getHeight());
         int var5 = Math.round((float)var1.getWidth() * var4);
         int var6 = Math.round((float)var1.getHeight() * var4);
         if (var1.getWidth() == var5 && var1.getHeight() == var6) {
            if (Log.isLoggable("TransformationUtils", 2)) {
               Log.v("TransformationUtils", "adjusted target size matches input, returning input");
            }

            return var1;
         } else {
            Bitmap var8 = var0.get((int)((float)var1.getWidth() * var4), (int)((float)var1.getHeight() * var4), getSafeConfig(var1));
            setAlpha(var1, var8);
            if (Log.isLoggable("TransformationUtils", 2)) {
               StringBuilder var7 = new StringBuilder();
               var7.append("request: ");
               var7.append(var2);
               var7.append("x");
               var7.append(var3);
               Log.v("TransformationUtils", var7.toString());
               var7 = new StringBuilder();
               var7.append("toFit:   ");
               var7.append(var1.getWidth());
               var7.append("x");
               var7.append(var1.getHeight());
               Log.v("TransformationUtils", var7.toString());
               var7 = new StringBuilder();
               var7.append("toReuse: ");
               var7.append(var8.getWidth());
               var7.append("x");
               var7.append(var8.getHeight());
               Log.v("TransformationUtils", var7.toString());
               var7 = new StringBuilder();
               var7.append("minPct:   ");
               var7.append(var4);
               Log.v("TransformationUtils", var7.toString());
            }

            Matrix var9 = new Matrix();
            var9.setScale(var4, var4);
            applyMatrix(var1, var8, var9);
            return var8;
         }
      }
   }

   private static Bitmap getAlphaSafeBitmap(BitmapPool var0, Bitmap var1) {
      if (Config.ARGB_8888.equals(var1.getConfig())) {
         return var1;
      } else {
         Bitmap var2 = var0.get(var1.getWidth(), var1.getHeight(), Config.ARGB_8888);
         (new Canvas(var2)).drawBitmap(var1, 0.0F, 0.0F, (Paint)null);
         return var2;
      }
   }

   public static Lock getBitmapDrawableLock() {
      return BITMAP_DRAWABLE_LOCK;
   }

   public static int getExifOrientationDegrees(int var0) {
      short var1;
      switch(var0) {
      case 3:
      case 4:
         var1 = 180;
         break;
      case 5:
      case 6:
         var1 = 90;
         break;
      case 7:
      case 8:
         var1 = 270;
         break;
      default:
         var1 = 0;
      }

      return var1;
   }

   private static Config getSafeConfig(Bitmap var0) {
      Config var1;
      if (var0.getConfig() != null) {
         var1 = var0.getConfig();
      } else {
         var1 = Config.ARGB_8888;
      }

      return var1;
   }

   static void initializeMatrixForRotation(int var0, Matrix var1) {
      switch(var0) {
      case 2:
         var1.setScale(-1.0F, 1.0F);
         break;
      case 3:
         var1.setRotate(180.0F);
         break;
      case 4:
         var1.setRotate(180.0F);
         var1.postScale(-1.0F, 1.0F);
         break;
      case 5:
         var1.setRotate(90.0F);
         var1.postScale(-1.0F, 1.0F);
         break;
      case 6:
         var1.setRotate(90.0F);
         break;
      case 7:
         var1.setRotate(-90.0F);
         var1.postScale(-1.0F, 1.0F);
         break;
      case 8:
         var1.setRotate(-90.0F);
      }

   }

   public static boolean isExifOrientationRequired(int var0) {
      switch(var0) {
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
         return true;
      default:
         return false;
      }
   }

   public static Bitmap rotateImageExif(BitmapPool var0, Bitmap var1, int var2) {
      if (!isExifOrientationRequired(var2)) {
         return var1;
      } else {
         Matrix var3 = new Matrix();
         initializeMatrixForRotation(var2, var3);
         RectF var4 = new RectF(0.0F, 0.0F, (float)var1.getWidth(), (float)var1.getHeight());
         var3.mapRect(var4);
         Bitmap var5 = var0.get(Math.round(var4.width()), Math.round(var4.height()), getSafeConfig(var1));
         var3.postTranslate(-var4.left, -var4.top);
         applyMatrix(var1, var5, var3);
         return var5;
      }
   }

   public static Bitmap roundedCorners(BitmapPool var0, Bitmap var1, int var2, int var3, int var4) {
      boolean var5;
      if (var2 > 0) {
         var5 = true;
      } else {
         var5 = false;
      }

      Preconditions.checkArgument(var5, "width must be greater than 0.");
      if (var3 > 0) {
         var5 = true;
      } else {
         var5 = false;
      }

      Preconditions.checkArgument(var5, "height must be greater than 0.");
      if (var4 > 0) {
         var5 = true;
      } else {
         var5 = false;
      }

      Preconditions.checkArgument(var5, "roundingRadius must be greater than 0.");
      Bitmap var6 = getAlphaSafeBitmap(var0, var1);
      Bitmap var7 = var0.get(var2, var3, Config.ARGB_8888);
      var7.setHasAlpha(true);
      BitmapShader var8 = new BitmapShader(var6, TileMode.CLAMP, TileMode.CLAMP);
      Paint var9 = new Paint();
      var9.setAntiAlias(true);
      var9.setShader(var8);
      RectF var10 = new RectF(0.0F, 0.0F, (float)var7.getWidth(), (float)var7.getHeight());
      BITMAP_DRAWABLE_LOCK.lock();

      label125: {
         Throwable var10000;
         label129: {
            boolean var10001;
            Canvas var19;
            try {
               var19 = new Canvas(var7);
               var19.drawColor(0, Mode.CLEAR);
            } catch (Throwable var17) {
               var10000 = var17;
               var10001 = false;
               break label129;
            }

            float var11 = (float)var4;

            label120:
            try {
               var19.drawRoundRect(var10, var11, var11, var9);
               clear(var19);
               break label125;
            } catch (Throwable var16) {
               var10000 = var16;
               var10001 = false;
               break label120;
            }
         }

         Throwable var18 = var10000;
         BITMAP_DRAWABLE_LOCK.unlock();
         throw var18;
      }

      BITMAP_DRAWABLE_LOCK.unlock();
      if (!var6.equals(var1)) {
         var0.put(var6);
      }

      return var7;
   }

   public static void setAlpha(Bitmap var0, Bitmap var1) {
      var1.setHasAlpha(var0.hasAlpha());
   }

   private static final class NoLock implements Lock {
      NoLock() {
      }

      public void lock() {
      }

      public void lockInterruptibly() throws InterruptedException {
      }

      public Condition newCondition() {
         throw new UnsupportedOperationException("Should not be called");
      }

      public boolean tryLock() {
         return true;
      }

      public boolean tryLock(long var1, TimeUnit var3) throws InterruptedException {
         return true;
      }

      public void unlock() {
      }
   }
}
