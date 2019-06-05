package com.google.zxing.pdf417.decoder;

final class BarcodeMetadata {
   private final int columnCount;
   private final int errorCorrectionLevel;
   private final int rowCount;
   private final int rowCountLowerPart;
   private final int rowCountUpperPart;

   BarcodeMetadata(int var1, int var2, int var3, int var4) {
      this.columnCount = var1;
      this.errorCorrectionLevel = var4;
      this.rowCountUpperPart = var2;
      this.rowCountLowerPart = var3;
      this.rowCount = var2 + var3;
   }

   int getColumnCount() {
      return this.columnCount;
   }

   int getErrorCorrectionLevel() {
      return this.errorCorrectionLevel;
   }

   int getRowCount() {
      return this.rowCount;
   }

   int getRowCountLowerPart() {
      return this.rowCountLowerPart;
   }

   int getRowCountUpperPart() {
      return this.rowCountUpperPart;
   }
}
