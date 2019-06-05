package com.airbnb.lottie.animation.keyframe;

import android.graphics.PointF;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;

public class PointKeyframeAnimation extends KeyframeAnimation {
   private final PointF point = new PointF();

   public PointKeyframeAnimation(List var1) {
      super(var1);
   }

   public PointF getValue(Keyframe var1, float var2) {
      if (var1.startValue != null && var1.endValue != null) {
         PointF var3 = (PointF)var1.startValue;
         PointF var4 = (PointF)var1.endValue;
         if (this.valueCallback != null) {
            PointF var5 = (PointF)this.valueCallback.getValueInternal(var1.startFrame, var1.endFrame, var3, var4, var2, this.getLinearCurrentKeyframeProgress(), this.getProgress());
            if (var5 != null) {
               return var5;
            }
         }

         this.point.set(var3.x + (var4.x - var3.x) * var2, var3.y + var2 * (var4.y - var3.y));
         return this.point;
      } else {
         throw new IllegalStateException("Missing values for keyframe.");
      }
   }
}
