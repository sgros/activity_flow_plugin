package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.view.View.MeasureSpec;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;

public class DividerCell extends View {
   public DividerCell(Context var1) {
      super(var1);
      this.setPadding(0, AndroidUtilities.dp(8.0F), 0, AndroidUtilities.dp(8.0F));
   }

   protected void onDraw(Canvas var1) {
      var1.drawLine((float)this.getPaddingLeft(), (float)this.getPaddingTop(), (float)(this.getWidth() - this.getPaddingRight()), (float)this.getPaddingTop(), Theme.dividerPaint);
   }

   protected void onMeasure(int var1, int var2) {
      this.setMeasuredDimension(MeasureSpec.getSize(var1), this.getPaddingTop() + this.getPaddingBottom() + 1);
   }
}
