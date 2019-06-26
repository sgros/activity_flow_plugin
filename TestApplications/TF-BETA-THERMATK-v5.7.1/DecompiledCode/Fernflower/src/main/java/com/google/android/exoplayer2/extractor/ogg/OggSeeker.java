package com.google.android.exoplayer2.extractor.ogg;

import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.SeekMap;
import java.io.IOException;

interface OggSeeker {
   SeekMap createSeekMap();

   long read(ExtractorInput var1) throws IOException, InterruptedException;

   long startSeek(long var1);
}
