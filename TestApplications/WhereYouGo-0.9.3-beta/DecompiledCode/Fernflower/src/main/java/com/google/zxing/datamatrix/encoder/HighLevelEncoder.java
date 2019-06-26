package com.google.zxing.datamatrix.encoder;

import com.google.zxing.Dimension;
import java.util.Arrays;

public final class HighLevelEncoder {
   static final int ASCII_ENCODATION = 0;
   static final int BASE256_ENCODATION = 5;
   static final int C40_ENCODATION = 1;
   static final char C40_UNLATCH = 'þ';
   static final int EDIFACT_ENCODATION = 4;
   static final char LATCH_TO_ANSIX12 = 'î';
   static final char LATCH_TO_BASE256 = 'ç';
   static final char LATCH_TO_C40 = 'æ';
   static final char LATCH_TO_EDIFACT = 'ð';
   static final char LATCH_TO_TEXT = 'ï';
   private static final char MACRO_05 = 'ì';
   private static final String MACRO_05_HEADER = "[)>\u001e05\u001d";
   private static final char MACRO_06 = 'í';
   private static final String MACRO_06_HEADER = "[)>\u001e06\u001d";
   private static final String MACRO_TRAILER = "\u001e\u0004";
   private static final char PAD = '\u0081';
   static final int TEXT_ENCODATION = 2;
   static final char UPPER_SHIFT = 'ë';
   static final int X12_ENCODATION = 3;
   static final char X12_UNLATCH = 'þ';

   private HighLevelEncoder() {
   }

