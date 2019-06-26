package org.telegram.messenger.audioinfo.util;

import java.io.IOException;
import java.io.InputStream;

public class RangeInputStream extends PositionInputStream {
   private final long endPosition;

   public RangeInputStream(InputStream var1, long var2, long var4) throws IOException {
      super(var1, var2);
      this.endPosition = var2 + var4;
   }

   public long getRemainingLength() {
      return this.endPosition - this.getPosition();
   }

   public int read() throws IOException {
      return this.getPosition() == this.endPosition ? -1 : super.read();
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      long var4 = this.getPosition();
      long var6 = (long)var3;
      long var8 = this.endPosition;
      if (var4 + var6 > var8) {
         int var10 = (int)(var8 - this.getPosition());
         var3 = var10;
         if (var10 == 0) {
            return -1;
         }
      }

      return super.read(var1, var2, var3);
   }

   public long skip(long var1) throws IOException {
      long var3 = this.getPosition();
      long var5 = this.endPosition;
      long var7 = var1;
      if (var3 + var1 > var5) {
         var7 = (long)((int)(var5 - this.getPosition()));
      }

      return super.skip(var7);
   }
}
