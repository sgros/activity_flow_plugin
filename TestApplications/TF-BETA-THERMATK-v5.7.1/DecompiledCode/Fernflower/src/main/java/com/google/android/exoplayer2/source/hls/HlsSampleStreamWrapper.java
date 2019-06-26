package com.google.android.exoplayer2.source.hls;

import android.os.Handler;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.extractor.DummyTrackOutput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.id3.PrivFrame;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.SampleQueue;
import com.google.android.exoplayer2.source.SampleStream;
import com.google.android.exoplayer2.source.SequenceableLoader;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.chunk.Chunk;
import com.google.android.exoplayer2.source.chunk.MediaChunkIterator;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

final class HlsSampleStreamWrapper implements Loader.Callback, Loader.ReleaseCallback, SequenceableLoader, ExtractorOutput, SampleQueue.UpstreamFormatChangedListener {
   private final Allocator allocator;
   private int audioSampleQueueIndex;
   private boolean audioSampleQueueMappingDone;
   private final HlsSampleStreamWrapper.Callback callback;
   private final HlsChunkSource chunkSource;
   private int chunkUid;
   private Format downstreamTrackFormat;
   private int enabledTrackGroupCount;
   private final MediaSourceEventListener.EventDispatcher eventDispatcher;
   private final Handler handler;
   private boolean haveAudioVideoSampleQueues;
   private final ArrayList hlsSampleStreams;
   private long lastSeekPositionUs;
   private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
   private final Loader loader;
   private boolean loadingFinished;
   private final Runnable maybeFinishPrepareRunnable;
   private final ArrayList mediaChunks;
   private final Format muxedAudioFormat;
   private final HlsChunkSource.HlsChunkHolder nextChunkHolder;
   private final Runnable onTracksEndedRunnable;
   private TrackGroupArray optionalTrackGroups;
   private long pendingResetPositionUs;
   private boolean pendingResetUpstreamFormats;
   private boolean prepared;
   private int primarySampleQueueIndex;
   private int primarySampleQueueType;
   private int primaryTrackGroupIndex;
   private final List readOnlyMediaChunks;
   private boolean released;
   private long sampleOffsetUs;
   private boolean[] sampleQueueIsAudioVideoFlags;
   private int[] sampleQueueTrackIds;
   private SampleQueue[] sampleQueues;
   private boolean sampleQueuesBuilt;
   private boolean[] sampleQueuesEnabledStates;
   private boolean seenFirstTrackSelection;
   private int[] trackGroupToSampleQueueIndex;
   private TrackGroupArray trackGroups;
   private final int trackType;
   private boolean tracksEnded;
   private Format upstreamTrackFormat;
   private int videoSampleQueueIndex;
   private boolean videoSampleQueueMappingDone;

   public HlsSampleStreamWrapper(int var1, HlsSampleStreamWrapper.Callback var2, HlsChunkSource var3, Allocator var4, long var5, Format var7, LoadErrorHandlingPolicy var8, MediaSourceEventListener.EventDispatcher var9) {
      this.trackType = var1;
      this.callback = var2;
      this.chunkSource = var3;
      this.allocator = var4;
      this.muxedAudioFormat = var7;
      this.loadErrorHandlingPolicy = var8;
      this.eventDispatcher = var9;
      this.loader = new Loader("Loader:HlsSampleStreamWrapper");
      this.nextChunkHolder = new HlsChunkSource.HlsChunkHolder();
      this.sampleQueueTrackIds = new int[0];
      this.audioSampleQueueIndex = -1;
      this.videoSampleQueueIndex = -1;
      this.sampleQueues = new SampleQueue[0];
      this.sampleQueueIsAudioVideoFlags = new boolean[0];
      this.sampleQueuesEnabledStates = new boolean[0];
      this.mediaChunks = new ArrayList();
      this.readOnlyMediaChunks = Collections.unmodifiableList(this.mediaChunks);
      this.hlsSampleStreams = new ArrayList();
      this.maybeFinishPrepareRunnable = new _$$Lambda$HlsSampleStreamWrapper$8JyeEr0irIOShv9LlAxAmgzl5vY(this);
      this.onTracksEndedRunnable = new _$$Lambda$HlsSampleStreamWrapper$afhkI3tagC__MAOTh7FzBWzQsno(this);
      this.handler = new Handler();
      this.lastSeekPositionUs = var5;
      this.pendingResetPositionUs = var5;
   }

