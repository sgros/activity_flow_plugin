// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.animatable;

import com.airbnb.lottie.animation.keyframe.PointKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;
import android.graphics.PointF;

public class AnimatablePointValue extends BaseAnimatableValue<PointF, PointF>
{
    public AnimatablePointValue(final List<Keyframe<PointF>> list) {
        super(list);
    }
    
    @Override
    public BaseKeyframeAnimation<PointF, PointF> createAnimation() {
        return (BaseKeyframeAnimation<PointF, PointF>)new PointKeyframeAnimation(super.keyframes);
    }
}
