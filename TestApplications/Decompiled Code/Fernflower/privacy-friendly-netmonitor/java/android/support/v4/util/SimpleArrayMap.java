package android.support.v4.util;

import java.util.ConcurrentModificationException;
import java.util.Map;

public class SimpleArrayMap {
   private static final int BASE_SIZE = 4;
   private static final int CACHE_SIZE = 10;
   private static final boolean CONCURRENT_MODIFICATION_EXCEPTIONS = true;
   private static final boolean DEBUG = false;
   private static final String TAG = "ArrayMap";
   static Object[] mBaseCache;
   static int mBaseCacheSize;
   static Object[] mTwiceBaseCache;
   static int mTwiceBaseCacheSize;
   Object[] mArray;
   int[] mHashes;
   int mSize;

   public SimpleArrayMap() {
      this.mHashes = ContainerHelpers.EMPTY_INTS;
      this.mArray = ContainerHelpers.EMPTY_OBJECTS;
      this.mSize = 0;
   }

   public SimpleArrayMap(int var1) {
      if (var1 == 0) {
         this.mHashes = ContainerHelpers.EMPTY_INTS;
         this.mArray = ContainerHelpers.EMPTY_OBJECTS;
      } else {
         this.allocArrays(var1);
      }

      this.mSize = 0;
   }

   public SimpleArrayMap(SimpleArrayMap var1) {
      this();
      if (var1 != null) {
         this.putAll(var1);
      }

   }

