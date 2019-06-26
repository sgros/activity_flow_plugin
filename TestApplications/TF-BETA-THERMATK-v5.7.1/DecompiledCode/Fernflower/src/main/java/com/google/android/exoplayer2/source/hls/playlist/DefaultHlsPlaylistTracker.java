package com.google.android.exoplayer2.source.hls.playlist;

import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.hls.HlsDataSourceFactory;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.upstream.ParsingLoadable;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.UriUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;

public final class DefaultHlsPlaylistTracker implements HlsPlaylistTracker, Loader.Callback {
   public static final HlsPlaylistTracker.Factory FACTORY;
   private final HlsDataSourceFactory dataSourceFactory;
   private MediaSourceEventListener.EventDispatcher eventDispatcher;
   private Loader initialPlaylistLoader;
   private long initialStartTimeUs;
   private boolean isLive;
   private final List listeners;
   private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
   private HlsMasterPlaylist masterPlaylist;
   private ParsingLoadable.Parser mediaPlaylistParser;
   private final IdentityHashMap playlistBundles;
   private final HlsPlaylistParserFactory playlistParserFactory;
   private Handler playlistRefreshHandler;
   private HlsMasterPlaylist.HlsUrl primaryHlsUrl;
   private HlsPlaylistTracker.PrimaryPlaylistListener primaryPlaylistListener;
   private HlsMediaPlaylist primaryUrlSnapshot;

   static {
      FACTORY = _$$Lambda$lKTLOVxne0MoBOOliKH0gO2KDMM.INSTANCE;
   }

   public DefaultHlsPlaylistTracker(HlsDataSourceFactory var1, LoadErrorHandlingPolicy var2, HlsPlaylistParserFactory var3) {
      this.dataSourceFactory = var1;
      this.playlistParserFactory = var3;
      this.loadErrorHandlingPolicy = var2;
      this.listeners = new ArrayList();
      this.playlistBundles = new IdentityHashMap();
      this.initialStartTimeUs = -9223372036854775807L;
   }

   @Deprecated
   public DefaultHlsPlaylistTracker(HlsDataSourceFactory var1, LoadErrorHandlingPolicy var2, ParsingLoadable.Parser var3) {
      this(var1, var2, createFixedFactory(var3));
   }

