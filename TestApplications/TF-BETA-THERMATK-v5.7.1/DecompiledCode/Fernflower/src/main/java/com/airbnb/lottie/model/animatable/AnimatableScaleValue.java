package com.airbnb.lottie.model.animatable;

import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.ScaleKeyframeAnimation;
import java.util.List;

public class AnimatableScaleValue extends BaseAnimatableValue {
   public AnimatableScaleValue(List var1) {
      super(var1);
   }

   public BaseKeyframeAnimation createAnimation() {
      return new ScaleKeyframeAnimation(super.keyframes);
   }
}
