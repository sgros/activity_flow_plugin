package com.google.zxing.aztec.encoder;

import com.google.zxing.common.BitMatrix;

public final class AztecCode {
   private int codeWords;
   private boolean compact;
   private int layers;
   private BitMatrix matrix;
   private int size;

   public int getCodeWords() {
      return this.codeWords;
   }

   public int getLayers() {
      return this.layers;
   }

   public BitMatrix getMatrix() {
      return this.matrix;
   }

   public int getSize() {
      return this.size;
   }

   public boolean isCompact() {
      return this.compact;
   }

   public void setCodeWords(int var1) {
      this.codeWords = var1;
   }

   public void setCompact(boolean var1) {
      this.compact = var1;
   }

   public void setLayers(int var1) {
      this.layers = var1;
   }

   public void setMatrix(BitMatrix var1) {
      this.matrix = var1;
   }

   public void setSize(int var1) {
      this.size = var1;
   }
}
