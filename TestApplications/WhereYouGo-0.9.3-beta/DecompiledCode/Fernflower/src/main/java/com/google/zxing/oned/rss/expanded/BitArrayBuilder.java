package com.google.zxing.oned.rss.expanded;

import com.google.zxing.common.BitArray;
import java.util.List;

final class BitArrayBuilder {
   private BitArrayBuilder() {
   }

   static BitArray buildBitArray(List var0) {
      int var1 = (var0.size() << 1) - 1;
      int var2 = var1;
      if (((ExpandedPair)var0.get(var0.size() - 1)).getRightChar() == null) {
         var2 = var1 - 1;
      }

      BitArray var3 = new BitArray(var2 * 12);
      var2 = 0;
      int var4 = ((ExpandedPair)var0.get(0)).getRightChar().getValue();

      for(var1 = 11; var1 >= 0; --var1) {
         if ((1 << var1 & var4) != 0) {
            var3.set(var2);
         }

         ++var2;
      }

      for(var4 = 1; var4 < var0.size(); var2 = var1) {
         ExpandedPair var5 = (ExpandedPair)var0.get(var4);
         int var6 = var5.getLeftChar().getValue();

         for(var1 = 11; var1 >= 0; --var1) {
            if ((1 << var1 & var6) != 0) {
               var3.set(var2);
            }

            ++var2;
         }

         var1 = var2;
         if (var5.getRightChar() != null) {
            int var7 = var5.getRightChar().getValue();
            var6 = 11;

            while(true) {
               var1 = var2;
               if (var6 < 0) {
                  break;
               }

               if ((1 << var6 & var7) != 0) {
                  var3.set(var2);
               }

               ++var2;
               --var6;
            }
         }

         ++var4;
      }

      return var3;
   }
}
