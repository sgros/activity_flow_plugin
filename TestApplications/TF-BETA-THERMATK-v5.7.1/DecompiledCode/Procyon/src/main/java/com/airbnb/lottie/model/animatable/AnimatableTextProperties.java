// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.animatable;

public class AnimatableTextProperties
{
    public final AnimatableColorValue color;
    public final AnimatableColorValue stroke;
    public final AnimatableFloatValue strokeWidth;
    public final AnimatableFloatValue tracking;
    
    public AnimatableTextProperties(final AnimatableColorValue color, final AnimatableColorValue stroke, final AnimatableFloatValue strokeWidth, final AnimatableFloatValue tracking) {
        this.color = color;
        this.stroke = stroke;
        this.strokeWidth = strokeWidth;
        this.tracking = tracking;
    }
}
