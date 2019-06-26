package com.google.android.exoplayer2.drm;

import com.google.android.exoplayer2.util.EventDispatcher.Event;

/* compiled from: lambda */
/* renamed from: com.google.android.exoplayer2.drm.-$$Lambda$jFcVU4qXZB2nhSZWHXCB9S7MtRI */
public final /* synthetic */ class C3327-$$Lambda$jFcVU4qXZB2nhSZWHXCB9S7MtRI implements Event {
    public static final /* synthetic */ C3327-$$Lambda$jFcVU4qXZB2nhSZWHXCB9S7MtRI INSTANCE = new C3327-$$Lambda$jFcVU4qXZB2nhSZWHXCB9S7MtRI();

    private /* synthetic */ C3327-$$Lambda$jFcVU4qXZB2nhSZWHXCB9S7MtRI() {
    }

    public final void sendTo(Object obj) {
        ((DefaultDrmSessionEventListener) obj).onDrmSessionAcquired();
    }
}
