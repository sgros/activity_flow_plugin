package android.support.v4.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.graphics.drawable.Drawable.ConstantState;

class WrappedDrawableApi14 extends Drawable implements Callback, TintAwareDrawable, WrappedDrawable {
   static final Mode DEFAULT_TINT_MODE;
   private boolean mColorFilterSet;
   private int mCurrentColor;
   private Mode mCurrentMode;
   Drawable mDrawable;
   private boolean mMutated;
   WrappedDrawableApi14.DrawableWrapperState mState;

   static {
      DEFAULT_TINT_MODE = Mode.SRC_IN;
   }

   WrappedDrawableApi14(Drawable var1) {
      this.mState = this.mutateConstantState();
      this.setWrappedDrawable(var1);
   }

   WrappedDrawableApi14(WrappedDrawableApi14.DrawableWrapperState var1, Resources var2) {
      this.mState = var1;
      this.updateLocalState(var2);
   }

   private void updateLocalState(Resources var1) {
      if (this.mState != null && this.mState.mDrawableState != null) {
         this.setWrappedDrawable(this.mState.mDrawableState.newDrawable(var1));
      }

   }

   private boolean updateTint(int[] var1) {
      if (!this.isCompatTintEnabled()) {
         return false;
      } else {
         ColorStateList var2 = this.mState.mTint;
         Mode var3 = this.mState.mTintMode;
         if (var2 != null && var3 != null) {
            int var4 = var2.getColorForState(var1, var2.getDefaultColor());
            if (!this.mColorFilterSet || var4 != this.mCurrentColor || var3 != this.mCurrentMode) {
               this.setColorFilter(var4, var3);
               this.mCurrentColor = var4;
               this.mCurrentMode = var3;
               this.mColorFilterSet = true;
               return true;
            }
         } else {
            this.mColorFilterSet = false;
            this.clearColorFilter();
         }

         return false;
      }
   }

   public void draw(Canvas var1) {
      this.mDrawable.draw(var1);
   }

   public int getChangingConfigurations() {
      int var1 = super.getChangingConfigurations();
      int var2;
      if (this.mState != null) {
         var2 = this.mState.getChangingConfigurations();
      } else {
         var2 = 0;
      }

      return var1 | var2 | this.mDrawable.getChangingConfigurations();
   }

   public ConstantState getConstantState() {
      if (this.mState != null && this.mState.canConstantState()) {
         this.mState.mChangingConfigurations = this.getChangingConfigurations();
         return this.mState;
      } else {
         return null;
      }
   }

   public Drawable getCurrent() {
      return this.mDrawable.getCurrent();
   }

   public int getIntrinsicHeight() {
      return this.mDrawable.getIntrinsicHeight();
   }

   public int getIntrinsicWidth() {
      return this.mDrawable.getIntrinsicWidth();
   }

   public int getMinimumHeight() {
      return this.mDrawable.getMinimumHeight();
   }

   public int getMinimumWidth() {
      return this.mDrawable.getMinimumWidth();
   }

   public int getOpacity() {
      return this.mDrawable.getOpacity();
   }

   public boolean getPadding(Rect var1) {
      return this.mDrawable.getPadding(var1);
   }

   public int[] getState() {
      return this.mDrawable.getState();
   }

   public Region getTransparentRegion() {
      return this.mDrawable.getTransparentRegion();
   }

   public final Drawable getWrappedDrawable() {
      return this.mDrawable;
   }

   public void invalidateDrawable(Drawable var1) {
      this.invalidateSelf();
   }

   public boolean isAutoMirrored() {
      return this.mDrawable.isAutoMirrored();
   }

   protected boolean isCompatTintEnabled() {
      return true;
   }

