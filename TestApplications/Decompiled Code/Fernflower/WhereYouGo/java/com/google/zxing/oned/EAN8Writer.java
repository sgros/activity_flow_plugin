package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.util.Map;

public final class EAN8Writer extends UPCEANWriter {
   private static final int CODE_WIDTH = 67;

   public BitMatrix encode(String var1, BarcodeFormat var2, int var3, int var4, Map var5) throws WriterException {
      if (var2 != BarcodeFormat.EAN_8) {
         throw new IllegalArgumentException("Can only encode EAN_8, but got " + var2);
      } else {
         return super.encode(var1, var2, var3, var4, var5);
      }
   }

   public boolean[] encode(String var1) {
      if (var1.length() != 8) {
         throw new IllegalArgumentException("Requested contents should be 8 digits long, but got " + var1.length());
      } else {
         boolean[] var2 = new boolean[67];
         int var3 = appendPattern(var2, 0, UPCEANReader.START_END_PATTERN, true) + 0;

         int var4;
         int var5;
         for(var4 = 0; var4 <= 3; ++var4) {
            var5 = Integer.parseInt(var1.substring(var4, var4 + 1));
            var3 += appendPattern(var2, var3, UPCEANReader.L_PATTERNS[var5], false);
         }

         var4 = var3 + appendPattern(var2, var3, UPCEANReader.MIDDLE_PATTERN, false);

         for(var3 = 4; var3 <= 7; ++var3) {
            var5 = Integer.parseInt(var1.substring(var3, var3 + 1));
            var4 += appendPattern(var2, var4, UPCEANReader.L_PATTERNS[var5], true);
         }

         appendPattern(var2, var4, UPCEANReader.START_END_PATTERN, true);
         return var2;
      }
   }
}
