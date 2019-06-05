package com.bumptech.glide.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ContentLengthInputStream extends FilterInputStream {
   private final long contentLength;
   private int readSoFar;

   ContentLengthInputStream(InputStream var1, long var2) {
      super(var1);
      this.contentLength = var2;
   }

   private int checkReadSoFarOrThrow(int var1) throws IOException {
      if (var1 >= 0) {
         this.readSoFar += var1;
      } else if (this.contentLength - (long)this.readSoFar > 0L) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Failed to read all expected data, expected: ");
         var2.append(this.contentLength);
         var2.append(", but read: ");
         var2.append(this.readSoFar);
         throw new IOException(var2.toString());
      }

      return var1;
   }

   public static InputStream obtain(InputStream var0, long var1) {
      return new ContentLengthInputStream(var0, var1);
   }

   public int available() throws IOException {
      synchronized(this){}
      boolean var6 = false;

      long var1;
      try {
         var6 = true;
         var1 = Math.max(this.contentLength - (long)this.readSoFar, (long)this.in.available());
         var6 = false;
      } finally {
         if (var6) {
            ;
         }
      }

      int var3 = (int)var1;
      return var3;
   }

   public int read() throws IOException {
      synchronized(this){}

      Throwable var10000;
      label81: {
         int var1;
         boolean var10001;
         try {
            var1 = super.read();
         } catch (Throwable var9) {
            var10000 = var9;
            var10001 = false;
            break label81;
         }

         byte var2;
         if (var1 >= 0) {
            var2 = 1;
         } else {
            var2 = -1;
         }

         label72:
         try {
            this.checkReadSoFarOrThrow(var2);
            return var1;
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label72;
         }
      }

      Throwable var3 = var10000;
      throw var3;
   }

   public int read(byte[] var1) throws IOException {
      return this.read(var1, 0, var1.length);
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      synchronized(this){}

      try {
         var2 = this.checkReadSoFarOrThrow(super.read(var1, var2, var3));
      } finally {
         ;
      }

      return var2;
   }
}
