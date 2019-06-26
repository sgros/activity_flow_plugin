package androidx.collection;

import java.util.ConcurrentModificationException;
import java.util.Map;

public class SimpleArrayMap {
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
      int var1 = this.mSize;
      if (var1 > 0) {
         int[] var2 = this.mHashes;
         Object[] var3 = this.mArray;
         this.mHashes = ContainerHelpers.EMPTY_INTS;
         this.mArray = ContainerHelpers.EMPTY_OBJECTS;
         this.mSize = 0;
         freeArrays(var2, var3, var1);
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
      int[] var3 = this.mHashes;
      if (var3.length < var1) {
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
            SimpleArrayMap var13 = (SimpleArrayMap)var1;
            if (this.size() != var13.size()) {
               return false;
            } else {
               var3 = 0;

               while(true) {
                  Object var14;
                  try {
                     if (var3 >= this.mSize) {
                        return true;
                     }

                     var14 = this.keyAt(var3);
                     var5 = this.valueAt(var3);
                     var1 = var13.get(var14);
                  } catch (ClassCastException | NullPointerException var9) {
                     var10001 = false;
                     break;
                  }

                  if (var5 == null) {
                     label67: {
                        if (var1 == null) {
                           try {
                              if (var13.containsKey(var14)) {
                                 break label67;
                              }
                           } catch (ClassCastException | NullPointerException var8) {
                              var10001 = false;
                              break;
                           }
                        }

                        return false;
                     }
                  } else {
                     try {
                        var6 = var5.equals(var1);
                     } catch (ClassCastException | NullPointerException var7) {
                        var10001 = false;
                        break;
                     }

                     if (!var6) {
                        return false;
                     }
                  }

                  ++var3;
               }

               return false;
            }
         } else {
            if (var1 instanceof Map) {
               Map var4 = (Map)var1;
               if (this.size() != var4.size()) {
                  return false;
               }

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
                  } catch (ClassCastException | NullPointerException var12) {
                     var10001 = false;
                     break;
                  }

                  if (var1 == null) {
                     label91: {
                        if (var2 == null) {
                           try {
                              if (var4.containsKey(var5)) {
                                 break label91;
                              }
                           } catch (ClassCastException | NullPointerException var11) {
                              var10001 = false;
                              break;
                           }
                        }

                        return false;
                     }
                  } else {
                     try {
                        var6 = var1.equals(var2);
                     } catch (ClassCastException | NullPointerException var10) {
                        var10001 = false;
                        break;
                     }

                     if (!var6) {
                        return false;
                     }
                  }

                  ++var3;
               }
            }

            return false;
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
      int var4 = 0;
      int var5 = 0;

      for(int var6 = 1; var4 < var3; var6 += 2) {
         Object var7 = var2[var6];
         int var8 = var1[var4];
         int var9;
         if (var7 == null) {
            var9 = 0;
         } else {
            var9 = var7.hashCode();
         }

         var5 += var9 ^ var8;
         ++var4;
      }

      return var5;
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

            for(var3 = var4 - 1; var3 >= 0 && this.mHashes[var3] == var2; --var3) {
               if (var1.equals(this.mArray[var3 << 1])) {
                  return var3;
               }
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

            --var2;

            while(var2 >= 0 && this.mHashes[var2] == 0) {
               if (this.mArray[var2 << 1] == null) {
                  return var2;
               }

               --var2;
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

      Object[] var10;
      if (var4 >= 0) {
         var4 = (var4 << 1) + 1;
         var10 = this.mArray;
         var1 = var10[var4];
         var10[var4] = var2;
         return var1;
      } else {
         int var7 = ~var4;
         int[] var6;
         if (var3 >= this.mHashes.length) {
            var4 = 4;
            if (var3 >= 8) {
               var4 = (var3 >> 1) + var3;
            } else if (var3 >= 4) {
               var4 = 8;
            }

            int[] var8 = this.mHashes;
            Object[] var9 = this.mArray;
            this.allocArrays(var4);
            if (var3 != this.mSize) {
               throw new ConcurrentModificationException();
            }

            var6 = this.mHashes;
            if (var6.length > 0) {
               System.arraycopy(var8, 0, var6, 0, var8.length);
               System.arraycopy(var9, 0, this.mArray, 0, var9.length);
            }

            freeArrays(var8, var9, var3);
         }

         if (var7 < var3) {
            var6 = this.mHashes;
            var4 = var7 + 1;
            System.arraycopy(var6, var7, var6, var4, var3 - var7);
            var10 = this.mArray;
            System.arraycopy(var10, var7 << 1, var10, var4 << 1, this.mSize - var7 << 1);
         }

         var4 = this.mSize;
         if (var3 == var4) {
            var6 = this.mHashes;
            if (var7 < var6.length) {
               var6[var7] = var5;
               var10 = this.mArray;
               var5 = var7 << 1;
               var10[var5] = var1;
               var10[var5 + 1] = var2;
               this.mSize = var4 + 1;
               return null;
            }
         }

         throw new ConcurrentModificationException();
      }
   }

   public Object remove(Object var1) {
      int var2 = this.indexOfKey(var1);
      return var2 >= 0 ? this.removeAt(var2) : null;
   }

   public Object removeAt(int var1) {
      Object[] var2 = this.mArray;
      int var3 = var1 << 1;
      Object var4 = var2[var3 + 1];
      int var5 = this.mSize;
      int var6;
      if (var5 <= 1) {
         freeArrays(this.mHashes, var2, var5);
         this.mHashes = ContainerHelpers.EMPTY_INTS;
         this.mArray = ContainerHelpers.EMPTY_OBJECTS;
         var6 = 0;
      } else {
         int var7 = var5 - 1;
         int[] var11 = this.mHashes;
         int var8 = var11.length;
         var6 = 8;
         if (var8 > 8 && var5 < var11.length / 3) {
            if (var5 > 8) {
               var6 = var5 + (var5 >> 1);
            }

            var11 = this.mHashes;
            Object[] var9 = this.mArray;
            this.allocArrays(var6);
            if (var5 != this.mSize) {
               throw new ConcurrentModificationException();
            }

            if (var1 > 0) {
               System.arraycopy(var11, 0, this.mHashes, 0, var1);
               System.arraycopy(var9, 0, this.mArray, 0, var3);
            }

            var6 = var7;
            if (var1 < var7) {
               var6 = var1 + 1;
               int[] var10 = this.mHashes;
               var8 = var7 - var1;
               System.arraycopy(var11, var6, var10, var1, var8);
               System.arraycopy(var9, var6 << 1, this.mArray, var3, var8 << 1);
               var6 = var7;
            }
         } else {
            if (var1 < var7) {
               var11 = this.mHashes;
               var6 = var1 + 1;
               var8 = var7 - var1;
               System.arraycopy(var11, var6, var11, var1, var8);
               var2 = this.mArray;
               System.arraycopy(var2, var6 << 1, var2, var3, var8 << 1);
            }

            var2 = this.mArray;
            var1 = var7 << 1;
            var2[var1] = null;
            var2[var1 + 1] = null;
            var6 = var7;
         }
      }

      if (var5 == this.mSize) {
         this.mSize = var6;
         return var4;
      } else {
         throw new ConcurrentModificationException();
      }
   }

   public Object setValueAt(int var1, Object var2) {
      var1 = (var1 << 1) + 1;
      Object[] var3 = this.mArray;
      Object var4 = var3[var1];
      var3[var1] = var2;
      return var4;
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
