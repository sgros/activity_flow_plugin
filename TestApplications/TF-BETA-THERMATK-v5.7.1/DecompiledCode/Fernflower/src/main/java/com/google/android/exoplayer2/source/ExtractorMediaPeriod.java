package com.google.android.exoplayer2.source;

import android.net.Uri;
import android.os.Handler;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.extractor.DefaultExtractorInput;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.icy.IcyHeaders;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.upstream.StatsDataSource;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.ConditionVariable;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;

final class ExtractorMediaPeriod implements MediaPeriod, ExtractorOutput, Loader.Callback, Loader.ReleaseCallback, SampleQueue.UpstreamFormatChangedListener {
   private static final Format ICY_FORMAT = Format.createSampleFormat("icy", "application/x-icy", Long.MAX_VALUE);
   private final Allocator allocator;
   private MediaPeriod.Callback callback;
   private final long continueLoadingCheckIntervalBytes;
   private final String customCacheKey;
   private final DataSource dataSource;
   private int dataType;
   private long durationUs;
   private int enabledTrackCount;
   private final MediaSourceEventListener.EventDispatcher eventDispatcher;
   private int extractedSamplesCountAtStartOfLoad;
   private final ExtractorMediaPeriod.ExtractorHolder extractorHolder;
   private final Handler handler;
   private boolean haveAudioVideoTracks;
   private IcyHeaders icyHeaders;
   private long lastSeekPositionUs;
   private long length;
   private final ExtractorMediaPeriod.Listener listener;
   private final ConditionVariable loadCondition;
   private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
   private final Loader loader;
   private boolean loadingFinished;
   private final Runnable maybeFinishPrepareRunnable;
   private boolean notifiedReadingStarted;
   private boolean notifyDiscontinuity;
   private final Runnable onContinueLoadingRequestedRunnable;
   private boolean pendingDeferredRetry;
   private long pendingResetPositionUs;
   private boolean prepared;
   private ExtractorMediaPeriod.PreparedState preparedState;
   private boolean released;
   private ExtractorMediaPeriod.TrackId[] sampleQueueTrackIds;
   private SampleQueue[] sampleQueues;
   private boolean sampleQueuesBuilt;
   private SeekMap seekMap;
   private boolean seenFirstTrackSelection;
   private final Uri uri;

   public ExtractorMediaPeriod(Uri var1, DataSource var2, Extractor[] var3, LoadErrorHandlingPolicy var4, MediaSourceEventListener.EventDispatcher var5, ExtractorMediaPeriod.Listener var6, Allocator var7, String var8, int var9) {
      this.uri = var1;
      this.dataSource = var2;
      this.loadErrorHandlingPolicy = var4;
      this.eventDispatcher = var5;
      this.listener = var6;
      this.allocator = var7;
      this.customCacheKey = var8;
      this.continueLoadingCheckIntervalBytes = (long)var9;
      this.loader = new Loader("Loader:ExtractorMediaPeriod");
      this.extractorHolder = new ExtractorMediaPeriod.ExtractorHolder(var3);
      this.loadCondition = new ConditionVariable();
      this.maybeFinishPrepareRunnable = new _$$Lambda$ExtractorMediaPeriod$Ll7lI30pD07GZk92Lo8XgkQMAAY(this);
      this.onContinueLoadingRequestedRunnable = new _$$Lambda$ExtractorMediaPeriod$Hd_sBytb6cpkhM49l8dYCND3wmk(this);
      this.handler = new Handler();
      this.sampleQueueTrackIds = new ExtractorMediaPeriod.TrackId[0];
      this.sampleQueues = new SampleQueue[0];
      this.pendingResetPositionUs = -9223372036854775807L;
      this.length = -1L;
      this.durationUs = -9223372036854775807L;
      this.dataType = 1;
      var5.mediaPeriodCreated();
   }

   private boolean configureRetry(ExtractorMediaPeriod.ExtractingLoadable var1, int var2) {
      if (this.length == -1L) {
         SeekMap var3 = this.seekMap;
         if (var3 == null || var3.getDurationUs() == -9223372036854775807L) {
            boolean var4 = this.prepared;
            var2 = 0;
            if (var4 && !this.suppressRead()) {
               this.pendingDeferredRetry = true;
               return false;
            } else {
               this.notifyDiscontinuity = this.prepared;
               this.lastSeekPositionUs = 0L;
               this.extractedSamplesCountAtStartOfLoad = 0;
               SampleQueue[] var6 = this.sampleQueues;

               for(int var5 = var6.length; var2 < var5; ++var2) {
                  var6[var2].reset();
               }

               var1.setLoadPosition(0L, 0L);
               return true;
            }
         }
      }

      this.extractedSamplesCountAtStartOfLoad = var2;
      return true;
   }

   private void copyLengthFromLoader(ExtractorMediaPeriod.ExtractingLoadable var1) {
      if (this.length == -1L) {
         this.length = var1.length;
      }

   }

