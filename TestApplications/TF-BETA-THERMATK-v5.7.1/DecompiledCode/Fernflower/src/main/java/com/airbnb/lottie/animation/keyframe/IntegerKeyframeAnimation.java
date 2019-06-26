package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.Keyframe;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.List;

public class IntegerKeyframeAnimation extends KeyframeAnimation {
   public IntegerKeyframeAnimation(List var1) {
      super(var1);
   }

   public int getIntValue() {
      return this.getIntValue(this.getCurrentKeyframe(), this.getInterpolatedCurrentKeyframeProgress());
   }

   int getIntValue(Keyframe var1, float var2) {
      if (var1.startValue != null && var1.endValue != null) {
         LottieValueCallback var3 = super.valueCallback;
         if (var3 != null) {
            Integer var4 = (Integer)var3.getValueInternal(var1.startFrame, var1.endFrame, var1.startValue, var1.endValue, var2, this.getLinearCurrentKeyframeProgress(), this.getProgress());
            if (var4 != null) {
               return var4;
            }
         }

         return MiscUtils.lerp(var1.getStartValueInt(), var1.getEndValueInt(), var2);
      } else {
         throw new IllegalStateException("Missing values for keyframe.");
      }
   }

   Integer getValue(Keyframe var1, float var2) {
      return this.getIntValue(var1, var2);
   }
}