   private void buildTracksFromSampleStreams() {
      int var1 = this.sampleQueues.length;
      boolean var2 = false;
      int var3 = 0;
      byte var4 = 6;
      int var5 = -1;

      while(true) {
         byte var6 = 2;
         int var8;
         if (var3 >= var1) {
            TrackGroup var10 = this.chunkSource.getTrackGroup();
            var8 = var10.length;
            this.primaryTrackGroupIndex = -1;
            this.trackGroupToSampleQueueIndex = new int[var1];

            int var14;
            for(var14 = 0; var14 < var1; this.trackGroupToSampleQueueIndex[var14] = var14++) {
            }

            TrackGroup[] var11 = new TrackGroup[var1];

            for(var14 = 0; var14 < var1; ++var14) {
               Format var12 = this.sampleQueues[var14].getUpstreamFormat();
               if (var14 == var5) {
                  Format[] var15 = new Format[var8];
                  if (var8 == 1) {
                     var15[0] = var12.copyWithManifestFormatInfo(var10.getFormat(0));
                  } else {
                     for(var3 = 0; var3 < var8; ++var3) {
                        var15[var3] = deriveFormat(var10.getFormat(var3), var12, true);
                     }
                  }

                  var11[var14] = new TrackGroup(var15);
                  this.primaryTrackGroupIndex = var14;
               } else {
                  Format var13;
                  if (var4 == 2 && MimeTypes.isAudio(var12.sampleMimeType)) {
                     var13 = this.muxedAudioFormat;
                  } else {
                     var13 = null;
                  }

                  var11[var14] = new TrackGroup(new Format[]{deriveFormat(var13, var12, false)});
               }
            }

            this.trackGroups = new TrackGroupArray(var11);
            if (this.optionalTrackGroups == null) {
               var2 = true;
            }

            Assertions.checkState(var2);
            this.optionalTrackGroups = TrackGroupArray.EMPTY;
            return;
         }

         String var7 = this.sampleQueues[var3].getUpstreamFormat().sampleMimeType;
         if (!MimeTypes.isVideo(var7)) {
            if (MimeTypes.isAudio(var7)) {
               var6 = 1;
            } else if (MimeTypes.isText(var7)) {
               var6 = 3;
            } else {
               var6 = 6;
            }
         }

         byte var9;
         if (getTrackTypeScore(var6) > getTrackTypeScore(var4)) {
            var8 = var3;
            var9 = var6;
         } else {
            var9 = var4;
            var8 = var5;
            if (var6 == var4) {
               var9 = var4;
               var8 = var5;
               if (var5 != -1) {
                  var8 = -1;
                  var9 = var4;
               }
            }
         }

         ++var3;
         var4 = var9;
         var5 = var8;
      }
   }

   private static DummyTrackOutput createDummyTrackOutput(int var0, int var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("Unmapped track with id ");
      var2.append(var0);
      var2.append(" of type ");
      var2.append(var1);
      Log.w("HlsSampleStreamWrapper", var2.toString());
      return new DummyTrackOutput();
   }

   private static Format deriveFormat(Format var0, Format var1, boolean var2) {
      if (var0 == null) {
         return var1;
      } else {
         int var3;
         if (var2) {
            var3 = var0.bitrate;
         } else {
            var3 = -1;
         }

         int var4 = MimeTypes.getTrackType(var1.sampleMimeType);
         String var5 = Util.getCodecsOfType(var0.codecs, var4);
         String var6 = MimeTypes.getMediaMimeType(var5);
         String var7 = var6;
         if (var6 == null) {
            var7 = var1.sampleMimeType;
         }

         return var1.copyWithContainerInfo(var0.id, var0.label, var7, var5, var3, var0.width, var0.height, var0.selectionFlags, var0.language);
      }
   }

   private boolean finishedReadingChunk(HlsMediaChunk var1) {
      int var2 = var1.uid;
      int var3 = this.sampleQueues.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         if (this.sampleQueuesEnabledStates[var4] && this.sampleQueues[var4].peekSourceId() == var2) {
            return false;
         }
      }

