package com.google.android.exoplayer2.util;

import java.util.Arrays;

public final class TimedValueQueue {
   private static final int INITIAL_BUFFER_SIZE = 10;
   private int first;
   private int size;
   private long[] timestamps;
   private Object[] values;

   public TimedValueQueue() {
      this(10);
   }

   public TimedValueQueue(int var1) {
      this.timestamps = new long[var1];
      this.values = newArray(var1);
   }

   private void addUnchecked(long var1, Object var3) {
      int var4 = this.first;
      int var5 = this.size;
      Object[] var6 = this.values;
      var4 = (var4 + var5) % var6.length;
      this.timestamps[var4] = var1;
      var6[var4] = var3;
      this.size = var5 + 1;
   }

   private void clearBufferOnTimeDiscontinuity(long var1) {
      int var3 = this.size;
      if (var3 > 0) {
         int var4 = this.first;
         int var5 = this.values.length;
         if (var1 <= this.timestamps[(var4 + var3 - 1) % var5]) {
            this.clear();
         }
      }

   }

   private void doubleCapacityIfFull() {
      int var1 = this.values.length;
      if (this.size >= var1) {
         int var2 = var1 * 2;
         long[] var3 = new long[var2];
         Object[] var4 = newArray(var2);
         var2 = this.first;
         var1 -= var2;
         System.arraycopy(this.timestamps, var2, var3, 0, var1);
         System.arraycopy(this.values, this.first, var4, 0, var1);
         var2 = this.first;
         if (var2 > 0) {
            System.arraycopy(this.timestamps, 0, var3, var1, var2);
            System.arraycopy(this.values, 0, var4, var1, this.first);
         }

         this.timestamps = var3;
         this.values = var4;
         this.first = 0;
      }
   }

   private static Object[] newArray(int var0) {
      return new Object[var0];
   }

   private Object poll(long var1, boolean var3) {
      long var4 = Long.MAX_VALUE;

      Object var6;
      long var7;
      for(var6 = null; this.size > 0; var4 = var7) {
         var7 = var1 - this.timestamps[this.first];
         if (var7 < 0L && (var3 || -var7 >= var4)) {
            break;
         }

         Object[] var9 = this.values;
         int var10 = this.first;
         var6 = var9[var10];
         var9[var10] = null;
         this.first = (var10 + 1) % var9.length;
         --this.size;
      }

      return var6;
   }

   public void add(long var1, Object var3) {
      synchronized(this){}

      try {
         this.clearBufferOnTimeDiscontinuity(var1);
         this.doubleCapacityIfFull();
         this.addUnchecked(var1, var3);
      } finally {
         ;
      }

   }

   public void clear() {
      synchronized(this){}

      try {
         this.first = 0;
         this.size = 0;
         Arrays.fill(this.values, (Object)null);
      } finally {
         ;
      }

   }

   public Object poll(long var1) {
      synchronized(this){}

      Object var3;
      try {
         var3 = this.poll(var1, false);
      } finally {
         ;
      }

      return var3;
   }

   public Object pollFloor(long var1) {
      synchronized(this){}

      Object var3;
      try {
         var3 = this.poll(var1, true);
      } finally {
         ;
      }

      return var3;
   }

   public int size() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.size;
      } finally {
         ;
      }

      return var1;
   }
}
