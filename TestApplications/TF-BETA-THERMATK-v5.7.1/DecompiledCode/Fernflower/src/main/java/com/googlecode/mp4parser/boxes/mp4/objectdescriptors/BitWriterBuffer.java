package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import java.nio.ByteBuffer;

public class BitWriterBuffer {
   private ByteBuffer buffer;
   int initialPos;
   int position = 0;

   public BitWriterBuffer(ByteBuffer var1) {
      this.buffer = var1;
      this.initialPos = var1.position();
   }

   public void writeBits(int var1, int var2) {
      int var3 = this.position;
      int var4 = 8 - var3 % 8;
      byte var5 = 1;
      ByteBuffer var7;
      if (var2 <= var4) {
         byte var6 = this.buffer.get(this.initialPos + var3 / 8);
         var3 = var6;
         if (var6 < 0) {
            var3 = var6 + 256;
         }

         var3 += var1 << var4 - var2;
         var7 = this.buffer;
         var4 = this.initialPos;
         int var9 = this.position / 8;
         var1 = var3;
         if (var3 > 127) {
            var1 = var3 - 256;
         }

         var7.put(var4 + var9, (byte)var1);
         this.position += var2;
      } else {
         var2 -= var4;
         this.writeBits(var1 >> var2, var4);
         this.writeBits(var1 & (1 << var2) - 1, var2);
      }

      var7 = this.buffer;
      var2 = this.initialPos;
      var1 = this.position;
      var3 = var1 / 8;
      byte var8;
      if (var1 % 8 > 0) {
         var8 = var5;
      } else {
         var8 = 0;
      }

      var7.position(var2 + var3 + var8);
   }
}
