package com.google.zxing.qrcode.decoder;

final class FormatInformation {
   private static final int[][] FORMAT_INFO_DECODE_LOOKUP;
   private static final int FORMAT_INFO_MASK_QR = 21522;
   private final byte dataMask;
   private final ErrorCorrectionLevel errorCorrectionLevel;

   static {
      int[] var0 = new int[]{23371, 3};
      int[] var1 = new int[]{16590, 5};
      int[] var2 = new int[]{29427, 9};
      int[] var3 = new int[]{32170, 10};
      int[] var4 = new int[]{26998, 15};
      int[] var5 = new int[]{5054, 17};
      int[] var6 = new int[]{2107, 23};
      int[] var7 = new int[]{14854, 27};
      int[] var8 = new int[]{11994, 30};
      FORMAT_INFO_DECODE_LOOKUP = new int[][]{{21522, 0}, {20773, 1}, {24188, 2}, var0, {17913, 4}, var1, {20375, 6}, {19104, 7}, {30660, 8}, var2, var3, {30877, 11}, {26159, 12}, {25368, 13}, {27713, 14}, var4, {5769, 16}, var5, {7399, 18}, {6608, 19}, {1890, 20}, {597, 21}, {3340, 22}, var6, {13663, 24}, {12392, 25}, {16177, 26}, var7, {9396, 28}, {8579, 29}, var8, {11245, 31}};
   }

   private FormatInformation(int var1) {
      this.errorCorrectionLevel = ErrorCorrectionLevel.forBits(var1 >> 3 & 3);
      this.dataMask = (byte)((byte)(var1 & 7));
   }

   static FormatInformation decodeFormatInformation(int var0, int var1) {
      FormatInformation var2 = doDecodeFormatInformation(var0, var1);
      if (var2 == null) {
         var2 = doDecodeFormatInformation(var0 ^ 21522, var1 ^ 21522);
      }

      return var2;
   }

   private static FormatInformation doDecodeFormatInformation(int var0, int var1) {
      int var2 = Integer.MAX_VALUE;
      int var3 = 0;
      int[][] var4 = FORMAT_INFO_DECODE_LOOKUP;
      int var5 = var4.length;
      int var6 = 0;

      FormatInformation var11;
      while(true) {
         if (var6 < var5) {
            int[] var7 = var4[var6];
            int var8 = var7[0];
            if (var8 != var0 && var8 != var1) {
               int var9 = numBitsDiffering(var0, var8);
               int var10 = var2;
               if (var9 < var2) {
                  var3 = var7[1];
                  var10 = var9;
               }

               var2 = var10;
               var9 = var3;
               if (var0 != var1) {
                  var8 = numBitsDiffering(var1, var8);
                  var2 = var10;
                  var9 = var3;
                  if (var8 < var10) {
                     var9 = var7[1];
                     var2 = var8;
                  }
               }

               ++var6;
               var3 = var9;
               continue;
            }

            var11 = new FormatInformation(var7[1]);
            break;
         }

         if (var2 <= 3) {
            var11 = new FormatInformation(var3);
         } else {
            var11 = null;
         }
         break;
      }

      return var11;
   }

   static int numBitsDiffering(int var0, int var1) {
      return Integer.bitCount(var0 ^ var1);
   }

   public boolean equals(Object var1) {
      boolean var2 = false;
      boolean var3;
      if (!(var1 instanceof FormatInformation)) {
         var3 = var2;
      } else {
         FormatInformation var4 = (FormatInformation)var1;
         var3 = var2;
         if (this.errorCorrectionLevel == var4.errorCorrectionLevel) {
            var3 = var2;
            if (this.dataMask == var4.dataMask) {
               var3 = true;
            }
         }
      }

      return var3;
   }

   byte getDataMask() {
      return this.dataMask;
   }

   ErrorCorrectionLevel getErrorCorrectionLevel() {
      return this.errorCorrectionLevel;
   }

   public int hashCode() {
      return this.errorCorrectionLevel.ordinal() << 3 | this.dataMask;
   }
}
