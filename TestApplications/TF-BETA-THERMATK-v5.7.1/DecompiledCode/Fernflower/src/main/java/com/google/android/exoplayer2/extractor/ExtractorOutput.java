package com.google.android.exoplayer2.extractor;

public interface ExtractorOutput {
   void endTracks();

   void seekMap(SeekMap var1);

   TrackOutput track(int var1, int var2);
}
