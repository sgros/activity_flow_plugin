package org.telegram.ui.Components.voip;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.AndroidUtilities;

public class FabBackgroundDrawable extends Drawable {
   private Paint bgPaint = new Paint(1);
   private Bitmap shadowBitmap;
   private Paint shadowPaint = new Paint();

   public FabBackgroundDrawable() {
      this.shadowPaint.setColor(1275068416);
   }

   public void draw(Canvas var1) {
      if (this.shadowBitmap == null) {
         this.onBoundsChange(this.getBounds());
      }

      int var2 = Math.min(this.getBounds().width(), this.getBounds().height());
      Bitmap var3 = this.shadowBitmap;
      if (var3 != null) {
         var1.drawBitmap(var3, (float)(this.getBounds().centerX() - this.shadowBitmap.getWidth() / 2), (float)(this.getBounds().centerY() - this.shadowBitmap.getHeight() / 2), this.shadowPaint);
      }

      var2 /= 2;
      float var4 = (float)var2;
      var1.drawCircle(var4, var4, (float)(var2 - AndroidUtilities.dp(4.0F)), this.bgPaint);
   }

   public int getOpacity() {
      return 0;
   }

   public boolean getPadding(Rect var1) {
      int var2 = AndroidUtilities.dp(4.0F);
      var1.set(var2, var2, var2, var2);
      return true;
   }

   protected void onBoundsChange(Rect var1) {
      int var2 = Math.min(var1.width(), var1.height());
      if (var2 <= 0) {
         this.shadowBitmap = null;
      } else {
         this.shadowBitmap = Bitmap.createBitmap(var2, var2, Config.ALPHA_8);
         Canvas var5 = new Canvas(this.shadowBitmap);
         Paint var3 = new Paint(1);
         var3.setShadowLayer((float)AndroidUtilities.dp(3.33333F), 0.0F, (float)AndroidUtilities.dp(0.666F), -1);
         var2 /= 2;
         float var4 = (float)var2;
         var5.drawCircle(var4, var4, (float)(var2 - AndroidUtilities.dp(4.0F)), var3);
      }
   }

   public void setAlpha(int var1) {
   }

   public void setColor(int var1) {
      this.bgPaint.setColor(var1);
      this.invalidateSelf();
   }

   public void setColorFilter(ColorFilter var1) {
   }
}
