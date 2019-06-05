// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.keyframe;

import java.util.ArrayList;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.List;
import com.airbnb.lottie.value.Keyframe;

public abstract class BaseKeyframeAnimation<K, A>
{
    private Keyframe<K> cachedKeyframe;
    private boolean isDiscrete;
    private final List<? extends Keyframe<K>> keyframes;
    final List<AnimationListener> listeners;
    private float progress;
    protected LottieValueCallback<A> valueCallback;
    
    BaseKeyframeAnimation(final List<? extends Keyframe<K>> keyframes) {
        this.listeners = new ArrayList<AnimationListener>();
        this.isDiscrete = false;
        this.progress = 0.0f;
        this.keyframes = keyframes;
    }
    
    private Keyframe<K> getCurrentKeyframe() {
        if (this.cachedKeyframe != null && this.cachedKeyframe.containsProgress(this.progress)) {
            return this.cachedKeyframe;
        }
        Keyframe<K> cachedKeyframe;
        final Keyframe keyframe = cachedKeyframe = (Keyframe<K>)this.keyframes.get(this.keyframes.size() - 1);
        if (this.progress < keyframe.getStartProgress()) {
            int i = this.keyframes.size() - 1;
            cachedKeyframe = (Keyframe<K>)keyframe;
            while (i >= 0) {
                cachedKeyframe = (Keyframe<K>)this.keyframes.get(i);
                if (cachedKeyframe.containsProgress(this.progress)) {
                    break;
                }
                --i;
            }
        }
        return this.cachedKeyframe = cachedKeyframe;
    }
    
    private float getInterpolatedCurrentKeyframeProgress() {
        final Keyframe<K> currentKeyframe = this.getCurrentKeyframe();
        if (currentKeyframe.isStatic()) {
            return 0.0f;
        }
        return currentKeyframe.interpolator.getInterpolation(this.getLinearCurrentKeyframeProgress());
    }
    
    private float getStartDelayProgress() {
        float startProgress;
        if (this.keyframes.isEmpty()) {
            startProgress = 0.0f;
        }
        else {
            startProgress = ((Keyframe)this.keyframes.get(0)).getStartProgress();
        }
        return startProgress;
    }
    
    public void addUpdateListener(final AnimationListener animationListener) {
        this.listeners.add(animationListener);
    }
    
    float getEndProgress() {
        float endProgress;
        if (this.keyframes.isEmpty()) {
            endProgress = 1.0f;
        }
        else {
            endProgress = ((Keyframe)this.keyframes.get(this.keyframes.size() - 1)).getEndProgress();
        }
        return endProgress;
    }
    
    float getLinearCurrentKeyframeProgress() {
        if (this.isDiscrete) {
            return 0.0f;
        }
        final Keyframe<K> currentKeyframe = this.getCurrentKeyframe();
        if (currentKeyframe.isStatic()) {
            return 0.0f;
        }
        return (this.progress - currentKeyframe.getStartProgress()) / (currentKeyframe.getEndProgress() - currentKeyframe.getStartProgress());
    }
    
    public float getProgress() {
        return this.progress;
    }
    
    public A getValue() {
        return this.getValue(this.getCurrentKeyframe(), this.getInterpolatedCurrentKeyframeProgress());
    }
    
    abstract A getValue(final Keyframe<K> p0, final float p1);
    
    public void notifyListeners() {
        for (int i = 0; i < this.listeners.size(); ++i) {
            this.listeners.get(i).onValueChanged();
        }
    }
    
    public void setIsDiscrete() {
        this.isDiscrete = true;
    }
    
    public void setProgress(final float n) {
        float progress;
        if (n < this.getStartDelayProgress()) {
            progress = this.getStartDelayProgress();
        }
        else {
            progress = n;
            if (n > this.getEndProgress()) {
                progress = this.getEndProgress();
            }
        }
        if (progress == this.progress) {
            return;
        }
        this.progress = progress;
        this.notifyListeners();
    }
    
    public void setValueCallback(final LottieValueCallback<A> valueCallback) {
        if (this.valueCallback != null) {
            this.valueCallback.setAnimation(null);
        }
        if ((this.valueCallback = valueCallback) != null) {
            valueCallback.setAnimation(this);
        }
    }
    
    public interface AnimationListener
    {
        void onValueChanged();
    }
}
