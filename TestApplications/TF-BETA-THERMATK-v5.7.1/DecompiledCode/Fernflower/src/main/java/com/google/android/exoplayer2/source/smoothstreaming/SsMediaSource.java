package com.google.android.exoplayer2.source.smoothstreaming;

import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.source.BaseMediaSource;
import com.google.android.exoplayer2.source.CompositeSequenceableLoaderFactory;
import com.google.android.exoplayer2.source.DefaultCompositeSequenceableLoaderFactory;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.SinglePeriodTimeline;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifest;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsUtil;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultLoadErrorHandlingPolicy;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.upstream.LoaderErrorThrower;
import com.google.android.exoplayer2.upstream.ParsingLoadable;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;
import java.util.ArrayList;

public final class SsMediaSource extends BaseMediaSource implements Loader.Callback {
   private final SsChunkSource.Factory chunkSourceFactory;
   private final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
   private final long livePresentationDelayMs;
   private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
   private SsManifest manifest;
   private DataSource manifestDataSource;
   private final DataSource.Factory manifestDataSourceFactory;
   private final MediaSourceEventListener.EventDispatcher manifestEventDispatcher;
   private long manifestLoadStartTimestamp;
   private Loader manifestLoader;
   private LoaderErrorThrower manifestLoaderErrorThrower;
   private final ParsingLoadable.Parser manifestParser;
   private Handler manifestRefreshHandler;
   private final Uri manifestUri;
   private final ArrayList mediaPeriods;
   private TransferListener mediaTransferListener;
   private final boolean sideloadedManifest;
   private final Object tag;

   static {
      ExoPlayerLibraryInfo.registerModule("goog.exo.smoothstreaming");
   }

   @Deprecated
   public SsMediaSource(Uri var1, DataSource.Factory var2, SsChunkSource.Factory var3, int var4, long var5, Handler var7, MediaSourceEventListener var8) {
      this(var1, var2, new SsManifestParser(), var3, var4, var5, var7, var8);
   }

   @Deprecated
   public SsMediaSource(Uri var1, DataSource.Factory var2, SsChunkSource.Factory var3, Handler var4, MediaSourceEventListener var5) {
      this(var1, var2, var3, 3, 30000L, var4, var5);
   }

   @Deprecated
   public SsMediaSource(Uri var1, DataSource.Factory var2, ParsingLoadable.Parser var3, SsChunkSource.Factory var4, int var5, long var6, Handler var8, MediaSourceEventListener var9) {
      this((SsManifest)null, var1, var2, var3, var4, new DefaultCompositeSequenceableLoaderFactory(), new DefaultLoadErrorHandlingPolicy(var5), var6, (Object)null);
      if (var8 != null && var9 != null) {
         this.addEventListener(var8, var9);
      }

   }

   private SsMediaSource(SsManifest var1, Uri var2, DataSource.Factory var3, ParsingLoadable.Parser var4, SsChunkSource.Factory var5, CompositeSequenceableLoaderFactory var6, LoadErrorHandlingPolicy var7, long var8, Object var10) {
      boolean var11 = false;
      boolean var12;
      if (var1 != null && var1.isLive) {
         var12 = false;
      } else {
         var12 = true;
      }

      Assertions.checkState(var12);
      this.manifest = var1;
      if (var2 == null) {
         var2 = null;
      } else {
         var2 = SsUtil.fixManifestUri(var2);
      }

      this.manifestUri = var2;
      this.manifestDataSourceFactory = var3;
      this.manifestParser = var4;
      this.chunkSourceFactory = var5;
      this.compositeSequenceableLoaderFactory = var6;
      this.loadErrorHandlingPolicy = var7;
      this.livePresentationDelayMs = var8;
      this.manifestEventDispatcher = this.createEventDispatcher((MediaSource.MediaPeriodId)null);
      this.tag = var10;
      var12 = var11;
      if (var1 != null) {
         var12 = true;
      }

      this.sideloadedManifest = var12;
      this.mediaPeriods = new ArrayList();
   }

   // $FF: synthetic method
   public static void lambda$tFjHmMdOxDkhvkY7QhPdfdPmbtI(SsMediaSource var0) {
      var0.startLoadingManifest();
   }

   private void processManifest() {
      int var1;
      for(var1 = 0; var1 < this.mediaPeriods.size(); ++var1) {
         ((SsMediaPeriod)this.mediaPeriods.get(var1)).updateManifest(this.manifest);
      }

      SsManifest.StreamElement[] var2 = this.manifest.streamElements;
      int var3 = var2.length;
      long var4 = Long.MIN_VALUE;
      long var6 = Long.MAX_VALUE;

      long var9;
      long var11;
      for(var1 = 0; var1 < var3; var6 = var11) {
         SsManifest.StreamElement var8 = var2[var1];
         var9 = var4;
         var11 = var6;
         if (var8.chunkCount > 0) {
            var11 = Math.min(var6, var8.getStartTimeUs(0));
            var9 = Math.max(var4, var8.getStartTimeUs(var8.chunkCount - 1) + var8.getChunkDurationUs(var8.chunkCount - 1));
         }

         ++var1;
         var4 = var9;
      }

      SinglePeriodTimeline var13;
      if (var6 == Long.MAX_VALUE) {
         if (this.manifest.isLive) {
            var6 = -9223372036854775807L;
         } else {
            var6 = 0L;
         }

         var13 = new SinglePeriodTimeline(var6, 0L, 0L, 0L, true, this.manifest.isLive, this.tag);
      } else {
         SsManifest var14 = this.manifest;
         if (var14.isLive) {
            var9 = var14.dvrWindowLengthUs;
            var11 = var6;
            if (var9 != -9223372036854775807L) {
               var11 = var6;
               if (var9 > 0L) {
                  var11 = Math.max(var6, var4 - var9);
               }
            }

            var9 = var4 - var11;
            var4 = var9 - C.msToUs(this.livePresentationDelayMs);
            var6 = var4;
            if (var4 < 5000000L) {
               var6 = Math.min(5000000L, var9 / 2L);
            }

            var13 = new SinglePeriodTimeline(-9223372036854775807L, var9, var11, var6, true, true, this.tag);
         } else {
            var11 = var14.durationUs;
            if (var11 != -9223372036854775807L) {
               var4 = var11;
            } else {
               var4 -= var6;
            }

            var13 = new SinglePeriodTimeline(var6 + var4, var4, var6, 0L, true, false, this.tag);
         }
      }

      this.refreshSourceInfo(var13, this.manifest);
   }

