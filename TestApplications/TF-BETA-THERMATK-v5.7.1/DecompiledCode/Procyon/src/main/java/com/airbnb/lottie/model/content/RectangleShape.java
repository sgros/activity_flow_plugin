// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.content;

import com.airbnb.lottie.animation.content.RectangleContent;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.model.animatable.AnimatablePointValue;
import android.graphics.PointF;
import com.airbnb.lottie.model.animatable.AnimatableValue;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;

public class RectangleShape implements ContentModel
{
    private final AnimatableFloatValue cornerRadius;
    private final boolean hidden;
    private final String name;
    private final AnimatableValue<PointF, PointF> position;
    private final AnimatablePointValue size;
    
    public RectangleShape(final String name, final AnimatableValue<PointF, PointF> position, final AnimatablePointValue size, final AnimatableFloatValue cornerRadius, final boolean hidden) {
        this.name = name;
        this.position = position;
        this.size = size;
        this.cornerRadius = cornerRadius;
        this.hidden = hidden;
    }
    
    public AnimatableFloatValue getCornerRadius() {
        return this.cornerRadius;
    }
    
    public String getName() {
        return this.name;
    }
    
    public AnimatableValue<PointF, PointF> getPosition() {
        return this.position;
    }
    
    public AnimatablePointValue getSize() {
        return this.size;
    }
    
    public boolean isHidden() {
        return this.hidden;
    }
    
    @Override
    public Content toContent(final LottieDrawable lottieDrawable, final BaseLayer baseLayer) {
        return new RectangleContent(lottieDrawable, baseLayer, this);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("RectangleShape{position=");
        sb.append(this.position);
        sb.append(", size=");
        sb.append(this.size);
        sb.append('}');
        return sb.toString();
    }
}
