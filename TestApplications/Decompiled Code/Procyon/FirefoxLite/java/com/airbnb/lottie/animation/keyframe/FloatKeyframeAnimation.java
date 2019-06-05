// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;

public class FloatKeyframeAnimation extends KeyframeAnimation<Float>
{
    public FloatKeyframeAnimation(final List<Keyframe<Float>> list) {
        super(list);
    }
    
    @Override
    Float getValue(final Keyframe<Float> keyframe, final float n) {
        if (keyframe.startValue != null && keyframe.endValue != null) {
            if (this.valueCallback != null) {
                final Float n2 = (Float)this.valueCallback.getValueInternal(keyframe.startFrame, keyframe.endFrame, (A)keyframe.startValue, (A)keyframe.endValue, n, this.getLinearCurrentKeyframeProgress(), this.getProgress());
                if (n2 != null) {
                    return n2;
                }
            }
            return MiscUtils.lerp(keyframe.startValue, keyframe.endValue, n);
        }
        throw new IllegalStateException("Missing values for keyframe.");
    }
}
