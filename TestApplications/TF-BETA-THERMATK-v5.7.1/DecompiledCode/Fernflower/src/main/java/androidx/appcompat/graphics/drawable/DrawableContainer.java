package androidx.appcompat.graphics.drawable;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.util.SparseArray;
import androidx.core.graphics.drawable.DrawableCompat;

class DrawableContainer extends Drawable implements Callback {
   private int mAlpha = 255;
   private Runnable mAnimationRunnable;
   private DrawableContainer.BlockInvalidateCallback mBlockInvalidateCallback;
   private int mCurIndex = -1;
   private Drawable mCurrDrawable;
   private DrawableContainer.DrawableContainerState mDrawableContainerState;
   private long mEnterAnimationEnd;
   private long mExitAnimationEnd;
   private boolean mHasAlpha;
   private Rect mHotspotBounds;
   private Drawable mLastDrawable;
   private int mLastIndex = -1;
   private boolean mMutated;

   private void initializeDrawableForDisplay(Drawable var1) {
      if (this.mBlockInvalidateCallback == null) {
         this.mBlockInvalidateCallback = new DrawableContainer.BlockInvalidateCallback();
      }

      DrawableContainer.BlockInvalidateCallback var2 = this.mBlockInvalidateCallback;
      var2.wrap(var1.getCallback());
      var1.setCallback(var2);

      label735: {
         Throwable var10000;
         label741: {
            boolean var10001;
            try {
               if (this.mDrawableContainerState.mEnterFadeDuration <= 0 && this.mHasAlpha) {
                  var1.setAlpha(this.mAlpha);
               }
            } catch (Throwable var74) {
               var10000 = var74;
               var10001 = false;
               break label741;
            }

            label743: {
               try {
                  if (this.mDrawableContainerState.mHasColorFilter) {
                     var1.setColorFilter(this.mDrawableContainerState.mColorFilter);
                     break label743;
                  }
               } catch (Throwable var73) {
                  var10000 = var73;
                  var10001 = false;
                  break label741;
               }

               try {
                  if (this.mDrawableContainerState.mHasTintList) {
                     DrawableCompat.setTintList(var1, this.mDrawableContainerState.mTintList);
                  }
               } catch (Throwable var72) {
                  var10000 = var72;
                  var10001 = false;
                  break label741;
               }

               try {
                  if (this.mDrawableContainerState.mHasTintMode) {
                     DrawableCompat.setTintMode(var1, this.mDrawableContainerState.mTintMode);
                  }
               } catch (Throwable var71) {
                  var10000 = var71;
                  var10001 = false;
                  break label741;
               }
            }

            try {
               var1.setVisible(this.isVisible(), true);
               var1.setDither(this.mDrawableContainerState.mDither);
               var1.setState(this.getState());
               var1.setLevel(this.getLevel());
               var1.setBounds(this.getBounds());
               if (VERSION.SDK_INT >= 23) {
                  var1.setLayoutDirection(this.getLayoutDirection());
               }
            } catch (Throwable var70) {
               var10000 = var70;
               var10001 = false;
               break label741;
            }

            try {
               if (VERSION.SDK_INT >= 19) {
                  var1.setAutoMirrored(this.mDrawableContainerState.mAutoMirrored);
               }
            } catch (Throwable var69) {
               var10000 = var69;
               var10001 = false;
               break label741;
            }

            Rect var75;
            try {
               var75 = this.mHotspotBounds;
               if (VERSION.SDK_INT < 21) {
                  break label735;
               }
            } catch (Throwable var68) {
               var10000 = var68;
               var10001 = false;
               break label741;
            }

            if (var75 == null) {
               break label735;
            }

            label706:
            try {
               var1.setHotspotBounds(var75.left, var75.top, var75.right, var75.bottom);
               break label735;
            } catch (Throwable var67) {
               var10000 = var67;
               var10001 = false;
               break label706;
            }
         }

         Throwable var76 = var10000;
         var1.setCallback(this.mBlockInvalidateCallback.unwrap());
         throw var76;
      }

      var1.setCallback(this.mBlockInvalidateCallback.unwrap());
   }

   @SuppressLint({"WrongConstant"})
   @TargetApi(23)
   private boolean needsMirroring() {
      boolean var1 = this.isAutoMirrored();
      boolean var2 = true;
      if (!var1 || this.getLayoutDirection() != 1) {
         var2 = false;
      }

      return var2;
   }

