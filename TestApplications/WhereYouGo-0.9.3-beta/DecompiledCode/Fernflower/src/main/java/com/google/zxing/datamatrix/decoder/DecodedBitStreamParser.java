package com.google.zxing.datamatrix.decoder;

import com.google.zxing.FormatException;
import com.google.zxing.common.BitSource;
import com.google.zxing.common.DecoderResult;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;

final class DecodedBitStreamParser {
   private static final char[] C40_BASIC_SET_CHARS = new char[]{'*', '*', '*', ' ', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
   private static final char[] C40_SHIFT2_SET_CHARS = new char[]{'!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', ':', ';', '<', '=', '>', '?', '@', '[', '\\', ']', '^', '_'};
   private static final char[] TEXT_BASIC_SET_CHARS = new char[]{'*', '*', '*', ' ', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
   private static final char[] TEXT_SHIFT2_SET_CHARS;
   private static final char[] TEXT_SHIFT3_SET_CHARS;

   static {
      TEXT_SHIFT2_SET_CHARS = C40_SHIFT2_SET_CHARS;
      TEXT_SHIFT3_SET_CHARS = new char[]{'`', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '{', '|', '}', '~', '\u007f'};
   }

   private DecodedBitStreamParser() {
   }

   static DecoderResult decode(byte[] var0) throws FormatException {
      BitSource var1 = new BitSource(var0);
      StringBuilder var2 = new StringBuilder(100);
      StringBuilder var3 = new StringBuilder(0);
      ArrayList var4 = new ArrayList(1);
      DecodedBitStreamParser.Mode var5 = DecodedBitStreamParser.Mode.ASCII_ENCODE;

      do {
         if (var5 == DecodedBitStreamParser.Mode.ASCII_ENCODE) {
            var5 = decodeAsciiSegment(var1, var2, var3);
         } else {
            switch(var5) {
            case C40_ENCODE:
               decodeC40Segment(var1, var2);
               break;
            case TEXT_ENCODE:
               decodeTextSegment(var1, var2);
               break;
            case ANSIX12_ENCODE:
               decodeAnsiX12Segment(var1, var2);
               break;
            case EDIFACT_ENCODE:
               decodeEdifactSegment(var1, var2);
               break;
            case BASE256_ENCODE:
               decodeBase256Segment(var1, var2, var4);
               break;
            default:
               throw FormatException.getFormatInstance();
            }

            var5 = DecodedBitStreamParser.Mode.ASCII_ENCODE;
         }
      } while(var5 != DecodedBitStreamParser.Mode.PAD_ENCODE && var1.available() > 0);

      if (var3.length() > 0) {
         var2.append(var3);
      }

      String var6 = var2.toString();
      ArrayList var7 = var4;
      if (var4.isEmpty()) {
         var7 = null;
      }

      return new DecoderResult(var0, var6, var7, (String)null);
   }

   private static void decodeAnsiX12Segment(BitSource var0, StringBuilder var1) throws FormatException {
      int[] var2 = new int[3];

      while(var0.available() != 8) {
         int var3 = var0.readBits(8);
         if (var3 == 254) {
            break;
         }

         parseTwoBytes(var3, var0.readBits(8), var2);

         for(var3 = 0; var3 < 3; ++var3) {
            int var4 = var2[var3];
            if (var4 == 0) {
               var1.append('\r');
            } else if (var4 == 1) {
               var1.append('*');
            } else if (var4 == 2) {
               var1.append('>');
            } else if (var4 == 3) {
               var1.append(' ');
            } else if (var4 < 14) {
               var1.append((char)(var4 + 44));
            } else {
               if (var4 >= 40) {
                  throw FormatException.getFormatInstance();
               }

               var1.append((char)(var4 + 51));
            }
         }

         if (var0.available() <= 0) {
            break;
         }
      }

   }

   private static DecodedBitStreamParser.Mode decodeAsciiSegment(BitSource var0, StringBuilder var1, StringBuilder var2) throws FormatException {
      boolean var3 = false;

      while(true) {
         int var4 = var0.readBits(8);
         if (var4 == 0) {
            throw FormatException.getFormatInstance();
         }

         DecodedBitStreamParser.Mode var6;
         int var7;
         if (var4 <= 128) {
            var7 = var4;
            if (var3) {
               var7 = var4 + 128;
            }

            var1.append((char)(var7 - 1));
            var6 = DecodedBitStreamParser.Mode.ASCII_ENCODE;
         } else if (var4 == 129) {
            var6 = DecodedBitStreamParser.Mode.PAD_ENCODE;
         } else {
            boolean var5;
            if (var4 <= 229) {
               var7 = var4 - 130;
               if (var7 < 10) {
                  var1.append('0');
               }

               var1.append(var7);
               var5 = var3;
            } else {
               if (var4 == 230) {
                  var6 = DecodedBitStreamParser.Mode.C40_ENCODE;
                  return var6;
               }

               if (var4 == 231) {
                  var6 = DecodedBitStreamParser.Mode.BASE256_ENCODE;
                  return var6;
               }

               if (var4 == 232) {
                  var1.append('\u001d');
                  var5 = var3;
               } else {
                  var5 = var3;
                  if (var4 != 233) {
                     var5 = var3;
                     if (var4 != 234) {
                        if (var4 == 235) {
                           var5 = true;
                        } else if (var4 == 236) {
                           var1.append("[)>\u001e05\u001d");
                           var2.insert(0, "\u001e\u0004");
                           var5 = var3;
                        } else if (var4 == 237) {
                           var1.append("[)>\u001e06\u001d");
                           var2.insert(0, "\u001e\u0004");
                           var5 = var3;
                        } else {
                           if (var4 == 238) {
                              var6 = DecodedBitStreamParser.Mode.ANSIX12_ENCODE;
                              return var6;
                           }

                           if (var4 == 239) {
                              var6 = DecodedBitStreamParser.Mode.TEXT_ENCODE;
                              return var6;
                           }

                           if (var4 == 240) {
                              var6 = DecodedBitStreamParser.Mode.EDIFACT_ENCODE;
                              return var6;
                           }

                           var5 = var3;
                           if (var4 != 241) {
                              var5 = var3;
                              if (var4 >= 242) {
                                 label115: {
                                    if (var4 == 254) {
                                       var5 = var3;
                                       if (var0.available() == 0) {
                                          break label115;
                                       }
                                    }

                                    throw FormatException.getFormatInstance();
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }

            var3 = var5;
            if (var0.available() > 0) {
               continue;
            }

            var6 = DecodedBitStreamParser.Mode.ASCII_ENCODE;
         }

         return var6;
      }
   }

   private static void decodeBase256Segment(BitSource var0, StringBuilder var1, Collection var2) throws FormatException {
      int var3 = var0.getByteOffset() + 1;
      int var4 = var0.readBits(8);
      int var5 = var3 + 1;
      var4 = unrandomize255State(var4, var3);
      if (var4 == 0) {
         var3 = var0.available() / 8;
      } else if (var4 < 250) {
         var3 = var4;
      } else {
         int var6 = var0.readBits(8);
         var3 = var5 + 1;
         var4 = (var4 - 249) * 250 + unrandomize255State(var6, var5);
         var5 = var3;
         var3 = var4;
      }

      if (var3 < 0) {
         throw FormatException.getFormatInstance();
      } else {
         byte[] var7 = new byte[var3];

         for(var4 = 0; var4 < var3; ++var5) {
            if (var0.available() < 8) {
               throw FormatException.getFormatInstance();
            }

            var7[var4] = (byte)((byte)unrandomize255State(var0.readBits(8), var5));
            ++var4;
         }

         var2.add(var7);

         try {
            String var9 = new String(var7, "ISO8859_1");
            var1.append(var9);
         } catch (UnsupportedEncodingException var8) {
            throw new IllegalStateException("Platform does not support required encoding: " + var8);
         }
      }
   }

   private static void decodeC40Segment(BitSource var0, StringBuilder var1) throws FormatException {
      boolean var2 = false;
      int[] var3 = new int[3];
      int var4 = 0;

      while(var0.available() != 8) {
         int var5 = var0.readBits(8);
         if (var5 == 254) {
            break;
         }

         parseTwoBytes(var5, var0.readBits(8), var3);

         for(var5 = 0; var5 < 3; ++var5) {
            int var6 = var3[var5];
            char var7;
            switch(var4) {
            case 0:
               if (var6 < 3) {
                  var4 = var6 + 1;
               } else {
                  if (var6 >= C40_BASIC_SET_CHARS.length) {
                     throw FormatException.getFormatInstance();
                  }

                  var7 = C40_BASIC_SET_CHARS[var6];
                  if (var2) {
                     var1.append((char)(var7 + 128));
                     var2 = false;
                  } else {
                     var1.append(var7);
                  }
               }
               break;
            case 1:
               if (var2) {
                  var1.append((char)(var6 + 128));
                  var2 = false;
               } else {
                  var1.append((char)var6);
               }

               var4 = 0;
               break;
            case 2:
               if (var6 < C40_SHIFT2_SET_CHARS.length) {
                  var7 = C40_SHIFT2_SET_CHARS[var6];
                  if (var2) {
                     var1.append((char)(var7 + 128));
                     var2 = false;
                  } else {
                     var1.append(var7);
                  }
               } else if (var6 == 27) {
                  var1.append('\u001d');
               } else {
                  if (var6 != 30) {
                     throw FormatException.getFormatInstance();
                  }

                  var2 = true;
               }

               var4 = 0;
               break;
            case 3:
               if (var2) {
                  var1.append((char)(var6 + 224));
                  var2 = false;
               } else {
                  var1.append((char)(var6 + 96));
               }

               var4 = 0;
               break;
            default:
               throw FormatException.getFormatInstance();
            }
         }

         if (var0.available() <= 0) {
            break;
         }
      }

   }

   private static void decodeEdifactSegment(BitSource var0, StringBuilder var1) {
      label28:
      while(true) {
         if (var0.available() > 16) {
            int var2 = 0;

            while(true) {
               if (var2 >= 4) {
                  if (var0.available() > 0) {
                     continue label28;
                  }
                  break;
               }

               int var3 = var0.readBits(6);
               if (var3 == 31) {
                  var2 = 8 - var0.getBitOffset();
                  if (var2 != 8) {
                     var0.readBits(var2);
                  }
                  break;
               }

               int var4 = var3;
               if ((var3 & 32) == 0) {
                  var4 = var3 | 64;
               }

               var1.append((char)var4);
               ++var2;
            }
         }

         return;
      }
   }

   private static void decodeTextSegment(BitSource var0, StringBuilder var1) throws FormatException {
      boolean var2 = false;
      int[] var3 = new int[3];
      int var4 = 0;

      while(var0.available() != 8) {
         int var5 = var0.readBits(8);
         if (var5 == 254) {
            break;
         }

         parseTwoBytes(var5, var0.readBits(8), var3);

         for(var5 = 0; var5 < 3; ++var5) {
            int var6 = var3[var5];
            char var7;
            switch(var4) {
            case 0:
               if (var6 < 3) {
                  var4 = var6 + 1;
               } else {
                  if (var6 >= TEXT_BASIC_SET_CHARS.length) {
                     throw FormatException.getFormatInstance();
                  }

                  var7 = TEXT_BASIC_SET_CHARS[var6];
                  if (var2) {
                     var1.append((char)(var7 + 128));
                     var2 = false;
                  } else {
                     var1.append(var7);
                  }
               }
               break;
            case 1:
               if (var2) {
                  var1.append((char)(var6 + 128));
                  var2 = false;
               } else {
                  var1.append((char)var6);
               }

               var4 = 0;
               break;
            case 2:
               if (var6 < TEXT_SHIFT2_SET_CHARS.length) {
                  var7 = TEXT_SHIFT2_SET_CHARS[var6];
                  if (var2) {
                     var1.append((char)(var7 + 128));
                     var2 = false;
                  } else {
                     var1.append(var7);
                  }
               } else if (var6 == 27) {
                  var1.append('\u001d');
               } else {
                  if (var6 != 30) {
                     throw FormatException.getFormatInstance();
                  }

                  var2 = true;
               }

               var4 = 0;
               break;
            case 3:
               if (var6 >= TEXT_SHIFT3_SET_CHARS.length) {
                  throw FormatException.getFormatInstance();
               }

               var7 = TEXT_SHIFT3_SET_CHARS[var6];
               if (var2) {
                  var1.append((char)(var7 + 128));
                  var2 = false;
               } else {
                  var1.append(var7);
               }

               var4 = 0;
               break;
            default:
               throw FormatException.getFormatInstance();
            }
         }

         if (var0.available() <= 0) {
            break;
         }
      }

   }

   private static void parseTwoBytes(int var0, int var1, int[] var2) {
      var0 = (var0 << 8) + var1 - 1;
      var1 = var0 / 1600;
      var2[0] = var1;
      var0 -= var1 * 1600;
      var1 = var0 / 40;
      var2[1] = var1;
      var2[2] = var0 - var1 * 40;
   }

   private static int unrandomize255State(int var0, int var1) {
      var0 -= var1 * 149 % 255 + 1;
      if (var0 < 0) {
         var0 += 256;
      }

      return var0;
   }

   private static enum Mode {
      ANSIX12_ENCODE,
      ASCII_ENCODE,
      BASE256_ENCODE,
      C40_ENCODE,
      EDIFACT_ENCODE,
      PAD_ENCODE,
      TEXT_ENCODE;
   }
}
