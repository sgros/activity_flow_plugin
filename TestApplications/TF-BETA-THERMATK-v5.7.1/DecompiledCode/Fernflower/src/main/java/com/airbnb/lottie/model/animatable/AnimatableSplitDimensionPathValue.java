package com.airbnb.lottie.model.animatable;

import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.SplitDimensionPathKeyframeAnimation;
import java.util.List;

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

   public List getKeyframes() {
      throw new UnsupportedOperationException("Cannot call getKeyframes on AnimatableSplitDimensionPathValue.");
   }

   public boolean isStatic() {
      boolean var1;
      if (this.animatableXDimension.isStatic() && this.animatableYDimension.isStatic()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }
}
