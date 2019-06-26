package org.telegram.messenger.audioinfo.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PositionInputStream extends FilterInputStream {
   private long position;
   private long positionMark;

   public PositionInputStream(InputStream var1) {
      this(var1, 0L);
   }

   public PositionInputStream(InputStream var1, long var2) {
      super(var1);
      this.position = var2;
   }

   public long getPosition() {
      return this.position;
   }

   public void mark(int var1) {
      synchronized(this){}

      try {
         this.positionMark = this.position;
         super.mark(var1);
      } finally {
         ;
      }

   }

   public int read() throws IOException {
      int var1 = super.read();
      if (var1 >= 0) {
         ++this.position;
      }

      return var1;
   }

   public final int read(byte[] var1) throws IOException {
      return this.read(var1, 0, var1.length);
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      long var4 = this.position;
      var2 = super.read(var1, var2, var3);
      if (var2 > 0) {
         this.position = var4 + (long)var2;
      }

      return var2;
   }

   public void reset() throws IOException {
      synchronized(this){}

      try {
         super.reset();
         this.position = this.positionMark;
      } finally {
         ;
      }

   }

   public long skip(long var1) throws IOException {
      long var3 = this.position;
      var1 = super.skip(var1);
      this.position = var3 + var1;
      return var1;
   }
}
