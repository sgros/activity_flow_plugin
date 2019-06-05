package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.value.Keyframe;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseKeyframeAnimation<K, A> {
    private Keyframe<K> cachedKeyframe;
    private boolean isDiscrete = false;
    private final List<? extends Keyframe<K>> keyframes;
    final List<AnimationListener> listeners = new ArrayList();
    private float progress = 0.0f;
    protected LottieValueCallback<A> valueCallback;

    public interface AnimationListener {
        void onValueChanged();
    }

    public abstract A getValue(Keyframe<K> keyframe, float f);

    BaseKeyframeAnimation(List<? extends Keyframe<K>> list) {
        this.keyframes = list;
    }

    public void setIsDiscrete() {
        this.isDiscrete = true;
    }

    public void addUpdateListener(AnimationListener animationListener) {
        this.listeners.add(animationListener);
    }

    public void setProgress(float f) {
        if (f < getStartDelayProgress()) {
            f = getStartDelayProgress();
        } else if (f > getEndProgress()) {
            f = getEndProgress();
        }
        if (f != this.progress) {
            this.progress = f;
            notifyListeners();
        }
    }

    public void notifyListeners() {
        for (int i = 0; i < this.listeners.size(); i++) {
            ((AnimationListener) this.listeners.get(i)).onValueChanged();
        }
    }

    private Keyframe<K> getCurrentKeyframe() {
        if (this.cachedKeyframe != null && this.cachedKeyframe.containsProgress(this.progress)) {
            return this.cachedKeyframe;
        }
        Keyframe keyframe = (Keyframe) this.keyframes.get(this.keyframes.size() - 1);
        if (this.progress < keyframe.getStartProgress()) {
            for (int size = this.keyframes.size() - 1; size >= 0; size--) {
                keyframe = (Keyframe) this.keyframes.get(size);
                if (keyframe.containsProgress(this.progress)) {
                    break;
                }
            }
        }
        this.cachedKeyframe = keyframe;
        return keyframe;
    }

    /* Access modifiers changed, original: 0000 */
    public float getLinearCurrentKeyframeProgress() {
        if (this.isDiscrete) {
            return 0.0f;
        }
        Keyframe currentKeyframe = getCurrentKeyframe();
        if (currentKeyframe.isStatic()) {
            return 0.0f;
        }
        return (this.progress - currentKeyframe.getStartProgress()) / (currentKeyframe.getEndProgress() - currentKeyframe.getStartProgress());
    }

    private float getInterpolatedCurrentKeyframeProgress() {
        Keyframe currentKeyframe = getCurrentKeyframe();
        if (currentKeyframe.isStatic()) {
            return 0.0f;
        }
        return currentKeyframe.interpolator.getInterpolation(getLinearCurrentKeyframeProgress());
    }

    private float getStartDelayProgress() {
        return this.keyframes.isEmpty() ? 0.0f : ((Keyframe) this.keyframes.get(0)).getStartProgress();
    }

    /* Access modifiers changed, original: 0000 */
    public float getEndProgress() {
        return this.keyframes.isEmpty() ? 1.0f : ((Keyframe) this.keyframes.get(this.keyframes.size() - 1)).getEndProgress();
    }

    public A getValue() {
        return getValue(getCurrentKeyframe(), getInterpolatedCurrentKeyframeProgress());
    }

    public float getProgress() {
        return this.progress;
    }

    public void setValueCallback(LottieValueCallback<A> lottieValueCallback) {
        if (this.valueCallback != null) {
            this.valueCallback.setAnimation(null);
        }
        this.valueCallback = lottieValueCallback;
        if (lottieValueCallback != null) {
            lottieValueCallback.setAnimation(this);
        }
    }
}
