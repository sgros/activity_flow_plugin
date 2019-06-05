package android.support.v4.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Build.VERSION;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;

public final class GestureDetectorCompat {
   private final GestureDetectorCompat.GestureDetectorCompatImpl mImpl;

   public GestureDetectorCompat(Context var1, OnGestureListener var2) {
      this(var1, var2, (Handler)null);
   }

   public GestureDetectorCompat(Context var1, OnGestureListener var2, Handler var3) {
      if (VERSION.SDK_INT > 17) {
         this.mImpl = new GestureDetectorCompat.GestureDetectorCompatImplJellybeanMr2(var1, var2, var3);
      } else {
         this.mImpl = new GestureDetectorCompat.GestureDetectorCompatImplBase(var1, var2, var3);
      }

   }

   public boolean isLongpressEnabled() {
      return this.mImpl.isLongpressEnabled();
   }

   public boolean onTouchEvent(MotionEvent var1) {
      return this.mImpl.onTouchEvent(var1);
   }

   public void setIsLongpressEnabled(boolean var1) {
      this.mImpl.setIsLongpressEnabled(var1);
   }

   public void setOnDoubleTapListener(OnDoubleTapListener var1) {
      this.mImpl.setOnDoubleTapListener(var1);
   }

   interface GestureDetectorCompatImpl {
      boolean isLongpressEnabled();

      boolean onTouchEvent(MotionEvent var1);

      void setIsLongpressEnabled(boolean var1);

      void setOnDoubleTapListener(OnDoubleTapListener var1);
   }

   static class GestureDetectorCompatImplBase implements GestureDetectorCompat.GestureDetectorCompatImpl {
      private static final int DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();
      private static final int LONGPRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
      private static final int LONG_PRESS = 2;
      private static final int SHOW_PRESS = 1;
      private static final int TAP = 3;
      private static final int TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
      private boolean mAlwaysInBiggerTapRegion;
      private boolean mAlwaysInTapRegion;
      MotionEvent mCurrentDownEvent;
      boolean mDeferConfirmSingleTap;
      OnDoubleTapListener mDoubleTapListener;
      private int mDoubleTapSlopSquare;
      private float mDownFocusX;
      private float mDownFocusY;
      private final Handler mHandler;
      private boolean mInLongPress;
      private boolean mIsDoubleTapping;
      private boolean mIsLongpressEnabled;
      private float mLastFocusX;
      private float mLastFocusY;
      final OnGestureListener mListener;
      private int mMaximumFlingVelocity;
      private int mMinimumFlingVelocity;
      private MotionEvent mPreviousUpEvent;
      boolean mStillDown;
      private int mTouchSlopSquare;
      private VelocityTracker mVelocityTracker;

      public GestureDetectorCompatImplBase(Context var1, OnGestureListener var2, Handler var3) {
         if (var3 != null) {
            this.mHandler = new GestureDetectorCompat.GestureDetectorCompatImplBase.GestureHandler(var3);
         } else {
            this.mHandler = new GestureDetectorCompat.GestureDetectorCompatImplBase.GestureHandler();
         }

         this.mListener = var2;
         if (var2 instanceof OnDoubleTapListener) {
            this.setOnDoubleTapListener((OnDoubleTapListener)var2);
         }

         this.init(var1);
      }

      private void cancel() {
         this.mHandler.removeMessages(1);
         this.mHandler.removeMessages(2);
         this.mHandler.removeMessages(3);
         this.mVelocityTracker.recycle();
         this.mVelocityTracker = null;
         this.mIsDoubleTapping = false;
         this.mStillDown = false;
         this.mAlwaysInTapRegion = false;
         this.mAlwaysInBiggerTapRegion = false;
         this.mDeferConfirmSingleTap = false;
         if (this.mInLongPress) {
            this.mInLongPress = false;
         }

      }

