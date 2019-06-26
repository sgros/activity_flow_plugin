package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.Collections;
import java.util.List;

public final class DvbSubtitleReader implements ElementaryStreamReader {
   private int bytesToCheck;
   private final TrackOutput[] outputs;
   private int sampleBytesWritten;
   private long sampleTimeUs;
   private final List subtitleInfos;
   private boolean writingSample;

   public DvbSubtitleReader(List var1) {
      this.subtitleInfos = var1;
      this.outputs = new TrackOutput[var1.size()];
   }

   private boolean checkNextByte(ParsableByteArray var1, int var2) {
      if (var1.bytesLeft() == 0) {
         return false;
      } else {
         if (var1.readUnsignedByte() != var2) {
            this.writingSample = false;
         }

         --this.bytesToCheck;
         return this.writingSample;
      }
   }

   public void consume(ParsableByteArray var1) {
      if (this.writingSample) {
         if (this.bytesToCheck == 2 && !this.checkNextByte(var1, 32)) {
            return;
         }

         int var2 = this.bytesToCheck;
         int var3 = 0;
         if (var2 == 1 && !this.checkNextByte(var1, 0)) {
            return;
         }

         int var4 = var1.getPosition();
         var2 = var1.bytesLeft();
         TrackOutput[] var5 = this.outputs;

         for(int var6 = var5.length; var3 < var6; ++var3) {
            TrackOutput var7 = var5[var3];
            var1.setPosition(var4);
            var7.sampleData(var1, var2);
         }

         this.sampleBytesWritten += var2;
      }

   }

   public void createTracks(ExtractorOutput var1, TsPayloadReader.TrackIdGenerator var2) {
      for(int var3 = 0; var3 < this.outputs.length; ++var3) {
         TsPayloadReader.DvbSubtitleInfo var4 = (TsPayloadReader.DvbSubtitleInfo)this.subtitleInfos.get(var3);
         var2.generateNewId();
         TrackOutput var5 = var1.track(var2.getTrackId(), 3);
         var5.format(Format.createImageSampleFormat(var2.getFormatId(), "application/dvbsubs", (String)null, -1, 0, Collections.singletonList(var4.initializationData), var4.language, (DrmInitData)null));
         this.outputs[var3] = var5;
      }

   }

   public void packetFinished() {
      if (this.writingSample) {
         TrackOutput[] var1 = this.outputs;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            var1[var3].sampleMetadata(this.sampleTimeUs, 1, this.sampleBytesWritten, 0, (TrackOutput.CryptoData)null);
         }

         this.writingSample = false;
      }

   }

   public void packetStarted(long var1, int var3) {
      if ((var3 & 4) != 0) {
         this.writingSample = true;
         this.sampleTimeUs = var1;
         this.sampleBytesWritten = 0;
         this.bytesToCheck = 2;
      }
   }

   public void seek() {
      this.writingSample = false;
   }
}
