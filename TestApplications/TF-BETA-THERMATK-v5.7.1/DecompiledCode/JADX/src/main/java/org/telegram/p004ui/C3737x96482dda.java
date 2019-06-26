package org.telegram.p004ui;

import org.telegram.p004ui.LoginActivity.LoginActivityRecoverView;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$LoginActivity$LoginActivityRecoverView$rgaN2I5iElJnNYqU1iHy0vwcr6w */
public final /* synthetic */ class C3737x96482dda implements RequestDelegate {
    private final /* synthetic */ LoginActivityRecoverView f$0;

    public /* synthetic */ C3737x96482dda(LoginActivityRecoverView loginActivityRecoverView) {
        this.f$0 = loginActivityRecoverView;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$onNextPressed$5$LoginActivity$LoginActivityRecoverView(tLObject, tL_error);
    }
}
