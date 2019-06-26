package com.google.zxing.oned.rss;

public final class RSSUtils {
   private RSSUtils() {
   }

   private static int combins(int var0, int var1) {
      int var2;
      int var3;
      if (var0 - var1 > var1) {
         var2 = var1;
         var3 = var0 - var1;
      } else {
         var2 = var0 - var1;
         var3 = var1;
      }

      var1 = 1;
      byte var4 = 1;
      int var5 = var0;
      var0 = var4;

      while(true) {
         int var7 = var0;
         int var6 = var1;
         if (var5 <= var3) {
            while(var7 <= var2) {
               var6 /= var7;
               ++var7;
            }

            return var6;
         }

         var6 = var1 * var5;
         var7 = var0;
         var1 = var6;
         if (var0 <= var2) {
            var1 = var6 / var0;
            var7 = var0 + 1;
         }

         --var5;
         var0 = var7;
      }
   }

   public static int getRSSvalue(int[] var0, int var1, boolean var2) {
      int var3 = 0;
      int var4 = var0.length;

      int var5;
      for(var5 = 0; var5 < var4; ++var5) {
         var3 += var0[var5];
      }

      int var6 = 0;
      var5 = 0;
      int var7 = var0.length;
      int var8 = 0;

      for(int var9 = var3; var8 < var7 - 1; var5 = var3) {
         int var10 = 1;

         for(var3 = var5 | 1 << var8; var10 < var0[var8]; var3 &= ~(1 << var8)) {
            var4 = combins(var9 - var10 - 1, var7 - var8 - 2);
            var5 = var4;
            if (var2) {
               var5 = var4;
               if (var3 == 0) {
                  var5 = var4;
                  if (var9 - var10 - (var7 - var8 - 1) >= var7 - var8 - 1) {
                     var5 = var4 - combins(var9 - var10 - (var7 - var8), var7 - var8 - 2);
                  }
               }
            }

            if (var7 - var8 - 1 <= 1) {
               var4 = var5;
               if (var9 - var10 > var1) {
                  var4 = var5 - 1;
               }
            } else {
               int var11 = 0;

               for(var4 = var9 - var10 - (var7 - var8 - 2); var4 > var1; --var4) {
                  var11 += combins(var9 - var10 - var4 - 1, var7 - var8 - 3);
               }

               var4 = var5 - (var7 - 1 - var8) * var11;
            }

            var6 += var4;
            ++var10;
         }

         var9 -= var10;
         ++var8;
      }

      return var6;
   }
}
