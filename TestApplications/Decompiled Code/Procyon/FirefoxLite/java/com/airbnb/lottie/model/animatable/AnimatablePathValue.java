// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.animatable;

import com.airbnb.lottie.animation.keyframe.PathKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.PointKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import java.util.Collections;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;
import android.graphics.PointF;

public class AnimatablePathValue implements AnimatableValue<PointF, PointF>
{
    private final List<Keyframe<PointF>> keyframes;
    
    public AnimatablePathValue() {
        this.keyframes = Collections.singletonList(new Keyframe<PointF>(new PointF(0.0f, 0.0f)));
    }
    
    public AnimatablePathValue(final List<Keyframe<PointF>> keyframes) {
        this.keyframes = keyframes;
    }
    
    @Override
    public BaseKeyframeAnimation<PointF, PointF> createAnimation() {
        if (this.keyframes.get(0).isStatic()) {
            return (BaseKeyframeAnimation<PointF, PointF>)new PointKeyframeAnimation(this.keyframes);
        }
        return (BaseKeyframeAnimation<PointF, PointF>)new PathKeyframeAnimation(this.keyframes);
    }
}