   private void createBundles(List var1) {
      int var2 = var1.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         HlsMasterPlaylist.HlsUrl var4 = (HlsMasterPlaylist.HlsUrl)var1.get(var3);
         DefaultHlsPlaylistTracker.MediaPlaylistBundle var5 = new DefaultHlsPlaylistTracker.MediaPlaylistBundle(var4);
         this.playlistBundles.put(var4, var5);
      }

   }

   private static HlsPlaylistParserFactory createFixedFactory(final ParsingLoadable.Parser var0) {
      return new HlsPlaylistParserFactory() {
         public ParsingLoadable.Parser createPlaylistParser() {
            return var0;
         }

         public ParsingLoadable.Parser createPlaylistParser(HlsMasterPlaylist var1) {
            return var0;
         }
      };
   }

   private static HlsMediaPlaylist.Segment getFirstOldOverlappingSegment(HlsMediaPlaylist var0, HlsMediaPlaylist var1) {
      int var2 = (int)(var1.mediaSequence - var0.mediaSequence);
      List var3 = var0.segments;
      HlsMediaPlaylist.Segment var4;
      if (var2 < var3.size()) {
         var4 = (HlsMediaPlaylist.Segment)var3.get(var2);
      } else {
         var4 = null;
      }

      return var4;
   }

   private HlsMediaPlaylist getLatestPlaylistSnapshot(HlsMediaPlaylist var1, HlsMediaPlaylist var2) {
      if (!var2.isNewerThan(var1)) {
         HlsMediaPlaylist var3 = var1;
         if (var2.hasEndTag) {
            var3 = var1.copyWithEndTag();
         }

         return var3;
      } else {
         return var2.copyWith(this.getLoadedPlaylistStartTimeUs(var1, var2), this.getLoadedPlaylistDiscontinuitySequence(var1, var2));
      }
   }

   private int getLoadedPlaylistDiscontinuitySequence(HlsMediaPlaylist var1, HlsMediaPlaylist var2) {
      if (var2.hasDiscontinuitySequence) {
         return var2.discontinuitySequence;
      } else {
         HlsMediaPlaylist var3 = this.primaryUrlSnapshot;
         int var4;
         if (var3 != null) {
            var4 = var3.discontinuitySequence;
         } else {
            var4 = 0;
         }

         if (var1 == null) {
            return var4;
         } else {
            HlsMediaPlaylist.Segment var5 = getFirstOldOverlappingSegment(var1, var2);
            return var5 != null ? var1.discontinuitySequence + var5.relativeDiscontinuitySequence - ((HlsMediaPlaylist.Segment)var2.segments.get(0)).relativeDiscontinuitySequence : var4;
         }
      }
   }

   private long getLoadedPlaylistStartTimeUs(HlsMediaPlaylist var1, HlsMediaPlaylist var2) {
      if (var2.hasProgramDateTime) {
         return var2.startTimeUs;
      } else {
         HlsMediaPlaylist var3 = this.primaryUrlSnapshot;
         long var4;
         if (var3 != null) {
            var4 = var3.startTimeUs;
         } else {
            var4 = 0L;
         }

         if (var1 == null) {
            return var4;
         } else {
            int var6 = var1.segments.size();
            HlsMediaPlaylist.Segment var7 = getFirstOldOverlappingSegment(var1, var2);
            if (var7 != null) {
               return var1.startTimeUs + var7.relativeStartTimeUs;
            } else {
               return (long)var6 == var2.mediaSequence - var1.mediaSequence ? var1.getEndTimeUs() : var4;
            }
         }
      }
   }

   private boolean maybeSelectNewPrimaryUrl() {
      List var1 = this.masterPlaylist.variants;
      int var2 = var1.size();
      long var3 = SystemClock.elapsedRealtime();

      for(int var5 = 0; var5 < var2; ++var5) {
         DefaultHlsPlaylistTracker.MediaPlaylistBundle var6 = (DefaultHlsPlaylistTracker.MediaPlaylistBundle)this.playlistBundles.get(var1.get(var5));
         if (var3 > var6.blacklistUntilMs) {
            this.primaryHlsUrl = var6.playlistUrl;
            var6.loadPlaylist();
            return true;
         }
      }

      return false;
   }

   private void maybeSetPrimaryUrl(HlsMasterPlaylist.HlsUrl var1) {
      if (var1 != this.primaryHlsUrl && this.masterPlaylist.variants.contains(var1)) {
         HlsMediaPlaylist var2 = this.primaryUrlSnapshot;
         if (var2 == null || !var2.hasEndTag) {
            this.primaryHlsUrl = var1;
            ((DefaultHlsPlaylistTracker.MediaPlaylistBundle)this.playlistBundles.get(this.primaryHlsUrl)).loadPlaylist();
         }
      }

   }

   private boolean notifyPlaylistError(HlsMasterPlaylist.HlsUrl var1, long var2) {
      int var4 = this.listeners.size();
      int var5 = 0;

      boolean var6;
      for(var6 = false; var5 < var4; ++var5) {
         var6 |= ((HlsPlaylistTracker.PlaylistEventListener)this.listeners.get(var5)).onPlaylistError(var1, var2) ^ true;
      }

      return var6;
   }

   private void onPlaylistUpdated(HlsMasterPlaylist.HlsUrl var1, HlsMediaPlaylist var2) {
      if (var1 == this.primaryHlsUrl) {
         if (this.primaryUrlSnapshot == null) {
            this.isLive = var2.hasEndTag ^ true;
            this.initialStartTimeUs = var2.startTimeUs;
         }

         this.primaryUrlSnapshot = var2;
         this.primaryPlaylistListener.onPrimaryPlaylistRefreshed(var2);
      }

      int var3 = this.listeners.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         ((HlsPlaylistTracker.PlaylistEventListener)this.listeners.get(var4)).onPlaylistChanged();
      }

   }

   public void addListener(HlsPlaylistTracker.PlaylistEventListener var1) {
      this.listeners.add(var1);
   }

   public long getInitialStartTimeUs() {
      return this.initialStartTimeUs;
   }

   public HlsMasterPlaylist getMasterPlaylist() {
      return this.masterPlaylist;
   }

   public HlsMediaPlaylist getPlaylistSnapshot(HlsMasterPlaylist.HlsUrl var1, boolean var2) {
      HlsMediaPlaylist var3 = ((DefaultHlsPlaylistTracker.MediaPlaylistBundle)this.playlistBundles.get(var1)).getPlaylistSnapshot();
      if (var3 != null && var2) {
         this.maybeSetPrimaryUrl(var1);
      }

      return var3;
   }

   public boolean isLive() {
      return this.isLive;
   }

   public boolean isSnapshotValid(HlsMasterPlaylist.HlsUrl var1) {
      return ((DefaultHlsPlaylistTracker.MediaPlaylistBundle)this.playlistBundles.get(var1)).isSnapshotValid();
   }

   public void maybeThrowPlaylistRefreshError(HlsMasterPlaylist.HlsUrl var1) throws IOException {
      ((DefaultHlsPlaylistTracker.MediaPlaylistBundle)this.playlistBundles.get(var1)).maybeThrowPlaylistRefreshError();
   }

   public void maybeThrowPrimaryPlaylistRefreshError() throws IOException {
      Loader var1 = this.initialPlaylistLoader;
      if (var1 != null) {
         var1.maybeThrowError();
      }

      HlsMasterPlaylist.HlsUrl var2 = this.primaryHlsUrl;
      if (var2 != null) {
         this.maybeThrowPlaylistRefreshError(var2);
      }

   }

   public void onLoadCanceled(ParsingLoadable var1, long var2, long var4, boolean var6) {
      this.eventDispatcher.loadCanceled(var1.dataSpec, var1.getUri(), var1.getResponseHeaders(), 4, var2, var4, var1.bytesLoaded());
   }

   public void onLoadCompleted(ParsingLoadable var1, long var2, long var4) {
      HlsPlaylist var6 = (HlsPlaylist)var1.getResult();
      boolean var7 = var6 instanceof HlsMediaPlaylist;
      HlsMasterPlaylist var8;
      if (var7) {
         var8 = HlsMasterPlaylist.createSingleVariantMasterPlaylist(var6.baseUri);
      } else {
         var8 = (HlsMasterPlaylist)var6;
      }

      this.masterPlaylist = var8;
      this.mediaPlaylistParser = this.playlistParserFactory.createPlaylistParser(var8);
      this.primaryHlsUrl = (HlsMasterPlaylist.HlsUrl)var8.variants.get(0);
      ArrayList var9 = new ArrayList();
      var9.addAll(var8.variants);
      var9.addAll(var8.audios);
      var9.addAll(var8.subtitles);
      this.createBundles(var9);
      DefaultHlsPlaylistTracker.MediaPlaylistBundle var10 = (DefaultHlsPlaylistTracker.MediaPlaylistBundle)this.playlistBundles.get(this.primaryHlsUrl);
      if (var7) {
         var10.processLoadedPlaylist((HlsMediaPlaylist)var6, var4);
      } else {
         var10.loadPlaylist();
      }

      this.eventDispatcher.loadCompleted(var1.dataSpec, var1.getUri(), var1.getResponseHeaders(), 4, var2, var4, var1.bytesLoaded());
   }

   public Loader.LoadErrorAction onLoadError(ParsingLoadable var1, long var2, long var4, IOException var6, int var7) {
      long var8 = this.loadErrorHandlingPolicy.getRetryDelayMsFor(var1.type, var4, var6, var7);
      boolean var10;
      if (var8 == -9223372036854775807L) {
         var10 = true;
      } else {
         var10 = false;
      }

      this.eventDispatcher.loadError(var1.dataSpec, var1.getUri(), var1.getResponseHeaders(), 4, var2, var4, var1.bytesLoaded(), var6, var10);
      Loader.LoadErrorAction var11;
      if (var10) {
         var11 = Loader.DONT_RETRY_FATAL;
      } else {
         var11 = Loader.createRetryAction(false, var8);
      }

      return var11;
   }

   public void refreshPlaylist(HlsMasterPlaylist.HlsUrl var1) {
      ((DefaultHlsPlaylistTracker.MediaPlaylistBundle)this.playlistBundles.get(var1)).loadPlaylist();
   }

   public void removeListener(HlsPlaylistTracker.PlaylistEventListener var1) {
      this.listeners.remove(var1);
   }

   public void start(Uri var1, MediaSourceEventListener.EventDispatcher var2, HlsPlaylistTracker.PrimaryPlaylistListener var3) {
      this.playlistRefreshHandler = new Handler();
      this.eventDispatcher = var2;
      this.primaryPlaylistListener = var3;
      ParsingLoadable var7 = new ParsingLoadable(this.dataSourceFactory.createDataSource(4), var1, 4, this.playlistParserFactory.createPlaylistParser());
      boolean var4;
      if (this.initialPlaylistLoader == null) {
         var4 = true;
      } else {
         var4 = false;
      }

      Assertions.checkState(var4);
      this.initialPlaylistLoader = new Loader("DefaultHlsPlaylistTracker:MasterPlaylist");
      long var5 = this.initialPlaylistLoader.startLoading(var7, this, this.loadErrorHandlingPolicy.getMinimumLoadableRetryCount(var7.type));
      var2.loadStarted(var7.dataSpec, var7.type, var5);
   }

   public void stop() {
      this.primaryHlsUrl = null;
      this.primaryUrlSnapshot = null;
      this.masterPlaylist = null;
      this.initialStartTimeUs = -9223372036854775807L;
      this.initialPlaylistLoader.release();
      this.initialPlaylistLoader = null;
      Iterator var1 = this.playlistBundles.values().iterator();

      while(var1.hasNext()) {
         ((DefaultHlsPlaylistTracker.MediaPlaylistBundle)var1.next()).release();
      }

      this.playlistRefreshHandler.removeCallbacksAndMessages((Object)null);
      this.playlistRefreshHandler = null;
      this.playlistBundles.clear();
   }

   private final class MediaPlaylistBundle implements Loader.Callback, Runnable {
      private long blacklistUntilMs;
      private long earliestNextLoadTimeMs;
      private long lastSnapshotChangeMs;
      private long lastSnapshotLoadMs;
      private boolean loadPending;
      private final ParsingLoadable mediaPlaylistLoadable;
      private final Loader mediaPlaylistLoader;
      private IOException playlistError;
      private HlsMediaPlaylist playlistSnapshot;
      private final HlsMasterPlaylist.HlsUrl playlistUrl;

      public MediaPlaylistBundle(HlsMasterPlaylist.HlsUrl var2) {
         this.playlistUrl = var2;
         this.mediaPlaylistLoader = new Loader("DefaultHlsPlaylistTracker:MediaPlaylist");
         this.mediaPlaylistLoadable = new ParsingLoadable(DefaultHlsPlaylistTracker.this.dataSourceFactory.createDataSource(4), UriUtil.resolveToUri(DefaultHlsPlaylistTracker.this.masterPlaylist.baseUri, var2.url), 4, DefaultHlsPlaylistTracker.this.mediaPlaylistParser);
      }

      private boolean blacklistPlaylist(long var1) {
         this.blacklistUntilMs = SystemClock.elapsedRealtime() + var1;
         boolean var3;
         if (DefaultHlsPlaylistTracker.this.primaryHlsUrl == this.playlistUrl && !DefaultHlsPlaylistTracker.this.maybeSelectNewPrimaryUrl()) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }

      private void loadPlaylistImmediately() {
         long var1 = this.mediaPlaylistLoader.startLoading(this.mediaPlaylistLoadable, this, DefaultHlsPlaylistTracker.this.loadErrorHandlingPolicy.getMinimumLoadableRetryCount(this.mediaPlaylistLoadable.type));
         MediaSourceEventListener.EventDispatcher var3 = DefaultHlsPlaylistTracker.this.eventDispatcher;
         ParsingLoadable var4 = this.mediaPlaylistLoadable;
         var3.loadStarted(var4.dataSpec, var4.type, var1);
      }

      private void processLoadedPlaylist(HlsMediaPlaylist var1, long var2) {
         HlsMediaPlaylist var4 = this.playlistSnapshot;
         long var5 = SystemClock.elapsedRealtime();
         this.lastSnapshotLoadMs = var5;
         this.playlistSnapshot = DefaultHlsPlaylistTracker.this.getLatestPlaylistSnapshot(var4, var1);
         HlsMediaPlaylist var7 = this.playlistSnapshot;
         if (var7 != var4) {
            this.playlistError = null;
            this.lastSnapshotChangeMs = var5;
            DefaultHlsPlaylistTracker.this.onPlaylistUpdated(this.playlistUrl, var7);
         } else if (!var7.hasEndTag) {
            long var8 = var1.mediaSequence;
            long var10 = (long)var1.segments.size();
            var1 = this.playlistSnapshot;
            if (var8 + var10 < var1.mediaSequence) {
               this.playlistError = new HlsPlaylistTracker.PlaylistResetException(this.playlistUrl.url);
               DefaultHlsPlaylistTracker.this.notifyPlaylistError(this.playlistUrl, -9223372036854775807L);
            } else {
               double var12 = (double)(var5 - this.lastSnapshotChangeMs);
               double var14 = (double)C.usToMs(var1.targetDurationUs);
               Double.isNaN(var14);
               if (var12 > var14 * 3.5D) {
                  this.playlistError = new HlsPlaylistTracker.PlaylistStuckException(this.playlistUrl.url);
                  var2 = DefaultHlsPlaylistTracker.this.loadErrorHandlingPolicy.getBlacklistDurationMsFor(4, var2, this.playlistError, 1);
                  DefaultHlsPlaylistTracker.this.notifyPlaylistError(this.playlistUrl, var2);
                  if (var2 != -9223372036854775807L) {
                     this.blacklistPlaylist(var2);
                  }
               }
            }
         }

         var1 = this.playlistSnapshot;
         if (var1 != var4) {
            var2 = var1.targetDurationUs;
         } else {
            var2 = var1.targetDurationUs / 2L;
         }

         this.earliestNextLoadTimeMs = var5 + C.usToMs(var2);
         if (this.playlistUrl == DefaultHlsPlaylistTracker.this.primaryHlsUrl && !this.playlistSnapshot.hasEndTag) {
            this.loadPlaylist();
         }

      }

      public HlsMediaPlaylist getPlaylistSnapshot() {
         return this.playlistSnapshot;
      }

      public boolean isSnapshotValid() {
         HlsMediaPlaylist var1 = this.playlistSnapshot;
         boolean var2 = false;
         if (var1 == null) {
            return false;
         } else {
            long var3 = SystemClock.elapsedRealtime();
            long var5 = Math.max(30000L, C.usToMs(this.playlistSnapshot.durationUs));
            var1 = this.playlistSnapshot;
            if (!var1.hasEndTag) {
               int var7 = var1.playlistType;
               if (var7 != 2 && var7 != 1 && this.lastSnapshotLoadMs + var5 <= var3) {
                  return var2;
               }
            }

            var2 = true;
            return var2;
         }
      }

      public void loadPlaylist() {
         this.blacklistUntilMs = 0L;
         if (!this.loadPending && !this.mediaPlaylistLoader.isLoading()) {
            long var1 = SystemClock.elapsedRealtime();
            if (var1 < this.earliestNextLoadTimeMs) {
               this.loadPending = true;
               DefaultHlsPlaylistTracker.this.playlistRefreshHandler.postDelayed(this, this.earliestNextLoadTimeMs - var1);
            } else {
               this.loadPlaylistImmediately();
            }
         }

      }

      public void maybeThrowPlaylistRefreshError() throws IOException {
         this.mediaPlaylistLoader.maybeThrowError();
         IOException var1 = this.playlistError;
         if (var1 != null) {
            throw var1;
         }
      }

      public void onLoadCanceled(ParsingLoadable var1, long var2, long var4, boolean var6) {
         DefaultHlsPlaylistTracker.this.eventDispatcher.loadCanceled(var1.dataSpec, var1.getUri(), var1.getResponseHeaders(), 4, var2, var4, var1.bytesLoaded());
      }

      public void onLoadCompleted(ParsingLoadable var1, long var2, long var4) {
         HlsPlaylist var6 = (HlsPlaylist)var1.getResult();
         if (var6 instanceof HlsMediaPlaylist) {
            this.processLoadedPlaylist((HlsMediaPlaylist)var6, var4);
            DefaultHlsPlaylistTracker.this.eventDispatcher.loadCompleted(var1.dataSpec, var1.getUri(), var1.getResponseHeaders(), 4, var2, var4, var1.bytesLoaded());
         } else {
            this.playlistError = new ParserException("Loaded playlist has unexpected type.");
         }

      }

      public Loader.LoadErrorAction onLoadError(ParsingLoadable var1, long var2, long var4, IOException var6, int var7) {
         long var8 = DefaultHlsPlaylistTracker.this.loadErrorHandlingPolicy.getBlacklistDurationMsFor(var1.type, var4, var6, var7);
         boolean var10;
         if (var8 != -9223372036854775807L) {
            var10 = true;
         } else {
            var10 = false;
         }

         boolean var11;
         if (!DefaultHlsPlaylistTracker.this.notifyPlaylistError(this.playlistUrl, var8) && var10) {
            var11 = false;
         } else {
            var11 = true;
         }

         boolean var12 = var11;
         if (var10) {
            var12 = var11 | this.blacklistPlaylist(var8);
         }

         Loader.LoadErrorAction var13;
         if (var12) {
            var8 = DefaultHlsPlaylistTracker.this.loadErrorHandlingPolicy.getRetryDelayMsFor(var1.type, var4, var6, var7);
            if (var8 != -9223372036854775807L) {
               var13 = Loader.createRetryAction(false, var8);
            } else {
               var13 = Loader.DONT_RETRY_FATAL;
            }
         } else {
            var13 = Loader.DONT_RETRY;
         }

         DefaultHlsPlaylistTracker.this.eventDispatcher.loadError(var1.dataSpec, var1.getUri(), var1.getResponseHeaders(), 4, var2, var4, var1.bytesLoaded(), var6, var13.isRetry() ^ true);
         return var13;
      }

      public void release() {
         this.mediaPlaylistLoader.release();
      }

      public void run() {
         this.loadPending = false;
         this.loadPlaylistImmediately();
      }
   }
}
