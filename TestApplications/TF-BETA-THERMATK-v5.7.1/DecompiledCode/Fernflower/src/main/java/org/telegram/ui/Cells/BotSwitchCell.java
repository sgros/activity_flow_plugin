package org.telegram.ui.Cells;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

public class BotSwitchCell extends FrameLayout {
   private TextView textView;

   public BotSwitchCell(Context var1) {
      super(var1);
      this.textView = new TextView(var1);
      this.textView.setTextSize(1, 15.0F);
      this.textView.setTextColor(Theme.getColor("chat_botSwitchToInlineText"));
      this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.textView.setSingleLine(true);
      this.textView.setEllipsize(TruncateAt.END);
      this.textView.setMaxLines(1);
      TextView var5 = this.textView;
      boolean var2 = LocaleController.isRTL;
      byte var3 = 5;
      byte var4;
      if (var2) {
         var4 = 5;
      } else {
         var4 = 3;
      }

      var5.setGravity(var4);
      var5 = this.textView;
      if (LocaleController.isRTL) {
         var4 = var3;
      } else {
         var4 = 3;
      }

      this.addView(var5, LayoutHelper.createFrame(-2, -2.0F, var4 | 16, 14.0F, 0.0F, 14.0F, 0.0F));
   }

   public TextView getTextView() {
      return this.textView;
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(36.0F), 1073741824));
   }

   public void setText(String var1) {
      this.textView.setText(var1);
   }
}
