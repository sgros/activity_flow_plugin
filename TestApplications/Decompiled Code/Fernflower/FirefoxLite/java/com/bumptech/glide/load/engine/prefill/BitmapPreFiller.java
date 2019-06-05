package com.bumptech.glide.load.engine.prefill;

import android.os.Handler;
import android.os.Looper;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.cache.MemoryCache;

public final class BitmapPreFiller {
   private final BitmapPool bitmapPool;
   private final DecodeFormat defaultFormat;
   private final Handler handler = new Handler(Looper.getMainLooper());
   private final MemoryCache memoryCache;

   public BitmapPreFiller(MemoryCache var1, BitmapPool var2, DecodeFormat var3) {
      this.memoryCache = var1;
      this.bitmapPool = var2;
      this.defaultFormat = var3;
   }
}
