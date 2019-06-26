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

   public List getKeyframes() {
      return this.keyframes;
   }

   public boolean isStatic() {
      boolean var1 = this.keyframes.isEmpty();
      boolean var2 = false;
      if (!var1) {
         var1 = var2;
         if (this.keyframes.size() != 1) {
            return var1;
         }

         var1 = var2;
         if (!((Keyframe)this.keyframes.get(0)).isStatic()) {
            return var1;
         }
      }

      var1 = true;
      return var1;
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
