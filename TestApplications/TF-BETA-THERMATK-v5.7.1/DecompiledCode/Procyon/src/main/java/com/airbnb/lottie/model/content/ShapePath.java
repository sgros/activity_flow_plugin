// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.content;

import com.airbnb.lottie.animation.content.ShapeContent;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.model.animatable.AnimatableShapeValue;

public class ShapePath implements ContentModel
{
    private final boolean hidden;
    private final int index;
    private final String name;
    private final AnimatableShapeValue shapePath;
    
    public ShapePath(final String name, final int index, final AnimatableShapeValue shapePath, final boolean hidden) {
        this.name = name;
        this.index = index;
        this.shapePath = shapePath;
        this.hidden = hidden;
    }
    
    public String getName() {
        return this.name;
    }
    
    public AnimatableShapeValue getShapePath() {
        return this.shapePath;
    }
    
    public boolean isHidden() {
        return this.hidden;
    }
    
    @Override
    public Content toContent(final LottieDrawable lottieDrawable, final BaseLayer baseLayer) {
        return new ShapeContent(lottieDrawable, baseLayer, this);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ShapePath{name=");
        sb.append(this.name);
        sb.append(", index=");
        sb.append(this.index);
        sb.append('}');
        return sb.toString();
    }
}
