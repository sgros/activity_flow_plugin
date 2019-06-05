package com.github.mikephil.charting.animation;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.os.Build.VERSION;

public class ChartAnimator {
   private AnimatorUpdateListener mListener;
   protected float mPhaseX = 1.0F;
   protected float mPhaseY = 1.0F;

   public ChartAnimator() {
   }

   public ChartAnimator(AnimatorUpdateListener var1) {
      this.mListener = var1;
   }

   public void animateX(int var1) {
      if (VERSION.SDK_INT >= 11) {
         ObjectAnimator var2 = ObjectAnimator.ofFloat(this, "phaseX", new float[]{0.0F, 1.0F});
         var2.setDuration((long)var1);
         var2.addUpdateListener(this.mListener);
         var2.start();
      }
   }

   public void animateX(int var1, Easing.EasingOption var2) {
      if (VERSION.SDK_INT >= 11) {
         ObjectAnimator var3 = ObjectAnimator.ofFloat(this, "phaseX", new float[]{0.0F, 1.0F});
         var3.setInterpolator(Easing.getEasingFunctionFromOption(var2));
         var3.setDuration((long)var1);
         var3.addUpdateListener(this.mListener);
         var3.start();
      }
   }

   public void animateX(int var1, EasingFunction var2) {
      if (VERSION.SDK_INT >= 11) {
         ObjectAnimator var3 = ObjectAnimator.ofFloat(this, "phaseX", new float[]{0.0F, 1.0F});
         var3.setInterpolator(var2);
         var3.setDuration((long)var1);
         var3.addUpdateListener(this.mListener);
         var3.start();
      }
   }

   public void animateXY(int var1, int var2) {
      if (VERSION.SDK_INT >= 11) {
         ObjectAnimator var3 = ObjectAnimator.ofFloat(this, "phaseY", new float[]{0.0F, 1.0F});
         var3.setDuration((long)var2);
         ObjectAnimator var4 = ObjectAnimator.ofFloat(this, "phaseX", new float[]{0.0F, 1.0F});
         var4.setDuration((long)var1);
         if (var1 > var2) {
            var4.addUpdateListener(this.mListener);
         } else {
            var3.addUpdateListener(this.mListener);
         }

         var4.start();
         var3.start();
      }
   }

   public void animateXY(int var1, int var2, Easing.EasingOption var3, Easing.EasingOption var4) {
      if (VERSION.SDK_INT >= 11) {
         ObjectAnimator var5 = ObjectAnimator.ofFloat(this, "phaseY", new float[]{0.0F, 1.0F});
         var5.setInterpolator(Easing.getEasingFunctionFromOption(var4));
         var5.setDuration((long)var2);
         ObjectAnimator var6 = ObjectAnimator.ofFloat(this, "phaseX", new float[]{0.0F, 1.0F});
         var6.setInterpolator(Easing.getEasingFunctionFromOption(var3));
         var6.setDuration((long)var1);
         if (var1 > var2) {
            var6.addUpdateListener(this.mListener);
         } else {
            var5.addUpdateListener(this.mListener);
         }

         var6.start();
         var5.start();
      }
   }

   public void animateXY(int var1, int var2, EasingFunction var3, EasingFunction var4) {
      if (VERSION.SDK_INT >= 11) {
         ObjectAnimator var5 = ObjectAnimator.ofFloat(this, "phaseY", new float[]{0.0F, 1.0F});
         var5.setInterpolator(var4);
         var5.setDuration((long)var2);
         ObjectAnimator var6 = ObjectAnimator.ofFloat(this, "phaseX", new float[]{0.0F, 1.0F});
         var6.setInterpolator(var3);
         var6.setDuration((long)var1);
         if (var1 > var2) {
            var6.addUpdateListener(this.mListener);
         } else {
            var5.addUpdateListener(this.mListener);
         }

         var6.start();
         var5.start();
      }
   }

   public void animateY(int var1) {
      if (VERSION.SDK_INT >= 11) {
         ObjectAnimator var2 = ObjectAnimator.ofFloat(this, "phaseY", new float[]{0.0F, 1.0F});
         var2.setDuration((long)var1);
         var2.addUpdateListener(this.mListener);
         var2.start();
      }
   }

   public void animateY(int var1, Easing.EasingOption var2) {
      if (VERSION.SDK_INT >= 11) {
         ObjectAnimator var3 = ObjectAnimator.ofFloat(this, "phaseY", new float[]{0.0F, 1.0F});
         var3.setInterpolator(Easing.getEasingFunctionFromOption(var2));
         var3.setDuration((long)var1);
         var3.addUpdateListener(this.mListener);
         var3.start();
      }
   }

   public void animateY(int var1, EasingFunction var2) {
      if (VERSION.SDK_INT >= 11) {
         ObjectAnimator var3 = ObjectAnimator.ofFloat(this, "phaseY", new float[]{0.0F, 1.0F});
         var3.setInterpolator(var2);
         var3.setDuration((long)var1);
         var3.addUpdateListener(this.mListener);
         var3.start();
      }
   }

   public float getPhaseX() {
      return this.mPhaseX;
   }

   public float getPhaseY() {
      return this.mPhaseY;
   }

   public void setPhaseX(float var1) {
      this.mPhaseX = var1;
   }

   public void setPhaseY(float var1) {
      this.mPhaseY = var1;
   }
}
