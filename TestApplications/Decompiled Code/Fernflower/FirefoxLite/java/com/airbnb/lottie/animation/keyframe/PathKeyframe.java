package com.airbnb.lottie.animation.keyframe;

import android.graphics.Path;
import android.graphics.PointF;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.value.Keyframe;

public class PathKeyframe extends Keyframe {
   private Path path;

   public PathKeyframe(LottieComposition var1, Keyframe var2) {
      super(var1, var2.startValue, var2.endValue, var2.interpolator, var2.startFrame, var2.endFrame);
      boolean var3;
      if (this.endValue != null && this.startValue != null && ((PointF)this.startValue).equals(((PointF)this.endValue).x, ((PointF)this.endValue).y)) {
         var3 = true;
      } else {
         var3 = false;
      }

      if (this.endValue != null && !var3) {
         this.path = Utils.createPath((PointF)this.startValue, (PointF)this.endValue, var2.pathCp1, var2.pathCp2);
      }

   }

   Path getPath() {
      return this.path;
   }
}
