package org.telegram.ui.Components.Paint;

import android.graphics.Matrix;

public class GLMatrix {
   public static float[] LoadGraphicsMatrix(Matrix var0) {
      float[] var1 = new float[9];
      var0.getValues(var1);
      return new float[]{var1[0], var1[1], 0.0F, 0.0F, var1[3], var1[4], 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, var1[2], var1[5], 0.0F, 1.0F};
   }

   public static float[] LoadOrtho(float var0, float var1, float var2, float var3, float var4, float var5) {
      float var6 = var1 - var0;
      float var7 = var3 - var2;
      float var8 = var5 - var4;
      var0 = -(var1 + var0) / var6;
      var1 = -(var3 + var2) / var7;
      var2 = -(var5 + var4) / var8;
      return new float[]{2.0F / var6, 0.0F, 0.0F, 0.0F, 0.0F, 2.0F / var7, 0.0F, 0.0F, 0.0F, 0.0F, -2.0F / var8, 0.0F, var0, var1, var2, 1.0F};
   }

   public static float[] MultiplyMat4f(float[] var0, float[] var1) {
      return new float[]{var0[0] * var1[0] + var0[4] * var1[1] + var0[8] * var1[2] + var0[12] * var1[3], var0[1] * var1[0] + var0[5] * var1[1] + var0[9] * var1[2] + var0[13] * var1[3], var0[2] * var1[0] + var0[6] * var1[1] + var0[10] * var1[2] + var0[14] * var1[3], var0[3] * var1[0] + var0[7] * var1[1] + var0[11] * var1[2] + var0[15] * var1[3], var0[0] * var1[4] + var0[4] * var1[5] + var0[8] * var1[6] + var0[12] * var1[7], var0[1] * var1[4] + var0[5] * var1[5] + var0[9] * var1[6] + var0[13] * var1[7], var0[2] * var1[4] + var0[6] * var1[5] + var0[10] * var1[6] + var0[14] * var1[7], var0[3] * var1[4] + var0[7] * var1[5] + var0[11] * var1[6] + var0[15] * var1[7], var0[0] * var1[8] + var0[4] * var1[9] + var0[8] * var1[10] + var0[12] * var1[11], var0[1] * var1[8] + var0[5] * var1[9] + var0[9] * var1[10] + var0[13] * var1[11], var0[2] * var1[8] + var0[6] * var1[9] + var0[10] * var1[10] + var0[14] * var1[11], var0[3] * var1[8] + var0[7] * var1[9] + var0[11] * var1[10] + var0[15] * var1[11], var0[0] * var1[12] + var0[4] * var1[13] + var0[8] * var1[14] + var0[12] * var1[15], var0[1] * var1[12] + var0[5] * var1[13] + var0[9] * var1[14] + var0[13] * var1[15], var0[2] * var1[12] + var0[6] * var1[13] + var0[10] * var1[14] + var0[14] * var1[15], var0[3] * var1[12] + var0[7] * var1[13] + var0[11] * var1[14] + var0[15] * var1[15]};
   }
}
