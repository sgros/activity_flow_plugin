package org.telegram.messenger.time;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

abstract class FormatCache {
   static final int NONE = -1;
   private static final ConcurrentMap cDateTimeInstanceCache = new ConcurrentHashMap(7);
   private final ConcurrentMap cInstanceCache = new ConcurrentHashMap(7);

   private Format getDateTimeInstance(Integer var1, Integer var2, TimeZone var3, Locale var4) {
      Locale var5 = var4;
      if (var4 == null) {
         var5 = Locale.getDefault();
      }

      return this.getInstance(getPatternForStyle(var1, var2, var5), var3, var5);
   }

   static String getPatternForStyle(Integer var0, Integer var1, Locale var2) {
      FormatCache.MultipartKey var3 = new FormatCache.MultipartKey(new Object[]{var0, var1, var2});
      String var4 = (String)cDateTimeInstanceCache.get(var3);
      String var5 = var4;
      if (var4 == null) {
         String var12;
         label43: {
            label42: {
               boolean var10001;
               DateFormat var10;
               if (var0 == null) {
                  try {
                     var10 = DateFormat.getTimeInstance(var1, var2);
                  } catch (ClassCastException var9) {
                     var10001 = false;
                     break label42;
                  }
               } else if (var1 == null) {
                  try {
                     var10 = DateFormat.getDateInstance(var0, var2);
                  } catch (ClassCastException var8) {
                     var10001 = false;
                     break label42;
                  }
               } else {
                  try {
                     var10 = DateFormat.getDateTimeInstance(var0, var1, var2);
                  } catch (ClassCastException var7) {
                     var10001 = false;
                     break label42;
                  }
               }

               try {
                  var5 = ((SimpleDateFormat)var10).toPattern();
                  var12 = (String)cDateTimeInstanceCache.putIfAbsent(var3, var5);
                  break label43;
               } catch (ClassCastException var6) {
                  var10001 = false;
               }
            }

            StringBuilder var11 = new StringBuilder();
            var11.append("No date time pattern for locale: ");
            var11.append(var2);
            throw new IllegalArgumentException(var11.toString());
         }

         if (var12 != null) {
            var5 = var12;
         }
      }

      return var5;
   }

   protected abstract Format createInstance(String var1, TimeZone var2, Locale var3);

   Format getDateInstance(int var1, TimeZone var2, Locale var3) {
      return this.getDateTimeInstance(var1, (Integer)null, var2, var3);
   }

   Format getDateTimeInstance(int var1, int var2, TimeZone var3, Locale var4) {
      return this.getDateTimeInstance(var1, var2, var3, var4);
   }

   public Format getInstance() {
      return this.getDateTimeInstance(3, 3, TimeZone.getDefault(), Locale.getDefault());
   }

   public Format getInstance(String var1, TimeZone var2, Locale var3) {
      if (var1 != null) {
         TimeZone var4 = var2;
         if (var2 == null) {
            var4 = TimeZone.getDefault();
         }

         Locale var5 = var3;
         if (var3 == null) {
            var5 = Locale.getDefault();
         }

         FormatCache.MultipartKey var6 = new FormatCache.MultipartKey(new Object[]{var1, var4, var5});
         Format var9 = (Format)this.cInstanceCache.get(var6);
         Format var8 = var9;
         if (var9 == null) {
            var8 = this.createInstance(var1, var4, var5);
            Format var7 = (Format)this.cInstanceCache.putIfAbsent(var6, var8);
            if (var7 != null) {
               var8 = var7;
            }
         }

         return var8;
      } else {
         throw new NullPointerException("pattern must not be null");
      }
   }

   Format getTimeInstance(int var1, TimeZone var2, Locale var3) {
      return this.getDateTimeInstance((Integer)null, var1, var2, var3);
   }

   private static class MultipartKey {
      private int hashCode;
      private final Object[] keys;

      public MultipartKey(Object... var1) {
         this.keys = var1;
      }

      public boolean equals(Object var1) {
         return Arrays.equals(this.keys, ((FormatCache.MultipartKey)var1).keys);
      }

      public int hashCode() {
         if (this.hashCode == 0) {
            Object[] var1 = this.keys;
            int var2 = var1.length;
            int var3 = 0;

            int var4;
            int var6;
            for(var4 = 0; var3 < var2; var4 = var6) {
               Object var5 = var1[var3];
               var6 = var4;
               if (var5 != null) {
                  var6 = var4 * 7 + var5.hashCode();
               }

               ++var3;
            }

            this.hashCode = var4;
         }

         return this.hashCode;
      }
   }
}
