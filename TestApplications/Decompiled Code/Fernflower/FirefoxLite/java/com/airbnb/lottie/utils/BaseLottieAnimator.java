package com.airbnb.lottie.utils;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.os.Build.VERSION;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class BaseLottieAnimator extends ValueAnimator {
   private final Set listeners = new CopyOnWriteArraySet();
   private final Set updateListeners = new CopyOnWriteArraySet();

   public void addListener(AnimatorListener var1) {
      this.listeners.add(var1);
   }

   public void addUpdateListener(AnimatorUpdateListener var1) {
      this.updateListeners.add(var1);
   }

   public long getStartDelay() {
      throw new UnsupportedOperationException("LottieAnimator does not support getStartDelay.");
   }

   void notifyCancel() {
      Iterator var1 = this.listeners.iterator();

      while(var1.hasNext()) {
         ((AnimatorListener)var1.next()).onAnimationCancel(this);
      }

   }

   void notifyEnd(boolean var1) {
      Iterator var2 = this.listeners.iterator();

      while(var2.hasNext()) {
         AnimatorListener var3 = (AnimatorListener)var2.next();
         if (VERSION.SDK_INT >= 26) {
            var3.onAnimationEnd(this, var1);
         } else {
            var3.onAnimationEnd(this);
         }
      }

   }

   void notifyRepeat() {
      Iterator var1 = this.listeners.iterator();

      while(var1.hasNext()) {
         ((AnimatorListener)var1.next()).onAnimationRepeat(this);
      }

   }

   void notifyStart(boolean var1) {
      Iterator var2 = this.listeners.iterator();

      while(var2.hasNext()) {
         AnimatorListener var3 = (AnimatorListener)var2.next();
         if (VERSION.SDK_INT >= 26) {
            var3.onAnimationStart(this, var1);
         } else {
            var3.onAnimationStart(this);
         }
      }

   }

   void notifyUpdate() {
      Iterator var1 = this.updateListeners.iterator();

      while(var1.hasNext()) {
         ((AnimatorUpdateListener)var1.next()).onAnimationUpdate(this);
      }

   }

   public void removeAllListeners() {
      this.listeners.clear();
   }

   public void removeAllUpdateListeners() {
      this.updateListeners.clear();
   }

   public void removeListener(AnimatorListener var1) {
      this.listeners.remove(var1);
   }

   public void removeUpdateListener(AnimatorUpdateListener var1) {
      this.updateListeners.remove(var1);
   }

   public ValueAnimator setDuration(long var1) {
      throw new UnsupportedOperationException("LottieAnimator does not support setDuration.");
   }

   public void setInterpolator(TimeInterpolator var1) {
      throw new UnsupportedOperationException("LottieAnimator does not support setInterpolator.");
   }

   public void setStartDelay(long var1) {
      throw new UnsupportedOperationException("LottieAnimator does not support setStartDelay.");
   }
}