   static int resolveDensity(Resources var0, int var1) {
      if (var0 != null) {
         var1 = var0.getDisplayMetrics().densityDpi;
      }

      int var2 = var1;
      if (var1 == 0) {
         var2 = 160;
      }

      return var2;
   }

   void animate(boolean var1) {
      boolean var2;
      long var3;
      Drawable var5;
      long var6;
      boolean var8;
      label33: {
         var2 = true;
         this.mHasAlpha = true;
         var3 = SystemClock.uptimeMillis();
         var5 = this.mCurrDrawable;
         if (var5 != null) {
            var6 = this.mEnterAnimationEnd;
            if (var6 != 0L) {
               if (var6 > var3) {
                  var5.setAlpha((255 - (int)((var6 - var3) * 255L) / this.mDrawableContainerState.mEnterFadeDuration) * this.mAlpha / 255);
                  var8 = true;
                  break label33;
               }

               var5.setAlpha(this.mAlpha);
               this.mEnterAnimationEnd = 0L;
            }
         } else {
            this.mEnterAnimationEnd = 0L;
         }

         var8 = false;
      }

      var5 = this.mLastDrawable;
      if (var5 != null) {
         var6 = this.mExitAnimationEnd;
         if (var6 != 0L) {
            if (var6 <= var3) {
               var5.setVisible(false, false);
               this.mLastDrawable = null;
               this.mLastIndex = -1;
               this.mExitAnimationEnd = 0L;
            } else {
               var5.setAlpha((int)((var6 - var3) * 255L) / this.mDrawableContainerState.mExitFadeDuration * this.mAlpha / 255);
               var8 = var2;
            }
         }
      } else {
         this.mExitAnimationEnd = 0L;
      }

      if (var1 && var8) {
         this.scheduleSelf(this.mAnimationRunnable, var3 + 16L);
      }

   }

   public void applyTheme(Theme var1) {
      this.mDrawableContainerState.applyTheme(var1);
   }

   public boolean canApplyTheme() {
      return this.mDrawableContainerState.canApplyTheme();
   }

   DrawableContainer.DrawableContainerState cloneConstantState() {
      throw null;
   }

   public void draw(Canvas var1) {
      Drawable var2 = this.mCurrDrawable;
      if (var2 != null) {
         var2.draw(var1);
      }

      var2 = this.mLastDrawable;
      if (var2 != null) {
         var2.draw(var1);
      }

   }

   public int getAlpha() {
      return this.mAlpha;
   }

   public int getChangingConfigurations() {
      return super.getChangingConfigurations() | this.mDrawableContainerState.getChangingConfigurations();
   }

   public final ConstantState getConstantState() {
      if (this.mDrawableContainerState.canConstantState()) {
         this.mDrawableContainerState.mChangingConfigurations = this.getChangingConfigurations();
         return this.mDrawableContainerState;
      } else {
         return null;
      }
   }

   public Drawable getCurrent() {
      return this.mCurrDrawable;
   }

   int getCurrentIndex() {
      return this.mCurIndex;
   }

   public void getHotspotBounds(Rect var1) {
      Rect var2 = this.mHotspotBounds;
      if (var2 != null) {
         var1.set(var2);
      } else {
         super.getHotspotBounds(var1);
      }

   }

   public int getIntrinsicHeight() {
      if (this.mDrawableContainerState.isConstantSize()) {
         return this.mDrawableContainerState.getConstantHeight();
      } else {
         Drawable var1 = this.mCurrDrawable;
         int var2;
         if (var1 != null) {
            var2 = var1.getIntrinsicHeight();
         } else {
            var2 = -1;
         }

         return var2;
      }
   }

   public int getIntrinsicWidth() {
      if (this.mDrawableContainerState.isConstantSize()) {
         return this.mDrawableContainerState.getConstantWidth();
      } else {
         Drawable var1 = this.mCurrDrawable;
         int var2;
         if (var1 != null) {
            var2 = var1.getIntrinsicWidth();
         } else {
            var2 = -1;
         }

         return var2;
      }
   }

   public int getMinimumHeight() {
      if (this.mDrawableContainerState.isConstantSize()) {
         return this.mDrawableContainerState.getConstantMinimumHeight();
      } else {
         Drawable var1 = this.mCurrDrawable;
         int var2;
         if (var1 != null) {
            var2 = var1.getMinimumHeight();
         } else {
            var2 = 0;
         }

         return var2;
      }
   }

