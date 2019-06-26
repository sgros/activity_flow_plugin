package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.tgnet.TLRPC;

public class IdenticonDrawable extends Drawable {
   private int[] colors = new int[]{-1, -2758925, -13805707, -13657655};
   private byte[] data;
   private Paint paint = new Paint();

   private int getBits(int var1) {
      return this.data[var1 / 8] >> var1 % 8 & 3;
   }

   public void draw(Canvas var1) {
      byte[] var2 = this.data;
      if (var2 != null) {
         float var3;
         float var4;
         float var5;
         int var6;
         int var7;
         int var8;
         int var9;
         float var10;
         float var11;
         if (var2.length == 16) {
            var3 = (float)Math.floor((double)((float)Math.min(this.getBounds().width(), this.getBounds().height()) / 8.0F));
            var4 = (float)this.getBounds().width();
            var5 = 8.0F * var3;
            var4 = Math.max(0.0F, (var4 - var5) / 2.0F);
            var5 = Math.max(0.0F, ((float)this.getBounds().height() - var5) / 2.0F);
            var6 = 0;

            for(var7 = 0; var6 < 8; ++var6) {
               for(var8 = 0; var8 < 8; ++var8) {
                  var9 = this.getBits(var7);
                  var7 += 2;
                  var9 = Math.abs(var9);
                  this.paint.setColor(this.colors[var9 % 4]);
                  var10 = var4 + (float)var8 * var3;
                  var11 = (float)var6 * var3;
                  var1.drawRect(var10, var11 + var5, var10 + var3, var11 + var3 + var5, this.paint);
               }
            }
         } else {
            var3 = (float)Math.floor((double)((float)Math.min(this.getBounds().width(), this.getBounds().height()) / 12.0F));
            var4 = (float)this.getBounds().width();
            var5 = 12.0F * var3;
            var4 = Math.max(0.0F, (var4 - var5) / 2.0F);
            var11 = Math.max(0.0F, ((float)this.getBounds().height() - var5) / 2.0F);
            var6 = 0;

            for(var7 = 0; var6 < 12; ++var6) {
               for(var8 = 0; var8 < 12; ++var8) {
                  var9 = Math.abs(this.getBits(var7));
                  this.paint.setColor(this.colors[var9 % 4]);
                  var5 = var4 + (float)var8 * var3;
                  var10 = (float)var6 * var3;
                  var1.drawRect(var5, var10 + var11, var5 + var3, var10 + var3 + var11, this.paint);
                  var7 += 2;
               }
            }
         }

      }
   }

   public int getIntrinsicHeight() {
      return AndroidUtilities.dp(32.0F);
   }

   public int getIntrinsicWidth() {
      return AndroidUtilities.dp(32.0F);
   }

   public int getOpacity() {
      return 0;
   }

   public void setAlpha(int var1) {
   }

   public void setColorFilter(ColorFilter var1) {
   }

   public void setColors(int[] var1) {
      if (this.colors.length == 4) {
         this.colors = var1;
         this.invalidateSelf();
      } else {
         throw new IllegalArgumentException("colors must have length of 4");
      }
   }

   public void setEncryptedChat(TLRPC.EncryptedChat var1) {
      this.data = var1.key_hash;
      if (this.data == null) {
         byte[] var2 = AndroidUtilities.calcAuthKeyHash(var1.auth_key);
         this.data = var2;
         var1.key_hash = var2;
      }

      this.invalidateSelf();
   }
}
