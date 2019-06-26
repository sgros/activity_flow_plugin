package org.telegram.ui.Cells;

import android.content.Context;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RadioButton;

public class RadioColorCell extends FrameLayout {
   private RadioButton radioButton;
   private TextView textView;

   public RadioColorCell(Context var1) {
      super(var1);
      this.radioButton = new RadioButton(var1);
      this.radioButton.setSize(AndroidUtilities.dp(20.0F));
      this.radioButton.setColor(Theme.getColor("dialogRadioBackground"), Theme.getColor("dialogRadioBackgroundChecked"));
      RadioButton var2 = this.radioButton;
      boolean var3 = LocaleController.isRTL;
      byte var4 = 5;
      byte var5;
      if (var3) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      var3 = LocaleController.isRTL;
      byte var6 = 0;
      byte var7;
      if (var3) {
         var7 = 0;
      } else {
         var7 = 18;
      }

      float var8 = (float)var7;
      var7 = var6;
      if (LocaleController.isRTL) {
         var7 = 18;
      }

      this.addView(var2, LayoutHelper.createFrame(22, 22.0F, var5 | 48, var8, 14.0F, (float)var7, 0.0F));
      this.textView = new TextView(var1);
      this.textView.setTextColor(Theme.getColor("dialogTextBlack"));
      this.textView.setTextSize(1, 16.0F);
      this.textView.setLines(1);
      this.textView.setMaxLines(1);
      this.textView.setSingleLine(true);
      TextView var9 = this.textView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      var9.setGravity(var5 | 16);
      var9 = this.textView;
      if (LocaleController.isRTL) {
         var5 = var4;
      } else {
         var5 = 3;
      }

      var3 = LocaleController.isRTL;
      var4 = 21;
      if (var3) {
         var7 = 21;
      } else {
         var7 = 51;
      }

      var8 = (float)var7;
      var7 = var4;
      if (LocaleController.isRTL) {
         var7 = 51;
      }

      this.addView(var9, LayoutHelper.createFrame(-2, -2.0F, var5 | 48, var8, 13.0F, (float)var7, 0.0F));
   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
      var1.setClassName("android.widget.RadioButton");
      var1.setCheckable(true);
      var1.setChecked(this.radioButton.isChecked());
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(50.0F), 1073741824));
   }

   public void setCheckColor(int var1, int var2) {
      this.radioButton.setColor(var1, var2);
   }

   public void setChecked(boolean var1, boolean var2) {
      this.radioButton.setChecked(var1, var2);
   }

   public void setTextAndValue(String var1, boolean var2) {
      this.textView.setText(var1);
      this.radioButton.setChecked(var2, false);
   }
}
