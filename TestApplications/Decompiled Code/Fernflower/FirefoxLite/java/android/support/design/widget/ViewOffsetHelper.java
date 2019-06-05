package android.support.design.widget;

import android.support.v4.view.ViewCompat;
import android.view.View;

class ViewOffsetHelper {
   private int layoutLeft;
   private int layoutTop;
   private int offsetLeft;
   private int offsetTop;
   private final View view;

   public ViewOffsetHelper(View var1) {
      this.view = var1;
   }

   private void updateOffsets() {
      ViewCompat.offsetTopAndBottom(this.view, this.offsetTop - (this.view.getTop() - this.layoutTop));
      ViewCompat.offsetLeftAndRight(this.view, this.offsetLeft - (this.view.getLeft() - this.layoutLeft));
   }

   public int getTopAndBottomOffset() {
      return this.offsetTop;
   }

   public void onViewLayout() {
      this.layoutTop = this.view.getTop();
      this.layoutLeft = this.view.getLeft();
      this.updateOffsets();
   }

   public boolean setLeftAndRightOffset(int var1) {
      if (this.offsetLeft != var1) {
         this.offsetLeft = var1;
         this.updateOffsets();
         return true;
      } else {
         return false;
      }
   }

   public boolean setTopAndBottomOffset(int var1) {
      if (this.offsetTop != var1) {
         this.offsetTop = var1;
         this.updateOffsets();
         return true;
      } else {
         return false;
      }
   }
}
