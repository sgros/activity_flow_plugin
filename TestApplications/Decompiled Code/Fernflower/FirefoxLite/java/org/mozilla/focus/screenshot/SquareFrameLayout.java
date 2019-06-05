package org.mozilla.focus.screenshot;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;

public class SquareFrameLayout extends FrameLayout {
   public SquareFrameLayout(Context var1) {
      super(var1);
   }

   public SquareFrameLayout(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   public SquareFrameLayout(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
   }

   protected void onMeasure(int var1, int var2) {
      int var3 = MeasureSpec.getMode(var1);
      var1 = MeasureSpec.getSize(var1);
      int var4 = MeasureSpec.getMode(var2);
      var2 = MeasureSpec.getSize(var2);
      if ((var3 != 1073741824 || var1 <= 0) && (var4 == 1073741824 && var2 > 0 || var1 >= var2)) {
         var1 = var2;
      }

      var1 = MeasureSpec.makeMeasureSpec(var1, 1073741824);
      super.onMeasure(var1, var1);
   }
}
