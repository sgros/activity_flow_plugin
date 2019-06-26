package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.drawable.Drawable;
import android.view.animation.DecelerateInterpolator;
import org.telegram.messenger.AndroidUtilities;

public class CloseProgressDrawable extends Drawable {
   private int currentAnimationTime;
   private int currentSegment;
   private DecelerateInterpolator interpolator = new DecelerateInterpolator();
   private long lastFrameTime;
   private Paint paint = new Paint(1);

   public CloseProgressDrawable() {
      this.paint.setColor(-9079435);
      this.paint.setStrokeWidth((float)AndroidUtilities.dp(2.0F));
      this.paint.setStrokeCap(Cap.ROUND);
   }

   public void draw(Canvas var1) {
      long var2 = System.currentTimeMillis();
      long var4 = this.lastFrameTime;
      if (var4 != 0L) {
         this.currentAnimationTime = (int)((long)this.currentAnimationTime + (var2 - var4));
         if (this.currentAnimationTime > 200) {
            this.currentAnimationTime = 0;
            ++this.currentSegment;
            int var6 = this.currentSegment;
            if (var6 == 4) {
               this.currentSegment = var6 - 4;
            }
         }
      }

      var1.save();
      var1.translate((float)(this.getIntrinsicWidth() / 2), (float)(this.getIntrinsicHeight() / 2));
      var1.rotate(45.0F);
      this.paint.setAlpha(255 - this.currentSegment % 4 * 40);
      var1.drawLine((float)(-AndroidUtilities.dp(8.0F)), 0.0F, 0.0F, 0.0F, this.paint);
      this.paint.setAlpha(255 - (this.currentSegment + 1) % 4 * 40);
      var1.drawLine(0.0F, (float)(-AndroidUtilities.dp(8.0F)), 0.0F, 0.0F, this.paint);
      this.paint.setAlpha(255 - (this.currentSegment + 2) % 4 * 40);
      var1.drawLine(0.0F, 0.0F, (float)AndroidUtilities.dp(8.0F), 0.0F, this.paint);
      this.paint.setAlpha(255 - (this.currentSegment + 3) % 4 * 40);
      var1.drawLine(0.0F, 0.0F, 0.0F, (float)AndroidUtilities.dp(8.0F), this.paint);
      var1.restore();
      this.lastFrameTime = var2;
      this.invalidateSelf();
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
}
