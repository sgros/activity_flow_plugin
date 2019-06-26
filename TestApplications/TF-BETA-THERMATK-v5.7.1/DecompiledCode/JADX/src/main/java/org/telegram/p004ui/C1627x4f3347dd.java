package org.telegram.p004ui;

import org.telegram.p004ui.LoginActivity.LoginActivityRegisterView;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$LoginActivity$LoginActivityRegisterView$7BLOUX6Psn1I67qrNVHHeqDT7F0 */
public final /* synthetic */ class C1627x4f3347dd implements Runnable {
    private final /* synthetic */ LoginActivityRegisterView f$0;
    private final /* synthetic */ TL_error f$1;
    private final /* synthetic */ TLObject f$2;

    public /* synthetic */ C1627x4f3347dd(LoginActivityRegisterView loginActivityRegisterView, TL_error tL_error, TLObject tLObject) {
        this.f$0 = loginActivityRegisterView;
        this.f$1 = tL_error;
        this.f$2 = tLObject;
    }

    public final void run() {
        this.f$0.lambda$null$12$LoginActivity$LoginActivityRegisterView(this.f$1, this.f$2);
    }
}
