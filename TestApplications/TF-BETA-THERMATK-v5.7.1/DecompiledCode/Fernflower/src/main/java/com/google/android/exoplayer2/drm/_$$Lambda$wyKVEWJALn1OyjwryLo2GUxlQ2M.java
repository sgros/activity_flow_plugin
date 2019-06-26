package com.google.android.exoplayer2.drm;

import com.google.android.exoplayer2.drm.-..Lambda.wyKVEWJALn1OyjwryLo2GUxlQ2M;
import com.google.android.exoplayer2.util.EventDispatcher;

// $FF: synthetic class
public final class _$$Lambda$wyKVEWJALn1OyjwryLo2GUxlQ2M implements EventDispatcher.Event {
   // $FF: synthetic field
   public static final wyKVEWJALn1OyjwryLo2GUxlQ2M INSTANCE = new _$$Lambda$wyKVEWJALn1OyjwryLo2GUxlQ2M();

   // $FF: synthetic method
   private _$$Lambda$wyKVEWJALn1OyjwryLo2GUxlQ2M() {
   }

   public final void sendTo(Object var1) {
      ((DefaultDrmSessionEventListener)var1).onDrmKeysLoaded();
   }
}
