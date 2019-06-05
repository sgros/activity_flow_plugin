package com.google.zxing.aztec.encoder;

import com.google.zxing.common.BitArray;

final class BinaryShiftToken extends Token {
   private final short binaryShiftByteCount;
   private final short binaryShiftStart;

   BinaryShiftToken(Token var1, int var2, int var3) {
      super(var1);
      this.binaryShiftStart = (short)((short)var2);
      this.binaryShiftByteCount = (short)((short)var3);
   }

   public void appendTo(BitArray var1, byte[] var2) {
      for(int var3 = 0; var3 < this.binaryShiftByteCount; ++var3) {
         if (var3 == 0 || var3 == 31 && this.binaryShiftByteCount <= 62) {
            var1.appendBits(31, 5);
            if (this.binaryShiftByteCount > 62) {
               var1.appendBits(this.binaryShiftByteCount - 31, 16);
            } else if (var3 == 0) {
               var1.appendBits(Math.min(this.binaryShiftByteCount, 31), 5);
            } else {
               var1.appendBits(this.binaryShiftByteCount - 31, 5);
            }
         }

         var1.appendBits(var2[this.binaryShiftStart + var3], 8);
      }

   }

   public String toString() {
      return "<" + this.binaryShiftStart + "::" + (this.binaryShiftStart + this.binaryShiftByteCount - 1) + '>';
   }
}
