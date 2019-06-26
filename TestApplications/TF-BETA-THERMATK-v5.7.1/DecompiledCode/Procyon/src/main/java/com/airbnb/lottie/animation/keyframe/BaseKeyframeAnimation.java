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
    private float cachedEndProgress;
    private A cachedGetValue;
    private Keyframe<K> cachedGetValueKeyframe;
    private float cachedGetValueProgress;
    private Keyframe<K> cachedKeyframe;
    private float cachedStartDelayProgress;
    private boolean isDiscrete;
    private final List<? extends Keyframe<K>> keyframes;
    final List<AnimationListener> listeners;
    private float progress;
    protected LottieValueCallback<A> valueCallback;
    
    BaseKeyframeAnimation(final List<? extends Keyframe<K>> keyframes) {
        this.listeners = new ArrayList<AnimationListener>(1);
        this.isDiscrete = false;
        this.progress = 0.0f;
        this.cachedGetValueProgress = -1.0f;
        this.cachedGetValue = null;
        this.cachedStartDelayProgress = -1.0f;
        this.cachedEndProgress = -1.0f;
        this.keyframes = keyframes;
    }
    
    private float getStartDelayProgress() {
        if (this.cachedStartDelayProgress == -1.0f) {
            float startProgress;
            if (this.keyframes.isEmpty()) {
                startProgress = 0.0f;
            }
            else {
                startProgress = ((Keyframe)this.keyframes.get(0)).getStartProgress();
            }
            this.cachedStartDelayProgress = startProgress;
        }
        return this.cachedStartDelayProgress;
    }
    
    public void addUpdateListener(final AnimationListener animationListener) {
        this.listeners.add(animationListener);
    }
    
    protected Keyframe<K> getCurrentKeyframe() {
        final Keyframe<K> cachedKeyframe = this.cachedKeyframe;
        if (cachedKeyframe != null && cachedKeyframe.containsProgress(this.progress)) {
            return this.cachedKeyframe;
        }
        final List<? extends Keyframe<K>> keyframes = this.keyframes;
        Keyframe<K> cachedKeyframe2;
        final Keyframe<K> keyframe = cachedKeyframe2 = (Keyframe<K>)keyframes.get(keyframes.size() - 1);
        if (this.progress < keyframe.getStartProgress()) {
            int i = this.keyframes.size() - 1;
            cachedKeyframe2 = keyframe;
            while (i >= 0) {
                cachedKeyframe2 = (Keyframe<K>)this.keyframes.get(i);
                if (cachedKeyframe2.containsProgress(this.progress)) {
                    break;
                }
                --i;
            }
        }
        return this.cachedKeyframe = cachedKeyframe2;
    }
    
    float getEndProgress() {
        if (this.cachedEndProgress == -1.0f) {
            float endProgress;
            if (this.keyframes.isEmpty()) {
                endProgress = 1.0f;
            }
            else {
                final List<? extends Keyframe<K>> keyframes = this.keyframes;
                endProgress = ((Keyframe)keyframes.get(keyframes.size() - 1)).getEndProgress();
            }
            this.cachedEndProgress = endProgress;
        }
        return this.cachedEndProgress;
    }
    
    protected float getInterpolatedCurrentKeyframeProgress() {
        final Keyframe<K> currentKeyframe = this.getCurrentKeyframe();
        if (currentKeyframe.isStatic()) {
            return 0.0f;
        }
        return currentKeyframe.interpolator.getInterpolation(this.getLinearCurrentKeyframeProgress());
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
        final Keyframe<K> currentKeyframe = this.getCurrentKeyframe();
        final float interpolatedCurrentKeyframeProgress = this.getInterpolatedCurrentKeyframeProgress();
        if (this.valueCallback == null && currentKeyframe == this.cachedGetValueKeyframe && this.cachedGetValueProgress == interpolatedCurrentKeyframeProgress) {
            return this.cachedGetValue;
        }
        this.cachedGetValueKeyframe = currentKeyframe;
        this.cachedGetValueProgress = interpolatedCurrentKeyframeProgress;
        return this.cachedGetValue = this.getValue(currentKeyframe, interpolatedCurrentKeyframeProgress);
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
        if (this.keyframes.isEmpty()) {
            return;
        }
        final Keyframe<K> currentKeyframe = this.getCurrentKeyframe();
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
        final Keyframe<K> currentKeyframe2 = this.getCurrentKeyframe();
        if (currentKeyframe != currentKeyframe2 || !currentKeyframe2.isStatic()) {
            this.notifyListeners();
        }
    }
    
    public void setValueCallback(final LottieValueCallback<A> valueCallback) {
        final LottieValueCallback<A> valueCallback2 = this.valueCallback;
        if (valueCallback2 != null) {
            valueCallback2.setAnimation(null);
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
