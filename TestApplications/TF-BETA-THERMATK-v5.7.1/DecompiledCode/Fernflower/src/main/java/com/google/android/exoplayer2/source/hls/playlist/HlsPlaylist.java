package com.google.android.exoplayer2.source.hls.playlist;

import com.google.android.exoplayer2.offline.FilterableManifest;
import java.util.Collections;
import java.util.List;

public abstract class HlsPlaylist implements FilterableManifest {
   public final String baseUri;
   public final boolean hasIndependentSegments;
   public final List tags;

   protected HlsPlaylist(String var1, List var2, boolean var3) {
      this.baseUri = var1;
      this.tags = Collections.unmodifiableList(var2);
      this.hasIndependentSegments = var3;
   }
}
