package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.common.BitArray;

final class AI01320xDecoder extends AI013x0xDecoder {
   AI01320xDecoder(BitArray var1) {
      super(var1);
   }

   protected void addWeightCode(StringBuilder var1, int var2) {
      if (var2 < 10000) {
         var1.append("(3202)");
      } else {
         var1.append("(3203)");
      }

   }

   protected int checkWeight(int var1) {
      if (var1 >= 10000) {
         var1 -= 10000;
      }

      return var1;
   }
}
