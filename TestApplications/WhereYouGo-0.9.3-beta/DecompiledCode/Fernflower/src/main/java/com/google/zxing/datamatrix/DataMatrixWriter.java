package com.google.zxing.datamatrix;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Dimension;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.datamatrix.encoder.DefaultPlacement;
import com.google.zxing.datamatrix.encoder.ErrorCorrection;
import com.google.zxing.datamatrix.encoder.HighLevelEncoder;
import com.google.zxing.datamatrix.encoder.SymbolInfo;
import com.google.zxing.datamatrix.encoder.SymbolShapeHint;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import java.util.Map;

public final class DataMatrixWriter implements Writer {
   private static BitMatrix convertByteMatrixToBitMatrix(ByteMatrix var0) {
      int var1 = var0.getWidth();
      int var2 = var0.getHeight();
      BitMatrix var3 = new BitMatrix(var1, var2);
      var3.clear();

      for(int var4 = 0; var4 < var1; ++var4) {
         for(int var5 = 0; var5 < var2; ++var5) {
            if (var0.get(var4, var5) == 1) {
               var3.set(var4, var5);
            }
         }
      }

      return var3;
   }

   private static BitMatrix encodeLowLevel(DefaultPlacement var0, SymbolInfo var1) {
      int var2 = var1.getSymbolDataWidth();
      int var3 = var1.getSymbolDataHeight();
      ByteMatrix var4 = new ByteMatrix(var1.getSymbolWidth(), var1.getSymbolHeight());
      int var5 = 0;

      for(int var6 = 0; var6 < var3; ++var6) {
         int var7 = var5;
         int var8;
         boolean var9;
         if (var6 % var1.matrixHeight == 0) {
            var8 = 0;

            for(var7 = 0; var7 < var1.getSymbolWidth(); ++var7) {
               if (var7 % 2 == 0) {
                  var9 = true;
               } else {
                  var9 = false;
               }

               var4.set(var8, var5, var9);
               ++var8;
            }

            var7 = var5 + 1;
         }

         var5 = 0;

         for(var8 = 0; var8 < var2; ++var8) {
            int var10 = var5;
            if (var8 % var1.matrixWidth == 0) {
               var4.set(var5, var7, true);
               var10 = var5 + 1;
            }

            var4.set(var10, var7, var0.getBit(var8, var6));
            ++var10;
            var5 = var10;
            if (var8 % var1.matrixWidth == var1.matrixWidth - 1) {
               if (var6 % 2 == 0) {
                  var9 = true;
               } else {
                  var9 = false;
               }

               var4.set(var10, var7, var9);
               var5 = var10 + 1;
            }
         }

         var8 = var7 + 1;
         var5 = var8;
         if (var6 % var1.matrixHeight == var1.matrixHeight - 1) {
            var7 = 0;

            for(var5 = 0; var5 < var1.getSymbolWidth(); ++var5) {
               var4.set(var7, var8, true);
               ++var7;
            }

            var5 = var8 + 1;
         }
      }

      return convertByteMatrixToBitMatrix(var4);
   }

   public BitMatrix encode(String var1, BarcodeFormat var2, int var3, int var4) {
      return this.encode(var1, var2, var3, var4, (Map)null);
   }

   public BitMatrix encode(String var1, BarcodeFormat var2, int var3, int var4, Map var5) {
      if (var1.isEmpty()) {
         throw new IllegalArgumentException("Found empty contents");
      } else if (var2 != BarcodeFormat.DATA_MATRIX) {
         throw new IllegalArgumentException("Can only encode DATA_MATRIX, but got " + var2);
      } else if (var3 >= 0 && var4 >= 0) {
         SymbolShapeHint var12 = SymbolShapeHint.FORCE_NONE;
         Dimension var6 = null;
         Dimension var7 = null;
         Object var8 = null;
         Dimension var9 = (Dimension)var8;
         SymbolShapeHint var10 = var12;
         if (var5 != null) {
            SymbolShapeHint var15 = (SymbolShapeHint)var5.get(EncodeHintType.DATA_MATRIX_SHAPE);
            if (var15 != null) {
               var12 = var15;
            }

            var9 = (Dimension)var5.get(EncodeHintType.MIN_SIZE);
            if (var9 != null) {
               var7 = var9;
            }

            Dimension var14 = (Dimension)var5.get(EncodeHintType.MAX_SIZE);
            var9 = (Dimension)var8;
            var6 = var7;
            var10 = var12;
            if (var14 != null) {
               var9 = var14;
               var10 = var12;
               var6 = var7;
            }
         }

         var1 = HighLevelEncoder.encodeHighLevel(var1, var10, var6, var9);
         SymbolInfo var13 = SymbolInfo.lookup(var1.length(), var10, var6, var9, true);
         DefaultPlacement var11 = new DefaultPlacement(ErrorCorrection.encodeECC200(var1, var13), var13.getSymbolDataWidth(), var13.getSymbolDataHeight());
         var11.place();
         return encodeLowLevel(var11, var13);
      } else {
         throw new IllegalArgumentException("Requested dimensions are too small: " + var3 + 'x' + var4);
      }
   }
}
