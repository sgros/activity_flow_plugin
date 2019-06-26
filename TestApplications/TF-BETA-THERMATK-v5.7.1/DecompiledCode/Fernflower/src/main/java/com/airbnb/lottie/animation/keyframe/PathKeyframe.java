package com.airbnb.lottie.animation.keyframe;

import android.graphics.Path;
import android.graphics.PointF;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.value.Keyframe;

public class PathKeyframe extends Keyframe {
   private Path path;
   private final Keyframe pointKeyFrame;

   public PathKeyframe(LottieComposition var1, Keyframe var2) {
      super(var1, var2.startValue, var2.endValue, var2.interpolator, var2.startFrame, var2.endFrame);
      this.pointKeyFrame = var2;
      this.createPath();
   }

   public void createPath() {
      Object var2;
      boolean var3;
      label20: {
         Object var1 = super.endValue;
         if (var1 != null) {
            var2 = super.startValue;
            if (var2 != null && ((PointF)var2).equals(((PointF)var1).x, ((PointF)var1).y)) {
               var3 = true;
               break label20;
            }
         }

         var3 = false;
      }

      var2 = super.endValue;
      if (var2 != null && !var3) {
         PointF var5 = (PointF)super.startValue;
         PointF var4 = (PointF)var2;
         Keyframe var6 = this.pointKeyFrame;
         this.path = Utils.createPath(var5, var4, var6.pathCp1, var6.pathCp2);
      }

   }

   Path getPath() {
      return this.path;
   }
}
