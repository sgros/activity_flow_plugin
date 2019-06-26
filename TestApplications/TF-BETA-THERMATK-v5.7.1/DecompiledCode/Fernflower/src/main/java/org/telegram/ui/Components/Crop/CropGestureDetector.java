package org.telegram.ui.Components.Crop;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import org.telegram.messenger.AndroidUtilities;

public class CropGestureDetector {
   private static final int INVALID_POINTER_ID = -1;
   private int mActivePointerId;
   private int mActivePointerIndex;
   private ScaleGestureDetector mDetector;
   private boolean mIsDragging;
   float mLastTouchX;
   float mLastTouchY;
   private CropGestureDetector.CropGestureListener mListener;
   final float mMinimumVelocity;
   final float mTouchSlop;
   private VelocityTracker mVelocityTracker;
   private boolean started;

   public CropGestureDetector(Context var1) {
      this.mMinimumVelocity = (float)ViewConfiguration.get(var1).getScaledMinimumFlingVelocity();
      this.mTouchSlop = (float)AndroidUtilities.dp(1.0F);
      this.mActivePointerId = -1;
      this.mActivePointerIndex = 0;
      this.mDetector = new ScaleGestureDetector(var1, new OnScaleGestureListener() {
         public boolean onScale(ScaleGestureDetector var1) {
            float var2 = var1.getScaleFactor();
            if (!Float.isNaN(var2) && !Float.isInfinite(var2)) {
               CropGestureDetector.this.mListener.onScale(var2, var1.getFocusX(), var1.getFocusY());
               return true;
            } else {
               return false;
            }
         }

         public boolean onScaleBegin(ScaleGestureDetector var1) {
            return true;
         }

         public void onScaleEnd(ScaleGestureDetector var1) {
         }
      });
   }

   float getActiveX(MotionEvent var1) {
      try {
         float var2 = var1.getX(this.mActivePointerIndex);
         return var2;
      } catch (Exception var4) {
         return var1.getX();
      }
   }

   float getActiveY(MotionEvent var1) {
      try {
         float var2 = var1.getY(this.mActivePointerIndex);
         return var2;
      } catch (Exception var4) {
         return var1.getY();
      }
   }

   public boolean isDragging() {
      return this.mIsDragging;
   }

   public boolean isScaling() {
      return this.mDetector.isInProgress();
   }

   public boolean onTouchEvent(MotionEvent var1) {
      this.mDetector.onTouchEvent(var1);
      int var2 = var1.getAction() & 255;
      boolean var3 = false;
      if (var2 != 0) {
         if (var2 != 1 && var2 != 3) {
            if (var2 == 6) {
               var2 = ('\uff00' & var1.getAction()) >> 8;
               if (var1.getPointerId(var2) == this.mActivePointerId) {
                  byte var10;
                  if (var2 == 0) {
                     var10 = 1;
                  } else {
                     var10 = 0;
                  }

                  this.mActivePointerId = var1.getPointerId(var10);
                  this.mLastTouchX = var1.getX(var10);
                  this.mLastTouchY = var1.getY(var10);
               }
            }
         } else {
            this.mActivePointerId = -1;
         }
      } else {
         this.mActivePointerId = var1.getPointerId(0);
      }

      var2 = this.mActivePointerId;
      if (var2 == -1) {
         var2 = 0;
      }

      this.mActivePointerIndex = var1.findPointerIndex(var2);
      var2 = var1.getAction();
      float var4;
      float var5;
      if (var2 != 0) {
         VelocityTracker var9;
         if (var2 == 1) {
            if (this.mIsDragging) {
               if (this.mVelocityTracker != null) {
                  this.mLastTouchX = this.getActiveX(var1);
                  this.mLastTouchY = this.getActiveY(var1);
                  this.mVelocityTracker.addMovement(var1);
                  this.mVelocityTracker.computeCurrentVelocity(1000);
                  var4 = this.mVelocityTracker.getXVelocity();
                  var5 = this.mVelocityTracker.getYVelocity();
                  if (Math.max(Math.abs(var4), Math.abs(var5)) >= this.mMinimumVelocity) {
                     this.mListener.onFling(this.mLastTouchX, this.mLastTouchY, -var4, -var5);
                  }
               }

               this.mIsDragging = false;
            }

            var9 = this.mVelocityTracker;
            if (var9 != null) {
               var9.recycle();
               this.mVelocityTracker = null;
            }

            this.started = false;
            return true;
         }

         if (var2 != 2) {
            if (var2 == 3) {
               var9 = this.mVelocityTracker;
               if (var9 != null) {
                  var9.recycle();
                  this.mVelocityTracker = null;
               }

               this.started = false;
               this.mIsDragging = false;
            }

            return true;
         }
      }

      VelocityTracker var6;
      if (!this.started) {
         this.mVelocityTracker = VelocityTracker.obtain();
         var6 = this.mVelocityTracker;
         if (var6 != null) {
            var6.addMovement(var1);
         }

         this.mLastTouchX = this.getActiveX(var1);
         this.mLastTouchY = this.getActiveY(var1);
         this.mIsDragging = false;
         this.started = true;
         return true;
      } else {
         float var7 = this.getActiveX(var1);
         var5 = this.getActiveY(var1);
         var4 = var7 - this.mLastTouchX;
         float var8 = var5 - this.mLastTouchY;
         if (!this.mIsDragging) {
            if ((float)Math.sqrt((double)(var4 * var4 + var8 * var8)) >= this.mTouchSlop) {
               var3 = true;
            }

            this.mIsDragging = var3;
         }

         if (this.mIsDragging) {
            this.mListener.onDrag(var4, var8);
            this.mLastTouchX = var7;
            this.mLastTouchY = var5;
            var6 = this.mVelocityTracker;
            if (var6 != null) {
               var6.addMovement(var1);
            }
         }

         return true;
      }
   }

   public void setOnGestureListener(CropGestureDetector.CropGestureListener var1) {
      this.mListener = var1;
   }

   public interface CropGestureListener {
      void onDrag(float var1, float var2);

      void onFling(float var1, float var2, float var3, float var4);

      void onScale(float var1, float var2, float var3);
   }
}
