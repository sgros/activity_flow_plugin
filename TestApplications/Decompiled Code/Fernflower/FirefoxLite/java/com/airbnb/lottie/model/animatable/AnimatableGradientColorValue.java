package com.airbnb.lottie.model.animatable;

import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.GradientColorKeyframeAnimation;
import java.util.List;

public class AnimatableGradientColorValue extends BaseAnimatableValue {
   public AnimatableGradientColorValue(List var1) {
      super(var1);
   }

   public BaseKeyframeAnimation createAnimation() {
      return new GradientColorKeyframeAnimation(this.keyframes);
   }
}
