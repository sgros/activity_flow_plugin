package com.google.zxing.datamatrix.encoder;

import com.google.zxing.Dimension;

public class SymbolInfo {
   static final SymbolInfo[] PROD_SYMBOLS;
   private static SymbolInfo[] symbols;
   private final int dataCapacity;
   private final int dataRegions;
   private final int errorCodewords;
   public final int matrixHeight;
   public final int matrixWidth;
   private final boolean rectangular;
   private final int rsBlockData;
   private final int rsBlockError;

   static {
      SymbolInfo[] var0 = new SymbolInfo[]{new SymbolInfo(false, 3, 5, 8, 8, 1), new SymbolInfo(false, 5, 7, 10, 10, 1), new SymbolInfo(true, 5, 7, 16, 6, 1), new SymbolInfo(false, 8, 10, 12, 12, 1), new SymbolInfo(true, 10, 11, 14, 6, 2), new SymbolInfo(false, 12, 12, 14, 14, 1), new SymbolInfo(true, 16, 14, 24, 10, 1), new SymbolInfo(false, 18, 14, 16, 16, 1), new SymbolInfo(false, 22, 18, 18, 18, 1), new SymbolInfo(true, 22, 18, 16, 10, 2), new SymbolInfo(false, 30, 20, 20, 20, 1), new SymbolInfo(true, 32, 24, 16, 14, 2), new SymbolInfo(false, 36, 24, 22, 22, 1), new SymbolInfo(false, 44, 28, 24, 24, 1), new SymbolInfo(true, 49, 28, 22, 14, 2), new SymbolInfo(false, 62, 36, 14, 14, 4), new SymbolInfo(false, 86, 42, 16, 16, 4), new SymbolInfo(false, 114, 48, 18, 18, 4), new SymbolInfo(false, 144, 56, 20, 20, 4), new SymbolInfo(false, 174, 68, 22, 22, 4), new SymbolInfo(false, 204, 84, 24, 24, 4, 102, 42), new SymbolInfo(false, 280, 112, 14, 14, 16, 140, 56), new SymbolInfo(false, 368, 144, 16, 16, 16, 92, 36), new SymbolInfo(false, 456, 192, 18, 18, 16, 114, 48), new SymbolInfo(false, 576, 224, 20, 20, 16, 144, 56), new SymbolInfo(false, 696, 272, 22, 22, 16, 174, 68), new SymbolInfo(false, 816, 336, 24, 24, 16, 136, 56), new SymbolInfo(false, 1050, 408, 18, 18, 36, 175, 68), new SymbolInfo(false, 1304, 496, 20, 20, 36, 163, 62), new DataMatrixSymbolInfo144()};
      PROD_SYMBOLS = var0;
      symbols = var0;
   }

   public SymbolInfo(boolean var1, int var2, int var3, int var4, int var5, int var6) {
      this(var1, var2, var3, var4, var5, var6, var2, var3);
   }

   SymbolInfo(boolean var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      this.rectangular = var1;
      this.dataCapacity = var2;
      this.errorCodewords = var3;
      this.matrixWidth = var4;
      this.matrixHeight = var5;
      this.dataRegions = var6;
      this.rsBlockData = var7;
      this.rsBlockError = var8;
   }

   private int getHorizontalDataRegions() {
      byte var1;
      switch(this.dataRegions) {
      case 1:
         var1 = 1;
         break;
      case 2:
      case 4:
         var1 = 2;
         break;
      case 16:
         var1 = 4;
         break;
      case 36:
         var1 = 6;
         break;
      default:
         throw new IllegalStateException("Cannot handle this number of data regions");
      }

      return var1;
   }

   private int getVerticalDataRegions() {
      byte var1;
      switch(this.dataRegions) {
      case 1:
      case 2:
         var1 = 1;
         break;
      case 4:
         var1 = 2;
         break;
      case 16:
         var1 = 4;
         break;
      case 36:
         var1 = 6;
         break;
      default:
         throw new IllegalStateException("Cannot handle this number of data regions");
      }

      return var1;
   }

