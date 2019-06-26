package org.telegram.ui.Cells;

import android.content.Context;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

public class GraySectionCell extends FrameLayout {
   private TextView righTextView;
   private TextView textView;

   public GraySectionCell(Context var1) {
      super(var1);
      this.setBackgroundColor(Theme.getColor("graySection"));
      this.textView = new TextView(this.getContext());
      this.textView.setTextSize(1, 14.0F);
      this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.textView.setTextColor(Theme.getColor("key_graySectionText"));
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
         var4 = 5;
      } else {
         var4 = 3;
      }

      this.addView(var5, LayoutHelper.createFrame(-1, -1.0F, var4 | 48, 16.0F, 0.0F, 16.0F, 0.0F));
      this.righTextView = new TextView(this.getContext());
      this.righTextView.setTextSize(1, 14.0F);
      this.righTextView.setTextColor(Theme.getColor("key_graySectionText"));
      var5 = this.righTextView;
      if (LocaleController.isRTL) {
         var4 = 3;
      } else {
         var4 = 5;
      }

      var5.setGravity(var4 | 16);
      var5 = this.righTextView;
      var4 = var3;
      if (LocaleController.isRTL) {
         var4 = 3;
      }

      this.addView(var5, LayoutHelper.createFrame(-2, -1.0F, var4 | 48, 16.0F, 0.0F, 16.0F, 0.0F));
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(32.0F), 1073741824));
   }

   public void setText(String var1) {
      this.textView.setText(var1);
      this.righTextView.setVisibility(8);
   }

   public void setText(String var1, String var2, OnClickListener var3) {
      this.textView.setText(var1);
      this.righTextView.setText(var2);
      this.righTextView.setOnClickListener(var3);
      this.righTextView.setVisibility(0);
   }
}
