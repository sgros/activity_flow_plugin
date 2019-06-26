package com.google.zxing.pdf417.decoder;

import com.google.zxing.FormatException;
import com.google.zxing.common.CharacterSetECI;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.pdf417.PDF417ResultMetadata;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

final class DecodedBitStreamParser {
   private static final int AL = 28;
   private static final int AS = 27;
   private static final int BEGIN_MACRO_PDF417_CONTROL_BLOCK = 928;
   private static final int BEGIN_MACRO_PDF417_OPTIONAL_FIELD = 923;
   private static final int BYTE_COMPACTION_MODE_LATCH = 901;
   private static final int BYTE_COMPACTION_MODE_LATCH_6 = 924;
   private static final Charset DEFAULT_ENCODING = Charset.forName("ISO-8859-1");
   private static final int ECI_CHARSET = 927;
   private static final int ECI_GENERAL_PURPOSE = 926;
   private static final int ECI_USER_DEFINED = 925;
   private static final BigInteger[] EXP900;
   private static final int LL = 27;
   private static final int MACRO_PDF417_TERMINATOR = 922;
   private static final int MAX_NUMERIC_CODEWORDS = 15;
   private static final char[] MIXED_CHARS = "0123456789&\r\t,:#-.$/+%*=^".toCharArray();
   private static final int ML = 28;
   private static final int MODE_SHIFT_TO_BYTE_COMPACTION_MODE = 913;
   private static final int NUMBER_OF_SEQUENCE_CODEWORDS = 2;
   private static final int NUMERIC_COMPACTION_MODE_LATCH = 902;
   private static final int PAL = 29;
   private static final int PL = 25;
   private static final int PS = 29;
   private static final char[] PUNCT_CHARS = ";<>@[\\]_`~!\r\t,:\n-.$/\"|*()?{}'".toCharArray();
   private static final int TEXT_COMPACTION_MODE_LATCH = 900;

   static {
      BigInteger[] var0 = new BigInteger[16];
      EXP900 = var0;
      var0[0] = BigInteger.ONE;
      BigInteger var2 = BigInteger.valueOf(900L);
      EXP900[1] = var2;

      for(int var1 = 2; var1 < EXP900.length; ++var1) {
         EXP900[var1] = EXP900[var1 - 1].multiply(var2);
      }

   }

   private DecodedBitStreamParser() {
   }

   private static int byteCompaction(int var0, int[] var1, Charset var2, int var3, StringBuilder var4) {
      ByteArrayOutputStream var5 = new ByteArrayOutputStream();
      long var7;
      int var11;
      int var18;
      if (var0 == 901) {
         byte var6 = 0;
         var7 = 0L;
         int[] var9 = new int[6];
         boolean var10 = false;
         var11 = var1[var3];
         var0 = var3 + 1;
         var3 = var6;

         label121:
         while(true) {
            while(true) {
               while(var0 < var1[0] && !var10) {
                  int var16 = var3 + 1;
                  var9[var3] = var11;
                  var7 = 900L * var7 + (long)var11;
                  var3 = var0 + 1;
                  var11 = var1[var0];
                  if (var11 != 900 && var11 != 901 && var11 != 902 && var11 != 924 && var11 != 928 && var11 != 923 && var11 != 922) {
                     if (var16 % 5 == 0 && var16 > 0) {
                        for(var0 = 0; var0 < 6; ++var0) {
                           var5.write((byte)((int)(var7 >> (5 - var0) * 8)));
                        }

                        var7 = 0L;
                        var6 = 0;
                        var0 = var3;
                        var3 = var6;
                     } else {
                        var0 = var3;
                        var3 = var16;
                     }
                  } else {
                     var0 = var3 - 1;
                     var10 = true;
                     var3 = var16;
                  }
               }

               var18 = var3;
               if (var0 == var1[0]) {
                  var18 = var3;
                  if (var11 < 900) {
                     var9[var3] = var11;
                     var18 = var3 + 1;
                  }
               }

               var3 = 0;

               while(true) {
                  var11 = var0;
                  if (var3 >= var18) {
                     break label121;
                  }

                  var5.write((byte)var9[var3]);
                  ++var3;
               }
            }
         }
      } else {
         var11 = var3;
         if (var0 == 924) {
            byte var15 = 0;
            var7 = 0L;
            boolean var17 = false;
            var18 = var3;
            var3 = var15;

            label85:
            while(true) {
               int var12;
               long var13;
               boolean var19;
               do {
                  do {
                     var11 = var18;
                     if (var18 >= var1[0]) {
                        break label85;
                     }

                     var11 = var18;
                     if (var17) {
                        break label85;
                     }

                     var0 = var18 + 1;
                     var18 = var1[var18];
                     if (var18 < 900) {
                        var12 = var3 + 1;
                        var13 = 900L * var7 + (long)var18;
                        var19 = var17;
                     } else if (var18 != 900 && var18 != 901 && var18 != 902 && var18 != 924 && var18 != 928 && var18 != 923 && var18 != 922) {
                        var12 = var3;
                        var19 = var17;
                        var13 = var7;
                     } else {
                        --var0;
                        var19 = true;
                        var12 = var3;
                        var13 = var7;
                     }

                     var3 = var12;
                     var17 = var19;
                     var7 = var13;
                     var18 = var0;
                  } while(var12 % 5 != 0);

                  var3 = var12;
                  var17 = var19;
                  var7 = var13;
                  var18 = var0;
               } while(var12 <= 0);

               for(var3 = 0; var3 < 6; ++var3) {
                  var5.write((byte)((int)(var13 >> (5 - var3) * 8)));
               }

               var7 = 0L;
               var3 = 0;
               var17 = var19;
               var18 = var0;
            }
         }
      }

      var4.append(new String(var5.toByteArray(), var2));
      return var11;
   }

