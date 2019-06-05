package com.bumptech.glide.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicReference;

public final class ByteBufferUtil {
   private static final AtomicReference BUFFER_REF = new AtomicReference();

   public static ByteBuffer fromFile(File param0) throws IOException {
      // $FF: Couldn't be decompiled
   }

   public static void toFile(ByteBuffer param0, File param1) throws IOException {
      // $FF: Couldn't be decompiled
   }

   public static InputStream toStream(ByteBuffer var0) {
      return new ByteBufferUtil.ByteBufferStream(var0);
   }

   private static class ByteBufferStream extends InputStream {
      private final ByteBuffer byteBuffer;
      private int markPos = -1;

      public ByteBufferStream(ByteBuffer var1) {
         this.byteBuffer = var1;
      }

      public int available() throws IOException {
         return this.byteBuffer.remaining();
      }

      public void mark(int var1) {
         synchronized(this){}

         try {
            this.markPos = this.byteBuffer.position();
         } finally {
            ;
         }

      }

      public boolean markSupported() {
         return true;
      }

      public int read() throws IOException {
         return !this.byteBuffer.hasRemaining() ? -1 : this.byteBuffer.get();
      }

      public int read(byte[] var1, int var2, int var3) throws IOException {
         if (!this.byteBuffer.hasRemaining()) {
            return -1;
         } else {
            var3 = Math.min(var3, this.available());
            this.byteBuffer.get(var1, var2, var3);
            return var3;
         }
      }

      public void reset() throws IOException {
         synchronized(this){}

         try {
            if (this.markPos == -1) {
               IOException var1 = new IOException("Cannot reset to unset mark position");
               throw var1;
            }

            this.byteBuffer.position(this.markPos);
         } finally {
            ;
         }

      }

      public long skip(long var1) throws IOException {
         if (!this.byteBuffer.hasRemaining()) {
            return -1L;
         } else {
            var1 = Math.min(var1, (long)this.available());
            this.byteBuffer.position((int)((long)this.byteBuffer.position() + var1));
            return var1;
         }
      }
   }
}
