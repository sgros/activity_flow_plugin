package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.value.Keyframe;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.Collections;

public class ValueCallbackKeyframeAnimation<K, A> extends BaseKeyframeAnimation<K, A> {
    private final LottieFrameInfo<A> frameInfo = new LottieFrameInfo();

    /* Access modifiers changed, original: 0000 */
    public float getEndProgress() {
        return 1.0f;
    }

    public ValueCallbackKeyframeAnimation(LottieValueCallback<A> lottieValueCallback) {
        super(Collections.emptyList());
        setValueCallback(lottieValueCallback);
    }

    public void notifyListeners() {
        if (this.valueCallback != null) {
            super.notifyListeners();
        }
    }

    public A getValue() {
        return this.valueCallback.getValueInternal(0.0f, 0.0f, null, null, getProgress(), getProgress(), getProgress());
    }

    /* Access modifiers changed, original: 0000 */
    public A getValue(Keyframe<K> keyframe, float f) {
        return getValue();
    }
}
