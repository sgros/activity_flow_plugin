package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View.MeasureSpec;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;

public class HashtagSearchCell extends TextView {
   private boolean needDivider;

   public HashtagSearchCell(Context var1) {
      super(var1);
      this.setGravity(16);
      this.setPadding(AndroidUtilities.dp(16.0F), 0, AndroidUtilities.dp(16.0F), 0);
      this.setTextSize(1, 17.0F);
      this.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
   }

   protected void onDraw(Canvas var1) {
      super.onDraw(var1);
      if (this.needDivider) {
         var1.drawLine(0.0F, (float)(this.getHeight() - 1), (float)this.getWidth(), (float)(this.getHeight() - 1), Theme.dividerPaint);
      }

   }

   protected void onMeasure(int var1, int var2) {
      this.setMeasuredDimension(MeasureSpec.getSize(var1), AndroidUtilities.dp(48.0F) + 1);
   }

   public void setNeedDivider(boolean var1) {
      this.needDivider = var1;
   }
}
