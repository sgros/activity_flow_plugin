package com.bumptech.glide.load.engine.bitmap_recycle;

import android.util.Log;
import com.bumptech.glide.util.Preconditions;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public final class LruArrayPool implements ArrayPool {
   private final Map adapters = new HashMap();
   private int currentSize;
   private final GroupedLinkedMap groupedMap = new GroupedLinkedMap();
   private final LruArrayPool.KeyPool keyPool = new LruArrayPool.KeyPool();
   private final int maxSize;
   private final Map sortedSizes = new HashMap();

   public LruArrayPool() {
      this.maxSize = 4194304;
   }

   public LruArrayPool(int var1) {
      this.maxSize = var1;
   }

   private void decrementArrayOfSize(int var1, Class var2) {
      NavigableMap var3 = this.getSizesForAdapter(var2);
      Integer var4 = (Integer)var3.get(var1);
      if (var4 != null) {
         if (var4 == 1) {
            var3.remove(var1);
         } else {
            var3.put(var1, var4 - 1);
         }

      } else {
         StringBuilder var5 = new StringBuilder();
         var5.append("Tried to decrement empty size, size: ");
         var5.append(var1);
         var5.append(", this: ");
         var5.append(this);
         throw new NullPointerException(var5.toString());
      }
   }

   private void evict() {
      this.evictToSize(this.maxSize);
   }

   private void evictToSize(int var1) {
      while(this.currentSize > var1) {
         Object var2 = this.groupedMap.removeLast();
         Preconditions.checkNotNull(var2);
         ArrayAdapterInterface var3 = this.getAdapterFromObject(var2);
         this.currentSize -= var3.getArrayLength(var2) * var3.getElementSizeInBytes();
         this.decrementArrayOfSize(var3.getArrayLength(var2), var2.getClass());
         if (Log.isLoggable(var3.getTag(), 2)) {
            String var4 = var3.getTag();
            StringBuilder var5 = new StringBuilder();
            var5.append("evicted: ");
            var5.append(var3.getArrayLength(var2));
            Log.v(var4, var5.toString());
         }
      }

   }

   private ArrayAdapterInterface getAdapterFromObject(Object var1) {
      return this.getAdapterFromType(var1.getClass());
   }

   private ArrayAdapterInterface getAdapterFromType(Class var1) {
      ArrayAdapterInterface var2 = (ArrayAdapterInterface)this.adapters.get(var1);
      Object var3 = var2;
      if (var2 == null) {
         if (var1.equals(int[].class)) {
            var3 = new IntegerArrayAdapter();
         } else {
            if (!var1.equals(byte[].class)) {
               StringBuilder var4 = new StringBuilder();
               var4.append("No array pool found for: ");
               var4.append(var1.getSimpleName());
               throw new IllegalArgumentException(var4.toString());
            }

            var3 = new ByteArrayAdapter();
         }

         this.adapters.put(var1, var3);
      }

      return (ArrayAdapterInterface)var3;
   }

   private Object getArrayForKey(LruArrayPool.Key var1) {
      return this.groupedMap.get(var1);
   }

   private NavigableMap getSizesForAdapter(Class var1) {
      NavigableMap var2 = (NavigableMap)this.sortedSizes.get(var1);
      Object var3 = var2;
      if (var2 == null) {
         var3 = new TreeMap();
         this.sortedSizes.put(var1, var3);
      }

      return (NavigableMap)var3;
   }

   private boolean isNoMoreThanHalfFull() {
      boolean var1;
      if (this.currentSize != 0 && this.maxSize / this.currentSize < 2) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private boolean isSmallEnoughForReuse(int var1) {
      boolean var2;
      if (var1 <= this.maxSize / 2) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private boolean mayFillRequest(int var1, Integer var2) {
      boolean var3;
      if (var2 == null || !this.isNoMoreThanHalfFull() && var2 > var1 * 8) {
         var3 = false;
      } else {
         var3 = true;
      }

      return var3;
   }

   public void clearMemory() {
      synchronized(this){}

      try {
         this.evictToSize(0);
      } finally {
         ;
      }

   }

   public Object get(int var1, Class var2) {
      ArrayAdapterInterface var3 = this.getAdapterFromType(var2);
      synchronized(this){}

      Object var51;
      label438: {
         Throwable var10000;
         boolean var10001;
         label440: {
            LruArrayPool.Key var50;
            label431: {
               try {
                  Integer var4 = (Integer)this.getSizesForAdapter(var2).ceilingKey(var1);
                  if (this.mayFillRequest(var1, var4)) {
                     var50 = this.keyPool.get(var4, var2);
                     break label431;
                  }
               } catch (Throwable var46) {
                  var10000 = var46;
                  var10001 = false;
                  break label440;
               }

               try {
                  var50 = this.keyPool.get(var1, var2);
               } catch (Throwable var45) {
                  var10000 = var45;
                  var10001 = false;
                  break label440;
               }
            }

            try {
               var51 = this.getArrayForKey(var50);
            } catch (Throwable var44) {
               var10000 = var44;
               var10001 = false;
               break label440;
            }

            if (var51 != null) {
               try {
                  this.currentSize -= var3.getArrayLength(var51) * var3.getElementSizeInBytes();
                  this.decrementArrayOfSize(var3.getArrayLength(var51), var2);
               } catch (Throwable var43) {
                  var10000 = var43;
                  var10001 = false;
                  break label440;
               }
            }

            label415:
            try {
               break label438;
            } catch (Throwable var42) {
               var10000 = var42;
               var10001 = false;
               break label415;
            }
         }

         while(true) {
            Throwable var47 = var10000;

            try {
               throw var47;
            } catch (Throwable var41) {
               var10000 = var41;
               var10001 = false;
               continue;
            }
         }
      }

      Object var48 = var51;
      if (var51 == null) {
         if (Log.isLoggable(var3.getTag(), 2)) {
            String var49 = var3.getTag();
            StringBuilder var52 = new StringBuilder();
            var52.append("Allocated ");
            var52.append(var1);
            var52.append(" bytes");
            Log.v(var49, var52.toString());
         }

         var48 = var3.newArray(var1);
      }

      return var48;
   }

   public void put(Object var1, Class var2) {
      synchronized(this){}

      Throwable var10000;
      label191: {
         int var4;
         int var5;
         boolean var6;
         boolean var10001;
         try {
            ArrayAdapterInterface var3 = this.getAdapterFromType(var2);
            var4 = var3.getArrayLength(var1);
            var5 = var3.getElementSizeInBytes() * var4;
            var6 = this.isSmallEnoughForReuse(var5);
         } catch (Throwable var27) {
            var10000 = var27;
            var10001 = false;
            break label191;
         }

         if (!var6) {
            return;
         }

         int var7;
         Integer var28;
         NavigableMap var30;
         try {
            LruArrayPool.Key var31 = this.keyPool.get(var4, var2);
            this.groupedMap.put(var31, var1);
            var30 = this.getSizesForAdapter(var2);
            var28 = (Integer)var30.get(var31.size);
            var7 = var31.size;
         } catch (Throwable var26) {
            var10000 = var26;
            var10001 = false;
            break label191;
         }

         var4 = 1;
         if (var28 != null) {
            try {
               var4 = 1 + var28;
            } catch (Throwable var25) {
               var10000 = var25;
               var10001 = false;
               break label191;
            }
         }

         try {
            var30.put(var7, var4);
            this.currentSize += var5;
            this.evict();
         } catch (Throwable var24) {
            var10000 = var24;
            var10001 = false;
            break label191;
         }

         return;
      }

      Throwable var29 = var10000;
      throw var29;
   }

   public void trimMemory(int var1) {
      synchronized(this){}
      Throwable var10000;
      boolean var10001;
      if (var1 >= 40) {
         label63:
         try {
            this.clearMemory();
            return;
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
            break label63;
         }
      } else {
         if (var1 < 20) {
            return;
         }

         label65:
         try {
            this.evictToSize(this.maxSize / 2);
            return;
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label65;
         }
      }

      Throwable var2 = var10000;
      throw var2;
   }

   private static final class Key implements Poolable {
      private Class arrayClass;
      private final LruArrayPool.KeyPool pool;
      int size;

      Key(LruArrayPool.KeyPool var1) {
         this.pool = var1;
      }

      public boolean equals(Object var1) {
         boolean var2 = var1 instanceof LruArrayPool.Key;
         boolean var3 = false;
         if (var2) {
            LruArrayPool.Key var4 = (LruArrayPool.Key)var1;
            var2 = var3;
            if (this.size == var4.size) {
               var2 = var3;
               if (this.arrayClass == var4.arrayClass) {
                  var2 = true;
               }
            }

            return var2;
         } else {
            return false;
         }
      }

      public int hashCode() {
         int var1 = this.size;
         int var2;
         if (this.arrayClass != null) {
            var2 = this.arrayClass.hashCode();
         } else {
            var2 = 0;
         }

         return var1 * 31 + var2;
      }

      void init(int var1, Class var2) {
         this.size = var1;
         this.arrayClass = var2;
      }

      public void offer() {
         this.pool.offer(this);
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("Key{size=");
         var1.append(this.size);
         var1.append("array=");
         var1.append(this.arrayClass);
         var1.append('}');
         return var1.toString();
      }
   }

   private static final class KeyPool extends BaseKeyPool {
      KeyPool() {
      }

      protected LruArrayPool.Key create() {
         return new LruArrayPool.Key(this);
      }

      LruArrayPool.Key get(int var1, Class var2) {
         LruArrayPool.Key var3 = (LruArrayPool.Key)this.get();
         var3.init(var1, var2);
         return var3;
      }
   }
}
