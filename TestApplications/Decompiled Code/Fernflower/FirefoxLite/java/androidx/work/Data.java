package androidx.work;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public final class Data {
   public static final Data EMPTY = (new Data.Builder()).build();
   private static final String TAG = Logger.tagWithPrefix("Data");
   Map mValues;

   Data() {
   }

   public Data(Data var1) {
      this.mValues = new HashMap(var1.mValues);
   }

   Data(Map var1) {
      this.mValues = new HashMap(var1);
   }

   static Boolean[] convertPrimitiveBooleanArray(boolean[] var0) {
      Boolean[] var1 = new Boolean[var0.length];

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1[var2] = var0[var2];
      }

      return var1;
   }

   static Double[] convertPrimitiveDoubleArray(double[] var0) {
      Double[] var1 = new Double[var0.length];

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1[var2] = var0[var2];
      }

      return var1;
   }

   static Float[] convertPrimitiveFloatArray(float[] var0) {
      Float[] var1 = new Float[var0.length];

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1[var2] = var0[var2];
      }

      return var1;
   }

   static Integer[] convertPrimitiveIntArray(int[] var0) {
      Integer[] var1 = new Integer[var0.length];

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1[var2] = var0[var2];
      }

      return var1;
   }

   static Long[] convertPrimitiveLongArray(long[] var0) {
      Long[] var1 = new Long[var0.length];

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1[var2] = var0[var2];
      }

      return var1;
   }

   public static Data fromByteArray(byte[] param0) throws IllegalStateException {
      // $FF: Couldn't be decompiled
   }

   public static byte[] toByteArray(Data param0) throws IllegalStateException {
      // $FF: Couldn't be decompiled
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         Data var2 = (Data)var1;
         return this.mValues.equals(var2.mValues);
      } else {
         return false;
      }
   }

   public Map getKeyValueMap() {
      return Collections.unmodifiableMap(this.mValues);
   }

   public String getString(String var1) {
      Object var2 = this.mValues.get(var1);
      return var2 instanceof String ? (String)var2 : null;
   }

   public int hashCode() {
      return this.mValues.hashCode() * 31;
   }

   public int size() {
      return this.mValues.size();
   }

   public static final class Builder {
      private Map mValues = new HashMap();

      public Data build() {
         Data var1 = new Data(this.mValues);
         Data.toByteArray(var1);
         return var1;
      }

      public Data.Builder put(String var1, Object var2) {
         if (var2 == null) {
            this.mValues.put(var1, (Object)null);
         } else {
            Class var3 = var2.getClass();
            if (var3 != Boolean.class && var3 != Integer.class && var3 != Long.class && var3 != Float.class && var3 != Double.class && var3 != String.class && var3 != Boolean[].class && var3 != Integer[].class && var3 != Long[].class && var3 != Float[].class && var3 != Double[].class && var3 != String[].class) {
               if (var3 == boolean[].class) {
                  this.mValues.put(var1, Data.convertPrimitiveBooleanArray((boolean[])var2));
               } else if (var3 == int[].class) {
                  this.mValues.put(var1, Data.convertPrimitiveIntArray((int[])var2));
               } else if (var3 == long[].class) {
                  this.mValues.put(var1, Data.convertPrimitiveLongArray((long[])var2));
               } else if (var3 == float[].class) {
                  this.mValues.put(var1, Data.convertPrimitiveFloatArray((float[])var2));
               } else {
                  if (var3 != double[].class) {
                     throw new IllegalArgumentException(String.format("Key %s has invalid type %s", var1, var3));
                  }

                  this.mValues.put(var1, Data.convertPrimitiveDoubleArray((double[])var2));
               }
            } else {
               this.mValues.put(var1, var2);
            }
         }

         return this;
      }

      public Data.Builder putAll(Data var1) {
         this.putAll(var1.mValues);
         return this;
      }

      public Data.Builder putAll(Map var1) {
         Iterator var3 = var1.entrySet().iterator();

         while(var3.hasNext()) {
            Entry var2 = (Entry)var3.next();
            this.put((String)var2.getKey(), var2.getValue());
         }

         return this;
      }

      public Data.Builder putString(String var1, String var2) {
         this.mValues.put(var1, var2);
         return this;
      }
   }
}
