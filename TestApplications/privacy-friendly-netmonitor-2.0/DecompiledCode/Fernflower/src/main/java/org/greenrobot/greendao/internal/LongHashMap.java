package org.greenrobot.greendao.internal;

import java.util.Arrays;
import org.greenrobot.greendao.DaoLog;

public final class LongHashMap {
   private int capacity;
   private int size;
   private LongHashMap.Entry[] table;
   private int threshold;

   public LongHashMap() {
      this(16);
   }

   public LongHashMap(int var1) {
      this.capacity = var1;
      this.threshold = var1 * 4 / 3;
      this.table = new LongHashMap.Entry[var1];
   }

   public void clear() {
      this.size = 0;
      Arrays.fill(this.table, (Object)null);
   }

   public boolean containsKey(long var1) {
      int var3 = (int)(var1 >>> 32);
      int var4 = (int)var1;
      int var5 = this.capacity;

      for(LongHashMap.Entry var6 = this.table[((var3 ^ var4) & Integer.MAX_VALUE) % var5]; var6 != null; var6 = var6.next) {
         if (var6.key == var1) {
            return true;
         }
      }

      return false;
   }

   public Object get(long var1) {
      int var3 = (int)(var1 >>> 32);
      int var4 = (int)var1;
      int var5 = this.capacity;

      for(LongHashMap.Entry var6 = this.table[((var3 ^ var4) & Integer.MAX_VALUE) % var5]; var6 != null; var6 = var6.next) {
         if (var6.key == var1) {
            return var6.value;
         }
      }

      return null;
   }

   public void logStats() {
      LongHashMap.Entry[] var1 = this.table;
      int var2 = 0;
      int var3 = var1.length;

      int var4;
      for(var4 = 0; var2 < var3; ++var2) {
         for(LongHashMap.Entry var5 = var1[var2]; var5 != null && var5.next != null; var5 = var5.next) {
            ++var4;
         }
      }

      StringBuilder var6 = new StringBuilder();
      var6.append("load: ");
      var6.append((float)this.size / (float)this.capacity);
      var6.append(", size: ");
      var6.append(this.size);
      var6.append(", capa: ");
      var6.append(this.capacity);
      var6.append(", collisions: ");
      var6.append(var4);
      var6.append(", collision ratio: ");
      var6.append((float)var4 / (float)this.size);
      DaoLog.d(var6.toString());
   }

   public Object put(long var1, Object var3) {
      int var4 = (((int)(var1 >>> 32) ^ (int)var1) & Integer.MAX_VALUE) % this.capacity;
      LongHashMap.Entry var5 = this.table[var4];

      for(LongHashMap.Entry var6 = var5; var6 != null; var6 = var6.next) {
         if (var6.key == var1) {
            Object var7 = var6.value;
            var6.value = var3;
            return var7;
         }
      }

      this.table[var4] = new LongHashMap.Entry(var1, var3, var5);
      ++this.size;
      if (this.size > this.threshold) {
         this.setCapacity(2 * this.capacity);
      }

      return null;
   }

   public Object remove(long var1) {
      int var3 = (((int)(var1 >>> 32) ^ (int)var1) & Integer.MAX_VALUE) % this.capacity;
      LongHashMap.Entry var4 = this.table[var3];

      LongHashMap.Entry var6;
      for(LongHashMap.Entry var5 = null; var4 != null; var4 = var6) {
         var6 = var4.next;
         if (var4.key == var1) {
            if (var5 == null) {
               this.table[var3] = var6;
            } else {
               var5.next = var6;
            }

            --this.size;
            return var4.value;
         }

         var5 = var4;
      }

      return null;
   }

   public void reserveRoom(int var1) {
      this.setCapacity(var1 * 5 / 3);
   }

   public void setCapacity(int var1) {
      LongHashMap.Entry[] var2 = new LongHashMap.Entry[var1];
      LongHashMap.Entry[] var3 = this.table;
      int var4 = 0;

      LongHashMap.Entry var9;
      for(int var5 = var3.length; var4 < var5; ++var4) {
         for(LongHashMap.Entry var10 = this.table[var4]; var10 != null; var10 = var9) {
            long var6 = var10.key;
            int var8 = (int)(var6 >>> 32);
            var8 = (((int)var6 ^ var8) & Integer.MAX_VALUE) % var1;
            var9 = var10.next;
            var10.next = var2[var8];
            var2[var8] = var10;
         }
      }

      this.table = var2;
      this.capacity = var1;
      this.threshold = var1 * 4 / 3;
   }

   public int size() {
      return this.size;
   }

   static final class Entry {
      final long key;
      LongHashMap.Entry next;
      Object value;

      Entry(long var1, Object var3, LongHashMap.Entry var4) {
         this.key = var1;
         this.value = var3;
         this.next = var4;
      }
   }
}
