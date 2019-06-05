package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;

public final class EAN8Reader extends UPCEANReader {
   private final int[] decodeMiddleCounters = new int[4];

   protected int decodeMiddle(BitArray var1, int[] var2, StringBuilder var3) throws NotFoundException {
      int[] var4 = this.decodeMiddleCounters;
      var4[0] = 0;
      var4[1] = 0;
      var4[2] = 0;
      var4[3] = 0;
      int var5 = var1.getSize();
      int var6 = var2[1];

      int var7;
      int var8;
      int var9;
      for(var7 = 0; var7 < 4 && var6 < var5; ++var7) {
         var3.append((char)(decodeDigit(var1, var4, var6, L_PATTERNS) + 48));
         var8 = var4.length;

         for(var9 = 0; var9 < var8; ++var9) {
            var6 += var4[var9];
         }
      }

      var6 = findGuardPattern(var1, var6, true, MIDDLE_PATTERN)[1];

      for(var7 = 0; var7 < 4 && var6 < var5; ++var7) {
         var3.append((char)(decodeDigit(var1, var4, var6, L_PATTERNS) + 48));
         var8 = var4.length;

         for(var9 = 0; var9 < var8; ++var9) {
            var6 += var4[var9];
         }
      }

      return var6;
   }

   BarcodeFormat getBarcodeFormat() {
      return BarcodeFormat.EAN_8;
   }
}
