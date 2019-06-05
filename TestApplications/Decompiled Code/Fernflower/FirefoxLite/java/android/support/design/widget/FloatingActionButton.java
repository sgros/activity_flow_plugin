package android.support.design.widget;

import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.support.design.R;
import android.support.design.animation.MotionSpec;
import android.support.design.expandable.ExpandableTransformationWidget;
import android.support.design.expandable.ExpandableWidgetHelper;
import android.support.design.stateful.ExtendableSavedState;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.TintableBackgroundView;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.TintableImageSourceView;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.AppCompatImageHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import java.util.List;

@CoordinatorLayout.DefaultBehavior(FloatingActionButton.Behavior.class)
public class FloatingActionButton extends VisibilityAwareImageButton implements ExpandableTransformationWidget, TintableBackgroundView, TintableImageSourceView {
   private ColorStateList backgroundTint;
   private Mode backgroundTintMode;
   boolean compatPadding;
   private int customSize;
   private final ExpandableWidgetHelper expandableWidgetHelper;
   private final AppCompatImageHelper imageHelper;
   private Mode imageMode;
   private int imagePadding;
   private ColorStateList imageTint;
   private FloatingActionButtonImpl impl;
   private int maxImageSize;
   private ColorStateList rippleColor;
   final Rect shadowPadding;
   private int size;
   private final Rect touchArea;

   private FloatingActionButtonImpl createImpl() {
      return (FloatingActionButtonImpl)(VERSION.SDK_INT >= 21 ? new FloatingActionButtonImplLollipop(this, new FloatingActionButton.ShadowDelegateImpl()) : new FloatingActionButtonImpl(this, new FloatingActionButton.ShadowDelegateImpl()));
   }

   private FloatingActionButtonImpl getImpl() {
      if (this.impl == null) {
         this.impl = this.createImpl();
      }

      return this.impl;
   }

   private int getSizeDimension(int var1) {
      if (this.customSize != 0) {
         return this.customSize;
      } else {
         Resources var2 = this.getResources();
         if (var1 != -1) {
            return var1 != 1 ? var2.getDimensionPixelSize(R.dimen.design_fab_size_normal) : var2.getDimensionPixelSize(R.dimen.design_fab_size_mini);
         } else {
            if (Math.max(var2.getConfiguration().screenWidthDp, var2.getConfiguration().screenHeightDp) < 470) {
               var1 = this.getSizeDimension(1);
            } else {
               var1 = this.getSizeDimension(0);
            }

            return var1;
         }
      }
   }

   private void offsetRectWithShadow(Rect var1) {
      var1.left += this.shadowPadding.left;
      var1.top += this.shadowPadding.top;
      var1.right -= this.shadowPadding.right;
      var1.bottom -= this.shadowPadding.bottom;
   }

   private void onApplySupportImageTint() {
      Drawable var1 = this.getDrawable();
      if (var1 != null) {
         if (this.imageTint == null) {
            DrawableCompat.clearColorFilter(var1);
         } else {
            int var2 = this.imageTint.getColorForState(this.getDrawableState(), 0);
            Mode var3 = this.imageMode;
            Mode var4 = var3;
            if (var3 == null) {
               var4 = Mode.SRC_IN;
            }

            var1.mutate().setColorFilter(AppCompatDrawableManager.getPorterDuffColorFilter(var2, var4));
         }
      }
   }

   private static int resolveAdjustedSize(int var0, int var1) {
      int var2 = MeasureSpec.getMode(var1);
      var1 = MeasureSpec.getSize(var1);
      if (var2 != Integer.MIN_VALUE) {
         if (var2 != 0) {
            if (var2 != 1073741824) {
               throw new IllegalArgumentException();
            }

            var0 = var1;
         }
      } else {
         var0 = Math.min(var0, var1);
      }

      return var0;
   }

   private FloatingActionButtonImpl.InternalVisibilityChangedListener wrapOnVisibilityChangedListener(final FloatingActionButton.OnVisibilityChangedListener var1) {
      return var1 == null ? null : new FloatingActionButtonImpl.InternalVisibilityChangedListener() {
         public void onHidden() {
            var1.onHidden(FloatingActionButton.this);
         }

         public void onShown() {
            var1.onShown(FloatingActionButton.this);
         }
      };
   }

