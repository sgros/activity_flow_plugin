package com.google.zxing.datamatrix.encoder;

public final class ErrorCorrection {
   private static final int[] ALOG;
   private static final int[][] FACTORS;
   private static final int[] FACTOR_SETS = new int[]{5, 7, 10, 11, 12, 14, 18, 20, 24, 28, 36, 42, 48, 56, 62, 68};
   private static final int[] LOG;
   private static final int MODULO_VALUE = 301;

   static {
      int[] var0 = new int[]{228, 48, 15, 111, 62};
      int[] var1 = new int[]{41, 153, 158, 91, 61, 42, 142, 213, 97, 178, 100, 242};
      int[] var2 = new int[]{156, 97, 192, 252, 95, 9, 157, 119, 138, 45, 18, 186, 83, 185};
      int[] var3 = new int[]{77, 193, 137, 31, 19, 38, 22, 153, 247, 105, 122, 2, 245, 133, 242, 8, 175, 95, 100, 9, 167, 105, 214, 111, 57, 121, 21, 1, 253, 57, 54, 101, 248, 202, 69, 50, 150, 177, 226, 5, 9, 5};
      FACTORS = new int[][]{var0, {23, 68, 144, 134, 240, 92, 254}, {28, 24, 185, 166, 223, 248, 116, 255, 110, 61}, {175, 138, 205, 12, 194, 168, 39, 245, 60, 97, 120}, var1, var2, {83, 195, 100, 39, 188, 75, 66, 61, 241, 213, 109, 129, 94, 254, 225, 48, 90, 188}, {15, 195, 244, 9, 233, 71, 168, 2, 188, 160, 153, 145, 253, 79, 108, 82, 27, 174, 186, 172}, {52, 190, 88, 205, 109, 39, 176, 21, 155, 197, 251, 223, 155, 21, 5, 172, 254, 124, 12, 181, 184, 96, 50, 193}, {211, 231, 43, 97, 71, 96, 103, 174, 37, 151, 170, 53, 75, 34, 249, 121, 17, 138, 110, 213, 141, 136, 120, 151, 233, 168, 93, 255}, {245, 127, 242, 218, 130, 250, 162, 181, 102, 120, 84, 179, 220, 251, 80, 182, 229, 18, 2, 4, 68, 33, 101, 137, 95, 119, 115, 44, 175, 184, 59, 25, 225, 98, 81, 112}, var3, {245, 132, 172, 223, 96, 32, 117, 22, 238, 133, 238, 231, 205, 188, 237, 87, 191, 106, 16, 147, 118, 23, 37, 90, 170, 205, 131, 88, 120, 100, 66, 138, 186, 240, 82, 44, 176, 87, 187, 147, 160, 175, 69, 213, 92, 253, 225, 19}, {175, 9, 223, 238, 12, 17, 220, 208, 100, 29, 175, 170, 230, 192, 215, 235, 150, 159, 36, 223, 38, 200, 132, 54, 228, 146, 218, 234, 117, 203, 29, 232, 144, 238, 22, 150, 201, 117, 62, 207, 164, 13, 137, 245, 127, 67, 247, 28, 155, 43, 203, 107, 233, 53, 143, 46}, {242, 93, 169, 50, 144, 210, 39, 118, 202, 188, 201, 189, 143, 108, 196, 37, 185, 112, 134, 230, 245, 63, 197, 190, 250, 106, 185, 221, 175, 64, 114, 71, 161, 44, 147, 6, 27, 218, 51, 63, 87, 10, 40, 130, 188, 17, 163, 31, 176, 170, 4, 107, 232, 7, 94, 166, 224, 124, 86, 47, 11, 204}, {220, 228, 173, 89, 251, 149, 159, 56, 89, 33, 147, 244, 154, 36, 73, 127, 213, 136, 248, 180, 234, 197, 158, 177, 68, 122, 93, 213, 15, 160, 227, 236, 66, 139, 153, 185, 202, 167, 179, 25, 220, 232, 96, 210, 231, 136, 223, 239, 181, 241, 59, 52, 172, 25, 49, 232, 211, 189, 64, 54, 108, 153, 132, 63, 96, 103, 82, 186}};
      LOG = new int[256];
      ALOG = new int[255];
      int var4 = 1;

      for(int var5 = 0; var5 < 255; ++var5) {
         ALOG[var5] = var4;
         LOG[var4] = var5;
         int var6 = var4 << 1;
         var4 = var6;
         if (var6 >= 256) {
            var4 = var6 ^ 301;
         }
      }

   }

