package com.google.android.exoplayer2.source.hls;

import android.util.Pair;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.DefaultExtractorInput;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.id3.Id3Decoder;
import com.google.android.exoplayer2.metadata.id3.PrivFrame;
import com.google.android.exoplayer2.source.chunk.MediaChunk;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.Util;
import java.io.EOFException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

final class HlsMediaChunk extends MediaChunk {
   private static final AtomicInteger uidSource = new AtomicInteger();
   public final int discontinuitySequenceNumber;
   private final DrmInitData drmInitData;
   private Extractor extractor;
   private final HlsExtractorFactory extractorFactory;
   private final boolean hasGapTag;
   public final HlsMasterPlaylist.HlsUrl hlsUrl;
   private final ParsableByteArray id3Data;
   private final Id3Decoder id3Decoder;
   private final DataSource initDataSource;
   private final DataSpec initDataSpec;
   private boolean initLoadCompleted;
   private int initSegmentBytesLoaded;
   private final boolean isEncrypted;
   private final boolean isMasterTimestampSource;
   private volatile boolean loadCanceled;
   private boolean loadCompleted;
   private final List muxedCaptionFormats;
   private int nextLoadPosition;
   private HlsSampleStreamWrapper output;
   private final Extractor previousExtractor;
   private final boolean shouldSpliceIn;
   private final TimestampAdjuster timestampAdjuster;
   public final int uid;

   public HlsMediaChunk(HlsExtractorFactory var1, DataSource var2, DataSpec var3, DataSpec var4, HlsMasterPlaylist.HlsUrl var5, List var6, int var7, Object var8, long var9, long var11, long var13, int var15, boolean var16, boolean var17, TimestampAdjuster var18, HlsMediaChunk var19, DrmInitData var20, byte[] var21, byte[] var22) {
      super(buildDataSource(var2, var21, var22), var3, var5.format, var7, var8, var9, var11, var13);
      this.discontinuitySequenceNumber = var15;
      this.initDataSpec = var4;
      this.hlsUrl = var5;
      this.isMasterTimestampSource = var17;
      this.timestampAdjuster = var18;
      boolean var23 = true;
      if (var21 != null) {
         var17 = true;
      } else {
         var17 = false;
      }

      this.isEncrypted = var17;
      this.hasGapTag = var16;
      this.extractorFactory = var1;
      this.muxedCaptionFormats = var6;
      this.drmInitData = var20;
      var3 = null;
      Extractor var24;
      if (var19 != null) {
         this.id3Decoder = var19.id3Decoder;
         this.id3Data = var19.id3Data;
         var16 = var23;
         if (var19.hlsUrl == var5) {
            if (!var19.loadCompleted) {
               var16 = var23;
            } else {
               var16 = false;
            }
         }

         this.shouldSpliceIn = var16;
         var24 = var3;
         if (var19.discontinuitySequenceNumber == var15) {
            if (this.shouldSpliceIn) {
               var24 = var3;
            } else {
               var24 = var19.extractor;
            }
         }
      } else {
         this.id3Decoder = new Id3Decoder();
         this.id3Data = new ParsableByteArray(10);
         this.shouldSpliceIn = false;
         var24 = var3;
      }

      this.previousExtractor = var24;
      this.initDataSource = var2;
      this.uid = uidSource.getAndIncrement();
   }

   private static DataSource buildDataSource(DataSource var0, byte[] var1, byte[] var2) {
      return (DataSource)(var1 != null ? new Aes128DataSource(var0, var1, var2) : var0);
   }

   private void loadMedia() throws IOException, InterruptedException {
      byte var2;
      DataSpec var4;
      boolean var5;
      label387: {
         boolean var1 = this.isEncrypted;
         var2 = 0;
         if (var1) {
            DataSpec var3 = super.dataSpec;
            var4 = var3;
            if (this.nextLoadPosition != 0) {
               var5 = true;
               var4 = var3;
               break label387;
            }
         } else {
            var4 = super.dataSpec.subrange((long)this.nextLoadPosition);
         }

         var5 = false;
      }

      if (!this.isMasterTimestampSource) {
         this.timestampAdjuster.waitUntilInitialized();
      } else if (this.timestampAdjuster.getFirstSampleTimestampUs() == Long.MAX_VALUE) {
         this.timestampAdjuster.setFirstSampleTimestampUs(super.startTimeUs);
      }

      label381: {
         Throwable var10000;
         label390: {
            DefaultExtractorInput var38;
            boolean var10001;
            try {
               var38 = this.prepareExtraction(super.dataSource, var4);
            } catch (Throwable var37) {
               var10000 = var37;
               var10001 = false;
               break label390;
            }

            int var6 = var2;
            if (var5) {
               try {
                  var38.skipFully(this.nextLoadPosition);
               } catch (Throwable var35) {
                  var10000 = var35;
                  var10001 = false;
                  break label390;
               }

               var6 = var2;
            }

            while(var6 == 0) {
               boolean var22 = false;

               try {
                  var22 = true;
                  if (this.loadCanceled) {
                     var22 = false;
                     break;
                  }

                  var6 = this.extractor.read(var38, (PositionHolder)null);
                  var22 = false;
               } finally {
                  if (var22) {
                     try {
                        this.nextLoadPosition = (int)(var38.getPosition() - super.dataSpec.absoluteStreamPosition);
                     } catch (Throwable var33) {
                        var10000 = var33;
                        var10001 = false;
                        break label390;
                     }
                  }
               }
            }

            label363:
            try {
               this.nextLoadPosition = (int)(var38.getPosition() - super.dataSpec.absoluteStreamPosition);
               break label381;
            } catch (Throwable var34) {
               var10000 = var34;
               var10001 = false;
               break label363;
            }
         }

         Throwable var39 = var10000;
         Util.closeQuietly((DataSource)super.dataSource);
         throw var39;
      }

      Util.closeQuietly((DataSource)super.dataSource);
   }

