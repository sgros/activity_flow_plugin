package com.google.android.exoplayer2.video;

import com.google.android.exoplayer2.Format;

// $FF: synthetic class
public final class _$$Lambda$VideoRendererEventListener$EventDispatcher$26y6c6BFFT4OL6bJiMmdsfxDEMQ implements Runnable {
   // $FF: synthetic field
   private final VideoRendererEventListener.EventDispatcher f$0;
   // $FF: synthetic field
   private final Format f$1;

   // $FF: synthetic method
   public _$$Lambda$VideoRendererEventListener$EventDispatcher$26y6c6BFFT4OL6bJiMmdsfxDEMQ(VideoRendererEventListener.EventDispatcher var1, Format var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$inputFormatChanged$2$VideoRendererEventListener$EventDispatcher(this.f$1);
   }
}
