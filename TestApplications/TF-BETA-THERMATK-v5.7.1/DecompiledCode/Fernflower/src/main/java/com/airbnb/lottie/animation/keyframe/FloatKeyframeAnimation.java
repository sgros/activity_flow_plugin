package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.Keyframe;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.List;

public class FloatKeyframeAnimation extends KeyframeAnimation {
   public FloatKeyframeAnimation(List var1) {
      super(var1);
   }

   public float getFloatValue() {
      return this.getFloatValue(this.getCurrentKeyframe(), this.getInterpolatedCurrentKeyframeProgress());
   }

   float getFloatValue(Keyframe var1, float var2) {
      if (var1.startValue != null && var1.endValue != null) {
         LottieValueCallback var3 = super.valueCallback;
         if (var3 != null) {
            Float var4 = (Float)var3.getValueInternal(var1.startFrame, var1.endFrame, var1.startValue, var1.endValue, var2, this.getLinearCurrentKeyframeProgress(), this.getProgress());
            if (var4 != null) {
               return var4;
            }
         }

         return MiscUtils.lerp(var1.getStartValueFloat(), var1.getEndValueFloat(), var2);
      } else {
         throw new IllegalStateException("Missing values for keyframe.");
      }
   }

   Float getValue(Keyframe var1, float var2) {
      return this.getFloatValue(var1, var2);
   }
}
