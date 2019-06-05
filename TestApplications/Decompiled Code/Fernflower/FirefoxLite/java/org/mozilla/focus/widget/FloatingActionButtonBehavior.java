package org.mozilla.focus.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;

public class FloatingActionButtonBehavior extends CoordinatorLayout.Behavior implements AppBarLayout.OnOffsetChangedListener {
   private FloatingActionButton button;
   private AppBarLayout layout;
   private boolean visible = true;

   public FloatingActionButtonBehavior(Context var1, AttributeSet var2) {
   }

   private void animate(final View var1, final boolean var2) {
      ViewPropertyAnimator var3 = var1.animate();
      float var4 = 1.0F;
      float var5;
      if (var2) {
         var5 = 0.0F;
      } else {
         var5 = 1.0F;
      }

      var3 = var3.scaleX(var5);
      var5 = var4;
      if (var2) {
         var5 = 0.0F;
      }

      var3.scaleY(var5).setDuration(300L).setListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1x) {
            FloatingActionButtonBehavior.this.visible = var2 ^ true;
            if (var2) {
               var1.setVisibility(8);
            }

         }

         public void onAnimationStart(Animator var1x) {
            if (!var2) {
               var1.setVisibility(0);
            }

         }
      }).start();
   }

   private void hideButton() {
      this.animate(this.button, true);
   }

   private void showButton() {
      this.animate(this.button, false);
   }

   public boolean layoutDependsOn(CoordinatorLayout var1, FloatingActionButton var2, View var3) {
      if (this.button != var2) {
         this.button = var2;
      }

      if (var3 instanceof AppBarLayout && this.layout != var3) {
         this.layout = (AppBarLayout)var3;
         this.layout.addOnOffsetChangedListener((AppBarLayout.OnOffsetChangedListener)this);
         return true;
      } else {
         return super.layoutDependsOn(var1, var2, var3);
      }
   }

   public void onDependentViewRemoved(CoordinatorLayout var1, FloatingActionButton var2, View var3) {
      super.onDependentViewRemoved(var1, var2, var3);
      this.layout.removeOnOffsetChangedListener((AppBarLayout.OnOffsetChangedListener)this);
      this.layout = null;
   }

   public void onOffsetChanged(AppBarLayout var1, int var2) {
      if (var2 == 0 && !this.visible) {
         this.showButton();
      } else if (Math.abs(var2) >= var1.getTotalScrollRange() && this.visible) {
         this.hideButton();
      }

   }
}
