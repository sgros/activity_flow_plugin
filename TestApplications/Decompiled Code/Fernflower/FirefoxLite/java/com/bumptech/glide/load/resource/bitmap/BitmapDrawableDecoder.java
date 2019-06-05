package com.bumptech.glide.load.resource.bitmap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Preconditions;
import java.io.IOException;

public class BitmapDrawableDecoder implements ResourceDecoder {
   private final BitmapPool bitmapPool;
   private final ResourceDecoder decoder;
   private final Resources resources;

   public BitmapDrawableDecoder(Resources var1, BitmapPool var2, ResourceDecoder var3) {
      this.resources = (Resources)Preconditions.checkNotNull(var1);
      this.bitmapPool = (BitmapPool)Preconditions.checkNotNull(var2);
      this.decoder = (ResourceDecoder)Preconditions.checkNotNull(var3);
   }

   public Resource decode(Object var1, int var2, int var3, Options var4) throws IOException {
      Resource var5 = this.decoder.decode(var1, var2, var3, var4);
      return var5 == null ? null : LazyBitmapDrawableResource.obtain(this.resources, this.bitmapPool, (Bitmap)var5.get());
   }

   public boolean handles(Object var1, Options var2) throws IOException {
      return this.decoder.handles(var1, var2);
   }
}
