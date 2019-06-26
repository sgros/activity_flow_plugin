// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.content;

import com.airbnb.lottie.animation.content.PolystarContent;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.LottieDrawable;
import android.graphics.PointF;
import com.airbnb.lottie.model.animatable.AnimatableValue;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;

public class PolystarShape implements ContentModel
{
    private final boolean hidden;
    private final AnimatableFloatValue innerRadius;
    private final AnimatableFloatValue innerRoundedness;
    private final String name;
    private final AnimatableFloatValue outerRadius;
    private final AnimatableFloatValue outerRoundedness;
    private final AnimatableFloatValue points;
    private final AnimatableValue<PointF, PointF> position;
    private final AnimatableFloatValue rotation;
    private final Type type;
    
    public PolystarShape(final String name, final Type type, final AnimatableFloatValue points, final AnimatableValue<PointF, PointF> position, final AnimatableFloatValue rotation, final AnimatableFloatValue innerRadius, final AnimatableFloatValue outerRadius, final AnimatableFloatValue innerRoundedness, final AnimatableFloatValue outerRoundedness, final boolean hidden) {
        this.name = name;
        this.type = type;
        this.points = points;
        this.position = position;
        this.rotation = rotation;
        this.innerRadius = innerRadius;
        this.outerRadius = outerRadius;
        this.innerRoundedness = innerRoundedness;
        this.outerRoundedness = outerRoundedness;
        this.hidden = hidden;
    }
    
    public AnimatableFloatValue getInnerRadius() {
        return this.innerRadius;
    }
    
    public AnimatableFloatValue getInnerRoundedness() {
        return this.innerRoundedness;
    }
    
    public String getName() {
        return this.name;
    }
    
    public AnimatableFloatValue getOuterRadius() {
        return this.outerRadius;
    }
    
    public AnimatableFloatValue getOuterRoundedness() {
        return this.outerRoundedness;
    }
    
    public AnimatableFloatValue getPoints() {
        return this.points;
    }
    
    public AnimatableValue<PointF, PointF> getPosition() {
        return this.position;
    }
    
    public AnimatableFloatValue getRotation() {
        return this.rotation;
    }
    
    public Type getType() {
        return this.type;
    }
    
    public boolean isHidden() {
        return this.hidden;
    }
    
    @Override
    public Content toContent(final LottieDrawable lottieDrawable, final BaseLayer baseLayer) {
        return new PolystarContent(lottieDrawable, baseLayer, this);
    }
    
    public enum Type
    {
        POLYGON(2), 
        STAR(1);
        
        private final int value;
        
        private Type(final int value) {
            this.value = value;
        }
        
        public static Type forValue(final int n) {
            for (final Type type : values()) {
                if (type.value == n) {
                    return type;
                }
            }
            return null;
        }
    }
}
