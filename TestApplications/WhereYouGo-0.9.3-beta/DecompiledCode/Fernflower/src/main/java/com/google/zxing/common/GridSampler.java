package com.google.zxing.common;

import com.google.zxing.NotFoundException;

public abstract class GridSampler {
   private static GridSampler gridSampler = new DefaultGridSampler();

   protected static void checkAndNudgePoints(BitMatrix var0, float[] var1) throws NotFoundException {
      int var2 = var0.getWidth();
      int var3 = var0.getHeight();
      boolean var4 = true;
      int var5 = 0;

      while(true) {
         int var6;
         int var7;
         if (var5 < var1.length && var4) {
            var6 = (int)var1[var5];
            var7 = (int)var1[var5 + 1];
            if (var6 >= -1 && var6 <= var2 && var7 >= -1 && var7 <= var3) {
               var4 = false;
               if (var6 == -1) {
                  var1[var5] = 0.0F;
                  var4 = true;
               } else if (var6 == var2) {
                  var1[var5] = (float)(var2 - 1);
                  var4 = true;
               }

               if (var7 == -1) {
                  var1[var5 + 1] = 0.0F;
                  var4 = true;
               } else if (var7 == var3) {
                  var1[var5 + 1] = (float)(var3 - 1);
                  var4 = true;
               }

               var5 += 2;
               continue;
            }

            throw NotFoundException.getNotFoundInstance();
         }

         var4 = true;
         var5 = var1.length - 2;

         while(true) {
            if (var5 >= 0 && var4) {
               var6 = (int)var1[var5];
               var7 = (int)var1[var5 + 1];
               if (var6 >= -1 && var6 <= var2 && var7 >= -1 && var7 <= var3) {
                  var4 = false;
                  if (var6 == -1) {
                     var1[var5] = 0.0F;
                     var4 = true;
                  } else if (var6 == var2) {
                     var1[var5] = (float)(var2 - 1);
                     var4 = true;
                  }

                  if (var7 == -1) {
                     var1[var5 + 1] = 0.0F;
                     var4 = true;
                  } else if (var7 == var3) {
                     var1[var5 + 1] = (float)(var3 - 1);
                     var4 = true;
                  }

                  var5 -= 2;
                  continue;
               }

               throw NotFoundException.getNotFoundInstance();
            }

            return;
         }
      }
   }

   public static GridSampler getInstance() {
      return gridSampler;
   }

   public static void setGridSampler(GridSampler var0) {
      gridSampler = var0;
   }

   public abstract BitMatrix sampleGrid(BitMatrix var1, int var2, int var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17, float var18, float var19) throws NotFoundException;

   public abstract BitMatrix sampleGrid(BitMatrix var1, int var2, int var3, PerspectiveTransform var4) throws NotFoundException;
}
