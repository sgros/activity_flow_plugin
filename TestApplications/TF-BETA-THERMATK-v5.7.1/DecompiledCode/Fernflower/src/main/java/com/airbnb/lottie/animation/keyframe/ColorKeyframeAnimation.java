package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.utils.GammaEvaluator;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.Keyframe;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.List;

public class ColorKeyframeAnimation extends KeyframeAnimation {
   public ColorKeyframeAnimation(List var1) {
      super(var1);
   }

   public int getIntValue() {
      return this.getIntValue(this.getCurrentKeyframe(), this.getInterpolatedCurrentKeyframeProgress());
   }

   public int getIntValue(Keyframe var1, float var2) {
      Object var3 = var1.startValue;
      if (var3 != null && var1.endValue != null) {
         int var4 = (Integer)var3;
         int var5 = (Integer)var1.endValue;
         LottieValueCallback var7 = super.valueCallback;
         if (var7 != null) {
            Integer var6 = (Integer)var7.getValueInternal(var1.startFrame, var1.endFrame, var4, var5, var2, this.getLinearCurrentKeyframeProgress(), this.getProgress());
            if (var6 != null) {
               return var6;
            }
         }

         return GammaEvaluator.evaluate(MiscUtils.clamp(var2, 0.0F, 1.0F), var4, var5);
      } else {
         throw new IllegalStateException("Missing values for keyframe.");
      }
   }

   Integer getValue(Keyframe var1, float var2) {
      return this.getIntValue(var1, var2);
   }
}