   public void addOnHideAnimationListener(AnimatorListener var1) {
      this.getImpl().addOnHideAnimationListener(var1);
   }

   public void addOnShowAnimationListener(AnimatorListener var1) {
      this.getImpl().addOnShowAnimationListener(var1);
   }

   protected void drawableStateChanged() {
      super.drawableStateChanged();
      this.getImpl().onDrawableStateChanged(this.getDrawableState());
   }

   public ColorStateList getBackgroundTintList() {
      return this.backgroundTint;
   }

   public Mode getBackgroundTintMode() {
      return this.backgroundTintMode;
   }

   public float getCompatElevation() {
      return this.getImpl().getElevation();
   }

   public float getCompatHoveredFocusedTranslationZ() {
      return this.getImpl().getHoveredFocusedTranslationZ();
   }

   public float getCompatPressedTranslationZ() {
      return this.getImpl().getPressedTranslationZ();
   }

   public Drawable getContentBackground() {
      return this.getImpl().getContentBackground();
   }

   @Deprecated
   public boolean getContentRect(Rect var1) {
      if (ViewCompat.isLaidOut(this)) {
         var1.set(0, 0, this.getWidth(), this.getHeight());
         this.offsetRectWithShadow(var1);
         return true;
      } else {
         return false;
      }
   }

   public int getCustomSize() {
      return this.customSize;
   }

   public int getExpandedComponentIdHint() {
      return this.expandableWidgetHelper.getExpandedComponentIdHint();
   }

   public MotionSpec getHideMotionSpec() {
      return this.getImpl().getHideMotionSpec();
   }

