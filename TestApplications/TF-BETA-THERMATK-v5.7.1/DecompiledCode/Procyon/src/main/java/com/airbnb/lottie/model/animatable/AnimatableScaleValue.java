// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.animatable;

import com.airbnb.lottie.animation.keyframe.ScaleKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;
import com.airbnb.lottie.value.ScaleXY;

public class AnimatableScaleValue extends BaseAnimatableValue<ScaleXY, ScaleXY>
{
    public AnimatableScaleValue(final List<Keyframe<ScaleXY>> list) {
        super(list);
    }
    
    @Override
    public BaseKeyframeAnimation<ScaleXY, ScaleXY> createAnimation() {
        return (BaseKeyframeAnimation<ScaleXY, ScaleXY>)new ScaleKeyframeAnimation(super.keyframes);
    }
}
