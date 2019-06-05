package org.mozilla.rocket.glide.transformation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class ShrinkSizeTransformation extends BitmapTransformation {
   private static final byte[] ID_BYTES;
   private static Paint paint = new Paint();
   private double scale;

   static {
      ID_BYTES = "org.mozilla.PorterDuffTransformation".getBytes(StandardCharsets.UTF_8);
   }

   public ShrinkSizeTransformation(double var1) {
      if (var1 < 1.0D) {
         this.scale = var1;
      } else {
         throw new IllegalArgumentException("Scale should be <= 1");
      }
   }

   private int margin(int var1, double var2) {
      return (int)((double)var1 * (1.0D / var2 - 1.0D) / 2.0D);
   }

   public boolean equals(Object var1) {
      return var1 instanceof ShrinkSizeTransformation;
   }

   public int hashCode() {
      return "org.mozilla.PorterDuffTransformation".hashCode();
   }

   protected Bitmap transform(BitmapPool var1, Bitmap var2, int var3, int var4) {
      Bitmap var5 = var1.get((int)((double)var2.getWidth() / this.scale), (int)((double)var2.getHeight() / this.scale), var2.getConfig());
      (new Canvas(var5)).drawBitmap(var2, (float)this.margin(var2.getWidth(), this.scale), (float)this.margin(var2.getHeight(), this.scale), paint);
      return var5;
   }

   public void updateDiskCacheKey(MessageDigest var1) {
      var1.update(ID_BYTES);
   }
}