   public int getMinimumWidth() {
      if (this.mDrawableContainerState.isConstantSize()) {
         return this.mDrawableContainerState.getConstantMinimumWidth();
      } else {
         Drawable var1 = this.mCurrDrawable;
         int var2;
         if (var1 != null) {
            var2 = var1.getMinimumWidth();
         } else {
            var2 = 0;
         }

         return var2;
      }
   }

   public int getOpacity() {
      Drawable var1 = this.mCurrDrawable;
      int var2;
      if (var1 != null && var1.isVisible()) {
         var2 = this.mDrawableContainerState.getOpacity();
      } else {
         var2 = -2;
      }

      return var2;
   }

   public void getOutline(Outline var1) {
      Drawable var2 = this.mCurrDrawable;
      if (var2 != null) {
         var2.getOutline(var1);
      }

   }

   public boolean getPadding(Rect var1) {
      Rect var2 = this.mDrawableContainerState.getConstantPadding();
      int var3;
      boolean var6;
      if (var2 != null) {
         var1.set(var2);
         var3 = var2.left;
         int var4 = var2.top;
         int var5 = var2.bottom;
         if ((var2.right | var3 | var4 | var5) != 0) {
            var6 = true;
         } else {
            var6 = false;
         }
      } else {
         Drawable var7 = this.mCurrDrawable;
         if (var7 != null) {
            var6 = var7.getPadding(var1);
         } else {
            var6 = super.getPadding(var1);
         }
      }

      if (this.needsMirroring()) {
         var3 = var1.left;
         var1.left = var1.right;
         var1.right = var3;
      }

      return var6;
   }

   public void invalidateDrawable(Drawable var1) {
      DrawableContainer.DrawableContainerState var2 = this.mDrawableContainerState;
      if (var2 != null) {
         var2.invalidateCache();
      }

      if (var1 == this.mCurrDrawable && this.getCallback() != null) {
         this.getCallback().invalidateDrawable(this);
      }

   }

   public boolean isAutoMirrored() {
      return this.mDrawableContainerState.mAutoMirrored;
   }

   public void jumpToCurrentState() {
      Drawable var1 = this.mLastDrawable;
      boolean var2;
      if (var1 != null) {
         var1.jumpToCurrentState();
         this.mLastDrawable = null;
         this.mLastIndex = -1;
         var2 = true;
      } else {
         var2 = false;
      }

      var1 = this.mCurrDrawable;
      if (var1 != null) {
         var1.jumpToCurrentState();
         if (this.mHasAlpha) {
            this.mCurrDrawable.setAlpha(this.mAlpha);
         }
      }

      if (this.mExitAnimationEnd != 0L) {
         this.mExitAnimationEnd = 0L;
         var2 = true;
      }

      if (this.mEnterAnimationEnd != 0L) {
         this.mEnterAnimationEnd = 0L;
         var2 = true;
      }

      if (var2) {
         this.invalidateSelf();
      }

   }

   public Drawable mutate() {
      if (!this.mMutated && super.mutate() == this) {
         DrawableContainer.DrawableContainerState var1 = this.cloneConstantState();
         var1.mutate();
         this.setConstantState(var1);
         this.mMutated = true;
      }

      return this;
   }

   protected void onBoundsChange(Rect var1) {
      Drawable var2 = this.mLastDrawable;
      if (var2 != null) {
         var2.setBounds(var1);
      }

      var2 = this.mCurrDrawable;
      if (var2 != null) {
         var2.setBounds(var1);
      }

   }

   public boolean onLayoutDirectionChanged(int var1) {
      return this.mDrawableContainerState.setLayoutDirection(var1, this.getCurrentIndex());
   }

   protected boolean onLevelChange(int var1) {
      Drawable var2 = this.mLastDrawable;
      if (var2 != null) {
         return var2.setLevel(var1);
      } else {
         var2 = this.mCurrDrawable;
         return var2 != null ? var2.setLevel(var1) : false;
      }
   }

   protected boolean onStateChange(int[] var1) {
      Drawable var2 = this.mLastDrawable;
      if (var2 != null) {
         return var2.setState(var1);
      } else {
         var2 = this.mCurrDrawable;
         return var2 != null ? var2.setState(var1) : false;
      }
   }

   public void scheduleDrawable(Drawable var1, Runnable var2, long var3) {
      if (var1 == this.mCurrDrawable && this.getCallback() != null) {
         this.getCallback().scheduleDrawable(this, var2, var3);
      }

   }

