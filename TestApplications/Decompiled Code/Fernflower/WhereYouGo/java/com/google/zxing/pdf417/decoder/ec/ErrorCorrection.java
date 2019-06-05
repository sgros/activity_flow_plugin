package com.google.zxing.pdf417.decoder.ec;

import com.google.zxing.ChecksumException;

public final class ErrorCorrection {
   private final ModulusGF field;

   public ErrorCorrection() {
      this.field = ModulusGF.PDF417_GF;
   }

   private int[] findErrorLocations(ModulusPoly var1) throws ChecksumException {
      int var2 = var1.getDegree();
      int[] var3 = new int[var2];
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

      if (var4 != var2) {
         throw ChecksumException.getChecksumInstance();
      } else {
         return var3;
      }
   }

   private int[] findErrorMagnitudes(ModulusPoly var1, ModulusPoly var2, int[] var3) {
      int var4 = var2.getDegree();
      int[] var5 = new int[var4];

      int var6;
      for(var6 = 1; var6 <= var4; ++var6) {
         var5[var4 - var6] = this.field.multiply(var6, var2.getCoefficient(var6));
      }

      ModulusPoly var10 = new ModulusPoly(this.field, var5);
      var4 = var3.length;
      int[] var9 = new int[var4];

      for(var6 = 0; var6 < var4; ++var6) {
         int var7 = this.field.inverse(var3[var6]);
         int var8 = this.field.subtract(0, var1.evaluateAt(var7));
         var7 = this.field.inverse(var10.evaluateAt(var7));
         var9[var6] = this.field.multiply(var8, var7);
      }

      return var9;
   }

   private ModulusPoly[] runEuclideanAlgorithm(ModulusPoly var1, ModulusPoly var2, int var3) throws ChecksumException {
      ModulusPoly var4 = var1;
      ModulusPoly var5 = var2;
      if (var1.getDegree() < var2.getDegree()) {
         var5 = var1;
         var4 = var2;
      }

      var2 = var4;
      ModulusPoly var6 = var5;
      var4 = this.field.getZero();
      var1 = this.field.getOne();
      var5 = var2;
      var2 = var6;

      while(true) {
         ModulusPoly var7 = var4;
         var6 = var5;
         if (var2.getDegree() < var3 / 2) {
            var3 = var1.getCoefficient(0);
            if (var3 == 0) {
               throw ChecksumException.getChecksumInstance();
            }

            var3 = this.field.inverse(var3);
            return new ModulusPoly[]{var1.multiply(var3), var2.multiply(var3)};
         }

         var5 = var2;
         var4 = var1;
         if (var2.isZero()) {
            throw ChecksumException.getChecksumInstance();
         }

         var2 = var6;
         var1 = this.field.getZero();
         int var8 = var5.getCoefficient(var5.getDegree());

         int var10;
         for(int var9 = this.field.inverse(var8); var2.getDegree() >= var5.getDegree() && !var2.isZero(); var2 = var2.subtract(var5.multiplyByMonomial(var8, var10))) {
            var8 = var2.getDegree() - var5.getDegree();
            var10 = this.field.multiply(var2.getCoefficient(var2.getDegree()), var9);
            var1 = var1.add(this.field.buildMonomial(var8, var10));
         }

         var1 = var1.multiply(var4).subtract(var7).negative();
      }
   }

   public int decode(int[] var1, int var2, int[] var3) throws ChecksumException {
      ModulusPoly var4 = new ModulusPoly(this.field, var1);
      int[] var5 = new int[var2];
      boolean var6 = false;

      int var7;
      int var8;
      for(var7 = var2; var7 > 0; --var7) {
         var8 = var4.evaluateAt(this.field.exp(var7));
         var5[var2 - var7] = var8;
         if (var8 != 0) {
            var6 = true;
         }
      }

      if (!var6) {
         var2 = 0;
      } else {
         var4 = this.field.getOne();
         if (var3 != null) {
            int var13 = var3.length;

            for(var7 = 0; var7 < var13; ++var7) {
               var8 = var3[var7];
               var8 = this.field.exp(var1.length - 1 - var8);
               var4 = var4.multiply(new ModulusPoly(this.field, new int[]{this.field.subtract(0, var8), 1}));
            }
         }

         ModulusPoly var9 = new ModulusPoly(this.field, var5);
         ModulusPoly[] var10 = this.runEuclideanAlgorithm(this.field.buildMonomial(var2, 1), var9, var2);
         var9 = var10[0];
         ModulusPoly var11 = var10[1];
         int[] var12 = this.findErrorLocations(var9);
         var3 = this.findErrorMagnitudes(var11, var9, var12);

         for(var2 = 0; var2 < var12.length; ++var2) {
            var7 = var1.length - 1 - this.field.log(var12[var2]);
            if (var7 < 0) {
               throw ChecksumException.getChecksumInstance();
            }

            var1[var7] = this.field.subtract(var1[var7], var3[var2]);
         }

         var2 = var12.length;
      }

      return var2;
   }
}
