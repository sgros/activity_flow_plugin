package com.google.android.exoplayer2.source.dash;

import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.SparseArray;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.BaseMediaSource;
import com.google.android.exoplayer2.source.CompositeSequenceableLoaderFactory;
import com.google.android.exoplayer2.source.DefaultCompositeSequenceableLoaderFactory;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.dash.manifest.AdaptationSet;
import com.google.android.exoplayer2.source.dash.manifest.DashManifest;
import com.google.android.exoplayer2.source.dash.manifest.DashManifestParser;
import com.google.android.exoplayer2.source.dash.manifest.Period;
import com.google.android.exoplayer2.source.dash.manifest.Representation;
import com.google.android.exoplayer2.source.dash.manifest.UtcTimingElement;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultLoadErrorHandlingPolicy;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.upstream.LoaderErrorThrower;
import com.google.android.exoplayer2.upstream.ParsingLoadable;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DashMediaSource extends BaseMediaSource {
   private final DashChunkSource.Factory chunkSourceFactory;
   private final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
   private DataSource dataSource;
   private long elapsedRealtimeOffsetMs;
   private long expiredManifestPublishTimeUs;
   private int firstPeriodId;
   private Handler handler;
   private Uri initialManifestUri;
   private final long livePresentationDelayMs;
   private final boolean livePresentationDelayOverridesManifest;
   private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
   private Loader loader;
   private DashManifest manifest;
   private final DashMediaSource.ManifestCallback manifestCallback;
   private final DataSource.Factory manifestDataSourceFactory;
   private final MediaSourceEventListener.EventDispatcher manifestEventDispatcher;
   private IOException manifestFatalError;
   private long manifestLoadEndTimestampMs;
   private final LoaderErrorThrower manifestLoadErrorThrower;
   private boolean manifestLoadPending;
   private long manifestLoadStartTimestampMs;
   private final ParsingLoadable.Parser manifestParser;
   private Uri manifestUri;
   private final Object manifestUriLock;
   private TransferListener mediaTransferListener;
   private final SparseArray periodsById;
   private final PlayerEmsgHandler.PlayerEmsgCallback playerEmsgCallback;
   private final Runnable refreshManifestRunnable;
   private final boolean sideloadedManifest;
   private final Runnable simulateManifestRefreshRunnable;
   private int staleManifestReloadAttempt;
   private final Object tag;

   static {
      ExoPlayerLibraryInfo.registerModule("goog.exo.dash");
   }

   @Deprecated
   public DashMediaSource(Uri var1, DataSource.Factory var2, DashChunkSource.Factory var3, int var4, long var5, Handler var7, MediaSourceEventListener var8) {
      this(var1, var2, new DashManifestParser(), var3, var4, var5, var7, var8);
   }

   @Deprecated
   public DashMediaSource(Uri var1, DataSource.Factory var2, DashChunkSource.Factory var3, Handler var4, MediaSourceEventListener var5) {
      this(var1, var2, var3, 3, -1L, var4, var5);
   }

   @Deprecated
   public DashMediaSource(Uri var1, DataSource.Factory var2, ParsingLoadable.Parser var3, DashChunkSource.Factory var4, int var5, long var6, Handler var8, MediaSourceEventListener var9) {
      DefaultCompositeSequenceableLoaderFactory var10 = new DefaultCompositeSequenceableLoaderFactory();
      DefaultLoadErrorHandlingPolicy var11 = new DefaultLoadErrorHandlingPolicy(var5);
      long var12;
      if (var6 == -1L) {
         var12 = 30000L;
      } else {
         var12 = var6;
      }

      boolean var14;
      if (var6 != -1L) {
         var14 = true;
      } else {
         var14 = false;
      }

      this((DashManifest)null, var1, var2, var3, var4, var10, var11, var12, var14, (Object)null);
      if (var8 != null && var9 != null) {
         this.addEventListener(var8, var9);
      }

   }

   private DashMediaSource(DashManifest var1, Uri var2, DataSource.Factory var3, ParsingLoadable.Parser var4, DashChunkSource.Factory var5, CompositeSequenceableLoaderFactory var6, LoadErrorHandlingPolicy var7, long var8, boolean var10, Object var11) {
      this.initialManifestUri = var2;
      this.manifest = var1;
      this.manifestUri = var2;
      this.manifestDataSourceFactory = var3;
      this.manifestParser = var4;
      this.chunkSourceFactory = var5;
      this.loadErrorHandlingPolicy = var7;
      this.livePresentationDelayMs = var8;
      this.livePresentationDelayOverridesManifest = var10;
      this.compositeSequenceableLoaderFactory = var6;
      this.tag = var11;
      if (var1 != null) {
         var10 = true;
      } else {
         var10 = false;
      }

      this.sideloadedManifest = var10;
      this.manifestEventDispatcher = this.createEventDispatcher((MediaSource.MediaPeriodId)null);
      this.manifestUriLock = new Object();
      this.periodsById = new SparseArray();
      this.playerEmsgCallback = new DashMediaSource.DefaultPlayerEmsgCallback();
      this.expiredManifestPublishTimeUs = -9223372036854775807L;
      if (this.sideloadedManifest) {
         Assertions.checkState(var1.dynamic ^ true);
         this.manifestCallback = null;
         this.refreshManifestRunnable = null;
         this.simulateManifestRefreshRunnable = null;
         this.manifestLoadErrorThrower = new LoaderErrorThrower.Dummy();
      } else {
         this.manifestCallback = new DashMediaSource.ManifestCallback();
         this.manifestLoadErrorThrower = new DashMediaSource.ManifestLoadErrorThrower();
         this.refreshManifestRunnable = new _$$Lambda$DashMediaSource$QbzYvqCY1TT8f0KClkalovG_Oxc(this);
         this.simulateManifestRefreshRunnable = new _$$Lambda$DashMediaSource$e1nzB_O4m3YSG1BkxQDKPaNvDa8(this);
      }

   }

   private long getManifestLoadRetryDelayMillis() {
      return (long)Math.min((this.staleManifestReloadAttempt - 1) * 1000, 5000);
   }

   private long getNowUnixTimeUs() {
      return this.elapsedRealtimeOffsetMs != 0L ? C.msToUs(SystemClock.elapsedRealtime() + this.elapsedRealtimeOffsetMs) : C.msToUs(System.currentTimeMillis());
   }

   // $FF: synthetic method
   public static void lambda$QbzYvqCY1TT8f0KClkalovG_Oxc/* $FF was: lambda$QbzYvqCY1TT8f0KClkalovG-Oxc*/(DashMediaSource var0) {
      var0.startLoadingManifest();
   }

   private void onUtcTimestampResolutionError(IOException var1) {
      Log.e("DashMediaSource", "Failed to resolve UtcTiming element.", var1);
      this.processManifest(true);
   }

   private void onUtcTimestampResolved(long var1) {
      this.elapsedRealtimeOffsetMs = var1;
      this.processManifest(true);
   }

   private void processManifest(boolean var1) {
      int var2;
      int var3;
      for(var2 = 0; var2 < this.periodsById.size(); ++var2) {
         var3 = this.periodsById.keyAt(var2);
         if (var3 >= this.firstPeriodId) {
            ((DashMediaPeriod)this.periodsById.valueAt(var2)).updateManifest(this.manifest, var3 - this.firstPeriodId);
         }
      }

      var2 = this.manifest.getPeriodCount() - 1;
      DashMediaSource.PeriodSeekInfo var4 = DashMediaSource.PeriodSeekInfo.createPeriodSeekInfo(this.manifest.getPeriod(0), this.manifest.getPeriodDurationUs(0));
      DashMediaSource.PeriodSeekInfo var5 = DashMediaSource.PeriodSeekInfo.createPeriodSeekInfo(this.manifest.getPeriod(var2), this.manifest.getPeriodDurationUs(var2));
      long var6 = var4.availableStartTimeUs;
      long var8 = var5.availableEndTimeUs;
      long var10;
      long var12;
      boolean var18;
      DashManifest var19;
      if (this.manifest.dynamic && !var5.isIndexExplicit) {
         var10 = Math.min(this.getNowUnixTimeUs() - C.msToUs(this.manifest.availabilityStartTimeMs) - C.msToUs(this.manifest.getPeriod(var2).startMs), var8);
         var12 = this.manifest.timeShiftBufferDepthMs;
         var8 = var6;
         if (var12 != -9223372036854775807L) {
            for(var8 = var10 - C.msToUs(var12); var8 < 0L && var2 > 0; var8 += var19.getPeriodDurationUs(var2)) {
               var19 = this.manifest;
               --var2;
            }

            if (var2 == 0) {
               var8 = Math.max(var6, var8);
            } else {
               var8 = this.manifest.getPeriodDurationUs(0);
            }
         }

         var6 = var8;
         var18 = true;
         var8 = var10;
      } else {
         var18 = false;
      }

      var10 = var8 - var6;

      for(var3 = 0; var3 < this.manifest.getPeriodCount() - 1; ++var3) {
         var10 += this.manifest.getPeriodDurationUs(var3);
      }

      var19 = this.manifest;
      long var14;
      if (var19.dynamic) {
         var12 = this.livePresentationDelayMs;
         var8 = var12;
         if (!this.livePresentationDelayOverridesManifest) {
            var14 = var19.suggestedPresentationDelayMs;
            var8 = var12;
            if (var14 != -9223372036854775807L) {
               var8 = var14;
            }
         }

         var12 = var10 - C.msToUs(var8);
         var8 = var12;
         if (var12 < 5000000L) {
            var8 = Math.min(5000000L, var10 / 2L);
         }
      } else {
         var8 = 0L;
      }

      var19 = this.manifest;
      long var16 = var19.availabilityStartTimeMs;
      var14 = var19.getPeriod(0).startMs;
      var12 = C.usToMs(var6);
      var19 = this.manifest;
      this.refreshSourceInfo(new DashMediaSource.DashTimeline(var19.availabilityStartTimeMs, var16 + var14 + var12, this.firstPeriodId, var6, var10, var8, var19, this.tag), this.manifest);
      if (!this.sideloadedManifest) {
         this.handler.removeCallbacks(this.simulateManifestRefreshRunnable);
         if (var18) {
            this.handler.postDelayed(this.simulateManifestRefreshRunnable, 5000L);
         }

         if (this.manifestLoadPending) {
            this.startLoadingManifest();
         } else if (var1) {
            var19 = this.manifest;
            if (var19.dynamic) {
               var6 = var19.minUpdatePeriodMs;
               if (var6 != -9223372036854775807L) {
                  var8 = var6;
                  if (var6 == 0L) {
                     var8 = 5000L;
                  }

                  this.scheduleManifestRefresh(Math.max(0L, this.manifestLoadStartTimestampMs + var8 - SystemClock.elapsedRealtime()));
               }
            }
         }
      }

   }

   private void resolveUtcTimingElement(UtcTimingElement var1) {
      String var2 = var1.schemeIdUri;
      if (!Util.areEqual(var2, "urn:mpeg:dash:utc:direct:2014") && !Util.areEqual(var2, "urn:mpeg:dash:utc:direct:2012")) {
         if (!Util.areEqual(var2, "urn:mpeg:dash:utc:http-iso:2014") && !Util.areEqual(var2, "urn:mpeg:dash:utc:http-iso:2012")) {
            if (!Util.areEqual(var2, "urn:mpeg:dash:utc:http-xsdate:2014") && !Util.areEqual(var2, "urn:mpeg:dash:utc:http-xsdate:2012")) {
               this.onUtcTimestampResolutionError(new IOException("Unsupported UTC timing scheme"));
            } else {
               this.resolveUtcTimingElementHttp(var1, new DashMediaSource.XsDateTimeParser());
            }
         } else {
            this.resolveUtcTimingElementHttp(var1, new DashMediaSource.Iso8601Parser());
         }
      } else {
         this.resolveUtcTimingElementDirect(var1);
      }

   }

   private void resolveUtcTimingElementDirect(UtcTimingElement var1) {
      try {
         this.onUtcTimestampResolved(Util.parseXsDateTime(var1.value) - this.manifestLoadEndTimestampMs);
      } catch (ParserException var2) {
         this.onUtcTimestampResolutionError(var2);
      }

   }

   private void resolveUtcTimingElementHttp(UtcTimingElement var1, ParsingLoadable.Parser var2) {
      this.startLoading(new ParsingLoadable(this.dataSource, Uri.parse(var1.value), 5, var2), new DashMediaSource.UtcTimestampCallback(), 1);
   }

   private void scheduleManifestRefresh(long var1) {
      this.handler.postDelayed(this.refreshManifestRunnable, var1);
   }

   private void startLoading(ParsingLoadable var1, Loader.Callback var2, int var3) {
      long var4 = this.loader.startLoading(var1, var2, var3);
      this.manifestEventDispatcher.loadStarted(var1.dataSpec, var1.type, var4);
   }

   private void startLoadingManifest() {
      // $FF: Couldn't be decompiled
   }

   public MediaPeriod createPeriod(MediaSource.MediaPeriodId var1, Allocator var2, long var3) {
      int var5 = (Integer)var1.periodUid - this.firstPeriodId;
      MediaSourceEventListener.EventDispatcher var6 = this.createEventDispatcher(var1, this.manifest.getPeriod(var5).startMs);
      DashMediaPeriod var7 = new DashMediaPeriod(this.firstPeriodId + var5, this.manifest, var5, this.chunkSourceFactory, this.mediaTransferListener, this.loadErrorHandlingPolicy, var6, this.elapsedRealtimeOffsetMs, this.manifestLoadErrorThrower, var2, this.compositeSequenceableLoaderFactory, this.playerEmsgCallback);
      this.periodsById.put(var7.id, var7);
      return var7;
   }

   // $FF: synthetic method
   public void lambda$new$0$DashMediaSource() {
      this.processManifest(false);
   }

   public void maybeThrowSourceInfoRefreshError() throws IOException {
      this.manifestLoadErrorThrower.maybeThrowError();
   }

   void onDashManifestPublishTimeExpired(long var1) {
      long var3 = this.expiredManifestPublishTimeUs;
      if (var3 == -9223372036854775807L || var3 < var1) {
         this.expiredManifestPublishTimeUs = var1;
      }

   }

   void onDashManifestRefreshRequested() {
      this.handler.removeCallbacks(this.simulateManifestRefreshRunnable);
      this.startLoadingManifest();
   }

   void onLoadCanceled(ParsingLoadable var1, long var2, long var4) {
      this.manifestEventDispatcher.loadCanceled(var1.dataSpec, var1.getUri(), var1.getResponseHeaders(), var1.type, var2, var4, var1.bytesLoaded());
   }

   void onManifestLoadCompleted(ParsingLoadable var1, long var2, long var4) {
      this.manifestEventDispatcher.loadCompleted(var1.dataSpec, var1.getUri(), var1.getResponseHeaders(), var1.type, var2, var4, var1.bytesLoaded());
      DashManifest var6 = (DashManifest)var1.getResult();
      DashManifest var7 = this.manifest;
      boolean var8 = false;
      int var9;
      if (var7 == null) {
         var9 = 0;
      } else {
         var9 = var7.getPeriodCount();
      }

      long var10 = var6.getPeriod(0).startMs;

      int var12;
      for(var12 = 0; var12 < var9 && this.manifest.getPeriod(var12).startMs < var10; ++var12) {
      }

      boolean var13;
      if (var6.dynamic) {
         label540: {
            if (var9 - var12 > var6.getPeriodCount()) {
               Log.w("DashMediaSource", "Loaded out of sync manifest");
            } else {
               var10 = this.expiredManifestPublishTimeUs;
               if (var10 == -9223372036854775807L || var6.publishTimeMs * 1000L > var10) {
                  var13 = false;
                  break label540;
               }

               StringBuilder var39 = new StringBuilder();
               var39.append("Loaded stale dynamic manifest: ");
               var39.append(var6.publishTimeMs);
               var39.append(", ");
               var39.append(this.expiredManifestPublishTimeUs);
               Log.w("DashMediaSource", var39.toString());
            }

            var13 = true;
         }

         if (var13) {
            var9 = this.staleManifestReloadAttempt++;
            if (var9 < this.loadErrorHandlingPolicy.getMinimumLoadableRetryCount(var1.type)) {
               this.scheduleManifestRefresh(this.getManifestLoadRetryDelayMillis());
            } else {
               this.manifestFatalError = new DashManifestStaleException();
            }

            return;
         }

         this.staleManifestReloadAttempt = 0;
      }

      this.manifest = var6;
      boolean var14 = this.manifestLoadPending;
      var6 = this.manifest;
      this.manifestLoadPending = var14 & var6.dynamic;
      this.manifestLoadStartTimestampMs = var2 - var4;
      this.manifestLoadEndTimestampMs = var2;
      if (var6.location != null) {
         label538: {
            Object var38 = this.manifestUriLock;
            synchronized(var38){}
            var13 = var8;

            Throwable var10000;
            boolean var10001;
            label536: {
               label507: {
                  try {
                     if (var1.dataSpec.uri != this.manifestUri) {
                        break label507;
                     }
                  } catch (Throwable var34) {
                     var10000 = var34;
                     var10001 = false;
                     break label536;
                  }

                  var13 = true;
               }

               if (var13) {
                  try {
                     this.manifestUri = this.manifest.location;
                  } catch (Throwable var33) {
                     var10000 = var33;
                     var10001 = false;
                     break label536;
                  }
               }

               label498:
               try {
                  break label538;
               } catch (Throwable var32) {
                  var10000 = var32;
                  var10001 = false;
                  break label498;
               }
            }

            while(true) {
               Throwable var35 = var10000;

               try {
                  throw var35;
               } catch (Throwable var31) {
                  var10000 = var31;
                  var10001 = false;
                  continue;
               }
            }
         }
      }

      if (var9 == 0) {
         DashManifest var36 = this.manifest;
         if (var36.dynamic) {
            UtcTimingElement var37 = var36.utcTiming;
            if (var37 != null) {
               this.resolveUtcTimingElement(var37);
               return;
            }
         }

         this.processManifest(true);
      } else {
         this.firstPeriodId += var12;
         this.processManifest(true);
      }

   }

   Loader.LoadErrorAction onManifestLoadError(ParsingLoadable var1, long var2, long var4, IOException var6) {
      boolean var7 = var6 instanceof ParserException;
      this.manifestEventDispatcher.loadError(var1.dataSpec, var1.getUri(), var1.getResponseHeaders(), var1.type, var2, var4, var1.bytesLoaded(), var6, var7);
      Loader.LoadErrorAction var8;
      if (var7) {
         var8 = Loader.DONT_RETRY_FATAL;
      } else {
         var8 = Loader.RETRY;
      }

      return var8;
   }

   void onUtcTimestampLoadCompleted(ParsingLoadable var1, long var2, long var4) {
      this.manifestEventDispatcher.loadCompleted(var1.dataSpec, var1.getUri(), var1.getResponseHeaders(), var1.type, var2, var4, var1.bytesLoaded());
      this.onUtcTimestampResolved((Long)var1.getResult() - var2);
   }

   Loader.LoadErrorAction onUtcTimestampLoadError(ParsingLoadable var1, long var2, long var4, IOException var6) {
      this.manifestEventDispatcher.loadError(var1.dataSpec, var1.getUri(), var1.getResponseHeaders(), var1.type, var2, var4, var1.bytesLoaded(), var6, true);
      this.onUtcTimestampResolutionError(var6);
      return Loader.DONT_RETRY;
   }

   public void prepareSourceInternal(TransferListener var1) {
      this.mediaTransferListener = var1;
      if (this.sideloadedManifest) {
         this.processManifest(false);
      } else {
         this.dataSource = this.manifestDataSourceFactory.createDataSource();
         this.loader = new Loader("Loader:DashMediaSource");
         this.handler = new Handler();
         this.startLoadingManifest();
      }

   }

   public void releasePeriod(MediaPeriod var1) {
      DashMediaPeriod var2 = (DashMediaPeriod)var1;
      var2.release();
      this.periodsById.remove(var2.id);
   }

   public void releaseSourceInternal() {
      this.manifestLoadPending = false;
      this.dataSource = null;
      Loader var1 = this.loader;
      if (var1 != null) {
         var1.release();
         this.loader = null;
      }

      this.manifestLoadStartTimestampMs = 0L;
      this.manifestLoadEndTimestampMs = 0L;
      DashManifest var2;
      if (this.sideloadedManifest) {
         var2 = this.manifest;
      } else {
         var2 = null;
      }

      this.manifest = var2;
      this.manifestUri = this.initialManifestUri;
      this.manifestFatalError = null;
      Handler var3 = this.handler;
      if (var3 != null) {
         var3.removeCallbacksAndMessages((Object)null);
         this.handler = null;
      }

      this.elapsedRealtimeOffsetMs = 0L;
      this.staleManifestReloadAttempt = 0;
      this.expiredManifestPublishTimeUs = -9223372036854775807L;
      this.firstPeriodId = 0;
      this.periodsById.clear();
   }

   private static final class DashTimeline extends Timeline {
      private final int firstPeriodId;
      private final DashManifest manifest;
      private final long offsetInFirstPeriodUs;
      private final long presentationStartTimeMs;
      private final long windowDefaultStartPositionUs;
      private final long windowDurationUs;
      private final long windowStartTimeMs;
      private final Object windowTag;

      public DashTimeline(long var1, long var3, int var5, long var6, long var8, long var10, DashManifest var12, Object var13) {
         this.presentationStartTimeMs = var1;
         this.windowStartTimeMs = var3;
         this.firstPeriodId = var5;
         this.offsetInFirstPeriodUs = var6;
         this.windowDurationUs = var8;
         this.windowDefaultStartPositionUs = var10;
         this.manifest = var12;
         this.windowTag = var13;
      }

      private long getAdjustedWindowDefaultStartPositionUs(long var1) {
         long var3 = this.windowDefaultStartPositionUs;
         if (!this.manifest.dynamic) {
            return var3;
         } else {
            long var5 = var3;
            if (var1 > 0L) {
               var1 += var3;
               var5 = var1;
               if (var1 > this.windowDurationUs) {
                  return -9223372036854775807L;
               }
            }

            var3 = this.offsetInFirstPeriodUs;
            var1 = this.manifest.getPeriodDurationUs(0);
            var3 += var5;

            int var7;
            for(var7 = 0; var7 < this.manifest.getPeriodCount() - 1 && var3 >= var1; var1 = this.manifest.getPeriodDurationUs(var7)) {
               var3 -= var1;
               ++var7;
            }

            Period var8 = this.manifest.getPeriod(var7);
            var7 = var8.getAdaptationSetIndex(2);
            if (var7 == -1) {
               return var5;
            } else {
               DashSegmentIndex var11 = ((Representation)((AdaptationSet)var8.adaptationSets.get(var7)).representations.get(0)).getIndex();
               long var9 = var5;
               if (var11 != null) {
                  if (var11.getSegmentCount(var1) == 0) {
                     var9 = var5;
                  } else {
                     var9 = var5 + var11.getTimeUs(var11.getSegmentNum(var3, var1)) - var3;
                  }
               }

               return var9;
            }
         }
      }

      public int getIndexOfPeriod(Object var1) {
         if (!(var1 instanceof Integer)) {
            return -1;
         } else {
            int var2 = (Integer)var1 - this.firstPeriodId;
            int var3;
            if (var2 >= 0) {
               var3 = var2;
               if (var2 < this.getPeriodCount()) {
                  return var3;
               }
            }

            var3 = -1;
            return var3;
         }
      }

      public Timeline.Period getPeriod(int var1, Timeline.Period var2, boolean var3) {
         Assertions.checkIndex(var1, 0, this.getPeriodCount());
         Integer var4 = null;
         String var5;
         if (var3) {
            var5 = this.manifest.getPeriod(var1).id;
         } else {
            var5 = null;
         }

         if (var3) {
            var4 = this.firstPeriodId + var1;
         }

         var2.set(var5, var4, 0, this.manifest.getPeriodDurationUs(var1), C.msToUs(this.manifest.getPeriod(var1).startMs - this.manifest.getPeriod(0).startMs) - this.offsetInFirstPeriodUs);
         return var2;
      }

      public int getPeriodCount() {
         return this.manifest.getPeriodCount();
      }

      public Object getUidOfPeriod(int var1) {
         Assertions.checkIndex(var1, 0, this.getPeriodCount());
         return this.firstPeriodId + var1;
      }

      public Timeline.Window getWindow(int var1, Timeline.Window var2, boolean var3, long var4) {
         Assertions.checkIndex(var1, 0, 1);
         var4 = this.getAdjustedWindowDefaultStartPositionUs(var4);
         Object var6;
         if (var3) {
            var6 = this.windowTag;
         } else {
            var6 = null;
         }

         DashManifest var7 = this.manifest;
         if (var7.dynamic && var7.minUpdatePeriodMs != -9223372036854775807L && var7.durationMs == -9223372036854775807L) {
            var3 = true;
         } else {
            var3 = false;
         }

         var2.set(var6, this.presentationStartTimeMs, this.windowStartTimeMs, true, var3, var4, this.windowDurationUs, 0, this.getPeriodCount() - 1, this.offsetInFirstPeriodUs);
         return var2;
      }

      public int getWindowCount() {
         return 1;
      }
   }

   private final class DefaultPlayerEmsgCallback implements PlayerEmsgHandler.PlayerEmsgCallback {
      private DefaultPlayerEmsgCallback() {
      }

      // $FF: synthetic method
      DefaultPlayerEmsgCallback(Object var2) {
         this();
      }

      public void onDashManifestPublishTimeExpired(long var1) {
         DashMediaSource.this.onDashManifestPublishTimeExpired(var1);
      }

      public void onDashManifestRefreshRequested() {
         DashMediaSource.this.onDashManifestRefreshRequested();
      }
   }

   static final class Iso8601Parser implements ParsingLoadable.Parser {
      private static final Pattern TIMESTAMP_WITH_TIMEZONE_PATTERN = Pattern.compile("(.+?)(Z|((\\+|-|−)(\\d\\d)(:?(\\d\\d))?))");

      public Long parse(Uri var1, InputStream var2) throws IOException {
         String var21 = (new BufferedReader(new InputStreamReader(var2, Charset.forName("UTF-8")))).readLine();

         ParseException var10000;
         label62: {
            long var6;
            boolean var10001;
            label61: {
               label66: {
                  long var4;
                  Matcher var18;
                  label59: {
                     try {
                        var18 = TIMESTAMP_WITH_TIMEZONE_PATTERN.matcher(var21);
                        if (!var18.matches()) {
                           break label66;
                        }

                        String var3 = var18.group(1);
                        SimpleDateFormat var23 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
                        var23.setTimeZone(TimeZone.getTimeZone("UTC"));
                        var4 = var23.parse(var3).getTime();
                        if (!"Z".equals(var18.group(2))) {
                           break label59;
                        }
                     } catch (ParseException var17) {
                        var10000 = var17;
                        var10001 = false;
                        break label62;
                     }

                     var6 = var4;
                     break label61;
                  }

                  label50: {
                     label49: {
                        try {
                           if ("+".equals(var18.group(4))) {
                              break label49;
                           }
                        } catch (ParseException var16) {
                           var10000 = var16;
                           var10001 = false;
                           break label62;
                        }

                        var6 = -1L;
                        break label50;
                     }

                     var6 = 1L;
                  }

                  long var8;
                  long var10;
                  label43: {
                     String var19;
                     label42: {
                        try {
                           var8 = Long.parseLong(var18.group(5));
                           var19 = var18.group(7);
                           if (!TextUtils.isEmpty(var19)) {
                              break label42;
                           }
                        } catch (ParseException var15) {
                           var10000 = var15;
                           var10001 = false;
                           break label62;
                        }

                        var10 = 0L;
                        break label43;
                     }

                     try {
                        var10 = Long.parseLong(var19);
                     } catch (ParseException var14) {
                        var10000 = var14;
                        var10001 = false;
                        break label62;
                     }
                  }

                  var6 = var4 - var6 * (var8 * 60L + var10) * 60L * 1000L;
                  break label61;
               }

               try {
                  StringBuilder var24 = new StringBuilder();
                  var24.append("Couldn't parse timestamp: ");
                  var24.append(var21);
                  ParserException var22 = new ParserException(var24.toString());
                  throw var22;
               } catch (ParseException var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label62;
               }
            }

            try {
               return var6;
            } catch (ParseException var12) {
               var10000 = var12;
               var10001 = false;
            }
         }

         ParseException var20 = var10000;
         throw new ParserException(var20);
      }
   }

   private final class ManifestCallback implements Loader.Callback {
      private ManifestCallback() {
      }

      // $FF: synthetic method
      ManifestCallback(Object var2) {
         this();
      }

      public void onLoadCanceled(ParsingLoadable var1, long var2, long var4, boolean var6) {
         DashMediaSource.this.onLoadCanceled(var1, var2, var4);
      }

      public void onLoadCompleted(ParsingLoadable var1, long var2, long var4) {
         DashMediaSource.this.onManifestLoadCompleted(var1, var2, var4);
      }

      public Loader.LoadErrorAction onLoadError(ParsingLoadable var1, long var2, long var4, IOException var6, int var7) {
         return DashMediaSource.this.onManifestLoadError(var1, var2, var4, var6);
      }
   }

   final class ManifestLoadErrorThrower implements LoaderErrorThrower {
      private void maybeThrowManifestError() throws IOException {
         if (DashMediaSource.this.manifestFatalError != null) {
            throw DashMediaSource.this.manifestFatalError;
         }
      }

      public void maybeThrowError() throws IOException {
         DashMediaSource.this.loader.maybeThrowError();
         this.maybeThrowManifestError();
      }
   }

   private static final class PeriodSeekInfo {
      public final long availableEndTimeUs;
      public final long availableStartTimeUs;
      public final boolean isIndexExplicit;

      private PeriodSeekInfo(boolean var1, long var2, long var4) {
         this.isIndexExplicit = var1;
         this.availableStartTimeUs = var2;
         this.availableEndTimeUs = var4;
      }

      public static DashMediaSource.PeriodSeekInfo createPeriodSeekInfo(Period var0, long var1) {
         Period var3 = var0;
         int var4 = var0.adaptationSets.size();
         int var5 = 0;

         int var6;
         boolean var18;
         while(true) {
            if (var5 < var4) {
               var6 = ((AdaptationSet)var3.adaptationSets.get(var5)).type;
               if (var6 != 1 && var6 != 2) {
                  ++var5;
                  continue;
               }

               var18 = true;
               break;
            }

            var18 = false;
            break;
         }

         long var7 = Long.MAX_VALUE;
         var6 = 0;
         boolean var9 = false;
         boolean var10 = false;

         long var11;
         for(var11 = 0L; var6 < var4; ++var6) {
            AdaptationSet var16 = (AdaptationSet)var0.adaptationSets.get(var6);
            if (!var18 || var16.type != 3) {
               DashSegmentIndex var17 = ((Representation)var16.representations.get(0)).getIndex();
               if (var17 == null) {
                  return new DashMediaSource.PeriodSeekInfo(true, 0L, var1);
               }

               var10 |= var17.isExplicit();
               int var13 = var17.getSegmentCount(var1);
               if (var13 == 0) {
                  var9 = true;
                  var11 = 0L;
                  var7 = 0L;
               } else if (!var9) {
                  long var14 = var17.getFirstSegmentNum();
                  var11 = Math.max(var11, var17.getTimeUs(var14));
                  if (var13 != -1) {
                     var14 = var14 + (long)var13 - 1L;
                     var7 = Math.min(var7, var17.getTimeUs(var14) + var17.getDurationUs(var14, var1));
                  }
               }
            }
         }

         return new DashMediaSource.PeriodSeekInfo(var10, var11, var7);
      }
   }

   private final class UtcTimestampCallback implements Loader.Callback {
      private UtcTimestampCallback() {
      }

      // $FF: synthetic method
      UtcTimestampCallback(Object var2) {
         this();
      }

      public void onLoadCanceled(ParsingLoadable var1, long var2, long var4, boolean var6) {
         DashMediaSource.this.onLoadCanceled(var1, var2, var4);
      }

      public void onLoadCompleted(ParsingLoadable var1, long var2, long var4) {
         DashMediaSource.this.onUtcTimestampLoadCompleted(var1, var2, var4);
      }

      public Loader.LoadErrorAction onLoadError(ParsingLoadable var1, long var2, long var4, IOException var6, int var7) {
         return DashMediaSource.this.onUtcTimestampLoadError(var1, var2, var4, var6);
      }
   }

   private static final class XsDateTimeParser implements ParsingLoadable.Parser {
      private XsDateTimeParser() {
      }

      // $FF: synthetic method
      XsDateTimeParser(Object var1) {
         this();
      }

      public Long parse(Uri var1, InputStream var2) throws IOException {
         return Util.parseXsDateTime((new BufferedReader(new InputStreamReader(var2))).readLine());
      }
   }
}
