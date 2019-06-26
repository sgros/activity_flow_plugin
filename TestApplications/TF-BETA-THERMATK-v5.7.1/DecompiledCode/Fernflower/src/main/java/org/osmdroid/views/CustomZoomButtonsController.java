package org.osmdroid.views;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.graphics.Canvas;
import android.os.Build.VERSION;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;
import java.lang.Thread.State;

public class CustomZoomButtonsController {
   private boolean detached;
   private float mAlpha01;
   private CustomZoomButtonsDisplay mDisplay;
   private final ValueAnimator mFadeOutAnimation;
   private int mFadeOutAnimationDurationInMillis;
   private boolean mJustActivated;
   private long mLatestActivation;
   private CustomZoomButtonsController.OnZoomListener mListener;
   private final MapView mMapView;
   private final Runnable mRunnable;
   private int mShowDelayInMillis;
   private Thread mThread;
   private final Object mThreadSync = new Object();
   private CustomZoomButtonsController.Visibility mVisibility;
   private boolean mZoomInEnabled;
   private boolean mZoomOutEnabled;

   public CustomZoomButtonsController(MapView var1) {
      this.mVisibility = CustomZoomButtonsController.Visibility.NEVER;
      this.mFadeOutAnimationDurationInMillis = 500;
      this.mShowDelayInMillis = 3500;
      this.mMapView = var1;
      this.mDisplay = new CustomZoomButtonsDisplay(this.mMapView);
      if (VERSION.SDK_INT >= 11) {
         this.mFadeOutAnimation = ValueAnimator.ofFloat(new float[]{0.0F, 1.0F});
         this.mFadeOutAnimation.setInterpolator(new LinearInterpolator());
         this.mFadeOutAnimation.setDuration((long)this.mFadeOutAnimationDurationInMillis);
         this.mFadeOutAnimation.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator var1) {
               if (CustomZoomButtonsController.this.detached) {
                  CustomZoomButtonsController.this.mFadeOutAnimation.cancel();
               } else {
                  CustomZoomButtonsController.this.mAlpha01 = 1.0F - (Float)var1.getAnimatedValue();
                  CustomZoomButtonsController.this.invalidate();
               }
            }
         });
      } else {
         this.mFadeOutAnimation = null;
      }

      this.mRunnable = new Runnable() {
         public void run() {
            while(true) {
               long var1 = CustomZoomButtonsController.this.mLatestActivation + (long)CustomZoomButtonsController.this.mShowDelayInMillis - CustomZoomButtonsController.this.nowInMillis();
               if (var1 <= 0L) {
                  CustomZoomButtonsController.this.startFadeOut();
                  return;
               }

               try {
                  Thread.sleep(var1, 0);
               } catch (InterruptedException var4) {
               }
            }
         }
      };
   }

   private boolean checkJustActivated() {
      if (this.mJustActivated) {
         this.mJustActivated = false;
         return true;
      } else {
         return false;
      }
   }

   private void invalidate() {
      if (!this.detached) {
         this.mMapView.postInvalidate();
      }
   }

   private boolean isTouched(MotionEvent var1) {
      if (this.mAlpha01 == 0.0F) {
         return false;
      } else if (this.checkJustActivated()) {
         return false;
      } else {
         CustomZoomButtonsController.OnZoomListener var2;
         if (this.mDisplay.isTouchedRotated(var1, true)) {
            if (this.mZoomInEnabled) {
               var2 = this.mListener;
               if (var2 != null) {
                  var2.onZoom(true);
               }
            }

            return true;
         } else if (this.mDisplay.isTouchedRotated(var1, false)) {
            if (this.mZoomOutEnabled) {
               var2 = this.mListener;
               if (var2 != null) {
                  var2.onZoom(false);
               }
            }

            return true;
         } else {
            return false;
         }
      }
   }

   private long nowInMillis() {
      return System.currentTimeMillis();
   }

   private void startFadeOut() {
      if (!this.detached) {
         if (VERSION.SDK_INT >= 11) {
            this.mFadeOutAnimation.setStartDelay(0L);
            this.mMapView.post(new Runnable() {
               public void run() {
                  CustomZoomButtonsController.this.mFadeOutAnimation.start();
               }
            });
         } else {
            this.mAlpha01 = 0.0F;
            this.invalidate();
         }

      }
   }

   private void stopFadeOut() {
      if (VERSION.SDK_INT >= 11) {
         this.mFadeOutAnimation.cancel();
      }

   }

   public void activate() {
      if (!this.detached) {
         if (this.mVisibility == CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT) {
            float var1 = this.mAlpha01;
            boolean var2 = this.mJustActivated;
            boolean var3 = false;
            if (!var2) {
               if (var1 == 0.0F) {
                  var3 = true;
               }

               this.mJustActivated = var3;
            } else {
               this.mJustActivated = false;
            }

            this.stopFadeOut();
            this.mAlpha01 = 1.0F;
            this.mLatestActivation = this.nowInMillis();
            this.invalidate();
            Thread var4 = this.mThread;
            if (var4 == null || var4.getState() == State.TERMINATED) {
               Object var26 = this.mThreadSync;
               synchronized(var26){}

               Throwable var10000;
               boolean var10001;
               label304: {
                  label303: {
                     try {
                        if (this.mThread != null && this.mThread.getState() != State.TERMINATED) {
                           break label303;
                        }
                     } catch (Throwable var25) {
                        var10000 = var25;
                        var10001 = false;
                        break label304;
                     }

                     try {
                        Thread var5 = new Thread(this.mRunnable);
                        this.mThread = var5;
                        this.mThread.start();
                     } catch (Throwable var24) {
                        var10000 = var24;
                        var10001 = false;
                        break label304;
                     }
                  }

                  label294:
                  try {
                     return;
                  } catch (Throwable var23) {
                     var10000 = var23;
                     var10001 = false;
                     break label294;
                  }
               }

               while(true) {
                  Throwable var27 = var10000;

                  try {
                     throw var27;
                  } catch (Throwable var22) {
                     var10000 = var22;
                     var10001 = false;
                     continue;
                  }
               }
            }
         }
      }
   }

   public void draw(Canvas var1) {
      this.mDisplay.draw(var1, this.mAlpha01, this.mZoomInEnabled, this.mZoomOutEnabled);
   }

   public void onDetach() {
      this.detached = true;
      this.stopFadeOut();
   }

   public boolean onLongPress(MotionEvent var1) {
      return this.isTouched(var1);
   }

   public boolean onSingleTapConfirmed(MotionEvent var1) {
      return this.isTouched(var1);
   }

   public void setOnZoomListener(CustomZoomButtonsController.OnZoomListener var1) {
      this.mListener = var1;
   }

   public void setVisibility(CustomZoomButtonsController.Visibility var1) {
      this.mVisibility = var1;
      int var2 = null.$SwitchMap$org$osmdroid$views$CustomZoomButtonsController$Visibility[this.mVisibility.ordinal()];
      if (var2 != 1) {
         if (var2 == 2 || var2 == 3) {
            this.mAlpha01 = 0.0F;
         }
      } else {
         this.mAlpha01 = 1.0F;
      }

   }

   public void setZoomInEnabled(boolean var1) {
      this.mZoomInEnabled = var1;
   }

   public void setZoomOutEnabled(boolean var1) {
      this.mZoomOutEnabled = var1;
   }

   public interface OnZoomListener {
      void onZoom(boolean var1);
   }

   public static enum Visibility {
      ALWAYS,
      NEVER,
      SHOW_AND_FADEOUT;
   }
}
