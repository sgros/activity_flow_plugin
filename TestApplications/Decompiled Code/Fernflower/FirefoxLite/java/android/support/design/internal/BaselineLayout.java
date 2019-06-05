package android.support.design.internal;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class BaselineLayout extends ViewGroup {
   private int baseline = -1;

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
      return this.baseline;
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
            if (this.baseline != -1 && var10.getBaseline() != -1) {
               var5 = this.baseline + var9 - var10.getBaseline();
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
      int var6 = -1;
      int var7 = 0;
      int var8 = 0;

      int var9;
      int var13;
      for(var9 = 0; var4 < var3; ++var4) {
         View var10 = this.getChildAt(var4);
         if (var10.getVisibility() != 8) {
            this.measureChild(var10, var1, var2);
            int var11 = var10.getBaseline();
            int var12 = var5;
            var13 = var6;
            if (var11 != -1) {
               var12 = Math.max(var5, var11);
               var13 = Math.max(var6, var10.getMeasuredHeight() - var11);
            }

            var8 = Math.max(var8, var10.getMeasuredWidth());
            var7 = Math.max(var7, var10.getMeasuredHeight());
            var9 = View.combineMeasuredStates(var9, var10.getMeasuredState());
            var6 = var13;
            var5 = var12;
         }
      }

      var13 = var7;
      if (var5 != -1) {
         var13 = Math.max(var7, Math.max(var6, this.getPaddingBottom()) + var5);
         this.baseline = var5;
      }

      var7 = Math.max(var13, this.getSuggestedMinimumHeight());
      this.setMeasuredDimension(View.resolveSizeAndState(Math.max(var8, this.getSuggestedMinimumWidth()), var1, var9), View.resolveSizeAndState(var7, var2, var9 << 16));
   }
}