   static DecoderResult decode(int[] var0, String var1) throws FormatException {
      StringBuilder var2 = new StringBuilder(var0.length << 1);
      Charset var3 = DEFAULT_ENCODING;
      int var4 = 1 + 1;
      int var5 = var0[1];

      PDF417ResultMetadata var6;
      int var7;
      for(var6 = new PDF417ResultMetadata(); var4 < var0[0]; var4 = var7) {
         switch(var5) {
         case 900:
            var5 = textCompaction(var0, var4, var2);
            break;
         case 901:
         case 924:
            var5 = byteCompaction(var5, var0, var3, var4, var2);
            break;
         case 902:
            var5 = numericCompaction(var0, var4, var2);
            break;
         case 913:
            var2.append((char)var0[var4]);
            var5 = var4 + 1;
            break;
         case 922:
         case 923:
            throw FormatException.getFormatInstance();
         case 925:
            var5 = var4 + 1;
            break;
         case 926:
            var5 = var4 + 2;
            break;
         case 927:
            var3 = Charset.forName(CharacterSetECI.getCharacterSetECIByValue(var0[var4]).name());
            var5 = var4 + 1;
            break;
         case 928:
            var5 = decodeMacroBlock(var0, var4, var6);
            break;
         default:
            var5 = textCompaction(var0, var4 - 1, var2);
         }

         if (var5 >= var0.length) {
            throw FormatException.getFormatInstance();
         }

         var4 = var0[var5];
         var7 = var5 + 1;
         var5 = var4;
      }

      if (var2.length() == 0) {
         throw FormatException.getFormatInstance();
      } else {
         DecoderResult var8 = new DecoderResult((byte[])null, var2.toString(), (List)null, var1);
         var8.setOther(var6);
         return var8;
      }
   }

   private static String decodeBase900toBase10(int[] var0, int var1) throws FormatException {
      BigInteger var2 = BigInteger.ZERO;

      for(int var3 = 0; var3 < var1; ++var3) {
         var2 = var2.add(EXP900[var1 - var3 - 1].multiply(BigInteger.valueOf((long)var0[var3])));
      }

      String var4 = var2.toString();
      if (var4.charAt(0) != '1') {
         throw FormatException.getFormatInstance();
      } else {
         return var4.substring(1);
      }
   }

   private static int decodeMacroBlock(int[] var0, int var1, PDF417ResultMetadata var2) throws FormatException {
      if (var1 + 2 > var0[0]) {
         throw FormatException.getFormatInstance();
      } else {
         int[] var3 = new int[2];

         int var4;
         for(var4 = 0; var4 < 2; ++var1) {
            var3[var4] = var0[var1];
            ++var4;
         }

         var2.setSegmentIndex(Integer.parseInt(decodeBase900toBase10(var3, 2)));
         StringBuilder var7 = new StringBuilder();
         var4 = textCompaction(var0, var1, var7);
         var2.setFileId(var7.toString());
         if (var0[var4] == 923) {
            var1 = var4 + 1;
            var3 = new int[var0[0] - var1];
            int var5 = 0;
            boolean var8 = false;

            while(var1 < var0[0] && !var8) {
               int var6 = var1 + 1;
               var1 = var0[var1];
               if (var1 < 900) {
                  var3[var5] = var1;
                  ++var5;
                  var1 = var6;
               } else {
                  switch(var1) {
                  case 922:
                     var2.setLastSegment(true);
                     var1 = var6 + 1;
                     var8 = true;
                     break;
                  default:
                     throw FormatException.getFormatInstance();
                  }
               }
            }

            var2.setOptionalData(Arrays.copyOf(var3, var5));
         } else {
            var1 = var4;
            if (var0[var4] == 922) {
               var2.setLastSegment(true);
               var1 = var4 + 1;
            }
         }

         return var1;
      }
   }

