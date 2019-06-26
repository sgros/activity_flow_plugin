package org.telegram.p004ui;

import org.telegram.p004ui.Cells.TextCheckCell;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PrivacySettingsActivity$9W-WSazbZdTZMbEaunliHg9yKU0 */
public final /* synthetic */ class C3831-$$Lambda$PrivacySettingsActivity$9W-WSazbZdTZMbEaunliHg9yKU0 implements RequestDelegate {
    private final /* synthetic */ PrivacySettingsActivity f$0;
    private final /* synthetic */ TextCheckCell f$1;

    public /* synthetic */ C3831-$$Lambda$PrivacySettingsActivity$9W-WSazbZdTZMbEaunliHg9yKU0(PrivacySettingsActivity privacySettingsActivity, TextCheckCell textCheckCell) {
        this.f$0 = privacySettingsActivity;
        this.f$1 = textCheckCell;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$null$10$PrivacySettingsActivity(this.f$1, tLObject, tL_error);
    }
}
