package com.google.android.exoplayer2.extractor.wav;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;
import java.util.List;

public final class WavExtractor implements Extractor {
   public static final ExtractorsFactory FACTORY;
   private int bytesPerFrame;
   private ExtractorOutput extractorOutput;
   private int pendingBytes;
   private TrackOutput trackOutput;
   private WavHeader wavHeader;

   static {
      FACTORY = _$$Lambda$WavExtractor$5r6M_S0QCNNj_Xavzq9WwuFHep0.INSTANCE;
   }

   // $FF: synthetic method
   static Extractor[] lambda$static$0() {
      return new Extractor[]{new WavExtractor()};
   }

   public void init(ExtractorOutput var1) {
      this.extractorOutput = var1;
      this.trackOutput = var1.track(0, 1);
      this.wavHeader = null;
      var1.endTracks();
   }

   public int read(ExtractorInput var1, PositionHolder var2) throws IOException, InterruptedException {
      if (this.wavHeader == null) {
         this.wavHeader = WavHeaderReader.peek(var1);
         WavHeader var9 = this.wavHeader;
         if (var9 == null) {
            throw new ParserException("Unsupported or unrecognized wav header.");
         }

         Format var10 = Format.createAudioSampleFormat((String)null, "audio/raw", (String)null, var9.getBitrate(), 32768, this.wavHeader.getNumChannels(), this.wavHeader.getSampleRateHz(), this.wavHeader.getEncoding(), (List)null, (DrmInitData)null, 0, (String)null);
         this.trackOutput.format(var10);
         this.bytesPerFrame = this.wavHeader.getBytesPerFrame();
      }

      if (!this.wavHeader.hasDataBounds()) {
         WavHeaderReader.skipToData(var1, this.wavHeader);
         this.extractorOutput.seekMap(this.wavHeader);
      }

      long var3 = this.wavHeader.getDataLimit();
      byte var5 = 0;
      boolean var6;
      if (var3 != -1L) {
         var6 = true;
      } else {
         var6 = false;
      }

      Assertions.checkState(var6);
      var3 -= var1.getPosition();
      if (var3 <= 0L) {
         return -1;
      } else {
         int var7 = (int)Math.min((long)('耀' - this.pendingBytes), var3);
         var7 = this.trackOutput.sampleData(var1, var7, true);
         if (var7 != -1) {
            this.pendingBytes += var7;
         }

         int var8 = this.pendingBytes / this.bytesPerFrame;
         if (var8 > 0) {
            var3 = this.wavHeader.getTimeUs(var1.getPosition() - (long)this.pendingBytes);
            var8 *= this.bytesPerFrame;
            this.pendingBytes -= var8;
            this.trackOutput.sampleMetadata(var3, 1, var8, this.pendingBytes, (TrackOutput.CryptoData)null);
         }

         if (var7 == -1) {
            var5 = -1;
         }

         return var5;
      }
   }

   public void release() {
   }

   public void seek(long var1, long var3) {
      this.pendingBytes = 0;
   }

   public boolean sniff(ExtractorInput var1) throws IOException, InterruptedException {
      boolean var2;
      if (WavHeaderReader.peek(var1) != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }
}
