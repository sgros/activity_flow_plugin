package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.res.ColorStateList;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.design.R;
import android.support.design.animation.AnimationUtils;
import android.support.design.animation.AnimatorSetCompat;
import android.support.design.animation.ImageMatrixProperty;
import android.support.design.animation.MatrixEvaluator;
import android.support.design.animation.MotionSpec;
import android.support.design.ripple.RippleUtils;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewTreeObserver.OnPreDrawListener;
import java.util.ArrayList;
import java.util.Iterator;

class FloatingActionButtonImpl {
   static final TimeInterpolator ELEVATION_ANIM_INTERPOLATOR;
   static final int[] EMPTY_STATE_SET;
   static final int[] ENABLED_STATE_SET;
   static final int[] FOCUSED_ENABLED_STATE_SET;
   static final int[] HOVERED_ENABLED_STATE_SET;
   static final int[] HOVERED_FOCUSED_ENABLED_STATE_SET;
   static final int[] PRESSED_ENABLED_STATE_SET;
   int animState = 0;
   CircularBorderDrawable borderDrawable;
   Drawable contentBackground;
   Animator currentAnimator;
   private MotionSpec defaultHideMotionSpec;
   private MotionSpec defaultShowMotionSpec;
   float elevation;
   private ArrayList hideListeners;
   MotionSpec hideMotionSpec;
   float hoveredFocusedTranslationZ;
   float imageMatrixScale = 1.0F;
   int maxImageSize;
   private OnPreDrawListener preDrawListener;
   float pressedTranslationZ;
   Drawable rippleDrawable;
   private float rotation;
   ShadowDrawableWrapper shadowDrawable;
   final ShadowViewDelegate shadowViewDelegate;
   Drawable shapeDrawable;
   private ArrayList showListeners;
   MotionSpec showMotionSpec;
   private final StateListAnimator stateListAnimator;
   private final Matrix tmpMatrix = new Matrix();
   private final Rect tmpRect = new Rect();
   private final RectF tmpRectF1 = new RectF();
   private final RectF tmpRectF2 = new RectF();
   final VisibilityAwareImageButton view;

   static {
      ELEVATION_ANIM_INTERPOLATOR = AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR;
      PRESSED_ENABLED_STATE_SET = new int[]{16842919, 16842910};
      HOVERED_FOCUSED_ENABLED_STATE_SET = new int[]{16843623, 16842908, 16842910};
      FOCUSED_ENABLED_STATE_SET = new int[]{16842908, 16842910};
      HOVERED_ENABLED_STATE_SET = new int[]{16843623, 16842910};
      ENABLED_STATE_SET = new int[]{16842910};
      EMPTY_STATE_SET = new int[0];
   }

   FloatingActionButtonImpl(VisibilityAwareImageButton var1, ShadowViewDelegate var2) {
      this.view = var1;
      this.shadowViewDelegate = var2;
      this.stateListAnimator = new StateListAnimator();
      this.stateListAnimator.addState(PRESSED_ENABLED_STATE_SET, this.createElevationAnimator(new FloatingActionButtonImpl.ElevateToPressedTranslationZAnimation()));
      this.stateListAnimator.addState(HOVERED_FOCUSED_ENABLED_STATE_SET, this.createElevationAnimator(new FloatingActionButtonImpl.ElevateToHoveredFocusedTranslationZAnimation()));
      this.stateListAnimator.addState(FOCUSED_ENABLED_STATE_SET, this.createElevationAnimator(new FloatingActionButtonImpl.ElevateToHoveredFocusedTranslationZAnimation()));
      this.stateListAnimator.addState(HOVERED_ENABLED_STATE_SET, this.createElevationAnimator(new FloatingActionButtonImpl.ElevateToHoveredFocusedTranslationZAnimation()));
      this.stateListAnimator.addState(ENABLED_STATE_SET, this.createElevationAnimator(new FloatingActionButtonImpl.ResetElevationAnimation()));
      this.stateListAnimator.addState(EMPTY_STATE_SET, this.createElevationAnimator(new FloatingActionButtonImpl.DisabledElevationAnimation()));
      this.rotation = this.view.getRotation();
   }

