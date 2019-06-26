package com.google.zxing.pdf417.decoder;

import java.util.Formatter;

class DetectionResultColumn {
   private static final int MAX_NEARBY_DISTANCE = 5;
   private final BoundingBox boundingBox;
   private final Codeword[] codewords;

   DetectionResultColumn(BoundingBox var1) {
      this.boundingBox = new BoundingBox(var1);
      this.codewords = new Codeword[var1.getMaxY() - var1.getMinY() + 1];
   }

   final BoundingBox getBoundingBox() {
      return this.boundingBox;
   }

   final Codeword getCodeword(int var1) {
      return this.codewords[this.imageRowToCodewordIndex(var1)];
   }

   final Codeword getCodewordNearby(int var1) {
      Codeword var2 = this.getCodeword(var1);
      if (var2 == null) {
         int var3 = 1;

         while(true) {
            if (var3 >= 5) {
               var2 = null;
               break;
            }

            int var4 = this.imageRowToCodewordIndex(var1) - var3;
            if (var4 >= 0) {
               var2 = this.codewords[var4];
               if (var2 != null) {
                  break;
               }
            }

            var4 = this.imageRowToCodewordIndex(var1) + var3;
            if (var4 < this.codewords.length) {
               var2 = this.codewords[var4];
               if (var2 != null) {
                  break;
               }
            }

            ++var3;
         }
      }

      return var2;
   }

   final Codeword[] getCodewords() {
      return this.codewords;
   }

   final int imageRowToCodewordIndex(int var1) {
      return var1 - this.boundingBox.getMinY();
   }

   final void setCodeword(int var1, Codeword var2) {
      this.codewords[this.imageRowToCodewordIndex(var1)] = var2;
   }

   public String toString() {
      Formatter var1 = new Formatter();
      Codeword[] var2 = this.codewords;
      int var3 = var2.length;
      int var4 = 0;

      for(int var5 = 0; var4 < var3; ++var4) {
         Codeword var6 = var2[var4];
         int var7;
         if (var6 == null) {
            var7 = var5 + 1;
            var1.format("%3d:    |   %n", var5);
            var5 = var7;
         } else {
            var7 = var5 + 1;
            var1.format("%3d: %3d|%3d%n", var5, var6.getRowNumber(), var6.getValue());
            var5 = var7;
         }
      }

      String var8 = var1.toString();
      var1.close();
      return var8;
   }
}
