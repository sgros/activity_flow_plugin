package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils.TruncateAt;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

public class TextPriceCell extends FrameLayout {
   private TextView textView;
   private TextView valueTextView;

   public TextPriceCell(Context var1) {
      super(var1);
      this.setWillNotDraw(false);
      this.textView = new TextView(var1);
      this.textView.setTextSize(1, 16.0F);
      this.textView.setLines(1);
      this.textView.setMaxLines(1);
      this.textView.setSingleLine(true);
      this.textView.setEllipsize(TruncateAt.END);
      TextView var2 = this.textView;
      boolean var3 = LocaleController.isRTL;
      byte var4 = 5;
      byte var5;
      if (var3) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      var2.setGravity(var5 | 16);
      var2 = this.textView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      this.addView(var2, LayoutHelper.createFrame(-2, -1.0F, var5 | 48, 21.0F, 0.0F, 21.0F, 0.0F));
      this.valueTextView = new TextView(var1);
      this.valueTextView.setTextSize(1, 16.0F);
      this.valueTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.valueTextView.setLines(1);
      this.valueTextView.setMaxLines(1);
      this.valueTextView.setSingleLine(true);
      this.valueTextView.setEllipsize(TruncateAt.END);
      TextView var6 = this.valueTextView;
      if (LocaleController.isRTL) {
         var5 = 3;
      } else {
         var5 = 5;
      }

      var6.setGravity(var5 | 16);
      var6 = this.valueTextView;
      var5 = var4;
      if (LocaleController.isRTL) {
         var5 = 3;
      }

      this.addView(var6, LayoutHelper.createFrame(-2, -1.0F, var5 | 48, 21.0F, 0.0F, 21.0F, 0.0F));
   }

   protected void onMeasure(int var1, int var2) {
      this.setMeasuredDimension(MeasureSpec.getSize(var1), AndroidUtilities.dp(40.0F));
      var1 = this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight() - AndroidUtilities.dp(34.0F);
      var2 = var1 / 2;
      this.valueTextView.measure(MeasureSpec.makeMeasureSpec(var2, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 1073741824));
      var2 = this.valueTextView.getMeasuredWidth();
      int var3 = AndroidUtilities.dp(8.0F);
      this.textView.measure(MeasureSpec.makeMeasureSpec(var1 - var2 - var3, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 1073741824));
   }

   public void setTextAndValue(String var1, String var2, boolean var3) {
      this.textView.setText(var1);
      if (var2 != null) {
         this.valueTextView.setText(var2);
         this.valueTextView.setVisibility(0);
      } else {
         this.valueTextView.setVisibility(4);
      }

      if (var3) {
         this.setTag("windowBackgroundWhiteBlackText");
         this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.valueTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         this.valueTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      } else {
         this.setTag("windowBackgroundWhiteGrayText2");
         this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
         this.valueTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
         this.textView.setTypeface(Typeface.DEFAULT);
         this.valueTextView.setTypeface(Typeface.DEFAULT);
      }

      this.requestLayout();
   }

   public void setTextColor(int var1) {
      this.textView.setTextColor(var1);
   }

   public void setTextValueColor(int var1) {
      this.valueTextView.setTextColor(var1);
   }
}
