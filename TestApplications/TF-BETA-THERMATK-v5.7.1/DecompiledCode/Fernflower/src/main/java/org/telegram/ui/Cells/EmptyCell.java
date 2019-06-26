package org.telegram.ui.Cells;

import android.content.Context;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;

public class EmptyCell extends FrameLayout {
   int cellHeight;

   public EmptyCell(Context var1) {
      this(var1, 8);
   }

   public EmptyCell(Context var1, int var2) {
      super(var1);
      this.cellHeight = var2;
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(this.cellHeight, 1073741824));
   }

   public void setHeight(int var1) {
      this.cellHeight = var1;
      this.requestLayout();
   }
}
