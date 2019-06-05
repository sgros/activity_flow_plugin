package android.support.design.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import java.util.List;

abstract class HeaderScrollingViewBehavior extends ViewOffsetBehavior {
   private int overlayTop;
   final Rect tempRect1 = new Rect();
   final Rect tempRect2 = new Rect();
   private int verticalLayoutGap = 0;

   public HeaderScrollingViewBehavior() {
   }

   public HeaderScrollingViewBehavior(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   private static int resolveGravity(int var0) {
      int var1 = var0;
      if (var0 == 0) {
         var1 = 8388659;
      }

      return var1;
   }

   abstract View findFirstDependency(List var1);

   final int getOverlapPixelsForOffset(View var1) {
      int var2 = this.overlayTop;
      int var3 = 0;
      if (var2 != 0) {
         var3 = android.support.v4.math.MathUtils.clamp((int)(this.getOverlapRatioForOffset(var1) * (float)this.overlayTop), 0, this.overlayTop);
      }

      return var3;
   }

   float getOverlapRatioForOffset(View var1) {
      return 1.0F;
   }

   public final int getOverlayTop() {
      return this.overlayTop;
   }

   int getScrollRange(View var1) {
      return var1.getMeasuredHeight();
   }

   final int getVerticalLayoutGap() {
      return this.verticalLayoutGap;
   }

   protected void layoutChild(CoordinatorLayout var1, View var2, int var3) {
      View var4 = this.findFirstDependency(var1.getDependencies(var2));
      if (var4 != null) {
         CoordinatorLayout.LayoutParams var5 = (CoordinatorLayout.LayoutParams)var2.getLayoutParams();
         Rect var6 = this.tempRect1;
         var6.set(var1.getPaddingLeft() + var5.leftMargin, var4.getBottom() + var5.topMargin, var1.getWidth() - var1.getPaddingRight() - var5.rightMargin, var1.getHeight() + var4.getBottom() - var1.getPaddingBottom() - var5.bottomMargin);
         WindowInsetsCompat var7 = var1.getLastWindowInsets();
         if (var7 != null && ViewCompat.getFitsSystemWindows(var1) && !ViewCompat.getFitsSystemWindows(var2)) {
            var6.left += var7.getSystemWindowInsetLeft();
            var6.right -= var7.getSystemWindowInsetRight();
         }

         Rect var8 = this.tempRect2;
         GravityCompat.apply(resolveGravity(var5.gravity), var2.getMeasuredWidth(), var2.getMeasuredHeight(), var6, var8, var3);
         var3 = this.getOverlapPixelsForOffset(var4);
         var2.layout(var8.left, var8.top - var3, var8.right, var8.bottom - var3);
         this.verticalLayoutGap = var8.top - var4.getBottom();
      } else {
         super.layoutChild(var1, var2, var3);
         this.verticalLayoutGap = 0;
      }

   }

   public boolean onMeasureChild(CoordinatorLayout var1, View var2, int var3, int var4, int var5, int var6) {
      int var7 = var2.getLayoutParams().height;
      if (var7 == -1 || var7 == -2) {
         View var8 = this.findFirstDependency(var1.getDependencies(var2));
         if (var8 != null) {
            if (ViewCompat.getFitsSystemWindows(var8) && !ViewCompat.getFitsSystemWindows(var2)) {
               ViewCompat.setFitsSystemWindows(var2, true);
               if (ViewCompat.getFitsSystemWindows(var2)) {
                  var2.requestLayout();
                  return true;
               }
            }

            int var9 = MeasureSpec.getSize(var5);
            var5 = var9;
            if (var9 == 0) {
               var5 = var1.getHeight();
            }

            int var10 = var8.getMeasuredHeight();
            int var11 = this.getScrollRange(var8);
            if (var7 == -1) {
               var9 = 1073741824;
            } else {
               var9 = Integer.MIN_VALUE;
            }

            var1.onMeasureChild(var2, var3, var4, MeasureSpec.makeMeasureSpec(var5 - var10 + var11, var9), var6);
            return true;
         }
      }

      return false;
   }

   public final void setOverlayTop(int var1) {
      this.overlayTop = var1;
   }
}
