package com.google.zxing.common;

import com.google.zxing.NotFoundException;

public final class DefaultGridSampler extends GridSampler {
   public BitMatrix sampleGrid(BitMatrix var1, int var2, int var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17, float var18, float var19) throws NotFoundException {
      return this.sampleGrid(var1, var2, var3, PerspectiveTransform.quadrilateralToQuadrilateral(var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17, var18, var19));
   }

   public BitMatrix sampleGrid(BitMatrix var1, int var2, int var3, PerspectiveTransform var4) throws NotFoundException {
      if (var2 > 0 && var3 > 0) {
         BitMatrix var5 = new BitMatrix(var2, var3);
         float[] var6 = new float[var2 * 2];

         for(var2 = 0; var2 < var3; ++var2) {
            int var7 = var6.length;
            float var8 = (float)var2;

            int var9;
            for(var9 = 0; var9 < var7; var9 += 2) {
               var6[var9] = (float)(var9 / 2) + 0.5F;
               var6[var9 + 1] = var8 + 0.5F;
            }

            var4.transformPoints(var6);
            checkAndNudgePoints(var1, var6);

            for(var9 = 0; var9 < var7; var9 += 2) {
               try {
                  if (var1.get((int)var6[var9], (int)var6[var9 + 1])) {
                     var5.set(var9 / 2, var2);
                  }
               } catch (ArrayIndexOutOfBoundsException var10) {
                  throw NotFoundException.getNotFoundInstance();
               }
            }
         }

         return var5;
      } else {
         throw NotFoundException.getNotFoundInstance();
      }
   }
}
