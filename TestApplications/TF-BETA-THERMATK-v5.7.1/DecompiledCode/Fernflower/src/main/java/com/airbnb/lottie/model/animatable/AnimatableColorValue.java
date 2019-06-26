package com.airbnb.lottie.model.animatable;

import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.ColorKeyframeAnimation;
import java.util.List;

public class AnimatableColorValue extends BaseAnimatableValue {
   public AnimatableColorValue(List var1) {
      super(var1);
   }

   public BaseKeyframeAnimation createAnimation() {
      return new ColorKeyframeAnimation(super.keyframes);
   }
}
