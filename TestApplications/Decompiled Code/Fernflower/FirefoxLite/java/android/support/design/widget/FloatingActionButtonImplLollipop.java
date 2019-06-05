package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build.VERSION;
import android.support.design.ripple.RippleUtils;
import android.view.View;
import java.util.ArrayList;

class FloatingActionButtonImplLollipop extends FloatingActionButtonImpl {
   private InsetDrawable insetDrawable;

   FloatingActionButtonImplLollipop(VisibilityAwareImageButton var1, ShadowViewDelegate var2) {
      super(var1, var2);
   }

   private Animator createElevationAnimator(float var1, float var2) {
      AnimatorSet var3 = new AnimatorSet();
      var3.play(ObjectAnimator.ofFloat(this.view, "elevation", new float[]{var1}).setDuration(0L)).with(ObjectAnimator.ofFloat(this.view, View.TRANSLATION_Z, new float[]{var2}).setDuration(100L));
      var3.setInterpolator(ELEVATION_ANIM_INTERPOLATOR);
      return var3;
   }

   public float getElevation() {
      return this.view.getElevation();
   }

   void getPadding(Rect var1) {
      if (this.shadowViewDelegate.isCompatPaddingEnabled()) {
         float var2 = this.shadowViewDelegate.getRadius();
         float var3 = this.getElevation() + this.pressedTranslationZ;
         int var4 = (int)Math.ceil((double)ShadowDrawableWrapper.calculateHorizontalPadding(var3, var2, false));
         int var5 = (int)Math.ceil((double)ShadowDrawableWrapper.calculateVerticalPadding(var3, var2, false));
         var1.set(var4, var5, var4, var5);
      } else {
         var1.set(0, 0, 0, 0);
      }

   }

   void jumpDrawableToCurrentState() {
   }

   void onCompatShadowChanged() {
      this.updatePadding();
   }

   void onDrawableStateChanged(int[] var1) {
      if (VERSION.SDK_INT == 21) {
         if (this.view.isEnabled()) {
            this.view.setElevation(this.elevation);
            if (this.view.isPressed()) {
               this.view.setTranslationZ(this.pressedTranslationZ);
            } else if (!this.view.isFocused() && !this.view.isHovered()) {
               this.view.setTranslationZ(0.0F);
            } else {
               this.view.setTranslationZ(this.hoveredFocusedTranslationZ);
            }
         } else {
            this.view.setElevation(0.0F);
            this.view.setTranslationZ(0.0F);
         }
      }

   }

   void onElevationsChanged(float var1, float var2, float var3) {
      if (VERSION.SDK_INT == 21) {
         this.view.refreshDrawableState();
      } else {
         android.animation.StateListAnimator var4 = new android.animation.StateListAnimator();
         var4.addState(PRESSED_ENABLED_STATE_SET, this.createElevationAnimator(var1, var3));
         var4.addState(HOVERED_FOCUSED_ENABLED_STATE_SET, this.createElevationAnimator(var1, var2));
         var4.addState(FOCUSED_ENABLED_STATE_SET, this.createElevationAnimator(var1, var2));
         var4.addState(HOVERED_ENABLED_STATE_SET, this.createElevationAnimator(var1, var2));
         AnimatorSet var5 = new AnimatorSet();
         ArrayList var6 = new ArrayList();
         var6.add(ObjectAnimator.ofFloat(this.view, "elevation", new float[]{var1}).setDuration(0L));
         if (VERSION.SDK_INT >= 22 && VERSION.SDK_INT <= 24) {
            var6.add(ObjectAnimator.ofFloat(this.view, View.TRANSLATION_Z, new float[]{this.view.getTranslationZ()}).setDuration(100L));
         }

         var6.add(ObjectAnimator.ofFloat(this.view, View.TRANSLATION_Z, new float[]{0.0F}).setDuration(100L));
         var5.playSequentially((Animator[])var6.toArray(new Animator[0]));
         var5.setInterpolator(ELEVATION_ANIM_INTERPOLATOR);
         var4.addState(ENABLED_STATE_SET, var5);
         var4.addState(EMPTY_STATE_SET, this.createElevationAnimator(0.0F, 0.0F));
         this.view.setStateListAnimator(var4);
      }

      if (this.shadowViewDelegate.isCompatPaddingEnabled()) {
         this.updatePadding();
      }

   }

   void onPaddingUpdated(Rect var1) {
      if (this.shadowViewDelegate.isCompatPaddingEnabled()) {
         this.insetDrawable = new InsetDrawable(this.rippleDrawable, var1.left, var1.top, var1.right, var1.bottom);
         this.shadowViewDelegate.setBackgroundDrawable(this.insetDrawable);
      } else {
         this.shadowViewDelegate.setBackgroundDrawable(this.rippleDrawable);
      }

   }

   boolean requirePreDrawListener() {
      return false;
   }

   void setRippleColor(ColorStateList var1) {
      if (this.rippleDrawable instanceof RippleDrawable) {
         ((RippleDrawable)this.rippleDrawable).setColor(RippleUtils.convertToRippleDrawableColor(var1));
      } else {
         super.setRippleColor(var1);
      }

   }
}
