// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.animatable;

import com.airbnb.lottie.animation.keyframe.GradientColorKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;
import com.airbnb.lottie.model.content.GradientColor;

public class AnimatableGradientColorValue extends BaseAnimatableValue<GradientColor, GradientColor>
{
    public AnimatableGradientColorValue(final List<Keyframe<GradientColor>> list) {
        super(list);
    }
    
    @Override
    public BaseKeyframeAnimation<GradientColor, GradientColor> createAnimation() {
        return (BaseKeyframeAnimation<GradientColor, GradientColor>)new GradientColorKeyframeAnimation((List<Keyframe<GradientColor>>)this.keyframes);
    }
}
