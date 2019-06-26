package android.support.v4.util;

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
      label779: {
         Object[] var2;
         label801: {
            if (var1 == 8) {
               synchronized(ArraySet.class){}

               try {
                  if (sTwiceBaseCache != null) {
                     var2 = sTwiceBaseCache;
                     this.mArray = var2;
                     sTwiceBaseCache = (Object[])var2[0];
                     this.mHashes = (int[])var2[1];
                     break label801;
                  }
               } catch (Throwable var72) {
                  var10000 = var72;
                  var10001 = false;
                  break label779;
               }

               try {
                  ;
               } catch (Throwable var71) {
                  var10000 = var71;
                  var10001 = false;
                  break label779;
               }
            } else if (var1 == 4) {
               label784: {
                  synchronized(ArraySet.class){}

                  label768: {
                     label782: {
                        try {
                           if (sBaseCache == null) {
                              break label782;
                           }

                           var2 = sBaseCache;
                           this.mArray = var2;
                           sBaseCache = (Object[])var2[0];
                           this.mHashes = (int[])var2[1];
                        } catch (Throwable var74) {
                           var10000 = var74;
                           var10001 = false;
                           break label768;
                        }

                        var2[1] = null;
                        var2[0] = null;

                        try {
                           --sBaseCacheSize;
                           return;
                        } catch (Throwable var70) {
                           var10000 = var70;
                           var10001 = false;
                           break label768;
                        }
                     }

                     label761:
                     try {
                        break label784;
                     } catch (Throwable var73) {
                        var10000 = var73;
                        var10001 = false;
                        break label761;
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
               }
            }

            this.mHashes = new int[var1];
            this.mArray = new Object[var1];
            return;
         }

         var2[1] = null;
         var2[0] = null;

         label742:
         try {
            --sTwiceBaseCacheSize;
            return;
         } catch (Throwable var69) {
            var10000 = var69;
            var10001 = false;
            break label742;
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

   private static void freeArrays(int[] var0, Object[] var1, int var2) {
      Throwable var75;
      Throwable var10000;
      boolean var10001;
      if (var0.length == 8) {
         synchronized(ArraySet.class){}

         label810: {
            label837: {
               try {
                  if (sTwiceBaseCacheSize >= 10) {
                     break label837;
                  }

                  var1[0] = sTwiceBaseCache;
               } catch (Throwable var71) {
                  var10000 = var71;
                  var10001 = false;
                  break label810;
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
                        break label810;
                     }
                  }

                  var1[var2] = null;
                  --var2;
               }
            }

            label797:
            try {
               return;
            } catch (Throwable var69) {
               var10000 = var69;
               var10001 = false;
               break label797;
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

         label828: {
            label838: {
               try {
                  if (sBaseCacheSize >= 10) {
                     break label838;
                  }

                  var1[0] = sBaseCache;
               } catch (Throwable var74) {
                  var10000 = var74;
                  var10001 = false;
                  break label828;
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
                        break label828;
                     }
                  }

                  var1[var2] = null;
                  --var2;
               }
            }

            label815:
            try {
               return;
            } catch (Throwable var72) {
               var10000 = var72;
               var10001 = false;
               break label815;
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
      int var4;
      if (var3 == 0) {
         var4 = -1;
      } else {
         int var5 = ContainerHelpers.binarySearch(this.mHashes, var3, var2);
         var4 = var5;
         if (var5 >= 0) {
            var4 = var5;
            if (!var1.equals(this.mArray[var5])) {
               for(var4 = var5 + 1; var4 < var3 && this.mHashes[var4] == var2; ++var4) {
                  if (var1.equals(this.mArray[var4])) {
                     return var4;
                  }
               }

               --var5;

               while(var5 >= 0 && this.mHashes[var5] == var2) {
                  if (var1.equals(this.mArray[var5])) {
                     var4 = var5;
                     return var4;
                  }

                  --var5;
               }

               var4 = ~var4;
            }
         }
      }

      return var4;
   }

   private int indexOfNull() {
      int var1 = this.mSize;
      int var2;
      if (var1 == 0) {
         var2 = -1;
      } else {
         int var3 = ContainerHelpers.binarySearch(this.mHashes, var1, 0);
         var2 = var3;
         if (var3 >= 0) {
            var2 = var3;
            if (this.mArray[var3] != null) {
               for(var2 = var3 + 1; var2 < var1 && this.mHashes[var2] == 0; ++var2) {
                  if (this.mArray[var2] == null) {
                     return var2;
                  }
               }

               --var3;

               while(var3 >= 0 && this.mHashes[var3] == 0) {
                  if (this.mArray[var3] == null) {
                     var2 = var3;
                     return var2;
                  }

                  --var3;
               }

               var2 = ~var2;
            }
         }
      }

      return var2;
   }

   public boolean add(Object var1) {
      byte var2 = 8;
      int var3;
      int var4;
      int var6;
      if (var1 == null) {
         var3 = 0;
         var4 = this.indexOfNull();
      } else {
         if (this.mIdentityHashCode) {
            var4 = System.identityHashCode(var1);
         } else {
            var4 = var1.hashCode();
         }

         var6 = this.indexOf(var1, var4);
         var3 = var4;
         var4 = var6;
      }

      boolean var5;
      if (var4 >= 0) {
         var5 = false;
      } else {
         var6 = ~var4;
         if (this.mSize >= this.mHashes.length) {
            if (this.mSize >= 8) {
               var4 = this.mSize + (this.mSize >> 1);
            } else {
               var4 = var2;
               if (this.mSize < 4) {
                  var4 = 4;
               }
            }

            int[] var7 = this.mHashes;
            Object[] var8 = this.mArray;
            this.allocArrays(var4);
            if (this.mHashes.length > 0) {
               System.arraycopy(var7, 0, this.mHashes, 0, var7.length);
               System.arraycopy(var8, 0, this.mArray, 0, var8.length);
            }

            freeArrays(var7, var8, this.mSize);
         }

         if (var6 < this.mSize) {
            System.arraycopy(this.mHashes, var6, this.mHashes, var6 + 1, this.mSize - var6);
            System.arraycopy(this.mArray, var6, this.mArray, var6 + 1, this.mSize - var6);
         }

         this.mHashes[var6] = var3;
         this.mArray[var6] = var1;
         ++this.mSize;
         var5 = true;
      }

      return var5;
   }

   public void addAll(ArraySet var1) {
      int var2 = var1.mSize;
      this.ensureCapacity(this.mSize + var2);
      if (this.mSize == 0) {
         if (var2 > 0) {
            System.arraycopy(var1.mHashes, 0, this.mHashes, 0, var2);
            System.arraycopy(var1.mArray, 0, this.mArray, 0, var2);
            this.mSize = var2;
         }
      } else {
         for(int var3 = 0; var3 < var2; ++var3) {
            this.add(var1.valueAt(var3));
         }
      }

   }

   public boolean addAll(Collection var1) {
      this.ensureCapacity(this.mSize + var1.size());
      boolean var2 = false;

      for(Iterator var3 = var1.iterator(); var3.hasNext(); var2 |= this.add(var3.next())) {
      }

      return var2;
   }

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
      } else {
         if (var2 > 0 && this.mHashes[var2 - 1] > var3) {
            this.add(var1);
         } else {
            this.mSize = var2 + 1;
            this.mHashes[var2] = var3;
            this.mArray[var2] = var1;
         }

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
      Iterator var3 = var1.iterator();

      boolean var2;
      while(true) {
         if (var3.hasNext()) {
            if (this.contains(var3.next())) {
               continue;
            }

            var2 = false;
            break;
         }

         var2 = true;
         break;
      }

      return var2;
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
      boolean var2 = true;
      boolean var3;
      if (this == var1) {
         var3 = var2;
      } else if (var1 instanceof Set) {
         Set var7 = (Set)var1;
         if (this.size() != var7.size()) {
            var3 = false;
         } else {
            int var4 = 0;

            while(true) {
               var3 = var2;

               try {
                  if (var4 >= this.mSize) {
                     break;
                  }

                  var3 = var7.contains(this.valueAt(var4));
               } catch (NullPointerException var5) {
                  var3 = false;
                  break;
               } catch (ClassCastException var6) {
                  var3 = false;
                  break;
               }

               if (!var3) {
                  var3 = false;
                  break;
               }

               ++var4;
            }
         }
      } else {
         var3 = false;
      }

      return var3;
   }

   public int hashCode() {
      int[] var1 = this.mHashes;
      int var2 = 0;
      int var3 = 0;

      for(int var4 = this.mSize; var3 < var4; ++var3) {
         var2 += var1[var3];
      }

      return var2;
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
      boolean var3;
      if (var2 >= 0) {
         this.removeAt(var2);
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public boolean removeAll(ArraySet var1) {
      int var2 = var1.mSize;
      int var3 = this.mSize;

      for(int var4 = 0; var4 < var2; ++var4) {
         this.remove(var1.valueAt(var4));
      }

      boolean var5;
      if (var3 != this.mSize) {
         var5 = true;
      } else {
         var5 = false;
      }

      return var5;
   }

   public boolean removeAll(Collection var1) {
      boolean var2 = false;

      for(Iterator var3 = var1.iterator(); var3.hasNext(); var2 |= this.remove(var3.next())) {
      }

      return var2;
   }

   public Object removeAt(int var1) {
      int var2 = 8;
      Object var3 = this.mArray[var1];
      if (this.mSize <= 1) {
         freeArrays(this.mHashes, this.mArray, this.mSize);
         this.mHashes = INT;
         this.mArray = OBJECT;
         this.mSize = 0;
      } else if (this.mHashes.length > 8 && this.mSize < this.mHashes.length / 3) {
         if (this.mSize > 8) {
            var2 = this.mSize + (this.mSize >> 1);
         }

         int[] var4 = this.mHashes;
         Object[] var5 = this.mArray;
         this.allocArrays(var2);
         --this.mSize;
         if (var1 > 0) {
            System.arraycopy(var4, 0, this.mHashes, 0, var1);
            System.arraycopy(var5, 0, this.mArray, 0, var1);
         }

         if (var1 < this.mSize) {
            System.arraycopy(var4, var1 + 1, this.mHashes, var1, this.mSize - var1);
            System.arraycopy(var5, var1 + 1, this.mArray, var1, this.mSize - var1);
         }
      } else {
         --this.mSize;
         if (var1 < this.mSize) {
            System.arraycopy(this.mHashes, var1 + 1, this.mHashes, var1, this.mSize - var1);
            System.arraycopy(this.mArray, var1 + 1, this.mArray, var1, this.mSize - var1);
         }

         this.mArray[this.mSize] = null;
      }

      return var3;
   }

   public boolean retainAll(Collection var1) {
      boolean var2 = false;

      for(int var3 = this.mSize - 1; var3 >= 0; --var3) {
         if (!var1.contains(this.mArray[var3])) {
            this.removeAt(var3);
            var2 = true;
         }
      }

      return var2;
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
      String var1;
      if (this.isEmpty()) {
         var1 = "{}";
      } else {
         StringBuilder var4 = new StringBuilder(this.mSize * 14);
         var4.append('{');

         for(int var2 = 0; var2 < this.mSize; ++var2) {
            if (var2 > 0) {
               var4.append(", ");
            }

            Object var3 = this.valueAt(var2);
            if (var3 != this) {
               var4.append(var3);
            } else {
               var4.append("(this Set)");
            }
         }

         var4.append('}');
         var1 = var4.toString();
      }

      return var1;
   }

   public Object valueAt(int var1) {
      return this.mArray[var1];
   }
}
