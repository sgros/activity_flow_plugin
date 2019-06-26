package com.google.zxing.pdf417.encoder;

public final class BarcodeMatrix {
   private int currentRow;
   private final int height;
   private final BarcodeRow[] matrix;
   private final int width;

   BarcodeMatrix(int var1, int var2) {
      this.matrix = new BarcodeRow[var1];
      int var3 = 0;

      for(int var4 = this.matrix.length; var3 < var4; ++var3) {
         this.matrix[var3] = new BarcodeRow((var2 + 4) * 17 + 1);
      }

      this.width = var2 * 17;
      this.height = var1;
      this.currentRow = -1;
   }

   BarcodeRow getCurrentRow() {
      return this.matrix[this.currentRow];
   }

   public byte[][] getMatrix() {
      return this.getScaledMatrix(1, 1);
   }

   public byte[][] getScaledMatrix(int var1, int var2) {
      int var3 = this.height;
      int var4 = this.width;
      byte[][] var5 = new byte[var3 * var2][var4 * var1];
      var3 = this.height * var2;

      for(var4 = 0; var4 < var3; ++var4) {
         var5[var3 - var4 - 1] = this.matrix[var4 / var2].getScaledRow(var1);
      }

      return var5;
   }

   void set(int var1, int var2, byte var3) {
      this.matrix[var2].set(var1, var3);
   }

   void startRow() {
      ++this.currentRow;
   }
}
