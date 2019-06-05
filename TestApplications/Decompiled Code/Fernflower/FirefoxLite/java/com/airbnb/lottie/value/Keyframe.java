package com.airbnb.lottie.value;

import android.graphics.PointF;
import android.view.animation.Interpolator;
import com.airbnb.lottie.LottieComposition;

public class Keyframe {
   private final LottieComposition composition;
   public Float endFrame;
   private float endProgress = Float.MIN_VALUE;
   public final Object endValue;
   public final Interpolator interpolator;
   public PointF pathCp1 = null;
   public PointF pathCp2 = null;
   public final float startFrame;
   private float startProgress = Float.MIN_VALUE;
   public final Object startValue;

   public Keyframe(LottieComposition var1, Object var2, Object var3, Interpolator var4, float var5, Float var6) {
      this.composition = var1;
      this.startValue = var2;
      this.endValue = var3;
      this.interpolator = var4;
      this.startFrame = var5;
      this.endFrame = var6;
   }

   public Keyframe(Object var1) {
      this.composition = null;
      this.startValue = var1;
      this.endValue = var1;
      this.interpolator = null;
      this.startFrame = Float.MIN_VALUE;
      this.endFrame = Float.MAX_VALUE;
   }

   public boolean containsProgress(float var1) {
      boolean var2;
      if (var1 >= this.getStartProgress() && var1 < this.getEndProgress()) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public float getEndProgress() {
      if (this.composition == null) {
         return 1.0F;
      } else {
         if (this.endProgress == Float.MIN_VALUE) {
            if (this.endFrame == null) {
               this.endProgress = 1.0F;
            } else {
               this.endProgress = this.getStartProgress() + (this.endFrame - this.startFrame) / this.composition.getDurationFrames();
            }
         }

         return this.endProgress;
      }
   }

   public float getStartProgress() {
      if (this.composition == null) {
         return 0.0F;
      } else {
         if (this.startProgress == Float.MIN_VALUE) {
            this.startProgress = (this.startFrame - this.composition.getStartFrame()) / this.composition.getDurationFrames();
         }

         return this.startProgress;
      }
   }

   public boolean isStatic() {
      boolean var1;
      if (this.interpolator == null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("Keyframe{startValue=");
      var1.append(this.startValue);
      var1.append(", endValue=");
      var1.append(this.endValue);
      var1.append(", startFrame=");
      var1.append(this.startFrame);
      var1.append(", endFrame=");
      var1.append(this.endFrame);
      var1.append(", interpolator=");
      var1.append(this.interpolator);
      var1.append('}');
      return var1.toString();
   }
}
