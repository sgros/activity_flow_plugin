package com.airbnb.lottie.model.animatable;

import com.airbnb.lottie.value.Keyframe;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

abstract class BaseAnimatableValue implements AnimatableValue {
   final List keyframes;

   BaseAnimatableValue(Object var1) {
      this(Collections.singletonList(new Keyframe(var1)));
   }

   BaseAnimatableValue(List var1) {
      this.keyframes = var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      if (!this.keyframes.isEmpty()) {
         var1.append("values=");
         var1.append(Arrays.toString(this.keyframes.toArray()));
      }

      return var1.toString();
   }
}
