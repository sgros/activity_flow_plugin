package com.google.android.exoplayer2.source;

// $FF: synthetic class
public final class _$$Lambda$MediaSourceEventListener$EventDispatcher$PV8wmqGm7vRMJNlt__V3zhXfxiE implements Runnable {
   // $FF: synthetic field
   private final MediaSourceEventListener.EventDispatcher f$0;
   // $FF: synthetic field
   private final MediaSourceEventListener f$1;
   // $FF: synthetic field
   private final MediaSource.MediaPeriodId f$2;

   // $FF: synthetic method
   public _$$Lambda$MediaSourceEventListener$EventDispatcher$PV8wmqGm7vRMJNlt__V3zhXfxiE(MediaSourceEventListener.EventDispatcher var1, MediaSourceEventListener var2, MediaSource.MediaPeriodId var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$readingStarted$6$MediaSourceEventListener$EventDispatcher(this.f$1, this.f$2);
   }
}
