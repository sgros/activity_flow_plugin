package com.bumptech.glide.load.resource.bitmap;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import android.util.Log;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.ImageHeaderParserUtils;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public final class Downsampler {
   public static final Option ALLOW_HARDWARE_CONFIG;
   public static final Option DECODE_FORMAT;
   public static final Option DOWNSAMPLE_STRATEGY;
   private static final Downsampler.DecodeCallbacks EMPTY_CALLBACKS;
   public static final Option FIX_BITMAP_SIZE_TO_REQUESTED_DIMENSIONS;
   private static final Set NO_DOWNSAMPLE_PRE_N_MIME_TYPES;
   private static final Queue OPTIONS_QUEUE;
   private static final Set TYPES_THAT_USE_POOL_PRE_KITKAT;
   private final BitmapPool bitmapPool;
   private final ArrayPool byteArrayPool;
   private final DisplayMetrics displayMetrics;
   private final HardwareConfigState hardwareConfigState = HardwareConfigState.getInstance();
   private final List parsers;

   static {
      DECODE_FORMAT = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.DecodeFormat", DecodeFormat.DEFAULT);
      DOWNSAMPLE_STRATEGY = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.DownsampleStrategy", DownsampleStrategy.AT_LEAST);
      FIX_BITMAP_SIZE_TO_REQUESTED_DIMENSIONS = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.FixBitmapSize", false);
      ALLOW_HARDWARE_CONFIG = Option.memory("com.bumtpech.glide.load.resource.bitmap.Downsampler.AllowHardwareDecode", (Object)null);
      NO_DOWNSAMPLE_PRE_N_MIME_TYPES = Collections.unmodifiableSet(new HashSet(Arrays.asList("image/vnd.wap.wbmp", "image/x-ico")));
      EMPTY_CALLBACKS = new Downsampler.DecodeCallbacks() {
         public void onDecodeComplete(BitmapPool var1, Bitmap var2) throws IOException {
         }

         public void onObtainBounds() {
         }
      };
      TYPES_THAT_USE_POOL_PRE_KITKAT = Collections.unmodifiableSet(EnumSet.of(ImageHeaderParser.ImageType.JPEG, ImageHeaderParser.ImageType.PNG_A, ImageHeaderParser.ImageType.PNG));
      OPTIONS_QUEUE = Util.createQueue(0);
   }

   public Downsampler(List var1, DisplayMetrics var2, BitmapPool var3, ArrayPool var4) {
      this.parsers = var1;
      this.displayMetrics = (DisplayMetrics)Preconditions.checkNotNull(var2);
      this.bitmapPool = (BitmapPool)Preconditions.checkNotNull(var3);
      this.byteArrayPool = (ArrayPool)Preconditions.checkNotNull(var4);
   }

   private static int adjustTargetDensityForError(double var0) {
      int var2 = round(1.0E9D * var0);
      return round(var0 / (double)((float)var2 / 1.0E9F) * (double)var2);
   }

   private void calculateConfig(InputStream var1, DecodeFormat var2, boolean var3, boolean var4, Options var5, int var6, int var7) throws IOException {
      if (!this.hardwareConfigState.setHardwareConfigIfAllowed(var6, var7, var5, var2, var3, var4)) {
         if (var2 != DecodeFormat.PREFER_ARGB_8888 && var2 != DecodeFormat.PREFER_ARGB_8888_DISALLOW_HARDWARE && VERSION.SDK_INT != 16) {
            try {
               var3 = ImageHeaderParserUtils.getType(this.parsers, var1, this.byteArrayPool).hasAlpha();
            } catch (IOException var9) {
               if (Log.isLoggable("Downsampler", 3)) {
                  StringBuilder var10 = new StringBuilder();
                  var10.append("Cannot determine whether the image has alpha or not from header, format ");
                  var10.append(var2);
                  Log.d("Downsampler", var10.toString(), var9);
               }

               var3 = false;
            }

            Config var11;
            if (var3) {
               var11 = Config.ARGB_8888;
            } else {
               var11 = Config.RGB_565;
            }

            var5.inPreferredConfig = var11;
            if (var5.inPreferredConfig == Config.RGB_565 || var5.inPreferredConfig == Config.ARGB_4444 || var5.inPreferredConfig == Config.ALPHA_8) {
               var5.inDither = true;
            }

         } else {
            var5.inPreferredConfig = Config.ARGB_8888;
         }
      }
   }

   static void calculateScaling(ImageHeaderParser.ImageType var0, InputStream var1, Downsampler.DecodeCallbacks var2, BitmapPool var3, DownsampleStrategy var4, int var5, int var6, int var7, int var8, int var9, Options var10) throws IOException {
      if (var6 > 0 && var7 > 0) {
         float var11;
         if (var5 != 90 && var5 != 270) {
            var11 = var4.getScaleFactor(var6, var7, var8, var9);
         } else {
            var11 = var4.getScaleFactor(var7, var6, var8, var9);
         }

         StringBuilder var24;
         if (var11 <= 0.0F) {
            var24 = new StringBuilder();
            var24.append("Cannot scale with factor: ");
            var24.append(var11);
            var24.append(" from: ");
            var24.append(var4);
            var24.append(", source: [");
            var24.append(var6);
            var24.append("x");
            var24.append(var7);
            var24.append("], target: [");
            var24.append(var8);
            var24.append("x");
            var24.append(var9);
            var24.append("]");
            throw new IllegalArgumentException(var24.toString());
         } else {
            DownsampleStrategy.SampleSizeRounding var12 = var4.getSampleSizeRounding(var6, var7, var8, var9);
            if (var12 != null) {
               float var13 = (float)var6;
               var5 = round((double)(var11 * var13));
               float var14 = (float)var7;
               int var15 = round((double)(var11 * var14));
               var5 = var6 / var5;
               var15 = var7 / var15;
               if (var12 == DownsampleStrategy.SampleSizeRounding.MEMORY) {
                  var5 = Math.max(var5, var15);
               } else {
                  var5 = Math.min(var5, var15);
               }

               int var16;
               if (VERSION.SDK_INT <= 23 && NO_DOWNSAMPLE_PRE_N_MIME_TYPES.contains(var10.outMimeType)) {
                  var16 = 1;
               } else {
                  var16 = Math.max(1, Integer.highestOneBit(var5));
                  if (var12 == DownsampleStrategy.SampleSizeRounding.MEMORY && (float)var16 < 1.0F / var11) {
                     var16 <<= 1;
                  }
               }

               var10.inSampleSize = var16;
               float var17;
               if (var0 == ImageHeaderParser.ImageType.JPEG) {
                  var17 = (float)Math.min(var16, 8);
                  int var18 = (int)Math.ceil((double)(var13 / var17));
                  int var19 = (int)Math.ceil((double)(var14 / var17));
                  int var20 = var16 / 8;
                  var5 = var19;
                  var15 = var18;
                  if (var20 > 0) {
                     var15 = var18 / var20;
                     var5 = var19 / var20;
                  }
               } else if (var0 != ImageHeaderParser.ImageType.PNG && var0 != ImageHeaderParser.ImageType.PNG_A) {
                  if (var0 != ImageHeaderParser.ImageType.WEBP && var0 != ImageHeaderParser.ImageType.WEBP_A) {
                     if (var6 % var16 == 0 && var7 % var16 == 0) {
                        var15 = var6 / var16;
                        var5 = var7 / var16;
                     } else {
                        int[] var23 = getDimensions(var1, var10, var2, var3);
                        var15 = var23[0];
                        var5 = var23[1];
                     }
                  } else if (VERSION.SDK_INT >= 24) {
                     var17 = (float)var16;
                     var15 = Math.round(var13 / var17);
                     var5 = Math.round(var14 / var17);
                  } else {
                     var17 = (float)var16;
                     var15 = (int)Math.floor((double)(var13 / var17));
                     var5 = (int)Math.floor((double)(var14 / var17));
                  }
               } else {
                  var17 = (float)var16;
                  var15 = (int)Math.floor((double)(var13 / var17));
                  var5 = (int)Math.floor((double)(var14 / var17));
               }

               double var21 = (double)var4.getScaleFactor(var15, var5, var8, var9);
               if (VERSION.SDK_INT >= 19) {
                  var10.inTargetDensity = adjustTargetDensityForError(var21);
                  var10.inDensity = 1000000000;
               }

               if (isScaling(var10)) {
                  var10.inScaled = true;
               } else {
                  var10.inTargetDensity = 0;
                  var10.inDensity = 0;
               }

               if (Log.isLoggable("Downsampler", 2)) {
                  var24 = new StringBuilder();
                  var24.append("Calculate scaling, source: [");
                  var24.append(var6);
                  var24.append("x");
                  var24.append(var7);
                  var24.append("], target: [");
                  var24.append(var8);
                  var24.append("x");
                  var24.append(var9);
                  var24.append("], power of two scaled: [");
                  var24.append(var15);
                  var24.append("x");
                  var24.append(var5);
                  var24.append("], exact scale factor: ");
                  var24.append(var11);
                  var24.append(", power of 2 sample size: ");
                  var24.append(var16);
                  var24.append(", adjusted scale factor: ");
                  var24.append(var21);
                  var24.append(", target density: ");
                  var24.append(var10.inTargetDensity);
                  var24.append(", density: ");
                  var24.append(var10.inDensity);
                  Log.v("Downsampler", var24.toString());
               }

            } else {
               throw new IllegalArgumentException("Cannot round with null rounding");
            }
         }
      }
   }

   private Bitmap decodeFromWrappedStreams(InputStream var1, Options var2, DownsampleStrategy var3, DecodeFormat var4, boolean var5, int var6, int var7, boolean var8, Downsampler.DecodeCallbacks var9) throws IOException {
      long var10 = LogTime.getLogTime();
      int[] var12 = getDimensions(var1, var2, var9, this.bitmapPool);
      boolean var13 = false;
      int var14 = var12[0];
      int var15 = var12[1];
      String var30 = var2.outMimeType;
      if (var14 == -1 || var15 == -1) {
         var5 = false;
      }

      int var16 = ImageHeaderParserUtils.getOrientation(this.parsers, var1, this.byteArrayPool);
      int var17 = TransformationUtils.getExifOrientationDegrees(var16);
      boolean var18 = TransformationUtils.isExifOrientationRequired(var16);
      int var19;
      if (var6 == Integer.MIN_VALUE) {
         var19 = var14;
      } else {
         var19 = var6;
      }

      int var20 = var7;
      if (var7 == Integer.MIN_VALUE) {
         var20 = var15;
      }

      ImageHeaderParser.ImageType var21 = ImageHeaderParserUtils.getType(this.parsers, var1, this.byteArrayPool);
      calculateScaling(var21, var1, var9, this.bitmapPool, var3, var17, var14, var15, var19, var20, var2);
      this.calculateConfig(var1, var4, var5, var18, var2, var19, var20);
      if (VERSION.SDK_INT >= 19) {
         var13 = true;
      }

      if ((var2.inSampleSize == 1 || var13) && this.shouldUsePool(var21)) {
         if (!var8 || !var13) {
            float var22;
            if (isScaling(var2)) {
               var22 = (float)var2.inTargetDensity / (float)var2.inDensity;
            } else {
               var22 = 1.0F;
            }

            int var23 = var2.inSampleSize;
            float var24 = (float)var14;
            float var25 = (float)var23;
            var20 = (int)Math.ceil((double)(var24 / var25));
            var19 = (int)Math.ceil((double)((float)var15 / var25));
            int var31 = Math.round((float)var20 * var22);
            var17 = Math.round((float)var19 * var22);
            var19 = var31;
            var20 = var17;
            if (Log.isLoggable("Downsampler", 2)) {
               StringBuilder var28 = new StringBuilder();
               var28.append("Calculated target [");
               var28.append(var31);
               var28.append("x");
               var28.append(var17);
               var28.append("] for source [");
               var28.append(var14);
               var28.append("x");
               var28.append(var15);
               var28.append("], sampleSize: ");
               var28.append(var23);
               var28.append(", targetDensity: ");
               var28.append(var2.inTargetDensity);
               var28.append(", density: ");
               var28.append(var2.inDensity);
               var28.append(", density multiplier: ");
               var28.append(var22);
               Log.v("Downsampler", var28.toString());
               var20 = var17;
               var19 = var31;
            }
         }

         if (var19 > 0 && var20 > 0) {
            setInBitmap(var2, this.bitmapPool, var19, var20);
         }
      }

      Bitmap var29 = decodeStream(var1, var2, var9, this.bitmapPool);
      var9.onDecodeComplete(this.bitmapPool, var29);
      if (Log.isLoggable("Downsampler", 2)) {
         logDecode(var14, var15, var30, var2, var29, var6, var7, var10);
      }

      Bitmap var26 = null;
      if (var29 != null) {
         var29.setDensity(this.displayMetrics.densityDpi);
         Bitmap var27 = TransformationUtils.rotateImageExif(this.bitmapPool, var29, var16);
         var26 = var27;
         if (!var29.equals(var27)) {
            this.bitmapPool.put(var29);
            var26 = var27;
         }
      }

      return var26;
   }

   private static Bitmap decodeStream(InputStream param0, Options param1, Downsampler.DecodeCallbacks param2, BitmapPool param3) throws IOException {
      // $FF: Couldn't be decompiled
   }

   @TargetApi(19)
   private static String getBitmapString(Bitmap var0) {
      if (var0 == null) {
         return null;
      } else {
         String var3;
         if (VERSION.SDK_INT >= 19) {
            StringBuilder var1 = new StringBuilder();
            var1.append(" (");
            var1.append(var0.getAllocationByteCount());
            var1.append(")");
            var3 = var1.toString();
         } else {
            var3 = "";
         }

         StringBuilder var2 = new StringBuilder();
         var2.append("[");
         var2.append(var0.getWidth());
         var2.append("x");
         var2.append(var0.getHeight());
         var2.append("] ");
         var2.append(var0.getConfig());
         var2.append(var3);
         return var2.toString();
      }
   }

   private static Options getDefaultOptions() {
      // $FF: Couldn't be decompiled
   }

   private static int[] getDimensions(InputStream var0, Options var1, Downsampler.DecodeCallbacks var2, BitmapPool var3) throws IOException {
      var1.inJustDecodeBounds = true;
      decodeStream(var0, var1, var2, var3);
      var1.inJustDecodeBounds = false;
      return new int[]{var1.outWidth, var1.outHeight};
   }

   private static String getInBitmapString(Options var0) {
      return getBitmapString(var0.inBitmap);
   }

   private static boolean isScaling(Options var0) {
      boolean var1;
      if (var0.inTargetDensity > 0 && var0.inDensity > 0 && var0.inTargetDensity != var0.inDensity) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private static void logDecode(int var0, int var1, String var2, Options var3, Bitmap var4, int var5, int var6, long var7) {
      StringBuilder var9 = new StringBuilder();
      var9.append("Decoded ");
      var9.append(getBitmapString(var4));
      var9.append(" from [");
      var9.append(var0);
      var9.append("x");
      var9.append(var1);
      var9.append("] ");
      var9.append(var2);
      var9.append(" with inBitmap ");
      var9.append(getInBitmapString(var3));
      var9.append(" for [");
      var9.append(var5);
      var9.append("x");
      var9.append(var6);
      var9.append("], sample size: ");
      var9.append(var3.inSampleSize);
      var9.append(", density: ");
      var9.append(var3.inDensity);
      var9.append(", target density: ");
      var9.append(var3.inTargetDensity);
      var9.append(", thread: ");
      var9.append(Thread.currentThread().getName());
      var9.append(", duration: ");
      var9.append(LogTime.getElapsedMillis(var7));
      Log.v("Downsampler", var9.toString());
   }

   private static IOException newIoExceptionForInBitmapAssertion(IllegalArgumentException var0, int var1, int var2, String var3, Options var4) {
      StringBuilder var5 = new StringBuilder();
      var5.append("Exception decoding bitmap, outWidth: ");
      var5.append(var1);
      var5.append(", outHeight: ");
      var5.append(var2);
      var5.append(", outMimeType: ");
      var5.append(var3);
      var5.append(", inBitmap: ");
      var5.append(getInBitmapString(var4));
      return new IOException(var5.toString(), var0);
   }

   private static void releaseOptions(Options param0) {
      // $FF: Couldn't be decompiled
   }

   private static void resetOptions(Options var0) {
      var0.inTempStorage = null;
      var0.inDither = false;
      var0.inScaled = false;
      var0.inSampleSize = 1;
      var0.inPreferredConfig = null;
      var0.inJustDecodeBounds = false;
      var0.inDensity = 0;
      var0.inTargetDensity = 0;
      var0.outWidth = 0;
      var0.outHeight = 0;
      var0.outMimeType = null;
      var0.inBitmap = null;
      var0.inMutable = true;
   }

   private static int round(double var0) {
      return (int)(var0 + 0.5D);
   }

   @TargetApi(26)
   private static void setInBitmap(Options var0, BitmapPool var1, int var2, int var3) {
      if (VERSION.SDK_INT < 26 || var0.inPreferredConfig != Config.HARDWARE) {
         var0.inBitmap = var1.getDirty(var2, var3, var0.inPreferredConfig);
      }
   }

   private boolean shouldUsePool(ImageHeaderParser.ImageType var1) throws IOException {
      return VERSION.SDK_INT >= 19 ? true : TYPES_THAT_USE_POOL_PRE_KITKAT.contains(var1);
   }

   public Resource decode(InputStream var1, int var2, int var3, com.bumptech.glide.load.Options var4) throws IOException {
      return this.decode(var1, var2, var3, var4, EMPTY_CALLBACKS);
   }

   public Resource decode(InputStream var1, int var2, int var3, com.bumptech.glide.load.Options var4, Downsampler.DecodeCallbacks var5) throws IOException {
      Preconditions.checkArgument(var1.markSupported(), "You must provide an InputStream that supports mark()");
      byte[] var6 = (byte[])this.byteArrayPool.get(65536, byte[].class);
      Options var7 = getDefaultOptions();
      var7.inTempStorage = var6;
      DecodeFormat var8 = (DecodeFormat)var4.get(DECODE_FORMAT);
      DownsampleStrategy var9 = (DownsampleStrategy)var4.get(DOWNSAMPLE_STRATEGY);
      boolean var10 = (Boolean)var4.get(FIX_BITMAP_SIZE_TO_REQUESTED_DIMENSIONS);
      boolean var11;
      if (var4.get(ALLOW_HARDWARE_CONFIG) != null && (Boolean)var4.get(ALLOW_HARDWARE_CONFIG)) {
         var11 = true;
      } else {
         var11 = false;
      }

      if (var8 == DecodeFormat.PREFER_ARGB_8888_DISALLOW_HARDWARE) {
         var11 = false;
      }

      BitmapResource var14;
      try {
         var14 = BitmapResource.obtain(this.decodeFromWrappedStreams(var1, var7, var9, var8, var11, var2, var3, var10, var5), this.bitmapPool);
      } finally {
         releaseOptions(var7);
         this.byteArrayPool.put(var6, byte[].class);
      }

      return var14;
   }

   public boolean handles(InputStream var1) {
      return true;
   }

   public boolean handles(ByteBuffer var1) {
      return true;
   }

   public interface DecodeCallbacks {
      void onDecodeComplete(BitmapPool var1, Bitmap var2) throws IOException;

      void onObtainBounds();
   }
}
