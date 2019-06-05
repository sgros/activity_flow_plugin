package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.FormatException;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.util.Map;

public final class EAN13Writer extends UPCEANWriter {
   private static final int CODE_WIDTH = 95;

   public BitMatrix encode(String var1, BarcodeFormat var2, int var3, int var4, Map var5) throws WriterException {
      if (var2 != BarcodeFormat.EAN_13) {
         throw new IllegalArgumentException("Can only encode EAN_13, but got " + var2);
      } else {
         return super.encode(var1, var2, var3, var4, var5);
      }
   }

   public boolean[] encode(String var1) {
      if (var1.length() != 13) {
         throw new IllegalArgumentException("Requested contents should be 13 digits long, but got " + var1.length());
      } else {
         try {
            if (!UPCEANReader.checkStandardUPCEANChecksum(var1)) {
               IllegalArgumentException var9 = new IllegalArgumentException("Contents do not pass checksum");
               throw var9;
            }
         } catch (FormatException var8) {
            throw new IllegalArgumentException("Illegal contents");
         }

         int var2 = Integer.parseInt(var1.substring(0, 1));
         int var3 = EAN13Reader.FIRST_DIGIT_ENCODINGS[var2];
         boolean[] var4 = new boolean[95];
         var2 = appendPattern(var4, 0, UPCEANReader.START_END_PATTERN, true) + 0;

         int var5;
         int var7;
         for(var5 = 1; var5 <= 6; ++var5) {
            int var6 = Integer.parseInt(var1.substring(var5, var5 + 1));
            var7 = var6;
            if ((var3 >> 6 - var5 & 1) == 1) {
               var7 = var6 + 10;
            }

            var2 += appendPattern(var4, var2, UPCEANReader.L_AND_G_PATTERNS[var7], false);
         }

         var5 = var2 + appendPattern(var4, var2, UPCEANReader.MIDDLE_PATTERN, false);

         for(var2 = 7; var2 <= 12; ++var2) {
            var7 = Integer.parseInt(var1.substring(var2, var2 + 1));
            var5 += appendPattern(var4, var5, UPCEANReader.L_PATTERNS[var7], true);
         }

         appendPattern(var4, var5, UPCEANReader.START_END_PATTERN, true);
         return var4;
      }
   }
}
