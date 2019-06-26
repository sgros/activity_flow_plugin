package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import java.nio.ByteBuffer;

public class BitReaderBuffer {
   private ByteBuffer buffer;
   int initialPos;
   int position;

   public BitReaderBuffer(ByteBuffer var1) {
      this.buffer = var1;
      this.initialPos = var1.position();
   }

   public int readBits(int var1) {
      byte var2 = this.buffer.get(this.initialPos + this.position / 8);
      int var3 = var2;
      if (var2 < 0) {
         var3 = var2 + 256;
      }

      int var4 = this.position;
      int var8 = 8 - var4 % 8;
      if (var1 <= var8) {
         var3 = (var3 << var4 % 8 & 255) >> var4 % 8 + (var8 - var1);
         this.position = var4 + var1;
         var1 = var3;
      } else {
         var1 -= var8;
         var1 = (this.readBits(var8) << var1) + this.readBits(var1);
      }

      ByteBuffer var5 = this.buffer;
      var3 = this.initialPos;
      double var6 = (double)this.position;
      Double.isNaN(var6);
      var5.position(var3 + (int)Math.ceil(var6 / 8.0D));
      return var1;
   }

   public boolean readBool() {
      return this.readBits(1) == 1;
   }

   public int remainingBits() {
      return this.buffer.limit() * 8 - this.position;
   }
}
