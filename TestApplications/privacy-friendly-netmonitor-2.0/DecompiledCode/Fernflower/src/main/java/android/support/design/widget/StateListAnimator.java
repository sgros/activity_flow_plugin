package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
import android.util.StateSet;
import java.util.ArrayList;

final class StateListAnimator {
   private final AnimatorListener mAnimationListener = new AnimatorListenerAdapter() {
      public void onAnimationEnd(Animator var1) {
         if (StateListAnimator.this.mRunningAnimator == var1) {
            StateListAnimator.this.mRunningAnimator = null;
         }

      }
   };
   private StateListAnimator.Tuple mLastMatch = null;
   ValueAnimator mRunningAnimator = null;
   private final ArrayList mTuples = new ArrayList();

   private void cancel() {
      if (this.mRunningAnimator != null) {
         this.mRunningAnimator.cancel();
         this.mRunningAnimator = null;
      }

   }

   private void start(StateListAnimator.Tuple var1) {
      this.mRunningAnimator = var1.mAnimator;
      this.mRunningAnimator.start();
   }

   public void addState(int[] var1, ValueAnimator var2) {
      StateListAnimator.Tuple var3 = new StateListAnimator.Tuple(var1, var2);
      var2.addListener(this.mAnimationListener);
      this.mTuples.add(var3);
   }

   public void jumpToCurrentState() {
      if (this.mRunningAnimator != null) {
         this.mRunningAnimator.end();
         this.mRunningAnimator = null;
      }

   }

   void setState(int[] var1) {
      int var2 = this.mTuples.size();
      int var3 = 0;

      StateListAnimator.Tuple var5;
      while(true) {
         if (var3 >= var2) {
            var5 = null;
            break;
         }

         StateListAnimator.Tuple var4 = (StateListAnimator.Tuple)this.mTuples.get(var3);
         if (StateSet.stateSetMatches(var4.mSpecs, var1)) {
            var5 = var4;
            break;
         }

         ++var3;
      }

      if (var5 != this.mLastMatch) {
         if (this.mLastMatch != null) {
            this.cancel();
         }

         this.mLastMatch = var5;
         if (var5 != null) {
            this.start(var5);
         }

      }
   }

   static class Tuple {
      final ValueAnimator mAnimator;
      final int[] mSpecs;

      Tuple(int[] var1, ValueAnimator var2) {
         this.mSpecs = var1;
         this.mAnimator = var2;
      }
   }
}
