package org.telegram.ui.Components;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.view.View;
import androidx.annotation.Keep;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;

public class RadioButton extends View {
   private static Paint checkedPaint;
   private static Paint eraser;
   private static Paint paint;
   private boolean attachedToWindow;
   private Bitmap bitmap;
   private Canvas bitmapCanvas;
   private ObjectAnimator checkAnimator;
   private int checkedColor;
   private int color;
   private boolean isChecked;
   private float progress;
   private int size = AndroidUtilities.dp(16.0F);

   public RadioButton(Context var1) {
      super(var1);
      if (paint == null) {
         paint = new Paint(1);
         paint.setStrokeWidth((float)AndroidUtilities.dp(2.0F));
         paint.setStyle(Style.STROKE);
         checkedPaint = new Paint(1);
         eraser = new Paint(1);
         eraser.setColor(0);
         eraser.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
      }

      try {
         this.bitmap = Bitmap.createBitmap(AndroidUtilities.dp((float)this.size), AndroidUtilities.dp((float)this.size), Config.ARGB_4444);
         Canvas var3 = new Canvas(this.bitmap);
         this.bitmapCanvas = var3;
      } catch (Throwable var2) {
         FileLog.e(var2);
      }

   }

   private void animateToCheckedState(boolean var1) {
      float var2;
      if (var1) {
         var2 = 1.0F;
      } else {
         var2 = 0.0F;
      }

      this.checkAnimator = ObjectAnimator.ofFloat(this, "progress", new float[]{var2});
      this.checkAnimator.setDuration(200L);
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
      Bitmap var2 = this.bitmap;
      if (var2 == null || var2.getWidth() != this.getMeasuredWidth()) {
         var2 = this.bitmap;
         if (var2 != null) {
            var2.recycle();
            this.bitmap = null;
         }

         try {
            this.bitmap = Bitmap.createBitmap(this.getMeasuredWidth(), this.getMeasuredHeight(), Config.ARGB_8888);
            Canvas var12 = new Canvas(this.bitmap);
            this.bitmapCanvas = var12;
         } catch (Throwable var11) {
            FileLog.e(var11);
         }
      }

      float var3 = this.progress;
      float var5;
      if (var3 <= 0.5F) {
         paint.setColor(this.color);
         checkedPaint.setColor(this.color);
         var3 = this.progress / 0.5F;
      } else {
         var3 = 2.0F - var3 / 0.5F;
         int var4 = Color.red(this.color);
         var5 = (float)(Color.red(this.checkedColor) - var4);
         float var6 = 1.0F - var3;
         int var7 = (int)(var5 * var6);
         int var8 = Color.green(this.color);
         int var9 = (int)((float)(Color.green(this.checkedColor) - var8) * var6);
         int var10 = Color.blue(this.color);
         var9 = Color.rgb(var4 + var7, var8 + var9, var10 + (int)((float)(Color.blue(this.checkedColor) - var10) * var6));
         paint.setColor(var9);
         checkedPaint.setColor(var9);
      }

      var2 = this.bitmap;
      if (var2 != null) {
         var2.eraseColor(0);
         var5 = (float)(this.size / 2) - (var3 + 1.0F) * AndroidUtilities.density;
         this.bitmapCanvas.drawCircle((float)(this.getMeasuredWidth() / 2), (float)(this.getMeasuredHeight() / 2), var5, paint);
         if (this.progress <= 0.5F) {
            this.bitmapCanvas.drawCircle((float)(this.getMeasuredWidth() / 2), (float)(this.getMeasuredHeight() / 2), var5 - (float)AndroidUtilities.dp(1.0F), checkedPaint);
            this.bitmapCanvas.drawCircle((float)(this.getMeasuredWidth() / 2), (float)(this.getMeasuredHeight() / 2), (var5 - (float)AndroidUtilities.dp(1.0F)) * (1.0F - var3), eraser);
         } else {
            this.bitmapCanvas.drawCircle((float)(this.getMeasuredWidth() / 2), (float)(this.getMeasuredHeight() / 2), (float)(this.size / 4) + (var5 - (float)AndroidUtilities.dp(1.0F) - (float)(this.size / 4)) * var3, checkedPaint);
         }

         var1.drawBitmap(this.bitmap, 0.0F, 0.0F, (Paint)null);
      }

   }

   public void setBackgroundColor(int var1) {
      this.color = var1;
      this.invalidate();
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

   public void setCheckedColor(int var1) {
      this.checkedColor = var1;
      this.invalidate();
   }

   public void setColor(int var1, int var2) {
      this.color = var1;
      this.checkedColor = var2;
      this.invalidate();
   }

   @Keep
   public void setProgress(float var1) {
      if (this.progress != var1) {
         this.progress = var1;
         this.invalidate();
      }
   }

   public void setSize(int var1) {
      if (this.size != var1) {
         this.size = var1;
      }
   }
}
