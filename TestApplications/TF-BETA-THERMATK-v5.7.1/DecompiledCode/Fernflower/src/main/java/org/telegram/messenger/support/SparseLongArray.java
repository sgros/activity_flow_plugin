package org.telegram.messenger.support;

public class SparseLongArray implements Cloneable {
   private int[] mKeys;
   private int mSize;
   private long[] mValues;

   public SparseLongArray() {
      this(10);
   }

   public SparseLongArray(int var1) {
      var1 = ArrayUtils.idealLongArraySize(var1);
      this.mKeys = new int[var1];
      this.mValues = new long[var1];
      this.mSize = 0;
   }

   private static int binarySearch(int[] var0, int var1, int var2, long var3) {
      int var5 = var2 + var1;
      var2 = var1 - 1;
      var1 = var5;

      while(var1 - var2 > 1) {
         int var6 = (var1 + var2) / 2;
         if ((long)var0[var6] < var3) {
            var2 = var6;
         } else {
            var1 = var6;
         }
      }

      if (var1 == var5) {
         return ~var5;
      } else if ((long)var0[var1] == var3) {
         return var1;
      } else {
         return ~var1;
      }
   }

   private void growKeyAndValueArrays(int var1) {
      var1 = ArrayUtils.idealLongArraySize(var1);
      int[] var2 = new int[var1];
      long[] var3 = new long[var1];
      int[] var4 = this.mKeys;
      System.arraycopy(var4, 0, var2, 0, var4.length);
      long[] var5 = this.mValues;
      System.arraycopy(var5, 0, var3, 0, var5.length);
      this.mKeys = var2;
      this.mValues = var3;
   }

   public void append(int var1, long var2) {
      int var4 = this.mSize;
      if (var4 != 0 && var1 <= this.mKeys[var4 - 1]) {
         this.put(var1, var2);
      } else {
         var4 = this.mSize;
         if (var4 >= this.mKeys.length) {
            this.growKeyAndValueArrays(var4 + 1);
         }

         this.mKeys[var4] = var1;
         this.mValues[var4] = var2;
         this.mSize = var4 + 1;
      }
   }

   public void clear() {
      this.mSize = 0;
   }

   public SparseLongArray clone() {
      SparseLongArray var1;
      try {
         var1 = (SparseLongArray)super.clone();
      } catch (CloneNotSupportedException var4) {
         var1 = null;
         return var1;
      }

      try {
         var1.mKeys = (int[])this.mKeys.clone();
         var1.mValues = (long[])this.mValues.clone();
      } catch (CloneNotSupportedException var3) {
      }

      return var1;
   }

   public void delete(int var1) {
      var1 = binarySearch(this.mKeys, 0, this.mSize, (long)var1);
      if (var1 >= 0) {
         this.removeAt(var1);
      }

   }

   public long get(int var1) {
      return this.get(var1, 0L);
   }

   public long get(int var1, long var2) {
      var1 = binarySearch(this.mKeys, 0, this.mSize, (long)var1);
      return var1 < 0 ? var2 : this.mValues[var1];
   }

   public int indexOfKey(int var1) {
      return binarySearch(this.mKeys, 0, this.mSize, (long)var1);
   }

   public int indexOfValue(long var1) {
      for(int var3 = 0; var3 < this.mSize; ++var3) {
         if (this.mValues[var3] == var1) {
            return var3;
         }
      }

      return -1;
   }

   public int keyAt(int var1) {
      return this.mKeys[var1];
   }

   public void put(int var1, long var2) {
      int var4 = binarySearch(this.mKeys, 0, this.mSize, (long)var1);
      if (var4 >= 0) {
         this.mValues[var4] = var2;
      } else {
         var4 = ~var4;
         int var5 = this.mSize;
         if (var5 >= this.mKeys.length) {
            this.growKeyAndValueArrays(var5 + 1);
         }

         int var6 = this.mSize;
         if (var6 - var4 != 0) {
            int[] var7 = this.mKeys;
            var5 = var4 + 1;
            System.arraycopy(var7, var4, var7, var5, var6 - var4);
            long[] var8 = this.mValues;
            System.arraycopy(var8, var4, var8, var5, this.mSize - var4);
         }

         this.mKeys[var4] = var1;
         this.mValues[var4] = var2;
         ++this.mSize;
      }

   }

   public void removeAt(int var1) {
      int[] var2 = this.mKeys;
      int var3 = var1 + 1;
      System.arraycopy(var2, var3, var2, var1, this.mSize - var3);
      long[] var4 = this.mValues;
      System.arraycopy(var4, var3, var4, var1, this.mSize - var3);
      --this.mSize;
   }

   public int size() {
      return this.mSize;
   }

   public long valueAt(int var1) {
      return this.mValues[var1];
   }
}
