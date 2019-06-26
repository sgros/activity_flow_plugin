package com.google.android.exoplayer2.source.smoothstreaming;

import com.google.android.exoplayer2.source.chunk.ChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifest;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.LoaderErrorThrower;
import com.google.android.exoplayer2.upstream.TransferListener;

public interface SsChunkSource extends ChunkSource {
   void updateManifest(SsManifest var1);

   public interface Factory {
      SsChunkSource createChunkSource(LoaderErrorThrower var1, SsManifest var2, int var3, TrackSelection var4, TransferListener var5);
   }
}
