package com.google.android.exoplayer2.source;

// $FF: synthetic class
public final class _$$Lambda$MediaSourceEventListener$EventDispatcher$IejPnkXyHgj2V1iyO1dqtBKfihI implements Runnable {
   // $FF: synthetic field
   private final MediaSourceEventListener.EventDispatcher f$0;
   // $FF: synthetic field
   private final MediaSourceEventListener f$1;
   // $FF: synthetic field
   private final MediaSourceEventListener.LoadEventInfo f$2;
   // $FF: synthetic field
   private final MediaSourceEventListener.MediaLoadData f$3;

   // $FF: synthetic method
   public _$$Lambda$MediaSourceEventListener$EventDispatcher$IejPnkXyHgj2V1iyO1dqtBKfihI(MediaSourceEventListener.EventDispatcher var1, MediaSourceEventListener var2, MediaSourceEventListener.LoadEventInfo var3, MediaSourceEventListener.MediaLoadData var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$loadCompleted$3$MediaSourceEventListener$EventDispatcher(this.f$1, this.f$2, this.f$3);
   }
}
