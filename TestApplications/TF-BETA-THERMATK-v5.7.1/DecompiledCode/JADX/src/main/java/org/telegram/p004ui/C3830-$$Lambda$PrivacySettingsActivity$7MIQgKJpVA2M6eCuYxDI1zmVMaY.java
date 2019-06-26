package org.telegram.p004ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PrivacySettingsActivity$7MIQgKJpVA2M6eCuYxDI1zmVMaY */
public final /* synthetic */ class C3830-$$Lambda$PrivacySettingsActivity$7MIQgKJpVA2M6eCuYxDI1zmVMaY implements RequestDelegate {
    private final /* synthetic */ PrivacySettingsActivity f$0;

    public /* synthetic */ C3830-$$Lambda$PrivacySettingsActivity$7MIQgKJpVA2M6eCuYxDI1zmVMaY(PrivacySettingsActivity privacySettingsActivity) {
        this.f$0 = privacySettingsActivity;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$loadPasswordSettings$19$PrivacySettingsActivity(tLObject, tL_error);
    }
}
