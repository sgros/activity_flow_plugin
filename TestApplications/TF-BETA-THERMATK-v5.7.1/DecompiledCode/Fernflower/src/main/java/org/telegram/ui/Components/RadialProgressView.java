package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import androidx.annotation.Keep;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;

public class RadialProgressView extends View {
   private static final float risingTime = 500.0F;
   private static final float rotationTime = 2000.0F;
   private AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator();
   private RectF cicleRect = new RectF();
   private float currentCircleLength;
   private float currentProgressTime;
   private DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator();
   private long lastUpdateTime;
   private int progressColor = Theme.getColor("progressCircle");
   private Paint progressPaint = new Paint(1);
   private float radOffset;
   private boolean risingCircleLength;
   private int size = AndroidUtilities.dp(40.0F);
   private boolean useSelfAlpha;

   public RadialProgressView(Context var1) {
      super(var1);
      this.progressPaint.setStyle(Style.STROKE);
      this.progressPaint.setStrokeCap(Cap.ROUND);
      this.progressPaint.setStrokeWidth((float)AndroidUtilities.dp(3.0F));
      this.progressPaint.setColor(this.progressColor);
   }

   private void updateAnimation() {
      long var1 = System.currentTimeMillis();
      long var3 = var1 - this.lastUpdateTime;
      long var5 = var3;
      if (var3 > 17L) {
         var5 = 17L;
      }

      this.lastUpdateTime = var1;
      this.radOffset += (float)(360L * var5) / 2000.0F;
      float var7 = this.radOffset;
      this.radOffset = var7 - (float)((int)(var7 / 360.0F) * 360);
      this.currentProgressTime += (float)var5;
      if (this.currentProgressTime >= 500.0F) {
         this.currentProgressTime = 500.0F;
      }

      if (this.risingCircleLength) {
         this.currentCircleLength = this.accelerateInterpolator.getInterpolation(this.currentProgressTime / 500.0F) * 266.0F + 4.0F;
      } else {
         this.currentCircleLength = 4.0F - (1.0F - this.decelerateInterpolator.getInterpolation(this.currentProgressTime / 500.0F)) * 270.0F;
      }

      if (this.currentProgressTime == 500.0F) {
         if (this.risingCircleLength) {
            this.radOffset += 270.0F;
            this.currentCircleLength = -266.0F;
         }

         this.risingCircleLength ^= true;
         this.currentProgressTime = 0.0F;
      }

      this.invalidate();
   }

   protected void onDraw(Canvas var1) {
      int var2 = (this.getMeasuredWidth() - this.size) / 2;
      int var3 = this.getMeasuredHeight();
      int var4 = this.size;
      var3 = (var3 - var4) / 2;
      this.cicleRect.set((float)var2, (float)var3, (float)(var2 + var4), (float)(var3 + var4));
      var1.drawArc(this.cicleRect, this.radOffset, this.currentCircleLength, false, this.progressPaint);
      this.updateAnimation();
   }

   @Keep
   public void setAlpha(float var1) {
      super.setAlpha(var1);
      if (this.useSelfAlpha) {
         Drawable var2 = this.getBackground();
         int var3 = (int)(var1 * 255.0F);
         if (var2 != null) {
            var2.setAlpha(var3);
         }

         this.progressPaint.setAlpha(var3);
      }

   }

   public void setProgressColor(int var1) {
      this.progressColor = var1;
      this.progressPaint.setColor(this.progressColor);
   }

   public void setSize(int var1) {
      this.size = var1;
      this.invalidate();
   }

   public void setStrokeWidth(float var1) {
      this.progressPaint.setStrokeWidth((float)AndroidUtilities.dp(var1));
   }

   public void setUseSelfAlpha(boolean var1) {
      this.useSelfAlpha = var1;
   }
}
