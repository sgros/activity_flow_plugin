package com.google.android.exoplayer2.video;

// $FF: synthetic class
public final class _$$Lambda$VideoRendererEventListener$EventDispatcher$Y232CA7hogfrRJjYu2VeUSxg0VQ implements Runnable {
   // $FF: synthetic field
   private final VideoRendererEventListener.EventDispatcher f$0;
   // $FF: synthetic field
   private final String f$1;
   // $FF: synthetic field
   private final long f$2;
   // $FF: synthetic field
   private final long f$3;

   // $FF: synthetic method
   public _$$Lambda$VideoRendererEventListener$EventDispatcher$Y232CA7hogfrRJjYu2VeUSxg0VQ(VideoRendererEventListener.EventDispatcher var1, String var2, long var3, long var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var5;
   }

   public final void run() {
      this.f$0.lambda$decoderInitialized$1$VideoRendererEventListener$EventDispatcher(this.f$1, this.f$2, this.f$3);
   }
}
