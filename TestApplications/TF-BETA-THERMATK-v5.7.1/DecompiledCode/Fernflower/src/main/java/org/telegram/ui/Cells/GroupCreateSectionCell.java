package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
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

public class GroupCreateSectionCell extends FrameLayout {
   private Drawable drawable;
   private TextView textView;

   public GroupCreateSectionCell(Context var1) {
      super(var1);
      this.setBackgroundColor(Theme.getColor("graySection"));
      this.drawable = this.getResources().getDrawable(2131165817);
      this.drawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor("groupcreate_sectionShadow"), Mode.MULTIPLY));
      this.textView = new TextView(this.getContext());
      this.textView.setTextSize(1, 14.0F);
      this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.textView.setTextColor(Theme.getColor("groupcreate_sectionText"));
      TextView var5 = this.textView;
      boolean var2 = LocaleController.isRTL;
      byte var3 = 5;
      byte var4;
      if (var2) {
         var4 = 5;
      } else {
         var4 = 3;
      }

      var5.setGravity(var4 | 16);
      var5 = this.textView;
      if (LocaleController.isRTL) {
         var4 = var3;
      } else {
         var4 = 3;
      }

      this.addView(var5, LayoutHelper.createFrame(-1, -1.0F, var4 | 48, 16.0F, 0.0F, 16.0F, 0.0F));
   }

   protected void onDraw(Canvas var1) {
      this.drawable.setBounds(0, this.getMeasuredHeight() - AndroidUtilities.dp(3.0F), this.getMeasuredWidth(), this.getMeasuredHeight());
      this.drawable.draw(var1);
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(40.0F), 1073741824));
   }

   public void setText(String var1) {
      this.textView.setText(var1);
   }
}
