package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;

final class AI01392xDecoder extends AI01decoder {
   private static final int HEADER_SIZE = 8;
   private static final int LAST_DIGIT_SIZE = 2;

   AI01392xDecoder(BitArray var1) {
      super(var1);
   }

   public String parseInformation() throws NotFoundException, FormatException {
      if (this.getInformation().getSize() < 48) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         StringBuilder var1 = new StringBuilder();
         this.encodeCompressedGtin(var1, 8);
         int var2 = this.getGeneralDecoder().extractNumericValueFromBitArray(48, 2);
         var1.append("(392");
         var1.append(var2);
         var1.append(')');
         var1.append(this.getGeneralDecoder().decodeGeneralPurposeField(50, (String)null).getNewString());
         return var1.toString();
      }
   }
}
