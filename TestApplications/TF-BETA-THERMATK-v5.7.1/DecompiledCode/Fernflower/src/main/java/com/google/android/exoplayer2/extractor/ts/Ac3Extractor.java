package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.audio.Ac3Util;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;

public final class Ac3Extractor implements Extractor {
   public static final ExtractorsFactory FACTORY;
   private static final int ID3_TAG;
   private final long firstSampleTimestampUs;
   private final Ac3Reader reader;
   private final ParsableByteArray sampleData;
   private boolean startedPacket;

   static {
      FACTORY = _$$Lambda$Ac3Extractor$c2Fqr1pF6vjFNOhLk9sPPtkNnGE.INSTANCE;
      ID3_TAG = Util.getIntegerCodeForString("ID3");
   }

   public Ac3Extractor() {
      this(0L);
   }

   public Ac3Extractor(long var1) {
      this.firstSampleTimestampUs = var1;
      this.reader = new Ac3Reader();
      this.sampleData = new ParsableByteArray(2786);
   }

   // $FF: synthetic method
   static Extractor[] lambda$static$0() {
      return new Extractor[]{new Ac3Extractor()};
   }

   public void init(ExtractorOutput var1) {
      this.reader.createTracks(var1, new TsPayloadReader.TrackIdGenerator(0, 1));
      var1.endTracks();
      var1.seekMap(new SeekMap.Unseekable(-9223372036854775807L));
   }

   public int read(ExtractorInput var1, PositionHolder var2) throws IOException, InterruptedException {
      int var3 = var1.read(this.sampleData.data, 0, 2786);
      if (var3 == -1) {
         return -1;
      } else {
         this.sampleData.setPosition(0);
         this.sampleData.setLimit(var3);
         if (!this.startedPacket) {
            this.reader.packetStarted(this.firstSampleTimestampUs, 4);
            this.startedPacket = true;
         }

         this.reader.consume(this.sampleData);
         return 0;
      }
   }

   public void release() {
   }

   public void seek(long var1, long var3) {
      this.startedPacket = false;
      this.reader.seek();
   }

   public boolean sniff(ExtractorInput var1) throws IOException, InterruptedException {
      ParsableByteArray var2 = new ParsableByteArray(10);
      int var3 = 0;

      while(true) {
         var1.peekFully(var2.data, 0, 10);
         var2.setPosition(0);
         int var4;
         if (var2.readUnsignedInt24() != ID3_TAG) {
            var1.resetPeekPosition();
            var1.advancePeekPosition(var3);
            var4 = var3;

            while(true) {
               int var5 = 0;

               while(true) {
                  var1.peekFully(var2.data, 0, 6);
                  var2.setPosition(0);
                  if (var2.readUnsignedShort() != 2935) {
                     var1.resetPeekPosition();
                     ++var4;
                     if (var4 - var3 >= 8192) {
                        return false;
                     }

                     var1.advancePeekPosition(var4);
                     break;
                  }

                  ++var5;
                  if (var5 >= 4) {
                     return true;
                  }

                  int var6 = Ac3Util.parseAc3SyncframeSize(var2.data);
                  if (var6 == -1) {
                     return false;
                  }

                  var1.advancePeekPosition(var6 - 6);
               }
            }
         }

         var2.skipBytes(3);
         var4 = var2.readSynchSafeInt();
         var3 += var4 + 10;
         var1.advancePeekPosition(var4);
      }
   }
}
