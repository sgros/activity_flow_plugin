package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;

public class ProxyDrawable extends Drawable {
   private RectF cicleRect = new RectF();
   private boolean connected;
   private float connectedAnimationProgress;
   private int currentColorType;
   private Drawable emptyDrawable;
   private Drawable fullDrawable;
   private boolean isEnabled;
   private long lastUpdateTime;
   private Paint outerPaint = new Paint(1);
   private int radOffset = 0;

   public ProxyDrawable(Context var1) {
      this.emptyDrawable = var1.getResources().getDrawable(2131165795);
      this.fullDrawable = var1.getResources().getDrawable(2131165796);
      this.outerPaint.setStyle(Style.STROKE);
      this.outerPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0F));
      this.outerPaint.setStrokeCap(Cap.ROUND);
      this.lastUpdateTime = SystemClock.elapsedRealtime();
   }

   public void draw(Canvas var1) {
      long var2 = SystemClock.elapsedRealtime();
      long var4 = var2 - this.lastUpdateTime;
      this.lastUpdateTime = var2;
      if (!this.isEnabled) {
         this.emptyDrawable.setBounds(this.getBounds());
         this.emptyDrawable.draw(var1);
      } else if (!this.connected || this.connectedAnimationProgress != 1.0F) {
         this.emptyDrawable.setBounds(this.getBounds());
         this.emptyDrawable.draw(var1);
         this.outerPaint.setColor(Theme.getColor("contextProgressOuter2"));
         this.outerPaint.setAlpha((int)((1.0F - this.connectedAnimationProgress) * 255.0F));
         this.radOffset = (int)((float)this.radOffset + (float)(360L * var4) / 1000.0F);
         int var6 = this.getBounds().width();
         int var7 = this.getBounds().height();
         var6 = var6 / 2 - AndroidUtilities.dp(3.0F);
         var7 = var7 / 2 - AndroidUtilities.dp(3.0F);
         this.cicleRect.set((float)var6, (float)var7, (float)(var6 + AndroidUtilities.dp(6.0F)), (float)(var7 + AndroidUtilities.dp(6.0F)));
         var1.drawArc(this.cicleRect, (float)(this.radOffset - 90), 90.0F, false, this.outerPaint);
         this.invalidateSelf();
      }

      if (this.isEnabled && (this.connected || this.connectedAnimationProgress != 0.0F)) {
         this.fullDrawable.setAlpha((int)(this.connectedAnimationProgress * 255.0F));
         this.fullDrawable.setBounds(this.getBounds());
         this.fullDrawable.draw(var1);
      }

      float var8;
      if (this.connected) {
         var8 = this.connectedAnimationProgress;
         if (var8 != 1.0F) {
            this.connectedAnimationProgress = var8 + (float)var4 / 300.0F;
            if (this.connectedAnimationProgress > 1.0F) {
               this.connectedAnimationProgress = 1.0F;
            }

            this.invalidateSelf();
            return;
         }
      }

      if (!this.connected) {
         var8 = this.connectedAnimationProgress;
         if (var8 != 0.0F) {
            this.connectedAnimationProgress = var8 - (float)var4 / 300.0F;
            if (this.connectedAnimationProgress < 0.0F) {
               this.connectedAnimationProgress = 0.0F;
            }

            this.invalidateSelf();
         }
      }

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
      this.emptyDrawable.setColorFilter(var1);
      this.fullDrawable.setColorFilter(var1);
   }

   public void setConnected(boolean var1, boolean var2, boolean var3) {
      this.isEnabled = var1;
      this.connected = var2;
      this.lastUpdateTime = SystemClock.elapsedRealtime();
      if (!var3) {
         float var4;
         if (this.connected) {
            var4 = 1.0F;
         } else {
            var4 = 0.0F;
         }

         this.connectedAnimationProgress = var4;
      }

      this.invalidateSelf();
   }
}
