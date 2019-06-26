package androidx.collection;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class ArraySet implements Collection, Set {
   private static final int[] INT = new int[0];
   private static final Object[] OBJECT = new Object[0];
   private static Object[] sBaseCache;
   private static int sBaseCacheSize;
   private static Object[] sTwiceBaseCache;
   private static int sTwiceBaseCacheSize;
   Object[] mArray;
   private MapCollections mCollections;
   private int[] mHashes;
   int mSize;

   public ArraySet() {
      this(0);
   }

   public ArraySet(int var1) {
      if (var1 == 0) {
         this.mHashes = INT;
         this.mArray = OBJECT;
      } else {
         this.allocArrays(var1);
      }

      this.mSize = 0;
   }

   private void allocArrays(int var1) {
      Throwable var75;
      Throwable var10000;
      boolean var10001;
      label755: {
         Object[] var2;
         label761: {
            if (var1 == 8) {
               synchronized(ArraySet.class){}

               try {
                  if (sTwiceBaseCache != null) {
                     var2 = sTwiceBaseCache;
                     this.mArray = var2;
                     sTwiceBaseCache = (Object[])var2[0];
                     this.mHashes = (int[])var2[1];
                     break label761;
                  }
               } catch (Throwable var72) {
                  var10000 = var72;
                  var10001 = false;
                  break label755;
               }

               try {
                  ;
               } catch (Throwable var71) {
                  var10000 = var71;
                  var10001 = false;
                  break label755;
               }
            } else if (var1 == 4) {
               label760: {
                  synchronized(ArraySet.class){}

                  label745: {
                     label758: {
                        try {
                           if (sBaseCache == null) {
                              break label758;
                           }

                           var2 = sBaseCache;
                           this.mArray = var2;
                           sBaseCache = (Object[])var2[0];
                           this.mHashes = (int[])var2[1];
                        } catch (Throwable var74) {
                           var10000 = var74;
                           var10001 = false;
                           break label745;
                        }

                        var2[1] = null;
                        var2[0] = null;

                        try {
                           --sBaseCacheSize;
                           return;
                        } catch (Throwable var70) {
                           var10000 = var70;
                           var10001 = false;
                           break label745;
                        }
                     }

                     label738:
                     try {
                        break label760;
                     } catch (Throwable var73) {
                        var10000 = var73;
                        var10001 = false;
                        break label738;
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

         var2[1] = null;
         var2[0] = null;

         label719:
         try {
            --sTwiceBaseCacheSize;
            return;
         } catch (Throwable var69) {
            var10000 = var69;
            var10001 = false;
            break label719;
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

            for(var3 = var4 - 1; var3 >= 0 && this.mHashes[var3] == var2; --var3) {
               if (var1.equals(this.mArray[var3])) {
                  return var3;
               }
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
      if (var1 == null) {
         var2 = this.indexOfNull();
         var3 = 0;
      } else {
         var3 = var1.hashCode();
         var2 = this.indexOf(var1, var3);
      }

      if (var2 >= 0) {
         return false;
      } else {
         int var4 = ~var2;
         int var5 = this.mSize;
         int[] var8;
         if (var5 >= this.mHashes.length) {
            var2 = 4;
            if (var5 >= 8) {
               var2 = (var5 >> 1) + var5;
            } else if (var5 >= 4) {
               var2 = 8;
            }

            int[] var6 = this.mHashes;
            Object[] var7 = this.mArray;
            this.allocArrays(var2);
            var8 = this.mHashes;
            if (var8.length > 0) {
               System.arraycopy(var6, 0, var8, 0, var6.length);
               System.arraycopy(var7, 0, this.mArray, 0, var7.length);
            }

            freeArrays(var6, var7, this.mSize);
         }

         var2 = this.mSize;
         if (var4 < var2) {
            var8 = this.mHashes;
            var5 = var4 + 1;
            System.arraycopy(var8, var4, var8, var5, var2 - var4);
            Object[] var9 = this.mArray;
            System.arraycopy(var9, var4, var9, var5, this.mSize - var4);
         }

         this.mHashes[var4] = var3;
         this.mArray[var4] = var1;
         ++this.mSize;
         return true;
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

   public void clear() {
      int var1 = this.mSize;
      if (var1 != 0) {
         freeArrays(this.mHashes, this.mArray, var1);
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
      int[] var2 = this.mHashes;
      if (var2.length < var1) {
         Object[] var3 = this.mArray;
         this.allocArrays(var1);
         var1 = this.mSize;
         if (var1 > 0) {
            System.arraycopy(var2, 0, this.mHashes, 0, var1);
            System.arraycopy(var3, 0, this.mArray, 0, this.mSize);
         }

         freeArrays(var2, var3, this.mSize);
      }

   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else {
         if (var1 instanceof Set) {
            Set var5 = (Set)var1;
            if (this.size() != var5.size()) {
               return false;
            }

            int var2 = 0;

            while(true) {
               boolean var3;
               try {
                  if (var2 >= this.mSize) {
                     return true;
                  }

                  var3 = var5.contains(this.valueAt(var2));
               } catch (ClassCastException | NullPointerException var4) {
                  break;
               }

               if (!var3) {
                  return false;
               }

               ++var2;
            }
         }

         return false;
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
         var2 = this.indexOf(var1, var1.hashCode());
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

   public boolean removeAll(Collection var1) {
      Iterator var3 = var1.iterator();

      boolean var2;
      for(var2 = false; var3.hasNext(); var2 |= this.remove(var3.next())) {
      }

      return var2;
   }

   public Object removeAt(int var1) {
      Object[] var2 = this.mArray;
      Object var3 = var2[var1];
      int var4 = this.mSize;
      if (var4 <= 1) {
         freeArrays(this.mHashes, var2, var4);
         this.mHashes = INT;
         this.mArray = OBJECT;
         this.mSize = 0;
      } else {
         int[] var8 = this.mHashes;
         int var5 = var8.length;
         int var6 = 8;
         if (var5 > 8 && var4 < var8.length / 3) {
            if (var4 > 8) {
               var6 = var4 + (var4 >> 1);
            }

            int[] var7 = this.mHashes;
            var2 = this.mArray;
            this.allocArrays(var6);
            --this.mSize;
            if (var1 > 0) {
               System.arraycopy(var7, 0, this.mHashes, 0, var1);
               System.arraycopy(var2, 0, this.mArray, 0, var1);
            }

            var4 = this.mSize;
            if (var1 < var4) {
               var6 = var1 + 1;
               System.arraycopy(var7, var6, this.mHashes, var1, var4 - var1);
               System.arraycopy(var2, var6, this.mArray, var1, this.mSize - var1);
            }
         } else {
            --this.mSize;
            var6 = this.mSize;
            if (var1 < var6) {
               var8 = this.mHashes;
               var4 = var1 + 1;
               System.arraycopy(var8, var4, var8, var1, var6 - var1);
               var2 = this.mArray;
               System.arraycopy(var2, var4, var2, var1, this.mSize - var1);
            }

            this.mArray[this.mSize] = null;
         }
      }

      return var3;
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
      int var1 = this.mSize;
      Object[] var2 = new Object[var1];
      System.arraycopy(this.mArray, 0, var2, 0, var1);
      return var2;
   }

   public Object[] toArray(Object[] var1) {
      Object[] var2 = var1;
      if (var1.length < this.mSize) {
         var2 = (Object[])Array.newInstance(var1.getClass().getComponentType(), this.mSize);
      }

      System.arraycopy(this.mArray, 0, var2, 0, this.mSize);
      int var3 = var2.length;
      int var4 = this.mSize;
      if (var3 > var4) {
         var2[var4] = null;
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
