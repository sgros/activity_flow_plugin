// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.animatable;

import com.airbnb.lottie.animation.keyframe.FloatKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;

public class AnimatableFloatValue extends BaseAnimatableValue<Float, Float>
{
    AnimatableFloatValue() {
        super(0.0f);
    }
    
    public AnimatableFloatValue(final List<Keyframe<Float>> list) {
        super(list);
    }
    
    @Override
    public BaseKeyframeAnimation<Float, Float> createAnimation() {
        return (BaseKeyframeAnimation<Float, Float>)new FloatKeyframeAnimation((List<Keyframe<Float>>)this.keyframes);
    }
}
