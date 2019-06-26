package org.telegram.p004ui;

import org.telegram.p004ui.LoginActivity.LoginActivityRegisterView;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$LoginActivity$LoginActivityRegisterView$f0ZfM2Vj75AOg7vhpvjVYpg5d40 */
public final /* synthetic */ class C3738x814f93cc implements RequestDelegate {
    private final /* synthetic */ LoginActivityRegisterView f$0;

    public /* synthetic */ C3738x814f93cc(LoginActivityRegisterView loginActivityRegisterView) {
        this.f$0 = loginActivityRegisterView;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$onNextPressed$13$LoginActivity$LoginActivityRegisterView(tLObject, tL_error);
    }
}
