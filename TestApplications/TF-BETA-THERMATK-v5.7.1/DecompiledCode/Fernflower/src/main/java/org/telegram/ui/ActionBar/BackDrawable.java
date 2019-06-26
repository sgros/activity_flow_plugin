package org.telegram.ui.ActionBar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.animation.DecelerateInterpolator;
import org.telegram.messenger.AndroidUtilities;

public class BackDrawable extends Drawable {
   private boolean alwaysClose;
   private boolean animationInProgress;
   private float animationTime = 300.0F;
   private int arrowRotation;
   private int color = -1;
   private int currentAnimationTime;
   private float currentRotation;
   private float finalRotation;
   private DecelerateInterpolator interpolator = new DecelerateInterpolator();
   private long lastFrameTime;
   private Paint paint = new Paint(1);
   private boolean reverseAngle;
   private boolean rotated = true;
   private int rotatedColor = -9079435;

   public BackDrawable(boolean var1) {
      this.paint.setStrokeWidth((float)AndroidUtilities.dp(2.0F));
      this.alwaysClose = var1;
   }

   public void draw(Canvas var1) {
      int var6;
      float var7;
      float var8;
      if (this.currentRotation != this.finalRotation) {
         if (this.lastFrameTime != 0L) {
            long var2 = System.currentTimeMillis();
            long var4 = this.lastFrameTime;
            this.currentAnimationTime = (int)((long)this.currentAnimationTime + (var2 - var4));
            var6 = this.currentAnimationTime;
            var7 = (float)var6;
            var8 = this.animationTime;
            if (var7 >= var8) {
               this.currentRotation = this.finalRotation;
            } else if (this.currentRotation < this.finalRotation) {
               this.currentRotation = this.interpolator.getInterpolation((float)var6 / var8) * this.finalRotation;
            } else {
               this.currentRotation = 1.0F - this.interpolator.getInterpolation((float)var6 / var8);
            }
         }

         this.lastFrameTime = System.currentTimeMillis();
         this.invalidateSelf();
      }

      boolean var9 = this.rotated;
      int var10 = 0;
      if (var9) {
         var6 = (int)((float)(Color.red(this.rotatedColor) - Color.red(this.color)) * this.currentRotation);
      } else {
         var6 = 0;
      }

      int var11;
      if (this.rotated) {
         var11 = (int)((float)(Color.green(this.rotatedColor) - Color.green(this.color)) * this.currentRotation);
      } else {
         var11 = 0;
      }

      if (this.rotated) {
         var10 = (int)((float)(Color.blue(this.rotatedColor) - Color.blue(this.color)) * this.currentRotation);
      }

      var6 = Color.rgb(Color.red(this.color) + var6, Color.green(this.color) + var11, Color.blue(this.color) + var10);
      this.paint.setColor(var6);
      var1.save();
      var1.translate((float)(this.getIntrinsicWidth() / 2), (float)(this.getIntrinsicHeight() / 2));
      var6 = this.arrowRotation;
      if (var6 != 0) {
         var1.rotate((float)var6);
      }

      var8 = this.currentRotation;
      short var14;
      if (!this.alwaysClose) {
         if (this.reverseAngle) {
            var14 = -225;
         } else {
            var14 = 135;
         }

         var1.rotate((float)var14 * var8);
      } else {
         if (this.reverseAngle) {
            var14 = -180;
         } else {
            var14 = 180;
         }

         var1.rotate(var8 * (float)var14 + 135.0F);
         var8 = 1.0F;
      }

      var1.drawLine((float)(-AndroidUtilities.dp(7.0F)) - (float)AndroidUtilities.dp(1.0F) * var8, 0.0F, (float)AndroidUtilities.dp(8.0F), 0.0F, this.paint);
      var7 = (float)(-AndroidUtilities.dp(0.5F));
      float var12 = (float)AndroidUtilities.dp(7.0F) + (float)AndroidUtilities.dp(1.0F) * var8;
      float var13 = (float)(-AndroidUtilities.dp(7.0F)) + (float)AndroidUtilities.dp(7.0F) * var8;
      var8 = (float)AndroidUtilities.dp(0.5F) - (float)AndroidUtilities.dp(0.5F) * var8;
      var1.drawLine(var13, -var7, var8, -var12, this.paint);
      var1.drawLine(var13, var7, var8, var12, this.paint);
      var1.restore();
   }

   public int getIntrinsicHeight() {
      return AndroidUtilities.dp(24.0F);
   }

   public int getIntrinsicWidth() {
      return AndroidUtilities.dp(24.0F);
   }

   public int getOpacity() {
      return -2;
   }

   public void setAlpha(int var1) {
   }

   public void setAnimationTime(float var1) {
      this.animationTime = var1;
   }

   public void setArrowRotation(int var1) {
      this.arrowRotation = var1;
      this.invalidateSelf();
   }

   public void setColor(int var1) {
      this.color = var1;
      this.invalidateSelf();
   }

   public void setColorFilter(ColorFilter var1) {
   }

   public void setRotated(boolean var1) {
      this.rotated = var1;
   }

   public void setRotatedColor(int var1) {
      this.rotatedColor = var1;
      this.invalidateSelf();
   }

   public void setRotation(float var1, boolean var2) {
      this.lastFrameTime = 0L;
      float var3 = this.currentRotation;
      if (var3 == 1.0F) {
         this.reverseAngle = true;
      } else if (var3 == 0.0F) {
         this.reverseAngle = false;
      }

      this.lastFrameTime = 0L;
      if (var2) {
         var3 = this.currentRotation;
         if (var3 < var1) {
            this.currentAnimationTime = (int)(var3 * this.animationTime);
         } else {
            this.currentAnimationTime = (int)((1.0F - var3) * this.animationTime);
         }

         this.lastFrameTime = System.currentTimeMillis();
         this.finalRotation = var1;
      } else {
         this.currentRotation = var1;
         this.finalRotation = var1;
      }

      this.invalidateSelf();
   }
}
