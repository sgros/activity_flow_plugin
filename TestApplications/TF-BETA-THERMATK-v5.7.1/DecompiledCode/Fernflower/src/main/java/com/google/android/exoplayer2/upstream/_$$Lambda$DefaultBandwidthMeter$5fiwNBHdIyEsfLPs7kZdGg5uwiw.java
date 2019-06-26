package com.google.android.exoplayer2.upstream;

import com.google.android.exoplayer2.util.EventDispatcher;

// $FF: synthetic class
public final class _$$Lambda$DefaultBandwidthMeter$5fiwNBHdIyEsfLPs7kZdGg5uwiw implements EventDispatcher.Event {
   // $FF: synthetic field
   private final int f$0;
   // $FF: synthetic field
   private final long f$1;
   // $FF: synthetic field
   private final long f$2;

   // $FF: synthetic method
   public _$$Lambda$DefaultBandwidthMeter$5fiwNBHdIyEsfLPs7kZdGg5uwiw(int var1, long var2, long var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var4;
   }

   public final void sendTo(Object var1) {
      DefaultBandwidthMeter.lambda$maybeNotifyBandwidthSample$0(this.f$0, this.f$1, this.f$2, (BandwidthMeter.EventListener)var1);
   }
}
