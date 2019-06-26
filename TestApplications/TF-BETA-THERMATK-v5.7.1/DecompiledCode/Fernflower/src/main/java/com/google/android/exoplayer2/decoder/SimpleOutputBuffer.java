package com.google.android.exoplayer2.decoder;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SimpleOutputBuffer extends OutputBuffer {
   public ByteBuffer data;
   private final SimpleDecoder owner;

   public SimpleOutputBuffer(SimpleDecoder var1) {
      this.owner = var1;
   }

   public void clear() {
      super.clear();
      ByteBuffer var1 = this.data;
      if (var1 != null) {
         var1.clear();
      }

   }

   public ByteBuffer init(long var1, int var3) {
      super.timeUs = var1;
      ByteBuffer var4 = this.data;
      if (var4 == null || var4.capacity() < var3) {
         this.data = ByteBuffer.allocateDirect(var3).order(ByteOrder.nativeOrder());
      }

      this.data.position(0);
      this.data.limit(var3);
      return this.data;
   }

   public void release() {
      this.owner.releaseOutputBuffer(this);
   }
}
