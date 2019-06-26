package com.airbnb.lottie.animation.keyframe;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import com.airbnb.lottie.value.Keyframe;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.List;

public class PathKeyframeAnimation extends KeyframeAnimation {
   private PathMeasure pathMeasure = new PathMeasure();
   private PathKeyframe pathMeasureKeyframe;
   private final PointF point = new PointF();
   private final float[] pos = new float[2];

   public PathKeyframeAnimation(List var1) {
      super(var1);
   }

   public PointF getValue(Keyframe var1, float var2) {
      PathKeyframe var3 = (PathKeyframe)var1;
      Path var4 = var3.getPath();
      if (var4 == null) {
         return (PointF)var1.startValue;
      } else {
         LottieValueCallback var5 = super.valueCallback;
         PointF var6;
         if (var5 != null) {
            var6 = (PointF)var5.getValueInternal(var3.startFrame, var3.endFrame, var3.startValue, var3.endValue, this.getLinearCurrentKeyframeProgress(), var2, this.getProgress());
            if (var6 != null) {
               return var6;
            }
         }

         if (this.pathMeasureKeyframe != var3) {
            this.pathMeasure.setPath(var4, false);
            this.pathMeasureKeyframe = var3;
         }

         PathMeasure var7 = this.pathMeasure;
         var7.getPosTan(var2 * var7.getLength(), this.pos, (float[])null);
         var6 = this.point;
         float[] var8 = this.pos;
         var6.set(var8[0], var8[1]);
         return this.point;
      }
   }
}
