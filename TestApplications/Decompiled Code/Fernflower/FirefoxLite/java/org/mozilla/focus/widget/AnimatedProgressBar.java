package org.mozilla.focus.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import org.mozilla.focus.R;

public class AnimatedProgressBar extends ProgressBar {
   private float mClipRatio = 0.0F;
   private final ValueAnimator mClosingAnimator = ValueAnimator.ofFloat(new float[]{0.0F, 1.0F});
   private AnimatedProgressBar.EndingRunner mEndingRunner = new AnimatedProgressBar.EndingRunner();
   private int mExpectedProgress = 0;
   private boolean mInitialized = false;
   private boolean mIsRtl = false;
   private final AnimatorUpdateListener mListener = new AnimatorUpdateListener() {
      public void onAnimationUpdate(ValueAnimator var1) {
         AnimatedProgressBar.this.setProgressImmediately((Integer)AnimatedProgressBar.this.mPrimaryAnimator.getAnimatedValue());
      }
   };
   private ValueAnimator mPrimaryAnimator;
   private final Rect mRect = new Rect();

   public AnimatedProgressBar(Context var1) {
      super(var1, (AttributeSet)null);
      this.init(var1, (AttributeSet)null);
   }

   public AnimatedProgressBar(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.init(var1, var2);
   }

   public AnimatedProgressBar(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.init(var1, var2);
   }

   @TargetApi(21)
   public AnimatedProgressBar(Context var1, AttributeSet var2, int var3, int var4) {
      super(var1, var2, var3);
      this.init(var1, var2);
   }

   private void animateClosing() {
      this.mClosingAnimator.cancel();
      Handler var1 = this.getHandler();
      if (var1 != null) {
         var1.removeCallbacks(this.mEndingRunner);
         var1.postDelayed(this.mEndingRunner, 300L);
      }

   }

   private Drawable buildDrawable(Drawable var1, boolean var2, int var3, int var4) {
      if (var2) {
         Interpolator var5;
         if (this.isValidInterpolator(var4)) {
            var5 = AnimationUtils.loadInterpolator(this.getContext(), var4);
         } else {
            var5 = null;
         }

         return new ShiftDrawable(var1, var3, var5);
      } else {
         return var1;
      }
   }

   private void cancelAnimations() {
      if (this.mPrimaryAnimator != null) {
         this.mPrimaryAnimator.cancel();
      }

      if (this.mClosingAnimator != null) {
         this.mClosingAnimator.cancel();
      }

      this.mClipRatio = 0.0F;
   }

   private static ValueAnimator createAnimator(int var0, AnimatorUpdateListener var1) {
      ValueAnimator var2 = ValueAnimator.ofInt(new int[]{0, var0});
      var2.setInterpolator(new LinearInterpolator());
      var2.setDuration(200L);
      var2.addUpdateListener(var1);
      return var2;
   }

   private String endLoadingString() {
      return this.getContext().getString(2131755055);
   }

   private void init(Context var1, AttributeSet var2) {
      this.mInitialized = true;
      TypedArray var6 = var1.obtainStyledAttributes(var2, R.styleable.AnimatedProgressBar);
      int var3 = var6.getInteger(0, 1000);
      boolean var4 = var6.getBoolean(2, false);
      int var5 = var6.getResourceId(1, 0);
      var6.recycle();
      final Drawable var7 = this.buildDrawable(this.getProgressDrawable(), var4, var3, var5);
      this.setProgressDrawable(var7);
      this.mPrimaryAnimator = createAnimator(this.getMax(), this.mListener);
      this.mClosingAnimator.setDuration(300L);
      this.mClosingAnimator.setInterpolator(new LinearInterpolator());
      this.mClosingAnimator.addUpdateListener(new AnimatorUpdateListener() {
         public void onAnimationUpdate(ValueAnimator var1) {
            AnimatedProgressBar.this.mClipRatio = (Float)var1.getAnimatedValue();
            AnimatedProgressBar.this.invalidate();
         }
      });
      this.mClosingAnimator.addListener(new AnimatorListener() {
         public void onAnimationCancel(Animator var1) {
            AnimatedProgressBar.this.mClipRatio = 0.0F;
         }

         public void onAnimationEnd(Animator var1) {
            AnimatedProgressBar.this.setVisibilityImmediately(8);
            if (var7 instanceof ShiftDrawable) {
               ((ShiftDrawable)var7).stop();
            }

         }

         public void onAnimationRepeat(Animator var1) {
         }

         public void onAnimationStart(Animator var1) {
            AnimatedProgressBar.this.mClipRatio = 0.0F;
         }
      });
   }

