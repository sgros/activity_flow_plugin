package org.telegram.p004ui;

import org.telegram.p004ui.ChangePhoneActivity.LoginActivitySmsView;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_account_changePhone;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChangePhoneActivity$LoginActivitySmsView$_a_ihjluIJ2vz2DCIrqa5SJZHuo */
public final /* synthetic */ class C3599x43bdcb51 implements RequestDelegate {
    private final /* synthetic */ LoginActivitySmsView f$0;
    private final /* synthetic */ TL_account_changePhone f$1;

    public /* synthetic */ C3599x43bdcb51(LoginActivitySmsView loginActivitySmsView, TL_account_changePhone tL_account_changePhone) {
        this.f$0 = loginActivitySmsView;
        this.f$1 = tL_account_changePhone;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$onNextPressed$7$ChangePhoneActivity$LoginActivitySmsView(this.f$1, tLObject, tL_error);
    }
}