   private int getExtractedSamplesCount() {
      SampleQueue[] var1 = this.sampleQueues;
      int var2 = var1.length;
      int var3 = 0;

      int var4;
      for(var4 = 0; var3 < var2; ++var3) {
         var4 += var1[var3].getWriteIndex();
      }

      return var4;
   }

   private long getLargestQueuedTimestampUs() {
      SampleQueue[] var1 = this.sampleQueues;
      int var2 = var1.length;
      long var3 = Long.MIN_VALUE;

      for(int var5 = 0; var5 < var2; ++var5) {
         var3 = Math.max(var3, var1[var5].getLargestQueuedTimestampUs());
      }

      return var3;
   }

   private ExtractorMediaPeriod.PreparedState getPreparedState() {
      ExtractorMediaPeriod.PreparedState var1 = this.preparedState;
      Assertions.checkNotNull(var1);
      return (ExtractorMediaPeriod.PreparedState)var1;
   }

   private boolean isPendingReset() {
      boolean var1;
      if (this.pendingResetPositionUs != -9223372036854775807L) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   // $FF: synthetic method
   public static void lambda$Ll7lI30pD07GZk92Lo8XgkQMAAY(ExtractorMediaPeriod var0) {
      var0.maybeFinishPrepare();
   }

   private void maybeFinishPrepare() {
      SeekMap var1 = this.seekMap;
      if (!this.released && !this.prepared && this.sampleQueuesBuilt && var1 != null) {
         SampleQueue[] var2 = this.sampleQueues;
         int var3 = var2.length;

         int var4;
         for(var4 = 0; var4 < var3; ++var4) {
            if (var2[var4].getUpstreamFormat() == null) {
               return;
            }
         }

         this.loadCondition.close();
         var3 = this.sampleQueues.length;
         TrackGroup[] var5 = new TrackGroup[var3];
         boolean[] var6 = new boolean[var3];
         this.durationUs = var1.getDurationUs();

         for(var4 = 0; var4 < var3; ++var4) {
            Format var7 = this.sampleQueues[var4].getUpstreamFormat();
            String var13 = var7.sampleMimeType;
            boolean var8 = MimeTypes.isAudio(var13);
            boolean var9;
            if (!var8 && !MimeTypes.isVideo(var13)) {
               var9 = false;
            } else {
               var9 = true;
            }

            var6[var4] = var9;
            this.haveAudioVideoTracks |= var9;
            IcyHeaders var10 = this.icyHeaders;
            Format var14 = var7;
            if (var10 != null) {
               Format var11;
               label85: {
                  if (!var8) {
                     var11 = var7;
                     if (!this.sampleQueueTrackIds[var4].isIcyTrack) {
                        break label85;
                     }
                  }

                  Metadata var15 = var7.metadata;
                  if (var15 == null) {
                     var15 = new Metadata(new Metadata.Entry[]{var10});
                  } else {
                     var15 = var15.copyWithAppendedEntries(var10);
                  }

                  var11 = var7.copyWithMetadata(var15);
               }

               var14 = var11;
               if (var8) {
                  var14 = var11;
                  if (var11.bitrate == -1) {
                     int var12 = var10.bitrate;
                     var14 = var11;
                     if (var12 != -1) {
                        var14 = var11.copyWithBitrate(var12);
                     }
                  }
               }
            }

            var5[var4] = new TrackGroup(new Format[]{var14});
         }

         byte var16;
         if (this.length == -1L && var1.getDurationUs() == -9223372036854775807L) {
            var16 = 7;
         } else {
            var16 = 1;
         }

         this.dataType = var16;
         this.preparedState = new ExtractorMediaPeriod.PreparedState(var1, new TrackGroupArray(var5), var6);
         this.prepared = true;
         this.listener.onSourceInfoRefreshed(this.durationUs, var1.isSeekable());
         MediaPeriod.Callback var17 = this.callback;
         Assertions.checkNotNull(var17);
         ((MediaPeriod.Callback)var17).onPrepared(this);
      }

   }

   private void maybeNotifyDownstreamFormat(int var1) {
      ExtractorMediaPeriod.PreparedState var2 = this.getPreparedState();
      boolean[] var3 = var2.trackNotifiedDownstreamFormats;
      if (!var3[var1]) {
         Format var4 = var2.tracks.get(var1).getFormat(0);
         this.eventDispatcher.downstreamFormatChanged(MimeTypes.getTrackType(var4.sampleMimeType), var4, 0, (Object)null, this.lastSeekPositionUs);
         var3[var1] = true;
      }

   }

   private void maybeStartDeferredRetry(int var1) {
      boolean[] var2 = this.getPreparedState().trackIsAudioVideoFlags;
      if (this.pendingDeferredRetry && var2[var1] && !this.sampleQueues[var1].hasNextSample()) {
         this.pendingResetPositionUs = 0L;
         var1 = 0;
         this.pendingDeferredRetry = false;
         this.notifyDiscontinuity = true;
         this.lastSeekPositionUs = 0L;
         this.extractedSamplesCountAtStartOfLoad = 0;
         SampleQueue[] var4 = this.sampleQueues;

         for(int var3 = var4.length; var1 < var3; ++var1) {
            var4[var1].reset();
         }

         MediaPeriod.Callback var5 = this.callback;
         Assertions.checkNotNull(var5);
         ((MediaPeriod.Callback)var5).onContinueLoadingRequested(this);
      }

   }

   private TrackOutput prepareTrackOutput(ExtractorMediaPeriod.TrackId var1) {
      int var2 = this.sampleQueues.length;

      int var3;
      for(var3 = 0; var3 < var2; ++var3) {
         if (var1.equals(this.sampleQueueTrackIds[var3])) {
            return this.sampleQueues[var3];
         }
      }

      SampleQueue var4 = new SampleQueue(this.allocator);
      var4.setUpstreamFormatChangeListener(this);
      ExtractorMediaPeriod.TrackId[] var5 = this.sampleQueueTrackIds;
      var3 = var2 + 1;
      var5 = (ExtractorMediaPeriod.TrackId[])Arrays.copyOf(var5, var3);
      var5[var2] = var1;
      Util.castNonNullTypeArray(var5);
      this.sampleQueueTrackIds = (ExtractorMediaPeriod.TrackId[])var5;
      SampleQueue[] var6 = (SampleQueue[])Arrays.copyOf(this.sampleQueues, var3);
      var6[var2] = var4;
      Util.castNonNullTypeArray(var6);
      this.sampleQueues = (SampleQueue[])var6;
      return var4;
   }

   private boolean seekInsideBufferUs(boolean[] var1, long var2) {
      int var4 = this.sampleQueues.length;
      int var5 = 0;

      while(true) {
         boolean var6 = true;
         if (var5 >= var4) {
            return true;
         }

         SampleQueue var7 = this.sampleQueues[var5];
         var7.rewind();
         if (var7.advanceTo(var2, true, false) == -1) {
            var6 = false;
         }

         if (!var6 && (var1[var5] || !this.haveAudioVideoTracks)) {
            return false;
         }

         ++var5;
      }
   }

   private void startLoading() {
      ExtractorMediaPeriod.ExtractingLoadable var1 = new ExtractorMediaPeriod.ExtractingLoadable(this.uri, this.dataSource, this.extractorHolder, this, this.loadCondition);
      long var3;
      if (this.prepared) {
         SeekMap var2 = this.getPreparedState().seekMap;
         Assertions.checkState(this.isPendingReset());
         var3 = this.durationUs;
         if (var3 != -9223372036854775807L && this.pendingResetPositionUs >= var3) {
            this.loadingFinished = true;
            this.pendingResetPositionUs = -9223372036854775807L;
            return;
         }

         var1.setLoadPosition(var2.getSeekPoints(this.pendingResetPositionUs).first.position, this.pendingResetPositionUs);
         this.pendingResetPositionUs = -9223372036854775807L;
      }

      this.extractedSamplesCountAtStartOfLoad = this.getExtractedSamplesCount();
      var3 = this.loader.startLoading(var1, this, this.loadErrorHandlingPolicy.getMinimumLoadableRetryCount(this.dataType));
      this.eventDispatcher.loadStarted(var1.dataSpec, 1, -1, (Format)null, 0, (Object)null, var1.seekTimeUs, this.durationUs, var3);
   }

   private boolean suppressRead() {
      boolean var1;
      if (!this.notifyDiscontinuity && !this.isPendingReset()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean continueLoading(long var1) {
      if (!this.loadingFinished && !this.pendingDeferredRetry && (!this.prepared || this.enabledTrackCount != 0)) {
         boolean var3 = this.loadCondition.open();
         if (!this.loader.isLoading()) {
            this.startLoading();
            var3 = true;
         }

         return var3;
      } else {
         return false;
      }
   }

   public void discardBuffer(long var1, boolean var3) {
      if (!this.isPendingReset()) {
         boolean[] var4 = this.getPreparedState().trackEnabledStates;
         int var5 = this.sampleQueues.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            this.sampleQueues[var6].discardTo(var1, var3, var4[var6]);
         }

      }
   }

   public void endTracks() {
      this.sampleQueuesBuilt = true;
      this.handler.post(this.maybeFinishPrepareRunnable);
   }

   public long getAdjustedSeekPositionUs(long var1, SeekParameters var3) {
      SeekMap var4 = this.getPreparedState().seekMap;
      if (!var4.isSeekable()) {
         return 0L;
      } else {
         SeekMap.SeekPoints var5 = var4.getSeekPoints(var1);
         return Util.resolveSeekPositionUs(var1, var3, var5.first.timeUs, var5.second.timeUs);
      }
   }

   public long getBufferedPositionUs() {
      boolean[] var1 = this.getPreparedState().trackIsAudioVideoFlags;
      if (this.loadingFinished) {
         return Long.MIN_VALUE;
      } else if (this.isPendingReset()) {
         return this.pendingResetPositionUs;
      } else {
         long var4;
         long var6;
         if (this.haveAudioVideoTracks) {
            int var2 = this.sampleQueues.length;
            int var3 = 0;
            var4 = Long.MAX_VALUE;

            while(true) {
               var6 = var4;
               if (var3 >= var2) {
                  break;
               }

               var6 = var4;
               if (var1[var3]) {
                  var6 = var4;
                  if (!this.sampleQueues[var3].isLastSampleQueued()) {
                     var6 = Math.min(var4, this.sampleQueues[var3].getLargestQueuedTimestampUs());
                  }
               }

               ++var3;
               var4 = var6;
            }
         } else {
            var6 = Long.MAX_VALUE;
         }

         var4 = var6;
         if (var6 == Long.MAX_VALUE) {
            var4 = this.getLargestQueuedTimestampUs();
         }

         var6 = var4;
         if (var4 == Long.MIN_VALUE) {
            var6 = this.lastSeekPositionUs;
         }

         return var6;
      }
   }

   public long getNextLoadPositionUs() {
      long var1;
      if (this.enabledTrackCount == 0) {
         var1 = Long.MIN_VALUE;
      } else {
         var1 = this.getBufferedPositionUs();
      }

      return var1;
   }

   public TrackGroupArray getTrackGroups() {
      return this.getPreparedState().tracks;
   }

   TrackOutput icyTrack() {
      return this.prepareTrackOutput(new ExtractorMediaPeriod.TrackId(0, true));
   }

   boolean isReady(int var1) {
      boolean var2;
      if (this.suppressRead() || !this.loadingFinished && !this.sampleQueues[var1].hasNextSample()) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   // $FF: synthetic method
   public void lambda$new$0$ExtractorMediaPeriod() {
      if (!this.released) {
         MediaPeriod.Callback var1 = this.callback;
         Assertions.checkNotNull(var1);
         ((MediaPeriod.Callback)var1).onContinueLoadingRequested(this);
      }

   }

   void maybeThrowError() throws IOException {
      this.loader.maybeThrowError(this.loadErrorHandlingPolicy.getMinimumLoadableRetryCount(this.dataType));
   }

   public void maybeThrowPrepareError() throws IOException {
      this.maybeThrowError();
   }

   public void onLoadCanceled(ExtractorMediaPeriod.ExtractingLoadable var1, long var2, long var4, boolean var6) {
      this.eventDispatcher.loadCanceled(var1.dataSpec, var1.dataSource.getLastOpenedUri(), var1.dataSource.getLastResponseHeaders(), 1, -1, (Format)null, 0, (Object)null, var1.seekTimeUs, this.durationUs, var2, var4, var1.dataSource.getBytesRead());
      if (!var6) {
         this.copyLengthFromLoader(var1);
         SampleQueue[] var9 = this.sampleQueues;
         int var7 = var9.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            var9[var8].reset();
         }

         if (this.enabledTrackCount > 0) {
            MediaPeriod.Callback var10 = this.callback;
            Assertions.checkNotNull(var10);
            ((MediaPeriod.Callback)var10).onContinueLoadingRequested(this);
         }
      }

   }

   public void onLoadCompleted(ExtractorMediaPeriod.ExtractingLoadable var1, long var2, long var4) {
      if (this.durationUs == -9223372036854775807L) {
         SeekMap var6 = this.seekMap;
         Assertions.checkNotNull(var6);
         var6 = (SeekMap)var6;
         long var7 = this.getLargestQueuedTimestampUs();
         if (var7 == Long.MIN_VALUE) {
            var7 = 0L;
         } else {
            var7 += 10000L;
         }

         this.durationUs = var7;
         this.listener.onSourceInfoRefreshed(this.durationUs, var6.isSeekable());
      }

      this.eventDispatcher.loadCompleted(var1.dataSpec, var1.dataSource.getLastOpenedUri(), var1.dataSource.getLastResponseHeaders(), 1, -1, (Format)null, 0, (Object)null, var1.seekTimeUs, this.durationUs, var2, var4, var1.dataSource.getBytesRead());
      this.copyLengthFromLoader(var1);
      this.loadingFinished = true;
      MediaPeriod.Callback var9 = this.callback;
      Assertions.checkNotNull(var9);
      ((MediaPeriod.Callback)var9).onContinueLoadingRequested(this);
   }

   public Loader.LoadErrorAction onLoadError(ExtractorMediaPeriod.ExtractingLoadable var1, long var2, long var4, IOException var6, int var7) {
      this.copyLengthFromLoader(var1);
      long var8 = this.loadErrorHandlingPolicy.getRetryDelayMsFor(this.dataType, this.durationUs, var6, var7);
      Loader.LoadErrorAction var10;
      if (var8 == -9223372036854775807L) {
         var10 = Loader.DONT_RETRY_FATAL;
      } else {
         var7 = this.getExtractedSamplesCount();
         boolean var11;
         if (var7 > this.extractedSamplesCountAtStartOfLoad) {
            var11 = true;
         } else {
            var11 = false;
         }

         if (this.configureRetry(var1, var7)) {
            var10 = Loader.createRetryAction(var11, var8);
         } else {
            var10 = Loader.DONT_RETRY;
         }
      }

      this.eventDispatcher.loadError(var1.dataSpec, var1.dataSource.getLastOpenedUri(), var1.dataSource.getLastResponseHeaders(), 1, -1, (Format)null, 0, (Object)null, var1.seekTimeUs, this.durationUs, var2, var4, var1.dataSource.getBytesRead(), var6, var10.isRetry() ^ true);
      return var10;
   }

   public void onLoaderReleased() {
      SampleQueue[] var1 = this.sampleQueues;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         var1[var3].reset();
      }

      this.extractorHolder.release();
   }

   public void onUpstreamFormatChanged(Format var1) {
      this.handler.post(this.maybeFinishPrepareRunnable);
   }

   public void prepare(MediaPeriod.Callback var1, long var2) {
      this.callback = var1;
      this.loadCondition.open();
      this.startLoading();
   }

   int readData(int var1, FormatHolder var2, DecoderInputBuffer var3, boolean var4) {
      if (this.suppressRead()) {
         return -3;
      } else {
         this.maybeNotifyDownstreamFormat(var1);
         int var5 = this.sampleQueues[var1].read(var2, var3, var4, this.loadingFinished, this.lastSeekPositionUs);
         if (var5 == -3) {
            this.maybeStartDeferredRetry(var1);
         }

         return var5;
      }
   }

   public long readDiscontinuity() {
      if (!this.notifiedReadingStarted) {
         this.eventDispatcher.readingStarted();
         this.notifiedReadingStarted = true;
      }

      if (!this.notifyDiscontinuity || !this.loadingFinished && this.getExtractedSamplesCount() <= this.extractedSamplesCountAtStartOfLoad) {
         return -9223372036854775807L;
      } else {
         this.notifyDiscontinuity = false;
         return this.lastSeekPositionUs;
      }
   }

   public void reevaluateBuffer(long var1) {
   }

   public void release() {
      if (this.prepared) {
         SampleQueue[] var1 = this.sampleQueues;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            var1[var3].discardToEnd();
         }
      }

      this.loader.release(this);
      this.handler.removeCallbacksAndMessages((Object)null);
      this.callback = null;
      this.released = true;
      this.eventDispatcher.mediaPeriodReleased();
   }

