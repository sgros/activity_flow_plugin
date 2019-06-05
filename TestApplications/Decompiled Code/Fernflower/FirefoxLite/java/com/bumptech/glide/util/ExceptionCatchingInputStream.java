package com.bumptech.glide.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;

public class ExceptionCatchingInputStream extends InputStream {
   private static final Queue QUEUE = Util.createQueue(0);
   private IOException exception;
   private InputStream wrapped;

   ExceptionCatchingInputStream() {
   }

   public static ExceptionCatchingInputStream obtain(InputStream param0) {
      // $FF: Couldn't be decompiled
   }

   public int available() throws IOException {
      return this.wrapped.available();
   }

   public void close() throws IOException {
      this.wrapped.close();
   }

   public IOException getException() {
      return this.exception;
   }

   public void mark(int var1) {
      this.wrapped.mark(var1);
   }

   public boolean markSupported() {
      return this.wrapped.markSupported();
   }

   public int read() throws IOException {
      int var1;
      try {
         var1 = this.wrapped.read();
      } catch (IOException var3) {
         this.exception = var3;
         var1 = -1;
      }

      return var1;
   }

   public int read(byte[] var1) throws IOException {
      int var2;
      try {
         var2 = this.wrapped.read(var1);
      } catch (IOException var3) {
         this.exception = var3;
         var2 = -1;
      }

      return var2;
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      try {
         var2 = this.wrapped.read(var1, var2, var3);
      } catch (IOException var4) {
         this.exception = var4;
         var2 = -1;
      }

      return var2;
   }

   public void release() {
      // $FF: Couldn't be decompiled
   }

   public void reset() throws IOException {
      synchronized(this){}

      try {
         this.wrapped.reset();
      } finally {
         ;
      }

   }

   void setInputStream(InputStream var1) {
      this.wrapped = var1;
   }

   public long skip(long var1) throws IOException {
      try {
         var1 = this.wrapped.skip(var1);
      } catch (IOException var4) {
         this.exception = var4;
         var1 = 0L;
      }

      return var1;
   }
}
