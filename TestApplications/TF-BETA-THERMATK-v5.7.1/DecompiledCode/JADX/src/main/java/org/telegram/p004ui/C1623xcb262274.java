package org.telegram.p004ui;

import org.telegram.p004ui.LoginActivity.LoginActivityRecoverView;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$LoginActivity$LoginActivityRecoverView$xRlt0wartDC6m84eJ3SO5QPoI50 */
public final /* synthetic */ class C1623xcb262274 implements Runnable {
    private final /* synthetic */ LoginActivityRecoverView f$0;
    private final /* synthetic */ TL_error f$1;
    private final /* synthetic */ TLObject f$2;

    public /* synthetic */ C1623xcb262274(LoginActivityRecoverView loginActivityRecoverView, TL_error tL_error, TLObject tLObject) {
        this.f$0 = loginActivityRecoverView;
        this.f$1 = tL_error;
        this.f$2 = tLObject;
    }

    public final void run() {
        this.f$0.lambda$null$4$LoginActivity$LoginActivityRecoverView(this.f$1, this.f$2);
    }
}
