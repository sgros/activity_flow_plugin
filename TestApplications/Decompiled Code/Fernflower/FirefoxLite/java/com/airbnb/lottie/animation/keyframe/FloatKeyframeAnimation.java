package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;

public class FloatKeyframeAnimation extends KeyframeAnimation {
   public FloatKeyframeAnimation(List var1) {
      super(var1);
   }

   Float getValue(Keyframe var1, float var2) {
      if (var1.startValue != null && var1.endValue != null) {
         if (this.valueCallback != null) {
            Float var3 = (Float)this.valueCallback.getValueInternal(var1.startFrame, var1.endFrame, var1.startValue, var1.endValue, var2, this.getLinearCurrentKeyframeProgress(), this.getProgress());
            if (var3 != null) {
               return var3;
            }
         }

         return MiscUtils.lerp((Float)var1.startValue, (Float)var1.endValue, var2);
      } else {
         throw new IllegalStateException("Missing values for keyframe.");
      }
   }
}
