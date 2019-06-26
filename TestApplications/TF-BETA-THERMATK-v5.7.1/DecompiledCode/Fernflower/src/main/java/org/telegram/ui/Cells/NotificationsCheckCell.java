package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils.TruncateAt;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Switch;

public class NotificationsCheckCell extends FrameLayout {
   private Switch checkBox;
   private int currentHeight;
   private boolean drawLine;
   private boolean isMultiline;
   private boolean needDivider;
   private TextView textView;
   private TextView valueTextView;

   public NotificationsCheckCell(Context var1) {
      this(var1, 21, 70);
   }

   public NotificationsCheckCell(Context var1, int var2, int var3) {
      super(var1);
      this.drawLine = true;
      this.setWillNotDraw(false);
      this.currentHeight = var3;
      this.textView = new TextView(var1);
      this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.textView.setTextSize(1, 16.0F);
      this.textView.setLines(1);
      this.textView.setMaxLines(1);
      this.textView.setSingleLine(true);
      TextView var4 = this.textView;
      boolean var5 = LocaleController.isRTL;
      byte var11 = 5;
      byte var10;
      if (var5) {
         var10 = 5;
      } else {
         var10 = 3;
      }

      var4.setGravity(var10 | 16);
      this.textView.setEllipsize(TruncateAt.END);
      var4 = this.textView;
      if (LocaleController.isRTL) {
         var10 = 5;
      } else {
         var10 = 3;
      }

      float var6;
      if (LocaleController.isRTL) {
         var6 = 80.0F;
      } else {
         var6 = 23.0F;
      }

      float var7 = (float)((this.currentHeight - 70) / 2 + 13);
      float var8;
      if (LocaleController.isRTL) {
         var8 = 23.0F;
      } else {
         var8 = 80.0F;
      }

      this.addView(var4, LayoutHelper.createFrame(-1, -2.0F, var10 | 48, var6, var7, var8, 0.0F));
      this.valueTextView = new TextView(var1);
      this.valueTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
      this.valueTextView.setTextSize(1, 13.0F);
      var4 = this.valueTextView;
      if (LocaleController.isRTL) {
         var10 = 5;
      } else {
         var10 = 3;
      }

      var4.setGravity(var10);
      this.valueTextView.setLines(1);
      this.valueTextView.setMaxLines(1);
      this.valueTextView.setSingleLine(true);
      this.valueTextView.setPadding(0, 0, 0, 0);
      this.valueTextView.setEllipsize(TruncateAt.END);
      var4 = this.valueTextView;
      if (LocaleController.isRTL) {
         var10 = 5;
      } else {
         var10 = 3;
      }

      if (LocaleController.isRTL) {
         var6 = 80.0F;
      } else {
         var6 = 23.0F;
      }

      var7 = (float)((this.currentHeight - 70) / 2 + 38);
      if (LocaleController.isRTL) {
         var8 = 23.0F;
      } else {
         var8 = 80.0F;
      }

      this.addView(var4, LayoutHelper.createFrame(-2, -2.0F, var10 | 48, var6, var7, var8, 0.0F));
      this.checkBox = new Switch(var1);
      this.checkBox.setColors("switchTrack", "switchTrackChecked", "windowBackgroundWhite", "windowBackgroundWhite");
      Switch var9 = this.checkBox;
      var10 = var11;
      if (LocaleController.isRTL) {
         var10 = 3;
      }

      this.addView(var9, LayoutHelper.createFrame(37, 40.0F, var10 | 16, 21.0F, 0.0F, 21.0F, 0.0F));
      this.checkBox.setFocusable(true);
   }

   public boolean isChecked() {
      return this.checkBox.isChecked();
   }

   protected void onDraw(Canvas var1) {
      int var4;
      int var5;
      if (this.needDivider) {
         float var2;
         if (LocaleController.isRTL) {
            var2 = 0.0F;
         } else {
            var2 = (float)AndroidUtilities.dp(20.0F);
         }

         float var3 = (float)(this.getMeasuredHeight() - 1);
         var4 = this.getMeasuredWidth();
         if (LocaleController.isRTL) {
            var5 = AndroidUtilities.dp(20.0F);
         } else {
            var5 = 0;
         }

         var1.drawLine(var2, var3, (float)(var4 - var5), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
      }

      if (this.drawLine) {
         if (LocaleController.isRTL) {
            var5 = AndroidUtilities.dp(76.0F);
         } else {
            var5 = this.getMeasuredWidth() - AndroidUtilities.dp(76.0F) - 1;
         }

         var4 = (this.getMeasuredHeight() - AndroidUtilities.dp(22.0F)) / 2;
         var1.drawRect((float)var5, (float)var4, (float)(var5 + 2), (float)(var4 + AndroidUtilities.dp(22.0F)), Theme.dividerPaint);
      }

   }

   protected void onMeasure(int var1, int var2) {
      if (this.isMultiline) {
         super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(0, 0));
      } else {
         super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp((float)this.currentHeight), 1073741824));
      }

   }

   public void setChecked(boolean var1) {
      this.checkBox.setChecked(var1, true);
   }

   public void setChecked(boolean var1, int var2) {
      this.checkBox.setChecked(var1, var2, true);
   }

   public void setDrawLine(boolean var1) {
      this.drawLine = var1;
   }

   public void setTextAndValueAndCheck(String var1, CharSequence var2, boolean var3, int var4, boolean var5) {
      this.setTextAndValueAndCheck(var1, var2, var3, var4, false, var5);
   }

   public void setTextAndValueAndCheck(String var1, CharSequence var2, boolean var3, int var4, boolean var5, boolean var6) {
      this.textView.setText(var1);
      this.valueTextView.setText(var2);
      this.checkBox.setChecked(var3, var4, false);
      this.valueTextView.setVisibility(0);
      this.needDivider = var6;
      this.isMultiline = var5;
      if (var5) {
         this.valueTextView.setLines(0);
         this.valueTextView.setMaxLines(0);
         this.valueTextView.setSingleLine(false);
         this.valueTextView.setEllipsize((TruncateAt)null);
         this.valueTextView.setPadding(0, 0, 0, AndroidUtilities.dp(14.0F));
      } else {
         this.valueTextView.setLines(1);
         this.valueTextView.setMaxLines(1);
         this.valueTextView.setSingleLine(true);
         this.valueTextView.setEllipsize(TruncateAt.END);
         this.valueTextView.setPadding(0, 0, 0, 0);
      }

      this.checkBox.setContentDescription(var1);
   }

   public void setTextAndValueAndCheck(String var1, CharSequence var2, boolean var3, boolean var4) {
      this.setTextAndValueAndCheck(var1, var2, var3, 0, false, var4);
   }
}
