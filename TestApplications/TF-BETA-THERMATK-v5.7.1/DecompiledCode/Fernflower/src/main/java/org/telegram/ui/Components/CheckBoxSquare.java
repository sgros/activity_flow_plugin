package org.telegram.ui.Components;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.view.View;
import androidx.annotation.Keep;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;

public class CheckBoxSquare extends View {
   private static final float progressBounceDiff = 0.2F;
   private boolean attachedToWindow;
   private ObjectAnimator checkAnimator;
   private Bitmap drawBitmap;
   private Canvas drawCanvas;
   private boolean isAlert;
   private boolean isChecked;
   private boolean isDisabled;
   private float progress;
   private RectF rectF;

   public CheckBoxSquare(Context var1, boolean var2) {
      super(var1);
      if (Theme.checkboxSquare_backgroundPaint == null) {
         Theme.createCommonResources(var1);
      }

      this.rectF = new RectF();
      this.drawBitmap = Bitmap.createBitmap(AndroidUtilities.dp(18.0F), AndroidUtilities.dp(18.0F), Config.ARGB_4444);
      this.drawCanvas = new Canvas(this.drawBitmap);
      this.isAlert = var2;
   }

   private void animateToCheckedState(boolean var1) {
      float var2;
      if (var1) {
         var2 = 1.0F;
      } else {
         var2 = 0.0F;
      }

      this.checkAnimator = ObjectAnimator.ofFloat(this, "progress", new float[]{var2});
      this.checkAnimator.setDuration(300L);
      this.checkAnimator.start();
   }

   private void cancelCheckAnimator() {
      ObjectAnimator var1 = this.checkAnimator;
      if (var1 != null) {
         var1.cancel();
      }

   }

   public float getProgress() {
      return this.progress;
   }

   public boolean isChecked() {
      return this.isChecked;
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.attachedToWindow = true;
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.attachedToWindow = false;
   }

   protected void onDraw(Canvas var1) {
      if (this.getVisibility() == 0) {
         String var2;
         if (this.isAlert) {
            var2 = "dialogCheckboxSquareUnchecked";
         } else {
            var2 = "checkboxSquareUnchecked";
         }

         int var3 = Theme.getColor(var2);
         if (this.isAlert) {
            var2 = "dialogCheckboxSquareBackground";
         } else {
            var2 = "checkboxSquareBackground";
         }

         int var4 = Theme.getColor(var2);
         float var5 = this.progress;
         float var6;
         int var8;
         if (var5 <= 0.5F) {
            var6 = var5 / 0.5F;
            int var7 = (int)((float)(Color.red(var4) - Color.red(var3)) * var6);
            var8 = (int)((float)(Color.green(var4) - Color.green(var3)) * var6);
            var4 = (int)((float)(Color.blue(var4) - Color.blue(var3)) * var6);
            var8 = Color.rgb(Color.red(var3) + var7, Color.green(var3) + var8, Color.blue(var3) + var4);
            Theme.checkboxSquare_backgroundPaint.setColor(var8);
            var5 = var6;
         } else {
            var5 /= 0.5F;
            Theme.checkboxSquare_backgroundPaint.setColor(var4);
            var5 = 2.0F - var5;
            var6 = 1.0F;
         }

         Paint var9;
         if (this.isDisabled) {
            var9 = Theme.checkboxSquare_backgroundPaint;
            if (this.isAlert) {
               var2 = "dialogCheckboxSquareDisabled";
            } else {
               var2 = "checkboxSquareDisabled";
            }

            var9.setColor(Theme.getColor(var2));
         }

         float var10 = (float)AndroidUtilities.dp(1.0F) * var5;
         this.rectF.set(var10, var10, (float)AndroidUtilities.dp(18.0F) - var10, (float)AndroidUtilities.dp(18.0F) - var10);
         this.drawBitmap.eraseColor(0);
         this.drawCanvas.drawRoundRect(this.rectF, (float)AndroidUtilities.dp(2.0F), (float)AndroidUtilities.dp(2.0F), Theme.checkboxSquare_backgroundPaint);
         if (var6 != 1.0F) {
            var6 = Math.min((float)AndroidUtilities.dp(7.0F), (float)AndroidUtilities.dp(7.0F) * var6 + var10);
            this.rectF.set((float)AndroidUtilities.dp(2.0F) + var6, (float)AndroidUtilities.dp(2.0F) + var6, (float)AndroidUtilities.dp(16.0F) - var6, (float)AndroidUtilities.dp(16.0F) - var6);
            this.drawCanvas.drawRect(this.rectF, Theme.checkboxSquare_eraserPaint);
         }

         if (this.progress > 0.5F) {
            var9 = Theme.checkboxSquare_checkPaint;
            if (this.isAlert) {
               var2 = "dialogCheckboxSquareCheck";
            } else {
               var2 = "checkboxSquareCheck";
            }

            var9.setColor(Theme.getColor(var2));
            var10 = (float)AndroidUtilities.dp(7.5F);
            var6 = (float)AndroidUtilities.dp(5.0F);
            var5 = 1.0F - var5;
            var8 = (int)(var10 - var6 * var5);
            var3 = (int)(AndroidUtilities.dpf2(13.5F) - (float)AndroidUtilities.dp(5.0F) * var5);
            this.drawCanvas.drawLine((float)AndroidUtilities.dp(7.5F), (float)((int)AndroidUtilities.dpf2(13.5F)), (float)var8, (float)var3, Theme.checkboxSquare_checkPaint);
            var8 = (int)(AndroidUtilities.dpf2(6.5F) + (float)AndroidUtilities.dp(9.0F) * var5);
            var3 = (int)(AndroidUtilities.dpf2(13.5F) - (float)AndroidUtilities.dp(9.0F) * var5);
            this.drawCanvas.drawLine((float)((int)AndroidUtilities.dpf2(6.5F)), (float)((int)AndroidUtilities.dpf2(13.5F)), (float)var8, (float)var3, Theme.checkboxSquare_checkPaint);
         }

         var1.drawBitmap(this.drawBitmap, 0.0F, 0.0F, (Paint)null);
      }
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
   }

   public void setChecked(boolean var1, boolean var2) {
      if (var1 != this.isChecked) {
         this.isChecked = var1;
         if (this.attachedToWindow && var2) {
            this.animateToCheckedState(var1);
         } else {
            this.cancelCheckAnimator();
            float var3;
            if (var1) {
               var3 = 1.0F;
            } else {
               var3 = 0.0F;
            }

            this.setProgress(var3);
         }

      }
   }

   public void setDisabled(boolean var1) {
      this.isDisabled = var1;
      this.invalidate();
   }

   @Keep
   public void setProgress(float var1) {
      if (this.progress != var1) {
         this.progress = var1;
         this.invalidate();
      }
   }
}
