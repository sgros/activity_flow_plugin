package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.common.BitArray;

final class AI013103decoder extends AI013x0xDecoder {
   AI013103decoder(BitArray var1) {
      super(var1);
   }

   protected void addWeightCode(StringBuilder var1, int var2) {
      var1.append("(3103)");
   }

   protected int checkWeight(int var1) {
      return var1;
   }
}