   private static void decodeTextCompaction(int[] var0, int[] var1, int var2, StringBuilder var3) {
      DecodedBitStreamParser.Mode var4 = DecodedBitStreamParser.Mode.ALPHA;
      DecodedBitStreamParser.Mode var5 = DecodedBitStreamParser.Mode.ALPHA;

      DecodedBitStreamParser.Mode var9;
      for(int var6 = 0; var6 < var2; var5 = var9) {
         int var7 = var0[var6];
         byte var8 = 0;
         char var10;
         char var11;
         byte var12;
         switch(var4) {
         case ALPHA:
            if (var7 < 26) {
               var11 = (char)(var7 + 65);
               var10 = var11;
               var9 = var5;
            } else if (var7 == 26) {
               var12 = 32;
               var10 = (char)var12;
               var9 = var5;
            } else if (var7 == 27) {
               var4 = DecodedBitStreamParser.Mode.LOWER;
               var10 = (char)var8;
               var9 = var5;
            } else if (var7 == 28) {
               var4 = DecodedBitStreamParser.Mode.MIXED;
               var10 = (char)var8;
               var9 = var5;
            } else if (var7 == 29) {
               var5 = DecodedBitStreamParser.Mode.PUNCT_SHIFT;
               var10 = (char)var8;
               var9 = var4;
               var4 = var5;
            } else if (var7 == 913) {
               var3.append((char)var1[var6]);
               var10 = (char)var8;
               var9 = var5;
            } else {
               var10 = (char)var8;
               var9 = var5;
               if (var7 == 900) {
                  var4 = DecodedBitStreamParser.Mode.ALPHA;
                  var10 = (char)var8;
                  var9 = var5;
               }
            }
            break;
         case LOWER:
            if (var7 < 26) {
               var11 = (char)(var7 + 97);
               var10 = var11;
               var9 = var5;
            } else if (var7 == 26) {
               var12 = 32;
               var10 = (char)var12;
               var9 = var5;
            } else if (var7 == 27) {
               var5 = DecodedBitStreamParser.Mode.ALPHA_SHIFT;
               var10 = (char)var8;
               var9 = var4;
               var4 = var5;
            } else if (var7 == 28) {
               var4 = DecodedBitStreamParser.Mode.MIXED;
               var10 = (char)var8;
               var9 = var5;
            } else if (var7 == 29) {
               var5 = DecodedBitStreamParser.Mode.PUNCT_SHIFT;
               var10 = (char)var8;
               var9 = var4;
               var4 = var5;
            } else if (var7 == 913) {
               var3.append((char)var1[var6]);
               var10 = (char)var8;
               var9 = var5;
            } else {
               var10 = (char)var8;
               var9 = var5;
               if (var7 == 900) {
                  var4 = DecodedBitStreamParser.Mode.ALPHA;
                  var10 = (char)var8;
                  var9 = var5;
               }
            }
            break;
         case MIXED:
            if (var7 < 25) {
               var11 = MIXED_CHARS[var7];
               var10 = var11;
               var9 = var5;
            } else if (var7 == 25) {
               var4 = DecodedBitStreamParser.Mode.PUNCT;
               var10 = (char)var8;
               var9 = var5;
            } else if (var7 == 26) {
               var12 = 32;
               var10 = (char)var12;
               var9 = var5;
            } else if (var7 == 27) {
               var4 = DecodedBitStreamParser.Mode.LOWER;
               var10 = (char)var8;
               var9 = var5;
            } else if (var7 == 28) {
               var4 = DecodedBitStreamParser.Mode.ALPHA;
               var10 = (char)var8;
               var9 = var5;
            } else if (var7 == 29) {
               var5 = DecodedBitStreamParser.Mode.PUNCT_SHIFT;
               var10 = (char)var8;
               var9 = var4;
               var4 = var5;
            } else if (var7 == 913) {
               var3.append((char)var1[var6]);
               var10 = (char)var8;
               var9 = var5;
            } else {
               var10 = (char)var8;
               var9 = var5;
               if (var7 == 900) {
                  var4 = DecodedBitStreamParser.Mode.ALPHA;
                  var10 = (char)var8;
                  var9 = var5;
               }
            }
            break;
         case PUNCT:
            if (var7 < 29) {
               var11 = PUNCT_CHARS[var7];
               var10 = var11;
               var9 = var5;
            } else if (var7 == 29) {
               var4 = DecodedBitStreamParser.Mode.ALPHA;
               var10 = (char)var8;
               var9 = var5;
            } else if (var7 == 913) {
               var3.append((char)var1[var6]);
               var10 = (char)var8;
               var9 = var5;
            } else {
               var10 = (char)var8;
               var9 = var5;
               if (var7 == 900) {
                  var4 = DecodedBitStreamParser.Mode.ALPHA;
                  var10 = (char)var8;
                  var9 = var5;
               }
            }
            break;
         case ALPHA_SHIFT:
            var4 = var5;
            if (var7 < 26) {
               var11 = (char)(var7 + 65);
               var10 = var11;
               var9 = var5;
            } else if (var7 == 26) {
               var12 = 32;
               var10 = (char)var12;
               var9 = var5;
            } else {
               var10 = (char)var8;
               var9 = var5;
               if (var7 == 900) {
                  var4 = DecodedBitStreamParser.Mode.ALPHA;
                  var10 = (char)var8;
                  var9 = var5;
               }
            }
            break;
         case PUNCT_SHIFT:
            var4 = var5;
            if (var7 < 29) {
               var11 = PUNCT_CHARS[var7];
               var10 = var11;
               var9 = var5;
            } else if (var7 == 29) {
               var4 = DecodedBitStreamParser.Mode.ALPHA;
               var10 = (char)var8;
               var9 = var5;
            } else if (var7 == 913) {
               var3.append((char)var1[var6]);
               var10 = (char)var8;
               var9 = var5;
            } else {
               var10 = (char)var8;
               var9 = var5;
               if (var7 == 900) {
                  var4 = DecodedBitStreamParser.Mode.ALPHA;
                  var10 = (char)var8;
                  var9 = var5;
               }
            }
            break;
         default:
            var9 = var5;
            var10 = (char)var8;
         }

         if (var10 != 0) {
            var3.append(var10);
         }

         ++var6;
      }

   }

