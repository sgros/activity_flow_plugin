package androidx.core.widget;

import android.content.res.Resources;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import androidx.core.view.ViewCompat;

public abstract class AutoScrollHelper implements OnTouchListener {
   private static final int DEFAULT_ACTIVATION_DELAY = ViewConfiguration.getTapTimeout();
   private int mActivationDelay;
   private boolean mAlreadyDelayed;
   boolean mAnimating;
   private final Interpolator mEdgeInterpolator = new AccelerateInterpolator();
   private int mEdgeType;
   private boolean mEnabled;
   private boolean mExclusive;
   private float[] mMaximumEdges = new float[]{Float.MAX_VALUE, Float.MAX_VALUE};
   private float[] mMaximumVelocity = new float[]{Float.MAX_VALUE, Float.MAX_VALUE};
   private float[] mMinimumVelocity = new float[]{0.0F, 0.0F};
   boolean mNeedsCancel;
   boolean mNeedsReset;
   private float[] mRelativeEdges = new float[]{0.0F, 0.0F};
   private float[] mRelativeVelocity = new float[]{0.0F, 0.0F};
   private Runnable mRunnable;
   final AutoScrollHelper.ClampedScroller mScroller = new AutoScrollHelper.ClampedScroller();
   final View mTarget;

   public AutoScrollHelper(View var1) {
      this.mTarget = var1;
      float var2 = Resources.getSystem().getDisplayMetrics().density;
      int var3 = (int)(1575.0F * var2 + 0.5F);
      int var4 = (int)(var2 * 315.0F + 0.5F);
      var2 = (float)var3;
      this.setMaximumVelocity(var2, var2);
      var2 = (float)var4;
      this.setMinimumVelocity(var2, var2);
      this.setEdgeType(1);
      this.setMaximumEdges(Float.MAX_VALUE, Float.MAX_VALUE);
      this.setRelativeEdges(0.2F, 0.2F);
      this.setRelativeVelocity(1.0F, 1.0F);
      this.setActivationDelay(DEFAULT_ACTIVATION_DELAY);
      this.setRampUpDuration(500);
      this.setRampDownDuration(500);
   }

   private float computeTargetVelocity(int var1, float var2, float var3, float var4) {
      var3 = this.getEdgeValue(this.mRelativeEdges[var1], var3, this.mMaximumEdges[var1], var2);
      if (var3 == 0.0F) {
         return 0.0F;
      } else {
         float var5 = this.mRelativeVelocity[var1];
         float var6 = this.mMinimumVelocity[var1];
         var2 = this.mMaximumVelocity[var1];
         var4 = var5 * var4;
         return var3 > 0.0F ? constrain(var3 * var4, var6, var2) : -constrain(-var3 * var4, var6, var2);
      }
   }

   static float constrain(float var0, float var1, float var2) {
      if (var0 > var2) {
         return var2;
      } else {
         return var0 < var1 ? var1 : var0;
      }
   }

   static int constrain(int var0, int var1, int var2) {
      if (var0 > var2) {
         return var2;
      } else {
         return var0 < var1 ? var1 : var0;
      }
   }

   private float constrainEdgeValue(float var1, float var2) {
      if (var2 == 0.0F) {
         return 0.0F;
      } else {
         int var3 = this.mEdgeType;
         if (var3 != 0 && var3 != 1) {
            if (var3 == 2 && var1 < 0.0F) {
               return var1 / -var2;
            }
         } else if (var1 < var2) {
            if (var1 >= 0.0F) {
               return 1.0F - var1 / var2;
            }

            if (this.mAnimating && this.mEdgeType == 1) {
               return 1.0F;
            }
         }

         return 0.0F;
      }
   }

   private float getEdgeValue(float var1, float var2, float var3, float var4) {
      var3 = constrain(var1 * var2, 0.0F, var3);
      var1 = this.constrainEdgeValue(var4, var3);
      var1 = this.constrainEdgeValue(var2 - var4, var3) - var1;
      if (var1 < 0.0F) {
         var1 = -this.mEdgeInterpolator.getInterpolation(-var1);
      } else {
         if (var1 <= 0.0F) {
            return 0.0F;
         }

         var1 = this.mEdgeInterpolator.getInterpolation(var1);
      }

      return constrain(var1, -1.0F, 1.0F);
   }

   private void requestStop() {
      if (this.mNeedsReset) {
         this.mAnimating = false;
      } else {
         this.mScroller.requestStop();
      }

   }

