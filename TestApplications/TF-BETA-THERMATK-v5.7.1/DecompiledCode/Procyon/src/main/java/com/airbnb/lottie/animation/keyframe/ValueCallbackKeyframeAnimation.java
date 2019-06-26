// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.value.Keyframe;
import java.util.List;
import java.util.Collections;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.value.LottieFrameInfo;

public class ValueCallbackKeyframeAnimation<K, A> extends BaseKeyframeAnimation<K, A>
{
    private final LottieFrameInfo<A> frameInfo;
    private final A valueCallbackValue;
    
    public ValueCallbackKeyframeAnimation(final LottieValueCallback<A> lottieValueCallback) {
        this((LottieValueCallback<Object>)lottieValueCallback, null);
    }
    
    public ValueCallbackKeyframeAnimation(final LottieValueCallback<A> valueCallback, final A valueCallbackValue) {
        super(Collections.emptyList());
        this.frameInfo = new LottieFrameInfo<A>();
        this.setValueCallback(valueCallback);
        this.valueCallbackValue = valueCallbackValue;
    }
    
    @Override
    float getEndProgress() {
        return 1.0f;
    }
    
    @Override
    public A getValue() {
        final LottieValueCallback<A> valueCallback = (LottieValueCallback<A>)super.valueCallback;
        final A valueCallbackValue = this.valueCallbackValue;
        return valueCallback.getValueInternal(0.0f, 0.0f, valueCallbackValue, valueCallbackValue, this.getProgress(), this.getProgress(), this.getProgress());
    }
    
    @Override
    A getValue(final Keyframe<K> keyframe, final float n) {
        return this.getValue();
    }
    
    @Override
    public void notifyListeners() {
        if (super.valueCallback != null) {
            super.notifyListeners();
        }
    }
}
