package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.AndroidUtilities;

public class MapPlaceholderDrawable extends Drawable {
   private Paint linePaint;
   private Paint paint = new Paint();

   public MapPlaceholderDrawable() {
      this.paint.setColor(-2172970);
      this.linePaint = new Paint();
      this.linePaint.setColor(-3752002);
      this.linePaint.setStrokeWidth((float)AndroidUtilities.dp(1.0F));
   }

   public void draw(Canvas var1) {
      var1.drawRect(this.getBounds(), this.paint);
      int var2 = AndroidUtilities.dp(9.0F);
      int var3 = this.getBounds().width() / var2;
      int var4 = this.getBounds().height() / var2;
      int var5 = this.getBounds().left;
      int var6 = this.getBounds().top;
      byte var7 = 0;
      int var8 = 0;

      while(true) {
         int var9 = var7;
         float var10;
         if (var8 >= var3) {
            while(var9 < var4) {
               var10 = (float)var5;
               ++var9;
               float var11 = (float)(var2 * var9 + var6);
               var1.drawLine(var10, var11, (float)(this.getBounds().width() + var5), var11, this.linePaint);
            }

            return;
         }

         ++var8;
         var10 = (float)(var2 * var8 + var5);
         var1.drawLine(var10, (float)var6, var10, (float)(this.getBounds().height() + var6), this.linePaint);
      }
   }

   public int getIntrinsicHeight() {
      return 0;
   }

   public int getIntrinsicWidth() {
      return 0;
   }

   public int getOpacity() {
      return 0;
   }

   public void setAlpha(int var1) {
   }

   public void setColorFilter(ColorFilter var1) {
   }
}
