package com.google.android.exoplayer2.source.dash;

import android.util.Pair;
import android.util.SparseIntArray;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.source.CompositeSequenceableLoaderFactory;
import com.google.android.exoplayer2.source.EmptySampleStream;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.SampleStream;
import com.google.android.exoplayer2.source.SequenceableLoader;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.chunk.ChunkSampleStream;
import com.google.android.exoplayer2.source.dash.manifest.AdaptationSet;
import com.google.android.exoplayer2.source.dash.manifest.DashManifest;
import com.google.android.exoplayer2.source.dash.manifest.Descriptor;
import com.google.android.exoplayer2.source.dash.manifest.EventStream;
import com.google.android.exoplayer2.source.dash.manifest.Period;
import com.google.android.exoplayer2.source.dash.manifest.Representation;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy;
import com.google.android.exoplayer2.upstream.LoaderErrorThrower;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;

final class DashMediaPeriod implements MediaPeriod, SequenceableLoader.Callback, ChunkSampleStream.ReleaseCallback {
   private final Allocator allocator;
   private MediaPeriod.Callback callback;
   private final DashChunkSource.Factory chunkSourceFactory;
   private SequenceableLoader compositeSequenceableLoader;
   private final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
   private final long elapsedRealtimeOffsetMs;
   private final MediaSourceEventListener.EventDispatcher eventDispatcher;
   private EventSampleStream[] eventSampleStreams;
   private List eventStreams;
   final int id;
   private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
   private DashManifest manifest;
   private final LoaderErrorThrower manifestLoaderErrorThrower;
   private boolean notifiedReadingStarted;
   private int periodIndex;
   private final PlayerEmsgHandler playerEmsgHandler;
   private ChunkSampleStream[] sampleStreams;
   private final IdentityHashMap trackEmsgHandlerBySampleStream;
   private final DashMediaPeriod.TrackGroupInfo[] trackGroupInfos;
   private final TrackGroupArray trackGroups;
   private final TransferListener transferListener;

   public DashMediaPeriod(int var1, DashManifest var2, int var3, DashChunkSource.Factory var4, TransferListener var5, LoadErrorHandlingPolicy var6, MediaSourceEventListener.EventDispatcher var7, long var8, LoaderErrorThrower var10, Allocator var11, CompositeSequenceableLoaderFactory var12, PlayerEmsgHandler.PlayerEmsgCallback var13) {
      this.id = var1;
      this.manifest = var2;
      this.periodIndex = var3;
      this.chunkSourceFactory = var4;
      this.transferListener = var5;
      this.loadErrorHandlingPolicy = var6;
      this.eventDispatcher = var7;
      this.elapsedRealtimeOffsetMs = var8;
      this.manifestLoaderErrorThrower = var10;
      this.allocator = var11;
      this.compositeSequenceableLoaderFactory = var12;
      this.playerEmsgHandler = new PlayerEmsgHandler(var2, var13, var11);
      this.sampleStreams = newSampleStreamArray(0);
      this.eventSampleStreams = new EventSampleStream[0];
      this.trackEmsgHandlerBySampleStream = new IdentityHashMap();
      this.compositeSequenceableLoader = var12.createCompositeSequenceableLoader(this.sampleStreams);
      Period var14 = var2.getPeriod(var3);
      this.eventStreams = var14.eventStreams;
      Pair var15 = buildTrackGroups(var14.adaptationSets, this.eventStreams);
      this.trackGroups = (TrackGroupArray)var15.first;
      this.trackGroupInfos = (DashMediaPeriod.TrackGroupInfo[])var15.second;
      var7.mediaPeriodCreated();
   }

   private static void buildManifestEventTrackGroupInfos(List var0, TrackGroup[] var1, DashMediaPeriod.TrackGroupInfo[] var2, int var3) {
      for(int var4 = 0; var4 < var0.size(); ++var3) {
         var1[var3] = new TrackGroup(new Format[]{Format.createSampleFormat(((EventStream)var0.get(var4)).id(), "application/x-emsg", (String)null, -1, (DrmInitData)null)});
         var2[var3] = DashMediaPeriod.TrackGroupInfo.mpdEventTrack(var4);
         ++var4;
      }

   }

