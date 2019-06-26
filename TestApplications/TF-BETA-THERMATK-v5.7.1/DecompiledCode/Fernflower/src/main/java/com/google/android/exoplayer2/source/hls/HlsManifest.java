package com.google.android.exoplayer2.source.hls;

import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist;

public final class HlsManifest {
   public final HlsMasterPlaylist masterPlaylist;
   public final HlsMediaPlaylist mediaPlaylist;

   HlsManifest(HlsMasterPlaylist var1, HlsMediaPlaylist var2) {
      this.masterPlaylist = var1;
      this.mediaPlaylist = var2;
   }
}
