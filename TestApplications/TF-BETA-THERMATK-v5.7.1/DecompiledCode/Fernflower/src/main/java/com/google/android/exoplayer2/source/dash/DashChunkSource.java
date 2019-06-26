package com.google.android.exoplayer2.source.dash;

import com.google.android.exoplayer2.source.chunk.ChunkSource;
import com.google.android.exoplayer2.source.dash.manifest.DashManifest;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.LoaderErrorThrower;
import com.google.android.exoplayer2.upstream.TransferListener;

public interface DashChunkSource extends ChunkSource {
   void updateManifest(DashManifest var1, int var2);

   public interface Factory {
      DashChunkSource createDashChunkSource(LoaderErrorThrower var1, DashManifest var2, int var3, int[] var4, TrackSelection var5, int var6, long var7, boolean var9, boolean var10, PlayerEmsgHandler.PlayerTrackEmsgHandler var11, TransferListener var12);
   }
}
