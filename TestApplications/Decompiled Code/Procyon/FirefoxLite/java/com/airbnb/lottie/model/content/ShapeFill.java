// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.content;

import com.airbnb.lottie.animation.content.FillContent;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import android.graphics.Path$FillType;
import com.airbnb.lottie.model.animatable.AnimatableColorValue;

public class ShapeFill implements ContentModel
{
    private final AnimatableColorValue color;
    private final boolean fillEnabled;
    private final Path$FillType fillType;
    private final String name;
    private final AnimatableIntegerValue opacity;
    
    public ShapeFill(final String name, final boolean fillEnabled, final Path$FillType fillType, final AnimatableColorValue color, final AnimatableIntegerValue opacity) {
        this.name = name;
        this.fillEnabled = fillEnabled;
        this.fillType = fillType;
        this.color = color;
        this.opacity = opacity;
    }
    
    public AnimatableColorValue getColor() {
        return this.color;
    }
    
    public Path$FillType getFillType() {
        return this.fillType;
    }
    
    public String getName() {
        return this.name;
    }
    
    public AnimatableIntegerValue getOpacity() {
        return this.opacity;
    }
    
    @Override
    public Content toContent(final LottieDrawable lottieDrawable, final BaseLayer baseLayer) {
        return new FillContent(lottieDrawable, baseLayer, this);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ShapeFill{color=, fillEnabled=");
        sb.append(this.fillEnabled);
        sb.append('}');
        return sb.toString();
    }
}
