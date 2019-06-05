package org.mozilla.rocket.content.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;

public class SquaredLayout extends FrameLayout {
   public SquaredLayout(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   public SquaredLayout(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(var1, var2);
      var1 = MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), 1073741824);
      super.onMeasure(var1, var1);
   }
}