   boolean selectDrawable(int var1) {
      if (var1 == this.mCurIndex) {
         return false;
      } else {
         long var2 = SystemClock.uptimeMillis();
         Drawable var4;
         if (this.mDrawableContainerState.mExitFadeDuration > 0) {
            var4 = this.mLastDrawable;
            if (var4 != null) {
               var4.setVisible(false, false);
            }

            var4 = this.mCurrDrawable;
            if (var4 != null) {
               this.mLastDrawable = var4;
               this.mLastIndex = this.mCurIndex;
               this.mExitAnimationEnd = (long)this.mDrawableContainerState.mExitFadeDuration + var2;
            } else {
               this.mLastDrawable = null;
               this.mLastIndex = -1;
               this.mExitAnimationEnd = 0L;
            }
         } else {
            var4 = this.mCurrDrawable;
            if (var4 != null) {
               var4.setVisible(false, false);
            }
         }

         label46: {
            if (var1 >= 0) {
               DrawableContainer.DrawableContainerState var5 = this.mDrawableContainerState;
               if (var1 < var5.mNumChildren) {
                  var4 = var5.getChild(var1);
                  this.mCurrDrawable = var4;
                  this.mCurIndex = var1;
                  if (var4 != null) {
                     var1 = this.mDrawableContainerState.mEnterFadeDuration;
                     if (var1 > 0) {
                        this.mEnterAnimationEnd = var2 + (long)var1;
                     }

                     this.initializeDrawableForDisplay(var4);
                  }
                  break label46;
               }
            }

            this.mCurrDrawable = null;
            this.mCurIndex = -1;
         }

         if (this.mEnterAnimationEnd != 0L || this.mExitAnimationEnd != 0L) {
            Runnable var6 = this.mAnimationRunnable;
            if (var6 == null) {
               this.mAnimationRunnable = new Runnable() {
                  public void run() {
                     DrawableContainer.this.animate(true);
                     DrawableContainer.this.invalidateSelf();
                  }
               };
            } else {
               this.unscheduleSelf(var6);
            }

            this.animate(true);
         }

         this.invalidateSelf();
         return true;
      }
   }

   public void setAlpha(int var1) {
      if (!this.mHasAlpha || this.mAlpha != var1) {
         this.mHasAlpha = true;
         this.mAlpha = var1;
         Drawable var2 = this.mCurrDrawable;
         if (var2 != null) {
            if (this.mEnterAnimationEnd == 0L) {
               var2.setAlpha(var1);
            } else {
               this.animate(false);
            }
         }
      }

   }

   public void setAutoMirrored(boolean var1) {
      DrawableContainer.DrawableContainerState var2 = this.mDrawableContainerState;
      if (var2.mAutoMirrored != var1) {
         var2.mAutoMirrored = var1;
         Drawable var3 = this.mCurrDrawable;
         if (var3 != null) {
            DrawableCompat.setAutoMirrored(var3, var2.mAutoMirrored);
         }
      }

   }

   public void setColorFilter(ColorFilter var1) {
      DrawableContainer.DrawableContainerState var2 = this.mDrawableContainerState;
      var2.mHasColorFilter = true;
      if (var2.mColorFilter != var1) {
         var2.mColorFilter = var1;
         Drawable var3 = this.mCurrDrawable;
         if (var3 != null) {
            var3.setColorFilter(var1);
         }
      }

   }

   protected void setConstantState(DrawableContainer.DrawableContainerState var1) {
      this.mDrawableContainerState = var1;
      int var2 = this.mCurIndex;
      if (var2 >= 0) {
         this.mCurrDrawable = var1.getChild(var2);
         Drawable var3 = this.mCurrDrawable;
         if (var3 != null) {
            this.initializeDrawableForDisplay(var3);
         }
      }

      this.mLastIndex = -1;
      this.mLastDrawable = null;
   }

   public void setDither(boolean var1) {
      DrawableContainer.DrawableContainerState var2 = this.mDrawableContainerState;
      if (var2.mDither != var1) {
         var2.mDither = var1;
         Drawable var3 = this.mCurrDrawable;
         if (var3 != null) {
            var3.setDither(var2.mDither);
         }
      }

   }

   public void setHotspot(float var1, float var2) {
      Drawable var3 = this.mCurrDrawable;
      if (var3 != null) {
         DrawableCompat.setHotspot(var3, var1, var2);
      }

   }