   private static int buildPrimaryAndEmbeddedTrackGroupInfos(List var0, int[][] var1, int var2, boolean[] var3, boolean[] var4, TrackGroup[] var5, DashMediaPeriod.TrackGroupInfo[] var6) {
      int var7 = 0;

      int var8;
      int var12;
      for(var8 = 0; var7 < var2; var8 = var12) {
         int[] var9 = var1[var7];
         ArrayList var10 = new ArrayList();
         int var11 = var9.length;

         for(var12 = 0; var12 < var11; ++var12) {
            var10.addAll(((AdaptationSet)var0.get(var9[var12])).representations);
         }

         Format[] var13 = new Format[var10.size()];

         for(var12 = 0; var12 < var13.length; ++var12) {
            var13[var12] = ((Representation)var10.get(var12)).format;
         }

         AdaptationSet var16 = (AdaptationSet)var0.get(var9[0]);
         var11 = var8 + 1;
         if (var3[var7]) {
            var12 = var11 + 1;
         } else {
            var12 = var11;
            var11 = -1;
         }

         int var15;
         if (var4[var7]) {
            int var14 = var12 + 1;
            var15 = var12;
            var12 = var14;
         } else {
            var15 = -1;
         }

         var5[var8] = new TrackGroup(var13);
         var6[var8] = DashMediaPeriod.TrackGroupInfo.primaryTrack(var16.type, var9, var8, var11, var15);
         StringBuilder var17;
         if (var11 != -1) {
            var17 = new StringBuilder();
            var17.append(var16.id);
            var17.append(":emsg");
            var5[var11] = new TrackGroup(new Format[]{Format.createSampleFormat(var17.toString(), "application/x-emsg", (String)null, -1, (DrmInitData)null)});
            var6[var11] = DashMediaPeriod.TrackGroupInfo.embeddedEmsgTrack(var9, var8);
         }

         if (var15 != -1) {
            var17 = new StringBuilder();
            var17.append(var16.id);
            var17.append(":cea608");
            var5[var15] = new TrackGroup(new Format[]{Format.createTextSampleFormat(var17.toString(), "application/cea-608", 0, (String)null)});
            var6[var15] = DashMediaPeriod.TrackGroupInfo.embeddedCea608Track(var9, var8);
         }

         ++var7;
      }

      return var8;
   }

   private ChunkSampleStream buildSampleStream(DashMediaPeriod.TrackGroupInfo param1, TrackSelection param2, long param3) {
      // $FF: Couldn't be decompiled
   }

   private static Pair buildTrackGroups(List var0, List var1) {
      int[][] var2 = getGroupedAdaptationSetIndices(var0);
      int var3 = var2.length;
      boolean[] var4 = new boolean[var3];
      boolean[] var5 = new boolean[var3];
      int var6 = identifyEmbeddedTracks(var3, var0, var2, var4, var5) + var3 + var1.size();
      TrackGroup[] var7 = new TrackGroup[var6];
      DashMediaPeriod.TrackGroupInfo[] var8 = new DashMediaPeriod.TrackGroupInfo[var6];
      buildManifestEventTrackGroupInfos(var1, var7, var8, buildPrimaryAndEmbeddedTrackGroupInfos(var0, var2, var3, var4, var5, var7, var8));
      return Pair.create(new TrackGroupArray(var7), var8);
   }

   private static Descriptor findAdaptationSetSwitchingProperty(List var0) {
      for(int var1 = 0; var1 < var0.size(); ++var1) {
         Descriptor var2 = (Descriptor)var0.get(var1);
         if ("urn:mpeg:dash:adaptation-set-switching:2016".equals(var2.schemeIdUri)) {
            return var2;
         }
      }

      return null;
   }

