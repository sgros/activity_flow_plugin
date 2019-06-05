package android.support.design.behavior;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.support.design.animation.AnimationUtils;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;

public class HideBottomViewOnScrollBehavior extends CoordinatorLayout.Behavior {
   private ViewPropertyAnimator currentAnimator;
   private int currentState = 2;
   private int height = 0;

   public HideBottomViewOnScrollBehavior() {
   }

   public HideBottomViewOnScrollBehavior(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   private void animateChildTo(View var1, int var2, long var3, TimeInterpolator var5) {
      this.currentAnimator = var1.animate().translationY((float)var2).setInterpolator(var5).setDuration(var3).setListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1) {
            HideBottomViewOnScrollBehavior.this.currentAnimator = null;
         }
      });
   }

   public boolean onLayoutChild(CoordinatorLayout var1, View var2, int var3) {
      this.height = var2.getMeasuredHeight();
      return super.onLayoutChild(var1, var2, var3);
   }

   public void onNestedScroll(CoordinatorLayout var1, View var2, View var3, int var4, int var5, int var6, int var7) {
      if (this.currentState != 1 && var5 > 0) {
         this.slideDown(var2);
      } else if (this.currentState != 2 && var5 < 0) {
         this.slideUp(var2);
      }

   }

   public boolean onStartNestedScroll(CoordinatorLayout var1, View var2, View var3, View var4, int var5) {
      boolean var6;
      if (var5 == 2) {
         var6 = true;
      } else {
         var6 = false;
      }

      return var6;
   }

   protected void slideDown(View var1) {
      if (this.currentAnimator != null) {
         this.currentAnimator.cancel();
         var1.clearAnimation();
      }

      this.currentState = 1;
      this.animateChildTo(var1, this.height, 175L, AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR);
   }

   protected void slideUp(View var1) {
      if (this.currentAnimator != null) {
         this.currentAnimator.cancel();
         var1.clearAnimation();
      }

      this.currentState = 2;
      this.animateChildTo(var1, 0, 225L, AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR);
   }
}
