package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.value.Keyframe;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.Collections;

public class ValueCallbackKeyframeAnimation extends BaseKeyframeAnimation {
   private final LottieFrameInfo frameInfo = new LottieFrameInfo();

   public ValueCallbackKeyframeAnimation(LottieValueCallback var1) {
      super(Collections.emptyList());
      this.setValueCallback(var1);
   }

   float getEndProgress() {
      return 1.0F;
   }

   public Object getValue() {
      return this.valueCallback.getValueInternal(0.0F, 0.0F, (Object)null, (Object)null, this.getProgress(), this.getProgress(), this.getProgress());
   }

   Object getValue(Keyframe var1, float var2) {
      return this.getValue();
   }

   public void notifyListeners() {
      if (this.valueCallback != null) {
         super.notifyListeners();
      }

   }
}
