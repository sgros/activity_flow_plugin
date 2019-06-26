package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.Components.LayoutHelper;

public class TextColorThemeCell extends FrameLayout {
   private static Paint colorPaint;
   private float alpha = 1.0F;
   private int currentColor;
   private boolean needDivider;
   private TextView textView;

   public TextColorThemeCell(Context var1) {
      super(var1);
      if (colorPaint == null) {
         colorPaint = new Paint(1);
      }

      this.textView = new TextView(var1);
      this.textView.setTextColor(-14606047);
      this.textView.setTextSize(1, 16.0F);
      this.textView.setLines(1);
      this.textView.setMaxLines(1);
      this.textView.setSingleLine(true);
      TextView var7 = this.textView;
      boolean var2 = LocaleController.isRTL;
      byte var3 = 5;
      byte var4;
      if (var2) {
         var4 = 5;
      } else {
         var4 = 3;
      }

      var7.setGravity(var4 | 16);
      this.textView.setPadding(0, 0, 0, AndroidUtilities.dp(3.0F));
      var7 = this.textView;
      if (LocaleController.isRTL) {
         var4 = var3;
      } else {
         var4 = 3;
      }

      var2 = LocaleController.isRTL;
      byte var5 = 21;
      if (var2) {
         var3 = 21;
      } else {
         var3 = 57;
      }

      float var6 = (float)var3;
      var3 = var5;
      if (LocaleController.isRTL) {
         var3 = 57;
      }

      this.addView(var7, LayoutHelper.createFrame(-1, -1.0F, var4 | 48, var6, 0.0F, (float)var3, 0.0F));
   }

   public float getAlpha() {
      return this.alpha;
   }

   protected void onDraw(Canvas var1) {
      int var2 = this.currentColor;
      if (var2 != 0) {
         colorPaint.setColor(var2);
         colorPaint.setAlpha((int)(this.alpha * 255.0F));
         if (!LocaleController.isRTL) {
            var2 = AndroidUtilities.dp(28.0F);
         } else {
            var2 = this.getMeasuredWidth() - AndroidUtilities.dp(28.0F);
         }

         var1.drawCircle((float)var2, (float)(this.getMeasuredHeight() / 2), (float)AndroidUtilities.dp(10.0F), colorPaint);
      }

   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(50.0F) + this.needDivider, 1073741824));
   }

   public void setAlpha(float var1) {
      this.alpha = var1;
      this.invalidate();
   }

   public void setTextAndColor(CharSequence var1, int var2) {
      this.textView.setText(var1);
      this.currentColor = var2;
      boolean var3;
      if (!this.needDivider && this.currentColor == 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.setWillNotDraw(var3);
      this.invalidate();
   }
}
