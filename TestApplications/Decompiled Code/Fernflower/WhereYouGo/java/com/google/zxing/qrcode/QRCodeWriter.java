package com.google.zxing.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;
import java.util.Map;

public final class QRCodeWriter implements Writer {
   private static final int QUIET_ZONE_SIZE = 4;

   private static BitMatrix renderResult(QRCode var0, int var1, int var2, int var3) {
      ByteMatrix var10 = var0.getMatrix();
      if (var10 == null) {
         throw new IllegalStateException();
      } else {
         int var4 = var10.getWidth();
         int var5 = var10.getHeight();
         int var6 = var4 + (var3 << 1);
         int var7 = var5 + (var3 << 1);
         var3 = Math.max(var1, var6);
         var2 = Math.max(var2, var7);
         int var8 = Math.min(var3 / var6, var2 / var7);
         var7 = (var3 - var4 * var8) / 2;
         var1 = (var2 - var5 * var8) / 2;
         BitMatrix var9 = new BitMatrix(var3, var2);

         for(var2 = 0; var2 < var5; var1 += var8) {
            var6 = 0;

            for(var3 = var7; var6 < var4; var3 += var8) {
               if (var10.get(var6, var2) == 1) {
                  var9.setRegion(var3, var1, var8, var8);
               }

               ++var6;
            }

            ++var2;
         }

         return var9;
      }
   }

   public BitMatrix encode(String var1, BarcodeFormat var2, int var3, int var4) throws WriterException {
      return this.encode(var1, var2, var3, var4, (Map)null);
   }

   public BitMatrix encode(String var1, BarcodeFormat var2, int var3, int var4, Map var5) throws WriterException {
      if (var1.isEmpty()) {
         throw new IllegalArgumentException("Found empty contents");
      } else if (var2 != BarcodeFormat.QR_CODE) {
         throw new IllegalArgumentException("Can only encode QR_CODE, but got " + var2);
      } else if (var3 >= 0 && var4 >= 0) {
         ErrorCorrectionLevel var9 = ErrorCorrectionLevel.L;
         byte var6 = 4;
         ErrorCorrectionLevel var7 = var9;
         int var8 = var6;
         if (var5 != null) {
            if (var5.containsKey(EncodeHintType.ERROR_CORRECTION)) {
               var9 = ErrorCorrectionLevel.valueOf(var5.get(EncodeHintType.ERROR_CORRECTION).toString());
            }

            var7 = var9;
            var8 = var6;
            if (var5.containsKey(EncodeHintType.MARGIN)) {
               var8 = Integer.parseInt(var5.get(EncodeHintType.MARGIN).toString());
               var7 = var9;
            }
         }

         return renderResult(Encoder.encode(var1, var7, var5), var3, var4, var8);
      } else {
         throw new IllegalArgumentException("Requested dimensions are too small: " + var3 + 'x' + var4);
      }
   }
}