   public void getMeasuredContentRect(Rect var1) {
      var1.set(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
      this.offsetRectWithShadow(var1);
   }

   @Deprecated
   public int getRippleColor() {
      int var1;
      if (this.rippleColor != null) {
         var1 = this.rippleColor.getDefaultColor();
      } else {
         var1 = 0;
      }

      return var1;
   }

   public ColorStateList getRippleColorStateList() {
      return this.rippleColor;
   }

   public MotionSpec getShowMotionSpec() {
      return this.getImpl().getShowMotionSpec();
   }

   public int getSize() {
      return this.size;
   }

   int getSizeDimension() {
      return this.getSizeDimension(this.size);
   }

   public ColorStateList getSupportBackgroundTintList() {
      return this.getBackgroundTintList();
   }

   public Mode getSupportBackgroundTintMode() {
      return this.getBackgroundTintMode();
   }

   public ColorStateList getSupportImageTintList() {
      return this.imageTint;
   }

   public Mode getSupportImageTintMode() {
      return this.imageMode;
   }

   public boolean getUseCompatPadding() {
      return this.compatPadding;
   }

   void hide(FloatingActionButton.OnVisibilityChangedListener var1, boolean var2) {
      this.getImpl().hide(this.wrapOnVisibilityChangedListener(var1), var2);
   }

   public boolean isExpanded() {
      return this.expandableWidgetHelper.isExpanded();
   }

   public boolean isOrWillBeShown() {
      return this.getImpl().isOrWillBeShown();
   }

   public void jumpDrawablesToCurrentState() {
      super.jumpDrawablesToCurrentState();
      this.getImpl().jumpDrawableToCurrentState();
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.getImpl().onAttachedToWindow();
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.getImpl().onDetachedFromWindow();
   }

   protected void onMeasure(int var1, int var2) {
      int var3 = this.getSizeDimension();
      this.imagePadding = (var3 - this.maxImageSize) / 2;
      this.getImpl().updatePadding();
      var1 = Math.min(resolveAdjustedSize(var3, var1), resolveAdjustedSize(var3, var2));
      this.setMeasuredDimension(this.shadowPadding.left + var1 + this.shadowPadding.right, var1 + this.shadowPadding.top + this.shadowPadding.bottom);
   }

   protected void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof ExtendableSavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         ExtendableSavedState var2 = (ExtendableSavedState)var1;
         super.onRestoreInstanceState(var2.getSuperState());
         this.expandableWidgetHelper.onRestoreInstanceState((Bundle)var2.extendableStates.get("expandableWidgetHelper"));
      }
   }

   protected Parcelable onSaveInstanceState() {
      ExtendableSavedState var1 = new ExtendableSavedState(super.onSaveInstanceState());
      var1.extendableStates.put("expandableWidgetHelper", this.expandableWidgetHelper.onSaveInstanceState());
      return var1;
   }

   public boolean onTouchEvent(MotionEvent var1) {
      return var1.getAction() == 0 && this.getContentRect(this.touchArea) && !this.touchArea.contains((int)var1.getX(), (int)var1.getY()) ? false : super.onTouchEvent(var1);
   }

   public void removeOnHideAnimationListener(AnimatorListener var1) {
      this.getImpl().removeOnHideAnimationListener(var1);
   }

   public void removeOnShowAnimationListener(AnimatorListener var1) {
      this.getImpl().removeOnShowAnimationListener(var1);
   }

   public void setBackgroundColor(int var1) {
      Log.i("FloatingActionButton", "Setting a custom background is not supported.");
   }

   public void setBackgroundDrawable(Drawable var1) {
      Log.i("FloatingActionButton", "Setting a custom background is not supported.");
   }

   public void setBackgroundResource(int var1) {
      Log.i("FloatingActionButton", "Setting a custom background is not supported.");
   }

   public void setBackgroundTintList(ColorStateList var1) {
      if (this.backgroundTint != var1) {
         this.backgroundTint = var1;
         this.getImpl().setBackgroundTintList(var1);
      }

   }

   public void setBackgroundTintMode(Mode var1) {
      if (this.backgroundTintMode != var1) {
         this.backgroundTintMode = var1;
         this.getImpl().setBackgroundTintMode(var1);
      }

   }

   public void setCompatElevation(float var1) {
      this.getImpl().setElevation(var1);
   }

   public void setCompatElevationResource(int var1) {
      this.setCompatElevation(this.getResources().getDimension(var1));
   }

   public void setCompatHoveredFocusedTranslationZ(float var1) {
      this.getImpl().setHoveredFocusedTranslationZ(var1);
   }

   public void setCompatHoveredFocusedTranslationZResource(int var1) {
      this.setCompatHoveredFocusedTranslationZ(this.getResources().getDimension(var1));
   }

   public void setCompatPressedTranslationZ(float var1) {
      this.getImpl().setPressedTranslationZ(var1);
   }

   public void setCompatPressedTranslationZResource(int var1) {
      this.setCompatPressedTranslationZ(this.getResources().getDimension(var1));
   }

   public void setCustomSize(int var1) {
      if (var1 >= 0) {
         this.customSize = var1;
      } else {
         throw new IllegalArgumentException("Custom size must be non-negative");
      }
   }

   public void setExpandedComponentIdHint(int var1) {
      this.expandableWidgetHelper.setExpandedComponentIdHint(var1);
   }

   public void setHideMotionSpec(MotionSpec var1) {
      this.getImpl().setHideMotionSpec(var1);
   }

   public void setHideMotionSpecResource(int var1) {
      this.setHideMotionSpec(MotionSpec.createFromResource(this.getContext(), var1));
   }

   public void setImageDrawable(Drawable var1) {
      super.setImageDrawable(var1);
      this.getImpl().updateImageMatrixScale();
   }

   public void setImageResource(int var1) {
      this.imageHelper.setImageResource(var1);
   }

   public void setRippleColor(int var1) {
      this.setRippleColor(ColorStateList.valueOf(var1));
   }

   public void setRippleColor(ColorStateList var1) {
      if (this.rippleColor != var1) {
         this.rippleColor = var1;
         this.getImpl().setRippleColor(this.rippleColor);
      }

   }

   public void setShowMotionSpec(MotionSpec var1) {
      this.getImpl().setShowMotionSpec(var1);
   }

   public void setShowMotionSpecResource(int var1) {
      this.setShowMotionSpec(MotionSpec.createFromResource(this.getContext(), var1));
   }

   public void setSize(int var1) {
      this.customSize = 0;
      if (var1 != this.size) {
         this.size = var1;
         this.requestLayout();
      }

   }

   public void setSupportBackgroundTintList(ColorStateList var1) {
      this.setBackgroundTintList(var1);
   }

   public void setSupportBackgroundTintMode(Mode var1) {
      this.setBackgroundTintMode(var1);
   }

   public void setSupportImageTintList(ColorStateList var1) {
      if (this.imageTint != var1) {
         this.imageTint = var1;
         this.onApplySupportImageTint();
      }

   }

   public void setSupportImageTintMode(Mode var1) {
      if (this.imageMode != var1) {
         this.imageMode = var1;
         this.onApplySupportImageTint();
      }

   }

   public void setUseCompatPadding(boolean var1) {
      if (this.compatPadding != var1) {
         this.compatPadding = var1;
         this.getImpl().onCompatShadowChanged();
      }

   }

   void show(FloatingActionButton.OnVisibilityChangedListener var1, boolean var2) {
      this.getImpl().show(this.wrapOnVisibilityChangedListener(var1), var2);
   }

   protected static class BaseBehavior extends CoordinatorLayout.Behavior {
      private boolean autoHideEnabled;
      private FloatingActionButton.OnVisibilityChangedListener internalAutoHideListener;
      private Rect tmpRect;

      public BaseBehavior() {
         this.autoHideEnabled = true;
      }

      public BaseBehavior(Context var1, AttributeSet var2) {
         super(var1, var2);
         TypedArray var3 = var1.obtainStyledAttributes(var2, R.styleable.FloatingActionButton_Behavior_Layout);
         this.autoHideEnabled = var3.getBoolean(R.styleable.FloatingActionButton_Behavior_Layout_behavior_autoHide, true);
         var3.recycle();
      }

      private static boolean isBottomSheet(View var0) {
         LayoutParams var1 = var0.getLayoutParams();
         return var1 instanceof CoordinatorLayout.LayoutParams ? ((CoordinatorLayout.LayoutParams)var1).getBehavior() instanceof BottomSheetBehavior : false;
      }

      private void offsetIfNeeded(CoordinatorLayout var1, FloatingActionButton var2) {
         Rect var3 = var2.shadowPadding;
         if (var3 != null && var3.centerX() > 0 && var3.centerY() > 0) {
            CoordinatorLayout.LayoutParams var4 = (CoordinatorLayout.LayoutParams)var2.getLayoutParams();
            int var5 = var2.getRight();
            int var6 = var1.getWidth();
            int var7 = var4.rightMargin;
            int var8 = 0;
            if (var5 >= var6 - var7) {
               var6 = var3.right;
            } else if (var2.getLeft() <= var4.leftMargin) {
               var6 = -var3.left;
            } else {
               var6 = 0;
            }

            if (var2.getBottom() >= var1.getHeight() - var4.bottomMargin) {
               var8 = var3.bottom;
            } else if (var2.getTop() <= var4.topMargin) {
               var8 = -var3.top;
            }

            if (var8 != 0) {
               ViewCompat.offsetTopAndBottom(var2, var8);
            }

            if (var6 != 0) {
               ViewCompat.offsetLeftAndRight(var2, var6);
            }
         }

      }

      private boolean shouldUpdateVisibility(View var1, FloatingActionButton var2) {
         CoordinatorLayout.LayoutParams var3 = (CoordinatorLayout.LayoutParams)var2.getLayoutParams();
         if (!this.autoHideEnabled) {
            return false;
         } else if (var3.getAnchorId() != var1.getId()) {
            return false;
         } else {
            return var2.getUserSetVisibility() == 0;
         }
      }

      private boolean updateFabVisibilityForAppBarLayout(CoordinatorLayout var1, AppBarLayout var2, FloatingActionButton var3) {
         if (!this.shouldUpdateVisibility(var2, var3)) {
            return false;
         } else {
            if (this.tmpRect == null) {
               this.tmpRect = new Rect();
            }

            Rect var4 = this.tmpRect;
            DescendantOffsetUtils.getDescendantRect(var1, var2, var4);
            if (var4.bottom <= var2.getMinimumHeightForVisibleOverlappingContent()) {
               var3.hide(this.internalAutoHideListener, false);
            } else {
               var3.show(this.internalAutoHideListener, false);
            }

            return true;
         }
      }

      private boolean updateFabVisibilityForBottomSheet(View var1, FloatingActionButton var2) {
         if (!this.shouldUpdateVisibility(var1, var2)) {
            return false;
         } else {
            CoordinatorLayout.LayoutParams var3 = (CoordinatorLayout.LayoutParams)var2.getLayoutParams();
            if (var1.getTop() < var2.getHeight() / 2 + var3.topMargin) {
               var2.hide(this.internalAutoHideListener, false);
            } else {
               var2.show(this.internalAutoHideListener, false);
            }

            return true;
         }
      }

      public boolean getInsetDodgeRect(CoordinatorLayout var1, FloatingActionButton var2, Rect var3) {
         Rect var4 = var2.shadowPadding;
         var3.set(var2.getLeft() + var4.left, var2.getTop() + var4.top, var2.getRight() - var4.right, var2.getBottom() - var4.bottom);
         return true;
      }

      public void onAttachedToLayoutParams(CoordinatorLayout.LayoutParams var1) {
         if (var1.dodgeInsetEdges == 0) {
            var1.dodgeInsetEdges = 80;
         }

      }

      public boolean onDependentViewChanged(CoordinatorLayout var1, FloatingActionButton var2, View var3) {
         if (var3 instanceof AppBarLayout) {
            this.updateFabVisibilityForAppBarLayout(var1, (AppBarLayout)var3, var2);
         } else if (isBottomSheet(var3)) {
            this.updateFabVisibilityForBottomSheet(var3, var2);
         }

         return false;
      }

      public boolean onLayoutChild(CoordinatorLayout var1, FloatingActionButton var2, int var3) {
         List var4 = var1.getDependencies(var2);
         int var5 = var4.size();

         for(int var6 = 0; var6 < var5; ++var6) {
            View var7 = (View)var4.get(var6);
            if (var7 instanceof AppBarLayout) {
               if (this.updateFabVisibilityForAppBarLayout(var1, (AppBarLayout)var7, var2)) {
                  break;
               }
            } else if (isBottomSheet(var7) && this.updateFabVisibilityForBottomSheet(var7, var2)) {
               break;
            }
         }

         var1.onLayoutChild(var2, var3);
         this.offsetIfNeeded(var1, var2);
         return true;
      }
   }

   public static class Behavior extends FloatingActionButton.BaseBehavior {
      public Behavior() {
      }

      public Behavior(Context var1, AttributeSet var2) {
         super(var1, var2);
      }
   }

   public abstract static class OnVisibilityChangedListener {
      public void onHidden(FloatingActionButton var1) {
      }

      public void onShown(FloatingActionButton var1) {
      }
   }

   private class ShadowDelegateImpl implements ShadowViewDelegate {
      ShadowDelegateImpl() {
      }

      public float getRadius() {
         return (float)FloatingActionButton.this.getSizeDimension() / 2.0F;
      }

      public boolean isCompatPaddingEnabled() {
         return FloatingActionButton.this.compatPadding;
      }

      public void setBackgroundDrawable(Drawable var1) {
         FloatingActionButton.super.setBackgroundDrawable(var1);
      }

      public void setShadowPadding(int var1, int var2, int var3, int var4) {
         FloatingActionButton.this.shadowPadding.set(var1, var2, var3, var4);
         FloatingActionButton.this.setPadding(var1 + FloatingActionButton.this.imagePadding, var2 + FloatingActionButton.this.imagePadding, var3 + FloatingActionButton.this.imagePadding, var4 + FloatingActionButton.this.imagePadding);
      }
   }
}
