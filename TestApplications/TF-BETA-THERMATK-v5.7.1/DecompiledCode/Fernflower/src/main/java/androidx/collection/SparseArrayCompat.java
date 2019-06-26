package androidx.collection;

public class SparseArrayCompat implements Cloneable {
   private static final Object DELETED = new Object();
   private boolean mGarbage;
   private int[] mKeys;
   private int mSize;
   private Object[] mValues;

   public SparseArrayCompat() {
      this(10);
   }

   public SparseArrayCompat(int var1) {
      this.mGarbage = false;
      if (var1 == 0) {
         this.mKeys = ContainerHelpers.EMPTY_INTS;
         this.mValues = ContainerHelpers.EMPTY_OBJECTS;
      } else {
         var1 = ContainerHelpers.idealIntArraySize(var1);
         this.mKeys = new int[var1];
         this.mValues = new Object[var1];
      }

      this.mSize = 0;
   }

   private void gc() {
      int var1 = this.mSize;
      int[] var2 = this.mKeys;
      Object[] var3 = this.mValues;
      int var4 = 0;

      int var5;
      int var7;
      for(var5 = 0; var4 < var1; var5 = var7) {
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

   public void append(int var1, Object var2) {
      int var3 = this.mSize;
      if (var3 != 0 && var1 <= this.mKeys[var3 - 1]) {
         this.put(var1, var2);
      } else {
         if (this.mGarbage && this.mSize >= this.mKeys.length) {
            this.gc();
         }

         var3 = this.mSize;
         if (var3 >= this.mKeys.length) {
            int var4 = ContainerHelpers.idealIntArraySize(var3 + 1);
            int[] var5 = new int[var4];
            Object[] var6 = new Object[var4];
            int[] var7 = this.mKeys;
            System.arraycopy(var7, 0, var5, 0, var7.length);
            Object[] var8 = this.mValues;
            System.arraycopy(var8, 0, var6, 0, var8.length);
            this.mKeys = var5;
            this.mValues = var6;
         }

         this.mKeys[var3] = var1;
         this.mValues[var3] = var2;
         this.mSize = var3 + 1;
      }
   }

   public SparseArrayCompat clone() {
      try {
         SparseArrayCompat var1 = (SparseArrayCompat)super.clone();
         var1.mKeys = (int[])this.mKeys.clone();
         var1.mValues = (Object[])this.mValues.clone();
         return var1;
      } catch (CloneNotSupportedException var2) {
         throw new AssertionError(var2);
      }
   }

   public Object get(int var1) {
      return this.get(var1, (Object)null);
   }

   public Object get(int var1, Object var2) {
      var1 = ContainerHelpers.binarySearch(this.mKeys, this.mSize, var1);
      if (var1 >= 0) {
         Object[] var3 = this.mValues;
         if (var3[var1] != DELETED) {
            return var3[var1];
         }
      }

      return var2;
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

   public int keyAt(int var1) {
      if (this.mGarbage) {
         this.gc();
      }

      return this.mKeys[var1];
   }

   public void put(int var1, Object var2) {
      int var3 = ContainerHelpers.binarySearch(this.mKeys, this.mSize, var1);
      if (var3 >= 0) {
         this.mValues[var3] = var2;
      } else {
         int var4 = ~var3;
         Object[] var5;
         if (var4 < this.mSize) {
            var5 = this.mValues;
            if (var5[var4] == DELETED) {
               this.mKeys[var4] = var1;
               var5[var4] = var2;
               return;
            }
         }

         var3 = var4;
         if (this.mGarbage) {
            var3 = var4;
            if (this.mSize >= this.mKeys.length) {
               this.gc();
               var3 = ~ContainerHelpers.binarySearch(this.mKeys, this.mSize, var1);
            }
         }

         var4 = this.mSize;
         int[] var9;
         if (var4 >= this.mKeys.length) {
            var4 = ContainerHelpers.idealIntArraySize(var4 + 1);
            var9 = new int[var4];
            Object[] var6 = new Object[var4];
            int[] var7 = this.mKeys;
            System.arraycopy(var7, 0, var9, 0, var7.length);
            Object[] var10 = this.mValues;
            System.arraycopy(var10, 0, var6, 0, var10.length);
            this.mKeys = var9;
            this.mValues = var6;
         }

         var4 = this.mSize;
         if (var4 - var3 != 0) {
            var9 = this.mKeys;
            int var8 = var3 + 1;
            System.arraycopy(var9, var3, var9, var8, var4 - var3);
            var5 = this.mValues;
            System.arraycopy(var5, var3, var5, var8, this.mSize - var3);
         }

         this.mKeys[var3] = var1;
         this.mValues[var3] = var2;
         ++this.mSize;
      }

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