   private void allocArrays(int var1) {
      Throwable var75;
      Throwable var10000;
      boolean var10001;
      label756: {
         Object[] var2;
         label762: {
            if (var1 == 8) {
               label758: {
                  synchronized(ArrayMap.class){}

                  label759: {
                     label738: {
                        try {
                           if (mTwiceBaseCache != null) {
                              var2 = mTwiceBaseCache;
                              this.mArray = var2;
                              mTwiceBaseCache = (Object[])var2[0];
                              this.mHashes = (int[])var2[1];
                              break label738;
                           }
                        } catch (Throwable var73) {
                           var10000 = var73;
                           var10001 = false;
                           break label759;
                        }

                        try {
                           break label758;
                        } catch (Throwable var72) {
                           var10000 = var72;
                           var10001 = false;
                           break label759;
                        }
                     }

                     var2[1] = null;
                     var2[0] = null;

                     label719:
                     try {
                        --mTwiceBaseCacheSize;
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
                     } catch (Throwable var68) {
                        var10000 = var68;
                        var10001 = false;
                        continue;
                     }
                  }
               }
            } else if (var1 == 4) {
               synchronized(ArrayMap.class){}

               try {
                  if (mBaseCache != null) {
                     var2 = mBaseCache;
                     this.mArray = var2;
                     mBaseCache = (Object[])var2[0];
                     this.mHashes = (int[])var2[1];
                     break label762;
                  }
               } catch (Throwable var74) {
                  var10000 = var74;
                  var10001 = false;
                  break label756;
               }

               try {
                  ;
               } catch (Throwable var71) {
                  var10000 = var71;
                  var10001 = false;
                  break label756;
               }
            }

            this.mHashes = new int[var1];
            this.mArray = new Object[var1 << 1];
            return;
         }

         var2[1] = null;
         var2[0] = null;

         label722:
         try {
            --mBaseCacheSize;
            return;
         } catch (Throwable var70) {
            var10000 = var70;
            var10001 = false;
            break label722;
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

   private static int binarySearchHashes(int[] var0, int var1, int var2) {
      try {
         var1 = ContainerHelpers.binarySearch(var0, var1, var2);
         return var1;
      } catch (ArrayIndexOutOfBoundsException var3) {
         throw new ConcurrentModificationException();
      }
   }

   private static void freeArrays(int[] var0, Object[] var1, int var2) {
      Throwable var75;
      Throwable var10000;
      boolean var10001;
      if (var0.length == 8) {
         synchronized(ArrayMap.class){}

         label811: {
            label838: {
               try {
                  if (mTwiceBaseCacheSize >= 10) {
                     break label838;
                  }

                  var1[0] = mTwiceBaseCache;
               } catch (Throwable var71) {
                  var10000 = var71;
                  var10001 = false;
                  break label811;
               }

               var1[1] = var0;
               var2 = (var2 << 1) - 1;

               while(true) {
                  if (var2 < 2) {
                     try {
                        mTwiceBaseCache = var1;
                        ++mTwiceBaseCacheSize;
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
            } catch (Throwable var68) {
               var10000 = var68;
               var10001 = false;
               continue;
            }
         }
      } else if (var0.length == 4) {
         synchronized(ArrayMap.class){}

         label829: {
            label839: {
               try {
                  if (mBaseCacheSize >= 10) {
                     break label839;
                  }

                  var1[0] = mBaseCache;
               } catch (Throwable var74) {
                  var10000 = var74;
                  var10001 = false;
                  break label829;
               }

               var1[1] = var0;
               var2 = (var2 << 1) - 1;

               while(true) {
                  if (var2 < 2) {
                     try {
                        mBaseCache = var1;
                        ++mBaseCacheSize;
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
            } catch (Throwable var67) {
               var10000 = var67;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public void clear() {
      if (this.mSize > 0) {
         int[] var1 = this.mHashes;
         Object[] var2 = this.mArray;
         int var3 = this.mSize;
         this.mHashes = ContainerHelpers.EMPTY_INTS;
         this.mArray = ContainerHelpers.EMPTY_OBJECTS;
         this.mSize = 0;
         freeArrays(var1, var2, var3);
      }

      if (this.mSize > 0) {
         throw new ConcurrentModificationException();
      }
   }

   public boolean containsKey(Object var1) {
      boolean var2;
      if (this.indexOfKey(var1) >= 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean containsValue(Object var1) {
      boolean var2;
      if (this.indexOfValue(var1) >= 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public void ensureCapacity(int var1) {
      int var2 = this.mSize;
      if (this.mHashes.length < var1) {
         int[] var3 = this.mHashes;
         Object[] var4 = this.mArray;
         this.allocArrays(var1);
         if (this.mSize > 0) {
            System.arraycopy(var3, 0, this.mHashes, 0, var2);
            System.arraycopy(var4, 0, this.mArray, 0, var2 << 1);
         }

         freeArrays(var3, var4, var2);
      }

      if (this.mSize != var2) {
         throw new ConcurrentModificationException();
      }
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else {
         boolean var10001;
         int var3;
         Object var5;
         boolean var6;
         if (var1 instanceof SimpleArrayMap) {
            SimpleArrayMap var19 = (SimpleArrayMap)var1;
            if (this.size() != var19.size()) {
               return false;
            } else {
               var3 = 0;

               while(true) {
                  label171: {
                     Object var20;
                     try {
                        if (var3 >= this.mSize) {
                           return true;
                        }

                        var20 = this.keyAt(var3);
                        var5 = this.valueAt(var3);
                        var1 = var19.get(var20);
                     } catch (NullPointerException var11) {
                        var10001 = false;
                        return false;
                     } catch (ClassCastException var12) {
                        var10001 = false;
                        return false;
                     }

                     if (var5 == null) {
                        if (var1 == null) {
                           try {
                              if (var19.containsKey(var20)) {
                                 break label171;
                              }
                           } catch (NullPointerException var7) {
                              var10001 = false;
                              return false;
                           } catch (ClassCastException var8) {
                              var10001 = false;
                              return false;
                           }
                        }

                        return false;
                     } else {
                        try {
                           var6 = var5.equals(var1);
                        } catch (NullPointerException var9) {
                           var10001 = false;
                           return false;
                        } catch (ClassCastException var10) {
                           var10001 = false;
                           return false;
                        }
                     }

                     if (!var6) {
                        return false;
                     }
                  }

                  ++var3;
               }
            }
         } else if (!(var1 instanceof Map)) {
            return false;
         } else {
            Map var4 = (Map)var1;
            if (this.size() != var4.size()) {
               return false;
            } else {
               var3 = 0;

               while(true) {
                  Object var2;
                  try {
                     if (var3 >= this.mSize) {
                        return true;
                     }

                     var5 = this.keyAt(var3);
                     var1 = this.valueAt(var3);
                     var2 = var4.get(var5);
                  } catch (NullPointerException var17) {
                     var10001 = false;
                     return false;
                  } catch (ClassCastException var18) {
                     var10001 = false;
                     return false;
                  }

                  if (var1 == null) {
                     label110: {
                        if (var2 == null) {
                           try {
                              if (var4.containsKey(var5)) {
                                 break label110;
                              }
                           } catch (NullPointerException var13) {
                              var10001 = false;
                              return false;
                           } catch (ClassCastException var14) {
                              var10001 = false;
                              return false;
                           }
                        }

                        return false;
                     }
                  } else {
                     try {
                        var6 = var1.equals(var2);
                     } catch (NullPointerException var15) {
                        var10001 = false;
                        return false;
                     } catch (ClassCastException var16) {
                        var10001 = false;
                        return false;
                     }

                     if (!var6) {
                        return false;
                     }
                  }

                  ++var3;
               }
            }
         }
      }
   }

   public Object get(Object var1) {
      int var2 = this.indexOfKey(var1);
      if (var2 >= 0) {
         var1 = this.mArray[(var2 << 1) + 1];
      } else {
         var1 = null;
      }

      return var1;
   }

   public int hashCode() {
      int[] var1 = this.mHashes;
      Object[] var2 = this.mArray;
      int var3 = this.mSize;
      int var4 = 1;
      int var5 = 0;

      int var6;
      for(var6 = var5; var5 < var3; var4 += 2) {
         Object var7 = var2[var4];
         int var8 = var1[var5];
         int var9;
         if (var7 == null) {
            var9 = 0;
         } else {
            var9 = var7.hashCode();
         }

         var6 += var9 ^ var8;
         ++var5;
      }

      return var6;
   }

   int indexOf(Object var1, int var2) {
      int var3 = this.mSize;
      if (var3 == 0) {
         return -1;
      } else {
         int var4 = binarySearchHashes(this.mHashes, var3, var2);
         if (var4 < 0) {
            return var4;
         } else if (var1.equals(this.mArray[var4 << 1])) {
            return var4;
         } else {
            int var5;
            for(var5 = var4 + 1; var5 < var3 && this.mHashes[var5] == var2; ++var5) {
               if (var1.equals(this.mArray[var5 << 1])) {
                  return var5;
               }
            }

            --var4;

            while(var4 >= 0 && this.mHashes[var4] == var2) {
               if (var1.equals(this.mArray[var4 << 1])) {
                  return var4;
               }

               --var4;
            }

            return ~var5;
         }
      }
   }

   public int indexOfKey(Object var1) {
      int var2;
      if (var1 == null) {
         var2 = this.indexOfNull();
      } else {
         var2 = this.indexOf(var1, var1.hashCode());
      }

      return var2;
   }

   int indexOfNull() {
      int var1 = this.mSize;
      if (var1 == 0) {
         return -1;
      } else {
         int var2 = binarySearchHashes(this.mHashes, var1, 0);
         if (var2 < 0) {
            return var2;
         } else if (this.mArray[var2 << 1] == null) {
            return var2;
         } else {
            int var3;
            for(var3 = var2 + 1; var3 < var1 && this.mHashes[var3] == 0; ++var3) {
               if (this.mArray[var3 << 1] == null) {
                  return var3;
               }
            }

            for(var1 = var2 - 1; var1 >= 0 && this.mHashes[var1] == 0; --var1) {
               if (this.mArray[var1 << 1] == null) {
                  return var1;
               }
            }

            return ~var3;
         }
      }
   }

   int indexOfValue(Object var1) {
      int var2 = this.mSize * 2;
      Object[] var3 = this.mArray;
      int var4;
      if (var1 == null) {
         for(var4 = 1; var4 < var2; var4 += 2) {
            if (var3[var4] == null) {
               return var4 >> 1;
            }
         }
      } else {
         for(var4 = 1; var4 < var2; var4 += 2) {
            if (var1.equals(var3[var4])) {
               return var4 >> 1;
            }
         }
      }

      return -1;
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

   public Object keyAt(int var1) {
      return this.mArray[var1 << 1];
   }

   public Object put(Object var1, Object var2) {
      int var3 = this.mSize;
      int var4;
      int var5;
      if (var1 == null) {
         var4 = this.indexOfNull();
         var5 = 0;
      } else {
         var5 = var1.hashCode();
         var4 = this.indexOf(var1, var5);
      }

      if (var4 >= 0) {
         var4 = (var4 << 1) + 1;
         var1 = this.mArray[var4];
         this.mArray[var4] = var2;
         return var1;
      } else {
         int var6 = ~var4;
         int[] var7;
         if (var3 >= this.mHashes.length) {
            var4 = 4;
            if (var3 >= 8) {
               var4 = (var3 >> 1) + var3;
            } else if (var3 >= 4) {
               var4 = 8;
            }

            var7 = this.mHashes;
            Object[] var8 = this.mArray;
            this.allocArrays(var4);
            if (var3 != this.mSize) {
               throw new ConcurrentModificationException();
            }

            if (this.mHashes.length > 0) {
               System.arraycopy(var7, 0, this.mHashes, 0, var7.length);
               System.arraycopy(var8, 0, this.mArray, 0, var8.length);
            }

            freeArrays(var7, var8, var3);
         }

         if (var6 < var3) {
            var7 = this.mHashes;
            int[] var10 = this.mHashes;
            var4 = var6 + 1;
            System.arraycopy(var7, var6, var10, var4, var3 - var6);
            System.arraycopy(this.mArray, var6 << 1, this.mArray, var4 << 1, this.mSize - var6 << 1);
         }

         if (var3 == this.mSize && var6 < this.mHashes.length) {
            this.mHashes[var6] = var5;
            Object[] var9 = this.mArray;
            var4 = var6 << 1;
            var9[var4] = var1;
            this.mArray[var4 + 1] = var2;
            ++this.mSize;
            return null;
         } else {
            throw new ConcurrentModificationException();
         }
      }
   }

   public void putAll(SimpleArrayMap var1) {
      int var2 = var1.mSize;
      this.ensureCapacity(this.mSize + var2);
      int var3 = this.mSize;
      int var4 = 0;
      if (var3 == 0) {
         if (var2 > 0) {
            System.arraycopy(var1.mHashes, 0, this.mHashes, 0, var2);
            System.arraycopy(var1.mArray, 0, this.mArray, 0, var2 << 1);
            this.mSize = var2;
         }
      } else {
         while(var4 < var2) {
            this.put(var1.keyAt(var4), var1.valueAt(var4));
            ++var4;
         }
      }

   }

   public Object remove(Object var1) {
      int var2 = this.indexOfKey(var1);
      return var2 >= 0 ? this.removeAt(var2) : null;
   }

   public Object removeAt(int var1) {
      Object[] var2 = this.mArray;
      int var3 = var1 << 1;
      Object var11 = var2[var3 + 1];
      int var4 = this.mSize;
      byte var5 = 0;
      if (var4 <= 1) {
         freeArrays(this.mHashes, this.mArray, var4);
         this.mHashes = ContainerHelpers.EMPTY_INTS;
         this.mArray = ContainerHelpers.EMPTY_OBJECTS;
         var1 = var5;
      } else {
         int var6 = var4 - 1;
         int[] var7 = this.mHashes;
         int var12 = 8;
         int[] var8;
         int var10;
         if (var7.length > 8 && this.mSize < this.mHashes.length / 3) {
            if (var4 > 8) {
               var12 = var4 + (var4 >> 1);
            }

            var8 = this.mHashes;
            Object[] var9 = this.mArray;
            this.allocArrays(var12);
            if (var4 != this.mSize) {
               throw new ConcurrentModificationException();
            }

            if (var1 > 0) {
               System.arraycopy(var8, 0, this.mHashes, 0, var1);
               System.arraycopy(var9, 0, this.mArray, 0, var3);
            }

            if (var1 < var6) {
               var12 = var1 + 1;
               var7 = this.mHashes;
               var10 = var6 - var1;
               System.arraycopy(var8, var12, var7, var1, var10);
               System.arraycopy(var9, var12 << 1, this.mArray, var3, var10 << 1);
            }
         } else {
            if (var1 < var6) {
               var8 = this.mHashes;
               var12 = var1 + 1;
               var7 = this.mHashes;
               var10 = var6 - var1;
               System.arraycopy(var8, var12, var7, var1, var10);
               System.arraycopy(this.mArray, var12 << 1, this.mArray, var3, var10 << 1);
            }

            Object[] var13 = this.mArray;
            var1 = var6 << 1;
            var13[var1] = null;
            this.mArray[var1 + 1] = null;
         }

         var1 = var6;
      }

      if (var4 != this.mSize) {
         throw new ConcurrentModificationException();
      } else {
         this.mSize = var1;
         return var11;
      }
   }

   public Object setValueAt(int var1, Object var2) {
      var1 = (var1 << 1) + 1;
      Object var3 = this.mArray[var1];
      this.mArray[var1] = var2;
      return var3;
   }

   public int size() {
      return this.mSize;
   }

   public String toString() {
      if (this.isEmpty()) {
         return "{}";
      } else {
         StringBuilder var1 = new StringBuilder(this.mSize * 28);
         var1.append('{');

         for(int var2 = 0; var2 < this.mSize; ++var2) {
            if (var2 > 0) {
               var1.append(", ");
            }

            Object var3 = this.keyAt(var2);
            if (var3 != this) {
               var1.append(var3);
            } else {
               var1.append("(this Map)");
            }

            var1.append('=');
            var3 = this.valueAt(var2);
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
      return this.mArray[(var1 << 1) + 1];
   }
}
