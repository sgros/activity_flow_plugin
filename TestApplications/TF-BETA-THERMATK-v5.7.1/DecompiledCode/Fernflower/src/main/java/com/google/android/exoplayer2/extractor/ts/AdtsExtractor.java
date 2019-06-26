package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.ConstantBitrateSeekMap;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;

public final class AdtsExtractor implements Extractor {
   public static final ExtractorsFactory FACTORY;
   private static final int ID3_TAG;
   private int averageFrameSize;
   private ExtractorOutput extractorOutput;
   private long firstFramePosition;
   private long firstSampleTimestampUs;
   private final long firstStreamSampleTimestampUs;
   private final int flags;
   private boolean hasCalculatedAverageFrameSize;
   private boolean hasOutputSeekMap;
   private final ParsableByteArray packetBuffer;
   private final AdtsReader reader;
   private final ParsableByteArray scratch;
   private final ParsableBitArray scratchBits;
   private boolean startedPacket;

   static {
      FACTORY = _$$Lambda$AdtsExtractor$cqGYwjddB4W6E3ogPGiWfjTa23c.INSTANCE;
      ID3_TAG = Util.getIntegerCodeForString("ID3");
   }

   public AdtsExtractor() {
      this(0L);
   }

   public AdtsExtractor(long var1) {
      this(var1, 0);
   }

   public AdtsExtractor(long var1, int var3) {
      this.firstStreamSampleTimestampUs = var1;
      this.firstSampleTimestampUs = var1;
      this.flags = var3;
      this.reader = new AdtsReader(true);
      this.packetBuffer = new ParsableByteArray(2048);
      this.averageFrameSize = -1;
      this.firstFramePosition = -1L;
      this.scratch = new ParsableByteArray(10);
      this.scratchBits = new ParsableBitArray(this.scratch.data);
   }

   private void calculateAverageFrameSize(ExtractorInput var1) throws IOException, InterruptedException {
      if (!this.hasCalculatedAverageFrameSize) {
         this.averageFrameSize = -1;
         var1.resetPeekPosition();
         long var2 = var1.getPosition();
         long var4 = 0L;
         if (var2 == 0L) {
            this.peekId3Header(var1);
         }

         int var6 = 0;

         int var7;
         int var8;
         do {
            var7 = var6;
            var2 = var4;
            if (!var1.peekFully(this.scratch.data, 0, 2, true)) {
               break;
            }

            this.scratch.setPosition(0);
            if (!AdtsReader.isAdtsSyncWord(this.scratch.readUnsignedShort())) {
               var7 = 0;
               var2 = var4;
               break;
            }

            if (!var1.peekFully(this.scratch.data, 0, 4, true)) {
               var7 = var6;
               var2 = var4;
               break;
            }

            this.scratchBits.setPosition(14);
            var8 = this.scratchBits.readBits(13);
            if (var8 <= 6) {
               this.hasCalculatedAverageFrameSize = true;
               throw new ParserException("Malformed ADTS stream");
            }

            var2 = var4 + (long)var8;
            var7 = var6 + 1;
            if (var7 == 1000) {
               break;
            }

            var6 = var7;
            var4 = var2;
         } while(var1.advancePeekPosition(var8 - 6, true));

         var1.resetPeekPosition();
         if (var7 > 0) {
            this.averageFrameSize = (int)(var2 / (long)var7);
         } else {
            this.averageFrameSize = -1;
         }

         this.hasCalculatedAverageFrameSize = true;
      }
   }

   private static int getBitrateFromFrameSize(int var0, long var1) {
      return (int)((long)(var0 * 8) * 1000000L / var1);
   }

   private SeekMap getConstantBitrateSeekMap(long var1) {
      int var3 = getBitrateFromFrameSize(this.averageFrameSize, this.reader.getSampleDurationUs());
      return new ConstantBitrateSeekMap(var1, this.firstFramePosition, var3, this.averageFrameSize);
   }

   // $FF: synthetic method
   static Extractor[] lambda$static$0() {
      return new Extractor[]{new AdtsExtractor()};
   }

