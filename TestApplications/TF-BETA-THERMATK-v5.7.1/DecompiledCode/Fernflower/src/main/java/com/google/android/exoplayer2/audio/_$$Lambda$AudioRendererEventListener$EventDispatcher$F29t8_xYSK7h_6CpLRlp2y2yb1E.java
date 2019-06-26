package com.google.android.exoplayer2.audio;

// $FF: synthetic class
public final class _$$Lambda$AudioRendererEventListener$EventDispatcher$F29t8_xYSK7h_6CpLRlp2y2yb1E implements Runnable {
   // $FF: synthetic field
   private final AudioRendererEventListener.EventDispatcher f$0;
   // $FF: synthetic field
   private final String f$1;
   // $FF: synthetic field
   private final long f$2;
   // $FF: synthetic field
   private final long f$3;

   // $FF: synthetic method
   public _$$Lambda$AudioRendererEventListener$EventDispatcher$F29t8_xYSK7h_6CpLRlp2y2yb1E(AudioRendererEventListener.EventDispatcher var1, String var2, long var3, long var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var5;
   }

   public final void run() {
      this.f$0.lambda$decoderInitialized$1$AudioRendererEventListener$EventDispatcher(this.f$1, this.f$2, this.f$3);
   }
}
