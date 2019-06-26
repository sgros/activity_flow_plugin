package org.telegram.ui.Cells;

import android.content.Context;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

public class TextInfoCell extends FrameLayout {
   private TextView textView;

   public TextInfoCell(Context var1) {
      super(var1);
      this.textView = new TextView(var1);
      this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText5"));
      this.textView.setTextSize(1, 13.0F);
      this.textView.setGravity(17);
      this.textView.setPadding(0, AndroidUtilities.dp(19.0F), 0, AndroidUtilities.dp(19.0F));
      this.addView(this.textView, LayoutHelper.createFrame(-2, -2.0F, 17, 17.0F, 0.0F, 17.0F, 0.0F));
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(0, 0));
   }

   public void setText(String var1) {
      this.textView.setText(var1);
   }
}
