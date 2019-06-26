package com.google.android.exoplayer2.source.chunk;

public abstract class BaseMediaChunkIterator implements MediaChunkIterator {
   private long currentIndex;
   private final long fromIndex;
   private final long toIndex;

   public BaseMediaChunkIterator(long var1, long var3) {
      this.fromIndex = var1;
      this.toIndex = var3;
      this.reset();
   }

   public void reset() {
      this.currentIndex = this.fromIndex - 1L;
   }
}