   private static int numericCompaction(int[] var0, int var1, StringBuilder var2) throws FormatException {
      int var3 = 0;
      boolean var4 = false;
      int[] var5 = new int[15];
      int var6 = var1;

      while(var6 < var0[0] && !var4) {
         var1 = var6 + 1;
         var6 = var0[var6];
         boolean var7 = var4;
         if (var1 == var0[0]) {
            var7 = true;
         }

         int var8;
         if (var6 < 900) {
            var5[var3] = var6;
            var8 = var3 + 1;
         } else if (var6 != 900 && var6 != 901 && var6 != 924 && var6 != 928 && var6 != 923 && var6 != 922) {
            var8 = var3;
         } else {
            --var1;
            var7 = true;
            var8 = var3;
         }

         if (var8 % 15 != 0 && var6 != 902) {
            var3 = var8;
            var4 = var7;
            var6 = var1;
            if (!var7) {
               continue;
            }
         }

         var3 = var8;
         var4 = var7;
         var6 = var1;
         if (var8 > 0) {
            var2.append(decodeBase900toBase10(var5, var8));
            var3 = 0;
            var4 = var7;
            var6 = var1;
         }
      }

      return var6;
   }

   private static int textCompaction(int[] var0, int var1, StringBuilder var2) {
      int[] var3 = new int[var0[0] - var1 << 1];
      int[] var4 = new int[var0[0] - var1 << 1];
      int var5 = 0;
      boolean var6 = false;

      while(var1 < var0[0] && !var6) {
         int var7 = var1 + 1;
         var1 = var0[var1];
         if (var1 < 900) {
            var3[var5] = var1 / 30;
            var3[var5 + 1] = var1 % 30;
            var5 += 2;
            var1 = var7;
         } else {
            switch(var1) {
            case 900:
               var3[var5] = 900;
               ++var5;
               var1 = var7;
               break;
            case 901:
            case 902:
            case 922:
            case 923:
            case 924:
            case 928:
               var1 = var7 - 1;
               var6 = true;
               break;
            case 913:
               var3[var5] = 913;
               var1 = var7 + 1;
               var4[var5] = var0[var7];
               ++var5;
               break;
            default:
               var1 = var7;
            }
         }
      }

      decodeTextCompaction(var3, var4, var5, var2);
      return var1;
   }

   private static enum Mode {
      ALPHA,
      ALPHA_SHIFT,
      LOWER,
      MIXED,
      PUNCT,
      PUNCT_SHIFT;
   }
}