   public void setHotspotBounds(int var1, int var2, int var3, int var4) {
      Rect var5 = this.mHotspotBounds;
      if (var5 == null) {
         this.mHotspotBounds = new Rect(var1, var2, var3, var4);
      } else {
         var5.set(var1, var2, var3, var4);
      }

      Drawable var6 = this.mCurrDrawable;
      if (var6 != null) {
         DrawableCompat.setHotspotBounds(var6, var1, var2, var3, var4);
      }

   }

   public void setTintList(ColorStateList var1) {
      DrawableContainer.DrawableContainerState var2 = this.mDrawableContainerState;
      var2.mHasTintList = true;
      if (var2.mTintList != var1) {
         var2.mTintList = var1;
         DrawableCompat.setTintList(this.mCurrDrawable, var1);
      }

   }

   public void setTintMode(Mode var1) {
      DrawableContainer.DrawableContainerState var2 = this.mDrawableContainerState;
      var2.mHasTintMode = true;
      if (var2.mTintMode != var1) {
         var2.mTintMode = var1;
         DrawableCompat.setTintMode(this.mCurrDrawable, var1);
      }

   }

   public boolean setVisible(boolean var1, boolean var2) {
      boolean var3 = super.setVisible(var1, var2);
      Drawable var4 = this.mLastDrawable;
      if (var4 != null) {
         var4.setVisible(var1, var2);
      }

      var4 = this.mCurrDrawable;
      if (var4 != null) {
         var4.setVisible(var1, var2);
      }

      return var3;
   }

   public void unscheduleDrawable(Drawable var1, Runnable var2) {
      if (var1 == this.mCurrDrawable && this.getCallback() != null) {
         this.getCallback().unscheduleDrawable(this, var2);
      }

   }

   final void updateDensity(Resources var1) {
      this.mDrawableContainerState.updateDensity(var1);
   }

   static class BlockInvalidateCallback implements Callback {
      private Callback mCallback;

      public void invalidateDrawable(Drawable var1) {
      }

      public void scheduleDrawable(Drawable var1, Runnable var2, long var3) {
         Callback var5 = this.mCallback;
         if (var5 != null) {
            var5.scheduleDrawable(var1, var2, var3);
         }

      }

      public void unscheduleDrawable(Drawable var1, Runnable var2) {
         Callback var3 = this.mCallback;
         if (var3 != null) {
            var3.unscheduleDrawable(var1, var2);
         }

      }

      public Callback unwrap() {
         Callback var1 = this.mCallback;
         this.mCallback = null;
         return var1;
      }

      public DrawableContainer.BlockInvalidateCallback wrap(Callback var1) {
         this.mCallback = var1;
         return this;
      }
   }

   abstract static class DrawableContainerState extends ConstantState {
      boolean mAutoMirrored;
      boolean mCanConstantState;
      int mChangingConfigurations;
      boolean mCheckedConstantSize;
      boolean mCheckedConstantState;
      boolean mCheckedOpacity;
      boolean mCheckedPadding;
      boolean mCheckedStateful;
      int mChildrenChangingConfigurations;
      ColorFilter mColorFilter;
      int mConstantHeight;
      int mConstantMinimumHeight;
      int mConstantMinimumWidth;
      Rect mConstantPadding;
      boolean mConstantSize;
      int mConstantWidth;
      int mDensity = 160;
      boolean mDither;
      SparseArray mDrawableFutures;
      Drawable[] mDrawables;
      int mEnterFadeDuration;
      int mExitFadeDuration;
      boolean mHasColorFilter;
      boolean mHasTintList;
      boolean mHasTintMode;
      int mLayoutDirection;
      boolean mMutated;
      int mNumChildren;
      int mOpacity;
      final DrawableContainer mOwner;
      Resources mSourceRes;
      boolean mStateful;
      ColorStateList mTintList;
      Mode mTintMode;
      boolean mVariablePadding;

