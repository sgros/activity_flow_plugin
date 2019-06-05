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
         Version.ECBlocks var13 = var1.getECBlocksForLevel(var2);
         int var3 = 0;
         Version.ECB[] var4 = var13.getECBlocks();
         int var5 = var4.length;

         int var6;
         for(var6 = 0; var6 < var5; ++var6) {
            var3 += var4[var6].getCount();
         }

         DataBlock[] var12 = new DataBlock[var3];
         var5 = 0;
         int var7 = var4.length;

         int var9;
         for(var6 = 0; var6 < var7; ++var6) {
            Version.ECB var8 = var4[var6];

            for(var3 = 0; var3 < var8.getCount(); ++var5) {
               var9 = var8.getDataCodewords();
               var12[var5] = new DataBlock(var9, new byte[var13.getECCodewordsPerBlock() + var9]);
               ++var3;
            }
         }

         var3 = var12[0].codewords.length;

         for(var6 = var12.length - 1; var6 >= 0 && var12[var6].codewords.length != var3; --var6) {
         }

         int var10 = var6 + 1;
         var9 = var3 - var13.getECCodewordsPerBlock();
         var6 = 0;

         for(var3 = 0; var3 < var9; ++var3) {
            for(var7 = 0; var7 < var5; ++var6) {
               var12[var7].codewords[var3] = (byte)var0[var6];
               ++var7;
            }
         }

         for(var3 = var10; var3 < var5; ++var6) {
            var12[var3].codewords[var9] = (byte)var0[var6];
            ++var3;
         }

         int var11 = var12[0].codewords.length;
         var3 = var6;

         for(var6 = var9; var6 < var11; ++var6) {
            for(var7 = 0; var7 < var5; ++var3) {
               if (var7 < var10) {
                  var9 = var6;
               } else {
                  var9 = var6 + 1;
               }

               var12[var7].codewords[var9] = (byte)var0[var3];
               ++var7;
            }
         }

         return var12;
      }
   }

   byte[] getCodewords() {
      return this.codewords;
   }

   int getNumDataCodewords() {
      return this.numDataCodewords;
   }
}
