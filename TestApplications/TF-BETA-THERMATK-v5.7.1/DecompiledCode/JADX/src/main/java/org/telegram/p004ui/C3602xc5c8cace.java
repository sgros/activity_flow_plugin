package org.telegram.p004ui;

import android.os.Bundle;
import org.telegram.p004ui.ChangePhoneActivity.PhoneView;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_account_sendChangePhoneCode;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChangePhoneActivity$PhoneView$osRwjZiwNNc4O0LSbhXJvCtt8fY */
public final /* synthetic */ class C3602xc5c8cace implements RequestDelegate {
    private final /* synthetic */ PhoneView f$0;
    private final /* synthetic */ Bundle f$1;
    private final /* synthetic */ TL_account_sendChangePhoneCode f$2;

    public /* synthetic */ C3602xc5c8cace(PhoneView phoneView, Bundle bundle, TL_account_sendChangePhoneCode tL_account_sendChangePhoneCode) {
        this.f$0 = phoneView;
        this.f$1 = bundle;
        this.f$2 = tL_account_sendChangePhoneCode;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$onNextPressed$7$ChangePhoneActivity$PhoneView(this.f$1, this.f$2, tLObject, tL_error);
    }
}
