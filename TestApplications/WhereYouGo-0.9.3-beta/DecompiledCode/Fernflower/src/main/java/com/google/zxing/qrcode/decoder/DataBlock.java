package com.google.zxing.qrcode.decoder;

final class DataBlock {
   private final byte[] codewords;
   private final int numDataCodewords;

   private DataBlock(int var1, byte[] var2) {
      this.numDataCodewords = var1;
      this.codewords = var2;
   }

   static DataBlock[] getDataBlocks(byte[] var0, Version var1, ErrorCorrectionLevel var2) {
      if (var0.length != var1.getTotalCodewords()) {
         throw new IllegalArgumentException();
      } else {
         Version.ECBlocks var3 = var1.getECBlocksForLevel(var2);
         int var4 = 0;
         Version.ECB[] var13 = var3.getECBlocks();
         int var5 = var13.length;

         int var6;
         for(var6 = 0; var6 < var5; ++var6) {
            var4 += var13[var6].getCount();
         }

         DataBlock[] var7 = new DataBlock[var4];
         var5 = 0;
         int var8 = var13.length;

         int var9;
         for(var6 = 0; var6 < var8; ++var6) {
            Version.ECB var12 = var13[var6];

            for(var4 = 0; var4 < var12.getCount(); ++var5) {
               var9 = var12.getDataCodewords();
               var7[var5] = new DataBlock(var9, new byte[var3.getECCodewordsPerBlock() + var9]);
               ++var4;
            }
         }

         var4 = var7[0].codewords.length;

         for(var6 = var7.length - 1; var6 >= 0 && var7[var6].codewords.length != var4; --var6) {
         }

         int var10 = var6 + 1;
         var9 = var4 - var3.getECCodewordsPerBlock();
         var6 = 0;

         for(var4 = 0; var4 < var9; ++var4) {
            for(var8 = 0; var8 < var5; ++var6) {
               var7[var8].codewords[var4] = (byte)var0[var6];
               ++var8;
            }
         }

         for(var4 = var10; var4 < var5; ++var6) {
            var7[var4].codewords[var9] = (byte)var0[var6];
            ++var4;
         }

         int var11 = var7[0].codewords.length;
         var4 = var6;

         for(var6 = var9; var6 < var11; ++var6) {
            for(var8 = 0; var8 < var5; ++var4) {
               if (var8 < var10) {
                  var9 = var6;
               } else {
                  var9 = var6 + 1;
               }

               var7[var8].codewords[var9] = (byte)var0[var4];
               ++var8;
            }
         }

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
