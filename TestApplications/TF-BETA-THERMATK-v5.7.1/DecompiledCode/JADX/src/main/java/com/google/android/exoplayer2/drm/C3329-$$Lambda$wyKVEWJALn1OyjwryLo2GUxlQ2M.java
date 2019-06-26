package com.google.android.exoplayer2.drm;

import com.google.android.exoplayer2.util.EventDispatcher.Event;

/* compiled from: lambda */
/* renamed from: com.google.android.exoplayer2.drm.-$$Lambda$wyKVEWJALn1OyjwryLo2GUxlQ2M */
public final /* synthetic */ class C3329-$$Lambda$wyKVEWJALn1OyjwryLo2GUxlQ2M implements Event {
    public static final /* synthetic */ C3329-$$Lambda$wyKVEWJALn1OyjwryLo2GUxlQ2M INSTANCE = new C3329-$$Lambda$wyKVEWJALn1OyjwryLo2GUxlQ2M();

    private /* synthetic */ C3329-$$Lambda$wyKVEWJALn1OyjwryLo2GUxlQ2M() {
    }

    public final void sendTo(Object obj) {
        ((DefaultDrmSessionEventListener) obj).onDrmKeysLoaded();
    }
}