      return true;
   }

   private static boolean formatsMatch(Format var0, Format var1) {
      String var2 = var0.sampleMimeType;
      String var3 = var1.sampleMimeType;
      int var4 = MimeTypes.getTrackType(var2);
      boolean var5 = true;
      boolean var6 = true;
      if (var4 != 3) {
         if (var4 != MimeTypes.getTrackType(var3)) {
            var6 = false;
         }

         return var6;
      } else if (!Util.areEqual(var2, var3)) {
         return false;
      } else if (!"application/cea-608".equals(var2) && !"application/cea-708".equals(var2)) {
         return true;
      } else {
         if (var0.accessibilityChannel == var1.accessibilityChannel) {
            var6 = var5;
         } else {
            var6 = false;
         }

         return var6;
      }
   }

   private HlsMediaChunk getLastMediaChunk() {
      ArrayList var1 = this.mediaChunks;
      return (HlsMediaChunk)var1.get(var1.size() - 1);
   }

   private static int getTrackTypeScore(int var0) {
      if (var0 != 1) {
         if (var0 != 2) {
            return var0 != 3 ? 0 : 1;
         } else {
            return 3;
         }
      } else {
         return 2;
      }
   }

   private static boolean isMediaChunk(Chunk var0) {
      return var0 instanceof HlsMediaChunk;
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
   public static void lambda$8JyeEr0irIOShv9LlAxAmgzl5vY(HlsSampleStreamWrapper var0) {
      var0.maybeFinishPrepare();
   }

   // $FF: synthetic method
   public static void lambda$afhkI3tagC__MAOTh7FzBWzQsno/* $FF was: lambda$afhkI3tagC_-MAOTh7FzBWzQsno*/(HlsSampleStreamWrapper var0) {
      var0.onTracksEnded();
   }

   private void mapSampleQueuesToMatchTrackGroups() {
      int var1 = this.trackGroups.length;
      this.trackGroupToSampleQueueIndex = new int[var1];
      Arrays.fill(this.trackGroupToSampleQueueIndex, -1);

      for(int var2 = 0; var2 < var1; ++var2) {
         int var3 = 0;

         while(true) {
            SampleQueue[] var4 = this.sampleQueues;
            if (var3 >= var4.length) {
               break;
            }

            if (formatsMatch(var4[var3].getUpstreamFormat(), this.trackGroups.get(var2).getFormat(0))) {
               this.trackGroupToSampleQueueIndex[var2] = var3;
               break;
            }

            ++var3;
         }
      }

      Iterator var5 = this.hlsSampleStreams.iterator();

      while(var5.hasNext()) {
         ((HlsSampleStream)var5.next()).bindSampleQueue();
      }

   }

   private void maybeFinishPrepare() {
      if (!this.released && this.trackGroupToSampleQueueIndex == null && this.sampleQueuesBuilt) {
         SampleQueue[] var1 = this.sampleQueues;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            if (var1[var3].getUpstreamFormat() == null) {
               return;
            }
         }

         if (this.trackGroups != null) {
            this.mapSampleQueuesToMatchTrackGroups();
         } else {
            this.buildTracksFromSampleStreams();
            this.prepared = true;
            this.callback.onPrepared();
         }
      }

   }

   private void onTracksEnded() {
      this.sampleQueuesBuilt = true;
      this.maybeFinishPrepare();
   }

   private void resetSampleQueues() {
      SampleQueue[] var1 = this.sampleQueues;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         var1[var3].reset(this.pendingResetUpstreamFormats);
      }

      this.pendingResetUpstreamFormats = false;
   }

   private boolean seekInsideBufferUs(long var1) {
      int var3 = this.sampleQueues.length;
      int var4 = 0;

      while(true) {
         boolean var5 = true;
         if (var4 >= var3) {
            return true;
         }

         SampleQueue var6 = this.sampleQueues[var4];
         var6.rewind();
         if (var6.advanceTo(var1, true, false) == -1) {
            var5 = false;
         }

         if (!var5 && (this.sampleQueueIsAudioVideoFlags[var4] || !this.haveAudioVideoSampleQueues)) {
            return false;
         }

         ++var4;
      }
   }

   private void updateSampleStreams(SampleStream[] var1) {
      this.hlsSampleStreams.clear();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         SampleStream var4 = var1[var3];
         if (var4 != null) {
            this.hlsSampleStreams.add((HlsSampleStream)var4);
         }
      }

   }

   public int bindSampleQueueToSampleStream(int var1) {
      int var2 = this.trackGroupToSampleQueueIndex[var1];
      byte var3 = -2;
      if (var2 == -1) {
         byte var5;
         if (this.optionalTrackGroups.indexOf(this.trackGroups.get(var1)) == -1) {
            var5 = var3;
         } else {
            var5 = -3;
         }

         return var5;
      } else {
         boolean[] var4 = this.sampleQueuesEnabledStates;
         if (var4[var2]) {
            return -2;
         } else {
            var4[var2] = true;
            return var2;
         }
      }
   }

   public boolean continueLoading(long var1) {
      if (!this.loadingFinished && !this.loader.isLoading()) {
         List var3;
         long var4;
         HlsMediaChunk var6;
         if (this.isPendingReset()) {
            var3 = Collections.emptyList();
            var4 = this.pendingResetPositionUs;
         } else {
            var3 = this.readOnlyMediaChunks;
            var6 = this.getLastMediaChunk();
            if (var6.isLoadCompleted()) {
               var4 = var6.endTimeUs;
            } else {
               var4 = Math.max(this.lastSeekPositionUs, var6.startTimeUs);
            }
         }

         this.chunkSource.getNextChunk(var1, var4, var3, this.nextChunkHolder);
         HlsChunkSource.HlsChunkHolder var7 = this.nextChunkHolder;
         boolean var8 = var7.endOfStream;
         Chunk var9 = var7.chunk;
         HlsMasterPlaylist.HlsUrl var10 = var7.playlist;
         var7.clear();
         if (var8) {
            this.pendingResetPositionUs = -9223372036854775807L;
            this.loadingFinished = true;
            return true;
         } else if (var9 == null) {
            if (var10 != null) {
               this.callback.onPlaylistRefreshRequired(var10);
            }

            return false;
         } else {
            if (isMediaChunk(var9)) {
               this.pendingResetPositionUs = -9223372036854775807L;
               var6 = (HlsMediaChunk)var9;
               var6.init(this);
               this.mediaChunks.add(var6);
               this.upstreamTrackFormat = var6.trackFormat;
            }

            var1 = this.loader.startLoading(var9, this, this.loadErrorHandlingPolicy.getMinimumLoadableRetryCount(var9.type));
            this.eventDispatcher.loadStarted(var9.dataSpec, var9.type, this.trackType, var9.trackFormat, var9.trackSelectionReason, var9.trackSelectionData, var9.startTimeUs, var9.endTimeUs, var1);
            return true;
         }
      } else {
         return false;
      }
   }

   public void continuePreparing() {
      if (!this.prepared) {
         this.continueLoading(this.lastSeekPositionUs);
      }

   }

   public void discardBuffer(long var1, boolean var3) {
      if (this.sampleQueuesBuilt && !this.isPendingReset()) {
         int var4 = this.sampleQueues.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            this.sampleQueues[var5].discardTo(var1, var3, this.sampleQueuesEnabledStates[var5]);
         }
      }

   }

   public void endTracks() {
      this.tracksEnded = true;
      this.handler.post(this.onTracksEndedRunnable);
   }

   public long getBufferedPositionUs() {
      if (this.loadingFinished) {
         return Long.MIN_VALUE;
      } else if (this.isPendingReset()) {
         return this.pendingResetPositionUs;
      } else {
         long var1 = this.lastSeekPositionUs;
         HlsMediaChunk var3 = this.getLastMediaChunk();
         if (!var3.isLoadCompleted()) {
            if (this.mediaChunks.size() > 1) {
               ArrayList var8 = this.mediaChunks;
               var3 = (HlsMediaChunk)var8.get(var8.size() - 2);
            } else {
               var3 = null;
            }
         }

         long var4 = var1;
         if (var3 != null) {
            var4 = Math.max(var1, var3.endTimeUs);
         }

         var1 = var4;
         if (this.sampleQueuesBuilt) {
            SampleQueue[] var9 = this.sampleQueues;
            int var6 = var9.length;
            int var7 = 0;

            while(true) {
               var1 = var4;
               if (var7 >= var6) {
                  break;
               }

               var4 = Math.max(var4, var9[var7].getLargestQueuedTimestampUs());
               ++var7;
            }
         }

         return var1;
      }
   }

   public long getNextLoadPositionUs() {
      if (this.isPendingReset()) {
         return this.pendingResetPositionUs;
      } else {
         long var1;
         if (this.loadingFinished) {
            var1 = Long.MIN_VALUE;
         } else {
            var1 = this.getLastMediaChunk().endTimeUs;
         }

         return var1;
      }
   }

   public TrackGroupArray getTrackGroups() {
      return this.trackGroups;
   }

   public void init(int var1, boolean var2, boolean var3) {
      byte var4 = 0;
      if (!var3) {
         this.audioSampleQueueMappingDone = false;
         this.videoSampleQueueMappingDone = false;
      }

      this.chunkUid = var1;
      SampleQueue[] var5 = this.sampleQueues;
      int var6 = var5.length;

      int var7;
      for(var7 = 0; var7 < var6; ++var7) {
         var5[var7].sourceId(var1);
      }

      if (var2) {
         var5 = this.sampleQueues;
         var7 = var5.length;

         for(var1 = var4; var1 < var7; ++var1) {
            var5[var1].splice();
         }
      }

   }

   public boolean isReady(int var1) {
      boolean var2;
      if (this.loadingFinished || !this.isPendingReset() && this.sampleQueues[var1].hasNextSample()) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public void maybeThrowError() throws IOException {
      this.loader.maybeThrowError();
      this.chunkSource.maybeThrowError();
   }

   public void maybeThrowPrepareError() throws IOException {
      this.maybeThrowError();
   }

   public void onLoadCanceled(Chunk var1, long var2, long var4, boolean var6) {
      this.eventDispatcher.loadCanceled(var1.dataSpec, var1.getUri(), var1.getResponseHeaders(), var1.type, this.trackType, var1.trackFormat, var1.trackSelectionReason, var1.trackSelectionData, var1.startTimeUs, var1.endTimeUs, var2, var4, var1.bytesLoaded());
      if (!var6) {
         this.resetSampleQueues();
         if (this.enabledTrackGroupCount > 0) {
            this.callback.onContinueLoadingRequested(this);
         }
      }

   }

   public void onLoadCompleted(Chunk var1, long var2, long var4) {
      this.chunkSource.onChunkLoadCompleted(var1);
      this.eventDispatcher.loadCompleted(var1.dataSpec, var1.getUri(), var1.getResponseHeaders(), var1.type, this.trackType, var1.trackFormat, var1.trackSelectionReason, var1.trackSelectionData, var1.startTimeUs, var1.endTimeUs, var2, var4, var1.bytesLoaded());
      if (!this.prepared) {
         this.continueLoading(this.lastSeekPositionUs);
      } else {
         this.callback.onContinueLoadingRequested(this);
      }

   }

   public Loader.LoadErrorAction onLoadError(Chunk var1, long var2, long var4, IOException var6, int var7) {
      long var8 = var1.bytesLoaded();
      boolean var10 = isMediaChunk(var1);
      long var11 = this.loadErrorHandlingPolicy.getBlacklistDurationMsFor(var1.type, var4, var6, var7);
      boolean var13 = false;
      boolean var14;
      if (var11 != -9223372036854775807L) {
         var14 = this.chunkSource.maybeBlacklistTrack(var1, var11);
      } else {
         var14 = false;
      }

      Loader.LoadErrorAction var16;
      if (var14) {
         if (var10 && var8 == 0L) {
            ArrayList var15 = this.mediaChunks;
            if ((HlsMediaChunk)var15.remove(var15.size() - 1) == var1) {
               var13 = true;
            }

            Assertions.checkState(var13);
            if (this.mediaChunks.isEmpty()) {
               this.pendingResetPositionUs = this.lastSeekPositionUs;
            }
         }

         var16 = Loader.DONT_RETRY;
      } else {
         var11 = this.loadErrorHandlingPolicy.getRetryDelayMsFor(var1.type, var4, var6, var7);
         if (var11 != -9223372036854775807L) {
            var16 = Loader.createRetryAction(false, var11);
         } else {
            var16 = Loader.DONT_RETRY_FATAL;
         }
      }

      this.eventDispatcher.loadError(var1.dataSpec, var1.getUri(), var1.getResponseHeaders(), var1.type, this.trackType, var1.trackFormat, var1.trackSelectionReason, var1.trackSelectionData, var1.startTimeUs, var1.endTimeUs, var2, var4, var8, var6, var16.isRetry() ^ true);
      if (var14) {
         if (!this.prepared) {
            this.continueLoading(this.lastSeekPositionUs);
         } else {
            this.callback.onContinueLoadingRequested(this);
         }
      }

      return var16;
   }

   public void onLoaderReleased() {
      this.resetSampleQueues();
   }

   public boolean onPlaylistError(HlsMasterPlaylist.HlsUrl var1, long var2) {
      return this.chunkSource.onPlaylistError(var1, var2);
   }

   public void onUpstreamFormatChanged(Format var1) {
      this.handler.post(this.maybeFinishPrepareRunnable);
   }

   public void prepareWithMasterPlaylistInfo(TrackGroupArray var1, int var2, TrackGroupArray var3) {
      this.prepared = true;
      this.trackGroups = var1;
      this.optionalTrackGroups = var3;
      this.primaryTrackGroupIndex = var2;
      this.callback.onPrepared();
   }

   public int readData(int var1, FormatHolder var2, DecoderInputBuffer var3, boolean var4) {
      if (this.isPendingReset()) {
         return -3;
      } else {
         boolean var5 = this.mediaChunks.isEmpty();
         byte var6 = 0;
         int var7;
         if (!var5) {
            for(var7 = 0; var7 < this.mediaChunks.size() - 1 && this.finishedReadingChunk((HlsMediaChunk)this.mediaChunks.get(var7)); ++var7) {
            }

            Util.removeRange(this.mediaChunks, 0, var7);
            HlsMediaChunk var8 = (HlsMediaChunk)this.mediaChunks.get(0);
            Format var9 = var8.trackFormat;
            if (!var9.equals(this.downstreamTrackFormat)) {
               this.eventDispatcher.downstreamFormatChanged(this.trackType, var9, var8.trackSelectionReason, var8.trackSelectionData, var8.startTimeUs);
            }

            this.downstreamTrackFormat = var9;
         }

         var7 = this.sampleQueues[var1].read(var2, var3, var4, this.loadingFinished, this.lastSeekPositionUs);
         if (var7 == -5 && var1 == this.primarySampleQueueIndex) {
            int var10 = this.sampleQueues[var1].peekSourceId();

            for(var1 = var6; var1 < this.mediaChunks.size() && ((HlsMediaChunk)this.mediaChunks.get(var1)).uid != var10; ++var1) {
            }

            Format var11;
            if (var1 < this.mediaChunks.size()) {
               var11 = ((HlsMediaChunk)this.mediaChunks.get(var1)).trackFormat;
            } else {
               var11 = this.upstreamTrackFormat;
            }

            var2.format = var2.format.copyWithManifestFormatInfo(var11);
         }

         return var7;
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
      this.released = true;
      this.hlsSampleStreams.clear();
   }

   public void seekMap(SeekMap var1) {
   }

   public boolean seekToUs(long var1, boolean var3) {
      this.lastSeekPositionUs = var1;
      if (this.isPendingReset()) {
         this.pendingResetPositionUs = var1;
         return true;
      } else if (this.sampleQueuesBuilt && !var3 && this.seekInsideBufferUs(var1)) {
         return false;
      } else {
         this.pendingResetPositionUs = var1;
         this.loadingFinished = false;
         this.mediaChunks.clear();
         if (this.loader.isLoading()) {
            this.loader.cancelLoading();
         } else {
            this.resetSampleQueues();
         }

         return true;
      }
   }

   public boolean selectTracks(TrackSelection[] var1, boolean[] var2, SampleStream[] var3, boolean[] var4, long var5, boolean var7) {
      Assertions.checkState(this.prepared);
      int var8 = this.enabledTrackGroupCount;
      byte var9 = 0;
      byte var10 = 0;

      int var11;
      for(var11 = 0; var11 < var1.length; ++var11) {
         if (var3[var11] != null && (var1[var11] == null || !var2[var11])) {
            --this.enabledTrackGroupCount;
            ((HlsSampleStream)var3[var11]).unbindSampleQueue();
            var3[var11] = null;
         }
      }

      boolean var12;
      label121: {
         label120: {
            if (!var7) {
               if (this.seenFirstTrackSelection) {
                  if (var8 != 0) {
                     break label120;
                  }
               } else if (var5 == this.lastSeekPositionUs) {
                  break label120;
               }
            }

            var12 = true;
            break label121;
         }

         var12 = false;
      }

      TrackSelection var13 = this.chunkSource.getTrackSelection();
      TrackSelection var20 = var13;

      boolean var15;
      for(var11 = 0; var11 < var1.length; var12 = var15) {
         TrackSelection var14 = var20;
         var15 = var12;
         if (var3[var11] == null) {
            var14 = var20;
            var15 = var12;
            if (var1[var11] != null) {
               ++this.enabledTrackGroupCount;
               var14 = var1[var11];
               var8 = this.trackGroups.indexOf(var14.getTrackGroup());
               if (var8 == this.primaryTrackGroupIndex) {
                  this.chunkSource.selectTracks(var14);
                  var20 = var14;
               }

               var3[var11] = new HlsSampleStream(this, var8);
               var4[var11] = true;
               if (this.trackGroupToSampleQueueIndex != null) {
                  ((HlsSampleStream)var3[var11]).bindSampleQueue();
               }

               var14 = var20;
               var15 = var12;
               if (this.sampleQueuesBuilt) {
                  var14 = var20;
                  var15 = var12;
                  if (!var12) {
                     SampleQueue var23 = this.sampleQueues[this.trackGroupToSampleQueueIndex[var8]];
                     var23.rewind();
                     if (var23.advanceTo(var5, true, true) == -1 && var23.getReadIndex() != 0) {
                        var15 = true;
                        var14 = var20;
                     } else {
                        var15 = false;
                        var14 = var20;
                     }
                  }
               }
            }
         }

         ++var11;
         var20 = var14;
      }

      if (this.enabledTrackGroupCount == 0) {
         this.chunkSource.reset();
         this.downstreamTrackFormat = null;
         this.mediaChunks.clear();
         if (this.loader.isLoading()) {
            if (this.sampleQueuesBuilt) {
               SampleQueue[] var18 = this.sampleQueues;
               int var21 = var18.length;

               for(var11 = var10; var11 < var21; ++var11) {
                  var18[var11].discardToEnd();
               }
            }

            this.loader.cancelLoading();
            var15 = var12;
         } else {
            this.resetSampleQueues();
            var15 = var12;
         }
      } else {
         if (!this.mediaChunks.isEmpty() && !Util.areEqual(var20, var13)) {
            boolean var22;
            label79: {
               if (!this.seenFirstTrackSelection) {
                  long var16 = 0L;
                  if (var5 < 0L) {
                     var16 = -var5;
                  }

                  HlsMediaChunk var24 = this.getLastMediaChunk();
                  MediaChunkIterator[] var19 = this.chunkSource.createMediaChunkIterators(var24, var5);
                  var20.updateSelectedTrack(var5, var16, -9223372036854775807L, this.readOnlyMediaChunks, var19);
                  var11 = this.chunkSource.getTrackGroup().indexOf(var24.trackFormat);
                  if (var20.getSelectedIndexInTrackGroup() == var11) {
                     var22 = false;
                     break label79;
                  }
               }

               var22 = true;
            }

            if (var22) {
               this.pendingResetUpstreamFormats = true;
               var7 = true;
               var12 = true;
            }
         }

         var15 = var12;
         if (var12) {
            this.seekToUs(var5, var7);
            var11 = var9;

            while(true) {
               var15 = var12;
               if (var11 >= var3.length) {
                  break;
               }

               if (var3[var11] != null) {
                  var4[var11] = true;
               }

               ++var11;
            }
         }
      }

      this.updateSampleStreams(var3);
      this.seenFirstTrackSelection = true;
      return var15;
   }

   public void setIsTimestampMaster(boolean var1) {
      this.chunkSource.setIsTimestampMaster(var1);
   }

   public void setSampleOffsetUs(long var1) {
      this.sampleOffsetUs = var1;
      SampleQueue[] var3 = this.sampleQueues;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         var3[var5].setSampleOffsetUs(var1);
      }

   }

   public int skipData(int var1, long var2) {
      if (this.isPendingReset()) {
         return 0;
      } else {
         SampleQueue var4 = this.sampleQueues[var1];
         if (this.loadingFinished && var2 > var4.getLargestQueuedTimestampUs()) {
            return var4.advanceToEnd();
         } else {
            int var5 = var4.advanceTo(var2, true, true);
            var1 = var5;
            if (var5 == -1) {
               var1 = 0;
            }

            return var1;
         }
      }
   }

   public TrackOutput track(int var1, int var2) {
      SampleQueue[] var3 = this.sampleQueues;
      int var4 = var3.length;
      boolean var5 = false;
      int var6;
      Object var8;
      if (var2 == 1) {
         var6 = this.audioSampleQueueIndex;
         if (var6 != -1) {
            if (this.audioSampleQueueMappingDone) {
               if (this.sampleQueueTrackIds[var6] == var1) {
                  var8 = var3[var6];
               } else {
                  var8 = createDummyTrackOutput(var1, var2);
               }

               return (TrackOutput)var8;
            }

            this.audioSampleQueueMappingDone = true;
            this.sampleQueueTrackIds[var6] = var1;
            return var3[var6];
         }

         if (this.tracksEnded) {
            return createDummyTrackOutput(var1, var2);
         }
      } else if (var2 == 2) {
         var6 = this.videoSampleQueueIndex;
         if (var6 != -1) {
            if (this.videoSampleQueueMappingDone) {
               if (this.sampleQueueTrackIds[var6] == var1) {
                  var8 = var3[var6];
               } else {
                  var8 = createDummyTrackOutput(var1, var2);
               }

               return (TrackOutput)var8;
            }

            this.videoSampleQueueMappingDone = true;
            this.sampleQueueTrackIds[var6] = var1;
            return var3[var6];
         }

         if (this.tracksEnded) {
            return createDummyTrackOutput(var1, var2);
         }
      } else {
         for(var6 = 0; var6 < var4; ++var6) {
            if (this.sampleQueueTrackIds[var6] == var1) {
               return this.sampleQueues[var6];
            }
         }

         if (this.tracksEnded) {
            return createDummyTrackOutput(var1, var2);
         }
      }

      HlsSampleStreamWrapper.PrivTimestampStrippingSampleQueue var9 = new HlsSampleStreamWrapper.PrivTimestampStrippingSampleQueue(this.allocator);
      var9.setSampleOffsetUs(this.sampleOffsetUs);
      var9.sourceId(this.chunkUid);
      var9.setUpstreamFormatChangeListener(this);
      int[] var7 = this.sampleQueueTrackIds;
      var6 = var4 + 1;
      this.sampleQueueTrackIds = Arrays.copyOf(var7, var6);
      this.sampleQueueTrackIds[var4] = var1;
      this.sampleQueues = (SampleQueue[])Arrays.copyOf(this.sampleQueues, var6);
      this.sampleQueues[var4] = var9;
      this.sampleQueueIsAudioVideoFlags = Arrays.copyOf(this.sampleQueueIsAudioVideoFlags, var6);
      boolean[] var10 = this.sampleQueueIsAudioVideoFlags;
      if (var2 == 1 || var2 == 2) {
         var5 = true;
      }

      var10[var4] = var5;
      this.haveAudioVideoSampleQueues |= this.sampleQueueIsAudioVideoFlags[var4];
      if (var2 == 1) {
         this.audioSampleQueueMappingDone = true;
         this.audioSampleQueueIndex = var4;
      } else if (var2 == 2) {
         this.videoSampleQueueMappingDone = true;
         this.videoSampleQueueIndex = var4;
      }

      if (getTrackTypeScore(var2) > getTrackTypeScore(this.primarySampleQueueType)) {
         this.primarySampleQueueIndex = var4;
         this.primarySampleQueueType = var2;
      }

      this.sampleQueuesEnabledStates = Arrays.copyOf(this.sampleQueuesEnabledStates, var6);
      return var9;
   }

   public void unbindSampleQueue(int var1) {
      var1 = this.trackGroupToSampleQueueIndex[var1];
      Assertions.checkState(this.sampleQueuesEnabledStates[var1]);
      this.sampleQueuesEnabledStates[var1] = false;
   }

   public interface Callback extends SequenceableLoader.Callback {
      void onPlaylistRefreshRequired(HlsMasterPlaylist.HlsUrl var1);

      void onPrepared();
   }

   private static final class PrivTimestampStrippingSampleQueue extends SampleQueue {
      public PrivTimestampStrippingSampleQueue(Allocator var1) {
         super(var1);
      }

      private Metadata getAdjustedMetadata(Metadata var1) {
         if (var1 == null) {
            return null;
         } else {
            int var2 = var1.length();
            byte var3 = 0;
            int var4 = 0;

            int var6;
            while(true) {
               if (var4 >= var2) {
                  var6 = -1;
                  break;
               }

               Metadata.Entry var5 = var1.get(var4);
               if (var5 instanceof PrivFrame && "com.apple.streaming.transportStreamTimestamp".equals(((PrivFrame)var5).owner)) {
                  var6 = var4;
                  break;
               }

               ++var4;
            }

            if (var6 == -1) {
               return var1;
            } else if (var2 == 1) {
               return null;
            } else {
               Metadata.Entry[] var8 = new Metadata.Entry[var2 - 1];

               for(var4 = var3; var4 < var2; ++var4) {
                  if (var4 != var6) {
                     int var7;
                     if (var4 < var6) {
                        var7 = var4;
                     } else {
                        var7 = var4 - 1;
                     }

                     var8[var7] = var1.get(var4);
                  }
               }

               return new Metadata(var8);
            }
         }
      }

      public void format(Format var1) {
         super.format(var1.copyWithMetadata(this.getAdjustedMetadata(var1.metadata)));
      }
   }
}
