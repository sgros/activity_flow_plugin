// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.content;

import com.airbnb.lottie.animation.content.TrimPathContent;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;

public class ShapeTrimPath implements ContentModel
{
    private final AnimatableFloatValue end;
    private final String name;
    private final AnimatableFloatValue offset;
    private final AnimatableFloatValue start;
    private final Type type;
    
    public ShapeTrimPath(final String name, final Type type, final AnimatableFloatValue start, final AnimatableFloatValue end, final AnimatableFloatValue offset) {
        this.name = name;
        this.type = type;
        this.start = start;
        this.end = end;
        this.offset = offset;
    }
    
    public AnimatableFloatValue getEnd() {
        return this.end;
    }
    
    public String getName() {
        return this.name;
    }
    
    public AnimatableFloatValue getOffset() {
        return this.offset;
    }
    
    public AnimatableFloatValue getStart() {
        return this.start;
    }
    
    public Type getType() {
        return this.type;
    }
    
    @Override
    public Content toContent(final LottieDrawable lottieDrawable, final BaseLayer baseLayer) {
        return new TrimPathContent(baseLayer, this);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Trim Path: {start: ");
        sb.append(this.start);
        sb.append(", end: ");
        sb.append(this.end);
        sb.append(", offset: ");
        sb.append(this.offset);
        sb.append("}");
        return sb.toString();
    }
    
    public enum Type
    {
        Individually, 
        Simultaneously;
        
        public static Type forId(final int i) {
            switch (i) {
                default: {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unknown trim path type ");
                    sb.append(i);
                    throw new IllegalArgumentException(sb.toString());
                }
                case 2: {
                    return Type.Individually;
                }
                case 1: {
                    return Type.Simultaneously;
                }
            }
        }
    }
}
