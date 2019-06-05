package org.mozilla.focus.utils;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;

public class SwipeMotionDetector implements OnTouchListener {
   private GestureDetector gestureDetector;
   private OnSwipeListener onSwipeListener;

   public SwipeMotionDetector(Context var1, OnSwipeListener var2) {
      this.gestureDetector = new GestureDetector(var1, new SwipeMotionDetector.GestureListener());
      this.onSwipeListener = var2;
   }

   public boolean onTouch(View var1, MotionEvent var2) {
      return this.gestureDetector.onTouchEvent(var2);
   }

   private final class GestureListener extends SimpleOnGestureListener {
      private GestureListener() {
      }

      // $FF: synthetic method
      GestureListener(Object var2) {
         this();
      }

      public boolean onDoubleTap(MotionEvent var1) {
         return SwipeMotionDetector.this.onSwipeListener.onDoubleTap();
      }

      public boolean onDown(MotionEvent var1) {
         return true;
      }

      public boolean onFling(MotionEvent var1, MotionEvent var2, float var3, float var4) {
         float var5 = var2.getY() - var1.getY();
         float var6 = var2.getX() - var1.getX();
         if (Math.abs(var6) > Math.abs(var5)) {
            if (Math.abs(var6) > 100.0F && Math.abs(var3) > 100.0F) {
               if (var6 > 0.0F) {
                  if (SwipeMotionDetector.this.onSwipeListener != null) {
                     SwipeMotionDetector.this.onSwipeListener.onSwipeRight();
                  }
               } else if (SwipeMotionDetector.this.onSwipeListener != null) {
                  SwipeMotionDetector.this.onSwipeListener.onSwipeLeft();
               }
            }
         } else if (Math.abs(var5) > 100.0F && Math.abs(var4) > 100.0F) {
            if (var5 > 0.0F) {
               if (SwipeMotionDetector.this.onSwipeListener != null) {
                  SwipeMotionDetector.this.onSwipeListener.onSwipeDown();
               }
            } else if (SwipeMotionDetector.this.onSwipeListener != null) {
               SwipeMotionDetector.this.onSwipeListener.onSwipeUp();
            }
         }

         return false;
      }

      public void onLongPress(MotionEvent var1) {
         SwipeMotionDetector.this.onSwipeListener.onLongPress();
      }

      public boolean onSingleTapConfirmed(MotionEvent var1) {
         return SwipeMotionDetector.this.onSwipeListener.onSingleTapConfirmed();
      }
   }
}
