package com.google.android.exoplayer2.source.hls;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.source.CompositeSequenceableLoaderFactory;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.SampleStream;
import com.google.android.exoplayer2.source.SequenceableLoader;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;

public final class HlsMediaPeriod implements MediaPeriod, HlsSampleStreamWrapper.Callback, HlsPlaylistTracker.PlaylistEventListener {
   private final Allocator allocator;
   private final boolean allowChunklessPreparation;
   private MediaPeriod.Callback callback;
   private SequenceableLoader compositeSequenceableLoader;
   private final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
   private final HlsDataSourceFactory dataSourceFactory;
   private HlsSampleStreamWrapper[] enabledSampleStreamWrappers;
   private final MediaSourceEventListener.EventDispatcher eventDispatcher;
   private final HlsExtractorFactory extractorFactory;
   private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
   private final TransferListener mediaTransferListener;
   private boolean notifiedReadingStarted;
   private int pendingPrepareCount;
   private final HlsPlaylistTracker playlistTracker;
   private HlsSampleStreamWrapper[] sampleStreamWrappers;
   private final IdentityHashMap streamWrapperIndices;
   private final TimestampAdjusterProvider timestampAdjusterProvider;
   private TrackGroupArray trackGroups;

   public HlsMediaPeriod(HlsExtractorFactory var1, HlsPlaylistTracker var2, HlsDataSourceFactory var3, TransferListener var4, LoadErrorHandlingPolicy var5, MediaSourceEventListener.EventDispatcher var6, Allocator var7, CompositeSequenceableLoaderFactory var8, boolean var9) {
      this.extractorFactory = var1;
      this.playlistTracker = var2;
      this.dataSourceFactory = var3;
      this.mediaTransferListener = var4;
      this.loadErrorHandlingPolicy = var5;
      this.eventDispatcher = var6;
      this.allocator = var7;
      this.compositeSequenceableLoaderFactory = var8;
      this.allowChunklessPreparation = var9;
      this.compositeSequenceableLoader = var8.createCompositeSequenceableLoader();
      this.streamWrapperIndices = new IdentityHashMap();
      this.timestampAdjusterProvider = new TimestampAdjusterProvider();
      this.sampleStreamWrappers = new HlsSampleStreamWrapper[0];
      this.enabledSampleStreamWrappers = new HlsSampleStreamWrapper[0];
      var6.mediaPeriodCreated();
   }

   private void buildAndPrepareMainSampleStreamWrapper(HlsMasterPlaylist var1, long var2) {
      ArrayList var4 = new ArrayList(var1.variants);
      ArrayList var5 = new ArrayList();
      ArrayList var6 = new ArrayList();

      int var7;
      for(var7 = 0; var7 < var4.size(); ++var7) {
         HlsMasterPlaylist.HlsUrl var8 = (HlsMasterPlaylist.HlsUrl)var4.get(var7);
         Format var9 = var8.format;
         if (var9.height <= 0 && Util.getCodecsOfType(var9.codecs, 2) == null) {
            if (Util.getCodecsOfType(var9.codecs, 1) != null) {
               var6.add(var8);
            }
         } else {
            var5.add(var8);
         }
      }

      if (var5.isEmpty()) {
         if (var6.size() < var4.size()) {
            var4.removeAll(var6);
         }

         var5 = var4;
      }

      Assertions.checkArgument(var5.isEmpty() ^ true);
      HlsMasterPlaylist.HlsUrl[] var15 = (HlsMasterPlaylist.HlsUrl[])var5.toArray(new HlsMasterPlaylist.HlsUrl[0]);
      String var17 = var15[0].format.codecs;
      HlsSampleStreamWrapper var18 = this.buildSampleStreamWrapper(0, var15, var1.muxedAudioFormat, var1.muxedCaptionFormats, var2);
      this.sampleStreamWrappers[0] = var18;
      if (this.allowChunklessPreparation && var17 != null) {
         boolean var10;
         if (Util.getCodecsOfType(var17, 2) != null) {
            var10 = true;
         } else {
            var10 = false;
         }

         boolean var16;
         if (Util.getCodecsOfType(var17, 1) != null) {
            var16 = true;
         } else {
            var16 = false;
         }

         var4 = new ArrayList();
         Format[] var14;
         if (var10) {
            var14 = new Format[var5.size()];

            for(int var19 = 0; var19 < var14.length; ++var19) {
               var14[var19] = deriveVideoFormat(var15[var19].format);
            }

            var4.add(new TrackGroup(var14));
            if (var16 && (var1.muxedAudioFormat != null || var1.audios.isEmpty())) {
               var4.add(new TrackGroup(new Format[]{deriveAudioFormat(var15[0].format, var1.muxedAudioFormat, false)}));
            }

            List var12 = var1.muxedCaptionFormats;
            if (var12 != null) {
               for(var7 = 0; var7 < var12.size(); ++var7) {
                  var4.add(new TrackGroup(new Format[]{(Format)var12.get(var7)}));
               }
            }
         } else {
            if (!var16) {
               StringBuilder var11 = new StringBuilder();
               var11.append("Unexpected codecs attribute: ");
               var11.append(var17);
               throw new IllegalArgumentException(var11.toString());
            }

            var14 = new Format[var5.size()];

            for(var7 = 0; var7 < var14.length; ++var7) {
               var14[var7] = deriveAudioFormat(var15[var7].format, var1.muxedAudioFormat, true);
            }

            var4.add(new TrackGroup(var14));
         }

         TrackGroup var13 = new TrackGroup(new Format[]{Format.createSampleFormat("ID3", "application/id3", (String)null, -1, (DrmInitData)null)});
         var4.add(var13);
         var18.prepareWithMasterPlaylistInfo(new TrackGroupArray((TrackGroup[])var4.toArray(new TrackGroup[0])), 0, new TrackGroupArray(new TrackGroup[]{var13}));
      } else {
         var18.setIsTimestampMaster(true);
         var18.continuePreparing();
      }

   }

