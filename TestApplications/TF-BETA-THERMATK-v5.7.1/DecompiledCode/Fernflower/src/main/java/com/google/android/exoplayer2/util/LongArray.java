package com.google.android.exoplayer2.util;

import java.util.Arrays;

public final class LongArray {
   private static final int DEFAULT_INITIAL_CAPACITY = 32;
   private int size;
   private long[] values;

   public LongArray() {
      this(32);
   }

   public LongArray(int var1) {
      this.values = new long[var1];
   }

   public void add(long var1) {
      int var3 = this.size;
      long[] var4 = this.values;
      if (var3 == var4.length) {
         this.values = Arrays.copyOf(var4, var3 * 2);
      }

      var4 = this.values;
      var3 = this.size++;
      var4[var3] = var1;
   }

   public long get(int var1) {
      if (var1 >= 0 && var1 < this.size) {
         return this.values[var1];
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Invalid index ");
         var2.append(var1);
         var2.append(", size is ");
         var2.append(this.size);
         throw new IndexOutOfBoundsException(var2.toString());
      }
   }

   public int size() {
      return this.size;
   }

   public long[] toArray() {
      return Arrays.copyOf(this.values, this.size);
   }
}
