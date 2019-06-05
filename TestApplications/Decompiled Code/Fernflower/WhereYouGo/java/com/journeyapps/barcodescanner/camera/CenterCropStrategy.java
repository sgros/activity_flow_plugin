package com.journeyapps.barcodescanner.camera;

import android.graphics.Rect;
import android.util.Log;
import com.journeyapps.barcodescanner.Size;

public class CenterCropStrategy extends PreviewScalingStrategy {
   private static final String TAG = CenterCropStrategy.class.getSimpleName();

   protected float getScore(Size var1, Size var2) {
      float var3;
      if (var1.width > 0 && var1.height > 0) {
         Size var4 = var1.scaleCrop(var2);
         var3 = (float)var4.width * 1.0F / (float)var1.width;
         if (var3 > 1.0F) {
            var3 = (float)Math.pow((double)(1.0F / var3), 1.1D);
         }

         float var5 = (float)var4.width * 1.0F / (float)var2.width + (float)var4.height * 1.0F / (float)var2.height;
         var3 *= 1.0F / var5 / var5;
      } else {
         var3 = 0.0F;
      }

      return var3;
   }

   public Rect scalePreview(Size var1, Size var2) {
      Size var3 = var1.scaleCrop(var2);
      Log.i(TAG, "Preview: " + var1 + "; Scaled: " + var3 + "; Want: " + var2);
      int var4 = (var3.width - var2.width) / 2;
      int var5 = (var3.height - var2.height) / 2;
      return new Rect(-var4, -var5, var3.width - var4, var3.height - var5);
   }
}