   private void buildAndPrepareSampleStreamWrappers(long var1) {
      HlsMasterPlaylist var3 = this.playlistTracker.getMasterPlaylist();
      List var4 = var3.audios;
      List var5 = var3.subtitles;
      int var6 = var4.size() + 1 + var5.size();
      this.sampleStreamWrappers = new HlsSampleStreamWrapper[var6];
      this.pendingPrepareCount = var6;
      this.buildAndPrepareMainSampleStreamWrapper(var3, var1);
      int var7 = 0;

      List var9;
      HlsSampleStreamWrapper var10;
      for(var6 = 1; var7 < var4.size(); ++var6) {
         HlsMasterPlaylist.HlsUrl var8 = (HlsMasterPlaylist.HlsUrl)var4.get(var7);
         var9 = Collections.emptyList();
         var10 = this.buildSampleStreamWrapper(1, new HlsMasterPlaylist.HlsUrl[]{var8}, (Format)null, var9, var1);
         this.sampleStreamWrappers[var6] = var10;
         Format var12 = var8.format;
         if (this.allowChunklessPreparation && var12.codecs != null) {
            var10.prepareWithMasterPlaylistInfo(new TrackGroupArray(new TrackGroup[]{new TrackGroup(new Format[]{var12})}), 0, TrackGroupArray.EMPTY);
         } else {
            var10.continuePreparing();
         }

         ++var7;
      }

      for(var7 = 0; var7 < var5.size(); ++var6) {
         HlsMasterPlaylist.HlsUrl var11 = (HlsMasterPlaylist.HlsUrl)var5.get(var7);
         var9 = Collections.emptyList();
         var10 = this.buildSampleStreamWrapper(3, new HlsMasterPlaylist.HlsUrl[]{var11}, (Format)null, var9, var1);
         this.sampleStreamWrappers[var6] = var10;
         var10.prepareWithMasterPlaylistInfo(new TrackGroupArray(new TrackGroup[]{new TrackGroup(new Format[]{var11.format})}), 0, TrackGroupArray.EMPTY);
         ++var7;
      }

      this.enabledSampleStreamWrappers = this.sampleStreamWrappers;
   }

   private HlsSampleStreamWrapper buildSampleStreamWrapper(int var1, HlsMasterPlaylist.HlsUrl[] var2, Format var3, List var4, long var5) {
      return new HlsSampleStreamWrapper(var1, this, new HlsChunkSource(this.extractorFactory, this.playlistTracker, var2, this.dataSourceFactory, this.mediaTransferListener, this.timestampAdjusterProvider, var4), this.allocator, var5, var3, this.loadErrorHandlingPolicy, this.eventDispatcher);
   }

