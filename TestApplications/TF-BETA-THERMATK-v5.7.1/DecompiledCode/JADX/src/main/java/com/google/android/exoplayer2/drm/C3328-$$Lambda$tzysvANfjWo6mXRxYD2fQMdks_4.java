package com.google.android.exoplayer2.drm;

import com.google.android.exoplayer2.util.EventDispatcher.Event;

/* compiled from: lambda */
/* renamed from: com.google.android.exoplayer2.drm.-$$Lambda$tzysvANfjWo6mXRxYD2fQMdks_4 */
public final /* synthetic */ class C3328-$$Lambda$tzysvANfjWo6mXRxYD2fQMdks_4 implements Event {
    public static final /* synthetic */ C3328-$$Lambda$tzysvANfjWo6mXRxYD2fQMdks_4 INSTANCE = new C3328-$$Lambda$tzysvANfjWo6mXRxYD2fQMdks_4();

    private /* synthetic */ C3328-$$Lambda$tzysvANfjWo6mXRxYD2fQMdks_4() {
    }

    public final void sendTo(Object obj) {
        ((DefaultDrmSessionEventListener) obj).onDrmKeysRestored();
    }
}
