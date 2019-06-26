package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.audio.Ac3Util;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.List;

public final class Ac3Reader implements ElementaryStreamReader {
   private int bytesRead;
   private Format format;
   private final ParsableBitArray headerScratchBits;
   private final ParsableByteArray headerScratchBytes;
   private final String language;
   private boolean lastByteWas0B;
   private TrackOutput output;
   private long sampleDurationUs;
   private int sampleSize;
   private int state;
   private long timeUs;
   private String trackFormatId;

   public Ac3Reader() {
      this((String)null);
   }

   public Ac3Reader(String var1) {
      this.headerScratchBits = new ParsableBitArray(new byte[128]);
      this.headerScratchBytes = new ParsableByteArray(this.headerScratchBits.data);
      this.state = 0;
      this.language = var1;
   }

   private boolean continueRead(ParsableByteArray var1, byte[] var2, int var3) {
      int var4 = Math.min(var1.bytesLeft(), var3 - this.bytesRead);
      var1.readBytes(var2, this.bytesRead, var4);
      this.bytesRead += var4;
      boolean var5;
      if (this.bytesRead == var3) {
         var5 = true;
      } else {
         var5 = false;
      }

      return var5;
   }

   private void parseHeader() {
      this.headerScratchBits.setPosition(0);
      Ac3Util.SyncFrameInfo var1 = Ac3Util.parseAc3SyncframeInfo(this.headerScratchBits);
      Format var2 = this.format;
      if (var2 == null || var1.channelCount != var2.channelCount || var1.sampleRate != var2.sampleRate || var1.mimeType != var2.sampleMimeType) {
         this.format = Format.createAudioSampleFormat(this.trackFormatId, var1.mimeType, (String)null, -1, -1, var1.channelCount, var1.sampleRate, (List)null, (DrmInitData)null, 0, this.language);
         this.output.format(this.format);
      }

      this.sampleSize = var1.frameSize;
      this.sampleDurationUs = (long)var1.sampleCount * 1000000L / (long)this.format.sampleRate;
   }

   private boolean skipToNextSync(ParsableByteArray var1) {
      while(true) {
         int var2 = var1.bytesLeft();
         boolean var3 = false;
         boolean var4 = false;
         if (var2 <= 0) {
            return false;
         }

         if (!this.lastByteWas0B) {
            if (var1.readUnsignedByte() == 11) {
               var4 = true;
            }

            this.lastByteWas0B = var4;
         } else {
            var2 = var1.readUnsignedByte();
            if (var2 == 119) {
               this.lastByteWas0B = false;
               return true;
            }

            var4 = var3;
            if (var2 == 11) {
               var4 = true;
            }

            this.lastByteWas0B = var4;
         }
      }
   }

   public void consume(ParsableByteArray var1) {
      while(var1.bytesLeft() > 0) {
         int var2 = this.state;
         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 == 2) {
                  var2 = Math.min(var1.bytesLeft(), this.sampleSize - this.bytesRead);
                  this.output.sampleData(var1, var2);
                  this.bytesRead += var2;
                  int var3 = this.bytesRead;
                  var2 = this.sampleSize;
                  if (var3 == var2) {
                     this.output.sampleMetadata(this.timeUs, 1, var2, 0, (TrackOutput.CryptoData)null);
                     this.timeUs += this.sampleDurationUs;
                     this.state = 0;
                  }
               }
            } else if (this.continueRead(var1, this.headerScratchBytes.data, 128)) {
               this.parseHeader();
               this.headerScratchBytes.setPosition(0);
               this.output.sampleData(this.headerScratchBytes, 128);
               this.state = 2;
            }
         } else if (this.skipToNextSync(var1)) {
            this.state = 1;
            byte[] var4 = this.headerScratchBytes.data;
            var4[0] = (byte)11;
            var4[1] = (byte)119;
            this.bytesRead = 2;
         }
      }

   }

   public void createTracks(ExtractorOutput var1, TsPayloadReader.TrackIdGenerator var2) {
      var2.generateNewId();
      this.trackFormatId = var2.getFormatId();
      this.output = var1.track(var2.getTrackId(), 1);
   }

   public void packetFinished() {
   }

   public void packetStarted(long var1, int var3) {
      this.timeUs = var1;
   }

   public void seek() {
      this.state = 0;
      this.bytesRead = 0;
      this.lastByteWas0B = false;
   }
}
