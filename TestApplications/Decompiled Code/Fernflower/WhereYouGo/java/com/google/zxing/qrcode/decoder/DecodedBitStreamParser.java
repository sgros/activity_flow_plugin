package com.google.zxing.qrcode.decoder;

import com.google.zxing.FormatException;
import com.google.zxing.common.BitSource;
import com.google.zxing.common.CharacterSetECI;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.common.StringUtils;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

final class DecodedBitStreamParser {
   private static final char[] ALPHANUMERIC_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ $%*+-./:".toCharArray();
   private static final int GB2312_SUBSET = 1;

   private DecodedBitStreamParser() {
   }

   static DecoderResult decode(byte[] var0, Version var1, ErrorCorrectionLevel var2, Map var3) throws FormatException {
      BitSource var4 = new BitSource(var0);
      StringBuilder var5 = new StringBuilder(50);
      ArrayList var6 = new ArrayList(1);
      int var7 = -1;
      int var8 = -1;
      CharacterSetECI var9 = null;
      boolean var10 = false;

      while(true) {
         Mode var11;
         boolean var10001;
         label177: {
            try {
               if (var4.available() < 4) {
                  var11 = Mode.TERMINATOR;
                  break label177;
               }
            } catch (IllegalArgumentException var33) {
               var10001 = false;
               break;
            }

            try {
               var11 = Mode.forBits(var4.readBits(4));
            } catch (IllegalArgumentException var32) {
               var10001 = false;
               break;
            }
         }

         CharacterSetECI var12 = var9;
         int var13 = var7;
         int var14 = var8;
         boolean var15 = var10;

         label184: {
            label166: {
               label185: {
                  try {
                     if (var11 == Mode.TERMINATOR) {
                        break label166;
                     }

                     if (var11 == Mode.FNC1_FIRST_POSITION || var11 == Mode.FNC1_SECOND_POSITION) {
                        break label185;
                     }
                  } catch (IllegalArgumentException var31) {
                     var10001 = false;
                     break;
                  }

                  label187: {
                     try {
                        if (var11 != Mode.STRUCTURED_APPEND) {
                           break label187;
                        }

                        if (var4.available() < 16) {
                           throw FormatException.getFormatInstance();
                        }
                     } catch (IllegalArgumentException var30) {
                        var10001 = false;
                        break;
                     }

                     try {
                        var13 = var4.readBits(8);
                        var14 = var4.readBits(8);
                     } catch (IllegalArgumentException var23) {
                        var10001 = false;
                        break;
                     }

                     var12 = var9;
                     var15 = var10;
                     break label166;
                  }

                  label189: {
                     try {
                        if (var11 == Mode.ECI) {
                           var9 = CharacterSetECI.getCharacterSetECIByValue(parseECIValue(var4));
                           break label189;
                        }
                     } catch (IllegalArgumentException var29) {
                        var10001 = false;
                        break;
                     }

                     int var17;
                     int var18;
                     label190: {
                        try {
                           if (var11 == Mode.HANZI) {
                              var17 = var4.readBits(4);
                              var18 = var4.readBits(var11.getCharacterCountBits(var1));
                              break label190;
                           }
                        } catch (IllegalArgumentException var28) {
                           var10001 = false;
                           break;
                        }

                        label191: {
                           try {
                              var13 = var4.readBits(var11.getCharacterCountBits(var1));
                              if (var11 == Mode.NUMERIC) {
                                 decodeNumericSegment(var4, var5, var13);
                                 break label191;
                              }
                           } catch (IllegalArgumentException var27) {
                              var10001 = false;
                              break;
                           }

                           label192: {
                              try {
                                 if (var11 == Mode.ALPHANUMERIC) {
                                    decodeAlphanumericSegment(var4, var5, var13, var10);
                                    break label192;
                                 }
                              } catch (IllegalArgumentException var26) {
                                 var10001 = false;
                                 break;
                              }

                              label193: {
                                 try {
                                    if (var11 == Mode.BYTE) {
                                       decodeByteSegment(var4, var5, var13, var9, var6, var3);
                                       break label193;
                                    }
                                 } catch (IllegalArgumentException var25) {
                                    var10001 = false;
                                    break;
                                 }

                                 try {
                                    if (var11 != Mode.KANJI) {
                                       break label184;
                                    }

                                    decodeKanjiSegment(var4, var5, var13);
                                 } catch (IllegalArgumentException var24) {
                                    var10001 = false;
                                    break;
                                 }

                                 var12 = var9;
                                 var13 = var7;
                                 var14 = var8;
                                 var15 = var10;
                                 break label166;
                              }

                              var12 = var9;
                              var13 = var7;
                              var14 = var8;
                              var15 = var10;
                              break label166;
                           }

                           var12 = var9;
                           var13 = var7;
                           var14 = var8;
                           var15 = var10;
                           break label166;
                        }

                        var12 = var9;
                        var13 = var7;
                        var14 = var8;
                        var15 = var10;
                        break label166;
                     }

                     var12 = var9;
                     var13 = var7;
                     var14 = var8;
                     var15 = var10;
                     if (var17 == 1) {
                        try {
                           decodeHanziSegment(var4, var5, var18);
                        } catch (IllegalArgumentException var22) {
                           var10001 = false;
                           break;
                        }

                        var12 = var9;
                        var13 = var7;
                        var14 = var8;
                        var15 = var10;
                     }
                     break label166;
                  }

                  var12 = var9;
                  var13 = var7;
                  var14 = var8;
                  var15 = var10;
                  if (var9 == null) {
                     try {
                        throw FormatException.getFormatInstance();
                     } catch (IllegalArgumentException var19) {
                        var10001 = false;
                        break;
                     }
                  }
                  break label166;
               }

               var15 = true;
               var14 = var8;
               var13 = var7;
               var12 = var9;
            }

            Mode var16;
            try {
               var16 = Mode.TERMINATOR;
            } catch (IllegalArgumentException var21) {
               var10001 = false;
               break;
            }

            var9 = var12;
            var7 = var13;
            var8 = var14;
            var10 = var15;
            if (var11 == var16) {
               String var36 = var5.toString();
               ArrayList var34;
               if (var6.isEmpty()) {
                  var34 = null;
               } else {
                  var34 = var6;
               }

               String var35;
               if (var2 == null) {
                  var35 = null;
               } else {
                  var35 = var2.toString();
               }

               return new DecoderResult(var0, var36, var34, var35, var13, var14);
            }
            continue;
         }

         try {
            throw FormatException.getFormatInstance();
         } catch (IllegalArgumentException var20) {
            var10001 = false;
            break;
         }
      }

      throw FormatException.getFormatInstance();
   }

