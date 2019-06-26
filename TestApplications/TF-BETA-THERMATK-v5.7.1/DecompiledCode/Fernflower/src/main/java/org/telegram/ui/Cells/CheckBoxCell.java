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
import org.telegram.ui.Components.CheckBoxSquare;
import org.telegram.ui.Components.LayoutHelper;

public class CheckBoxCell extends FrameLayout {
   private CheckBoxSquare checkBox;
   private boolean isMultiline;
   private boolean needDivider;
   private TextView textView;
   private TextView valueTextView;

   public CheckBoxCell(Context var1, int var2) {
      this(var1, var2, 17);
   }

   public CheckBoxCell(Context var1, int var2, int var3) {
      super(var1);
      this.textView = new TextView(var1);
      TextView var4 = this.textView;
      boolean var5 = true;
      String var6;
      if (var2 == 1) {
         var6 = "dialogTextBlack";
      } else {
         var6 = "windowBackgroundWhiteBlackText";
      }

      var4.setTextColor(Theme.getColor(var6));
      var4 = this.textView;
      if (var2 == 1) {
         var6 = "dialogTextLink";
      } else {
         var6 = "windowBackgroundWhiteLinkText";
      }

      var4.setLinkTextColor(Theme.getColor(var6));
      this.textView.setTextSize(1, 16.0F);
      this.textView.setLines(1);
      this.textView.setMaxLines(1);
      this.textView.setSingleLine(true);
      this.textView.setEllipsize(TruncateAt.END);
      TextView var14 = this.textView;
      boolean var7 = LocaleController.isRTL;
      byte var8 = 5;
      byte var9;
      if (var7) {
         var9 = 5;
      } else {
         var9 = 3;
      }

      var14.setGravity(var9 | 16);
      float var12;
      if (var2 == 2) {
         var14 = this.textView;
         if (LocaleController.isRTL) {
            var9 = 5;
         } else {
            var9 = 3;
         }

         var7 = LocaleController.isRTL;
         byte var10 = 29;
         byte var11;
         if (var7) {
            var11 = 0;
         } else {
            var11 = 29;
         }

         var12 = (float)var11;
         if (LocaleController.isRTL) {
            var11 = var10;
         } else {
            var11 = 0;
         }

         this.addView(var14, LayoutHelper.createFrame(-1, -1.0F, var9 | 48, var12, 0.0F, (float)var11, 0.0F));
      } else {
         var14 = this.textView;
         if (LocaleController.isRTL) {
            var9 = 5;
         } else {
            var9 = 3;
         }

         int var15;
         if (LocaleController.isRTL) {
            var15 = var3;
         } else {
            var15 = var3 - 17 + 46;
         }

         var12 = (float)var15;
         if (LocaleController.isRTL) {
            var15 = var3 - 17 + 46;
         } else {
            var15 = var3;
         }

         this.addView(var14, LayoutHelper.createFrame(-1, -1.0F, var9 | 48, var12, 0.0F, (float)var15, 0.0F));
      }

      this.valueTextView = new TextView(var1);
      var4 = this.valueTextView;
      if (var2 == 1) {
         var6 = "dialogTextBlue";
      } else {
         var6 = "windowBackgroundWhiteValueText";
      }

      var4.setTextColor(Theme.getColor(var6));
      this.valueTextView.setTextSize(1, 16.0F);
      this.valueTextView.setLines(1);
      this.valueTextView.setMaxLines(1);
      this.valueTextView.setSingleLine(true);
      this.valueTextView.setEllipsize(TruncateAt.END);
      var14 = this.valueTextView;
      if (LocaleController.isRTL) {
         var9 = 3;
      } else {
         var9 = 5;
      }

      var14.setGravity(var9 | 16);
      var14 = this.valueTextView;
      if (LocaleController.isRTL) {
         var9 = 3;
      } else {
         var9 = 5;
      }

      var12 = (float)var3;
      this.addView(var14, LayoutHelper.createFrame(-2, -1.0F, var9 | 48, var12, 0.0F, var12, 0.0F));
      if (var2 != 1) {
         var5 = false;
      }

      this.checkBox = new CheckBoxSquare(var1, var5);
      CheckBoxSquare var13;
      if (var2 == 2) {
         var13 = this.checkBox;
         if (!LocaleController.isRTL) {
            var8 = 3;
         }

         this.addView(var13, LayoutHelper.createFrame(18, 18.0F, var8 | 48, 0.0F, 15.0F, 0.0F, 0.0F));
      } else {
         var13 = this.checkBox;
         if (!LocaleController.isRTL) {
            var8 = 3;
         }

         if (LocaleController.isRTL) {
            var2 = 0;
         } else {
            var2 = var3;
         }

         var12 = (float)var2;
         if (!LocaleController.isRTL) {
            var3 = 0;
         }

         this.addView(var13, LayoutHelper.createFrame(18, 18.0F, var8 | 48, var12, 16.0F, (float)var3, 0.0F));
      }

   }

