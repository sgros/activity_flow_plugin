package com.journeyapps.barcodescanner.camera;

import android.graphics.Rect;
import com.journeyapps.barcodescanner.Size;

public class FitXYStrategy extends PreviewScalingStrategy {
   private static final String TAG = FitXYStrategy.class.getSimpleName();

   private static float absRatio(float var0) {
      float var1 = var0;
      if (var0 < 1.0F) {
         var1 = 1.0F / var0;
      }

      return var1;
   }

   protected float getScore(Size var1, Size var2) {
      float var3;
      if (var1.width > 0 && var1.height > 0) {
         float var4 = absRatio((float)var1.width * 1.0F / (float)var2.width);
         var3 = absRatio((float)var1.height * 1.0F / (float)var2.height);
         var3 = 1.0F / var4 / var3;
         var4 = absRatio((float)var1.width * 1.0F / (float)var1.height / ((float)var2.width * 1.0F / (float)var2.height));
         var3 *= 1.0F / var4 / var4 / var4;
      } else {
         var3 = 0.0F;
      }

      return var3;
   }

   public Rect scalePreview(Size var1, Size var2) {
      return new Rect(0, 0, var2.width, var2.height);
   }
}
