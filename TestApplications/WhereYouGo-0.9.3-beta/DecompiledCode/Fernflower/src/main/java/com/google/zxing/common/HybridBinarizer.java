package com.google.zxing.common;

import com.google.zxing.Binarizer;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;

public final class HybridBinarizer extends GlobalHistogramBinarizer {
   private static final int BLOCK_SIZE = 8;
   private static final int BLOCK_SIZE_MASK = 7;
   private static final int BLOCK_SIZE_POWER = 3;
   private static final int MINIMUM_DIMENSION = 40;
   private static final int MIN_DYNAMIC_RANGE = 24;
   private BitMatrix matrix;

   public HybridBinarizer(LuminanceSource var1) {
      super(var1);
   }

   private static int[][] calculateBlackPoints(byte[] var0, int var1, int var2, int var3, int var4) {
      int[][] var5 = new int[var2][var1];

      for(int var6 = 0; var6 < var2; ++var6) {
         int var7 = var6 << 3;
         int var8 = var4 - 8;
         int var9 = var7;
         if (var7 > var8) {
            var9 = var8;
         }

         for(int var10 = 0; var10 < var1; ++var10) {
            int var11 = var10 << 3;
            var7 = var3 - 8;
            var8 = var11;
            if (var11 > var7) {
               var8 = var7;
            }

            byte var12 = 0;
            int var13 = 255;
            int var14 = 0;
            var7 = 0;
            var11 = var9 * var3 + var8;

            int var17;
            for(var8 = var12; var7 < 8; var8 = var17) {
               int var15;
               int var16;
               for(var17 = 0; var17 < 8; var8 = var16) {
                  var15 = var0[var11 + var17] & 255;
                  var16 = var8 + var15;
                  var8 = var13;
                  if (var15 < var13) {
                     var8 = var15;
                  }

                  var13 = var14;
                  if (var15 > var14) {
                     var13 = var15;
                  }

                  ++var17;
                  var14 = var13;
                  var13 = var8;
               }

               var15 = var11;
               var17 = var8;
               var16 = var7;
               if (var14 - var13 > 24) {
                  ++var7;
                  var17 = var11 + var3;
                  var11 = var8;
                  var8 = var17;

                  while(true) {
                     var15 = var8;
                     var17 = var11;
                     var16 = var7;
                     if (var7 >= 8) {
                        break;
                     }

                     for(var17 = 0; var17 < 8; ++var17) {
                        var11 += var0[var8 + var17] & 255;
                     }

                     ++var7;
                     var8 += var3;
                  }
               }

               var7 = var16 + 1;
               var11 = var15 + var3;
            }

            var8 >>= 6;
            if (var14 - var13 <= 24) {
               var7 = var13 / 2;
               var8 = var7;
               if (var6 > 0) {
                  var8 = var7;
                  if (var10 > 0) {
                     var11 = (var5[var6 - 1][var10] + var5[var6][var10 - 1] * 2 + var5[var6 - 1][var10 - 1]) / 4;
                     var8 = var7;
                     if (var13 < var11) {
                        var8 = var11;
                     }
                  }
               }
            }

            var5[var6][var10] = var8;
         }
      }

      return var5;
   }

   private static void calculateThresholdForBlock(byte[] var0, int var1, int var2, int var3, int var4, int[][] var5, BitMatrix var6) {
      for(int var7 = 0; var7 < var2; ++var7) {
         int var8 = var7 << 3;
         int var9 = var4 - 8;
         int var10 = var8;
         if (var8 > var9) {
            var10 = var9;
         }

         for(var9 = 0; var9 < var1; ++var9) {
            int var11 = var9 << 3;
            int var12 = var3 - 8;
            var8 = var11;
            if (var11 > var12) {
               var8 = var12;
            }

            int var13 = cap(var9, 2, var1 - 3);
            int var14 = cap(var7, 2, var2 - 3);
            var12 = 0;

            for(var11 = -2; var11 <= 2; ++var11) {
               int[] var15 = var5[var14 + var11];
               var12 += var15[var13 - 2] + var15[var13 - 1] + var15[var13] + var15[var13 + 1] + var15[var13 + 2];
            }

            thresholdBlock(var0, var8, var10, var12 / 25, var3, var6);
         }
      }

   }

   private static int cap(int var0, int var1, int var2) {
      if (var0 >= var1) {
         if (var0 > var2) {
            var1 = var2;
         } else {
            var1 = var0;
         }
      }

      return var1;
   }

   private static void thresholdBlock(byte[] var0, int var1, int var2, int var3, int var4, BitMatrix var5) {
      int var6 = 0;

      for(int var7 = var2 * var4 + var1; var6 < 8; var7 += var4) {
         for(int var8 = 0; var8 < 8; ++var8) {
            if ((var0[var7 + var8] & 255) <= var3) {
               var5.set(var1 + var8, var2 + var6);
            }
         }

         ++var6;
      }

   }

   public Binarizer createBinarizer(LuminanceSource var1) {
      return new HybridBinarizer(var1);
   }

   public BitMatrix getBlackMatrix() throws NotFoundException {
      BitMatrix var1;
      if (this.matrix != null) {
         var1 = this.matrix;
      } else {
         LuminanceSource var9 = this.getLuminanceSource();
         int var2 = var9.getWidth();
         int var3 = var9.getHeight();
         if (var2 >= 40 && var3 >= 40) {
            byte[] var10 = var9.getMatrix();
            int var4 = var2 >> 3;
            int var5 = var4;
            if ((var2 & 7) != 0) {
               var5 = var4 + 1;
            }

            int var6 = var3 >> 3;
            var4 = var6;
            if ((var3 & 7) != 0) {
               var4 = var6 + 1;
            }

            int[][] var7 = calculateBlackPoints(var10, var5, var4, var2, var3);
            BitMatrix var8 = new BitMatrix(var2, var3);
            calculateThresholdForBlock(var10, var5, var4, var2, var3, var7, var8);
            this.matrix = var8;
         } else {
            this.matrix = super.getBlackMatrix();
         }

         var1 = this.matrix;
      }

      return var1;
   }
}
