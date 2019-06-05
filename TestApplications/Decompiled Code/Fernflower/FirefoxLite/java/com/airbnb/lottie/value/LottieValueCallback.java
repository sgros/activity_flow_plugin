package com.airbnb.lottie.value;

import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;

public class LottieValueCallback {
   private BaseKeyframeAnimation animation;
   private final LottieFrameInfo frameInfo = new LottieFrameInfo();
   protected Object value = null;

   public LottieValueCallback() {
   }

   public LottieValueCallback(Object var1) {
      this.value = var1;
   }

   public Object getValue(LottieFrameInfo var1) {
      return this.value;
   }

   public final Object getValueInternal(float var1, float var2, Object var3, Object var4, float var5, float var6, float var7) {
      return this.getValue(this.frameInfo.set(var1, var2, var3, var4, var5, var6, var7));
   }

   public final void setAnimation(BaseKeyframeAnimation var1) {
      this.animation = var1;
   }
}
