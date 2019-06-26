// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.animatable;

import com.airbnb.lottie.value.Keyframe;
import java.util.List;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;

public interface AnimatableValue<K, A>
{
    BaseKeyframeAnimation<K, A> createAnimation();
    
    List<Keyframe<K>> getKeyframes();
    
    boolean isStatic();
}