      DrawableContainerState(DrawableContainer.DrawableContainerState var1, DrawableContainer var2, Resources var3) {
         byte var4 = 0;
         this.mVariablePadding = false;
         this.mConstantSize = false;
         this.mDither = true;
         this.mEnterFadeDuration = 0;
         this.mExitFadeDuration = 0;
         this.mOwner = var2;
         Resources var9;
         if (var3 != null) {
            var9 = var3;
         } else if (var1 != null) {
            var9 = var1.mSourceRes;
         } else {
            var9 = null;
         }

         this.mSourceRes = var9;
         int var5;
         if (var1 != null) {
            var5 = var1.mDensity;
         } else {
            var5 = 0;
         }

         this.mDensity = DrawableContainer.resolveDensity(var3, var5);
         if (var1 != null) {
            this.mChangingConfigurations = var1.mChangingConfigurations;
            this.mChildrenChangingConfigurations = var1.mChildrenChangingConfigurations;
            this.mCheckedConstantState = true;
            this.mCanConstantState = true;
            this.mVariablePadding = var1.mVariablePadding;
            this.mConstantSize = var1.mConstantSize;
            this.mDither = var1.mDither;
            this.mMutated = var1.mMutated;
            this.mLayoutDirection = var1.mLayoutDirection;
            this.mEnterFadeDuration = var1.mEnterFadeDuration;
            this.mExitFadeDuration = var1.mExitFadeDuration;
            this.mAutoMirrored = var1.mAutoMirrored;
            this.mColorFilter = var1.mColorFilter;
            this.mHasColorFilter = var1.mHasColorFilter;
            this.mTintList = var1.mTintList;
            this.mTintMode = var1.mTintMode;
            this.mHasTintList = var1.mHasTintList;
            this.mHasTintMode = var1.mHasTintMode;
            if (var1.mDensity == this.mDensity) {
               if (var1.mCheckedPadding) {
                  this.mConstantPadding = new Rect(var1.mConstantPadding);
                  this.mCheckedPadding = true;
               }

               if (var1.mCheckedConstantSize) {
                  this.mConstantWidth = var1.mConstantWidth;
                  this.mConstantHeight = var1.mConstantHeight;
                  this.mConstantMinimumWidth = var1.mConstantMinimumWidth;
                  this.mConstantMinimumHeight = var1.mConstantMinimumHeight;
                  this.mCheckedConstantSize = true;
               }
            }

            if (var1.mCheckedOpacity) {
               this.mOpacity = var1.mOpacity;
               this.mCheckedOpacity = true;
            }

            if (var1.mCheckedStateful) {
               this.mStateful = var1.mStateful;
               this.mCheckedStateful = true;
            }

            Drawable[] var10 = var1.mDrawables;
            this.mDrawables = new Drawable[var10.length];
            this.mNumChildren = var1.mNumChildren;
            SparseArray var7 = var1.mDrawableFutures;
            if (var7 != null) {
               this.mDrawableFutures = var7.clone();
            } else {
               this.mDrawableFutures = new SparseArray(this.mNumChildren);
            }

            int var6 = this.mNumChildren;

            for(var5 = var4; var5 < var6; ++var5) {
               if (var10[var5] != null) {
                  ConstantState var8 = var10[var5].getConstantState();
                  if (var8 != null) {
                     this.mDrawableFutures.put(var5, var8);
                  } else {
                     this.mDrawables[var5] = var10[var5];
                  }
               }
            }
         } else {
            this.mDrawables = new Drawable[10];
            this.mNumChildren = 0;
         }

      }

      private void createAllFutures() {
         SparseArray var1 = this.mDrawableFutures;
         if (var1 != null) {
            int var2 = var1.size();

            for(int var3 = 0; var3 < var2; ++var3) {
               int var4 = this.mDrawableFutures.keyAt(var3);
               ConstantState var5 = (ConstantState)this.mDrawableFutures.valueAt(var3);
               this.mDrawables[var4] = this.prepareDrawable(var5.newDrawable(this.mSourceRes));
            }

            this.mDrawableFutures = null;
         }

      }

      private Drawable prepareDrawable(Drawable var1) {
         if (VERSION.SDK_INT >= 23) {
            var1.setLayoutDirection(this.mLayoutDirection);
         }

         var1 = var1.mutate();
         var1.setCallback(this.mOwner);
         return var1;
      }

      public final int addChild(Drawable var1) {
         int var2 = this.mNumChildren;
         if (var2 >= this.mDrawables.length) {
            this.growArray(var2, var2 + 10);
         }

         var1.mutate();
         var1.setVisible(false, true);
         var1.setCallback(this.mOwner);
         this.mDrawables[var2] = var1;
         ++this.mNumChildren;
         int var3 = this.mChildrenChangingConfigurations;
         this.mChildrenChangingConfigurations = var1.getChangingConfigurations() | var3;
         this.invalidateCache();
         this.mConstantPadding = null;
         this.mCheckedPadding = false;
         this.mCheckedConstantSize = false;
         this.mCheckedConstantState = false;
         return var2;
      }

