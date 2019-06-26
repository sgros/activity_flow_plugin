package com.google.android.exoplayer2.extractor.mkv;

import com.google.android.exoplayer2.extractor.ExtractorInput;
import java.io.IOException;

interface EbmlReader {
   void init(EbmlReaderOutput var1);

   boolean read(ExtractorInput var1) throws IOException, InterruptedException;

   void reset();
}
