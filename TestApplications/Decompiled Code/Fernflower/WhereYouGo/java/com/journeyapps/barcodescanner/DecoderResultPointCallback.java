package com.journeyapps.barcodescanner;

import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;

public class DecoderResultPointCallback implements ResultPointCallback {
   private Decoder decoder;

   public DecoderResultPointCallback() {
   }

   public DecoderResultPointCallback(Decoder var1) {
      this.decoder = var1;
   }

   public void foundPossibleResultPoint(ResultPoint var1) {
      if (this.decoder != null) {
         this.decoder.foundPossibleResultPoint(var1);
      }

   }

   public Decoder getDecoder() {
      return this.decoder;
   }

   public void setDecoder(Decoder var1) {
      this.decoder = var1;
   }
}
