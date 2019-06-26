package com.google.android.exoplayer2.source.hls;

import android.net.Uri;
import android.os.SystemClock;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.chunk.BaseMediaChunkIterator;
import com.google.android.exoplayer2.source.chunk.Chunk;
import com.google.android.exoplayer2.source.chunk.DataChunk;
import com.google.android.exoplayer2.source.chunk.MediaChunkIterator;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker;
import com.google.android.exoplayer2.trackselection.BaseTrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.UriUtil;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

class HlsChunkSource {
   private final DataSource encryptionDataSource;
   private byte[] encryptionIv;
   private String encryptionIvString;
   private byte[] encryptionKey;
   private Uri encryptionKeyUri;
   private HlsMasterPlaylist.HlsUrl expectedPlaylistUrl;
   private final HlsExtractorFactory extractorFactory;
   private IOException fatalError;
   private boolean independentSegments;
   private boolean isTimestampMaster;
   private long liveEdgeInPeriodTimeUs;
   private final DataSource mediaDataSource;
   private final List muxedCaptionFormats;
   private final HlsPlaylistTracker playlistTracker;
   private byte[] scratchSpace;
   private boolean seenExpectedPlaylistError;
   private final TimestampAdjusterProvider timestampAdjusterProvider;
   private final TrackGroup trackGroup;
   private TrackSelection trackSelection;
   private final HlsMasterPlaylist.HlsUrl[] variants;

   public HlsChunkSource(HlsExtractorFactory var1, HlsPlaylistTracker var2, HlsMasterPlaylist.HlsUrl[] var3, HlsDataSourceFactory var4, TransferListener var5, TimestampAdjusterProvider var6, List var7) {
      this.extractorFactory = var1;
      this.playlistTracker = var2;
      this.variants = var3;
      this.timestampAdjusterProvider = var6;
      this.muxedCaptionFormats = var7;
      this.liveEdgeInPeriodTimeUs = -9223372036854775807L;
      Format[] var10 = new Format[var3.length];
      int[] var9 = new int[var3.length];

      for(int var8 = 0; var8 < var3.length; var9[var8] = var8++) {
         var10[var8] = var3[var8].format;
      }

      this.mediaDataSource = var4.createDataSource(1);
      if (var5 != null) {
         this.mediaDataSource.addTransferListener(var5);
      }

      this.encryptionDataSource = var4.createDataSource(3);
      this.trackGroup = new TrackGroup(var10);
      this.trackSelection = new HlsChunkSource.InitializationTrackSelection(this.trackGroup, var9);
   }

   private void clearEncryptionData() {
      this.encryptionKeyUri = null;
      this.encryptionKey = null;
      this.encryptionIvString = null;
      this.encryptionIv = null;
   }

   private long getChunkMediaSequence(HlsMediaChunk var1, boolean var2, HlsMediaPlaylist var3, long var4, long var6) {
      if (var1 != null && !var2) {
         return var1.getNextChunkIndex();
      } else {
         long var8 = var3.durationUs;
         long var10 = var6;
         if (var1 != null) {
            if (this.independentSegments) {
               var10 = var6;
            } else {
               var10 = var1.startTimeUs;
            }
         }

         if (!var3.hasEndTag && var10 >= var8 + var4) {
            var4 = var3.mediaSequence;
            var6 = (long)var3.segments.size();
         } else {
            List var12 = var3.segments;
            if (this.playlistTracker.isLive() && var1 != null) {
               var2 = false;
            } else {
               var2 = true;
            }

            var4 = (long)Util.binarySearchFloor(var12, var10 - var4, true, var2);
            var6 = var3.mediaSequence;
         }

         return var4 + var6;
      }
   }

   private HlsChunkSource.EncryptionKeyChunk newEncryptionKeyChunk(Uri var1, String var2, int var3, int var4, Object var5) {
      DataSpec var6 = new DataSpec(var1, 0L, -1L, (String)null, 1);
      return new HlsChunkSource.EncryptionKeyChunk(this.encryptionDataSource, var6, this.variants[var3].format, var4, var5, this.scratchSpace, var2);
   }

