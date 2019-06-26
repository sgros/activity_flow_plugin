package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import java.io.IOException;

public interface SampleStream {
   boolean isReady();

   void maybeThrowError() throws IOException;

   int readData(FormatHolder var1, DecoderInputBuffer var2, boolean var3);

   int skipData(long var1);
}
