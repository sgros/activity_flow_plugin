// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.content;

import android.graphics.Paint$Join;
import android.graphics.Paint$Cap;
import com.airbnb.lottie.animation.content.StrokeContent;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import java.util.List;
import com.airbnb.lottie.model.animatable.AnimatableColorValue;

public class ShapeStroke implements ContentModel
{
    private final LineCapType capType;
    private final AnimatableColorValue color;
    private final boolean hidden;
    private final LineJoinType joinType;
    private final List<AnimatableFloatValue> lineDashPattern;
    private final float miterLimit;
    private final String name;
    private final AnimatableFloatValue offset;
    private final AnimatableIntegerValue opacity;
    private final AnimatableFloatValue width;
    
    public ShapeStroke(final String name, final AnimatableFloatValue offset, final List<AnimatableFloatValue> lineDashPattern, final AnimatableColorValue color, final AnimatableIntegerValue opacity, final AnimatableFloatValue width, final LineCapType capType, final LineJoinType joinType, final float miterLimit, final boolean hidden) {
        this.name = name;
        this.offset = offset;
        this.lineDashPattern = lineDashPattern;
        this.color = color;
        this.opacity = opacity;
        this.width = width;
        this.capType = capType;
        this.joinType = joinType;
        this.miterLimit = miterLimit;
        this.hidden = hidden;
    }
    
    public LineCapType getCapType() {
        return this.capType;
    }
    
    public AnimatableColorValue getColor() {
        return this.color;
    }
    
    public AnimatableFloatValue getDashOffset() {
        return this.offset;
    }
    
    public LineJoinType getJoinType() {
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
    
    public AnimatableFloatValue getWidth() {
        return this.width;
    }
    
    public boolean isHidden() {
        return this.hidden;
    }
    
    @Override
    public Content toContent(final LottieDrawable lottieDrawable, final BaseLayer baseLayer) {
        return new StrokeContent(lottieDrawable, baseLayer, this);
    }
    
    public enum LineCapType
    {
        BUTT, 
        ROUND, 
        UNKNOWN;
        
        public Paint$Cap toPaintCap() {
            final int n = ShapeStroke$1.$SwitchMap$com$airbnb$lottie$model$content$ShapeStroke$LineCapType[this.ordinal()];
            if (n == 1) {
                return Paint$Cap.BUTT;
            }
            if (n != 2) {
                return Paint$Cap.SQUARE;
            }
            return Paint$Cap.ROUND;
        }
    }
    
    public enum LineJoinType
    {
        BEVEL, 
        MITER, 
        ROUND;
        
        public Paint$Join toPaintJoin() {
            final int n = ShapeStroke$1.$SwitchMap$com$airbnb$lottie$model$content$ShapeStroke$LineJoinType[this.ordinal()];
            if (n == 1) {
                return Paint$Join.BEVEL;
            }
            if (n == 2) {
                return Paint$Join.MITER;
            }
            if (n != 3) {
                return null;
            }
            return Paint$Join.ROUND;
        }
    }
}
