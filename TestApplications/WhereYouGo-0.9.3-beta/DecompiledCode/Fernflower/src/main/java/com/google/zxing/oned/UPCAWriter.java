package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.util.Map;

public final class UPCAWriter implements Writer {
   private final EAN13Writer subWriter = new EAN13Writer();

   private static String preencode(String var0) {
      int var1 = var0.length();
      String var5;
      if (var1 == 11) {
         int var2 = 0;

         for(var1 = 0; var1 < 11; ++var1) {
            char var3 = var0.charAt(var1);
            byte var4;
            if (var1 % 2 == 0) {
               var4 = 3;
            } else {
               var4 = 1;
            }

            var2 += var4 * (var3 - 48);
         }

         var5 = var0 + (1000 - var2) % 10;
      } else {
         var5 = var0;
         if (var1 != 12) {
            throw new IllegalArgumentException("Requested contents should be 11 or 12 digits long, but got " + var0.length());
         }
      }

      return "0" + var5;
   }

   public BitMatrix encode(String var1, BarcodeFormat var2, int var3, int var4) throws WriterException {
      return this.encode(var1, var2, var3, var4, (Map)null);
   }

   public BitMatrix encode(String var1, BarcodeFormat var2, int var3, int var4, Map var5) throws WriterException {
      if (var2 != BarcodeFormat.UPC_A) {
         throw new IllegalArgumentException("Can only encode UPC-A, but got " + var2);
      } else {
         return this.subWriter.encode(preencode(var1), BarcodeFormat.EAN_13, var3, var4, var5);
      }
   }
}
