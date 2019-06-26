package org.telegram.ui.Cells;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils.TruncateAt;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RadioButton;

public class DialogRadioCell extends FrameLayout {
   private boolean needDivider;
   private RadioButton radioButton;
   private TextView textView;

   public DialogRadioCell(Context var1) {
      this(var1, false);
   }

   public DialogRadioCell(Context var1, boolean var2) {
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
      TextView var3 = this.textView;
      boolean var4 = LocaleController.isRTL;
      byte var5 = 5;
      byte var6;
      if (var4) {
         var6 = 5;
      } else {
         var6 = 3;
      }

      var3.setGravity(var6 | 16);
      var3 = this.textView;
      if (LocaleController.isRTL) {
         var6 = 5;
      } else {
         var6 = 3;
      }

      float var7;
      if (LocaleController.isRTL) {
         var7 = 23.0F;
      } else {
         var7 = 61.0F;
      }

      float var8;
      if (LocaleController.isRTL) {
         var8 = 61.0F;
      } else {
         var8 = 23.0F;
      }

      this.addView(var3, LayoutHelper.createFrame(-1, -1.0F, var6 | 48, var7, 0.0F, var8, 0.0F));
      this.radioButton = new RadioButton(var1);
      this.radioButton.setSize(AndroidUtilities.dp(20.0F));
      if (var2) {
         this.radioButton.setColor(Theme.getColor("dialogRadioBackground"), Theme.getColor("dialogRadioBackgroundChecked"));
      } else {
         this.radioButton.setColor(Theme.getColor("radioBackground"), Theme.getColor("radioBackgroundChecked"));
      }

      RadioButton var9 = this.radioButton;
      if (LocaleController.isRTL) {
         var6 = var5;
      } else {
         var6 = 3;
      }

      this.addView(var9, LayoutHelper.createFrame(22, 22.0F, var6 | 48, 20.0F, 15.0F, 20.0F, 0.0F));
   }

   public boolean isChecked() {
      return this.radioButton.isChecked();
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

   protected void onMeasure(int var1, int var2) {
      this.setMeasuredDimension(MeasureSpec.getSize(var1), AndroidUtilities.dp(50.0F) + this.needDivider);
      var2 = this.getMeasuredWidth();
      int var3 = this.getPaddingLeft();
      var1 = this.getPaddingRight();
      int var4 = AndroidUtilities.dp(34.0F);
      this.radioButton.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(22.0F), Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(22.0F), 1073741824));
      this.textView.measure(MeasureSpec.makeMeasureSpec(var2 - var3 - var1 - var4, 1073741824), MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 1073741824));
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
