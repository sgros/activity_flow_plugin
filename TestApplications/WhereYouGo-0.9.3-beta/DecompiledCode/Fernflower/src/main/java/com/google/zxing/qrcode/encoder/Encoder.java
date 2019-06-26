package com.google.zxing.qrcode.encoder;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.CharacterSetECI;
import com.google.zxing.common.reedsolomon.GenericGF;
import com.google.zxing.common.reedsolomon.ReedSolomonEncoder;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.decoder.Mode;
import com.google.zxing.qrcode.decoder.Version;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public final class Encoder {
   private static final int[] ALPHANUMERIC_TABLE = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 36, -1, -1, -1, 37, 38, -1, -1, -1, -1, 39, 40, -1, 41, 42, 43, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 44, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, -1, -1, -1, -1, -1};
   static final String DEFAULT_BYTE_MODE_ENCODING = "ISO-8859-1";

   private Encoder() {
   }

   static void append8BitBytes(String var0, BitArray var1, String var2) throws WriterException {
      byte[] var6;
      try {
         var6 = var0.getBytes(var2);
      } catch (UnsupportedEncodingException var5) {
         throw new WriterException(var5);
      }

      int var3 = var6.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         var1.appendBits(var6[var4], 8);
      }

   }

   static void appendAlphanumericBytes(CharSequence var0, BitArray var1) throws WriterException {
      int var2 = var0.length();
      int var3 = 0;

      while(var3 < var2) {
         int var4 = getAlphanumericCode(var0.charAt(var3));
         if (var4 == -1) {
            throw new WriterException();
         }

         if (var3 + 1 < var2) {
            int var5 = getAlphanumericCode(var0.charAt(var3 + 1));
            if (var5 == -1) {
               throw new WriterException();
            }

            var1.appendBits(var4 * 45 + var5, 11);
            var3 += 2;
         } else {
            var1.appendBits(var4, 6);
            ++var3;
         }
      }

   }

   static void appendBytes(String var0, Mode var1, BitArray var2, String var3) throws WriterException {
      switch(var1) {
      case NUMERIC:
         appendNumericBytes(var0, var2);
         break;
      case ALPHANUMERIC:
         appendAlphanumericBytes(var0, var2);
         break;
      case BYTE:
         append8BitBytes(var0, var2, var3);
         break;
      case KANJI:
         appendKanjiBytes(var0, var2);
         break;
      default:
         throw new WriterException("Invalid mode: " + var1);
      }

   }

   private static void appendECI(CharacterSetECI var0, BitArray var1) {
      var1.appendBits(Mode.ECI.getBits(), 4);
      var1.appendBits(var0.getValue(), 8);
   }

   static void appendKanjiBytes(String var0, BitArray var1) throws WriterException {
      byte[] var8;
      try {
         var8 = var0.getBytes("Shift_JIS");
      } catch (UnsupportedEncodingException var7) {
         throw new WriterException(var7);
      }

      int var2 = var8.length;

      for(int var3 = 0; var3 < var2; var3 += 2) {
         int var4 = (var8[var3] & 255) << 8 | var8[var3 + 1] & 255;
         byte var5 = -1;
         int var6;
         if (var4 >= 33088 && var4 <= 40956) {
            var6 = var4 - '腀';
         } else {
            var6 = var5;
            if (var4 >= 57408) {
               var6 = var5;
               if (var4 <= 60351) {
                  var6 = var4 - '셀';
               }
            }
         }

         if (var6 == -1) {
            throw new WriterException("Invalid byte sequence");
         }

         var1.appendBits((var6 >> 8) * 192 + (var6 & 255), 13);
      }

   }

   static void appendLengthInfo(int var0, Version var1, Mode var2, BitArray var3) throws WriterException {
      int var4 = var2.getCharacterCountBits(var1);
      if (var0 >= 1 << var4) {
         throw new WriterException(var0 + " is bigger than " + ((1 << var4) - 1));
      } else {
         var3.appendBits(var0, var4);
      }
   }

   static void appendModeInfo(Mode var0, BitArray var1) {
      var1.appendBits(var0.getBits(), 4);
   }

   static void appendNumericBytes(CharSequence var0, BitArray var1) {
      int var2 = var0.length();
      int var3 = 0;

      while(var3 < var2) {
         int var4 = var0.charAt(var3) - 48;
         if (var3 + 2 < var2) {
            var1.appendBits(var4 * 100 + (var0.charAt(var3 + 1) - 48) * 10 + (var0.charAt(var3 + 2) - 48), 10);
            var3 += 3;
         } else if (var3 + 1 < var2) {
            var1.appendBits(var4 * 10 + (var0.charAt(var3 + 1) - 48), 7);
            var3 += 2;
         } else {
            var1.appendBits(var4, 4);
            ++var3;
         }
      }

   }

   private static int calculateBitsNeeded(Mode var0, BitArray var1, BitArray var2, Version var3) {
      return var1.getSize() + var0.getCharacterCountBits(var3) + var2.getSize();
   }

   private static int calculateMaskPenalty(ByteMatrix var0) {
      return MaskUtil.applyMaskPenaltyRule1(var0) + MaskUtil.applyMaskPenaltyRule2(var0) + MaskUtil.applyMaskPenaltyRule3(var0) + MaskUtil.applyMaskPenaltyRule4(var0);
   }

   private static int chooseMaskPattern(BitArray var0, ErrorCorrectionLevel var1, Version var2, ByteMatrix var3) throws WriterException {
      int var4 = Integer.MAX_VALUE;
      int var5 = -1;

      int var8;
      for(int var6 = 0; var6 < 8; var4 = var8) {
         MatrixUtil.buildMatrix(var0, var1, var2, var6, var3);
         int var7 = calculateMaskPenalty(var3);
         var8 = var4;
         if (var7 < var4) {
            var8 = var7;
            var5 = var6;
         }

         ++var6;
      }

      return var5;
   }

   public static Mode chooseMode(String var0) {
      return chooseMode(var0, (String)null);
   }

   private static Mode chooseMode(String var0, String var1) {
      Mode var6;
      if ("Shift_JIS".equals(var1) && isOnlyDoubleByteKanji(var0)) {
         var6 = Mode.KANJI;
      } else {
         boolean var2 = false;
         boolean var3 = false;
         int var4 = 0;

         while(true) {
            if (var4 >= var0.length()) {
               if (var3) {
                  var6 = Mode.ALPHANUMERIC;
               } else if (var2) {
                  var6 = Mode.NUMERIC;
               } else {
                  var6 = Mode.BYTE;
               }
               break;
            }

            char var5 = var0.charAt(var4);
            if (var5 >= '0' && var5 <= '9') {
               var2 = true;
            } else {
               if (getAlphanumericCode(var5) == -1) {
                  var6 = Mode.BYTE;
                  break;
               }

               var3 = true;
            }

            ++var4;
         }
      }

      return var6;
   }

   private static Version chooseVersion(int var0, ErrorCorrectionLevel var1) throws WriterException {
      for(int var2 = 1; var2 <= 40; ++var2) {
         Version var3 = Version.getVersionForNumber(var2);
         if (willFit(var0, var3, var1)) {
            return var3;
         }
      }

      throw new WriterException("Data too big");
   }

   public static QRCode encode(String var0, ErrorCorrectionLevel var1) throws WriterException {
      return encode(var0, var1, (Map)null);
   }

   public static QRCode encode(String var0, ErrorCorrectionLevel var1, Map var2) throws WriterException {
      String var3 = "ISO-8859-1";
      String var4 = var3;
      if (var2 != null) {
         var4 = var3;
         if (var2.containsKey(EncodeHintType.CHARACTER_SET)) {
            var4 = var2.get(EncodeHintType.CHARACTER_SET).toString();
         }
      }

      Mode var11 = chooseMode(var0, var4);
      BitArray var5 = new BitArray();
      if (var11 == Mode.BYTE && !"ISO-8859-1".equals(var4)) {
         CharacterSetECI var6 = CharacterSetECI.getCharacterSetECIByName(var4);
         if (var6 != null) {
            appendECI(var6, var5);
         }
      }

      appendModeInfo(var11, var5);
      BitArray var16 = new BitArray();
      appendBytes(var0, var11, var16, var4);
      Version var10;
      if (var2 != null && var2.containsKey(EncodeHintType.QR_VERSION)) {
         Version var13 = Version.getVersionForNumber(Integer.parseInt(var2.get(EncodeHintType.QR_VERSION).toString()));
         var10 = var13;
         if (!willFit(calculateBitsNeeded(var11, var5, var16, var13), var13, var1)) {
            throw new WriterException("Data too big for requested version");
         }
      } else {
         var10 = recommendVersion(var1, var11, var5, var16);
      }

      BitArray var14 = new BitArray();
      var14.appendBitArray(var5);
      int var7;
      if (var11 == Mode.BYTE) {
         var7 = var16.getSizeInBytes();
      } else {
         var7 = var0.length();
      }

      appendLengthInfo(var7, var10, var11, var14);
      var14.appendBitArray(var16);
      Version.ECBlocks var8 = var10.getECBlocksForLevel(var1);
      var7 = var10.getTotalCodewords() - var8.getTotalECCodewords();
      terminateBits(var7, var14);
      BitArray var9 = interleaveWithECBytes(var14, var10.getTotalCodewords(), var7, var8.getNumBlocks());
      QRCode var15 = new QRCode();
      var15.setECLevel(var1);
      var15.setMode(var11);
      var15.setVersion(var10);
      var7 = var10.getDimensionForVersion();
      ByteMatrix var12 = new ByteMatrix(var7, var7);
      var7 = chooseMaskPattern(var9, var1, var10, var12);
      var15.setMaskPattern(var7);
      MatrixUtil.buildMatrix(var9, var1, var10, var7, var12);
      var15.setMatrix(var12);
      return var15;
   }

   static byte[] generateECBytes(byte[] var0, int var1) {
      int var2 = var0.length;
      int[] var3 = new int[var2 + var1];

      int var4;
      for(var4 = 0; var4 < var2; ++var4) {
         var3[var4] = var0[var4] & 255;
      }

      (new ReedSolomonEncoder(GenericGF.QR_CODE_FIELD_256)).encode(var3, var1);
      var0 = new byte[var1];

      for(var4 = 0; var4 < var1; ++var4) {
         var0[var4] = (byte)((byte)var3[var2 + var4]);
      }

      return var0;
   }

   static int getAlphanumericCode(int var0) {
      if (var0 < ALPHANUMERIC_TABLE.length) {
         var0 = ALPHANUMERIC_TABLE[var0];
      } else {
         var0 = -1;
      }

      return var0;
   }

   static void getNumDataBytesAndNumECBytesForBlockID(int var0, int var1, int var2, int var3, int[] var4, int[] var5) throws WriterException {
      if (var3 >= var2) {
         throw new WriterException("Block ID too large");
      } else {
         int var6 = var0 % var2;
         int var7 = var2 - var6;
         int var8 = var0 / var2;
         int var9 = var1 / var2;
         int var10 = var9 + 1;
         var1 = var8 - var9;
         var8 = var8 + 1 - var10;
         if (var1 != var8) {
            throw new WriterException("EC bytes mismatch");
         } else if (var2 != var7 + var6) {
            throw new WriterException("RS blocks mismatch");
         } else if (var0 != (var9 + var1) * var7 + (var10 + var8) * var6) {
            throw new WriterException("Total bytes mismatch");
         } else {
            if (var3 < var7) {
               var4[0] = var9;
               var5[0] = var1;
            } else {
               var4[0] = var10;
               var5[0] = var8;
            }

         }
      }
   }

   static BitArray interleaveWithECBytes(BitArray var0, int var1, int var2, int var3) throws WriterException {
      if (var0.getSizeInBytes() != var2) {
         throw new WriterException("Number of bits and data bytes does not match");
      } else {
         int var4 = 0;
         int var5 = 0;
         int var6 = 0;
         ArrayList var7 = new ArrayList(var3);

         byte[] var12;
         for(int var8 = 0; var8 < var3; ++var8) {
            int[] var9 = new int[1];
            int[] var10 = new int[1];
            getNumDataBytesAndNumECBytesForBlockID(var1, var2, var3, var8, var9, var10);
            int var11 = var9[0];
            var12 = new byte[var11];
            var0.toBytes(var4 << 3, var12, 0, var11);
            byte[] var15 = generateECBytes(var12, var10[0]);
            var7.add(new BlockPair(var12, var15));
            var5 = Math.max(var5, var11);
            var6 = Math.max(var6, var15.length);
            var4 += var9[0];
         }

         if (var2 != var4) {
            throw new WriterException("Data bytes does not match offset");
         } else {
            var0 = new BitArray();

            for(var2 = 0; var2 < var5; ++var2) {
               Iterator var13 = var7.iterator();

               while(var13.hasNext()) {
                  var12 = ((BlockPair)var13.next()).getDataBytes();
                  if (var2 < var12.length) {
                     var0.appendBits(var12[var2], 8);
                  }
               }
            }

            for(var2 = 0; var2 < var6; ++var2) {
               Iterator var16 = var7.iterator();

               while(var16.hasNext()) {
                  byte[] var14 = ((BlockPair)var16.next()).getErrorCorrectionBytes();
                  if (var2 < var14.length) {
                     var0.appendBits(var14[var2], 8);
                  }
               }
            }

            if (var1 != var0.getSizeInBytes()) {
               throw new WriterException("Interleaving error: " + var1 + " and " + var0.getSizeInBytes() + " differ.");
            } else {
               return var0;
            }
         }
      }
   }

   private static boolean isOnlyDoubleByteKanji(String var0) {
      boolean var1 = false;

      boolean var3;
      byte[] var7;
      try {
         var7 = var0.getBytes("Shift_JIS");
      } catch (UnsupportedEncodingException var6) {
         var3 = var1;
         return var3;
      }

      int var2 = var7.length;
      if (var2 % 2 != 0) {
         var3 = var1;
      } else {
         int var4 = 0;

         while(true) {
            if (var4 >= var2) {
               var3 = true;
               break;
            }

            int var5 = var7[var4] & 255;
            if (var5 < 129 || var5 > 159) {
               var3 = var1;
               if (var5 < 224) {
                  break;
               }

               var3 = var1;
               if (var5 > 235) {
                  break;
               }
            }

            var4 += 2;
         }
      }

      return var3;
   }

   private static Version recommendVersion(ErrorCorrectionLevel var0, Mode var1, BitArray var2, BitArray var3) throws WriterException {
      return chooseVersion(calculateBitsNeeded(var1, var2, var3, chooseVersion(calculateBitsNeeded(var1, var2, var3, Version.getVersionForNumber(1)), var0)), var0);
   }

   static void terminateBits(int var0, BitArray var1) throws WriterException {
      int var2 = var0 << 3;
      if (var1.getSize() > var2) {
         throw new WriterException("data bits cannot fit in the QR Code" + var1.getSize() + " > " + var2);
      } else {
         int var3;
         for(var3 = 0; var3 < 4 && var1.getSize() < var2; ++var3) {
            var1.appendBit(false);
         }

         var3 = var1.getSize() & 7;
         if (var3 > 0) {
            while(var3 < 8) {
               var1.appendBit(false);
               ++var3;
            }
         }

         int var4 = var1.getSizeInBytes();

         for(var3 = 0; var3 < var0 - var4; ++var3) {
            short var5;
            if ((var3 & 1) == 0) {
               var5 = 236;
            } else {
               var5 = 17;
            }

            var1.appendBits(var5, 8);
         }

         if (var1.getSize() != var2) {
            throw new WriterException("Bits size does not equal capacity");
         }
      }
   }

   private static boolean willFit(int var0, Version var1, ErrorCorrectionLevel var2) {
      boolean var3;
      if (var1.getTotalCodewords() - var1.getECBlocksForLevel(var2).getTotalECCodewords() >= (var0 + 7) / 8) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }
}
