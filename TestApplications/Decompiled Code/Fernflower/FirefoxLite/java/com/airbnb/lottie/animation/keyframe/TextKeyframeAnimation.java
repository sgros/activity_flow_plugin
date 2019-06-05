package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.model.DocumentData;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;

public class TextKeyframeAnimation extends KeyframeAnimation {
   public TextKeyframeAnimation(List var1) {
      super(var1);
   }

   DocumentData getValue(Keyframe var1, float var2) {
      return (DocumentData)var1.startValue;
   }
}
