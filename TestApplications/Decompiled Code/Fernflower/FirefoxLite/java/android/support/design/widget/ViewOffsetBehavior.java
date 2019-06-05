package android.support.design.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

class ViewOffsetBehavior extends CoordinatorLayout.Behavior {
   private int tempLeftRightOffset = 0;
   private int tempTopBottomOffset = 0;
   private ViewOffsetHelper viewOffsetHelper;

   public ViewOffsetBehavior() {
   }

   public ViewOffsetBehavior(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   public int getTopAndBottomOffset() {
      int var1;
      if (this.viewOffsetHelper != null) {
         var1 = this.viewOffsetHelper.getTopAndBottomOffset();
      } else {
         var1 = 0;
      }

      return var1;
   }

   protected void layoutChild(CoordinatorLayout var1, View var2, int var3) {
      var1.onLayoutChild(var2, var3);
   }

   public boolean onLayoutChild(CoordinatorLayout var1, View var2, int var3) {
      this.layoutChild(var1, var2, var3);
      if (this.viewOffsetHelper == null) {
         this.viewOffsetHelper = new ViewOffsetHelper(var2);
      }

      this.viewOffsetHelper.onViewLayout();
      if (this.tempTopBottomOffset != 0) {
         this.viewOffsetHelper.setTopAndBottomOffset(this.tempTopBottomOffset);
         this.tempTopBottomOffset = 0;
      }

      if (this.tempLeftRightOffset != 0) {
         this.viewOffsetHelper.setLeftAndRightOffset(this.tempLeftRightOffset);
         this.tempLeftRightOffset = 0;
      }

      return true;
   }

   public boolean setTopAndBottomOffset(int var1) {
      if (this.viewOffsetHelper != null) {
         return this.viewOffsetHelper.setTopAndBottomOffset(var1);
      } else {
         this.tempTopBottomOffset = var1;
         return false;
      }
   }
}
