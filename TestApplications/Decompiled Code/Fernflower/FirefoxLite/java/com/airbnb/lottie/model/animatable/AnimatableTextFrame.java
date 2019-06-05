package com.airbnb.lottie.model.animatable;

import com.airbnb.lottie.animation.keyframe.TextKeyframeAnimation;
import java.util.List;

public class AnimatableTextFrame extends BaseAnimatableValue {
   public AnimatableTextFrame(List var1) {
      super(var1);
   }

   public TextKeyframeAnimation createAnimation() {
      return new TextKeyframeAnimation(this.keyframes);
   }
}
