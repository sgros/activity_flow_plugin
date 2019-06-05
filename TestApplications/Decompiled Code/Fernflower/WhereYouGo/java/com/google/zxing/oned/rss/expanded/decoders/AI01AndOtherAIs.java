package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;

final class AI01AndOtherAIs extends AI01decoder {
   private static final int HEADER_SIZE = 4;

   AI01AndOtherAIs(BitArray var1) {
      super(var1);
   }

   public String parseInformation() throws NotFoundException, FormatException {
      StringBuilder var1 = new StringBuilder();
      var1.append("(01)");
      int var2 = var1.length();
      var1.append(this.getGeneralDecoder().extractNumericValueFromBitArray(4, 4));
      this.encodeCompressedGtinWithoutAI(var1, 8, var2);
      return this.getGeneralDecoder().decodeAllCodes(var1, 48);
   }
}