   public void seekMap(SeekMap var1) {
      this.seekMap = var1;
      this.handler.post(this.maybeFinishPrepareRunnable);
   }

   public long seekToUs(long var1) {
      ExtractorMediaPeriod.PreparedState var3 = this.getPreparedState();
      SeekMap var4 = var3.seekMap;
      boolean[] var7 = var3.trackIsAudioVideoFlags;
      if (!var4.isSeekable()) {
         var1 = 0L;
      }

      int var5 = 0;
      this.notifyDiscontinuity = false;
      this.lastSeekPositionUs = var1;
      if (this.isPendingReset()) {
         this.pendingResetPositionUs = var1;
         return var1;
      } else if (this.dataType != 7 && this.seekInsideBufferUs(var7, var1)) {
         return var1;
      } else {
         this.pendingDeferredRetry = false;
         this.pendingResetPositionUs = var1;
         this.loadingFinished = false;
         if (this.loader.isLoading()) {
            this.loader.cancelLoading();
         } else {
            SampleQueue[] var8 = this.sampleQueues;

            for(int var6 = var8.length; var5 < var6; ++var5) {
               var8[var5].reset();
            }
         }

         return var1;
      }
   }

   public long selectTracks(TrackSelection[] var1, boolean[] var2, SampleStream[] var3, boolean[] var4, long var5) {
      ExtractorMediaPeriod.PreparedState var7 = this.getPreparedState();
      TrackGroupArray var8 = var7.tracks;
      boolean[] var22 = var7.trackEnabledStates;
      int var9 = this.enabledTrackCount;
      byte var10 = 0;
      byte var11 = 0;
      byte var12 = 0;

      int var13;
      for(var13 = 0; var13 < var1.length; ++var13) {
         if (var3[var13] != null && (var1[var13] == null || !var2[var13])) {
            int var14 = ((ExtractorMediaPeriod.SampleStreamImpl)var3[var13]).track;
            Assertions.checkState(var22[var14]);
            --this.enabledTrackCount;
            var22[var14] = false;
            var3[var13] = null;
         }
      }

      boolean var23;
      label96: {
         label95: {
            if (this.seenFirstTrackSelection) {
               if (var9 == 0) {
                  break label95;
               }
            } else if (var5 != 0L) {
               break label95;
            }

            var23 = false;
            break label96;
         }

         var23 = true;
      }

      var9 = 0;

      boolean var24;
      for(var24 = var23; var9 < var1.length; var24 = var23) {
         var23 = var24;
         if (var3[var9] == null) {
            var23 = var24;
            if (var1[var9] != null) {
               TrackSelection var20 = var1[var9];
               boolean var15;
               if (var20.length() == 1) {
                  var15 = true;
               } else {
                  var15 = false;
               }

               Assertions.checkState(var15);
               if (var20.getIndexInTrackGroup(0) == 0) {
                  var15 = true;
               } else {
                  var15 = false;
               }

               Assertions.checkState(var15);
               int var16 = var8.indexOf(var20.getTrackGroup());
               Assertions.checkState(var22[var16] ^ true);
               ++this.enabledTrackCount;
               var22[var16] = true;
               var3[var9] = new ExtractorMediaPeriod.SampleStreamImpl(var16);
               var4[var9] = true;
               var23 = var24;
               if (!var24) {
                  SampleQueue var21 = this.sampleQueues[var16];
                  var21.rewind();
                  if (var21.advanceTo(var5, true, true) == -1 && var21.getReadIndex() != 0) {
                     var23 = true;
                  } else {
                     var23 = false;
                  }
               }
            }
         }

         ++var9;
      }

      long var17;
      if (this.enabledTrackCount == 0) {
         this.pendingDeferredRetry = false;
         this.notifyDiscontinuity = false;
         SampleQueue[] var19;
         if (this.loader.isLoading()) {
            var19 = this.sampleQueues;
            var9 = var19.length;

            for(var13 = var12; var13 < var9; ++var13) {
               var19[var13].discardToEnd();
            }

            this.loader.cancelLoading();
            var17 = var5;
         } else {
            var19 = this.sampleQueues;
            var9 = var19.length;
            var13 = var10;

            while(true) {
               var17 = var5;
               if (var13 >= var9) {
                  break;
               }

               var19[var13].reset();
               ++var13;
            }
         }
      } else {
         var17 = var5;
         if (var24) {
            var5 = this.seekToUs(var5);
            var13 = var11;

            while(true) {
               var17 = var5;
               if (var13 >= var3.length) {
                  break;
               }

               if (var3[var13] != null) {
                  var4[var13] = true;
               }

               ++var13;
            }
         }
      }

      this.seenFirstTrackSelection = true;
      return var17;
   }