   public static SymbolInfo lookup(int var0) {
      return lookup(var0, SymbolShapeHint.FORCE_NONE, true);
   }

   public static SymbolInfo lookup(int var0, SymbolShapeHint var1) {
      return lookup(var0, var1, true);
   }

   public static SymbolInfo lookup(int var0, SymbolShapeHint var1, Dimension var2, Dimension var3, boolean var4) {
      SymbolInfo[] var5 = symbols;
      int var6 = var5.length;
      int var7 = 0;

      SymbolInfo var9;
      while(true) {
         if (var7 >= var6) {
            if (var4) {
               throw new IllegalArgumentException("Can't find a symbol arrangement that matches the message. Data codewords: " + var0);
            }

            var9 = null;
            break;
         }

         SymbolInfo var8 = var5[var7];
         if ((var1 != SymbolShapeHint.FORCE_SQUARE || !var8.rectangular) && (var1 != SymbolShapeHint.FORCE_RECTANGLE || var8.rectangular) && (var2 == null || var8.getSymbolWidth() >= var2.getWidth() && var8.getSymbolHeight() >= var2.getHeight()) && (var3 == null || var8.getSymbolWidth() <= var3.getWidth() && var8.getSymbolHeight() <= var3.getHeight()) && var0 <= var8.dataCapacity) {
            var9 = var8;
            break;
         }

         ++var7;
      }

      return var9;
   }

   private static SymbolInfo lookup(int var0, SymbolShapeHint var1, boolean var2) {
      return lookup(var0, var1, (Dimension)null, (Dimension)null, var2);
   }

   public static SymbolInfo lookup(int var0, boolean var1, boolean var2) {
      SymbolShapeHint var3;
      if (var1) {
         var3 = SymbolShapeHint.FORCE_NONE;
      } else {
         var3 = SymbolShapeHint.FORCE_SQUARE;
      }

      return lookup(var0, var3, var2);
   }

   public static void overrideSymbolSet(SymbolInfo[] var0) {
      symbols = var0;
   }

   public int getCodewordCount() {
      return this.dataCapacity + this.errorCodewords;
   }

   public final int getDataCapacity() {
      return this.dataCapacity;
   }

   public int getDataLengthForInterleavedBlock(int var1) {
      return this.rsBlockData;
   }

   public final int getErrorCodewords() {
      return this.errorCodewords;
   }

   public final int getErrorLengthForInterleavedBlock(int var1) {
      return this.rsBlockError;
   }

   public int getInterleavedBlockCount() {
      return this.dataCapacity / this.rsBlockData;
   }

   public final int getSymbolDataHeight() {
      return this.getVerticalDataRegions() * this.matrixHeight;
   }

   public final int getSymbolDataWidth() {
      return this.getHorizontalDataRegions() * this.matrixWidth;
   }

   public final int getSymbolHeight() {
      return this.getSymbolDataHeight() + (this.getVerticalDataRegions() << 1);
   }

   public final int getSymbolWidth() {
      return this.getSymbolDataWidth() + (this.getHorizontalDataRegions() << 1);
   }

   public final String toString() {
      StringBuilder var1 = new StringBuilder();
      String var2;
      if (this.rectangular) {
         var2 = "Rectangular Symbol:";
      } else {
         var2 = "Square Symbol:";
      }

      return var1.append(var2).append(" data region ").append(this.matrixWidth).append('x').append(this.matrixHeight).append(", symbol size ").append(this.getSymbolWidth()).append('x').append(this.getSymbolHeight()).append(", symbol data size ").append(this.getSymbolDataWidth()).append('x').append(this.getSymbolDataHeight()).append(", codewords ").append(this.dataCapacity).append('+').append(this.errorCodewords).toString();
   }
}
