package com.google.android.exoplayer2.source.dash;

import android.os.SystemClock;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.ChunkIndex;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.extractor.mkv.MatroskaExtractor;
import com.google.android.exoplayer2.extractor.mp4.FragmentedMp4Extractor;
import com.google.android.exoplayer2.extractor.mp4.Track;
import com.google.android.exoplayer2.extractor.rawcc.RawCcExtractor;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.chunk.BaseMediaChunkIterator;
import com.google.android.exoplayer2.source.chunk.Chunk;
import com.google.android.exoplayer2.source.chunk.ChunkExtractorWrapper;
import com.google.android.exoplayer2.source.chunk.ChunkHolder;
import com.google.android.exoplayer2.source.chunk.ContainerMediaChunk;
import com.google.android.exoplayer2.source.chunk.InitializationChunk;
import com.google.android.exoplayer2.source.chunk.MediaChunk;
import com.google.android.exoplayer2.source.chunk.MediaChunkIterator;
import com.google.android.exoplayer2.source.chunk.SingleSampleMediaChunk;
import com.google.android.exoplayer2.source.dash.manifest.AdaptationSet;
import com.google.android.exoplayer2.source.dash.manifest.DashManifest;
import com.google.android.exoplayer2.source.dash.manifest.RangedUri;
import com.google.android.exoplayer2.source.dash.manifest.Representation;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.LoaderErrorThrower;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultDashChunkSource implements DashChunkSource {
   private final int[] adaptationSetIndices;
   private final DataSource dataSource;
   private final long elapsedRealtimeOffsetMs;
   private IOException fatalError;
   private long liveEdgeTimeUs;
   private DashManifest manifest;
   private final LoaderErrorThrower manifestLoaderErrorThrower;
   private final int maxSegmentsPerLoad;
   private boolean missingLastSegment;
   private int periodIndex;
   private final PlayerEmsgHandler.PlayerTrackEmsgHandler playerTrackEmsgHandler;
   protected final DefaultDashChunkSource.RepresentationHolder[] representationHolders;
   private final TrackSelection trackSelection;
   private final int trackType;

   public DefaultDashChunkSource(LoaderErrorThrower var1, DashManifest var2, int var3, int[] var4, TrackSelection var5, int var6, DataSource var7, long var8, int var10, boolean var11, boolean var12, PlayerEmsgHandler.PlayerTrackEmsgHandler var13) {
      this.manifestLoaderErrorThrower = var1;
      this.manifest = var2;
      this.adaptationSetIndices = var4;
      this.trackSelection = var5;
      this.trackType = var6;
      this.dataSource = var7;
      this.periodIndex = var3;
      this.elapsedRealtimeOffsetMs = var8;
      this.maxSegmentsPerLoad = var10;
      this.playerTrackEmsgHandler = var13;
      var8 = var2.getPeriodDurationUs(var3);
      this.liveEdgeTimeUs = -9223372036854775807L;
      ArrayList var14 = this.getRepresentations();
      this.representationHolders = new DefaultDashChunkSource.RepresentationHolder[var5.length()];

      for(var3 = 0; var3 < this.representationHolders.length; ++var3) {
         Representation var15 = (Representation)var14.get(var5.getIndexInTrackGroup(var3));
         this.representationHolders[var3] = new DefaultDashChunkSource.RepresentationHolder(var8, var6, var15, var11, var12, var13);
      }

   }

   private long getNowUnixTimeUs() {
      long var1;
      if (this.elapsedRealtimeOffsetMs != 0L) {
         var1 = SystemClock.elapsedRealtime() + this.elapsedRealtimeOffsetMs;
      } else {
         var1 = System.currentTimeMillis();
      }

      return var1 * 1000L;
   }

   private ArrayList getRepresentations() {
      List var1 = this.manifest.getPeriod(this.periodIndex).adaptationSets;
      ArrayList var2 = new ArrayList();
      int[] var3 = this.adaptationSetIndices;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         var2.addAll(((AdaptationSet)var1.get(var3[var5])).representations);
      }

      return var2;
   }

   private long getSegmentNum(DefaultDashChunkSource.RepresentationHolder var1, MediaChunk var2, long var3, long var5, long var7) {
      if (var2 != null) {
         var3 = var2.getNextChunkIndex();
      } else {
         var3 = Util.constrainValue(var1.getSegmentNum(var3), var5, var7);
      }

      return var3;
   }

   private long resolveTimeToLiveEdgeUs(long var1) {
      boolean var3;
      if (this.manifest.dynamic && this.liveEdgeTimeUs != -9223372036854775807L) {
         var3 = true;
      } else {
         var3 = false;
      }

      if (var3) {
         var1 = this.liveEdgeTimeUs - var1;
      } else {
         var1 = -9223372036854775807L;
      }

      return var1;
   }

   private void updateLiveEdgeTimeUs(DefaultDashChunkSource.RepresentationHolder var1, long var2) {
      if (this.manifest.dynamic) {
         var2 = var1.getSegmentEndTimeUs(var2);
      } else {
         var2 = -9223372036854775807L;
      }

      this.liveEdgeTimeUs = var2;
   }

   public long getAdjustedSeekPositionUs(long var1, SeekParameters var3) {
      DefaultDashChunkSource.RepresentationHolder[] var4 = this.representationHolders;
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         DefaultDashChunkSource.RepresentationHolder var7 = var4[var6];
         if (var7.segmentIndex != null) {
            long var8 = var7.getSegmentNum(var1);
            long var10 = var7.getSegmentStartTimeUs(var8);
            if (var10 < var1 && var8 < (long)(var7.getSegmentCount() - 1)) {
               var8 = var7.getSegmentStartTimeUs(var8 + 1L);
            } else {
               var8 = var10;
            }

            return Util.resolveSeekPositionUs(var1, var3, var10, var8);
         }
      }

      return var1;
   }

   public void getNextChunk(long var1, long var3, List var5, ChunkHolder var6) {
      if (this.fatalError == null) {
         long var7 = this.resolveTimeToLiveEdgeUs(var1);
         long var9 = C.msToUs(this.manifest.availabilityStartTimeMs);
         long var11 = C.msToUs(this.manifest.getPeriod(this.periodIndex).startMs);
         PlayerEmsgHandler.PlayerTrackEmsgHandler var13 = this.playerTrackEmsgHandler;
         if (var13 == null || !var13.maybeRefreshManifestBeforeLoadingNextChunk(var9 + var11 + var3)) {
            var9 = this.getNowUnixTimeUs();
            MediaChunk var14;
            if (var5.isEmpty()) {
               var14 = null;
            } else {
               var14 = (MediaChunk)var5.get(var5.size() - 1);
            }

            MediaChunkIterator[] var24 = new MediaChunkIterator[this.trackSelection.length()];

            int var15;
            DefaultDashChunkSource.RepresentationHolder var16;
            long var19;
            for(var15 = 0; var15 < var24.length; ++var15) {
               var16 = this.representationHolders[var15];
               if (var16.segmentIndex == null) {
                  var24[var15] = MediaChunkIterator.EMPTY;
               } else {
                  long var17 = var16.getFirstAvailableSegmentNum(this.manifest, this.periodIndex, var9);
                  var19 = var16.getLastAvailableSegmentNum(this.manifest, this.periodIndex, var9);
                  var11 = this.getSegmentNum(var16, var14, var3, var17, var19);
                  if (var11 < var17) {
                     var24[var15] = MediaChunkIterator.EMPTY;
                  } else {
                     var24[var15] = new DefaultDashChunkSource.RepresentationSegmentIterator(var16, var11, var19);
                  }
               }
            }

            this.trackSelection.updateSelectedTrack(var1, var3 - var1, var7, var5, var24);
            var16 = this.representationHolders[this.trackSelection.getSelectedIndex()];
            ChunkExtractorWrapper var25 = var16.extractorWrapper;
            if (var25 != null) {
               Representation var21 = var16.representation;
               RangedUri var26;
               if (var25.getSampleFormats() == null) {
                  var26 = var21.getInitializationUri();
               } else {
                  var26 = null;
               }

               RangedUri var27;
               if (var16.segmentIndex == null) {
                  var27 = var21.getIndexUri();
               } else {
                  var27 = null;
               }

               if (var26 != null || var27 != null) {
                  var6.chunk = this.newInitializationChunk(var16, this.dataSource, this.trackSelection.getSelectedFormat(), this.trackSelection.getSelectionReason(), this.trackSelection.getSelectionData(), var26, var27);
                  return;
               }
            }

            var7 = var16.periodDurationUs;
            var1 = -9223372036854775807L;
            boolean var23;
            if (var7 != -9223372036854775807L) {
               var23 = true;
            } else {
               var23 = false;
            }

            if (var16.getSegmentCount() == 0) {
               var6.endOfStream = var23;
            } else {
               var11 = var16.getFirstAvailableSegmentNum(this.manifest, this.periodIndex, var9);
               var19 = var16.getLastAvailableSegmentNum(this.manifest, this.periodIndex, var9);
               this.updateLiveEdgeTimeUs(var16, var19);
               var9 = this.getSegmentNum(var16, var14, var3, var11, var19);
               if (var9 < var11) {
                  this.fatalError = new BehindLiveWindowException();
               } else if (var9 > var19 || this.missingLastSegment && var9 >= var19) {
                  var6.endOfStream = var23;
               } else if (var23 && var16.getSegmentStartTimeUs(var9) >= var7) {
                  var6.endOfStream = true;
               } else {
                  var15 = (int)Math.min((long)this.maxSegmentsPerLoad, var19 - var9 + 1L);
                  int var22 = var15;
                  if (var7 != -9223372036854775807L) {
                     while(true) {
                        var22 = var15;
                        if (var15 <= 1) {
                           break;
                        }

                        var22 = var15;
                        if (var16.getSegmentStartTimeUs((long)var15 + var9 - 1L) < var7) {
                           break;
                        }

                        --var15;
                     }
                  }

                  if (var5.isEmpty()) {
                     var1 = var3;
                  }

                  var6.chunk = this.newMediaChunk(var16, this.dataSource, this.trackType, this.trackSelection.getSelectedFormat(), this.trackSelection.getSelectionReason(), this.trackSelection.getSelectionData(), var9, var22, var1);
               }
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

   protected Chunk newInitializationChunk(DefaultDashChunkSource.RepresentationHolder var1, DataSource var2, Format var3, int var4, Object var5, RangedUri var6, RangedUri var7) {
      String var8 = var1.representation.baseUrl;
      RangedUri var9 = var7;
      if (var6 != null) {
         var7 = var6.attemptMerge(var7, var8);
         var9 = var7;
         if (var7 == null) {
            var9 = var6;
         }
      }

      return new InitializationChunk(var2, new DataSpec(var9.resolveUri(var8), var9.start, var9.length, var1.representation.getCacheKey()), var3, var4, var5, var1.extractorWrapper);
   }

   protected Chunk newMediaChunk(DefaultDashChunkSource.RepresentationHolder var1, DataSource var2, int var3, Format var4, int var5, Object var6, long var7, int var9, long var10) {
      Representation var12 = var1.representation;
      long var13 = var1.getSegmentStartTimeUs(var7);
      RangedUri var15 = var1.getSegmentUrl(var7);
      String var16 = var12.baseUrl;
      if (var1.extractorWrapper == null) {
         var10 = var1.getSegmentEndTimeUs(var7);
         return new SingleSampleMediaChunk(var2, new DataSpec(var15.resolveUri(var16), var15.start, var15.length, var12.getCacheKey()), var4, var5, var6, var13, var10, var7, var3, var4);
      } else {
         int var17 = 1;

         RangedUri var18;
         for(var3 = 1; var17 < var9; var15 = var18) {
            var18 = var15.attemptMerge(var1.getSegmentUrl((long)var17 + var7), var16);
            if (var18 == null) {
               break;
            }

            ++var3;
            ++var17;
         }

         long var19 = var1.getSegmentEndTimeUs((long)var3 + var7 - 1L);
         long var21 = var1.periodDurationUs;
         if (var21 == -9223372036854775807L || var21 > var19) {
            var21 = -9223372036854775807L;
         }

         return new ContainerMediaChunk(var2, new DataSpec(var15.resolveUri(var16), var15.start, var15.length, var12.getCacheKey()), var4, var5, var6, var13, var19, var10, var21, var7, var3, -var12.presentationTimeOffsetUs, var1.extractorWrapper);
      }
   }

   public void onChunkLoadCompleted(Chunk var1) {
      if (var1 instanceof InitializationChunk) {
         InitializationChunk var2 = (InitializationChunk)var1;
         int var3 = this.trackSelection.indexOf(var2.trackFormat);
         DefaultDashChunkSource.RepresentationHolder var4 = this.representationHolders[var3];
         if (var4.segmentIndex == null) {
            SeekMap var5 = var4.extractorWrapper.getSeekMap();
            if (var5 != null) {
               this.representationHolders[var3] = var4.copyWithNewSegmentIndex(new DashWrappingSegmentIndex((ChunkIndex)var5, var4.representation.presentationTimeOffsetUs));
            }
         }
      }

      PlayerEmsgHandler.PlayerTrackEmsgHandler var6 = this.playerTrackEmsgHandler;
      if (var6 != null) {
         var6.onChunkLoadCompleted(var1);
      }

   }

   public boolean onChunkLoadError(Chunk var1, boolean var2, Exception var3, long var4) {
      boolean var6 = false;
      if (!var2) {
         return false;
      } else {
         PlayerEmsgHandler.PlayerTrackEmsgHandler var7 = this.playerTrackEmsgHandler;
         if (var7 != null && var7.maybeRefreshManifestOnLoadingError(var1)) {
            return true;
         } else {
            if (!this.manifest.dynamic && var1 instanceof MediaChunk && var3 instanceof HttpDataSource.InvalidResponseCodeException && ((HttpDataSource.InvalidResponseCodeException)var3).responseCode == 404) {
               DefaultDashChunkSource.RepresentationHolder var13 = this.representationHolders[this.trackSelection.indexOf(var1.trackFormat)];
               int var8 = var13.getSegmentCount();
               if (var8 != -1 && var8 != 0) {
                  long var9 = var13.getFirstSegmentNum();
                  long var11 = (long)var8;
                  if (((MediaChunk)var1).getNextChunkIndex() > var9 + var11 - 1L) {
                     this.missingLastSegment = true;
                     return true;
                  }
               }
            }

            var2 = var6;
            if (var4 != -9223372036854775807L) {
               TrackSelection var14 = this.trackSelection;
               var2 = var6;
               if (var14.blacklist(var14.indexOf(var1.trackFormat), var4)) {
                  var2 = true;
               }
            }

            return var2;
         }
      }
   }

   public void updateManifest(DashManifest param1, int param2) {
      // $FF: Couldn't be decompiled
   }

   public static final class Factory implements DashChunkSource.Factory {
      private final DataSource.Factory dataSourceFactory;
      private final int maxSegmentsPerLoad;

      public Factory(DataSource.Factory var1) {
         this(var1, 1);
      }

      public Factory(DataSource.Factory var1, int var2) {
         this.dataSourceFactory = var1;
         this.maxSegmentsPerLoad = var2;
      }

      public DashChunkSource createDashChunkSource(LoaderErrorThrower var1, DashManifest var2, int var3, int[] var4, TrackSelection var5, int var6, long var7, boolean var9, boolean var10, PlayerEmsgHandler.PlayerTrackEmsgHandler var11, TransferListener var12) {
         DataSource var13 = this.dataSourceFactory.createDataSource();
         if (var12 != null) {
            var13.addTransferListener(var12);
         }

         return new DefaultDashChunkSource(var1, var2, var3, var4, var5, var6, var13, var7, this.maxSegmentsPerLoad, var9, var10, var11);
      }
   }

   protected static final class RepresentationHolder {
      final ChunkExtractorWrapper extractorWrapper;
      private final long periodDurationUs;
      public final Representation representation;
      public final DashSegmentIndex segmentIndex;
      private final long segmentNumShift;

      RepresentationHolder(long var1, int var3, Representation var4, boolean var5, boolean var6, TrackOutput var7) {
         this(var1, var4, createExtractorWrapper(var3, var4, var5, var6, var7), 0L, var4.getIndex());
      }

      private RepresentationHolder(long var1, Representation var3, ChunkExtractorWrapper var4, long var5, DashSegmentIndex var7) {
         this.periodDurationUs = var1;
         this.representation = var3;
         this.segmentNumShift = var5;
         this.extractorWrapper = var4;
         this.segmentIndex = var7;
      }

      private static ChunkExtractorWrapper createExtractorWrapper(int var0, Representation var1, boolean var2, boolean var3, TrackOutput var4) {
         String var5 = var1.format.containerMimeType;
         if (mimeTypeIsRawText(var5)) {
            return null;
         } else {
            Object var7;
            if ("application/x-rawcc".equals(var5)) {
               var7 = new RawCcExtractor(var1.format);
            } else if (mimeTypeIsWebm(var5)) {
               var7 = new MatroskaExtractor(1);
            } else {
               byte var6;
               if (var2) {
                  var6 = 4;
               } else {
                  var6 = 0;
               }

               List var8;
               if (var3) {
                  var8 = Collections.singletonList(Format.createTextSampleFormat((String)null, "application/cea-608", 0, (String)null));
               } else {
                  var8 = Collections.emptyList();
               }

               var7 = new FragmentedMp4Extractor(var6, (TimestampAdjuster)null, (Track)null, (DrmInitData)null, var8, var4);
            }

            return new ChunkExtractorWrapper((Extractor)var7, var0, var1.format);
         }
      }

      private static boolean mimeTypeIsRawText(String var0) {
         boolean var1;
         if (!MimeTypes.isText(var0) && !"application/ttml+xml".equals(var0)) {
            var1 = false;
         } else {
            var1 = true;
         }

         return var1;
      }

      private static boolean mimeTypeIsWebm(String var0) {
         boolean var1;
         if (!var0.startsWith("video/webm") && !var0.startsWith("audio/webm") && !var0.startsWith("application/webm")) {
            var1 = false;
         } else {
            var1 = true;
         }

         return var1;
      }

      DefaultDashChunkSource.RepresentationHolder copyWithNewRepresentation(long var1, Representation var3) throws BehindLiveWindowException {
         DashSegmentIndex var4 = this.representation.getIndex();
         DashSegmentIndex var5 = var3.getIndex();
         if (var4 == null) {
            return new DefaultDashChunkSource.RepresentationHolder(var1, var3, this.extractorWrapper, this.segmentNumShift, var4);
         } else if (!var4.isExplicit()) {
            return new DefaultDashChunkSource.RepresentationHolder(var1, var3, this.extractorWrapper, this.segmentNumShift, var5);
         } else {
            int var6 = var4.getSegmentCount(var1);
            if (var6 == 0) {
               return new DefaultDashChunkSource.RepresentationHolder(var1, var3, this.extractorWrapper, this.segmentNumShift, var5);
            } else {
               long var7 = var4.getFirstSegmentNum() + (long)var6 - 1L;
               long var9 = var4.getTimeUs(var7) + var4.getDurationUs(var7, var1);
               long var11 = var5.getFirstSegmentNum();
               long var13 = var5.getTimeUs(var11);
               long var15 = this.segmentNumShift;
               if (var9 == var13) {
                  var13 = var7 + 1L;
               } else {
                  if (var9 < var13) {
                     throw new BehindLiveWindowException();
                  }

                  var13 = var4.getSegmentNum(var13, var1);
               }

               return new DefaultDashChunkSource.RepresentationHolder(var1, var3, this.extractorWrapper, var15 + (var13 - var11), var5);
            }
         }
      }

      DefaultDashChunkSource.RepresentationHolder copyWithNewSegmentIndex(DashSegmentIndex var1) {
         return new DefaultDashChunkSource.RepresentationHolder(this.periodDurationUs, this.representation, this.extractorWrapper, this.segmentNumShift, var1);
      }

      public long getFirstAvailableSegmentNum(DashManifest var1, int var2, long var3) {
         if (this.getSegmentCount() == -1 && var1.timeShiftBufferDepthMs != -9223372036854775807L) {
            long var5 = C.msToUs(var1.availabilityStartTimeMs);
            long var7 = C.msToUs(var1.getPeriod(var2).startMs);
            long var9 = C.msToUs(var1.timeShiftBufferDepthMs);
            return Math.max(this.getFirstSegmentNum(), this.getSegmentNum(var3 - var5 - var7 - var9));
         } else {
            return this.getFirstSegmentNum();
         }
      }

      public long getFirstSegmentNum() {
         return this.segmentIndex.getFirstSegmentNum() + this.segmentNumShift;
      }

      public long getLastAvailableSegmentNum(DashManifest var1, int var2, long var3) {
         int var5 = this.getSegmentCount();
         if (var5 == -1) {
            var3 = this.getSegmentNum(var3 - C.msToUs(var1.availabilityStartTimeMs) - C.msToUs(var1.getPeriod(var2).startMs));
         } else {
            var3 = this.getFirstSegmentNum() + (long)var5;
         }

         return var3 - 1L;
      }

      public int getSegmentCount() {
         return this.segmentIndex.getSegmentCount(this.periodDurationUs);
      }

      public long getSegmentEndTimeUs(long var1) {
         return this.getSegmentStartTimeUs(var1) + this.segmentIndex.getDurationUs(var1 - this.segmentNumShift, this.periodDurationUs);
      }

      public long getSegmentNum(long var1) {
         return this.segmentIndex.getSegmentNum(var1, this.periodDurationUs) + this.segmentNumShift;
      }

      public long getSegmentStartTimeUs(long var1) {
         return this.segmentIndex.getTimeUs(var1 - this.segmentNumShift);
      }

      public RangedUri getSegmentUrl(long var1) {
         return this.segmentIndex.getSegmentUrl(var1 - this.segmentNumShift);
      }
   }

   protected static final class RepresentationSegmentIterator extends BaseMediaChunkIterator {
      private final DefaultDashChunkSource.RepresentationHolder representationHolder;

      public RepresentationSegmentIterator(DefaultDashChunkSource.RepresentationHolder var1, long var2, long var4) {
         super(var2, var4);
         this.representationHolder = var1;
      }
   }
}
