package com.airbnb.lottie.model.animatable;

import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.ScaleKeyframeAnimation;
import com.airbnb.lottie.value.ScaleXY;
import java.util.List;

public class AnimatableScaleValue extends BaseAnimatableValue {
   AnimatableScaleValue() {
      this(new ScaleXY(1.0F, 1.0F));
   }

   public AnimatableScaleValue(ScaleXY var1) {
      super((Object)var1);
   }

   public AnimatableScaleValue(List var1) {
      super(var1);
   }

   public BaseKeyframeAnimation createAnimation() {
      return new ScaleKeyframeAnimation(this.keyframes);
   }
}
