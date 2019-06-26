package com.google.android.exoplayer2.extractor.ogg;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.io.IOException;

abstract class StreamReader {
   private long currentGranule;
   private ExtractorOutput extractorOutput;
   private boolean formatSet;
   private long lengthOfReadPacket;
   private final OggPacket oggPacket = new OggPacket();
   private OggSeeker oggSeeker;
   private long payloadStartPosition;
   private int sampleRate;
   private boolean seekMapSet;
   private StreamReader.SetupData setupData;
   private int state;
   private long targetGranule;
   private TrackOutput trackOutput;

   public StreamReader() {
   }

   private int readHeaders(ExtractorInput var1) throws IOException, InterruptedException {
      boolean var2 = true;

      while(var2) {
         if (!this.oggPacket.populate(var1)) {
            this.state = 3;
            return -1;
         }

         this.lengthOfReadPacket = var1.getPosition() - this.payloadStartPosition;
         boolean var3 = this.readHeaders(this.oggPacket.getPayload(), this.payloadStartPosition, this.setupData);
         var2 = var3;
         if (var3) {
            this.payloadStartPosition = var1.getPosition();
            var2 = var3;
         }
      }

      Format var4 = this.setupData.format;
      this.sampleRate = var4.sampleRate;
      if (!this.formatSet) {
         this.trackOutput.format(var4);
         this.formatSet = true;
      }

      OggSeeker var5 = this.setupData.oggSeeker;
      if (var5 != null) {
         this.oggSeeker = var5;
      } else if (var1.getLength() == -1L) {
         this.oggSeeker = new StreamReader.UnseekableOggSeeker();
      } else {
         OggPageHeader var6 = this.oggPacket.getPageHeader();
         if ((var6.type & 4) != 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.oggSeeker = new DefaultOggSeeker(this.payloadStartPosition, var1.getLength(), this, (long)(var6.headerSize + var6.bodySize), var6.granulePosition, var2);
      }

      this.setupData = null;
      this.state = 2;
      this.oggPacket.trimPayload();
      return 0;
   }

   private int readPayload(ExtractorInput var1, PositionHolder var2) throws IOException, InterruptedException {
      long var3 = this.oggSeeker.read(var1);
      if (var3 >= 0L) {
         var2.position = var3;
         return 1;
      } else {
         if (var3 < -1L) {
            this.onSeekEnd(-(var3 + 2L));
         }

         if (!this.seekMapSet) {
            SeekMap var8 = this.oggSeeker.createSeekMap();
            this.extractorOutput.seekMap(var8);
            this.seekMapSet = true;
         }

         if (this.lengthOfReadPacket <= 0L && !this.oggPacket.populate(var1)) {
            this.state = 3;
            return -1;
         } else {
            this.lengthOfReadPacket = 0L;
            ParsableByteArray var7 = this.oggPacket.getPayload();
            var3 = this.preparePayload(var7);
            if (var3 >= 0L) {
               long var5 = this.currentGranule;
               if (var5 + var3 >= this.targetGranule) {
                  var5 = this.convertGranuleToTime(var5);
                  this.trackOutput.sampleData(var7, var7.limit());
                  this.trackOutput.sampleMetadata(var5, 1, var7.limit(), 0, (TrackOutput.CryptoData)null);
                  this.targetGranule = -1L;
               }
            }

            this.currentGranule += var3;
            return 0;
         }
      }
   }

   protected long convertGranuleToTime(long var1) {
      return var1 * 1000000L / (long)this.sampleRate;
   }

   protected long convertTimeToGranule(long var1) {
      return (long)this.sampleRate * var1 / 1000000L;
   }

   void init(ExtractorOutput var1, TrackOutput var2) {
      this.extractorOutput = var1;
      this.trackOutput = var2;
      this.reset(true);
   }

   protected void onSeekEnd(long var1) {
      this.currentGranule = var1;
   }

   protected abstract long preparePayload(ParsableByteArray var1);

   final int read(ExtractorInput var1, PositionHolder var2) throws IOException, InterruptedException {
      int var3 = this.state;
      if (var3 != 0) {
         if (var3 != 1) {
            if (var3 == 2) {
               return this.readPayload(var1, var2);
            } else {
               throw new IllegalStateException();
            }
         } else {
            var1.skipFully((int)this.payloadStartPosition);
            this.state = 2;
            return 0;
         }
      } else {
         return this.readHeaders(var1);
      }
   }

   protected abstract boolean readHeaders(ParsableByteArray var1, long var2, StreamReader.SetupData var4) throws IOException, InterruptedException;

   protected void reset(boolean var1) {
      if (var1) {
         this.setupData = new StreamReader.SetupData();
         this.payloadStartPosition = 0L;
         this.state = 0;
      } else {
         this.state = 1;
      }

      this.targetGranule = -1L;
      this.currentGranule = 0L;
   }

   final void seek(long var1, long var3) {
      this.oggPacket.reset();
      if (var1 == 0L) {
         this.reset(this.seekMapSet ^ true);
      } else if (this.state != 0) {
         this.targetGranule = this.oggSeeker.startSeek(var3);
         this.state = 2;
      }

   }

   static class SetupData {
      Format format;
      OggSeeker oggSeeker;
   }

   private static final class UnseekableOggSeeker implements OggSeeker {
      private UnseekableOggSeeker() {
      }

      // $FF: synthetic method
      UnseekableOggSeeker(Object var1) {
         this();
      }

      public SeekMap createSeekMap() {
         return new SeekMap.Unseekable(-9223372036854775807L);
      }

      public long read(ExtractorInput var1) throws IOException, InterruptedException {
         return -1L;
      }

      public long startSeek(long var1) {
         return 0L;
      }
   }
}
