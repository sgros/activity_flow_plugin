package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.utils.GammaEvaluator;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;

public class ColorKeyframeAnimation extends KeyframeAnimation {
   public ColorKeyframeAnimation(List var1) {
      super(var1);
   }

   public Integer getValue(Keyframe var1, float var2) {
      if (var1.startValue != null && var1.endValue != null) {
         int var3 = (Integer)var1.startValue;
         int var4 = (Integer)var1.endValue;
         if (this.valueCallback != null) {
            Integer var5 = (Integer)this.valueCallback.getValueInternal(var1.startFrame, var1.endFrame, var3, var4, var2, this.getLinearCurrentKeyframeProgress(), this.getProgress());
            if (var5 != null) {
               return var5;
            }
         }

         return GammaEvaluator.evaluate(var2, var3, var4);
      } else {
         throw new IllegalStateException("Missing values for keyframe.");
      }
   }
}