   private ErrorCorrection() {
   }

   private static String createECCBlock(CharSequence var0, int var1) {
      return createECCBlock(var0, 0, var0.length(), var1);
   }

   private static String createECCBlock(CharSequence var0, int var1, int var2, int var3) {
      byte var4 = -1;
      int var5 = 0;

      int var6;
      while(true) {
         var6 = var4;
         if (var5 >= FACTOR_SETS.length) {
            break;
         }

         if (FACTOR_SETS[var5] == var3) {
            var6 = var5;
            break;
         }

         ++var5;
      }

      if (var6 < 0) {
         throw new IllegalArgumentException("Illegal number of error correction codewords specified: " + var3);
      } else {
         int[] var7 = FACTORS[var6];
         char[] var8 = new char[var3];

         for(var5 = 0; var5 < var3; ++var5) {
            var8[var5] = (char)0;
         }

         for(var5 = var1; var5 < var1 + var2; ++var5) {
            int var10 = var8[var3 - 1] ^ var0.charAt(var5);

            for(var6 = var3 - 1; var6 > 0; --var6) {
               if (var10 != 0 && var7[var6] != 0) {
                  var8[var6] = (char)((char)(var8[var6 - 1] ^ ALOG[(LOG[var10] + LOG[var7[var6]]) % 255]));
               } else {
                  var8[var6] = (char)var8[var6 - 1];
               }
            }

            if (var10 != 0 && var7[0] != 0) {
               var8[0] = (char)((char)ALOG[(LOG[var10] + LOG[var7[0]]) % 255]);
            } else {
               var8[0] = (char)0;
            }
         }

         char[] var9 = new char[var3];

         for(var1 = 0; var1 < var3; ++var1) {
            var9[var1] = (char)var8[var3 - var1 - 1];
         }

         return String.valueOf(var9);
      }
   }

   public static String encodeECC200(String var0, SymbolInfo var1) {
      if (var0.length() != var1.getDataCapacity()) {
         throw new IllegalArgumentException("The number of codewords does not match the selected symbol");
      } else {
         StringBuilder var2 = new StringBuilder(var1.getDataCapacity() + var1.getErrorCodewords());
         var2.append(var0);
         int var3 = var1.getInterleavedBlockCount();
         if (var3 == 1) {
            var2.append(createECCBlock(var0, var1.getErrorCodewords()));
         } else {
            var2.setLength(var2.capacity());
            int[] var4 = new int[var3];
            int[] var5 = new int[var3];
            int[] var6 = new int[var3];

            int var7;
            for(var7 = 0; var7 < var3; ++var7) {
               var4[var7] = var1.getDataLengthForInterleavedBlock(var7 + 1);
               var5[var7] = var1.getErrorLengthForInterleavedBlock(var7 + 1);
               var6[var7] = 0;
               if (var7 > 0) {
                  var6[var7] = var6[var7 - 1] + var4[var7];
               }
            }

            for(var7 = 0; var7 < var3; ++var7) {
               StringBuilder var10 = new StringBuilder(var4[var7]);

               int var8;
               for(var8 = var7; var8 < var1.getDataCapacity(); var8 += var3) {
                  var10.append(var0.charAt(var8));
               }

               String var11 = createECCBlock(var10.toString(), var5[var7]);
               var8 = 0;

               for(int var9 = var7; var9 < var5[var7] * var3; ++var8) {
                  var2.setCharAt(var1.getDataCapacity() + var9, var11.charAt(var8));
                  var9 += var3;
               }
            }
         }

         return var2.toString();
      }
   }
}
