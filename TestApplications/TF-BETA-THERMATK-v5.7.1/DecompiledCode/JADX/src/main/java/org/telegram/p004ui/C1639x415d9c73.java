package org.telegram.p004ui;

import org.telegram.p004ui.LoginActivity.LoginActivityResetWaitView;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$LoginActivity$LoginActivityResetWaitView$ehRWxs7c1MtBNOs2EfgHKXhZShY */
public final /* synthetic */ class C1639x415d9c73 implements Runnable {
    private final /* synthetic */ LoginActivityResetWaitView f$0;
    private final /* synthetic */ TL_error f$1;

    public /* synthetic */ C1639x415d9c73(LoginActivityResetWaitView loginActivityResetWaitView, TL_error tL_error) {
        this.f$0 = loginActivityResetWaitView;
        this.f$1 = tL_error;
    }

    public final void run() {
        this.f$0.lambda$null$0$LoginActivity$LoginActivityResetWaitView(this.f$1);
    }
}
