package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.view.animation.DecelerateInterpolator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.ui.ActionBar.Theme;

public class TypingDotsDrawable extends StatusDrawable {
   private int currentAccount;
   private DecelerateInterpolator decelerateInterpolator;
   private float[] elapsedTimes;
   private boolean isChat;
   private long lastUpdateTime;
   private float[] scales;
   private float[] startTimes;
   private boolean started;

   public TypingDotsDrawable() {
      this.currentAccount = UserConfig.selectedAccount;
      this.isChat = false;
      this.scales = new float[3];
      this.startTimes = new float[]{0.0F, 150.0F, 300.0F};
      this.elapsedTimes = new float[]{0.0F, 0.0F, 0.0F};
      this.lastUpdateTime = 0L;
      this.started = false;
      this.decelerateInterpolator = new DecelerateInterpolator();
   }

   private void checkUpdate() {
      if (this.started) {
         if (!NotificationCenter.getInstance(this.currentAccount).isAnimationInProgress()) {
            this.update();
         } else {
            AndroidUtilities.runOnUIThread(new _$$Lambda$TypingDotsDrawable$6mZKSEfaAngfDGlsoqdZ2efS_EU(this), 100L);
         }
      }

   }

   // $FF: synthetic method
   public static void lambda$6mZKSEfaAngfDGlsoqdZ2efS_EU(TypingDotsDrawable var0) {
      var0.checkUpdate();
   }

   private void update() {
      long var1 = System.currentTimeMillis();
      long var3 = var1 - this.lastUpdateTime;
      this.lastUpdateTime = var1;
      var1 = 50L;
      if (var3 > 50L) {
         var3 = var1;
      }

      for(int var5 = 0; var5 < 3; ++var5) {
         float[] var6 = this.elapsedTimes;
         var6[var5] += (float)var3;
         float var7 = var6[var5];
         float[] var8 = this.startTimes;
         var7 -= var8[var5];
         if (var7 > 0.0F) {
            if (var7 <= 320.0F) {
               var7 = this.decelerateInterpolator.getInterpolation(var7 / 320.0F);
               this.scales[var5] = var7 + 1.33F;
            } else if (var7 <= 640.0F) {
               var7 = this.decelerateInterpolator.getInterpolation((var7 - 320.0F) / 320.0F);
               this.scales[var5] = 1.0F - var7 + 1.33F;
            } else if (var7 >= 800.0F) {
               var6[var5] = 0.0F;
               var8[var5] = 0.0F;
               this.scales[var5] = 1.33F;
            } else {
               this.scales[var5] = 1.33F;
            }
         } else {
            this.scales[var5] = 1.33F;
         }
      }

      this.invalidateSelf();
   }

   public void draw(Canvas var1) {
      int var2;
      int var3;
      if (this.isChat) {
         var2 = AndroidUtilities.dp(8.5F);
         var3 = this.getBounds().top;
      } else {
         var2 = AndroidUtilities.dp(9.3F);
         var3 = this.getBounds().top;
      }

      Theme.chat_statusPaint.setAlpha(255);
      float var4 = (float)AndroidUtilities.dp(3.0F);
      float var5 = (float)(var2 + var3);
      var1.drawCircle(var4, var5, this.scales[0] * AndroidUtilities.density, Theme.chat_statusPaint);
      var1.drawCircle((float)AndroidUtilities.dp(9.0F), var5, this.scales[1] * AndroidUtilities.density, Theme.chat_statusPaint);
      var1.drawCircle((float)AndroidUtilities.dp(15.0F), var5, this.scales[2] * AndroidUtilities.density, Theme.chat_statusPaint);
      this.checkUpdate();
   }

   public int getIntrinsicHeight() {
      return AndroidUtilities.dp(18.0F);
   }

   public int getIntrinsicWidth() {
      return AndroidUtilities.dp(18.0F);
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
      for(int var1 = 0; var1 < 3; ++var1) {
         this.elapsedTimes[var1] = 0.0F;
         this.scales[var1] = 1.33F;
      }

      float[] var2 = this.startTimes;
      var2[0] = 0.0F;
      var2[1] = 150.0F;
      var2[2] = 300.0F;
      this.started = false;
   }
}
