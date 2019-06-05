package com.google.zxing.common;

import java.util.Arrays;

public final class BitMatrix implements Cloneable {
   private final int[] bits;
   private final int height;
   private final int rowSize;
   private final int width;

   public BitMatrix(int var1) {
      this(var1, var1);
   }

   public BitMatrix(int var1, int var2) {
      if (var1 > 0 && var2 > 0) {
         this.width = var1;
         this.height = var2;
         this.rowSize = (var1 + 31) / 32;
         this.bits = new int[this.rowSize * var2];
      } else {
         throw new IllegalArgumentException("Both dimensions must be greater than 0");
      }
   }

   private BitMatrix(int var1, int var2, int var3, int[] var4) {
      this.width = var1;
      this.height = var2;
      this.rowSize = var3;
      this.bits = var4;
   }

   private String buildToString(String var1, String var2, String var3) {
      StringBuilder var4 = new StringBuilder(this.height * (this.width + 1));

      for(int var5 = 0; var5 < this.height; ++var5) {
         for(int var6 = 0; var6 < this.width; ++var6) {
            String var7;
            if (this.get(var6, var5)) {
               var7 = var1;
            } else {
               var7 = var2;
            }

            var4.append(var7);
         }

         var4.append(var3);
      }

      return var4.toString();
   }

   public static BitMatrix parse(String var0, String var1, String var2) {
      if (var0 == null) {
         throw new IllegalArgumentException();
      } else {
         boolean[] var3 = new boolean[var0.length()];
         int var4 = 0;
         int var5 = 0;
         int var6 = -1;
         int var7 = 0;
         int var8 = 0;

         while(true) {
            int var10;
            while(var8 < var0.length()) {
               if (var0.charAt(var8) != '\n' && var0.charAt(var8) != '\r') {
                  if (var0.substring(var8, var1.length() + var8).equals(var1)) {
                     var8 += var1.length();
                     var3[var4] = true;
                     ++var4;
                  } else {
                     if (!var0.substring(var8, var2.length() + var8).equals(var2)) {
                        throw new IllegalArgumentException("illegal character encountered: " + var0.substring(var8));
                     }

                     var8 += var2.length();
                     var3[var4] = false;
                     ++var4;
                  }
               } else {
                  int var9 = var7;
                  var10 = var6;
                  int var11 = var5;
                  if (var4 > var5) {
                     if (var6 == -1) {
                        var10 = var4 - var5;
                     } else {
                        var10 = var6;
                        if (var4 - var5 != var6) {
                           throw new IllegalArgumentException("row lengths do not match");
                        }
                     }

                     var11 = var4;
                     var9 = var7 + 1;
                  }

                  ++var8;
                  var7 = var9;
                  var6 = var10;
                  var5 = var11;
               }
            }

            var10 = var7;
            var8 = var6;
            if (var4 > var5) {
               if (var6 == -1) {
                  var8 = var4 - var5;
               } else {
                  var8 = var6;
                  if (var4 - var5 != var6) {
                     throw new IllegalArgumentException("row lengths do not match");
                  }
               }

               var10 = var7 + 1;
            }

            BitMatrix var12 = new BitMatrix(var8, var10);

            for(var6 = 0; var6 < var4; ++var6) {
               if (var3[var6]) {
                  var12.set(var6 % var8, var6 / var8);
               }
            }

            return var12;
         }
      }
   }

