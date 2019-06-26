package org.telegram.p004ui;

import org.telegram.p004ui.LoginActivity.LoginActivitySmsView;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_auth_signIn;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$LoginActivity$LoginActivitySmsView$ClZInkb3pcX3d_IEp0qMTOZ0dwc */
public final /* synthetic */ class C3742x12e6cfd6 implements RequestDelegate {
    private final /* synthetic */ LoginActivitySmsView f$0;
    private final /* synthetic */ TL_auth_signIn f$1;

    public /* synthetic */ C3742x12e6cfd6(LoginActivitySmsView loginActivitySmsView, TL_auth_signIn tL_auth_signIn) {
        this.f$0 = loginActivitySmsView;
        this.f$1 = tL_auth_signIn;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$null$6$LoginActivity$LoginActivitySmsView(this.f$1, tLObject, tL_error);
    }
}
