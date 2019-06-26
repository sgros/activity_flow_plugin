package org.telegram.p004ui;

import android.os.Bundle;
import org.telegram.p004ui.LoginActivity.PhoneView;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_auth_sendCode;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$LoginActivity$PhoneView$sXHfoJDr0ky4AX-vOjRSCgCsccc */
public final /* synthetic */ class C3746-$$Lambda$LoginActivity$PhoneView$sXHfoJDr0ky4AX-vOjRSCgCsccc implements RequestDelegate {
    private final /* synthetic */ PhoneView f$0;
    private final /* synthetic */ Bundle f$1;
    private final /* synthetic */ TL_auth_sendCode f$2;

    public /* synthetic */ C3746-$$Lambda$LoginActivity$PhoneView$sXHfoJDr0ky4AX-vOjRSCgCsccc(PhoneView phoneView, Bundle bundle, TL_auth_sendCode tL_auth_sendCode) {
        this.f$0 = phoneView;
        this.f$1 = bundle;
        this.f$2 = tL_auth_sendCode;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$onNextPressed$9$LoginActivity$PhoneView(this.f$1, this.f$2, tLObject, tL_error);
    }
}
