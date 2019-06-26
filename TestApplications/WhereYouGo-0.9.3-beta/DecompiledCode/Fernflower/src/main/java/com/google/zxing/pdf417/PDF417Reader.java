package com.google.zxing.pdf417;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.multi.MultipleBarcodeReader;
import com.google.zxing.pdf417.decoder.PDF417ScanningDecoder;
import com.google.zxing.pdf417.detector.Detector;
import com.google.zxing.pdf417.detector.PDF417DetectorResult;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public final class PDF417Reader implements Reader, MultipleBarcodeReader {
   private static Result[] decode(BinaryBitmap var0, Map var1, boolean var2) throws NotFoundException, FormatException, ChecksumException {
      ArrayList var3 = new ArrayList();
      PDF417DetectorResult var6 = Detector.detect(var0, var1, var2);

      Result var8;
      for(Iterator var7 = var6.getPoints().iterator(); var7.hasNext(); var3.add(var8)) {
         ResultPoint[] var4 = (ResultPoint[])var7.next();
         DecoderResult var5 = PDF417ScanningDecoder.decode(var6.getBits(), var4[4], var4[5], var4[6], var4[7], getMinCodewordWidth(var4), getMaxCodewordWidth(var4));
         var8 = new Result(var5.getText(), var5.getRawBytes(), var4, BarcodeFormat.PDF_417);
         var8.putMetadata(ResultMetadataType.ERROR_CORRECTION_LEVEL, var5.getECLevel());
         PDF417ResultMetadata var9 = (PDF417ResultMetadata)var5.getOther();
         if (var9 != null) {
            var8.putMetadata(ResultMetadataType.PDF417_EXTRA_METADATA, var9);
         }
      }

      return (Result[])var3.toArray(new Result[var3.size()]);
   }

   private static int getMaxCodewordWidth(ResultPoint[] var0) {
      return Math.max(Math.max(getMaxWidth(var0[0], var0[4]), getMaxWidth(var0[6], var0[2]) * 17 / 18), Math.max(getMaxWidth(var0[1], var0[5]), getMaxWidth(var0[7], var0[3]) * 17 / 18));
   }

   private static int getMaxWidth(ResultPoint var0, ResultPoint var1) {
      int var2;
      if (var0 != null && var1 != null) {
         var2 = (int)Math.abs(var0.getX() - var1.getX());
      } else {
         var2 = 0;
      }

      return var2;
   }

   private static int getMinCodewordWidth(ResultPoint[] var0) {
      return Math.min(Math.min(getMinWidth(var0[0], var0[4]), getMinWidth(var0[6], var0[2]) * 17 / 18), Math.min(getMinWidth(var0[1], var0[5]), getMinWidth(var0[7], var0[3]) * 17 / 18));
   }

   private static int getMinWidth(ResultPoint var0, ResultPoint var1) {
      int var2;
      if (var0 != null && var1 != null) {
         var2 = (int)Math.abs(var0.getX() - var1.getX());
      } else {
         var2 = Integer.MAX_VALUE;
      }

      return var2;
   }

   public Result decode(BinaryBitmap var1) throws NotFoundException, FormatException, ChecksumException {
      return this.decode(var1, (Map)null);
   }

   public Result decode(BinaryBitmap var1, Map var2) throws NotFoundException, FormatException, ChecksumException {
      Result[] var3 = decode(var1, var2, false);
      if (var3 != null && var3.length != 0 && var3[0] != null) {
         return var3[0];
      } else {
         throw NotFoundException.getNotFoundInstance();
      }
   }

   public Result[] decodeMultiple(BinaryBitmap var1) throws NotFoundException {
      return this.decodeMultiple(var1, (Map)null);
   }

   public Result[] decodeMultiple(BinaryBitmap var1, Map var2) throws NotFoundException {
      try {
         Result[] var5 = decode(var1, var2, true);
         return var5;
      } catch (FormatException var3) {
      } catch (ChecksumException var4) {
      }

      throw NotFoundException.getNotFoundInstance();
   }

   public void reset() {
   }
}
