package com.airbnb.lottie.model.animatable;

import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.PointKeyframeAnimation;
import java.util.List;

public class AnimatablePointValue extends BaseAnimatableValue {
   public AnimatablePointValue(List var1) {
      super(var1);
   }

   public BaseKeyframeAnimation createAnimation() {
      return new PointKeyframeAnimation(this.keyframes);
   }
}
