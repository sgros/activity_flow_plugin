package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.util.Map;

public final class UPCEWriter extends UPCEANWriter {
   private static final int CODE_WIDTH = 51;

   public BitMatrix encode(String var1, BarcodeFormat var2, int var3, int var4, Map var5) throws WriterException {
      if (var2 != BarcodeFormat.UPC_E) {
         throw new IllegalArgumentException("Can only encode UPC_E, but got " + var2);
      } else {
         return super.encode(var1, var2, var3, var4, var5);
      }
   }

   public boolean[] encode(String var1) {
      if (var1.length() != 8) {
         throw new IllegalArgumentException("Requested contents should be 8 digits long, but got " + var1.length());
      } else {
         int var2 = Integer.parseInt(var1.substring(7, 8));
         int var3 = UPCEReader.CHECK_DIGIT_ENCODINGS[var2];
         boolean[] var4 = new boolean[51];
         int var5 = appendPattern(var4, 0, UPCEANReader.START_END_PATTERN, true) + 0;

         for(var2 = 1; var2 <= 6; ++var2) {
            int var6 = Integer.parseInt(var1.substring(var2, var2 + 1));
            int var7 = var6;
            if ((var3 >> 6 - var2 & 1) == 1) {
               var7 = var6 + 10;
            }

            var5 += appendPattern(var4, var5, UPCEANReader.L_AND_G_PATTERNS[var7], false);
         }

         appendPattern(var4, var5, UPCEANReader.END_PATTERN, false);
         return var4;
      }
   }
}
