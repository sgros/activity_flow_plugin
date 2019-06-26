package com.google.zxing.aztec.decoder;

import com.google.zxing.FormatException;
import com.google.zxing.aztec.AztecDetectorResult;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.common.reedsolomon.GenericGF;
import com.google.zxing.common.reedsolomon.ReedSolomonDecoder;
import com.google.zxing.common.reedsolomon.ReedSolomonException;
import java.util.Arrays;
import java.util.List;

public final class Decoder {
   private static final String[] DIGIT_TABLE = new String[]{"CTRL_PS", " ", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ",", ".", "CTRL_UL", "CTRL_US"};
   private static final String[] LOWER_TABLE = new String[]{"CTRL_PS", " ", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "CTRL_US", "CTRL_ML", "CTRL_DL", "CTRL_BS"};
   private static final String[] MIXED_TABLE = new String[]{"CTRL_PS", " ", "\u0001", "\u0002", "\u0003", "\u0004", "\u0005", "\u0006", "\u0007", "\b", "\t", "\n", "\u000b", "\f", "\r", "\u001b", "\u001c", "\u001d", "\u001e", "\u001f", "@", "\\", "^", "_", "`", "|", "~", "\u007f", "CTRL_LL", "CTRL_UL", "CTRL_PL", "CTRL_BS"};
   private static final String[] PUNCT_TABLE = new String[]{"", "\r", "\r\n", ". ", ", ", ": ", "!", "\"", "#", "$", "%", "&", "'", "(", ")", "*", "+", ",", "-", ".", "/", ":", ";", "<", "=", ">", "?", "[", "]", "{", "}", "CTRL_UL"};
   private static final String[] UPPER_TABLE = new String[]{"CTRL_PS", " ", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "CTRL_LL", "CTRL_ML", "CTRL_DL", "CTRL_BS"};
   private AztecDetectorResult ddata;

   static byte[] convertBoolArrayToByteArray(boolean[] var0) {
      byte[] var1 = new byte[(var0.length + 7) / 8];

      for(int var2 = 0; var2 < var1.length; ++var2) {
         var1[var2] = readByte(var0, var2 << 3);
      }

      return var1;
   }

   private boolean[] correctBits(boolean[] var1) throws FormatException {
      byte var2;
      GenericGF var3;
      if (this.ddata.getNbLayers() <= 2) {
         var2 = 6;
         var3 = GenericGF.AZTEC_DATA_6;
      } else if (this.ddata.getNbLayers() <= 8) {
         var2 = 8;
         var3 = GenericGF.AZTEC_DATA_8;
      } else if (this.ddata.getNbLayers() <= 22) {
         var2 = 10;
         var3 = GenericGF.AZTEC_DATA_10;
      } else {
         var2 = 12;
         var3 = GenericGF.AZTEC_DATA_12;
      }

      int var4 = this.ddata.getNbDatablocks();
      int var5 = var1.length / var2;
      if (var5 < var4) {
         throw FormatException.getFormatInstance();
      } else {
         int var6 = var1.length % var2;
         int[] var7 = new int[var5];

         int var8;
         for(var8 = 0; var8 < var5; var6 += var2) {
            var7[var8] = readCode(var1, var6, var2);
            ++var8;
         }

         try {
            ReedSolomonDecoder var13 = new ReedSolomonDecoder(var3);
            var13.decode(var7, var5 - var4);
         } catch (ReedSolomonException var12) {
            throw FormatException.getFormatInstance(var12);
         }

         int var9 = (1 << var2) - 1;
         var5 = 0;

         int var10;
         for(var6 = 0; var6 < var4; var5 = var8) {
            var10 = var7[var6];
            if (var10 == 0 || var10 == var9) {
               throw FormatException.getFormatInstance();
            }

            label80: {
               if (var10 != 1) {
                  var8 = var5;
                  if (var10 != var9 - 1) {
                     break label80;
                  }
               }

               var8 = var5 + 1;
            }

            ++var6;
         }

         var1 = new boolean[var4 * var2 - var5];
         var6 = 0;

         for(var8 = 0; var8 < var4; ++var8) {
            var10 = var7[var8];
            boolean var11;
            if (var10 != 1 && var10 != var9 - 1) {
               for(var5 = var2 - 1; var5 >= 0; ++var6) {
                  if ((1 << var5 & var10) != 0) {
                     var11 = true;
                  } else {
                     var11 = false;
                  }

                  var1[var6] = var11;
                  --var5;
               }
            } else {
               if (var10 > 1) {
                  var11 = true;
               } else {
                  var11 = false;
               }

               Arrays.fill(var1, var6, var6 + var2 - 1, var11);
               var6 += var2 - 1;
            }
         }

         return var1;
      }
   }

