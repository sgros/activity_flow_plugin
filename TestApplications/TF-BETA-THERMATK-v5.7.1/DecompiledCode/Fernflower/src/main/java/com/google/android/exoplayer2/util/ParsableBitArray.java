package com.google.android.exoplayer2.util;

public final class ParsableBitArray {
   private int bitOffset;
   private int byteLimit;
   private int byteOffset;
   public byte[] data;

   public ParsableBitArray() {
      this.data = Util.EMPTY_BYTE_ARRAY;
   }

   public ParsableBitArray(byte[] var1) {
      this(var1, var1.length);
   }

   public ParsableBitArray(byte[] var1, int var2) {
      this.data = var1;
      this.byteLimit = var2;
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

   public int bitsLeft() {
      return (this.byteLimit - this.byteOffset) * 8 - this.bitOffset;
   }

   public void byteAlign() {
      if (this.bitOffset != 0) {
         this.bitOffset = 0;
         ++this.byteOffset;
         this.assertValidOffset();
      }
   }

   public int getBytePosition() {
      boolean var1;
      if (this.bitOffset == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      Assertions.checkState(var1);
      return this.byteOffset;
   }

   public int getPosition() {
      return this.byteOffset * 8 + this.bitOffset;
   }

   public void putInt(int var1, int var2) {
      int var3 = var1;
      if (var2 < 32) {
         var3 = var1 & (1 << var2) - 1;
      }

      int var4 = Math.min(8 - this.bitOffset, var2);
      int var5 = this.bitOffset;
      var1 = 8 - var5 - var4;
      byte[] var6 = this.data;
      int var7 = this.byteOffset;
      var6[var7] = (byte)((byte)(('\uff00' >> var5 | (1 << var1) - 1) & var6[var7]));
      var4 = var2 - var4;
      var6[var7] = (byte)((byte)(var3 >>> var4 << var1 | var6[var7]));

      for(var1 = var7 + 1; var4 > 8; ++var1) {
         this.data[var1] = (byte)((byte)(var3 >>> var4 - 8));
         var4 -= 8;
      }

      var7 = 8 - var4;
      var6 = this.data;
      var6[var1] = (byte)((byte)(var6[var1] & (1 << var7) - 1));
      var6[var1] = (byte)((byte)((var3 & (1 << var4) - 1) << var7 | var6[var1]));
      this.skipBits(var2);
      this.assertValidOffset();
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
      if (var1 == 0) {
         return 0;
      } else {
         this.bitOffset += var1;
         int var2 = 0;

         while(true) {
            int var3 = this.bitOffset;
            byte[] var4;
            if (var3 <= 8) {
               var4 = this.data;
               int var5 = this.byteOffset;
               byte var6 = var4[var5];
               if (var3 == 8) {
                  this.bitOffset = 0;
                  this.byteOffset = var5 + 1;
               }

               this.assertValidOffset();
               return -1 >>> 32 - var1 & (var2 | (var6 & 255) >> 8 - var3);
            }

            this.bitOffset = var3 - 8;
            var4 = this.data;
            var3 = this.byteOffset++;
            var2 |= (var4[var3] & 255) << this.bitOffset;
         }
      }
   }

   public void readBits(byte[] var1, int var2, int var3) {
      int var4;
      byte[] var5;
      int var6;
      for(var4 = (var3 >> 3) + var2; var2 < var4; ++var2) {
         var5 = this.data;
         var6 = this.byteOffset++;
         byte var7 = var5[var6];
         var6 = this.bitOffset;
         var1[var2] = (byte)((byte)(var7 << var6));
         var7 = var1[var2];
         var1[var2] = (byte)((byte)((255 & var5[this.byteOffset]) >> 8 - var6 | var7));
      }

      var2 = var3 & 7;
      if (var2 != 0) {
         var1[var4] = (byte)((byte)(var1[var4] & 255 >> var2));
         int var11 = this.bitOffset;
         byte var9;
         if (var11 + var2 > 8) {
            var9 = var1[var4];
            var5 = this.data;
            var6 = this.byteOffset++;
            var1[var4] = (byte)((byte)(var9 | (var5[var6] & 255) << var11));
            this.bitOffset = var11 - 8;
         }

         this.bitOffset += var2;
         var5 = this.data;
         var11 = this.byteOffset;
         byte var10 = var5[var11];
         int var8 = this.bitOffset;
         var9 = var1[var4];
         var1[var4] = (byte)((byte)((byte)((var10 & 255) >> 8 - var8 << 8 - var2) | var9));
         if (var8 == 8) {
            this.bitOffset = 0;
            this.byteOffset = var11 + 1;
         }

         this.assertValidOffset();
      }
   }

   public void readBytes(byte[] var1, int var2, int var3) {
      boolean var4;
      if (this.bitOffset == 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      Assertions.checkState(var4);
      System.arraycopy(this.data, this.byteOffset, var1, var2, var3);
      this.byteOffset += var3;
      this.assertValidOffset();
   }

   public void reset(ParsableByteArray var1) {
      this.reset(var1.data, var1.limit());
      this.setPosition(var1.getPosition() * 8);
   }

   public void reset(byte[] var1) {
      this.reset(var1, var1.length);
   }

   public void reset(byte[] var1, int var2) {
      this.data = var1;
      this.byteOffset = 0;
      this.bitOffset = 0;
      this.byteLimit = var2;
   }

   public void setPosition(int var1) {
      this.byteOffset = var1 / 8;
      this.bitOffset = var1 - this.byteOffset * 8;
      this.assertValidOffset();
   }

   public void skipBit() {
      int var1 = this.bitOffset + 1;
      this.bitOffset = var1;
      if (var1 == 8) {
         this.bitOffset = 0;
         ++this.byteOffset;
      }

      this.assertValidOffset();
   }

   public void skipBits(int var1) {
      int var2 = var1 / 8;
      this.byteOffset += var2;
      this.bitOffset += var1 - var2 * 8;
      var1 = this.bitOffset;
      if (var1 > 7) {
         ++this.byteOffset;
         this.bitOffset = var1 - 8;
      }

      this.assertValidOffset();
   }

   public void skipBytes(int var1) {
      boolean var2;
      if (this.bitOffset == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      Assertions.checkState(var2);
      this.byteOffset += var1;
      this.assertValidOffset();
   }
}
