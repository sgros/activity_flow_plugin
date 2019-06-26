package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

public class DrawerAddCell extends FrameLayout {
   private TextView textView;

   public DrawerAddCell(Context var1) {
      super(var1);
      this.textView = new TextView(var1);
      this.textView.setTextColor(Theme.getColor("chats_menuItemText"));
      this.textView.setTextSize(1, 15.0F);
      this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.textView.setLines(1);
      this.textView.setMaxLines(1);
      this.textView.setSingleLine(true);
      this.textView.setGravity(19);
      this.textView.setCompoundDrawablePadding(AndroidUtilities.dp(34.0F));
      this.addView(this.textView, LayoutHelper.createFrame(-1, -1.0F, 51, 23.0F, 0.0F, 16.0F, 0.0F));
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.textView.setTextColor(Theme.getColor("chats_menuItemText"));
      this.textView.setText(LocaleController.getString("AddAccount", 2131558556));
      Drawable var1 = this.getResources().getDrawable(2131165269);
      if (var1 != null) {
         var1.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chats_menuItemIcon"), Mode.MULTIPLY));
      }

      this.textView.setCompoundDrawablesWithIntrinsicBounds(var1, (Drawable)null, (Drawable)null, (Drawable)null);
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0F), 1073741824));
   }
}
