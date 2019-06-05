package com.google.zxing.qrcode;

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
import com.google.zxing.qrcode.decoder.Decoder;
import com.google.zxing.qrcode.decoder.QRCodeDecoderMetaData;
import com.google.zxing.qrcode.detector.Detector;
import java.util.List;
import java.util.Map;

public class QRCodeReader implements Reader {
   private static final ResultPoint[] NO_POINTS = new ResultPoint[0];
   private final Decoder decoder = new Decoder();

   private static BitMatrix extractPureBits(BitMatrix var0) throws NotFoundException {
      int[] var1 = var0.getTopLeftOnBit();
      int[] var2 = var0.getBottomRightOnBit();
      if (var1 != null && var2 != null) {
         float var3 = moduleSize(var1, var0);
         int var4 = var1[1];
         int var5 = var2[1];
         int var6 = var1[0];
         int var7 = var2[0];
         if (var6 < var7 && var4 < var5) {
            int var8 = var7;
            if (var5 - var4 != var7 - var6) {
               var7 = var6 + (var5 - var4);
               var8 = var7;
               if (var7 >= var0.getWidth()) {
                  throw NotFoundException.getNotFoundInstance();
               }
            }

            int var9 = Math.round((float)(var8 - var6 + 1) / var3);
            int var10 = Math.round((float)(var5 - var4 + 1) / var3);
            if (var9 > 0 && var10 > 0) {
               if (var10 != var9) {
                  throw NotFoundException.getNotFoundInstance();
               } else {
                  int var11 = (int)(var3 / 2.0F);
                  var4 += var11;
                  var7 = var6 + var11;
                  var6 = (int)((float)(var9 - 1) * var3) + var7 - var8;
                  var8 = var7;
                  if (var6 > 0) {
                     if (var6 > var11) {
                        throw NotFoundException.getNotFoundInstance();
                     }

                     var8 = var7 - var6;
                  }

                  var5 = (int)((float)(var10 - 1) * var3) + var4 - var5;
                  var7 = var4;
                  if (var5 > 0) {
                     if (var5 > var11) {
                        throw NotFoundException.getNotFoundInstance();
                     }

                     var7 = var4 - var5;
                  }

                  BitMatrix var12 = new BitMatrix(var9, var10);

                  for(var4 = 0; var4 < var10; ++var4) {
                     var11 = (int)((float)var4 * var3);

                     for(var5 = 0; var5 < var9; ++var5) {
                        if (var0.get((int)((float)var5 * var3) + var8, var7 + var11)) {
                           var12.set(var5, var4);
                        }
                     }
                  }

                  return var12;
               }
            } else {
               throw NotFoundException.getNotFoundInstance();
            }
         } else {
            throw NotFoundException.getNotFoundInstance();
         }
      } else {
         throw NotFoundException.getNotFoundInstance();
      }
   }

   private static float moduleSize(int[] var0, BitMatrix var1) throws NotFoundException {
      int var2 = var1.getHeight();
      int var3 = var1.getWidth();
      int var4 = var0[0];
      int var5 = var0[1];
      boolean var6 = true;

      int var9;
      for(int var7 = 0; var4 < var3 && var5 < var2; var7 = var9) {
         boolean var8 = var6;
         var9 = var7;
         if (var6 != var1.get(var4, var5)) {
            var9 = var7 + 1;
            if (var9 == 5) {
               break;
            }

            if (!var6) {
               var8 = true;
            } else {
               var8 = false;
            }
         }

         ++var4;
         ++var5;
         var6 = var8;
      }

      if (var4 != var3 && var5 != var2) {
         return (float)(var4 - var0[0]) / 7.0F;
      } else {
         throw NotFoundException.getNotFoundInstance();
      }
   }

   public Result decode(BinaryBitmap var1) throws NotFoundException, ChecksumException, FormatException {
      return this.decode(var1, (Map)null);
   }

   public final Result decode(BinaryBitmap var1, Map var2) throws NotFoundException, ChecksumException, FormatException {
      DecoderResult var4;
      ResultPoint[] var6;
      if (var2 != null && var2.containsKey(DecodeHintType.PURE_BARCODE)) {
         BitMatrix var5 = extractPureBits(var1.getBlackMatrix());
         var4 = this.decoder.decode(var5, var2);
         var6 = NO_POINTS;
      } else {
         DetectorResult var3 = (new Detector(var1.getBlackMatrix())).detect(var2);
         var4 = this.decoder.decode(var3.getBits(), var2);
         var6 = var3.getPoints();
      }

      if (var4.getOther() instanceof QRCodeDecoderMetaData) {
         ((QRCodeDecoderMetaData)var4.getOther()).applyMirroredCorrection(var6);
      }

      Result var8 = new Result(var4.getText(), var4.getRawBytes(), var6, BarcodeFormat.QR_CODE);
      List var7 = var4.getByteSegments();
      if (var7 != null) {
         var8.putMetadata(ResultMetadataType.BYTE_SEGMENTS, var7);
      }

      String var9 = var4.getECLevel();
      if (var9 != null) {
         var8.putMetadata(ResultMetadataType.ERROR_CORRECTION_LEVEL, var9);
      }

      if (var4.hasStructuredAppend()) {
         var8.putMetadata(ResultMetadataType.STRUCTURED_APPEND_SEQUENCE, var4.getStructuredAppendSequenceNumber());
         var8.putMetadata(ResultMetadataType.STRUCTURED_APPEND_PARITY, var4.getStructuredAppendParity());
      }

      return var8;
   }

   protected final Decoder getDecoder() {
      return this.decoder;
   }

   public void reset() {
   }
}
