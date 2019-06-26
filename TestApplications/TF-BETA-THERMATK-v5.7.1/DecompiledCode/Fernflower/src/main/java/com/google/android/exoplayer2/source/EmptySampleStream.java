package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import java.io.IOException;

public final class EmptySampleStream implements SampleStream {
   public boolean isReady() {
      return true;
   }

   public void maybeThrowError() throws IOException {
   }

   public int readData(FormatHolder var1, DecoderInputBuffer var2, boolean var3) {
      var2.setFlags(4);
      return -4;
   }

   public int skipData(long var1) {
      return 0;
   }
}
