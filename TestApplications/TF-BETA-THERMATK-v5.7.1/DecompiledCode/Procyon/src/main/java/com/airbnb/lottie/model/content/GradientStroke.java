// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.content;

import com.airbnb.lottie.animation.content.GradientStrokeContent;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import java.util.List;
import com.airbnb.lottie.model.animatable.AnimatableGradientColorValue;
import com.airbnb.lottie.model.animatable.AnimatablePointValue;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;

public class GradientStroke implements ContentModel
{
    private final ShapeStroke.LineCapType capType;
    private final AnimatableFloatValue dashOffset;
    private final AnimatablePointValue endPoint;
    private final AnimatableGradientColorValue gradientColor;
    private final GradientType gradientType;
    private final boolean hidden;
    private final ShapeStroke.LineJoinType joinType;
    private final List<AnimatableFloatValue> lineDashPattern;
    private final float miterLimit;
    private final String name;
    private final AnimatableIntegerValue opacity;
    private final AnimatablePointValue startPoint;
    private final AnimatableFloatValue width;
    
    public GradientStroke(final String name, final GradientType gradientType, final AnimatableGradientColorValue gradientColor, final AnimatableIntegerValue opacity, final AnimatablePointValue startPoint, final AnimatablePointValue endPoint, final AnimatableFloatValue width, final ShapeStroke.LineCapType capType, final ShapeStroke.LineJoinType joinType, final float miterLimit, final List<AnimatableFloatValue> lineDashPattern, final AnimatableFloatValue dashOffset, final boolean hidden) {
        this.name = name;
        this.gradientType = gradientType;
        this.gradientColor = gradientColor;
        this.opacity = opacity;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.width = width;
        this.capType = capType;
        this.joinType = joinType;
        this.miterLimit = miterLimit;
        this.lineDashPattern = lineDashPattern;
        this.dashOffset = dashOffset;
        this.hidden = hidden;
    }
    
    public ShapeStroke.LineCapType getCapType() {
        return this.capType;
    }
    
    public AnimatableFloatValue getDashOffset() {
        return this.dashOffset;
    }
    
    public AnimatablePointValue getEndPoint() {
        return this.endPoint;
    }
    
    public AnimatableGradientColorValue getGradientColor() {
        return this.gradientColor;
    }
    
    public GradientType getGradientType() {
        return this.gradientType;
    }
    
    public ShapeStroke.LineJoinType getJoinType() {
        return this.joinType;
    }
    
    public List<AnimatableFloatValue> getLineDashPattern() {
        return this.lineDashPattern;
    }
    
    public float getMiterLimit() {
        return this.miterLimit;
    }
    
    public String getName() {
        return this.name;
    }
    
    public AnimatableIntegerValue getOpacity() {
        return this.opacity;
    }
    
    public AnimatablePointValue getStartPoint() {
        return this.startPoint;
    }
    
    public AnimatableFloatValue getWidth() {
        return this.width;
    }
    
    public boolean isHidden() {
        return this.hidden;
    }
    
    @Override
    public Content toContent(final LottieDrawable lottieDrawable, final BaseLayer baseLayer) {
        return new GradientStrokeContent(lottieDrawable, baseLayer, this);
    }
}
