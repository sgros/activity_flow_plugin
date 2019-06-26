package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.audio.DtsUtil;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.ParsableByteArray;

public final class DtsReader implements ElementaryStreamReader {
   private int bytesRead;
   private Format format;
   private String formatId;
   private final ParsableByteArray headerScratchBytes = new ParsableByteArray(new byte[18]);
   private final String language;
   private TrackOutput output;
   private long sampleDurationUs;
   private int sampleSize;
   private int state = 0;
   private int syncBytes;
   private long timeUs;

   public DtsReader(String var1) {
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
      byte[] var1 = this.headerScratchBytes.data;
      if (this.format == null) {
         this.format = DtsUtil.parseDtsFormat(var1, this.formatId, this.language, (DrmInitData)null);
         this.output.format(this.format);
      }

      this.sampleSize = DtsUtil.getDtsFrameSize(var1);
      this.sampleDurationUs = (long)((int)((long)DtsUtil.parseDtsAudioSampleCount(var1) * 1000000L / (long)this.format.sampleRate));
   }

   private boolean skipToNextSync(ParsableByteArray var1) {
      while(true) {
         if (var1.bytesLeft() > 0) {
            this.syncBytes <<= 8;
            this.syncBytes |= var1.readUnsignedByte();
            if (!DtsUtil.isSyncWord(this.syncBytes)) {
               continue;
            }

            byte[] var3 = this.headerScratchBytes.data;
            int var2 = this.syncBytes;
            var3[0] = (byte)((byte)(var2 >> 24 & 255));
            var3[1] = (byte)((byte)(var2 >> 16 & 255));
            var3[2] = (byte)((byte)(var2 >> 8 & 255));
            var3[3] = (byte)((byte)(var2 & 255));
            this.bytesRead = 4;
            this.syncBytes = 0;
            return true;
         }

         return false;
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
            } else if (this.continueRead(var1, this.headerScratchBytes.data, 18)) {
               this.parseHeader();
               this.headerScratchBytes.setPosition(0);
               this.output.sampleData(this.headerScratchBytes, 18);
               this.state = 2;
            }
         } else if (this.skipToNextSync(var1)) {
            this.state = 1;
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
      this.bytesRead = 0;
      this.syncBytes = 0;
   }
}
