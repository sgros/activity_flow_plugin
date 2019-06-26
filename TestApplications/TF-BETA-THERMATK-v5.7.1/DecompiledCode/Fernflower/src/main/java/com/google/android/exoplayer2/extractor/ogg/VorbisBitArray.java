package com.google.android.exoplayer2.extractor.ogg;

import com.google.android.exoplayer2.util.Assertions;

final class VorbisBitArray {
   private int bitOffset;
   private final int byteLimit;
   private int byteOffset;
   private final byte[] data;

   public VorbisBitArray(byte[] var1) {
      this.data = var1;
      this.byteLimit = var1.length;
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

   public int getPosition() {
      return this.byteOffset * 8 + this.bitOffset;
   }

   public boolean readBit() {
      boolean var1;
      if (((this.data[this.byteOffset] & 255) >> this.bitOffset & 1) == 1) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.skipBits(1);
      return var1;
   }

   public int readBits(int var1) {
      int var2 = this.byteOffset;
      int var3 = Math.min(var1, 8 - this.bitOffset);
      byte[] var4 = this.data;
      int var5 = var2 + 1;

      for(var2 = (var4[var2] & 255) >> this.bitOffset & 255 >> 8 - var3; var3 < var1; ++var5) {
         var2 |= (this.data[var5] & 255) << var3;
         var3 += 8;
      }

      this.skipBits(var1);
      return var2 & -1 >>> 32 - var1;
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
}
