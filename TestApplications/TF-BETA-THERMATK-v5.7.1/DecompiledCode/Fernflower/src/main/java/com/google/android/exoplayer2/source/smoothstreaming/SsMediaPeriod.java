package com.google.android.exoplayer2.source.smoothstreaming;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.source.CompositeSequenceableLoaderFactory;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.SampleStream;
import com.google.android.exoplayer2.source.SequenceableLoader;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.chunk.ChunkSampleStream;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifest;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy;
import com.google.android.exoplayer2.upstream.LoaderErrorThrower;
import com.google.android.exoplayer2.upstream.TransferListener;
import java.io.IOException;
import java.util.ArrayList;

final class SsMediaPeriod implements MediaPeriod, SequenceableLoader.Callback {
   private final Allocator allocator;
   private MediaPeriod.Callback callback;
   private final SsChunkSource.Factory chunkSourceFactory;
   private SequenceableLoader compositeSequenceableLoader;
   private final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
   private final MediaSourceEventListener.EventDispatcher eventDispatcher;
   private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
   private SsManifest manifest;
   private final LoaderErrorThrower manifestLoaderErrorThrower;
   private boolean notifiedReadingStarted;
   private ChunkSampleStream[] sampleStreams;
   private final TrackGroupArray trackGroups;
   private final TransferListener transferListener;

   public SsMediaPeriod(SsManifest var1, SsChunkSource.Factory var2, TransferListener var3, CompositeSequenceableLoaderFactory var4, LoadErrorHandlingPolicy var5, MediaSourceEventListener.EventDispatcher var6, LoaderErrorThrower var7, Allocator var8) {
      this.manifest = var1;
      this.chunkSourceFactory = var2;
      this.transferListener = var3;
      this.manifestLoaderErrorThrower = var7;
      this.loadErrorHandlingPolicy = var5;
      this.eventDispatcher = var6;
      this.allocator = var8;
      this.compositeSequenceableLoaderFactory = var4;
      this.trackGroups = buildTrackGroups(var1);
      this.sampleStreams = newSampleStreamArray(0);
      this.compositeSequenceableLoader = var4.createCompositeSequenceableLoader(this.sampleStreams);
      var6.mediaPeriodCreated();
   }

   private ChunkSampleStream buildSampleStream(TrackSelection var1, long var2) {
      int var4 = this.trackGroups.indexOf(var1.getTrackGroup());
      SsChunkSource var5 = this.chunkSourceFactory.createChunkSource(this.manifestLoaderErrorThrower, this.manifest, var4, var1, this.transferListener);
      return new ChunkSampleStream(this.manifest.streamElements[var4].type, (int[])null, (Format[])null, var5, this, this.allocator, var2, this.loadErrorHandlingPolicy, this.eventDispatcher);
   }

   private static TrackGroupArray buildTrackGroups(SsManifest var0) {
      TrackGroup[] var1 = new TrackGroup[var0.streamElements.length];
      int var2 = 0;

      while(true) {
         SsManifest.StreamElement[] var3 = var0.streamElements;
         if (var2 >= var3.length) {
            return new TrackGroupArray(var1);
         }

         var1[var2] = new TrackGroup(var3[var2].formats);
         ++var2;
      }
   }

   private static ChunkSampleStream[] newSampleStreamArray(int var0) {
      return new ChunkSampleStream[var0];
   }

   public boolean continueLoading(long var1) {
      return this.compositeSequenceableLoader.continueLoading(var1);
   }

   public void discardBuffer(long var1, boolean var3) {
      ChunkSampleStream[] var4 = this.sampleStreams;
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         var4[var6].discardBuffer(var1, var3);
      }

   }

   public long getAdjustedSeekPositionUs(long var1, SeekParameters var3) {
      ChunkSampleStream[] var4 = this.sampleStreams;
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         ChunkSampleStream var7 = var4[var6];
         if (var7.primaryTrackType == 2) {
            return var7.getAdjustedSeekPositionUs(var1, var3);
         }
      }

      return var1;
   }

   public long getBufferedPositionUs() {
      return this.compositeSequenceableLoader.getBufferedPositionUs();
   }

   public long getNextLoadPositionUs() {
      return this.compositeSequenceableLoader.getNextLoadPositionUs();
   }

   public TrackGroupArray getTrackGroups() {
      return this.trackGroups;
   }

   public void maybeThrowPrepareError() throws IOException {
      this.manifestLoaderErrorThrower.maybeThrowError();
   }

   public void onContinueLoadingRequested(ChunkSampleStream var1) {
      this.callback.onContinueLoadingRequested(this);
   }

   public void prepare(MediaPeriod.Callback var1, long var2) {
      this.callback = var1;
      var1.onPrepared(this);
   }

   public long readDiscontinuity() {
      if (!this.notifiedReadingStarted) {
         this.eventDispatcher.readingStarted();
         this.notifiedReadingStarted = true;
      }

      return -9223372036854775807L;
   }

   public void reevaluateBuffer(long var1) {
      this.compositeSequenceableLoader.reevaluateBuffer(var1);
   }

   public void release() {
      ChunkSampleStream[] var1 = this.sampleStreams;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         var1[var3].release();
      }

      this.callback = null;
      this.eventDispatcher.mediaPeriodReleased();
   }

   public long seekToUs(long var1) {
      ChunkSampleStream[] var3 = this.sampleStreams;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         var3[var5].seekToUs(var1);
      }

      return var1;
   }

   public long selectTracks(TrackSelection[] var1, boolean[] var2, SampleStream[] var3, boolean[] var4, long var5) {
      ArrayList var7 = new ArrayList();

      for(int var8 = 0; var8 < var1.length; ++var8) {
         ChunkSampleStream var9;
         if (var3[var8] != null) {
            var9 = (ChunkSampleStream)var3[var8];
            if (var1[var8] != null && var2[var8]) {
               var7.add(var9);
            } else {
               var9.release();
               var3[var8] = null;
            }
         }

         if (var3[var8] == null && var1[var8] != null) {
            var9 = this.buildSampleStream(var1[var8], var5);
            var7.add(var9);
            var3[var8] = var9;
            var4[var8] = true;
         }
      }

      this.sampleStreams = newSampleStreamArray(var7.size());
      var7.toArray(this.sampleStreams);
      this.compositeSequenceableLoader = this.compositeSequenceableLoaderFactory.createCompositeSequenceableLoader(this.sampleStreams);
      return var5;
   }

   public void updateManifest(SsManifest var1) {
      this.manifest = var1;
      ChunkSampleStream[] var2 = this.sampleStreams;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ((SsChunkSource)var2[var4].getChunkSource()).updateManifest(var1);
      }

      this.callback.onContinueLoadingRequested(this);
   }
}
