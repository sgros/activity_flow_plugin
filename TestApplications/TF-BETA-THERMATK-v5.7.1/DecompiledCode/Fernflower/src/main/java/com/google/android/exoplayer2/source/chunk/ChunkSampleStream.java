package com.google.android.exoplayer2.source.chunk;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.SampleQueue;
import com.google.android.exoplayer2.source.SampleStream;
import com.google.android.exoplayer2.source.SequenceableLoader;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChunkSampleStream implements SampleStream, SequenceableLoader, Loader.Callback, Loader.ReleaseCallback {
   private final SequenceableLoader.Callback callback;
   private final ChunkSource chunkSource;
   long decodeOnlyUntilPositionUs;
   private final SampleQueue[] embeddedSampleQueues;
   private final Format[] embeddedTrackFormats;
   private final int[] embeddedTrackTypes;
   private final boolean[] embeddedTracksSelected;
   private final MediaSourceEventListener.EventDispatcher eventDispatcher;
   private long lastSeekPositionUs;
   private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
   private final Loader loader;
   boolean loadingFinished;
   private final BaseMediaChunkOutput mediaChunkOutput;
   private final ArrayList mediaChunks;
   private final ChunkHolder nextChunkHolder;
   private int nextNotifyPrimaryFormatMediaChunkIndex;
   private long pendingResetPositionUs;
   private Format primaryDownstreamTrackFormat;
   private final SampleQueue primarySampleQueue;
   public final int primaryTrackType;
   private final List readOnlyMediaChunks;
   private ChunkSampleStream.ReleaseCallback releaseCallback;

   public ChunkSampleStream(int var1, int[] var2, Format[] var3, ChunkSource var4, SequenceableLoader.Callback var5, Allocator var6, long var7, LoadErrorHandlingPolicy var9, MediaSourceEventListener.EventDispatcher var10) {
      this.primaryTrackType = var1;
      this.embeddedTrackTypes = var2;
      this.embeddedTrackFormats = var3;
      this.chunkSource = var4;
      this.callback = var5;
      this.eventDispatcher = var10;
      this.loadErrorHandlingPolicy = var9;
      this.loader = new Loader("Loader:ChunkSampleStream");
      this.nextChunkHolder = new ChunkHolder();
      this.mediaChunks = new ArrayList();
      this.readOnlyMediaChunks = Collections.unmodifiableList(this.mediaChunks);
      byte var11 = 0;
      int var12;
      if (var2 == null) {
         var12 = 0;
      } else {
         var12 = var2.length;
      }

      this.embeddedSampleQueues = new SampleQueue[var12];
      this.embeddedTracksSelected = new boolean[var12];
      int var13 = var12 + 1;
      int[] var16 = new int[var13];
      SampleQueue[] var17 = new SampleQueue[var13];
      this.primarySampleQueue = new SampleQueue(var6);
      var16[0] = var1;
      var17[0] = this.primarySampleQueue;

      int var14;
      for(var1 = var11; var1 < var12; var1 = var14) {
         SampleQueue var15 = new SampleQueue(var6);
         this.embeddedSampleQueues[var1] = var15;
         var14 = var1 + 1;
         var17[var14] = var15;
         var16[var14] = var2[var1];
      }

      this.mediaChunkOutput = new BaseMediaChunkOutput(var16, var17);
      this.pendingResetPositionUs = var7;
      this.lastSeekPositionUs = var7;
   }

   private void discardDownstreamMediaChunks(int var1) {
      var1 = Math.min(this.primarySampleIndexToMediaChunkIndex(var1, 0), this.nextNotifyPrimaryFormatMediaChunkIndex);
      if (var1 > 0) {
         Util.removeRange(this.mediaChunks, 0, var1);
         this.nextNotifyPrimaryFormatMediaChunkIndex -= var1;
      }

   }

   private BaseMediaChunk discardUpstreamMediaChunksFromIndex(int var1) {
      BaseMediaChunk var2 = (BaseMediaChunk)this.mediaChunks.get(var1);
      ArrayList var3 = this.mediaChunks;
      Util.removeRange(var3, var1, var3.size());
      this.nextNotifyPrimaryFormatMediaChunkIndex = Math.max(this.nextNotifyPrimaryFormatMediaChunkIndex, this.mediaChunks.size());
      SampleQueue var4 = this.primarySampleQueue;
      var1 = 0;
      var4.discardUpstreamSamples(var2.getFirstSampleIndex(0));

      while(true) {
         SampleQueue[] var5 = this.embeddedSampleQueues;
         if (var1 >= var5.length) {
            return var2;
         }

         var4 = var5[var1];
         ++var1;
         var4.discardUpstreamSamples(var2.getFirstSampleIndex(var1));
      }
   }

   private BaseMediaChunk getLastMediaChunk() {
      ArrayList var1 = this.mediaChunks;
      return (BaseMediaChunk)var1.get(var1.size() - 1);
   }

   private boolean haveReadFromMediaChunk(int var1) {
      BaseMediaChunk var2 = (BaseMediaChunk)this.mediaChunks.get(var1);
      if (this.primarySampleQueue.getReadIndex() > var2.getFirstSampleIndex(0)) {
         return true;
      } else {
         var1 = 0;

         int var4;
         int var5;
         do {
            SampleQueue[] var3 = this.embeddedSampleQueues;
            if (var1 >= var3.length) {
               return false;
            }

            var4 = var3[var1].getReadIndex();
            var5 = var1 + 1;
            var1 = var5;
         } while(var4 <= var2.getFirstSampleIndex(var5));

         return true;
      }
   }

   private boolean isMediaChunk(Chunk var1) {
      return var1 instanceof BaseMediaChunk;
   }

   private void maybeNotifyPrimaryTrackFormatChanged() {
      int var1 = this.primarySampleIndexToMediaChunkIndex(this.primarySampleQueue.getReadIndex(), this.nextNotifyPrimaryFormatMediaChunkIndex - 1);

      while(true) {
         int var2 = this.nextNotifyPrimaryFormatMediaChunkIndex;
         if (var2 > var1) {
            return;
         }

         this.nextNotifyPrimaryFormatMediaChunkIndex = var2 + 1;
         this.maybeNotifyPrimaryTrackFormatChanged(var2);
      }
   }

   private void maybeNotifyPrimaryTrackFormatChanged(int var1) {
      BaseMediaChunk var2 = (BaseMediaChunk)this.mediaChunks.get(var1);
      Format var3 = var2.trackFormat;
      if (!var3.equals(this.primaryDownstreamTrackFormat)) {
         this.eventDispatcher.downstreamFormatChanged(this.primaryTrackType, var3, var2.trackSelectionReason, var2.trackSelectionData, var2.startTimeUs);
      }

      this.primaryDownstreamTrackFormat = var3;
   }

   private int primarySampleIndexToMediaChunkIndex(int var1, int var2) {
      while(true) {
         int var3 = var2 + 1;
         if (var3 < this.mediaChunks.size()) {
            var2 = var3;
            if (((BaseMediaChunk)this.mediaChunks.get(var3)).getFirstSampleIndex(0) <= var1) {
               continue;
            }

            return var3 - 1;
         }

         return this.mediaChunks.size() - 1;
      }
   }

   public boolean continueLoading(long var1) {
      boolean var3 = this.loadingFinished;
      boolean var4 = false;
      if (!var3 && !this.loader.isLoading()) {
         var3 = this.isPendingReset();
         List var5;
         long var6;
         if (var3) {
            var5 = Collections.emptyList();
            var6 = this.pendingResetPositionUs;
         } else {
            var5 = this.readOnlyMediaChunks;
            var6 = this.getLastMediaChunk().endTimeUs;
         }

         this.chunkSource.getNextChunk(var1, var6, var5, this.nextChunkHolder);
         ChunkHolder var8 = this.nextChunkHolder;
         boolean var9 = var8.endOfStream;
         Chunk var10 = var8.chunk;
         var8.clear();
         if (var9) {
            this.pendingResetPositionUs = -9223372036854775807L;
            this.loadingFinished = true;
            return true;
         } else if (var10 == null) {
            return false;
         } else {
            if (this.isMediaChunk(var10)) {
               BaseMediaChunk var11 = (BaseMediaChunk)var10;
               if (var3) {
                  if (var11.startTimeUs == this.pendingResetPositionUs) {
                     var4 = true;
                  }

                  if (var4) {
                     var1 = 0L;
                  } else {
                     var1 = this.pendingResetPositionUs;
                  }

                  this.decodeOnlyUntilPositionUs = var1;
                  this.pendingResetPositionUs = -9223372036854775807L;
               }

               var11.init(this.mediaChunkOutput);
               this.mediaChunks.add(var11);
            }

            var1 = this.loader.startLoading(var10, this, this.loadErrorHandlingPolicy.getMinimumLoadableRetryCount(var10.type));
            this.eventDispatcher.loadStarted(var10.dataSpec, var10.type, this.primaryTrackType, var10.trackFormat, var10.trackSelectionReason, var10.trackSelectionData, var10.startTimeUs, var10.endTimeUs, var1);
            return true;
         }
      } else {
         return false;
      }
   }

   public void discardBuffer(long var1, boolean var3) {
      if (!this.isPendingReset()) {
         int var4 = this.primarySampleQueue.getFirstIndex();
         this.primarySampleQueue.discardTo(var1, var3, true);
         int var5 = this.primarySampleQueue.getFirstIndex();
         if (var5 > var4) {
            var1 = this.primarySampleQueue.getFirstTimestampUs();
            var4 = 0;

            while(true) {
               SampleQueue[] var6 = this.embeddedSampleQueues;
               if (var4 >= var6.length) {
                  break;
               }

               var6[var4].discardTo(var1, var3, this.embeddedTracksSelected[var4]);
               ++var4;
            }
         }

         this.discardDownstreamMediaChunks(var5);
      }
   }

   public long getAdjustedSeekPositionUs(long var1, SeekParameters var3) {
      return this.chunkSource.getAdjustedSeekPositionUs(var1, var3);
   }

   public long getBufferedPositionUs() {
      if (this.loadingFinished) {
         return Long.MIN_VALUE;
      } else if (this.isPendingReset()) {
         return this.pendingResetPositionUs;
      } else {
         long var1 = this.lastSeekPositionUs;
         BaseMediaChunk var3 = this.getLastMediaChunk();
         if (!var3.isLoadCompleted()) {
            if (this.mediaChunks.size() > 1) {
               ArrayList var6 = this.mediaChunks;
               var3 = (BaseMediaChunk)var6.get(var6.size() - 2);
            } else {
               var3 = null;
            }
         }

         long var4 = var1;
         if (var3 != null) {
            var4 = Math.max(var1, var3.endTimeUs);
         }

         return Math.max(var4, this.primarySampleQueue.getLargestQueuedTimestampUs());
      }
   }

   public ChunkSource getChunkSource() {
      return this.chunkSource;
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

   boolean isPendingReset() {
      boolean var1;
      if (this.pendingResetPositionUs != -9223372036854775807L) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isReady() {
      boolean var1;
      if (this.loadingFinished || !this.isPendingReset() && this.primarySampleQueue.hasNextSample()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void maybeThrowError() throws IOException {
      this.loader.maybeThrowError();
      if (!this.loader.isLoading()) {
         this.chunkSource.maybeThrowError();
      }

   }

   public void onLoadCanceled(Chunk var1, long var2, long var4, boolean var6) {
      this.eventDispatcher.loadCanceled(var1.dataSpec, var1.getUri(), var1.getResponseHeaders(), var1.type, this.primaryTrackType, var1.trackFormat, var1.trackSelectionReason, var1.trackSelectionData, var1.startTimeUs, var1.endTimeUs, var2, var4, var1.bytesLoaded());
      if (!var6) {
         this.primarySampleQueue.reset();
         SampleQueue[] var9 = this.embeddedSampleQueues;
         int var7 = var9.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            var9[var8].reset();
         }

         this.callback.onContinueLoadingRequested(this);
      }

   }

   public void onLoadCompleted(Chunk var1, long var2, long var4) {
      this.chunkSource.onChunkLoadCompleted(var1);
      this.eventDispatcher.loadCompleted(var1.dataSpec, var1.getUri(), var1.getResponseHeaders(), var1.type, this.primaryTrackType, var1.trackFormat, var1.trackSelectionReason, var1.trackSelectionData, var1.startTimeUs, var1.endTimeUs, var2, var4, var1.bytesLoaded());
      this.callback.onContinueLoadingRequested(this);
   }

   public Loader.LoadErrorAction onLoadError(Chunk var1, long var2, long var4, IOException var6, int var7) {
      long var8 = var1.bytesLoaded();
      boolean var10 = this.isMediaChunk(var1);
      int var11 = this.mediaChunks.size() - 1;
      boolean var12;
      if (var8 != 0L && var10 && this.haveReadFromMediaChunk(var11)) {
         var12 = false;
      } else {
         var12 = true;
      }

      long var13;
      if (var12) {
         var13 = this.loadErrorHandlingPolicy.getBlacklistDurationMsFor(var1.type, var4, var6, var7);
      } else {
         var13 = -9223372036854775807L;
      }

      Loader.LoadErrorAction var15 = null;
      Loader.LoadErrorAction var16 = var15;
      if (this.chunkSource.onChunkLoadError(var1, var12, var6, var13)) {
         if (var12) {
            var15 = Loader.DONT_RETRY;
            var16 = var15;
            if (var10) {
               if (this.discardUpstreamMediaChunksFromIndex(var11) == var1) {
                  var12 = true;
               } else {
                  var12 = false;
               }

               Assertions.checkState(var12);
               var16 = var15;
               if (this.mediaChunks.isEmpty()) {
                  this.pendingResetPositionUs = this.lastSeekPositionUs;
                  var16 = var15;
               }
            }
         } else {
            Log.w("ChunkSampleStream", "Ignoring attempt to cancel non-cancelable load.");
            var16 = var15;
         }
      }

      var15 = var16;
      if (var16 == null) {
         var13 = this.loadErrorHandlingPolicy.getRetryDelayMsFor(var1.type, var4, var6, var7);
         if (var13 != -9223372036854775807L) {
            var16 = Loader.createRetryAction(false, var13);
         } else {
            var16 = Loader.DONT_RETRY_FATAL;
         }

         var15 = var16;
      }

      var12 = var15.isRetry() ^ true;
      this.eventDispatcher.loadError(var1.dataSpec, var1.getUri(), var1.getResponseHeaders(), var1.type, this.primaryTrackType, var1.trackFormat, var1.trackSelectionReason, var1.trackSelectionData, var1.startTimeUs, var1.endTimeUs, var2, var4, var8, var6, var12);
      if (var12) {
         this.callback.onContinueLoadingRequested(this);
      }

      return var15;
   }

   public void onLoaderReleased() {
      this.primarySampleQueue.reset();
      SampleQueue[] var1 = this.embeddedSampleQueues;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         var1[var3].reset();
      }

      ChunkSampleStream.ReleaseCallback var4 = this.releaseCallback;
      if (var4 != null) {
         var4.onSampleStreamReleased(this);
      }

   }

   public int readData(FormatHolder var1, DecoderInputBuffer var2, boolean var3) {
      if (this.isPendingReset()) {
         return -3;
      } else {
         this.maybeNotifyPrimaryTrackFormatChanged();
         return this.primarySampleQueue.read(var1, var2, var3, this.loadingFinished, this.decodeOnlyUntilPositionUs);
      }
   }

   public void reevaluateBuffer(long var1) {
      if (!this.loader.isLoading() && !this.isPendingReset()) {
         int var3 = this.mediaChunks.size();
         int var4 = this.chunkSource.getPreferredQueueSize(var1, this.readOnlyMediaChunks);
         int var5 = var4;
         if (var3 <= var4) {
            return;
         }

         while(true) {
            if (var5 >= var3) {
               var5 = var3;
               break;
            }

            if (!this.haveReadFromMediaChunk(var5)) {
               break;
            }

            ++var5;
         }

         if (var5 == var3) {
            return;
         }

         var1 = this.getLastMediaChunk().endTimeUs;
         BaseMediaChunk var6 = this.discardUpstreamMediaChunksFromIndex(var5);
         if (this.mediaChunks.isEmpty()) {
            this.pendingResetPositionUs = this.lastSeekPositionUs;
         }

         this.loadingFinished = false;
         this.eventDispatcher.upstreamDiscarded(this.primaryTrackType, var6.startTimeUs, var1);
      }

   }

   public void release() {
      this.release((ChunkSampleStream.ReleaseCallback)null);
   }

   public void release(ChunkSampleStream.ReleaseCallback var1) {
      this.releaseCallback = var1;
      this.primarySampleQueue.discardToEnd();
      SampleQueue[] var4 = this.embeddedSampleQueues;
      int var2 = var4.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         var4[var3].discardToEnd();
      }

      this.loader.release(this);
   }

   public void seekToUs(long var1) {
      this.lastSeekPositionUs = var1;
      if (this.isPendingReset()) {
         this.pendingResetPositionUs = var1;
      } else {
         SampleQueue var3 = null;
         byte var4 = 0;
         int var5 = 0;

         BaseMediaChunk var6;
         while(true) {
            var6 = var3;
            if (var5 >= this.mediaChunks.size()) {
               break;
            }

            var6 = (BaseMediaChunk)this.mediaChunks.get(var5);
            long var7 = var6.startTimeUs;
            if (var7 == var1 && var6.clippedStartTimeUs == -9223372036854775807L) {
               break;
            }

            if (var7 > var1) {
               var6 = var3;
               break;
            }

            ++var5;
         }

         this.primarySampleQueue.rewind();
         boolean var9;
         if (var6 != null) {
            var9 = this.primarySampleQueue.setReadPosition(var6.getFirstSampleIndex(0));
            this.decodeOnlyUntilPositionUs = 0L;
         } else {
            SampleQueue var12 = this.primarySampleQueue;
            if (var1 < this.getNextLoadPositionUs()) {
               var9 = true;
            } else {
               var9 = false;
            }

            if (var12.advanceTo(var1, true, var9) != -1) {
               var9 = true;
            } else {
               var9 = false;
            }

            this.decodeOnlyUntilPositionUs = this.lastSeekPositionUs;
         }

         SampleQueue[] var13;
         if (var9) {
            this.nextNotifyPrimaryFormatMediaChunkIndex = this.primarySampleIndexToMediaChunkIndex(this.primarySampleQueue.getReadIndex(), 0);
            var13 = this.embeddedSampleQueues;
            int var11 = var13.length;

            for(var5 = 0; var5 < var11; ++var5) {
               var3 = var13[var5];
               var3.rewind();
               var3.advanceTo(var1, true, false);
            }
         } else {
            this.pendingResetPositionUs = var1;
            this.loadingFinished = false;
            this.mediaChunks.clear();
            this.nextNotifyPrimaryFormatMediaChunkIndex = 0;
            if (this.loader.isLoading()) {
               this.loader.cancelLoading();
            } else {
               this.primarySampleQueue.reset();
               var13 = this.embeddedSampleQueues;
               int var10 = var13.length;

               for(var5 = var4; var5 < var10; ++var5) {
                  var13[var5].reset();
               }
            }
         }

      }
   }

   public ChunkSampleStream.EmbeddedSampleStream selectEmbeddedTrack(long var1, int var3) {
      for(int var4 = 0; var4 < this.embeddedSampleQueues.length; ++var4) {
         if (this.embeddedTrackTypes[var4] == var3) {
            Assertions.checkState(this.embeddedTracksSelected[var4] ^ true);
            this.embeddedTracksSelected[var4] = true;
            this.embeddedSampleQueues[var4].rewind();
            this.embeddedSampleQueues[var4].advanceTo(var1, true, true);
            return new ChunkSampleStream.EmbeddedSampleStream(this, this.embeddedSampleQueues[var4], var4);
         }
      }

      throw new IllegalStateException();
   }

   public int skipData(long var1) {
      boolean var3 = this.isPendingReset();
      byte var4 = 0;
      if (var3) {
         return 0;
      } else {
         int var5;
         if (this.loadingFinished && var1 > this.primarySampleQueue.getLargestQueuedTimestampUs()) {
            var5 = this.primarySampleQueue.advanceToEnd();
         } else {
            var5 = this.primarySampleQueue.advanceTo(var1, true, true);
            if (var5 == -1) {
               var5 = var4;
            }
         }

         this.maybeNotifyPrimaryTrackFormatChanged();
         return var5;
      }
   }

   public final class EmbeddedSampleStream implements SampleStream {
      private final int index;
      private boolean notifiedDownstreamFormat;
      public final ChunkSampleStream parent;
      private final SampleQueue sampleQueue;

      public EmbeddedSampleStream(ChunkSampleStream var2, SampleQueue var3, int var4) {
         this.parent = var2;
         this.sampleQueue = var3;
         this.index = var4;
      }

      private void maybeNotifyDownstreamFormat() {
         if (!this.notifiedDownstreamFormat) {
            ChunkSampleStream.this.eventDispatcher.downstreamFormatChanged(ChunkSampleStream.this.embeddedTrackTypes[this.index], ChunkSampleStream.this.embeddedTrackFormats[this.index], 0, (Object)null, ChunkSampleStream.this.lastSeekPositionUs);
            this.notifiedDownstreamFormat = true;
         }

      }

      public boolean isReady() {
         ChunkSampleStream var1 = ChunkSampleStream.this;
         boolean var2;
         if (var1.loadingFinished || !var1.isPendingReset() && this.sampleQueue.hasNextSample()) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      public void maybeThrowError() throws IOException {
      }

      public int readData(FormatHolder var1, DecoderInputBuffer var2, boolean var3) {
         if (ChunkSampleStream.this.isPendingReset()) {
            return -3;
         } else {
            this.maybeNotifyDownstreamFormat();
            SampleQueue var4 = this.sampleQueue;
            ChunkSampleStream var5 = ChunkSampleStream.this;
            return var4.read(var1, var2, var3, var5.loadingFinished, var5.decodeOnlyUntilPositionUs);
         }
      }

      public void release() {
         Assertions.checkState(ChunkSampleStream.this.embeddedTracksSelected[this.index]);
         ChunkSampleStream.this.embeddedTracksSelected[this.index] = false;
      }

      public int skipData(long var1) {
         boolean var3 = ChunkSampleStream.this.isPendingReset();
         int var4 = 0;
         if (var3) {
            return 0;
         } else {
            this.maybeNotifyDownstreamFormat();
            if (ChunkSampleStream.this.loadingFinished && var1 > this.sampleQueue.getLargestQueuedTimestampUs()) {
               var4 = this.sampleQueue.advanceToEnd();
            } else {
               int var5 = this.sampleQueue.advanceTo(var1, true, true);
               if (var5 != -1) {
                  var4 = var5;
               }
            }

            return var4;
         }
      }
   }

   public interface ReleaseCallback {
      void onSampleStreamReleased(ChunkSampleStream var1);
   }
}