      private void cancelTaps() {
         this.mHandler.removeMessages(1);
         this.mHandler.removeMessages(2);
         this.mHandler.removeMessages(3);
         this.mIsDoubleTapping = false;
         this.mAlwaysInTapRegion = false;
         this.mAlwaysInBiggerTapRegion = false;
         this.mDeferConfirmSingleTap = false;
         if (this.mInLongPress) {
            this.mInLongPress = false;
         }

      }

      private void init(Context var1) {
         if (var1 == null) {
            throw new IllegalArgumentException("Context must not be null");
         } else if (this.mListener == null) {
            throw new IllegalArgumentException("OnGestureListener must not be null");
         } else {
            this.mIsLongpressEnabled = true;
            ViewConfiguration var4 = ViewConfiguration.get(var1);
            int var2 = var4.getScaledTouchSlop();
            int var3 = var4.getScaledDoubleTapSlop();
            this.mMinimumFlingVelocity = var4.getScaledMinimumFlingVelocity();
            this.mMaximumFlingVelocity = var4.getScaledMaximumFlingVelocity();
            this.mTouchSlopSquare = var2 * var2;
            this.mDoubleTapSlopSquare = var3 * var3;
         }
      }

      private boolean isConsideredDoubleTap(MotionEvent var1, MotionEvent var2, MotionEvent var3) {
         boolean var4 = false;
         boolean var5;
         if (!this.mAlwaysInBiggerTapRegion) {
            var5 = var4;
         } else {
            var5 = var4;
            if (var3.getEventTime() - var2.getEventTime() <= (long)DOUBLE_TAP_TIMEOUT) {
               int var6 = (int)var1.getX() - (int)var3.getX();
               int var7 = (int)var1.getY() - (int)var3.getY();
               var5 = var4;
               if (var6 * var6 + var7 * var7 < this.mDoubleTapSlopSquare) {
                  var5 = true;
               }
            }
         }

         return var5;
      }

      void dispatchLongPress() {
         this.mHandler.removeMessages(3);
         this.mDeferConfirmSingleTap = false;
         this.mInLongPress = true;
         this.mListener.onLongPress(this.mCurrentDownEvent);
      }

      public boolean isLongpressEnabled() {
         return this.mIsLongpressEnabled;
      }

      public boolean onTouchEvent(MotionEvent var1) {
         int var2 = var1.getAction();
         if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
         }

         this.mVelocityTracker.addMovement(var1);
         boolean var3;
         if ((var2 & 255) == 6) {
            var3 = true;
         } else {
            var3 = false;
         }

         int var4;
         if (var3) {
            var4 = MotionEventCompat.getActionIndex(var1);
         } else {
            var4 = -1;
         }

         float var5 = 0.0F;
         float var6 = 0.0F;
         int var7 = var1.getPointerCount();

         int var8;
         for(var8 = 0; var8 < var7; ++var8) {
            if (var4 != var8) {
               var5 += var1.getX(var8);
               var6 += var1.getY(var8);
            }
         }

         int var17;
         if (var3) {
            var17 = var7 - 1;
         } else {
            var17 = var7;
         }

