package com.google.zxing.aztec;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.aztec.encoder.AztecCode;
import com.google.zxing.aztec.encoder.Encoder;
import com.google.zxing.common.BitMatrix;
import java.nio.charset.Charset;
import java.util.Map;

public final class AztecWriter implements Writer {
   private static final Charset DEFAULT_CHARSET = Charset.forName("ISO-8859-1");

   private static BitMatrix encode(String var0, BarcodeFormat var1, int var2, int var3, Charset var4, int var5, int var6) {
      if (var1 != BarcodeFormat.AZTEC) {
         throw new IllegalArgumentException("Can only encode AZTEC, but got " + var1);
      } else {
         return renderResult(Encoder.encode(var0.getBytes(var4), var5, var6), var2, var3);
      }
   }

   private static BitMatrix renderResult(AztecCode var0, int var1, int var2) {
      BitMatrix var3 = var0.getMatrix();
      if (var3 == null) {
         throw new IllegalStateException();
      } else {
         int var4 = var3.getWidth();
         int var5 = var3.getHeight();
         int var6 = Math.max(var1, var4);
         var2 = Math.max(var2, var5);
         int var7 = Math.min(var6 / var4, var2 / var5);
         int var8 = (var6 - var4 * var7) / 2;
         var1 = (var2 - var5 * var7) / 2;
         BitMatrix var10 = new BitMatrix(var6, var2);

         for(var2 = 0; var2 < var5; var1 += var7) {
            int var9 = 0;

            for(var6 = var8; var9 < var4; var6 += var7) {
               if (var3.get(var9, var2)) {
                  var10.setRegion(var6, var1, var7, var7);
               }

               ++var9;
            }

            ++var2;
         }

         return var10;
      }
   }

   public BitMatrix encode(String var1, BarcodeFormat var2, int var3, int var4) {
      return this.encode(var1, var2, var3, var4, (Map)null);
   }

   public BitMatrix encode(String var1, BarcodeFormat var2, int var3, int var4, Map var5) {
      Charset var6 = DEFAULT_CHARSET;
      int var7 = 33;
      byte var8 = 0;
      Charset var9 = var6;
      int var10 = var7;
      int var11 = var8;
      if (var5 != null) {
         if (var5.containsKey(EncodeHintType.CHARACTER_SET)) {
            var6 = Charset.forName(var5.get(EncodeHintType.CHARACTER_SET).toString());
         }

         if (var5.containsKey(EncodeHintType.ERROR_CORRECTION)) {
            var7 = Integer.parseInt(var5.get(EncodeHintType.ERROR_CORRECTION).toString());
         }

         var9 = var6;
         var10 = var7;
         var11 = var8;
         if (var5.containsKey(EncodeHintType.AZTEC_LAYERS)) {
            var11 = Integer.parseInt(var5.get(EncodeHintType.AZTEC_LAYERS).toString());
            var10 = var7;
            var9 = var6;
         }
      }

      return encode(var1, var2, var3, var4, var9, var10, var11);
   }
}