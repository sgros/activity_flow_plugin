package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitArray;
import java.util.Map;

public final class ITFReader extends OneDReader {
   private static final int[] DEFAULT_ALLOWED_LENGTHS = new int[]{6, 8, 10, 12, 14};
   private static final int[] END_PATTERN_REVERSED = new int[]{1, 1, 3};
   private static final float MAX_AVG_VARIANCE = 0.38F;
   private static final float MAX_INDIVIDUAL_VARIANCE = 0.78F;
   private static final int N = 1;
   static final int[][] PATTERNS;
   private static final int[] START_PATTERN = new int[]{1, 1, 1, 1};
   private static final int W = 3;
   private int narrowLineWidth = -1;

   static {
      int[] var0 = new int[]{1, 1, 3, 3, 1};
      int[] var1 = new int[]{3, 1, 1, 1, 3};
      int[] var2 = new int[]{1, 3, 1, 1, 3};
      int[] var3 = new int[]{3, 1, 3, 1, 1};
      int[] var4 = new int[]{1, 3, 3, 1, 1};
      int[] var5 = new int[]{1, 1, 1, 3, 3};
      int[] var6 = new int[]{3, 1, 1, 3, 1};
      int[] var7 = new int[]{1, 3, 1, 3, 1};
      PATTERNS = new int[][]{var0, var1, var2, {3, 3, 1, 1, 1}, {1, 1, 3, 1, 3}, var3, var4, var5, var6, var7};
   }

   private static int decodeDigit(int[] var0) throws NotFoundException {
      float var1 = 0.38F;
      int var2 = -1;
      int var3 = PATTERNS.length;

      float var6;
      for(int var4 = 0; var4 < var3; var1 = var6) {
         float var5 = patternMatchVariance(var0, PATTERNS[var4], 0.78F);
         var6 = var1;
         if (var5 < var1) {
            var6 = var5;
            var2 = var4;
         }

         ++var4;
      }

      if (var2 >= 0) {
         return var2;
      } else {
         throw NotFoundException.getNotFoundInstance();
      }
   }

   private int[] decodeEnd(BitArray var1) throws NotFoundException {
      var1.reverse();

      int[] var2;
      label68: {
         Throwable var10000;
         label72: {
            boolean var10001;
            try {
               var2 = findGuardPattern(var1, skipWhiteSpace(var1), END_PATTERN_REVERSED);
               this.validateQuietZone(var1, var2[0]);
            } catch (Throwable var9) {
               var10000 = var9;
               var10001 = false;
               break label72;
            }

            int var3 = var2[0];

            label63:
            try {
               var2[0] = var1.getSize() - var2[1];
               var2[1] = var1.getSize() - var3;
               break label68;
            } catch (Throwable var8) {
               var10000 = var8;
               var10001 = false;
               break label63;
            }
         }

         Throwable var10 = var10000;
         var1.reverse();
         throw var10;
      }

      var1.reverse();
      return var2;
   }

   private static void decodeMiddle(BitArray var0, int var1, int var2, StringBuilder var3) throws NotFoundException {
      int[] var4 = new int[10];
      int[] var5 = new int[5];
      int[] var6 = new int[5];

      while(var1 < var2) {
         recordPattern(var0, var1, var4);

         int var7;
         int var8;
         for(var7 = 0; var7 < 5; ++var7) {
            var8 = var7 * 2;
            var5[var7] = var4[var8];
            var6[var7] = var4[var8 + 1];
         }

         var3.append((char)(decodeDigit(var5) + 48));
         var3.append((char)(decodeDigit(var6) + 48));
         var8 = 0;
         var7 = var1;

         while(true) {
            var1 = var7;
            if (var8 >= 10) {
               break;
            }

            var7 += var4[var8];
            ++var8;
         }
      }

   }

   private int[] decodeStart(BitArray var1) throws NotFoundException {
      int[] var2 = findGuardPattern(var1, skipWhiteSpace(var1), START_PATTERN);
      this.narrowLineWidth = (var2[1] - var2[0]) / 4;
      this.validateQuietZone(var1, var2[0]);
      return var2;
   }

   private static int[] findGuardPattern(BitArray var0, int var1, int[] var2) throws NotFoundException {
      int var3 = var2.length;
      int[] var4 = new int[var3];
      int var5 = var0.getSize();
      boolean var6 = false;
      byte var7 = 0;
      int var9 = var1;
      var1 = var1;

      for(int var8 = var7; var9 < var5; ++var9) {
         if (var0.get(var9) ^ var6) {
            int var10002 = var4[var8]++;
         } else {
            if (var8 == var3 - 1) {
               if (patternMatchVariance(var4, var2, 0.78F) < 0.38F) {
                  return new int[]{var1, var9};
               }

               var1 += var4[0] + var4[1];
               System.arraycopy(var4, 2, var4, 0, var3 - 2);
               var4[var3 - 2] = 0;
               var4[var3 - 1] = 0;
               --var8;
            } else {
               ++var8;
            }

            var4[var8] = 1;
            if (!var6) {
               var6 = true;
            } else {
               var6 = false;
            }
         }
      }

      throw NotFoundException.getNotFoundInstance();
   }

   private static int skipWhiteSpace(BitArray var0) throws NotFoundException {
      int var1 = var0.getSize();
      int var2 = var0.getNextSet(0);
      if (var2 == var1) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         return var2;
      }
   }

   private void validateQuietZone(BitArray var1, int var2) throws NotFoundException {
      int var3 = this.narrowLineWidth * 10;
      if (var3 >= var2) {
         var3 = var2;
      }

      --var2;

      while(var3 > 0 && var2 >= 0 && !var1.get(var2)) {
         --var3;
         --var2;
      }

      if (var3 != 0) {
         throw NotFoundException.getNotFoundInstance();
      }
   }

   public Result decodeRow(int var1, BitArray var2, Map var3) throws FormatException, NotFoundException {
      int[] var4 = this.decodeStart(var2);
      int[] var5 = this.decodeEnd(var2);
      StringBuilder var6 = new StringBuilder(20);
      decodeMiddle(var2, var4[1], var5[0], var6);
      String var19 = var6.toString();
      int[] var14 = null;
      if (var3 != null) {
         var14 = (int[])var3.get(DecodeHintType.ALLOWED_LENGTHS);
      }

      int[] var15 = var14;
      if (var14 == null) {
         var15 = DEFAULT_ALLOWED_LENGTHS;
      }

      int var7 = var19.length();
      boolean var8 = false;
      int var9 = 0;
      int var10 = var15.length;
      int var11 = 0;

      boolean var12;
      while(true) {
         var12 = var8;
         if (var11 >= var10) {
            break;
         }

         int var13 = var15[var11];
         if (var7 == var13) {
            var12 = true;
            break;
         }

         int var21 = var9;
         if (var13 > var9) {
            var21 = var13;
         }

         ++var11;
         var9 = var21;
      }

      boolean var20 = var12;
      if (!var12) {
         var20 = var12;
         if (var7 > var9) {
            var20 = true;
         }
      }

      if (!var20) {
         throw FormatException.getFormatInstance();
      } else {
         ResultPoint var16 = new ResultPoint((float)var4[1], (float)var1);
         ResultPoint var17 = new ResultPoint((float)var5[0], (float)var1);
         BarcodeFormat var18 = BarcodeFormat.ITF;
         return new Result(var19, (byte[])null, new ResultPoint[]{var16, var17}, var18);
      }
   }
}
