package com.google.android.exoplayer2.drm;

import com.google.android.exoplayer2.drm.-..Lambda.1U2yJBSMBm8ESUSz9LUzNXtoVus;
import com.google.android.exoplayer2.util.EventDispatcher;

// $FF: synthetic class
public final class _$$Lambda$1U2yJBSMBm8ESUSz9LUzNXtoVus implements EventDispatcher.Event {
   // $FF: synthetic field
   public static final 1U2yJBSMBm8ESUSz9LUzNXtoVus INSTANCE = new _$$Lambda$1U2yJBSMBm8ESUSz9LUzNXtoVus();

   // $FF: synthetic method
   private _$$Lambda$1U2yJBSMBm8ESUSz9LUzNXtoVus() {
   }

   public final void sendTo(Object var1) {
      ((DefaultDrmSessionEventListener)var1).onDrmSessionReleased();
   }
}
