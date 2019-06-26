package org.telegram.p004ui;

import android.os.Bundle;
import org.telegram.p004ui.CancelAccountDeletionActivity.PhoneView;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_account_sendConfirmPhoneCode;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$CancelAccountDeletionActivity$PhoneView$k7vfL3HDSHw4EqLEYwRz3mk5u4I */
public final /* synthetic */ class C3595x3004db75 implements RequestDelegate {
    private final /* synthetic */ PhoneView f$0;
    private final /* synthetic */ Bundle f$1;
    private final /* synthetic */ TL_account_sendConfirmPhoneCode f$2;

    public /* synthetic */ C3595x3004db75(PhoneView phoneView, Bundle bundle, TL_account_sendConfirmPhoneCode tL_account_sendConfirmPhoneCode) {
        this.f$0 = phoneView;
        this.f$1 = bundle;
        this.f$2 = tL_account_sendConfirmPhoneCode;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$onNextPressed$1$CancelAccountDeletionActivity$PhoneView(this.f$1, this.f$2, tLObject, tL_error);
    }
}
