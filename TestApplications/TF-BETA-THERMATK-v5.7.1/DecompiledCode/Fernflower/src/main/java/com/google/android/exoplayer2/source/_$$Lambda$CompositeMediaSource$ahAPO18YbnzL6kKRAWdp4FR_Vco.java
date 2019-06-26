package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.Timeline;

// $FF: synthetic class
public final class _$$Lambda$CompositeMediaSource$ahAPO18YbnzL6kKRAWdp4FR_Vco implements MediaSource.SourceInfoRefreshListener {
   // $FF: synthetic field
   private final CompositeMediaSource f$0;
   // $FF: synthetic field
   private final Object f$1;

   // $FF: synthetic method
   public _$$Lambda$CompositeMediaSource$ahAPO18YbnzL6kKRAWdp4FR_Vco(CompositeMediaSource var1, Object var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onSourceInfoRefreshed(MediaSource var1, Timeline var2, Object var3) {
      this.f$0.lambda$prepareChildSource$0$CompositeMediaSource(this.f$1, var1, var2, var3);
   }
}
