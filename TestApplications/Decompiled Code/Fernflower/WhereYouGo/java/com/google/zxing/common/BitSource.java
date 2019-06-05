package com.google.zxing.common;

public final class BitSource {
   private int bitOffset;
   private int byteOffset;
   private final byte[] bytes;

   public BitSource(byte[] var1) {
      this.bytes = var1;
   }

   public int available() {
      return (this.bytes.length - this.byteOffset) * 8 - this.bitOffset;
   }

   public int getBitOffset() {
      return this.bitOffset;
   }

   public int getByteOffset() {
      return this.byteOffset;
   }

   public int readBits(int var1) {
      if (var1 > 0 && var1 <= 32 && var1 <= this.available()) {
         int var2 = 0;
         int var3 = var1;
         if (this.bitOffset > 0) {
            var3 = 8 - this.bitOffset;
            if (var1 < var3) {
               var2 = var1;
            } else {
               var2 = var3;
            }

            var3 -= var2;
            int var4 = (this.bytes[this.byteOffset] & 255 >> 8 - var2 << var3) >> var3;
            var1 -= var2;
            this.bitOffset += var2;
            var2 = var4;
            var3 = var1;
            if (this.bitOffset == 8) {
               this.bitOffset = 0;
               ++this.byteOffset;
               var3 = var1;
               var2 = var4;
            }
         }

         var1 = var2;
         if (var3 > 0) {
            while(var3 >= 8) {
               var2 = var2 << 8 | this.bytes[this.byteOffset] & 255;
               ++this.byteOffset;
               var3 -= 8;
            }

            var1 = var2;
            if (var3 > 0) {
               var1 = 8 - var3;
               var1 = var2 << var3 | (this.bytes[this.byteOffset] & 255 >> var1 << var1) >> var1;
               this.bitOffset += var3;
            }
         }

         return var1;
      } else {
         throw new IllegalArgumentException(String.valueOf(var1));
      }
   }
}
