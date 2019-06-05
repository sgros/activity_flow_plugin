// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.animatable;

import com.airbnb.lottie.animation.keyframe.ShapeKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;
import android.graphics.Path;
import com.airbnb.lottie.model.content.ShapeData;

public class AnimatableShapeValue extends BaseAnimatableValue<ShapeData, Path>
{
    public AnimatableShapeValue(final List<Keyframe<ShapeData>> list) {
        super(list);
    }
    
    @Override
    public BaseKeyframeAnimation<ShapeData, Path> createAnimation() {
        return new ShapeKeyframeAnimation((List<Keyframe<ShapeData>>)this.keyframes);
    }
}
