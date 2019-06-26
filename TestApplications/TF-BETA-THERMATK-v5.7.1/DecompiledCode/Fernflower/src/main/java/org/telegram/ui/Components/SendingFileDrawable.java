package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;

public class SendingFileDrawable extends StatusDrawable {
   private boolean isChat = false;
   private long lastUpdateTime = 0L;
   private float progress;
   private boolean started = false;

   private void update() {
      long var1 = System.currentTimeMillis();
      long var3 = var1 - this.lastUpdateTime;
      this.lastUpdateTime = var1;
      var1 = 50L;
      if (var3 > 50L) {
         var3 = var1;
      }

      this.progress += (float)var3 / 500.0F;

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
      for(int var2 = 0; var2 < 3; ++var2) {
         if (var2 == 0) {
            Theme.chat_statusRecordPaint.setAlpha((int)(this.progress * 255.0F));
         } else if (var2 == 2) {
            Theme.chat_statusRecordPaint.setAlpha((int)((1.0F - this.progress) * 255.0F));
         } else {
            Theme.chat_statusRecordPaint.setAlpha(255);
         }

         float var3 = (float)(AndroidUtilities.dp(5.0F) * var2);
         float var4 = (float)AndroidUtilities.dp(5.0F) * this.progress + var3;
         if (this.isChat) {
            var3 = 3.0F;
         } else {
            var3 = 4.0F;
         }

         float var5 = (float)AndroidUtilities.dp(var3);
         float var6 = (float)AndroidUtilities.dp(4.0F);
         boolean var7 = this.isChat;
         float var8 = 7.0F;
         if (var7) {
            var3 = 7.0F;
         } else {
            var3 = 8.0F;
         }

         var1.drawLine(var4, var5, var4 + var6, (float)AndroidUtilities.dp(var3), Theme.chat_statusRecordPaint);
         if (this.isChat) {
            var3 = 11.0F;
         } else {
            var3 = 12.0F;
         }

         var6 = (float)AndroidUtilities.dp(var3);
         var5 = (float)AndroidUtilities.dp(4.0F);
         if (this.isChat) {
            var3 = var8;
         } else {
            var3 = 8.0F;
         }

         var1.drawLine(var4, var6, var4 + var5, (float)AndroidUtilities.dp(var3), Theme.chat_statusRecordPaint);
      }

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
