package com.google.android.exoplayer2.util;

public final class ParsableNalUnitBitArray {
   private int bitOffset;
   private int byteLimit;
   private int byteOffset;
   private byte[] data;

   public ParsableNalUnitBitArray(byte[] var1, int var2, int var3) {
      this.reset(var1, var2, var3);
   }

   private void assertValidOffset() {
      boolean var3;
      label18: {
         int var1 = this.byteOffset;
         if (var1 >= 0) {
            int var2 = this.byteLimit;
            if (var1 < var2 || var1 == var2 && this.bitOffset == 0) {
               var3 = true;
               break label18;
            }
         }

         var3 = false;
      }

      Assertions.checkState(var3);
   }

   private int readExpGolombCodeNum() {
      int var1 = 0;

      int var2;
      for(var2 = 0; !this.readBit(); ++var2) {
      }

      if (var2 > 0) {
         var1 = this.readBits(var2);
      }

      return (1 << var2) - 1 + var1;
   }

   private boolean shouldSkipByte(int var1) {
      boolean var2 = true;
      if (2 <= var1 && var1 < this.byteLimit) {
         byte[] var3 = this.data;
         if (var3[var1] == 3 && var3[var1 - 2] == 0 && var3[var1 - 1] == 0) {
            return var2;
         }
      }

      var2 = false;
      return var2;
   }

   public boolean canReadBits(int var1) {
      int var2 = this.byteOffset;
      int var3 = var1 / 8;
      int var4 = var2 + var3;
      int var5 = this.bitOffset + var1 - var3 * 8;
      var1 = var4;
      var3 = var5;
      if (var5 > 7) {
         var1 = var4 + 1;
         var3 = var5 - 8;
      }

      boolean var6 = true;
      var4 = var1;
      var1 = var2;

      while(true) {
         var2 = var1 + 1;
         if (var2 > var4 || var4 >= this.byteLimit) {
            var1 = this.byteLimit;
            boolean var7 = var6;
            if (var4 >= var1) {
               if (var4 == var1 && var3 == 0) {
                  var7 = var6;
               } else {
                  var7 = false;
               }
            }

            return var7;
         }

         var1 = var2;
         if (this.shouldSkipByte(var2)) {
            ++var4;
            var1 = var2 + 2;
         }
      }
   }

   public boolean canReadExpGolombCodedNum() {
      int var1 = this.byteOffset;
      int var2 = this.bitOffset;
      boolean var3 = false;

      int var4;
      for(var4 = 0; this.byteOffset < this.byteLimit && !this.readBit(); ++var4) {
      }

      boolean var5;
      if (this.byteOffset == this.byteLimit) {
         var5 = true;
      } else {
         var5 = false;
      }

      this.byteOffset = var1;
      this.bitOffset = var2;
      boolean var6 = var3;
      if (!var5) {
         var6 = var3;
         if (this.canReadBits(var4 * 2 + 1)) {
            var6 = true;
         }
      }

      return var6;
   }

   public boolean readBit() {
      boolean var1;
      if ((this.data[this.byteOffset] & 128 >> this.bitOffset) != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.skipBit();
      return var1;
   }

   public int readBits(int var1) {
      this.bitOffset += var1;
      int var2 = 0;

      while(true) {
         int var3 = this.bitOffset;
         byte var4 = 2;
         byte[] var5;
         int var6;
         if (var3 <= 8) {
            var5 = this.data;
            var6 = this.byteOffset;
            byte var7 = var5[var6];
            if (var3 == 8) {
               this.bitOffset = 0;
               if (!this.shouldSkipByte(var6 + 1)) {
                  var4 = 1;
               }

               this.byteOffset = var6 + var4;
            }

            this.assertValidOffset();
            return -1 >>> 32 - var1 & (var2 | (var7 & 255) >> 8 - var3);
         }

         this.bitOffset = var3 - 8;
         var5 = this.data;
         var6 = this.byteOffset;
         var2 |= (var5[var6] & 255) << this.bitOffset;
         if (!this.shouldSkipByte(var6 + 1)) {
            var4 = 1;
         }

         this.byteOffset = var6 + var4;
      }
   }

   public int readSignedExpGolombCodedInt() {
      int var1 = this.readExpGolombCodeNum();
      byte var2;
      if (var1 % 2 == 0) {
         var2 = -1;
      } else {
         var2 = 1;
      }

      return var2 * ((var1 + 1) / 2);
   }

   public int readUnsignedExpGolombCodedInt() {
      return this.readExpGolombCodeNum();
   }

   public void reset(byte[] var1, int var2, int var3) {
      this.data = var1;
      this.byteOffset = var2;
      this.byteLimit = var3;
      this.bitOffset = 0;
      this.assertValidOffset();
   }

   public void skipBit() {
      int var1 = this.bitOffset;
      byte var2 = 1;
      ++var1;
      this.bitOffset = var1;
      if (var1 == 8) {
         this.bitOffset = 0;
         var1 = this.byteOffset;
         if (this.shouldSkipByte(var1 + 1)) {
            var2 = 2;
         }

         this.byteOffset = var1 + var2;
      }

      this.assertValidOffset();
   }

   public void skipBits(int var1) {
      int var2 = this.byteOffset;
      int var3 = var1 / 8;
      this.byteOffset = var2 + var3;
      this.bitOffset += var1 - var3 * 8;
      var3 = this.bitOffset;
      var1 = var2;
      if (var3 > 7) {
         ++this.byteOffset;
         this.bitOffset = var3 - 8;
         var1 = var2;
      }

      while(true) {
         var2 = var1 + 1;
         if (var2 > this.byteOffset) {
            this.assertValidOffset();
            return;
         }

         var1 = var2;
         if (this.shouldSkipByte(var2)) {
            ++this.byteOffset;
            var1 = var2 + 2;
         }
      }
   }
}
