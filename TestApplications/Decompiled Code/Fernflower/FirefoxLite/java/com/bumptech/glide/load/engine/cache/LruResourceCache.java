package com.bumptech.glide.load.engine.cache;

import android.annotation.SuppressLint;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.LruCache;

public class LruResourceCache extends LruCache implements MemoryCache {
   private MemoryCache.ResourceRemovedListener listener;

   public LruResourceCache(int var1) {
      super(var1);
   }

   protected int getSize(Resource var1) {
      return var1.getSize();
   }

   protected void onItemEvicted(Key var1, Resource var2) {
      if (this.listener != null) {
         this.listener.onResourceRemoved(var2);
      }

   }

   public void setResourceRemovedListener(MemoryCache.ResourceRemovedListener var1) {
      this.listener = var1;
   }

   @SuppressLint({"InlinedApi"})
   public void trimMemory(int var1) {
      if (var1 >= 40) {
         this.clearMemory();
      } else if (var1 >= 20) {
         this.trimToSize(this.getCurrentSize() / 2);
      }

   }
}
