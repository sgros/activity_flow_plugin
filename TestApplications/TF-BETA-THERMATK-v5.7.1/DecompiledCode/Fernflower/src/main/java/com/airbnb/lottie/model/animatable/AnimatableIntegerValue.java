package com.airbnb.lottie.model.animatable;

import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.IntegerKeyframeAnimation;
import java.util.List;

public class AnimatableIntegerValue extends BaseAnimatableValue {
   public AnimatableIntegerValue(List var1) {
      super(var1);
   }

   public BaseKeyframeAnimation createAnimation() {
      return new IntegerKeyframeAnimation(super.keyframes);
   }
}
