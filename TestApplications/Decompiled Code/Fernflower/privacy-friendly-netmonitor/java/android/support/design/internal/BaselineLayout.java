package android.support.design.internal;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class BaselineLayout extends ViewGroup {
   private int mBaseline = -1;

   public BaselineLayout(Context var1) {
      super(var1, (AttributeSet)null, 0);
   }

   public BaselineLayout(Context var1, AttributeSet var2) {
      super(var1, var2, 0);
   }

   public BaselineLayout(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
   }

   public int getBaseline() {
      return this.mBaseline;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      int var6 = this.getChildCount();
      int var7 = this.getPaddingLeft();
      int var8 = this.getPaddingRight();
      int var9 = this.getPaddingTop();

      for(var3 = 0; var3 < var6; ++var3) {
         View var10 = this.getChildAt(var3);
         if (var10.getVisibility() != 8) {
            int var11 = var10.getMeasuredWidth();
            int var12 = var10.getMeasuredHeight();
            int var13 = (var4 - var2 - var8 - var7 - var11) / 2 + var7;
            if (this.mBaseline != -1 && var10.getBaseline() != -1) {
               var5 = this.mBaseline + var9 - var10.getBaseline();
            } else {
               var5 = var9;
            }

            var10.layout(var13, var5, var11 + var13, var12 + var5);
         }
      }

   }

   protected void onMeasure(int var1, int var2) {
      int var3 = this.getChildCount();
      int var4 = 0;
      int var5 = -1;
      int var6 = var5;
      byte var7 = 0;
      int var9 = var7;
      int var10 = var7;

      int var8;
      int var14;
      for(var8 = var7; var4 < var3; var5 = var14) {
         View var11 = this.getChildAt(var4);
         if (var11.getVisibility() == 8) {
            var14 = var5;
         } else {
            this.measureChild(var11, var1, var2);
            int var12 = var11.getBaseline();
            var14 = var5;
            int var13 = var6;
            if (var12 != -1) {
               var14 = Math.max(var5, var12);
               var13 = Math.max(var6, var11.getMeasuredHeight() - var12);
            }

            var10 = Math.max(var10, var11.getMeasuredWidth());
            var8 = Math.max(var8, var11.getMeasuredHeight());
            var9 = View.combineMeasuredStates(var9, var11.getMeasuredState());
            var6 = var13;
         }

         ++var4;
      }

      var14 = var8;
      if (var5 != -1) {
         var14 = Math.max(var8, Math.max(var6, this.getPaddingBottom()) + var5);
         this.mBaseline = var5;
      }

      var8 = Math.max(var14, this.getSuggestedMinimumHeight());
      this.setMeasuredDimension(View.resolveSizeAndState(Math.max(var10, this.getSuggestedMinimumWidth()), var1, var9), View.resolveSizeAndState(var8, var2, var9 << 16));
   }
}
