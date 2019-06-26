package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils.TruncateAt;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Switch;

public class TextCheckCell2 extends FrameLayout {
   private Switch checkBox;
   private boolean isMultiline;
   private boolean needDivider;
   private TextView textView;
   private TextView valueTextView;

   public TextCheckCell2(Context var1) {
      super(var1);
      this.textView = new TextView(var1);
      this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.textView.setTextSize(1, 16.0F);
      this.textView.setLines(1);
      this.textView.setMaxLines(1);
      this.textView.setSingleLine(true);
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
      this.textView.setEllipsize(TruncateAt.END);
      var2 = this.textView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      float var6;
      if (LocaleController.isRTL) {
         var6 = 64.0F;
      } else {
         var6 = 21.0F;
      }

      float var7;
      if (LocaleController.isRTL) {
         var7 = 21.0F;
      } else {
         var7 = 64.0F;
      }

      this.addView(var2, LayoutHelper.createFrame(-1, -1.0F, var5 | 48, var6, 0.0F, var7, 0.0F));
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
      this.valueTextView.setEllipsize(TruncateAt.END);
      var2 = this.valueTextView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      if (LocaleController.isRTL) {
         var6 = 64.0F;
      } else {
         var6 = 21.0F;
      }

      if (LocaleController.isRTL) {
         var7 = 21.0F;
      } else {
         var7 = 64.0F;
      }

      this.addView(var2, LayoutHelper.createFrame(-2, -2.0F, var5 | 48, var6, 35.0F, var7, 0.0F));
      this.checkBox = new Switch(var1);
      this.checkBox.setDrawIconType(1);
      Switch var8 = this.checkBox;
      var5 = var4;
      if (LocaleController.isRTL) {
         var5 = 3;
      }

      this.addView(var8, LayoutHelper.createFrame(37, 40.0F, var5 | 16, 22.0F, 0.0F, 22.0F, 0.0F));
   }

   public boolean hasIcon() {
      return this.checkBox.hasIcon();
   }

   public boolean isChecked() {
      return this.checkBox.isChecked();
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

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
      var1.setClassName("android.widget.Switch");
      var1.setCheckable(true);
      var1.setChecked(this.checkBox.isChecked());
      int var2;
      String var3;
      if (this.checkBox.isChecked()) {
         var2 = 2131560080;
         var3 = "NotificationsOn";
      } else {
         var2 = 2131560078;
         var3 = "NotificationsOff";
      }

      var1.setContentDescription(LocaleController.getString(var3, var2));
   }

   protected void onMeasure(int var1, int var2) {
      if (this.isMultiline) {
         super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(0, 0));
      } else {
         var1 = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824);
         float var3;
         if (this.valueTextView.getVisibility() == 0) {
            var3 = 64.0F;
         } else {
            var3 = 50.0F;
         }

         super.onMeasure(var1, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(var3) + this.needDivider, 1073741824));
      }

   }

   public void setChecked(boolean var1) {
      this.checkBox.setChecked(var1, true);
   }

   public void setEnabled(boolean var1) {
      super.setEnabled(var1);
      if (var1) {
         this.textView.setAlpha(1.0F);
         this.valueTextView.setAlpha(1.0F);
         this.checkBox.setAlpha(1.0F);
      } else {
         this.checkBox.setAlpha(0.5F);
         this.textView.setAlpha(0.5F);
         this.valueTextView.setAlpha(0.5F);
      }

   }

   public void setIcon(int var1) {
      this.checkBox.setIcon(var1);
   }

   public void setTextAndCheck(String var1, boolean var2, boolean var3) {
      this.textView.setText(var1);
      this.isMultiline = false;
      this.checkBox.setChecked(var2, false);
      this.needDivider = var3;
      this.valueTextView.setVisibility(8);
      LayoutParams var4 = (LayoutParams)this.textView.getLayoutParams();
      var4.height = -1;
      var4.topMargin = 0;
      this.textView.setLayoutParams(var4);
      this.setWillNotDraw(var3 ^ true);
   }

   public void setTextAndValueAndCheck(String var1, String var2, boolean var3, boolean var4, boolean var5) {
      this.textView.setText(var1);
      this.valueTextView.setText(var2);
      this.checkBox.setChecked(var3, false);
      this.needDivider = var5;
      this.valueTextView.setVisibility(0);
      this.isMultiline = var4;
      if (var4) {
         this.valueTextView.setLines(0);
         this.valueTextView.setMaxLines(0);
         this.valueTextView.setSingleLine(false);
         this.valueTextView.setEllipsize((TruncateAt)null);
         this.valueTextView.setPadding(0, 0, 0, AndroidUtilities.dp(11.0F));
      } else {
         this.valueTextView.setLines(1);
         this.valueTextView.setMaxLines(1);
         this.valueTextView.setSingleLine(true);
         this.valueTextView.setEllipsize(TruncateAt.END);
         this.valueTextView.setPadding(0, 0, 0, 0);
      }

      LayoutParams var6 = (LayoutParams)this.textView.getLayoutParams();
      var6.height = -2;
      var6.topMargin = AndroidUtilities.dp(10.0F);
      this.textView.setLayoutParams(var6);
      this.setWillNotDraw(true ^ var5);
   }
}
