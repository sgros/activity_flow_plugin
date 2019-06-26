package com.google.android.exoplayer2.video;

import com.google.android.exoplayer2.decoder.DecoderCounters;

// $FF: synthetic class
public final class _$$Lambda$VideoRendererEventListener$EventDispatcher$qTQ_0WnG_WelRJ9iR8L0OaiS0Go implements Runnable {
   // $FF: synthetic field
   private final VideoRendererEventListener.EventDispatcher f$0;
   // $FF: synthetic field
   private final DecoderCounters f$1;

   // $FF: synthetic method
   public _$$Lambda$VideoRendererEventListener$EventDispatcher$qTQ_0WnG_WelRJ9iR8L0OaiS0Go(VideoRendererEventListener.EventDispatcher var1, DecoderCounters var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$disabled$6$VideoRendererEventListener$EventDispatcher(this.f$1);
   }
}
