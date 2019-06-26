package com.airbnb.lottie.model.animatable;

import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.PathKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.PointKeyframeAnimation;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;

public class AnimatablePathValue implements AnimatableValue {
   private final List keyframes;

   public AnimatablePathValue(List var1) {
      this.keyframes = var1;
   }

   public BaseKeyframeAnimation createAnimation() {
      return (BaseKeyframeAnimation)(((Keyframe)this.keyframes.get(0)).isStatic() ? new PointKeyframeAnimation(this.keyframes) : new PathKeyframeAnimation(this.keyframes));
   }

   public List getKeyframes() {
      return this.keyframes;
   }

   public boolean isStatic() {
      int var1 = this.keyframes.size();
      boolean var2 = false;
      boolean var3 = var2;
      if (var1 == 1) {
         var3 = var2;
         if (((Keyframe)this.keyframes.get(0)).isStatic()) {
            var3 = true;
         }
      }

      return var3;
   }
}
