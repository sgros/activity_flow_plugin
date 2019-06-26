package com.google.zxing.qrcode.detector;

import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.common.BitMatrix;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

final class AlignmentPatternFinder {
   private final int[] crossCheckStateCount;
   private final int height;
   private final BitMatrix image;
   private final float moduleSize;
   private final List possibleCenters;
   private final ResultPointCallback resultPointCallback;
   private final int startX;
   private final int startY;
   private final int width;

   AlignmentPatternFinder(BitMatrix var1, int var2, int var3, int var4, int var5, float var6, ResultPointCallback var7) {
      this.image = var1;
      this.possibleCenters = new ArrayList(5);
      this.startX = var2;
      this.startY = var3;
      this.width = var4;
      this.height = var5;
      this.moduleSize = var6;
      this.crossCheckStateCount = new int[3];
      this.resultPointCallback = var7;
   }

   private static float centerFromEnd(int[] var0, int var1) {
      return (float)(var1 - var0[2]) - (float)var0[1] / 2.0F;
   }

   private float crossCheckVertical(int var1, int var2, int var3, int var4) {
      float var5 = Float.NaN;
      BitMatrix var6 = this.image;
      int var7 = var6.getHeight();
      int[] var8 = this.crossCheckStateCount;
      var8[0] = 0;
      var8[1] = 0;
      var8[2] = 0;

      int var10002;
      int var9;
      for(var9 = var1; var9 >= 0 && var6.get(var2, var9) && var8[1] <= var3; --var9) {
         var10002 = var8[1]++;
      }

      float var10 = var5;
      if (var9 >= 0) {
         if (var8[1] > var3) {
            var10 = var5;
         } else {
            while(var9 >= 0 && !var6.get(var2, var9) && var8[0] <= var3) {
               var10002 = var8[0]++;
               --var9;
            }

            var10 = var5;
            if (var8[0] <= var3) {
               ++var1;

               while(var1 < var7 && var6.get(var2, var1) && var8[1] <= var3) {
                  var10002 = var8[1]++;
                  ++var1;
               }

               var10 = var5;
               if (var1 != var7) {
                  var10 = var5;
                  if (var8[1] <= var3) {
                     while(var1 < var7 && !var6.get(var2, var1) && var8[2] <= var3) {
                        var10002 = var8[2]++;
                        ++var1;
                     }

                     var10 = var5;
                     if (var8[2] <= var3) {
                        var10 = var5;
                        if (Math.abs(var8[0] + var8[1] + var8[2] - var4) * 5 < var4 * 2) {
                           var10 = var5;
                           if (this.foundPatternCross(var8)) {
                              var10 = centerFromEnd(var8, var1);
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return var10;
   }

   private boolean foundPatternCross(int[] var1) {
      float var2 = this.moduleSize;
      float var3 = var2 / 2.0F;
      int var4 = 0;

      boolean var5;
      while(true) {
         if (var4 >= 3) {
            var5 = true;
            break;
         }

         if (Math.abs(var2 - (float)var1[var4]) >= var3) {
            var5 = false;
            break;
         }

         ++var4;
      }

      return var5;
   }

   private AlignmentPattern handlePossibleCenter(int[] var1, int var2, int var3) {
      int var4 = var1[0];
      int var5 = var1[1];
      int var6 = var1[2];
      float var7 = centerFromEnd(var1, var3);
      float var8 = this.crossCheckVertical(var2, (int)var7, var1[1] * 2, var4 + var5 + var6);
      AlignmentPattern var11;
      if (!Float.isNaN(var8)) {
         float var9 = (float)(var1[0] + var1[1] + var1[2]) / 3.0F;
         Iterator var10 = this.possibleCenters.iterator();

         while(var10.hasNext()) {
            var11 = (AlignmentPattern)var10.next();
            if (var11.aboutEquals(var9, var8, var7)) {
               var11 = var11.combineEstimate(var8, var7, var9);
               return var11;
            }
         }

         var11 = new AlignmentPattern(var7, var8, var9);
         this.possibleCenters.add(var11);
         if (this.resultPointCallback != null) {
            this.resultPointCallback.foundPossibleResultPoint(var11);
         }
      }

      var11 = null;
      return var11;
   }

   AlignmentPattern find() throws NotFoundException {
      int var1 = this.startX;
      int var2 = this.height;
      int var3 = var1 + this.width;
      int var4 = this.startY;
      int var5 = var2 / 2;
      int[] var6 = new int[3];
      int var7 = 0;

      AlignmentPattern var12;
      while(true) {
         if (var7 >= var2) {
            if (this.possibleCenters.isEmpty()) {
               throw NotFoundException.getNotFoundInstance();
            }

            var12 = (AlignmentPattern)this.possibleCenters.get(0);
            break;
         }

         int var8;
         if ((var7 & 1) == 0) {
            var8 = (var7 + 1) / 2;
         } else {
            var8 = -((var7 + 1) / 2);
         }

         int var9 = var4 + var5 + var8;
         var6[0] = 0;
         var6[1] = 0;
         var6[2] = 0;

         for(var8 = var1; var8 < var3 && !this.image.get(var8, var9); ++var8) {
         }

         byte var10 = 0;
         int var11 = var8;

         for(var8 = var10; var11 < var3; ++var11) {
            int var10002;
            if (this.image.get(var11, var9)) {
               if (var8 == 1) {
                  var10002 = var6[1]++;
               } else if (var8 == 2) {
                  if (this.foundPatternCross(var6)) {
                     var12 = this.handlePossibleCenter(var6, var9, var11);
                     if (var12 != null) {
                        return var12;
                     }
                  }

                  var6[0] = var6[2];
                  var6[1] = 1;
                  var6[2] = 0;
                  var8 = 1;
               } else {
                  ++var8;
                  var10002 = var6[var8]++;
               }
            } else {
               int var14 = var8;
               if (var8 == 1) {
                  var14 = var8 + 1;
               }

               var10002 = var6[var14]++;
               var8 = var14;
            }
         }

         if (this.foundPatternCross(var6)) {
            AlignmentPattern var13 = this.handlePossibleCenter(var6, var9, var3);
            var12 = var13;
            if (var13 != null) {
               break;
            }
         }

         ++var7;
      }

      return var12;
   }
}
