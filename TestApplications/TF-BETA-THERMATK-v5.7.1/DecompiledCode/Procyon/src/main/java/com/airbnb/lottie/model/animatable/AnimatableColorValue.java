// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.animatable;

import com.airbnb.lottie.animation.keyframe.ColorKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;

public class AnimatableColorValue extends BaseAnimatableValue<Integer, Integer>
{
    public AnimatableColorValue(final List<Keyframe<Integer>> list) {
        super(list);
    }
    
    @Override
    public BaseKeyframeAnimation<Integer, Integer> createAnimation() {
        return (BaseKeyframeAnimation<Integer, Integer>)new ColorKeyframeAnimation(super.keyframes);
    }
}
