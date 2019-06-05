package com.google.zxing.aztec.encoder;

import com.google.zxing.common.BitArray;

final class SimpleToken extends Token {
   private final short bitCount;
   private final short value;

   SimpleToken(Token var1, int var2, int var3) {
      super(var1);
      this.value = (short)((short)var2);
      this.bitCount = (short)((short)var3);
   }

   void appendTo(BitArray var1, byte[] var2) {
      var1.appendBits(this.value, this.bitCount);
   }

   public String toString() {
      short var1 = this.value;
      short var2 = this.bitCount;
      short var3 = this.bitCount;
      return "<" + Integer.toBinaryString(1 << this.bitCount | var1 & (1 << var2) - 1 | 1 << var3).substring(1) + '>';
   }
}
