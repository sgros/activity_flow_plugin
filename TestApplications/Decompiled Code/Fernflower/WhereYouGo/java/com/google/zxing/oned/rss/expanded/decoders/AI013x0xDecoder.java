package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;

abstract class AI013x0xDecoder extends AI01weightDecoder {
   private static final int HEADER_SIZE = 5;
   private static final int WEIGHT_SIZE = 15;

   AI013x0xDecoder(BitArray var1) {
      super(var1);
   }

   public String parseInformation() throws NotFoundException {
      if (this.getInformation().getSize() != 60) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         StringBuilder var1 = new StringBuilder();
         this.encodeCompressedGtin(var1, 5);
         this.encodeCompressedWeight(var1, 45, 15);
         return var1.toString();
      }
   }
}
