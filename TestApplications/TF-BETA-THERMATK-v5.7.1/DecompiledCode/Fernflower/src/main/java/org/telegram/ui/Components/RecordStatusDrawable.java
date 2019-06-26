package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.RectF;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;

public class RecordStatusDrawable extends StatusDrawable {
   private boolean isChat = false;
   private long lastUpdateTime = 0L;
   private float progress;
   private RectF rect = new RectF();
   private boolean started = false;

   private void update() {
      long var1 = System.currentTimeMillis();
      long var3 = var1 - this.lastUpdateTime;
      this.lastUpdateTime = var1;
      var1 = 50L;
      if (var3 > 50L) {
         var3 = var1;
      }

      this.progress += (float)var3 / 800.0F;

      while(true) {
         float var5 = this.progress;
         if (var5 <= 1.0F) {
            this.invalidateSelf();
            return;
         }

         this.progress = var5 - 1.0F;
      }
   }

   public void draw(Canvas var1) {
      var1.save();
      int var2 = this.getIntrinsicHeight() / 2;
      float var3;
      if (this.isChat) {
         var3 = 1.0F;
      } else {
         var3 = 2.0F;
      }

      var1.translate(0.0F, (float)(var2 + AndroidUtilities.dp(var3)));

      for(var2 = 0; var2 < 4; ++var2) {
         if (var2 == 0) {
            Theme.chat_statusRecordPaint.setAlpha((int)(this.progress * 255.0F));
         } else if (var2 == 3) {
            Theme.chat_statusRecordPaint.setAlpha((int)((1.0F - this.progress) * 255.0F));
         } else {
            Theme.chat_statusRecordPaint.setAlpha(255);
         }

         var3 = (float)(AndroidUtilities.dp(4.0F) * var2) + (float)AndroidUtilities.dp(4.0F) * this.progress;
         RectF var4 = this.rect;
         float var5 = -var3;
         var4.set(var5, var5, var3, var3);
         var1.drawArc(this.rect, -15.0F, 30.0F, false, Theme.chat_statusRecordPaint);
      }

      var1.restore();
      if (this.started) {
         this.update();
      }

   }

   public int getIntrinsicHeight() {
      return AndroidUtilities.dp(14.0F);
   }

   public int getIntrinsicWidth() {
      return AndroidUtilities.dp(18.0F);
   }

   public int getOpacity() {
      return 0;
   }

   public void setAlpha(int var1) {
   }

   public void setColorFilter(ColorFilter var1) {
   }

   public void setIsChat(boolean var1) {
      this.isChat = var1;
   }

   public void start() {
      this.lastUpdateTime = System.currentTimeMillis();
      this.started = true;
      this.invalidateSelf();
   }

   public void stop() {
      this.started = false;
   }
}
