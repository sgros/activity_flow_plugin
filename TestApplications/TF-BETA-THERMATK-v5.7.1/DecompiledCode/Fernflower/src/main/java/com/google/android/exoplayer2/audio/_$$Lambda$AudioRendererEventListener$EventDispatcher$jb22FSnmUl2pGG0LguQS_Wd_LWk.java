package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.decoder.DecoderCounters;

// $FF: synthetic class
public final class _$$Lambda$AudioRendererEventListener$EventDispatcher$jb22FSnmUl2pGG0LguQS_Wd_LWk implements Runnable {
   // $FF: synthetic field
   private final AudioRendererEventListener.EventDispatcher f$0;
   // $FF: synthetic field
   private final DecoderCounters f$1;

   // $FF: synthetic method
   public _$$Lambda$AudioRendererEventListener$EventDispatcher$jb22FSnmUl2pGG0LguQS_Wd_LWk(AudioRendererEventListener.EventDispatcher var1, DecoderCounters var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$disabled$4$AudioRendererEventListener$EventDispatcher(this.f$1);
   }
}
