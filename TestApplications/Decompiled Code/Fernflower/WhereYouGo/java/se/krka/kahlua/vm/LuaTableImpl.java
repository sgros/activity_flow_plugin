package se.krka.kahlua.vm;

import java.lang.ref.WeakReference;
import se.krka.kahlua.stdlib.BaseLib;

public final class LuaTableImpl implements LuaTable {
   private static final int[] log_2 = new int[]{0, 1, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8};
   private int freeIndex = 1;
   private Object keyIndexCacheKey;
   private int keyIndexCacheValue = -1;
   private Object[] keys = new Object[1];
   private LuaTable metatable;
   private int[] next = new int[1];
   private Object[] values = new Object[1];
   private boolean weakKeys;
   private boolean weakValues;

   private final Object __getKey(int var1) {
      Object var2 = this.keys[var1];
      Object var3 = var2;
      if (this.weakKeys) {
         var3 = this.unref(var2);
      }

      return var3;
   }

   private final Object __getValue(int var1) {
      Object var2 = this.values[var1];
      Object var3 = var2;
      if (this.weakValues) {
         var3 = this.unref(var2);
      }

      return var3;
   }

   private final void __setKey(int var1, Object var2) {
      Object var3 = var2;
      if (this.weakKeys) {
         var3 = this.ref(var2);
      }

      this.keys[var1] = var3;
   }

   private final void __setValue(int var1, Object var2) {
      Object var3 = var2;
      if (this.weakValues) {
         var3 = this.ref(var2);
      }

      this.values[var1] = var3;
   }

