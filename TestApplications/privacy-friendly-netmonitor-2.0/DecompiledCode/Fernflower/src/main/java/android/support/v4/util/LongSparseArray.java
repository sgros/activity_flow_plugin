package android.support.v4.util;

public class LongSparseArray implements Cloneable {
   private static final Object DELETED = new Object();
   private boolean mGarbage;
   private long[] mKeys;
   private int mSize;
   private Object[] mValues;

   public LongSparseArray() {
      this(10);
   }

   public LongSparseArray(int var1) {
      this.mGarbage = false;
      if (var1 == 0) {
         this.mKeys = ContainerHelpers.EMPTY_LONGS;
         this.mValues = ContainerHelpers.EMPTY_OBJECTS;
      } else {
         var1 = ContainerHelpers.idealLongArraySize(var1);
         this.mKeys = new long[var1];
         this.mValues = new Object[var1];
      }

      this.mSize = 0;
   }

   private void gc() {
      int var1 = this.mSize;
      long[] var2 = this.mKeys;
      Object[] var3 = this.mValues;
      int var4 = 0;

      int var5;
      int var7;
      for(var5 = var4; var4 < var1; var5 = var7) {
         Object var6 = var3[var4];
         var7 = var5;
         if (var6 != DELETED) {
            if (var4 != var5) {
               var2[var5] = var2[var4];
               var3[var5] = var6;
               var3[var4] = null;
            }

            var7 = var5 + 1;
         }

         ++var4;
      }

      this.mGarbage = false;
      this.mSize = var5;
   }

   public void append(long var1, Object var3) {
      if (this.mSize != 0 && var1 <= this.mKeys[this.mSize - 1]) {
         this.put(var1, var3);
      } else {
         if (this.mGarbage && this.mSize >= this.mKeys.length) {
            this.gc();
         }

         int var4 = this.mSize;
         if (var4 >= this.mKeys.length) {
            int var5 = ContainerHelpers.idealLongArraySize(var4 + 1);
            long[] var6 = new long[var5];
            Object[] var7 = new Object[var5];
            System.arraycopy(this.mKeys, 0, var6, 0, this.mKeys.length);
            System.arraycopy(this.mValues, 0, var7, 0, this.mValues.length);
            this.mKeys = var6;
            this.mValues = var7;
         }

         this.mKeys[var4] = var1;
         this.mValues[var4] = var3;
         this.mSize = var4 + 1;
      }
   }

   public void clear() {
      int var1 = this.mSize;
      Object[] var2 = this.mValues;

      for(int var3 = 0; var3 < var1; ++var3) {
         var2[var3] = null;
      }

      this.mSize = 0;
      this.mGarbage = false;
   }

   public LongSparseArray clone() {
      LongSparseArray var1;
      try {
         var1 = (LongSparseArray)super.clone();
      } catch (CloneNotSupportedException var4) {
         var1 = null;
         return var1;
      }

      try {
         var1.mKeys = (long[])this.mKeys.clone();
         var1.mValues = (Object[])this.mValues.clone();
      } catch (CloneNotSupportedException var3) {
      }

      return var1;
   }

   public void delete(long var1) {
      int var3 = ContainerHelpers.binarySearch(this.mKeys, this.mSize, var1);
      if (var3 >= 0 && this.mValues[var3] != DELETED) {
         this.mValues[var3] = DELETED;
         this.mGarbage = true;
      }

   }

   public Object get(long var1) {
      return this.get(var1, (Object)null);
   }

   public Object get(long var1, Object var3) {
      int var4 = ContainerHelpers.binarySearch(this.mKeys, this.mSize, var1);
      return var4 >= 0 && this.mValues[var4] != DELETED ? this.mValues[var4] : var3;
   }

   public int indexOfKey(long var1) {
      if (this.mGarbage) {
         this.gc();
      }

      return ContainerHelpers.binarySearch(this.mKeys, this.mSize, var1);
   }

   public int indexOfValue(Object var1) {
      if (this.mGarbage) {
         this.gc();
      }

      for(int var2 = 0; var2 < this.mSize; ++var2) {
         if (this.mValues[var2] == var1) {
            return var2;
         }
      }

      return -1;
   }

   public long keyAt(int var1) {
      if (this.mGarbage) {
         this.gc();
      }

      return this.mKeys[var1];
   }

   public void put(long var1, Object var3) {
      int var4 = ContainerHelpers.binarySearch(this.mKeys, this.mSize, var1);
      if (var4 >= 0) {
         this.mValues[var4] = var3;
      } else {
         int var5 = ~var4;
         if (var5 < this.mSize && this.mValues[var5] == DELETED) {
            this.mKeys[var5] = var1;
            this.mValues[var5] = var3;
            return;
         }

         var4 = var5;
         if (this.mGarbage) {
            var4 = var5;
            if (this.mSize >= this.mKeys.length) {
               this.gc();
               var4 = ~ContainerHelpers.binarySearch(this.mKeys, this.mSize, var1);
            }
         }

         long[] var6;
         if (this.mSize >= this.mKeys.length) {
            var5 = ContainerHelpers.idealLongArraySize(this.mSize + 1);
            var6 = new long[var5];
            Object[] var7 = new Object[var5];
            System.arraycopy(this.mKeys, 0, var6, 0, this.mKeys.length);
            System.arraycopy(this.mValues, 0, var7, 0, this.mValues.length);
            this.mKeys = var6;
            this.mValues = var7;
         }

         if (this.mSize - var4 != 0) {
            long[] var8 = this.mKeys;
            var6 = this.mKeys;
            var5 = var4 + 1;
            System.arraycopy(var8, var4, var6, var5, this.mSize - var4);
            System.arraycopy(this.mValues, var4, this.mValues, var5, this.mSize - var4);
         }

         this.mKeys[var4] = var1;
         this.mValues[var4] = var3;
         ++this.mSize;
      }

   }

   public void remove(long var1) {
      this.delete(var1);
   }

   public void removeAt(int var1) {
      if (this.mValues[var1] != DELETED) {
         this.mValues[var1] = DELETED;
         this.mGarbage = true;
      }

   }

   public void setValueAt(int var1, Object var2) {
      if (this.mGarbage) {
         this.gc();
      }

      this.mValues[var1] = var2;
   }

   public int size() {
      if (this.mGarbage) {
         this.gc();
      }

      return this.mSize;
   }

   public String toString() {
      if (this.size() <= 0) {
         return "{}";
      } else {
         StringBuilder var1 = new StringBuilder(this.mSize * 28);
         var1.append('{');

         for(int var2 = 0; var2 < this.mSize; ++var2) {
            if (var2 > 0) {
               var1.append(", ");
            }

            var1.append(this.keyAt(var2));
            var1.append('=');
            Object var3 = this.valueAt(var2);
            if (var3 != this) {
               var1.append(var3);
            } else {
               var1.append("(this Map)");
            }
         }

         var1.append('}');
         return var1.toString();
      }
   }

   public Object valueAt(int var1) {
      if (this.mGarbage) {
         this.gc();
      }

      return this.mValues[var1];
   }
}
