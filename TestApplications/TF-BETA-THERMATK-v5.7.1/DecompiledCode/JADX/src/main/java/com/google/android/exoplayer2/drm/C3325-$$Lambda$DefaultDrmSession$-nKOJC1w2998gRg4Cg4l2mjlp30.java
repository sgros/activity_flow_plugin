package com.google.android.exoplayer2.drm;

import com.google.android.exoplayer2.util.EventDispatcher.Event;

/* compiled from: lambda */
/* renamed from: com.google.android.exoplayer2.drm.-$$Lambda$DefaultDrmSession$-nKOJC1w2998gRg4Cg4l2mjlp30 */
public final /* synthetic */ class C3325-$$Lambda$DefaultDrmSession$-nKOJC1w2998gRg4Cg4l2mjlp30 implements Event {
    private final /* synthetic */ Exception f$0;

    public /* synthetic */ C3325-$$Lambda$DefaultDrmSession$-nKOJC1w2998gRg4Cg4l2mjlp30(Exception exception) {
        this.f$0 = exception;
    }

    public final void sendTo(Object obj) {
        ((DefaultDrmSessionEventListener) obj).onDrmSessionManagerError(this.f$0);
    }
}
