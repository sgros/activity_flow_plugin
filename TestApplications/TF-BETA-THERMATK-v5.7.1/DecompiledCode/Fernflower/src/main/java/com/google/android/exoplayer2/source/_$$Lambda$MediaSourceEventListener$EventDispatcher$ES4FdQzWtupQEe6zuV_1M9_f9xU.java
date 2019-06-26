package com.google.android.exoplayer2.source;

// $FF: synthetic class
public final class _$$Lambda$MediaSourceEventListener$EventDispatcher$ES4FdQzWtupQEe6zuV_1M9_f9xU implements Runnable {
   // $FF: synthetic field
   private final MediaSourceEventListener.EventDispatcher f$0;
   // $FF: synthetic field
   private final MediaSourceEventListener f$1;
   // $FF: synthetic field
   private final MediaSourceEventListener.MediaLoadData f$2;

   // $FF: synthetic method
   public _$$Lambda$MediaSourceEventListener$EventDispatcher$ES4FdQzWtupQEe6zuV_1M9_f9xU(MediaSourceEventListener.EventDispatcher var1, MediaSourceEventListener var2, MediaSourceEventListener.MediaLoadData var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$downstreamFormatChanged$8$MediaSourceEventListener$EventDispatcher(this.f$1, this.f$2);
   }
}
