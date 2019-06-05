package com.google.zxing.aztec.encoder;

import com.google.zxing.common.BitArray;

abstract class Token {
   static final Token EMPTY = new SimpleToken((Token)null, 0, 0);
   private final Token previous;

   Token(Token var1) {
      this.previous = var1;
   }

   final Token add(int var1, int var2) {
      return new SimpleToken(this, var1, var2);
   }

   final Token addBinaryShift(int var1, int var2) {
      return new BinaryShiftToken(this, var1, var2);
   }

   abstract void appendTo(BitArray var1, byte[] var2);

   final Token getPrevious() {
      return this.previous;
   }
}
