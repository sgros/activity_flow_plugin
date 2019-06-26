package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.decoder.DecoderCounters;

// $FF: synthetic class
public final class _$$Lambda$AudioRendererEventListener$EventDispatcher$MUMUaHcEfIpwDLi9gxmScOQxifc implements Runnable {
   // $FF: synthetic field
   private final AudioRendererEventListener.EventDispatcher f$0;
   // $FF: synthetic field
   private final DecoderCounters f$1;

   // $FF: synthetic method
   public _$$Lambda$AudioRendererEventListener$EventDispatcher$MUMUaHcEfIpwDLi9gxmScOQxifc(AudioRendererEventListener.EventDispatcher var1, DecoderCounters var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$enabled$0$AudioRendererEventListener$EventDispatcher(this.f$1);
   }
}
