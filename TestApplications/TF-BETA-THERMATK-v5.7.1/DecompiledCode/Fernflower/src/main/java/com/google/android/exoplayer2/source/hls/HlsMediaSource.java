package com.google.android.exoplayer2.source.hls;

import android.net.Uri;
import android.os.Handler;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import com.google.android.exoplayer2.source.BaseMediaSource;
import com.google.android.exoplayer2.source.CompositeSequenceableLoaderFactory;
import com.google.android.exoplayer2.source.DefaultCompositeSequenceableLoaderFactory;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.SinglePeriodTimeline;
import com.google.android.exoplayer2.source.hls.playlist.DefaultHlsPlaylistTracker;
import com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistParser;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultLoadErrorHandlingPolicy;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy;
import com.google.android.exoplayer2.upstream.ParsingLoadable;
import com.google.android.exoplayer2.upstream.TransferListener;
import java.io.IOException;
import java.util.List;

public final class HlsMediaSource extends BaseMediaSource implements HlsPlaylistTracker.PrimaryPlaylistListener {
   private final boolean allowChunklessPreparation;
   private final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
   private final HlsDataSourceFactory dataSourceFactory;
   private final HlsExtractorFactory extractorFactory;
   private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
   private final Uri manifestUri;
   private TransferListener mediaTransferListener;
   private final HlsPlaylistTracker playlistTracker;
   private final Object tag;

   static {
      ExoPlayerLibraryInfo.registerModule("goog.exo.hls");
   }

   @Deprecated
   public HlsMediaSource(Uri var1, HlsDataSourceFactory var2, HlsExtractorFactory var3, int var4, Handler var5, MediaSourceEventListener var6, ParsingLoadable.Parser var7) {
      this(var1, var2, var3, new DefaultCompositeSequenceableLoaderFactory(), new DefaultLoadErrorHandlingPolicy(var4), new DefaultHlsPlaylistTracker(var2, new DefaultLoadErrorHandlingPolicy(var4), var7), false, (Object)null);
      if (var5 != null && var6 != null) {
         this.addEventListener(var5, var6);
      }

   }

   private HlsMediaSource(Uri var1, HlsDataSourceFactory var2, HlsExtractorFactory var3, CompositeSequenceableLoaderFactory var4, LoadErrorHandlingPolicy var5, HlsPlaylistTracker var6, boolean var7, Object var8) {
      this.manifestUri = var1;
      this.dataSourceFactory = var2;
      this.extractorFactory = var3;
      this.compositeSequenceableLoaderFactory = var4;
      this.loadErrorHandlingPolicy = var5;
      this.playlistTracker = var6;
      this.allowChunklessPreparation = var7;
      this.tag = var8;
   }

   @Deprecated
   public HlsMediaSource(Uri var1, DataSource.Factory var2, int var3, Handler var4, MediaSourceEventListener var5) {
      this(var1, new DefaultHlsDataSourceFactory(var2), HlsExtractorFactory.DEFAULT, var3, var4, var5, new HlsPlaylistParser());
   }

   @Deprecated
   public HlsMediaSource(Uri var1, DataSource.Factory var2, Handler var3, MediaSourceEventListener var4) {
      this(var1, var2, 3, var3, var4);
   }

   public MediaPeriod createPeriod(MediaSource.MediaPeriodId var1, Allocator var2, long var3) {
      MediaSourceEventListener.EventDispatcher var5 = this.createEventDispatcher(var1);
      return new HlsMediaPeriod(this.extractorFactory, this.playlistTracker, this.dataSourceFactory, this.mediaTransferListener, this.loadErrorHandlingPolicy, var5, var2, this.compositeSequenceableLoaderFactory, this.allowChunklessPreparation);
   }

   public void maybeThrowSourceInfoRefreshError() throws IOException {
      this.playlistTracker.maybeThrowPrimaryPlaylistRefreshError();
   }

   public void onPrimaryPlaylistRefreshed(HlsMediaPlaylist var1) {
      long var2;
      if (var1.hasProgramDateTime) {
         var2 = C.usToMs(var1.startTimeUs);
      } else {
         var2 = -9223372036854775807L;
      }

      int var4 = var1.playlistType;
      long var5;
      if (var4 != 2 && var4 != 1) {
         var5 = -9223372036854775807L;
      } else {
         var5 = var2;
      }

      long var7 = var1.startOffsetUs;
      long var11;
      SinglePeriodTimeline var14;
      if (this.playlistTracker.isLive()) {
         long var9 = var1.startTimeUs - this.playlistTracker.getInitialStartTimeUs();
         if (var1.hasEndTag) {
            var11 = var9 + var1.durationUs;
         } else {
            var11 = -9223372036854775807L;
         }

         List var13 = var1.segments;
         if (var7 == -9223372036854775807L) {
            if (var13.isEmpty()) {
               var7 = 0L;
            } else {
               var7 = ((HlsMediaPlaylist.Segment)var13.get(Math.max(0, var13.size() - 3))).relativeStartTimeUs;
            }
         }

         var14 = new SinglePeriodTimeline(var5, var2, var11, var1.durationUs, var9, var7, true, var1.hasEndTag ^ true, this.tag);
      } else {
         if (var7 == -9223372036854775807L) {
            var7 = 0L;
         }

         var11 = var1.durationUs;
         var14 = new SinglePeriodTimeline(var5, var2, var11, var11, 0L, var7, true, false, this.tag);
      }

      this.refreshSourceInfo(var14, new HlsManifest(this.playlistTracker.getMasterPlaylist(), var1));
   }

   public void prepareSourceInternal(TransferListener var1) {
      this.mediaTransferListener = var1;
      MediaSourceEventListener.EventDispatcher var2 = this.createEventDispatcher((MediaSource.MediaPeriodId)null);
      this.playlistTracker.start(this.manifestUri, var2, this);
   }

   public void releasePeriod(MediaPeriod var1) {
      ((HlsMediaPeriod)var1).release();
   }

   public void releaseSourceInternal() {
      this.playlistTracker.stop();
   }
}