   public CheckBoxSquare getCheckBox() {
      return this.checkBox;
   }

   public TextView getTextView() {
      return this.textView;
   }

   public TextView getValueTextView() {
      return this.valueTextView;
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
      if (this.isMultiline) {
         super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(0, 0));
      } else {
         this.setMeasuredDimension(MeasureSpec.getSize(var1), AndroidUtilities.dp(50.0F) + this.needDivider);
         var1 = this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight() - AndroidUtilities.dp(34.0F);
         this.valueTextView.measure(MeasureSpec.makeMeasureSpec(var1 / 2, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 1073741824));
         this.textView.measure(MeasureSpec.makeMeasureSpec(var1 - this.valueTextView.getMeasuredWidth() - AndroidUtilities.dp(8.0F), 1073741824), MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 1073741824));
         this.checkBox.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(18.0F), Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(18.0F), 1073741824));
      }

   }

   public void setChecked(boolean var1, boolean var2) {
      this.checkBox.setChecked(var1, var2);
   }

   public void setEnabled(boolean var1) {
      super.setEnabled(var1);
      TextView var2 = this.textView;
      float var3 = 1.0F;
      float var4;
      if (var1) {
         var4 = 1.0F;
      } else {
         var4 = 0.5F;
      }

      var2.setAlpha(var4);
      var2 = this.valueTextView;
      if (var1) {
         var4 = 1.0F;
      } else {
         var4 = 0.5F;
      }

      var2.setAlpha(var4);
      CheckBoxSquare var5 = this.checkBox;
      if (var1) {
         var4 = var3;
      } else {
         var4 = 0.5F;
      }

      var5.setAlpha(var4);
   }

   public void setMultiline(boolean var1) {
      this.isMultiline = var1;
      LayoutParams var2 = (LayoutParams)this.textView.getLayoutParams();
      LayoutParams var3 = (LayoutParams)this.checkBox.getLayoutParams();
      if (this.isMultiline) {
         this.textView.setLines(0);
         this.textView.setMaxLines(0);
         this.textView.setSingleLine(false);
         this.textView.setEllipsize((TruncateAt)null);
         this.textView.setPadding(0, 0, 0, AndroidUtilities.dp(5.0F));
         var2.height = -2;
         var2.topMargin = AndroidUtilities.dp(10.0F);
         var3.topMargin = AndroidUtilities.dp(12.0F);
      } else {
         this.textView.setLines(1);
         this.textView.setMaxLines(1);
         this.textView.setSingleLine(true);
         this.textView.setEllipsize(TruncateAt.END);
         this.textView.setPadding(0, 0, 0, 0);
         var2.height = -1;
         var2.topMargin = 0;
         var3.topMargin = AndroidUtilities.dp(15.0F);
      }

      this.textView.setLayoutParams(var2);
      this.checkBox.setLayoutParams(var3);
   }

   public void setText(CharSequence var1, String var2, boolean var3, boolean var4) {
      this.textView.setText(var1);
      this.checkBox.setChecked(var3, false);
      this.valueTextView.setText(var2);
      this.needDivider = var4;
      this.setWillNotDraw(var4 ^ true);
   }

   public void setTextColor(int var1) {
      this.textView.setTextColor(var1);
   }
}
