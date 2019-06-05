package com.bumptech.glide.load.resource.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.Preconditions;
import java.security.MessageDigest;

public class BitmapDrawableTransformation implements Transformation {
   private final Transformation wrapped;

   public BitmapDrawableTransformation(Transformation var1) {
      this.wrapped = (Transformation)Preconditions.checkNotNull(var1);
   }

   public boolean equals(Object var1) {
      if (var1 instanceof BitmapDrawableTransformation) {
         BitmapDrawableTransformation var2 = (BitmapDrawableTransformation)var1;
         return this.wrapped.equals(var2.wrapped);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.wrapped.hashCode();
   }

   public Resource transform(Context var1, Resource var2, int var3, int var4) {
      BitmapResource var5 = BitmapResource.obtain(((BitmapDrawable)var2.get()).getBitmap(), Glide.get(var1).getBitmapPool());
      Resource var6 = this.wrapped.transform(var1, var5, var3, var4);
      return (Resource)(var6.equals(var5) ? var2 : LazyBitmapDrawableResource.obtain(var1, (Bitmap)var6.get()));
   }

   public void updateDiskCacheKey(MessageDigest var1) {
      this.wrapped.updateDiskCacheKey(var1);
   }
}
