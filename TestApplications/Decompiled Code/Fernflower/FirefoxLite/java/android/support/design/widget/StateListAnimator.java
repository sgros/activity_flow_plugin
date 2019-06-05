package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
import android.util.StateSet;
import java.util.ArrayList;

public final class StateListAnimator {
   private final AnimatorListener animationListener = new AnimatorListenerAdapter() {
      public void onAnimationEnd(Animator var1) {
         if (StateListAnimator.this.runningAnimator == var1) {
            StateListAnimator.this.runningAnimator = null;
         }

      }
   };
   private StateListAnimator.Tuple lastMatch = null;
   ValueAnimator runningAnimator = null;
   private final ArrayList tuples = new ArrayList();

   private void cancel() {
      if (this.runningAnimator != null) {
         this.runningAnimator.cancel();
         this.runningAnimator = null;
      }

   }

   private void start(StateListAnimator.Tuple var1) {
      this.runningAnimator = var1.animator;
      this.runningAnimator.start();
   }

   public void addState(int[] var1, ValueAnimator var2) {
      StateListAnimator.Tuple var3 = new StateListAnimator.Tuple(var1, var2);
      var2.addListener(this.animationListener);
      this.tuples.add(var3);
   }

   public void jumpToCurrentState() {
      if (this.runningAnimator != null) {
         this.runningAnimator.end();
         this.runningAnimator = null;
      }

   }

   public void setState(int[] var1) {
      int var2 = this.tuples.size();
      int var3 = 0;

      StateListAnimator.Tuple var5;
      while(true) {
         if (var3 >= var2) {
            var5 = null;
            break;
         }

         StateListAnimator.Tuple var4 = (StateListAnimator.Tuple)this.tuples.get(var3);
         if (StateSet.stateSetMatches(var4.specs, var1)) {
            var5 = var4;
            break;
         }

         ++var3;
      }

      if (var5 != this.lastMatch) {
         if (this.lastMatch != null) {
            this.cancel();
         }

         this.lastMatch = var5;
         if (var5 != null) {
            this.start(var5);
         }

      }
   }

   static class Tuple {
      final ValueAnimator animator;
      final int[] specs;

      Tuple(int[] var1, ValueAnimator var2) {
         this.specs = var1;
         this.animator = var2;
      }
   }
}
