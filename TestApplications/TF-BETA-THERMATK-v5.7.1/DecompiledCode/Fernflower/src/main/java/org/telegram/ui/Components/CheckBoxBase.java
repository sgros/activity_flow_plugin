package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.view.View;
import androidx.annotation.Keep;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;

public class CheckBoxBase {
   private static Paint backgroundPaint;
   private static Paint checkPaint;
   private static Paint eraser;
   private static Paint paint;
   private boolean attachedToWindow;
   private String background2ColorKey = "chat_serviceBackground";
   private float backgroundAlpha = 1.0F;
   private String backgroundColorKey = "chat_serviceBackground";
   private Canvas bitmapCanvas;
   private android.graphics.Rect bounds = new android.graphics.Rect();
   private ObjectAnimator checkAnimator;
   private String checkColorKey = "checkboxCheck";
   private int drawBackgroundAsArc;
   private Bitmap drawBitmap;
   private boolean drawUnchecked = true;
   private boolean enabled = true;
   private boolean isChecked;
   private View parentView;
   private Path path = new Path();
   private float progress;
   private CheckBoxBase.ProgressDelegate progressDelegate;
   private RectF rect = new RectF();
   private float size = 21.0F;
   private boolean useDefaultCheck;

   public CheckBoxBase(View var1) {
      this.parentView = var1;
      if (paint == null) {
         paint = new Paint(1);
         eraser = new Paint(1);
         eraser.setColor(0);
         eraser.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
         backgroundPaint = new Paint(1);
         backgroundPaint.setStyle(Style.STROKE);
         checkPaint = new Paint(1);
         checkPaint.setStrokeCap(Cap.ROUND);
         checkPaint.setStyle(Style.STROKE);
         checkPaint.setStrokeJoin(Join.ROUND);
      }

      checkPaint.setStrokeWidth((float)AndroidUtilities.dp(1.9F));
      backgroundPaint.setStrokeWidth((float)AndroidUtilities.dp(1.2F));
      this.drawBitmap = Bitmap.createBitmap(AndroidUtilities.dp(this.size), AndroidUtilities.dp(this.size), Config.ARGB_4444);
      this.bitmapCanvas = new Canvas(this.drawBitmap);
   }