   private void maybeLoadInitData() throws IOException, InterruptedException {
      if (!this.initLoadCompleted) {
         DataSpec var1 = this.initDataSpec;
         if (var1 != null) {
            var1 = var1.subrange((long)this.initSegmentBytesLoaded);

            label242: {
               Throwable var10000;
               label249: {
                  boolean var10001;
                  DefaultExtractorInput var2;
                  try {
                     var2 = this.prepareExtraction(this.initDataSource, var1);
                  } catch (Throwable var24) {
                     var10000 = var24;
                     var10001 = false;
                     break label249;
                  }

                  int var3 = 0;

                  while(var3 == 0) {
                     boolean var16 = false;

                     try {
                        var16 = true;
                        if (this.loadCanceled) {
                           var16 = false;
                           break;
                        }

                        var3 = this.extractor.read(var2, (PositionHolder)null);
                        var16 = false;
                     } finally {
                        if (var16) {
                           try {
                              this.initSegmentBytesLoaded = (int)(var2.getPosition() - this.initDataSpec.absoluteStreamPosition);
                           } catch (Throwable var21) {
                              var10000 = var21;
                              var10001 = false;
                              break label249;
                           }
                        }
                     }
                  }

                  label227:
                  try {
                     this.initSegmentBytesLoaded = (int)(var2.getPosition() - this.initDataSpec.absoluteStreamPosition);
                     break label242;
                  } catch (Throwable var22) {
                     var10000 = var22;
                     var10001 = false;
                     break label227;
                  }
               }

               Throwable var25 = var10000;
               Util.closeQuietly(this.initDataSource);
               throw var25;
            }

            Util.closeQuietly(this.initDataSource);
            this.initLoadCompleted = true;
            return;
         }
      }

   }

   private long peekId3PrivTimestamp(ExtractorInput var1) throws IOException, InterruptedException {
      var1.resetPeekPosition();

      try {
         var1.peekFully(this.id3Data.data, 0, 10);
      } catch (EOFException var6) {
         return -9223372036854775807L;
      }

      this.id3Data.reset(10);
      if (this.id3Data.readUnsignedInt24() != Id3Decoder.ID3_TAG) {
         return -9223372036854775807L;
      } else {
         this.id3Data.skipBytes(3);
         int var2 = this.id3Data.readSynchSafeInt();
         int var3 = var2 + 10;
         if (var3 > this.id3Data.capacity()) {
            ParsableByteArray var4 = this.id3Data;
            byte[] var5 = var4.data;
            var4.reset(var3);
            System.arraycopy(var5, 0, this.id3Data.data, 0, 10);
         }

         var1.peekFully(this.id3Data.data, 10, var2);
         Metadata var7 = this.id3Decoder.decode(this.id3Data.data, var2);
         if (var7 == null) {
            return -9223372036854775807L;
         } else {
            var2 = var7.length();

            for(var3 = 0; var3 < var2; ++var3) {
               Metadata.Entry var8 = var7.get(var3);
               if (var8 instanceof PrivFrame) {
                  PrivFrame var9 = (PrivFrame)var8;
                  if ("com.apple.streaming.transportStreamTimestamp".equals(var9.owner)) {
                     System.arraycopy(var9.privateData, 0, this.id3Data.data, 0, 8);
                     this.id3Data.reset(8);
                     return this.id3Data.readLong() & 8589934591L;
                  }
               }
            }

            return -9223372036854775807L;
         }
      }
   }

   private DefaultExtractorInput prepareExtraction(DataSource var1, DataSpec var2) throws IOException, InterruptedException {
      long var3 = var1.open(var2);
      DefaultExtractorInput var5 = new DefaultExtractorInput(var1, var2.absoluteStreamPosition, var3);
      if (this.extractor == null) {
         var3 = this.peekId3PrivTimestamp(var5);
         var5.resetPeekPosition();
         Pair var9 = this.extractorFactory.createExtractor(this.previousExtractor, var2.uri, super.trackFormat, this.muxedCaptionFormats, this.drmInitData, this.timestampAdjuster, var1.getResponseHeaders(), var5);
         this.extractor = (Extractor)var9.first;
         Extractor var6 = this.extractor;
         Extractor var11 = this.previousExtractor;
         boolean var7 = true;
         boolean var8;
         if (var6 == var11) {
            var8 = true;
         } else {
            var8 = false;
         }

         if ((Boolean)var9.second) {
            HlsSampleStreamWrapper var10 = this.output;
            if (var3 != -9223372036854775807L) {
               var3 = this.timestampAdjuster.adjustTsTimestamp(var3);
            } else {
               var3 = super.startTimeUs;
            }

            var10.setSampleOffsetUs(var3);
         }

         if (!var8 || this.initDataSpec == null) {
            var7 = false;
         }

         this.initLoadCompleted = var7;
         this.output.init(this.uid, this.shouldSpliceIn, var8);
         if (!var8) {
            this.extractor.init(this.output);
         }
      }

      return var5;
   }

   public void cancelLoad() {
      this.loadCanceled = true;
   }

   public void init(HlsSampleStreamWrapper var1) {
      this.output = var1;
   }

   public boolean isLoadCompleted() {
      return this.loadCompleted;
   }

   public void load() throws IOException, InterruptedException {
      this.maybeLoadInitData();
      if (!this.loadCanceled) {
         if (!this.hasGapTag) {
            this.loadMedia();
         }

         this.loadCompleted = true;
      }

   }
}
