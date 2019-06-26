package com.google.android.exoplayer2.source;

import java.io.IOException;

// $FF: synthetic class
public final class _$$Lambda$MediaSourceEventListener$EventDispatcher$0X_TAsNqR4TUW1yA_ZD1_p3oT84 implements Runnable {
   // $FF: synthetic field
   private final MediaSourceEventListener.EventDispatcher f$0;
   // $FF: synthetic field
   private final MediaSourceEventListener f$1;
   // $FF: synthetic field
   private final MediaSourceEventListener.LoadEventInfo f$2;
   // $FF: synthetic field
   private final MediaSourceEventListener.MediaLoadData f$3;
   // $FF: synthetic field
   private final IOException f$4;
   // $FF: synthetic field
   private final boolean f$5;

   // $FF: synthetic method
   public _$$Lambda$MediaSourceEventListener$EventDispatcher$0X_TAsNqR4TUW1yA_ZD1_p3oT84(MediaSourceEventListener.EventDispatcher var1, MediaSourceEventListener var2, MediaSourceEventListener.LoadEventInfo var3, MediaSourceEventListener.MediaLoadData var4, IOException var5, boolean var6) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
   }

   public final void run() {
      this.f$0.lambda$loadError$5$MediaSourceEventListener$EventDispatcher(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
   }
}