   private boolean isValidInterpolator(int var1) {
      boolean var2;
      if (var1 != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private void setProgressImmediately(int var1) {
      super.setProgress(var1);
   }

   private void setVisibilityImmediately(int var1) {
      super.setVisibility(var1);
   }

   private String startLoadingString() {
      return this.getContext().getString(2131755054);
   }

   public void onAttachedToWindow() {
      super.onAttachedToWindow();
      int var1 = ViewCompat.getLayoutDirection(this);
      boolean var2 = true;
      if (var1 != 1) {
         var2 = false;
      }

      this.mIsRtl = var2;
   }

   public void onDraw(Canvas var1) {
      if (this.mClipRatio == 0.0F) {
         super.onDraw(var1);
      } else {
         var1.getClipBounds(this.mRect);
         float var2 = (float)this.mRect.width() * this.mClipRatio;
         var1.save();
         if (this.mIsRtl) {
            var1.clipRect((float)this.mRect.left, (float)this.mRect.top, (float)this.mRect.right - var2, (float)this.mRect.bottom);
         } else {
            var1.clipRect((float)this.mRect.left + var2, (float)this.mRect.top, (float)this.mRect.right, (float)this.mRect.bottom);
         }

         super.onDraw(var1);
         var1.restore();
      }

   }

   public void setMax(int var1) {
      synchronized(this){}

      try {
         super.setMax(var1);
         this.mPrimaryAnimator = createAnimator(this.getMax(), this.mListener);
      } finally {
         ;
      }

   }

   public void setProgress(int var1) {
      var1 = Math.max(0, Math.min(var1, this.getMax()));
      this.mExpectedProgress = var1;
      if (var1 == this.getMax()) {
         if (this.getVisibility() == 0) {
            this.setVisibility(8);
            this.announceForAccessibility(this.endLoadingString());
         }
      } else {
         if (this.getVisibility() == 8) {
            this.setVisibility(0);
            this.announceForAccessibility(this.startLoadingString());
         }

         Drawable var2 = this.getProgressDrawable();
         if (var2 instanceof ShiftDrawable) {
            ((ShiftDrawable)var2).start();
         }
      }

      if (!this.mInitialized) {
         this.setProgressImmediately(this.mExpectedProgress);
      } else if (this.mExpectedProgress < this.getProgress()) {
         this.cancelAnimations();
         this.setProgressImmediately(this.mExpectedProgress);
      } else if (this.mExpectedProgress == 0 && this.getProgress() == this.getMax()) {
         this.cancelAnimations();
         this.setProgressImmediately(0);
      } else {
         this.cancelAnimations();
         this.mPrimaryAnimator.setIntValues(new int[]{this.getProgress(), var1});
         this.mPrimaryAnimator.start();
      }
   }

   public void setVisibility(int var1) {
      if (this.getVisibility() != var1) {
         if (var1 == 8) {
            if (this.mExpectedProgress == this.getMax()) {
               this.setProgressImmediately(this.mExpectedProgress);
               this.animateClosing();
            } else {
               this.setVisibilityImmediately(var1);
            }
         } else {
            Handler var2 = this.getHandler();
            if (var2 != null) {
               var2.removeCallbacks(this.mEndingRunner);
            }

            if (this.mClosingAnimator != null) {
               this.mClipRatio = 0.0F;
               this.mClosingAnimator.cancel();
            }

            this.setVisibilityImmediately(var1);
         }

      }
   }

   private class EndingRunner implements Runnable {
      private EndingRunner() {
      }

      // $FF: synthetic method
      EndingRunner(Object var2) {
         this();
      }

      public void run() {
         AnimatedProgressBar.this.mClosingAnimator.start();
      }
   }
}
