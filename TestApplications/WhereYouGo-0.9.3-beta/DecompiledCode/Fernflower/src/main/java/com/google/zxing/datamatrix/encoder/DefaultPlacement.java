package com.google.zxing.datamatrix.encoder;

import java.util.Arrays;

public class DefaultPlacement {
   private final byte[] bits;
   private final CharSequence codewords;
   private final int numcols;
   private final int numrows;

   public DefaultPlacement(CharSequence var1, int var2, int var3) {
      this.codewords = var1;
      this.numcols = var2;
      this.numrows = var3;
      this.bits = new byte[var2 * var3];
      Arrays.fill(this.bits, (byte)-1);
   }

   private void corner1(int var1) {
      this.module(this.numrows - 1, 0, var1, 1);
      this.module(this.numrows - 1, 1, var1, 2);
      this.module(this.numrows - 1, 2, var1, 3);
      this.module(0, this.numcols - 2, var1, 4);
      this.module(0, this.numcols - 1, var1, 5);
      this.module(1, this.numcols - 1, var1, 6);
      this.module(2, this.numcols - 1, var1, 7);
      this.module(3, this.numcols - 1, var1, 8);
   }

   private void corner2(int var1) {
      this.module(this.numrows - 3, 0, var1, 1);
      this.module(this.numrows - 2, 0, var1, 2);
      this.module(this.numrows - 1, 0, var1, 3);
      this.module(0, this.numcols - 4, var1, 4);
      this.module(0, this.numcols - 3, var1, 5);
      this.module(0, this.numcols - 2, var1, 6);
      this.module(0, this.numcols - 1, var1, 7);
      this.module(1, this.numcols - 1, var1, 8);
   }

   private void corner3(int var1) {
      this.module(this.numrows - 3, 0, var1, 1);
      this.module(this.numrows - 2, 0, var1, 2);
      this.module(this.numrows - 1, 0, var1, 3);
      this.module(0, this.numcols - 2, var1, 4);
      this.module(0, this.numcols - 1, var1, 5);
      this.module(1, this.numcols - 1, var1, 6);
      this.module(2, this.numcols - 1, var1, 7);
      this.module(3, this.numcols - 1, var1, 8);
   }

   private void corner4(int var1) {
      this.module(this.numrows - 1, 0, var1, 1);
      this.module(this.numrows - 1, this.numcols - 1, var1, 2);
      this.module(0, this.numcols - 3, var1, 3);
      this.module(0, this.numcols - 2, var1, 4);
      this.module(0, this.numcols - 1, var1, 5);
      this.module(1, this.numcols - 3, var1, 6);
      this.module(1, this.numcols - 2, var1, 7);
      this.module(1, this.numcols - 1, var1, 8);
   }

   private boolean hasBit(int var1, int var2) {
      boolean var3;
      if (this.bits[this.numcols * var2 + var1] >= 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   private void module(int var1, int var2, int var3, int var4) {
      boolean var5 = true;
      int var6 = var1;
      int var7 = var2;
      if (var1 < 0) {
         var6 = var1 + this.numrows;
         var7 = var2 + (4 - (this.numrows + 4) % 8);
      }

      var2 = var6;
      var1 = var7;
      if (var7 < 0) {
         var1 = var7 + this.numcols;
         var2 = var6 + (4 - (this.numcols + 4) % 8);
      }

      if ((this.codewords.charAt(var3) & 1 << 8 - var4) == 0) {
         var5 = false;
      }

      this.setBit(var1, var2, var5);
   }

   private void setBit(int var1, int var2, boolean var3) {
      byte[] var4 = this.bits;
      int var5 = this.numcols;
      byte var6;
      if (var3) {
         var6 = 1;
      } else {
         var6 = 0;
      }

      var4[var5 * var2 + var1] = (byte)((byte)var6);
   }

   private void utah(int var1, int var2, int var3) {
      this.module(var1 - 2, var2 - 2, var3, 1);
      this.module(var1 - 2, var2 - 1, var3, 2);
      this.module(var1 - 1, var2 - 2, var3, 3);
      this.module(var1 - 1, var2 - 1, var3, 4);
      this.module(var1 - 1, var2, var3, 5);
      this.module(var1, var2 - 2, var3, 6);
      this.module(var1, var2 - 1, var3, 7);
      this.module(var1, var2, var3, 8);
   }

   public final boolean getBit(int var1, int var2) {
      boolean var3 = true;
      if (this.bits[this.numcols * var2 + var1] != 1) {
         var3 = false;
      }

      return var3;
   }

   final byte[] getBits() {
      return this.bits;
   }

   final int getNumcols() {
      return this.numcols;
   }

   final int getNumrows() {
      return this.numrows;
   }

   public final void place() {
      int var1 = 0;
      int var2 = 4;
      int var3 = 0;

      while(true) {
         int var4 = var1;
         if (var2 == this.numrows) {
            var4 = var1;
            if (var3 == 0) {
               this.corner1(var1);
               var4 = var1 + 1;
            }
         }

         int var5 = var4;
         if (var2 == this.numrows - 2) {
            var5 = var4;
            if (var3 == 0) {
               var5 = var4;
               if (this.numcols % 4 != 0) {
                  this.corner2(var4);
                  var5 = var4 + 1;
               }
            }
         }

         var1 = var5;
         if (var2 == this.numrows - 2) {
            var1 = var5;
            if (var3 == 0) {
               var1 = var5;
               if (this.numcols % 8 == 4) {
                  this.corner3(var5);
                  var1 = var5 + 1;
               }
            }
         }

         int var6 = var3;
         var5 = var1;
         var4 = var2;
         if (var2 == this.numrows + 4) {
            var6 = var3;
            var5 = var1;
            var4 = var2;
            if (var3 == 2) {
               var6 = var3;
               var5 = var1;
               var4 = var2;
               if (this.numcols % 8 == 0) {
                  this.corner4(var1);
                  var5 = var1 + 1;
                  var4 = var2;
                  var6 = var3;
               }
            }
         }

         do {
            var2 = var5;
            if (var4 < this.numrows) {
               var2 = var5;
               if (var6 >= 0) {
                  var2 = var5;
                  if (!this.hasBit(var6, var4)) {
                     this.utah(var4, var6, var5);
                     var2 = var5 + 1;
                  }
               }
            }

            var1 = var4 - 2;
            var3 = var6 + 2;
            if (var1 < 0) {
               break;
            }

            var6 = var3;
            var5 = var2;
            var4 = var1;
         } while(var3 < this.numcols);

         var4 = var1 + 1;
         var1 = var3 + 3;
         var5 = var2;
         var3 = var4;
         var2 = var1;

         do {
            if (var3 >= 0 && var2 < this.numcols && !this.hasBit(var2, var3)) {
               var1 = var5 + 1;
               this.utah(var3, var2, var5);
               var5 = var1;
            }

            var3 += 2;
            var2 -= 2;
         } while(var3 < this.numrows && var2 >= 0);

         var4 = var3 + 3;
         var6 = var2 + 1;
         var3 = var6;
         var1 = var5;
         var2 = var4;
         if (var4 >= this.numrows) {
            var3 = var6;
            var1 = var5;
            var2 = var4;
            if (var6 >= this.numcols) {
               if (!this.hasBit(this.numcols - 1, this.numrows - 1)) {
                  this.setBit(this.numcols - 1, this.numrows - 1, true);
                  this.setBit(this.numcols - 2, this.numrows - 2, true);
               }

               return;
            }
         }
      }
   }
}
