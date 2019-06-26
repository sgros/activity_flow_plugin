package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.Keyframe;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.value.ScaleXY;
import java.util.List;

public class ScaleKeyframeAnimation extends KeyframeAnimation {
   private final ScaleXY scaleXY = new ScaleXY();

   public ScaleKeyframeAnimation(List var1) {
      super(var1);
   }

   public ScaleXY getValue(Keyframe var1, float var2) {
      Object var3 = var1.startValue;
      if (var3 != null) {
         Object var4 = var1.endValue;
         if (var4 != null) {
            ScaleXY var7 = (ScaleXY)var3;
            ScaleXY var8 = (ScaleXY)var4;
            LottieValueCallback var5 = super.valueCallback;
            if (var5 != null) {
               ScaleXY var6 = (ScaleXY)var5.getValueInternal(var1.startFrame, var1.endFrame, var7, var8, var2, this.getLinearCurrentKeyframeProgress(), this.getProgress());
               if (var6 != null) {
                  return var6;
               }
            }

            this.scaleXY.set(MiscUtils.lerp(var7.getScaleX(), var8.getScaleX(), var2), MiscUtils.lerp(var7.getScaleY(), var8.getScaleY(), var2));
            return this.scaleXY;
         }
      }

      throw new IllegalStateException("Missing values for keyframe.");
   }
}
