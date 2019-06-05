package com.bumptech.glide.load.resource.bitmap;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Initializable;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;

public class LazyBitmapDrawableResource implements Initializable, Resource {
   private final Bitmap bitmap;
   private final BitmapPool bitmapPool;
   private final Resources resources;

   LazyBitmapDrawableResource(Resources var1, BitmapPool var2, Bitmap var3) {
      this.resources = (Resources)Preconditions.checkNotNull(var1);
      this.bitmapPool = (BitmapPool)Preconditions.checkNotNull(var2);
      this.bitmap = (Bitmap)Preconditions.checkNotNull(var3);
   }

   public static LazyBitmapDrawableResource obtain(Context var0, Bitmap var1) {
      return obtain(var0.getResources(), Glide.get(var0).getBitmapPool(), var1);
   }

   public static LazyBitmapDrawableResource obtain(Resources var0, BitmapPool var1, Bitmap var2) {
      return new LazyBitmapDrawableResource(var0, var1, var2);
   }

   public BitmapDrawable get() {
      return new BitmapDrawable(this.resources, this.bitmap);
   }

   public Class getResourceClass() {
      return BitmapDrawable.class;
   }

   public int getSize() {
      return Util.getBitmapByteSize(this.bitmap);
   }

   public void initialize() {
      this.bitmap.prepareToDraw();
   }

   public void recycle() {
      this.bitmapPool.put(this.bitmap);
   }
}
