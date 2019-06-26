package com.google.android.exoplayer2.video;

import android.view.Surface;

// $FF: synthetic class
public final class _$$Lambda$VideoRendererEventListener$EventDispatcher$SFK5uUI0PHTm3Dg6Wdc1eRaQ9xk implements Runnable {
   // $FF: synthetic field
   private final VideoRendererEventListener.EventDispatcher f$0;
   // $FF: synthetic field
   private final Surface f$1;

   // $FF: synthetic method
   public _$$Lambda$VideoRendererEventListener$EventDispatcher$SFK5uUI0PHTm3Dg6Wdc1eRaQ9xk(VideoRendererEventListener.EventDispatcher var1, Surface var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$renderedFirstFrame$5$VideoRendererEventListener$EventDispatcher(this.f$1);
   }
}
