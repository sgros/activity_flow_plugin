package com.google.zxing.pdf417.encoder;

public final class Dimensions {
   private final int maxCols;
   private final int maxRows;
   private final int minCols;
   private final int minRows;

   public Dimensions(int var1, int var2, int var3, int var4) {
      this.minCols = var1;
      this.maxCols = var2;
      this.minRows = var3;
      this.maxRows = var4;
   }

   public int getMaxCols() {
      return this.maxCols;
   }

   public int getMaxRows() {
      return this.maxRows;
   }

   public int getMinCols() {
      return this.minCols;
   }

   public int getMinRows() {
      return this.minRows;
   }
}
