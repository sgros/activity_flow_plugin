package com.google.zxing.pdf417.decoder.ec;

public final class ModulusGF {
   public static final ModulusGF PDF417_GF = new ModulusGF(929, 3);
   private final int[] expTable;
   private final int[] logTable;
   private final int modulus;
   private final ModulusPoly one;
   private final ModulusPoly zero;

   private ModulusGF(int var1, int var2) {
      this.modulus = var1;
      this.expTable = new int[var1];
      this.logTable = new int[var1];
      int var3 = 1;

      for(int var4 = 0; var4 < var1; ++var4) {
         this.expTable[var4] = var3;
         var3 = var3 * var2 % var1;
      }

      for(var2 = 0; var2 < var1 - 1; this.logTable[this.expTable[var2]] = var2++) {
      }

      this.zero = new ModulusPoly(this, new int[]{0});
      this.one = new ModulusPoly(this, new int[]{1});
   }

   int add(int var1, int var2) {
      return (var1 + var2) % this.modulus;
   }

   ModulusPoly buildMonomial(int var1, int var2) {
      if (var1 < 0) {
         throw new IllegalArgumentException();
      } else {
         ModulusPoly var3;
         if (var2 == 0) {
            var3 = this.zero;
         } else {
            int[] var4 = new int[var1 + 1];
            var4[0] = var2;
            var3 = new ModulusPoly(this, var4);
         }

         return var3;
      }
   }

   int exp(int var1) {
      return this.expTable[var1];
   }

   ModulusPoly getOne() {
      return this.one;
   }

   int getSize() {
      return this.modulus;
   }

   ModulusPoly getZero() {
      return this.zero;
   }

   int inverse(int var1) {
      if (var1 == 0) {
         throw new ArithmeticException();
      } else {
         return this.expTable[this.modulus - this.logTable[var1] - 1];
      }
   }

   int log(int var1) {
      if (var1 == 0) {
         throw new IllegalArgumentException();
      } else {
         return this.logTable[var1];
      }
   }

   int multiply(int var1, int var2) {
      if (var1 != 0 && var2 != 0) {
         var1 = this.expTable[(this.logTable[var1] + this.logTable[var2]) % (this.modulus - 1)];
      } else {
         var1 = 0;
      }

      return var1;
   }

   int subtract(int var1, int var2) {
      return (this.modulus + var1 - var2) % this.modulus;
   }
}
