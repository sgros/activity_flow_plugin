package com.google.android.exoplayer2.source.smoothstreaming;

import android.net.Uri;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.mp4.FragmentedMp4Extractor;
import com.google.android.exoplayer2.extractor.mp4.Track;
import com.google.android.exoplayer2.extractor.mp4.TrackEncryptionBox;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.chunk.BaseMediaChunkIterator;
import com.google.android.exoplayer2.source.chunk.Chunk;
import com.google.android.exoplayer2.source.chunk.ChunkExtractorWrapper;
import com.google.android.exoplayer2.source.chunk.ChunkHolder;
import com.google.android.exoplayer2.source.chunk.ContainerMediaChunk;
import com.google.android.exoplayer2.source.chunk.MediaChunk;
import com.google.android.exoplayer2.source.chunk.MediaChunkIterator;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifest;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.LoaderErrorThrower;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.List;

public class DefaultSsChunkSource implements SsChunkSource {
   private int currentManifestChunkOffset;
   private final DataSource dataSource;
   private final ChunkExtractorWrapper[] extractorWrappers;
   private IOException fatalError;
   private SsManifest manifest;
   private final LoaderErrorThrower manifestLoaderErrorThrower;
   private final int streamElementIndex;
   private final TrackSelection trackSelection;

   public DefaultSsChunkSource(LoaderErrorThrower var1, SsManifest var2, int var3, TrackSelection var4, DataSource var5) {
      this.manifestLoaderErrorThrower = var1;
      this.manifest = var2;
      this.streamElementIndex = var3;
      this.trackSelection = var4;
      this.dataSource = var5;
      SsManifest.StreamElement var6 = var2.streamElements[var3];
      this.extractorWrappers = new ChunkExtractorWrapper[var4.length()];

      for(var3 = 0; var3 < this.extractorWrappers.length; ++var3) {
         int var7 = var4.getIndexInTrackGroup(var3);
         Format var11 = var6.formats[var7];
         TrackEncryptionBox[] var9;
         if (var11.drmInitData != null) {
            var9 = var2.protectionElement.trackEncryptionBoxes;
         } else {
            var9 = null;
         }

         byte var8;
         if (var6.type == 2) {
            var8 = 4;
         } else {
            var8 = 0;
         }

         FragmentedMp4Extractor var10 = new FragmentedMp4Extractor(3, (TimestampAdjuster)null, new Track(var7, var6.type, var6.timescale, -9223372036854775807L, var2.durationUs, var11, 0, var9, var8, (long[])null, (long[])null), (DrmInitData)null);
         this.extractorWrappers[var3] = new ChunkExtractorWrapper(var10, var6.type, var11);
      }

   }

   private static MediaChunk newMediaChunk(Format var0, DataSource var1, Uri var2, String var3, int var4, long var5, long var7, long var9, int var11, Object var12, ChunkExtractorWrapper var13) {
      return new ContainerMediaChunk(var1, new DataSpec(var2, 0L, -1L, var3), var0, var11, var12, var5, var7, var9, -9223372036854775807L, (long)var4, 1, var5, var13);
   }

   private long resolveTimeToLiveEdgeUs(long var1) {
      SsManifest var3 = this.manifest;
      if (!var3.isLive) {
         return -9223372036854775807L;
      } else {
         SsManifest.StreamElement var5 = var3.streamElements[this.streamElementIndex];
         int var4 = var5.chunkCount - 1;
         return var5.getStartTimeUs(var4) + var5.getChunkDurationUs(var4) - var1;
      }
   }

   public long getAdjustedSeekPositionUs(long var1, SeekParameters var3) {
      SsManifest.StreamElement var4 = this.manifest.streamElements[this.streamElementIndex];
      int var5 = var4.getChunkIndex(var1);
      long var6 = var4.getStartTimeUs(var5);
      long var8;
      if (var6 < var1 && var5 < var4.chunkCount - 1) {
         var8 = var4.getStartTimeUs(var5 + 1);
      } else {
         var8 = var6;
      }

      return Util.resolveSeekPositionUs(var1, var3, var6, var8);
   }

