package com.journeyapps.barcodescanner;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.common.HybridBinarizer;

public class InvertedDecoder extends Decoder {
   public InvertedDecoder(Reader var1) {
      super(var1);
   }

   protected BinaryBitmap toBitmap(LuminanceSource var1) {
      return new BinaryBitmap(new HybridBinarizer(var1.invert()));
   }
}
