package com.airbnb.lottie.model.animatable;

import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.FloatKeyframeAnimation;
import java.util.List;

public class AnimatableFloatValue extends BaseAnimatableValue {
   AnimatableFloatValue() {
      super((Object)0.0F);
   }

   public AnimatableFloatValue(List var1) {
      super(var1);
   }

   public BaseKeyframeAnimation createAnimation() {
      return new FloatKeyframeAnimation(this.keyframes);
   }
}
