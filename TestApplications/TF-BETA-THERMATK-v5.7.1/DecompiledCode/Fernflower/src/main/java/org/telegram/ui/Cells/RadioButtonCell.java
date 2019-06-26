package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RadioButton;

public class RadioButtonCell extends FrameLayout {
   private boolean needDivider;
   private RadioButton radioButton;
   private TextView textView;
   private TextView valueTextView;

   public RadioButtonCell(Context var1) {
      this(var1, false);
   }

   public RadioButtonCell(Context var1, boolean var2) {
      super(var1);
      this.radioButton = new RadioButton(var1);
      this.radioButton.setSize(AndroidUtilities.dp(20.0F));
      if (var2) {
         this.radioButton.setColor(Theme.getColor("dialogRadioBackground"), Theme.getColor("dialogRadioBackgroundChecked"));
      } else {
         this.radioButton.setColor(Theme.getColor("radioBackground"), Theme.getColor("radioBackgroundChecked"));
      }

      RadioButton var3 = this.radioButton;
      boolean var4 = LocaleController.isRTL;
      byte var5 = 5;
      byte var6;
      if (var4) {
         var6 = 5;
      } else {
         var6 = 3;
      }

      var4 = LocaleController.isRTL;
      byte var7 = 20;
      byte var8;
      if (var4) {
         var8 = 0;
      } else {
         var8 = 20;
      }

      float var9 = (float)var8;
      if (LocaleController.isRTL) {
         var8 = var7;
      } else {
         var8 = 0;
      }

      this.addView(var3, LayoutHelper.createFrame(22, 22.0F, var6 | 48, var9, 10.0F, (float)var8, 0.0F));
      this.textView = new TextView(var1);
      if (var2) {
         this.textView.setTextColor(Theme.getColor("dialogTextBlack"));
      } else {
         this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      }

      this.textView.setTextSize(1, 16.0F);
      this.textView.setLines(1);
      this.textView.setMaxLines(1);
      this.textView.setSingleLine(true);
      TextView var11 = this.textView;
      if (LocaleController.isRTL) {
         var6 = 5;
      } else {
         var6 = 3;
      }

      var11.setGravity(var6 | 16);
      var11 = this.textView;
      if (LocaleController.isRTL) {
         var6 = 5;
      } else {
         var6 = 3;
      }

      var4 = LocaleController.isRTL;
      var7 = 23;
      if (var4) {
         var8 = 23;
      } else {
         var8 = 61;
      }

      var9 = (float)var8;
      var8 = var7;
      if (LocaleController.isRTL) {
         var8 = 61;
      }

      this.addView(var11, LayoutHelper.createFrame(-2, -2.0F, var6 | 48, var9, 10.0F, (float)var8, 0.0F));
      this.valueTextView = new TextView(var1);
      if (var2) {
         this.valueTextView.setTextColor(Theme.getColor("dialogTextGray2"));
      } else {
         this.valueTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
      }

      this.valueTextView.setTextSize(1, 13.0F);
      TextView var10 = this.valueTextView;
      if (LocaleController.isRTL) {
         var6 = 5;
      } else {
         var6 = 3;
      }

      var10.setGravity(var6);
      this.valueTextView.setLines(0);
      this.valueTextView.setMaxLines(0);
      this.valueTextView.setSingleLine(false);
      this.valueTextView.setPadding(0, 0, 0, AndroidUtilities.dp(12.0F));
      var10 = this.valueTextView;
      if (LocaleController.isRTL) {
         var6 = var5;
      } else {
         var6 = 3;
      }

      var2 = LocaleController.isRTL;
      var5 = 17;
      if (var2) {
         var8 = 17;
      } else {
         var8 = 61;
      }

      var9 = (float)var8;
      var8 = var5;
      if (LocaleController.isRTL) {
         var8 = 61;
      }

      this.addView(var10, LayoutHelper.createFrame(-2, -2.0F, var6 | 48, var9, 35.0F, (float)var8, 0.0F));
   }

   protected void onDraw(Canvas var1) {
      if (this.needDivider) {
         boolean var2 = LocaleController.isRTL;
         float var3 = 0.0F;
         float var4;
         if (var2) {
            var4 = 0.0F;
         } else {
            var4 = 60.0F;
         }

         float var5 = (float)AndroidUtilities.dp(var4);
         float var6 = (float)(this.getHeight() - 1);
         int var7 = this.getMeasuredWidth();
         var4 = var3;
         if (LocaleController.isRTL) {
            var4 = 60.0F;
         }

         var1.drawLine(var5, var6, (float)(var7 - AndroidUtilities.dp(var4)), (float)(this.getHeight() - 1), Theme.dividerPaint);
      }

   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
      var1.setClassName("android.widget.RadioButton");
      var1.setCheckable(true);
      var1.setChecked(this.radioButton.isChecked());
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(0, 0));
   }

   public void setChecked(boolean var1, boolean var2) {
      this.radioButton.setChecked(var1, var2);
   }

   public void setTextAndValue(String var1, String var2, boolean var3, boolean var4) {
      this.textView.setText(var1);
      this.valueTextView.setText(var2);
      this.radioButton.setChecked(var4, false);
      this.needDivider = var3;
   }
}
