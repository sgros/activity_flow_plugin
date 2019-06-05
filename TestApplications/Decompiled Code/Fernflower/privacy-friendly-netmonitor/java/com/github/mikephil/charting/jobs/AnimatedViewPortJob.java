package com.github.mikephil.charting.jobs;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.view.View;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

@SuppressLint({"NewApi"})
public abstract class AnimatedViewPortJob extends ViewPortJob implements AnimatorUpdateListener, AnimatorListener {
   protected ObjectAnimator animator;
   protected float phase;
   protected float xOrigin;
   protected float yOrigin;

   public AnimatedViewPortJob(ViewPortHandler var1, float var2, float var3, Transformer var4, View var5, float var6, float var7, long var8) {
      super(var1, var2, var3, var4, var5);
      this.xOrigin = var6;
      this.yOrigin = var7;
      this.animator = ObjectAnimator.ofFloat(this, "phase", new float[]{0.0F, 1.0F});
      this.animator.setDuration(var8);
      this.animator.addUpdateListener(this);
      this.animator.addListener(this);
   }

   public float getPhase() {
      return this.phase;
   }

   public float getXOrigin() {
      return this.xOrigin;
   }

   public float getYOrigin() {
      return this.yOrigin;
   }

   public void onAnimationCancel(Animator var1) {
      try {
         this.recycleSelf();
      } catch (IllegalArgumentException var2) {
      }

   }

   public void onAnimationEnd(Animator var1) {
      try {
         this.recycleSelf();
      } catch (IllegalArgumentException var2) {
      }

   }

   public void onAnimationRepeat(Animator var1) {
   }

   public void onAnimationStart(Animator var1) {
   }

   public void onAnimationUpdate(ValueAnimator var1) {
   }

   public abstract void recycleSelf();

   protected void resetAnimator() {
      this.animator.removeAllListeners();
      this.animator.removeAllUpdateListeners();
      this.animator.reverse();
      this.animator.addUpdateListener(this);
      this.animator.addListener(this);
   }

   @SuppressLint({"NewApi"})
   public void run() {
      this.animator.start();
   }

   public void setPhase(float var1) {
      this.phase = var1;
   }
}
