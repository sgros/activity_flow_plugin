package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.Format;

// $FF: synthetic class
public final class _$$Lambda$AudioRendererEventListener$EventDispatcher$D7KvJbrpXrnWw4qzd_LI9ZtQytw implements Runnable {
   // $FF: synthetic field
   private final AudioRendererEventListener.EventDispatcher f$0;
   // $FF: synthetic field
   private final Format f$1;

   // $FF: synthetic method
   public _$$Lambda$AudioRendererEventListener$EventDispatcher$D7KvJbrpXrnWw4qzd_LI9ZtQytw(AudioRendererEventListener.EventDispatcher var1, Format var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$inputFormatChanged$2$AudioRendererEventListener$EventDispatcher(this.f$1);
   }
}
