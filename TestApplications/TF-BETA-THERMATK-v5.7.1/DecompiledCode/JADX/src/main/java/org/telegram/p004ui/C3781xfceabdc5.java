package org.telegram.p004ui;

import android.os.Bundle;
import org.telegram.p004ui.PassportActivity.PhoneConfirmationView;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_auth_resendCode;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PassportActivity$PhoneConfirmationView$1NsJYQaZadbCmSfsr4dWklizb2g */
public final /* synthetic */ class C3781xfceabdc5 implements RequestDelegate {
    private final /* synthetic */ PhoneConfirmationView f$0;
    private final /* synthetic */ Bundle f$1;
    private final /* synthetic */ TL_auth_resendCode f$2;

    public /* synthetic */ C3781xfceabdc5(PhoneConfirmationView phoneConfirmationView, Bundle bundle, TL_auth_resendCode tL_auth_resendCode) {
        this.f$0 = phoneConfirmationView;
        this.f$1 = bundle;
        this.f$2 = tL_auth_resendCode;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$resendCode$3$PassportActivity$PhoneConfirmationView(this.f$1, this.f$2, tLObject, tL_error);
    }
}
