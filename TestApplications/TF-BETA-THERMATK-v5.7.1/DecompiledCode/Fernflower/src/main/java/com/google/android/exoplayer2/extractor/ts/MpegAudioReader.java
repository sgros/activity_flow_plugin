package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.MpegAudioHeader;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.List;

public final class MpegAudioReader implements ElementaryStreamReader {
   private String formatId;
   private int frameBytesRead;
   private long frameDurationUs;
   private int frameSize;
   private boolean hasOutputFormat;
   private final MpegAudioHeader header;
   private final ParsableByteArray headerScratch;
   private final String language;
   private boolean lastByteWasFF;
   private TrackOutput output;
   private int state;
   private long timeUs;

   public MpegAudioReader() {
      this((String)null);
   }

   public MpegAudioReader(String var1) {
      this.state = 0;
      this.headerScratch = new ParsableByteArray(4);
      this.headerScratch.data[0] = (byte)-1;
      this.header = new MpegAudioHeader();
      this.language = var1;
   }

   private void findHeader(ParsableByteArray var1) {
      byte[] var2 = var1.data;
      int var3 = var1.getPosition();

      int var4;
      for(var4 = var1.limit(); var3 < var4; ++var3) {
         boolean var5;
         if ((var2[var3] & 255) == 255) {
            var5 = true;
         } else {
            var5 = false;
         }

         boolean var6;
         if (this.lastByteWasFF && (var2[var3] & 224) == 224) {
            var6 = true;
         } else {
            var6 = false;
         }

         this.lastByteWasFF = var5;
         if (var6) {
            var1.setPosition(var3 + 1);
            this.lastByteWasFF = false;
            this.headerScratch.data[1] = (byte)var2[var3];
            this.frameBytesRead = 2;
            this.state = 1;
            return;
         }
      }

      var1.setPosition(var4);
   }

   private void readFrameRemainder(ParsableByteArray var1) {
      int var2 = Math.min(var1.bytesLeft(), this.frameSize - this.frameBytesRead);
      this.output.sampleData(var1, var2);
      this.frameBytesRead += var2;
      int var3 = this.frameBytesRead;
      var2 = this.frameSize;
      if (var3 >= var2) {
         this.output.sampleMetadata(this.timeUs, 1, var2, 0, (TrackOutput.CryptoData)null);
         this.timeUs += this.frameDurationUs;
         this.frameBytesRead = 0;
         this.state = 0;
      }
   }

   private void readHeaderRemainder(ParsableByteArray var1) {
      int var2 = Math.min(var1.bytesLeft(), 4 - this.frameBytesRead);
      var1.readBytes(this.headerScratch.data, this.frameBytesRead, var2);
      this.frameBytesRead += var2;
      if (this.frameBytesRead >= 4) {
         this.headerScratch.setPosition(0);
         if (!MpegAudioHeader.populateHeader(this.headerScratch.readInt(), this.header)) {
            this.frameBytesRead = 0;
            this.state = 1;
         } else {
            MpegAudioHeader var5 = this.header;
            this.frameSize = var5.frameSize;
            if (!this.hasOutputFormat) {
               long var3 = (long)var5.samplesPerFrame;
               var2 = var5.sampleRate;
               this.frameDurationUs = var3 * 1000000L / (long)var2;
               Format var6 = Format.createAudioSampleFormat(this.formatId, var5.mimeType, (String)null, -1, 4096, var5.channels, var2, (List)null, (DrmInitData)null, 0, this.language);
               this.output.format(var6);
               this.hasOutputFormat = true;
            }

            this.headerScratch.setPosition(0);
            this.output.sampleData(this.headerScratch, 4);
            this.state = 2;
         }
      }
   }

   public void consume(ParsableByteArray var1) {
      while(var1.bytesLeft() > 0) {
         int var2 = this.state;
         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 != 2) {
                  throw new IllegalStateException();
               }

               this.readFrameRemainder(var1);
            } else {
               this.readHeaderRemainder(var1);
            }
         } else {
            this.findHeader(var1);
         }
      }

   }

   public void createTracks(ExtractorOutput var1, TsPayloadReader.TrackIdGenerator var2) {
      var2.generateNewId();
      this.formatId = var2.getFormatId();
      this.output = var1.track(var2.getTrackId(), 1);
   }

   public void packetFinished() {
   }

   public void packetStarted(long var1, int var3) {
      this.timeUs = var1;
   }

   public void seek() {
      this.state = 0;
      this.frameBytesRead = 0;
      this.lastByteWasFF = false;
   }
}
