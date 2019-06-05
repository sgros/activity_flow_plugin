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
      BitMatrixParser var2 = new BitMatrixParser(var1);
      Version var9 = var2.getVersion();
      DataBlock[] var11 = DataBlock.getDataBlocks(var2.readCodewords(), var9);
      int var3 = 0;
      int var4 = var11.length;

      int var5;
      for(var5 = 0; var5 < var4; ++var5) {
         var3 += var11[var5].getNumDataCodewords();
      }

      byte[] var6 = new byte[var3];
      var4 = var11.length;

      for(var5 = 0; var5 < var4; ++var5) {
         DataBlock var10 = var11[var5];
         byte[] var7 = var10.getCodewords();
         int var8 = var10.getNumDataCodewords();
         this.correctErrors(var7, var8);

         for(var3 = 0; var3 < var8; ++var3) {
            var6[var3 * var4 + var5] = (byte)var7[var3];
         }
      }

      return DecodedBitStreamParser.decode(var6);
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
