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

public final class Code93Reader extends OneDReader {
   private static final char[] ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%abcd*".toCharArray();
   static final String ALPHABET_STRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%abcd*";
   private static final int ASTERISK_ENCODING;
   static final int[] CHARACTER_ENCODINGS;
   private final int[] counters = new int[6];
   private final StringBuilder decodeRowResult = new StringBuilder(20);

   static {
      int[] var0 = new int[]{276, 328, 324, 322, 296, 292, 290, 336, 274, 266, 424, 420, 418, 404, 402, 394, 360, 356, 354, 308, 282, 344, 332, 326, 300, 278, 436, 434, 428, 422, 406, 410, 364, 358, 310, 314, 302, 468, 466, 458, 366, 374, 430, 294, 474, 470, 306, 350};
      CHARACTER_ENCODINGS = var0;
      ASTERISK_ENCODING = var0[47];
   }

   private static void checkChecksums(CharSequence var0) throws ChecksumException {
      int var1 = var0.length();
      checkOneChecksum(var0, var1 - 2, 20);
      checkOneChecksum(var0, var1 - 1, 15);
   }

   private static void checkOneChecksum(CharSequence var0, int var1, int var2) throws ChecksumException {
      int var3 = 1;
      int var4 = 0;

      for(int var5 = var1 - 1; var5 >= 0; --var5) {
         var4 += "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%abcd*".indexOf(var0.charAt(var5)) * var3;
         int var6 = var3 + 1;
         var3 = var6;
         if (var6 > var2) {
            var3 = 1;
         }
      }

      if (var0.charAt(var1) != ALPHABET[var4 % 47]) {
         throw ChecksumException.getChecksumInstance();
      }
   }

   private static String decodeExtended(CharSequence var0) throws FormatException {
      int var1 = var0.length();
      StringBuilder var2 = new StringBuilder(var1);

      for(int var3 = 0; var3 < var1; ++var3) {
         char var4 = var0.charAt(var3);
         if (var4 >= 'a' && var4 <= 'd') {
            if (var3 >= var1 - 1) {
               throw FormatException.getFormatInstance();
            }

            char var5 = var0.charAt(var3 + 1);
            byte var6 = 0;
            char var7;
            switch(var4) {
            case 'a':
               if (var5 < 'A' || var5 > 'Z') {
                  throw FormatException.getFormatInstance();
               }

               var7 = (char)(var5 - 64);
               var4 = var7;
               break;
            case 'b':
               if (var5 >= 'A' && var5 <= 'E') {
                  var7 = (char)(var5 - 38);
                  var4 = var7;
               } else if (var5 >= 'F' && var5 <= 'J') {
                  var7 = (char)(var5 - 11);
                  var4 = var7;
               } else if (var5 >= 'K' && var5 <= 'O') {
                  var7 = (char)(var5 + 16);
                  var4 = var7;
               } else if (var5 >= 'P' && var5 <= 'S') {
                  var7 = (char)(var5 + 43);
                  var4 = var7;
               } else {
                  if (var5 < 'T' || var5 > 'Z') {
                     throw FormatException.getFormatInstance();
                  }

                  var6 = 127;
                  var4 = (char)var6;
               }
               break;
            case 'c':
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
            case 'd':
               if (var5 >= 'A' && var5 <= 'Z') {
                  var7 = (char)(var5 + 32);
                  var4 = var7;
                  break;
               }

               throw FormatException.getFormatInstance();
            default:
               var4 = (char)var6;
            }

            var2.append(var4);
            ++var3;
         } else {
            var2.append(var4);
         }
      }

      return var2.toString();
   }

   private int[] findAsteriskPattern(BitArray var1) throws NotFoundException {
      int var2 = var1.getSize();
      int var3 = var1.getNextSet(0);
      Arrays.fill(this.counters, 0);
      int[] var4 = this.counters;
      int var5 = var3;
      boolean var6 = false;
      int var7 = var4.length;

      for(int var8 = 0; var3 < var2; ++var3) {
         if (var1.get(var3) ^ var6) {
            int var10002 = var4[var8]++;
         } else {
            if (var8 == var7 - 1) {
               if (toPattern(var4) == ASTERISK_ENCODING) {
                  return new int[]{var5, var3};
               }

               var5 += var4[0] + var4[1];
               System.arraycopy(var4, 2, var4, 0, var7 - 2);
               var4[var7 - 2] = 0;
               var4[var7 - 1] = 0;
               --var8;
            } else {
               ++var8;
            }

            var4[var8] = 1;
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
            return ALPHABET[var1];
         }
      }

      throw NotFoundException.getNotFoundInstance();
   }

   private static int toPattern(int[] var0) {
      int var1 = 0;
      int var2 = var0.length;

      int var3;
      for(var3 = 0; var3 < var2; ++var3) {
         var1 += var0[var3];
      }

      var3 = 0;
      int var4 = var0.length;
      int var5 = 0;

      while(true) {
         var2 = var3;
         if (var5 >= var4) {
            break;
         }

         int var6 = Math.round((float)var0[var5] * 9.0F / (float)var1);
         if (var6 <= 0 || var6 > 4) {
            var2 = -1;
            break;
         }

         if ((var5 & 1) == 0) {
            int var7 = 0;
            var2 = var3;

            while(true) {
               var3 = var2;
               if (var7 >= var6) {
                  break;
               }

               var2 = var2 << 1 | 1;
               ++var7;
            }
         } else {
            var3 <<= var6;
         }

         ++var5;
      }

      return var2;
   }

   public Result decodeRow(int var1, BitArray var2, Map var3) throws NotFoundException, ChecksumException, FormatException {
      int[] var17 = this.findAsteriskPattern(var2);
      int var4 = var2.getNextSet(var17[1]);
      int var5 = var2.getSize();
      int[] var6 = this.counters;
      Arrays.fill(var6, 0);
      StringBuilder var7 = this.decodeRowResult;
      var7.setLength(0);

      int var8;
      char var9;
      int var10;
      int var11;
      do {
         var8 = var4;
         recordPattern(var2, var4, var6);
         var4 = toPattern(var6);
         if (var4 < 0) {
            throw NotFoundException.getNotFoundInstance();
         }

         var9 = patternToChar(var4);
         var7.append(var9);
         var10 = var6.length;
         var11 = 0;

         for(var4 = var8; var11 < var10; ++var11) {
            var4 += var6[var11];
         }

         var10 = var2.getNextSet(var4);
         var4 = var10;
      } while(var9 != '*');

      var7.deleteCharAt(var7.length() - 1);
      var11 = 0;
      int var12 = var6.length;

      for(var4 = 0; var4 < var12; ++var4) {
         var11 += var6[var4];
      }

      if (var10 != var5 && var2.get(var10)) {
         if (var7.length() < 2) {
            throw NotFoundException.getNotFoundInstance();
         } else {
            checkChecksums(var7);
            var7.setLength(var7.length() - 2);
            String var16 = decodeExtended(var7);
            float var13 = (float)(var17[1] + var17[0]) / 2.0F;
            float var14 = (float)var8;
            float var15 = (float)var11 / 2.0F;
            ResultPoint var19 = new ResultPoint(var13, (float)var1);
            ResultPoint var20 = new ResultPoint(var14 + var15, (float)var1);
            BarcodeFormat var18 = BarcodeFormat.CODE_93;
            return new Result(var16, (byte[])null, new ResultPoint[]{var19, var20}, var18);
         }
      } else {
         throw NotFoundException.getNotFoundInstance();
      }
   }
}