   int skipData(int var1, long var2) {
      boolean var4 = this.suppressRead();
      int var5 = 0;
      if (var4) {
         return 0;
      } else {
         this.maybeNotifyDownstreamFormat(var1);
         SampleQueue var6 = this.sampleQueues[var1];
         if (this.loadingFinished && var2 > var6.getLargestQueuedTimestampUs()) {
            var5 = var6.advanceToEnd();
         } else {
            int var7 = var6.advanceTo(var2, true, true);
            if (var7 != -1) {
               var5 = var7;
            }
         }

         if (var5 == 0) {
            this.maybeStartDeferredRetry(var1);
         }

         return var5;
      }
   }

   public TrackOutput track(int var1, int var2) {
      return this.prepareTrackOutput(new ExtractorMediaPeriod.TrackId(var1, false));
   }

   final class ExtractingLoadable implements Loader.Loadable, IcyDataSource.Listener {
      private final StatsDataSource dataSource;
      private DataSpec dataSpec;
      private final ExtractorMediaPeriod.ExtractorHolder extractorHolder;
      private final ExtractorOutput extractorOutput;
      private TrackOutput icyTrackOutput;
      private long length;
      private volatile boolean loadCanceled;
      private final ConditionVariable loadCondition;
      private boolean pendingExtractorSeek;
      private final PositionHolder positionHolder;
      private long seekTimeUs;
      private boolean seenIcyMetadata;
      private final Uri uri;

