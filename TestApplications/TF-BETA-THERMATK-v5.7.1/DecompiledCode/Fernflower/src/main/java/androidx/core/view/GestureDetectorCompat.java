package androidx.core.view;

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

   public boolean onTouchEvent(MotionEvent var1) {
      return this.mImpl.onTouchEvent(var1);
   }

   interface GestureDetectorCompatImpl {
      boolean onTouchEvent(MotionEvent var1);
   }

   static class GestureDetectorCompatImplBase implements GestureDetectorCompat.GestureDetectorCompatImpl {
      private static final int DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();
      private static final int LONGPRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
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

      GestureDetectorCompatImplBase(Context var1, OnGestureListener var2, Handler var3) {
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
         if (var1 != null) {
            if (this.mListener != null) {
               this.mIsLongpressEnabled = true;
               ViewConfiguration var4 = ViewConfiguration.get(var1);
               int var2 = var4.getScaledTouchSlop();
               int var3 = var4.getScaledDoubleTapSlop();
               this.mMinimumFlingVelocity = var4.getScaledMinimumFlingVelocity();
               this.mMaximumFlingVelocity = var4.getScaledMaximumFlingVelocity();
               this.mTouchSlopSquare = var2 * var2;
               this.mDoubleTapSlopSquare = var3 * var3;
            } else {
               throw new IllegalArgumentException("OnGestureListener must not be null");
            }
         } else {
            throw new IllegalArgumentException("Context must not be null");
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

      public boolean onTouchEvent(MotionEvent var1) {
         int var2 = var1.getAction();
         if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
         }

         this.mVelocityTracker.addMovement(var1);
         int var3 = var2 & 255;
         boolean var4 = false;
         boolean var16;
         if (var3 == 6) {
            var16 = true;
         } else {
            var16 = false;
         }

         int var5;
         if (var16) {
            var5 = var1.getActionIndex();
         } else {
            var5 = -1;
         }

         int var6 = var1.getPointerCount();
         int var7 = 0;
         float var8 = 0.0F;

         float var9;
         for(var9 = 0.0F; var7 < var6; ++var7) {
            if (var5 != var7) {
               var8 += var1.getX(var7);
               var9 += var1.getY(var7);
            }
         }

         if (var16) {
            var2 = var6 - 1;
         } else {
            var2 = var6;
         }

         float var10 = (float)var2;
         var8 /= var10;
         float var11 = var9 / var10;
         boolean var12;
         MotionEvent var13;
         if (var3 != 0) {
            if (var3 != 1) {
               if (var3 != 2) {
                  if (var3 != 3) {
                     if (var3 != 5) {
                        if (var3 != 6) {
                           var12 = var4;
                        } else {
                           this.mLastFocusX = var8;
                           this.mDownFocusX = var8;
                           this.mLastFocusY = var11;
                           this.mDownFocusY = var11;
                           this.mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumFlingVelocity);
                           var5 = var1.getActionIndex();
                           var2 = var1.getPointerId(var5);
                           var9 = this.mVelocityTracker.getXVelocity(var2);
                           var8 = this.mVelocityTracker.getYVelocity(var2);
                           var2 = 0;

                           while(true) {
                              var12 = var4;
                              if (var2 >= var6) {
                                 break;
                              }

                              if (var2 != var5) {
                                 var7 = var1.getPointerId(var2);
                                 if (this.mVelocityTracker.getXVelocity(var7) * var9 + this.mVelocityTracker.getYVelocity(var7) * var8 < 0.0F) {
                                    this.mVelocityTracker.clear();
                                    var12 = var4;
                                    break;
                                 }
                              }

                              ++var2;
                           }
                        }
                     } else {
                        this.mLastFocusX = var8;
                        this.mDownFocusX = var8;
                        this.mLastFocusY = var11;
                        this.mDownFocusY = var11;
                        this.cancelTaps();
                        var12 = var4;
                     }
                  } else {
                     this.cancel();
                     var12 = var4;
                  }
               } else if (this.mInLongPress) {
                  var12 = var4;
               } else {
                  var10 = this.mLastFocusX - var8;
                  var9 = this.mLastFocusY - var11;
                  if (this.mIsDoubleTapping) {
                     var12 = false | this.mDoubleTapListener.onDoubleTapEvent(var1);
                  } else if (this.mAlwaysInTapRegion) {
                     var2 = (int)(var8 - this.mDownFocusX);
                     var5 = (int)(var11 - this.mDownFocusY);
                     var2 = var2 * var2 + var5 * var5;
                     if (var2 > this.mTouchSlopSquare) {
                        var4 = this.mListener.onScroll(this.mCurrentDownEvent, var1, var10, var9);
                        this.mLastFocusX = var8;
                        this.mLastFocusY = var11;
                        this.mAlwaysInTapRegion = false;
                        this.mHandler.removeMessages(3);
                        this.mHandler.removeMessages(1);
                        this.mHandler.removeMessages(2);
                     } else {
                        var4 = false;
                     }

                     var12 = var4;
                     if (var2 > this.mTouchSlopSquare) {
                        this.mAlwaysInBiggerTapRegion = false;
                        var12 = var4;
                     }
                  } else {
                     if (Math.abs(var10) < 1.0F) {
                        var12 = var4;
                        if (Math.abs(var9) < 1.0F) {
                           return var12;
                        }
                     }

                     var12 = this.mListener.onScroll(this.mCurrentDownEvent, var1, var10, var9);
                     this.mLastFocusX = var8;
                     this.mLastFocusY = var11;
                  }
               }
            } else {
               this.mStillDown = false;
               var13 = MotionEvent.obtain(var1);
               if (this.mIsDoubleTapping) {
                  var12 = this.mDoubleTapListener.onDoubleTapEvent(var1) | false;
               } else {
                  label173: {
                     if (this.mInLongPress) {
                        this.mHandler.removeMessages(3);
                        this.mInLongPress = false;
                     } else {
                        if (this.mAlwaysInTapRegion) {
                           var12 = this.mListener.onSingleTapUp(var1);
                           if (this.mDeferConfirmSingleTap) {
                              OnDoubleTapListener var17 = this.mDoubleTapListener;
                              if (var17 != null) {
                                 var17.onSingleTapConfirmed(var1);
                              }
                           }
                           break label173;
                        }

                        VelocityTracker var14 = this.mVelocityTracker;
                        var2 = var1.getPointerId(0);
                        var14.computeCurrentVelocity(1000, (float)this.mMaximumFlingVelocity);
                        var9 = var14.getYVelocity(var2);
                        var8 = var14.getXVelocity(var2);
                        if (Math.abs(var9) > (float)this.mMinimumFlingVelocity || Math.abs(var8) > (float)this.mMinimumFlingVelocity) {
                           var12 = this.mListener.onFling(this.mCurrentDownEvent, var1, var8, var9);
                           break label173;
                        }
                     }

                     var12 = false;
                  }
               }

               var1 = this.mPreviousUpEvent;
               if (var1 != null) {
                  var1.recycle();
               }

               this.mPreviousUpEvent = var13;
               VelocityTracker var15 = this.mVelocityTracker;
               if (var15 != null) {
                  var15.recycle();
                  this.mVelocityTracker = null;
               }

               this.mIsDoubleTapping = false;
               this.mDeferConfirmSingleTap = false;
               this.mHandler.removeMessages(1);
               this.mHandler.removeMessages(2);
            }
         } else {
            label131: {
               if (this.mDoubleTapListener != null) {
                  var12 = this.mHandler.hasMessages(3);
                  if (var12) {
                     this.mHandler.removeMessages(3);
                  }

                  var13 = this.mCurrentDownEvent;
                  if (var13 != null) {
                     MotionEvent var18 = this.mPreviousUpEvent;
                     if (var18 != null && var12 && this.isConsideredDoubleTap(var13, var18, var1)) {
                        this.mIsDoubleTapping = true;
                        var16 = this.mDoubleTapListener.onDoubleTap(this.mCurrentDownEvent) | false | this.mDoubleTapListener.onDoubleTapEvent(var1);
                        break label131;
                     }
                  }

                  this.mHandler.sendEmptyMessageDelayed(3, (long)DOUBLE_TAP_TIMEOUT);
               }

               var16 = false;
            }

            this.mLastFocusX = var8;
            this.mDownFocusX = var8;
            this.mLastFocusY = var11;
            this.mDownFocusY = var11;
            var13 = this.mCurrentDownEvent;
            if (var13 != null) {
               var13.recycle();
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
            var12 = var16 | this.mListener.onDown(var1);
         }

         return var12;
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
            int var2 = var1.what;
            if (var2 != 1) {
               if (var2 != 2) {
                  if (var2 != 3) {
                     StringBuilder var6 = new StringBuilder();
                     var6.append("Unknown message ");
                     var6.append(var1);
                     throw new RuntimeException(var6.toString());
                  }

                  GestureDetectorCompat.GestureDetectorCompatImplBase var3 = GestureDetectorCompatImplBase.this;
                  OnDoubleTapListener var4 = var3.mDoubleTapListener;
                  if (var4 != null) {
                     if (!var3.mStillDown) {
                        var4.onSingleTapConfirmed(var3.mCurrentDownEvent);
                     } else {
                        var3.mDeferConfirmSingleTap = true;
                     }
                  }
               } else {
                  GestureDetectorCompatImplBase.this.dispatchLongPress();
               }
            } else {
               GestureDetectorCompat.GestureDetectorCompatImplBase var5 = GestureDetectorCompatImplBase.this;
               var5.mListener.onShowPress(var5.mCurrentDownEvent);
            }

         }
      }
   }

   static class GestureDetectorCompatImplJellybeanMr2 implements GestureDetectorCompat.GestureDetectorCompatImpl {
      private final GestureDetector mDetector;

      GestureDetectorCompatImplJellybeanMr2(Context var1, OnGestureListener var2, Handler var3) {
         this.mDetector = new GestureDetector(var1, var2, var3);
      }

      public boolean onTouchEvent(MotionEvent var1) {
         return this.mDetector.onTouchEvent(var1);
      }
   }
}
