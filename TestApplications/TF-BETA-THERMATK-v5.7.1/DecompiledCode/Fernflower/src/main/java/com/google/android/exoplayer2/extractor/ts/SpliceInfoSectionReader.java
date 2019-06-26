package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;

public final class SpliceInfoSectionReader implements SectionPayloadReader {
   private boolean formatDeclared;
   private TrackOutput output;
   private TimestampAdjuster timestampAdjuster;

   public void consume(ParsableByteArray var1) {
      if (!this.formatDeclared) {
         if (this.timestampAdjuster.getTimestampOffsetUs() == -9223372036854775807L) {
            return;
         }

         this.output.format(Format.createSampleFormat((String)null, "application/x-scte35", this.timestampAdjuster.getTimestampOffsetUs()));
         this.formatDeclared = true;
      }

      int var2 = var1.bytesLeft();
      this.output.sampleData(var1, var2);
      this.output.sampleMetadata(this.timestampAdjuster.getLastAdjustedTimestampUs(), 1, var2, 0, (TrackOutput.CryptoData)null);
   }

   public void init(TimestampAdjuster var1, ExtractorOutput var2, TsPayloadReader.TrackIdGenerator var3) {
      this.timestampAdjuster = var1;
      var3.generateNewId();
      this.output = var2.track(var3.getTrackId(), 4);
      this.output.format(Format.createSampleFormat(var3.getFormatId(), "application/x-scte35", (String)null, -1, (DrmInitData)null));
   }
}
