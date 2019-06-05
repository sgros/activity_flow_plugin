// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.utils.GammaEvaluator;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;

public class ColorKeyframeAnimation extends KeyframeAnimation<Integer>
{
    public ColorKeyframeAnimation(final List<Keyframe<Integer>> list) {
        super(list);
    }
    
    public Integer getValue(final Keyframe<Integer> keyframe, final float n) {
        if (keyframe.startValue != null && keyframe.endValue != null) {
            final int intValue = keyframe.startValue;
            final int intValue2 = keyframe.endValue;
            if (this.valueCallback != null) {
                final Integer n2 = (Integer)this.valueCallback.getValueInternal(keyframe.startFrame, keyframe.endFrame, (A)intValue, (A)intValue2, n, this.getLinearCurrentKeyframeProgress(), this.getProgress());
                if (n2 != null) {
                    return n2;
                }
            }
            return GammaEvaluator.evaluate(n, intValue, intValue2);
        }
        throw new IllegalStateException("Missing values for keyframe.");
    }
}
