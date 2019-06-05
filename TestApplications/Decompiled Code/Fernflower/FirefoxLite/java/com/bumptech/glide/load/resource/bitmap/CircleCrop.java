package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import java.security.MessageDigest;

public class CircleCrop extends BitmapTransformation {
   private static final byte[] ID_BYTES;

   static {
      ID_BYTES = "com.bumptech.glide.load.resource.bitmap.CircleCrop.1".getBytes(CHARSET);
   }

   public boolean equals(Object var1) {
      return var1 instanceof CircleCrop;
   }

   public int hashCode() {
      return "com.bumptech.glide.load.resource.bitmap.CircleCrop.1".hashCode();
   }

   protected Bitmap transform(BitmapPool var1, Bitmap var2, int var3, int var4) {
      return TransformationUtils.circleCrop(var1, var2, var3, var4);
   }

   public void updateDiskCacheKey(MessageDigest var1) {
      var1.update(ID_BYTES);
   }
}