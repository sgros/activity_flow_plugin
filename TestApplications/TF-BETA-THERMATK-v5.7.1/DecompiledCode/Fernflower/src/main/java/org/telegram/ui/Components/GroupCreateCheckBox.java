package org.telegram.ui.Components;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.view.View;
import androidx.annotation.Keep;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;

public class GroupCreateCheckBox extends View {
   private static Paint eraser;
   private static Paint eraser2;
   private static final float progressBounceDiff = 0.2F;
   private boolean attachedToWindow;
   private Paint backgroundInnerPaint;
   private String backgroundKey = "checkboxCheck";
   private Paint backgroundPaint;
   private Canvas bitmapCanvas;
   private ObjectAnimator checkAnimator;
   private String checkKey = "checkboxCheck";
   private Paint checkPaint;
   private float checkScale = 1.0F;
   private Bitmap drawBitmap;
   private String innerKey = "checkbox";
   private int innerRadDiff;
   private boolean isCheckAnimation = true;
   private boolean isChecked;
   private float progress;

   public GroupCreateCheckBox(Context var1) {
      super(var1);
      if (eraser == null) {
         eraser = new Paint(1);
         eraser.setColor(0);
         eraser.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
         eraser2 = new Paint(1);
         eraser2.setColor(0);
         eraser2.setStyle(Style.STROKE);
         eraser2.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
      }

      this.backgroundPaint = new Paint(1);
      this.backgroundInnerPaint = new Paint(1);
      this.checkPaint = new Paint(1);
      this.checkPaint.setStyle(Style.STROKE);
      this.innerRadDiff = AndroidUtilities.dp(2.0F);
      this.checkPaint.setStrokeWidth((float)AndroidUtilities.dp(1.5F));
      eraser2.setStrokeWidth((float)AndroidUtilities.dp(28.0F));
      this.drawBitmap = Bitmap.createBitmap(AndroidUtilities.dp(24.0F), AndroidUtilities.dp(24.0F), Config.ARGB_4444);
      this.bitmapCanvas = new Canvas(this.drawBitmap);
      this.updateColors();
   }

   private void animateToCheckedState(boolean var1) {
      this.isCheckAnimation = var1;
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
      this.updateColors();
      this.attachedToWindow = true;
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.attachedToWindow = false;
   }

   protected void onDraw(Canvas var1) {
      if (this.getVisibility() == 0) {
         if (this.progress != 0.0F) {
            int var2 = this.getMeasuredWidth() / 2;
            int var3 = this.getMeasuredHeight() / 2;
            eraser2.setStrokeWidth((float)AndroidUtilities.dp(30.0F));
            this.drawBitmap.eraseColor(0);
            float var4 = this.progress;
            float var5;
            if (var4 >= 0.5F) {
               var5 = 1.0F;
            } else {
               var5 = var4 / 0.5F;
            }

            var4 = this.progress;
            float var6;
            if (var4 < 0.5F) {
               var6 = 0.0F;
            } else {
               var6 = (var4 - 0.5F) / 0.5F;
            }

            if (this.isCheckAnimation) {
               var4 = this.progress;
            } else {
               var4 = 1.0F - this.progress;
            }

            if (var4 < 0.2F) {
               var4 = (float)AndroidUtilities.dp(2.0F) * var4 / 0.2F;
            } else if (var4 < 0.4F) {
               var4 = (float)AndroidUtilities.dp(2.0F) - (float)AndroidUtilities.dp(2.0F) * (var4 - 0.2F) / 0.2F;
            } else {
               var4 = 0.0F;
            }

            if (var6 != 0.0F) {
               var1.drawCircle((float)var2, (float)var3, (float)(var2 - AndroidUtilities.dp(2.0F)) + (float)AndroidUtilities.dp(2.0F) * var6 - var4, this.backgroundPaint);
            }

            var4 = (float)(var2 - this.innerRadDiff) - var4;
            Canvas var7 = this.bitmapCanvas;
            float var8 = (float)var2;
            float var9 = (float)var3;
            var7.drawCircle(var8, var9, var4, this.backgroundInnerPaint);
            this.bitmapCanvas.drawCircle(var8, var9, var4 * (1.0F - var5), eraser);
            var1.drawBitmap(this.drawBitmap, 0.0F, 0.0F, (Paint)null);
            var4 = (float)AndroidUtilities.dp(10.0F) * var6 * this.checkScale;
            var5 = (float)AndroidUtilities.dp(5.0F) * var6 * this.checkScale;
            int var10 = var2 - AndroidUtilities.dp(1.0F);
            var2 = AndroidUtilities.dp(4.0F);
            var6 = (float)Math.sqrt((double)(var5 * var5 / 2.0F));
            var9 = (float)var10;
            var5 = (float)(var3 + var2);
            var1.drawLine(var9, var5, var9 - var6, var5 - var6, this.checkPaint);
            var6 = (float)Math.sqrt((double)(var4 * var4 / 2.0F));
            var4 = (float)(var10 - AndroidUtilities.dp(1.2F));
            var1.drawLine(var4, var5, var4 + var6, var5 - var6, this.checkPaint);
         }

      }
   }

   public void setCheckScale(float var1) {
      this.checkScale = var1;
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

   public void setColorKeysOverrides(String var1, String var2, String var3) {
      this.checkKey = var1;
      this.innerKey = var2;
      this.backgroundKey = var3;
      this.updateColors();
   }

   public void setInnerRadDiff(int var1) {
      this.innerRadDiff = var1;
   }

   @Keep
   public void setProgress(float var1) {
      if (this.progress != var1) {
         this.progress = var1;
         this.invalidate();
      }
   }

   public void updateColors() {
      this.backgroundInnerPaint.setColor(Theme.getColor(this.innerKey));
      this.backgroundPaint.setColor(Theme.getColor(this.backgroundKey));
      this.checkPaint.setColor(Theme.getColor(this.checkKey));
      this.invalidate();
   }
}
