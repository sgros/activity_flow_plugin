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

public class ContainerMediaChunk extends BaseMediaChunk {
   private static final PositionHolder DUMMY_POSITION_HOLDER = new PositionHolder();
   private final int chunkCount;
   private final ChunkExtractorWrapper extractorWrapper;
   private volatile boolean loadCanceled;
   private boolean loadCompleted;
   private long nextLoadPosition;
   private final long sampleOffsetUs;

   public ContainerMediaChunk(DataSource var1, DataSpec var2, Format var3, int var4, Object var5, long var6, long var8, long var10, long var12, long var14, int var16, long var17, ChunkExtractorWrapper var19) {
      super(var1, var2, var3, var4, var5, var6, var8, var10, var12, var14);
      this.chunkCount = var16;
      this.sampleOffsetUs = var17;
      this.extractorWrapper = var19;
   }

   public final void cancelLoad() {
      this.loadCanceled = true;
   }

   public long getNextChunkIndex() {
      return super.chunkIndex + (long)this.chunkCount;
   }

   public boolean isLoadCompleted() {
      return this.loadCompleted;
   }

   public final void load() throws IOException, InterruptedException {
      DataSpec var1 = super.dataSpec.subrange(this.nextLoadPosition);

      label1050: {
         Throwable var10000;
         label1054: {
            DefaultExtractorInput var2;
            boolean var10001;
            label1055: {
               BaseMediaChunkOutput var3;
               long var4;
               ChunkExtractorWrapper var120;
               label1047: {
                  label1046: {
                     try {
                        var2 = new DefaultExtractorInput(super.dataSource, var1.absoluteStreamPosition, super.dataSource.open(var1));
                        if (this.nextLoadPosition != 0L) {
                           break label1055;
                        }

                        var3 = this.getOutput();
                        var3.setSampleOffsetUs(this.sampleOffsetUs);
                        var120 = this.extractorWrapper;
                        if (super.clippedStartTimeUs == -9223372036854775807L) {
                           break label1046;
                        }
                     } catch (Throwable var119) {
                        var10000 = var119;
                        var10001 = false;
                        break label1054;
                     }

                     try {
                        var4 = super.clippedStartTimeUs - this.sampleOffsetUs;
                        break label1047;
                     } catch (Throwable var117) {
                        var10000 = var117;
                        var10001 = false;
                        break label1054;
                     }
                  }

                  var4 = -9223372036854775807L;
               }

               long var6;
               label1039: {
                  label1038: {
                     try {
                        if (super.clippedEndTimeUs != -9223372036854775807L) {
                           break label1038;
                        }
                     } catch (Throwable var118) {
                        var10000 = var118;
                        var10001 = false;
                        break label1054;
                     }

                     var6 = -9223372036854775807L;
                     break label1039;
                  }

                  try {
                     var6 = super.clippedEndTimeUs - this.sampleOffsetUs;
                  } catch (Throwable var116) {
                     var10000 = var116;
                     var10001 = false;
                     break label1054;
                  }
               }

               try {
                  var120.init(var3, var4, var6);
               } catch (Throwable var115) {
                  var10000 = var115;
                  var10001 = false;
                  break label1054;
               }
            }

            label1056: {
               label1058: {
                  Extractor var121;
                  try {
                     var121 = this.extractorWrapper.extractor;
                  } catch (Throwable var114) {
                     var10000 = var114;
                     var10001 = false;
                     break label1058;
                  }

                  boolean var8 = false;
                  int var9 = 0;

                  while(var9 == 0) {
                     try {
                        if (this.loadCanceled) {
                           break;
                        }

                        var9 = var121.read(var2, DUMMY_POSITION_HOLDER);
                     } catch (Throwable var113) {
                        var10000 = var113;
                        var10001 = false;
                        break label1058;
                     }
                  }

                  if (var9 != 1) {
                     var8 = true;
                  }

                  label1010:
                  try {
                     Assertions.checkState(var8);
                     break label1056;
                  } catch (Throwable var112) {
                     var10000 = var112;
                     var10001 = false;
                     break label1010;
                  }
               }

               Throwable var122 = var10000;

               try {
                  this.nextLoadPosition = var2.getPosition() - super.dataSpec.absoluteStreamPosition;
                  throw var122;
               } catch (Throwable var110) {
                  var10000 = var110;
                  var10001 = false;
                  break label1054;
               }
            }

            label1006:
            try {
               this.nextLoadPosition = var2.getPosition() - super.dataSpec.absoluteStreamPosition;
               break label1050;
            } catch (Throwable var111) {
               var10000 = var111;
               var10001 = false;
               break label1006;
            }
         }

         Throwable var123 = var10000;
         Util.closeQuietly((DataSource)super.dataSource);
         throw var123;
      }

      Util.closeQuietly((DataSource)super.dataSource);
      this.loadCompleted = true;
   }
}
