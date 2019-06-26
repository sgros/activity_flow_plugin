package android.support.v4.util;

import java.util.Map;

public class SimpleArrayMap {
   private static final int BASE_SIZE = 4;
   private static final int CACHE_SIZE = 10;
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
      label801: {
         Object[] var2;
         if (var1 == 8) {
            label781: {
               synchronized(ArrayMap.class){}

               label761: {
                  label782: {
                     try {
                        if (mTwiceBaseCache == null) {
                           break label782;
                        }

                        var2 = mTwiceBaseCache;
                        this.mArray = var2;
                        mTwiceBaseCache = (Object[])var2[0];
                        this.mHashes = (int[])var2[1];
                     } catch (Throwable var73) {
                        var10000 = var73;
                        var10001 = false;
                        break label761;
                     }

                     var2[1] = null;
                     var2[0] = null;

                     try {
                        --mTwiceBaseCacheSize;
                        return;
                     } catch (Throwable var69) {
                        var10000 = var69;
                        var10001 = false;
                        break label761;
                     }
                  }

                  label754:
                  try {
                     break label781;
                  } catch (Throwable var72) {
                     var10000 = var72;
                     var10001 = false;
                     break label754;
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
         } else if (var1 == 4) {
            label784: {
               synchronized(ArrayMap.class){}

               label770: {
                  try {
                     if (mBaseCache != null) {
                        var2 = mBaseCache;
                        this.mArray = var2;
                        mBaseCache = (Object[])var2[0];
                        this.mHashes = (int[])var2[1];
                        break label770;
                     }
                  } catch (Throwable var74) {
                     var10000 = var74;
                     var10001 = false;
                     break label801;
                  }

                  try {
                     break label784;
                  } catch (Throwable var71) {
                     var10000 = var71;
                     var10001 = false;
                     break label801;
                  }
               }

               var2[1] = null;
               var2[0] = null;

               try {
                  --mBaseCacheSize;
                  return;
               } catch (Throwable var70) {
                  var10000 = var70;
                  var10001 = false;
                  break label801;
               }
            }
         }

         this.mHashes = new int[var1];
         this.mArray = new Object[var1 << 1];
         return;
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
         synchronized(ArrayMap.class){}

         label810: {
            label837: {
               try {
                  if (mTwiceBaseCacheSize >= 10) {
                     break label837;
                  }

                  var1[0] = mTwiceBaseCache;
               } catch (Throwable var71) {
                  var10000 = var71;
                  var10001 = false;
                  break label810;
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
         synchronized(ArrayMap.class){}

         label828: {
            label838: {
               try {
                  if (mBaseCacheSize >= 10) {
                     break label838;
                  }

                  var1[0] = mBaseCache;
               } catch (Throwable var74) {
                  var10000 = var74;
                  var10001 = false;
                  break label828;
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

   public void clear() {
      if (this.mSize != 0) {
         freeArrays(this.mHashes, this.mArray, this.mSize);
         this.mHashes = ContainerHelpers.EMPTY_INTS;
         this.mArray = ContainerHelpers.EMPTY_OBJECTS;
         this.mSize = 0;
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
      if (this.mHashes.length < var1) {
         int[] var2 = this.mHashes;
         Object[] var3 = this.mArray;
         this.allocArrays(var1);
         if (this.mSize > 0) {
            System.arraycopy(var2, 0, this.mHashes, 0, this.mSize);
            System.arraycopy(var3, 0, this.mArray, 0, this.mSize << 1);
         }

         freeArrays(var2, var3, this.mSize);
      }

   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      boolean var3;
      if (this == var1) {
         var3 = var2;
      } else {
         boolean var10001;
         int var5;
         Object var6;
         Object var7;
         if (var1 instanceof SimpleArrayMap) {
            SimpleArrayMap var4 = (SimpleArrayMap)var1;
            if (this.size() != var4.size()) {
               var3 = false;
            } else {
               var5 = 0;

               while(true) {
                  var3 = var2;

                  label116: {
                     label152: {
                        label153: {
                           label113: {
                              try {
                                 if (var5 >= this.mSize) {
                                    return var3;
                                 }

                                 var6 = this.keyAt(var5);
                                 var1 = this.valueAt(var5);
                                 var7 = var4.get(var6);
                              } catch (NullPointerException var18) {
                                 var10001 = false;
                                 break label153;
                              } catch (ClassCastException var19) {
                                 var10001 = false;
                                 break label113;
                              }

                              if (var1 == null) {
                                 if (var7 != null) {
                                    break;
                                 }

                                 try {
                                    if (!var4.containsKey(var6)) {
                                       break;
                                    }
                                    break label116;
                                 } catch (NullPointerException var14) {
                                    var10001 = false;
                                    break label153;
                                 } catch (ClassCastException var15) {
                                    var10001 = false;
                                 }
                              } else {
                                 try {
                                    var3 = var1.equals(var7);
                                    break label152;
                                 } catch (NullPointerException var16) {
                                    var10001 = false;
                                    break label153;
                                 } catch (ClassCastException var17) {
                                    var10001 = false;
                                 }
                              }
                           }

                           var3 = false;
                           return var3;
                        }

                        var3 = false;
                        return var3;
                     }

                     if (!var3) {
                        var3 = false;
                        return var3;
                     }
                  }

                  ++var5;
               }

               var3 = false;
            }
         } else if (var1 instanceof Map) {
            Map var20 = (Map)var1;
            if (this.size() != var20.size()) {
               var3 = false;
            } else {
               var5 = 0;

               while(true) {
                  var3 = var2;

                  label154: {
                     label155: {
                        label85: {
                           try {
                              if (var5 >= this.mSize) {
                                 return var3;
                              }

                              var6 = this.keyAt(var5);
                              var7 = this.valueAt(var5);
                              var1 = var20.get(var6);
                           } catch (NullPointerException var12) {
                              var10001 = false;
                              break label155;
                           } catch (ClassCastException var13) {
                              var10001 = false;
                              break label85;
                           }

                           if (var7 == null) {
                              if (var1 != null) {
                                 break;
                              }

                              try {
                                 if (!var20.containsKey(var6)) {
                                    break;
                                 }
                                 break label154;
                              } catch (NullPointerException var8) {
                                 var10001 = false;
                                 break label155;
                              } catch (ClassCastException var9) {
                                 var10001 = false;
                              }
                           } else {
                              label81: {
                                 try {
                                    var3 = var7.equals(var1);
                                 } catch (NullPointerException var10) {
                                    var10001 = false;
                                    break label155;
                                 } catch (ClassCastException var11) {
                                    var10001 = false;
                                    break label81;
                                 }

                                 if (!var3) {
                                    var3 = false;
                                    return var3;
                                 }
                                 break label154;
                              }
                           }
                        }

                        var3 = false;
                        return var3;
                     }

                     var3 = false;
                     return var3;
                  }

                  ++var5;
               }

               var3 = false;
            }
         } else {
            var3 = false;
         }
      }

      return var3;
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
      int var3 = 0;
      int var4 = 0;
      int var5 = 1;

      for(int var6 = this.mSize; var4 < var6; var5 += 2) {
         Object var7 = var2[var5];
         int var8 = var1[var4];
         int var9;
         if (var7 == null) {
            var9 = 0;
         } else {
            var9 = var7.hashCode();
         }

         var3 += var9 ^ var8;
         ++var4;
      }

      return var3;
   }

   int indexOf(Object var1, int var2) {
      int var3 = this.mSize;
      int var4;
      if (var3 == 0) {
         var4 = -1;
      } else {
         int var5 = ContainerHelpers.binarySearch(this.mHashes, var3, var2);
         var4 = var5;
         if (var5 >= 0) {
            var4 = var5;
            if (!var1.equals(this.mArray[var5 << 1])) {
               for(var4 = var5 + 1; var4 < var3 && this.mHashes[var4] == var2; ++var4) {
                  if (var1.equals(this.mArray[var4 << 1])) {
                     return var4;
                  }
               }

               --var5;

               while(var5 >= 0 && this.mHashes[var5] == var2) {
                  if (var1.equals(this.mArray[var5 << 1])) {
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
      int var2;
      if (var1 == 0) {
         var2 = -1;
      } else {
         int var3 = ContainerHelpers.binarySearch(this.mHashes, var1, 0);
         var2 = var3;
         if (var3 >= 0) {
            var2 = var3;
            if (this.mArray[var3 << 1] != null) {
               for(var2 = var3 + 1; var2 < var1 && this.mHashes[var2] == 0; ++var2) {
                  if (this.mArray[var2 << 1] == null) {
                     return var2;
                  }
               }

               --var3;

               while(var3 >= 0 && this.mHashes[var3] == 0) {
                  if (this.mArray[var3 << 1] == null) {
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

   int indexOfValue(Object var1) {
      int var2 = this.mSize * 2;
      Object[] var3 = this.mArray;
      int var4;
      if (var1 == null) {
         for(var4 = 1; var4 < var2; var4 += 2) {
            if (var3[var4] == null) {
               var4 >>= 1;
               return var4;
            }
         }
      } else {
         for(var4 = 1; var4 < var2; var4 += 2) {
            if (var1.equals(var3[var4])) {
               var4 >>= 1;
               return var4;
            }
         }
      }

      var4 = -1;
      return var4;
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
      byte var3 = 8;
      int var4;
      int var5;
      if (var1 == null) {
         var4 = 0;
         var5 = this.indexOfNull();
      } else {
         var4 = var1.hashCode();
         var5 = this.indexOf(var1, var4);
      }

      if (var5 >= 0) {
         var5 = (var5 << 1) + 1;
         var1 = this.mArray[var5];
         this.mArray[var5] = var2;
      } else {
         int var6 = ~var5;
         if (this.mSize >= this.mHashes.length) {
            if (this.mSize >= 8) {
               var5 = this.mSize + (this.mSize >> 1);
            } else {
               var5 = var3;
               if (this.mSize < 4) {
                  var5 = 4;
               }
            }

            int[] var7 = this.mHashes;
            Object[] var8 = this.mArray;
            this.allocArrays(var5);
            if (this.mHashes.length > 0) {
               System.arraycopy(var7, 0, this.mHashes, 0, var7.length);
               System.arraycopy(var8, 0, this.mArray, 0, var8.length);
            }

            freeArrays(var7, var8, this.mSize);
         }

         if (var6 < this.mSize) {
            System.arraycopy(this.mHashes, var6, this.mHashes, var6 + 1, this.mSize - var6);
            System.arraycopy(this.mArray, var6 << 1, this.mArray, var6 + 1 << 1, this.mSize - var6 << 1);
         }

         this.mHashes[var6] = var4;
         this.mArray[var6 << 1] = var1;
         this.mArray[(var6 << 1) + 1] = var2;
         ++this.mSize;
         var1 = null;
      }

      return var1;
   }

   public void putAll(SimpleArrayMap var1) {
      int var2 = var1.mSize;
      this.ensureCapacity(this.mSize + var2);
      if (this.mSize == 0) {
         if (var2 > 0) {
            System.arraycopy(var1.mHashes, 0, this.mHashes, 0, var2);
            System.arraycopy(var1.mArray, 0, this.mArray, 0, var2 << 1);
            this.mSize = var2;
         }
      } else {
         for(int var3 = 0; var3 < var2; ++var3) {
            this.put(var1.keyAt(var3), var1.valueAt(var3));
         }
      }

   }

   public Object remove(Object var1) {
      int var2 = this.indexOfKey(var1);
      if (var2 >= 0) {
         var1 = this.removeAt(var2);
      } else {
         var1 = null;
      }

      return var1;
   }

   public Object removeAt(int var1) {
      int var2 = 8;
      Object var3 = this.mArray[(var1 << 1) + 1];
      if (this.mSize <= 1) {
         freeArrays(this.mHashes, this.mArray, this.mSize);
         this.mHashes = ContainerHelpers.EMPTY_INTS;
         this.mArray = ContainerHelpers.EMPTY_OBJECTS;
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
            System.arraycopy(var5, 0, this.mArray, 0, var1 << 1);
         }

         if (var1 < this.mSize) {
            System.arraycopy(var4, var1 + 1, this.mHashes, var1, this.mSize - var1);
            System.arraycopy(var5, var1 + 1 << 1, this.mArray, var1 << 1, this.mSize - var1 << 1);
         }
      } else {
         --this.mSize;
         if (var1 < this.mSize) {
            System.arraycopy(this.mHashes, var1 + 1, this.mHashes, var1, this.mSize - var1);
            System.arraycopy(this.mArray, var1 + 1 << 1, this.mArray, var1 << 1, this.mSize - var1 << 1);
         }

         this.mArray[this.mSize << 1] = null;
         this.mArray[(this.mSize << 1) + 1] = null;
      }

      return var3;
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
      String var1;
      if (this.isEmpty()) {
         var1 = "{}";
      } else {
         StringBuilder var4 = new StringBuilder(this.mSize * 28);
         var4.append('{');

         for(int var2 = 0; var2 < this.mSize; ++var2) {
            if (var2 > 0) {
               var4.append(", ");
            }

            Object var3 = this.keyAt(var2);
            if (var3 != this) {
               var4.append(var3);
            } else {
               var4.append("(this Map)");
            }

            var4.append('=');
            var3 = this.valueAt(var2);
            if (var3 != this) {
               var4.append(var3);
            } else {
               var4.append("(this Map)");
            }
         }

         var4.append('}');
         var1 = var4.toString();
      }

      return var1;
   }

   public Object valueAt(int var1) {
      return this.mArray[(var1 << 1) + 1];
   }
}
