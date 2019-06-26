package com.coremedia.iso;

public class Hex {
   private static final char[] DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

   public static String encodeHex(byte[] var0) {
      return encodeHex(var0, 0);
   }

   public static String encodeHex(byte[] var0, int var1) {
      int var2 = var0.length;
      int var3 = 0;
      int var4;
      if (var1 > 0) {
         var4 = var2 / var1;
      } else {
         var4 = 0;
      }

      char[] var5 = new char[(var2 << 1) + var4];

      for(var4 = 0; var3 < var2; ++var3) {
         int var6 = var4;
         if (var1 > 0) {
            var6 = var4;
            if (var3 % var1 == 0) {
               var6 = var4;
               if (var4 > 0) {
                  var5[var4] = (char)45;
                  var6 = var4 + 1;
               }
            }
         }

         int var7 = var6 + 1;
         char[] var8 = DIGITS;
         var5[var6] = (char)var8[(var0[var3] & 240) >>> 4];
         var4 = var7 + 1;
         var5[var7] = (char)var8[var0[var3] & 15];
      }

      return new String(var5);
   }
}
