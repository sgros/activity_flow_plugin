package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import org.telegram.messenger.AndroidUtilities;

public class LineProgressView extends View {
   private static DecelerateInterpolator decelerateInterpolator;
   private static Paint progressPaint;
   private float animatedAlphaValue = 1.0F;
   private float animatedProgressValue;
   private float animationProgressStart;
   private int backColor;
   private float currentProgress;
   private long currentProgressTime;
   private long lastUpdateTime;
   private int progressColor;

   public LineProgressView(Context var1) {
      super(var1);
      if (decelerateInterpolator == null) {
         decelerateInterpolator = new DecelerateInterpolator();
         progressPaint = new Paint(1);
         progressPaint.setStrokeCap(Cap.ROUND);
         progressPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0F));
      }

   }

   private void updateAnimation() {
      long var1 = System.currentTimeMillis();
      long var3 = var1 - this.lastUpdateTime;
      this.lastUpdateTime = var1;
      float var5 = this.animatedProgressValue;
      float var6;
      if (var5 != 1.0F) {
         var6 = this.currentProgress;
         if (var5 != var6) {
            var5 = this.animationProgressStart;
            float var7 = var6 - var5;
            if (var7 > 0.0F) {
               this.currentProgressTime += var3;
               var1 = this.currentProgressTime;
               if (var1 >= 300L) {
                  this.animatedProgressValue = var6;
                  this.animationProgressStart = var6;
                  this.currentProgressTime = 0L;
               } else {
                  this.animatedProgressValue = var5 + var7 * decelerateInterpolator.getInterpolation((float)var1 / 300.0F);
               }
            }

            this.invalidate();
         }
      }

      var6 = this.animatedProgressValue;
      if (var6 >= 1.0F && var6 == 1.0F) {
         var6 = this.animatedAlphaValue;
         if (var6 != 0.0F) {
            this.animatedAlphaValue = var6 - (float)var3 / 200.0F;
            if (this.animatedAlphaValue <= 0.0F) {
               this.animatedAlphaValue = 0.0F;
            }

            this.invalidate();
         }
      }

   }

   public float getCurrentProgress() {
      return this.currentProgress;
   }

   public void onDraw(Canvas var1) {
      int var2 = this.backColor;
      if (var2 != 0 && this.animatedProgressValue != 1.0F) {
         progressPaint.setColor(var2);
         progressPaint.setAlpha((int)(this.animatedAlphaValue * 255.0F));
         var1.drawRect((float)((int)((float)this.getWidth() * this.animatedProgressValue)), 0.0F, (float)this.getWidth(), (float)this.getHeight(), progressPaint);
      }

      progressPaint.setColor(this.progressColor);
      progressPaint.setAlpha((int)(this.animatedAlphaValue * 255.0F));
      var1.drawRect(0.0F, 0.0F, (float)this.getWidth() * this.animatedProgressValue, (float)this.getHeight(), progressPaint);
      this.updateAnimation();
   }

   public void setBackColor(int var1) {
      this.backColor = var1;
   }

   public void setProgress(float var1, boolean var2) {
      if (!var2) {
         this.animatedProgressValue = var1;
         this.animationProgressStart = var1;
      } else {
         this.animationProgressStart = this.animatedProgressValue;
      }

      if (var1 != 1.0F) {
         this.animatedAlphaValue = 1.0F;
      }

      this.currentProgress = var1;
      this.currentProgressTime = 0L;
      this.lastUpdateTime = System.currentTimeMillis();
      this.invalidate();
   }

   public void setProgressColor(int var1) {
      this.progressColor = var1;
   }
}
