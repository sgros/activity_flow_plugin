package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.ParsableByteArray;

public final class Id3Reader implements ElementaryStreamReader {
   private final ParsableByteArray id3Header = new ParsableByteArray(10);
   private TrackOutput output;
   private int sampleBytesRead;
   private int sampleSize;
   private long sampleTimeUs;
   private boolean writingSample;

   public void consume(ParsableByteArray var1) {
      if (this.writingSample) {
         int var2 = var1.bytesLeft();
         int var3 = this.sampleBytesRead;
         if (var3 < 10) {
            var3 = Math.min(var2, 10 - var3);
            System.arraycopy(var1.data, var1.getPosition(), this.id3Header.data, this.sampleBytesRead, var3);
            if (this.sampleBytesRead + var3 == 10) {
               this.id3Header.setPosition(0);
               if (73 != this.id3Header.readUnsignedByte() || 68 != this.id3Header.readUnsignedByte() || 51 != this.id3Header.readUnsignedByte()) {
                  Log.w("Id3Reader", "Discarding invalid ID3 tag");
                  this.writingSample = false;
                  return;
               }

               this.id3Header.skipBytes(3);
               this.sampleSize = this.id3Header.readSynchSafeInt() + 10;
            }
         }

         var2 = Math.min(var2, this.sampleSize - this.sampleBytesRead);
         this.output.sampleData(var1, var2);
         this.sampleBytesRead += var2;
      }
   }

   public void createTracks(ExtractorOutput var1, TsPayloadReader.TrackIdGenerator var2) {
      var2.generateNewId();
      this.output = var1.track(var2.getTrackId(), 4);
      this.output.format(Format.createSampleFormat(var2.getFormatId(), "application/id3", (String)null, -1, (DrmInitData)null));
   }

   public void packetFinished() {
      if (this.writingSample) {
         int var1 = this.sampleSize;
         if (var1 != 0 && this.sampleBytesRead == var1) {
            this.output.sampleMetadata(this.sampleTimeUs, 1, var1, 0, (TrackOutput.CryptoData)null);
            this.writingSample = false;
         }
      }

   }

   public void packetStarted(long var1, int var3) {
      if ((var3 & 4) != 0) {
         this.writingSample = true;
         this.sampleTimeUs = var1;
         this.sampleSize = 0;
         this.sampleBytesRead = 0;
      }
   }

   public void seek() {
      this.writingSample = false;
   }
}
