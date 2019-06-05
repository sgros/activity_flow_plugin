package com.bumptech.glide.load.data;

import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.resource.bitmap.RecyclableBufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class InputStreamRewinder implements DataRewinder {
   private final RecyclableBufferedInputStream bufferedStream;

   InputStreamRewinder(InputStream var1, ArrayPool var2) {
      this.bufferedStream = new RecyclableBufferedInputStream(var1, var2);
      this.bufferedStream.mark(5242880);
   }

   public void cleanup() {
      this.bufferedStream.release();
   }

   public InputStream rewindAndGet() throws IOException {
      this.bufferedStream.reset();
      return this.bufferedStream;
   }

   public static final class Factory implements DataRewinder.Factory {
      private final ArrayPool byteArrayPool;

      public Factory(ArrayPool var1) {
         this.byteArrayPool = var1;
      }

      public DataRewinder build(InputStream var1) {
         return new InputStreamRewinder(var1, this.byteArrayPool);
      }

      public Class getDataClass() {
         return InputStream.class;
      }
   }
}
