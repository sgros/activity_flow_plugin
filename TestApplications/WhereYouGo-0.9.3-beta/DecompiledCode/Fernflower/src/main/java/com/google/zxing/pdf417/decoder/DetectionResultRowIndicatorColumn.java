package com.google.zxing.pdf417.decoder;

import com.google.zxing.ResultPoint;

final class DetectionResultRowIndicatorColumn extends DetectionResultColumn {
   private final boolean isLeft;

   DetectionResultRowIndicatorColumn(BoundingBox var1, boolean var2) {
      super(var1);
      this.isLeft = var2;
   }

   private void adjustIncompleteIndicatorColumnRowNumbers(BarcodeMetadata var1) {
      BoundingBox var2 = this.getBoundingBox();
      ResultPoint var3;
      if (this.isLeft) {
         var3 = var2.getTopLeft();
      } else {
         var3 = var2.getTopRight();
      }

      ResultPoint var12;
      if (this.isLeft) {
         var12 = var2.getBottomLeft();
      } else {
         var12 = var2.getBottomRight();
      }

      int var4 = this.imageRowToCodewordIndex((int)var3.getY());
      int var5 = this.imageRowToCodewordIndex((int)var12.getY());
      Codeword[] var14 = this.getCodewords();
      int var6 = -1;
      int var7 = 1;

      int var11;
      for(int var8 = 0; var4 < var5; var7 = var11) {
         int var9 = var6;
         int var10 = var8;
         var11 = var7;
         if (var14[var4] != null) {
            Codeword var13 = var14[var4];
            var13.setRowNumberAsRowIndicatorColumn();
            var10 = var13.getRowNumber() - var6;
            if (var10 == 0) {
               var10 = var8 + 1;
               var11 = var7;
               var9 = var6;
            } else if (var10 == 1) {
               var11 = Math.max(var7, var8);
               var10 = 1;
               var9 = var13.getRowNumber();
            } else if (var13.getRowNumber() >= var1.getRowCount()) {
               var14[var4] = null;
               var9 = var6;
               var10 = var8;
               var11 = var7;
            } else {
               var9 = var13.getRowNumber();
               var10 = 1;
               var11 = var7;
            }
         }

         ++var4;
         var6 = var9;
         var8 = var10;
      }

   }

   private void removeIncorrectCodewords(Codeword[] var1, BarcodeMetadata var2) {
      for(int var3 = 0; var3 < var1.length; ++var3) {
         Codeword var4 = var1[var3];
         if (var1[var3] != null) {
            int var5 = var4.getValue() % 30;
            int var6 = var4.getRowNumber();
            if (var6 > var2.getRowCount()) {
               var1[var3] = null;
            } else {
               int var7 = var6;
               if (!this.isLeft) {
                  var7 = var6 + 2;
               }

               switch(var7 % 3) {
               case 0:
                  if (var5 * 3 + 1 != var2.getRowCountUpperPart()) {
                     var1[var3] = null;
                  }
                  break;
               case 1:
                  if (var5 / 3 != var2.getErrorCorrectionLevel() || var5 % 3 != var2.getRowCountLowerPart()) {
                     var1[var3] = null;
                  }
                  break;
               case 2:
                  if (var5 + 1 != var2.getColumnCount()) {
                     var1[var3] = null;
                  }
               }
            }
         }
      }

   }

