package android.support.v4.util;

import android.support.annotation.RestrictTo;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class ArraySet implements Collection, Set {
   private static final int BASE_SIZE = 4;
   private static final int CACHE_SIZE = 10;
   private static final boolean DEBUG = false;
   private static final int[] INT = new int[0];
   private static final Object[] OBJECT = new Object[0];
   private static final String TAG = "ArraySet";
   static Object[] sBaseCache;
   static int sBaseCacheSize;
   static Object[] sTwiceBaseCache;
   static int sTwiceBaseCacheSize;
   Object[] mArray;
   MapCollections mCollections;
   int[] mHashes;
   final boolean mIdentityHashCode;
   int mSize;

   public ArraySet() {
      this(0, false);
   }

   public ArraySet(int var1) {
      this(var1, false);
   }

   public ArraySet(int var1, boolean var2) {
      this.mIdentityHashCode = var2;
      if (var1 == 0) {
         this.mHashes = INT;
         this.mArray = OBJECT;
      } else {
         this.allocArrays(var1);
      }

      this.mSize = 0;
   }

   public ArraySet(ArraySet var1) {
      this();
      if (var1 != null) {
         this.addAll(var1);
      }

   }

   public ArraySet(Collection var1) {
      this();
      if (var1 != null) {
         this.addAll(var1);
      }

   }

   private void allocArrays(int var1) {
      Throwable var75;
      Throwable var10000;
      boolean var10001;
      label758: {
         Object[] var2;
         if (var1 == 8) {
            synchronized(ArraySet.class){}

            label754: {
               try {
                  if (sTwiceBaseCache == null) {
                     break label754;
                  }

                  var2 = sTwiceBaseCache;
                  this.mArray = var2;
                  sTwiceBaseCache = (Object[])var2[0];
                  this.mHashes = (int[])var2[1];
               } catch (Throwable var72) {
                  var10000 = var72;
                  var10001 = false;
                  break label758;
               }

               var2[1] = null;
               var2[0] = null;

               try {
                  --sTwiceBaseCacheSize;
                  return;
               } catch (Throwable var69) {
                  var10000 = var69;
                  var10001 = false;
                  break label758;
               }
            }

            try {
               ;
            } catch (Throwable var71) {
               var10000 = var71;
               var10001 = false;
               break label758;
            }
         } else if (var1 == 4) {
            label757: {
               synchronized(ArraySet.class){}

               label743: {
                  label755: {
                     try {
                        if (sBaseCache == null) {
                           break label755;
                        }

                        var2 = sBaseCache;
                        this.mArray = var2;
                        sBaseCache = (Object[])var2[0];
                        this.mHashes = (int[])var2[1];
                     } catch (Throwable var74) {
                        var10000 = var74;
                        var10001 = false;
                        break label743;
                     }

                     var2[1] = null;
                     var2[0] = null;

                     try {
                        --sBaseCacheSize;
                        return;
                     } catch (Throwable var70) {
                        var10000 = var70;
                        var10001 = false;
                        break label743;
                     }
                  }

                  label736:
                  try {
                     break label757;
                  } catch (Throwable var73) {
                     var10000 = var73;
                     var10001 = false;
                     break label736;
                  }
               }

               while(true) {
                  var75 = var10000;

                  try {
                     throw var75;
                  } catch (Throwable var68) {
                     var10000 = var68;
                     var10001 = false;
                     continue;
                  }
               }
            }
         }

         this.mHashes = new int[var1];
         this.mArray = new Object[var1];
         return;
      }

      while(true) {
         var75 = var10000;

         try {
            throw var75;
         } catch (Throwable var67) {
            var10000 = var67;
            var10001 = false;
            continue;
         }
      }
   }

   private static void freeArrays(int[] var0, Object[] var1, int var2) {
      Throwable var75;
      Throwable var10000;
      boolean var10001;
      if (var0.length == 8) {
         synchronized(ArraySet.class){}

         label811: {
            label838: {
               try {
                  if (sTwiceBaseCacheSize >= 10) {
                     break label838;
                  }

                  var1[0] = sTwiceBaseCache;
               } catch (Throwable var71) {
                  var10000 = var71;
                  var10001 = false;
                  break label811;
               }

               var1[1] = var0;
               --var2;

               while(true) {
                  if (var2 < 2) {
                     try {
                        sTwiceBaseCache = var1;
                        ++sTwiceBaseCacheSize;
                        break;
                     } catch (Throwable var70) {
                        var10000 = var70;
                        var10001 = false;
                        break label811;
                     }
                  }

                  var1[var2] = null;
                  --var2;
               }
            }

            label798:
            try {
               return;
            } catch (Throwable var69) {
               var10000 = var69;
               var10001 = false;
               break label798;
            }
         }

         while(true) {
            var75 = var10000;

            try {
               throw var75;
            } catch (Throwable var67) {
               var10000 = var67;
               var10001 = false;
               continue;
            }
         }
      } else if (var0.length == 4) {
         synchronized(ArraySet.class){}

         label829: {
            label839: {
               try {
                  if (sBaseCacheSize >= 10) {
                     break label839;
                  }

                  var1[0] = sBaseCache;
               } catch (Throwable var74) {
                  var10000 = var74;
                  var10001 = false;
                  break label829;
               }

               var1[1] = var0;
               --var2;

               while(true) {
                  if (var2 < 2) {
                     try {
                        sBaseCache = var1;
                        ++sBaseCacheSize;
                        break;
                     } catch (Throwable var73) {
                        var10000 = var73;
                        var10001 = false;
                        break label829;
                     }
                  }

                  var1[var2] = null;
                  --var2;
               }
            }

            label816:
            try {
               return;
            } catch (Throwable var72) {
               var10000 = var72;
               var10001 = false;
               break label816;
            }
         }

         while(true) {
            var75 = var10000;

            try {
               throw var75;
            } catch (Throwable var68) {
               var10000 = var68;
               var10001 = false;
               continue;
            }
         }
      }
   }

   private MapCollections getCollection() {
      if (this.mCollections == null) {
         this.mCollections = new MapCollections() {
            protected void colClear() {
               ArraySet.this.clear();
            }

            protected Object colGetEntry(int var1, int var2) {
               return ArraySet.this.mArray[var1];
            }

            protected Map colGetMap() {
               throw new UnsupportedOperationException("not a map");
            }

            protected int colGetSize() {
               return ArraySet.this.mSize;
            }

            protected int colIndexOfKey(Object var1) {
               return ArraySet.this.indexOf(var1);
            }

            protected int colIndexOfValue(Object var1) {
               return ArraySet.this.indexOf(var1);
            }

            protected void colPut(Object var1, Object var2) {
               ArraySet.this.add(var1);
            }

            protected void colRemoveAt(int var1) {
               ArraySet.this.removeAt(var1);
            }

            protected Object colSetValue(int var1, Object var2) {
               throw new UnsupportedOperationException("not a map");
            }
         };
      }

      return this.mCollections;
   }

   private int indexOf(Object var1, int var2) {
      int var3 = this.mSize;
      if (var3 == 0) {
         return -1;
      } else {
         int var4 = ContainerHelpers.binarySearch(this.mHashes, var3, var2);
         if (var4 < 0) {
            return var4;
         } else if (var1.equals(this.mArray[var4])) {
            return var4;
         } else {
            int var5;
            for(var5 = var4 + 1; var5 < var3 && this.mHashes[var5] == var2; ++var5) {
               if (var1.equals(this.mArray[var5])) {
                  return var5;
               }
            }

            --var4;

            while(var4 >= 0 && this.mHashes[var4] == var2) {
               if (var1.equals(this.mArray[var4])) {
                  return var4;
               }

               --var4;
            }

            return ~var5;
         }
      }
   }

   private int indexOfNull() {
      int var1 = this.mSize;
      if (var1 == 0) {
         return -1;
      } else {
         int var2 = ContainerHelpers.binarySearch(this.mHashes, var1, 0);
         if (var2 < 0) {
            return var2;
         } else if (this.mArray[var2] == null) {
            return var2;
         } else {
            int var3;
            for(var3 = var2 + 1; var3 < var1 && this.mHashes[var3] == 0; ++var3) {
               if (this.mArray[var3] == null) {
                  return var3;
               }
            }

            --var2;

            while(var2 >= 0 && this.mHashes[var2] == 0) {
               if (this.mArray[var2] == null) {
                  return var2;
               }

               --var2;
            }

            return ~var3;
         }
      }
   }

   public boolean add(Object var1) {
      int var2;
      int var3;
      int var4;
      if (var1 == null) {
         var2 = this.indexOfNull();
         var3 = 0;
      } else {
         if (this.mIdentityHashCode) {
            var2 = System.identityHashCode(var1);
         } else {
            var2 = var1.hashCode();
         }

         var4 = this.indexOf(var1, var2);
         var3 = var2;
         var2 = var4;
      }

      if (var2 >= 0) {
         return false;
      } else {
         var4 = ~var2;
         int[] var6;
         if (this.mSize >= this.mHashes.length) {
            int var5 = this.mSize;
            var2 = 4;
            if (var5 >= 8) {
               var2 = this.mSize;
               var2 += this.mSize >> 1;
            } else if (this.mSize >= 4) {
               var2 = 8;
            }

            var6 = this.mHashes;
            Object[] var7 = this.mArray;
            this.allocArrays(var2);
            if (this.mHashes.length > 0) {
               System.arraycopy(var6, 0, this.mHashes, 0, var6.length);
               System.arraycopy(var7, 0, this.mArray, 0, var7.length);
            }

            freeArrays(var6, var7, this.mSize);
         }

         if (var4 < this.mSize) {
            var6 = this.mHashes;
            int[] var8 = this.mHashes;
            var2 = var4 + 1;
            System.arraycopy(var6, var4, var8, var2, this.mSize - var4);
            System.arraycopy(this.mArray, var4, this.mArray, var2, this.mSize - var4);
         }

         this.mHashes[var4] = var3;
         this.mArray[var4] = var1;
         ++this.mSize;
         return true;
      }
   }

   public void addAll(ArraySet var1) {
      int var2 = var1.mSize;
      this.ensureCapacity(this.mSize + var2);
      int var3 = this.mSize;
      int var4 = 0;
      if (var3 == 0) {
         if (var2 > 0) {
            System.arraycopy(var1.mHashes, 0, this.mHashes, 0, var2);
            System.arraycopy(var1.mArray, 0, this.mArray, 0, var2);
            this.mSize = var2;
         }
      } else {
         while(var4 < var2) {
            this.add(var1.valueAt(var4));
            ++var4;
         }
      }

   }

   public boolean addAll(Collection var1) {
      this.ensureCapacity(this.mSize + var1.size());
      Iterator var3 = var1.iterator();

      boolean var2;
      for(var2 = false; var3.hasNext(); var2 |= this.add(var3.next())) {
      }

      return var2;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void append(Object var1) {
      int var2 = this.mSize;
      int var3;
      if (var1 == null) {
         var3 = 0;
      } else if (this.mIdentityHashCode) {
         var3 = System.identityHashCode(var1);
      } else {
         var3 = var1.hashCode();
      }

      if (var2 >= this.mHashes.length) {
         throw new IllegalStateException("Array is full");
      } else if (var2 > 0 && this.mHashes[var2 - 1] > var3) {
         this.add(var1);
      } else {
         this.mSize = var2 + 1;
         this.mHashes[var2] = var3;
         this.mArray[var2] = var1;
      }
   }

   public void clear() {
      if (this.mSize != 0) {
         freeArrays(this.mHashes, this.mArray, this.mSize);
         this.mHashes = INT;
         this.mArray = OBJECT;
         this.mSize = 0;
      }

   }

   public boolean contains(Object var1) {
      boolean var2;
      if (this.indexOf(var1) >= 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean containsAll(Collection var1) {
      Iterator var2 = var1.iterator();

      do {
         if (!var2.hasNext()) {
            return true;
         }
      } while(this.contains(var2.next()));

      return false;
   }

   public void ensureCapacity(int var1) {
      if (this.mHashes.length < var1) {
         int[] var2 = this.mHashes;
         Object[] var3 = this.mArray;
         this.allocArrays(var1);
         if (this.mSize > 0) {
            System.arraycopy(var2, 0, this.mHashes, 0, this.mSize);
            System.arraycopy(var3, 0, this.mArray, 0, this.mSize);
         }

         freeArrays(var2, var3, this.mSize);
      }

   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof Set)) {
         return false;
      } else {
         Set var6 = (Set)var1;
         if (this.size() != var6.size()) {
            return false;
         } else {
            int var2 = 0;

            while(true) {
               boolean var3;
               try {
                  if (var2 >= this.mSize) {
                     return true;
                  }

                  var3 = var6.contains(this.valueAt(var2));
               } catch (NullPointerException var4) {
                  return false;
               } catch (ClassCastException var5) {
                  return false;
               }

               if (!var3) {
                  return false;
               }

               ++var2;
            }
         }
      }
   }

   public int hashCode() {
      int[] var1 = this.mHashes;
      int var2 = this.mSize;
      int var3 = 0;

      int var4;
      for(var4 = 0; var3 < var2; ++var3) {
         var4 += var1[var3];
      }

      return var4;
   }

   public int indexOf(Object var1) {
      int var2;
      if (var1 == null) {
         var2 = this.indexOfNull();
      } else {
         if (this.mIdentityHashCode) {
            var2 = System.identityHashCode(var1);
         } else {
            var2 = var1.hashCode();
         }

         var2 = this.indexOf(var1, var2);
      }

      return var2;
   }

   public boolean isEmpty() {
      boolean var1;
      if (this.mSize <= 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public Iterator iterator() {
      return this.getCollection().getKeySet().iterator();
   }

   public boolean remove(Object var1) {
      int var2 = this.indexOf(var1);
      if (var2 >= 0) {
         this.removeAt(var2);
         return true;
      } else {
         return false;
      }
   }

   public boolean removeAll(ArraySet var1) {
      int var2 = var1.mSize;
      int var3 = this.mSize;
      boolean var4 = false;

      for(int var5 = 0; var5 < var2; ++var5) {
         this.remove(var1.valueAt(var5));
      }

      if (var3 != this.mSize) {
         var4 = true;
      }

      return var4;
   }

   public boolean removeAll(Collection var1) {
      Iterator var3 = var1.iterator();

      boolean var2;
      for(var2 = false; var3.hasNext(); var2 |= this.remove(var3.next())) {
      }

      return var2;
   }

   public Object removeAt(int var1) {
      Object var2 = this.mArray[var1];
      if (this.mSize <= 1) {
         freeArrays(this.mHashes, this.mArray, this.mSize);
         this.mHashes = INT;
         this.mArray = OBJECT;
         this.mSize = 0;
      } else {
         int[] var3 = this.mHashes;
         int var4 = 8;
         if (var3.length > 8 && this.mSize < this.mHashes.length / 3) {
            if (this.mSize > 8) {
               var4 = this.mSize;
               var4 += this.mSize >> 1;
            }

            int[] var5 = this.mHashes;
            Object[] var6 = this.mArray;
            this.allocArrays(var4);
            --this.mSize;
            if (var1 > 0) {
               System.arraycopy(var5, 0, this.mHashes, 0, var1);
               System.arraycopy(var6, 0, this.mArray, 0, var1);
            }

            if (var1 < this.mSize) {
               var4 = var1 + 1;
               System.arraycopy(var5, var4, this.mHashes, var1, this.mSize - var1);
               System.arraycopy(var6, var4, this.mArray, var1, this.mSize - var1);
            }
         } else {
            --this.mSize;
            if (var1 < this.mSize) {
               var3 = this.mHashes;
               var4 = var1 + 1;
               System.arraycopy(var3, var4, this.mHashes, var1, this.mSize - var1);
               System.arraycopy(this.mArray, var4, this.mArray, var1, this.mSize - var1);
            }

            this.mArray[this.mSize] = null;
         }
      }

      return var2;
   }

   public boolean retainAll(Collection var1) {
      int var2 = this.mSize - 1;

      boolean var3;
      for(var3 = false; var2 >= 0; --var2) {
         if (!var1.contains(this.mArray[var2])) {
            this.removeAt(var2);
            var3 = true;
         }
      }

      return var3;
   }

   public int size() {
      return this.mSize;
   }

   public Object[] toArray() {
      Object[] var1 = new Object[this.mSize];
      System.arraycopy(this.mArray, 0, var1, 0, this.mSize);
      return var1;
   }

   public Object[] toArray(Object[] var1) {
      Object[] var2 = var1;
      if (var1.length < this.mSize) {
         var2 = (Object[])Array.newInstance(var1.getClass().getComponentType(), this.mSize);
      }

      System.arraycopy(this.mArray, 0, var2, 0, this.mSize);
      if (var2.length > this.mSize) {
         var2[this.mSize] = null;
      }

      return var2;
   }

   public String toString() {
      if (this.isEmpty()) {
         return "{}";
      } else {
         StringBuilder var1 = new StringBuilder(this.mSize * 14);
         var1.append('{');

         for(int var2 = 0; var2 < this.mSize; ++var2) {
            if (var2 > 0) {
               var1.append(", ");
            }

            Object var3 = this.valueAt(var2);
            if (var3 != this) {
               var1.append(var3);
            } else {
               var1.append("(this Set)");
            }
         }

         var1.append('}');
         return var1.toString();
      }
   }

   public Object valueAt(int var1) {
      return this.mArray[var1];
   }
}
