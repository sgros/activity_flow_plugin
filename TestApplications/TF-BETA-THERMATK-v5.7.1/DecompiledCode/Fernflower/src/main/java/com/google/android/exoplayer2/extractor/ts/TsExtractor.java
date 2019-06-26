package com.google.android.exoplayer2.extractor.ts;

import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.BinarySearchSeeker;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class TsExtractor implements Extractor {
   private static final long AC3_FORMAT_IDENTIFIER;
   private static final long E_AC3_FORMAT_IDENTIFIER;
   public static final ExtractorsFactory FACTORY;
   private static final long HEVC_FORMAT_IDENTIFIER;
   private int bytesSinceLastSync;
   private final SparseIntArray continuityCounters;
   private final TsDurationReader durationReader;
   private boolean hasOutputSeekMap;
   private TsPayloadReader id3Reader;
   private final int mode;
   private ExtractorOutput output;
   private final TsPayloadReader.Factory payloadReaderFactory;
   private int pcrPid;
   private boolean pendingSeekToStart;
   private int remainingPmts;
   private final List timestampAdjusters;
   private final SparseBooleanArray trackIds;
   private final SparseBooleanArray trackPids;
   private boolean tracksEnded;
   private TsBinarySearchSeeker tsBinarySearchSeeker;
   private final ParsableByteArray tsPacketBuffer;
   private final SparseArray tsPayloadReaders;

   static {
      FACTORY = _$$Lambda$TsExtractor$f_UE6PC86cqq4V_qVoFQnPhfFZ8.INSTANCE;
      AC3_FORMAT_IDENTIFIER = (long)Util.getIntegerCodeForString("AC-3");
      E_AC3_FORMAT_IDENTIFIER = (long)Util.getIntegerCodeForString("EAC3");
      HEVC_FORMAT_IDENTIFIER = (long)Util.getIntegerCodeForString("HEVC");
   }

   public TsExtractor() {
      this(0);
   }

   public TsExtractor(int var1) {
      this(1, var1);
   }

   public TsExtractor(int var1, int var2) {
      this(var1, new TimestampAdjuster(0L), new DefaultTsPayloadReaderFactory(var2));
   }

   public TsExtractor(int var1, TimestampAdjuster var2, TsPayloadReader.Factory var3) {
      Assertions.checkNotNull(var3);
      this.payloadReaderFactory = (TsPayloadReader.Factory)var3;
      this.mode = var1;
      if (var1 != 1 && var1 != 2) {
         this.timestampAdjusters = new ArrayList();
         this.timestampAdjusters.add(var2);
      } else {
         this.timestampAdjusters = Collections.singletonList(var2);
      }

      this.tsPacketBuffer = new ParsableByteArray(new byte[9400], 0);
      this.trackIds = new SparseBooleanArray();
      this.trackPids = new SparseBooleanArray();
      this.tsPayloadReaders = new SparseArray();
      this.continuityCounters = new SparseIntArray();
      this.durationReader = new TsDurationReader();
      this.pcrPid = -1;
      this.resetPayloadReaders();
   }

   // $FF: synthetic method
   static int access$108(TsExtractor var0) {
      int var1 = var0.remainingPmts++;
      return var1;
   }

   private boolean fillBufferWithAtLeastOnePacket(ExtractorInput var1) throws IOException, InterruptedException {
      ParsableByteArray var2 = this.tsPacketBuffer;
      byte[] var3 = var2.data;
      int var4;
      if (9400 - var2.getPosition() < 188) {
         var4 = this.tsPacketBuffer.bytesLeft();
         if (var4 > 0) {
            System.arraycopy(var3, this.tsPacketBuffer.getPosition(), var3, 0, var4);
         }

         this.tsPacketBuffer.reset(var3, var4);
      }

      while(this.tsPacketBuffer.bytesLeft() < 188) {
         var4 = this.tsPacketBuffer.limit();
         int var5 = var1.read(var3, var4, 9400 - var4);
         if (var5 == -1) {
            return false;
         }

         this.tsPacketBuffer.setLimit(var4 + var5);
      }

      return true;
   }

   private int findEndOfFirstTsPacketInBuffer() throws ParserException {
      int var1 = this.tsPacketBuffer.getPosition();
      int var2 = this.tsPacketBuffer.limit();
      int var3 = TsUtil.findSyncBytePosition(this.tsPacketBuffer.data, var1, var2);
      this.tsPacketBuffer.setPosition(var3);
      int var4 = var3 + 188;
      if (var4 > var2) {
         this.bytesSinceLastSync += var3 - var1;
         if (this.mode == 2 && this.bytesSinceLastSync > 376) {
            throw new ParserException("Cannot find sync byte. Most likely not a Transport Stream.");
         }
      } else {
         this.bytesSinceLastSync = 0;
      }

      return var4;
   }

   // $FF: synthetic method
   static Extractor[] lambda$static$0() {
      return new Extractor[]{new TsExtractor()};
   }

   private void maybeOutputSeekMap(long var1) {
      if (!this.hasOutputSeekMap) {
         this.hasOutputSeekMap = true;
         if (this.durationReader.getDurationUs() != -9223372036854775807L) {
            this.tsBinarySearchSeeker = new TsBinarySearchSeeker(this.durationReader.getPcrTimestampAdjuster(), this.durationReader.getDurationUs(), var1, this.pcrPid);
            this.output.seekMap(this.tsBinarySearchSeeker.getSeekMap());
         } else {
            this.output.seekMap(new SeekMap.Unseekable(this.durationReader.getDurationUs()));
         }
      }

   }

   private void resetPayloadReaders() {
      this.trackIds.clear();
      this.tsPayloadReaders.clear();
      SparseArray var1 = this.payloadReaderFactory.createInitialPayloadReaders();
      int var2 = var1.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         this.tsPayloadReaders.put(var1.keyAt(var3), var1.valueAt(var3));
      }

      this.tsPayloadReaders.put(0, new SectionReader(new TsExtractor.PatReader()));
      this.id3Reader = null;
   }

   private boolean shouldConsumePacketPayload(int var1) {
      int var2 = this.mode;
      boolean var3 = false;
      if (var2 == 2 || this.tracksEnded || !this.trackPids.get(var1, false)) {
         var3 = true;
      }

      return var3;
   }

   public void init(ExtractorOutput var1) {
      this.output = var1;
   }

   public int read(ExtractorInput var1, PositionHolder var2) throws IOException, InterruptedException {
      long var3 = var1.getLength();
      boolean var5 = this.tracksEnded;
      Object var6 = null;
      boolean var7;
      if (var5) {
         if (var3 != -1L && this.mode != 2) {
            var7 = true;
         } else {
            var7 = false;
         }

         if (var7 && !this.durationReader.isDurationReadFinished()) {
            return this.durationReader.readDuration(var1, var2, this.pcrPid);
         }

         this.maybeOutputSeekMap(var3);
         if (this.pendingSeekToStart) {
            this.pendingSeekToStart = false;
            this.seek(0L, 0L);
            if (var1.getPosition() != 0L) {
               var2.position = 0L;
               return 1;
            }
         }

         TsBinarySearchSeeker var8 = this.tsBinarySearchSeeker;
         if (var8 != null && var8.isSeeking()) {
            return this.tsBinarySearchSeeker.handlePendingSeek(var1, var2, (BinarySearchSeeker.OutputFrameHolder)null);
         }
      }

      if (!this.fillBufferWithAtLeastOnePacket(var1)) {
         return -1;
      } else {
         int var9 = this.findEndOfFirstTsPacketInBuffer();
         int var10 = this.tsPacketBuffer.limit();
         if (var9 > var10) {
            return 0;
         } else {
            int var11 = this.tsPacketBuffer.readInt();
            if ((8388608 & var11) != 0) {
               this.tsPacketBuffer.setPosition(var9);
               return 0;
            } else {
               byte var16;
               if ((4194304 & var11) != 0) {
                  var16 = 1;
               } else {
                  var16 = 0;
               }

               int var12 = var16 | 0;
               int var13 = (2096896 & var11) >> 8;
               if ((var11 & 32) != 0) {
                  var7 = true;
               } else {
                  var7 = false;
               }

               boolean var14;
               if ((var11 & 16) != 0) {
                  var14 = true;
               } else {
                  var14 = false;
               }

               TsPayloadReader var15 = (TsPayloadReader)var6;
               if (var14) {
                  var15 = (TsPayloadReader)this.tsPayloadReaders.get(var13);
               }

               if (var15 == null) {
                  this.tsPacketBuffer.setPosition(var9);
                  return 0;
               } else {
                  int var17;
                  if (this.mode != 2) {
                     var17 = var11 & 15;
                     var11 = this.continuityCounters.get(var13, var17 - 1);
                     this.continuityCounters.put(var13, var17);
                     if (var11 == var17) {
                        this.tsPacketBuffer.setPosition(var9);
                        return 0;
                     }

                     if (var17 != (var11 + 1 & 15)) {
                        var15.seek();
                     }
                  }

                  var17 = var12;
                  if (var7) {
                     var11 = this.tsPacketBuffer.readUnsignedByte();
                     if ((this.tsPacketBuffer.readUnsignedByte() & 64) != 0) {
                        var16 = 2;
                     } else {
                        var16 = 0;
                     }

                     var17 = var12 | var16;
                     this.tsPacketBuffer.skipBytes(var11 - 1);
                  }

                  var5 = this.tracksEnded;
                  if (this.shouldConsumePacketPayload(var13)) {
                     this.tsPacketBuffer.setLimit(var9);
                     var15.consume(this.tsPacketBuffer, var17);
                     this.tsPacketBuffer.setLimit(var10);
                  }

                  if (this.mode != 2 && !var5 && this.tracksEnded && var3 != -1L) {
                     this.pendingSeekToStart = true;
                  }

                  this.tsPacketBuffer.setPosition(var9);
                  return 0;
               }
            }
         }
      }
   }

   public void release() {
   }

   public void seek(long var1, long var3) {
      boolean var5;
      if (this.mode != 2) {
         var5 = true;
      } else {
         var5 = false;
      }

      Assertions.checkState(var5);
      int var6 = this.timestampAdjusters.size();

      int var7;
      for(var7 = 0; var7 < var6; ++var7) {
         TimestampAdjuster var8 = (TimestampAdjuster)this.timestampAdjusters.get(var7);
         boolean var9;
         if (var8.getTimestampOffsetUs() == -9223372036854775807L) {
            var9 = true;
         } else {
            var9 = false;
         }

         if (var9 || var8.getTimestampOffsetUs() != 0L && var8.getFirstSampleTimestampUs() != var3) {
            var8.reset();
            var8.setFirstSampleTimestampUs(var3);
         }
      }

      if (var3 != 0L) {
         TsBinarySearchSeeker var10 = this.tsBinarySearchSeeker;
         if (var10 != null) {
            var10.setSeekTargetUs(var3);
         }
      }

      this.tsPacketBuffer.reset();
      this.continuityCounters.clear();

      for(var7 = 0; var7 < this.tsPayloadReaders.size(); ++var7) {
         ((TsPayloadReader)this.tsPayloadReaders.valueAt(var7)).seek();
      }

      this.bytesSinceLastSync = 0;
   }

   public boolean sniff(ExtractorInput var1) throws IOException, InterruptedException {
      byte[] var2 = this.tsPacketBuffer.data;
      var1.peekFully(var2, 0, 940);

      for(int var3 = 0; var3 < 188; ++var3) {
         int var4 = 0;

         boolean var5;
         while(true) {
            if (var4 >= 5) {
               var5 = true;
               break;
            }

            if (var2[var4 * 188 + var3] != 71) {
               var5 = false;
               break;
            }

            ++var4;
         }

         if (var5) {
            var1.skipFully(var3);
            return true;
         }
      }

      return false;
   }

   private class PatReader implements SectionPayloadReader {
      private final ParsableBitArray patScratch = new ParsableBitArray(new byte[4]);

      public PatReader() {
      }

      public void consume(ParsableByteArray var1) {
         if (var1.readUnsignedByte() == 0) {
            var1.skipBytes(7);
            int var2 = var1.bytesLeft() / 4;

            for(int var3 = 0; var3 < var2; ++var3) {
               var1.readBytes((ParsableBitArray)this.patScratch, 4);
               int var4 = this.patScratch.readBits(16);
               this.patScratch.skipBits(3);
               if (var4 == 0) {
                  this.patScratch.skipBits(13);
               } else {
                  var4 = this.patScratch.readBits(13);
                  TsExtractor.this.tsPayloadReaders.put(var4, new SectionReader(TsExtractor.this.new PmtReader(var4)));
                  TsExtractor.access$108(TsExtractor.this);
               }
            }

            if (TsExtractor.this.mode != 2) {
               TsExtractor.this.tsPayloadReaders.remove(0);
            }

         }
      }

      public void init(TimestampAdjuster var1, ExtractorOutput var2, TsPayloadReader.TrackIdGenerator var3) {
      }
   }

   private class PmtReader implements SectionPayloadReader {
      private final int pid;
      private final ParsableBitArray pmtScratch = new ParsableBitArray(new byte[5]);
      private final SparseIntArray trackIdToPidScratch = new SparseIntArray();
      private final SparseArray trackIdToReaderScratch = new SparseArray();

      public PmtReader(int var2) {
         this.pid = var2;
      }

      private TsPayloadReader.EsInfo readEsInfo(ParsableByteArray var1, int var2) {
         int var3 = var1.getPosition();
         int var4 = var2 + var3;
         String var5 = null;
         short var13 = -1;

         ArrayList var6;
         ArrayList var12;
         for(var6 = null; var1.getPosition() < var4; var6 = var12) {
            int var8;
            String var11;
            label53: {
               label52: {
                  int var7 = var1.readUnsignedByte();
                  var8 = var1.readUnsignedByte();
                  var8 += var1.getPosition();
                  if (var7 == 5) {
                     long var9 = var1.readUnsignedInt();
                     if (var9 == TsExtractor.AC3_FORMAT_IDENTIFIER) {
                        break label52;
                     }

                     if (var9 != TsExtractor.E_AC3_FORMAT_IDENTIFIER) {
                        var11 = var5;
                        var12 = var6;
                        if (var9 == TsExtractor.HEVC_FORMAT_IDENTIFIER) {
                           var13 = 36;
                           var11 = var5;
                           var12 = var6;
                        }
                        break label53;
                     }
                  } else {
                     if (var7 == 106) {
                        break label52;
                     }

                     if (var7 != 122) {
                        if (var7 == 123) {
                           var13 = 138;
                           var11 = var5;
                           var12 = var6;
                           break label53;
                        }

                        if (var7 == 10) {
                           var11 = var1.readString(3).trim();
                           var12 = var6;
                           break label53;
                        }

                        var11 = var5;
                        var12 = var6;
                        if (var7 != 89) {
                           break label53;
                        }

                        var12 = new ArrayList();

                        while(var1.getPosition() < var8) {
                           String var14 = var1.readString(3).trim();
                           var2 = var1.readUnsignedByte();
                           byte[] var15 = new byte[4];
                           var1.readBytes(var15, 0, 4);
                           var12.add(new TsPayloadReader.DvbSubtitleInfo(var14, var2, var15));
                        }

                        var13 = 89;
                        var11 = var5;
                        break label53;
                     }
                  }

                  var13 = 135;
                  var11 = var5;
                  var12 = var6;
                  break label53;
               }

               var13 = 129;
               var11 = var5;
               var12 = var6;
            }

            var1.skipBytes(var8 - var1.getPosition());
            var5 = var11;
         }

         var1.setPosition(var4);
         return new TsPayloadReader.EsInfo(var13, var5, var6, Arrays.copyOfRange(var1.data, var3, var4));
      }

      public void consume(ParsableByteArray var1) {
         if (var1.readUnsignedByte() == 2) {
            int var2 = TsExtractor.this.mode;
            byte var3 = 0;
            TimestampAdjuster var4;
            if (var2 != 1 && TsExtractor.this.mode != 2 && TsExtractor.this.remainingPmts != 1) {
               var4 = new TimestampAdjuster(((TimestampAdjuster)TsExtractor.this.timestampAdjusters.get(0)).getFirstSampleTimestampUs());
               TsExtractor.this.timestampAdjusters.add(var4);
            } else {
               var4 = (TimestampAdjuster)TsExtractor.this.timestampAdjusters.get(0);
            }

            var1.skipBytes(2);
            int var5 = var1.readUnsignedShort();
            var1.skipBytes(3);
            var1.readBytes((ParsableBitArray)this.pmtScratch, 2);
            this.pmtScratch.skipBits(3);
            TsExtractor.this.pcrPid = this.pmtScratch.readBits(13);
            var1.readBytes((ParsableBitArray)this.pmtScratch, 2);
            this.pmtScratch.skipBits(4);
            var1.skipBytes(this.pmtScratch.readBits(12));
            if (TsExtractor.this.mode == 2 && TsExtractor.this.id3Reader == null) {
               TsPayloadReader.EsInfo var6 = new TsPayloadReader.EsInfo(21, (String)null, (List)null, Util.EMPTY_BYTE_ARRAY);
               TsExtractor var7 = TsExtractor.this;
               var7.id3Reader = var7.payloadReaderFactory.createPayloadReader(21, var6);
               TsExtractor.this.id3Reader.init(var4, TsExtractor.this.output, new TsPayloadReader.TrackIdGenerator(var5, 21, 8192));
            }

            this.trackIdToReaderScratch.clear();
            this.trackIdToPidScratch.clear();

            int var8;
            int var9;
            int var10;
            for(var8 = var1.bytesLeft(); var8 > 0; var8 = var9) {
               var1.readBytes((ParsableBitArray)this.pmtScratch, 5);
               var9 = this.pmtScratch.readBits(8);
               this.pmtScratch.skipBits(3);
               var10 = this.pmtScratch.readBits(13);
               this.pmtScratch.skipBits(4);
               int var11 = this.pmtScratch.readBits(12);
               TsPayloadReader.EsInfo var14 = this.readEsInfo(var1, var11);
               var2 = var9;
               if (var9 == 6) {
                  var2 = var14.streamType;
               }

               var9 = var8 - (var11 + 5);
               if (TsExtractor.this.mode == 2) {
                  var8 = var2;
               } else {
                  var8 = var10;
               }

               if (!TsExtractor.this.trackIds.get(var8)) {
                  TsPayloadReader var15;
                  if (TsExtractor.this.mode == 2 && var2 == 21) {
                     var15 = TsExtractor.this.id3Reader;
                  } else {
                     var15 = TsExtractor.this.payloadReaderFactory.createPayloadReader(var2, var14);
                  }

                  if (TsExtractor.this.mode != 2 || var10 < this.trackIdToPidScratch.get(var8, 8192)) {
                     this.trackIdToPidScratch.put(var8, var10);
                     this.trackIdToReaderScratch.put(var8, var15);
                  }
               }
            }

            var8 = this.trackIdToPidScratch.size();

            for(var2 = 0; var2 < var8; ++var2) {
               var10 = this.trackIdToPidScratch.keyAt(var2);
               var9 = this.trackIdToPidScratch.valueAt(var2);
               TsExtractor.this.trackIds.put(var10, true);
               TsExtractor.this.trackPids.put(var9, true);
               TsPayloadReader var12 = (TsPayloadReader)this.trackIdToReaderScratch.valueAt(var2);
               if (var12 != null) {
                  if (var12 != TsExtractor.this.id3Reader) {
                     var12.init(var4, TsExtractor.this.output, new TsPayloadReader.TrackIdGenerator(var5, var10, 8192));
                  }

                  TsExtractor.this.tsPayloadReaders.put(var9, var12);
               }
            }

            if (TsExtractor.this.mode == 2) {
               if (!TsExtractor.this.tracksEnded) {
                  TsExtractor.this.output.endTracks();
                  TsExtractor.this.remainingPmts = 0;
                  TsExtractor.this.tracksEnded = true;
               }
            } else {
               TsExtractor.this.tsPayloadReaders.remove(this.pid);
               TsExtractor var13 = TsExtractor.this;
               if (var13.mode == 1) {
                  var2 = var3;
               } else {
                  var2 = TsExtractor.this.remainingPmts - 1;
               }

               var13.remainingPmts = var2;
               if (TsExtractor.this.remainingPmts == 0) {
                  TsExtractor.this.output.endTracks();
                  TsExtractor.this.tracksEnded = true;
               }
            }

         }
      }

      public void init(TimestampAdjuster var1, ExtractorOutput var2, TsPayloadReader.TrackIdGenerator var3) {
      }
   }
}