   private static void decodeAlphanumericSegment(BitSource var0, StringBuilder var1, int var2, boolean var3) throws FormatException {
      int var4;
      for(var4 = var1.length(); var2 > 1; var2 -= 2) {
         if (var0.available() < 11) {
            throw FormatException.getFormatInstance();
         }

         int var5 = var0.readBits(11);
         var1.append(toAlphaNumericChar(var5 / 45));
         var1.append(toAlphaNumericChar(var5 % 45));
      }

      if (var2 == 1) {
         if (var0.available() < 6) {
            throw FormatException.getFormatInstance();
         }

         var1.append(toAlphaNumericChar(var0.readBits(6)));
      }

      if (var3) {
         for(var2 = var4; var2 < var1.length(); ++var2) {
            if (var1.charAt(var2) == '%') {
               if (var2 < var1.length() - 1 && var1.charAt(var2 + 1) == '%') {
                  var1.deleteCharAt(var2 + 1);
               } else {
                  var1.setCharAt(var2, '\u001d');
               }
            }
         }
      }

   }

   private static void decodeByteSegment(BitSource var0, StringBuilder var1, int var2, CharacterSetECI var3, Collection var4, Map var5) throws FormatException {
      if (var2 << 3 > var0.available()) {
         throw FormatException.getFormatInstance();
      } else {
         byte[] var6 = new byte[var2];

         for(int var7 = 0; var7 < var2; ++var7) {
            var6[var7] = (byte)((byte)var0.readBits(8));
         }

         String var9;
         if (var3 == null) {
            var9 = StringUtils.guessEncoding(var6, var5);
         } else {
            var9 = var3.name();
         }

         try {
            String var10 = new String(var6, var9);
            var1.append(var10);
         } catch (UnsupportedEncodingException var8) {
            throw FormatException.getFormatInstance();
         }

         var4.add(var6);
      }
   }

