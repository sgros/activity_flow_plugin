// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.animatable;

import java.util.Arrays;
import java.util.Collections;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;

abstract class BaseAnimatableValue<V, O> implements AnimatableValue<V, O>
{
    final List<Keyframe<V>> keyframes;
    
    BaseAnimatableValue(final V v) {
        this(Collections.singletonList(new Keyframe<V>(v)));
    }
    
    BaseAnimatableValue(final List<Keyframe<V>> keyframes) {
        this.keyframes = keyframes;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if (!this.keyframes.isEmpty()) {
            sb.append("values=");
            sb.append(Arrays.toString(this.keyframes.toArray()));
        }
        return sb.toString();
    }
}
