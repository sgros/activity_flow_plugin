package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.Keyframe;
import com.airbnb.lottie.value.ScaleXY;
import java.util.List;

public class ScaleKeyframeAnimation extends KeyframeAnimation {
   public ScaleKeyframeAnimation(List var1) {
      super(var1);
   }

   public ScaleXY getValue(Keyframe var1, float var2) {
      if (var1.startValue != null && var1.endValue != null) {
         ScaleXY var3 = (ScaleXY)var1.startValue;
         ScaleXY var4 = (ScaleXY)var1.endValue;
         if (this.valueCallback != null) {
            ScaleXY var5 = (ScaleXY)this.valueCallback.getValueInternal(var1.startFrame, var1.endFrame, var3, var4, var2, this.getLinearCurrentKeyframeProgress(), this.getProgress());
            if (var5 != null) {
               return var5;
            }
         }

         return new ScaleXY(MiscUtils.lerp(var3.getScaleX(), var4.getScaleX(), var2), MiscUtils.lerp(var3.getScaleY(), var4.getScaleY(), var2));
      } else {
         throw new IllegalStateException("Missing values for keyframe.");
      }
   }
}
