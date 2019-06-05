package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;

public class IntegerKeyframeAnimation extends KeyframeAnimation {
   public IntegerKeyframeAnimation(List var1) {
      super(var1);
   }

   Integer getValue(Keyframe var1, float var2) {
      if (var1.startValue != null && var1.endValue != null) {
         if (this.valueCallback != null) {
            Integer var3 = (Integer)this.valueCallback.getValueInternal(var1.startFrame, var1.endFrame, var1.startValue, var1.endValue, var2, this.getLinearCurrentKeyframeProgress(), this.getProgress());
            if (var3 != null) {
               return var3;
            }
         }

         return MiscUtils.lerp((Integer)var1.startValue, (Integer)var1.endValue, var2);
      } else {
         throw new IllegalStateException("Missing values for keyframe.");
      }
   }
}
