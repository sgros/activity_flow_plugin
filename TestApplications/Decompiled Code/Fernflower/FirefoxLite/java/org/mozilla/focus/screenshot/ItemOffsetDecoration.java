package org.mozilla.focus.screenshot;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {
   private int spacing;
   private int spanCount;

   public ItemOffsetDecoration(int var1, int var2) {
      this.spanCount = var1;
      this.spacing = var2;
   }

   public void getItemOffsets(Rect var1, View var2, RecyclerView var3, RecyclerView.State var4) {
      super.getItemOffsets(var1, var2, var3, var4);
      int var5 = var3.getChildAdapterPosition(var2);
      if (var5 >= 0 && var3.getAdapter().getItemViewType(var5) == 1) {
         var5 = ((ScreenshotItemAdapter)var3.getAdapter()).getAdjustPosition(var5);
         int var6 = var5 % this.spanCount;
         var1.left = this.spacing - this.spacing * var6 / this.spanCount;
         var1.right = (var6 + 1) * this.spacing / this.spanCount;
         if (var5 < this.spanCount) {
            var1.top = this.spacing;
         }

         var1.bottom = this.spacing;
      }

   }
}
