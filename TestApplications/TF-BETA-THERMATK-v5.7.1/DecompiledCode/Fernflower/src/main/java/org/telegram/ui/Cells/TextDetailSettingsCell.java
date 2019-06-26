package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.text.TextUtils.TruncateAt;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

public class TextDetailSettingsCell extends FrameLayout {
   private ImageView imageView;
   private boolean multiline;
   private boolean needDivider;
   private TextView textView;
   private TextView valueTextView;

   public TextDetailSettingsCell(Context var1) {
      super(var1);
      this.textView = new TextView(var1);
      this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
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

      this.addView(var2, LayoutHelper.createFrame(-2, -2.0F, var5 | 48, 21.0F, 10.0F, 21.0F, 0.0F));
      this.valueTextView = new TextView(var1);
      this.valueTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
      this.valueTextView.setTextSize(1, 13.0F);
      var2 = this.valueTextView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      var2.setGravity(var5);
      this.valueTextView.setLines(1);
      this.valueTextView.setMaxLines(1);
      this.valueTextView.setSingleLine(true);
      this.valueTextView.setPadding(0, 0, 0, 0);
      var2 = this.valueTextView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      this.addView(var2, LayoutHelper.createFrame(-2, -2.0F, var5 | 48, 21.0F, 35.0F, 21.0F, 0.0F));
      this.imageView = new ImageView(var1);
      this.imageView.setScaleType(ScaleType.CENTER);
      this.imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayIcon"), Mode.MULTIPLY));
      this.imageView.setVisibility(8);
      ImageView var6 = this.imageView;
      if (LocaleController.isRTL) {
         var5 = var4;
      } else {
         var5 = 3;
      }

      this.addView(var6, LayoutHelper.createFrame(52, 52.0F, var5 | 48, 8.0F, 6.0F, 8.0F, 0.0F));
   }

   public TextView getTextView() {
      return this.textView;
   }

   public TextView getValueTextView() {
      return this.valueTextView;
   }

   public void invalidate() {
      super.invalidate();
      this.textView.invalidate();
   }

   protected void onDraw(Canvas var1) {
      if (this.needDivider && Theme.dividerPaint != null) {
         boolean var2 = LocaleController.isRTL;
         float var3 = 71.0F;
         float var4;
         if (var2) {
            var4 = 0.0F;
         } else {
            if (this.imageView.getVisibility() == 0) {
               var4 = 71.0F;
            } else {
               var4 = 20.0F;
            }

            var4 = (float)AndroidUtilities.dp(var4);
         }

         float var5 = (float)(this.getMeasuredHeight() - 1);
         int var6 = this.getMeasuredWidth();
         int var7;
         if (LocaleController.isRTL) {
            if (this.imageView.getVisibility() != 0) {
               var3 = 20.0F;
            }

            var7 = AndroidUtilities.dp(var3);
         } else {
            var7 = 0;
         }

         var1.drawLine(var4, var5, (float)(var6 - var7), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
      }

   }

   protected void onMeasure(int var1, int var2) {
      if (!this.multiline) {
         super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0F) + this.needDivider, 1073741824));
      } else {
         super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(0, 0));
      }

   }

   public void setMultilineDetail(boolean var1) {
      this.multiline = var1;
      if (var1) {
         this.valueTextView.setLines(0);
         this.valueTextView.setMaxLines(0);
         this.valueTextView.setSingleLine(false);
         this.valueTextView.setPadding(0, 0, 0, AndroidUtilities.dp(12.0F));
      } else {
         this.valueTextView.setLines(1);
         this.valueTextView.setMaxLines(1);
         this.valueTextView.setSingleLine(true);
         this.valueTextView.setPadding(0, 0, 0, 0);
      }

   }

   public void setTextAndValue(String var1, CharSequence var2, boolean var3) {
      this.textView.setText(var1);
      this.valueTextView.setText(var2);
      this.needDivider = var3;
      this.imageView.setVisibility(8);
      this.setWillNotDraw(var3 ^ true);
   }

   public void setTextAndValueAndIcon(String var1, CharSequence var2, int var3, boolean var4) {
      this.textView.setText(var1);
      this.valueTextView.setText(var2);
      this.imageView.setImageResource(var3);
      this.imageView.setVisibility(0);
      TextView var7 = this.textView;
      if (LocaleController.isRTL) {
         var3 = 0;
      } else {
         var3 = AndroidUtilities.dp(50.0F);
      }

      int var5;
      if (LocaleController.isRTL) {
         var5 = AndroidUtilities.dp(50.0F);
      } else {
         var5 = 0;
      }

      var7.setPadding(var3, 0, var5, 0);
      var7 = this.valueTextView;
      if (LocaleController.isRTL) {
         var3 = 0;
      } else {
         var3 = AndroidUtilities.dp(50.0F);
      }

      if (LocaleController.isRTL) {
         var5 = AndroidUtilities.dp(50.0F);
      } else {
         var5 = 0;
      }

      int var6;
      if (this.multiline) {
         var6 = AndroidUtilities.dp(12.0F);
      } else {
         var6 = 0;
      }

      var7.setPadding(var3, 0, var5, var6);
      this.needDivider = var4;
      this.setWillNotDraw(var4 ^ true);
   }

   public void setTextWithEmojiAnd21Value(String var1, CharSequence var2, boolean var3) {
      TextView var4 = this.textView;
      var4.setText(Emoji.replaceEmoji(var1, var4.getPaint().getFontMetricsInt(), AndroidUtilities.dp(14.0F), false));
      this.valueTextView.setText(var2);
      this.needDivider = var3;
      this.setWillNotDraw(var3 ^ true);
   }

   public void setValue(CharSequence var1) {
      this.valueTextView.setText(var1);
   }
}