   private long resolveTimeToLiveEdgeUs(long var1) {
      long var3 = this.liveEdgeInPeriodTimeUs;
      long var5 = -9223372036854775807L;
      boolean var7;
      if (var3 != -9223372036854775807L) {
         var7 = true;
      } else {
         var7 = false;
      }

      if (var7) {
         var5 = this.liveEdgeInPeriodTimeUs - var1;
      }

      return var5;
   }

   private void setEncryptionData(Uri var1, String var2, byte[] var3) {
      String var4;
      if (Util.toLowerInvariant(var2).startsWith("0x")) {
         var4 = var2.substring(2);
      } else {
         var4 = var2;
      }

      byte[] var7 = (new BigInteger(var4, 16)).toByteArray();
      byte[] var5 = new byte[16];
      int var6;
      if (var7.length > 16) {
         var6 = var7.length - 16;
      } else {
         var6 = 0;
      }

      System.arraycopy(var7, var6, var5, var5.length - var7.length + var6, var7.length - var6);
      this.encryptionKeyUri = var1;
      this.encryptionKey = var3;
      this.encryptionIvString = var2;
      this.encryptionIv = var5;
   }

   private void updateLiveEdgeTimeUs(HlsMediaPlaylist var1) {
      long var2;
      if (var1.hasEndTag) {
         var2 = -9223372036854775807L;
      } else {
         var2 = var1.getEndTimeUs() - this.playlistTracker.getInitialStartTimeUs();
      }

      this.liveEdgeInPeriodTimeUs = var2;
   }

   public MediaChunkIterator[] createMediaChunkIterators(HlsMediaChunk var1, long var2) {
      int var4;
      if (var1 == null) {
         var4 = -1;
      } else {
         var4 = this.trackGroup.indexOf(var1.trackFormat);
      }

      MediaChunkIterator[] var5 = new MediaChunkIterator[this.trackSelection.length()];

      for(int var6 = 0; var6 < var5.length; ++var6) {
         int var7 = this.trackSelection.getIndexInTrackGroup(var6);
         HlsMasterPlaylist.HlsUrl var8 = this.variants[var7];
         if (!this.playlistTracker.isSnapshotValid(var8)) {
            var5[var6] = MediaChunkIterator.EMPTY;
         } else {
            HlsMediaPlaylist var16 = this.playlistTracker.getPlaylistSnapshot(var8, false);
            long var9 = var16.startTimeUs - this.playlistTracker.getInitialStartTimeUs();
            boolean var11;
            if (var7 != var4) {
               var11 = true;
            } else {
               var11 = false;
            }

            long var12 = this.getChunkMediaSequence(var1, var11, var16, var9, var2);
            long var14 = var16.mediaSequence;
            if (var12 < var14) {
               var5[var6] = MediaChunkIterator.EMPTY;
            } else {
               var5[var6] = new HlsChunkSource.HlsMediaPlaylistSegmentIterator(var16, var9, (int)(var12 - var14));
            }
         }
      }

      return var5;
   }