   private void calculateImageMatrixFromScale(float var1, Matrix var2) {
      var2.reset();
      Drawable var3 = this.view.getDrawable();
      if (var3 != null && this.maxImageSize != 0) {
         RectF var4 = this.tmpRectF1;
         RectF var5 = this.tmpRectF2;
         var4.set(0.0F, 0.0F, (float)var3.getIntrinsicWidth(), (float)var3.getIntrinsicHeight());
         var5.set(0.0F, 0.0F, (float)this.maxImageSize, (float)this.maxImageSize);
         var2.setRectToRect(var4, var5, ScaleToFit.CENTER);
         var2.postScale(var1, var1, (float)this.maxImageSize / 2.0F, (float)this.maxImageSize / 2.0F);
      }

   }

   private AnimatorSet createAnimator(MotionSpec var1, float var2, float var3, float var4) {
      ArrayList var5 = new ArrayList();
      ObjectAnimator var6 = ObjectAnimator.ofFloat(this.view, View.ALPHA, new float[]{var2});
      var1.getTiming("opacity").apply(var6);
      var5.add(var6);
      var6 = ObjectAnimator.ofFloat(this.view, View.SCALE_X, new float[]{var3});
      var1.getTiming("scale").apply(var6);
      var5.add(var6);
      var6 = ObjectAnimator.ofFloat(this.view, View.SCALE_Y, new float[]{var3});
      var1.getTiming("scale").apply(var6);
      var5.add(var6);
      this.calculateImageMatrixFromScale(var4, this.tmpMatrix);
      var6 = ObjectAnimator.ofObject(this.view, new ImageMatrixProperty(), new MatrixEvaluator(), new Matrix[]{new Matrix(this.tmpMatrix)});
      var1.getTiming("iconScale").apply(var6);
      var5.add(var6);
      AnimatorSet var7 = new AnimatorSet();
      AnimatorSetCompat.playTogether(var7, var5);
      return var7;
   }

   private ValueAnimator createElevationAnimator(FloatingActionButtonImpl.ShadowAnimatorImpl var1) {
      ValueAnimator var2 = new ValueAnimator();
      var2.setInterpolator(ELEVATION_ANIM_INTERPOLATOR);
      var2.setDuration(100L);
      var2.addListener(var1);
      var2.addUpdateListener(var1);
      var2.setFloatValues(new float[]{0.0F, 1.0F});
      return var2;
   }

   private void ensurePreDrawListener() {
      if (this.preDrawListener == null) {
         this.preDrawListener = new OnPreDrawListener() {
            public boolean onPreDraw() {
               FloatingActionButtonImpl.this.onPreDraw();
               return true;
            }
         };
      }

   }

   private MotionSpec getDefaultHideMotionSpec() {
      if (this.defaultHideMotionSpec == null) {
         this.defaultHideMotionSpec = MotionSpec.createFromResource(this.view.getContext(), R.animator.design_fab_hide_motion_spec);
      }

      return this.defaultHideMotionSpec;
   }

   private MotionSpec getDefaultShowMotionSpec() {
      if (this.defaultShowMotionSpec == null) {
         this.defaultShowMotionSpec = MotionSpec.createFromResource(this.view.getContext(), R.animator.design_fab_show_motion_spec);
      }

      return this.defaultShowMotionSpec;
   }

