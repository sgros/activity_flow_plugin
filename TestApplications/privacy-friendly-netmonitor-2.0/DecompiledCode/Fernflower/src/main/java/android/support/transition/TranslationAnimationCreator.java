package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.view.View;

class TranslationAnimationCreator {
   static Animator createAnimation(View var0, TransitionValues var1, int var2, int var3, float var4, float var5, float var6, float var7, TimeInterpolator var8) {
      float var9 = var0.getTranslationX();
      float var10 = var0.getTranslationY();
      int[] var11 = (int[])var1.view.getTag(R.id.transition_position);
      float var12;
      if (var11 != null) {
         var12 = (float)(var11[0] - var2) + var9;
         var4 = (float)(var11[1] - var3) + var10;
      } else {
         var12 = var4;
         var4 = var5;
      }

      int var13 = Math.round(var12 - var9);
      int var14 = Math.round(var4 - var10);
      var0.setTranslationX(var12);
      var0.setTranslationY(var4);
      if (var12 == var6 && var4 == var7) {
         return null;
      } else {
         ObjectAnimator var16 = ObjectAnimator.ofPropertyValuesHolder(var0, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat(View.TRANSLATION_X, new float[]{var12, var6}), PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, new float[]{var4, var7})});
         TranslationAnimationCreator.TransitionPositionListener var15 = new TranslationAnimationCreator.TransitionPositionListener(var0, var1.view, var2 + var13, var3 + var14, var9, var10);
         var16.addListener(var15);
         AnimatorUtils.addPauseListener(var16, var15);
         var16.setInterpolator(var8);
         return var16;
      }
   }

   private static class TransitionPositionListener extends AnimatorListenerAdapter {
      private final View mMovingView;
      private float mPausedX;
      private float mPausedY;
      private final int mStartX;
      private final int mStartY;
      private final float mTerminalX;
      private final float mTerminalY;
      private int[] mTransitionPosition;
      private final View mViewInHierarchy;

      private TransitionPositionListener(View var1, View var2, int var3, int var4, float var5, float var6) {
         this.mMovingView = var1;
         this.mViewInHierarchy = var2;
         this.mStartX = var3 - Math.round(this.mMovingView.getTranslationX());
         this.mStartY = var4 - Math.round(this.mMovingView.getTranslationY());
         this.mTerminalX = var5;
         this.mTerminalY = var6;
         this.mTransitionPosition = (int[])this.mViewInHierarchy.getTag(R.id.transition_position);
         if (this.mTransitionPosition != null) {
            this.mViewInHierarchy.setTag(R.id.transition_position, (Object)null);
         }

      }

      // $FF: synthetic method
      TransitionPositionListener(View var1, View var2, int var3, int var4, float var5, float var6, Object var7) {
         this(var1, var2, var3, var4, var5, var6);
      }

      public void onAnimationCancel(Animator var1) {
         if (this.mTransitionPosition == null) {
            this.mTransitionPosition = new int[2];
         }

         this.mTransitionPosition[0] = Math.round((float)this.mStartX + this.mMovingView.getTranslationX());
         this.mTransitionPosition[1] = Math.round((float)this.mStartY + this.mMovingView.getTranslationY());
         this.mViewInHierarchy.setTag(R.id.transition_position, this.mTransitionPosition);
      }

      public void onAnimationEnd(Animator var1) {
         this.mMovingView.setTranslationX(this.mTerminalX);
         this.mMovingView.setTranslationY(this.mTerminalY);
      }

      public void onAnimationPause(Animator var1) {
         this.mPausedX = this.mMovingView.getTranslationX();
         this.mPausedY = this.mMovingView.getTranslationY();
         this.mMovingView.setTranslationX(this.mTerminalX);
         this.mMovingView.setTranslationY(this.mTerminalY);
      }

      public void onAnimationResume(Animator var1) {
         this.mMovingView.setTranslationX(this.mPausedX);
         this.mMovingView.setTranslationY(this.mPausedY);
      }
   }
}
