package com.google.zxing.pdf417.decoder.ec;

final class ModulusPoly {
   private final int[] coefficients;
   private final ModulusGF field;

   ModulusPoly(ModulusGF var1, int[] var2) {
      if (var2.length == 0) {
         throw new IllegalArgumentException();
      } else {
         this.field = var1;
         int var3 = var2.length;
         if (var3 > 1 && var2[0] == 0) {
            int var4;
            for(var4 = 1; var4 < var3 && var2[var4] == 0; ++var4) {
            }

            if (var4 == var3) {
               this.coefficients = new int[]{0};
            } else {
               this.coefficients = new int[var3 - var4];
               System.arraycopy(var2, var4, this.coefficients, 0, this.coefficients.length);
            }
         } else {
            this.coefficients = var2;
         }

      }
   }

   ModulusPoly add(ModulusPoly var1) {
      if (!this.field.equals(var1.field)) {
         throw new IllegalArgumentException("ModulusPolys do not have same ModulusGF field");
      } else {
         if (!this.isZero()) {
            if (var1.isZero()) {
               var1 = this;
            } else {
               int[] var2 = this.coefficients;
               int[] var3 = var1.coefficients;
               int[] var4 = var3;
               int[] var7 = var2;
               if (var2.length > var3.length) {
                  var7 = var3;
                  var4 = var2;
               }

               var2 = new int[var4.length];
               int var5 = var4.length - var7.length;
               System.arraycopy(var4, 0, var2, 0, var5);

               for(int var6 = var5; var6 < var4.length; ++var6) {
                  var2[var6] = this.field.add(var7[var6 - var5], var4[var6]);
               }

               var1 = new ModulusPoly(this.field, var2);
            }
         }

         return var1;
      }
   }

   int evaluateAt(int var1) {
      int var2 = 0;
      int var3;
      if (var1 == 0) {
         var3 = this.getCoefficient(0);
      } else {
         int var5;
         if (var1 == 1) {
            var1 = 0;
            int[] var4 = this.coefficients;
            var5 = var4.length;

            while(true) {
               var3 = var1;
               if (var2 >= var5) {
                  break;
               }

               var3 = var4[var2];
               var1 = this.field.add(var1, var3);
               ++var2;
            }
         } else {
            var2 = this.coefficients[0];
            int var6 = this.coefficients.length;
            var5 = 1;

            while(true) {
               var3 = var2;
               if (var5 >= var6) {
                  break;
               }

               var2 = this.field.add(this.field.multiply(var1, var2), this.coefficients[var5]);
               ++var5;
            }
         }
      }

      return var3;
   }

   int getCoefficient(int var1) {
      return this.coefficients[this.coefficients.length - 1 - var1];
   }

   int[] getCoefficients() {
      return this.coefficients;
   }

   int getDegree() {
      return this.coefficients.length - 1;
   }

   boolean isZero() {
      boolean var1 = false;
      if (this.coefficients[0] == 0) {
         var1 = true;
      }

      return var1;
   }

   ModulusPoly multiply(int var1) {
      ModulusPoly var2;
      if (var1 == 0) {
         var2 = this.field.getZero();
      } else {
         var2 = this;
         if (var1 != 1) {
            int var3 = this.coefficients.length;
            int[] var5 = new int[var3];

            for(int var4 = 0; var4 < var3; ++var4) {
               var5[var4] = this.field.multiply(this.coefficients[var4], var1);
            }

            var2 = new ModulusPoly(this.field, var5);
         }
      }

      return var2;
   }

   ModulusPoly multiply(ModulusPoly var1) {
      if (!this.field.equals(var1.field)) {
         throw new IllegalArgumentException("ModulusPolys do not have same ModulusGF field");
      } else {
         if (!this.isZero() && !var1.isZero()) {
            int[] var2 = this.coefficients;
            int var3 = var2.length;
            int[] var4 = var1.coefficients;
            int var5 = var4.length;
            int[] var9 = new int[var3 + var5 - 1];

            for(int var6 = 0; var6 < var3; ++var6) {
               int var7 = var2[var6];

               for(int var8 = 0; var8 < var5; ++var8) {
                  var9[var6 + var8] = this.field.add(var9[var6 + var8], this.field.multiply(var7, var4[var8]));
               }
            }

            var1 = new ModulusPoly(this.field, var9);
         } else {
            var1 = this.field.getZero();
         }

         return var1;
      }
   }

   ModulusPoly multiplyByMonomial(int var1, int var2) {
      if (var1 < 0) {
         throw new IllegalArgumentException();
      } else {
         ModulusPoly var3;
         if (var2 == 0) {
            var3 = this.field.getZero();
         } else {
            int var4 = this.coefficients.length;
            int[] var5 = new int[var4 + var1];

            for(var1 = 0; var1 < var4; ++var1) {
               var5[var1] = this.field.multiply(this.coefficients[var1], var2);
            }

            var3 = new ModulusPoly(this.field, var5);
         }

         return var3;
      }
   }

   ModulusPoly negative() {
      int var1 = this.coefficients.length;
      int[] var2 = new int[var1];

      for(int var3 = 0; var3 < var1; ++var3) {
         var2[var3] = this.field.subtract(0, this.coefficients[var3]);
      }

      return new ModulusPoly(this.field, var2);
   }

   ModulusPoly subtract(ModulusPoly var1) {
      if (!this.field.equals(var1.field)) {
         throw new IllegalArgumentException("ModulusPolys do not have same ModulusGF field");
      } else {
         if (var1.isZero()) {
            var1 = this;
         } else {
            var1 = this.add(var1.negative());
         }

         return var1;
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(this.getDegree() * 8);

      for(int var2 = this.getDegree(); var2 >= 0; --var2) {
         int var3 = this.getCoefficient(var2);
         if (var3 != 0) {
            int var4;
            if (var3 < 0) {
               var1.append(" - ");
               var4 = -var3;
            } else {
               var4 = var3;
               if (var1.length() > 0) {
                  var1.append(" + ");
                  var4 = var3;
               }
            }

            if (var2 == 0 || var4 != 1) {
               var1.append(var4);
            }

            if (var2 != 0) {
               if (var2 == 1) {
                  var1.append('x');
               } else {
                  var1.append("x^");
                  var1.append(var2);
               }
            }
         }
      }

      return var1.toString();
   }
}
