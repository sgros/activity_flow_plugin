package com.google.android.exoplayer2.upstream;

import android.os.Handler;

public interface BandwidthMeter {
   void addEventListener(Handler var1, BandwidthMeter.EventListener var2);

   long getBitrateEstimate();

   TransferListener getTransferListener();

   void removeEventListener(BandwidthMeter.EventListener var1);

   public interface EventListener {
      void onBandwidthSample(int var1, long var2, long var4);
   }
}