      public ExtractingLoadable(Uri var2, DataSource var3, ExtractorMediaPeriod.ExtractorHolder var4, ExtractorOutput var5, ConditionVariable var6) {
         this.uri = var2;
         this.dataSource = new StatsDataSource(var3);
         this.extractorHolder = var4;
         this.extractorOutput = var5;
         this.loadCondition = var6;
         this.positionHolder = new PositionHolder();
         this.pendingExtractorSeek = true;
         this.length = -1L;
         this.dataSpec = this.buildDataSpec(0L);
      }

      private DataSpec buildDataSpec(long var1) {
         return new DataSpec(this.uri, var1, -1L, ExtractorMediaPeriod.this.customCacheKey, 22);
      }

      private void setLoadPosition(long var1, long var3) {
         this.positionHolder.position = var1;
         this.seekTimeUs = var3;
         this.pendingExtractorSeek = true;
         this.seenIcyMetadata = false;
      }

      public void cancelLoad() {
         this.loadCanceled = true;
      }

      public void load() throws IOException, InterruptedException {
         for(int var1 = 0; var1 == 0 && !this.loadCanceled; Util.closeQuietly((DataSource)this.dataSource)) {
            Object var2 = null;

            DefaultExtractorInput var8;
            int var10;
            label2786: {
               DefaultExtractorInput var286;
               Throwable var288;
               label2787: {
                  long var3;
                  Uri var6;
                  Throwable var10000;
                  boolean var10001;
                  label2776: {
                     label2788: {
                        try {
                           var3 = this.positionHolder.position;
                           this.dataSpec = this.buildDataSpec(var3);
                           this.length = this.dataSource.open(this.dataSpec);
                           if (this.length != -1L) {
                              this.length += var3;
                           }
                        } catch (Throwable var284) {
                           var10000 = var284;
                           var10001 = false;
                           break label2788;
                        }

                        StatsDataSource var7;
                        try {
                           Uri var5 = this.dataSource.getUri();
                           Assertions.checkNotNull(var5);
                           var6 = (Uri)var5;
                           ExtractorMediaPeriod.this.icyHeaders = IcyHeaders.parse(this.dataSource.getResponseHeaders());
                           var7 = this.dataSource;
                        } catch (Throwable var283) {
                           var10000 = var283;
                           var10001 = false;
                           break label2788;
                        }

                        Object var285 = var7;

                        label2789: {
                           try {
                              if (ExtractorMediaPeriod.this.icyHeaders == null) {
                                 break label2789;
                              }
                           } catch (Throwable var282) {
                              var10000 = var282;
                              var10001 = false;
                              break label2788;
                           }

                           var285 = var7;

                           try {
                              if (ExtractorMediaPeriod.this.icyHeaders.metadataInterval != -1) {
                                 var285 = new IcyDataSource(this.dataSource, ExtractorMediaPeriod.this.icyHeaders.metadataInterval, this);
                                 this.icyTrackOutput = ExtractorMediaPeriod.this.icyTrack();
                                 this.icyTrackOutput.format(ExtractorMediaPeriod.ICY_FORMAT);
                              }
                           } catch (Throwable var281) {
                              var10000 = var281;
                              var10001 = false;
                              break label2788;
                           }
                        }

                        label2760:
                        try {
                           var8 = new DefaultExtractorInput((DataSource)var285, var3, this.length);
                           break label2776;
                        } catch (Throwable var280) {
                           var10000 = var280;
                           var10001 = false;
                           break label2760;
                        }
                     }

                     var288 = var10000;
                     var286 = (DefaultExtractorInput)var2;
                     break label2787;
                  }

                  int var9 = var1;

                  label2790: {
                     Extractor var287;
                     try {
                        var287 = this.extractorHolder.selectExtractor(var8, this.extractorOutput, var6);
                     } catch (Throwable var278) {
                        var10000 = var278;
                        var10001 = false;
                        break label2790;
                     }

                     var10 = var1;
                     long var11 = var3;
                     var9 = var1;

                     label2791: {
                        try {
                           if (!this.pendingExtractorSeek) {
                              break label2791;
                           }
                        } catch (Throwable var279) {
                           var10000 = var279;
                           var10001 = false;
                           break label2790;
                        }

                        var9 = var1;

                        try {
                           var287.seek(var3, this.seekTimeUs);
                        } catch (Throwable var277) {
                           var10000 = var277;
                           var10001 = false;
                           break label2790;
                        }

                        var9 = var1;

                        try {
                           this.pendingExtractorSeek = false;
                        } catch (Throwable var276) {
                           var10000 = var276;
                           var10001 = false;
                           break label2790;
                        }

                        var11 = var3;
                        var10 = var1;
                     }

                     while(true) {
                        if (var10 != 0) {
                           break label2786;
                        }

                        var9 = var10;

                        try {
                           if (this.loadCanceled) {
                              break label2786;
                           }
                        } catch (Throwable var275) {
                           var10000 = var275;
                           var10001 = false;
                           break;
                        }

                        var9 = var10;

                        try {
                           this.loadCondition.block();
                        } catch (Throwable var274) {
                           var10000 = var274;
                           var10001 = false;
                           break;
                        }

                        var9 = var10;

                        try {
                           var1 = var287.read(var8, this.positionHolder);
                        } catch (Throwable var273) {
                           var10000 = var273;
                           var10001 = false;
                           break;
                        }

                        var10 = var1;
                        var9 = var1;

                        try {
                           if (var8.getPosition() <= ExtractorMediaPeriod.this.continueLoadingCheckIntervalBytes + var11) {
                              continue;
                           }
                        } catch (Throwable var272) {
                           var10000 = var272;
                           var10001 = false;
                           break;
                        }

                        var9 = var1;

                        try {
                           var11 = var8.getPosition();
                        } catch (Throwable var271) {
                           var10000 = var271;
                           var10001 = false;
                           break;
                        }

                        var9 = var1;

                        try {
                           this.loadCondition.close();
                        } catch (Throwable var270) {
                           var10000 = var270;
                           var10001 = false;
                           break;
                        }

                        var9 = var1;

                        try {
                           ExtractorMediaPeriod.this.handler.post(ExtractorMediaPeriod.this.onContinueLoadingRequestedRunnable);
                        } catch (Throwable var269) {
                           var10000 = var269;
                           var10001 = false;
                           break;
                        }

                        var10 = var1;
                     }
                  }

                  var288 = var10000;
                  var286 = var8;
                  var1 = var9;
               }

               if (var1 != 1 && var286 != null) {
                  this.positionHolder.position = var286.getPosition();
               }

               Util.closeQuietly((DataSource)this.dataSource);
               throw var288;
            }

            if (var10 == 1) {
               var1 = 0;
            } else {
               this.positionHolder.position = var8.getPosition();
               var1 = var10;
            }
         }

      }