   private void maybeOutputSeekMap(long var1, boolean var3, boolean var4) {
      if (!this.hasOutputSeekMap) {
         boolean var5;
         if (var3 && this.averageFrameSize > 0) {
            var5 = true;
         } else {
            var5 = false;
         }

         if (!var5 || this.reader.getSampleDurationUs() != -9223372036854775807L || var4) {
            ExtractorOutput var6 = this.extractorOutput;
            Assertions.checkNotNull(var6);
            var6 = (ExtractorOutput)var6;
            if (var5 && this.reader.getSampleDurationUs() != -9223372036854775807L) {
               var6.seekMap(this.getConstantBitrateSeekMap(var1));
            } else {
               var6.seekMap(new SeekMap.Unseekable(-9223372036854775807L));
            }

            this.hasOutputSeekMap = true;
         }
      }
   }

   private int peekId3Header(ExtractorInput var1) throws IOException, InterruptedException {
      int var2 = 0;

      while(true) {
         var1.peekFully(this.scratch.data, 0, 10);
         this.scratch.setPosition(0);
         if (this.scratch.readUnsignedInt24() != ID3_TAG) {
            var1.resetPeekPosition();
            var1.advancePeekPosition(var2);
            if (this.firstFramePosition == -1L) {
               this.firstFramePosition = (long)var2;
            }

            return var2;
         }

         this.scratch.skipBytes(3);
         int var3 = this.scratch.readSynchSafeInt();
         var2 += var3 + 10;
         var1.advancePeekPosition(var3);
      }
   }

   public void init(ExtractorOutput var1) {
      this.extractorOutput = var1;
      this.reader.createTracks(var1, new TsPayloadReader.TrackIdGenerator(0, 1));
      var1.endTracks();
   }

   public int read(ExtractorInput var1, PositionHolder var2) throws IOException, InterruptedException {
      long var3 = var1.getLength();
      boolean var5;
      if ((this.flags & 1) != 0 && var3 != -1L) {
         var5 = true;
      } else {
         var5 = false;
      }

      if (var5) {
         this.calculateAverageFrameSize(var1);
      }

      int var6 = var1.read(this.packetBuffer.data, 0, 2048);
      boolean var7;
      if (var6 == -1) {
         var7 = true;
      } else {
         var7 = false;
      }

      this.maybeOutputSeekMap(var3, var5, var7);
      if (var7) {
         return -1;
      } else {
         this.packetBuffer.setPosition(0);
         this.packetBuffer.setLimit(var6);
         if (!this.startedPacket) {
            this.reader.packetStarted(this.firstSampleTimestampUs, 4);
            this.startedPacket = true;
         }

         this.reader.consume(this.packetBuffer);
         return 0;
      }
   }

   public void release() {
   }

   public void seek(long var1, long var3) {
      this.startedPacket = false;
      this.reader.seek();
      this.firstSampleTimestampUs = this.firstStreamSampleTimestampUs + var3;
   }

   public boolean sniff(ExtractorInput var1) throws IOException, InterruptedException {
      int var2 = this.peekId3Header(var1);
      int var3 = var2;

      while(true) {
         int var4 = 0;
         int var5 = 0;

         while(true) {
            var1.peekFully(this.scratch.data, 0, 2);
            this.scratch.setPosition(0);
            if (!AdtsReader.isAdtsSyncWord(this.scratch.readUnsignedShort())) {
               var1.resetPeekPosition();
               ++var3;
               if (var3 - var2 >= 8192) {
                  return false;
               }

               var1.advancePeekPosition(var3);
               break;
            }

            ++var4;
            if (var4 >= 4 && var5 > 188) {
               return true;
            }

            var1.peekFully(this.scratch.data, 0, 4);
            this.scratchBits.setPosition(14);
            int var6 = this.scratchBits.readBits(13);
            if (var6 <= 6) {
               return false;
            }

            var1.advancePeekPosition(var6 - 6);
            var5 += var6;
         }
      }
   }
}
