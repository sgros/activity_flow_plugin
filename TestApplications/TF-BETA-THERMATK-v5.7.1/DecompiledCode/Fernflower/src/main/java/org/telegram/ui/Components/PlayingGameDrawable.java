package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.ui.ActionBar.Theme;

public class PlayingGameDrawable extends StatusDrawable {
   private int currentAccount;
   private boolean isChat = false;
   private long lastUpdateTime;
   private Paint paint = new Paint(1);
   private float progress;
   private RectF rect;
   private boolean started;

   public PlayingGameDrawable() {
      this.currentAccount = UserConfig.selectedAccount;
      this.lastUpdateTime = 0L;
      this.started = false;
      this.rect = new RectF();
   }

   private void checkUpdate() {
      if (this.started) {
         if (!NotificationCenter.getInstance(this.currentAccount).isAnimationInProgress()) {
            this.update();
         } else {
            AndroidUtilities.runOnUIThread(new _$$Lambda$PlayingGameDrawable$65ulwJDGFoIbDH0sYp9eXDBVUBc(this), 100L);
         }
      }

   }

   // $FF: synthetic method
   public static void lambda$65ulwJDGFoIbDH0sYp9eXDBVUBc(PlayingGameDrawable var0) {
      var0.checkUpdate();
   }

   private void update() {
      long var1 = System.currentTimeMillis();
      long var3 = var1 - this.lastUpdateTime;
      this.lastUpdateTime = var1;
      var1 = 16L;
      if (var3 > 16L) {
         var3 = var1;
      }

      if (this.progress >= 1.0F) {
         this.progress = 0.0F;
      }

      this.progress += (float)var3 / 300.0F;
      if (this.progress > 1.0F) {
         this.progress = 1.0F;
      }

      this.invalidateSelf();
   }

   public void draw(Canvas var1) {
      int var2 = AndroidUtilities.dp(10.0F);
      int var3 = this.getBounds().top + (this.getIntrinsicHeight() - var2) / 2;
      if (!this.isChat) {
         var3 += AndroidUtilities.dp(1.0F);
      }

      this.paint.setColor(Theme.getColor("chat_status"));
      this.rect.set(0.0F, (float)var3, (float)var2, (float)(var3 + var2));
      float var4 = this.progress;
      if (var4 < 0.5F) {
         var4 = (1.0F - var4 / 0.5F) * 35.0F;
      } else {
         var4 = (var4 - 0.5F) * 35.0F / 0.5F;
      }

      int var5 = (int)var4;

      for(int var6 = 0; var6 < 3; ++var6) {
         float var7 = (float)(AndroidUtilities.dp(5.0F) * var6 + AndroidUtilities.dp(9.2F));
         var4 = (float)AndroidUtilities.dp(5.0F);
         float var8 = this.progress;
         if (var6 == 2) {
            this.paint.setAlpha(Math.min(255, (int)(var8 * 255.0F / 0.5F)));
         } else if (var6 == 0) {
            if (var8 > 0.5F) {
               this.paint.setAlpha((int)((1.0F - (var8 - 0.5F) / 0.5F) * 255.0F));
            } else {
               this.paint.setAlpha(255);
            }
         } else {
            this.paint.setAlpha(255);
         }

         var1.drawCircle(var7 - var4 * var8, (float)(var2 / 2 + var3), (float)AndroidUtilities.dp(1.2F), this.paint);
      }

      this.paint.setAlpha(255);
      var1.drawArc(this.rect, (float)var5, (float)(360 - var5 * 2), true, this.paint);
      this.paint.setColor(Theme.getColor("actionBarDefault"));
      var1.drawCircle((float)AndroidUtilities.dp(4.0F), (float)(var3 + var2 / 2 - AndroidUtilities.dp(2.0F)), (float)AndroidUtilities.dp(1.0F), this.paint);
      this.checkUpdate();
   }

   public int getIntrinsicHeight() {
      return AndroidUtilities.dp(18.0F);
   }

   public int getIntrinsicWidth() {
      return AndroidUtilities.dp(20.0F);
   }

   public int getOpacity() {
      return -2;
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
      this.progress = 0.0F;
      this.started = false;
   }
}