   public void clear() {
      int var1 = this.bits.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         this.bits[var2] = 0;
      }

   }

   public BitMatrix clone() {
      return new BitMatrix(this.width, this.height, this.rowSize, (int[])this.bits.clone());
   }

   public boolean equals(Object var1) {
      boolean var2 = false;
      boolean var3;
      if (!(var1 instanceof BitMatrix)) {
         var3 = var2;
      } else {
         BitMatrix var4 = (BitMatrix)var1;
         var3 = var2;
         if (this.width == var4.width) {
            var3 = var2;
            if (this.height == var4.height) {
               var3 = var2;
               if (this.rowSize == var4.rowSize) {
                  var3 = var2;
                  if (Arrays.equals(this.bits, var4.bits)) {
                     var3 = true;
                  }
               }
            }
         }
      }

      return var3;
   }

   public void flip(int var1, int var2) {
      var2 = this.rowSize * var2 + var1 / 32;
      int[] var3 = this.bits;
      var3[var2] ^= 1 << (var1 & 31);
   }

   public boolean get(int var1, int var2) {
      int var3 = this.rowSize;
      int var4 = var1 / 32;
      boolean var5;
      if ((this.bits[var3 * var2 + var4] >>> (var1 & 31) & 1) != 0) {
         var5 = true;
      } else {
         var5 = false;
      }

      return var5;
   }

   public int[] getBottomRightOnBit() {
      int var1;
      for(var1 = this.bits.length - 1; var1 >= 0 && this.bits[var1] == 0; --var1) {
      }

      int[] var2;
      if (var1 < 0) {
         var2 = null;
      } else {
         int var3 = var1 / this.rowSize;
         int var4 = this.rowSize;
         int var5 = this.bits[var1];

         int var6;
         for(var6 = 31; var5 >>> var6 == 0; --var6) {
         }

         var2 = new int[]{(var1 % var4 << 5) + var6, var3};
      }

      return var2;
   }

   public int[] getEnclosingRectangle() {
      int var1 = this.width;
      int var2 = this.height;
      int var3 = -1;
      int var4 = -1;

      int var11;
      for(int var5 = 0; var5 < this.height; ++var5) {
         for(int var6 = 0; var6 < this.rowSize; var2 = var11) {
            int var7 = this.bits[this.rowSize * var5 + var6];
            int var8 = var4;
            int var9 = var1;
            int var10 = var3;
            var11 = var2;
            if (var7 != 0) {
               int var12 = var2;
               if (var5 < var2) {
                  var12 = var5;
               }

               var2 = var4;
               if (var5 > var4) {
                  var2 = var5;
               }

               var4 = var1;
               if (var6 << 5 < var1) {
                  for(var11 = 0; var7 << 31 - var11 == 0; ++var11) {
                  }

                  var4 = var1;
                  if ((var6 << 5) + var11 < var1) {
                     var4 = (var6 << 5) + var11;
                  }
               }

               var8 = var2;
               var9 = var4;
               var10 = var3;
               var11 = var12;
               if ((var6 << 5) + 31 > var3) {
                  for(var1 = 31; var7 >>> var1 == 0; --var1) {
                  }

                  var8 = var2;
                  var9 = var4;
                  var10 = var3;
                  var11 = var12;
                  if ((var6 << 5) + var1 > var3) {
                     var10 = (var6 << 5) + var1;
                     var11 = var12;
                     var9 = var4;
                     var8 = var2;
                  }
               }
            }

            ++var6;
            var4 = var8;
            var1 = var9;
            var3 = var10;
         }
      }

      int[] var13;
      if (var3 >= var1 && var4 >= var2) {
         var13 = new int[]{var1, var2, var3 - var1 + 1, var4 - var2 + 1};
      } else {
         var13 = null;
      }

      return var13;
   }

   public int getHeight() {
      return this.height;
   }

   public BitArray getRow(int var1, BitArray var2) {
      if (var2 != null && var2.getSize() >= this.width) {
         var2.clear();
      } else {
         var2 = new BitArray(this.width);
      }

      int var3 = this.rowSize;

      for(int var4 = 0; var4 < this.rowSize; ++var4) {
         var2.setBulk(var4 << 5, this.bits[var1 * var3 + var4]);
      }

      return var2;
   }

   public int getRowSize() {
      return this.rowSize;
   }

   public int[] getTopLeftOnBit() {
      int var1;
      for(var1 = 0; var1 < this.bits.length && this.bits[var1] == 0; ++var1) {
      }

      int[] var2;
      if (var1 == this.bits.length) {
         var2 = null;
      } else {
         int var3 = var1 / this.rowSize;
         int var4 = this.rowSize;
         int var5 = this.bits[var1];

         int var6;
         for(var6 = 0; var5 << 31 - var6 == 0; ++var6) {
         }

         var2 = new int[]{(var1 % var4 << 5) + var6, var3};
      }

      return var2;
   }

   public int getWidth() {
      return this.width;
   }

   public int hashCode() {
      return (((this.width * 31 + this.width) * 31 + this.height) * 31 + this.rowSize) * 31 + Arrays.hashCode(this.bits);
   }

   public void rotate180() {
      int var1 = this.getWidth();
      int var2 = this.getHeight();
      BitArray var3 = new BitArray(var1);
      BitArray var4 = new BitArray(var1);

      for(var1 = 0; var1 < (var2 + 1) / 2; ++var1) {
         var3 = this.getRow(var1, var3);
         var4 = this.getRow(var2 - 1 - var1, var4);
         var3.reverse();
         var4.reverse();
         this.setRow(var1, var4);
         this.setRow(var2 - 1 - var1, var3);
      }

   }

   public void set(int var1, int var2) {
      var2 = this.rowSize * var2 + var1 / 32;
      int[] var3 = this.bits;
      var3[var2] |= 1 << (var1 & 31);
   }

   public void setRegion(int var1, int var2, int var3, int var4) {
      if (var2 >= 0 && var1 >= 0) {
         if (var4 > 0 && var3 > 0) {
            int var5 = var1 + var3;
            var4 += var2;
            if (var4 <= this.height && var5 <= this.width) {
               while(var2 < var4) {
                  int var6 = this.rowSize;

                  for(var3 = var1; var3 < var5; ++var3) {
                     int[] var7 = this.bits;
                     int var8 = var3 / 32 + var2 * var6;
                     var7[var8] |= 1 << (var3 & 31);
                  }

                  ++var2;
               }

            } else {
               throw new IllegalArgumentException("The region must fit inside the matrix");
            }
         } else {
            throw new IllegalArgumentException("Height and width must be at least 1");
         }
      } else {
         throw new IllegalArgumentException("Left and top must be nonnegative");
      }
   }

   public void setRow(int var1, BitArray var2) {
      System.arraycopy(var2.getBitArray(), 0, this.bits, this.rowSize * var1, this.rowSize);
   }

   public String toString() {
      return this.toString("X ", "  ");
   }

   public String toString(String var1, String var2) {
      return this.buildToString(var1, var2, "\n");
   }

   @Deprecated
   public String toString(String var1, String var2, String var3) {
      return this.buildToString(var1, var2, var3);
   }

   public void unset(int var1, int var2) {
      var2 = this.rowSize * var2 + var1 / 32;
      int[] var3 = this.bits;
      var3[var2] &= ~(1 << (var1 & 31));
   }

   public void xor(BitMatrix var1) {
      if (this.width == var1.getWidth() && this.height == var1.getHeight() && this.rowSize == var1.getRowSize()) {
         BitArray var2 = new BitArray(this.width / 32 + 1);

         for(int var3 = 0; var3 < this.height; ++var3) {
            int var4 = this.rowSize;
            int[] var5 = var1.getRow(var3, var2).getBitArray();

            for(int var6 = 0; var6 < this.rowSize; ++var6) {
               int[] var7 = this.bits;
               int var8 = var3 * var4 + var6;
               var7[var8] ^= var5[var6];
            }
         }

      } else {
         throw new IllegalArgumentException("input matrix dimensions do not match");
      }
   }
}