   public static int determineConsecutiveDigitCount(CharSequence var0, int var1) {
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

            var2 = var1 + 1;
            int var9 = var5 + 1;
            var1 = var2;
            var5 = var9;
            if (var9 < var4) {
               char var7 = var0.charAt(var9);
               var6 = var7;
               var1 = var2;
               var5 = var9;
            }
         }
      }

      return var2;
   }

   public static String encodeHighLevel(String var0) {
      return encodeHighLevel(var0, SymbolShapeHint.FORCE_NONE, (Dimension)null, (Dimension)null);
   }

   public static String encodeHighLevel(String var0, SymbolShapeHint var1, Dimension var2, Dimension var3) {
      ASCIIEncoder var4 = new ASCIIEncoder();
      C40Encoder var5 = new C40Encoder();
      TextEncoder var6 = new TextEncoder();
      X12Encoder var7 = new X12Encoder();
      EdifactEncoder var8 = new EdifactEncoder();
      Base256Encoder var9 = new Base256Encoder();
      EncoderContext var10 = new EncoderContext(var0);
      var10.setSymbolShape(var1);
      var10.setSizeConstraints(var2, var3);
      if (var0.startsWith("[)>\u001e05\u001d") && var0.endsWith("\u001e\u0004")) {
         var10.writeCodeword('ì');
         var10.setSkipAtEnd(2);
         var10.pos += 7;
      } else if (var0.startsWith("[)>\u001e06\u001d") && var0.endsWith("\u001e\u0004")) {
         var10.writeCodeword('í');
         var10.setSkipAtEnd(2);
         var10.pos += 7;
      }

      int var11 = 0;

      while(var10.hasMoreCharacters()) {
         (new Encoder[]{var4, var5, var6, var7, var8, var9})[var11].encode(var10);
         if (var10.getNewEncoding() >= 0) {
            var11 = var10.getNewEncoding();
            var10.resetEncoderSignal();
         }
      }

      int var12 = var10.getCodewordCount();
      var10.updateSymbolInfo();
      int var13 = var10.getSymbolInfo().getDataCapacity();
      if (var12 < var13 && var11 != 0 && var11 != 5) {
         var10.writeCodeword('þ');
      }

      StringBuilder var14 = var10.getCodewords();
      if (var14.length() < var13) {
         var14.append('\u0081');
      }

      while(var14.length() < var13) {
         var14.append(randomize253State('\u0081', var14.length() + 1));
      }

      return var10.getCodewords().toString();
   }

   private static int findMinimums(float[] var0, int[] var1, int var2, byte[] var3) {
      Arrays.fill(var3, (byte)0);

      int var6;
      for(int var4 = 0; var4 < 6; var2 = var6) {
         var1[var4] = (int)Math.ceil((double)var0[var4]);
         int var5 = var1[var4];
         var6 = var2;
         if (var2 > var5) {
            var6 = var5;
            Arrays.fill(var3, (byte)0);
         }

         if (var6 == var5) {
            var3[var4] = (byte)((byte)(var3[var4] + 1));
         }

         ++var4;
      }

      return var2;
   }

   private static int getMinimumCount(byte[] var0) {
      int var1 = 0;

      for(int var2 = 0; var2 < 6; ++var2) {
         var1 += var0[var2];
      }

      return var1;
   }

   static void illegalCharacter(char var0) {
      String var1 = Integer.toHexString(var0);
      var1 = "0000".substring(0, 4 - var1.length()) + var1;
      throw new IllegalArgumentException("Illegal character: " + var0 + " (0x" + var1 + ')');
   }

   static boolean isDigit(char var0) {
      boolean var1;
      if (var0 >= '0' && var0 <= '9') {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   static boolean isExtendedASCII(char var0) {
      boolean var1;
      if (var0 >= 128 && var0 <= 255) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private static boolean isNativeC40(char var0) {
      boolean var1;
      if (var0 != ' ' && (var0 < '0' || var0 > '9') && (var0 < 'A' || var0 > 'Z')) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private static boolean isNativeEDIFACT(char var0) {
      boolean var1;
      if (var0 >= ' ' && var0 <= '^') {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private static boolean isNativeText(char var0) {
      boolean var1;
      if (var0 != ' ' && (var0 < '0' || var0 > '9') && (var0 < 'a' || var0 > 'z')) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private static boolean isNativeX12(char var0) {
      boolean var1;
      if (!isX12TermSep(var0) && var0 != ' ' && (var0 < '0' || var0 > '9') && (var0 < 'A' || var0 > 'Z')) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private static boolean isSpecialB256(char var0) {
      return false;
   }

   private static boolean isX12TermSep(char var0) {
      boolean var1;
      if (var0 != '\r' && var0 != '*' && var0 != '>') {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   static int lookAheadTest(CharSequence var0, int var1, int var2) {
      if (var1 < var0.length()) {
         float[] var3;
         if (var2 == 0) {
            var3 = new float[]{0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.25F};
         } else {
            var3 = new float[]{1.0F, 2.0F, 2.0F, 2.0F, 2.0F, 2.25F};
            var3[var2] = 0.0F;
         }

         var2 = 0;

         while(true) {
            if (var1 + var2 == var0.length()) {
               byte[] var9 = new byte[6];
               int[] var8 = new int[6];
               var2 = findMinimums(var3, var8, Integer.MAX_VALUE, var9);
               var1 = getMinimumCount(var9);
               if (var8[0] == var2) {
                  var2 = 0;
               } else if (var1 == 1 && var9[5] > 0) {
                  var2 = 5;
               } else if (var1 == 1 && var9[4] > 0) {
                  var2 = 4;
               } else if (var1 == 1 && var9[2] > 0) {
                  var2 = 2;
               } else if (var1 == 1 && var9[3] > 0) {
                  var2 = 3;
               } else {
                  var2 = 1;
               }
               break;
            }

            char var5 = var0.charAt(var1 + var2);
            int var6 = var2 + 1;
            int var10002;
            if (isDigit(var5)) {
               var3[0] += 0.5F;
            } else if (isExtendedASCII(var5)) {
               var3[0] = (float)Math.ceil((double)var3[0]);
               var3[0] += 2.0F;
            } else {
               var3[0] = (float)Math.ceil((double)var3[0]);
               var10002 = var3[0]++;
            }

            if (isNativeC40(var5)) {
               var3[1] += 0.6666667F;
            } else if (isExtendedASCII(var5)) {
               var3[1] += 2.6666667F;
            } else {
               var10002 = var3[1]++;
            }

            if (isNativeText(var5)) {
               var3[2] += 0.6666667F;
            } else if (isExtendedASCII(var5)) {
               var3[2] += 2.6666667F;
            } else {
               var10002 = var3[2]++;
            }

            if (isNativeX12(var5)) {
               var3[3] += 0.6666667F;
            } else if (isExtendedASCII(var5)) {
               var3[3] += 4.3333335F;
            } else {
               var3[3] += 3.3333333F;
            }

            if (isNativeEDIFACT(var5)) {
               var3[4] += 0.75F;
            } else if (isExtendedASCII(var5)) {
               var3[4] += 4.25F;
            } else {
               var3[4] += 3.25F;
            }

            if (isSpecialB256(var5)) {
               var3[5] += 4.0F;
            } else {
               var10002 = var3[5]++;
            }

            var2 = var6;
            if (var6 >= 4) {
               int[] var4 = new int[6];
               byte[] var7 = new byte[6];
               findMinimums(var3, var4, Integer.MAX_VALUE, var7);
               var2 = getMinimumCount(var7);
               if (var4[0] < var4[5] && var4[0] < var4[1] && var4[0] < var4[2] && var4[0] < var4[3] && var4[0] < var4[4]) {
                  var2 = 0;
                  break;
               }

               if (var4[5] < var4[0] || var7[1] + var7[2] + var7[3] + var7[4] == 0) {
                  var2 = 5;
                  break;
               }

               if (var2 == 1 && var7[4] > 0) {
                  var2 = 4;
                  break;
               }

               if (var2 == 1 && var7[2] > 0) {
                  var2 = 2;
                  break;
               }

               if (var2 == 1 && var7[3] > 0) {
                  var2 = 3;
                  break;
               }

               var2 = var6;
               if (var4[1] + 1 < var4[0]) {
                  var2 = var6;
                  if (var4[1] + 1 < var4[5]) {
                     var2 = var6;
                     if (var4[1] + 1 < var4[4]) {
                        var2 = var6;
                        if (var4[1] + 1 < var4[2]) {
                           if (var4[1] < var4[3]) {
                              var2 = 1;
                              break;
                           }

                           var2 = var6;
                           if (var4[1] == var4[3]) {
                              for(var1 = var1 + var6 + 1; var1 < var0.length(); ++var1) {
                                 var5 = var0.charAt(var1);
                                 if (isX12TermSep(var5)) {
                                    var2 = 3;
                                    return var2;
                                 }

                                 if (!isNativeX12(var5)) {
                                    break;
                                 }
                              }

                              var2 = 1;
                              break;
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return var2;
   }

   private static char randomize253State(char var0, int var1) {
      int var2 = var0 + var1 * 149 % 253 + 1;
      if (var2 > 254) {
         var2 -= 254;
      }

      return (char)var2;
   }
}
