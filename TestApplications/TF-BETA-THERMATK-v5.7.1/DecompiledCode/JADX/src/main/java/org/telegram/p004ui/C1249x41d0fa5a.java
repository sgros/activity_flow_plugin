package org.telegram.p004ui;

import android.os.Bundle;
import org.telegram.p004ui.ChangePhoneActivity.PhoneView;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_account_sendChangePhoneCode;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChangePhoneActivity$PhoneView$_8ZmDMwVcKhBgNHbawOvyWif9Ck */
public final /* synthetic */ class C1249x41d0fa5a implements Runnable {
    private final /* synthetic */ PhoneView f$0;
    private final /* synthetic */ TL_error f$1;
    private final /* synthetic */ Bundle f$2;
    private final /* synthetic */ TLObject f$3;
    private final /* synthetic */ TL_account_sendChangePhoneCode f$4;

    public /* synthetic */ C1249x41d0fa5a(PhoneView phoneView, TL_error tL_error, Bundle bundle, TLObject tLObject, TL_account_sendChangePhoneCode tL_account_sendChangePhoneCode) {
        this.f$0 = phoneView;
        this.f$1 = tL_error;
        this.f$2 = bundle;
        this.f$3 = tLObject;
        this.f$4 = tL_account_sendChangePhoneCode;
    }

    public final void run() {
        this.f$0.lambda$null$6$ChangePhoneActivity$PhoneView(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
