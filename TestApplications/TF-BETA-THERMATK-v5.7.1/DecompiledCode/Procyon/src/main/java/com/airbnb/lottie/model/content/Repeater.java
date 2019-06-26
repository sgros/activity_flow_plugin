// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.content;

import com.airbnb.lottie.animation.content.RepeaterContent;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.model.animatable.AnimatableTransform;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;

public class Repeater implements ContentModel
{
    private final AnimatableFloatValue copies;
    private final boolean hidden;
    private final String name;
    private final AnimatableFloatValue offset;
    private final AnimatableTransform transform;
    
    public Repeater(final String name, final AnimatableFloatValue copies, final AnimatableFloatValue offset, final AnimatableTransform transform, final boolean hidden) {
        this.name = name;
        this.copies = copies;
        this.offset = offset;
        this.transform = transform;
        this.hidden = hidden;
    }
    
    public AnimatableFloatValue getCopies() {
        return this.copies;
    }
    
    public String getName() {
        return this.name;
    }
    
    public AnimatableFloatValue getOffset() {
        return this.offset;
    }
    
    public AnimatableTransform getTransform() {
        return this.transform;
    }
    
    public boolean isHidden() {
        return this.hidden;
    }
    
    @Override
    public Content toContent(final LottieDrawable lottieDrawable, final BaseLayer baseLayer) {
        return new RepeaterContent(lottieDrawable, baseLayer, this);
    }
}
