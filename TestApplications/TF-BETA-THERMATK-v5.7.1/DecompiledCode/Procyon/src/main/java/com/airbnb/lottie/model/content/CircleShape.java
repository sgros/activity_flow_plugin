// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.content;

import com.airbnb.lottie.animation.content.EllipseContent;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.model.animatable.AnimatablePointValue;
import android.graphics.PointF;
import com.airbnb.lottie.model.animatable.AnimatableValue;

public class CircleShape implements ContentModel
{
    private final boolean hidden;
    private final boolean isReversed;
    private final String name;
    private final AnimatableValue<PointF, PointF> position;
    private final AnimatablePointValue size;
    
    public CircleShape(final String name, final AnimatableValue<PointF, PointF> position, final AnimatablePointValue size, final boolean isReversed, final boolean hidden) {
        this.name = name;
        this.position = position;
        this.size = size;
        this.isReversed = isReversed;
        this.hidden = hidden;
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
    
    public boolean isReversed() {
        return this.isReversed;
    }
    
    @Override
    public Content toContent(final LottieDrawable lottieDrawable, final BaseLayer baseLayer) {
        return new EllipseContent(lottieDrawable, baseLayer, this);
    }
}
