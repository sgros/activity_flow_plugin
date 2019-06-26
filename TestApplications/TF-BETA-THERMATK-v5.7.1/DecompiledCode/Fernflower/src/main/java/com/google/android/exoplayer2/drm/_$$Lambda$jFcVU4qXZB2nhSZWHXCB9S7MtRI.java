package com.google.android.exoplayer2.drm;

import com.google.android.exoplayer2.drm.-..Lambda.jFcVU4qXZB2nhSZWHXCB9S7MtRI;
import com.google.android.exoplayer2.util.EventDispatcher;

// $FF: synthetic class
public final class _$$Lambda$jFcVU4qXZB2nhSZWHXCB9S7MtRI implements EventDispatcher.Event {
   // $FF: synthetic field
   public static final jFcVU4qXZB2nhSZWHXCB9S7MtRI INSTANCE = new _$$Lambda$jFcVU4qXZB2nhSZWHXCB9S7MtRI();

   // $FF: synthetic method
   private _$$Lambda$jFcVU4qXZB2nhSZWHXCB9S7MtRI() {
   }

   public final void sendTo(Object var1) {
      ((DefaultDrmSessionEventListener)var1).onDrmSessionAcquired();
   }
}
