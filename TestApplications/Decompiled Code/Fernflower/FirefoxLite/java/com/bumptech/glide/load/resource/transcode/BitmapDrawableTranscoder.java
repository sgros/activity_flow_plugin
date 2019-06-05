package com.bumptech.glide.load.resource.transcode;

import android.content.res.Resources;
import android.graphics.Bitmap;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.LazyBitmapDrawableResource;
import com.bumptech.glide.util.Preconditions;

public class BitmapDrawableTranscoder implements ResourceTranscoder {
   private final BitmapPool bitmapPool;
   private final Resources resources;

   public BitmapDrawableTranscoder(Resources var1, BitmapPool var2) {
      this.resources = (Resources)Preconditions.checkNotNull(var1);
      this.bitmapPool = (BitmapPool)Preconditions.checkNotNull(var2);
   }

   public Resource transcode(Resource var1, Options var2) {
      return LazyBitmapDrawableResource.obtain(this.resources, this.bitmapPool, (Bitmap)var1.get());
   }
}
