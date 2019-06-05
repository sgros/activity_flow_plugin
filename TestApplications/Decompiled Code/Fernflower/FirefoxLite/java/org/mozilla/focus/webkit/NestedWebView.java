package org.mozilla.focus.webkit;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

public class NestedWebView extends WebView implements NestedScrollingChild {
   private NestedScrollingChildHelper mChildHelper = new NestedScrollingChildHelper(this);
   private int mLastY;
   private int mNestedOffsetY;
   private final int[] mScrollConsumed = new int[2];
   private final int[] mScrollOffset = new int[2];

   public NestedWebView(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.setNestedScrollingEnabled(true);
   }

   public boolean dispatchNestedFling(float var1, float var2, boolean var3) {
      return this.mChildHelper.dispatchNestedFling(var1, var2, var3);
   }

   public boolean dispatchNestedPreFling(float var1, float var2) {
      return this.mChildHelper.dispatchNestedPreFling(var1, var2);
   }

   public boolean dispatchNestedPreScroll(int var1, int var2, int[] var3, int[] var4) {
      return this.mChildHelper.dispatchNestedPreScroll(var1, var2, var3, var4);
   }

   public boolean dispatchNestedScroll(int var1, int var2, int var3, int var4, int[] var5) {
      return this.mChildHelper.dispatchNestedScroll(var1, var2, var3, var4, var5);
   }

   public boolean hasNestedScrollingParent() {
      return this.mChildHelper.hasNestedScrollingParent();
   }

   public boolean isNestedScrollingEnabled() {
      return this.mChildHelper.isNestedScrollingEnabled();
   }

   public boolean onTouchEvent(MotionEvent var1) {
      var1 = MotionEvent.obtain(var1);
      int var2 = MotionEventCompat.getActionMasked(var1);
      boolean var3 = false;
      if (var2 == 0) {
         this.mNestedOffsetY = 0;
      }

      if (var1.getPointerCount() > 1) {
         return super.onTouchEvent(var1);
      } else {
         int var4 = (int)var1.getY();
         var1.offsetLocation(0.0F, (float)this.mNestedOffsetY);
         switch(var2) {
         case 0:
            var3 = super.onTouchEvent(var1);
            this.mLastY = var4;
            this.startNestedScroll(2);
            break;
         case 1:
         case 3:
            var3 = super.onTouchEvent(var1);
            this.stopNestedScroll();
            break;
         case 2:
            int var5 = this.mLastY - var4;
            var2 = var5;
            if (this.dispatchNestedPreScroll(0, var5, this.mScrollConsumed, this.mScrollOffset)) {
               var2 = var5 - this.mScrollConsumed[1];
               this.mLastY = var4 - this.mScrollOffset[1];
               var1.offsetLocation(0.0F, (float)(-this.mScrollOffset[1]));
               this.mNestedOffsetY += this.mScrollOffset[1];
            }

            boolean var6 = super.onTouchEvent(var1);
            var3 = var6;
            if (this.dispatchNestedScroll(0, this.mScrollOffset[1], 0, var2, this.mScrollOffset)) {
               var1.offsetLocation(0.0F, (float)this.mScrollOffset[1]);
               this.mNestedOffsetY += this.mScrollOffset[1];
               this.mLastY -= this.mScrollOffset[1];
               var3 = var6;
            }
         }

         return var3;
      }
   }

   public void setNestedScrollingEnabled(boolean var1) {
      this.mChildHelper.setNestedScrollingEnabled(var1);
   }

   public boolean startNestedScroll(int var1) {
      return this.mChildHelper.startNestedScroll(var1);
   }

   public void stopNestedScroll() {
      this.mChildHelper.stopNestedScroll();
   }
}
