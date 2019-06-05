package com.google.zxing.qrcode.decoder;

import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.common.reedsolomon.GenericGF;
import com.google.zxing.common.reedsolomon.ReedSolomonDecoder;
import com.google.zxing.common.reedsolomon.ReedSolomonException;
import java.util.Map;

public final class Decoder {
   private final ReedSolomonDecoder rsDecoder;

   public Decoder() {
      this.rsDecoder = new ReedSolomonDecoder(GenericGF.QR_CODE_FIELD_256);
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

   private DecoderResult decode(BitMatrixParser var1, Map var2) throws FormatException, ChecksumException {
      Version var3 = var1.readVersion();
      ErrorCorrectionLevel var4 = var1.readFormatInformation().getErrorCorrectionLevel();
      DataBlock[] var13 = DataBlock.getDataBlocks(var1.readCodewords(), var3, var4);
      int var5 = 0;
      int var6 = var13.length;

      int var7;
      for(var7 = 0; var7 < var6; ++var7) {
         var5 += var13[var7].getNumDataCodewords();
      }

      byte[] var8 = new byte[var5];
      var7 = 0;
      int var9 = var13.length;

      for(var5 = 0; var5 < var9; ++var5) {
         DataBlock var10 = var13[var5];
         byte[] var11 = var10.getCodewords();
         int var12 = var10.getNumDataCodewords();
         this.correctErrors(var11, var12);

         for(var6 = 0; var6 < var12; ++var7) {
            var8[var7] = (byte)var11[var6];
            ++var6;
         }
      }

      return DecodedBitStreamParser.decode(var8, var3, var4, var2);
   }

   public DecoderResult decode(BitMatrix var1) throws ChecksumException, FormatException {
      return this.decode((BitMatrix)var1, (Map)null);
   }

   public DecoderResult decode(BitMatrix var1, Map var2) throws FormatException, ChecksumException {
      BitMatrixParser var3 = new BitMatrixParser(var1);
      FormatException var4 = null;
      ChecksumException var10 = null;

      DecoderResult var11;
      DecoderResult var14;
      label46: {
         try {
            var14 = this.decode(var3, var2);
            break label46;
         } catch (FormatException var8) {
            var4 = var8;
         } catch (ChecksumException var9) {
            var10 = var9;
         }

         DecoderResult var13;
         label41: {
            Object var12;
            try {
               var3.remask();
               var3.setMirror(true);
               var3.readVersion();
               var3.readFormatInformation();
               var3.mirror();
               var13 = this.decode(var3, var2);
               QRCodeDecoderMetaData var5 = new QRCodeDecoderMetaData(true);
               var13.setOther(var5);
               break label41;
            } catch (FormatException var6) {
               var12 = var6;
            } catch (ChecksumException var7) {
               var12 = var7;
            }

            if (var4 != null) {
               throw var4;
            }

            if (var10 != null) {
               throw var10;
            }

            throw var12;
         }

         var11 = var13;
         return var11;
      }

      var11 = var14;
      return var11;
   }

   public DecoderResult decode(boolean[][] var1) throws ChecksumException, FormatException {
      return this.decode((boolean[][])var1, (Map)null);
   }

   public DecoderResult decode(boolean[][] var1, Map var2) throws ChecksumException, FormatException {
      int var3 = var1.length;
      BitMatrix var4 = new BitMatrix(var3);

      for(int var5 = 0; var5 < var3; ++var5) {
         for(int var6 = 0; var6 < var3; ++var6) {
            if (var1[var5][var6]) {
               var4.set(var6, var5);
            }
         }
      }

      return this.decode(var4, var2);
   }
}
