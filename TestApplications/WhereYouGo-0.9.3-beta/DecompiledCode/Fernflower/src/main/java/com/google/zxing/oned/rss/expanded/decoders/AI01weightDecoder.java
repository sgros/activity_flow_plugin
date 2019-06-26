package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.common.BitArray;

abstract class AI01weightDecoder extends AI01decoder {
   AI01weightDecoder(BitArray var1) {
      super(var1);
   }

   protected abstract void addWeightCode(StringBuilder var1, int var2);

   protected abstract int checkWeight(int var1);

   final void encodeCompressedWeight(StringBuilder var1, int var2, int var3) {
      var2 = this.getGeneralDecoder().extractNumericValueFromBitArray(var2, var3);
      this.addWeightCode(var1, var2);
      int var4 = this.checkWeight(var2);
      var3 = 100000;

      for(var2 = 0; var2 < 5; ++var2) {
         if (var4 / var3 == 0) {
            var1.append('0');
         }

         var3 /= 10;
      }

      var1.append(var4);
   }
}