   private static void decodeHanziSegment(BitSource var0, StringBuilder var1, int var2) throws FormatException {
      if (var2 * 13 > var0.available()) {
         throw FormatException.getFormatInstance();
      } else {
         byte[] var3 = new byte[var2 * 2];

         for(int var4 = 0; var2 > 0; --var2) {
            int var5 = var0.readBits(13);
            var5 = var5 / 96 << 8 | var5 % 96;
            if (var5 < 959) {
               var5 += 41377;
            } else {
               var5 += 42657;
            }

            var3[var4] = (byte)((byte)(var5 >> 8));
            var3[var4 + 1] = (byte)((byte)var5);
            var4 += 2;
         }

         try {
            String var7 = new String(var3, "GB2312");
            var1.append(var7);
         } catch (UnsupportedEncodingException var6) {
            throw FormatException.getFormatInstance();
         }
      }
   }

   private static void decodeKanjiSegment(BitSource var0, StringBuilder var1, int var2) throws FormatException {
      if (var2 * 13 > var0.available()) {
         throw FormatException.getFormatInstance();
      } else {
         byte[] var3 = new byte[var2 * 2];

         for(int var4 = 0; var2 > 0; --var2) {
            int var5 = var0.readBits(13);
            var5 = var5 / 192 << 8 | var5 % 192;
            if (var5 < 7936) {
               var5 += 33088;
            } else {
               var5 += 49472;
            }

            var3[var4] = (byte)((byte)(var5 >> 8));
            var3[var4 + 1] = (byte)((byte)var5);
            var4 += 2;
         }

         try {
            String var7 = new String(var3, "SJIS");
            var1.append(var7);
         } catch (UnsupportedEncodingException var6) {
            throw FormatException.getFormatInstance();
         }
      }
   }

   private static void decodeNumericSegment(BitSource var0, StringBuilder var1, int var2) throws FormatException {
      while(var2 >= 3) {
         if (var0.available() < 10) {
            throw FormatException.getFormatInstance();
         }

         int var3 = var0.readBits(10);
         if (var3 >= 1000) {
            throw FormatException.getFormatInstance();
         }

         var1.append(toAlphaNumericChar(var3 / 100));
         var1.append(toAlphaNumericChar(var3 / 10 % 10));
         var1.append(toAlphaNumericChar(var3 % 10));
         var2 -= 3;
      }

      if (var2 == 2) {
         if (var0.available() < 7) {
            throw FormatException.getFormatInstance();
         }

         var2 = var0.readBits(7);
         if (var2 >= 100) {
            throw FormatException.getFormatInstance();
         }

         var1.append(toAlphaNumericChar(var2 / 10));
         var1.append(toAlphaNumericChar(var2 % 10));
      } else if (var2 == 1) {
         if (var0.available() < 4) {
            throw FormatException.getFormatInstance();
         }

         var2 = var0.readBits(4);
         if (var2 >= 10) {
            throw FormatException.getFormatInstance();
         }

         var1.append(toAlphaNumericChar(var2));
      }

   }

   private static int parseECIValue(BitSource var0) throws FormatException {
      int var1 = var0.readBits(8);
      if ((var1 & 128) == 0) {
         var1 &= 127;
      } else if ((var1 & 192) == 128) {
         var1 = (var1 & 63) << 8 | var0.readBits(8);
      } else {
         if ((var1 & 224) != 192) {
            throw FormatException.getFormatInstance();
         }

         var1 = (var1 & 31) << 16 | var0.readBits(16);
      }

      return var1;
   }

   private static char toAlphaNumericChar(int var0) throws FormatException {
      if (var0 >= ALPHANUMERIC_CHARS.length) {
         throw FormatException.getFormatInstance();
      } else {
         return ALPHANUMERIC_CHARS[var0];
      }
   }
}
