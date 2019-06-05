// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.animatable;

import com.airbnb.lottie.animation.keyframe.SplitDimensionPathKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import android.graphics.PointF;

public class AnimatableSplitDimensionPathValue implements AnimatableValue<PointF, PointF>
{
    private final AnimatableFloatValue animatableXDimension;
    private final AnimatableFloatValue animatableYDimension;
    
    public AnimatableSplitDimensionPathValue(final AnimatableFloatValue animatableXDimension, final AnimatableFloatValue animatableYDimension) {
        this.animatableXDimension = animatableXDimension;
        this.animatableYDimension = animatableYDimension;
    }
    
    @Override
    public BaseKeyframeAnimation<PointF, PointF> createAnimation() {
        return new SplitDimensionPathKeyframeAnimation(this.animatableXDimension.createAnimation(), this.animatableYDimension.createAnimation());
    }
}
