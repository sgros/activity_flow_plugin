package com.google.android.exoplayer2.drm;

import com.google.android.exoplayer2.util.EventDispatcher;

// $FF: synthetic class
public final class _$$Lambda$DefaultDrmSession$_nKOJC1w2998gRg4Cg4l2mjlp30 implements EventDispatcher.Event {
   // $FF: synthetic field
   private final Exception f$0;

   // $FF: synthetic method
   public _$$Lambda$DefaultDrmSession$_nKOJC1w2998gRg4Cg4l2mjlp30(Exception var1) {
      this.f$0 = var1;
   }

   public final void sendTo(Object var1) {
      DefaultDrmSession.lambda$onError$0(this.f$0, (DefaultDrmSessionEventListener)var1);
   }
}