   private static Format deriveAudioFormat(Format var0, Format var1, boolean var2) {
      String var3;
      int var4;
      int var5;
      String var6;
      String var9;
      if (var1 != null) {
         var3 = var1.codecs;
         var4 = var1.channelCount;
         var5 = var1.selectionFlags;
         var6 = var1.language;
         var9 = var1.label;
      } else {
         var3 = Util.getCodecsOfType(var0.codecs, 1);
         if (var2) {
            var4 = var0.channelCount;
            var5 = var0.selectionFlags;
            var9 = var0.label;
            var6 = var9;
         } else {
            var9 = null;
            var6 = var9;
            var4 = -1;
            var5 = 0;
         }
      }

      String var7 = MimeTypes.getMediaMimeType(var3);
      int var8;
      if (var2) {
         var8 = var0.bitrate;
      } else {
         var8 = -1;
      }

      return Format.createAudioContainerFormat(var0.id, var9, var0.containerMimeType, var7, var3, var8, var4, -1, (List)null, var5, var6);
   }

   private static Format deriveVideoFormat(Format var0) {
      String var1 = Util.getCodecsOfType(var0.codecs, 2);
      String var2 = MimeTypes.getMediaMimeType(var1);
      return Format.createVideoContainerFormat(var0.id, var0.label, var0.containerMimeType, var2, var1, var0.bitrate, var0.width, var0.height, var0.frameRate, (List)null, var0.selectionFlags);
   }

