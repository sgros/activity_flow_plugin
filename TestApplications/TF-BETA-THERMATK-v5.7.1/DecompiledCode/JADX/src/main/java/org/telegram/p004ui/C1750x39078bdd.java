package org.telegram.p004ui;

import android.os.Bundle;
import org.telegram.p004ui.PassportActivity.PhoneConfirmationView;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_auth_resendCode;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PassportActivity$PhoneConfirmationView$UHXDno5AXqEJ78FjOAmHbJDCDI4 */
public final /* synthetic */ class C1750x39078bdd implements Runnable {
    private final /* synthetic */ PhoneConfirmationView f$0;
    private final /* synthetic */ TL_error f$1;
    private final /* synthetic */ Bundle f$2;
    private final /* synthetic */ TLObject f$3;
    private final /* synthetic */ TL_auth_resendCode f$4;

    public /* synthetic */ C1750x39078bdd(PhoneConfirmationView phoneConfirmationView, TL_error tL_error, Bundle bundle, TLObject tLObject, TL_auth_resendCode tL_auth_resendCode) {
        this.f$0 = phoneConfirmationView;
        this.f$1 = tL_error;
        this.f$2 = bundle;
        this.f$3 = tLObject;
        this.f$4 = tL_auth_resendCode;
    }

    public final void run() {
        this.f$0.lambda$null$2$PassportActivity$PhoneConfirmationView(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
