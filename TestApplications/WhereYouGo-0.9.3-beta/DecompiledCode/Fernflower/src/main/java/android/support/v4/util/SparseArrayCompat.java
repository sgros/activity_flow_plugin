package android.support.v4.util;

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
      int var2 = 0;
      int[] var3 = this.mKeys;
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

   public void append(int var1, Object var2) {
      if (this.mSize != 0 && var1 <= this.mKeys[this.mSize - 1]) {
         this.put(var1, var2);
      } else {
         if (this.mGarbage && this.mSize >= this.mKeys.length) {
            this.gc();
         }

         int var3 = this.mSize;
         if (var3 >= this.mKeys.length) {
            int var4 = ContainerHelpers.idealIntArraySize(var3 + 1);
            int[] var5 = new int[var4];
            Object[] var6 = new Object[var4];
            System.arraycopy(this.mKeys, 0, var5, 0, this.mKeys.length);
            System.arraycopy(this.mValues, 0, var6, 0, this.mValues.length);
            this.mKeys = var5;
            this.mValues = var6;
         }

         this.mKeys[var3] = var1;
         this.mValues[var3] = var2;
         this.mSize = var3 + 1;
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

   public SparseArrayCompat clone() {
      SparseArrayCompat var1 = null;

      boolean var10001;
      SparseArrayCompat var2;
      try {
         var2 = (SparseArrayCompat)super.clone();
      } catch (CloneNotSupportedException var5) {
         var10001 = false;
         return var1;
      }

      var1 = var2;

      try {
         var2.mKeys = (int[])this.mKeys.clone();
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

   public void delete(int var1) {
      var1 = ContainerHelpers.binarySearch(this.mKeys, this.mSize, var1);
      if (var1 >= 0 && this.mValues[var1] != DELETED) {
         this.mValues[var1] = DELETED;
         this.mGarbage = true;
      }

   }

   public Object get(int var1) {
      return this.get(var1, (Object)null);
   }

   public Object get(int var1, Object var2) {
      var1 = ContainerHelpers.binarySearch(this.mKeys, this.mSize, var1);
      Object var3 = var2;
      if (var1 >= 0) {
         if (this.mValues[var1] == DELETED) {
            var3 = var2;
         } else {
            var3 = this.mValues[var1];
         }
      }

      return var3;
   }

   public int indexOfKey(int var1) {
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
         if (var4 < this.mSize && this.mValues[var4] == DELETED) {
            this.mKeys[var4] = var1;
            this.mValues[var4] = var2;
         } else {
            var3 = var4;
            if (this.mGarbage) {
               var3 = var4;
               if (this.mSize >= this.mKeys.length) {
                  this.gc();
                  var3 = ~ContainerHelpers.binarySearch(this.mKeys, this.mSize, var1);
               }
            }

            if (this.mSize >= this.mKeys.length) {
               var4 = ContainerHelpers.idealIntArraySize(this.mSize + 1);
               int[] var5 = new int[var4];
               Object[] var6 = new Object[var4];
               System.arraycopy(this.mKeys, 0, var5, 0, this.mKeys.length);
               System.arraycopy(this.mValues, 0, var6, 0, this.mValues.length);
               this.mKeys = var5;
               this.mValues = var6;
            }

            if (this.mSize - var3 != 0) {
               System.arraycopy(this.mKeys, var3, this.mKeys, var3 + 1, this.mSize - var3);
               System.arraycopy(this.mValues, var3, this.mValues, var3 + 1, this.mSize - var3);
            }

            this.mKeys[var3] = var1;
            this.mValues[var3] = var2;
            ++this.mSize;
         }
      }

   }

   public void remove(int var1) {
      this.delete(var1);
   }

   public void removeAt(int var1) {
      if (this.mValues[var1] != DELETED) {
         this.mValues[var1] = DELETED;
         this.mGarbage = true;
      }

   }

   public void removeAtRange(int var1, int var2) {
      for(var2 = Math.min(this.mSize, var1 + var2); var1 < var2; ++var1) {
         this.removeAt(var1);
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
