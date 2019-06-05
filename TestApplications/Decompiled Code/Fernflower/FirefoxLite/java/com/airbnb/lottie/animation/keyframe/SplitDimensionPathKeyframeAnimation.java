package com.airbnb.lottie.animation.keyframe;

import android.graphics.PointF;
import com.airbnb.lottie.value.Keyframe;
import java.util.Collections;

public class SplitDimensionPathKeyframeAnimation extends BaseKeyframeAnimation {
   private final PointF point = new PointF();
   private final BaseKeyframeAnimation xAnimation;
   private final BaseKeyframeAnimation yAnimation;

   public SplitDimensionPathKeyframeAnimation(BaseKeyframeAnimation var1, BaseKeyframeAnimation var2) {
      super(Collections.emptyList());
      this.xAnimation = var1;
      this.yAnimation = var2;
      this.setProgress(this.getProgress());
   }

   public PointF getValue() {
      return this.getValue((Keyframe)null, 0.0F);
   }

   PointF getValue(Keyframe var1, float var2) {
      return this.point;
   }

   public void setProgress(float var1) {
      this.xAnimation.setProgress(var1);
      this.yAnimation.setProgress(var1);
      this.point.set((Float)this.xAnimation.getValue(), (Float)this.yAnimation.getValue());

      for(int var2 = 0; var2 < this.listeners.size(); ++var2) {
         ((BaseKeyframeAnimation.AnimationListener)this.listeners.get(var2)).onValueChanged();
      }

   }
}
