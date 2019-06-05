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
    
    public ValueCallbackKeyframeAnimation(final LottieValueCallback<A> valueCallback) {
        super(Collections.emptyList());
        this.frameInfo = new LottieFrameInfo<A>();
        this.setValueCallback(valueCallback);
    }
    
    @Override
    float getEndProgress() {
        return 1.0f;
    }
    
    @Override
    public A getValue() {
        return this.valueCallback.getValueInternal(0.0f, 0.0f, null, null, this.getProgress(), this.getProgress(), this.getProgress());
    }
    
    @Override
    A getValue(final Keyframe<K> keyframe, final float n) {
        return this.getValue();
    }
    
    @Override
    public void notifyListeners() {
        if (this.valueCallback != null) {
            super.notifyListeners();
        }
    }
}
