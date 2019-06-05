package org.mozilla.focus.tabs.tabtray;

import android.content.Context;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class InterceptBehavior extends BottomSheetBehavior {
   private boolean intercept = true;

   public InterceptBehavior(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   public boolean onInterceptTouchEvent(CoordinatorLayout var1, View var2, MotionEvent var3) {
      boolean var4;
      if (!this.intercept && !super.onInterceptTouchEvent(var1, var2, var3)) {
         var4 = false;
      } else {
         var4 = true;
      }

      return var4;
   }

   public boolean onTouchEvent(CoordinatorLayout var1, View var2, MotionEvent var3) {
      boolean var4;
      if (!this.intercept && !super.onTouchEvent(var1, var2, var3)) {
         var4 = false;
      } else {
         var4 = true;
      }

      return var4;
   }

   void setIntercept(boolean var1) {
      this.intercept = var1;
   }
}
