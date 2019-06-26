package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.model.content.GradientColor;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;

public class GradientColorKeyframeAnimation extends KeyframeAnimation {
   private final GradientColor gradientColor;

   public GradientColorKeyframeAnimation(List var1) {
      super(var1);
      int var2 = 0;
      GradientColor var3 = (GradientColor)((Keyframe)var1.get(0)).startValue;
      if (var3 != null) {
         var2 = var3.getSize();
      }

      this.gradientColor = new GradientColor(new float[var2], new int[var2]);
   }

   GradientColor getValue(Keyframe var1, float var2) {
      this.gradientColor.lerp((GradientColor)var1.startValue, (GradientColor)var1.endValue, var2);
      return this.gradientColor;
   }
}
