// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.value.Keyframe;
import java.util.List;
import com.airbnb.lottie.model.DocumentData;

public class TextKeyframeAnimation extends KeyframeAnimation<DocumentData>
{
    public TextKeyframeAnimation(final List<Keyframe<DocumentData>> list) {
        super(list);
    }
    
    @Override
    DocumentData getValue(final Keyframe<DocumentData> keyframe, final float n) {
        return keyframe.startValue;
    }
}
