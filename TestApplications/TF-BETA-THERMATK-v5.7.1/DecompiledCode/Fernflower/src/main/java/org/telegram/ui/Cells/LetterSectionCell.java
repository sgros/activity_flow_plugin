package org.telegram.ui.Cells;

import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

public class LetterSectionCell extends FrameLayout {
   private TextView textView;

   public LetterSectionCell(Context var1) {
      super(var1);
      this.setLayoutParams(new LayoutParams(AndroidUtilities.dp(54.0F), AndroidUtilities.dp(64.0F)));
      this.textView = new TextView(this.getContext());
      this.textView.setTextSize(1, 22.0F);
      this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText4"));
      this.textView.setGravity(17);
      this.addView(this.textView, LayoutHelper.createFrame(-1, -1.0F));
   }

   public void setCellHeight(int var1) {
      this.setLayoutParams(new LayoutParams(AndroidUtilities.dp(54.0F), var1));
   }

   public void setLetter(String var1) {
      this.textView.setText(var1.toUpperCase());
   }
}
