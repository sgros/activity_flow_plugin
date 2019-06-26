// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.value.Keyframe;
import java.util.List;

abstract class KeyframeAnimation<T> extends BaseKeyframeAnimation<T, T>
{
    KeyframeAnimation(final List<? extends Keyframe<T>> list) {
        super(list);
    }
}
