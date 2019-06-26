package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;

public class RoundVideoPlayingDrawable extends Drawable {
   private long lastUpdateTime = 0L;
   private Paint paint = new Paint(1);
   private View parentView;
   private float progress1 = 0.47F;
   private int progress1Direction = 1;
   private float progress2 = 0.0F;
   private int progress2Direction = 1;
   private float progress3 = 0.32F;
   private int progress3Direction = 1;
   private boolean started = false;

   public RoundVideoPlayingDrawable(View var1) {
      this.parentView = var1;
   }

   private void update() {
      long var1 = System.currentTimeMillis();
      long var3 = var1 - this.lastUpdateTime;
      this.lastUpdateTime = var1;
      var1 = 50L;
      if (var3 > 50L) {
         var3 = var1;
      }

      float var5 = this.progress1;
      float var6 = (float)var3;
      this.progress1 = var5 + var6 / 300.0F * (float)this.progress1Direction;
      var5 = this.progress1;
      if (var5 > 1.0F) {
         this.progress1Direction = -1;
         this.progress1 = 1.0F;
      } else if (var5 < 0.0F) {
         this.progress1Direction = 1;
         this.progress1 = 0.0F;
      }

      this.progress2 += var6 / 310.0F * (float)this.progress2Direction;
      var5 = this.progress2;
      if (var5 > 1.0F) {
         this.progress2Direction = -1;
         this.progress2 = 1.0F;
      } else if (var5 < 0.0F) {
         this.progress2Direction = 1;
         this.progress2 = 0.0F;
      }

      this.progress3 += var6 / 320.0F * (float)this.progress3Direction;
      var6 = this.progress3;
      if (var6 > 1.0F) {
         this.progress3Direction = -1;
         this.progress3 = 1.0F;
      } else if (var6 < 0.0F) {
         this.progress3Direction = 1;
         this.progress3 = 0.0F;
      }

      this.parentView.invalidate();
   }

   public void draw(Canvas var1) {
      this.paint.setColor(Theme.getColor("chat_mediaTimeText"));
      int var2 = this.getBounds().left;
      int var3 = this.getBounds().top;

      for(int var4 = 0; var4 < 3; ++var4) {
         var1.drawRect((float)(AndroidUtilities.dp(2.0F) + var2), (float)(AndroidUtilities.dp(this.progress1 * 7.0F + 2.0F) + var3), (float)(AndroidUtilities.dp(4.0F) + var2), (float)(AndroidUtilities.dp(10.0F) + var3), this.paint);
         var1.drawRect((float)(AndroidUtilities.dp(5.0F) + var2), (float)(AndroidUtilities.dp(this.progress2 * 7.0F + 2.0F) + var3), (float)(AndroidUtilities.dp(7.0F) + var2), (float)(AndroidUtilities.dp(10.0F) + var3), this.paint);
         var1.drawRect((float)(AndroidUtilities.dp(8.0F) + var2), (float)(AndroidUtilities.dp(this.progress3 * 7.0F + 2.0F) + var3), (float)(AndroidUtilities.dp(10.0F) + var2), (float)(AndroidUtilities.dp(10.0F) + var3), this.paint);
      }

      if (this.started) {
         this.update();
      }

   }

   public int getIntrinsicHeight() {
      return AndroidUtilities.dp(12.0F);
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

   public void start() {
      if (!this.started) {
         this.lastUpdateTime = System.currentTimeMillis();
         this.started = true;
         this.parentView.invalidate();
      }
   }

   public void stop() {
      if (this.started) {
         this.started = false;
      }
   }
}
