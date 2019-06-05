package com.google.zxing.pdf417.detector;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class Detector {
   private static final int BARCODE_MIN_HEIGHT = 10;
   private static final int[] INDEXES_START_PATTERN = new int[]{0, 4, 1, 5};
   private static final int[] INDEXES_STOP_PATTERN = new int[]{6, 2, 7, 3};
   private static final float MAX_AVG_VARIANCE = 0.42F;
   private static final float MAX_INDIVIDUAL_VARIANCE = 0.8F;
   private static final int MAX_PATTERN_DRIFT = 5;
   private static final int MAX_PIXEL_DRIFT = 3;
   private static final int ROW_STEP = 5;
   private static final int SKIPPED_ROW_COUNT_MAX = 25;
   private static final int[] START_PATTERN = new int[]{8, 1, 1, 1, 1, 1, 1, 3};
   private static final int[] STOP_PATTERN = new int[]{7, 1, 1, 3, 1, 1, 1, 2, 1};

   private Detector() {
   }

   private static void copyToResult(ResultPoint[] var0, ResultPoint[] var1, int[] var2) {
      for(int var3 = 0; var3 < var2.length; ++var3) {
         var0[var2[var3]] = var1[var3];
      }

   }

   public static PDF417DetectorResult detect(BinaryBitmap var0, Map var1, boolean var2) throws NotFoundException {
      BitMatrix var3 = var0.getBlackMatrix();
      List var4 = detect(var2, var3);
      List var6 = var4;
      BitMatrix var5 = var3;
      if (var4.isEmpty()) {
         var5 = var3.clone();
         var5.rotate180();
         var6 = detect(var2, var5);
      }

      return new PDF417DetectorResult(var5, var6);
   }

   private static List detect(boolean var0, BitMatrix var1) {
      ArrayList var2 = new ArrayList();
      int var3 = 0;
      int var4 = 0;
      boolean var5 = false;

      while(var3 < var1.getHeight()) {
         ResultPoint[] var6 = findVertices(var1, var3, var4);
         if (var6[0] == null && var6[3] == null) {
            if (!var5) {
               break;
            }

            var5 = false;
            byte var7 = 0;
            Iterator var9 = var2.iterator();

            while(var9.hasNext()) {
               ResultPoint[] var8 = (ResultPoint[])var9.next();
               var4 = var3;
               if (var8[1] != null) {
                  var4 = (int)Math.max((float)var3, var8[1].getY());
               }

               var3 = var4;
               if (var8[3] != null) {
                  var3 = Math.max(var4, (int)var8[3].getY());
               }
            }

            var3 += 5;
            var4 = var7;
         } else {
            var5 = true;
            var2.add(var6);
            if (!var0) {
               break;
            }

            if (var6[2] != null) {
               var4 = (int)var6[2].getX();
               var3 = (int)var6[2].getY();
            } else {
               var4 = (int)var6[4].getX();
               var3 = (int)var6[4].getY();
            }
         }
      }

      return var2;
   }

   private static int[] findGuardPattern(BitMatrix var0, int var1, int var2, int var3, boolean var4, int[] var5, int[] var6) {
      Arrays.fill(var6, 0, var6.length, 0);

      int var7;
      for(var7 = 0; var0.get(var1, var2) && var1 > 0 && var7 < 3; ++var7) {
         --var1;
      }

      int var8 = var1;
      var7 = 0;
      int var9 = var5.length;

      int[] var10;
      while(true) {
         if (var8 >= var3) {
            if (var7 == var9 - 1 && patternMatchVariance(var6, var5, 0.8F) < 0.42F) {
               var10 = new int[]{var1, var8 - 1};
               break;
            }

            var10 = null;
            break;
         }

         if (var0.get(var8, var2) ^ var4) {
            int var10002 = var6[var7]++;
         } else {
            if (var7 == var9 - 1) {
               if (patternMatchVariance(var6, var5, 0.8F) < 0.42F) {
                  var10 = new int[]{var1, var8};
                  break;
               }

               var1 += var6[0] + var6[1];
               System.arraycopy(var6, 2, var6, 0, var9 - 2);
               var6[var9 - 2] = 0;
               var6[var9 - 1] = 0;
               --var7;
            } else {
               ++var7;
            }

            var6[var7] = 1;
            if (!var4) {
               var4 = true;
            } else {
               var4 = false;
            }
         }

         ++var8;
      }

      return var10;
   }

   private static ResultPoint[] findRowsWithPattern(BitMatrix var0, int var1, int var2, int var3, int var4, int[] var5) {
      ResultPoint[] var6 = new ResultPoint[4];
      boolean var7 = false;
      int[] var8 = new int[var5.length];

      boolean var9;
      int var10;
      int[] var11;
      int[] var12;
      while(true) {
         var9 = var7;
         var10 = var3;
         if (var3 >= var1) {
            break;
         }

         var11 = findGuardPattern(var0, var4, var3, var2, false, var5, var8);
         if (var11 != null) {
            var10 = var3;

            while(true) {
               var3 = var10;
               if (var10 <= 0) {
                  break;
               }

               --var10;
               var12 = findGuardPattern(var0, var4, var10, var2, false, var5, var8);
               if (var12 == null) {
                  var3 = var10 + 1;
                  break;
               }

               var11 = var12;
            }

            var6[0] = new ResultPoint((float)var11[0], (float)var3);
            var6[1] = new ResultPoint((float)var11[1], (float)var3);
            var9 = true;
            var10 = var3;
            break;
         }

         var3 += 5;
      }

      var3 = var10 + 1;
      var4 = var3;
      if (var9) {
         int var13 = 0;
         var11 = new int[]{(int)var6[0].getX(), (int)var6[1].getX()};

         for(var4 = var3; var4 < var1; var13 = var3) {
            var12 = findGuardPattern(var0, var11[0], var4, var2, false, var5, var8);
            if (var12 != null && Math.abs(var11[0] - var12[0]) < 5 && Math.abs(var11[1] - var12[1]) < 5) {
               var11 = var12;
               var3 = 0;
            } else {
               if (var13 > 25) {
                  break;
               }

               var3 = var13 + 1;
            }

            ++var4;
         }

         var4 -= var13 + 1;
         var6[2] = new ResultPoint((float)var11[0], (float)var4);
         var6[3] = new ResultPoint((float)var11[1], (float)var4);
      }

      if (var4 - var10 < 10) {
         for(var1 = 0; var1 < 4; ++var1) {
            var6[var1] = null;
         }
      }

      return var6;
   }

   private static ResultPoint[] findVertices(BitMatrix var0, int var1, int var2) {
      int var3 = var0.getHeight();
      int var4 = var0.getWidth();
      ResultPoint[] var5 = new ResultPoint[8];
      copyToResult(var5, findRowsWithPattern(var0, var3, var4, var1, var2, START_PATTERN), INDEXES_START_PATTERN);
      if (var5[4] != null) {
         var2 = (int)var5[4].getX();
         var1 = (int)var5[4].getY();
      }

      copyToResult(var5, findRowsWithPattern(var0, var3, var4, var1, var2, STOP_PATTERN), INDEXES_STOP_PATTERN);
      return var5;
   }

   private static float patternMatchVariance(int[] var0, int[] var1, float var2) {
      float var3 = Float.POSITIVE_INFINITY;
      int var4 = var0.length;
      int var5 = 0;
      int var6 = 0;

      int var7;
      for(var7 = 0; var7 < var4; ++var7) {
         var5 += var0[var7];
         var6 += var1[var7];
      }

      float var8;
      if (var5 < var6) {
         var8 = var3;
      } else {
         float var9 = (float)var5 / (float)var6;
         float var10 = 0.0F;

         for(var7 = 0; var7 < var4; ++var7) {
            var6 = var0[var7];
            float var11 = (float)var1[var7] * var9;
            if ((float)var6 > var11) {
               var11 = (float)var6 - var11;
            } else {
               var11 -= (float)var6;
            }

            var8 = var3;
            if (var11 > var2 * var9) {
               return var8;
            }

            var10 += var11;
         }

         var8 = var10 / (float)var5;
      }

      return var8;
   }
}
