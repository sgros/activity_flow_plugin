package com.airbnb.lottie.utils;

public class GammaEvaluator {
   private static float EOCF_sRGB(float var0) {
      if (var0 <= 0.04045F) {
         var0 /= 12.92F;
      } else {
         var0 = (float)Math.pow((double)((var0 + 0.055F) / 1.055F), 2.4000000953674316D);
      }

      return var0;
   }

   private static float OECF_sRGB(float var0) {
      if (var0 <= 0.0031308F) {
         var0 *= 12.92F;
      } else {
         var0 = (float)(Math.pow((double)var0, 0.4166666567325592D) * 1.0549999475479126D - 0.054999999701976776D);
      }

      return var0;
   }

   public static int evaluate(float var0, int var1, int var2) {
      float var3 = (float)(var1 >> 24 & 255) / 255.0F;
      float var4 = (float)(var1 >> 16 & 255) / 255.0F;
      float var5 = (float)(var1 >> 8 & 255) / 255.0F;
      float var6 = (float)(var1 & 255) / 255.0F;
      float var7 = (float)(var2 >> 24 & 255) / 255.0F;
      float var8 = (float)(var2 >> 16 & 255) / 255.0F;
      float var9 = (float)(var2 >> 8 & 255) / 255.0F;
      float var10 = (float)(var2 & 255) / 255.0F;
      var4 = EOCF_sRGB(var4);
      var5 = EOCF_sRGB(var5);
      var6 = EOCF_sRGB(var6);
      var8 = EOCF_sRGB(var8);
      var9 = EOCF_sRGB(var9);
      var10 = EOCF_sRGB(var10);
      var4 = OECF_sRGB(var4 + (var8 - var4) * var0);
      var5 = OECF_sRGB(var5 + (var9 - var5) * var0);
      var10 = OECF_sRGB(var6 + var0 * (var10 - var6));
      var1 = Math.round((var3 + (var7 - var3) * var0) * 255.0F);
      return Math.round(var4 * 255.0F) << 16 | var1 << 24 | Math.round(var5 * 255.0F) << 8 | Math.round(var10 * 255.0F);
   }
}
