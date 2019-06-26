package com.google.android.exoplayer2.source.chunk;

import com.google.android.exoplayer2.SeekParameters;
import java.io.IOException;
import java.util.List;

public interface ChunkSource {
   long getAdjustedSeekPositionUs(long var1, SeekParameters var3);

   void getNextChunk(long var1, long var3, List var5, ChunkHolder var6);

   int getPreferredQueueSize(long var1, List var3);

   void maybeThrowError() throws IOException;

   void onChunkLoadCompleted(Chunk var1);

   boolean onChunkLoadError(Chunk var1, boolean var2, Exception var3, long var4);
}
