package android.support.design.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;

abstract class HeaderBehavior extends ViewOffsetBehavior {
   private int activePointerId = -1;
   private Runnable flingRunnable;
   private boolean isBeingDragged;
   private int lastMotionY;
   OverScroller scroller;
   private int touchSlop = -1;
   private VelocityTracker velocityTracker;

   public HeaderBehavior() {
   }

   public HeaderBehavior(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   private void ensureVelocityTracker() {
      if (this.velocityTracker == null) {
         this.velocityTracker = VelocityTracker.obtain();
      }

   }

   boolean canDragView(View var1) {
      return false;
   }

   final boolean fling(CoordinatorLayout var1, View var2, int var3, int var4, float var5) {
      if (this.flingRunnable != null) {
         var2.removeCallbacks(this.flingRunnable);
         this.flingRunnable = null;
      }

      if (this.scroller == null) {
         this.scroller = new OverScroller(var2.getContext());
      }

      this.scroller.fling(0, this.getTopAndBottomOffset(), 0, Math.round(var5), 0, 0, var3, var4);
      if (this.scroller.computeScrollOffset()) {
         this.flingRunnable = new HeaderBehavior.FlingRunnable(var1, var2);
         ViewCompat.postOnAnimation(var2, this.flingRunnable);
         return true;
      } else {
         this.onFlingFinished(var1, var2);
         return false;
      }
   }

   int getMaxDragOffset(View var1) {
      return -var1.getHeight();
   }

   int getScrollRangeForDragFling(View var1) {
      return var1.getHeight();
   }

   int getTopBottomOffsetForScrollingSibling() {
      return this.getTopAndBottomOffset();
   }

   void onFlingFinished(CoordinatorLayout var1, View var2) {
   }

   public boolean onInterceptTouchEvent(CoordinatorLayout var1, View var2, MotionEvent var3) {
      if (this.touchSlop < 0) {
         this.touchSlop = ViewConfiguration.get(var1.getContext()).getScaledTouchSlop();
      }

      if (var3.getAction() == 2 && this.isBeingDragged) {
         return true;
      } else {
         int var4;
         switch(var3.getActionMasked()) {
         case 0:
            this.isBeingDragged = false;
            var4 = (int)var3.getX();
            int var5 = (int)var3.getY();
            if (this.canDragView(var2) && var1.isPointInChildBounds(var2, var4, var5)) {
               this.lastMotionY = var5;
               this.activePointerId = var3.getPointerId(0);
               this.ensureVelocityTracker();
            }
            break;
         case 1:
         case 3:
            this.isBeingDragged = false;
            this.activePointerId = -1;
            if (this.velocityTracker != null) {
               this.velocityTracker.recycle();
               this.velocityTracker = null;
            }
            break;
         case 2:
            var4 = this.activePointerId;
            if (var4 != -1) {
               var4 = var3.findPointerIndex(var4);
               if (var4 != -1) {
                  var4 = (int)var3.getY(var4);
                  if (Math.abs(var4 - this.lastMotionY) > this.touchSlop) {
                     this.isBeingDragged = true;
                     this.lastMotionY = var4;
                  }
               }
            }
         }

         if (this.velocityTracker != null) {
            this.velocityTracker.addMovement(var3);
         }

         return this.isBeingDragged;
      }
   }

   public boolean onTouchEvent(CoordinatorLayout var1, View var2, MotionEvent var3) {
      if (this.touchSlop < 0) {
         this.touchSlop = ViewConfiguration.get(var1.getContext()).getScaledTouchSlop();
      }

      int var4;
      int var6;
      switch(var3.getActionMasked()) {
      case 0:
         var4 = (int)var3.getX();
         var6 = (int)var3.getY();
         if (!var1.isPointInChildBounds(var2, var4, var6) || !this.canDragView(var2)) {
            return false;
         }

         this.lastMotionY = var6;
         this.activePointerId = var3.getPointerId(0);
         this.ensureVelocityTracker();
         break;
      case 1:
         if (this.velocityTracker != null) {
            this.velocityTracker.addMovement(var3);
            this.velocityTracker.computeCurrentVelocity(1000);
            float var7 = this.velocityTracker.getYVelocity(this.activePointerId);
            this.fling(var1, var2, -this.getScrollRangeForDragFling(var2), 0, var7);
         }
      case 3:
         this.isBeingDragged = false;
         this.activePointerId = -1;
         if (this.velocityTracker != null) {
            this.velocityTracker.recycle();
            this.velocityTracker = null;
         }
         break;
      case 2:
         var4 = var3.findPointerIndex(this.activePointerId);
         if (var4 == -1) {
            return false;
         }

         int var5 = (int)var3.getY(var4);
         var6 = this.lastMotionY - var5;
         var4 = var6;
         if (!this.isBeingDragged) {
            var4 = var6;
            if (Math.abs(var6) > this.touchSlop) {
               this.isBeingDragged = true;
               if (var6 > 0) {
                  var4 = var6 - this.touchSlop;
               } else {
                  var4 = var6 + this.touchSlop;
               }
            }
         }

         if (this.isBeingDragged) {
            this.lastMotionY = var5;
            this.scroll(var1, var2, var4, this.getMaxDragOffset(var2), 0);
         }
      }

      if (this.velocityTracker != null) {
         this.velocityTracker.addMovement(var3);
      }

      return true;
   }

   final int scroll(CoordinatorLayout var1, View var2, int var3, int var4, int var5) {
      return this.setHeaderTopBottomOffset(var1, var2, this.getTopBottomOffsetForScrollingSibling() - var3, var4, var5);
   }

   int setHeaderTopBottomOffset(CoordinatorLayout var1, View var2, int var3) {
      return this.setHeaderTopBottomOffset(var1, var2, var3, Integer.MIN_VALUE, Integer.MAX_VALUE);
   }

   int setHeaderTopBottomOffset(CoordinatorLayout var1, View var2, int var3, int var4, int var5) {
      int var6 = this.getTopAndBottomOffset();
      if (var4 != 0 && var6 >= var4 && var6 <= var5) {
         var3 = android.support.v4.math.MathUtils.clamp(var3, var4, var5);
         if (var6 != var3) {
            this.setTopAndBottomOffset(var3);
            var3 = var6 - var3;
            return var3;
         }
      }

      var3 = 0;
      return var3;
   }

   private class FlingRunnable implements Runnable {
      private final View layout;
      private final CoordinatorLayout parent;

      FlingRunnable(CoordinatorLayout var2, View var3) {
         this.parent = var2;
         this.layout = var3;
      }

      public void run() {
         if (this.layout != null && HeaderBehavior.this.scroller != null) {
            if (HeaderBehavior.this.scroller.computeScrollOffset()) {
               HeaderBehavior.this.setHeaderTopBottomOffset(this.parent, this.layout, HeaderBehavior.this.scroller.getCurrY());
               ViewCompat.postOnAnimation(this.layout, this);
            } else {
               HeaderBehavior.this.onFlingFinished(this.parent, this.layout);
            }
         }

      }
   }
}
