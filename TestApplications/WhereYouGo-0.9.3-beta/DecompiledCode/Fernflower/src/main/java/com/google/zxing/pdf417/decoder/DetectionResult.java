package com.google.zxing.pdf417.decoder;

import java.util.Formatter;

final class DetectionResult {
   private static final int ADJUST_ROW_NUMBER_SKIP = 2;
   private final int barcodeColumnCount;
   private final BarcodeMetadata barcodeMetadata;
   private BoundingBox boundingBox;
   private final DetectionResultColumn[] detectionResultColumns;

   DetectionResult(BarcodeMetadata var1, BoundingBox var2) {
      this.barcodeMetadata = var1;
      this.barcodeColumnCount = var1.getColumnCount();
      this.boundingBox = var2;
      this.detectionResultColumns = new DetectionResultColumn[this.barcodeColumnCount + 2];
   }

   private void adjustIndicatorColumnRowNumbers(DetectionResultColumn var1) {
      if (var1 != null) {
         ((DetectionResultRowIndicatorColumn)var1).adjustCompleteIndicatorColumnRowNumbers(this.barcodeMetadata);
      }

   }

   private static boolean adjustRowNumber(Codeword var0, Codeword var1) {
      boolean var2 = false;
      boolean var3;
      if (var1 == null) {
         var3 = var2;
      } else {
         var3 = var2;
         if (var1.hasValidRowNumber()) {
            var3 = var2;
            if (var1.getBucket() == var0.getBucket()) {
               var0.setRowNumber(var1.getRowNumber());
               var3 = true;
            }
         }
      }

      return var3;
   }

   private static int adjustRowNumberIfValid(int var0, int var1, Codeword var2) {
      if (var2 != null) {
         int var3 = var1;
         if (!var2.hasValidRowNumber()) {
            if (var2.isValidRowNumber(var0)) {
               var2.setRowNumber(var0);
               var3 = 0;
            } else {
               var3 = var1 + 1;
            }
         }

         var1 = var3;
      }

      return var1;
   }

   private int adjustRowNumbers() {
      int var1 = this.adjustRowNumbersByRow();
      int var2;
      if (var1 == 0) {
         var2 = 0;
      } else {
         int var3 = 1;

         while(true) {
            var2 = var1;
            if (var3 >= this.barcodeColumnCount + 1) {
               break;
            }

            Codeword[] var4 = this.detectionResultColumns[var3].getCodewords();

            for(var2 = 0; var2 < var4.length; ++var2) {
               if (var4[var2] != null && !var4[var2].hasValidRowNumber()) {
                  this.adjustRowNumbers(var3, var2, var4);
               }
            }

            ++var3;
         }
      }

      return var2;
   }

   private void adjustRowNumbers(int var1, int var2, Codeword[] var3) {
      byte var4 = 0;
      Codeword var5 = var3[var2];
      Codeword[] var6 = this.detectionResultColumns[var1 - 1].getCodewords();
      Codeword[] var7 = var6;
      if (this.detectionResultColumns[var1 + 1] != null) {
         var7 = this.detectionResultColumns[var1 + 1].getCodewords();
      }

      Codeword[] var8 = new Codeword[14];
      var8[2] = var6[var2];
      var8[3] = var7[var2];
      if (var2 > 0) {
         var8[0] = var3[var2 - 1];
         var8[4] = var6[var2 - 1];
         var8[5] = var7[var2 - 1];
      }

      if (var2 > 1) {
         var8[8] = var3[var2 - 2];
         var8[10] = var6[var2 - 2];
         var8[11] = var7[var2 - 2];
      }

      if (var2 < var3.length - 1) {
         var8[1] = var3[var2 + 1];
         var8[6] = var6[var2 + 1];
         var8[7] = var7[var2 + 1];
      }

      var1 = var4;
      if (var2 < var3.length - 2) {
         var8[9] = var3[var2 + 2];
         var8[12] = var6[var2 + 2];
         var8[13] = var7[var2 + 2];
         var1 = var4;
      }

      while(var1 < 14 && !adjustRowNumber(var5, var8[var1])) {
         ++var1;
      }

   }

   private int adjustRowNumbersByRow() {
      this.adjustRowNumbersFromBothRI();
      return this.adjustRowNumbersFromLRI() + this.adjustRowNumbersFromRRI();
   }

   private void adjustRowNumbersFromBothRI() {
      if (this.detectionResultColumns[0] != null && this.detectionResultColumns[this.barcodeColumnCount + 1] != null) {
         Codeword[] var1 = this.detectionResultColumns[0].getCodewords();
         Codeword[] var2 = this.detectionResultColumns[this.barcodeColumnCount + 1].getCodewords();

         for(int var3 = 0; var3 < var1.length; ++var3) {
            if (var1[var3] != null && var2[var3] != null && var1[var3].getRowNumber() == var2[var3].getRowNumber()) {
               for(int var4 = 1; var4 <= this.barcodeColumnCount; ++var4) {
                  Codeword var5 = this.detectionResultColumns[var4].getCodewords()[var3];
                  if (var5 != null) {
                     var5.setRowNumber(var1[var3].getRowNumber());
                     if (!var5.hasValidRowNumber()) {
                        this.detectionResultColumns[var4].getCodewords()[var3] = null;
                     }
                  }
               }
            }
         }
      }

   }

