package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.AndroidUtilities;

public class VideoForwardDrawable extends Drawable {
   private static final int[] playPath = new int[]{10, 7, 26, 16, 10, 25};
   private boolean animating;
   private float animationProgress;
   private VideoForwardDrawable.VideoForwardDrawableDelegate delegate;
   private long lastAnimationTime;
   private boolean leftSide;
   private Paint paint = new Paint(1);
   private Path path1 = new Path();

   public VideoForwardDrawable() {
      this.paint.setColor(-1);
      this.path1.reset();
      int var1 = 0;

      while(true) {
         int[] var2 = playPath;
         if (var1 >= var2.length / 2) {
            this.path1.close();
            return;
         }

         Path var3;
         int var4;
         if (var1 == 0) {
            var3 = this.path1;
            var4 = var1 * 2;
            var3.moveTo((float)AndroidUtilities.dp((float)var2[var4]), (float)AndroidUtilities.dp((float)playPath[var4 + 1]));
         } else {
            var3 = this.path1;
            var4 = var1 * 2;
            var3.lineTo((float)AndroidUtilities.dp((float)var2[var4]), (float)AndroidUtilities.dp((float)playPath[var4 + 1]));
         }

         ++var1;
      }
   }

   public void draw(Canvas var1) {
      android.graphics.Rect var2 = this.getBounds();
      int var3 = var2.left + (var2.width() - this.getIntrinsicWidth()) / 2;
      int var4 = var2.top + (var2.height() - this.getIntrinsicHeight()) / 2;
      if (this.leftSide) {
         var3 -= var2.width() / 4 - AndroidUtilities.dp(16.0F);
      } else {
         var3 += var2.width() / 4 + AndroidUtilities.dp(16.0F);
      }

      var1.save();
      var1.clipRect(var2.left, var2.top, var2.right, var2.bottom);
      float var5 = this.animationProgress;
      if (var5 <= 0.7F) {
         this.paint.setAlpha((int)(Math.min(1.0F, var5 / 0.3F) * 80.0F));
      } else {
         this.paint.setAlpha((int)((1.0F - (var5 - 0.7F) / 0.3F) * 80.0F));
      }

      int var6 = Math.max(var2.width(), var2.height()) / 4;
      byte var7;
      if (this.leftSide) {
         var7 = -1;
      } else {
         var7 = 1;
      }

      var1.drawCircle((float)(var6 * var7 + var3), (float)(AndroidUtilities.dp(16.0F) + var4), (float)(Math.max(var2.width(), var2.height()) / 2), this.paint);
      var1.restore();
      var1.save();
      if (this.leftSide) {
         var1.rotate(180.0F, (float)var3, (float)(this.getIntrinsicHeight() / 2 + var4));
      }

      var1.translate((float)var3, (float)var4);
      var5 = this.animationProgress;
      if (var5 <= 0.6F) {
         if (var5 < 0.4F) {
            this.paint.setAlpha(Math.min(255, (int)(var5 * 255.0F / 0.2F)));
         } else {
            this.paint.setAlpha((int)((1.0F - (var5 - 0.4F) / 0.2F) * 255.0F));
         }

         var1.drawPath(this.path1, this.paint);
      }

      var1.translate((float)AndroidUtilities.dp(18.0F), 0.0F);
      var5 = this.animationProgress;
      if (var5 >= 0.2F && var5 <= 0.8F) {
         var5 -= 0.2F;
         if (var5 < 0.4F) {
            this.paint.setAlpha(Math.min(255, (int)(var5 * 255.0F / 0.2F)));
         } else {
            this.paint.setAlpha((int)((1.0F - (var5 - 0.4F) / 0.2F) * 255.0F));
         }

         var1.drawPath(this.path1, this.paint);
      }

      var1.translate((float)AndroidUtilities.dp(18.0F), 0.0F);
      var5 = this.animationProgress;
      if (var5 >= 0.4F && var5 <= 1.0F) {
         var5 -= 0.4F;
         if (var5 < 0.4F) {
            this.paint.setAlpha(Math.min(255, (int)(var5 * 255.0F / 0.2F)));
         } else {
            this.paint.setAlpha((int)((1.0F - (var5 - 0.4F) / 0.2F) * 255.0F));
         }

         var1.drawPath(this.path1, this.paint);
      }

      var1.restore();
      if (this.animating) {
         long var8 = System.currentTimeMillis();
         long var10 = var8 - this.lastAnimationTime;
         long var12 = var10;
         if (var10 > 17L) {
            var12 = 17L;
         }

         this.lastAnimationTime = var8;
         var5 = this.animationProgress;
         if (var5 < 1.0F) {
            this.animationProgress = var5 + (float)var12 / 800.0F;
            VideoForwardDrawable.VideoForwardDrawableDelegate var14;
            if (this.animationProgress >= 1.0F) {
               this.animationProgress = 0.0F;
               this.animating = false;
               var14 = this.delegate;
               if (var14 != null) {
                  var14.onAnimationEnd();
               }
            }

            var14 = this.delegate;
            if (var14 != null) {
               var14.invalidate();
            } else {
               this.invalidateSelf();
            }
         }
      }

   }

   public int getIntrinsicHeight() {
      return AndroidUtilities.dp(32.0F);
   }

   public int getIntrinsicWidth() {
      return AndroidUtilities.dp(32.0F);
   }

   public int getMinimumHeight() {
      return AndroidUtilities.dp(32.0F);
   }

   public int getMinimumWidth() {
      return AndroidUtilities.dp(32.0F);
   }

   public int getOpacity() {
      return -2;
   }

   public boolean isAnimating() {
      return this.animating;
   }

   public void setAlpha(int var1) {
      this.paint.setAlpha(var1);
   }

   public void setColor(int var1) {
      this.paint.setColor(var1);
   }

   public void setColorFilter(ColorFilter var1) {
      this.paint.setColorFilter(var1);
   }

   public void setDelegate(VideoForwardDrawable.VideoForwardDrawableDelegate var1) {
      this.delegate = var1;
   }

   public void setLeftSide(boolean var1) {
      if (this.leftSide != var1 || this.animationProgress < 1.0F) {
         this.leftSide = var1;
         this.startAnimation();
      }
   }

   public void startAnimation() {
      this.animating = true;
      this.animationProgress = 0.0F;
      this.invalidateSelf();
   }

   public interface VideoForwardDrawableDelegate {
      void invalidate();

      void onAnimationEnd();
   }
}
