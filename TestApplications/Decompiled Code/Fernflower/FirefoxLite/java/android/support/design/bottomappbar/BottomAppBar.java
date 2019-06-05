package android.support.design.bottomappbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.support.design.animation.AnimationUtils;
import android.support.design.behavior.HideBottomViewOnScrollBehavior;
import android.support.design.shape.MaterialShapeDrawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BottomAppBar extends Toolbar implements CoordinatorLayout.AttachedBehavior {
   private Animator attachAnimator;
   private int fabAlignmentMode;
   AnimatorListenerAdapter fabAnimationListener;
   private boolean fabAttached;
   private final int fabOffsetEndMode;
   private boolean hideOnScroll;
   private final MaterialShapeDrawable materialShapeDrawable;
   private Animator menuAnimator;
   private Animator modeAnimator;
   private final BottomAppBarTopEdgeTreatment topEdgeTreatment;

   private void addFabAnimationListeners(FloatingActionButton var1) {
      this.removeFabAnimationListeners(var1);
      var1.addOnHideAnimationListener(this.fabAnimationListener);
      var1.addOnShowAnimationListener(this.fabAnimationListener);
   }

   private void cancelAnimations() {
      if (this.attachAnimator != null) {
         this.attachAnimator.cancel();
      }

      if (this.menuAnimator != null) {
         this.menuAnimator.cancel();
      }

      if (this.modeAnimator != null) {
         this.modeAnimator.cancel();
      }

   }

   private void createCradleTranslationAnimation(int var1, List var2) {
      if (this.fabAttached) {
         ValueAnimator var3 = ValueAnimator.ofFloat(new float[]{this.topEdgeTreatment.getHorizontalOffset(), (float)this.getFabTranslationX(var1)});
         var3.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator var1) {
               BottomAppBar.this.topEdgeTreatment.setHorizontalOffset((Float)var1.getAnimatedValue());
               BottomAppBar.this.materialShapeDrawable.invalidateSelf();
            }
         });
         var3.setDuration(300L);
         var2.add(var3);
      }
   }

   private void createFabTranslationXAnimation(int var1, List var2) {
      ObjectAnimator var3 = ObjectAnimator.ofFloat(this.findDependentFab(), "translationX", new float[]{(float)this.getFabTranslationX(var1)});
      var3.setDuration(300L);
      var2.add(var3);
   }

   private void createMenuViewTranslationAnimation(final int var1, final boolean var2, List var3) {
      final ActionMenuView var4 = this.getActionMenuView();
      if (var4 != null) {
         ObjectAnimator var5 = ObjectAnimator.ofFloat(var4, "alpha", new float[]{1.0F});
         if ((this.fabAttached || var2 && this.isVisibleFab()) && (this.fabAlignmentMode == 1 || var1 == 1)) {
            ObjectAnimator var6 = ObjectAnimator.ofFloat(var4, "alpha", new float[]{0.0F});
            var6.addListener(new AnimatorListenerAdapter() {
               public boolean cancelled;

               public void onAnimationCancel(Animator var1x) {
                  this.cancelled = true;
               }

               public void onAnimationEnd(Animator var1x) {
                  if (!this.cancelled) {
                     BottomAppBar.this.translateActionMenuView(var4, var1, var2);
                  }

               }
            });
            AnimatorSet var7 = new AnimatorSet();
            var7.setDuration(150L);
            var7.playSequentially(new Animator[]{var6, var5});
            var3.add(var7);
         } else if (var4.getAlpha() < 1.0F) {
            var3.add(var5);
         }

      }
   }

   private FloatingActionButton findDependentFab() {
      if (!(this.getParent() instanceof CoordinatorLayout)) {
         return null;
      } else {
         Iterator var1 = ((CoordinatorLayout)this.getParent()).getDependents(this).iterator();

         View var2;
         do {
            if (!var1.hasNext()) {
               return null;
            }

            var2 = (View)var1.next();
         } while(!(var2 instanceof FloatingActionButton));

         return (FloatingActionButton)var2;
      }
   }

   private ActionMenuView getActionMenuView() {
      for(int var1 = 0; var1 < this.getChildCount(); ++var1) {
         View var2 = this.getChildAt(var1);
         if (var2 instanceof ActionMenuView) {
            return (ActionMenuView)var2;
         }
      }

      return null;
   }

   private float getFabTranslationX() {
      return (float)this.getFabTranslationX(this.fabAlignmentMode);
   }

   private int getFabTranslationX(int var1) {
      int var2 = ViewCompat.getLayoutDirection(this);
      int var3 = 0;
      byte var4 = 1;
      boolean var7;
      if (var2 == 1) {
         var7 = true;
      } else {
         var7 = false;
      }

      if (var1 == 1) {
         int var5 = this.getMeasuredWidth() / 2;
         var3 = this.fabOffsetEndMode;
         byte var6 = var4;
         if (var7) {
            var6 = -1;
         }

         var3 = (var5 - var3) * var6;
      }

      return var3;
   }

   private float getFabTranslationY() {
      return this.getFabTranslationY(this.fabAttached);
   }

   private float getFabTranslationY(boolean var1) {
      FloatingActionButton var2 = this.findDependentFab();
      if (var2 == null) {
         return 0.0F;
      } else {
         Rect var3 = new Rect();
         var2.getContentRect(var3);
         float var4 = (float)var3.height();
         float var5 = var4;
         if (var4 == 0.0F) {
            var5 = (float)var2.getMeasuredHeight();
         }

         var4 = (float)(var2.getHeight() - var3.bottom);
         float var6 = (float)(var2.getHeight() - var3.height());
         float var7 = -this.getCradleVerticalOffset();
         float var8 = var5 / 2.0F;
         var5 = var6 - (float)var2.getPaddingBottom();
         var6 = (float)(-this.getMeasuredHeight());
         if (var1) {
            var5 = var7 + var8 + var4;
         }

         return var6 + var5;
      }
   }

   private boolean isAnimationRunning() {
      boolean var1;
      if ((this.attachAnimator == null || !this.attachAnimator.isRunning()) && (this.menuAnimator == null || !this.menuAnimator.isRunning()) && (this.modeAnimator == null || !this.modeAnimator.isRunning())) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private boolean isVisibleFab() {
      FloatingActionButton var1 = this.findDependentFab();
      boolean var2;
      if (var1 != null && var1.isOrWillBeShown()) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private void maybeAnimateMenuView(int var1, boolean var2) {
      if (ViewCompat.isLaidOut(this)) {
         if (this.menuAnimator != null) {
            this.menuAnimator.cancel();
         }

         ArrayList var3 = new ArrayList();
         if (!this.isVisibleFab()) {
            var1 = 0;
            var2 = false;
         }

         this.createMenuViewTranslationAnimation(var1, var2, var3);
         AnimatorSet var4 = new AnimatorSet();
         var4.playTogether(var3);
         this.menuAnimator = var4;
         this.menuAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1) {
               BottomAppBar.this.menuAnimator = null;
            }
         });
         this.menuAnimator.start();
      }
   }

   private void maybeAnimateModeChange(int var1) {
      if (this.fabAlignmentMode != var1 && ViewCompat.isLaidOut(this)) {
         if (this.modeAnimator != null) {
            this.modeAnimator.cancel();
         }

         ArrayList var2 = new ArrayList();
         this.createCradleTranslationAnimation(var1, var2);
         this.createFabTranslationXAnimation(var1, var2);
         AnimatorSet var3 = new AnimatorSet();
         var3.playTogether(var2);
         this.modeAnimator = var3;
         this.modeAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1) {
               BottomAppBar.this.modeAnimator = null;
            }
         });
         this.modeAnimator.start();
      }
   }

   private void removeFabAnimationListeners(FloatingActionButton var1) {
      var1.removeOnHideAnimationListener(this.fabAnimationListener);
      var1.removeOnShowAnimationListener(this.fabAnimationListener);
   }

   private void setCutoutState() {
      this.topEdgeTreatment.setHorizontalOffset(this.getFabTranslationX());
      FloatingActionButton var1 = this.findDependentFab();
      MaterialShapeDrawable var2 = this.materialShapeDrawable;
      float var3;
      if (this.fabAttached && this.isVisibleFab()) {
         var3 = 1.0F;
      } else {
         var3 = 0.0F;
      }

      var2.setInterpolation(var3);
      if (var1 != null) {
         var1.setTranslationY(this.getFabTranslationY());
         var1.setTranslationX(this.getFabTranslationX());
      }

      ActionMenuView var4 = this.getActionMenuView();
      if (var4 != null) {
         var4.setAlpha(1.0F);
         if (!this.isVisibleFab()) {
            this.translateActionMenuView(var4, 0, false);
         } else {
            this.translateActionMenuView(var4, this.fabAlignmentMode, this.fabAttached);
         }
      }

   }

   private void translateActionMenuView(ActionMenuView var1, int var2, boolean var3) {
      boolean var4;
      if (ViewCompat.getLayoutDirection(this) == 1) {
         var4 = true;
      } else {
         var4 = false;
      }

      int var5 = 0;

      int var6;
      int var9;
      for(var6 = 0; var5 < this.getChildCount(); var6 = var9) {
         View var7 = this.getChildAt(var5);
         boolean var8;
         if (var7.getLayoutParams() instanceof Toolbar.LayoutParams && (((Toolbar.LayoutParams)var7.getLayoutParams()).gravity & 8388615) == 8388611) {
            var8 = true;
         } else {
            var8 = false;
         }

         var9 = var6;
         if (var8) {
            if (var4) {
               var9 = var7.getLeft();
            } else {
               var9 = var7.getRight();
            }

            var9 = Math.max(var6, var9);
         }

         ++var5;
      }

      int var11;
      if (var4) {
         var11 = var1.getRight();
      } else {
         var11 = var1.getLeft();
      }

      float var10;
      if (var2 == 1 && var3) {
         var10 = (float)(var6 - var11);
      } else {
         var10 = 0.0F;
      }

      var1.setTranslationX(var10);
   }

   public ColorStateList getBackgroundTint() {
      return this.materialShapeDrawable.getTintList();
   }

   public CoordinatorLayout.Behavior getBehavior() {
      return new BottomAppBar.Behavior();
   }

   public float getCradleVerticalOffset() {
      return this.topEdgeTreatment.getCradleVerticalOffset();
   }

   public int getFabAlignmentMode() {
      return this.fabAlignmentMode;
   }

   public float getFabCradleMargin() {
      return this.topEdgeTreatment.getFabCradleMargin();
   }

   public float getFabCradleRoundedCornerRadius() {
      return this.topEdgeTreatment.getFabCradleRoundedCornerRadius();
   }

   public boolean getHideOnScroll() {
      return this.hideOnScroll;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      this.cancelAnimations();
      this.setCutoutState();
   }

   protected void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof BottomAppBar.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         BottomAppBar.SavedState var2 = (BottomAppBar.SavedState)var1;
         super.onRestoreInstanceState(var2.getSuperState());
         this.fabAlignmentMode = var2.fabAlignmentMode;
         this.fabAttached = var2.fabAttached;
      }
   }

   protected Parcelable onSaveInstanceState() {
      BottomAppBar.SavedState var1 = new BottomAppBar.SavedState(super.onSaveInstanceState());
      var1.fabAlignmentMode = this.fabAlignmentMode;
      var1.fabAttached = this.fabAttached;
      return var1;
   }

   public void setBackgroundTint(ColorStateList var1) {
      DrawableCompat.setTintList(this.materialShapeDrawable, var1);
   }

   public void setCradleVerticalOffset(float var1) {
      if (var1 != this.getCradleVerticalOffset()) {
         this.topEdgeTreatment.setCradleVerticalOffset(var1);
         this.materialShapeDrawable.invalidateSelf();
      }

   }

   public void setFabAlignmentMode(int var1) {
      this.maybeAnimateModeChange(var1);
      this.maybeAnimateMenuView(var1, this.fabAttached);
      this.fabAlignmentMode = var1;
   }

   public void setFabCradleMargin(float var1) {
      if (var1 != this.getFabCradleMargin()) {
         this.topEdgeTreatment.setFabCradleMargin(var1);
         this.materialShapeDrawable.invalidateSelf();
      }

   }

   public void setFabCradleRoundedCornerRadius(float var1) {
      if (var1 != this.getFabCradleRoundedCornerRadius()) {
         this.topEdgeTreatment.setFabCradleRoundedCornerRadius(var1);
         this.materialShapeDrawable.invalidateSelf();
      }

   }

   void setFabDiameter(int var1) {
      float var2 = (float)var1;
      if (var2 != this.topEdgeTreatment.getFabDiameter()) {
         this.topEdgeTreatment.setFabDiameter(var2);
         this.materialShapeDrawable.invalidateSelf();
      }

   }

   public void setHideOnScroll(boolean var1) {
      this.hideOnScroll = var1;
   }

   public void setSubtitle(CharSequence var1) {
   }

   public void setTitle(CharSequence var1) {
   }

   public static class Behavior extends HideBottomViewOnScrollBehavior {
      private final Rect fabContentRect = new Rect();

      public Behavior() {
      }

      public Behavior(Context var1, AttributeSet var2) {
         super(var1, var2);
      }

      private boolean updateFabPositionAndVisibility(FloatingActionButton var1, BottomAppBar var2) {
         ((CoordinatorLayout.LayoutParams)var1.getLayoutParams()).anchorGravity = 17;
         var2.addFabAnimationListeners(var1);
         return true;
      }

      public boolean onLayoutChild(CoordinatorLayout var1, BottomAppBar var2, int var3) {
         FloatingActionButton var4 = var2.findDependentFab();
         if (var4 != null) {
            this.updateFabPositionAndVisibility(var4, var2);
            var4.getMeasuredContentRect(this.fabContentRect);
            var2.setFabDiameter(this.fabContentRect.height());
         }

         if (!var2.isAnimationRunning()) {
            var2.setCutoutState();
         }

         var1.onLayoutChild(var2, var3);
         return super.onLayoutChild(var1, var2, var3);
      }

      public boolean onStartNestedScroll(CoordinatorLayout var1, BottomAppBar var2, View var3, View var4, int var5, int var6) {
         boolean var7;
         if (var2.getHideOnScroll() && super.onStartNestedScroll(var1, var2, var3, var4, var5, var6)) {
            var7 = true;
         } else {
            var7 = false;
         }

         return var7;
      }

      protected void slideDown(BottomAppBar var1) {
         super.slideDown(var1);
         FloatingActionButton var3 = var1.findDependentFab();
         if (var3 != null) {
            var3.getContentRect(this.fabContentRect);
            float var2 = (float)(var3.getMeasuredHeight() - this.fabContentRect.height());
            var3.clearAnimation();
            var3.animate().translationY((float)(-var3.getPaddingBottom()) + var2).setInterpolator(AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR).setDuration(175L);
         }

      }

      protected void slideUp(BottomAppBar var1) {
         super.slideUp(var1);
         FloatingActionButton var2 = var1.findDependentFab();
         if (var2 != null) {
            var2.clearAnimation();
            var2.animate().translationY(var1.getFabTranslationY()).setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR).setDuration(225L);
         }

      }
   }

   static class SavedState extends AbsSavedState {
      public static final Creator CREATOR = new ClassLoaderCreator() {
         public BottomAppBar.SavedState createFromParcel(Parcel var1) {
            return new BottomAppBar.SavedState(var1, (ClassLoader)null);
         }

         public BottomAppBar.SavedState createFromParcel(Parcel var1, ClassLoader var2) {
            return new BottomAppBar.SavedState(var1, var2);
         }

         public BottomAppBar.SavedState[] newArray(int var1) {
            return new BottomAppBar.SavedState[var1];
         }
      };
      int fabAlignmentMode;
      boolean fabAttached;

      public SavedState(Parcel var1, ClassLoader var2) {
         super(var1, var2);
         this.fabAlignmentMode = var1.readInt();
         boolean var3;
         if (var1.readInt() != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.fabAttached = var3;
      }

      public SavedState(Parcelable var1) {
         super(var1);
      }

      public void writeToParcel(Parcel var1, int var2) {
         super.writeToParcel(var1, var2);
         var1.writeInt(this.fabAlignmentMode);
         var1.writeInt(this.fabAttached);
      }
   }
}
