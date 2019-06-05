package com.bumptech.glide.load.engine.cache;

import android.util.Log;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.bumptech.glide.load.Key;
import java.io.File;
import java.io.IOException;

public class DiskLruCacheWrapper implements DiskCache {
   private static DiskLruCacheWrapper wrapper;
   private final File directory;
   private DiskLruCache diskLruCache;
   private final int maxSize;
   private final SafeKeyGenerator safeKeyGenerator;
   private final DiskCacheWriteLocker writeLocker = new DiskCacheWriteLocker();

   protected DiskLruCacheWrapper(File var1, int var2) {
      this.directory = var1;
      this.maxSize = var2;
      this.safeKeyGenerator = new SafeKeyGenerator();
   }

   public static DiskCache get(File var0, int var1) {
      synchronized(DiskLruCacheWrapper.class){}

      DiskLruCacheWrapper var5;
      try {
         if (wrapper == null) {
            DiskLruCacheWrapper var2 = new DiskLruCacheWrapper(var0, var1);
            wrapper = var2;
         }

         var5 = wrapper;
      } finally {
         ;
      }

      return var5;
   }

   private DiskLruCache getDiskCache() throws IOException {
      synchronized(this){}

      DiskLruCache var1;
      try {
         if (this.diskLruCache == null) {
            this.diskLruCache = DiskLruCache.open(this.directory, 1, 1, (long)this.maxSize);
         }

         var1 = this.diskLruCache;
      } finally {
         ;
      }

      return var1;
   }

   public File get(Key var1) {
      String var2 = this.safeKeyGenerator.getSafeKey(var1);
      StringBuilder var3;
      if (Log.isLoggable("DiskLruCacheWrapper", 2)) {
         var3 = new StringBuilder();
         var3.append("Get: Obtained: ");
         var3.append(var2);
         var3.append(" for for Key: ");
         var3.append(var1);
         Log.v("DiskLruCacheWrapper", var3.toString());
      }

      var3 = null;

      IOException var10000;
      File var6;
      label34: {
         boolean var10001;
         DiskLruCache.Value var7;
         try {
            var7 = this.getDiskCache().get(var2);
         } catch (IOException var5) {
            var10000 = var5;
            var10001 = false;
            break label34;
         }

         var6 = var3;
         if (var7 == null) {
            return var6;
         }

         try {
            var6 = var7.getFile(0);
            return var6;
         } catch (IOException var4) {
            var10000 = var4;
            var10001 = false;
         }
      }

      IOException var8 = var10000;
      var6 = var3;
      if (Log.isLoggable("DiskLruCacheWrapper", 5)) {
         Log.w("DiskLruCacheWrapper", "Unable to get from disk cache", var8);
         var6 = var3;
      }

      return var6;
   }

   public void put(Key param1, DiskCache.Writer param2) {
      // $FF: Couldn't be decompiled
   }
}
