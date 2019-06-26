package org.telegram.p004ui;

import android.os.Bundle;
import org.telegram.p004ui.CancelAccountDeletionActivity.LoginActivitySmsView;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_auth_resendCode;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$CancelAccountDeletionActivity$LoginActivitySmsView$ymkWVQFIhlznIih0Xq953phiTOo */
public final /* synthetic */ class C3594xc6f28bc9 implements RequestDelegate {
    private final /* synthetic */ LoginActivitySmsView f$0;
    private final /* synthetic */ Bundle f$1;
    private final /* synthetic */ TL_auth_resendCode f$2;

    public /* synthetic */ C3594xc6f28bc9(LoginActivitySmsView loginActivitySmsView, Bundle bundle, TL_auth_resendCode tL_auth_resendCode) {
        this.f$0 = loginActivitySmsView;
        this.f$1 = bundle;
        this.f$2 = tL_auth_resendCode;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.mo15415x5f36a36a(this.f$1, this.f$2, tLObject, tL_error);
    }
}