   public void getNextChunk(long var1, long var3, List var5, HlsChunkSource.HlsChunkHolder var6) {
      HlsMediaChunk var7;
      if (var5.isEmpty()) {
         var7 = null;
      } else {
         var7 = (HlsMediaChunk)var5.get(var5.size() - 1);
      }

      int var8;
      if (var7 == null) {
         var8 = -1;
      } else {
         var8 = this.trackGroup.indexOf(var7.trackFormat);
      }

      long var9;
      long var13;
      label79: {
         var9 = var3 - var1;
         long var11 = this.resolveTimeToLiveEdgeUs(var1);
         var13 = var9;
         if (var7 != null) {
            var13 = var9;
            if (!this.independentSegments) {
               long var15 = var7.getDurationUs();
               var9 = Math.max(0L, var9 - var15);
               var13 = var9;
               if (var11 != -9223372036854775807L) {
                  var11 = Math.max(0L, var11 - var15);
                  var13 = var9;
                  var9 = var11;
                  break label79;
               }
            }
         }

         var9 = var11;
      }

      MediaChunkIterator[] var17 = this.createMediaChunkIterators(var7, var3);
      this.trackSelection.updateSelectedTrack(var1, var13, var9, var5, var17);
      int var18 = this.trackSelection.getSelectedIndexInTrackGroup();
      boolean var19 = false;
      boolean var20 = false;
      boolean var21;
      if (var8 != var18) {
         var21 = true;
      } else {
         var21 = false;
      }

      HlsMasterPlaylist.HlsUrl var25 = this.variants[var18];
      boolean var26;
      if (!this.playlistTracker.isSnapshotValid(var25)) {
         var6.playlist = var25;
         var21 = this.seenExpectedPlaylistError;
         var26 = var20;
         if (this.expectedPlaylistUrl == var25) {
            var26 = true;
         }

         this.seenExpectedPlaylistError = var21 & var26;
         this.expectedPlaylistUrl = var25;
      } else {
         HlsMediaPlaylist var27 = this.playlistTracker.getPlaylistSnapshot(var25, true);
         this.independentSegments = var27.hasIndependentSegments;
         this.updateLiveEdgeTimeUs(var27);
         var13 = var27.startTimeUs - this.playlistTracker.getInitialStartTimeUs();
         var1 = this.getChunkMediaSequence(var7, var21, var27, var13, var3);
         if (var1 < var27.mediaSequence) {
            if (var7 == null || !var21) {
               this.fatalError = new BehindLiveWindowException();
               return;
            }

            var25 = this.variants[var8];
            var27 = this.playlistTracker.getPlaylistSnapshot(var25, true);
            var3 = var27.startTimeUs - this.playlistTracker.getInitialStartTimeUs();
            var1 = var7.getNextChunkIndex();
         } else {
            var8 = var18;
            var3 = var13;
         }

         int var29 = (int)(var1 - var27.mediaSequence);
         if (var29 >= var27.segments.size()) {
            if (var27.hasEndTag) {
               var6.endOfStream = true;
            } else {
               var6.playlist = var25;
               var21 = this.seenExpectedPlaylistError;
               var26 = var19;
               if (this.expectedPlaylistUrl == var25) {
                  var26 = true;
               }

               this.seenExpectedPlaylistError = var21 & var26;
               this.expectedPlaylistUrl = var25;
            }

         } else {
            this.seenExpectedPlaylistError = false;
            DataSpec var22 = null;
            this.expectedPlaylistUrl = null;
            HlsMediaPlaylist.Segment var23 = (HlsMediaPlaylist.Segment)var27.segments.get(var29);
            String var24 = var23.fullSegmentEncryptionKeyUri;
            if (var24 != null) {
               Uri var30 = UriUtil.resolveToUri(var27.baseUri, var24);
               if (!var30.equals(this.encryptionKeyUri)) {
                  var6.chunk = this.newEncryptionKeyChunk(var30, var23.encryptionIV, var8, this.trackSelection.getSelectionReason(), this.trackSelection.getSelectionData());
                  return;
               }

               if (!Util.areEqual(var23.encryptionIV, this.encryptionIvString)) {
                  this.setEncryptionData(var30, var23.encryptionIV, this.encryptionKey);
               }
            } else {
               this.clearEncryptionData();
            }

            HlsMediaPlaylist.Segment var31 = var23.initializationSegment;
            if (var31 != null) {
               var22 = new DataSpec(UriUtil.resolveToUri(var27.baseUri, var31.url), var31.byterangeOffset, var31.byterangeLength, (String)null);
            }

            var3 += var23.relativeStartTimeUs;
            var8 = var27.discontinuitySequence + var23.relativeDiscontinuitySequence;
            TimestampAdjuster var32 = this.timestampAdjusterProvider.getAdjuster(var8);
            DataSpec var28 = new DataSpec(UriUtil.resolveToUri(var27.baseUri, var23.url), var23.byterangeOffset, var23.byterangeLength, (String)null);
            var6.chunk = new HlsMediaChunk(this.extractorFactory, this.mediaDataSource, var28, var22, var25, this.muxedCaptionFormats, this.trackSelection.getSelectionReason(), this.trackSelection.getSelectionData(), var3, var3 + var23.durationUs, var1, var8, var23.hasGapTag, this.isTimestampMaster, var32, var7, var23.drmInitData, this.encryptionKey, this.encryptionIv);
         }
      }
   }

   public TrackGroup getTrackGroup() {
      return this.trackGroup;
   }

   public TrackSelection getTrackSelection() {
      return this.trackSelection;
   }

   public boolean maybeBlacklistTrack(Chunk var1, long var2) {
      TrackSelection var4 = this.trackSelection;
      return var4.blacklist(var4.indexOf(this.trackGroup.indexOf(var1.trackFormat)), var2);
   }

