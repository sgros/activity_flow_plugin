package com.google.android.exoplayer2.drm;

import com.google.android.exoplayer2.drm.-..Lambda.tzysvANfjWo6mXRxYD2fQMdks_4;
import com.google.android.exoplayer2.util.EventDispatcher;

// $FF: synthetic class
public final class _$$Lambda$tzysvANfjWo6mXRxYD2fQMdks_4 implements EventDispatcher.Event {
   // $FF: synthetic field
   public static final tzysvANfjWo6mXRxYD2fQMdks_4 INSTANCE = new _$$Lambda$tzysvANfjWo6mXRxYD2fQMdks_4();

   // $FF: synthetic method
   private _$$Lambda$tzysvANfjWo6mXRxYD2fQMdks_4() {
   }

   public final void sendTo(Object var1) {
      ((DefaultDrmSessionEventListener)var1).onDrmKeysRestored();
   }
}