   private void setRowNumbers() {
      Codeword[] var1 = this.getCodewords();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Codeword var4 = var1[var3];
         if (var4 != null) {
            var4.setRowNumberAsRowIndicatorColumn();
         }
      }

   }

   void adjustCompleteIndicatorColumnRowNumbers(BarcodeMetadata var1) {
      Codeword[] var2 = this.getCodewords();
      this.setRowNumbers();
      this.removeIncorrectCodewords(var2, var1);
      BoundingBox var3 = this.getBoundingBox();
      ResultPoint var4;
      if (this.isLeft) {
         var4 = var3.getTopLeft();
      } else {
         var4 = var3.getTopRight();
      }

      ResultPoint var13;
      if (this.isLeft) {
         var13 = var3.getBottomLeft();
      } else {
         var13 = var3.getBottomRight();
      }

      int var5 = this.imageRowToCodewordIndex((int)var4.getY());
      int var6 = this.imageRowToCodewordIndex((int)var13.getY());
      int var7 = -1;
      int var8 = 1;

      int var12;
      for(int var9 = 0; var5 < var6; var8 = var12) {
         int var10 = var7;
         int var11 = var9;
         var12 = var8;
         if (var2[var5] != null) {
            Codeword var14 = var2[var5];
            var10 = var14.getRowNumber() - var7;
            if (var10 == 0) {
               var11 = var9 + 1;
               var12 = var8;
               var10 = var7;
            } else if (var10 == 1) {
               var12 = Math.max(var8, var9);
               var11 = 1;
               var10 = var14.getRowNumber();
            } else if (var10 >= 0 && var14.getRowNumber() < var1.getRowCount() && var10 <= var5) {
               if (var8 > 2) {
                  var10 = (var8 - 2) * var10;
               }

               boolean var15;
               if (var10 >= var5) {
                  var15 = true;
               } else {
                  var15 = false;
               }

               for(var12 = 1; var12 <= var10 && !var15; ++var12) {
                  if (var2[var5 - var12] != null) {
                     var15 = true;
                  } else {
                     var15 = false;
                  }
               }

               if (var15) {
                  var2[var5] = null;
                  var10 = var7;
                  var11 = var9;
                  var12 = var8;
               } else {
                  var10 = var14.getRowNumber();
                  var11 = 1;
                  var12 = var8;
               }
            } else {
               var2[var5] = null;
               var10 = var7;
               var11 = var9;
               var12 = var8;
            }
         }

         ++var5;
         var7 = var10;
         var9 = var11;
      }

   }

   BarcodeMetadata getBarcodeMetadata() {
      Codeword[] var1 = this.getCodewords();
      BarcodeValue var2 = new BarcodeValue();
      BarcodeValue var3 = new BarcodeValue();
      BarcodeValue var4 = new BarcodeValue();
      BarcodeValue var5 = new BarcodeValue();
      int var6 = var1.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         Codeword var8 = var1[var7];
         if (var8 != null) {
            var8.setRowNumberAsRowIndicatorColumn();
            int var9 = var8.getValue() % 30;
            int var10 = var8.getRowNumber();
            int var11 = var10;
            if (!this.isLeft) {
               var11 = var10 + 2;
            }

            switch(var11 % 3) {
            case 0:
               var3.setValue(var9 * 3 + 1);
               break;
            case 1:
               var5.setValue(var9 / 3);
               var4.setValue(var9 % 3);
               break;
            case 2:
               var2.setValue(var9 + 1);
            }
         }
      }

      BarcodeMetadata var12;
      if (var2.getValue().length != 0 && var3.getValue().length != 0 && var4.getValue().length != 0 && var5.getValue().length != 0 && var2.getValue()[0] > 0 && var3.getValue()[0] + var4.getValue()[0] >= 3 && var3.getValue()[0] + var4.getValue()[0] <= 90) {
         var12 = new BarcodeMetadata(var2.getValue()[0], var3.getValue()[0], var4.getValue()[0], var5.getValue()[0]);
         this.removeIncorrectCodewords(var1, var12);
      } else {
         var12 = null;
      }

      return var12;
   }

   int[] getRowHeights() {
      BarcodeMetadata var1 = this.getBarcodeMetadata();
      int[] var7;
      if (var1 == null) {
         var7 = null;
      } else {
         this.adjustIncompleteIndicatorColumnRowNumbers(var1);
         int[] var2 = new int[var1.getRowCount()];
         Codeword[] var3 = this.getCodewords();
         int var4 = var3.length;
         int var5 = 0;

         while(true) {
            var7 = var2;
            if (var5 >= var4) {
               break;
            }

            Codeword var8 = var3[var5];
            if (var8 != null) {
               int var6 = var8.getRowNumber();
               if (var6 < var2.length) {
                  int var10002 = var2[var6]++;
               }
            }

            ++var5;
         }
      }

      return var7;
   }

   boolean isLeft() {
      return this.isLeft;
   }

   public String toString() {
      return "IsLeft: " + this.isLeft + '\n' + super.toString();
   }
}
