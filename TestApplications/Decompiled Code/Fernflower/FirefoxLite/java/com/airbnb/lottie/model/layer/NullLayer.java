package com.airbnb.lottie.model.layer;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import com.airbnb.lottie.LottieDrawable;

public class NullLayer extends BaseLayer {
   NullLayer(LottieDrawable var1, Layer var2) {
      super(var1, var2);
   }

   void drawLayer(Canvas var1, Matrix var2, int var3) {
   }

   public void getBounds(RectF var1, Matrix var2) {
      super.getBounds(var1, var2);
      var1.set(0.0F, 0.0F, 0.0F, 0.0F);
   }
}
