package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitArray;
import java.util.Arrays;
import java.util.Map;

public final class Code39Reader extends OneDReader {
   static final String ALPHABET_STRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. *$/+%";
   static final int ASTERISK_ENCODING;
   static final int[] CHARACTER_ENCODINGS;
   private static final String CHECK_DIGIT_STRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%";
   private final int[] counters;
   private final StringBuilder decodeRowResult;
   private final boolean extendedMode;
   private final boolean usingCheckDigit;

   static {
      int[] var0 = new int[]{52, 289, 97, 352, 49, 304, 112, 37, 292, 100, 265, 73, 328, 25, 280, 88, 13, 268, 76, 28, 259, 67, 322, 19, 274, 82, 7, 262, 70, 22, 385, 193, 448, 145, 400, 208, 133, 388, 196, 148, 168, 162, 138, 42};
      CHARACTER_ENCODINGS = var0;
      ASTERISK_ENCODING = var0[39];
   }

   public Code39Reader() {
      this(false);
   }

   public Code39Reader(boolean var1) {
      this(var1, false);
   }

   public Code39Reader(boolean var1, boolean var2) {
      this.usingCheckDigit = var1;
      this.extendedMode = var2;
      this.decodeRowResult = new StringBuilder(20);
      this.counters = new int[9];
   }

   private static String decodeExtended(CharSequence var0) throws FormatException {
      int var1 = var0.length();
      StringBuilder var2 = new StringBuilder(var1);

      for(int var3 = 0; var3 < var1; ++var3) {
         char var4 = var0.charAt(var3);
         if (var4 != '+' && var4 != '$' && var4 != '%' && var4 != '/') {
            var2.append(var4);
         } else {
            char var5 = var0.charAt(var3 + 1);
            byte var6 = 0;
            char var7;
            switch(var4) {
            case '$':
               if (var5 < 'A' || var5 > 'Z') {
                  throw FormatException.getFormatInstance();
               }

               var7 = (char)(var5 - 64);
               var4 = var7;
               break;
            case '%':
               if (var5 >= 'A' && var5 <= 'E') {
                  var7 = (char)(var5 - 38);
                  var4 = var7;
               } else {
                  if (var5 < 'F' || var5 > 'W') {
                     throw FormatException.getFormatInstance();
                  }

                  var7 = (char)(var5 - 11);
                  var4 = var7;
               }
               break;
            case '+':
               if (var5 >= 'A' && var5 <= 'Z') {
                  var7 = (char)(var5 + 32);
                  var4 = var7;
                  break;
               }

               throw FormatException.getFormatInstance();
            case '/':
               if (var5 >= 'A' && var5 <= 'O') {
                  var7 = (char)(var5 - 32);
                  var4 = var7;
               } else {
                  if (var5 != 'Z') {
                     throw FormatException.getFormatInstance();
                  }

                  var6 = 58;
                  var4 = (char)var6;
               }
               break;
            default:
               var4 = (char)var6;
            }

            var2.append(var4);
            ++var3;
         }
      }

      return var2.toString();
   }

   private static int[] findAsteriskPattern(BitArray var0, int[] var1) throws NotFoundException {
      int var2 = var0.getSize();
      int var3 = var0.getNextSet(0);
      int var4 = 0;
      int var5 = var3;
      boolean var6 = false;

      for(int var7 = var1.length; var3 < var2; ++var3) {
         if (var0.get(var3) ^ var6) {
            int var10002 = var1[var4]++;
         } else {
            if (var4 == var7 - 1) {
               if (toNarrowWidePattern(var1) == ASTERISK_ENCODING && var0.isRange(Math.max(0, var5 - (var3 - var5) / 2), var5, false)) {
                  return new int[]{var5, var3};
               }

               var5 += var1[0] + var1[1];
               System.arraycopy(var1, 2, var1, 0, var7 - 2);
               var1[var7 - 2] = 0;
               var1[var7 - 1] = 0;
               --var4;
            } else {
               ++var4;
            }

            var1[var4] = 1;
            if (!var6) {
               var6 = true;
            } else {
               var6 = false;
            }
         }
      }

      throw NotFoundException.getNotFoundInstance();
   }

