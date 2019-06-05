// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.content;

import java.util.ArrayList;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.model.content.ShapeTrimPath;
import java.util.List;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;

public class TrimPathContent implements Content, AnimationListener
{
    private final BaseKeyframeAnimation<?, Float> endAnimation;
    private final List<AnimationListener> listeners;
    private final String name;
    private final BaseKeyframeAnimation<?, Float> offsetAnimation;
    private final BaseKeyframeAnimation<?, Float> startAnimation;
    private final ShapeTrimPath.Type type;
    
    public TrimPathContent(final BaseLayer baseLayer, final ShapeTrimPath shapeTrimPath) {
        this.listeners = new ArrayList<AnimationListener>();
        this.name = shapeTrimPath.getName();
        this.type = shapeTrimPath.getType();
        this.startAnimation = shapeTrimPath.getStart().createAnimation();
        this.endAnimation = shapeTrimPath.getEnd().createAnimation();
        this.offsetAnimation = shapeTrimPath.getOffset().createAnimation();
        baseLayer.addAnimation(this.startAnimation);
        baseLayer.addAnimation(this.endAnimation);
        baseLayer.addAnimation(this.offsetAnimation);
        this.startAnimation.addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        this.endAnimation.addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        this.offsetAnimation.addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
    }
    
    void addListener(final AnimationListener animationListener) {
        this.listeners.add(animationListener);
    }
    
    public BaseKeyframeAnimation<?, Float> getEnd() {
        return this.endAnimation;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    public BaseKeyframeAnimation<?, Float> getOffset() {
        return this.offsetAnimation;
    }
    
    public BaseKeyframeAnimation<?, Float> getStart() {
        return this.startAnimation;
    }
    
    ShapeTrimPath.Type getType() {
        return this.type;
    }
    
    @Override
    public void onValueChanged() {
        for (int i = 0; i < this.listeners.size(); ++i) {
            this.listeners.get(i).onValueChanged();
        }
    }
    
    @Override
    public void setContents(final List<Content> list, final List<Content> list2) {
    }
}