   public boolean continueLoading(long var1) {
      if (this.trackGroups != null) {
         return this.compositeSequenceableLoader.continueLoading(var1);
      } else {
         HlsSampleStreamWrapper[] var3 = this.sampleStreamWrappers;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            var3[var5].continuePreparing();
         }

         return false;
      }
   }

   public void discardBuffer(long var1, boolean var3) {
      HlsSampleStreamWrapper[] var4 = this.enabledSampleStreamWrappers;
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         var4[var6].discardBuffer(var1, var3);
      }

   }

   public long getAdjustedSeekPositionUs(long var1, SeekParameters var3) {
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
      HlsSampleStreamWrapper[] var1 = this.sampleStreamWrappers;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         var1[var3].maybeThrowPrepareError();
      }

   }

   public void onContinueLoadingRequested(HlsSampleStreamWrapper var1) {
      this.callback.onContinueLoadingRequested(this);
   }

   public void onPlaylistChanged() {
      this.callback.onContinueLoadingRequested(this);
   }

   public boolean onPlaylistError(HlsMasterPlaylist.HlsUrl var1, long var2) {
      HlsSampleStreamWrapper[] var4 = this.sampleStreamWrappers;
      int var5 = var4.length;
      boolean var6 = true;

      for(int var7 = 0; var7 < var5; ++var7) {
         var6 &= var4[var7].onPlaylistError(var1, var2);
      }

      this.callback.onContinueLoadingRequested(this);
      return var6;
   }

   public void onPlaylistRefreshRequired(HlsMasterPlaylist.HlsUrl var1) {
      this.playlistTracker.refreshPlaylist(var1);
   }

   public void onPrepared() {
      int var1 = this.pendingPrepareCount - 1;
      this.pendingPrepareCount = var1;
      if (var1 <= 0) {
         HlsSampleStreamWrapper[] var2 = this.sampleStreamWrappers;
         int var3 = var2.length;
         int var4 = 0;

         for(var1 = 0; var4 < var3; ++var4) {
            var1 += var2[var4].getTrackGroups().length;
         }

         TrackGroup[] var5 = new TrackGroup[var1];
         var2 = this.sampleStreamWrappers;
         int var6 = var2.length;
         var4 = 0;

         for(var1 = 0; var4 < var6; ++var4) {
            HlsSampleStreamWrapper var7 = var2[var4];
            int var8 = var7.getTrackGroups().length;

            for(var3 = 0; var3 < var8; ++var1) {
               var5[var1] = var7.getTrackGroups().get(var3);
               ++var3;
            }
         }

         this.trackGroups = new TrackGroupArray(var5);
         this.callback.onPrepared(this);
      }
   }

   public void prepare(MediaPeriod.Callback var1, long var2) {
      this.callback = var1;
      this.playlistTracker.addListener(this);
      this.buildAndPrepareSampleStreamWrappers(var2);
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
      this.playlistTracker.removeListener(this);
      HlsSampleStreamWrapper[] var1 = this.sampleStreamWrappers;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         var1[var3].release();
      }

      this.callback = null;
      this.eventDispatcher.mediaPeriodReleased();
   }

   public long seekToUs(long var1) {
      HlsSampleStreamWrapper[] var3 = this.enabledSampleStreamWrappers;
      if (var3.length > 0) {
         boolean var4 = var3[0].seekToUs(var1, false);
         int var5 = 1;

         while(true) {
            var3 = this.enabledSampleStreamWrappers;
            if (var5 >= var3.length) {
               if (var4) {
                  this.timestampAdjusterProvider.reset();
               }
               break;
            }

            var3[var5].seekToUs(var1, var4);
            ++var5;
         }
      }

      return var1;
   }

   public long selectTracks(TrackSelection[] var1, boolean[] var2, SampleStream[] var3, boolean[] var4, long var5) {
      SampleStream[] var7 = var3;
      int[] var8 = new int[var1.length];
      int[] var9 = new int[var1.length];

      int var10;
      int var11;
      HlsSampleStreamWrapper[] var13;
      for(var10 = 0; var10 < var1.length; ++var10) {
         if (var7[var10] == null) {
            var11 = -1;
         } else {
            var11 = (Integer)this.streamWrapperIndices.get(var7[var10]);
         }

         var8[var10] = var11;
         var9[var10] = -1;
         if (var1[var10] != null) {
            TrackGroup var12 = var1[var10].getTrackGroup();
            var11 = 0;

            while(true) {
               var13 = this.sampleStreamWrappers;
               if (var11 >= var13.length) {
                  break;
               }

               if (var13[var11].getTrackGroups().indexOf(var12) != -1) {
                  var9[var10] = var11;
                  break;
               }

               ++var11;
            }
         }
      }

      this.streamWrapperIndices.clear();
      SampleStream[] var14 = new SampleStream[var1.length];
      SampleStream[] var15 = new SampleStream[var1.length];
      TrackSelection[] var23 = new TrackSelection[var1.length];
      var13 = new HlsSampleStreamWrapper[this.sampleStreamWrappers.length];
      var10 = 0;
      var11 = 0;

      for(boolean var16 = false; var11 < this.sampleStreamWrappers.length; ++var11) {
         int var17;
         HlsSampleStreamWrapper[] var19;
         for(var17 = 0; var17 < var1.length; ++var17) {
            int var18 = var8[var17];
            var19 = null;
            SampleStream var24;
            if (var18 == var11) {
               var24 = var3[var17];
            } else {
               var24 = null;
            }

            var15[var17] = var24;
            TrackSelection var25 = var19;
            if (var9[var17] == var11) {
               var25 = var1[var17];
            }

            var23[var17] = var25;
         }

         HlsSampleStreamWrapper var26 = this.sampleStreamWrappers[var11];
         boolean var20 = var26.selectTracks(var23, var2, var15, var4, var5, var16);
         var17 = 0;
         boolean var27 = false;

         while(true) {
            int var21 = var1.length;
            boolean var22 = true;
            if (var17 >= var21) {
               if (var27) {
                  label86: {
                     var13[var10] = var26;
                     var17 = var10 + 1;
                     if (var10 == 0) {
                        var26.setIsTimestampMaster(true);
                        if (var20) {
                           break label86;
                        }

                        var19 = this.enabledSampleStreamWrappers;
                        if (var19.length == 0 || var26 != var19[0]) {
                           break label86;
                        }
                     } else {
                        var26.setIsTimestampMaster(false);
                     }

                     var10 = var17;
                     break;
                  }

                  this.timestampAdjusterProvider.reset();
                  var10 = var17;
                  var16 = true;
               }
               break;
            }

            boolean var28;
            if (var9[var17] == var11) {
               if (var15[var17] != null) {
                  var22 = true;
               } else {
                  var22 = false;
               }

               Assertions.checkState(var22);
               var14[var17] = var15[var17];
               this.streamWrapperIndices.put(var15[var17], var11);
               var28 = true;
            } else {
               var28 = var27;
               if (var8[var17] == var11) {
                  if (var15[var17] != null) {
                     var22 = false;
                  }

                  Assertions.checkState(var22);
                  var28 = var27;
               }
            }

            ++var17;
            var27 = var28;
         }
      }

      System.arraycopy(var14, 0, var3, 0, var14.length);
      this.enabledSampleStreamWrappers = (HlsSampleStreamWrapper[])Arrays.copyOf(var13, var10);
      this.compositeSequenceableLoader = this.compositeSequenceableLoaderFactory.createCompositeSequenceableLoader(this.enabledSampleStreamWrappers);
      return var5;
   }
}
