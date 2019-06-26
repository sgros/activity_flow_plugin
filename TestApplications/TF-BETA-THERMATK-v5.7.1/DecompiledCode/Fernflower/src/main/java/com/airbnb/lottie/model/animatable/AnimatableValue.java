package com.airbnb.lottie.model.animatable;

import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import java.util.List;

public interface AnimatableValue {
   BaseKeyframeAnimation createAnimation();

   List getKeyframes();

   boolean isStatic();
}
