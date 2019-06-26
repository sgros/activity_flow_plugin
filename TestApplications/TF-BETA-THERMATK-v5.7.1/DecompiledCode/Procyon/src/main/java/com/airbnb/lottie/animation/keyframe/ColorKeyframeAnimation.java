// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.utils.GammaEvaluator;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;

public class ColorKeyframeAnimation extends KeyframeAnimation<Integer>
{
    public ColorKeyframeAnimation(final List<Keyframe<Integer>> list) {
        super(list);
    }
    
    public int getIntValue() {
        return this.getIntValue(((BaseKeyframeAnimation<Integer, A>)this).getCurrentKeyframe(), this.getInterpolatedCurrentKeyframeProgress());
    }
    
    public int getIntValue(final Keyframe<Integer> keyframe, final float n) {
        final Integer startValue = keyframe.startValue;
        if (startValue != null && keyframe.endValue != null) {
            final int intValue = startValue;
            final int intValue2 = keyframe.endValue;
            final LottieValueCallback<A> valueCallback = (LottieValueCallback<A>)super.valueCallback;
            if (valueCallback != null) {
                final Integer n2 = (Integer)valueCallback.getValueInternal(keyframe.startFrame, keyframe.endFrame, (A)intValue, (A)intValue2, n, this.getLinearCurrentKeyframeProgress(), this.getProgress());
                if (n2 != null) {
                    return n2;
                }
            }
            return GammaEvaluator.evaluate(MiscUtils.clamp(n, 0.0f, 1.0f), intValue, intValue2);
        }
        throw new IllegalStateException("Missing values for keyframe.");
    }
    
    @Override
    Integer getValue(final Keyframe<Integer> keyframe, final float n) {
        return this.getIntValue(keyframe, n);
    }
}
