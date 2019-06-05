package org.mozilla.focus.firstrun;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;

public class FirstrunCardView extends CardView {
   private int maxHeight;
   private int maxWidth;

   public FirstrunCardView(Context var1) {
      super(var1);
      this.init();
   }

   public FirstrunCardView(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.init();
   }

   public FirstrunCardView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.init();
   }

   private void init() {
      Resources var1 = this.getResources();
      this.maxWidth = var1.getDimensionPixelSize(2131165344);
      this.maxHeight = var1.getDimensionPixelSize(2131165343);
   }

   protected void onMeasure(int var1, int var2) {
      int var3 = MeasureSpec.getSize(var1);
      var1 = MeasureSpec.getSize(var2);
      var2 = Math.min(var3, this.maxWidth);
      var1 = Math.min(var1, this.maxHeight);
      super.onMeasure(MeasureSpec.makeMeasureSpec(var2, 1073741824), MeasureSpec.makeMeasureSpec(var1, 1073741824));
   }
}
