package com.airbnb.lottie.model.animatable;

import android.graphics.PointF;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.PathKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.PointKeyframeAnimation;
import com.airbnb.lottie.value.Keyframe;
import java.util.Collections;
import java.util.List;

public class AnimatablePathValue implements AnimatableValue {
   private final List keyframes;

   public AnimatablePathValue() {
      this.keyframes = Collections.singletonList(new Keyframe(new PointF(0.0F, 0.0F)));
   }

   public AnimatablePathValue(List var1) {
      this.keyframes = var1;
   }

   public BaseKeyframeAnimation createAnimation() {
      return (BaseKeyframeAnimation)(((Keyframe)this.keyframes.get(0)).isStatic() ? new PointKeyframeAnimation(this.keyframes) : new PathKeyframeAnimation(this.keyframes));
   }
}
