// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;

public class FloatKeyframeAnimation extends KeyframeAnimation<Float>
{
    public FloatKeyframeAnimation(final List<Keyframe<Float>> list) {
        super(list);
    }
    
    public float getFloatValue() {
        return this.getFloatValue(((BaseKeyframeAnimation<Float, A>)this).getCurrentKeyframe(), this.getInterpolatedCurrentKeyframeProgress());
    }
    
    float getFloatValue(final Keyframe<Float> keyframe, final float n) {
        if (keyframe.startValue != null && keyframe.endValue != null) {
            final LottieValueCallback<A> valueCallback = (LottieValueCallback<A>)super.valueCallback;
            if (valueCallback != null) {
                final Float n2 = (Float)valueCallback.getValueInternal(keyframe.startFrame, keyframe.endFrame, (A)keyframe.startValue, (A)keyframe.endValue, n, this.getLinearCurrentKeyframeProgress(), this.getProgress());
                if (n2 != null) {
                    return n2;
                }
            }
            return MiscUtils.lerp(keyframe.getStartValueFloat(), keyframe.getEndValueFloat(), n);
        }
        throw new IllegalStateException("Missing values for keyframe.");
    }
    
    @Override
    Float getValue(final Keyframe<Float> keyframe, final float n) {
        return this.getFloatValue(keyframe, n);
    }
}
