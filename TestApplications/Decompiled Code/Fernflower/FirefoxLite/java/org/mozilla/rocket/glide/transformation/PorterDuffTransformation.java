package org.mozilla.rocket.glide.transformation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class PorterDuffTransformation extends BitmapTransformation {
   private static final byte[] ID_BYTES;
   private static Paint paint = new Paint();
   private PorterDuffColorFilter porterDuffColorFilter;

   static {
      ID_BYTES = "org.mozilla.PorterDuffTransformation".getBytes(StandardCharsets.UTF_8);
   }

   public PorterDuffTransformation(PorterDuffColorFilter var1) {
      this.porterDuffColorFilter = var1;
   }

   public boolean equals(Object var1) {
      return var1 instanceof PorterDuffTransformation;
   }

   public int hashCode() {
      return "org.mozilla.PorterDuffTransformation".hashCode();
   }

   protected Bitmap transform(BitmapPool var1, Bitmap var2, int var3, int var4) {
      paint.setColorFilter(this.porterDuffColorFilter);
      Bitmap var5 = var1.get(var2.getWidth(), var2.getHeight(), var2.getConfig());
      (new Canvas(var5)).drawBitmap(var2, 0.0F, 0.0F, paint);
      return var5;
   }

   public void updateDiskCacheKey(MessageDigest var1) {
      var1.update(ID_BYTES);
   }
}
