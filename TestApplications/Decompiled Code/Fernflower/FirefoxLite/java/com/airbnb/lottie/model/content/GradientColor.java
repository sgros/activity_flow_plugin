package com.airbnb.lottie.model.content;

import com.airbnb.lottie.utils.GammaEvaluator;
import com.airbnb.lottie.utils.MiscUtils;

public class GradientColor {
   private final int[] colors;
   private final float[] positions;

   public GradientColor(float[] var1, int[] var2) {
      this.positions = var1;
      this.colors = var2;
   }

   public int[] getColors() {
      return this.colors;
   }

   public float[] getPositions() {
      return this.positions;
   }

   public int getSize() {
      return this.colors.length;
   }

   public void lerp(GradientColor var1, GradientColor var2, float var3) {
      if (var1.colors.length != var2.colors.length) {
         StringBuilder var5 = new StringBuilder();
         var5.append("Cannot interpolate between gradients. Lengths vary (");
         var5.append(var1.colors.length);
         var5.append(" vs ");
         var5.append(var2.colors.length);
         var5.append(")");
         throw new IllegalArgumentException(var5.toString());
      } else {
         for(int var4 = 0; var4 < var1.colors.length; ++var4) {
            this.positions[var4] = MiscUtils.lerp(var1.positions[var4], var2.positions[var4], var3);
            this.colors[var4] = GammaEvaluator.evaluate(var3, var1.colors[var4], var2.colors[var4]);
         }

      }
   }
}
