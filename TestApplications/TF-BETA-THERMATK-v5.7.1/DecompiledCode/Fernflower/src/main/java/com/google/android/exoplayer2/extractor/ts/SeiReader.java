package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.text.cea.CeaUtil;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.List;

final class SeiReader {
   private final List closedCaptionFormats;
   private final TrackOutput[] outputs;

   public SeiReader(List var1) {
      this.closedCaptionFormats = var1;
      this.outputs = new TrackOutput[var1.size()];
   }

   public void consume(long var1, ParsableByteArray var3) {
      CeaUtil.consume(var1, var3, this.outputs);
   }

   public void createTracks(ExtractorOutput var1, TsPayloadReader.TrackIdGenerator var2) {
      for(int var3 = 0; var3 < this.outputs.length; ++var3) {
         var2.generateNewId();
         TrackOutput var4 = var1.track(var2.getTrackId(), 3);
         Format var5 = (Format)this.closedCaptionFormats.get(var3);
         String var6 = var5.sampleMimeType;
         boolean var7;
         if (!"application/cea-608".equals(var6) && !"application/cea-708".equals(var6)) {
            var7 = false;
         } else {
            var7 = true;
         }

         StringBuilder var8 = new StringBuilder();
         var8.append("Invalid closed caption mime type provided: ");
         var8.append(var6);
         Assertions.checkArgument(var7, var8.toString());
         String var9 = var5.id;
         if (var9 == null) {
            var9 = var2.getFormatId();
         }

         var4.format(Format.createTextSampleFormat(var9, var6, (String)null, -1, var5.selectionFlags, var5.language, var5.accessibilityChannel, (DrmInitData)null, Long.MAX_VALUE, var5.initializationData));
         this.outputs[var3] = var4;
      }

   }
}
