package com.google.android.exoplayer2;

import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.Allocator;

public interface LoadControl {
   Allocator getAllocator();

   long getBackBufferDurationUs();

   void onPrepared();

   void onReleased();

   void onStopped();

   void onTracksSelected(Renderer[] var1, TrackGroupArray var2, TrackSelectionArray var3);

   boolean retainBackBufferFromKeyframe();

   boolean shouldContinueLoading(long var1, float var3);

   boolean shouldStartPlayback(long var1, float var3, boolean var4);
}