   public void maybeThrowError() throws IOException {
      IOException var1 = this.fatalError;
      if (var1 == null) {
         HlsMasterPlaylist.HlsUrl var2 = this.expectedPlaylistUrl;
         if (var2 != null && this.seenExpectedPlaylistError) {
            this.playlistTracker.maybeThrowPlaylistRefreshError(var2);
         }

      } else {
         throw var1;
      }
   }

   public void onChunkLoadCompleted(Chunk var1) {
      if (var1 instanceof HlsChunkSource.EncryptionKeyChunk) {
         HlsChunkSource.EncryptionKeyChunk var2 = (HlsChunkSource.EncryptionKeyChunk)var1;
         this.scratchSpace = var2.getDataHolder();
         this.setEncryptionData(var2.dataSpec.uri, var2.iv, var2.getResult());
      }

   }

   public boolean onPlaylistError(HlsMasterPlaylist.HlsUrl var1, long var2) {
      int var4 = this.trackGroup.indexOf(var1.format);
      boolean var5 = true;
      if (var4 == -1) {
         return true;
      } else {
         int var6 = this.trackSelection.indexOf(var4);
         if (var6 == -1) {
            return true;
         } else {
            boolean var7 = this.seenExpectedPlaylistError;
            boolean var8;
            if (this.expectedPlaylistUrl == var1) {
               var8 = true;
            } else {
               var8 = false;
            }

            this.seenExpectedPlaylistError = var8 | var7;
            var7 = var5;
            if (var2 != -9223372036854775807L) {
               if (this.trackSelection.blacklist(var6, var2)) {
                  var7 = var5;
               } else {
                  var7 = false;
               }
            }

            return var7;
         }
      }
   }

   public void reset() {
      this.fatalError = null;
   }

   public void selectTracks(TrackSelection var1) {
      this.trackSelection = var1;
   }

   public void setIsTimestampMaster(boolean var1) {
      this.isTimestampMaster = var1;
   }

   private static final class EncryptionKeyChunk extends DataChunk {
      public final String iv;
      private byte[] result;

      public EncryptionKeyChunk(DataSource var1, DataSpec var2, Format var3, int var4, Object var5, byte[] var6, String var7) {
         super(var1, var2, 3, var3, var4, var5, var6);
         this.iv = var7;
      }

      protected void consume(byte[] var1, int var2) throws IOException {
         this.result = Arrays.copyOf(var1, var2);
      }

      public byte[] getResult() {
         return this.result;
      }
   }

   public static final class HlsChunkHolder {
      public Chunk chunk;
      public boolean endOfStream;
      public HlsMasterPlaylist.HlsUrl playlist;

      public HlsChunkHolder() {
         this.clear();
      }

      public void clear() {
         this.chunk = null;
         this.endOfStream = false;
         this.playlist = null;
      }
   }

   private static final class HlsMediaPlaylistSegmentIterator extends BaseMediaChunkIterator {
      private final HlsMediaPlaylist playlist;
      private final long startOfPlaylistInPeriodUs;

      public HlsMediaPlaylistSegmentIterator(HlsMediaPlaylist var1, long var2, int var4) {
         super((long)var4, (long)(var1.segments.size() - 1));
         this.playlist = var1;
         this.startOfPlaylistInPeriodUs = var2;
      }
   }

   private static final class InitializationTrackSelection extends BaseTrackSelection {
      private int selectedIndex;

      public InitializationTrackSelection(TrackGroup var1, int[] var2) {
         super(var1, var2);
         this.selectedIndex = this.indexOf(var1.getFormat(0));
      }

      public int getSelectedIndex() {
         return this.selectedIndex;
      }

      public Object getSelectionData() {
         return null;
      }

      public int getSelectionReason() {
         return 0;
      }

      public void updateSelectedTrack(long var1, long var3, long var5, List var7, MediaChunkIterator[] var8) {
         var1 = SystemClock.elapsedRealtime();
         if (this.isBlacklisted(this.selectedIndex, var1)) {
            for(int var9 = super.length - 1; var9 >= 0; --var9) {
               if (!this.isBlacklisted(var9, var1)) {
                  this.selectedIndex = var9;
                  return;
               }
            }

            throw new IllegalStateException();
         }
      }
   }
}
