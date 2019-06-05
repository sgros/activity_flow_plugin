package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;

final class AnyAIDecoder extends AbstractExpandedDecoder {
   private static final int HEADER_SIZE = 5;

   AnyAIDecoder(BitArray var1) {
      super(var1);
   }

   public String parseInformation() throws NotFoundException, FormatException {
      StringBuilder var1 = new StringBuilder();
      return this.getGeneralDecoder().decodeAllCodes(var1, 5);
   }
}
