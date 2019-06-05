package android.support.v4.view;

import android.view.View;
import android.view.ViewGroup;

public class NestedScrollingParentHelper {
   private int mNestedScrollAxes;
   private final ViewGroup mViewGroup;

   public NestedScrollingParentHelper(ViewGroup var1) {
      this.mViewGroup = var1;
   }

   public int getNestedScrollAxes() {
      return this.mNestedScrollAxes;
   }

   public void onNestedScrollAccepted(View var1, View var2, int var3) {
      this.onNestedScrollAccepted(var1, var2, var3, 0);
   }

   public void onNestedScrollAccepted(View var1, View var2, int var3, int var4) {
      this.mNestedScrollAxes = var3;
   }

   public void onStopNestedScroll(View var1, int var2) {
      this.mNestedScrollAxes = 0;
   }
}
