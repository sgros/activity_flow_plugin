package com.google.android.exoplayer2.source;

public interface SequenceableLoader {
   boolean continueLoading(long var1);

   long getBufferedPositionUs();

   long getNextLoadPositionUs();

   void reevaluateBuffer(long var1);

   public interface Callback {
      void onContinueLoadingRequested(SequenceableLoader var1);
   }
}