   private boolean[] extractBits(BitMatrix var1) {
      boolean var2 = this.ddata.isCompact();
      int var3 = this.ddata.getNbLayers();
      byte var4;
      if (var2) {
         var4 = 11;
      } else {
         var4 = 14;
      }

      int var5 = var4 + (var3 << 2);
      int[] var6 = new int[var5];
      boolean[] var7 = new boolean[totalBitsInLayer(var3, var2)];
      int var8;
      int var9;
      int var10;
      int var15;
      if (var2) {
         for(var15 = 0; var15 < var6.length; var6[var15] = var15++) {
         }
      } else {
         var15 = (var5 / 2 - 1) / 15;
         var8 = var5 / 2;
         var9 = (var5 + 1 + var15 * 2) / 2;

         for(var15 = 0; var15 < var8; ++var15) {
            var10 = var15 + var15 / 15;
            var6[var8 - var15 - 1] = var9 - var10 - 1;
            var6[var8 + var15] = var9 + var10 + 1;
         }
      }

      var15 = 0;

      for(var8 = 0; var15 < var3; ++var15) {
         byte var16;
         if (var2) {
            var16 = 9;
         } else {
            var16 = 12;
         }

         int var11 = (var3 - var15 << 2) + var16;
         int var12 = var15 << 1;
         int var13 = var5 - 1 - var12;

         for(var9 = 0; var9 < var11; ++var9) {
            int var14 = var9 << 1;

            for(var10 = 0; var10 < 2; ++var10) {
               var7[var8 + var14 + var10] = var1.get(var6[var12 + var10], var6[var12 + var9]);
               var7[var11 * 2 + var8 + var14 + var10] = var1.get(var6[var12 + var9], var6[var13 - var10]);
               var7[var11 * 4 + var8 + var14 + var10] = var1.get(var6[var13 - var10], var6[var13 - var9]);
               var7[var11 * 6 + var8 + var14 + var10] = var1.get(var6[var13 - var9], var6[var12 + var10]);
            }
         }

         var8 += var11 << 3;
      }

      return var7;
   }

   private static String getCharacter(Decoder.Table var0, int var1) {
      String var2;
      switch(var0) {
      case UPPER:
         var2 = UPPER_TABLE[var1];
         break;
      case LOWER:
         var2 = LOWER_TABLE[var1];
         break;
      case MIXED:
         var2 = MIXED_TABLE[var1];
         break;
      case PUNCT:
         var2 = PUNCT_TABLE[var1];
         break;
      case DIGIT:
         var2 = DIGIT_TABLE[var1];
         break;
      default:
         throw new IllegalStateException("Bad table");
      }

      return var2;
   }

   private static String getEncodedData(boolean[] var0) {
      int var1 = var0.length;
      Decoder.Table var2 = Decoder.Table.UPPER;
      Decoder.Table var3 = Decoder.Table.UPPER;
      StringBuilder var4 = new StringBuilder(20);
      int var5 = 0;

      while(var5 < var1) {
         int var7;
         int var8;
         if (var3 != Decoder.Table.BINARY) {
            byte var11;
            if (var3 == Decoder.Table.DIGIT) {
               var11 = 4;
            } else {
               var11 = 5;
            }

            if (var1 - var5 < var11) {
               break;
            }

            var8 = readCode(var0, var5, var11);
            var7 = var5 + var11;
            String var9 = getCharacter(var3, var8);
            if (var9.startsWith("CTRL_")) {
               var2 = var3;
               Decoder.Table var10 = getTable(var9.charAt(5));
               var5 = var7;
               var3 = var10;
               if (var9.charAt(6) == 'L') {
                  var2 = var10;
                  var5 = var7;
                  var3 = var10;
               }
            } else {
               var4.append(var9);
               var3 = var2;
               var5 = var7;
            }
         } else {
            if (var1 - var5 < 5) {
               break;
            }

            int var6 = readCode(var0, var5, 5);
            var7 = var5 + 5;
            var5 = var7;
            var8 = var6;
            if (var6 == 0) {
               if (var1 - var7 < 11) {
                  break;
               }

               var8 = readCode(var0, var7, 11) + 31;
               var5 = var7 + 11;
            }

            var6 = 0;

            while(true) {
               var7 = var5;
               if (var6 >= var8) {
                  break;
               }

               if (var1 - var5 < 8) {
                  var7 = var1;
                  break;
               }

               var4.append((char)readCode(var0, var5, 8));
               var5 += 8;
               ++var6;
            }

            var3 = var2;
            var5 = var7;
         }
      }

      return var4.toString();
   }

   private static Decoder.Table getTable(char var0) {
      Decoder.Table var1;
      switch(var0) {
      case 'B':
         var1 = Decoder.Table.BINARY;
         break;
      case 'D':
         var1 = Decoder.Table.DIGIT;
         break;
      case 'L':
         var1 = Decoder.Table.LOWER;
         break;
      case 'M':
         var1 = Decoder.Table.MIXED;
         break;
      case 'P':
         var1 = Decoder.Table.PUNCT;
         break;
      default:
         var1 = Decoder.Table.UPPER;
      }

      return var1;
   }

   public static String highLevelDecode(boolean[] var0) {
      return getEncodedData(var0);
   }

   private static byte readByte(boolean[] var0, int var1) {
      int var2 = var0.length - var1;
      byte var3;
      byte var4;
      if (var2 >= 8) {
         var4 = (byte)readCode(var0, var1, 8);
         var3 = var4;
      } else {
         var4 = (byte)(readCode(var0, var1, var2) << 8 - var2);
         var3 = var4;
      }

      return var3;
   }

   private static int readCode(boolean[] var0, int var1, int var2) {
      int var3 = 0;

      for(int var4 = var1; var4 < var1 + var2; ++var4) {
         int var5 = var3 << 1;
         var3 = var5;
         if (var0[var4]) {
            var3 = var5 | 1;
         }
      }

      return var3;
   }

   private static int totalBitsInLayer(int var0, boolean var1) {
      byte var2;
      if (var1) {
         var2 = 88;
      } else {
         var2 = 112;
      }

      return (var2 + (var0 << 4)) * var0;
   }

   public DecoderResult decode(AztecDetectorResult var1) throws FormatException {
      this.ddata = var1;
      boolean[] var3 = this.correctBits(this.extractBits(var1.getBits()));
      DecoderResult var2 = new DecoderResult(convertBoolArrayToByteArray(var3), getEncodedData(var3), (List)null, (String)null);
      var2.setNumBits(var3.length);
      return var2;
   }

   private static enum Table {
      BINARY,
      DIGIT,
      LOWER,
      MIXED,
      PUNCT,
      UPPER;
   }
}
