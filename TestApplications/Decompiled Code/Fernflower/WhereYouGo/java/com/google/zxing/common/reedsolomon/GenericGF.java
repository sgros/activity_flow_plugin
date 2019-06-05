package com.google.zxing.common.reedsolomon;

public final class GenericGF {
   public static final GenericGF AZTEC_DATA_10 = new GenericGF(1033, 1024, 1);
   public static final GenericGF AZTEC_DATA_12 = new GenericGF(4201, 4096, 1);
   public static final GenericGF AZTEC_DATA_6 = new GenericGF(67, 64, 1);
   public static final GenericGF AZTEC_DATA_8;
   public static final GenericGF AZTEC_PARAM = new GenericGF(19, 16, 1);
   public static final GenericGF DATA_MATRIX_FIELD_256;
   public static final GenericGF MAXICODE_FIELD_64;
   public static final GenericGF QR_CODE_FIELD_256 = new GenericGF(285, 256, 0);
   private final int[] expTable;
   private final int generatorBase;
   private final int[] logTable;
   private final GenericGFPoly one;
   private final int primitive;
   private final int size;
   private final GenericGFPoly zero;

   static {
      GenericGF var0 = new GenericGF(301, 256, 1);
      DATA_MATRIX_FIELD_256 = var0;
      AZTEC_DATA_8 = var0;
      MAXICODE_FIELD_64 = AZTEC_DATA_6;
   }

   public GenericGF(int var1, int var2, int var3) {
      this.primitive = var1;
      this.size = var2;
      this.generatorBase = var3;
      this.expTable = new int[var2];
      this.logTable = new int[var2];
      var3 = 1;

      for(int var4 = 0; var4 < var2; ++var4) {
         this.expTable[var4] = var3;
         int var5 = var3 << 1;
         var3 = var5;
         if (var5 >= var2) {
            var3 = (var5 ^ var1) & var2 - 1;
         }
      }

      for(var1 = 0; var1 < var2 - 1; this.logTable[this.expTable[var1]] = var1++) {
      }

      this.zero = new GenericGFPoly(this, new int[]{0});
      this.one = new GenericGFPoly(this, new int[]{1});
   }

   static int addOrSubtract(int var0, int var1) {
      return var0 ^ var1;
   }

   GenericGFPoly buildMonomial(int var1, int var2) {
      if (var1 < 0) {
         throw new IllegalArgumentException();
      } else {
         GenericGFPoly var3;
         if (var2 == 0) {
            var3 = this.zero;
         } else {
            int[] var4 = new int[var1 + 1];
            var4[0] = var2;
            var3 = new GenericGFPoly(this, var4);
         }

         return var3;
      }
   }

   int exp(int var1) {
      return this.expTable[var1];
   }

   public int getGeneratorBase() {
      return this.generatorBase;
   }

   GenericGFPoly getOne() {
      return this.one;
   }

   public int getSize() {
      return this.size;
   }

   GenericGFPoly getZero() {
      return this.zero;
   }

   int inverse(int var1) {
      if (var1 == 0) {
         throw new ArithmeticException();
      } else {
         return this.expTable[this.size - this.logTable[var1] - 1];
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
         var1 = this.expTable[(this.logTable[var1] + this.logTable[var2]) % (this.size - 1)];
      } else {
         var1 = 0;
      }

      return var1;
   }

   public String toString() {
      return "GF(0x" + Integer.toHexString(this.primitive) + ',' + this.size + ')';
   }
}
