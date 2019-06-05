package com.airbnb.lottie.value;

public class LottieFrameInfo {
   private float endFrame;
   private Object endValue;
   private float interpolatedKeyframeProgress;
   private float linearKeyframeProgress;
   private float overallProgress;
   private float startFrame;
   private Object startValue;

   public LottieFrameInfo set(float var1, float var2, Object var3, Object var4, float var5, float var6, float var7) {
      this.startFrame = var1;
      this.endFrame = var2;
      this.startValue = var3;
      this.endValue = var4;
      this.linearKeyframeProgress = var5;
      this.interpolatedKeyframeProgress = var6;
      this.overallProgress = var7;
      return this;
   }
}
