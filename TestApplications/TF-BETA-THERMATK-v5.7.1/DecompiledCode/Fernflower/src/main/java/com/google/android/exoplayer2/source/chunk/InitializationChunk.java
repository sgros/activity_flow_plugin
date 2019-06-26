package com.google.android.exoplayer2.source.chunk;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.DefaultExtractorInput;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;

public final class InitializationChunk extends Chunk {
   private static final PositionHolder DUMMY_POSITION_HOLDER = new PositionHolder();
   private final ChunkExtractorWrapper extractorWrapper;
   private volatile boolean loadCanceled;
   private long nextLoadPosition;

   public InitializationChunk(DataSource var1, DataSpec var2, Format var3, int var4, Object var5, ChunkExtractorWrapper var6) {
      super(var1, var2, 2, var3, var4, var5, -9223372036854775807L, -9223372036854775807L);
      this.extractorWrapper = var6;
   }

   public void cancelLoad() {
      this.loadCanceled = true;
   }

   public void load() throws IOException, InterruptedException {
      DataSpec var1 = super.dataSpec.subrange(this.nextLoadPosition);

      label472: {
         Throwable var10000;
         label476: {
            DefaultExtractorInput var2;
            boolean var10001;
            try {
               var2 = new DefaultExtractorInput(super.dataSource, var1.absoluteStreamPosition, super.dataSource.open(var1));
               if (this.nextLoadPosition == 0L) {
                  this.extractorWrapper.init((ChunkExtractorWrapper.TrackOutputProvider)null, -9223372036854775807L, -9223372036854775807L);
               }
            } catch (Throwable var46) {
               var10000 = var46;
               var10001 = false;
               break label476;
            }

            label477: {
               label479: {
                  Extractor var47;
                  try {
                     var47 = this.extractorWrapper.extractor;
                  } catch (Throwable var45) {
                     var10000 = var45;
                     var10001 = false;
                     break label479;
                  }

                  int var3 = 0;

                  while(var3 == 0) {
                     try {
                        if (this.loadCanceled) {
                           break;
                        }

                        var3 = var47.read(var2, DUMMY_POSITION_HOLDER);
                     } catch (Throwable var44) {
                        var10000 = var44;
                        var10001 = false;
                        break label479;
                     }
                  }

                  boolean var4 = true;
                  if (var3 == 1) {
                     var4 = false;
                  }

                  label452:
                  try {
                     Assertions.checkState(var4);
                     break label477;
                  } catch (Throwable var43) {
                     var10000 = var43;
                     var10001 = false;
                     break label452;
                  }
               }

               Throwable var48 = var10000;

               try {
                  this.nextLoadPosition = var2.getPosition() - super.dataSpec.absoluteStreamPosition;
                  throw var48;
               } catch (Throwable var41) {
                  var10000 = var41;
                  var10001 = false;
                  break label476;
               }
            }

            label448:
            try {
               this.nextLoadPosition = var2.getPosition() - super.dataSpec.absoluteStreamPosition;
               break label472;
            } catch (Throwable var42) {
               var10000 = var42;
               var10001 = false;
               break label448;
            }
         }

         Throwable var49 = var10000;
         Util.closeQuietly((DataSource)super.dataSource);
         throw var49;
      }

      Util.closeQuietly((DataSource)super.dataSource);
   }
}
