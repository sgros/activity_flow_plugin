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
         boolean var4 = this.mAlwaysInBiggerTapRegion;
         boolean var5 = false;
         if (!var4) {
            return false;
         } else if (var3.getEventTime() - var2.getEventTime() > (long)DOUBLE_TAP_TIMEOUT) {
            return false;
         } else {
            int var6 = (int)var1.getX() - (int)var3.getX();
            int var7 = (int)var1.getY() - (int)var3.getY();
            if (var6 * var6 + var7 * var7 < this.mDoubleTapSlopSquare) {
               var5 = true;
            }

            return var5;
         }
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
         int var3 = var2 & 255;
         boolean var4 = false;
         boolean var15;
         if (var3 == 6) {
            var15 = true;
         } else {
            var15 = false;
         }

         int var5;
         if (var15) {
            var5 = var1.getActionIndex();
         } else {
            var5 = -1;
         }

         int var6 = var1.getPointerCount();
         int var7 = 0;
         float var8 = 0.0F;

         float var9;
         for(var9 = var8; var7 < var6; ++var7) {
            if (var5 != var7) {
               var8 += var1.getX(var7);
               var9 += var1.getY(var7);
            }
         }

         if (var15) {
            var2 = var6 - 1;
         } else {
            var2 = var6;
         }

         float var10 = (float)var2;
         var8 /= var10;
         var10 = var9 / var10;
         boolean var11;
         switch(var3) {
         case 0:
            label126: {
               if (this.mDoubleTapListener != null) {
                  var11 = this.mHandler.hasMessages(3);
                  if (var11) {
                     this.mHandler.removeMessages(3);
                  }

                  if (this.mCurrentDownEvent != null && this.mPreviousUpEvent != null && var11 && this.isConsideredDoubleTap(this.mCurrentDownEvent, this.mPreviousUpEvent, var1)) {
                     this.mIsDoubleTapping = true;
                     var15 = this.mDoubleTapListener.onDoubleTap(this.mCurrentDownEvent) | false | this.mDoubleTapListener.onDoubleTapEvent(var1);
                     break label126;
                  }

                  this.mHandler.sendEmptyMessageDelayed(3, (long)DOUBLE_TAP_TIMEOUT);
               }

               var15 = false;
            }

            this.mLastFocusX = var8;
            this.mDownFocusX = var8;
            this.mLastFocusY = var10;
            this.mDownFocusY = var10;
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
            var11 = var15 | this.mListener.onDown(var1);
            break;
         case 1:
            this.mStillDown = false;
            MotionEvent var13 = MotionEvent.obtain(var1);
            if (this.mIsDoubleTapping) {
               var11 = this.mDoubleTapListener.onDoubleTapEvent(var1) | false;
            } else {
               label163: {
                  if (this.mInLongPress) {
                     this.mHandler.removeMessages(3);
                     this.mInLongPress = false;
                  } else {
                     if (this.mAlwaysInTapRegion) {
                        var11 = this.mListener.onSingleTapUp(var1);
                        if (this.mDeferConfirmSingleTap && this.mDoubleTapListener != null) {
                           this.mDoubleTapListener.onSingleTapConfirmed(var1);
                        }
                        break label163;
                     }

                     VelocityTracker var14 = this.mVelocityTracker;
                     var2 = var1.getPointerId(0);
                     var14.computeCurrentVelocity(1000, (float)this.mMaximumFlingVelocity);
                     var8 = var14.getYVelocity(var2);
                     var9 = var14.getXVelocity(var2);
                     if (Math.abs(var8) > (float)this.mMinimumFlingVelocity || Math.abs(var9) > (float)this.mMinimumFlingVelocity) {
                        var11 = this.mListener.onFling(this.mCurrentDownEvent, var1, var9, var8);
                        break label163;
                     }
                  }

                  var11 = false;
               }
            }

            if (this.mPreviousUpEvent != null) {
               this.mPreviousUpEvent.recycle();
            }

            this.mPreviousUpEvent = var13;
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
            if (this.mInLongPress) {
               var11 = var4;
            } else {
               var9 = this.mLastFocusX - var8;
               float var12 = this.mLastFocusY - var10;
               if (this.mIsDoubleTapping) {
                  var11 = false | this.mDoubleTapListener.onDoubleTapEvent(var1);
               } else if (this.mAlwaysInTapRegion) {
                  var2 = (int)(var8 - this.mDownFocusX);
                  var5 = (int)(var10 - this.mDownFocusY);
                  var2 = var2 * var2 + var5 * var5;
                  if (var2 > this.mTouchSlopSquare) {
                     var4 = this.mListener.onScroll(this.mCurrentDownEvent, var1, var9, var12);
                     this.mLastFocusX = var8;
                     this.mLastFocusY = var10;
                     this.mAlwaysInTapRegion = false;
                     this.mHandler.removeMessages(3);
                     this.mHandler.removeMessages(1);
                     this.mHandler.removeMessages(2);
                  } else {
                     var4 = false;
                  }

                  var11 = var4;
                  if (var2 > this.mTouchSlopSquare) {
                     this.mAlwaysInBiggerTapRegion = false;
                     var11 = var4;
                  }
               } else {
                  if (Math.abs(var9) < 1.0F) {
                     var11 = var4;
                     if (Math.abs(var12) < 1.0F) {
                        break;
                     }
                  }

                  var11 = this.mListener.onScroll(this.mCurrentDownEvent, var1, var9, var12);
                  this.mLastFocusX = var8;
                  this.mLastFocusY = var10;
               }
            }
            break;
         case 3:
            this.cancel();
            var11 = var4;
            break;
         case 4:
         default:
            var11 = var4;
            break;
         case 5:
            this.mLastFocusX = var8;
            this.mDownFocusX = var8;
            this.mLastFocusY = var10;
            this.mDownFocusY = var10;
            this.cancelTaps();
            var11 = var4;
            break;
         case 6:
            this.mLastFocusX = var8;
            this.mDownFocusX = var8;
            this.mLastFocusY = var10;
            this.mDownFocusY = var10;
            this.mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumFlingVelocity);
            var5 = var1.getActionIndex();
            var2 = var1.getPointerId(var5);
            var9 = this.mVelocityTracker.getXVelocity(var2);
            var8 = this.mVelocityTracker.getYVelocity(var2);
            var2 = 0;

            while(true) {
               var11 = var4;
               if (var2 >= var6) {
                  break;
               }

               if (var2 != var5) {
                  var7 = var1.getPointerId(var2);
                  if (this.mVelocityTracker.getXVelocity(var7) * var9 + this.mVelocityTracker.getYVelocity(var7) * var8 < 0.0F) {
                     this.mVelocityTracker.clear();
                     var11 = var4;
                     break;
                  }
               }

               ++var2;
            }
         }

         return var11;
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
               StringBuilder var2 = new StringBuilder();
               var2.append("Unknown message ");
               var2.append(var1);
               throw new RuntimeException(var2.toString());
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