   private static char patternToChar(int var0) throws NotFoundException {
      for(int var1 = 0; var1 < CHARACTER_ENCODINGS.length; ++var1) {
         if (CHARACTER_ENCODINGS[var1] == var0) {
            return "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. *$/+%".charAt(var1);
         }
      }

      throw NotFoundException.getNotFoundInstance();
   }

   private static int toNarrowWidePattern(int[] var0) {
      int var1 = var0.length;
      int var2 = 0;

      int var5;
      do {
         int var3 = Integer.MAX_VALUE;
         int var4 = var0.length;

         int var6;
         int var7;
         for(var5 = 0; var5 < var4; var3 = var7) {
            var6 = var0[var5];
            var7 = var3;
            if (var6 < var3) {
               var7 = var3;
               if (var6 > var2) {
                  var7 = var6;
               }
            }

            ++var5;
         }

         var2 = var3;
         var5 = 0;
         var7 = 0;
         var3 = 0;

         int var10;
         for(var4 = 0; var4 < var1; var5 = var6) {
            int var8 = var0[var4];
            int var9 = var3;
            var10 = var7;
            var6 = var5;
            if (var8 > var2) {
               var9 = var3 | 1 << var1 - 1 - var4;
               var6 = var5 + 1;
               var10 = var7 + var8;
            }

            ++var4;
            var3 = var9;
            var7 = var10;
         }

         if (var5 == 3) {
            var4 = 0;
            var6 = var5;

            while(true) {
               var5 = var3;
               if (var4 >= var1) {
                  return var5;
               }

               var5 = var3;
               if (var6 <= 0) {
                  return var5;
               }

               var10 = var0[var4];
               var5 = var6;
               if (var10 > var2) {
                  var5 = var6 - 1;
                  if (var10 << 1 >= var7) {
                     var5 = -1;
                     return var5;
                  }
               }

               ++var4;
               var6 = var5;
            }
         }
      } while(var5 > 3);

      var5 = -1;
      return var5;
   }

   public Result decodeRow(int var1, BitArray var2, Map var3) throws NotFoundException, ChecksumException, FormatException {
      int[] var4 = this.counters;
      Arrays.fill(var4, 0);
      StringBuilder var5 = this.decodeRowResult;
      var5.setLength(0);
      int[] var17 = findAsteriskPattern(var2, var4);
      int var6 = var2.getNextSet(var17[1]);
      int var7 = var2.getSize();

      int var8;
      char var9;
      int var10;
      int var11;
      do {
         var8 = var6;
         recordPattern(var2, var6, var4);
         var6 = toNarrowWidePattern(var4);
         if (var6 < 0) {
            throw NotFoundException.getNotFoundInstance();
         }

         var9 = patternToChar(var6);
         var5.append(var9);
         var10 = var4.length;
         var11 = 0;

         for(var6 = var8; var11 < var10; ++var11) {
            var6 += var4[var11];
         }

         var10 = var2.getNextSet(var6);
         var6 = var10;
      } while(var9 != '*');

      var5.setLength(var5.length() - 1);
      var6 = 0;
      int var12 = var4.length;

      for(var11 = 0; var11 < var12; ++var11) {
         var6 += var4[var11];
      }

      if (var10 != var7 && var10 - var8 - var6 << 1 < var6) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         if (this.usingCheckDigit) {
            var7 = var5.length() - 1;
            var11 = 0;

            for(var10 = 0; var10 < var7; ++var10) {
               var11 += "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%".indexOf(this.decodeRowResult.charAt(var10));
            }

            if (var5.charAt(var7) != "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%".charAt(var11 % 43)) {
               throw ChecksumException.getChecksumInstance();
            }

            var5.setLength(var7);
         }

         if (var5.length() == 0) {
            throw NotFoundException.getNotFoundInstance();
         } else {
            String var16;
            if (this.extendedMode) {
               var16 = decodeExtended(var5);
            } else {
               var16 = var5.toString();
            }

            float var13 = (float)(var17[1] + var17[0]) / 2.0F;
            float var14 = (float)var8;
            float var15 = (float)var6 / 2.0F;
            ResultPoint var18 = new ResultPoint(var13, (float)var1);
            ResultPoint var20 = new ResultPoint(var14 + var15, (float)var1);
            BarcodeFormat var19 = BarcodeFormat.CODE_39;
            return new Result(var16, (byte[])null, new ResultPoint[]{var18, var20}, var19);
         }
      }
   }
}
