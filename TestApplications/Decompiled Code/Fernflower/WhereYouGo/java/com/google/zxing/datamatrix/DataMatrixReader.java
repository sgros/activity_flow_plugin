package com.google.zxing.datamatrix;

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
import com.google.zxing.common.DetectorResult;
import com.google.zxing.datamatrix.decoder.Decoder;
import com.google.zxing.datamatrix.detector.Detector;
import java.util.List;
import java.util.Map;

public final class DataMatrixReader implements Reader {
   private static final ResultPoint[] NO_POINTS = new ResultPoint[0];
   private final Decoder decoder = new Decoder();

   private static BitMatrix extractPureBits(BitMatrix var0) throws NotFoundException {
      int[] var1 = var0.getTopLeftOnBit();
      int[] var2 = var0.getBottomRightOnBit();
      if (var1 != null && var2 != null) {
         int var3 = moduleSize(var1, var0);
         int var4 = var1[1];
         int var5 = var2[1];
         int var6 = var1[0];
         int var7 = (var2[0] - var6 + 1) / var3;
         int var8 = (var5 - var4 + 1) / var3;
         if (var7 > 0 && var8 > 0) {
            int var9 = var3 / 2;
            BitMatrix var11 = new BitMatrix(var7, var8);

            for(var5 = 0; var5 < var8; ++var5) {
               for(int var10 = 0; var10 < var7; ++var10) {
                  if (var0.get(var10 * var3 + var6 + var9, var4 + var9 + var5 * var3)) {
                     var11.set(var10, var5);
                  }
               }
            }

            return var11;
         } else {
            throw NotFoundException.getNotFoundInstance();
         }
      } else {
         throw NotFoundException.getNotFoundInstance();
      }
   }

   private static int moduleSize(int[] var0, BitMatrix var1) throws NotFoundException {
      int var2 = var1.getWidth();
      int var3 = var0[0];

      for(int var4 = var0[1]; var3 < var2 && var1.get(var3, var4); ++var3) {
      }

      if (var3 == var2) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         var3 -= var0[0];
         if (var3 == 0) {
            throw NotFoundException.getNotFoundInstance();
         } else {
            return var3;
         }
      }
   }

   public Result decode(BinaryBitmap var1) throws NotFoundException, ChecksumException, FormatException {
      return this.decode(var1, (Map)null);
   }

   public Result decode(BinaryBitmap var1, Map var2) throws NotFoundException, ChecksumException, FormatException {
      DecoderResult var4;
      ResultPoint[] var7;
      if (var2 != null && var2.containsKey(DecodeHintType.PURE_BARCODE)) {
         BitMatrix var5 = extractPureBits(var1.getBlackMatrix());
         var4 = this.decoder.decode(var5);
         var7 = NO_POINTS;
      } else {
         DetectorResult var6 = (new Detector(var1.getBlackMatrix())).detect();
         var4 = this.decoder.decode(var6.getBits());
         var7 = var6.getPoints();
      }

      Result var9 = new Result(var4.getText(), var4.getRawBytes(), var7, BarcodeFormat.DATA_MATRIX);
      List var3 = var4.getByteSegments();
      if (var3 != null) {
         var9.putMetadata(ResultMetadataType.BYTE_SEGMENTS, var3);
      }

      String var8 = var4.getECLevel();
      if (var8 != null) {
         var9.putMetadata(ResultMetadataType.ERROR_CORRECTION_LEVEL, var8);
      }

      return var9;
   }

   public void reset() {
   }
}
