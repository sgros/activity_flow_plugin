package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.AndroidUtilities;

public class ShareLocationDrawable extends Drawable {
   private Drawable drawable;
   private Drawable drawableLeft;
   private Drawable drawableRight;
   private boolean isSmall;
   private long lastUpdateTime = 0L;
   private float[] progress = new float[]{0.0F, -0.5F};

   public ShareLocationDrawable(Context var1, boolean var2) {
      this.isSmall = var2;
      if (var2) {
         this.drawable = var1.getResources().getDrawable(2131165830);
         this.drawableLeft = var1.getResources().getDrawable(2131165831);
         this.drawableRight = var1.getResources().getDrawable(2131165832);
      } else {
         this.drawable = var1.getResources().getDrawable(2131165281);
         this.drawableLeft = var1.getResources().getDrawable(2131165282);
         this.drawableRight = var1.getResources().getDrawable(2131165283);
      }

   }

   private void update() {
      long var1 = System.currentTimeMillis();
      long var3 = var1 - this.lastUpdateTime;
      this.lastUpdateTime = var1;
      var1 = 16L;
      if (var3 > 16L) {
         var3 = var1;
      }

      for(int var5 = 0; var5 < 2; ++var5) {
         float[] var6 = this.progress;
         if (var6[var5] >= 1.0F) {
            var6[var5] = 0.0F;
         }

         var6 = this.progress;
         var6[var5] += (float)var3 / 1300.0F;
         if (var6[var5] > 1.0F) {
            var6[var5] = 1.0F;
         }
      }

      this.invalidateSelf();
   }

   public void draw(Canvas var1) {
      float var2;
      if (this.isSmall) {
         var2 = 30.0F;
      } else {
         var2 = 120.0F;
      }

      int var3 = AndroidUtilities.dp(var2);
      int var4 = this.getBounds().top + (this.getIntrinsicHeight() - var3) / 2;
      int var5 = this.getBounds().left + (this.getIntrinsicWidth() - var3) / 2;
      Drawable var6 = this.drawable;
      var6.setBounds(var5, var4, var6.getIntrinsicWidth() + var5, this.drawable.getIntrinsicHeight() + var4);
      this.drawable.draw(var1);

      for(var3 = 0; var3 < 2; ++var3) {
         float[] var17 = this.progress;
         if (var17[var3] >= 0.0F) {
            float var7 = var17[var3] * 0.5F + 0.5F;
            if (this.isSmall) {
               var2 = 2.5F;
            } else {
               var2 = 5.0F;
            }

            int var8 = AndroidUtilities.dp(var2 * var7);
            if (this.isSmall) {
               var2 = 6.5F;
            } else {
               var2 = 18.0F;
            }

            int var9 = AndroidUtilities.dp(var2 * var7);
            if (this.isSmall) {
               var2 = 6.0F;
            } else {
               var2 = 15.0F;
            }

            int var10 = AndroidUtilities.dp(var2 * this.progress[var3]);
            var17 = this.progress;
            if (var17[var3] < 0.5F) {
               var2 = var17[var3] / 0.5F;
            } else {
               var2 = 1.0F - (var17[var3] - 0.5F) / 0.5F;
            }

            boolean var11 = this.isSmall;
            float var12 = 42.0F;
            if (var11) {
               var7 = 7.0F;
            } else {
               var7 = 42.0F;
            }

            int var13 = AndroidUtilities.dp(var7) + var5 - var10;
            int var14 = this.drawable.getIntrinsicHeight() / 2;
            int var15;
            if (this.isSmall) {
               var15 = 0;
            } else {
               var15 = AndroidUtilities.dp(7.0F);
            }

            int var16 = var14 + var4 - var15;
            var6 = this.drawableLeft;
            var14 = (int)(var2 * 255.0F);
            var6.setAlpha(var14);
            var6 = this.drawableLeft;
            var15 = var16 - var9;
            var9 += var16;
            var6.setBounds(var13 - var8, var15, var13 + var8, var9);
            this.drawableLeft.draw(var1);
            var13 = this.drawable.getIntrinsicWidth();
            var2 = var12;
            if (this.isSmall) {
               var2 = 7.0F;
            }

            var10 += var13 + var5 - AndroidUtilities.dp(var2);
            this.drawableRight.setAlpha(var14);
            this.drawableRight.setBounds(var10 - var8, var15, var10 + var8, var9);
            this.drawableRight.draw(var1);
         }
      }

      this.update();
   }

   public int getIntrinsicHeight() {
      float var1;
      if (this.isSmall) {
         var1 = 40.0F;
      } else {
         var1 = 180.0F;
      }

      return AndroidUtilities.dp(var1);
   }

   public int getIntrinsicWidth() {
      float var1;
      if (this.isSmall) {
         var1 = 40.0F;
      } else {
         var1 = 120.0F;
      }

      return AndroidUtilities.dp(var1);
   }

   public int getOpacity() {
      return 0;
   }

   public void setAlpha(int var1) {
   }

   public void setColorFilter(ColorFilter var1) {
      this.drawable.setColorFilter(var1);
      this.drawableLeft.setColorFilter(var1);
      this.drawableRight.setColorFilter(var1);
   }
}
