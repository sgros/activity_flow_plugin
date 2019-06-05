package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;

public final class EAN13Reader extends UPCEANReader {
   static final int[] FIRST_DIGIT_ENCODINGS = new int[]{0, 11, 13, 14, 19, 25, 28, 21, 22, 26};
   private final int[] decodeMiddleCounters = new int[4];

   private static void determineFirstDigit(StringBuilder var0, int var1) throws NotFoundException {
      for(int var2 = 0; var2 < 10; ++var2) {
         if (var1 == FIRST_DIGIT_ENCODINGS[var2]) {
            var0.insert(0, (char)(var2 + 48));
            return;
         }
      }

      throw NotFoundException.getNotFoundInstance();
   }

   protected int decodeMiddle(BitArray var1, int[] var2, StringBuilder var3) throws NotFoundException {
      int[] var4 = this.decodeMiddleCounters;
      var4[0] = 0;
      var4[1] = 0;
      var4[2] = 0;
      var4[3] = 0;
      int var5 = var1.getSize();
      int var6 = var2[1];
      int var7 = 0;

      int var8;
      int var11;
      for(var8 = 0; var8 < 6 && var6 < var5; var7 = var11) {
         int var9 = decodeDigit(var1, var4, var6, L_AND_G_PATTERNS);
         var3.append((char)(var9 % 10 + 48));
         int var10 = var4.length;

         for(var11 = 0; var11 < var10; ++var11) {
            var6 += var4[var11];
         }

         var11 = var7;
         if (var9 >= 10) {
            var11 = var7 | 1 << 5 - var8;
         }

         ++var8;
      }

      determineFirstDigit(var3, var7);
      var7 = findGuardPattern(var1, var6, true, MIDDLE_PATTERN)[1];

      for(var8 = 0; var8 < 6 && var7 < var5; ++var8) {
         var3.append((char)(decodeDigit(var1, var4, var7, L_PATTERNS) + 48));
         var11 = var4.length;

         for(var6 = 0; var6 < var11; ++var6) {
            var7 += var4[var6];
         }
      }

      return var7;
   }

   BarcodeFormat getBarcodeFormat() {
      return BarcodeFormat.EAN_13;
   }
}
