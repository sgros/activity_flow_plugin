package com.airbnb.lottie.model.animatable;

import com.airbnb.lottie.value.Keyframe;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

abstract class BaseAnimatableValue<V, O> implements AnimatableValue<V, O> {
    final List<Keyframe<V>> keyframes;

    BaseAnimatableValue(V v) {
        this(Collections.singletonList(new Keyframe(v)));
    }

    BaseAnimatableValue(List<Keyframe<V>> list) {
        this.keyframes = list;
    }

    public List<Keyframe<V>> getKeyframes() {
        return this.keyframes;
    }

    public boolean isStatic() {
        return this.keyframes.isEmpty() || (this.keyframes.size() == 1 && ((Keyframe) this.keyframes.get(0)).isStatic());
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (!this.keyframes.isEmpty()) {
            stringBuilder.append("values=");
            stringBuilder.append(Arrays.toString(this.keyframes.toArray()));
        }
        return stringBuilder.toString();
    }
}
