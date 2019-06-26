package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.util.Map;

public class Code93Writer extends OneDimensionalCodeWriter {
   protected static int appendPattern(boolean[] var0, int var1, int[] var2, boolean var3) {
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var1) {
         if (var2[var5] != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         var0[var1] = var3;
         ++var5;
      }

      return 9;
   }

   private static int computeChecksumIndex(String var0, int var1) {
      int var2 = 1;
      int var3 = 0;

      for(int var4 = var0.length() - 1; var4 >= 0; --var4) {
         var3 += "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%abcd*".indexOf(var0.charAt(var4)) * var2;
         int var5 = var2 + 1;
         var2 = var5;
         if (var5 > var1) {
            var2 = 1;
         }
      }

      return var3 % 47;
   }

   private static void toIntArray(int var0, int[] var1) {
      for(int var2 = 0; var2 < 9; ++var2) {
         byte var3;
         if ((var0 & 1 << 8 - var2) == 0) {
            var3 = 0;
         } else {
            var3 = 1;
         }

         var1[var2] = var3;
      }

   }

   public BitMatrix encode(String var1, BarcodeFormat var2, int var3, int var4, Map var5) throws WriterException {
      if (var2 != BarcodeFormat.CODE_93) {
         throw new IllegalArgumentException("Can only encode CODE_93, but got " + var2);
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
         boolean[] var4 = new boolean[(var1.length() + 2 + 2) * 9 + 1];
         toIntArray(Code93Reader.CHARACTER_ENCODINGS[47], var3);
         int var5 = appendPattern(var4, 0, var3, true);

         int var6;
         for(var6 = 0; var6 < var2; ++var6) {
            int var7 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%abcd*".indexOf(var1.charAt(var6));
            toIntArray(Code93Reader.CHARACTER_ENCODINGS[var7], var3);
            var5 += appendPattern(var4, var5, var3, true);
         }

         var6 = computeChecksumIndex(var1, 20);
         toIntArray(Code93Reader.CHARACTER_ENCODINGS[var6], var3);
         var5 += appendPattern(var4, var5, var3, true);
         var6 = computeChecksumIndex(var1 + "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%abcd*".charAt(var6), 15);
         toIntArray(Code93Reader.CHARACTER_ENCODINGS[var6], var3);
         var5 += appendPattern(var4, var5, var3, true);
         toIntArray(Code93Reader.CHARACTER_ENCODINGS[47], var3);
         var4[var5 + appendPattern(var4, var5, var3, true)] = true;
         return var4;
      }
   }
}
