package com.google.android.exoplayer2.extractor.ts;

import android.util.SparseArray;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.BinarySearchSeeker;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import java.io.IOException;

public final class PsExtractor implements Extractor {
   public static final ExtractorsFactory FACTORY;
   private final PsDurationReader durationReader;
   private boolean foundAllTracks;
   private boolean foundAudioTrack;
   private boolean foundVideoTrack;
   private boolean hasOutputSeekMap;
   private long lastTrackPosition;
   private ExtractorOutput output;
   private PsBinarySearchSeeker psBinarySearchSeeker;
   private final ParsableByteArray psPacketBuffer;
   private final SparseArray psPayloadReaders;
   private final TimestampAdjuster timestampAdjuster;

   static {
      FACTORY = _$$Lambda$PsExtractor$U8l9TedlJUwsYwV9EOSFo_ngcXY.INSTANCE;
   }

   public PsExtractor() {
      this(new TimestampAdjuster(0L));
   }

   public PsExtractor(TimestampAdjuster var1) {
      this.timestampAdjuster = var1;
      this.psPacketBuffer = new ParsableByteArray(4096);
      this.psPayloadReaders = new SparseArray();
      this.durationReader = new PsDurationReader();
   }

   // $FF: synthetic method
   static Extractor[] lambda$static$0() {
      return new Extractor[]{new PsExtractor()};
   }

   private void maybeOutputSeekMap(long var1) {
      if (!this.hasOutputSeekMap) {
         this.hasOutputSeekMap = true;
         if (this.durationReader.getDurationUs() != -9223372036854775807L) {
            this.psBinarySearchSeeker = new PsBinarySearchSeeker(this.durationReader.getScrTimestampAdjuster(), this.durationReader.getDurationUs(), var1);
            this.output.seekMap(this.psBinarySearchSeeker.getSeekMap());
         } else {
            this.output.seekMap(new SeekMap.Unseekable(this.durationReader.getDurationUs()));
         }
      }

   }

   public void init(ExtractorOutput var1) {
      this.output = var1;
   }

   public int read(ExtractorInput var1, PositionHolder var2) throws IOException, InterruptedException {
      long var3 = var1.getLength();
      boolean var5;
      if (var3 != -1L) {
         var5 = true;
      } else {
         var5 = false;
      }

      if (var5 && !this.durationReader.isDurationReadFinished()) {
         return this.durationReader.readDuration(var1, var2);
      } else {
         this.maybeOutputSeekMap(var3);
         PsBinarySearchSeeker var6 = this.psBinarySearchSeeker;
         Object var7 = null;
         if (var6 != null && var6.isSeeking()) {
            return this.psBinarySearchSeeker.handlePendingSeek(var1, var2, (BinarySearchSeeker.OutputFrameHolder)null);
         } else {
            var1.resetPeekPosition();
            if (var3 != -1L) {
               var3 -= var1.getPeekPosition();
            } else {
               var3 = -1L;
            }

            if (var3 != -1L && var3 < 4L) {
               return -1;
            } else if (!var1.peekFully(this.psPacketBuffer.data, 0, 4, true)) {
               return -1;
            } else {
               this.psPacketBuffer.setPosition(0);
               int var12 = this.psPacketBuffer.readInt();
               if (var12 == 441) {
                  return -1;
               } else if (var12 == 442) {
                  var1.peekFully(this.psPacketBuffer.data, 0, 10);
                  this.psPacketBuffer.setPosition(9);
                  var1.skipFully((this.psPacketBuffer.readUnsignedByte() & 7) + 14);
                  return 0;
               } else if (var12 == 443) {
                  var1.peekFully(this.psPacketBuffer.data, 0, 2);
                  this.psPacketBuffer.setPosition(0);
                  var1.skipFully(this.psPacketBuffer.readUnsignedShort() + 6);
                  return 0;
               } else if ((var12 & -256) >> 8 != 1) {
                  var1.skipFully(1);
                  return 0;
               } else {
                  var12 &= 255;
                  PsExtractor.PesReader var8 = (PsExtractor.PesReader)this.psPayloadReaders.get(var12);
                  PsExtractor.PesReader var10 = var8;
                  if (!this.foundAllTracks) {
                     PsExtractor.PesReader var13 = var8;
                     if (var8 == null) {
                        Object var11;
                        if (var12 == 189) {
                           var11 = new Ac3Reader();
                           this.foundAudioTrack = true;
                           this.lastTrackPosition = var1.getPosition();
                        } else if ((var12 & 224) == 192) {
                           var11 = new MpegAudioReader();
                           this.foundAudioTrack = true;
                           this.lastTrackPosition = var1.getPosition();
                        } else {
                           var11 = var7;
                           if ((var12 & 240) == 224) {
                              var11 = new H262Reader();
                              this.foundVideoTrack = true;
                              this.lastTrackPosition = var1.getPosition();
                           }
                        }

                        var13 = var8;
                        if (var11 != null) {
                           TsPayloadReader.TrackIdGenerator var14 = new TsPayloadReader.TrackIdGenerator(var12, 256);
                           ((ElementaryStreamReader)var11).createTracks(this.output, var14);
                           var13 = new PsExtractor.PesReader((ElementaryStreamReader)var11, this.timestampAdjuster);
                           this.psPayloadReaders.put(var12, var13);
                        }
                     }

                     if (this.foundAudioTrack && this.foundVideoTrack) {
                        var3 = this.lastTrackPosition + 8192L;
                     } else {
                        var3 = 1048576L;
                     }

                     var10 = var13;
                     if (var1.getPosition() > var3) {
                        this.foundAllTracks = true;
                        this.output.endTracks();
                        var10 = var13;
                     }
                  }

                  var1.peekFully(this.psPacketBuffer.data, 0, 2);
                  this.psPacketBuffer.setPosition(0);
                  var12 = this.psPacketBuffer.readUnsignedShort() + 6;
                  if (var10 == null) {
                     var1.skipFully(var12);
                  } else {
                     this.psPacketBuffer.reset(var12);
                     var1.readFully(this.psPacketBuffer.data, 0, var12);
                     this.psPacketBuffer.setPosition(6);
                     var10.consume(this.psPacketBuffer);
                     ParsableByteArray var9 = this.psPacketBuffer;
                     var9.setLimit(var9.capacity());
                  }

                  return 0;
               }
            }
         }
      }
   }

