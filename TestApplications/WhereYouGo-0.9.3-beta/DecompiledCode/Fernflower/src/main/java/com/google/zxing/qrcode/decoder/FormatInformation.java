package com.google.zxing.qrcode.decoder;

final class FormatInformation {
   private static final int[][] FORMAT_INFO_DECODE_LOOKUP;
   private static final int FORMAT_INFO_MASK_QR = 21522;
   private final byte dataMask;
   private final ErrorCorrectionLevel errorCorrectionLevel;

   static {
      int[] var0 = new int[]{21522, 0};
      int[] var1 = new int[]{23371, 3};
      int[] var2 = new int[]{17913, 4};
      int[] var3 = new int[]{16590, 5};
      int[] var4 = new int[]{19104, 7};
      int[] var5 = new int[]{29427, 9};
      int[] var6 = new int[]{32170, 10};
      int[] var7 = new int[]{30877, 11};
      int[] var8 = new int[]{26159, 12};
      int[] var9 = new int[]{26998, 15};
      int[] var10 = new int[]{5769, 16};
      int[] var11 = new int[]{7399, 18};
      int[] var12 = new int[]{6608, 19};
      int[] var13 = new int[]{2107, 23};
      int[] var14 = new int[]{13663, 24};
      int[] var15 = new int[]{12392, 25};
      int[] var16 = new int[]{16177, 26};
      int[] var17 = new int[]{14854, 27};
      int[] var18 = new int[]{8579, 29};
      int[] var19 = new int[]{11994, 30};
      FORMAT_INFO_DECODE_LOOKUP = new int[][]{var0, {20773, 1}, {24188, 2}, var1, var2, var3, {20375, 6}, var4, {30660, 8}, var5, var6, var7, var8, {25368, 13}, {27713, 14}, var9, var10, {5054, 17}, var11, var12, {1890, 20}, {597, 21}, {3340, 22}, var13, var14, var15, var16, var17, {9396, 28}, var18, var19, {11245, 31}};
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

      FormatInformation var7;
      while(true) {
         if (var6 < var5) {
            int[] var11 = var4[var6];
            int var8 = var11[0];
            if (var8 != var0 && var8 != var1) {
               int var9 = numBitsDiffering(var0, var8);
               int var10 = var2;
               if (var9 < var2) {
                  var3 = var11[1];
                  var10 = var9;
               }

               var2 = var10;
               var9 = var3;
               if (var0 != var1) {
                  var8 = numBitsDiffering(var1, var8);
                  var2 = var10;
                  var9 = var3;
                  if (var8 < var10) {
                     var9 = var11[1];
                     var2 = var8;
                  }
               }

               ++var6;
               var3 = var9;
               continue;
            }

            var7 = new FormatInformation(var11[1]);
            break;
         }

         if (var2 <= 3) {
            var7 = new FormatInformation(var3);
         } else {
            var7 = null;
         }
         break;
      }

      return var7;
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
