package com.google.zxing.pdf417.decoder;

final class Codeword {
   private static final int BARCODE_ROW_UNKNOWN = -1;
   private final int bucket;
   private final int endX;
   private int rowNumber = -1;
   private final int startX;
   private final int value;

   Codeword(int var1, int var2, int var3, int var4) {
      this.startX = var1;
      this.endX = var2;
      this.bucket = var3;
      this.value = var4;
   }

   int getBucket() {
      return this.bucket;
   }

   int getEndX() {
      return this.endX;
   }

   int getRowNumber() {
      return this.rowNumber;
   }

   int getStartX() {
      return this.startX;
   }

   int getValue() {
      return this.value;
   }

   int getWidth() {
      return this.endX - this.startX;
   }

   boolean hasValidRowNumber() {
      return this.isValidRowNumber(this.rowNumber);
   }

   boolean isValidRowNumber(int var1) {
      boolean var2;
      if (var1 != -1 && this.bucket == var1 % 3 * 3) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   void setRowNumber(int var1) {
      this.rowNumber = var1;
   }

   void setRowNumberAsRowIndicatorColumn() {
      this.rowNumber = this.value / 30 * 3 + this.bucket / 3;
   }

   public String toString() {
      return this.rowNumber + "|" + this.value;
   }
}
