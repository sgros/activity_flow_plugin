package com.google.zxing.qrcode.decoder;

public enum ErrorCorrectionLevel {
   private static final ErrorCorrectionLevel[] FOR_BITS = new ErrorCorrectionLevel[]{M, L, H, Q};
   H(2),
   L(1),
   M(0),
   Q(3);

   private final int bits;

   private ErrorCorrectionLevel(int var3) {
      this.bits = var3;
   }

   public static ErrorCorrectionLevel forBits(int var0) {
      if (var0 >= 0 && var0 < FOR_BITS.length) {
         return FOR_BITS[var0];
      } else {
         throw new IllegalArgumentException();
      }
   }

   public int getBits() {
      return this.bits;
   }
}
