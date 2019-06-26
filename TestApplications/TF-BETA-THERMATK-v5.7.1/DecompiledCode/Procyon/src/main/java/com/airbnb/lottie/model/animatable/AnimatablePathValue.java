// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.animatable;

import com.airbnb.lottie.animation.keyframe.PathKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.PointKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;
import android.graphics.PointF;

public class AnimatablePathValue implements AnimatableValue<PointF, PointF>
{
    private final List<Keyframe<PointF>> keyframes;
    
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
    
    @Override
    public List<Keyframe<PointF>> getKeyframes() {
        return this.keyframes;
    }
    
    @Override
    public boolean isStatic() {
        final int size = this.keyframes.size();
        boolean b = false;
        if (size == 1) {
            b = b;
            if (this.keyframes.get(0).isStatic()) {
                b = true;
            }
        }
        return b;
    }
}
