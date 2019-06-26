package org.telegram.ui.Cells;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.Keep;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

public class TextColorCell extends FrameLayout {
   private static Paint colorPaint;
   public static final int[] colors = new int[]{-1031100, -29183, -12769, -8792480, -12521994, -12140801, -2984711, -45162, -4473925};
   public static final int[] colorsToSave = new int[]{-65536, -29183, -256, -16711936, -16711681, -16776961, -2984711, -65281, -1};
   private float alpha = 1.0F;
   private int currentColor;
   private boolean needDivider;
   private TextView textView;

   public TextColorCell(Context var1) {
      super(var1);
      if (colorPaint == null) {
         colorPaint = new Paint(1);
      }

      this.textView = new TextView(var1);
      this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.textView.setTextSize(1, 16.0F);
      this.textView.setLines(1);
      this.textView.setMaxLines(1);
      this.textView.setSingleLine(true);
      TextView var5 = this.textView;
      boolean var2 = LocaleController.isRTL;
      byte var3 = 5;
      byte var4;
      if (var2) {
         var4 = 5;
      } else {
         var4 = 3;
      }

      var5.setGravity(var4 | 16);
      var5 = this.textView;
      if (LocaleController.isRTL) {
         var4 = var3;
      } else {
         var4 = 3;
      }

      this.addView(var5, LayoutHelper.createFrame(-1, -1.0F, var4 | 48, 21.0F, 0.0F, 21.0F, 0.0F));
   }

   public float getAlpha() {
      return this.alpha;
   }

   protected void onDraw(Canvas var1) {
      int var5;
      if (this.needDivider) {
         float var2;
         if (LocaleController.isRTL) {
            var2 = 0.0F;
         } else {
            var2 = (float)AndroidUtilities.dp(20.0F);
         }

         float var3 = (float)(this.getMeasuredHeight() - 1);
         int var4 = this.getMeasuredWidth();
         if (LocaleController.isRTL) {
            var5 = AndroidUtilities.dp(20.0F);
         } else {
            var5 = 0;
         }

         var1.drawLine(var2, var3, (float)(var4 - var5), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
      }

      var5 = this.currentColor;
      if (var5 != 0) {
         colorPaint.setColor(var5);
         colorPaint.setAlpha((int)(this.alpha * 255.0F));
         if (LocaleController.isRTL) {
            var5 = AndroidUtilities.dp(33.0F);
         } else {
            var5 = this.getMeasuredWidth() - AndroidUtilities.dp(33.0F);
         }

         var1.drawCircle((float)var5, (float)(this.getMeasuredHeight() / 2), (float)AndroidUtilities.dp(10.0F), colorPaint);
      }

   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(50.0F) + this.needDivider, 1073741824));
   }

   @Keep
   public void setAlpha(float var1) {
      this.alpha = var1;
      this.invalidate();
   }

   public void setEnabled(boolean var1, ArrayList var2) {
      super.setEnabled(var1);
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
         if (!var1) {
            var3 = 0.5F;
         }

         var2.add(ObjectAnimator.ofFloat(this, "alpha", new float[]{var3}));
      } else {
         TextView var6 = this.textView;
         if (var1) {
            var5 = 1.0F;
         } else {
            var5 = 0.5F;
         }

         var6.setAlpha(var5);
         if (!var1) {
            var3 = 0.5F;
         }

         this.setAlpha(var3);
      }

   }

   public void setTextAndColor(String var1, int var2, boolean var3) {
      this.textView.setText(var1);
      this.needDivider = var3;
      this.currentColor = var2;
      if (!this.needDivider && this.currentColor == 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.setWillNotDraw(var3);
      this.invalidate();
   }
}
