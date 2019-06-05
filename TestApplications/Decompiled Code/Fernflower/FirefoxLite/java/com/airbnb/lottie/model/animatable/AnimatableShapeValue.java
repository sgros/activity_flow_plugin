package com.airbnb.lottie.model.animatable;

import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.ShapeKeyframeAnimation;
import java.util.List;

public class AnimatableShapeValue extends BaseAnimatableValue {
   public AnimatableShapeValue(List var1) {
      super(var1);
   }

   public BaseKeyframeAnimation createAnimation() {
      return new ShapeKeyframeAnimation(this.keyframes);
   }
}
