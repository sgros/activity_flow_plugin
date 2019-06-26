package com.google.android.exoplayer2.source.chunk;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.util.Assertions;

public abstract class MediaChunk extends Chunk {
   public final long chunkIndex;

   public MediaChunk(DataSource var1, DataSpec var2, Format var3, int var4, Object var5, long var6, long var8, long var10) {
      super(var1, var2, 1, var3, var4, var5, var6, var8);
      Assertions.checkNotNull(var3);
      this.chunkIndex = var10;
   }

   public long getNextChunkIndex() {
      long var1 = this.chunkIndex;
      long var3 = -1L;
      if (var1 != -1L) {
         var3 = 1L + var1;
      }

      return var3;
   }

   public abstract boolean isLoadCompleted();
}
