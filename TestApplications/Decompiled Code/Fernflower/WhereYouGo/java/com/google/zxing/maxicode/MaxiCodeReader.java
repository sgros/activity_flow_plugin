package com.google.zxing.maxicode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.maxicode.decoder.Decoder;
import java.util.Map;

public final class MaxiCodeReader implements Reader {
   private static final int MATRIX_HEIGHT = 33;
   private static final int MATRIX_WIDTH = 30;
   private static final ResultPoint[] NO_POINTS = new ResultPoint[0];
   private final Decoder decoder = new Decoder();

   private static BitMatrix extractPureBits(BitMatrix var0) throws NotFoundException {
      int[] var1 = var0.getEnclosingRectangle();
      if (var1 == null) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         int var2 = var1[0];
         int var3 = var1[1];
         int var4 = var1[2];
         int var5 = var1[3];
         BitMatrix var9 = new BitMatrix(30, 33);

         for(int var6 = 0; var6 < 33; ++var6) {
            int var7 = (var6 * var5 + var5 / 2) / 33;

            for(int var8 = 0; var8 < 30; ++var8) {
               if (var0.get(var2 + (var8 * var4 + var4 / 2 + (var6 & 1) * var4 / 2) / 30, var3 + var7)) {
                  var9.set(var8, var6);
               }
            }
         }

         return var9;
      }
   }

   public Result decode(BinaryBitmap var1) throws NotFoundException, ChecksumException, FormatException {
      return this.decode(var1, (Map)null);
   }

   public Result decode(BinaryBitmap var1, Map var2) throws NotFoundException, ChecksumException, FormatException {
      if (var2 != null && var2.containsKey(DecodeHintType.PURE_BARCODE)) {
         BitMatrix var3 = extractPureBits(var1.getBlackMatrix());
         DecoderResult var5 = this.decoder.decode(var3, var2);
         Result var4 = new Result(var5.getText(), var5.getRawBytes(), NO_POINTS, BarcodeFormat.MAXICODE);
         String var6 = var5.getECLevel();
         if (var6 != null) {
            var4.putMetadata(ResultMetadataType.ERROR_CORRECTION_LEVEL, var6);
         }

         return var4;
      } else {
         throw NotFoundException.getNotFoundInstance();
      }
   }

   public void reset() {
   }
}
