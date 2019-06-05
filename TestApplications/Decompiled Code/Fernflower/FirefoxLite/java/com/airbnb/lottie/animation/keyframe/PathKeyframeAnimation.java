package com.airbnb.lottie.animation.keyframe;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;

public class PathKeyframeAnimation extends KeyframeAnimation {
   private PathMeasure pathMeasure;
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
         if (this.valueCallback != null) {
            PointF var5 = (PointF)this.valueCallback.getValueInternal(var3.startFrame, var3.endFrame, var3.startValue, var3.endValue, this.getLinearCurrentKeyframeProgress(), var2, this.getProgress());
            if (var5 != null) {
               return var5;
            }
         }

         if (this.pathMeasureKeyframe != var3) {
            this.pathMeasure = new PathMeasure(var4, false);
            this.pathMeasureKeyframe = var3;
         }

         this.pathMeasure.getPosTan(var2 * this.pathMeasure.getLength(), this.pos, (float[])null);
         this.point.set(this.pos[0], this.pos[1]);
         return this.point;
      }
   }
}
