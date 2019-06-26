package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import java.io.IOException;

public interface MediaPeriod extends SequenceableLoader {
   boolean continueLoading(long var1);

   void discardBuffer(long var1, boolean var3);

   long getAdjustedSeekPositionUs(long var1, SeekParameters var3);

   long getBufferedPositionUs();

   long getNextLoadPositionUs();

   TrackGroupArray getTrackGroups();

   void maybeThrowPrepareError() throws IOException;

   void prepare(MediaPeriod.Callback var1, long var2);

   long readDiscontinuity();

   void reevaluateBuffer(long var1);

   long seekToUs(long var1);

   long selectTracks(TrackSelection[] var1, boolean[] var2, SampleStream[] var3, boolean[] var4, long var5);

   public interface Callback extends SequenceableLoader.Callback {
      void onPrepared(MediaPeriod var1);
   }
}
