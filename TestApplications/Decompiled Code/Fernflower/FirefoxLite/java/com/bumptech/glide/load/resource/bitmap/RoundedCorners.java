package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Preconditions;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

public final class RoundedCorners extends BitmapTransformation {
   private static final byte[] ID_BYTES;
   private final int roundingRadius;

   static {
      ID_BYTES = "com.bumptech.glide.load.resource.bitmap.RoundedCorners".getBytes(CHARSET);
   }

   public RoundedCorners(int var1) {
      boolean var2;
      if (var1 > 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      Preconditions.checkArgument(var2, "roundingRadius must be greater than 0.");
      this.roundingRadius = var1;
   }

   public boolean equals(Object var1) {
      boolean var2;
      if (var1 instanceof RoundedCorners && ((RoundedCorners)var1).roundingRadius == this.roundingRadius) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public int hashCode() {
      return "com.bumptech.glide.load.resource.bitmap.RoundedCorners".hashCode() + this.roundingRadius;
   }

   protected Bitmap transform(BitmapPool var1, Bitmap var2, int var3, int var4) {
      return TransformationUtils.roundedCorners(var1, var2, var3, var4, this.roundingRadius);
   }

   public void updateDiskCacheKey(MessageDigest var1) {
      var1.update(ID_BYTES);
      var1.update(ByteBuffer.allocate(4).putInt(this.roundingRadius).array());
   }
}
