package com.google.android.exoplayer2.extractor.rawcc;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;

public final class RawCcExtractor implements Extractor {
   private static final int HEADER_ID = Util.getIntegerCodeForString("RCC\u0001");
   private final ParsableByteArray dataScratch;
   private final Format format;
   private int parserState;
   private int remainingSampleCount;
   private int sampleBytesWritten;
   private long timestampUs;
   private TrackOutput trackOutput;
   private int version;

   public RawCcExtractor(Format var1) {
      this.format = var1;
      this.dataScratch = new ParsableByteArray(9);
      this.parserState = 0;
   }

   private boolean parseHeader(ExtractorInput var1) throws IOException, InterruptedException {
      this.dataScratch.reset();
      if (var1.readFully(this.dataScratch.data, 0, 8, true)) {
         if (this.dataScratch.readInt() == HEADER_ID) {
            this.version = this.dataScratch.readUnsignedByte();
            return true;
         } else {
            throw new IOException("Input not RawCC");
         }
      } else {
         return false;
      }
   }

   private void parseSamples(ExtractorInput var1) throws IOException, InterruptedException {
      while(this.remainingSampleCount > 0) {
         this.dataScratch.reset();
         var1.readFully(this.dataScratch.data, 0, 3);
         this.trackOutput.sampleData(this.dataScratch, 3);
         this.sampleBytesWritten += 3;
         --this.remainingSampleCount;
      }

      int var2 = this.sampleBytesWritten;
      if (var2 > 0) {
         this.trackOutput.sampleMetadata(this.timestampUs, 1, var2, 0, (TrackOutput.CryptoData)null);
      }

   }

   private boolean parseTimestampAndSampleCount(ExtractorInput var1) throws IOException, InterruptedException {
      this.dataScratch.reset();
      int var2 = this.version;
      if (var2 == 0) {
         if (!var1.readFully(this.dataScratch.data, 0, 5, true)) {
            return false;
         }

         this.timestampUs = this.dataScratch.readUnsignedInt() * 1000L / 45L;
      } else {
         if (var2 != 1) {
            StringBuilder var3 = new StringBuilder();
            var3.append("Unsupported version number: ");
            var3.append(this.version);
            throw new ParserException(var3.toString());
         }

         if (!var1.readFully(this.dataScratch.data, 0, 9, true)) {
            return false;
         }

         this.timestampUs = this.dataScratch.readLong();
      }

      this.remainingSampleCount = this.dataScratch.readUnsignedByte();
      this.sampleBytesWritten = 0;
      return true;
   }

   public void init(ExtractorOutput var1) {
      var1.seekMap(new SeekMap.Unseekable(-9223372036854775807L));
      this.trackOutput = var1.track(0, 3);
      var1.endTracks();
      this.trackOutput.format(this.format);
   }

   public int read(ExtractorInput var1, PositionHolder var2) throws IOException, InterruptedException {
      while(true) {
         int var3 = this.parserState;
         if (var3 != 0) {
            if (var3 != 1) {
               if (var3 == 2) {
                  this.parseSamples(var1);
                  this.parserState = 1;
                  return 0;
               }

               throw new IllegalStateException();
            }

            if (!this.parseTimestampAndSampleCount(var1)) {
               this.parserState = 0;
               return -1;
            }

            this.parserState = 2;
         } else {
            if (!this.parseHeader(var1)) {
               return -1;
            }

            this.parserState = 1;
         }
      }
   }

   public void release() {
   }

   public void seek(long var1, long var3) {
      this.parserState = 0;
   }

   public boolean sniff(ExtractorInput var1) throws IOException, InterruptedException {
      this.dataScratch.reset();
      byte[] var2 = this.dataScratch.data;
      boolean var3 = false;
      var1.peekFully(var2, 0, 8);
      if (this.dataScratch.readInt() == HEADER_ID) {
         var3 = true;
      }

      return var3;
   }
}
