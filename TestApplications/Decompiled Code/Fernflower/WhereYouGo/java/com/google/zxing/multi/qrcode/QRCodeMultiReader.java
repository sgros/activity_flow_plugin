package com.google.zxing.multi.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.NotFoundException;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.multi.MultipleBarcodeReader;
import com.google.zxing.multi.qrcode.detector.MultiDetector;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.decoder.QRCodeDecoderMetaData;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class QRCodeMultiReader extends QRCodeReader implements MultipleBarcodeReader {
   private static final Result[] EMPTY_RESULT_ARRAY = new Result[0];
   private static final ResultPoint[] NO_POINTS = new ResultPoint[0];

   private static List processStructuredAppend(List var0) {
      boolean var1 = false;
      Iterator var2 = ((List)var0).iterator();

      boolean var3;
      while(true) {
         var3 = var1;
         if (!var2.hasNext()) {
            break;
         }

         if (((Result)var2.next()).getResultMetadata().containsKey(ResultMetadataType.STRUCTURED_APPEND_SEQUENCE)) {
            var3 = true;
            break;
         }
      }

      if (var3) {
         ArrayList var15 = new ArrayList();
         ArrayList var4 = new ArrayList();
         Iterator var5 = ((List)var0).iterator();

         Result var12;
         while(var5.hasNext()) {
            var12 = (Result)var5.next();
            var15.add(var12);
            if (var12.getResultMetadata().containsKey(ResultMetadataType.STRUCTURED_APPEND_SEQUENCE)) {
               var4.add(var12);
            }
         }

         Collections.sort(var4, new QRCodeMultiReader.SAComparator());
         StringBuilder var13 = new StringBuilder();
         int var14 = 0;
         int var16 = 0;
         var5 = var4.iterator();

         while(true) {
            Result var6;
            int var7;
            int var8;
            do {
               if (!var5.hasNext()) {
                  byte[] var20 = new byte[var14];
                  byte[] var18 = new byte[var16];
                  var14 = 0;
                  var7 = 0;
                  Iterator var17 = var4.iterator();

                  while(true) {
                     Result var9;
                     int var10;
                     do {
                        if (!var17.hasNext()) {
                           var12 = new Result(var13.toString(), var20, NO_POINTS, BarcodeFormat.QR_CODE);
                           if (var16 > 0) {
                              var4 = new ArrayList();
                              var4.add(var18);
                              var12.putMetadata(ResultMetadataType.BYTE_SEGMENTS, var4);
                           }

                           var15.add(var12);
                           var0 = var15;
                           return (List)var0;
                        }

                        var9 = (Result)var17.next();
                        System.arraycopy(var9.getRawBytes(), 0, var20, var14, var9.getRawBytes().length);
                        var10 = var14 + var9.getRawBytes().length;
                        var14 = var10;
                     } while(!var9.getResultMetadata().containsKey(ResultMetadataType.BYTE_SEGMENTS));

                     Iterator var21 = ((Iterable)var9.getResultMetadata().get(ResultMetadataType.BYTE_SEGMENTS)).iterator();
                     var8 = var7;

                     while(true) {
                        var7 = var8;
                        var14 = var10;
                        if (!var21.hasNext()) {
                           break;
                        }

                        byte[] var11 = (byte[])var21.next();
                        System.arraycopy(var11, 0, var18, var8, var11.length);
                        var8 += var11.length;
                     }
                  }
               }

               var6 = (Result)var5.next();
               var13.append(var6.getText());
               var7 = var14 + var6.getRawBytes().length;
               var14 = var7;
            } while(!var6.getResultMetadata().containsKey(ResultMetadataType.BYTE_SEGMENTS));

            Iterator var19 = ((Iterable)var6.getResultMetadata().get(ResultMetadataType.BYTE_SEGMENTS)).iterator();
            var8 = var16;

            while(true) {
               var16 = var8;
               var14 = var7;
               if (!var19.hasNext()) {
                  break;
               }

               var8 += ((byte[])var19.next()).length;
            }
         }
      } else {
         return (List)var0;
      }
   }

   public Result[] decodeMultiple(BinaryBitmap var1) throws NotFoundException {
      return this.decodeMultiple(var1, (Map)null);
   }

   public Result[] decodeMultiple(BinaryBitmap var1, Map var2) throws NotFoundException {
      ArrayList var3 = new ArrayList();
      DetectorResult[] var16 = (new MultiDetector(var1.getBlackMatrix())).detectMulti(var2);
      int var4 = var16.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         DetectorResult var6 = var16[var5];

         DecoderResult var7;
         ResultPoint[] var8;
         boolean var10001;
         try {
            var7 = this.getDecoder().decode(var6.getBits(), var2);
            var8 = var6.getPoints();
            if (var7.getOther() instanceof QRCodeDecoderMetaData) {
               ((QRCodeDecoderMetaData)var7.getOther()).applyMirroredCorrection(var8);
            }
         } catch (ReaderException var15) {
            var10001 = false;
            continue;
         }

         Result var19;
         List var20;
         try {
            var19 = new Result(var7.getText(), var7.getRawBytes(), var8, BarcodeFormat.QR_CODE);
            var20 = var7.getByteSegments();
         } catch (ReaderException var14) {
            var10001 = false;
            continue;
         }

         if (var20 != null) {
            try {
               var19.putMetadata(ResultMetadataType.BYTE_SEGMENTS, var20);
            } catch (ReaderException var13) {
               var10001 = false;
               continue;
            }
         }

         String var21;
         try {
            var21 = var7.getECLevel();
         } catch (ReaderException var12) {
            var10001 = false;
            continue;
         }

         if (var21 != null) {
            try {
               var19.putMetadata(ResultMetadataType.ERROR_CORRECTION_LEVEL, var21);
            } catch (ReaderException var11) {
               var10001 = false;
               continue;
            }
         }

         try {
            if (var7.hasStructuredAppend()) {
               var19.putMetadata(ResultMetadataType.STRUCTURED_APPEND_SEQUENCE, var7.getStructuredAppendSequenceNumber());
               var19.putMetadata(ResultMetadataType.STRUCTURED_APPEND_PARITY, var7.getStructuredAppendParity());
            }
         } catch (ReaderException var10) {
            var10001 = false;
            continue;
         }

         try {
            var3.add(var19);
         } catch (ReaderException var9) {
            var10001 = false;
         }
      }

      Result[] var17;
      if (var3.isEmpty()) {
         var17 = EMPTY_RESULT_ARRAY;
      } else {
         List var18 = processStructuredAppend(var3);
         var17 = (Result[])var18.toArray(new Result[var18.size()]);
      }

      return var17;
   }

   private static final class SAComparator implements Serializable, Comparator {
      private SAComparator() {
      }

      // $FF: synthetic method
      SAComparator(Object var1) {
         this();
      }

      public int compare(Result var1, Result var2) {
         int var3 = (Integer)var1.getResultMetadata().get(ResultMetadataType.STRUCTURED_APPEND_SEQUENCE);
         int var4 = (Integer)var2.getResultMetadata().get(ResultMetadataType.STRUCTURED_APPEND_SEQUENCE);
         byte var5;
         if (var3 < var4) {
            var5 = -1;
         } else if (var3 > var4) {
            var5 = 1;
         } else {
            var5 = 0;
         }

         return var5;
      }
   }
}