   private void animateToCheckedState(boolean var1) {
      float var2;
      if (var1) {
         var2 = 1.0F;
      } else {
         var2 = 0.0F;
      }

      this.checkAnimator = ObjectAnimator.ofFloat(this, "progress", new float[]{var2});
      this.checkAnimator.addListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1) {
            if (var1.equals(CheckBoxBase.this.checkAnimator)) {
               CheckBoxBase.this.checkAnimator = null;
            }

         }
      });
      this.checkAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT);
      this.checkAnimator.setDuration(200L);
      this.checkAnimator.start();
   }

   private void cancelCheckAnimator() {
      ObjectAnimator var1 = this.checkAnimator;
      if (var1 != null) {
         var1.cancel();
         this.checkAnimator = null;
      }

   }

   public void draw(Canvas var1) {
      Bitmap var2 = this.drawBitmap;
      if (var2 != null) {
         var2.eraseColor(0);
         float var3 = (float)AndroidUtilities.dp(this.size / 2.0F);
         float var4;
         if (this.drawBackgroundAsArc != 0) {
            var4 = var3 - (float)AndroidUtilities.dp(0.2F);
         } else {
            var4 = var3;
         }

         float var5 = this.progress;
         float var6 = 1.0F;
         if (var5 >= 0.5F) {
            var5 = 1.0F;
         } else {
            var5 /= 0.5F;
         }

         int var7 = this.bounds.centerX();
         int var8 = this.bounds.centerY();
         Paint var9;
         String var14;
         if (this.backgroundColorKey != null) {
            if (this.drawUnchecked) {
               paint.setColor(Theme.getServiceMessageColor() & 16777215 | 671088640);
               backgroundPaint.setColor(Theme.getColor(this.checkColorKey));
            } else {
               var9 = backgroundPaint;
               var14 = this.background2ColorKey;
               if (var14 == null) {
                  var14 = this.checkColorKey;
               }

               var9.setColor(AndroidUtilities.getOffsetColor(16777215, Theme.getColor(var14), this.progress, this.backgroundAlpha));
            }
         } else if (this.drawUnchecked) {
            paint.setColor(Color.argb((int)(this.backgroundAlpha * 25.0F), 0, 0, 0));
            backgroundPaint.setColor(AndroidUtilities.getOffsetColor(-1, Theme.getColor(this.checkColorKey), this.progress, this.backgroundAlpha));
         } else {
            var9 = backgroundPaint;
            var14 = this.background2ColorKey;
            if (var14 == null) {
               var14 = this.checkColorKey;
            }

            var9.setColor(AndroidUtilities.getOffsetColor(16777215, Theme.getColor(var14), this.progress, this.backgroundAlpha));
         }

         if (this.drawUnchecked) {
            var1.drawCircle((float)var7, (float)var8, var3, paint);
         }

         paint.setColor(Theme.getColor(this.checkColorKey));
         float var10;
         int var13;
         if (this.drawBackgroundAsArc == 0) {
            var1.drawCircle((float)var7, (float)var8, var3, backgroundPaint);
         } else {
            RectF var15 = this.rect;
            var10 = (float)var7;
            float var11 = (float)var8;
            var15.set(var10 - var4, var11 - var4, var10 + var4, var11 + var4);
            byte var12;
            if (this.drawBackgroundAsArc == 1) {
               var12 = -90;
               var10 = -270.0F;
               var4 = this.progress;
            } else {
               var12 = 90;
               var10 = 270.0F;
               var4 = this.progress;
            }

            var13 = (int)(var4 * var10);
            var1.drawArc(this.rect, (float)var12, (float)var13, false, backgroundPaint);
         }

         if (var5 > 0.0F) {
            var4 = this.progress;
            if (var4 < 0.5F) {
               var4 = 0.0F;
            } else {
               var4 = (var4 - 0.5F) / 0.5F;
            }

            label94: {
               if (!this.drawUnchecked) {
                  var14 = this.backgroundColorKey;
                  if (var14 != null) {
                     paint.setColor(Theme.getColor(var14));
                     break label94;
                  }
               }

               var9 = paint;
               if (this.enabled) {
                  var14 = "checkbox";
               } else {
                  var14 = "checkboxDisabled";
               }

               var9.setColor(Theme.getColor(var14));
            }

            label76: {
               if (!this.useDefaultCheck) {
                  var14 = this.checkColorKey;
                  if (var14 != null) {
                     checkPaint.setColor(Theme.getColor(var14));
                     break label76;
                  }
               }

               checkPaint.setColor(Theme.getColor("checkboxCheck"));
            }

            var10 = var3 - (float)AndroidUtilities.dp(0.5F);
            this.bitmapCanvas.drawCircle((float)(this.drawBitmap.getWidth() / 2), (float)(this.drawBitmap.getHeight() / 2), var10, paint);
            this.bitmapCanvas.drawCircle((float)(this.drawBitmap.getWidth() / 2), (float)(this.drawBitmap.getWidth() / 2), var10 * (1.0F - var5), eraser);
            var2 = this.drawBitmap;
            var1.drawBitmap(var2, (float)(var7 - var2.getWidth() / 2), (float)(var8 - this.drawBitmap.getHeight() / 2), (Paint)null);
            if (var4 != 0.0F) {
               this.path.reset();
               var5 = var6;
               if (this.drawBackgroundAsArc == 5) {
                  var5 = 0.8F;
               }

               var10 = (float)AndroidUtilities.dp(9.0F * var5) * var4;
               var5 = (float)AndroidUtilities.dp(var5 * 4.0F) * var4;
               int var17 = AndroidUtilities.dp(1.5F);
               var13 = AndroidUtilities.dp(4.0F);
               var3 = (float)Math.sqrt((double)(var5 * var5 / 2.0F));
               Path var16 = this.path;
               var4 = (float)(var7 - var17);
               var5 = (float)(var8 + var13);
               var16.moveTo(var4 - var3, var5 - var3);
               this.path.lineTo(var4, var5);
               var10 = (float)Math.sqrt((double)(var10 * var10 / 2.0F));
               this.path.lineTo(var4 + var10, var5 - var10);
               var1.drawPath(this.path, checkPaint);
            }
         }

      }
   }

   public float getProgress() {
      return this.progress;
   }

   public boolean isChecked() {
      return this.isChecked;
   }

   public void onAttachedToWindow() {
      this.attachedToWindow = true;
   }

   public void onDetachedFromWindow() {
      this.attachedToWindow = false;
   }

   public void setBackgroundAlpha(float var1) {
      this.backgroundAlpha = var1;
   }

   public void setBounds(int var1, int var2, int var3, int var4) {
      android.graphics.Rect var5 = this.bounds;
      var5.left = var1;
      var5.top = var2;
      var5.right = var1 + var3;
      var5.bottom = var2 + var4;
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

   public void setColor(String var1, String var2, String var3) {
      this.backgroundColorKey = var1;
      this.background2ColorKey = var2;
      this.checkColorKey = var3;
   }

   public void setDrawBackgroundAsArc(int var1) {
      this.drawBackgroundAsArc = var1;
      if (var1 != 4 && var1 != 5) {
         if (var1 == 3) {
            backgroundPaint.setStrokeWidth((float)AndroidUtilities.dp(1.2F));
         } else if (var1 != 0) {
            backgroundPaint.setStrokeWidth((float)AndroidUtilities.dp(1.5F));
         }
      } else {
         backgroundPaint.setStrokeWidth((float)AndroidUtilities.dp(1.9F));
         if (var1 == 5) {
            checkPaint.setStrokeWidth((float)AndroidUtilities.dp(1.5F));
         }
      }

   }

   public void setDrawUnchecked(boolean var1) {
      this.drawUnchecked = var1;
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
   }

   @Keep
   public void setProgress(float var1) {
      if (this.progress != var1) {
         this.progress = var1;
         if (this.parentView.getParent() != null) {
            ((View)this.parentView.getParent()).invalidate();
         }

         this.parentView.invalidate();
         CheckBoxBase.ProgressDelegate var2 = this.progressDelegate;
         if (var2 != null) {
            var2.setProgress(var1);
         }

      }
   }

   public void setProgressDelegate(CheckBoxBase.ProgressDelegate var1) {
      this.progressDelegate = var1;
   }

   public void setSize(int var1) {
      this.size = (float)var1;
   }

   public void setUseDefaultCheck(boolean var1) {
      this.useDefaultCheck = var1;
   }

   public interface ProgressDelegate {
      void setProgress(float var1);
   }
}
