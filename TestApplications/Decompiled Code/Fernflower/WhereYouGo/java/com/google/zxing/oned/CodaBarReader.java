package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitArray;
import java.util.Arrays;
import java.util.Map;

public final class CodaBarReader extends OneDReader {
   static final char[] ALPHABET = "0123456789-$:/.+ABCD".toCharArray();
   private static final String ALPHABET_STRING = "0123456789-$:/.+ABCD";
   static final int[] CHARACTER_ENCODINGS = new int[]{3, 6, 9, 96, 18, 66, 33, 36, 48, 72, 12, 24, 69, 81, 84, 21, 26, 41, 11, 14};
   private static final float MAX_ACCEPTABLE = 2.0F;
   private static final int MIN_CHARACTER_LENGTH = 3;
   private static final float PADDING = 1.5F;
   private static final char[] STARTEND_ENCODING = new char[]{'A', 'B', 'C', 'D'};
   private int counterLength = 0;
   private int[] counters = new int[80];
   private final StringBuilder decodeRowResult = new StringBuilder(20);

   static boolean arrayContains(char[] var0, char var1) {
      boolean var2 = false;
      boolean var3 = var2;
      if (var0 != null) {
         int var4 = var0.length;
         int var5 = 0;

         while(true) {
            var3 = var2;
            if (var5 >= var4) {
               break;
            }

            if (var0[var5] == var1) {
               var3 = true;
               break;
            }

            ++var5;
         }
      }

      return var3;
   }

   private void counterAppend(int var1) {
      this.counters[this.counterLength] = var1;
      ++this.counterLength;
      if (this.counterLength >= this.counters.length) {
         int[] var2 = new int[this.counterLength << 1];
         System.arraycopy(this.counters, 0, var2, 0, this.counterLength);
         this.counters = var2;
      }

   }

   private int findStartPattern() throws NotFoundException {
      for(int var1 = 1; var1 < this.counterLength; var1 += 2) {
         int var2 = this.toNarrowWidePattern(var1);
         if (var2 != -1 && arrayContains(STARTEND_ENCODING, ALPHABET[var2])) {
            var2 = 0;

            for(int var3 = var1; var3 < var1 + 7; ++var3) {
               var2 += this.counters[var3];
            }

            if (var1 == 1 || this.counters[var1 - 1] >= var2 / 2) {
               return var1;
            }
         }
      }

      throw NotFoundException.getNotFoundInstance();
   }

