package com.bumptech.glide.load.resource.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Util;

public abstract class BitmapTransformation implements Transformation {
   protected abstract Bitmap transform(BitmapPool var1, Bitmap var2, int var3, int var4);

   public final Resource transform(Context var1, Resource var2, int var3, int var4) {
      if (Util.isValidDimensions(var3, var4)) {
         BitmapPool var5 = Glide.get(var1).getBitmapPool();
         Bitmap var9 = (Bitmap)((Resource)var2).get();
         int var6 = var3;
         if (var3 == Integer.MIN_VALUE) {
            var6 = var9.getWidth();
         }

         var3 = var4;
         if (var4 == Integer.MIN_VALUE) {
            var3 = var9.getHeight();
         }

         Bitmap var7 = this.transform(var5, var9, var6, var3);
         if (!var9.equals(var7)) {
            var2 = BitmapResource.obtain(var7, var5);
         }

         return (Resource)var2;
      } else {
         StringBuilder var8 = new StringBuilder();
         var8.append("Cannot apply transformation on width: ");
         var8.append(var3);
         var8.append(" or height: ");
         var8.append(var4);
         var8.append(" less than or equal to zero and not Target.SIZE_ORIGINAL");
         throw new IllegalArgumentException(var8.toString());
      }
   }
}