   private int adjustRowNumbersFromLRI() {
      int var1;
      if (this.detectionResultColumns[0] == null) {
         var1 = 0;
      } else {
         int var2 = 0;
         Codeword[] var3 = this.detectionResultColumns[0].getCodewords();
         int var4 = 0;

         while(true) {
            var1 = var2;
            if (var4 >= var3.length) {
               break;
            }

            int var5 = var2;
            if (var3[var4] != null) {
               int var6 = var3[var4].getRowNumber();
               int var7 = 0;
               var1 = 1;

               while(true) {
                  var5 = var2;
                  if (var1 >= this.barcodeColumnCount + 1) {
                     break;
                  }

                  var5 = var2;
                  if (var7 >= 2) {
                     break;
                  }

                  Codeword var8 = this.detectionResultColumns[var1].getCodewords()[var4];
                  var5 = var7;
                  int var9 = var2;
                  if (var8 != null) {
                     var7 = adjustRowNumberIfValid(var6, var7, var8);
                     var5 = var7;
                     var9 = var2;
                     if (!var8.hasValidRowNumber()) {
                        var9 = var2 + 1;
                        var5 = var7;
                     }
                  }

                  ++var1;
                  var7 = var5;
                  var2 = var9;
               }
            }

            ++var4;
            var2 = var5;
         }
      }

      return var1;
   }

   private int adjustRowNumbersFromRRI() {
      int var1;
      if (this.detectionResultColumns[this.barcodeColumnCount + 1] == null) {
         var1 = 0;
      } else {
         int var2 = 0;
         Codeword[] var3 = this.detectionResultColumns[this.barcodeColumnCount + 1].getCodewords();
         int var4 = 0;

         while(true) {
            var1 = var2;
            if (var4 >= var3.length) {
               break;
            }

            int var5 = var2;
            if (var3[var4] != null) {
               int var6 = var3[var4].getRowNumber();
               int var7 = 0;
               var1 = this.barcodeColumnCount + 1;

               while(true) {
                  var5 = var2;
                  if (var1 <= 0) {
                     break;
                  }

                  var5 = var2;
                  if (var7 >= 2) {
                     break;
                  }

                  Codeword var8 = this.detectionResultColumns[var1].getCodewords()[var4];
                  int var9 = var7;
                  var5 = var2;
                  if (var8 != null) {
                     var7 = adjustRowNumberIfValid(var6, var7, var8);
                     var9 = var7;
                     var5 = var2;
                     if (!var8.hasValidRowNumber()) {
                        var5 = var2 + 1;
                        var9 = var7;
                     }
                  }

                  --var1;
                  var7 = var9;
                  var2 = var5;
               }
            }

            ++var4;
            var2 = var5;
         }
      }

      return var1;
   }

   int getBarcodeColumnCount() {
      return this.barcodeColumnCount;
   }

   int getBarcodeECLevel() {
      return this.barcodeMetadata.getErrorCorrectionLevel();
   }

   int getBarcodeRowCount() {
      return this.barcodeMetadata.getRowCount();
   }

   BoundingBox getBoundingBox() {
      return this.boundingBox;
   }

   DetectionResultColumn getDetectionResultColumn(int var1) {
      return this.detectionResultColumns[var1];
   }

   DetectionResultColumn[] getDetectionResultColumns() {
      this.adjustIndicatorColumnRowNumbers(this.detectionResultColumns[0]);
      this.adjustIndicatorColumnRowNumbers(this.detectionResultColumns[this.barcodeColumnCount + 1]);
      int var1 = 928;

      int var2;
      int var3;
      do {
         var2 = var1;
         var3 = this.adjustRowNumbers();
         if (var3 <= 0) {
            break;
         }

         var1 = var3;
      } while(var3 < var2);

      return this.detectionResultColumns;
   }

   public void setBoundingBox(BoundingBox var1) {
      this.boundingBox = var1;
   }

   void setDetectionResultColumn(int var1, DetectionResultColumn var2) {
      this.detectionResultColumns[var1] = var2;
   }

   public String toString() {
      DetectionResultColumn var1 = this.detectionResultColumns[0];
      DetectionResultColumn var2 = var1;
      if (var1 == null) {
         var2 = this.detectionResultColumns[this.barcodeColumnCount + 1];
      }

      Formatter var6 = new Formatter();

      for(int var3 = 0; var3 < var2.getCodewords().length; ++var3) {
         var6.format("CW %3d:", var3);

         for(int var4 = 0; var4 < this.barcodeColumnCount + 2; ++var4) {
            if (this.detectionResultColumns[var4] == null) {
               var6.format("    |   ");
            } else {
               Codeword var5 = this.detectionResultColumns[var4].getCodewords()[var3];
               if (var5 == null) {
                  var6.format("    |   ");
               } else {
                  var6.format(" %3d|%3d", var5.getRowNumber(), var5.getValue());
               }
            }
         }

         var6.format("%n");
      }

      String var7 = var6.toString();
      var6.close();
      return var7;
   }
}
