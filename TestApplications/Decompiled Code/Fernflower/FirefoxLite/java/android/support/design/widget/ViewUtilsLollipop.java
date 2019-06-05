package android.support.design.widget;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.R;
import android.support.design.internal.ThemeEnforcement;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

class ViewUtilsLollipop {
   private static final int[] STATE_LIST_ANIM_ATTRS = new int[]{16843848};

   static void setBoundsViewOutlineProvider(View var0) {
      var0.setOutlineProvider(ViewOutlineProvider.BOUNDS);
   }

   static void setDefaultAppBarLayoutStateListAnimator(View var0, float var1) {
      int var2 = var0.getResources().getInteger(R.integer.app_bar_elevation_anim_duration);
      android.animation.StateListAnimator var3 = new android.animation.StateListAnimator();
      int var4 = R.attr.state_liftable;
      int var5 = -R.attr.state_lifted;
      ObjectAnimator var6 = ObjectAnimator.ofFloat(var0, "elevation", new float[]{0.0F});
      long var7 = (long)var2;
      var6 = var6.setDuration(var7);
      var3.addState(new int[]{16842766, var4, var5}, var6);
      var6 = ObjectAnimator.ofFloat(var0, "elevation", new float[]{var1}).setDuration(var7);
      var3.addState(new int[]{16842766}, var6);
      var6 = ObjectAnimator.ofFloat(var0, "elevation", new float[]{0.0F}).setDuration(0L);
      var3.addState(new int[0], var6);
      var0.setStateListAnimator(var3);
   }

   static void setStateListAnimatorFromAttrs(View var0, AttributeSet var1, int var2, int var3) {
      Context var4 = var0.getContext();
      TypedArray var7 = ThemeEnforcement.obtainStyledAttributes(var4, var1, STATE_LIST_ANIM_ATTRS, var2, var3);

      try {
         if (var7.hasValue(0)) {
            var0.setStateListAnimator(AnimatorInflater.loadStateListAnimator(var4, var7.getResourceId(0, 0)));
         }
      } finally {
         var7.recycle();
      }

   }
}