   private void startAnimating() {
      if (this.mRunnable == null) {
         this.mRunnable = new AutoScrollHelper.ScrollAnimationRunnable();
      }

      label15: {
         this.mAnimating = true;
         this.mNeedsReset = true;
         if (!this.mAlreadyDelayed) {
            int var1 = this.mActivationDelay;
            if (var1 > 0) {
               ViewCompat.postOnAnimationDelayed(this.mTarget, this.mRunnable, (long)var1);
               break label15;
            }
         }

         this.mRunnable.run();
      }

      this.mAlreadyDelayed = true;
   }

   public abstract boolean canTargetScrollHorizontally(int var1);

   public abstract boolean canTargetScrollVertically(int var1);

   void cancelTargetTouch() {
      long var1 = SystemClock.uptimeMillis();
      MotionEvent var3 = MotionEvent.obtain(var1, var1, 3, 0.0F, 0.0F, 0);
      this.mTarget.onTouchEvent(var3);
      var3.recycle();
   }

   public boolean onTouch(View var1, MotionEvent var2) {
      boolean var3 = this.mEnabled;
      boolean var4 = false;
      if (!var3) {
         return false;
      } else {
         label34: {
            label33: {
               int var5 = var2.getActionMasked();
               if (var5 != 0) {
                  if (var5 == 1) {
                     break label33;
                  }

                  if (var5 != 2) {
                     if (var5 != 3) {
                        break label34;
                     }
                     break label33;
                  }
               } else {
                  this.mNeedsCancel = true;
                  this.mAlreadyDelayed = false;
               }

               float var6 = this.computeTargetVelocity(0, var2.getX(), (float)var1.getWidth(), (float)this.mTarget.getWidth());
               float var7 = this.computeTargetVelocity(1, var2.getY(), (float)var1.getHeight(), (float)this.mTarget.getHeight());
               this.mScroller.setTargetVelocity(var6, var7);
               if (!this.mAnimating && this.shouldAnimate()) {
                  this.startAnimating();
               }
               break label34;
            }

            this.requestStop();
         }

         var3 = var4;
         if (this.mExclusive) {
            var3 = var4;
            if (this.mAnimating) {
               var3 = true;
            }
         }

         return var3;
      }
   }

   public abstract void scrollTargetBy(int var1, int var2);

   public AutoScrollHelper setActivationDelay(int var1) {
      this.mActivationDelay = var1;
      return this;
   }

   public AutoScrollHelper setEdgeType(int var1) {
      this.mEdgeType = var1;
      return this;
   }

   public AutoScrollHelper setEnabled(boolean var1) {
      if (this.mEnabled && !var1) {
         this.requestStop();
      }

      this.mEnabled = var1;
      return this;
   }

   public AutoScrollHelper setMaximumEdges(float var1, float var2) {
      float[] var3 = this.mMaximumEdges;
      var3[0] = var1;
      var3[1] = var2;
      return this;
   }

   public AutoScrollHelper setMaximumVelocity(float var1, float var2) {
      float[] var3 = this.mMaximumVelocity;
      var3[0] = var1 / 1000.0F;
      var3[1] = var2 / 1000.0F;
      return this;
   }

   public AutoScrollHelper setMinimumVelocity(float var1, float var2) {
      float[] var3 = this.mMinimumVelocity;
      var3[0] = var1 / 1000.0F;
      var3[1] = var2 / 1000.0F;
      return this;
   }

   public AutoScrollHelper setRampDownDuration(int var1) {
      this.mScroller.setRampDownDuration(var1);
      return this;
   }

   public AutoScrollHelper setRampUpDuration(int var1) {
      this.mScroller.setRampUpDuration(var1);
      return this;
   }

   public AutoScrollHelper setRelativeEdges(float var1, float var2) {
      float[] var3 = this.mRelativeEdges;
      var3[0] = var1;
      var3[1] = var2;
      return this;
   }

   public AutoScrollHelper setRelativeVelocity(float var1, float var2) {
      float[] var3 = this.mRelativeVelocity;
      var3[0] = var1 / 1000.0F;
      var3[1] = var2 / 1000.0F;
      return this;
   }

   boolean shouldAnimate() {
      AutoScrollHelper.ClampedScroller var1 = this.mScroller;
      int var2 = var1.getVerticalDirection();
      int var3 = var1.getHorizontalDirection();
      boolean var4;
      if ((var2 == 0 || !this.canTargetScrollVertically(var2)) && (var3 == 0 || !this.canTargetScrollHorizontally(var3))) {
         var4 = false;
      } else {
         var4 = true;
      }

      return var4;
   }

