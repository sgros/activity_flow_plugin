package com.google.zxing.qrcode.decoder;

import com.google.zxing.ResultPoint;

public final class QRCodeDecoderMetaData {
   private final boolean mirrored;

   QRCodeDecoderMetaData(boolean var1) {
      this.mirrored = var1;
   }

   public void applyMirroredCorrection(ResultPoint[] var1) {
      if (this.mirrored && var1 != null && var1.length >= 3) {
         ResultPoint var2 = var1[0];
         var1[0] = var1[2];
         var1[2] = var2;
      }

   }

   public boolean isMirrored() {
      return this.mirrored;
   }
}
