package org.telegram.ui.Cells;

import android.content.Context;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

public class StickerSetGroupInfoCell extends LinearLayout {
   private TextView addButton;
   private boolean isLast;

   public StickerSetGroupInfoCell(Context var1) {
      super(var1);
      this.setOrientation(1);
      TextView var2 = new TextView(var1);
      var2.setTextColor(Theme.getColor("chat_emojiPanelTrendingDescription"));
      var2.setTextSize(1, 14.0F);
      var2.setText(LocaleController.getString("GroupStickersInfo", 2131559616));
      this.addView(var2, LayoutHelper.createLinear(-1, -2, 51, 17, 4, 17, 0));
      this.addButton = new TextView(var1);
      this.addButton.setPadding(AndroidUtilities.dp(17.0F), 0, AndroidUtilities.dp(17.0F), 0);
      this.addButton.setGravity(17);
      this.addButton.setTextColor(Theme.getColor("featuredStickers_buttonText"));
      this.addButton.setTextSize(1, 14.0F);
      this.addButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.addButton.setBackgroundDrawable(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(4.0F), Theme.getColor("featuredStickers_addButton"), Theme.getColor("featuredStickers_addButtonPressed")));
      this.addButton.setText(LocaleController.getString("ChooseStickerSet", 2131559092).toUpperCase());
      this.addView(this.addButton, LayoutHelper.createLinear(-2, 28, 51, 17, 10, 14, 8));
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), var2);
      if (this.isLast) {
         View var3 = (View)this.getParent();
         if (var3 != null) {
            var1 = var3.getMeasuredHeight() - var3.getPaddingBottom() - var3.getPaddingTop() - AndroidUtilities.dp(24.0F);
            if (this.getMeasuredHeight() < var1) {
               this.setMeasuredDimension(this.getMeasuredWidth(), var1);
            }
         }
      }

   }

   public void setAddOnClickListener(OnClickListener var1) {
      this.addButton.setOnClickListener(var1);
   }

   public void setIsLast(boolean var1) {
      this.isLast = var1;
      this.requestLayout();
   }
}
