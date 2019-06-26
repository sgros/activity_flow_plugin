package com.google.zxing.common.reedsolomon;

public final class ReedSolomonDecoder {
   private final GenericGF field;

   public ReedSolomonDecoder(GenericGF var1) {
      this.field = var1;
   }

   private int[] findErrorLocations(GenericGFPoly var1) throws ReedSolomonException {
      int var2 = var1.getDegree();
      int[] var3;
      int[] var7;
      if (var2 == 1) {
         var3 = new int[]{var1.getCoefficient(1)};
         var7 = var3;
      } else {
         var3 = new int[var2];
         int var4 = 0;

         int var6;
         for(int var5 = 1; var5 < this.field.getSize() && var4 < var2; var4 = var6) {
            var6 = var4;
            if (var1.evaluateAt(var5) == 0) {
               var3[var4] = this.field.inverse(var5);
               var6 = var4 + 1;
            }

            ++var5;
         }

         var7 = var3;
         if (var4 != var2) {
            throw new ReedSolomonException("Error locator degree does not match number of roots");
         }
      }

      return var7;
   }

   private int[] findErrorMagnitudes(GenericGFPoly var1, int[] var2) {
      int var3 = var2.length;
      int[] var4 = new int[var3];

      for(int var5 = 0; var5 < var3; ++var5) {
         int var6 = this.field.inverse(var2[var5]);
         int var7 = 1;

         int var9;
         for(int var8 = 0; var8 < var3; var7 = var9) {
            var9 = var7;
            if (var5 != var8) {
               var9 = this.field.multiply(var2[var8], var6);
               if ((var9 & 1) == 0) {
                  var9 |= 1;
               } else {
                  var9 &= -2;
               }

               var9 = this.field.multiply(var7, var9);
            }

            ++var8;
         }

         var4[var5] = this.field.multiply(var1.evaluateAt(var6), this.field.inverse(var7));
         if (this.field.getGeneratorBase() != 0) {
            var4[var5] = this.field.multiply(var4[var5], var6);
         }
      }

      return var4;
   }

   private GenericGFPoly[] runEuclideanAlgorithm(GenericGFPoly var1, GenericGFPoly var2, int var3) throws ReedSolomonException {
      GenericGFPoly var4 = var1;
      GenericGFPoly var5 = var2;
      if (var1.getDegree() < var2.getDegree()) {
         var5 = var1;
         var4 = var2;
      }

      GenericGFPoly var6 = this.field.getZero();
      var2 = this.field.getOne();

      GenericGFPoly var8;
      do {
         GenericGFPoly var7 = var6;
         if (var5.getDegree() < var3 / 2) {
            var3 = var2.getCoefficient(0);
            if (var3 == 0) {
               throw new ReedSolomonException("sigmaTilde(0) was zero");
            }

            var3 = this.field.inverse(var3);
            return new GenericGFPoly[]{var2.multiply(var3), var5.multiply(var3)};
         }

         var8 = var5;
         var6 = var2;
         if (var5.isZero()) {
            throw new ReedSolomonException("r_{i-1} was zero");
         }

         var1 = var4;
         var2 = this.field.getZero();
         int var9 = var5.getCoefficient(var5.getDegree());

         int var10;
         int var11;
         for(var9 = this.field.inverse(var9); var1.getDegree() >= var8.getDegree() && !var1.isZero(); var1 = var1.addOrSubtract(var8.multiplyByMonomial(var10, var11))) {
            var10 = var1.getDegree() - var8.getDegree();
            var11 = this.field.multiply(var1.getCoefficient(var1.getDegree()), var9);
            var2 = var2.addOrSubtract(this.field.buildMonomial(var10, var11));
         }

         var2 = var2.multiply(var6).addOrSubtract(var7);
         var5 = var1;
         var4 = var8;
      } while(var1.getDegree() < var8.getDegree());

      throw new IllegalStateException("Division algorithm failed to reduce polynomial?");
   }

   public void decode(int[] var1, int var2) throws ReedSolomonException {
      GenericGFPoly var3 = new GenericGFPoly(this.field, var1);
      int[] var4 = new int[var2];
      boolean var5 = true;

      int var6;
      for(var6 = 0; var6 < var2; ++var6) {
         int var7 = var3.evaluateAt(this.field.exp(this.field.getGeneratorBase() + var6));
         var4[var2 - 1 - var6] = var7;
         if (var7 != 0) {
            var5 = false;
         }
      }

      if (!var5) {
         var3 = new GenericGFPoly(this.field, var4);
         GenericGFPoly[] var8 = this.runEuclideanAlgorithm(this.field.buildMonomial(var2, 1), var3, var2);
         GenericGFPoly var9 = var8[0];
         var3 = var8[1];
         var4 = this.findErrorLocations(var9);
         int[] var10 = this.findErrorMagnitudes(var3, var4);

         for(var2 = 0; var2 < var4.length; ++var2) {
            var6 = var1.length - 1 - this.field.log(var4[var2]);
            if (var6 < 0) {
               throw new ReedSolomonException("Bad error location");
            }

            var1[var6] = GenericGF.addOrSubtract(var1[var6], var10[var2]);
         }
      }

   }
}