   public void release() {
   }

   public void seek(long var1, long var3) {
      var1 = this.timestampAdjuster.getTimestampOffsetUs();
      byte var5 = 0;
      boolean var6;
      if (var1 == -9223372036854775807L) {
         var6 = true;
      } else {
         var6 = false;
      }

      if (var6 || this.timestampAdjuster.getFirstSampleTimestampUs() != 0L && this.timestampAdjuster.getFirstSampleTimestampUs() != var3) {
         this.timestampAdjuster.reset();
         this.timestampAdjuster.setFirstSampleTimestampUs(var3);
      }

      PsBinarySearchSeeker var7 = this.psBinarySearchSeeker;
      int var8 = var5;
      if (var7 != null) {
         var7.setSeekTargetUs(var3);
         var8 = var5;
      }

      while(var8 < this.psPayloadReaders.size()) {
         ((PsExtractor.PesReader)this.psPayloadReaders.valueAt(var8)).seek();
         ++var8;
      }

   }

   public boolean sniff(ExtractorInput var1) throws IOException, InterruptedException {
      byte[] var2 = new byte[14];
      boolean var3 = false;
      var1.peekFully(var2, 0, 14);
      if (442 != ((var2[0] & 255) << 24 | (var2[1] & 255) << 16 | (var2[2] & 255) << 8 | var2[3] & 255)) {
         return false;
      } else if ((var2[4] & 196) != 68) {
         return false;
      } else if ((var2[6] & 4) != 4) {
         return false;
      } else if ((var2[8] & 4) != 4) {
         return false;
      } else if ((var2[9] & 1) != 1) {
         return false;
      } else if ((var2[12] & 3) != 3) {
         return false;
      } else {
         var1.advancePeekPosition(var2[13] & 7);
         var1.peekFully(var2, 0, 3);
         if (1 == ((var2[0] & 255) << 16 | (var2[1] & 255) << 8 | var2[2] & 255)) {
            var3 = true;
         }

         return var3;
      }
   }

   private static final class PesReader {
      private boolean dtsFlag;
      private int extendedHeaderLength;
      private final ElementaryStreamReader pesPayloadReader;
      private final ParsableBitArray pesScratch;
      private boolean ptsFlag;
      private boolean seenFirstDts;
      private long timeUs;
      private final TimestampAdjuster timestampAdjuster;

      public PesReader(ElementaryStreamReader var1, TimestampAdjuster var2) {
         this.pesPayloadReader = var1;
         this.timestampAdjuster = var2;
         this.pesScratch = new ParsableBitArray(new byte[64]);
      }

      private void parseHeader() {
         this.pesScratch.skipBits(8);
         this.ptsFlag = this.pesScratch.readBit();
         this.dtsFlag = this.pesScratch.readBit();
         this.pesScratch.skipBits(6);
         this.extendedHeaderLength = this.pesScratch.readBits(8);
      }

      private void parseHeaderExtension() {
         this.timeUs = 0L;
         if (this.ptsFlag) {
            this.pesScratch.skipBits(4);
            long var1 = (long)this.pesScratch.readBits(3);
            this.pesScratch.skipBits(1);
            long var3 = (long)(this.pesScratch.readBits(15) << 15);
            this.pesScratch.skipBits(1);
            long var5 = (long)this.pesScratch.readBits(15);
            this.pesScratch.skipBits(1);
            if (!this.seenFirstDts && this.dtsFlag) {
               this.pesScratch.skipBits(4);
               long var7 = (long)this.pesScratch.readBits(3);
               this.pesScratch.skipBits(1);
               long var9 = (long)(this.pesScratch.readBits(15) << 15);
               this.pesScratch.skipBits(1);
               long var11 = (long)this.pesScratch.readBits(15);
               this.pesScratch.skipBits(1);
               this.timestampAdjuster.adjustTsTimestamp(var7 << 30 | var9 | var11);
               this.seenFirstDts = true;
            }

            this.timeUs = this.timestampAdjuster.adjustTsTimestamp(var1 << 30 | var3 | var5);
         }

      }

      public void consume(ParsableByteArray var1) throws ParserException {
         var1.readBytes(this.pesScratch.data, 0, 3);
         this.pesScratch.setPosition(0);
         this.parseHeader();
         var1.readBytes(this.pesScratch.data, 0, this.extendedHeaderLength);
         this.pesScratch.setPosition(0);
         this.parseHeaderExtension();
         this.pesPayloadReader.packetStarted(this.timeUs, 4);
         this.pesPayloadReader.consume(var1);
         this.pesPayloadReader.packetFinished();
      }

      public void seek() {
         this.seenFirstDts = false;
         this.pesPayloadReader.seek();
      }
   }
}
