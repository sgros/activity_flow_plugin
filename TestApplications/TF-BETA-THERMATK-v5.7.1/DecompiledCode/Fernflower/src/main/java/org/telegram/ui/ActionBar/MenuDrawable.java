package org.telegram.ui.ActionBar;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.animation.DecelerateInterpolator;
import org.telegram.messenger.AndroidUtilities;

public class MenuDrawable extends Drawable {
   private boolean animationInProgress;
   private int currentAnimationTime;
   private float currentRotation;
   private float finalRotation;
   private DecelerateInterpolator interpolator = new DecelerateInterpolator();
   private long lastFrameTime;
   private Paint paint = new Paint(1);
   private boolean reverseAngle;
   private boolean rotateToBack = true;

   public MenuDrawable() {
      this.paint.setStrokeWidth((float)AndroidUtilities.dp(2.0F));
   }

   public void draw(Canvas var1) {
      int var6;
      if (this.currentRotation != this.finalRotation) {
         if (this.lastFrameTime != 0L) {
            long var2 = System.currentTimeMillis();
            long var4 = this.lastFrameTime;
            this.currentAnimationTime = (int)((long)this.currentAnimationTime + (var2 - var4));
            var6 = this.currentAnimationTime;
            if (var6 >= 300) {
               this.currentRotation = this.finalRotation;
            } else if (this.currentRotation < this.finalRotation) {
               this.currentRotation = this.interpolator.getInterpolation((float)var6 / 300.0F) * this.finalRotation;
            } else {
               this.currentRotation = 1.0F - this.interpolator.getInterpolation((float)var6 / 300.0F);
            }
         }

         this.lastFrameTime = System.currentTimeMillis();
         this.invalidateSelf();
      }

      var1.save();
      var1.translate((float)(this.getIntrinsicWidth() / 2), (float)(this.getIntrinsicHeight() / 2));
      int var7 = Theme.getColor("actionBarDefaultIcon");
      float var8;
      float var9;
      float var10;
      float var11;
      float var12;
      float var13;
      short var14;
      if (this.rotateToBack) {
         var8 = this.currentRotation;
         if (this.reverseAngle) {
            var14 = -180;
         } else {
            var14 = 180;
         }

         var1.rotate(var8 * (float)var14);
         this.paint.setColor(var7);
         var1.drawLine((float)(-AndroidUtilities.dp(9.0F)), 0.0F, (float)AndroidUtilities.dp(9.0F) - (float)AndroidUtilities.dp(3.0F) * this.currentRotation, 0.0F, this.paint);
         var8 = (float)AndroidUtilities.dp(5.0F) * (1.0F - Math.abs(this.currentRotation)) - (float)AndroidUtilities.dp(0.5F) * Math.abs(this.currentRotation);
         var9 = (float)AndroidUtilities.dp(9.0F) - (float)AndroidUtilities.dp(2.5F) * Math.abs(this.currentRotation);
         var10 = (float)AndroidUtilities.dp(5.0F) + (float)AndroidUtilities.dp(2.0F) * Math.abs(this.currentRotation);
         var11 = (float)(-AndroidUtilities.dp(9.0F));
         var12 = (float)AndroidUtilities.dp(7.5F);
         var13 = Math.abs(this.currentRotation);
      } else {
         var8 = this.currentRotation;
         if (this.reverseAngle) {
            var14 = -225;
         } else {
            var14 = 135;
         }

         var1.rotate(var8 * (float)var14);
         var6 = Theme.getColor("actionBarActionModeDefaultIcon");
         this.paint.setColor(AndroidUtilities.getOffsetColor(var7, var6, this.currentRotation, 1.0F));
         var1.drawLine((float)(-AndroidUtilities.dp(9.0F)) + (float)AndroidUtilities.dp(1.0F) * this.currentRotation, 0.0F, (float)AndroidUtilities.dp(9.0F) - (float)AndroidUtilities.dp(1.0F) * this.currentRotation, 0.0F, this.paint);
         var8 = (float)AndroidUtilities.dp(5.0F) * (1.0F - Math.abs(this.currentRotation)) - (float)AndroidUtilities.dp(0.5F) * Math.abs(this.currentRotation);
         var9 = (float)AndroidUtilities.dp(9.0F) - (float)AndroidUtilities.dp(9.0F) * Math.abs(this.currentRotation);
         var10 = (float)AndroidUtilities.dp(5.0F) + (float)AndroidUtilities.dp(3.0F) * Math.abs(this.currentRotation);
         var11 = (float)(-AndroidUtilities.dp(9.0F));
         var12 = (float)AndroidUtilities.dp(9.0F);
         var13 = Math.abs(this.currentRotation);
      }

      var13 = var11 + var12 * var13;
      var1.drawLine(var13, -var10, var9, -var8, this.paint);
      var1.drawLine(var13, var10, var9, var8, this.paint);
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

   public void setColorFilter(ColorFilter var1) {
   }

   public void setRotateToBack(boolean var1) {
      this.rotateToBack = var1;
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
            this.currentAnimationTime = (int)(var3 * 300.0F);
         } else {
            this.currentAnimationTime = (int)((1.0F - var3) * 300.0F);
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
