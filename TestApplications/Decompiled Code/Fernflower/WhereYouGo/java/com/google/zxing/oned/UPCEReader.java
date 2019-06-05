package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;

public final class UPCEReader extends UPCEANReader {
   static final int[] CHECK_DIGIT_ENCODINGS = new int[]{56, 52, 50, 49, 44, 38, 35, 42, 41, 37};
   private static final int[] MIDDLE_END_PATTERN = new int[]{1, 1, 1, 1, 1, 1};
   private static final int[][] NUMSYS_AND_CHECK_DIGIT_PATTERNS;
   private final int[] decodeMiddleCounters = new int[4];

   static {
      int[] var0 = new int[]{7, 11, 13, 14, 19, 25, 28, 21, 22, 26};
      NUMSYS_AND_CHECK_DIGIT_PATTERNS = new int[][]{{56, 52, 50, 49, 44, 38, 35, 42, 41, 37}, var0};
   }

   public static String convertUPCEtoUPCA(String var0) {
      char[] var1 = new char[6];
      var0.getChars(1, 7, var1, 0);
      StringBuilder var2 = new StringBuilder(12);
      var2.append(var0.charAt(0));
      char var3 = var1[5];
      switch(var3) {
      case '0':
      case '1':
      case '2':
         var2.append(var1, 0, 2);
         var2.append(var3);
         var2.append("0000");
         var2.append(var1, 2, 3);
         break;
      case '3':
         var2.append(var1, 0, 3);
         var2.append("00000");
         var2.append(var1, 3, 2);
         break;
      case '4':
         var2.append(var1, 0, 4);
         var2.append("00000");
         var2.append(var1[4]);
         break;
      default:
         var2.append(var1, 0, 5);
         var2.append("0000");
         var2.append(var3);
      }

      var2.append(var0.charAt(7));
      return var2.toString();
   }

   private static void determineNumSysAndCheckDigit(StringBuilder var0, int var1) throws NotFoundException {
      for(int var2 = 0; var2 <= 1; ++var2) {
         for(int var3 = 0; var3 < 10; ++var3) {
            if (var1 == NUMSYS_AND_CHECK_DIGIT_PATTERNS[var2][var3]) {
               var0.insert(0, (char)(var2 + 48));
               var0.append((char)(var3 + 48));
               return;
            }
         }
      }

      throw NotFoundException.getNotFoundInstance();
   }

   protected boolean checkChecksum(String var1) throws FormatException {
      return super.checkChecksum(convertUPCEtoUPCA(var1));
   }

   protected int[] decodeEnd(BitArray var1, int var2) throws NotFoundException {
      return findGuardPattern(var1, var2, true, MIDDLE_END_PATTERN);
   }

   protected int decodeMiddle(BitArray var1, int[] var2, StringBuilder var3) throws NotFoundException {
      int[] var4 = this.decodeMiddleCounters;
      var4[0] = 0;
      var4[1] = 0;
      var4[2] = 0;
      var4[3] = 0;
      int var5 = var1.getSize();
      int var6 = var2[1];
      int var7 = 0;

      int var11;
      for(int var8 = 0; var8 < 6 && var6 < var5; var7 = var11) {
         int var9 = decodeDigit(var1, var4, var6, L_AND_G_PATTERNS);
         var3.append((char)(var9 % 10 + 48));
         int var10 = var4.length;

         for(var11 = 0; var11 < var10; ++var11) {
            var6 += var4[var11];
         }

         var11 = var7;
         if (var9 >= 10) {
            var11 = var7 | 1 << 5 - var8;
         }

         ++var8;
      }

      determineNumSysAndCheckDigit(var3, var7);
      return var6;
   }

   BarcodeFormat getBarcodeFormat() {
      return BarcodeFormat.UPC_E;
   }
}
