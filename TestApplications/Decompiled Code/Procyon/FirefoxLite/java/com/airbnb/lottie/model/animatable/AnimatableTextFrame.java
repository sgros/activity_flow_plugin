// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.animatable;

import com.airbnb.lottie.animation.keyframe.TextKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;
import com.airbnb.lottie.model.DocumentData;

public class AnimatableTextFrame extends BaseAnimatableValue<DocumentData, DocumentData>
{
    public AnimatableTextFrame(final List<Keyframe<DocumentData>> list) {
        super(list);
    }
    
    @Override
    public TextKeyframeAnimation createAnimation() {
        return new TextKeyframeAnimation((List<Keyframe<DocumentData>>)this.keyframes);
    }
}
