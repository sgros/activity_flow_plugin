package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.view.animation.DecelerateInterpolator;
import org.telegram.messenger.AndroidUtilities;

public class CloseProgressDrawable2 extends Drawable {
   private float angle;
   private boolean animating;
   private DecelerateInterpolator interpolator = new DecelerateInterpolator();
   private long lastFrameTime;
   private Paint paint = new Paint(1);
   private RectF rect = new RectF();
   private int side;

   public CloseProgressDrawable2() {
      this.paint.setColor(-1);
      this.paint.setStrokeWidth((float)AndroidUtilities.dp(2.0F));
      this.paint.setStrokeCap(Cap.ROUND);
      this.paint.setStyle(Style.STROKE);
      this.side = AndroidUtilities.dp(8.0F);
   }

   public void draw(Canvas var1) {
      long var2 = System.currentTimeMillis();
      long var4 = this.lastFrameTime;
      float var6 = 0.0F;
      float var7;
      if (var4 != 0L && (this.animating || this.angle != 0.0F)) {
         this.angle += (float)((var2 - var4) * 360L) / 500.0F;
         if (!this.animating && this.angle >= 720.0F) {
            this.angle = 0.0F;
         } else {
            var7 = this.angle;
            this.angle = var7 - (float)((int)(var7 / 720.0F) * 720);
         }

         this.invalidateSelf();
      }

      float var8;
      float var9;
      float var11;
      label103: {
         label102: {
            label101: {
               label100: {
                  label122: {
                     var1.save();
                     var1.translate((float)(this.getIntrinsicWidth() / 2), (float)(this.getIntrinsicHeight() / 2));
                     var1.rotate(-45.0F);
                     var7 = this.angle;
                     if (var7 >= 0.0F && var7 < 90.0F) {
                        var7 = 1.0F - var7 / 90.0F;
                     } else {
                        var7 = this.angle;
                        if (var7 >= 90.0F && var7 < 180.0F) {
                           var8 = 1.0F - (var7 - 90.0F) / 90.0F;
                           var7 = 0.0F;
                           break label100;
                        }

                        var7 = this.angle;
                        if (var7 >= 180.0F && var7 < 270.0F) {
                           var9 = 1.0F - (var7 - 180.0F) / 90.0F;
                           var7 = 0.0F;
                           var8 = 0.0F;
                           break label101;
                        }

                        var7 = this.angle;
                        if (var7 >= 270.0F && var7 < 360.0F) {
                           var7 = (var7 - 270.0F) / 90.0F;
                           break label102;
                        }

                        var7 = this.angle;
                        if (var7 >= 360.0F && var7 < 450.0F) {
                           var7 = 1.0F - (var7 - 360.0F) / 90.0F;
                           break label102;
                        }

                        var7 = this.angle;
                        if (var7 >= 450.0F && var7 < 540.0F) {
                           var7 = (var7 - 450.0F) / 90.0F;
                           var8 = 0.0F;
                           break label122;
                        }

                        var7 = this.angle;
                        if (var7 >= 540.0F && var7 < 630.0F) {
                           var8 = (var7 - 540.0F) / 90.0F;
                           var7 = 1.0F;
                           break label122;
                        }

                        var7 = this.angle;
                        if (var7 >= 630.0F && var7 < 720.0F) {
                           var9 = (var7 - 630.0F) / 90.0F;
                           var7 = 1.0F;
                           var8 = 1.0F;
                           break label101;
                        }

                        var7 = 1.0F;
                     }

                     var8 = 1.0F;
                     break label100;
                  }

                  var9 = 0.0F;
                  break label101;
               }

               var9 = 1.0F;
            }

            float var10 = 0.0F;
            var11 = var9;
            var9 = var10;
            break label103;
         }

         var9 = var7;
         var7 = 0.0F;
         var8 = 0.0F;
         var11 = 0.0F;
      }

      if (var7 != 0.0F) {
         var1.drawLine(0.0F, 0.0F, 0.0F, (float)this.side * var7, this.paint);
      }

      if (var8 != 0.0F) {
         var1.drawLine((float)(-this.side) * var8, 0.0F, 0.0F, 0.0F, this.paint);
      }

      if (var11 != 0.0F) {
         var1.drawLine(0.0F, (float)(-this.side) * var11, 0.0F, 0.0F, this.paint);
      }

      int var12;
      if (var9 != 1.0F) {
         var12 = this.side;
         var1.drawLine((float)var12 * var9, 0.0F, (float)var12, 0.0F, this.paint);
      }

      var1.restore();
      int var13 = this.getBounds().centerX();
      var12 = this.getBounds().centerY();
      RectF var14 = this.rect;
      int var15 = this.side;
      var14.set((float)(var13 - var15), (float)(var12 - var15), (float)(var13 + var15), (float)(var12 + var15));
      var14 = this.rect;
      var7 = this.angle;
      if (var7 < 360.0F) {
         var7 = var6;
      } else {
         var7 -= 360.0F;
      }

      var8 = this.angle;
      if (var8 >= 360.0F) {
         var8 = 720.0F - var8;
      }

      var1.drawArc(var14, var7 - 45.0F, var8, false, this.paint);
      this.lastFrameTime = var2;
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

   public boolean isAnimating() {
      return this.animating;
   }

   public void setAlpha(int var1) {
   }

   public void setColor(int var1) {
      this.paint.setColor(var1);
   }

   public void setColorFilter(ColorFilter var1) {
      this.paint.setColorFilter(var1);
   }

   public void setSide(int var1) {
      this.side = var1;
   }

   public void startAnimation() {
      this.animating = true;
      this.lastFrameTime = System.currentTimeMillis();
      this.invalidateSelf();
   }

   public void stopAnimation() {
      this.animating = false;
   }
}
