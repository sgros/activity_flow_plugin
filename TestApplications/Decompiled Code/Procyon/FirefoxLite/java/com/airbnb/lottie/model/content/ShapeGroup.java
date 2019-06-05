// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.content;

import java.util.Arrays;
import com.airbnb.lottie.animation.content.ContentGroup;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.LottieDrawable;
import java.util.List;

public class ShapeGroup implements ContentModel
{
    private final List<ContentModel> items;
    private final String name;
    
    public ShapeGroup(final String name, final List<ContentModel> items) {
        this.name = name;
        this.items = items;
    }
    
    public List<ContentModel> getItems() {
        return this.items;
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public Content toContent(final LottieDrawable lottieDrawable, final BaseLayer baseLayer) {
        return new ContentGroup(lottieDrawable, baseLayer, this);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ShapeGroup{name='");
        sb.append(this.name);
        sb.append("' Shapes: ");
        sb.append(Arrays.toString(this.items.toArray()));
        sb.append('}');
        return sb.toString();
    }
}