   private void scheduleManifestRefresh() {
      if (this.manifest.isLive) {
         long var1 = Math.max(0L, this.manifestLoadStartTimestamp + 5000L - SystemClock.elapsedRealtime());
         this.manifestRefreshHandler.postDelayed(new _$$Lambda$SsMediaSource$tFjHmMdOxDkhvkY7QhPdfdPmbtI(this), var1);
      }
   }

   private void startLoadingManifest() {
      ParsingLoadable var1 = new ParsingLoadable(this.manifestDataSource, this.manifestUri, 4, this.manifestParser);
      long var2 = this.manifestLoader.startLoading(var1, this, this.loadErrorHandlingPolicy.getMinimumLoadableRetryCount(var1.type));
      this.manifestEventDispatcher.loadStarted(var1.dataSpec, var1.type, var2);
   }

   public MediaPeriod createPeriod(MediaSource.MediaPeriodId var1, Allocator var2, long var3) {
      MediaSourceEventListener.EventDispatcher var5 = this.createEventDispatcher(var1);
      SsMediaPeriod var6 = new SsMediaPeriod(this.manifest, this.chunkSourceFactory, this.mediaTransferListener, this.compositeSequenceableLoaderFactory, this.loadErrorHandlingPolicy, var5, this.manifestLoaderErrorThrower, var2);
      this.mediaPeriods.add(var6);
      return var6;
   }

   public void maybeThrowSourceInfoRefreshError() throws IOException {
      this.manifestLoaderErrorThrower.maybeThrowError();
   }

   public void onLoadCanceled(ParsingLoadable var1, long var2, long var4, boolean var6) {
      this.manifestEventDispatcher.loadCanceled(var1.dataSpec, var1.getUri(), var1.getResponseHeaders(), var1.type, var2, var4, var1.bytesLoaded());
   }

   public void onLoadCompleted(ParsingLoadable var1, long var2, long var4) {
      this.manifestEventDispatcher.loadCompleted(var1.dataSpec, var1.getUri(), var1.getResponseHeaders(), var1.type, var2, var4, var1.bytesLoaded());
      this.manifest = (SsManifest)var1.getResult();
      this.manifestLoadStartTimestamp = var2 - var4;
      this.processManifest();
      this.scheduleManifestRefresh();
   }

   public Loader.LoadErrorAction onLoadError(ParsingLoadable var1, long var2, long var4, IOException var6, int var7) {
      boolean var8 = var6 instanceof ParserException;
      this.manifestEventDispatcher.loadError(var1.dataSpec, var1.getUri(), var1.getResponseHeaders(), var1.type, var2, var4, var1.bytesLoaded(), var6, var8);
      Loader.LoadErrorAction var9;
      if (var8) {
         var9 = Loader.DONT_RETRY_FATAL;
      } else {
         var9 = Loader.RETRY;
      }

      return var9;
   }

   public void prepareSourceInternal(TransferListener var1) {
      this.mediaTransferListener = var1;
      if (this.sideloadedManifest) {
         this.manifestLoaderErrorThrower = new LoaderErrorThrower.Dummy();
         this.processManifest();
      } else {
         this.manifestDataSource = this.manifestDataSourceFactory.createDataSource();
         this.manifestLoader = new Loader("Loader:Manifest");
         this.manifestLoaderErrorThrower = this.manifestLoader;
         this.manifestRefreshHandler = new Handler();
         this.startLoadingManifest();
      }

   }

   public void releasePeriod(MediaPeriod var1) {
      ((SsMediaPeriod)var1).release();
      this.mediaPeriods.remove(var1);
   }

   public void releaseSourceInternal() {
      SsManifest var1;
      if (this.sideloadedManifest) {
         var1 = this.manifest;
      } else {
         var1 = null;
      }

      this.manifest = var1;
      this.manifestDataSource = null;
      this.manifestLoadStartTimestamp = 0L;
      Loader var2 = this.manifestLoader;
      if (var2 != null) {
         var2.release();
         this.manifestLoader = null;
      }

      Handler var3 = this.manifestRefreshHandler;
      if (var3 != null) {
         var3.removeCallbacksAndMessages((Object)null);
         this.manifestRefreshHandler = null;
      }

   }
}
