package com.google.android.exoplayer2.drm;

import com.google.android.exoplayer2.drm.DefaultDrmSessionManager.MissingSchemeDataException;
import com.google.android.exoplayer2.util.EventDispatcher.Event;

/* compiled from: lambda */
/* renamed from: com.google.android.exoplayer2.drm.-$$Lambda$DefaultDrmSessionManager$lsU4S5fVqixyNsHyDBIvI3jEzVc */
public final /* synthetic */ class C3326-$$Lambda$DefaultDrmSessionManager$lsU4S5fVqixyNsHyDBIvI3jEzVc implements Event {
    private final /* synthetic */ MissingSchemeDataException f$0;

    public /* synthetic */ C3326-$$Lambda$DefaultDrmSessionManager$lsU4S5fVqixyNsHyDBIvI3jEzVc(MissingSchemeDataException missingSchemeDataException) {
        this.f$0 = missingSchemeDataException;
    }

    public final void sendTo(Object obj) {
        ((DefaultDrmSessionEventListener) obj).onDrmSessionManagerError(this.f$0);
    }
}
