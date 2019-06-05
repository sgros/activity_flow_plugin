package com.airbnb.lottie.model.animatable;

import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;

public interface AnimatableValue {
   BaseKeyframeAnimation createAnimation();
}
