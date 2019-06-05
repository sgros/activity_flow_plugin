package com.google.zxing.common;

import java.util.Arrays;

public final class BitArray implements Cloneable {
   private int[] bits;
   private int size;

   public BitArray() {
      this.size = 0;
      this.bits = new int[1];
   }

   public BitArray(int var1) {
      this.size = var1;
      this.bits = makeArray(var1);
   }

   BitArray(int[] var1, int var2) {
      this.bits = var1;
      this.size = var2;
   }

   private void ensureCapacity(int var1) {
      if (var1 > this.bits.length << 5) {
         int[] var2 = makeArray(var1);
         System.arraycopy(this.bits, 0, var2, 0, this.bits.length);
         this.bits = var2;
      }

   }

   private static int[] makeArray(int var0) {
      return new int[(var0 + 31) / 32];
   }

   public void appendBit(boolean var1) {
      this.ensureCapacity(this.size + 1);
      if (var1) {
         int[] var2 = this.bits;
         int var3 = this.size / 32;
         var2[var3] |= 1 << (this.size & 31);
      }

      ++this.size;
   }

   public void appendBitArray(BitArray var1) {
      int var2 = var1.size;
      this.ensureCapacity(this.size + var2);

      for(int var3 = 0; var3 < var2; ++var3) {
         this.appendBit(var1.get(var3));
      }

   }

   public void appendBits(int var1, int var2) {
      if (var2 >= 0 && var2 <= 32) {
         this.ensureCapacity(this.size + var2);

         while(var2 > 0) {
            boolean var3;
            if ((var1 >> var2 - 1 & 1) == 1) {
               var3 = true;
            } else {
               var3 = false;
            }

            this.appendBit(var3);
            --var2;
         }

      } else {
         throw new IllegalArgumentException("Num bits must be between 0 and 32");
      }
   }

