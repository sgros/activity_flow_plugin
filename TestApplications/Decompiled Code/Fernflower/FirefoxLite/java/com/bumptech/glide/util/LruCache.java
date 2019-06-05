package com.bumptech.glide.util;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class LruCache {
   private final LinkedHashMap cache = new LinkedHashMap(100, 0.75F, true);
   private int currentSize = 0;
   private final int initialMaxSize;
   private int maxSize;

   public LruCache(int var1) {
      this.initialMaxSize = var1;
      this.maxSize = var1;
   }

   private void evict() {
      this.trimToSize(this.maxSize);
   }

   public void clearMemory() {
      this.trimToSize(0);
   }

   public Object get(Object var1) {
      synchronized(this){}

      try {
         var1 = this.cache.get(var1);
      } finally {
         ;
      }

      return var1;
   }

   public int getCurrentSize() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.currentSize;
      } finally {
         ;
      }

      return var1;
   }

   protected int getSize(Object var1) {
      return 1;
   }

   protected void onItemEvicted(Object var1, Object var2) {
   }

   public Object put(Object var1, Object var2) {
      synchronized(this){}

      Throwable var10000;
      label292: {
         boolean var10001;
         try {
            if (this.getSize(var2) >= this.maxSize) {
               this.onItemEvicted(var1, var2);
               return null;
            }
         } catch (Throwable var32) {
            var10000 = var32;
            var10001 = false;
            break label292;
         }

         try {
            var1 = this.cache.put(var1, var2);
         } catch (Throwable var31) {
            var10000 = var31;
            var10001 = false;
            break label292;
         }

         if (var2 != null) {
            try {
               this.currentSize += this.getSize(var2);
            } catch (Throwable var30) {
               var10000 = var30;
               var10001 = false;
               break label292;
            }
         }

         if (var1 != null) {
            try {
               this.currentSize -= this.getSize(var1);
            } catch (Throwable var29) {
               var10000 = var29;
               var10001 = false;
               break label292;
            }
         }

         try {
            this.evict();
         } catch (Throwable var28) {
            var10000 = var28;
            var10001 = false;
            break label292;
         }

         return var1;
      }

      Throwable var33 = var10000;
      throw var33;
   }

   public Object remove(Object var1) {
      synchronized(this){}

      Throwable var10000;
      label75: {
         boolean var10001;
         try {
            var1 = this.cache.remove(var1);
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
            break label75;
         }

         if (var1 == null) {
            return var1;
         }

         label66:
         try {
            this.currentSize -= this.getSize(var1);
            return var1;
         } catch (Throwable var6) {
            var10000 = var6;
            var10001 = false;
            break label66;
         }
      }

      Throwable var8 = var10000;
      throw var8;
   }

   protected void trimToSize(int var1) {
      synchronized(this){}

      while(true) {
         boolean var5 = false;

         try {
            var5 = true;
            if (this.currentSize <= var1) {
               var5 = false;
               return;
            }

            Entry var2 = (Entry)this.cache.entrySet().iterator().next();
            Object var3 = var2.getValue();
            this.currentSize -= this.getSize(var3);
            Object var7 = var2.getKey();
            this.cache.remove(var7);
            this.onItemEvicted(var7, var3);
            var5 = false;
         } finally {
            if (var5) {
               ;
            }
         }
      }
   }
}
