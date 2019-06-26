package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.util.Map;

public final class ITFWriter extends OneDimensionalCodeWriter {
   private static final int[] END_PATTERN = new int[]{3, 1, 1};
   private static final int[] START_PATTERN = new int[]{1, 1, 1, 1};

   public BitMatrix encode(String var1, BarcodeFormat var2, int var3, int var4, Map var5) throws WriterException {
      if (var2 != BarcodeFormat.ITF) {
         throw new IllegalArgumentException("Can only encode ITF, but got " + var2);
      } else {
         return super.encode(var1, var2, var3, var4, var5);
      }
   }

   public boolean[] encode(String var1) {
      int var2 = var1.length();
      if (var2 % 2 != 0) {
         throw new IllegalArgumentException("The length of the input should be even");
      } else if (var2 > 80) {
         throw new IllegalArgumentException("Requested contents should be less than 80 digits long, but got " + var2);
      } else {
         boolean[] var3 = new boolean[var2 * 9 + 9];
         int var4 = appendPattern(var3, 0, START_PATTERN, true);

         for(int var5 = 0; var5 < var2; var5 += 2) {
            int var6 = Character.digit(var1.charAt(var5), 10);
            int var7 = Character.digit(var1.charAt(var5 + 1), 10);
            int[] var8 = new int[18];

            for(int var9 = 0; var9 < 5; ++var9) {
               var8[var9 * 2] = ITFReader.PATTERNS[var6][var9];
               var8[var9 * 2 + 1] = ITFReader.PATTERNS[var7][var9];
            }

            var4 += appendPattern(var3, var4, var8, true);
         }

         appendPattern(var3, var4, END_PATTERN, true);
         return var3;
      }
   }
}
