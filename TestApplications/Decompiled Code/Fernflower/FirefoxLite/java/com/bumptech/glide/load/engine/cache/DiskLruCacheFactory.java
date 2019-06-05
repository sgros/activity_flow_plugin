package com.bumptech.glide.load.engine.cache;

import java.io.File;

public class DiskLruCacheFactory implements DiskCache.Factory {
   private final DiskLruCacheFactory.CacheDirectoryGetter cacheDirectoryGetter;
   private final int diskCacheSize;

   public DiskLruCacheFactory(DiskLruCacheFactory.CacheDirectoryGetter var1, int var2) {
      this.diskCacheSize = var2;
      this.cacheDirectoryGetter = var1;
   }

   public DiskCache build() {
      File var1 = this.cacheDirectoryGetter.getCacheDirectory();
      if (var1 == null) {
         return null;
      } else {
         return var1.mkdirs() || var1.exists() && var1.isDirectory() ? DiskLruCacheWrapper.get(var1, this.diskCacheSize) : null;
      }
   }

   public interface CacheDirectoryGetter {
      File getCacheDirectory();
   }
}
