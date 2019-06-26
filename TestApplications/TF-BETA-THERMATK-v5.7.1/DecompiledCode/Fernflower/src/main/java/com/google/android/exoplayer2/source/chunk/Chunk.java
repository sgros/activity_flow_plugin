package com.google.android.exoplayer2.source.chunk;

import android.net.Uri;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.upstream.StatsDataSource;
import com.google.android.exoplayer2.util.Assertions;
import java.util.Map;

public abstract class Chunk implements Loader.Loadable {
   protected final StatsDataSource dataSource;
   public final DataSpec dataSpec;
   public final long endTimeUs;
   public final long startTimeUs;
   public final Format trackFormat;
   public final Object trackSelectionData;
   public final int trackSelectionReason;
   public final int type;

   public Chunk(DataSource var1, DataSpec var2, int var3, Format var4, int var5, Object var6, long var7, long var9) {
      this.dataSource = new StatsDataSource(var1);
      Assertions.checkNotNull(var2);
      this.dataSpec = (DataSpec)var2;
      this.type = var3;
      this.trackFormat = var4;
      this.trackSelectionReason = var5;
      this.trackSelectionData = var6;
      this.startTimeUs = var7;
      this.endTimeUs = var9;
   }

   public final long bytesLoaded() {
      return this.dataSource.getBytesRead();
   }

   public final long getDurationUs() {
      return this.endTimeUs - this.startTimeUs;
   }

   public final Map getResponseHeaders() {
      return this.dataSource.getLastResponseHeaders();
   }

   public final Uri getUri() {
      return this.dataSource.getLastOpenedUri();
   }
}