   private static class ClampedScroller {
      private long mDeltaTime = 0L;
      private int mDeltaX = 0;
      private int mDeltaY = 0;
      private int mEffectiveRampDown;
      private int mRampDownDuration;
      private int mRampUpDuration;
      private long mStartTime = Long.MIN_VALUE;
      private long mStopTime = -1L;
      private float mStopValue;
      private float mTargetVelocityX;
      private float mTargetVelocityY;

      ClampedScroller() {
      }

      private float getValueAt(long var1) {
         if (var1 < this.mStartTime) {
            return 0.0F;
         } else {
            long var3 = this.mStopTime;
            if (var3 >= 0L && var1 >= var3) {
               float var5 = this.mStopValue;
               return 1.0F - var5 + var5 * AutoScrollHelper.constrain((float)(var1 - var3) / (float)this.mEffectiveRampDown, 0.0F, 1.0F);
            } else {
               return AutoScrollHelper.constrain((float)(var1 - this.mStartTime) / (float)this.mRampUpDuration, 0.0F, 1.0F) * 0.5F;
            }
         }
      }

      private float interpolateValue(float var1) {
         return -4.0F * var1 * var1 + var1 * 4.0F;
      }

      public void computeScrollDelta() {
         if (this.mDeltaTime != 0L) {
            long var1 = AnimationUtils.currentAnimationTimeMillis();
            float var3 = this.interpolateValue(this.getValueAt(var1));
            long var4 = this.mDeltaTime;
            this.mDeltaTime = var1;
            var3 = (float)(var1 - var4) * var3;
            this.mDeltaX = (int)(this.mTargetVelocityX * var3);
            this.mDeltaY = (int)(var3 * this.mTargetVelocityY);
         } else {
            throw new RuntimeException("Cannot compute scroll delta before calling start()");
         }
      }

      public int getDeltaX() {
         return this.mDeltaX;
      }

      public int getDeltaY() {
         return this.mDeltaY;
      }

      public int getHorizontalDirection() {
         float var1 = this.mTargetVelocityX;
         return (int)(var1 / Math.abs(var1));
      }

      public int getVerticalDirection() {
         float var1 = this.mTargetVelocityY;
         return (int)(var1 / Math.abs(var1));
      }

      public boolean isFinished() {
         boolean var1;
         if (this.mStopTime > 0L && AnimationUtils.currentAnimationTimeMillis() > this.mStopTime + (long)this.mEffectiveRampDown) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public void requestStop() {
         long var1 = AnimationUtils.currentAnimationTimeMillis();
         this.mEffectiveRampDown = AutoScrollHelper.constrain((int)(var1 - this.mStartTime), 0, this.mRampDownDuration);
         this.mStopValue = this.getValueAt(var1);
         this.mStopTime = var1;
      }

      public void setRampDownDuration(int var1) {
         this.mRampDownDuration = var1;
      }

      public void setRampUpDuration(int var1) {
         this.mRampUpDuration = var1;
      }

      public void setTargetVelocity(float var1, float var2) {
         this.mTargetVelocityX = var1;
         this.mTargetVelocityY = var2;
      }

      public void start() {
         this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
         this.mStopTime = -1L;
         this.mDeltaTime = this.mStartTime;
         this.mStopValue = 0.5F;
         this.mDeltaX = 0;
         this.mDeltaY = 0;
      }
   }

   private class ScrollAnimationRunnable implements Runnable {
      ScrollAnimationRunnable() {
      }

      public void run() {
         AutoScrollHelper var1 = AutoScrollHelper.this;
         if (var1.mAnimating) {
            if (var1.mNeedsReset) {
               var1.mNeedsReset = false;
               var1.mScroller.start();
            }

            AutoScrollHelper.ClampedScroller var2 = AutoScrollHelper.this.mScroller;
            if (!var2.isFinished() && AutoScrollHelper.this.shouldAnimate()) {
               var1 = AutoScrollHelper.this;
               if (var1.mNeedsCancel) {
                  var1.mNeedsCancel = false;
                  var1.cancelTargetTouch();
               }

               var2.computeScrollDelta();
               int var3 = var2.getDeltaX();
               int var4 = var2.getDeltaY();
               AutoScrollHelper.this.scrollTargetBy(var3, var4);
               ViewCompat.postOnAnimation(AutoScrollHelper.this.mTarget, this);
            } else {
               AutoScrollHelper.this.mAnimating = false;
            }
         }
      }
   }
}
