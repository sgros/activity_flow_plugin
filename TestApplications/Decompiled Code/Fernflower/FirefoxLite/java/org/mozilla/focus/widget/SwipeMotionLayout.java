package org.mozilla.focus.widget;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import org.mozilla.focus.utils.OnSwipeListener;
import org.mozilla.focus.utils.SwipeMotionDetector;

public class SwipeMotionLayout extends ConstraintLayout {
   private SwipeMotionDetector swipeMotionDetector;

   public SwipeMotionLayout(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public SwipeMotionLayout(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public SwipeMotionLayout(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
   }

   public boolean onInterceptTouchEvent(MotionEvent var1) {
      boolean var2 = super.onInterceptTouchEvent(var1);
      if (var2 && this.swipeMotionDetector != null) {
         this.swipeMotionDetector.onTouch(this, var1);
      }

      return var2;
   }

   public void setOnSwipeListener(OnSwipeListener var1) {
      if (var1 != null) {
         this.swipeMotionDetector = new SwipeMotionDetector(this.getContext(), var1);
         this.setOnTouchListener(this.swipeMotionDetector);
      } else {
         this.swipeMotionDetector = null;
         this.setOnTouchListener((OnTouchListener)null);
      }

   }
}