   private static int[][] getGroupedAdaptationSetIndices(List var0) {
      int var1 = var0.size();
      SparseIntArray var2 = new SparseIntArray(var1);

      int var3;
      for(var3 = 0; var3 < var1; ++var3) {
         var2.put(((AdaptationSet)var0.get(var3)).id, var3);
      }

      int[][] var4 = new int[var1][];
      boolean[] var5 = new boolean[var1];
      int var6 = 0;

      for(var3 = 0; var6 < var1; ++var6) {
         if (!var5[var6]) {
            var5[var6] = true;
            Descriptor var7 = findAdaptationSetSwitchingProperty(((AdaptationSet)var0.get(var6)).supplementalProperties);
            if (var7 == null) {
               var4[var3] = new int[]{var6};
               ++var3;
            } else {
               String[] var14 = Util.split(var7.value, ",");
               int[] var8 = new int[var14.length + 1];
               var8[0] = var6;
               int var9 = 0;

               int var10;
               int var12;
               for(var10 = 1; var9 < var14.length; var10 = var12) {
                  int var11 = var2.get(Integer.parseInt(var14[var9]), -1);
                  var12 = var10;
                  if (var11 != -1) {
                     var5[var11] = true;
                     var8[var10] = var11;
                     var12 = var10 + 1;
                  }

                  ++var9;
               }

               int[] var15 = var8;
               if (var10 < var8.length) {
                  var15 = Arrays.copyOf(var8, var10);
               }

               var4[var3] = var15;
               ++var3;
            }
         }
      }

      int[][] var13 = var4;
      if (var3 < var1) {
         var13 = (int[][])Arrays.copyOf(var4, var3);
      }

      return var13;
   }

   private int getPrimaryStreamIndex(int var1, int[] var2) {
      var1 = var2[var1];
      if (var1 == -1) {
         return -1;
      } else {
         int var3 = this.trackGroupInfos[var1].primaryTrackGroupIndex;

         for(var1 = 0; var1 < var2.length; ++var1) {
            int var4 = var2[var1];
            if (var4 == var3 && this.trackGroupInfos[var4].trackGroupCategory == 0) {
               return var1;
            }
         }

         return -1;
      }
   }

   private int[] getStreamIndexToTrackGroupIndex(TrackSelection[] var1) {
      int[] var2 = new int[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (var1[var3] != null) {
            var2[var3] = this.trackGroups.indexOf(var1[var3].getTrackGroup());
         } else {
            var2[var3] = -1;
         }
      }

      return var2;
   }

   private static boolean hasCea608Track(List var0, int[] var1) {
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         List var4 = ((AdaptationSet)var0.get(var1[var3])).accessibilityDescriptors;

         for(int var5 = 0; var5 < var4.size(); ++var5) {
            if ("urn:scte:dash:cc:cea-608:2015".equals(((Descriptor)var4.get(var5)).schemeIdUri)) {
               return true;
            }
         }
      }

