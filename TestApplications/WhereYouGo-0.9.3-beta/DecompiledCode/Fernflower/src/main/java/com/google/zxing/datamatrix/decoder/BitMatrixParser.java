package com.google.zxing.datamatrix.decoder;

import com.google.zxing.FormatException;
import com.google.zxing.common.BitMatrix;

final class BitMatrixParser {
   private final BitMatrix mappingBitMatrix;
   private final BitMatrix readMappingMatrix;
   private final Version version;

   BitMatrixParser(BitMatrix var1) throws FormatException {
      int var2 = var1.getHeight();
      if (var2 >= 8 && var2 <= 144 && (var2 & 1) == 0) {
         this.version = readVersion(var1);
         this.mappingBitMatrix = this.extractDataRegion(var1);
         this.readMappingMatrix = new BitMatrix(this.mappingBitMatrix.getWidth(), this.mappingBitMatrix.getHeight());
      } else {
         throw FormatException.getFormatInstance();
      }
   }

   private BitMatrix extractDataRegion(BitMatrix var1) {
      int var2 = this.version.getSymbolSizeRows();
      int var3 = this.version.getSymbolSizeColumns();
      if (var1.getHeight() != var2) {
         throw new IllegalArgumentException("Dimension of bitMarix must match the version size");
      } else {
         int var4 = this.version.getDataRegionSizeRows();
         int var5 = this.version.getDataRegionSizeColumns();
         int var6 = var2 / var4;
         int var7 = var3 / var5;
         BitMatrix var8 = new BitMatrix(var7 * var5, var6 * var4);

         for(var3 = 0; var3 < var6; ++var3) {
            for(var2 = 0; var2 < var7; ++var2) {
               for(int var9 = 0; var9 < var4; ++var9) {
                  for(int var10 = 0; var10 < var5; ++var10) {
                     if (var1.get((var5 + 2) * var2 + 1 + var10, (var4 + 2) * var3 + 1 + var9)) {
                        var8.set(var2 * var5 + var10, var3 * var4 + var9);
                     }
                  }
               }
            }
         }

         return var8;
      }
   }

