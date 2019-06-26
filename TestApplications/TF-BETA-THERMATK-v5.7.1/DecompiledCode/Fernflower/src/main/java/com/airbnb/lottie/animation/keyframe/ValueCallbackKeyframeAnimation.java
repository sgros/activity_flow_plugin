package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.value.Keyframe;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.Collections;

public class ValueCallbackKeyframeAnimation extends BaseKeyframeAnimation {
   private final LottieFrameInfo frameInfo;
   private final Object valueCallbackValue;

   public ValueCallbackKeyframeAnimation(LottieValueCallback var1) {
      this(var1, (Object)null);
   }

   public ValueCallbackKeyframeAnimation(LottieValueCallback var1, Object var2) {
      super(Collections.emptyList());
      this.frameInfo = new LottieFrameInfo();
      this.setValueCallback(var1);
      this.valueCallbackValue = var2;
   }

   float getEndProgress() {
      return 1.0F;
   }

   public Object getValue() {
      LottieValueCallback var1 = super.valueCallback;
      Object var2 = this.valueCallbackValue;
      return var1.getValueInternal(0.0F, 0.0F, var2, var2, this.getProgress(), this.getProgress(), this.getProgress());
   }

   Object getValue(Keyframe var1, float var2) {
      return this.getValue();
   }

   public void notifyListeners() {
      if (super.valueCallback != null) {
         super.notifyListeners();
      }

   }
}
