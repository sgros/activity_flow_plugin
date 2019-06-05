package com.bumptech.glide.load.engine.cache;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build.VERSION;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;

public final class MemorySizeCalculator {
   private final int arrayPoolSize;
   private final int bitmapPoolSize;
   private final Context context;
   private final int memoryCacheSize;

   MemorySizeCalculator(MemorySizeCalculator.Builder var1) {
      this.context = var1.context;
      int var2;
      if (isLowMemoryDevice(var1.activityManager)) {
         var2 = var1.arrayPoolSizeBytes / 2;
      } else {
         var2 = var1.arrayPoolSizeBytes;
      }

      this.arrayPoolSize = var2;
      int var3 = getMaxSize(var1.activityManager, var1.maxSizeMultiplier, var1.lowMemoryMaxSizeMultiplier);
      float var4 = (float)(var1.screenDimensions.getWidthPixels() * var1.screenDimensions.getHeightPixels() * 4);
      int var5 = Math.round(var1.bitmapPoolScreens * var4);
      int var6 = Math.round(var4 * var1.memoryCacheScreens);
      int var7 = var3 - this.arrayPoolSize;
      var2 = var6 + var5;
      if (var2 <= var7) {
         this.memoryCacheSize = var6;
         this.bitmapPoolSize = var5;
      } else {
         var4 = (float)var7 / (var1.bitmapPoolScreens + var1.memoryCacheScreens);
         this.memoryCacheSize = Math.round(var1.memoryCacheScreens * var4);
         this.bitmapPoolSize = Math.round(var4 * var1.bitmapPoolScreens);
      }

      if (Log.isLoggable("MemorySizeCalculator", 3)) {
         StringBuilder var8 = new StringBuilder();
         var8.append("Calculation complete, Calculated memory cache size: ");
         var8.append(this.toMb(this.memoryCacheSize));
         var8.append(", pool size: ");
         var8.append(this.toMb(this.bitmapPoolSize));
         var8.append(", byte array size: ");
         var8.append(this.toMb(this.arrayPoolSize));
         var8.append(", memory class limited? ");
         boolean var9;
         if (var2 > var3) {
            var9 = true;
         } else {
            var9 = false;
         }

         var8.append(var9);
         var8.append(", max size: ");
         var8.append(this.toMb(var3));
         var8.append(", memoryClass: ");
         var8.append(var1.activityManager.getMemoryClass());
         var8.append(", isLowMemoryDevice: ");
         var8.append(isLowMemoryDevice(var1.activityManager));
         Log.d("MemorySizeCalculator", var8.toString());
      }

   }

   private static int getMaxSize(ActivityManager var0, float var1, float var2) {
      int var3 = var0.getMemoryClass();
      boolean var4 = isLowMemoryDevice(var0);
      float var5 = (float)(var3 * 1024 * 1024);
      if (var4) {
         var1 = var2;
      }

      return Math.round(var5 * var1);
   }

   private static boolean isLowMemoryDevice(ActivityManager var0) {
      return VERSION.SDK_INT >= 19 ? var0.isLowRamDevice() : false;
   }

   private String toMb(int var1) {
      return Formatter.formatFileSize(this.context, (long)var1);
   }

   public int getArrayPoolSizeInBytes() {
      return this.arrayPoolSize;
   }

   public int getBitmapPoolSize() {
      return this.bitmapPoolSize;
   }

   public int getMemoryCacheSize() {
      return this.memoryCacheSize;
   }

   public static final class Builder {
      static final int BITMAP_POOL_TARGET_SCREENS;
      private ActivityManager activityManager;
      private int arrayPoolSizeBytes;
      private float bitmapPoolScreens;
      private final Context context;
      private float lowMemoryMaxSizeMultiplier;
      private float maxSizeMultiplier;
      private float memoryCacheScreens = 2.0F;
      private MemorySizeCalculator.ScreenDimensions screenDimensions;

      static {
         byte var0;
         if (VERSION.SDK_INT < 26) {
            var0 = 4;
         } else {
            var0 = 1;
         }

         BITMAP_POOL_TARGET_SCREENS = var0;
      }

      public Builder(Context var1) {
         this.bitmapPoolScreens = (float)BITMAP_POOL_TARGET_SCREENS;
         this.maxSizeMultiplier = 0.4F;
         this.lowMemoryMaxSizeMultiplier = 0.33F;
         this.arrayPoolSizeBytes = 4194304;
         this.context = var1;
         this.activityManager = (ActivityManager)var1.getSystemService("activity");
         this.screenDimensions = new MemorySizeCalculator.DisplayMetricsScreenDimensions(var1.getResources().getDisplayMetrics());
         if (VERSION.SDK_INT >= 26 && MemorySizeCalculator.isLowMemoryDevice(this.activityManager)) {
            this.bitmapPoolScreens = 0.0F;
         }

      }

      public MemorySizeCalculator build() {
         return new MemorySizeCalculator(this);
      }
   }

   private static final class DisplayMetricsScreenDimensions implements MemorySizeCalculator.ScreenDimensions {
      private final DisplayMetrics displayMetrics;

      public DisplayMetricsScreenDimensions(DisplayMetrics var1) {
         this.displayMetrics = var1;
      }

      public int getHeightPixels() {
         return this.displayMetrics.heightPixels;
      }

      public int getWidthPixels() {
         return this.displayMetrics.widthPixels;
      }
   }

   interface ScreenDimensions {
      int getHeightPixels();

      int getWidthPixels();
   }
}
