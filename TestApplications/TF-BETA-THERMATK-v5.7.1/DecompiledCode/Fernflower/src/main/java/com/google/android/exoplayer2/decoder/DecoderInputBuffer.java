package com.google.android.exoplayer2.decoder;

import java.nio.ByteBuffer;

public class DecoderInputBuffer extends Buffer {
   private final int bufferReplacementMode;
   public final CryptoInfo cryptoInfo = new CryptoInfo();
   public ByteBuffer data;
   public long timeUs;

   public DecoderInputBuffer(int var1) {
      this.bufferReplacementMode = var1;
   }

   private ByteBuffer createReplacementByteBuffer(int var1) {
      int var2 = this.bufferReplacementMode;
      if (var2 == 1) {
         return ByteBuffer.allocate(var1);
      } else if (var2 == 2) {
         return ByteBuffer.allocateDirect(var1);
      } else {
         ByteBuffer var3 = this.data;
         if (var3 == null) {
            var2 = 0;
         } else {
            var2 = var3.capacity();
         }

         StringBuilder var4 = new StringBuilder();
         var4.append("Buffer too small (");
         var4.append(var2);
         var4.append(" < ");
         var4.append(var1);
         var4.append(")");
         throw new IllegalStateException(var4.toString());
      }
   }

   public static DecoderInputBuffer newFlagsOnlyInstance() {
      return new DecoderInputBuffer(0);
   }

   public void clear() {
      super.clear();
      ByteBuffer var1 = this.data;
      if (var1 != null) {
         var1.clear();
      }

   }

   public void ensureSpaceForWrite(int var1) {
      ByteBuffer var2 = this.data;
      if (var2 == null) {
         this.data = this.createReplacementByteBuffer(var1);
      } else {
         int var3 = var2.capacity();
         int var4 = this.data.position();
         var1 += var4;
         if (var3 < var1) {
            var2 = this.createReplacementByteBuffer(var1);
            if (var4 > 0) {
               this.data.position(0);
               this.data.limit(var4);
               var2.put(this.data);
            }

            this.data = var2;
         }
      }
   }

   public final void flip() {
      this.data.flip();
   }

   public final boolean isEncrypted() {
      return this.getFlag(1073741824);
   }

   public final boolean isFlagsOnly() {
      boolean var1;
      if (this.data == null && this.bufferReplacementMode == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }
}
