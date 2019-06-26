package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import androidx.annotation.Keep;
import org.telegram.messenger.AndroidUtilities;

public class AnimatedArrowDrawable extends Drawable {
   private float animProgress;
   private float animateToProgress;
   private boolean isSmall;
   private long lastUpdateTime;
   private Paint paint = new Paint(1);
   private Path path = new Path();

   public AnimatedArrowDrawable(int var1, boolean var2) {
      this.paint.setStyle(Style.STROKE);
      this.paint.setStrokeWidth((float)AndroidUtilities.dp(2.0F));
      this.paint.setColor(var1);
      this.paint.setStrokeCap(Cap.ROUND);
      this.paint.setStrokeJoin(Join.ROUND);
      this.isSmall = var2;
      this.updatePath();
   }

   private void checkAnimation() {
      if (this.animateToProgress != this.animProgress) {
         long var1 = SystemClock.elapsedRealtime();
         long var3 = var1 - this.lastUpdateTime;
         this.lastUpdateTime = var1;
         float var5 = this.animProgress;
         float var6 = this.animateToProgress;
         if (var5 < var6) {
            this.animProgress = var5 + (float)var3 / 180.0F;
            if (this.animProgress > var6) {
               this.animProgress = var6;
            }
         } else {
            this.animProgress = var5 - (float)var3 / 180.0F;
            if (this.animProgress < var6) {
               this.animProgress = var6;
            }
         }

         this.updatePath();
         this.invalidateSelf();
      }

   }

   private void updatePath() {
      this.path.reset();
      float var1 = this.animProgress * 2.0F - 1.0F;
      if (this.isSmall) {
         this.path.moveTo((float)AndroidUtilities.dp(3.0F), (float)AndroidUtilities.dp(6.0F) - (float)AndroidUtilities.dp(2.0F) * var1);
         this.path.lineTo((float)AndroidUtilities.dp(8.0F), (float)AndroidUtilities.dp(6.0F) + (float)AndroidUtilities.dp(2.0F) * var1);
         this.path.lineTo((float)AndroidUtilities.dp(13.0F), (float)AndroidUtilities.dp(6.0F) - (float)AndroidUtilities.dp(2.0F) * var1);
      } else {
         this.path.moveTo((float)AndroidUtilities.dp(4.5F), (float)AndroidUtilities.dp(12.0F) - (float)AndroidUtilities.dp(4.0F) * var1);
         this.path.lineTo((float)AndroidUtilities.dp(13.0F), (float)AndroidUtilities.dp(12.0F) + (float)AndroidUtilities.dp(4.0F) * var1);
         this.path.lineTo((float)AndroidUtilities.dp(21.5F), (float)AndroidUtilities.dp(12.0F) - (float)AndroidUtilities.dp(4.0F) * var1);
      }

   }

   public void draw(Canvas var1) {
      var1.drawPath(this.path, this.paint);
      this.checkAnimation();
   }

   public float getAnimationProgress() {
      return this.animProgress;
   }

   public int getIntrinsicHeight() {
      return AndroidUtilities.dp(26.0F);
   }

   public int getIntrinsicWidth() {
      return AndroidUtilities.dp(26.0F);
   }

   public int getOpacity() {
      return -2;
   }

   public void setAlpha(int var1) {
   }

   @Keep
   public void setAnimationProgress(float var1) {
      this.animProgress = var1;
      this.animateToProgress = var1;
      this.updatePath();
      this.invalidateSelf();
   }

   public void setAnimationProgressAnimated(float var1) {
      if (this.animateToProgress != var1) {
         this.animateToProgress = var1;
         this.lastUpdateTime = SystemClock.elapsedRealtime();
         this.invalidateSelf();
      }
   }

   public void setColor(int var1) {
      this.paint.setColor(var1);
   }

   public void setColorFilter(ColorFilter var1) {
      this.paint.setColorFilter(var1);
   }
}
