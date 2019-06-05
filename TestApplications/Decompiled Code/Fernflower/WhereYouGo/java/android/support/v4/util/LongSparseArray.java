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
      int var2 = 0;
      long[] var3 = this.mKeys;
      Object[] var4 = this.mValues;

      int var7;
      for(int var5 = 0; var5 < var1; var2 = var7) {
         Object var6 = var4[var5];
         var7 = var2;
         if (var6 != DELETED) {
            if (var5 != var2) {
               var3[var2] = var3[var5];
               var4[var2] = var6;
               var4[var5] = null;
            }

            var7 = var2 + 1;
         }

         ++var5;
      }

      this.mGarbage = false;
      this.mSize = var2;
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
      LongSparseArray var1 = null;

      boolean var10001;
      LongSparseArray var2;
      try {
         var2 = (LongSparseArray)super.clone();
      } catch (CloneNotSupportedException var5) {
         var10001 = false;
         return var1;
      }

      var1 = var2;

      try {
         var2.mKeys = (long[])this.mKeys.clone();
      } catch (CloneNotSupportedException var4) {
         var10001 = false;
         return var1;
      }

      var1 = var2;

      try {
         var2.mValues = (Object[])this.mValues.clone();
      } catch (CloneNotSupportedException var3) {
         var10001 = false;
         return var1;
      }

      var1 = var2;
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
      Object var5 = var3;
      if (var4 >= 0) {
         if (this.mValues[var4] == DELETED) {
            var5 = var3;
         } else {
            var5 = this.mValues[var4];
         }
      }

      return var5;
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

      int var2 = 0;

      while(true) {
         if (var2 >= this.mSize) {
            var2 = -1;
            break;
         }

         if (this.mValues[var2] == var1) {
            break;
         }

         ++var2;
      }

      return var2;
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
         } else {
            var4 = var5;
            if (this.mGarbage) {
               var4 = var5;
               if (this.mSize >= this.mKeys.length) {
                  this.gc();
                  var4 = ~ContainerHelpers.binarySearch(this.mKeys, this.mSize, var1);
               }
            }

            if (this.mSize >= this.mKeys.length) {
               var5 = ContainerHelpers.idealLongArraySize(this.mSize + 1);
               long[] var6 = new long[var5];
               Object[] var7 = new Object[var5];
               System.arraycopy(this.mKeys, 0, var6, 0, this.mKeys.length);
               System.arraycopy(this.mValues, 0, var7, 0, this.mValues.length);
               this.mKeys = var6;
               this.mValues = var7;
            }

            if (this.mSize - var4 != 0) {
               System.arraycopy(this.mKeys, var4, this.mKeys, var4 + 1, this.mSize - var4);
               System.arraycopy(this.mValues, var4, this.mValues, var4 + 1, this.mSize - var4);
            }

            this.mKeys[var4] = var1;
            this.mValues[var4] = var3;
            ++this.mSize;
         }
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
      String var1;
      if (this.size() <= 0) {
         var1 = "{}";
      } else {
         StringBuilder var2 = new StringBuilder(this.mSize * 28);
         var2.append('{');

         for(int var3 = 0; var3 < this.mSize; ++var3) {
            if (var3 > 0) {
               var2.append(", ");
            }

            var2.append(this.keyAt(var3));
            var2.append('=');
            Object var4 = this.valueAt(var3);
            if (var4 != this) {
               var2.append(var4);
            } else {
               var2.append("(this Map)");
            }
         }

         var2.append('}');
         var1 = var2.toString();
      }

      return var1;
   }

   public Object valueAt(int var1) {
      if (this.mGarbage) {
         this.gc();
      }

      return this.mValues[var1];
   }
}
