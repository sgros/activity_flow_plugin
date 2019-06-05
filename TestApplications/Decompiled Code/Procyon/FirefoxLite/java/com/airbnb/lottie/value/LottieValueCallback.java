// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.value;

import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;

public class LottieValueCallback<T>
{
    private BaseKeyframeAnimation<?, ?> animation;
    private final LottieFrameInfo<T> frameInfo;
    protected T value;
    
    public LottieValueCallback() {
        this.frameInfo = new LottieFrameInfo<T>();
        this.value = null;
    }
    
    public LottieValueCallback(final T value) {
        this.frameInfo = new LottieFrameInfo<T>();
        this.value = null;
        this.value = value;
    }
    
    public T getValue(final LottieFrameInfo<T> lottieFrameInfo) {
        return this.value;
    }
    
    public final T getValueInternal(final float n, final float n2, final T t, final T t2, final float n3, final float n4, final float n5) {
        return this.getValue(this.frameInfo.set(n, n2, t, t2, n3, n4, n5));
    }
    
    public final void setAnimation(final BaseKeyframeAnimation<?, ?> animation) {
        this.animation = animation;
    }
}