   private int readCorner1(int var1, int var2) {
      byte var3 = 0;
      if (this.readModule(var1 - 1, 0, var1, var2)) {
         var3 = 1;
      }

      int var4 = var3 << 1;
      int var5 = var4;
      if (this.readModule(var1 - 1, 1, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if (this.readModule(var1 - 1, 2, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if (this.readModule(0, var2 - 2, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if (this.readModule(0, var2 - 1, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if (this.readModule(1, var2 - 1, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if (this.readModule(2, var2 - 1, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if (this.readModule(3, var2 - 1, var1, var2)) {
         var5 = var4 | 1;
      }

      return var5;
   }

   private int readCorner2(int var1, int var2) {
      byte var3 = 0;
      if (this.readModule(var1 - 3, 0, var1, var2)) {
         var3 = 1;
      }

      int var4 = var3 << 1;
      int var5 = var4;
      if (this.readModule(var1 - 2, 0, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if (this.readModule(var1 - 1, 0, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if (this.readModule(0, var2 - 4, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if (this.readModule(0, var2 - 3, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if (this.readModule(0, var2 - 2, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if (this.readModule(0, var2 - 1, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if (this.readModule(1, var2 - 1, var1, var2)) {
         var5 = var4 | 1;
      }

      return var5;
   }

   private int readCorner3(int var1, int var2) {
      byte var3 = 0;
      if (this.readModule(var1 - 1, 0, var1, var2)) {
         var3 = 1;
      }

      int var4 = var3 << 1;
      int var5 = var4;
      if (this.readModule(var1 - 1, var2 - 1, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if (this.readModule(0, var2 - 3, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if (this.readModule(0, var2 - 2, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if (this.readModule(0, var2 - 1, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if (this.readModule(1, var2 - 3, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if (this.readModule(1, var2 - 2, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if (this.readModule(1, var2 - 1, var1, var2)) {
         var5 = var4 | 1;
      }

      return var5;
   }

   private int readCorner4(int var1, int var2) {
      byte var3 = 0;
      if (this.readModule(var1 - 3, 0, var1, var2)) {
         var3 = 1;
      }

      int var4 = var3 << 1;
      int var5 = var4;
      if (this.readModule(var1 - 2, 0, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if (this.readModule(var1 - 1, 0, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if (this.readModule(0, var2 - 2, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if (this.readModule(0, var2 - 1, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if (this.readModule(1, var2 - 1, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if (this.readModule(2, var2 - 1, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if (this.readModule(3, var2 - 1, var1, var2)) {
         var5 = var4 | 1;
      }

      return var5;
   }

   private boolean readModule(int var1, int var2, int var3, int var4) {
      int var5 = var1;
      int var6 = var2;
      if (var1 < 0) {
         var5 = var1 + var3;
         var6 = var2 + (4 - (var3 + 4 & 7));
      }

      var2 = var5;
      var1 = var6;
      if (var6 < 0) {
         var1 = var6 + var4;
         var2 = var5 + (4 - (var4 + 4 & 7));
      }

      this.readMappingMatrix.set(var1, var2);
      return this.mappingBitMatrix.get(var1, var2);
   }

   private int readUtah(int var1, int var2, int var3, int var4) {
      byte var5 = 0;
      if (this.readModule(var1 - 2, var2 - 2, var3, var4)) {
         var5 = 1;
      }

      int var6 = var5 << 1;
      int var7 = var6;
      if (this.readModule(var1 - 2, var2 - 1, var3, var4)) {
         var7 = var6 | 1;
      }

      var6 = var7 << 1;
      var7 = var6;
      if (this.readModule(var1 - 1, var2 - 2, var3, var4)) {
         var7 = var6 | 1;
      }

      var6 = var7 << 1;
      var7 = var6;
      if (this.readModule(var1 - 1, var2 - 1, var3, var4)) {
         var7 = var6 | 1;
      }

      var6 = var7 << 1;
      var7 = var6;
      if (this.readModule(var1 - 1, var2, var3, var4)) {
         var7 = var6 | 1;
      }

      var6 = var7 << 1;
      var7 = var6;
      if (this.readModule(var1, var2 - 2, var3, var4)) {
         var7 = var6 | 1;
      }

      var6 = var7 << 1;
      var7 = var6;
      if (this.readModule(var1, var2 - 1, var3, var4)) {
         var7 = var6 | 1;
      }

      var6 = var7 << 1;
      var7 = var6;
      if (this.readModule(var1, var2, var3, var4)) {
         var7 = var6 | 1;
      }

      return var7;
   }

   private static Version readVersion(BitMatrix var0) throws FormatException {
      return Version.getVersionForDimensions(var0.getHeight(), var0.getWidth());
   }

   Version getVersion() {
      return this.version;
   }

   byte[] readCodewords() throws FormatException {
      byte[] var1 = new byte[this.version.getTotalCodewords()];
      int var2 = 4;
      int var3 = 0;
      int var4 = this.mappingBitMatrix.getHeight();
      int var5 = this.mappingBitMatrix.getWidth();
      boolean var6 = false;
      boolean var7 = false;
      boolean var8 = false;
      boolean var9 = false;
      int var10 = 0;

      label100:
      do {
         int var11;
         if (var2 == var4 && var3 == 0 && !var6) {
            var11 = var10 + 1;
            var1[var10] = (byte)((byte)this.readCorner1(var4, var5));
            var2 -= 2;
            var3 += 2;
            var6 = true;
            var10 = var11;
         } else if (var2 == var4 - 2 && var3 == 0 && (var5 & 3) != 0 && !var7) {
            var11 = var10 + 1;
            var1[var10] = (byte)((byte)this.readCorner2(var4, var5));
            var2 -= 2;
            var3 += 2;
            var7 = true;
            var10 = var11;
         } else if (var2 == var4 + 4 && var3 == 2 && (var5 & 7) == 0 && !var8) {
            var11 = var10 + 1;
            var1[var10] = (byte)((byte)this.readCorner3(var4, var5));
            var2 -= 2;
            var3 += 2;
            var8 = true;
            var10 = var11;
         } else {
            int var12 = var3;
            var11 = var10;
            int var13 = var2;
            if (var2 == var4 - 2) {
               var12 = var3;
               var11 = var10;
               var13 = var2;
               if (var3 == 0) {
                  var12 = var3;
                  var11 = var10;
                  var13 = var2;
                  if ((var5 & 7) == 4) {
                     var12 = var3;
                     var11 = var10;
                     var13 = var2;
                     if (!var9) {
                        var11 = var10 + 1;
                        var1[var10] = (byte)((byte)this.readCorner4(var4, var5));
                        var2 -= 2;
                        var3 += 2;
                        var9 = true;
                        var10 = var11;
                        continue;
                     }
                  }
               }
            }

            while(true) {
               if (var13 < var4 && var12 >= 0 && !this.readMappingMatrix.get(var12, var13)) {
                  var3 = var11 + 1;
                  var1[var11] = (byte)((byte)this.readUtah(var13, var12, var4, var5));
               } else {
                  var3 = var11;
               }

               var13 -= 2;
               var12 += 2;
               if (var13 < 0 || var12 >= var5) {
                  var2 = var13 + 1;
                  var11 = var12 + 3;

                  while(true) {
                     if (var2 >= 0 && var11 < var5 && !this.readMappingMatrix.get(var11, var2)) {
                        var10 = var3 + 1;
                        var1[var3] = (byte)((byte)this.readUtah(var2, var11, var4, var5));
                     } else {
                        var10 = var3;
                     }

                     var2 += 2;
                     var11 -= 2;
                     if (var2 >= var4 || var11 < 0) {
                        var2 += 3;
                        var3 = var11 + 1;
                        continue label100;
                     }

                     var3 = var10;
                  }
               }

               var11 = var3;
            }
         }
      } while(var2 < var4 || var3 < var5);

      if (var10 != this.version.getTotalCodewords()) {
         throw FormatException.getFormatInstance();
      } else {
         return var1;
      }
   }
}
