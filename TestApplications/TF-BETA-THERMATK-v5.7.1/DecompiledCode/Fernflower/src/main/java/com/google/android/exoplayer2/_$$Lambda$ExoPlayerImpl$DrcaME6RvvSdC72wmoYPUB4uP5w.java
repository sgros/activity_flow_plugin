package com.google.android.exoplayer2;

import java.util.concurrent.CopyOnWriteArrayList;

// $FF: synthetic class
public final class _$$Lambda$ExoPlayerImpl$DrcaME6RvvSdC72wmoYPUB4uP5w implements Runnable {
   // $FF: synthetic field
   private final CopyOnWriteArrayList f$0;
   // $FF: synthetic field
   private final BasePlayer.ListenerInvocation f$1;

   // $FF: synthetic method
   public _$$Lambda$ExoPlayerImpl$DrcaME6RvvSdC72wmoYPUB4uP5w(CopyOnWriteArrayList var1, BasePlayer.ListenerInvocation var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      ExoPlayerImpl.lambda$notifyListeners$6(this.f$0, this.f$1);
   }
}