      final void applyTheme(Theme var1) {
         if (var1 != null) {
            this.createAllFutures();
            int var2 = this.mNumChildren;
            Drawable[] var3 = this.mDrawables;

            for(int var4 = 0; var4 < var2; ++var4) {
               if (var3[var4] != null && var3[var4].canApplyTheme()) {
                  var3[var4].applyTheme(var1);
                  this.mChildrenChangingConfigurations |= var3[var4].getChangingConfigurations();
               }
            }

            this.updateDensity(var1.getResources());
         }

      }

      public boolean canApplyTheme() {
         int var1 = this.mNumChildren;
         Drawable[] var2 = this.mDrawables;

         for(int var3 = 0; var3 < var1; ++var3) {
            Drawable var4 = var2[var3];
            if (var4 != null) {
               if (var4.canApplyTheme()) {
                  return true;
               }
            } else {
               ConstantState var5 = (ConstantState)this.mDrawableFutures.get(var3);
               if (var5 != null && var5.canApplyTheme()) {
                  return true;
               }
            }
         }

         return false;
      }

      public boolean canConstantState() {
         synchronized(this){}

         Throwable var10000;
         label271: {
            boolean var10001;
            try {
               if (this.mCheckedConstantState) {
                  boolean var1 = this.mCanConstantState;
                  return var1;
               }
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break label271;
            }

            int var2;
            Drawable[] var3;
            try {
               this.createAllFutures();
               this.mCheckedConstantState = true;
               var2 = this.mNumChildren;
               var3 = this.mDrawables;
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label271;
            }

            int var4 = 0;

            while(true) {
               if (var4 >= var2) {
                  try {
                     this.mCanConstantState = true;
                  } catch (Throwable var21) {
                     var10000 = var21;
                     var10001 = false;
                     break;
                  }

                  return true;
               }

               try {
                  if (var3[var4].getConstantState() == null) {
                     this.mCanConstantState = false;
                     return false;
                  }
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break;
               }

               ++var4;
            }
         }

         Throwable var25 = var10000;
         throw var25;
      }

      protected void computeConstantSize() {
         this.mCheckedConstantSize = true;
         this.createAllFutures();
         int var1 = this.mNumChildren;
         Drawable[] var2 = this.mDrawables;
         this.mConstantHeight = -1;
         this.mConstantWidth = -1;
         int var3 = 0;
         this.mConstantMinimumHeight = 0;

         for(this.mConstantMinimumWidth = 0; var3 < var1; ++var3) {
            Drawable var4 = var2[var3];
            int var5 = var4.getIntrinsicWidth();
            if (var5 > this.mConstantWidth) {
               this.mConstantWidth = var5;
            }

            var5 = var4.getIntrinsicHeight();
            if (var5 > this.mConstantHeight) {
               this.mConstantHeight = var5;
            }

            var5 = var4.getMinimumWidth();
            if (var5 > this.mConstantMinimumWidth) {
               this.mConstantMinimumWidth = var5;
            }

            var5 = var4.getMinimumHeight();
            if (var5 > this.mConstantMinimumHeight) {
               this.mConstantMinimumHeight = var5;
            }
         }

      }

      final int getCapacity() {
         return this.mDrawables.length;
      }

      public int getChangingConfigurations() {
         return this.mChangingConfigurations | this.mChildrenChangingConfigurations;
      }

      public final Drawable getChild(int var1) {
         Drawable var2 = this.mDrawables[var1];
         if (var2 != null) {
            return var2;
         } else {
            SparseArray var4 = this.mDrawableFutures;
            if (var4 != null) {
               int var3 = var4.indexOfKey(var1);
               if (var3 >= 0) {
                  var2 = this.prepareDrawable(((ConstantState)this.mDrawableFutures.valueAt(var3)).newDrawable(this.mSourceRes));
                  this.mDrawables[var1] = var2;
                  this.mDrawableFutures.removeAt(var3);
                  if (this.mDrawableFutures.size() == 0) {
                     this.mDrawableFutures = null;
                  }

                  return var2;
               }
            }

            return null;
         }
      }

      public final int getChildCount() {
         return this.mNumChildren;
      }

      public final int getConstantHeight() {
         if (!this.mCheckedConstantSize) {
            this.computeConstantSize();
         }

         return this.mConstantHeight;
      }