         var5 /= (float)var17;
         var6 /= (float)var17;
         boolean var18 = false;
         boolean var9 = false;
         boolean var10 = false;
         boolean var11 = false;
         boolean var12 = var11;
         switch(var2 & 255) {
         case 0:
            var3 = var18;
            if (this.mDoubleTapListener != null) {
               var12 = this.mHandler.hasMessages(3);
               if (var12) {
                  this.mHandler.removeMessages(3);
               }

               if (this.mCurrentDownEvent != null && this.mPreviousUpEvent != null && var12 && this.isConsideredDoubleTap(this.mCurrentDownEvent, this.mPreviousUpEvent, var1)) {
                  this.mIsDoubleTapping = true;
                  var3 = false | this.mDoubleTapListener.onDoubleTap(this.mCurrentDownEvent) | this.mDoubleTapListener.onDoubleTapEvent(var1);
               } else {
                  this.mHandler.sendEmptyMessageDelayed(3, (long)DOUBLE_TAP_TIMEOUT);
                  var3 = var18;
               }
            }

            this.mLastFocusX = var5;
            this.mDownFocusX = var5;
            this.mLastFocusY = var6;
            this.mDownFocusY = var6;
            if (this.mCurrentDownEvent != null) {
               this.mCurrentDownEvent.recycle();
            }

            this.mCurrentDownEvent = MotionEvent.obtain(var1);
            this.mAlwaysInTapRegion = true;
            this.mAlwaysInBiggerTapRegion = true;
            this.mStillDown = true;
            this.mInLongPress = false;
            this.mDeferConfirmSingleTap = false;
            if (this.mIsLongpressEnabled) {
               this.mHandler.removeMessages(2);
               this.mHandler.sendEmptyMessageAtTime(2, this.mCurrentDownEvent.getDownTime() + (long)TAP_TIMEOUT + (long)LONGPRESS_TIMEOUT);
            }

            this.mHandler.sendEmptyMessageAtTime(1, this.mCurrentDownEvent.getDownTime() + (long)TAP_TIMEOUT);
            var12 = var3 | this.mListener.onDown(var1);
            break;
         case 1:
            this.mStillDown = false;
            MotionEvent var15 = MotionEvent.obtain(var1);
            if (this.mIsDoubleTapping) {
               var12 = false | this.mDoubleTapListener.onDoubleTapEvent(var1);
            } else if (this.mInLongPress) {
               this.mHandler.removeMessages(3);
               this.mInLongPress = false;
               var12 = var10;
            } else if (this.mAlwaysInTapRegion) {
               var11 = this.mListener.onSingleTapUp(var1);
               var12 = var11;
               if (this.mDeferConfirmSingleTap) {
                  var12 = var11;
                  if (this.mDoubleTapListener != null) {
                     this.mDoubleTapListener.onSingleTapConfirmed(var1);
                     var12 = var11;
                  }
               }
            } else {
               label151: {
                  VelocityTracker var16 = this.mVelocityTracker;
                  var17 = var1.getPointerId(0);
                  var16.computeCurrentVelocity(1000, (float)this.mMaximumFlingVelocity);
                  var6 = VelocityTrackerCompat.getYVelocity(var16, var17);
                  var5 = VelocityTrackerCompat.getXVelocity(var16, var17);
                  if (Math.abs(var6) <= (float)this.mMinimumFlingVelocity) {
                     var12 = var10;
                     if (Math.abs(var5) <= (float)this.mMinimumFlingVelocity) {
                        break label151;
                     }
                  }

                  var12 = this.mListener.onFling(this.mCurrentDownEvent, var1, var5, var6);
               }
            }

            if (this.mPreviousUpEvent != null) {
               this.mPreviousUpEvent.recycle();
            }

            this.mPreviousUpEvent = var15;
            if (this.mVelocityTracker != null) {
               this.mVelocityTracker.recycle();
               this.mVelocityTracker = null;
            }

            this.mIsDoubleTapping = false;
            this.mDeferConfirmSingleTap = false;
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(2);
            break;
         case 2:
            var12 = var11;
            if (!this.mInLongPress) {
               float var13 = this.mLastFocusX - var5;
               float var14 = this.mLastFocusY - var6;
               if (this.mIsDoubleTapping) {
                  var12 = false | this.mDoubleTapListener.onDoubleTapEvent(var1);
               } else if (this.mAlwaysInTapRegion) {
                  var17 = (int)(var5 - this.mDownFocusX);
                  var4 = (int)(var6 - this.mDownFocusY);
                  var17 = var17 * var17 + var4 * var4;
                  var11 = var9;
                  if (var17 > this.mTouchSlopSquare) {
                     var11 = this.mListener.onScroll(this.mCurrentDownEvent, var1, var13, var14);
                     this.mLastFocusX = var5;
                     this.mLastFocusY = var6;
                     this.mAlwaysInTapRegion = false;
                     this.mHandler.removeMessages(3);
                     this.mHandler.removeMessages(1);
                     this.mHandler.removeMessages(2);
                  }

                  var12 = var11;
                  if (var17 > this.mTouchSlopSquare) {
                     this.mAlwaysInBiggerTapRegion = false;
                     var12 = var11;
                  }
               } else {
                  if (Math.abs(var13) < 1.0F) {
                     var12 = var11;
                     if (Math.abs(var14) < 1.0F) {
                        return var12;
                     }
                  }

                  var12 = this.mListener.onScroll(this.mCurrentDownEvent, var1, var13, var14);
                  this.mLastFocusX = var5;
                  this.mLastFocusY = var6;
               }
            }
            break;
         case 3:
            this.cancel();
            var12 = var11;
         case 4:
            break;
         case 5:
            this.mLastFocusX = var5;
            this.mDownFocusX = var5;
            this.mLastFocusY = var6;
            this.mDownFocusY = var6;
            this.cancelTaps();
            var12 = var11;
            break;
         case 6:
            this.mLastFocusX = var5;
            this.mDownFocusX = var5;
            this.mLastFocusY = var6;
            this.mDownFocusY = var6;
            this.mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumFlingVelocity);
            var4 = MotionEventCompat.getActionIndex(var1);
            var17 = var1.getPointerId(var4);
            var5 = VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, var17);
            var6 = VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, var17);
            var17 = 0;

            while(true) {
               var12 = var11;
               if (var17 >= var7) {
                  return var12;
               }

               if (var17 != var4) {
                  var8 = var1.getPointerId(var17);
                  if (var5 * VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, var8) + var6 * VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, var8) < 0.0F) {
                     this.mVelocityTracker.clear();
                     var12 = var11;
                     return var12;
                  }
               }

               ++var17;
            }
         default:
            var12 = var11;
         }

         return var12;
      }

      public void setIsLongpressEnabled(boolean var1) {
         this.mIsLongpressEnabled = var1;
      }

      public void setOnDoubleTapListener(OnDoubleTapListener var1) {
         this.mDoubleTapListener = var1;
      }

      private class GestureHandler extends Handler {
         GestureHandler() {
         }

         GestureHandler(Handler var2) {
            super(var2.getLooper());
         }

         public void handleMessage(Message var1) {
            switch(var1.what) {
            case 1:
               GestureDetectorCompatImplBase.this.mListener.onShowPress(GestureDetectorCompatImplBase.this.mCurrentDownEvent);
               break;
            case 2:
               GestureDetectorCompatImplBase.this.dispatchLongPress();
               break;
            case 3:
               if (GestureDetectorCompatImplBase.this.mDoubleTapListener != null) {
                  if (!GestureDetectorCompatImplBase.this.mStillDown) {
                     GestureDetectorCompatImplBase.this.mDoubleTapListener.onSingleTapConfirmed(GestureDetectorCompatImplBase.this.mCurrentDownEvent);
                  } else {
                     GestureDetectorCompatImplBase.this.mDeferConfirmSingleTap = true;
                  }
               }
               break;
            default:
               throw new RuntimeException("Unknown message " + var1);
            }

         }
      }
   }

   static class GestureDetectorCompatImplJellybeanMr2 implements GestureDetectorCompat.GestureDetectorCompatImpl {
      private final GestureDetector mDetector;

      public GestureDetectorCompatImplJellybeanMr2(Context var1, OnGestureListener var2, Handler var3) {
         this.mDetector = new GestureDetector(var1, var2, var3);
      }

      public boolean isLongpressEnabled() {
         return this.mDetector.isLongpressEnabled();
      }

      public boolean onTouchEvent(MotionEvent var1) {
         return this.mDetector.onTouchEvent(var1);
      }

      public void setIsLongpressEnabled(boolean var1) {
         this.mDetector.setIsLongpressEnabled(var1);
      }

      public void setOnDoubleTapListener(OnDoubleTapListener var1) {
         this.mDetector.setOnDoubleTapListener(var1);
      }
   }
}