      return false;
   }

   private static boolean hasEventMessageTrack(List var0, int[] var1) {
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         List var4 = ((AdaptationSet)var0.get(var1[var3])).representations;

         for(int var5 = 0; var5 < var4.size(); ++var5) {
            if (!((Representation)var4.get(var5)).inbandEventStreams.isEmpty()) {
               return true;
            }
         }
      }

      return false;
   }

   private static int identifyEmbeddedTracks(int var0, List var1, int[][] var2, boolean[] var3, boolean[] var4) {
      int var5 = 0;

      int var6;
      for(var6 = 0; var5 < var0; ++var5) {
         int var7 = var6;
         if (hasEventMessageTrack(var1, var2[var5])) {
            var3[var5] = true;
            var7 = var6 + 1;
         }

         var6 = var7;
         if (hasCea608Track(var1, var2[var5])) {
            var4[var5] = true;
            var6 = var7 + 1;
         }
      }

      return var6;
   }

   private static ChunkSampleStream[] newSampleStreamArray(int var0) {
      return new ChunkSampleStream[var0];
   }

   private void releaseDisabledStreams(TrackSelection[] var1, boolean[] var2, SampleStream[] var3) {
      for(int var4 = 0; var4 < var1.length; ++var4) {
         if (var1[var4] == null || !var2[var4]) {
            if (var3[var4] instanceof ChunkSampleStream) {
               ((ChunkSampleStream)var3[var4]).release(this);
            } else if (var3[var4] instanceof ChunkSampleStream.EmbeddedSampleStream) {
               ((ChunkSampleStream.EmbeddedSampleStream)var3[var4]).release();
            }

            var3[var4] = null;
         }
      }

   }

   private void releaseOrphanEmbeddedStreams(TrackSelection[] var1, SampleStream[] var2, int[] var3) {
      for(int var4 = 0; var4 < var1.length; ++var4) {
         if (var2[var4] instanceof EmptySampleStream || var2[var4] instanceof ChunkSampleStream.EmbeddedSampleStream) {
            int var5 = this.getPrimaryStreamIndex(var4, var3);
            boolean var6;
            if (var5 == -1) {
               var6 = var2[var4] instanceof EmptySampleStream;
            } else if (var2[var4] instanceof ChunkSampleStream.EmbeddedSampleStream && ((ChunkSampleStream.EmbeddedSampleStream)var2[var4]).parent == var2[var5]) {
               var6 = true;
            } else {
               var6 = false;
            }

            if (!var6) {
               if (var2[var4] instanceof ChunkSampleStream.EmbeddedSampleStream) {
                  ((ChunkSampleStream.EmbeddedSampleStream)var2[var4]).release();
               }

               var2[var4] = null;
            }
         }
      }

   }

   private void selectNewStreams(TrackSelection[] var1, SampleStream[] var2, boolean[] var3, long var4, int[] var6) {
      byte var7 = 0;
      int var8 = 0;

      while(true) {
         int var9 = var7;
         if (var8 >= var1.length) {
            for(; var9 < var1.length; ++var9) {
               if (var2[var9] == null && var1[var9] != null) {
                  var8 = var6[var9];
                  DashMediaPeriod.TrackGroupInfo var11 = this.trackGroupInfos[var8];
                  if (var11.trackGroupCategory == 1) {
                     var8 = this.getPrimaryStreamIndex(var9, var6);
                     if (var8 == -1) {
                        var2[var9] = new EmptySampleStream();
                     } else {
                        var2[var9] = ((ChunkSampleStream)var2[var8]).selectEmbeddedTrack(var4, var11.trackType);
                     }
                  }
               }
            }

            return;
         }

         if (var2[var8] == null && var1[var8] != null) {
            var3[var8] = true;
            var9 = var6[var8];
            DashMediaPeriod.TrackGroupInfo var10 = this.trackGroupInfos[var9];
            var9 = var10.trackGroupCategory;
            if (var9 == 0) {
               var2[var8] = this.buildSampleStream(var10, var1[var8], var4);
            } else if (var9 == 2) {
               var2[var8] = new EventSampleStream((EventStream)this.eventStreams.get(var10.eventStreamGroupIndex), var1[var8].getTrackGroup().getFormat(0), this.manifest.dynamic);
            }
         }

         ++var8;
      }
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

   public void onSampleStreamReleased(ChunkSampleStream var1) {
      synchronized(this){}

      Throwable var10000;
      label75: {
         boolean var10001;
         PlayerEmsgHandler.PlayerTrackEmsgHandler var8;
         try {
            var8 = (PlayerEmsgHandler.PlayerTrackEmsgHandler)this.trackEmsgHandlerBySampleStream.remove(var1);
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
            break label75;
         }

         if (var8 == null) {
            return;
         }

         label66:
         try {
            var8.release();
            return;
         } catch (Throwable var6) {
            var10000 = var6;
            var10001 = false;
            break label66;
         }
      }

      Throwable var9 = var10000;
      throw var9;
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
      this.playerEmsgHandler.release();
      ChunkSampleStream[] var1 = this.sampleStreams;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         var1[var3].release(this);
      }

      this.callback = null;
      this.eventDispatcher.mediaPeriodReleased();
   }

   public long seekToUs(long var1) {
      ChunkSampleStream[] var3 = this.sampleStreams;
      int var4 = var3.length;
      byte var5 = 0;

      int var6;
      for(var6 = 0; var6 < var4; ++var6) {
         var3[var6].seekToUs(var1);
      }

      EventSampleStream[] var7 = this.eventSampleStreams;
      var4 = var7.length;

      for(var6 = var5; var6 < var4; ++var6) {
         var7[var6].seekToUs(var1);
      }

      return var1;
   }

   public long selectTracks(TrackSelection[] var1, boolean[] var2, SampleStream[] var3, boolean[] var4, long var5) {
      int[] var7 = this.getStreamIndexToTrackGroupIndex(var1);
      this.releaseDisabledStreams(var1, var2, var3);
      this.releaseOrphanEmbeddedStreams(var1, var3, var7);
      this.selectNewStreams(var1, var3, var4, var5, var7);
      ArrayList var10 = new ArrayList();
      ArrayList var11 = new ArrayList();
      int var8 = var3.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         SampleStream var12 = var3[var9];
         if (var12 instanceof ChunkSampleStream) {
            var10.add((ChunkSampleStream)var12);
         } else if (var12 instanceof EventSampleStream) {
            var11.add((EventSampleStream)var12);
         }
      }

      this.sampleStreams = newSampleStreamArray(var10.size());
      var10.toArray(this.sampleStreams);
      this.eventSampleStreams = new EventSampleStream[var11.size()];
      var11.toArray(this.eventSampleStreams);
      this.compositeSequenceableLoader = this.compositeSequenceableLoaderFactory.createCompositeSequenceableLoader(this.sampleStreams);
      return var5;
   }

   public void updateManifest(DashManifest var1, int var2) {
      this.manifest = var1;
      this.periodIndex = var2;
      this.playerEmsgHandler.updateManifest(var1);
      ChunkSampleStream[] var3 = this.sampleStreams;
      int var4;
      int var5;
      if (var3 != null) {
         var4 = var3.length;

         for(var5 = 0; var5 < var4; ++var5) {
            ((DashChunkSource)var3[var5].getChunkSource()).updateManifest(var1, var2);
         }

         this.callback.onContinueLoadingRequested(this);
      }

      this.eventStreams = var1.getPeriod(var2).eventStreams;
      EventSampleStream[] var11 = this.eventSampleStreams;
      var4 = var11.length;

      for(var5 = 0; var5 < var4; ++var5) {
         EventSampleStream var6 = var11[var5];
         Iterator var7 = this.eventStreams.iterator();

         while(var7.hasNext()) {
            EventStream var8 = (EventStream)var7.next();
            if (var8.id().equals(var6.eventStreamId())) {
               int var9 = var1.getPeriodCount();
               boolean var10 = true;
               if (!var1.dynamic || var2 != var9 - 1) {
                  var10 = false;
               }

               var6.updateEventStream(var8, var10);
               break;
            }
         }
      }

   }

   private static final class TrackGroupInfo {
      public final int[] adaptationSetIndices;
      public final int embeddedCea608TrackGroupIndex;
      public final int embeddedEventMessageTrackGroupIndex;
      public final int eventStreamGroupIndex;
      public final int primaryTrackGroupIndex;
      public final int trackGroupCategory;
      public final int trackType;

      private TrackGroupInfo(int var1, int var2, int[] var3, int var4, int var5, int var6, int var7) {
         this.trackType = var1;
         this.adaptationSetIndices = var3;
         this.trackGroupCategory = var2;
         this.primaryTrackGroupIndex = var4;
         this.embeddedEventMessageTrackGroupIndex = var5;
         this.embeddedCea608TrackGroupIndex = var6;
         this.eventStreamGroupIndex = var7;
      }

      public static DashMediaPeriod.TrackGroupInfo embeddedCea608Track(int[] var0, int var1) {
         return new DashMediaPeriod.TrackGroupInfo(3, 1, var0, var1, -1, -1, -1);
      }

      public static DashMediaPeriod.TrackGroupInfo embeddedEmsgTrack(int[] var0, int var1) {
         return new DashMediaPeriod.TrackGroupInfo(4, 1, var0, var1, -1, -1, -1);
      }

      public static DashMediaPeriod.TrackGroupInfo mpdEventTrack(int var0) {
         return new DashMediaPeriod.TrackGroupInfo(4, 2, (int[])null, -1, -1, -1, var0);
      }

      public static DashMediaPeriod.TrackGroupInfo primaryTrack(int var0, int[] var1, int var2, int var3, int var4) {
         return new DashMediaPeriod.TrackGroupInfo(var0, 0, var1, var2, var3, var4, -1);
      }
   }
}
