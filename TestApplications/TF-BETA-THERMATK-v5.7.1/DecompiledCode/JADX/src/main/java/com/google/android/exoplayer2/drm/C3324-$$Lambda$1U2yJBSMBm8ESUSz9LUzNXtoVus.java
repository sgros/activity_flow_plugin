package com.google.android.exoplayer2.drm;

import com.google.android.exoplayer2.util.EventDispatcher.Event;

/* compiled from: lambda */
/* renamed from: com.google.android.exoplayer2.drm.-$$Lambda$1U2yJBSMBm8ESUSz9LUzNXtoVus */
public final /* synthetic */ class C3324-$$Lambda$1U2yJBSMBm8ESUSz9LUzNXtoVus implements Event {
    public static final /* synthetic */ C3324-$$Lambda$1U2yJBSMBm8ESUSz9LUzNXtoVus INSTANCE = new C3324-$$Lambda$1U2yJBSMBm8ESUSz9LUzNXtoVus();

    private /* synthetic */ C3324-$$Lambda$1U2yJBSMBm8ESUSz9LUzNXtoVus() {
    }

    public final void sendTo(Object obj) {
        ((DefaultDrmSessionEventListener) obj).onDrmSessionReleased();
    }
}
