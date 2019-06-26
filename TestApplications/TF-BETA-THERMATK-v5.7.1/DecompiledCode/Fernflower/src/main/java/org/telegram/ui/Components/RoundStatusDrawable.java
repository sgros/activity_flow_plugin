package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;

public class RoundStatusDrawable extends StatusDrawable {
   private boolean isChat = false;
   private long lastUpdateTime = 0L;
   private float progress;
   private int progressDirection = 1;
   private boolean started = false;

   private void update() {
      long var1 = System.currentTimeMillis();
      long var3 = var1 - this.lastUpdateTime;
      this.lastUpdateTime = var1;
      var1 = 50L;
      if (var3 > 50L) {
         var3 = var1;
      }

      float var5 = this.progress;
      int var6 = this.progressDirection;
      this.progress = var5 + (float)((long)var6 * var3) / 400.0F;
      if (var6 > 0 && this.progress >= 1.0F) {
         this.progressDirection = -1;
         this.progress = 1.0F;
      } else if (this.progressDirection < 0 && this.progress <= 0.0F) {
         this.progressDirection = 1;
         this.progress = 0.0F;
      }

      this.invalidateSelf();
   }

   public void draw(Canvas var1) {
      Theme.chat_statusPaint.setAlpha((int)(this.progress * 200.0F) + 55);
      float var2 = (float)AndroidUtilities.dp(6.0F);
      float var3;
      if (this.isChat) {
         var3 = 8.0F;
      } else {
         var3 = 9.0F;
      }

      var1.drawCircle(var2, (float)AndroidUtilities.dp(var3), (float)AndroidUtilities.dp(4.0F), Theme.chat_statusPaint);
      if (this.started) {
         this.update();
      }

   }

   public int getIntrinsicHeight() {
      return AndroidUtilities.dp(10.0F);
   }

   public int getIntrinsicWidth() {
      return AndroidUtilities.dp(12.0F);
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
