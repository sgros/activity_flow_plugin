package com.google.zxing.pdf417;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.pdf417.encoder.Compaction;
import com.google.zxing.pdf417.encoder.Dimensions;
import com.google.zxing.pdf417.encoder.PDF417;
import java.nio.charset.Charset;
import java.util.Map;

public final class PDF417Writer implements Writer {
   static final int DEFAULT_ERROR_CORRECTION_LEVEL = 2;
   static final int WHITE_SPACE = 30;

   private static BitMatrix bitMatrixFromEncoder(PDF417 var0, String var1, int var2, int var3, int var4, int var5) throws WriterException {
      var0.generateBarcodeLogic(var1, var2);
      byte[][] var6 = var0.getBarcodeMatrix().getScaledMatrix(1, 4);
      boolean var12 = false;
      boolean var7;
      if (var4 > var3) {
         var7 = true;
      } else {
         var7 = false;
      }

      boolean var8;
      if (var6[0].length < var6.length) {
         var8 = true;
      } else {
         var8 = false;
      }

      byte[][] var10 = var6;
      if (var7 ^ var8) {
         var10 = rotateArray(var6);
         var12 = true;
      }

      var3 /= var10[0].length;
      var4 /= var10.length;
      if (var3 >= var4) {
         var3 = var4;
      }

      BitMatrix var11;
      if (var3 > 1) {
         var10 = var0.getBarcodeMatrix().getScaledMatrix(var3, var3 << 2);
         byte[][] var9 = var10;
         if (var12) {
            var9 = rotateArray(var10);
         }

         var11 = bitMatrixFrombitArray(var9, var5);
      } else {
         var11 = bitMatrixFrombitArray(var10, var5);
      }

      return var11;
   }

   private static BitMatrix bitMatrixFrombitArray(byte[][] var0, int var1) {
      BitMatrix var2 = new BitMatrix(var0[0].length + var1 * 2, var0.length + var1 * 2);
      var2.clear();
      int var3 = 0;

      for(int var4 = var2.getHeight() - var1 - 1; var3 < var0.length; --var4) {
         for(int var5 = 0; var5 < var0[0].length; ++var5) {
            if (var0[var3][var5] == 1) {
               var2.set(var5 + var1, var4);
            }
         }

         ++var3;
      }

      return var2;
   }

   private static byte[][] rotateArray(byte[][] var0) {
      int var1 = var0[0].length;
      int var2 = var0.length;
      byte[][] var3 = new byte[var1][var2];

      for(var1 = 0; var1 < var0.length; ++var1) {
         int var4 = var0.length;

         for(var2 = 0; var2 < var0[0].length; ++var2) {
            var3[var2][var4 - var1 - 1] = (byte)var0[var1][var2];
         }
      }

      return var3;
   }

   public BitMatrix encode(String var1, BarcodeFormat var2, int var3, int var4) throws WriterException {
      return this.encode(var1, var2, var3, var4, (Map)null);
   }

   public BitMatrix encode(String var1, BarcodeFormat var2, int var3, int var4, Map var5) throws WriterException {
      if (var2 != BarcodeFormat.PDF_417) {
         throw new IllegalArgumentException("Can only encode PDF_417, but got " + var2);
      } else {
         PDF417 var11 = new PDF417();
         int var6 = 30;
         int var7 = 2;
         int var8 = var7;
         int var9 = var6;
         if (var5 != null) {
            if (var5.containsKey(EncodeHintType.PDF417_COMPACT)) {
               var11.setCompact(Boolean.valueOf(var5.get(EncodeHintType.PDF417_COMPACT).toString()));
            }

            if (var5.containsKey(EncodeHintType.PDF417_COMPACTION)) {
               var11.setCompaction(Compaction.valueOf(var5.get(EncodeHintType.PDF417_COMPACTION).toString()));
            }

            if (var5.containsKey(EncodeHintType.PDF417_DIMENSIONS)) {
               Dimensions var10 = (Dimensions)var5.get(EncodeHintType.PDF417_DIMENSIONS);
               var11.setDimensions(var10.getMaxCols(), var10.getMinCols(), var10.getMaxRows(), var10.getMinRows());
            }

            if (var5.containsKey(EncodeHintType.MARGIN)) {
               var6 = Integer.parseInt(var5.get(EncodeHintType.MARGIN).toString());
            }

            if (var5.containsKey(EncodeHintType.ERROR_CORRECTION)) {
               var7 = Integer.parseInt(var5.get(EncodeHintType.ERROR_CORRECTION).toString());
            }

            var8 = var7;
            var9 = var6;
            if (var5.containsKey(EncodeHintType.CHARACTER_SET)) {
               var11.setEncoding(Charset.forName(var5.get(EncodeHintType.CHARACTER_SET).toString()));
               var9 = var6;
               var8 = var7;
            }
         }

         return bitMatrixFromEncoder(var11, var1, var8, var3, var4, var9);
      }
   }
}
