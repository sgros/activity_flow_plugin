// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.content;

import com.airbnb.lottie.animation.content.GradientFillContent;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableGradientColorValue;
import android.graphics.Path$FillType;
import com.airbnb.lottie.model.animatable.AnimatablePointValue;

public class GradientFill implements ContentModel
{
    private final AnimatablePointValue endPoint;
    private final Path$FillType fillType;
    private final AnimatableGradientColorValue gradientColor;
    private final GradientType gradientType;
    private final AnimatableFloatValue highlightAngle;
    private final AnimatableFloatValue highlightLength;
    private final String name;
    private final AnimatableIntegerValue opacity;
    private final AnimatablePointValue startPoint;
    
    public GradientFill(final String name, final GradientType gradientType, final Path$FillType fillType, final AnimatableGradientColorValue gradientColor, final AnimatableIntegerValue opacity, final AnimatablePointValue startPoint, final AnimatablePointValue endPoint, final AnimatableFloatValue highlightLength, final AnimatableFloatValue highlightAngle) {
        this.gradientType = gradientType;
        this.fillType = fillType;
        this.gradientColor = gradientColor;
        this.opacity = opacity;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.name = name;
        this.highlightLength = highlightLength;
        this.highlightAngle = highlightAngle;
    }
    
    public AnimatablePointValue getEndPoint() {
        return this.endPoint;
    }
    
    public Path$FillType getFillType() {
        return this.fillType;
    }
    
    public AnimatableGradientColorValue getGradientColor() {
        return this.gradientColor;
    }
    
    public GradientType getGradientType() {
        return this.gradientType;
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
    
    @Override
    public Content toContent(final LottieDrawable lottieDrawable, final BaseLayer baseLayer) {
        return new GradientFillContent(lottieDrawable, baseLayer, this);
    }
}
