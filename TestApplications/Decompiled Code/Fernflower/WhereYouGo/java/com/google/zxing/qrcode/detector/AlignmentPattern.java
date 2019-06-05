package com.google.zxing.qrcode.detector;

import com.google.zxing.ResultPoint;

public final class AlignmentPattern extends ResultPoint {
   private final float estimatedModuleSize;

   AlignmentPattern(float var1, float var2, float var3) {
      super(var1, var2);
      this.estimatedModuleSize = var3;
   }

   boolean aboutEquals(float var1, float var2, float var3) {
      boolean var4 = false;
      boolean var5 = var4;
      if (Math.abs(var2 - this.getY()) <= var1) {
         var5 = var4;
         if (Math.abs(var3 - this.getX()) <= var1) {
            var1 = Math.abs(var1 - this.estimatedModuleSize);
            if (var1 > 1.0F) {
               var5 = var4;
               if (var1 > this.estimatedModuleSize) {
                  return var5;
               }
            }

            var5 = true;
         }
      }

      return var5;
   }

   AlignmentPattern combineEstimate(float var1, float var2, float var3) {
      return new AlignmentPattern((this.getX() + var2) / 2.0F, (this.getY() + var1) / 2.0F, (this.estimatedModuleSize + var3) / 2.0F);
   }
}
