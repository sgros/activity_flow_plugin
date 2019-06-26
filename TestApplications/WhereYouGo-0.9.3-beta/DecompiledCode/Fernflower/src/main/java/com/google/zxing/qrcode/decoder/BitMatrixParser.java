package com.google.zxing.qrcode.decoder;

import com.google.zxing.FormatException;
import com.google.zxing.common.BitMatrix;

final class BitMatrixParser {
   private final BitMatrix bitMatrix;
   private boolean mirror;
   private FormatInformation parsedFormatInfo;
   private Version parsedVersion;

   BitMatrixParser(BitMatrix var1) throws FormatException {
      int var2 = var1.getHeight();
      if (var2 >= 21 && (var2 & 3) == 1) {
         this.bitMatrix = var1;
      } else {
         throw FormatException.getFormatInstance();
      }
   }

   private int copyBit(int var1, int var2, int var3) {
      boolean var4;
      if (this.mirror) {
         var4 = this.bitMatrix.get(var2, var1);
      } else {
         var4 = this.bitMatrix.get(var1, var2);
      }

      if (var4) {
         var1 = var3 << 1 | 1;
      } else {
         var1 = var3 << 1;
      }

      return var1;
   }

   void mirror() {
      for(int var1 = 0; var1 < this.bitMatrix.getWidth(); ++var1) {
         for(int var2 = var1 + 1; var2 < this.bitMatrix.getHeight(); ++var2) {
            if (this.bitMatrix.get(var1, var2) != this.bitMatrix.get(var2, var1)) {
               this.bitMatrix.flip(var2, var1);
               this.bitMatrix.flip(var1, var2);
            }
         }
      }

   }

   byte[] readCodewords() throws FormatException {
      FormatInformation var1 = this.readFormatInformation();
      Version var2 = this.readVersion();
      DataMask var15 = DataMask.values()[var1.getDataMask()];
      int var3 = this.bitMatrix.getHeight();
      var15.unmaskBitMatrix(this.bitMatrix, var3);
      BitMatrix var4 = var2.buildFunctionPattern();
      boolean var5 = true;
      byte[] var16 = new byte[var2.getTotalCodewords()];
      int var6 = 0;
      int var7 = 0;
      int var8 = 0;

      int var10;
      for(int var9 = var3 - 1; var9 > 0; var9 = var10 - 2) {
         var10 = var9;
         if (var9 == 6) {
            var10 = var9 - 1;
         }

         for(var9 = 0; var9 < var3; ++var9) {
            int var11;
            if (var5) {
               var11 = var3 - 1 - var9;
            } else {
               var11 = var9;
            }

            for(int var12 = 0; var12 < 2; ++var12) {
               int var13 = var8;
               int var14 = var7;
               if (!var4.get(var10 - var12, var11)) {
                  ++var8;
                  var14 = var7 << 1;
                  var7 = var14;
                  if (this.bitMatrix.get(var10 - var12, var11)) {
                     var7 = var14 | 1;
                  }

                  var13 = var8;
                  var14 = var7;
                  if (var8 == 8) {
                     var8 = var6 + 1;
                     var16[var6] = (byte)((byte)var7);
                     byte var17 = 0;
                     var7 = 0;
                     var6 = var8;
                     var8 = var17;
                     continue;
                  }
               }

               var8 = var13;
               var7 = var14;
            }
         }

         var5 ^= true;
      }

      if (var6 != var2.getTotalCodewords()) {
         throw FormatException.getFormatInstance();
      } else {
         return var16;
      }
   }

   FormatInformation readFormatInformation() throws FormatException {
      FormatInformation var1;
      if (this.parsedFormatInfo != null) {
         var1 = this.parsedFormatInfo;
      } else {
         int var2 = 0;

         int var3;
         for(var3 = 0; var3 < 6; ++var3) {
            var2 = this.copyBit(var3, 8, var2);
         }

         var2 = this.copyBit(8, 7, this.copyBit(8, 8, this.copyBit(7, 8, var2)));

         for(var3 = 5; var3 >= 0; --var3) {
            var2 = this.copyBit(8, var3, var2);
         }

         int var4 = this.bitMatrix.getHeight();
         var3 = 0;

         int var5;
         for(var5 = var4 - 1; var5 >= var4 - 7; --var5) {
            var3 = this.copyBit(8, var5, var3);
         }

         for(var5 = var4 - 8; var5 < var4; ++var5) {
            var3 = this.copyBit(var5, 8, var3);
         }

         this.parsedFormatInfo = FormatInformation.decodeFormatInformation(var2, var3);
         if (this.parsedFormatInfo == null) {
            throw FormatException.getFormatInstance();
         }

         var1 = this.parsedFormatInfo;
      }

      return var1;
   }

   Version readVersion() throws FormatException {
      Version var1;
      if (this.parsedVersion != null) {
         var1 = this.parsedVersion;
      } else {
         int var2 = this.bitMatrix.getHeight();
         int var3 = (var2 - 17) / 4;
         if (var3 <= 6) {
            var1 = Version.getVersionForNumber(var3);
         } else {
            int var4 = 0;
            int var5 = var2 - 11;
            var3 = 5;

            while(true) {
               int var6;
               if (var3 < 0) {
                  var1 = Version.decodeVersionInformation(var4);
                  if (var1 != null && var1.getDimensionForVersion() == var2) {
                     this.parsedVersion = var1;
                     break;
                  }

                  var4 = 0;

                  for(var3 = 5; var3 >= 0; --var3) {
                     for(var6 = var2 - 9; var6 >= var5; --var6) {
                        var4 = this.copyBit(var3, var6, var4);
                     }
                  }

                  var1 = Version.decodeVersionInformation(var4);
                  if (var1 != null && var1.getDimensionForVersion() == var2) {
                     this.parsedVersion = var1;
                     break;
                  }

                  throw FormatException.getFormatInstance();
               }

               for(var6 = var2 - 9; var6 >= var5; --var6) {
                  var4 = this.copyBit(var6, var3, var4);
               }

               --var3;
            }
         }
      }

      return var1;
   }

   void remask() {
      if (this.parsedFormatInfo != null) {
         DataMask var1 = DataMask.values()[this.parsedFormatInfo.getDataMask()];
         int var2 = this.bitMatrix.getHeight();
         var1.unmaskBitMatrix(this.bitMatrix, var2);
      }

   }

   void setMirror(boolean var1) {
      this.parsedVersion = null;
      this.parsedFormatInfo = null;
      this.mirror = var1;
   }
}
