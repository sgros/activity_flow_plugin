package com.google.android.exoplayer2.extractor;

import java.io.IOException;

public interface Extractor {
   void init(ExtractorOutput var1);

   int read(ExtractorInput var1, PositionHolder var2) throws IOException, InterruptedException;

   void release();

   void seek(long var1, long var3);

   boolean sniff(ExtractorInput var1) throws IOException, InterruptedException;
}