   public boolean isStateful() {
      ColorStateList var1;
      if (this.isCompatTintEnabled() && this.mState != null) {
         var1 = this.mState.mTint;
      } else {
         var1 = null;
      }

      boolean var2;
      if ((var1 == null || !var1.isStateful()) && !this.mDrawable.isStateful()) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public void jumpToCurrentState() {
      this.mDrawable.jumpToCurrentState();
   }

   public Drawable mutate() {
      if (!this.mMutated && super.mutate() == this) {
         this.mState = this.mutateConstantState();
         if (this.mDrawable != null) {
            this.mDrawable.mutate();
         }

         if (this.mState != null) {
            WrappedDrawableApi14.DrawableWrapperState var1 = this.mState;
            ConstantState var2;
            if (this.mDrawable != null) {
               var2 = this.mDrawable.getConstantState();
            } else {
               var2 = null;
            }

            var1.mDrawableState = var2;
         }

         this.mMutated = true;
      }

      return this;
   }

   WrappedDrawableApi14.DrawableWrapperState mutateConstantState() {
      return new WrappedDrawableApi14.DrawableWrapperStateBase(this.mState, (Resources)null);
   }

   protected void onBoundsChange(Rect var1) {
      if (this.mDrawable != null) {
         this.mDrawable.setBounds(var1);
      }

   }

   protected boolean onLevelChange(int var1) {
      return this.mDrawable.setLevel(var1);
   }

   public void scheduleDrawable(Drawable var1, Runnable var2, long var3) {
      this.scheduleSelf(var2, var3);
   }

   public void setAlpha(int var1) {
      this.mDrawable.setAlpha(var1);
   }

   public void setAutoMirrored(boolean var1) {
      this.mDrawable.setAutoMirrored(var1);
   }

   public void setChangingConfigurations(int var1) {
      this.mDrawable.setChangingConfigurations(var1);
   }

   public void setColorFilter(ColorFilter var1) {
      this.mDrawable.setColorFilter(var1);
   }

   public void setDither(boolean var1) {
      this.mDrawable.setDither(var1);
   }

   public void setFilterBitmap(boolean var1) {
      this.mDrawable.setFilterBitmap(var1);
   }

   public boolean setState(int[] var1) {
      boolean var2 = this.mDrawable.setState(var1);
      if (!this.updateTint(var1) && !var2) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public void setTint(int var1) {
      this.setTintList(ColorStateList.valueOf(var1));
   }

   public void setTintList(ColorStateList var1) {
      this.mState.mTint = var1;
      this.updateTint(this.getState());
   }

   public void setTintMode(Mode var1) {
      this.mState.mTintMode = var1;
      this.updateTint(this.getState());
   }

   public boolean setVisible(boolean var1, boolean var2) {
      if (!super.setVisible(var1, var2) && !this.mDrawable.setVisible(var1, var2)) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public final void setWrappedDrawable(Drawable var1) {
      if (this.mDrawable != null) {
         this.mDrawable.setCallback((Callback)null);
      }

      this.mDrawable = var1;
      if (var1 != null) {
         var1.setCallback(this);
         this.setVisible(var1.isVisible(), true);
         this.setState(var1.getState());
         this.setLevel(var1.getLevel());
         this.setBounds(var1.getBounds());
         if (this.mState != null) {
            this.mState.mDrawableState = var1.getConstantState();
         }
      }

      this.invalidateSelf();
   }

   public void unscheduleDrawable(Drawable var1, Runnable var2) {
      this.unscheduleSelf(var2);
   }

   protected abstract static class DrawableWrapperState extends ConstantState {
      int mChangingConfigurations;
      ConstantState mDrawableState;
      ColorStateList mTint = null;
      Mode mTintMode;

      DrawableWrapperState(WrappedDrawableApi14.DrawableWrapperState var1, Resources var2) {
         this.mTintMode = WrappedDrawableApi14.DEFAULT_TINT_MODE;
         if (var1 != null) {
            this.mChangingConfigurations = var1.mChangingConfigurations;
            this.mDrawableState = var1.mDrawableState;
            this.mTint = var1.mTint;
            this.mTintMode = var1.mTintMode;
         }

      }

      boolean canConstantState() {
         boolean var1;
         if (this.mDrawableState != null) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public int getChangingConfigurations() {
         int var1 = this.mChangingConfigurations;
         int var2;
         if (this.mDrawableState != null) {
            var2 = this.mDrawableState.getChangingConfigurations();
         } else {
            var2 = 0;
         }

         return var1 | var2;
      }

      public Drawable newDrawable() {
         return this.newDrawable((Resources)null);
      }

      public abstract Drawable newDrawable(Resources var1);
   }

   private static class DrawableWrapperStateBase extends WrappedDrawableApi14.DrawableWrapperState {
      DrawableWrapperStateBase(WrappedDrawableApi14.DrawableWrapperState var1, Resources var2) {
         super(var1, var2);
      }

      public Drawable newDrawable(Resources var1) {
         return new WrappedDrawableApi14(this, var1);
      }
   }
}
