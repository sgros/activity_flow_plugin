package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.value.Keyframe;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.Collections;

public class ValueCallbackKeyframeAnimation<K, A> extends BaseKeyframeAnimation<K, A> {
    private final LottieFrameInfo<A> frameInfo;
    private final A valueCallbackValue;

    /* Access modifiers changed, original: 0000 */
    public float getEndProgress() {
        return 1.0f;
    }

    public ValueCallbackKeyframeAnimation(LottieValueCallback<A> lottieValueCallback) {
        this(lottieValueCallback, null);
    }

    public ValueCallbackKeyframeAnimation(LottieValueCallback<A> lottieValueCallback, A a) {
        super(Collections.emptyList());
        this.frameInfo = new LottieFrameInfo();
        setValueCallback(lottieValueCallback);
        this.valueCallbackValue = a;
    }

    public void notifyListeners() {
        if (this.valueCallback != null) {
            super.notifyListeners();
        }
    }

    public A getValue() {
        LottieValueCallback lottieValueCallback = this.valueCallback;
        Object obj = this.valueCallbackValue;
        return lottieValueCallback.getValueInternal(0.0f, 0.0f, obj, obj, getProgress(), getProgress(), getProgress());
    }

    /* Access modifiers changed, original: 0000 */
    public A getValue(Keyframe<K> keyframe, float f) {
        return getValue();
    }
}