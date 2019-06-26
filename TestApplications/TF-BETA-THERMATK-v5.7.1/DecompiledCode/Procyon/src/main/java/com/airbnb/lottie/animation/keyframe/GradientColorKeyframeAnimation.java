// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.value.Keyframe;
import java.util.List;
import com.airbnb.lottie.model.content.GradientColor;

public class GradientColorKeyframeAnimation extends KeyframeAnimation<GradientColor>
{
    private final GradientColor gradientColor;
    
    public GradientColorKeyframeAnimation(final List<Keyframe<GradientColor>> list) {
        super(list);
        int size = 0;
        final GradientColor gradientColor = list.get(0).startValue;
        if (gradientColor != null) {
            size = gradientColor.getSize();
        }
        this.gradientColor = new GradientColor(new float[size], new int[size]);
    }
    
    @Override
    GradientColor getValue(final Keyframe<GradientColor> keyframe, final float n) {
        this.gradientColor.lerp(keyframe.startValue, keyframe.endValue, n);
        return this.gradientColor;
    }
}
