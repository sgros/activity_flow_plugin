package org.telegram.p004ui;

import android.os.Bundle;
import org.telegram.p004ui.LoginActivity.LoginActivitySmsView;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$LoginActivity$LoginActivitySmsView$PO7r5AWMTSHLIApv2-6O4ar48qM */
public final /* synthetic */ class C1645xc1a23552 implements Runnable {
    private final /* synthetic */ LoginActivitySmsView f$0;
    private final /* synthetic */ TL_error f$1;
    private final /* synthetic */ Bundle f$2;
    private final /* synthetic */ TLObject f$3;

    public /* synthetic */ C1645xc1a23552(LoginActivitySmsView loginActivitySmsView, TL_error tL_error, Bundle bundle, TLObject tLObject) {
        this.f$0 = loginActivitySmsView;
        this.f$1 = tL_error;
        this.f$2 = bundle;
        this.f$3 = tLObject;
    }

    public final void run() {
        this.f$0.lambda$null$1$LoginActivity$LoginActivitySmsView(this.f$1, this.f$2, this.f$3);
    }
}
