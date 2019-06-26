package com.google.android.exoplayer2.extractor;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.io.EOFException;
import java.io.IOException;

public final class DummyTrackOutput implements TrackOutput {
   public void format(Format var1) {
   }

   public int sampleData(ExtractorInput var1, int var2, boolean var3) throws IOException, InterruptedException {
      var2 = var1.skip(var2);
      if (var2 == -1) {
         if (var3) {
            return -1;
         } else {
            throw new EOFException();
         }
      } else {
         return var2;
      }
   }

   public void sampleData(ParsableByteArray var1, int var2) {
      var1.skipBytes(var2);
   }

   public void sampleMetadata(long var1, int var3, int var4, int var5, TrackOutput.CryptoData var6) {
   }
}