   private boolean shouldAnimateVisibilityChange() {
      boolean var1;
      if (ViewCompat.isLaidOut(this.view) && !this.view.isInEditMode()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private void updateFromViewRotation() {
      if (VERSION.SDK_INT == 19) {
         if (this.rotation % 90.0F != 0.0F) {
            if (this.view.getLayerType() != 1) {
               this.view.setLayerType(1, (Paint)null);
            }
         } else if (this.view.getLayerType() != 0) {
            this.view.setLayerType(0, (Paint)null);
         }
      }

      if (this.shadowDrawable != null) {
         this.shadowDrawable.setRotation(-this.rotation);
      }

      if (this.borderDrawable != null) {
         this.borderDrawable.setRotation(-this.rotation);
      }

   }

   public void addOnHideAnimationListener(AnimatorListener var1) {
      if (this.hideListeners == null) {
         this.hideListeners = new ArrayList();
      }

      this.hideListeners.add(var1);
   }

   void addOnShowAnimationListener(AnimatorListener var1) {
      if (this.showListeners == null) {
         this.showListeners = new ArrayList();
      }

      this.showListeners.add(var1);
   }

   final Drawable getContentBackground() {
      return this.contentBackground;
   }

   float getElevation() {
      return this.elevation;
   }

   final MotionSpec getHideMotionSpec() {
      return this.hideMotionSpec;
   }

   float getHoveredFocusedTranslationZ() {
      return this.hoveredFocusedTranslationZ;
   }

   void getPadding(Rect var1) {
      this.shadowDrawable.getPadding(var1);
   }

   float getPressedTranslationZ() {
      return this.pressedTranslationZ;
   }

   final MotionSpec getShowMotionSpec() {
      return this.showMotionSpec;
   }

   void hide(final FloatingActionButtonImpl.InternalVisibilityChangedListener var1, final boolean var2) {
      if (!this.isOrWillBeHidden()) {
         if (this.currentAnimator != null) {
            this.currentAnimator.cancel();
         }

         if (this.shouldAnimateVisibilityChange()) {
            MotionSpec var3;
            if (this.hideMotionSpec != null) {
               var3 = this.hideMotionSpec;
            } else {
               var3 = this.getDefaultHideMotionSpec();
            }

            AnimatorSet var6 = this.createAnimator(var3, 0.0F, 0.0F, 0.0F);
            var6.addListener(new AnimatorListenerAdapter() {
               private boolean cancelled;

               public void onAnimationCancel(Animator var1x) {
                  this.cancelled = true;
               }

               public void onAnimationEnd(Animator var1x) {
                  FloatingActionButtonImpl.this.animState = 0;
                  FloatingActionButtonImpl.this.currentAnimator = null;
                  if (!this.cancelled) {
                     VisibilityAwareImageButton var3 = FloatingActionButtonImpl.this.view;
                     byte var2x;
                     if (var2) {
                        var2x = 8;
                     } else {
                        var2x = 4;
                     }

                     var3.internalSetVisibility(var2x, var2);
                     if (var1 != null) {
                        var1.onHidden();
                     }
                  }

               }

               public void onAnimationStart(Animator var1x) {
                  FloatingActionButtonImpl.this.view.internalSetVisibility(0, var2);
                  FloatingActionButtonImpl.this.animState = 1;
                  FloatingActionButtonImpl.this.currentAnimator = var1x;
                  this.cancelled = false;
               }
            });
            if (this.hideListeners != null) {
               Iterator var5 = this.hideListeners.iterator();

               while(var5.hasNext()) {
                  var6.addListener((AnimatorListener)var5.next());
               }
            }

            var6.start();
         } else {
            VisibilityAwareImageButton var7 = this.view;
            byte var4;
            if (var2) {
               var4 = 8;
            } else {
               var4 = 4;
            }

            var7.internalSetVisibility(var4, var2);
            if (var1 != null) {
               var1.onHidden();
            }
         }

      }
   }

   boolean isOrWillBeHidden() {
      int var1 = this.view.getVisibility();
      boolean var2 = false;
      boolean var3 = false;
      if (var1 == 0) {
         if (this.animState == 1) {
            var3 = true;
         }

         return var3;
      } else {
         var3 = var2;
         if (this.animState != 2) {
            var3 = true;
         }

         return var3;
      }
   }

   boolean isOrWillBeShown() {
      int var1 = this.view.getVisibility();
      boolean var2 = false;
      boolean var3 = false;
      if (var1 != 0) {
         if (this.animState == 2) {
            var3 = true;
         }

         return var3;
      } else {
         var3 = var2;
         if (this.animState != 1) {
            var3 = true;
         }

         return var3;
      }
   }

   void jumpDrawableToCurrentState() {
      this.stateListAnimator.jumpToCurrentState();
   }

   void onAttachedToWindow() {
      if (this.requirePreDrawListener()) {
         this.ensurePreDrawListener();
         this.view.getViewTreeObserver().addOnPreDrawListener(this.preDrawListener);
      }

   }

   void onCompatShadowChanged() {
   }

   void onDetachedFromWindow() {
      if (this.preDrawListener != null) {
         this.view.getViewTreeObserver().removeOnPreDrawListener(this.preDrawListener);
         this.preDrawListener = null;
      }

   }

   void onDrawableStateChanged(int[] var1) {
      this.stateListAnimator.setState(var1);
   }

   void onElevationsChanged(float var1, float var2, float var3) {
      if (this.shadowDrawable != null) {
         this.shadowDrawable.setShadowSize(var1, this.pressedTranslationZ + var1);
         this.updatePadding();
      }

   }

   void onPaddingUpdated(Rect var1) {
   }

   void onPreDraw() {
      float var1 = this.view.getRotation();
      if (this.rotation != var1) {
         this.rotation = var1;
         this.updateFromViewRotation();
      }

   }

   public void removeOnHideAnimationListener(AnimatorListener var1) {
      if (this.hideListeners != null) {
         this.hideListeners.remove(var1);
      }
   }

   void removeOnShowAnimationListener(AnimatorListener var1) {
      if (this.showListeners != null) {
         this.showListeners.remove(var1);
      }
   }

   boolean requirePreDrawListener() {
      return true;
   }

   void setBackgroundTintList(ColorStateList var1) {
      if (this.shapeDrawable != null) {
         DrawableCompat.setTintList(this.shapeDrawable, var1);
      }

      if (this.borderDrawable != null) {
         this.borderDrawable.setBorderTint(var1);
      }

   }

   void setBackgroundTintMode(Mode var1) {
      if (this.shapeDrawable != null) {
         DrawableCompat.setTintMode(this.shapeDrawable, var1);
      }

   }

   final void setElevation(float var1) {
      if (this.elevation != var1) {
         this.elevation = var1;
         this.onElevationsChanged(this.elevation, this.hoveredFocusedTranslationZ, this.pressedTranslationZ);
      }

   }

   final void setHideMotionSpec(MotionSpec var1) {
      this.hideMotionSpec = var1;
   }

   final void setHoveredFocusedTranslationZ(float var1) {
      if (this.hoveredFocusedTranslationZ != var1) {
         this.hoveredFocusedTranslationZ = var1;
         this.onElevationsChanged(this.elevation, this.hoveredFocusedTranslationZ, this.pressedTranslationZ);
      }

   }

   final void setImageMatrixScale(float var1) {
      this.imageMatrixScale = var1;
      Matrix var2 = this.tmpMatrix;
      this.calculateImageMatrixFromScale(var1, var2);
      this.view.setImageMatrix(var2);
   }

   final void setPressedTranslationZ(float var1) {
      if (this.pressedTranslationZ != var1) {
         this.pressedTranslationZ = var1;
         this.onElevationsChanged(this.elevation, this.hoveredFocusedTranslationZ, this.pressedTranslationZ);
      }

   }

   void setRippleColor(ColorStateList var1) {
      if (this.rippleDrawable != null) {
         DrawableCompat.setTintList(this.rippleDrawable, RippleUtils.convertToRippleDrawableColor(var1));
      }

   }

   final void setShowMotionSpec(MotionSpec var1) {
      this.showMotionSpec = var1;
   }

   void show(final FloatingActionButtonImpl.InternalVisibilityChangedListener var1, final boolean var2) {
      if (!this.isOrWillBeShown()) {
         if (this.currentAnimator != null) {
            this.currentAnimator.cancel();
         }

         if (this.shouldAnimateVisibilityChange()) {
            if (this.view.getVisibility() != 0) {
               this.view.setAlpha(0.0F);
               this.view.setScaleY(0.0F);
               this.view.setScaleX(0.0F);
               this.setImageMatrixScale(0.0F);
            }

            MotionSpec var3;
            if (this.showMotionSpec != null) {
               var3 = this.showMotionSpec;
            } else {
               var3 = this.getDefaultShowMotionSpec();
            }

            AnimatorSet var5 = this.createAnimator(var3, 1.0F, 1.0F, 1.0F);
            var5.addListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1x) {
                  FloatingActionButtonImpl.this.animState = 0;
                  FloatingActionButtonImpl.this.currentAnimator = null;
                  if (var1 != null) {
                     var1.onShown();
                  }

               }

               public void onAnimationStart(Animator var1x) {
                  FloatingActionButtonImpl.this.view.internalSetVisibility(0, var2);
                  FloatingActionButtonImpl.this.animState = 2;
                  FloatingActionButtonImpl.this.currentAnimator = var1x;
               }
            });
            if (this.showListeners != null) {
               Iterator var4 = this.showListeners.iterator();

               while(var4.hasNext()) {
                  var5.addListener((AnimatorListener)var4.next());
               }
            }

            var5.start();
         } else {
            this.view.internalSetVisibility(0, var2);
            this.view.setAlpha(1.0F);
            this.view.setScaleY(1.0F);
            this.view.setScaleX(1.0F);
            this.setImageMatrixScale(1.0F);
            if (var1 != null) {
               var1.onShown();
            }
         }

      }
   }

   final void updateImageMatrixScale() {
      this.setImageMatrixScale(this.imageMatrixScale);
   }

   final void updatePadding() {
      Rect var1 = this.tmpRect;
      this.getPadding(var1);
      this.onPaddingUpdated(var1);
      this.shadowViewDelegate.setShadowPadding(var1.left, var1.top, var1.right, var1.bottom);
   }

   private class DisabledElevationAnimation extends FloatingActionButtonImpl.ShadowAnimatorImpl {
      DisabledElevationAnimation() {
         super(null);
      }

      protected float getTargetShadowSize() {
         return 0.0F;
      }
   }

   private class ElevateToHoveredFocusedTranslationZAnimation extends FloatingActionButtonImpl.ShadowAnimatorImpl {
      ElevateToHoveredFocusedTranslationZAnimation() {
         super(null);
      }

      protected float getTargetShadowSize() {
         return FloatingActionButtonImpl.this.elevation + FloatingActionButtonImpl.this.hoveredFocusedTranslationZ;
      }
   }

   private class ElevateToPressedTranslationZAnimation extends FloatingActionButtonImpl.ShadowAnimatorImpl {
      ElevateToPressedTranslationZAnimation() {
         super(null);
      }

      protected float getTargetShadowSize() {
         return FloatingActionButtonImpl.this.elevation + FloatingActionButtonImpl.this.pressedTranslationZ;
      }
   }

   interface InternalVisibilityChangedListener {
      void onHidden();

      void onShown();
   }

   private class ResetElevationAnimation extends FloatingActionButtonImpl.ShadowAnimatorImpl {
      ResetElevationAnimation() {
         super(null);
      }

      protected float getTargetShadowSize() {
         return FloatingActionButtonImpl.this.elevation;
      }
   }

   private abstract class ShadowAnimatorImpl extends AnimatorListenerAdapter implements AnimatorUpdateListener {
      private float shadowSizeEnd;
      private float shadowSizeStart;
      private boolean validValues;

      private ShadowAnimatorImpl() {
      }

      // $FF: synthetic method
      ShadowAnimatorImpl(Object var2) {
         this();
      }

      protected abstract float getTargetShadowSize();

      public void onAnimationEnd(Animator var1) {
         FloatingActionButtonImpl.this.shadowDrawable.setShadowSize(this.shadowSizeEnd);
         this.validValues = false;
      }

      public void onAnimationUpdate(ValueAnimator var1) {
         if (!this.validValues) {
            this.shadowSizeStart = FloatingActionButtonImpl.this.shadowDrawable.getShadowSize();
            this.shadowSizeEnd = this.getTargetShadowSize();
            this.validValues = true;
         }

         FloatingActionButtonImpl.this.shadowDrawable.setShadowSize(this.shadowSizeStart + (this.shadowSizeEnd - this.shadowSizeStart) * var1.getAnimatedFraction());
      }
   }
}
