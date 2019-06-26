package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.util.Map;

public final class Code39Writer extends OneDimensionalCodeWriter {
   private static void toIntArray(int var0, int[] var1) {
      for(int var2 = 0; var2 < 9; ++var2) {
         byte var3;
         if ((var0 & 1 << 8 - var2) == 0) {
            var3 = 1;
         } else {
            var3 = 2;
         }

         var1[var2] = var3;
      }

   }

   public BitMatrix encode(String var1, BarcodeFormat var2, int var3, int var4, Map var5) throws WriterException {
      if (var2 != BarcodeFormat.CODE_39) {
         throw new IllegalArgumentException("Can only encode CODE_39, but got " + var2);
      } else {
         return super.encode(var1, var2, var3, var4, var5);
      }
   }

   public boolean[] encode(String var1) {
      int var2 = var1.length();
      if (var2 > 80) {
         throw new IllegalArgumentException("Requested contents should be less than 80 digits long, but got " + var2);
      } else {
         int[] var3 = new int[9];
         int var4 = var2 + 25;

         int var5;
         int var6;
         for(var5 = 0; var5 < var2; ++var5) {
            var6 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. *$/+%".indexOf(var1.charAt(var5));
            if (var6 < 0) {
               throw new IllegalArgumentException("Bad contents: " + var1);
            }

            toIntArray(Code39Reader.CHARACTER_ENCODINGS[var6], var3);

            for(var6 = 0; var6 < 9; ++var6) {
               var4 += var3[var6];
            }
         }

         boolean[] var7 = new boolean[var4];
         toIntArray(Code39Reader.ASTERISK_ENCODING, var3);
         var5 = appendPattern(var7, 0, var3, true);
         int[] var8 = new int[]{1};
         var4 = var5 + appendPattern(var7, var5, var8, false);

         for(var5 = 0; var5 < var2; ++var5) {
            var6 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. *$/+%".indexOf(var1.charAt(var5));
            toIntArray(Code39Reader.CHARACTER_ENCODINGS[var6], var3);
            var4 += appendPattern(var7, var4, var3, true);
            var4 += appendPattern(var7, var4, var8, false);
         }

         toIntArray(Code39Reader.ASTERISK_ENCODING, var3);
         appendPattern(var7, var4, var3, true);
         return var7;
      }
   }
}