   private void setCounters(BitArray var1) throws NotFoundException {
      this.counterLength = 0;
      int var2 = var1.getNextUnset(0);
      int var3 = var1.getSize();
      if (var2 >= var3) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         boolean var4 = true;

         int var5;
         boolean var6;
         for(var5 = 0; var2 < var3; var4 = var6) {
            int var7;
            if (var1.get(var2) ^ var4) {
               ++var5;
               var6 = var4;
               var7 = var5;
            } else {
               this.counterAppend(var5);
               byte var8 = 1;
               if (!var4) {
                  var6 = true;
               } else {
                  var6 = false;
               }

               var7 = var8;
            }

            ++var2;
            var5 = var7;
         }

         this.counterAppend(var5);
      }
   }

   private int toNarrowWidePattern(int var1) {
      int var2 = var1 + 7;
      int var3;
      if (var2 >= this.counterLength) {
         var3 = -1;
      } else {
         int[] var4 = this.counters;
         int var5 = 0;
         int var6 = Integer.MAX_VALUE;

         int var7;
         int var8;
         for(var7 = var1; var7 < var2; var6 = var3) {
            var8 = var4[var7];
            var3 = var6;
            if (var8 < var6) {
               var3 = var8;
            }

            var6 = var5;
            if (var8 > var5) {
               var6 = var8;
            }

            var7 += 2;
            var5 = var6;
         }

         int var9 = (var6 + var5) / 2;
         var7 = 0;
         var6 = Integer.MAX_VALUE;

         for(var3 = var1 + 1; var3 < var2; var6 = var5) {
            var8 = var4[var3];
            var5 = var6;
            if (var8 < var6) {
               var5 = var8;
            }

            var6 = var7;
            if (var8 > var7) {
               var6 = var8;
            }

            var3 += 2;
            var7 = var6;
         }

         var2 = (var6 + var7) / 2;
         var5 = 128;
         var7 = 0;

         for(var3 = 0; var3 < 7; var7 = var6) {
            if ((var3 & 1) == 0) {
               var8 = var9;
            } else {
               var8 = var2;
            }

            var5 >>= 1;
            var6 = var7;
            if (var4[var1 + var3] > var8) {
               var6 = var7 | var5;
            }

            ++var3;
         }

         var1 = 0;

         while(true) {
            if (var1 >= CHARACTER_ENCODINGS.length) {
               var3 = -1;
               break;
            }

            var3 = var1;
            if (CHARACTER_ENCODINGS[var1] == var7) {
               break;
            }

            ++var1;
         }
      }

      return var3;
   }

   private void validatePattern(int var1) throws NotFoundException {
      int[] var2 = new int[]{0, 0, 0, 0};
      int[] var3 = new int[]{0, 0, 0, 0};
      int var4 = this.decodeRowResult.length() - 1;
      int var5 = var1;
      int var6 = 0;

      while(true) {
         int var7 = CHARACTER_ENCODINGS[this.decodeRowResult.charAt(var6)];

         int var8;
         int var9;
         for(var8 = 6; var8 >= 0; --var8) {
            var9 = (var8 & 1) + ((var7 & 1) << 1);
            var2[var9] += this.counters[var5 + var8];
            int var10002 = var3[var9]++;
            var7 >>= 1;
         }

         if (var6 >= var4) {
            float[] var10 = new float[4];
            float[] var11 = new float[4];

            for(var5 = 0; var5 < 2; ++var5) {
               var11[var5] = 0.0F;
               var11[var5 + 2] = ((float)var2[var5] / (float)var3[var5] + (float)var2[var5 + 2] / (float)var3[var5 + 2]) / 2.0F;
               var10[var5] = var11[var5 + 2];
               var10[var5 + 2] = ((float)var2[var5 + 2] * 2.0F + 1.5F) / (float)var3[var5 + 2];
            }

            var5 = 0;

            while(true) {
               var7 = CHARACTER_ENCODINGS[this.decodeRowResult.charAt(var5)];

               for(var6 = 6; var6 >= 0; --var6) {
                  var9 = (var6 & 1) + ((var7 & 1) << 1);
                  var8 = this.counters[var1 + var6];
                  if ((float)var8 < var11[var9] || (float)var8 > var10[var9]) {
                     throw NotFoundException.getNotFoundInstance();
                  }

                  var7 >>= 1;
               }

               if (var5 >= var4) {
                  return;
               }

               var1 += 8;
               ++var5;
            }
         }

         var5 += 8;
         ++var6;
      }
   }

   public Result decodeRow(int var1, BitArray var2, Map var3) throws NotFoundException {
      Arrays.fill(this.counters, 0);
      this.setCounters(var2);
      int var4 = this.findStartPattern();
      int var5 = var4;
      this.decodeRowResult.setLength(0);

      int var6;
      int var7;
      do {
         var6 = this.toNarrowWidePattern(var5);
         if (var6 == -1) {
            throw NotFoundException.getNotFoundInstance();
         }

         this.decodeRowResult.append((char)var6);
         var7 = var5 + 8;
         if (this.decodeRowResult.length() > 1 && arrayContains(STARTEND_ENCODING, ALPHABET[var6])) {
            break;
         }

         var5 = var7;
      } while(var7 < this.counterLength);

      int var8 = this.counters[var7 - 1];
      var6 = 0;

      for(var5 = -8; var5 < -1; ++var5) {
         var6 += this.counters[var7 + var5];
      }

      if (var7 < this.counterLength && var8 < var6 / 2) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         this.validatePattern(var4);

         for(var5 = 0; var5 < this.decodeRowResult.length(); ++var5) {
            this.decodeRowResult.setCharAt(var5, ALPHABET[this.decodeRowResult.charAt(var5)]);
         }

         char var9 = this.decodeRowResult.charAt(0);
         if (!arrayContains(STARTEND_ENCODING, var9)) {
            throw NotFoundException.getNotFoundInstance();
         } else {
            var9 = this.decodeRowResult.charAt(this.decodeRowResult.length() - 1);
            if (!arrayContains(STARTEND_ENCODING, var9)) {
               throw NotFoundException.getNotFoundInstance();
            } else if (this.decodeRowResult.length() <= 3) {
               throw NotFoundException.getNotFoundInstance();
            } else {
               if (var3 == null || !var3.containsKey(DecodeHintType.RETURN_CODABAR_START_END)) {
                  this.decodeRowResult.deleteCharAt(this.decodeRowResult.length() - 1);
                  this.decodeRowResult.deleteCharAt(0);
               }

               var5 = 0;

               for(var6 = 0; var6 < var4; ++var6) {
                  var5 += this.counters[var6];
               }

               float var10 = (float)var5;
               var6 = var4;
               var4 = var5;

               for(var5 = var6; var5 < var7 - 1; ++var5) {
                  var4 += this.counters[var5];
               }

               float var11 = (float)var4;
               String var14 = this.decodeRowResult.toString();
               ResultPoint var12 = new ResultPoint(var10, (float)var1);
               ResultPoint var15 = new ResultPoint(var11, (float)var1);
               BarcodeFormat var13 = BarcodeFormat.CODABAR;
               return new Result(var14, (byte[])null, new ResultPoint[]{var12, var15}, var13);
            }
         }
      }
   }
}
