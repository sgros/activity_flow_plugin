package com.airbnb.lottie.model.animatable;

import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.SplitDimensionPathKeyframeAnimation;

public class AnimatableSplitDimensionPathValue implements AnimatableValue {
   private final AnimatableFloatValue animatableXDimension;
   private final AnimatableFloatValue animatableYDimension;

   public AnimatableSplitDimensionPathValue(AnimatableFloatValue var1, AnimatableFloatValue var2) {
      this.animatableXDimension = var1;
      this.animatableYDimension = var2;
   }

   public BaseKeyframeAnimation createAnimation() {
      return new SplitDimensionPathKeyframeAnimation(this.animatableXDimension.createAnimation(), this.animatableYDimension.createAnimation());
   }
}