   public final void getNextChunk(long var1, long var3, List var5, ChunkHolder var6) {
      if (this.fatalError == null) {
         SsManifest var7 = this.manifest;
         SsManifest.StreamElement var8 = var7.streamElements[this.streamElementIndex];
         if (var8.chunkCount == 0) {
            var6.endOfStream = var7.isLive ^ true;
         } else {
            int var9;
            int var10;
            if (var5.isEmpty()) {
               var9 = var8.getChunkIndex(var3);
            } else {
               var10 = (int)(((MediaChunk)var5.get(var5.size() - 1)).getNextChunkIndex() - (long)this.currentManifestChunkOffset);
               var9 = var10;
               if (var10 < 0) {
                  this.fatalError = new BehindLiveWindowException();
                  return;
               }
            }

            if (var9 >= var8.chunkCount) {
               var6.endOfStream = this.manifest.isLive ^ true;
            } else {
               long var11 = this.resolveTimeToLiveEdgeUs(var1);
               MediaChunkIterator[] var15 = new MediaChunkIterator[this.trackSelection.length()];

               for(var10 = 0; var10 < var15.length; ++var10) {
                  var15[var10] = new DefaultSsChunkSource.StreamElementIterator(var8, this.trackSelection.getIndexInTrackGroup(var10), var9);
               }

               this.trackSelection.updateSelectedTrack(var1, var3 - var1, var11, var5, var15);
               var1 = var8.getStartTimeUs(var9);
               var11 = var8.getChunkDurationUs(var9);
               if (!var5.isEmpty()) {
                  var3 = -9223372036854775807L;
               }

               int var13 = this.currentManifestChunkOffset;
               var10 = this.trackSelection.getSelectedIndex();
               ChunkExtractorWrapper var14 = this.extractorWrappers[var10];
               Uri var16 = var8.buildRequestUri(this.trackSelection.getIndexInTrackGroup(var10), var9);
               var6.chunk = newMediaChunk(this.trackSelection.getSelectedFormat(), this.dataSource, var16, (String)null, var9 + var13, var1, var1 + var11, var3, this.trackSelection.getSelectionReason(), this.trackSelection.getSelectionData(), var14);
            }
         }
      }
   }

   public int getPreferredQueueSize(long var1, List var3) {
      return this.fatalError == null && this.trackSelection.length() >= 2 ? this.trackSelection.evaluateQueueSize(var1, var3) : var3.size();
   }

   public void maybeThrowError() throws IOException {
      IOException var1 = this.fatalError;
      if (var1 == null) {
         this.manifestLoaderErrorThrower.maybeThrowError();
      } else {
         throw var1;
      }
   }

   public void onChunkLoadCompleted(Chunk var1) {
   }

   public boolean onChunkLoadError(Chunk var1, boolean var2, Exception var3, long var4) {
      if (var2 && var4 != -9223372036854775807L) {
         TrackSelection var6 = this.trackSelection;
         if (var6.blacklist(var6.indexOf(var1.trackFormat), var4)) {
            var2 = true;
            return var2;
         }
      }

      var2 = false;
      return var2;
   }

   public void updateManifest(SsManifest var1) {
      SsManifest.StreamElement[] var2 = this.manifest.streamElements;
      int var3 = this.streamElementIndex;
      SsManifest.StreamElement var12 = var2[var3];
      int var4 = var12.chunkCount;
      SsManifest.StreamElement var5 = var1.streamElements[var3];
      if (var4 != 0 && var5.chunkCount != 0) {
         var3 = var4 - 1;
         long var6 = var12.getStartTimeUs(var3);
         long var8 = var12.getChunkDurationUs(var3);
         long var10 = var5.getStartTimeUs(0);
         if (var6 + var8 <= var10) {
            this.currentManifestChunkOffset += var4;
         } else {
            this.currentManifestChunkOffset += var12.getChunkIndex(var10);
         }
      } else {
         this.currentManifestChunkOffset += var4;
      }

      this.manifest = var1;
   }

   public static final class Factory implements SsChunkSource.Factory {
      private final DataSource.Factory dataSourceFactory;

      public Factory(DataSource.Factory var1) {
         this.dataSourceFactory = var1;
      }

      public SsChunkSource createChunkSource(LoaderErrorThrower var1, SsManifest var2, int var3, TrackSelection var4, TransferListener var5) {
         DataSource var6 = this.dataSourceFactory.createDataSource();
         if (var5 != null) {
            var6.addTransferListener(var5);
         }

         return new DefaultSsChunkSource(var1, var2, var3, var4, var6);
      }
   }

   private static final class StreamElementIterator extends BaseMediaChunkIterator {
      private final SsManifest.StreamElement streamElement;
      private final int trackIndex;

      public StreamElementIterator(SsManifest.StreamElement var1, int var2, int var3) {
         super((long)var3, (long)(var1.chunkCount - 1));
         this.streamElement = var1;
         this.trackIndex = var2;
      }
   }
}
