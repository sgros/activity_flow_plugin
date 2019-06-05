package com.google.zxing.pdf417.encoder;

import com.google.zxing.WriterException;
import com.google.zxing.common.CharacterSetECI;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;

final class PDF417HighLevelEncoder {
   private static final int BYTE_COMPACTION = 1;
   private static final Charset DEFAULT_ENCODING = Charset.forName("ISO-8859-1");
   private static final int ECI_CHARSET = 927;
   private static final int ECI_GENERAL_PURPOSE = 926;
   private static final int ECI_USER_DEFINED = 925;
   private static final int LATCH_TO_BYTE = 924;
   private static final int LATCH_TO_BYTE_PADDED = 901;
   private static final int LATCH_TO_NUMERIC = 902;
   private static final int LATCH_TO_TEXT = 900;
   private static final byte[] MIXED = new byte[128];
   private static final int NUMERIC_COMPACTION = 2;
   private static final byte[] PUNCTUATION = new byte[128];
   private static final int SHIFT_TO_BYTE = 913;
   private static final int SUBMODE_ALPHA = 0;
   private static final int SUBMODE_LOWER = 1;
   private static final int SUBMODE_MIXED = 2;
   private static final int SUBMODE_PUNCTUATION = 3;
   private static final int TEXT_COMPACTION = 0;
   private static final byte[] TEXT_MIXED_RAW = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 38, 13, 9, 44, 58, 35, 45, 46, 36, 47, 43, 37, 42, 61, 94, 0, 32, 0, 0, 0};
   private static final byte[] TEXT_PUNCTUATION_RAW = new byte[]{59, 60, 62, 64, 91, 92, 93, 95, 96, 126, 33, 13, 9, 44, 58, 10, 45, 46, 36, 47, 34, 124, 42, 40, 41, 63, 123, 125, 39, 0};

   static {
      Arrays.fill(MIXED, (byte)-1);

      int var0;
      byte var1;
      for(var0 = 0; var0 < TEXT_MIXED_RAW.length; ++var0) {
         var1 = TEXT_MIXED_RAW[var0];
         if (var1 > 0) {
            MIXED[var1] = (byte)((byte)var0);
         }
      }

      Arrays.fill(PUNCTUATION, (byte)-1);

      for(var0 = 0; var0 < TEXT_PUNCTUATION_RAW.length; ++var0) {
         var1 = TEXT_PUNCTUATION_RAW[var0];
         if (var1 > 0) {
            PUNCTUATION[var1] = (byte)((byte)var0);
         }
      }

   }

   private PDF417HighLevelEncoder() {
   }

   private static int determineConsecutiveBinaryCount(String var0, int var1, Charset var2) throws WriterException {
      CharsetEncoder var9 = var2.newEncoder();
      int var3 = var0.length();
      int var4 = var1;

      while(true) {
         if (var4 >= var3) {
            var1 = var4 - var1;
            break;
         }

         char var5 = var0.charAt(var4);
         int var6 = 0;
         char var7 = var5;

         int var10;
         while(true) {
            var10 = var6;
            if (var6 >= 13) {
               break;
            }

            var10 = var6;
            if (!isDigit(var7)) {
               break;
            }

            ++var6;
            int var8 = var4 + var6;
            var10 = var6;
            if (var8 >= var3) {
               break;
            }

            var5 = var0.charAt(var8);
            var7 = var5;
         }

         if (var10 >= 13) {
            var1 = var4 - var1;
            break;
         }

         var7 = var0.charAt(var4);
         if (!var9.canEncode(var7)) {
            throw new WriterException("Non-encodable character detected: " + var7 + " (Unicode: " + var7 + ')');
         }

         ++var4;
      }

      return var1;
   }

   private static int determineConsecutiveDigitCount(CharSequence var0, int var1) {
      int var2 = 0;
      byte var3 = 0;
      int var4 = var0.length();
      int var5 = var1;
      if (var1 < var4) {
         char var8 = var0.charAt(var1);
         var1 = var3;
         char var6 = var8;

         while(true) {
            var2 = var1;
            if (!isDigit(var6)) {
               break;
            }

            var2 = var1;
            if (var5 >= var4) {
               break;
            }

            int var9 = var1 + 1;
            var2 = var5 + 1;
            var1 = var9;
            var5 = var2;
            if (var2 < var4) {
               char var7 = var0.charAt(var2);
               var6 = var7;
               var1 = var9;
               var5 = var2;
            }
         }
      }

      return var2;
   }

   private static int determineConsecutiveTextCount(CharSequence var0, int var1) {
      int var2 = var0.length();
      int var3 = var1;

      while(true) {
         int var4 = var3;
         if (var3 < var2) {
            char var5 = var0.charAt(var3);
            var4 = 0;
            int var6 = var3;
            char var7 = var5;

            while(var4 < 13 && isDigit(var7) && var6 < var2) {
               int var8 = var4 + 1;
               var3 = var6 + 1;
               var6 = var3;
               var4 = var8;
               if (var3 < var2) {
                  char var9 = var0.charAt(var3);
                  var7 = var9;
                  var6 = var3;
                  var4 = var8;
               }
            }

            if (var4 >= 13) {
               var1 = var6 - var1 - var4;
               break;
            }

            var3 = var6;
            if (var4 > 0) {
               continue;
            }

            var4 = var6;
            if (isText(var0.charAt(var6))) {
               var3 = var6 + 1;
               continue;
            }
         }

         var1 = var4 - var1;
         break;
      }

      return var1;
   }

   private static void encodeBinary(byte[] var0, int var1, int var2, int var3, StringBuilder var4) {
      if (var2 == 1 && var3 == 0) {
         var4.append('Α');
      } else if (var2 % 6 == 0) {
         var4.append('Μ');
      } else {
         var4.append('΅');
      }

      var3 = var1;
      int var5 = var1;
      if (var2 >= 6) {
         char[] var6 = new char[5];

         while(true) {
            var5 = var3;
            if (var1 + var2 - var3 < 6) {
               break;
            }

            long var7 = 0L;

            for(var5 = 0; var5 < 6; ++var5) {
               var7 = (var7 << 8) + (long)(var0[var3 + var5] & 255);
            }

            for(var5 = 0; var5 < 5; ++var5) {
               var6[var5] = (char)((char)((int)(var7 % 900L)));
               var7 /= 900L;
            }

            for(var5 = 4; var5 >= 0; --var5) {
               var4.append(var6[var5]);
            }

            var3 += 6;
         }
      }

      while(var5 < var1 + var2) {
         var4.append((char)(var0[var5] & 255));
         ++var5;
      }

   }

   static String encodeHighLevel(String var0, Compaction var1, Charset var2) throws WriterException {
      StringBuilder var3 = new StringBuilder(var0.length());
      Charset var4;
      if (var2 == null) {
         var4 = DEFAULT_ENCODING;
      } else {
         var4 = var2;
         if (!DEFAULT_ENCODING.equals(var2)) {
            CharacterSetECI var8 = CharacterSetECI.getCharacterSetECIByName(var2.name());
            var4 = var2;
            if (var8 != null) {
               encodingECI(var8.getValue(), var3);
               var4 = var2;
            }
         }
      }

      int var5 = var0.length();
      int var6 = 0;
      int var7 = 0;
      if (var1 == Compaction.TEXT) {
         encodeText(var0, 0, var5, var3, 0);
      } else if (var1 == Compaction.BYTE) {
         byte[] var12 = var0.getBytes(var4);
         encodeBinary(var12, 0, var12.length, 1, var3);
      } else if (var1 == Compaction.NUMERIC) {
         var3.append('Ά');
         encodeNumeric(var0, 0, var5, var3);
      } else {
         byte var9 = 0;

         while(true) {
            while(var6 < var5) {
               int var10 = determineConsecutiveDigitCount(var0, var6);
               if (var10 >= 13) {
                  var3.append('Ά');
                  var9 = 2;
                  var7 = 0;
                  encodeNumeric(var0, var6, var10, var3);
                  var6 += var10;
               } else {
                  int var11 = determineConsecutiveTextCount(var0, var6);
                  if (var11 < 5 && var10 != var5) {
                     var11 = determineConsecutiveBinaryCount(var0, var6, var4);
                     var10 = var11;
                     if (var11 == 0) {
                        var10 = 1;
                     }

                     byte[] var13 = var0.substring(var6, var6 + var10).getBytes(var4);
                     if (var13.length == 1 && var9 == 0) {
                        encodeBinary(var13, 0, 1, 0, var3);
                     } else {
                        encodeBinary(var13, 0, var13.length, var9, var3);
                        var9 = 1;
                        var7 = 0;
                     }

                     var6 += var10;
                  } else {
                     byte var14 = var9;
                     if (var9 != 0) {
                        var3.append('΄');
                        var14 = 0;
                        var7 = 0;
                     }

                     var7 = encodeText(var0, var6, var11, var3, var7);
                     var6 += var11;
                     var9 = var14;
                  }
               }
            }

            return var3.toString();
         }
      }

      return var3.toString();
   }

   private static void encodeNumeric(String var0, int var1, int var2, StringBuilder var3) {
      int var4 = 0;
      StringBuilder var5 = new StringBuilder(var2 / 3 + 1);
      BigInteger var6 = BigInteger.valueOf(900L);

      int var8;
      for(BigInteger var7 = BigInteger.valueOf(0L); var4 < var2; var4 += var8) {
         var5.setLength(0);
         var8 = Math.min(44, var2 - var4);
         BigInteger var9 = new BigInteger("1" + var0.substring(var1 + var4, var1 + var4 + var8));

         BigInteger var10;
         do {
            var5.append((char)var9.mod(var6).intValue());
            var10 = var9.divide(var6);
            var9 = var10;
         } while(!var10.equals(var7));

         for(int var11 = var5.length() - 1; var11 >= 0; --var11) {
            var3.append(var5.charAt(var11));
         }
      }

   }

   private static int encodeText(CharSequence var0, int var1, int var2, StringBuilder var3, int var4) {
      StringBuilder var5 = new StringBuilder(var2);
      int var6 = 0;

      while(true) {
         while(true) {
            char var7 = var0.charAt(var1 + var6);
            switch(var4) {
            case 0:
               if (isAlphaUpper(var7)) {
                  if (var7 == ' ') {
                     var5.append('\u001a');
                  } else {
                     var5.append((char)(var7 - 65));
                  }
               } else {
                  if (isAlphaLower(var7)) {
                     var4 = 1;
                     var5.append('\u001b');
                     continue;
                  }

                  if (isMixed(var7)) {
                     var4 = 2;
                     var5.append('\u001c');
                     continue;
                  }

                  var5.append('\u001d');
                  var5.append((char)PUNCTUATION[var7]);
               }
               break;
            case 1:
               if (isAlphaLower(var7)) {
                  if (var7 == ' ') {
                     var5.append('\u001a');
                  } else {
                     var5.append((char)(var7 - 97));
                  }
               } else if (isAlphaUpper(var7)) {
                  var5.append('\u001b');
                  var5.append((char)(var7 - 65));
               } else {
                  if (isMixed(var7)) {
                     var4 = 2;
                     var5.append('\u001c');
                     continue;
                  }

                  var5.append('\u001d');
                  var5.append((char)PUNCTUATION[var7]);
               }
               break;
            case 2:
               if (isMixed(var7)) {
                  var5.append((char)MIXED[var7]);
               } else {
                  if (isAlphaUpper(var7)) {
                     var4 = 0;
                     var5.append('\u001c');
                     continue;
                  }

                  if (isAlphaLower(var7)) {
                     var4 = 1;
                     var5.append('\u001b');
                     continue;
                  }

                  if (var1 + var6 + 1 < var2 && isPunctuation(var0.charAt(var1 + var6 + 1))) {
                     var4 = 3;
                     var5.append('\u0019');
                     continue;
                  }

                  var5.append('\u001d');
                  var5.append((char)PUNCTUATION[var7]);
               }
               break;
            default:
               if (!isPunctuation(var7)) {
                  var4 = 0;
                  var5.append('\u001d');
                  continue;
               }

               var5.append((char)PUNCTUATION[var7]);
            }

            int var8 = var6 + 1;
            var6 = var8;
            if (var8 >= var2) {
               char var9 = 0;
               var8 = var5.length();

               for(var1 = 0; var1 < var8; ++var1) {
                  boolean var10;
                  if (var1 % 2 != 0) {
                     var10 = true;
                  } else {
                     var10 = false;
                  }

                  if (var10) {
                     var7 = (char)(var9 * 30 + var5.charAt(var1));
                     var3.append(var7);
                     var9 = var7;
                  } else {
                     var9 = var5.charAt(var1);
                  }
               }

               if (var8 % 2 != 0) {
                  var3.append((char)(var9 * 30 + 29));
               }

               return var4;
            }
         }
      }
   }

   private static void encodingECI(int var0, StringBuilder var1) throws WriterException {
      if (var0 >= 0 && var0 < 900) {
         var1.append('Ο');
         var1.append((char)var0);
      } else if (var0 < 810900) {
         var1.append('Ξ');
         var1.append((char)(var0 / 900 - 1));
         var1.append((char)(var0 % 900));
      } else {
         if (var0 >= 811800) {
            throw new WriterException("ECI number not in valid range from 0..811799, but was " + var0);
         }

         var1.append('Ν');
         var1.append((char)(810900 - var0));
      }

   }

   private static boolean isAlphaLower(char var0) {
      boolean var1;
      if (var0 != ' ' && (var0 < 'a' || var0 > 'z')) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private static boolean isAlphaUpper(char var0) {
      boolean var1;
      if (var0 != ' ' && (var0 < 'A' || var0 > 'Z')) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private static boolean isDigit(char var0) {
      boolean var1;
      if (var0 >= '0' && var0 <= '9') {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private static boolean isMixed(char var0) {
      boolean var1;
      if (MIXED[var0] != -1) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private static boolean isPunctuation(char var0) {
      boolean var1;
      if (PUNCTUATION[var0] != -1) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private static boolean isText(char var0) {
      boolean var1;
      if (var0 != '\t' && var0 != '\n' && var0 != '\r' && (var0 < ' ' || var0 > '~')) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }
}
