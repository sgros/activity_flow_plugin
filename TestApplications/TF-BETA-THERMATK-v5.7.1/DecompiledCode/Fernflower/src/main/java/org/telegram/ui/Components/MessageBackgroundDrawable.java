package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;

public class MessageBackgroundDrawable extends Drawable {
   public static final float ANIMATION_DURATION = 200.0F;
   private boolean animationInProgress;
   private float currentAnimationProgress;
   private int finalRadius;
   private boolean isSelected;
   private long lastAnimationTime;
   private Paint paint = new Paint(1);
   private float touchX = -1.0F;
   private float touchY = -1.0F;

   public MessageBackgroundDrawable(int var1) {
      this.paint.setColor(var1);
   }

   private void calcRadius() {
      android.graphics.Rect var1;
      float var2;
      float var3;
      label31: {
         var1 = this.getBounds();
         var2 = this.touchX;
         if (var2 >= 0.0F) {
            var3 = this.touchY;
            if (var3 >= 0.0F) {
               break label31;
            }
         }

         var2 = (float)var1.centerX();
         var3 = (float)var1.centerY();
      }

      int var4 = 0;

      for(this.finalRadius = 0; var4 < 4; ++var4) {
         float var5;
         int var6;
         if (var4 != 0) {
            if (var4 != 1) {
               if (var4 != 2) {
                  var5 = (float)var1.right;
                  var6 = var1.bottom;
               } else {
                  var5 = (float)var1.right;
                  var6 = var1.top;
               }
            } else {
               var5 = (float)var1.left;
               var6 = var1.bottom;
            }
         } else {
            var5 = (float)var1.left;
            var6 = var1.top;
         }

         float var7 = (float)var6;
         var6 = this.finalRadius;
         var5 -= var2;
         var7 -= var3;
         this.finalRadius = Math.max(var6, (int)Math.ceil(Math.sqrt((double)(var5 * var5 + var7 * var7))));
      }

   }

   public void draw(Canvas var1) {
      if (this.animationInProgress) {
         long var2 = SystemClock.uptimeMillis();
         long var4 = var2 - this.lastAnimationTime;
         this.lastAnimationTime = var2;
         if (this.isSelected) {
            this.currentAnimationProgress += (float)var4 / 200.0F;
            if (this.currentAnimationProgress >= 1.0F) {
               this.touchX = -1.0F;
               this.touchY = -1.0F;
               this.currentAnimationProgress = 1.0F;
               this.animationInProgress = false;
            }

            this.invalidateSelf();
         } else {
            this.currentAnimationProgress -= (float)var4 / 200.0F;
            if (this.currentAnimationProgress <= 0.0F) {
               this.touchX = -1.0F;
               this.touchY = -1.0F;
               this.currentAnimationProgress = 0.0F;
               this.animationInProgress = false;
            }

            this.invalidateSelf();
         }
      }

      float var6 = this.currentAnimationProgress;
      if (var6 == 1.0F) {
         var1.drawRect(this.getBounds(), this.paint);
      } else if (var6 != 0.0F) {
         float var7;
         label30: {
            var6 = this.touchX;
            if (var6 >= 0.0F) {
               var7 = this.touchY;
               if (var7 >= 0.0F) {
                  break label30;
               }
            }

            android.graphics.Rect var8 = this.getBounds();
            var6 = (float)var8.centerX();
            var7 = (float)var8.centerY();
         }

         var1.drawCircle(var6, var7, (float)this.finalRadius * CubicBezierInterpolator.EASE_OUT.getInterpolation(this.currentAnimationProgress), this.paint);
      }

   }

   public int getOpacity() {
      return -2;
   }

   public void setAlpha(int var1) {
      this.paint.setAlpha(var1);
   }

   public void setBounds(int var1, int var2, int var3, int var4) {
      super.setBounds(var1, var2, var3, var4);
      this.calcRadius();
   }

   public void setBounds(android.graphics.Rect var1) {
      super.setBounds(var1);
      this.calcRadius();
   }

   public void setColor(int var1) {
      this.paint.setColor(var1);
   }

   public void setColorFilter(ColorFilter var1) {
      this.paint.setColorFilter(var1);
   }

   public void setSelected(boolean var1, boolean var2) {
      var2 = this.isSelected;
      float var3 = 1.0F;
      if (var2 == var1) {
         if (this.animationInProgress) {
            if (!var1) {
               var3 = 0.0F;
            }

            this.currentAnimationProgress = var3;
            this.animationInProgress = false;
         }

      } else {
         this.isSelected = var1;
         this.animationInProgress = false;
         if (!var1) {
            var3 = 0.0F;
         }

         this.currentAnimationProgress = var3;
         this.calcRadius();
         this.invalidateSelf();
      }
   }

   public void setTouchCoords(float var1, float var2) {
      this.touchX = var1;
      this.touchY = var2;
      this.calcRadius();
      this.invalidateSelf();
   }
}
