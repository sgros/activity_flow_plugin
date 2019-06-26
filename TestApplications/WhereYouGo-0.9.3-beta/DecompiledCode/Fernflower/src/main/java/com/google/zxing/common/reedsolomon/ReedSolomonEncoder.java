package com.google.zxing.common.reedsolomon;

import java.util.ArrayList;
import java.util.List;

public final class ReedSolomonEncoder {
   private final List cachedGenerators;
   private final GenericGF field;

   public ReedSolomonEncoder(GenericGF var1) {
      this.field = var1;
      this.cachedGenerators = new ArrayList();
      this.cachedGenerators.add(new GenericGFPoly(var1, new int[]{1}));
   }

   private GenericGFPoly buildGenerator(int var1) {
      if (var1 >= this.cachedGenerators.size()) {
         GenericGFPoly var2 = (GenericGFPoly)this.cachedGenerators.get(this.cachedGenerators.size() - 1);

         for(int var3 = this.cachedGenerators.size(); var3 <= var1; ++var3) {
            var2 = var2.multiply(new GenericGFPoly(this.field, new int[]{1, this.field.exp(var3 - 1 + this.field.getGeneratorBase())}));
            this.cachedGenerators.add(var2);
         }
      }

      return (GenericGFPoly)this.cachedGenerators.get(var1);
   }

   public void encode(int[] var1, int var2) {
      if (var2 == 0) {
         throw new IllegalArgumentException("No error correction bytes");
      } else {
         int var3 = var1.length - var2;
         if (var3 <= 0) {
            throw new IllegalArgumentException("No data bytes provided");
         } else {
            GenericGFPoly var4 = this.buildGenerator(var2);
            int[] var5 = new int[var3];
            System.arraycopy(var1, 0, var5, 0, var3);
            int[] var7 = (new GenericGFPoly(this.field, var5)).multiplyByMonomial(var2, 1).divide(var4)[1].getCoefficients();
            int var6 = var2 - var7.length;

            for(var2 = 0; var2 < var6; ++var2) {
               var1[var3 + var2] = 0;
            }

            System.arraycopy(var7, 0, var1, var3 + var6, var7.length);
         }
      }
   }
}
