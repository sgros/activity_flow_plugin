package org.telegram.ui.Cells;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils.TruncateAt;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RadioButton;

public class RadioCell extends FrameLayout {
   private boolean needDivider;
   private RadioButton radioButton;
   private TextView textView;

   public RadioCell(Context var1) {
      this(var1, false, 21);
   }

   public RadioCell(Context var1, boolean var2, int var3) {
      super(var1);
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
      this.textView.setEllipsize(TruncateAt.END);
      TextView var4 = this.textView;
      boolean var5 = LocaleController.isRTL;
      byte var6 = 5;
      byte var7;
      if (var5) {
         var7 = 5;
      } else {
         var7 = 3;
      }

      var4.setGravity(var7 | 16);
      var4 = this.textView;
      if (LocaleController.isRTL) {
         var7 = 5;
      } else {
         var7 = 3;
      }

      float var8 = (float)var3;
      this.addView(var4, LayoutHelper.createFrame(-1, -1.0F, var7 | 48, var8, 0.0F, var8, 0.0F));
      this.radioButton = new RadioButton(var1);
      this.radioButton.setSize(AndroidUtilities.dp(20.0F));
      if (var2) {
         this.radioButton.setColor(Theme.getColor("dialogRadioBackground"), Theme.getColor("dialogRadioBackgroundChecked"));
      } else {
         this.radioButton.setColor(Theme.getColor("radioBackground"), Theme.getColor("radioBackgroundChecked"));
      }

      RadioButton var10 = this.radioButton;
      var7 = var6;
      if (LocaleController.isRTL) {
         var7 = 3;
      }

      var2 = LocaleController.isRTL;
      byte var9 = 0;
      int var11;
      if (var2) {
         var11 = var3 + 1;
      } else {
         var11 = 0;
      }

      var8 = (float)var11;
      if (LocaleController.isRTL) {
         var3 = var9;
      } else {
         ++var3;
      }

      this.addView(var10, LayoutHelper.createFrame(22, 22.0F, var7 | 48, var8, 14.0F, (float)var3, 0.0F));
   }

   public boolean isChecked() {
      return this.radioButton.isChecked();
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
      var1.setClassName("android.widget.RadioButton");
      var1.setCheckable(true);
      var1.setChecked(this.isChecked());
   }

   protected void onMeasure(int var1, int var2) {
      this.setMeasuredDimension(MeasureSpec.getSize(var1), AndroidUtilities.dp(50.0F) + this.needDivider);
      int var3 = this.getMeasuredWidth();
      int var4 = this.getPaddingLeft();
      var1 = this.getPaddingRight();
      var2 = AndroidUtilities.dp(34.0F);
      this.radioButton.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(22.0F), Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(22.0F), 1073741824));
      this.textView.measure(MeasureSpec.makeMeasureSpec(var3 - var4 - var1 - var2, 1073741824), MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 1073741824));
   }

   public void setChecked(boolean var1, boolean var2) {
      this.radioButton.setChecked(var1, var2);
   }

   public void setEnabled(boolean var1, ArrayList var2) {
      float var3 = 1.0F;
      float var5;
      if (var2 != null) {
         TextView var4 = this.textView;
         if (var1) {
            var5 = 1.0F;
         } else {
            var5 = 0.5F;
         }

         var2.add(ObjectAnimator.ofFloat(var4, "alpha", new float[]{var5}));
         RadioButton var8 = this.radioButton;
         if (!var1) {
            var3 = 0.5F;
         }

         var2.add(ObjectAnimator.ofFloat(var8, "alpha", new float[]{var3}));
      } else {
         TextView var6 = this.textView;
         if (var1) {
            var5 = 1.0F;
         } else {
            var5 = 0.5F;
         }

         var6.setAlpha(var5);
         RadioButton var7 = this.radioButton;
         if (!var1) {
            var3 = 0.5F;
         }

         var7.setAlpha(var3);
      }

   }

   public void setText(String var1, boolean var2, boolean var3) {
      this.textView.setText(var1);
      this.radioButton.setChecked(var2, false);
      this.needDivider = var3;
      this.setWillNotDraw(var3 ^ true);
   }

   public void setTextColor(int var1) {
      this.textView.setTextColor(var1);
   }
}
