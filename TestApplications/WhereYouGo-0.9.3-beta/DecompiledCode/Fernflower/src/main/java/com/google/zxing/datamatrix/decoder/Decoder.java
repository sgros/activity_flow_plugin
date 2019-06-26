package com.google.zxing.datamatrix.decoder;

import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.common.reedsolomon.GenericGF;
import com.google.zxing.common.reedsolomon.ReedSolomonDecoder;
import com.google.zxing.common.reedsolomon.ReedSolomonException;

public final class Decoder {
   private final ReedSolomonDecoder rsDecoder;

   public Decoder() {
      this.rsDecoder = new ReedSolomonDecoder(GenericGF.DATA_MATRIX_FIELD_256);
   }

   private void correctErrors(byte[] var1, int var2) throws ChecksumException {
      int var3 = var1.length;
      int[] var4 = new int[var3];

      int var5;
      for(var5 = 0; var5 < var3; ++var5) {
         var4[var5] = var1[var5] & 255;
      }

      try {
         this.rsDecoder.decode(var4, var1.length - var2);
      } catch (ReedSolomonException var6) {
         throw ChecksumException.getChecksumInstance();
      }

      for(var5 = 0; var5 < var2; ++var5) {
         var1[var5] = (byte)((byte)var4[var5]);
      }

   }

   public DecoderResult decode(BitMatrix var1) throws FormatException, ChecksumException {
      BitMatrixParser var9 = new BitMatrixParser(var1);
      Version var2 = var9.getVersion();
      DataBlock[] var3 = DataBlock.getDataBlocks(var9.readCodewords(), var2);
      int var4 = 0;
      int var5 = var3.length;

      int var6;
      for(var6 = 0; var6 < var5; ++var6) {
         var4 += var3[var6].getNumDataCodewords();
      }

      byte[] var10 = new byte[var4];
      var5 = var3.length;

      for(var6 = 0; var6 < var5; ++var6) {
         DataBlock var7 = var3[var6];
         byte[] var11 = var7.getCodewords();
         int var8 = var7.getNumDataCodewords();
         this.correctErrors(var11, var8);

         for(var4 = 0; var4 < var8; ++var4) {
            var10[var4 * var5 + var6] = (byte)var11[var4];
         }
      }

      return DecodedBitStreamParser.decode(var10);
   }

   public DecoderResult decode(boolean[][] var1) throws FormatException, ChecksumException {
      int var2 = var1.length;
      BitMatrix var3 = new BitMatrix(var2);

      for(int var4 = 0; var4 < var2; ++var4) {
         for(int var5 = 0; var5 < var2; ++var5) {
            if (var1[var4][var5]) {
               var3.set(var5, var4);
            }
         }
      }

      return this.decode(var3);
   }
}
