package org.telegram.p004ui;

import java.util.HashMap;
import org.telegram.p004ui.LocationActivity.LocationActivityDelegate;
import org.telegram.tgnet.TLRPC.MessageMedia;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$LaunchActivity$vKuTfZca44pasLbBQmx8DozvnHc */
public final /* synthetic */ class C3725-$$Lambda$LaunchActivity$vKuTfZca44pasLbBQmx8DozvnHc implements LocationActivityDelegate {
    private final /* synthetic */ HashMap f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C3725-$$Lambda$LaunchActivity$vKuTfZca44pasLbBQmx8DozvnHc(HashMap hashMap, int i) {
        this.f$0 = hashMap;
        this.f$1 = i;
    }

    public final void didSelectLocation(MessageMedia messageMedia, int i) {
        LaunchActivity.lambda$null$42(this.f$0, this.f$1, messageMedia, i);
    }
}
