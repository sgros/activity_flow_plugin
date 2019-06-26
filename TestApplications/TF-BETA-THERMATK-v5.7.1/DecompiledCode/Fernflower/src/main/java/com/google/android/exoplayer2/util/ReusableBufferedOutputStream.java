package com.google.android.exoplayer2.util;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public final class ReusableBufferedOutputStream extends BufferedOutputStream {
   private boolean closed;

   public ReusableBufferedOutputStream(OutputStream var1) {
      super(var1);
   }

   public ReusableBufferedOutputStream(OutputStream var1, int var2) {
      super(var1, var2);
   }

   public void close() throws IOException {
      this.closed = true;

      Throwable var1;
      label29: {
         try {
            this.flush();
         } catch (Throwable var5) {
            var1 = var5;
            break label29;
         }

         var1 = null;
      }

      Throwable var2;
      label25: {
         try {
            super.out.close();
         } catch (Throwable var4) {
            var2 = var1;
            if (var1 == null) {
               var2 = var4;
            }
            break label25;
         }

         var2 = var1;
      }

      if (var2 != null) {
         Util.sneakyThrow(var2);
         throw null;
      }
   }

   public void reset(OutputStream var1) {
      Assertions.checkState(this.closed);
      super.out = var1;
      super.count = 0;
      this.closed = false;
   }
}