      public void onIcyMetadata(ParsableByteArray var1) {
         long var2;
         if (!this.seenIcyMetadata) {
            var2 = this.seekTimeUs;
         } else {
            var2 = Math.max(ExtractorMediaPeriod.this.getLargestQueuedTimestampUs(), this.seekTimeUs);
         }

         int var4 = var1.bytesLeft();
         TrackOutput var5 = this.icyTrackOutput;
         Assertions.checkNotNull(var5);
         var5 = (TrackOutput)var5;
         var5.sampleData(var1, var4);
         var5.sampleMetadata(var2, 1, var4, 0, (TrackOutput.CryptoData)null);
         this.seenIcyMetadata = true;
      }
   }

   private static final class ExtractorHolder {
      private Extractor extractor;
      private final Extractor[] extractors;

      public ExtractorHolder(Extractor[] var1) {
         this.extractors = var1;
      }

      public void release() {
         Extractor var1 = this.extractor;
         if (var1 != null) {
            var1.release();
            this.extractor = null;
         }

      }

      public Extractor selectExtractor(ExtractorInput var1, ExtractorOutput var2, Uri var3) throws IOException, InterruptedException {
         Extractor var4 = this.extractor;
         if (var4 != null) {
            return var4;
         } else {
            Extractor[] var14 = this.extractors;
            int var5 = var14.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               Extractor var7 = var14[var6];

               try {
                  if (var7.sniff(var1)) {
                     this.extractor = var7;
                     break;
                  }
               } catch (EOFException var10) {
               } finally {
                  var1.resetPeekPosition();
               }
            }

            Extractor var12 = this.extractor;
            if (var12 != null) {
               var12.init(var2);
               return this.extractor;
            } else {
               StringBuilder var13 = new StringBuilder();
               var13.append("None of the available extractors (");
               var13.append(Util.getCommaDelimitedSimpleClassNames(this.extractors));
               var13.append(") could read the stream.");
               throw new UnrecognizedInputFormatException(var13.toString(), var3);
            }
         }
      }
   }

   interface Listener {
      void onSourceInfoRefreshed(long var1, boolean var3);
   }

   private static final class PreparedState {
      public final SeekMap seekMap;
      public final boolean[] trackEnabledStates;
      public final boolean[] trackIsAudioVideoFlags;
      public final boolean[] trackNotifiedDownstreamFormats;
      public final TrackGroupArray tracks;

      public PreparedState(SeekMap var1, TrackGroupArray var2, boolean[] var3) {
         this.seekMap = var1;
         this.tracks = var2;
         this.trackIsAudioVideoFlags = var3;
         int var4 = var2.length;
         this.trackEnabledStates = new boolean[var4];
         this.trackNotifiedDownstreamFormats = new boolean[var4];
      }
   }

   private final class SampleStreamImpl implements SampleStream {
      private final int track;

      public SampleStreamImpl(int var2) {
         this.track = var2;
      }

      public boolean isReady() {
         return ExtractorMediaPeriod.this.isReady(this.track);
      }

      public void maybeThrowError() throws IOException {
         ExtractorMediaPeriod.this.maybeThrowError();
      }

      public int readData(FormatHolder var1, DecoderInputBuffer var2, boolean var3) {
         return ExtractorMediaPeriod.this.readData(this.track, var1, var2, var3);
      }

      public int skipData(long var1) {
         return ExtractorMediaPeriod.this.skipData(this.track, var1);
      }
   }

   private static final class TrackId {
      public final int id;
      public final boolean isIcyTrack;

      public TrackId(int var1, boolean var2) {
         this.id = var1;
         this.isIcyTrack = var2;
      }

      public boolean equals(Object var1) {
         boolean var2 = true;
         if (this == var1) {
            return true;
         } else if (var1 != null && ExtractorMediaPeriod.TrackId.class == var1.getClass()) {
            ExtractorMediaPeriod.TrackId var3 = (ExtractorMediaPeriod.TrackId)var1;
            if (this.id != var3.id || this.isIcyTrack != var3.isIcyTrack) {
               var2 = false;
            }

            return var2;
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.id * 31 + this.isIcyTrack;
      }
   }
}
