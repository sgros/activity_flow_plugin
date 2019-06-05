package android.support.v4.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.graphics.Paint;
import android.os.Build.VERSION;
import android.view.View;
import android.view.animation.Interpolator;
import java.lang.ref.WeakReference;

public final class ViewPropertyAnimatorCompat {
   Runnable mEndAction = null;
   int mOldLayerType = -1;
   Runnable mStartAction = null;
   private WeakReference mView;

   ViewPropertyAnimatorCompat(View var1) {
      this.mView = new WeakReference(var1);
   }

   private void setListenerInternal(final View var1, final ViewPropertyAnimatorListener var2) {
      if (var2 != null) {
         var1.animate().setListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator var1x) {
               var2.onAnimationCancel(var1);
            }

            public void onAnimationEnd(Animator var1x) {
               var2.onAnimationEnd(var1);
            }

            public void onAnimationStart(Animator var1x) {
               var2.onAnimationStart(var1);
            }
         });
      } else {
         var1.animate().setListener((AnimatorListener)null);
      }

   }

   public ViewPropertyAnimatorCompat alpha(float var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null) {
         var2.animate().alpha(var1);
      }

      return this;
   }

   public void cancel() {
      View var1 = (View)this.mView.get();
      if (var1 != null) {
         var1.animate().cancel();
      }

   }

   public long getDuration() {
      View var1 = (View)this.mView.get();
      return var1 != null ? var1.animate().getDuration() : 0L;
   }

   public ViewPropertyAnimatorCompat setDuration(long var1) {
      View var3 = (View)this.mView.get();
      if (var3 != null) {
         var3.animate().setDuration(var1);
      }

      return this;
   }

   public ViewPropertyAnimatorCompat setInterpolator(Interpolator var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null) {
         var2.animate().setInterpolator(var1);
      }

      return this;
   }

   public ViewPropertyAnimatorCompat setListener(ViewPropertyAnimatorListener var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null) {
         if (VERSION.SDK_INT >= 16) {
            this.setListenerInternal(var2, var1);
         } else {
            var2.setTag(2113929216, var1);
            this.setListenerInternal(var2, new ViewPropertyAnimatorCompat.ViewPropertyAnimatorListenerApi14(this));
         }
      }

      return this;
   }

   public ViewPropertyAnimatorCompat setStartDelay(long var1) {
      View var3 = (View)this.mView.get();
      if (var3 != null) {
         var3.animate().setStartDelay(var1);
      }

      return this;
   }

   public ViewPropertyAnimatorCompat setUpdateListener(final ViewPropertyAnimatorUpdateListener var1) {
      final View var2 = (View)this.mView.get();
      if (var2 != null && VERSION.SDK_INT >= 19) {
         AnimatorUpdateListener var3 = null;
         if (var1 != null) {
            var3 = new AnimatorUpdateListener() {
               public void onAnimationUpdate(ValueAnimator var1x) {
                  var1.onAnimationUpdate(var2);
               }
            };
         }

         var2.animate().setUpdateListener(var3);
      }

      return this;
   }

   public void start() {
      View var1 = (View)this.mView.get();
      if (var1 != null) {
         var1.animate().start();
      }

   }

   public ViewPropertyAnimatorCompat translationY(float var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null) {
         var2.animate().translationY(var1);
      }

      return this;
   }

   static class ViewPropertyAnimatorListenerApi14 implements ViewPropertyAnimatorListener {
      boolean mAnimEndCalled;
      ViewPropertyAnimatorCompat mVpa;

      ViewPropertyAnimatorListenerApi14(ViewPropertyAnimatorCompat var1) {
         this.mVpa = var1;
      }

      public void onAnimationCancel(View var1) {
         Object var2 = var1.getTag(2113929216);
         ViewPropertyAnimatorListener var3;
         if (var2 instanceof ViewPropertyAnimatorListener) {
            var3 = (ViewPropertyAnimatorListener)var2;
         } else {
            var3 = null;
         }

         if (var3 != null) {
            var3.onAnimationCancel(var1);
         }

      }

      public void onAnimationEnd(View var1) {
         int var2 = this.mVpa.mOldLayerType;
         ViewPropertyAnimatorListener var3 = null;
         if (var2 > -1) {
            var1.setLayerType(this.mVpa.mOldLayerType, (Paint)null);
            this.mVpa.mOldLayerType = -1;
         }

         if (VERSION.SDK_INT >= 16 || !this.mAnimEndCalled) {
            if (this.mVpa.mEndAction != null) {
               Runnable var4 = this.mVpa.mEndAction;
               this.mVpa.mEndAction = null;
               var4.run();
            }

            Object var5 = var1.getTag(2113929216);
            if (var5 instanceof ViewPropertyAnimatorListener) {
               var3 = (ViewPropertyAnimatorListener)var5;
            }

            if (var3 != null) {
               var3.onAnimationEnd(var1);
            }

            this.mAnimEndCalled = true;
         }

      }

      public void onAnimationStart(View var1) {
         this.mAnimEndCalled = false;
         int var2 = this.mVpa.mOldLayerType;
         ViewPropertyAnimatorListener var3 = null;
         if (var2 > -1) {
            var1.setLayerType(2, (Paint)null);
         }

         if (this.mVpa.mStartAction != null) {
            Runnable var4 = this.mVpa.mStartAction;
            this.mVpa.mStartAction = null;
            var4.run();
         }

         Object var5 = var1.getTag(2113929216);
         if (var5 instanceof ViewPropertyAnimatorListener) {
            var3 = (ViewPropertyAnimatorListener)var5;
         }

         if (var3 != null) {
            var3.onAnimationStart(var1);
         }

      }
   }
}
