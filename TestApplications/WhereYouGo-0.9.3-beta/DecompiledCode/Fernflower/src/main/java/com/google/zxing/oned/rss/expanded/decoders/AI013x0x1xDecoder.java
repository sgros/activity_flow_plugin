package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;

final class AI013x0x1xDecoder extends AI01weightDecoder {
   private static final int DATE_SIZE = 16;
   private static final int HEADER_SIZE = 8;
   private static final int WEIGHT_SIZE = 20;
   private final String dateCode;
   private final String firstAIdigits;

   AI013x0x1xDecoder(BitArray var1, String var2, String var3) {
      super(var1);
      this.dateCode = var3;
      this.firstAIdigits = var2;
   }

   private void encodeCompressedDate(StringBuilder var1, int var2) {
      int var3 = this.getGeneralDecoder().extractNumericValueFromBitArray(var2, 16);
      if (var3 != 38400) {
         var1.append('(');
         var1.append(this.dateCode);
         var1.append(')');
         var2 = var3 % 32;
         int var4 = var3 / 32;
         var3 = var4 % 12 + 1;
         var4 /= 12;
         if (var4 / 10 == 0) {
            var1.append('0');
         }

         var1.append(var4);
         if (var3 / 10 == 0) {
            var1.append('0');
         }

         var1.append(var3);
         if (var2 / 10 == 0) {
            var1.append('0');
         }

         var1.append(var2);
      }

   }

   protected void addWeightCode(StringBuilder var1, int var2) {
      var1.append('(');
      var1.append(this.firstAIdigits);
      var1.append(var2 / 100000);
      var1.append(')');
   }

   protected int checkWeight(int var1) {
      return var1 % 100000;
   }

   public String parseInformation() throws NotFoundException {
      if (this.getInformation().getSize() != 84) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         StringBuilder var1 = new StringBuilder();
         this.encodeCompressedGtin(var1, 8);
         this.encodeCompressedWeight(var1, 48, 20);
         this.encodeCompressedDate(var1, 68);
         return var1.toString();
      }
   }
}
