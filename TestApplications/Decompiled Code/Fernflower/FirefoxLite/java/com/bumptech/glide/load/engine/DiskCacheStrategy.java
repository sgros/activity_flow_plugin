package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.EncodeStrategy;

public abstract class DiskCacheStrategy {
   public static final DiskCacheStrategy ALL = new DiskCacheStrategy() {
      public boolean decodeCachedData() {
         return true;
      }

      public boolean decodeCachedResource() {
         return true;
      }

      public boolean isDataCacheable(DataSource var1) {
         boolean var2;
         if (var1 == DataSource.REMOTE) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      public boolean isResourceCacheable(boolean var1, DataSource var2, EncodeStrategy var3) {
         if (var2 != DataSource.RESOURCE_DISK_CACHE && var2 != DataSource.MEMORY_CACHE) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }
   };
   public static final DiskCacheStrategy AUTOMATIC = new DiskCacheStrategy() {
      public boolean decodeCachedData() {
         return true;
      }

      public boolean decodeCachedResource() {
         return true;
      }

      public boolean isDataCacheable(DataSource var1) {
         boolean var2;
         if (var1 == DataSource.REMOTE) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      public boolean isResourceCacheable(boolean var1, DataSource var2, EncodeStrategy var3) {
         if ((var1 && var2 == DataSource.DATA_DISK_CACHE || var2 == DataSource.LOCAL) && var3 == EncodeStrategy.TRANSFORMED) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }
   };
   public static final DiskCacheStrategy DATA = new DiskCacheStrategy() {
      public boolean decodeCachedData() {
         return true;
      }

      public boolean decodeCachedResource() {
         return false;
      }

      public boolean isDataCacheable(DataSource var1) {
         boolean var2;
         if (var1 != DataSource.DATA_DISK_CACHE && var1 != DataSource.MEMORY_CACHE) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      public boolean isResourceCacheable(boolean var1, DataSource var2, EncodeStrategy var3) {
         return false;
      }
   };
   public static final DiskCacheStrategy NONE = new DiskCacheStrategy() {
      public boolean decodeCachedData() {
         return false;
      }

      public boolean decodeCachedResource() {
         return false;
      }

      public boolean isDataCacheable(DataSource var1) {
         return false;
      }

      public boolean isResourceCacheable(boolean var1, DataSource var2, EncodeStrategy var3) {
         return false;
      }
   };
   public static final DiskCacheStrategy RESOURCE = new DiskCacheStrategy() {
      public boolean decodeCachedData() {
         return false;
      }

      public boolean decodeCachedResource() {
         return true;
      }

      public boolean isDataCacheable(DataSource var1) {
         return false;
      }

      public boolean isResourceCacheable(boolean var1, DataSource var2, EncodeStrategy var3) {
         if (var2 != DataSource.RESOURCE_DISK_CACHE && var2 != DataSource.MEMORY_CACHE) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }
   };

   public abstract boolean decodeCachedData();

   public abstract boolean decodeCachedResource();

   public abstract boolean isDataCacheable(DataSource var1);

   public abstract boolean isResourceCacheable(boolean var1, DataSource var2, EncodeStrategy var3);
}
