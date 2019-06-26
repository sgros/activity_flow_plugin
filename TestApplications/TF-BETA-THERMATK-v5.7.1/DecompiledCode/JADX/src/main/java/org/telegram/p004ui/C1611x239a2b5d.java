package org.telegram.p004ui;

import org.telegram.p004ui.LoginActivity.LoginActivityPasswordView;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$LoginActivity$LoginActivityPasswordView$UcCVsMRXQ-iTPOpCCyDnzwrWa-M */
public final /* synthetic */ class C1611x239a2b5d implements Runnable {
    private final /* synthetic */ LoginActivityPasswordView f$0;
    private final /* synthetic */ TL_error f$1;
    private final /* synthetic */ TLObject f$2;

    public /* synthetic */ C1611x239a2b5d(LoginActivityPasswordView loginActivityPasswordView, TL_error tL_error, TLObject tLObject) {
        this.f$0 = loginActivityPasswordView;
        this.f$1 = tL_error;
        this.f$2 = tLObject;
    }

    public final void run() {
        this.f$0.lambda$null$2$LoginActivity$LoginActivityPasswordView(this.f$1, this.f$2);
    }
}
