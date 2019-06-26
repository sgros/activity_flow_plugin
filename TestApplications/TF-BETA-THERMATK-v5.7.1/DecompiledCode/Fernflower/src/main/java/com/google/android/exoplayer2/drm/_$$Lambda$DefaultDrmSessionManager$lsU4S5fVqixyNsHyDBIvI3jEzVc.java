package com.google.android.exoplayer2.drm;

import com.google.android.exoplayer2.util.EventDispatcher;

// $FF: synthetic class
public final class _$$Lambda$DefaultDrmSessionManager$lsU4S5fVqixyNsHyDBIvI3jEzVc implements EventDispatcher.Event {
   // $FF: synthetic field
   private final DefaultDrmSessionManager.MissingSchemeDataException f$0;

   // $FF: synthetic method
   public _$$Lambda$DefaultDrmSessionManager$lsU4S5fVqixyNsHyDBIvI3jEzVc(DefaultDrmSessionManager.MissingSchemeDataException var1) {
      this.f$0 = var1;
   }

   public final void sendTo(Object var1) {
      DefaultDrmSessionManager.lambda$acquireSession$0(this.f$0, (DefaultDrmSessionEventListener)var1);
   }
}