      public final int getConstantMinimumHeight() {
         if (!this.mCheckedConstantSize) {
            this.computeConstantSize();
         }

         return this.mConstantMinimumHeight;
      }

      public final int getConstantMinimumWidth() {
         if (!this.mCheckedConstantSize) {
            this.computeConstantSize();
         }

         return this.mConstantMinimumWidth;
      }

      public final Rect getConstantPadding() {
         if (this.mVariablePadding) {
            return null;
         } else if (this.mConstantPadding == null && !this.mCheckedPadding) {
            this.createAllFutures();
            Rect var1 = new Rect();
            int var2 = this.mNumChildren;
            Drawable[] var3 = this.mDrawables;
            Rect var4 = null;

            Rect var6;
            for(int var5 = 0; var5 < var2; var4 = var6) {
               var6 = var4;
               if (var3[var5].getPadding(var1)) {
                  Rect var7 = var4;
                  if (var4 == null) {
                     var7 = new Rect(0, 0, 0, 0);
                  }

                  int var8 = var1.left;
                  if (var8 > var7.left) {
                     var7.left = var8;
                  }

                  var8 = var1.top;
                  if (var8 > var7.top) {
                     var7.top = var8;
                  }

                  var8 = var1.right;
                  if (var8 > var7.right) {
                     var7.right = var8;
                  }

                  var8 = var1.bottom;
                  var6 = var7;
                  if (var8 > var7.bottom) {
                     var7.bottom = var8;
                     var6 = var7;
                  }
               }

               ++var5;
            }

            this.mCheckedPadding = true;
            this.mConstantPadding = var4;
            return var4;
         } else {
            return this.mConstantPadding;
         }
      }

      public final int getConstantWidth() {
         if (!this.mCheckedConstantSize) {
            this.computeConstantSize();
         }

         return this.mConstantWidth;
      }

      public final int getOpacity() {
         if (this.mCheckedOpacity) {
            return this.mOpacity;
         } else {
            this.createAllFutures();
            int var1 = this.mNumChildren;
            Drawable[] var2 = this.mDrawables;
            int var3;
            if (var1 > 0) {
               var3 = var2[0].getOpacity();
            } else {
               var3 = -2;
            }

            for(int var4 = 1; var4 < var1; ++var4) {
               var3 = Drawable.resolveOpacity(var3, var2[var4].getOpacity());
            }

            this.mOpacity = var3;
            this.mCheckedOpacity = true;
            return var3;
         }
      }

      public void growArray(int var1, int var2) {
         Drawable[] var3 = new Drawable[var2];
         System.arraycopy(this.mDrawables, 0, var3, 0, var1);
         this.mDrawables = var3;
      }

      void invalidateCache() {
         this.mCheckedOpacity = false;
         this.mCheckedStateful = false;
      }

      public final boolean isConstantSize() {
         return this.mConstantSize;
      }

      abstract void mutate();

      public final void setConstantSize(boolean var1) {
         this.mConstantSize = var1;
      }

      public final void setEnterFadeDuration(int var1) {
         this.mEnterFadeDuration = var1;
      }

      public final void setExitFadeDuration(int var1) {
         this.mExitFadeDuration = var1;
      }

      final boolean setLayoutDirection(int var1, int var2) {
         int var3 = this.mNumChildren;
         Drawable[] var4 = this.mDrawables;
         int var5 = 0;

         boolean var6;
         boolean var7;
         for(var6 = false; var5 < var3; var6 = var7) {
            var7 = var6;
            if (var4[var5] != null) {
               boolean var8;
               if (VERSION.SDK_INT >= 23) {
                  var8 = var4[var5].setLayoutDirection(var1);
               } else {
                  var8 = false;
               }

               var7 = var6;
               if (var5 == var2) {
                  var7 = var8;
               }
            }

            ++var5;
         }

         this.mLayoutDirection = var1;
         return var6;
      }

      public final void setVariablePadding(boolean var1) {
         this.mVariablePadding = var1;
      }

      final void updateDensity(Resources var1) {
         if (var1 != null) {
            this.mSourceRes = var1;
            int var2 = DrawableContainer.resolveDensity(var1, this.mDensity);
            int var3 = this.mDensity;
            this.mDensity = var2;
            if (var3 != var2) {
               this.mCheckedConstantSize = false;
               this.mCheckedPadding = false;
            }
         }

      }
   }
}