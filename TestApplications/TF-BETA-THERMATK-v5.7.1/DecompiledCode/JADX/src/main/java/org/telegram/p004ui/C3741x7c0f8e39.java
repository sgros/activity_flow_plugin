package org.telegram.p004ui;

import android.os.Bundle;
import org.telegram.p004ui.LoginActivity.LoginActivitySmsView;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$LoginActivity$LoginActivitySmsView$7Coa87SRXNZ2UoSLGVeBv7VQ3Fs */
public final /* synthetic */ class C3741x7c0f8e39 implements RequestDelegate {
    private final /* synthetic */ LoginActivitySmsView f$0;
    private final /* synthetic */ Bundle f$1;

    public /* synthetic */ C3741x7c0f8e39(LoginActivitySmsView loginActivitySmsView, Bundle bundle) {
        this.f$0 = loginActivitySmsView;
        this.f$1 = bundle;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$resendCode$2$LoginActivity$LoginActivitySmsView(this.f$1, tLObject, tL_error);
    }
}