   private boolean canBeWeakObject(Object var1) {
      boolean var2;
      if (var1 != null && !(var1 instanceof String) && !(var1 instanceof Double) && !(var1 instanceof Boolean)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public static void checkKey(Object var0) {
      boolean var1;
      if (var0 != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      BaseLib.luaAssert(var1, "table index is nil");
   }

   private void fixWeakRefs(Object[] var1, boolean var2) {
      for(int var3 = var1.length - 1; var3 >= 0; --var3) {
         Object var4 = var1[var3];
         if (var2) {
            var4 = this.ref(var4);
         } else {
            var4 = this.unref(var4);
         }

         var1[var3] = var4;
      }

   }

   private int getHashIndex(Object var1) {
      int var2;
      if (var1 == this.keyIndexCacheKey) {
         var2 = this.keyIndexCacheValue;
      } else {
         int var3 = this.hash_primitiveFindKey(var1, this.getMP(var1));
         var2 = var3;
         if (!this.weakKeys) {
            this.keyIndexCacheKey = var1;
            this.keyIndexCacheValue = var3;
            var2 = var3;
         }
      }

      return var2;
   }

   private int getMP(Object var1) {
      int var2 = this.keys.length;
      return luaHashcode(var1) & var2 - 1;
   }

   private final int hash_primitiveFindKey(Object var1, int var2) {
      byte var3 = -1;
      Object var4 = this.__getKey(var2);
      if (var4 == null) {
         var2 = var3;
      } else {
         int var7;
         if (var1 instanceof Double) {
            for(double var5 = LuaState.fromDouble(var1); !(var4 instanceof Double) || var5 != LuaState.fromDouble(var4); var2 = var7) {
               var7 = this.next[var2];
               var2 = var3;
               if (var7 == -1) {
                  break;
               }

               var4 = this.__getKey(var7);
            }
         } else {
            Object var8 = var4;
            var7 = var2;
            if (var1 instanceof String) {
               while(!var1.equals(var4)) {
                  var7 = this.next[var2];
                  var2 = var3;
                  if (var7 == -1) {
                     break;
                  }

                  var4 = this.__getKey(var7);
                  var2 = var7;
               }
            } else {
               while(var1 != var8) {
                  var7 = this.next[var7];
                  if (var7 == -1) {
                     var2 = var3;
                     return var2;
                  }

                  var8 = this.__getKey(var7);
               }

               var2 = var7;
            }
         }
      }

      return var2;
   }

   private final int hash_primitiveNewKey(Object var1, int var2) {
      this.keyIndexCacheKey = null;
      this.keyIndexCacheValue = -1;
      Object var3 = this.__getKey(var2);
      if (var3 == null) {
         this.__setKey(var2, var1);
         this.next[var2] = -1;
      } else {
         while(true) {
            int var4;
            Object var5;
            try {
               var4 = this.freeIndex - 1;
               this.freeIndex = var4;
               var5 = this.__getKey(var4);
            } catch (ArrayIndexOutOfBoundsException var7) {
               this.hash_rehash(var1);
               var2 = -1;
               break;
            }

            if (var5 == null) {
               var4 = this.getMP(var3);
               if (var4 == var2) {
                  this.__setKey(this.freeIndex, var1);
                  this.next[this.freeIndex] = this.next[var2];
                  this.next[var2] = this.freeIndex;
                  var2 = this.freeIndex;
                  break;
               } else {
                  this.keys[this.freeIndex] = this.keys[var2];
                  this.values[this.freeIndex] = this.values[var2];
                  this.next[this.freeIndex] = this.next[var2];
                  this.__setKey(var2, var1);
                  this.next[var2] = -1;

                  while(true) {
                     int var6 = this.next[var4];
                     if (var6 == var2) {
                        this.next[var4] = this.freeIndex;
                        return var2;
                     }

                     var4 = var6;
                  }
               }
            }
         }
      }

      return var2;
   }

   private void hash_rehash(Object var1) {
      boolean var2 = this.weakKeys;
      boolean var3 = this.weakValues;
      this.updateWeakSettings(false, false);
      Object[] var4 = this.keys;
      Object[] var5 = this.values;
      int var6 = var4.length;
      int var7 = 1;

      int var8;
      int var9;
      for(var8 = var6 - 1; var8 >= 0; var7 = var9) {
         var9 = var7;
         if (this.keys[var8] != null) {
            var9 = var7;
            if (this.values[var8] != null) {
               var9 = var7 + 1;
            }
         }

         --var8;
      }

      var8 = nearestPowerOfTwo(var7) * 2;
      var9 = var8;
      if (var8 < 2) {
         var9 = 2;
      }

      this.keys = new Object[var9];
      this.values = new Object[var9];
      this.next = new int[var9];
      this.freeIndex = var9;

      for(var9 = var6 - 1; var9 >= 0; --var9) {
         Object var10 = var4[var9];
         if (var10 != null) {
            var1 = var5[var9];
            if (var1 != null) {
               this.rawset(var10, var1);
            }
         }
      }

      this.updateWeakSettings(var2, var3);
   }

   public static int luaHashcode(Object var0) {
      int var3;
      if (var0 instanceof Double) {
         long var1 = Double.doubleToLongBits((Double)var0) & Long.MAX_VALUE;
         var3 = (int)(var1 >>> 32 ^ var1);
      } else if (var0 instanceof String) {
         var3 = var0.hashCode();
      } else {
         var3 = System.identityHashCode(var0);
      }

      return var3;
   }

   private static int luaO_log2(int var0) {
      int var1;
      for(var1 = -1; var0 >= 256; var0 >>= 8) {
         var1 += 8;
      }

      return log_2[var0] + var1;
   }

   private static int nearestPowerOfTwo(int var0) {
      return 1 << luaO_log2(var0);
   }

   private static int neededBits(int var0) {
      return luaO_log2(var0) + 1;
   }

   private Object nextHash(Object var1) {
      int var2 = 0;
      if (var1 != null) {
         int var3 = this.getHashIndex(var1) + 1;
         var2 = var3;
         if (var3 <= 0) {
            BaseLib.fail("invalid key to 'next'");
            var1 = null;
            return var1;
         }
      }

      while(true) {
         if (var2 == this.keys.length) {
            var1 = null;
            break;
         }

         var1 = this.__getKey(var2);
         if (var1 != null && this.__getValue(var2) != null) {
            break;
         }

         ++var2;
      }

      return var1;
   }

   private Object rawgetHash(Object var1) {
      int var2 = this.getHashIndex(var1);
      if (var2 >= 0) {
         var1 = this.__getValue(var2);
      } else {
         var1 = null;
      }

      return var1;
   }

   private void rawsetHash(Object var1, Object var2) {
      int var3 = this.getHashIndex(var1);
      int var4 = var3;
      if (var3 < 0) {
         var3 = this.hash_primitiveNewKey(var1, this.getMP(var1));
         var4 = var3;
         if (var3 < 0) {
            this.rawset(var1, var2);
            return;
         }
      }

      this.__setValue(var4, var2);
   }

   private final Object ref(Object var1) {
      if (this.canBeWeakObject(var1)) {
         var1 = new WeakReference(var1);
      }

      return var1;
   }

   private final Object unref(Object var1) {
      if (this.canBeWeakObject(var1)) {
         var1 = ((WeakReference)var1).get();
      }

      return var1;
   }

   private void updateWeakSettings(boolean var1, boolean var2) {
      this.keyIndexCacheKey = null;
      this.keyIndexCacheValue = -1;
      if (var1 != this.weakKeys) {
         this.fixWeakRefs(this.keys, var1);
         this.weakKeys = var1;
      }

      if (var2 != this.weakValues) {
         this.fixWeakRefs(this.values, var2);
         this.weakValues = var2;
      }

   }

   public LuaTable getMetatable() {
      return this.metatable;
   }

   public final int len() {
      int var1 = this.keys.length * 2;
      int var2 = 0;

      while(true) {
         int var3 = var2;
         if (var2 >= var1) {
            while(this.rawget(var3 + 1) != null) {
               ++var3;
            }

            return var3;
         }

         var3 = var1 + var2 + 1 >> 1;
         if (this.rawget(var3) == null) {
            var1 = var3 - 1;
         } else {
            var2 = var3;
         }
      }
   }

   public final Object next(Object var1) {
      return this.nextHash(var1);
   }

   public Object rawget(int var1) {
      return this.rawgetHash(LuaState.toDouble((long)var1));
   }

   public final Object rawget(Object var1) {
      checkKey(var1);
      if (var1 instanceof Double) {
         boolean var2;
         if (!((Double)var1).isNaN()) {
            var2 = true;
         } else {
            var2 = false;
         }

         BaseLib.luaAssert(var2, "table index is NaN");
      }

      return this.rawgetHash(var1);
   }

   public void rawset(int var1, Object var2) {
      this.rawsetHash(LuaState.toDouble((long)var1), var2);
   }

   public final void rawset(Object var1, Object var2) {
      checkKey(var1);
      this.rawsetHash(var1, var2);
   }

   public void setMetatable(LuaTable var1) {
      this.metatable = var1;
      boolean var2 = false;
      boolean var3 = false;
      boolean var4 = var2;
      boolean var5 = var3;
      if (var1 != null) {
         Object var6 = var1.rawget(BaseLib.MODE_KEY);
         var4 = var2;
         var5 = var3;
         if (var6 != null) {
            var4 = var2;
            var5 = var3;
            if (var6 instanceof String) {
               String var7 = (String)var6;
               if (var7.indexOf(107) >= 0) {
                  var4 = true;
               } else {
                  var4 = false;
               }

               if (var7.indexOf(118) >= 0) {
                  var5 = true;
               } else {
                  var5 = false;
               }
            }
         }
      }

      this.updateWeakSettings(var4, var5);
   }
}
