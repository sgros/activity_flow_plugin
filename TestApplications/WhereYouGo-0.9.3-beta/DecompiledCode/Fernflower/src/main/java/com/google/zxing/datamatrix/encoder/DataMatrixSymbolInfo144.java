package com.google.zxing.datamatrix.encoder;

final class DataMatrixSymbolInfo144 extends SymbolInfo {
   DataMatrixSymbolInfo144() {
      super(false, 1558, 620, 22, 22, 36, -1, 62);
   }

   public int getDataLengthForInterleavedBlock(int var1) {
      short var2;
      if (var1 <= 8) {
         var2 = 156;
      } else {
         var2 = 155;
      }

      return var2;
   }

   public int getInterleavedBlockCount() {
      return 10;
   }
}
