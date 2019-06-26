package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.annotation.Keep;
import org.telegram.messenger.AndroidUtilities;

public class CheckBox extends View {
   private static Paint backgroundPaint;
   private static Paint checkPaint;
   private static Paint eraser;
   private static Paint eraser2;
   private static Paint paint;
   private static final float progressBounceDiff = 0.2F;
   private boolean attachedToWindow;
   private Canvas bitmapCanvas;
   private ObjectAnimator checkAnimator;
   private Bitmap checkBitmap;
   private Canvas checkCanvas;
   private Drawable checkDrawable;
   private int checkOffset;
   private String checkedText;
   private int color;
   private boolean drawBackground;
   private Bitmap drawBitmap;
   private boolean hasBorder;
   private boolean isCheckAnimation = true;
   private boolean isChecked;
   private float progress;
   private int size = 22;
   private TextPaint textPaint;

   public CheckBox(Context var1, int var2) {
      super(var1);
      if (paint == null) {
         paint = new Paint(1);
         eraser = new Paint(1);
         eraser.setColor(0);
         eraser.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
         eraser2 = new Paint(1);
         eraser2.setColor(0);
         eraser2.setStyle(Style.STROKE);
         eraser2.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
         backgroundPaint = new Paint(1);
         backgroundPaint.setColor(-1);
         backgroundPaint.setStyle(Style.STROKE);
      }

      eraser2.setStrokeWidth((float)AndroidUtilities.dp(28.0F));
      backgroundPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0F));
      this.textPaint = new TextPaint(1);
      this.textPaint.setTextSize((float)AndroidUtilities.dp(18.0F));
      this.textPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.checkDrawable = var1.getResources().getDrawable(var2).mutate();
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
      this.checkAnimator.addListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1) {
            if (var1.equals(CheckBox.this.checkAnimator)) {
               CheckBox.this.checkAnimator = null;
            }

            if (!CheckBox.this.isChecked) {
               CheckBox.this.checkedText = null;
            }

         }
      });
      this.checkAnimator.setDuration(300L);
      this.checkAnimator.start();
   }

   private void cancelCheckAnimator() {
      ObjectAnimator var1 = this.checkAnimator;
      if (var1 != null) {
         var1.cancel();
         this.checkAnimator = null;
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
      if (this.getVisibility() == 0 && this.drawBitmap != null && this.checkBitmap != null && (this.drawBackground || this.progress != 0.0F)) {
         eraser2.setStrokeWidth((float)AndroidUtilities.dp((float)(this.size + 6)));
         this.drawBitmap.eraseColor(0);
         float var2 = (float)(this.getMeasuredWidth() / 2);
         float var3 = this.progress;
         float var4;
         if (var3 >= 0.5F) {
            var4 = 1.0F;
         } else {
            var4 = var3 / 0.5F;
         }

         var3 = this.progress;
         float var5;
         if (var3 < 0.5F) {
            var5 = 0.0F;
         } else {
            var5 = (var3 - 0.5F) / 0.5F;
         }

         float var6;
         if (this.isCheckAnimation) {
            var6 = this.progress;
         } else {
            var6 = 1.0F - this.progress;
         }

         label53: {
            if (var6 < 0.2F) {
               var3 = (float)AndroidUtilities.dp(2.0F) * var6 / 0.2F;
            } else {
               var3 = var2;
               if (var6 >= 0.4F) {
                  break label53;
               }

               var3 = (float)AndroidUtilities.dp(2.0F) - (float)AndroidUtilities.dp(2.0F) * (var6 - 0.2F) / 0.2F;
            }

            var3 = var2 - var3;
         }

         if (this.drawBackground) {
            paint.setColor(1140850688);
            var1.drawCircle((float)(this.getMeasuredWidth() / 2), (float)(this.getMeasuredHeight() / 2), var3 - (float)AndroidUtilities.dp(1.0F), paint);
            var1.drawCircle((float)(this.getMeasuredWidth() / 2), (float)(this.getMeasuredHeight() / 2), var3 - (float)AndroidUtilities.dp(1.0F), backgroundPaint);
         }

         paint.setColor(this.color);
         var6 = var3;
         if (this.hasBorder) {
            var6 = var3 - (float)AndroidUtilities.dp(2.0F);
         }

         this.bitmapCanvas.drawCircle((float)(this.getMeasuredWidth() / 2), (float)(this.getMeasuredHeight() / 2), var6, paint);
         this.bitmapCanvas.drawCircle((float)(this.getMeasuredWidth() / 2), (float)(this.getMeasuredHeight() / 2), var6 * (1.0F - var4), eraser);
         var1.drawBitmap(this.drawBitmap, 0.0F, 0.0F, (Paint)null);
         this.checkBitmap.eraseColor(0);
         String var7 = this.checkedText;
         int var8;
         if (var7 != null) {
            var8 = (int)Math.ceil((double)this.textPaint.measureText(var7));
            Canvas var14 = this.checkCanvas;
            String var9 = this.checkedText;
            var4 = (float)((this.getMeasuredWidth() - var8) / 2);
            if (this.size == 40) {
               var3 = 28.0F;
            } else {
               var3 = 21.0F;
            }

            var14.drawText(var9, var4, (float)AndroidUtilities.dp(var3), this.textPaint);
         } else {
            int var10 = this.checkDrawable.getIntrinsicWidth();
            int var11 = this.checkDrawable.getIntrinsicHeight();
            var8 = (this.getMeasuredWidth() - var10) / 2;
            int var12 = (this.getMeasuredHeight() - var11) / 2;
            Drawable var15 = this.checkDrawable;
            int var13 = this.checkOffset;
            var15.setBounds(var8, var12 + var13, var10 + var8, var12 + var11 + var13);
            this.checkDrawable.draw(this.checkCanvas);
         }

         this.checkCanvas.drawCircle((float)(this.getMeasuredWidth() / 2 - AndroidUtilities.dp(2.5F)), (float)(this.getMeasuredHeight() / 2 + AndroidUtilities.dp(4.0F)), (float)((this.getMeasuredWidth() + AndroidUtilities.dp(6.0F)) / 2) * (1.0F - var5), eraser2);
         var1.drawBitmap(this.checkBitmap, 0.0F, 0.0F, (Paint)null);
      }

   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
      var1.setClassName("android.widget.CheckBox");
      var1.setCheckable(true);
      var1.setChecked(this.isChecked);
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
   }

   public void setBackgroundColor(int var1) {
      this.color = var1;
      this.invalidate();
   }

   public void setCheckColor(int var1) {
      this.checkDrawable.setColorFilter(new PorterDuffColorFilter(var1, Mode.MULTIPLY));
      this.textPaint.setColor(var1);
      this.invalidate();
   }

   public void setCheckOffset(int var1) {
      this.checkOffset = var1;
   }

   public void setChecked(int var1, boolean var2, boolean var3) {
      if (var1 >= 0) {
         StringBuilder var4 = new StringBuilder();
         var4.append("");
         var4.append(var1 + 1);
         this.checkedText = var4.toString();
         this.invalidate();
      }

      if (var2 != this.isChecked) {
         this.isChecked = var2;
         if (this.attachedToWindow && var3) {
            this.animateToCheckedState(var2);
         } else {
            this.cancelCheckAnimator();
            float var5;
            if (var2) {
               var5 = 1.0F;
            } else {
               var5 = 0.0F;
            }

            this.setProgress(var5);
         }

      }
   }

   public void setChecked(boolean var1, boolean var2) {
      this.setChecked(-1, var1, var2);
   }

   public void setColor(int var1, int var2) {
      this.color = var1;
      this.checkDrawable.setColorFilter(new PorterDuffColorFilter(var2, Mode.MULTIPLY));
      this.textPaint.setColor(var2);
      this.invalidate();
   }

   public void setDrawBackground(boolean var1) {
      this.drawBackground = var1;
   }

   public void setHasBorder(boolean var1) {
      this.hasBorder = var1;
   }

   public void setNum(int var1) {
      if (var1 >= 0) {
         StringBuilder var2 = new StringBuilder();
         var2.append("");
         var2.append(var1 + 1);
         this.checkedText = var2.toString();
      } else if (this.checkAnimator == null) {
         this.checkedText = null;
      }

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
      this.size = var1;
      if (var1 == 40) {
         this.textPaint.setTextSize((float)AndroidUtilities.dp(24.0F));
      }

   }

   public void setVisibility(int var1) {
      super.setVisibility(var1);
      if (var1 == 0 && this.drawBitmap == null) {
         try {
            this.drawBitmap = Bitmap.createBitmap(AndroidUtilities.dp((float)this.size), AndroidUtilities.dp((float)this.size), Config.ARGB_4444);
            Canvas var2 = new Canvas(this.drawBitmap);
            this.bitmapCanvas = var2;
            this.checkBitmap = Bitmap.createBitmap(AndroidUtilities.dp((float)this.size), AndroidUtilities.dp((float)this.size), Config.ARGB_4444);
            var2 = new Canvas(this.checkBitmap);
            this.checkCanvas = var2;
         } catch (Throwable var3) {
         }
      }

   }
}
