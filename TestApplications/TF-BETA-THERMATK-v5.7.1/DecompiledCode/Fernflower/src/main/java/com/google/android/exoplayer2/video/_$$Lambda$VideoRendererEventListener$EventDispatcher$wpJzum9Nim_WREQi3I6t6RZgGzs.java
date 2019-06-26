package com.google.android.exoplayer2.video;

// $FF: synthetic class
public final class _$$Lambda$VideoRendererEventListener$EventDispatcher$wpJzum9Nim_WREQi3I6t6RZgGzs implements Runnable {
   // $FF: synthetic field
   private final VideoRendererEventListener.EventDispatcher f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final long f$2;

   // $FF: synthetic method
   public _$$Lambda$VideoRendererEventListener$EventDispatcher$wpJzum9Nim_WREQi3I6t6RZgGzs(VideoRendererEventListener.EventDispatcher var1, int var2, long var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$droppedFrames$3$VideoRendererEventListener$EventDispatcher(this.f$1, this.f$2);
   }
}
