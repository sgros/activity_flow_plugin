package com.google.android.exoplayer2.source.chunk;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.DefaultExtractorInput;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;

public final class SingleSampleMediaChunk extends BaseMediaChunk {
   private boolean loadCompleted;
   private long nextLoadPosition;
   private final Format sampleFormat;
   private final int trackType;

   public SingleSampleMediaChunk(DataSource var1, DataSpec var2, Format var3, int var4, Object var5, long var6, long var8, long var10, int var12, Format var13) {
      super(var1, var2, var3, var4, var5, var6, var8, -9223372036854775807L, -9223372036854775807L, var10);
      this.trackType = var12;
      this.sampleFormat = var13;
   }

   public void cancelLoad() {
   }

   public boolean isLoadCompleted() {
      return this.loadCompleted;
   }

   public void load() throws IOException, InterruptedException {
      DataSpec var1 = super.dataSpec.subrange(this.nextLoadPosition);

      label347: {
         Throwable var10000;
         label351: {
            long var2;
            boolean var10001;
            try {
               var2 = super.dataSource.open(var1);
            } catch (Throwable var50) {
               var10000 = var50;
               var10001 = false;
               break label351;
            }

            long var4 = var2;
            if (var2 != -1L) {
               try {
                  var4 = var2 + this.nextLoadPosition;
               } catch (Throwable var49) {
                  var10000 = var49;
                  var10001 = false;
                  break label351;
               }
            }

            BaseMediaChunkOutput var6;
            int var7;
            DefaultExtractorInput var51;
            try {
               var51 = new DefaultExtractorInput(super.dataSource, this.nextLoadPosition, var4);
               var6 = this.getOutput();
               var6.setSampleOffsetUs(0L);
               var7 = this.trackType;
            } catch (Throwable var48) {
               var10000 = var48;
               var10001 = false;
               break label351;
            }

            int var8 = 0;

            TrackOutput var53;
            try {
               var53 = var6.track(0, var7);
               var53.format(this.sampleFormat);
            } catch (Throwable var47) {
               var10000 = var47;
               var10001 = false;
               break label351;
            }

            while(var8 != -1) {
               try {
                  this.nextLoadPosition += (long)var8;
                  var8 = var53.sampleData(var51, Integer.MAX_VALUE, true);
               } catch (Throwable var46) {
                  var10000 = var46;
                  var10001 = false;
                  break label351;
               }
            }

            label326:
            try {
               var8 = (int)this.nextLoadPosition;
               var53.sampleMetadata(super.startTimeUs, 1, var8, 0, (TrackOutput.CryptoData)null);
               break label347;
            } catch (Throwable var45) {
               var10000 = var45;
               var10001 = false;
               break label326;
            }
         }

         Throwable var52 = var10000;
         Util.closeQuietly((DataSource)super.dataSource);
         throw var52;
      }

      Util.closeQuietly((DataSource)super.dataSource);
      this.loadCompleted = true;
   }
}
