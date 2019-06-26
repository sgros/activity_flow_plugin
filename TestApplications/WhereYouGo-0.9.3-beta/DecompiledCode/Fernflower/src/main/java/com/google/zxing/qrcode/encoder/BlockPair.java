package com.google.zxing.qrcode.encoder;

final class BlockPair {
   private final byte[] dataBytes;
   private final byte[] errorCorrectionBytes;

   BlockPair(byte[] var1, byte[] var2) {
      this.dataBytes = var1;
      this.errorCorrectionBytes = var2;
   }

   public byte[] getDataBytes() {
      return this.dataBytes;
   }

   public byte[] getErrorCorrectionBytes() {
      return this.errorCorrectionBytes;
   }
}
