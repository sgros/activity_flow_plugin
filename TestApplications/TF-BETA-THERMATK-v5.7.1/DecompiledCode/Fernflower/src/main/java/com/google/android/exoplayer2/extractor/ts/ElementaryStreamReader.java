package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.util.ParsableByteArray;

public interface ElementaryStreamReader {
   void consume(ParsableByteArray var1) throws ParserException;

   void createTracks(ExtractorOutput var1, TsPayloadReader.TrackIdGenerator var2);

   void packetFinished();

   void packetStarted(long var1, int var3);

   void seek();
}
