package com.google.zxing.common;

import com.google.zxing.Binarizer;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;

public class GlobalHistogramBinarizer extends Binarizer {
   private static final byte[] EMPTY = new byte[0];
   private static final int LUMINANCE_BITS = 5;
   private static final int LUMINANCE_BUCKETS = 32;
   private static final int LUMINANCE_SHIFT = 3;
   private final int[] buckets;
   private byte[] luminances;

   public GlobalHistogramBinarizer(LuminanceSource var1) {
      super(var1);
      this.luminances = EMPTY;
      this.buckets = new int[32];
   }

   private static int estimateBlackPoint(int[] var0) throws NotFoundException {
      int var1 = var0.length;
      int var2 = 0;
      int var3 = 0;
      int var4 = 0;

      int var5;
      int var6;
      int var7;
      for(var5 = 0; var5 < var1; var2 = var7) {
         var6 = var4;
         if (var0[var5] > var4) {
            var3 = var5;
            var6 = var0[var5];
         }

         var7 = var2;
         if (var0[var5] > var2) {
            var7 = var0[var5];
         }

         ++var5;
         var4 = var6;
      }

      var5 = 0;
      var4 = 0;

      int var8;
      for(var6 = 0; var6 < var1; var4 = var7) {
         var7 = var6 - var3;
         var8 = var0[var6] * var7 * var7;
         var7 = var4;
         if (var8 > var4) {
            var5 = var6;
            var7 = var8;
         }

         ++var6;
      }

      var4 = var3;
      var6 = var5;
      if (var3 > var5) {
         var6 = var3;
         var4 = var5;
      }

      if (var6 - var4 <= var1 / 16) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         var8 = var6 - 1;
         var5 = -1;

         for(var3 = var6 - 1; var3 > var4; var5 = var7) {
            var7 = var3 - var4;
            var1 = var7 * var7 * (var6 - var3) * (var2 - var0[var3]);
            var7 = var5;
            if (var1 > var5) {
               var8 = var3;
               var7 = var1;
            }

            --var3;
         }

         return var8 << 3;
      }
   }

   private void initArrays(int var1) {
      if (this.luminances.length < var1) {
         this.luminances = new byte[var1];
      }

      for(var1 = 0; var1 < 32; ++var1) {
         this.buckets[var1] = 0;
      }

   }

   public Binarizer createBinarizer(LuminanceSource var1) {
      return new GlobalHistogramBinarizer(var1);
   }

   public BitMatrix getBlackMatrix() throws NotFoundException {
      LuminanceSource var1 = this.getLuminanceSource();
      int var2 = var1.getWidth();
      int var3 = var1.getHeight();
      BitMatrix var4 = new BitMatrix(var2, var3);
      this.initArrays(var2);
      int[] var5 = this.buckets;

      int var6;
      byte[] var7;
      int var8;
      int var9;
      for(var6 = 1; var6 < 5; ++var6) {
         var7 = var1.getRow(var3 * var6 / 5, this.luminances);
         var8 = (var2 << 2) / 5;

         for(var9 = var2 / 5; var9 < var8; ++var9) {
            int var10 = (var7[var9] & 255) >> 3;
            int var10002 = var5[var10]++;
         }
      }

      var8 = estimateBlackPoint(var5);
      var7 = var1.getMatrix();

      for(var6 = 0; var6 < var3; ++var6) {
         for(var9 = 0; var9 < var2; ++var9) {
            if ((var7[var6 * var2 + var9] & 255) < var8) {
               var4.set(var9, var6);
            }
         }
      }

      return var4;
   }

   public BitArray getBlackRow(int var1, BitArray var2) throws NotFoundException {
      LuminanceSource var3 = this.getLuminanceSource();
      int var4 = var3.getWidth();
      if (var2 != null && var2.getSize() >= var4) {
         var2.clear();
      } else {
         var2 = new BitArray(var4);
      }

      this.initArrays(var4);
      byte[] var5 = var3.getRow(var1, this.luminances);
      int[] var10 = this.buckets;

      int var6;
      for(var1 = 0; var1 < var4; ++var1) {
         var6 = (var5[var1] & 255) >> 3;
         int var10002 = var10[var6]++;
      }

      int var7 = estimateBlackPoint(var10);
      if (var4 < 3) {
         for(var1 = 0; var1 < var4; ++var1) {
            if ((var5[var1] & 255) < var7) {
               var2.set(var1);
            }
         }
      } else {
         int var8 = var5[0] & 255;
         var1 = var5[1] & 255;

         for(var6 = 1; var6 < var4 - 1; ++var6) {
            int var9 = var5[var6 + 1] & 255;
            if (((var1 << 2) - var8 - var9) / 2 < var7) {
               var2.set(var6);
            }

            var8 = var1;
            var1 = var9;
         }
      }

      return var2;
   }
}
