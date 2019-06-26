package com.airbnb.lottie.animation.keyframe;

import android.graphics.PointF;
import com.airbnb.lottie.value.Keyframe;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.List;

public class PointKeyframeAnimation extends KeyframeAnimation {
   private final PointF point = new PointF();

   public PointKeyframeAnimation(List var1) {
      super(var1);
   }

   public PointF getValue(Keyframe var1, float var2) {
      Object var3 = var1.startValue;
      if (var3 != null) {
         Object var4 = var1.endValue;
         if (var4 != null) {
            PointF var10 = (PointF)var3;
            PointF var11 = (PointF)var4;
            LottieValueCallback var5 = super.valueCallback;
            PointF var9;
            if (var5 != null) {
               var9 = (PointF)var5.getValueInternal(var1.startFrame, var1.endFrame, var10, var11, var2, this.getLinearCurrentKeyframeProgress(), this.getProgress());
               if (var9 != null) {
                  return var9;
               }
            }

            var9 = this.point;
            float var6 = var10.x;
            float var7 = var11.x;
            float var8 = var10.y;
            var9.set(var6 + (var7 - var6) * var2, var8 + var2 * (var11.y - var8));
            return this.point;
         }
      }

      throw new IllegalStateException("Missing values for keyframe.");
   }
}
