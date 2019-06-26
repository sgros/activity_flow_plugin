package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils.TruncateAt;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.CheckBoxSquare;
import org.telegram.ui.Components.LayoutHelper;

public class TextCheckBoxCell extends FrameLayout {
   private CheckBoxSquare checkBox;
   private boolean needDivider;
   private TextView textView;

   public TextCheckBoxCell(Context var1) {
      this(var1, false);
   }

   public TextCheckBoxCell(Context var1, boolean var2) {
      super(var1);
      this.textView = new TextView(var1);
      TextView var3 = this.textView;
      String var4;
      if (var2) {
         var4 = "dialogTextBlack";
      } else {
         var4 = "windowBackgroundWhiteBlackText";
      }

      var3.setTextColor(Theme.getColor(var4));
      this.textView.setTextSize(1, 16.0F);
      this.textView.setLines(1);
      this.textView.setMaxLines(1);
      this.textView.setSingleLine(true);
      TextView var11 = this.textView;
      boolean var5 = LocaleController.isRTL;
      byte var6 = 5;
      byte var7;
      if (var5) {
         var7 = 5;
      } else {
         var7 = 3;
      }

      var11.setGravity(var7 | 16);
      this.textView.setEllipsize(TruncateAt.END);
      var11 = this.textView;
      if (LocaleController.isRTL) {
         var7 = 5;
      } else {
         var7 = 3;
      }

      float var8;
      if (LocaleController.isRTL) {
         var8 = 66.0F;
      } else {
         var8 = 21.0F;
      }

      float var9;
      if (LocaleController.isRTL) {
         var9 = 21.0F;
      } else {
         var9 = 66.0F;
      }

      this.addView(var11, LayoutHelper.createFrame(-1, -1.0F, var7 | 48, var8, 0.0F, var9, 0.0F));
      this.checkBox = new CheckBoxSquare(var1, var2);
      this.checkBox.setDuplicateParentStateEnabled(false);
      this.checkBox.setFocusable(false);
      this.checkBox.setFocusableInTouchMode(false);
      this.checkBox.setClickable(false);
      CheckBoxSquare var10 = this.checkBox;
      var7 = var6;
      if (LocaleController.isRTL) {
         var7 = 3;
      }

      this.addView(var10, LayoutHelper.createFrame(18, 18.0F, var7 | 16, 21.0F, 0.0F, 21.0F, 0.0F));
   }

   public void invalidate() {
      super.invalidate();
      this.checkBox.invalidate();
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
      var1.setClassName("android.widget.CheckBox");
      var1.setCheckable(true);
      var1.setChecked(this.isChecked());
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(50.0F) + this.needDivider, 1073741824));
   }

   public void setChecked(boolean var1) {
      this.checkBox.setChecked(var1, true);
   }

   public void setTextAndCheck(String var1, boolean var2, boolean var3) {
      this.textView.setText(var1);
      this.checkBox.setChecked(var2, false);
      this.needDivider = var3;
      this.setWillNotDraw(var3 ^ true);
   }
}
