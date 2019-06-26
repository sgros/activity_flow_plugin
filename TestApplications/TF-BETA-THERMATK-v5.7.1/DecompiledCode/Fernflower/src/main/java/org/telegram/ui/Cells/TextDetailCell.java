package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils.TruncateAt;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

public class TextDetailCell extends FrameLayout {
   private boolean needDivider;
   private TextView textView;
   private TextView valueTextView;

   public TextDetailCell(Context var1) {
      super(var1);
      this.textView = new TextView(var1);
      this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.textView.setTextSize(1, 16.0F);
      TextView var2 = this.textView;
      byte var3;
      if (LocaleController.isRTL) {
         var3 = 5;
      } else {
         var3 = 3;
      }

      var2.setGravity(var3);
      this.textView.setLines(1);
      this.textView.setMaxLines(1);
      this.textView.setSingleLine(true);
      this.textView.setEllipsize(TruncateAt.END);
      var2 = this.textView;
      if (LocaleController.isRTL) {
         var3 = 5;
      } else {
         var3 = 3;
      }

      this.addView(var2, LayoutHelper.createFrame(-2, -2.0F, var3, 23.0F, 8.0F, 23.0F, 0.0F));
      this.valueTextView = new TextView(var1);
      this.valueTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
      this.valueTextView.setTextSize(1, 13.0F);
      this.valueTextView.setLines(1);
      this.valueTextView.setMaxLines(1);
      this.valueTextView.setSingleLine(true);
      TextView var4 = this.valueTextView;
      if (LocaleController.isRTL) {
         var3 = 5;
      } else {
         var3 = 3;
      }

      var4.setGravity(var3);
      var4 = this.valueTextView;
      if (LocaleController.isRTL) {
         var3 = 5;
      } else {
         var3 = 3;
      }

      this.addView(var4, LayoutHelper.createFrame(-2, -2.0F, var3, 23.0F, 33.0F, 23.0F, 0.0F));
   }

   public void invalidate() {
      super.invalidate();
      this.textView.invalidate();
   }

   protected void onDraw(Canvas var1) {
      if (this.needDivider) {
         float var2;
         if (LocaleController.isRTL) {
            var2 = 0.0F;
         } else {
            var2 = (float)AndroidUtilities.dp(20.0F);
         }

         float var3 = (float)(this.getMeasuredHeight() - 1);
         int var4 = this.getMeasuredWidth();
         int var5;
         if (LocaleController.isRTL) {
            var5 = AndroidUtilities.dp(20.0F);
         } else {
            var5 = 0;
         }

         var1.drawLine(var2, var3, (float)(var4 - var5), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
      }

   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(60.0F) + this.needDivider, 1073741824));
   }

   public void setTextAndValue(String var1, String var2, boolean var3) {
      this.textView.setText(var1);
      this.valueTextView.setText(var2);
      this.needDivider = var3;
      this.setWillNotDraw(this.needDivider ^ true);
   }

   public void setTextWithEmojiAndValue(String var1, CharSequence var2, boolean var3) {
      TextView var4 = this.textView;
      var4.setText(Emoji.replaceEmoji(var1, var4.getPaint().getFontMetricsInt(), AndroidUtilities.dp(14.0F), false));
      this.valueTextView.setText(var2);
      this.needDivider = var3;
      this.setWillNotDraw(var3 ^ true);
   }
}
