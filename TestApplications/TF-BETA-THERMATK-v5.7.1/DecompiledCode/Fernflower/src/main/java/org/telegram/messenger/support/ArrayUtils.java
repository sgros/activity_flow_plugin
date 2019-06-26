package org.telegram.messenger.support;

import java.lang.reflect.Array;

public class ArrayUtils {
   private static final int CACHE_SIZE = 73;
   private static Object[] EMPTY = new Object[0];
   private static Object[] sCache = new Object[73];

   private ArrayUtils() {
   }

   public static Object[] appendElement(Class var0, Object[] var1, Object var2) {
      int var3 = 0;
      Object[] var4;
      if (var1 != null) {
         var3 = var1.length;
         var4 = (Object[])Array.newInstance(var0, var3 + 1);
         System.arraycopy(var1, 0, var4, 0, var3);
      } else {
         var4 = (Object[])Array.newInstance(var0, 1);
      }

      var4[var3] = var2;
      return var4;
   }

   public static int[] appendInt(int[] var0, int var1) {
      if (var0 == null) {
         return new int[]{var1};
      } else {
         int var2 = var0.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            if (var0[var3] == var1) {
               return var0;
            }
         }

         int[] var4 = new int[var2 + 1];
         System.arraycopy(var0, 0, var4, 0, var2);
         var4[var2] = var1;
         return var4;
      }
   }

   public static boolean contains(int[] var0, int var1) {
      int var2 = var0.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         if (var0[var3] == var1) {
            return true;
         }
      }

      return false;
   }

   public static boolean contains(Object[] var0, Object var1) {
      int var2 = var0.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Object var4 = var0[var3];
         if (var4 == null) {
            if (var1 == null) {
               return true;
            }
         } else if (var1 != null && var4.equals(var1)) {
            return true;
         }
      }

      return false;
   }

   public static Object[] emptyArray(Class var0) {
      if (var0 == Object.class) {
         return EMPTY;
      } else {
         int var1 = (System.identityHashCode(var0) / 8 & Integer.MAX_VALUE) % 73;
         Object var2 = sCache[var1];
         Object var3;
         if (var2 != null) {
            var3 = var2;
            if (var2.getClass().getComponentType() == var0) {
               return (Object[])var3;
            }
         }

         var3 = Array.newInstance(var0, 0);
         sCache[var1] = var3;
         return (Object[])var3;
      }
   }

   public static boolean equals(byte[] var0, byte[] var1, int var2) {
      if (var0 == var1) {
         return true;
      } else if (var0 != null && var1 != null && var0.length >= var2 && var1.length >= var2) {
         for(int var3 = 0; var3 < var2; ++var3) {
            if (var0[var3] != var1[var3]) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static int idealBooleanArraySize(int var0) {
      return idealByteArraySize(var0);
   }

   public static int idealByteArraySize(int var0) {
      for(int var1 = 4; var1 < 32; ++var1) {
         int var2 = (1 << var1) - 12;
         if (var0 <= var2) {
            return var2;
         }
      }

      return var0;
   }

   public static int idealCharArraySize(int var0) {
      return idealByteArraySize(var0 * 2) / 2;
   }

   public static int idealFloatArraySize(int var0) {
      return idealByteArraySize(var0 * 4) / 4;
   }

   public static int idealIntArraySize(int var0) {
      return idealByteArraySize(var0 * 4) / 4;
   }

   public static int idealLongArraySize(int var0) {
      return idealByteArraySize(var0 * 8) / 8;
   }

   public static int idealObjectArraySize(int var0) {
      return idealByteArraySize(var0 * 4) / 4;
   }

   public static int idealShortArraySize(int var0) {
      return idealByteArraySize(var0 * 2) / 2;
   }

   public static Object[] removeElement(Class var0, Object[] var1, Object var2) {
      if (var1 != null) {
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            if (var1[var4] == var2) {
               if (var3 == 1) {
                  return null;
               }

               Object[] var5 = (Object[])Array.newInstance(var0, var3 - 1);
               System.arraycopy(var1, 0, var5, 0, var4);
               System.arraycopy(var1, var4 + 1, var5, var4, var3 - var4 - 1);
               return var5;
            }
         }
      }

      return var1;
   }

   public static int[] removeInt(int[] var0, int var1) {
      if (var0 == null) {
         return null;
      } else {
         int var2 = var0.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            if (var0[var3] == var1) {
               var1 = var2 - 1;
               int[] var4 = new int[var1];
               if (var3 > 0) {
                  System.arraycopy(var0, 0, var4, 0, var3);
               }

               if (var3 < var1) {
                  System.arraycopy(var0, var3 + 1, var4, var3, var2 - var3 - 1);
               }

               return var4;
            }
         }

         return var0;
      }
   }

   public static long total(long[] var0) {
      int var1 = var0.length;
      long var2 = 0L;

      for(int var4 = 0; var4 < var1; ++var4) {
         var2 += var0[var4];
      }

      return var2;
   }
}