   public void clear() {
      int var1 = this.bits.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         this.bits[var2] = 0;
      }

   }

   public BitArray clone() {
      return new BitArray((int[])this.bits.clone(), this.size);
   }

   public boolean equals(Object var1) {
      boolean var2 = false;
      boolean var3;
      if (!(var1 instanceof BitArray)) {
         var3 = var2;
      } else {
         BitArray var4 = (BitArray)var1;
         var3 = var2;
         if (this.size == var4.size) {
            var3 = var2;
            if (Arrays.equals(this.bits, var4.bits)) {
               var3 = true;
            }
         }
      }

      return var3;
   }

   public void flip(int var1) {
      int[] var2 = this.bits;
      int var3 = var1 / 32;
      var2[var3] ^= 1 << (var1 & 31);
   }

   public boolean get(int var1) {
      boolean var2 = true;
      if ((this.bits[var1 / 32] & 1 << (var1 & 31)) == 0) {
         var2 = false;
      }

      return var2;
   }

   public int[] getBitArray() {
      return this.bits;
   }

   public int getNextSet(int var1) {
      if (var1 >= this.size) {
         var1 = this.size;
      } else {
         int var2 = var1 / 32;
         var1 = this.bits[var2] & ~((1 << (var1 & 31)) - 1);

         while(true) {
            if (var1 != 0) {
               var2 = (var2 << 5) + Integer.numberOfTrailingZeros(var1);
               var1 = var2;
               if (var2 > this.size) {
                  var1 = this.size;
               }
               break;
            }

            ++var2;
            if (var2 == this.bits.length) {
               var1 = this.size;
               break;
            }

            var1 = this.bits[var2];
         }
      }

      return var1;
   }

   public int getNextUnset(int var1) {
      if (var1 >= this.size) {
         var1 = this.size;
      } else {
         int var2 = var1 / 32;
         var1 = ~this.bits[var2] & ~((1 << (var1 & 31)) - 1);

         while(true) {
            if (var1 != 0) {
               var2 = (var2 << 5) + Integer.numberOfTrailingZeros(var1);
               var1 = var2;
               if (var2 > this.size) {
                  var1 = this.size;
               }
               break;
            }

            ++var2;
            if (var2 == this.bits.length) {
               var1 = this.size;
               break;
            }

            var1 = ~this.bits[var2];
         }
      }

      return var1;
   }

   public int getSize() {
      return this.size;
   }

   public int getSizeInBytes() {
      return (this.size + 7) / 8;
   }

   public int hashCode() {
      return this.size * 31 + Arrays.hashCode(this.bits);
   }

   public boolean isRange(int var1, int var2, boolean var3) {
      boolean var4 = true;
      if (var2 >= var1 && var1 >= 0 && var2 <= this.size) {
         boolean var5;
         if (var2 == var1) {
            var5 = var4;
         } else {
            int var6 = var2 - 1;
            int var7 = var1 / 32;
            int var8 = var6 / 32;
            int var9 = var7;

            while(true) {
               var5 = var4;
               if (var9 > var8) {
                  break;
               }

               if (var9 > var7) {
                  var2 = 0;
               } else {
                  var2 = var1 & 31;
               }

               int var10;
               if (var9 < var8) {
                  var10 = 31;
               } else {
                  var10 = var6 & 31;
               }

               var10 = (2 << var10) - (1 << var2);
               int var11 = this.bits[var9];
               if (var3) {
                  var2 = var10;
               } else {
                  var2 = 0;
               }

               if ((var11 & var10) != var2) {
                  var5 = false;
                  break;
               }

               ++var9;
            }
         }

         return var5;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public void reverse() {
      int[] var1 = new int[this.bits.length];
      int var2 = (this.size - 1) / 32;
      int var3 = var2 + 1;

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         long var5 = (long)this.bits[var4];
         var5 = var5 >> 1 & 1431655765L | (1431655765L & var5) << 1;
         var5 = var5 >> 2 & 858993459L | (858993459L & var5) << 2;
         var5 = var5 >> 4 & 252645135L | (252645135L & var5) << 4;
         var5 = var5 >> 8 & 16711935L | (16711935L & var5) << 8;
         var1[var2 - var4] = (int)(var5 >> 16 & 65535L | (65535L & var5) << 16);
      }

      if (this.size != var3 << 5) {
         int var7 = (var3 << 5) - this.size;
         var2 = var1[0] >>> var7;

         for(var4 = 1; var4 < var3; ++var4) {
            int var8 = var1[var4];
            var1[var4 - 1] = var2 | var8 << 32 - var7;
            var2 = var8 >>> var7;
         }

         var1[var3 - 1] = var2;
      }

      this.bits = var1;
   }

   public void set(int var1) {
      int[] var2 = this.bits;
      int var3 = var1 / 32;
      var2[var3] |= 1 << (var1 & 31);
   }

   public void setBulk(int var1, int var2) {
      this.bits[var1 / 32] = var2;
   }

   public void setRange(int var1, int var2) {
      if (var2 >= var1 && var1 >= 0 && var2 <= this.size) {
         if (var2 != var1) {
            int var3 = var2 - 1;
            int var4 = var1 / 32;
            int var5 = var3 / 32;

            for(var2 = var4; var2 <= var5; ++var2) {
               int var6;
               if (var2 > var4) {
                  var6 = 0;
               } else {
                  var6 = var1 & 31;
               }

               int var7;
               if (var2 < var5) {
                  var7 = 31;
               } else {
                  var7 = var3 & 31;
               }

               int[] var8 = this.bits;
               var8[var2] |= (2 << var7) - (1 << var6);
            }
         }

      } else {
         throw new IllegalArgumentException();
      }
   }

   public void toBytes(int var1, byte[] var2, int var3, int var4) {
      byte var5 = 0;
      int var6 = var1;

      for(var1 = var5; var1 < var4; ++var1) {
         int var7 = 0;

         int var8;
         for(int var9 = 0; var9 < 8; var7 = var8) {
            var8 = var7;
            if (this.get(var6)) {
               var8 = var7 | 1 << 7 - var9;
            }

            ++var6;
            ++var9;
         }

         var2[var3 + var1] = (byte)((byte)var7);
      }

   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(this.size);

      for(int var2 = 0; var2 < this.size; ++var2) {
         if ((var2 & 7) == 0) {
            var1.append(' ');
         }

         byte var3;
         char var4;
         if (this.get(var2)) {
            var3 = 88;
            var4 = (char)var3;
         } else {
            var3 = 46;
            var4 = (char)var3;
         }

         var1.append(var4);
      }

      return var1.toString();
   }

   public void xor(BitArray var1) {
      if (this.size != var1.size) {
         throw new IllegalArgumentException("Sizes don't match");
      } else {
         for(int var2 = 0; var2 < this.bits.length; ++var2) {
            int[] var3 = this.bits;
            var3[var2] ^= var1.bits[var2];
         }

      }
   }
}
