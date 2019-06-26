// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.animatable;

import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.keyframe.TransformKeyframeAnimation;
import android.graphics.PointF;
import com.airbnb.lottie.model.content.ContentModel;
import com.airbnb.lottie.animation.content.ModifierContent;

public class AnimatableTransform implements ModifierContent, ContentModel
{
    private final AnimatablePathValue anchorPoint;
    private final AnimatableFloatValue endOpacity;
    private final AnimatableIntegerValue opacity;
    private final AnimatableValue<PointF, PointF> position;
    private final AnimatableFloatValue rotation;
    private final AnimatableScaleValue scale;
    private final AnimatableFloatValue skew;
    private final AnimatableFloatValue skewAngle;
    private final AnimatableFloatValue startOpacity;
    
    public AnimatableTransform() {
        this(null, null, null, null, null, null, null, null, null);
    }
    
    public AnimatableTransform(final AnimatablePathValue anchorPoint, final AnimatableValue<PointF, PointF> position, final AnimatableScaleValue scale, final AnimatableFloatValue rotation, final AnimatableIntegerValue opacity, final AnimatableFloatValue startOpacity, final AnimatableFloatValue endOpacity, final AnimatableFloatValue skew, final AnimatableFloatValue skewAngle) {
        this.anchorPoint = anchorPoint;
        this.position = position;
        this.scale = scale;
        this.rotation = rotation;
        this.opacity = opacity;
        this.startOpacity = startOpacity;
        this.endOpacity = endOpacity;
        this.skew = skew;
        this.skewAngle = skewAngle;
    }
    
    public TransformKeyframeAnimation createAnimation() {
        return new TransformKeyframeAnimation(this);
    }
    
    public AnimatablePathValue getAnchorPoint() {
        return this.anchorPoint;
    }
    
    public AnimatableFloatValue getEndOpacity() {
        return this.endOpacity;
    }
    
    public AnimatableIntegerValue getOpacity() {
        return this.opacity;
    }
    
    public AnimatableValue<PointF, PointF> getPosition() {
        return this.position;
    }
    
    public AnimatableFloatValue getRotation() {
        return this.rotation;
    }
    
    public AnimatableScaleValue getScale() {
        return this.scale;
    }
    
    public AnimatableFloatValue getSkew() {
        return this.skew;
    }
    
    public AnimatableFloatValue getSkewAngle() {
        return this.skewAngle;
    }
    
    public AnimatableFloatValue getStartOpacity() {
        return this.startOpacity;
    }
    
    @Override
    public Content toContent(final LottieDrawable lottieDrawable, final BaseLayer baseLayer) {
        return null;
    }
}
