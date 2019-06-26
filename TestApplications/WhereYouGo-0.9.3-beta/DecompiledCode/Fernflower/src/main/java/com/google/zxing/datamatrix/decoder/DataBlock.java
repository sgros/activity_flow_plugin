package com.google.zxing.datamatrix.decoder;

final class DataBlock {
   private final byte[] codewords;
   private final int numDataCodewords;

   private DataBlock(int var1, byte[] var2) {
      this.numDataCodewords = var1;
      this.codewords = var2;
   }

   static DataBlock[] getDataBlocks(byte[] var0, Version var1) {
      Version.ECBlocks var2 = var1.getECBlocks();
      int var3 = 0;
      Version.ECB[] var4 = var2.getECBlocks();
      int var5 = var4.length;

      int var6;
      for(var6 = 0; var6 < var5; ++var6) {
         var3 += var4[var6].getCount();
      }

      DataBlock[] var7 = new DataBlock[var3];
      var3 = 0;
      int var8 = var4.length;

      for(var6 = 0; var6 < var8; ++var6) {
         Version.ECB var9 = var4[var6];

         for(var5 = 0; var5 < var9.getCount(); ++var3) {
            int var10 = var9.getDataCodewords();
            var7[var3] = new DataBlock(var10, new byte[var2.getECCodewords() + var10]);
            ++var5;
         }
      }

      int var11 = var7[0].codewords.length - var2.getECCodewords();
      var6 = 0;

      for(var5 = 0; var5 < var11 - 1; ++var5) {
         for(var8 = 0; var8 < var3; ++var6) {
            var7[var8].codewords[var5] = (byte)var0[var6];
            ++var8;
         }
      }

      boolean var14;
      if (var1.getVersionNumber() == 24) {
         var14 = true;
      } else {
         var14 = false;
      }

      if (var14) {
         var5 = 8;
      } else {
         var5 = var3;
      }

      for(var8 = 0; var8 < var5; ++var6) {
         var7[var8].codewords[var11 - 1] = (byte)var0[var6];
         ++var8;
      }

      int var12 = var7[0].codewords.length;
      var5 = var6;

      for(var6 = var11; var6 < var12; ++var6) {
         for(var8 = 0; var8 < var3; ++var5) {
            if (var14) {
               var11 = (var8 + 8) % var3;
            } else {
               var11 = var8;
            }

            int var13;
            if (var14 && var11 > 7) {
               var13 = var6 - 1;
            } else {
               var13 = var6;
            }

            var7[var11].codewords[var13] = (byte)var0[var5];
            ++var8;
         }
      }

      if (var5 != var0.length) {
         throw new IllegalArgumentException();
      } else {
         return var7;
      }
   }

   byte[] getCodewords() {
      return this.codewords;
   }

   int getNumDataCodewords() {
      return this.numDataCodewords;
   }
}
